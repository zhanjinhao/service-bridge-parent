## 使用微服务构建服务软件

作者：

1. 王丰坚：中国台湾新竹国立交通大学计算机科学系。fjwang@cs.nctu.edu.tw
2. Faisal Fahmi：中国台湾新竹国立交通大学电子工程和计算机科学国际交流项目。



### 摘要

微服务体系结构是面向服务的体系结构（SOA）的变体，它允许将服务分解为多个较小的但独立并发运行的单元，从而使应用程序的性能和可维护性都可以得到很大的提高。 在本文中，我们介绍了一种使用面向对象的规范来构建具有分层和分布式微服务的软件的方法。 所提出的方法可以增加应用程序系统的并发性，从而提高性能，并可以提高微服务的可重用性。 关键字²微服务，系统方法，面向对象的规范。

关键字：微服务、系统方法、面向对象的规范



### 介绍

SOA是最流行的软件体系结构之一。 SOA中的服务通常在单个代码上实现为单片应用程序，并作为单个单元进行部署。因此，由于服务的可扩展性和可维护性不能被孤立地执行并且具有大的粒度，因此被抑制，例如，单个服务的改变会完全影响系统。与SOA相比，微服务架构允许将服务划分为多个较小的规模，但可以同时运行。它使微服务能够以较小的粒度独立地缩放和维护，从而可以大大提高系统的性能和可维护性。

在本文中，我们提出了一种使用面向对象的需求规范[1]构建基于微服务的软件的方法，该方法由于其需求规范与面向对象的完整性和一致性而被采用。使用该方法，将系统递归分解为几个子系统，然后相应地定义微服务。在与现有方法进行比较之后，所提出的方法可以增加系统的并发性，从而可以提高面向对象的微服务的性能并提高其可重用性。



### 微服务和架构特征

微服务可以定义为受上下文限制的可编程单元，并且可以通过消息传递相互通信。可以在各种平台[10、16]，例如物理机，虚拟机或不同级别的容器中独立管理，部署和扩展不同的微服务。因此，为了提高性能和可维护性，微服务体系结构至少应具有以下特征：受上下文限制，尺寸较小和独立性。例如，较小的特征带来了可维护性的主要好处，例如，可以轻松修改它，而独立的微服务则允许对其进行缩放或修改而不会影响其他功能。

分布式应用程序可以由分布式微服务组成，每个微服务都运行自己的进程。例如，考虑一个在线产品销售系统。它具有创建订单，付款等各种功能。它们每个都可以开发为微服务，并且可以独立部署不同的代码以提高速度。



### 构建一个机遇微服务的软件

在本节中，介绍了我们称为MOOS的方法。 使用该方法，系统将系统递归分解为几个子系统，其中一个子系统提供接口，并且其架构风格自然类似于微服务。 因此，MOOS的详细信息如下所示：

#### 步骤1:

根据基于对象的分析规范描述每个子系统。面向对象的需求规范包括功能（例如用例）和非功能（例如性能）。通过在子系统的界面下封装一组相关对象（例如，在相同的用例或主题中）来描述子系统。描述子系统的典型考虑因素包括：

1. 只要接口保持不变，就可以独立开发子系统； 
2. 可以在一组分布式计算节点之间独立部署子系统；
3. 可以是一个子系统。独立更改，而不会破坏系统的其他部分。

#### 步骤2

将子系统映射到平台。每个子系统都映射到一个平台，该平台可能包含多个子系统。平台配置包括硬件（例如CPU和内存）和软件（例如Jolie，JSP等）。该映射用于识别子系统之间的并发并解决其非功能性需求，例如性能和可靠性。映射注意事项可能包括：

1. 如果两个子系统可以同时运行，则每个子系统都可以映射到不同的平台。
2. 如果子系统有特殊的性能要求，例如，它需要支持1000个并发用户，则可以将其映射到一个不同的平台。
3. 如果子系统被赋予特殊的可靠性要求（例如，它需要应用断路器来访问外部服务），则可以将其映射到不同的平台。
4. 如果有两个相关的子系统，则映射注意事项可以包括：
   1. 在紧密的关系中，例如，它们经常相互通信，如果它们之一或两者未获得特殊的性能或可靠性要求，则可以将它们映射到同一平台以最小化通信成本。否则，它们每个都可以映射到不同的平台。 
   2. 在松散关系中，它们每个都可以映射到不同的平台。

#### 步骤3

确定全局资源并相应地创建其访问控制机制。为了标识全局资源，首先标识必须持久的对象。通过步骤1中采用的需求规范，可以识别不同用例中用户之间共享的对象。如果将这些对象包含在不同的分布式子系统中，则为简化数据一致性的维护，可以将它们作为访问控制的共享数据分成一个新的子系统。将相同的规则应用于共享功能，以提高可重用性，这些功能可以放入新的子系统中（并视为实用程序功能）。

#### 步骤4

描述每个子系统的并发控制。在分布式系统中，子系统需要处理外部请求并及时响应。由于可以同时接受外部请求，因此采用Jolie来减少为每个子系统应用并发控制的复杂性。 Jolie可以同时或顺序执行工作，例如，Jolie通过支持多线程机制执行并发工作，该机制允许每个子系统通过为该点接受的每个请求创建一个新线程来同时服务多个请求。

