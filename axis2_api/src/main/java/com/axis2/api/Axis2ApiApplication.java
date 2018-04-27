package com.axis2.api;

import com.axis2.api.config.Axis2ServiceRegister;
import org.apache.axis2.extensions.spring.receivers.ApplicationContextHolder;
import org.apache.axis2.transport.http.AxisServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


@SpringBootApplication
public class Axis2ApiApplication extends SpringBootServletInitializer {

    @Autowired
    private Axis2ServiceRegister register;

	public static void main(String[] args) {
		SpringApplication.run(Axis2ApiApplication.class, args);
	}

    @Bean
    public ServletRegistrationBean servletRegistration() throws IOException {

        //load the service
        ServletRegistrationBean bean=new ServletRegistrationBean(new AxisServlet(), "/services/*");
        bean.setLoadOnStartup(1);
        bean.addInitParameter("axis2.repository.path",register.getRepoPath());
//        bean.addInitParameter("axis2.repository.path",new ClassPathResource("services/carService/META-INF/services.xml").getURL().toString());
        bean.addInitParameter("axis2.xml.url", new ClassPathResource("axis2.xml").getURL().toString());

        return bean;
    }

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Axis2ApiApplication.class);
	}

	@Bean
    public ApplicationContextHolder getApplicationContext(){
		ApplicationContextHolder applicationContextHolder=new ApplicationContextHolder();
		return applicationContextHolder;
	}

}
