package com.dpworld.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.dpworld" })
@EnableJpaRepositories("com.dpworld.persistence.repository")
@EntityScan("com.dpworld.persistence.entity")
@EnableScheduling
@EnableAsync
@EnableSwagger2
@EnableRetry
@EnableEncryptableProperties
//@EnableSchedulerLock(defaultLockAtMostFor="10m")
public class WebAppServer extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WebAppServer.class, args);
		try{  
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.67:1524:UTILITY","DPW_DTCR","jT3$2dP$41");  
			  
			//step3 create the statement object  
			//Statement stmt=con.createStatement();  
		 
			if (con != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            };
			con.close();  
			  //System.exit(0);
			}catch(Exception e){ 
				System.out.println(e);
			}  

	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WebAppServer.class);
	}

}
