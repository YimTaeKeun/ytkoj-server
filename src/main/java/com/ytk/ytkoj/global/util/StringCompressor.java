package com.ytk.ytkoj.global.util;

import com.ytk.ytkoj.global.exception.InternalServerException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 문자열 압축 기능을 제공합니다.
 * */
@Component
public class StringCompressor {

    public byte[] compress(String target){
        byte[] input = target.getBytes(StandardCharsets.UTF_8);
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.setLevel(Deflater.BEST_COMPRESSION); // 최대 압축
        deflater.finish(); // 현재까지 받은 데이터로 압축을 완전히 마무리 해달라

        // 파일이나 네트워크가 아닌 메모리에 데이터를 임시로 작성할 때 사용함
        // 바이트 배열에 데이터를 쓰기 위한 용도
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] buffer = new byte[1024]; // 1kb를 기준으로 압축 진행합니다.
            /*
            deflater.finished()는 finish() 메소드가 호출된 이후부터 deflater 내부 버퍼에 남아있는
            모든 입력 데이터가 압축되어 최종 출력용 바이트로 변환되었을 때 true를 반환한다.
            */
            while(!deflater.finished()){
                int count = deflater.deflate(buffer); // 실제 압축 진행
                bos.write(buffer, 0, count); // buffer 배열의 0번 인덱스부터 실제 들어온 갯수까지 bos에 써넣음
            }
            bos.close();
        } catch (IOException e){
            throw new InternalServerException(e.toString());
        }
        finally {
            deflater.end(); // end를 해줌으로써 java의 가비지 컬렉터가 처리하지 못한 부분을 처리
        }
        return bos.toByteArray(); // 하나의 온전한 byte[] 배열로 합쳐 반환
    }

    public String decompress(byte[] compressedByte){
        Inflater inflater = new Inflater();
        inflater.setInput(compressedByte);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            byte[] buffer = new byte[1024]; // 1kb 기준
            while(!inflater.finished()){
                int count = inflater.inflate(buffer);
                if (count == 0) {
                    if (inflater.needsInput()) {
                        throw new InternalServerException("압축 해제에 필요한 입력 데이터가 부족합니다.");
                    }

                    if (inflater.needsDictionary()) {
                        throw new InternalServerException("압축 해제에 필요한 dictionary가 없습니다.");
                    }

                    throw new InternalServerException("압축 해제를 더 이상 진행할 수 없습니다.");
                }
                bos.write(buffer, 0, count);
            }
            bos.close();
        } catch (IOException | DataFormatException e){
            throw new InternalServerException(e.toString());
        }finally {
            inflater.end();
        }
        return bos.toString(StandardCharsets.UTF_8);
    }
}
