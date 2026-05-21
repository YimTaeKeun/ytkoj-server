package com.ytk.ytkoj;

import com.ytk.ytkoj.global.util.StringCompressor;
import org.junit.jupiter.api.Test;

public class StringCompressorTest {
    private StringCompressor compressor = new StringCompressor();

    @Test
    public void compressDecompressTest(){
        String target = "Hello My Name Is Tae";
        try{
            byte[] result = compressor.compress(target);
            System.out.println("compress: " + result);
            System.out.println("decompress: " + compressor.decompress(result));
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
