package com.qy.es;

import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EsMapperScan("com.qy.es.mapper")
@SpringBootApplication
public class EasyEsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyEsApplication.class, args);
	}

}
