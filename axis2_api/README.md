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