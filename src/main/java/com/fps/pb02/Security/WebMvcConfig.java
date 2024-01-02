package com.fps.pb02.Security;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
//@EnableTransactionManagement
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	private final long MAX_AGE_SECS = 3600;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
				.allowedMethods(RequestMethod.HEAD.toString(), RequestMethod.OPTIONS.toString(), RequestMethod.GET.toString(),
						RequestMethod.POST.toString(), RequestMethod.PUT.toString(), RequestMethod.PATCH.toString(),
						RequestMethod.DELETE.toString())
//                .allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   return builder.build();
	}
}