package com.ytk.ytkoj;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class YtkojApplication {

	// DB에 들어가는 createdAt 값으로 한국 표준시 적용
	@PostConstruct
	public void setTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		Locale.setDefault(Locale.KOREA);
	}

	public static void main(String[] args) {
		SpringApplication.run(YtkojApplication.class, args);
	}

}
