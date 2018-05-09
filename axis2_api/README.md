# springBoot整合axis2发布webService接口
## 请求wsdl地址
    http://localhost:8093/services/CarService?wsdl
## 请求方法
    1.查询所有车型
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://service.api.axis2.com">
       <soapenv:Header/>
       <soapenv:Body>
          <ser:findAllCars/>
       </soapenv:Body>
    </soapenv:Envelope>
    
    2.按照名称查询车型
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://service.api.axis2.com">
       <soapenv:Header/>
       <soapenv:Body>
          <ser:findCarByName>
             <!--Optional:-->
             <ser:args0>宝马3系</ser:args0>
          </ser:findCarByName>
       </soapenv:Body>
    </soapenv:Envelope>
## xml转json工具类
   [博客地址](https://blog.csdn.net/tkggetg/article/details/47784321)

## wsdl解析例子
    博客中的附件代码已经放在项目中wsdlparse路径中
   [博客地址](http://zheng12tian.iteye.com/blog/1166505)
   
## 使用jdk自带的命令生成wsdl客户端文件
   [博客地址](https://www.imooc.com/article/14363)
    
    一般是发布了WEB SERVICE接口，我们就可以等到相应接口的WSDL文件，而WSDL文件中经常会用到一些XSD定义的类。但其实我们可以通过先定义WSDL文件，再通过一些工具自动生成WEB SERVICE的接口。
    发布WEB SERVICE接口，可以通过XFIRE,JAX WS等技术做到。
    XSD,WSDL文件，我们可能通过XML SPY，也可以通过ECLISPE来做，前者要钱的噢
    
    以前我通过com.sun.tools.xjc.XJCTask 集合ANT来基于XSD生成相应的CLASS.
    今天发现通过JDK6带的WSIMPORT命令，很方便的就基于WSDL生成了应该的CLIENT代码，以及所引入的XSD定义的类，超级方便。
    
    wsimport
    wsimport也是在JDK的bin目录下的一个exe文件（Windows版），主要功能是根据服务端发布的wsdl文件生成客户端存根及框架，负责与Web Service服务器通信，并在将其封装成实例，客户端可以直接使用，就像使用本地实例一样。对Java而言，wsimport帮助程序员生存调用webservice所需要的客户端类文件.java和.class。要提醒指出的是，wsimport可以用于非Java的服务器端，如：服务器端也许是C#编写的web service，通过wsimport则生成Java的客户端实现。
    命令参数说明： -d 生成客户端执行类的class文件的存放目录 -s 生成客户端执行类的源文件的存放目录 -p 定义生成类的包名
    
    命令范例：wsimport -d ./bin -s ./src -p org.jsoso.jws.client.ref http://localhost:8080/hello?wsdl
    
测试代码
```java
package com.gao.webservice;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
public class Test {
public static void main(String[] args) throws MalformedURLException {
            URL url = new URL("http://localhost:7777/ns?wsdl");
            QName qName = new QName("http://webservice.gao.com/","MyServiceImplService");
            MyServiceImplService ms = new MyServiceImplService(url,qName);
            MyService myService = ms.getMyServiceImplPort();
            System.out.println(myService.add(22, 12345));
            System.out.println(myService.minus(5454, 54));
    }
}

```

## 常用webservice
    天气预报Web服务，数据来源于中国气象局
    Endpoint :http://www.webxml.com.cn/WebServices/WeatherWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl
    
    IP地址来源搜索 WEB 服务（是目前最完整的IP地址数据）
    Endpoint :http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl
    
    随机英文、数字和中文简体字 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/RandomFontsWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/RandomFontsWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/RandomFontsWebService.asmx?wsdl
    
    中国邮政编码 <-> 地址信息双向查询/搜索 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/ChinaZipSearchWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/ChinaZipSearchWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ChinaZipSearchWebService.asmx?wsdl
    
    验证码图片 WEB 服务 支持中文、字母、数字 图像和多媒体
    Endpoint :http://www.webxml.com.cn/WebServices/ValidateCodeWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/ValidateCodeWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ValidateCodeWebService.asmx?wsdl
    
    Email 电子邮件地址验证 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/ValidateEmailWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/ValidateEmailWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ValidateEmailWebService.asmx?wsdl
    
    中文简体字 <->繁体字转换 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/TraditionalSimplifiedWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/TraditionalSimplifiedWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/TraditionalSimplifiedWebService.asmx?wsdl
    
    中文 <-> 英文双向翻译 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/TranslatorWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/TranslatorWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/TranslatorWebService.asmx?wsdl
    
    火车时刻表 WEB 服务 （第六次提速最新列车时刻表）
    Endpoint :http://www.webxml.com.cn/WebServices/TrainTimeWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/TrainTimeWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/TrainTimeWebService.asmx?wsdl
    
    中国股票行情数据 WEB 服务（支持深圳和上海股市的基金、债券和股票）
    Endpoint :http://www.webxml.com.cn/WebServices/ChinaStockWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/ChinaStockWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ChinaStockWebService.asmx?wsdl
    
    即时外汇汇率数据 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/ExchangeRateWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/ExchangeRateWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ExchangeRateWebService.asmx?wsdl
    
    腾讯QQ在线状态 WEB 服务
    Endpoint :http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx
    Disco :http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/webservices/qqOnlineWebService.asmx?wsdl
    
    中国电视节目预告（电视节目表） WEB 服务
    Endpoint :http://www.webxml.com.cn/webservices/ChinaTVprogramWebService.asmx
    Disco :http://www.webxml.com.cn/webservices/ChinaTVprogramWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/webservices/ChinaTVprogramWebService.asmx?wsdl
    
    外汇-人民币即时报价 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/ForexRmbRateWebService.asmx
    Disco :http://www.webxml.com.cn/WebServices/ForexRmbRateWebService.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ForexRmbRateWebService.asmx?wsdl
    
    中国股票行情分时走势预览缩略图 WEB 服务
    Endpoint : http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx
    Disco : http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?disco
    WSDL : http://www.webxml.com.cn/webservices/ChinaStockSmallImageWS.asmx?wsdl
    
    国内飞机航班时刻表 WEB 服务
    Endpoint :http://www.webxml.com.cn/webservices/DomesticAirline.asmx
    Disco : http://www.webxml.com.cn/webservices/DomesticAirline.asmx?disco
    WSDL : http://www.webxml.com.cn/webservices/DomesticAirline.asmx?wsdl
    
    中国开放式基金数据 WEB 服务
    Endpoint :http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx
    Disco : http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/ChinaOpenFundWS.asmx?wsdl
    
    股票行情数据 WEB 服务（支持香港、深圳、上海基金、债券和股票；支持多股票同时查询）
    Endpoint :http://www.webxml.com.cn/WebServices/StockInfoWS.asmx
    Disco :http://www.webxml.com.cn/WebServices/StockInfoWS.asmx?disco
    WSDL :http://www.webxml.com.cn/WebServices/StockInfoWS.asmx?wsdl

## XSD相关
[博客地址](http://www.cnblogs.com/newsouls/archive/2011/10/28/2227765.html)