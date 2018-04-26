package com.cxf.api;

import com.cxf.api.entity.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CxfApiApplicationTests {

    private Country getCountry(String name){
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

	@Test
	public void contextLoads() {
        Country country = getCountry("French");
        System.out.println(country.getCapital());
	}

}
