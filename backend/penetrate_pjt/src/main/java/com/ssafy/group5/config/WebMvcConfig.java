package com.ssafy.group5.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.group5.interceptor.LoginInterceptor;

@Configuration
@EnableAspectJAutoProxy	//aop autoproxy 설정
@MapperScan(basePackages = {"com.ssafy.group5.model.mapper"})	//매퍼 인터페이스 스캔
public class WebMvcConfig implements WebMvcConfigurer{

	//인터셉터 등록
	@Autowired
	LoginInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//해당 요청 인터셉트
		registry.addInterceptor(interceptor).addPathPatterns("/**/board/**");
		registry.addInterceptor(interceptor).addPathPatterns("/**/auth");
		registry.addInterceptor(interceptor).addPathPatterns("/**/auth/updatepw");
		registry.addInterceptor(interceptor).addPathPatterns("/**/rate/**");
		registry.addInterceptor(interceptor).addPathPatterns("/**/myplace/**");
		
		//해당 요청을 제외한 나머지 요청들 인터셉트
//		registry.addInterceptor(interceptor).excludePathPatterns("/**/auth/**");
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*").allowedHeaders("*");
	}
}
