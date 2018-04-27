package com.axis2.api;

import com.axis2.api.entity.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Axis2ApiApplicationTests {

	private Car getCarByName(String name){
		Map<String,Car> map = new HashMap<>();

		map.put("哈弗H6",new Car("哈弗H6","指导价：8.88 - 14.68万","2018款 红标 运动版 1.5T 手动 两驱 精英型 前轮驱动 6挡 手动"));
		map.put("帕萨特",new Car("帕萨特","指导价：15.29 - 30.39万","2017款 280TSI DSG尊雅版 前轮驱动 7挡 双离合"));
		map.put("奥迪A3",new Car("奥迪A3","指导价：15.23 - 20.90万","2018款 30周年 Sportback 35TFSI 进取型 前轮驱动 7挡 双离合"));
		map.put("昂科拉ENCORE",new Car("昂科拉ENCORE","指导价：10.99 - 16.30万","2018款 18T 手动 两驱 都市进取型 前轮驱动 6挡 手动"));
		map.put("宝马3系",new Car("宝马3系","指导价：28.30 - 59.88万","2018款 318i 后轮驱动 8挡 手自一体"));

		return map.get(name);
	}

	@Test
	public void contextLoads() {
		Car car = getCarByName("宝马3系");

	}

}
