# Spring Integration
## 使用的技术
    1.Spring Integration
    2.thymeleaf
    3.Spring Boot
    4.webService
## 一个Spring Integration入门的文章
[文章地址](http://www.importnew.com/16538.html)

## 企业消息总线学习资料

[EIP——Spring Integration4.3使用篇](https://blog.csdn.net/xiayutai1/article/details/53302652?locationNum=4&fps=1)

[Spring Integration环境搭建](http://joinandjoin.iteye.com/blog/2004028)

[SpringWS环境搭建](https://www.cnblogs.com/hippo0918/p/3662587.html)

### 简书--马国标
1.[Spring integration 基本概念](https://www.jianshu.com/p/bf1643539f99)

2.[Spring Integration Message](https://www.jianshu.com/p/8d2481cfb20c)

3.[Spring Integration Channel](https://www.jianshu.com/p/b11d37866986)

[一个Spring Integration示例](http://www.importnew.com/16538.html)

[JMS学习一(JMS介绍)](https://blog.csdn.net/qh_java/article/details/55224259)

[Spring Integration在项目中的运用](http://binginx.com/2015/07/20/Spring-Integration-%E5%9C%A8%E9%A1%B9%E7%9B%AE%E4%B8%AD%E7%9A%84%E8%BF%90%E7%94%A8/)

[业集成框架spring integration体验](http://zhyi-12.iteye.com/blog/1880739)

```
企业服务总线ESB(Enterprise Service Bus)

总线（Bus）：计算机各个部件之间传输信息的公共通路叫总线

message-producers 消息生产者  message-consumers 消息消费者
```

## message channel 通道 
	1.point to point 点对点通道       ：  最多只有一个消费者可以接收消息
	2.publish-subscribe 发布-订阅通道 ：  所有订阅者都可以接收到消息
## pollable channel 轮询通道
	具有在一个队列中缓冲消息的能力

## 消息端点：
	1.Transformer 转化器
	可以将payload(消息负载)转化成其他格式，比如json转xml、xml转json
	2.Filter 过滤器
	它通常和发布订阅通道一起使用，最终决定是否将消息发送到消息输出通道
	3.Router路由
	它决定消息由哪些通道接收
	4.Splitter分解器
	它从输入通道接收消息，把消息分解成多个消息，最后发送到相应的输出通道
	5.Aggregator聚合器
	它负责把接收多个消息，然后合成一个消息
	6.Service Activator 服务催化剂
	它是用来调用服务实例的。
	前提条件：输入消息通道必须是配置好的，并且服务的方法调用后必须返回一个值，然后提供一个输出消息的通道
	7.channel Adapter通道适配器
	它可以将其他系统和消息通道进行连接，可以接入或者接出
## 消息通道 Message channel
	public interface MessageChannel {
	    boolean send(Message message);
	    boolean send(Message message, long timeout);
	}