#### 步骤5

基于子系统定义和重用微服务。为了降低派生微服务的管理复杂度，提出了一种分层微服务模型（LMM）。LMM包括域，共享和实用程序微服务层，其中每个层分别包含与应用程序域，共享数据和实用程序功能相关的微服务，如图1所示。图1中的箭头指示允许的访问之间的微服务。不同层中的微服务，同一层中的微服务可以相互通信。在LMM内部，采用了[4，14]中的策略来定义微服务的五种类型，称为核心微服务，支持微服务，通用微服务，共享微服务和实用微服务。因此，详细的策略包括：

1. 在应用程序域中按以下步骤依次定义核心，支持和通用微服务：
   1. x收集其相应子系统在可靠性和性能方面具有最高优先级的微服务，并将它们定义为核心微服务。 
   2. x收集将通过使用未定义为核心的开源实现的微服务，并将它们定义为通用微服务。
   3. x应用程序域中的其余微服务定义为支持微服务。
2. 基于分别在步骤3中派生的共享数据和实用程序功能，定义共享微服务和实用程序微服务。例如，在一个在线产品销售系统中，核心可以是立即分析为每个客户推荐的产品的核心，支持可以是管理库存或销售的核心，而通用名称可以是管理员工或工资单的核心。

<div align="center"><img width="60%" src="http://blogfileqiniu.isjinhao.site/8f22aa4d-1a48-4ecd-84fb-5e42c5c5f707" /></div>
#### 步骤6

检查并更新系统设计。 在此步骤中，我们检查所解决的每个需求和设计问题是否包含任何矛盾。 如果发现任何错误，请返回相应的步骤，执行必要的更新，然后继续。



### 与现有方法的比较

分层体系结构可以降低派生微服务的管理复杂性。从现有方法[4、6、7、10、13、16]中，当前存在一种具有称为Erl‘s的结构能力的方法。该方法包括四个层，分别是任务服务，微服务，实体服务和实用程序服务层。使用Erl's方法），微服务用于实现需要特殊要求的服务，例如运行时，部署等。

在根据[5]改编的同一研究案例中应用Erl’s后，发现Erl‘s的部署面临瓶颈，因为除微服务之外的所有服务可以部署到单个平台上。此外，被Erl's方法构建的微服务仅作特殊用途，称为不可知论者，因此会降低可重用性。仅由微服务组成的MOOS可以增加系统的并发性，并减少出现瓶颈的可能性；通过为开源实现通用微服务，可以提高微服务的可重用性。



### 结论

在本文中，我们介绍了MOOS，这是一种使用面向对象的规范来构建具有分层微服务的软件的系统方法。 与现有的构建方法相比，MOOS可以增加应用程序系统的并发性，从而提高微服务的性能和可重用性。



### 参考

1. B. Brueggle and A.H. Dutoit, Object-Oriented Software Enginering: Using UML, Pattrens, and Java, Prentice Hall, 2004. 
2. N. Dragoni, et al., "Microservices: How to Make Your Application Scale," Perspectives of System Informatics, vol. 10742, pp. 95-104, 2017.
3. N. Dragoni, et al., "Microservices: Yesterday, Today, and Tomorrow," Present and Ulterior Software Engineering, pp. 195-216, 2017.
4. T. Erl, Service-Oriented Architecture: Analysis and Design for Services and Microservices, Prentice Hall, 2016. 
5. K. Fakhroutdinov. "Online Shopping: UML Use Case Diagram Example," https://www.uml-diagrams.org/examples/online-shopping-use-casediagram-example.html
6. G. Granchelli, et al. "Towards Recovering the Software Architecture of Microservice-Based System," Proc. IEEE ICSAW, pp. 46-53, 2017. 
7. C. Guidi, et al., "Microservices: a language-based approach," Present and Ulterior Software Engineering, pp. 217-225, 2017. 
8. K. Huang and B. Shen, "Service deployment strategies for efficient execution of composite SaaS applications on cloud platform," Journal of Systems and Software, vol. 107, pp. 127-141, 2015. 
9. Jolie. "The first language for Microservices," http://www.jolie-lang.org/ 
10. S. Newman, Building Microservice: Designing Fine-Grained Systems, O'Reilly Media, 2015. 
11. M. Papathomas, Concurrency in Object-Oriented Programming Languages, in Object-Oriented Software Composition, Prentice Hall, pp. 31-68, 1995.
12. M. Richardson. "Guideline: Design Subsystem," http://www.michaelrichardson.com/processes/rup_for_sqa/core.base_rup/guidances/guideline s/design_subsystem_B26FD609.html 
13. L. Sun, Y. Li, and R.A. Memon, "An open IoT framework based on microservices architecture," China Communications, vol. 14, pp. 154-162, 2017. 
14. V. Vernon, Implementing Domain-Driven Design, Pearson, 2013. 
15. Wikipedia. "Monolithic Application," https://en.wikipedia.org/wiki/ Monolithic_application [16] E. Wolff, Microservices: Flexible Software Architecture, Addison-Wesley, 2016.