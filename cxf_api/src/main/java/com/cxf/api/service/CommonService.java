package com.cxf.api.service;

import com.cxf.api.entity.Country;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;

@WebService(name = "CommonService",//暴露服务名
    targetNamespace = "http://service.api.cxf.com" // 命名空间,一般是接口的包名倒序
)
public interface CommonService {
    @WebMethod
    @WebResult(name = "Country",targetNamespace = "http://service.api.cxf.com")
    public Country getCountry(@WebParam(name = "name",targetNamespace = "http://service.api.cxf.com")String name);


    @WebMethod
    @WebResult(name = "Country",targetNamespace = "http://service.api.cxf.com")
    public List<Country> getAllCountry();
}
