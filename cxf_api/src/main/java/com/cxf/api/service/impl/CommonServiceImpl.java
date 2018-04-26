package com.cxf.api.service.impl;

import com.cxf.api.entity.Country;
import com.cxf.api.service.CommonService;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService(
        serviceName = "commonService",//与接口中的名字一样
        targetNamespace = "http://service.api.cxf.com",//一般是包名的倒序
        endpointInterface = "com.cxf.api.service.CommonService"//接口地址
)
@Service
public class CommonServiceImpl implements CommonService {

    @Override
    public Country getCountry(String name) {
        Map<String,Country> map = new HashMap<>();

        //美国
        map.put("USA",new Country("USA","3.231亿（2016年）","华盛顿"));
        //中国
        map.put("China",new Country("China","13.83亿人(2016年)","北京"));
        //法国
        map.put("French",new Country("French","6689.6万（2016年）","巴黎"));
        //英国
        map.put("UK",new Country("UK","6563.7万（2016年）","伦敦"));

        return map.get(name);
    }

    @Override
    public List<Country> getAllCountry() {
        List<Country> list = new ArrayList<>();
        list.add(new Country("USA","3.231亿（2016年）","华盛顿"));
        list.add(new Country("China","13.83亿人(2016年)","北京"));
        list.add(new Country("French","6689.6万（2016年）","巴黎"));
        list.add(new Country("UK","6563.7万（2016年）","伦敦"));
        return list;
    }
}
