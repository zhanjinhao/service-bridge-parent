## 关于

### 名词解释

- 单体应用（monomer application）：传统的应用。
- 微服务系统（microservice system）：一个微服务系统由三个部分组成，提供者、消费者、注册中心。
- 提供者（provider）：此提供者不是某个微服务系统的提供者，而指的是图上右侧的单体应用或微服务系统。
- 消费者（consumer）：此提供者不是某个微服务系统的消费者，而指的是图上左侧的代理端或微服务系统。
- 服务集合（service set）：一个jar包内包含多个接口。一个接口是一个服务，一个jar包是一个服务集合。
- 拉取服务驱动（pull service driver）：用于从提供者的系统中加载服务。
- 暴露服务驱动（expose service driver）：用于将服务暴露给指定的微服务系统。
- pull配置文件（pull config properties）：保存提供者的信息，如IP和端口等。存在一个配置文件中。
- expose配置文件（expose config properties）：保存消费者的信息，如IP和端口等。存在一个配置文件中。
- pull注册中心（pull registry）：系统加载pull配置文件到内存中后对应的对象。
- expose注册中心（expose registry）：系统加载expose配置文件到内存中后对应的对象。



### 适用场景

系统是一种逻辑模型，支持热部署的编程语言都具有实现此模型的能力。

相比于gRPC这种跨语言的框架，此系统基本不能适用于多种编程语言的环境。其只适合用于在同种编程语言内整合已存在的系统，如Java语言内著名的开发栈，Servlet、SSH、SSM、Dubbo、SofaRPC、SpringCloud之间可以进行整合。相比于gRPC等跨语言的框架，它的优势在于不需要修改已存在的服务。

开发此系统的目的是为了解决如下应用场景的问题。公司已存在一个由SSM开发的登录系统，随着业务的扩充，新的业务使用了分布式系统，但是我不想重构登录系统，便可以搭一个中间系统将登录系统作为一个服务注册到分布式系统中去。同时如果有一个已存在的日志系统，也可以作为一个服务注册到分布式系统去。也就是说此系统起了一个集线器的功能。

同样的，如果您有一个分布式系统，假设旧系统使用的是SpringBoot 1.x，新系统升级到SpringBoot 2.x而不担心旧的代码升级到SpringBoot 1.x，可以搭建此系统作为转化系统将旧的系统作为多个服务注册到新系统中去。

对于跨语言的业务需求，主要需要解决的是消息传输协议和实体类序列化这两个问题。如果借助gRPC这样的框架，那么剩下的需要解决便是如何传输实体类序列化这个问题。此问题不在此版本中解决。也就是此版本只能做到同种编程语言内的服务整合。

如果非要支持多种编程语言，可以使用暴露出来的HTTP接口。但是**强烈不建议**。



## 系统图示

### 构件图

**单节点构件图**

<div align="center"><img width="100%" src="http://blogfileqiniu.isjinhao.site/f0dd3823-0105-462e-aa42-a6ef243a5163" /></div>

**节点运行构件图**

<div align="center"><img width="100%" src="http://blogfileqiniu.isjinhao.site/d4f8f215-48e4-45f8-b388-f1d7d90126cc" /></div>

**集群运行构件图**

<div align="center"><img width="100%" src="http://blogfileqiniu.isjinhao.site/2494689c-a6fa-41e6-bf33-5ef46cb49937" /></div>



### 分层图

**admin操作**

<div align="center"><img width="100%" src="http://blogfileqiniu.isjinhao.site/e48682fc-e75e-4867-94df-0b7fb7493ffd" /></div>

**consumer操作**

<div align="center"><img width="100%" src="http://blogfileqiniu.isjinhao.site/977344b6-3316-4e8c-95f0-ad98d6ec1893" /></div>



### 用例图

**系统**

<div align="center"><img width="60%" src="http://blogfileqiniu.isjinhao.site/434379b8-5ac8-4614-9a97-b2e7d6aa9d80" /></div>

**消费者**

<div align="center"><img width="35%" src="http://blogfileqiniu.isjinhao.site/750606c9-c847-4089-93cd-3e08b6e44166" /></div>

**管理员**

<div align="center"><img width="80%" src="http://blogfileqiniu.isjinhao.site/5fb85acc-e582-4ce8-981e-80c3e20ecfd6" /></div>