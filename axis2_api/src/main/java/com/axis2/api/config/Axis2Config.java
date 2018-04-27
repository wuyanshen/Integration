package com.axis2.api.config;

import org.apache.axis2.transport.http.AxisServlet;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.axis2.deployment.WarBasedAxisConfigurator.PARAM_AXIS2_REPOSITORY_PATH;

//@Configuration
public class Axis2Config {

//    private File repo=new File(System.getProperty("java.io.tmpdir"),"axis2repo");

    @Bean
    public ServletRegistrationBean axisServlet(){

        String repositoryPath = String.format("/", getClass().getResource("/").getPath());

        ServletRegistrationBean servlet = new ServletRegistrationBean();
        servlet.setServlet(new AxisServlet());
        servlet.addUrlMappings("/services/*");
        servlet.setName("AxisServlet");
        servlet.setLoadOnStartup(1);
        servlet.addInitParameter(PARAM_AXIS2_REPOSITORY_PATH, repositoryPath);
        return servlet;
    }

   /* @PostConstruct
    public void init(){
        loadServices();
    }

    private void loadServices() {
        try {
            registerServiceXml(new ClassPathResource("carService/services.xml"),"carService");
        } catch (IOException e) {
            System.out.println("error when load service: {}"+e);
        }
    }

    public void registerServiceXml(Resource resource,String serviceName) throws IOException{

        FileOutputStream fileOutputStream=null;
        try{
            File serviceDir=new File(repo,"services/"+serviceName+"/META-INF");
            serviceDir.mkdirs();
            fileOutputStream=new FileOutputStream(new File(serviceDir, "services.xml"));
            IOUtils.copy(resource.getInputStream(),fileOutputStream);
        }finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }*/
}
