# Spring Boot整合CXF实现WebService
## 请求WSDL地址
    http://localhost:8092/services/CommonService?wsdl
## 请求的soap xml报文
    1.请求所有国家信息：
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://service.api.cxf.com">
       <soapenv:Header/>
       <soapenv:Body>
          <ser:getAllCountry/>
       </soapenv:Body>
    </soapenv:Envelope>
    
    2.请求具体某一的国家信息：
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://service.api.cxf.com">
       <soapenv:Header/>
       <soapenv:Body>
          <ser:getCountry>
           <name>China</name>
          </ser:getCountry>
       </soapenv:Body>
    </soapenv:Envelope>