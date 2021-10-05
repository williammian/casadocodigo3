package br.com.casadocodigo3.loja.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfiguration {

	@Autowired
	private Environment environment;

	private String access_key;
	private String secret_key;
	private String region="us-east-1";
	
	//ACCESS_KEY=AKjAXKDXFOYMGI5HJQKT
	//SECRET_KEY=sJ402vtXe5uwwRG590wcvUaYR7ym6Zt8NfiabKs/

	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> (env.equalsIgnoreCase("dev")))) {
			access_key = "";
			secret_key = "";
		} else {
			//parametros informados na configuração do beanstalk / software / propriedades de ambiente (tomcat passava essa informacao)
			//access_key = System.getProperty("ACCESS_KEY");
			//secret_key = System.getProperty("SECRET_KEY");
			
			//parametros informados na configuração propriedades de ambiente (container do docker)
			access_key = System.getenv("ACCESS_KEY");
			secret_key = System.getenv("SECRET_KEY");
		}
		return new BasicAWSCredentials(access_key, secret_key);
	}

	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials())).build();
	}

}
