## 配置说明
### 配置文件位置
项目运行目录下的文件夹 config_directory

### jar包位置
项目运行目录下的文件夹 jar_directory

### properties配置文件
- 以pull-registry-开头表示配置pull-registry
- 以expose-registry-开头表示配置expose-registry

## 运行流程
1. 启动的时候加载config文件，或启动后手动加载（RegistryConfigs.loadConfig(String fileName)）。每个config文件对应一个Registry。
2. binding-service?registry-id=""&toolId="" ： 区分pull-service和expose-service
2. create-service-container/registryId?service-name=""&version=""：会在对应的注册中心创建一个空的容器（new一个loader对象，service为null）
3. load-jar/registryId/service-name=""&version=""&jar-name=""：将对应的jar加载进入loader
    1. 记录jar的使用情况
4. load-service/registryId/service-name=""&version=""&class-name=""
5. expose-service/pullRegistryId/exposeRegistryId/service-name=""&version=""


## 返回信息
- Result(400, "exception");
- Result(200, "success");
- Result(500, "error");
- Result(600, "asynchronous operation success, please check your log for more information");

- Result(401, "registryId 不存在！");
- Result(402, "pullToolId不存在");
- Result(401, "registryId 不存在！");
- Result(403, "exposeToolId不存在");
- Result(405, serviceName + ":" + version + " has existed");  createServiceContainer时
- Result(406, "urlClassLoader is null");
- Result(407, "file does not exist");
- Result(408, "interfaceClass does not existed");
- Result(409, "pullService failed");
- Result(410, "service is null");
- Result(411, "pullServiceTool is null");
- Result(412, "exposeServiceTool is null");
- Result(413, "exposeService failed");

- Result(701, "jarExists");

## 约定
### 端口号
rpc项目使用13xxx
- sofarpcProviderZookeeper：13001
- dubboProviderZookeeper：13002

sb项目使用12xxx
- expose-sofarpc-zookeeper：12001
- expose-dubbo-zookeeper：12002


## 操作分解

注册中心的配置文件应该在启动时配置。并且各个注册中心会在启动时都创建。

### 原子操作
#### loadJar
1. 将jar包加载至PullRegistry的 Map<jarName, URLClassLoaderWrap> urlClassLoaderWrapMap


#### loadService
1. 加载服务并设置到ServiceValue中，并记录在pullRegistry的Map<ServiceKey, ServiceValue> servicesBuffer中
2. ServiceKey记录在URLClassLoaderWrap中


#### exposeService
1. 暴露服务
2. 记录ServiceKey到exposeRegistry中：Map<pullRegistryId, List<ServiceKey>> pullRegistryServiceKeysMap
3. 记录ServiceKey到pullRegistry中：Map<exposeRegistryId, List<ServiceKey>> exposeRegistryServiceKeysMap
4. 记录exposeRegistryId到ServiceValue中：List<String> exposeRegistries


#### shutdownService
1. 关闭服务
2. 从exposeRegistry中移除当前的ServiceKey：Map<pullRegistryId, List<ServiceKey>> pullRegistryServiceKeysMap
3. 从pullRegistry中移除当前的ServiceKey：Map<exposeRegistryId, List<ServiceKey>> exposeRegistryServiceKeysMap
4. 从ServiceValue中移除当前的exposeRegistryId：List<String> exposeRegistries


#### releaseService
1. 从ServiceValue中获取记录的所有的exposeRegistryId，关闭所有的已暴露的服务（shutdownService）
2. 从URLClassLoaderWrap中移除ServiceKey
3. 从Map<ServiceKey, ServiceValue> servicesBuffer中移除ServiceKey


#### releaseJar
1. 从URLClassLoaderWrap中获取记录的所有的ServiceKey，释放所有的服务（releaseService）
2. 从PullRegistry的Map<jarName, URLClassLoaderWrap> urlClassLoaderWrapMap中移除当前的jarName


### 复合操作
#### reexposeService
1. shutdownService
2. exposeService


#### updateService
1. 记录已经暴露此服务的注册中心：List<String> exposeRegistries
2. releaseService
3. loadService
4. 在1中记录的所有的注册中心上exposeService


#### updateJar
1. 获取此jar包中记录的load的ServiceKey
2. 由1中记录的ServiceKey获取所有的ServiceValue
3. releaseJar
4. loadJar
5. 对1中获得的所有的ServiceKey进行loadService
    1. 获得ServiceValue中记录的List<String> exposeRegistries
    2. 对所有的exposeRegistryId进行exposeService






