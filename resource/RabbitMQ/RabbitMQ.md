# MQ的基本概念

MQ全称Message Queue（消息队列），是在消息的传输过程中保存消息的容器。多用于分布式系统之间进行通信。MQ的发送方称为生产者，接收方称为消费者。

分布式系统通信两种方式：直接远程调用 和借助第三方完成间接通信，MQ可以充当间接通信的中间件（第三方）。

## MQ的优势与劣势

MQ的优势包括：

- **应用解耦**。一个系统可以通过远程调用直接操纵其他系统，但是这会带来两个弊端：被调用系统产生异常会影响调用系统，因此整个系统的容错性低；增加其他系统需要修改调用系统以访问该系统，因此调用系统的可维护性低。系统的耦合性越高，容错性就越低，可维护性就越低。使用MQ使得应用间解耦，提升容错性和可维护性。一个系统只需要发送消息到MQ，其他系统从MQ中取出消息并消费即可。其他系统的错误不会传播到调用系统（该错误最终会被其他系统处理）。
- **异步提速**。MQ可以将一个系统对多个系统的串行操作转换为一个系统对MQ的操作（该操作一般远快于对多个系统的串行操作），其他系统则异步从MQ中取出消息，提升用户体验和系统吞吐量（单位时间内处理请求的数目）。
- **削峰填谷**。如果一个系统的处理能力有限，MQ可以作为客户端与该系统的中间件，延缓用户的大量请求。使用了MQ之后，限制消费消息的速度，这样一来，高峰期产生的数据势必会被积压在MQ中，高峰就被“削”掉了，但是因为消息积压，在高峰期过后的一段时间内，消费消息的速度还是会维持在恒定值，直到消费完积压的消息，这就叫做“填谷”。使用MQ后，可以提高系统稳定性。

MQ的劣势包括：

- **系统可用性降低**。系统引入的外部依赖越多，系统稳定性越差。一旦MQ宕机，就会对业务造成影响。如何保证MQ的高可用？
- **系统复杂度提高**。MQ 的加入大大增加了系统的复杂度，以前系统间是同步的远程调用，现在是通过MQ进行异步调用。如何保证消息没有被重复消费？怎么处理消息丢失情况？那么保证消息传递的顺序性？
- **一致性问题**。A系统处理完业务，通过MQ给B、C、D三个系统发消息数据，如果B系统、C系统处理成功，D系统处理失败。如何保证消息数据处理的一致性？

使用MQ需要满足如下条件：

- 生产者不需要从消费者处获得反馈。引入消息队列之前的直接调用，其接口的返回值应该为空，这才让明明下层的动作还没做，上层却当成动作做完了继续往后走，即所谓异步成为了可能。
- 容许短暂的不一致性。
- 确实是用了有效果。即解耦、提速、削峰这些方面的收益，超过加入MQ，管理MQ这些成本。

## 常见的MQ产品

目前业界有很多的MQ产品，例如[RabbitMQ](https://www.rabbitmq.com/)、RocketMQ、ActiveMQ、Kafka、ZeroMQ、MetaMQ等，也有直接使用Redis充当消息队列的案例，而这些消息队列产品，各有侧重，在实际选型时，需要结合自身需求及MQ产品特征，综合考虑。

|                | RabbitMQ                                                     | ActiveMQ                                | RocketMQ                 | Kafka                                          |
| -------------- | ------------------------------------------------------------ | --------------------------------------- | ------------------------ | ---------------------------------------------- |
| 公司/社区      | Rabbit                                                       | Apache                                  | 阿里                     | Apache                                         |
| 开发语言       | Erlang                                                       | Java                                    | Java                     | Scala & Java                                   |
| 协议支持       | AMQP、XMPP、SMTP、STOMP                                      | OpenWire、STOMP、REST、XMPP、AMQP       | 自定义                   | 自定义协议，社区封装了http协议支持             |
| 客户端支持语言 | 官方支持Erlang、Java、Ruby等,社区产出多种API，几乎支持所有语言 | Java、C、C++、Python、PHP、Perl、.net等 | Java、C++（不成熟）      | 官方支持Java,社区产出多种API，如PHP，Python等  |
| 单机吞吐量     | 万级（其次）                                                 | 万级（最差）                            | 十万级（最好）           | 十万级（次之）                                 |
| 消息延迟       | 微妙级                                                       | 毫秒级                                  | 毫秒级                   | 毫秒以内                                       |
| 功能特性       | 并发能力强，性能极其好，延时低，社区活跃，管理界面丰富       | 老牌产品，成熟度高，文档较多            | MQ功能比较完备，扩展性佳 | 只支持主要的MQ功能，毕竟是为大数据领域准备的。 |

## RabbitMQ简介

AMQP，即Advanced Message Queuing Protocol（高级消息队列协议），是一个网络协议，是应用层协议的一个开放标准，为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品、不同的开发语言等条件的限制。2006年，AMQP规范发布。类比HTTP。

AMQP规定了如下角色：

- 消息队列中间件：包括交换机（Exchange）与队列（Queue）。交换机用来分发消息到不同队列，队列则存储消息。分发就是路由（Routes）的过程。
- 生产者（Publisher）发送（Publish）到交换机。
- 消费者（Consumer）从队列消费（Consume）消息。

2007年，Rabbit技术公司基于AMQP标准开发的RabbitMQ 1.0发布。RabbitMQ 采用Erlang语言开发。

Erlang语言由Ericson设计，专门为开发高并发和分布式系统的一种语言，在电信领域使用广泛。

RabbitMQ 基础架构如下图：![RabbitMQ基础架构](资源\RabbitMQ基础架构.png)

RabbitMQ 中的相关概念：

- Broker：接收和分发消息的应用，RabbitMQ Server就是Message Broker。
- Virtual host：出于多租户和安全因素设计的，把AMQP的基本组件划分到一个虚拟的分组中，类似于网络中的namespace概念。当多个不同的用户使用同一个RabbitMQ server提供的服务时，可以划分出多个vhost，每个用户在自己的vhost创建交换机、队列等。
- Connection：生产者、消费者与broker之间的TCP连接。
- Channel：如果每一次访问RabbitMQ都建立一个连接，在消息量大的时候建立 TCP连接的开销将是巨大的，效率也较低。Channel是在连接内部建立的逻辑连接，如果应用程序支持多线程，通常每个线程创建单独的channel进行通讯，AMQP方法包含了channel id帮助客户端和message broker识别channel，所以channel之间是完全隔离的。Channel作为轻量级的连接极大减少了操作系统建立TCP连接的开销。
- Exchange：消息到达broker的第一站，根据分发规则，匹配查询表中的routing key（位于消息的头中），分发消息到队列中去。常用的类型有：`direct` （point-to-point）、`topic`（publish-subscribe）与`fanout`（multicast）。
- Queue：消息最终被送到这里等待消费者取走。
- Binding：交换机和队列之间的虚拟连接，binding中可以包含routing key。Binding信息被保存到交换机中的查询表中，用于消息的分发依据。

## JMS

JMS即Java消息服务（Java Message Service）应用程序接口，是一个Java平台中关于面向消息中间件的API。

JMS是JavaEE规范中的一种，类比JDBC。

很多消息中间件都实现了JMS规范，例如：ActiveMQ。RabbitMQ官方没有提供JMS的实现包，但是开源社区有。

# RabbitMQ的安装与配置[^2]

下面在CentOS 7中安装RabbitMQ。参考[Installing on RPM-based Linux (RedHat Enterprise Linux, CentOS, Fedora, openSUSE)](https://www.rabbitmq.com/install-rpm.html)。

进入[erlang版本仓](https://packagecloud.io/rabbitmq/erlang)，安装对应版本的[erlang](https://packagecloud.io/rabbitmq/erlang/packages/el/7/erlang-23.3.4.11-1.el7.x86_64.rpm)：

```sh
curl -s https://packagecloud.io/install/repositories/rabbitmq/erlang/script.rpm.sh | sudo bash
```

```sh
wget --content-disposition https://packagecloud.io/rabbitmq/erlang/packages/el/7/erlang-23.3.4.11-1.el7.x86_64.rpm/download.rpm
```

```sh
sudo yum install erlang-23.3.4.11-1.el7.x86_64
```

```sh
erl  # 显示版本号，说明erlang安装成功
```

安装rabbitmq-server，注意[erlang版本与rabbitmq版本兼容问题](https://www.rabbitmq.com/which-erlang.html)。

进入[RabbitMQ版本仓](https://packagecloud.io/rabbitmq/rabbitmq-server)，安装对应版本的[RabbitMQ](https://packagecloud.io/rabbitmq/rabbitmq-server/packages/el/7/rabbitmq-server-3.9.16-1.el7.noarch.rpm)：

```sh
curl -s https://packagecloud.io/install/repositories/rabbitmq/rabbitmq-server/script.rpm.sh | sudo bash
```

```sh
wget --content-disposition https://packagecloud.io/rabbitmq/rabbitmq-server/packages/el/7/rabbitmq-server-3.9.16-1.el7.noarch.rpm/download.rpm
```

```sh
sudo yum install rabbitmq-server-3.9.16-1.el7.noarch
```

启动RabbitMQ的插件管理：

```sh
rabbitmq-plugins enable rabbitmq_management  # 开启管理界面
```

 启动RabbitMQ服务：

```sh
service rabbitmq-server start  # 启动服务，或者：systemctl start rabbitmq-server
service rabbitmq-server stop  # 停止服务
service rabbitmq-server restart  # 重启服务
```

创建admin用户并设置密码：

```sh
rabbitmqctl add_user admin admin
```

添加admin用户为administrator角色：

```sh
rabbitmqctl set_user_tags admin administrator
```

设置admin用户的权限，指定允许访问的vhost以及write/read：

```sh
rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"
```

查看vhost（/）允许哪些用户访问：

```sh
rabbitmqctl list_permissions -p /
```

查看用户列表：

```sh
rabbitmqctl list_users
```

访问CentOS 7的15672端口，登录，进入RabbitMQ管理界面，创建一个名为/itcast的虚拟机。

# RabbitMQ的工作模式

## 简单模式

简单模式（["Hello World!"](https://www.rabbitmq.com/tutorials/tutorial-one-python.html)）下，生产者（P）发送消息到消息队列，消费者（C）从中取出消息。

![简单模式](资源\简单模式.png)

创建两个模块，分别作为生产者与消费者。

引入依赖与插件：

```xml
<!-- RabbitMQ Java客户端-->
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.15.0</version>
</dependency>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

生产者代码：

```java
package com.example.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置参数
        factory.setHost("192.168.227.128");  // 默认值为localhost
        factory.setPort(5672);  // 默认值为5672
        factory.setVirtualHost("/itcast");  // 默认值为/
        factory.setUsername("admin");  // 默认值为guest
        factory.setPassword("admin");  // 默认值为guest
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建Channel
        Channel channel = connection.createChannel();
        // 创建Queue
        channel.queueDeclare("hello_world" /* 队列名称，不存在会自动创建 */,
                true /* 是否持久化，即当RabbitMQ重启后是否还在 */,
                false /* 是否独占，即只有一个消费者监听该队列；当Connection关闭后，是否删除队列 */,
                false /* 没有消费者时是否自动删除 */ ,
                null /* 其他参数 */
        );
        // 发送消息
        String body = "hello rabbitmq";
        channel.basicPublish(
                ""  /* 交换机名称，简单模式下交换机会使用默认的，指定空字符串即可 */,
                "hello_world" /* 路由名称，简单模式下必须与队列名称对应 */,
                null /* 配置信息 */,
                body.getBytes() /* 发送的消息 */
        );
        // 释放资源，注释掉则会一直运行，且管理界面可以看到对应的channel与连接
        channel.close();
        connection.close();
    }

}
```

消费者代码类似：

```java
package com.example.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置参数
        factory.setHost("192.168.227.128");  // 默认值为localhost
        factory.setPort(5672);  // 默认值为5672
        factory.setVirtualHost("/itcast");  // 默认值为/
        factory.setUsername("admin");  // 默认值为guest
        factory.setPassword("admin");  // 默认值为guest
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建Channel
        Channel channel = connection.createChannel();
        // 创建队列（可以省略，因为生产者已经创建了）
        channel.queueDeclare("hello_world" /* 队列名称，不存在会自动创建 */,
                true /* 是否持久化，即当RabbitMQ重启后是否还在 */,
                false /* 是否独占，即只有一个消费者监听该队列；当Connection关闭后，是否删除队列 */,
                false /* 没有消费者时是否自动删除 */ ,
                null /* 其他参数 */
        );
        // 接收消息
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {

            /**
             * 回调方法，当收到消息后，会自动执行该方法
             * @param consumerTag 标识
             * @param envelope 获取交换机、路由名称等信息
             * @param properties 配置信息
             * @param body 数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("consumerTag: " + consumerTag);
                System.out.println("Exchange: " + envelope.getExchange());
                System.out.println("RoutingKey: " + envelope.getRoutingKey());
                System.out.println("properties: " + properties);
                System.out.println("body: " + new String(body));
            }

        };
        channel.basicConsume(
                "hello_world" /* 队列名称 */,
                true /* 是否自动确认 */,
                consumer /* 回调对象 */
        );
        // 消费者一直监听消息，不要关闭资源
    }

}
```

## 工作队列模式

在工作队列模式（[Work queues](https://www.rabbitmq.com/tutorials/tutorial-two-python.html)）下，多个消费者共同消费同一个队列中的消息。在一个队列中如果有多个消费者，那么消费者之间对于同一个消息的关系是竞争的关系。

![工作队列模式](资源\工作队列模式.png)

对于任务过重或任务较多情况使用工作队列可以提高任务处理的速度。例如：短信服务部署多个，只需要有一个节点成功发送即可。

复制一个`Consumer`，命名为`Consumer2`。同时启动这两个消费者。然后修改`Producer`，发送十条消息：

```java
// 发送消息
for (int i = 1; i <= 10; i++) {
    String body = "hello rabbitmq " + i;
    channel.basicPublish(
        ""  /* 交换机名称，简单模式下交换机会使用默认的，指定空字符串即可 */,
        "hello_world" /* 路由名称，简单模式下必须与队列名称对应 */,
        null /* 配置信息 */,
        body.getBytes() /* 发送的消息 */
    );
}
```

可以看到，两个消费者顺序消费完所有消息。

## 订阅模式

在订阅模式（[Publish/Subscribe](https://www.rabbitmq.com/tutorials/tutorial-three-python.html)）中，多了一个交换机角色，而且过程略有变化：

- 生产者发送消息给交换机（X）而不是队列。
- 交换机接收生产者发送的消息，同时知道如何处理消息，例如递交给某个队列、递交给所有队列（**队列中的消息只被消费一次**），或是将消息丢弃。到底如何操作，取决于交换机的类型，有三种常见类型：
  - `Fanout`：广播，将消息交给所有绑定到交换机的队列。这会得到订阅模式。
  - `Direct`：定向，将消息交给符合指定路由名称（routing key）的队列。
  - `Topic`：通配符，将消息交给符合路由模式（routing pattern）的队列。

交换机只负责转发消息，不具有存储消息的能力。因此如果没有任何队列与交换机绑定，或者没有符合路由规则的队列，那么消息会丢失。

![订阅模式](资源\订阅模式.png)



修改`Producer`类：

```java
// 创建交换机
String exchangeName = "test_fanout";
channel.exchangeDeclare(
        exchangeName /* 交换机名称 */,
        BuiltinExchangeType.FANOUT /* 交换机类型 */,
        true /* 是否持久化 */,
        false /* 是否自动删除 */,
        false /* 是否内部使用，一般为false */,
        null /* 其他参数 */
);
// 创建队列
String queue1Name = "test_fanout_queue1";
String queue2Name = "test_fanout_queue2";
channel.queueDeclare(queue1Name, true, false, false, null);
channel.queueDeclare(queue2Name, true, false, false, null);
// 绑定队列与交换机
channel.queueBind(
        queue1Name /* 队列名称 */,
        exchangeName /* 交换机名称 */,
        "" /* 路由名称绑定规则，对于Fanout类型的交换机，设置为空字符串 */
);
channel.queueBind(queue2Name, exchangeName, "");
// 发送消息
String body = "日志信息：张三调用了findAll方法...日志级别：info...";
channel.basicPublish(exchangeName, "", null, body.getBytes());
// 释放资源
channel.close();
connection.close();
```

两个消费者的代码基本不变，修改如下：

```java
// 接收消息
com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("body: " + new String(body));
        System.out.println("将日志信息存储到数据库...");
    }
};
String queue1Name = "test_fanout_queue1";
channel.basicConsume(queue1Name, true, consumer);
```

```java
// 接收消息
com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("body: " + new String(body));
        System.out.println("将日志信息打印到控制台...");
    }

};
String queue2Name = "test_fanout_queue2";
channel.basicConsume(queue2Name, true, consumer);
```

分别启动生产者与两个消费者，两个消费者分别消费了消息。

## 路由模式

指定交换机的类型为`Direct`，就得到路由模式（[Routing](https://www.rabbitmq.com/tutorials/tutorial-four-python.html)）。路由模式要求队列在绑定交换机时要指定路由名称，消息会转发到符合路由名称的队列。

![路由模式](资源\路由模式.png)

修改`Producer`的队列名称与绑定规则：

```java
// 创建交换机
String exchangeName = "test_direct";
channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
// 创建队列
String queue1Name = "test_direct_queue1";
String queue2Name = "test_direct_queue2";
channel.queueDeclare(queue1Name, true, false, false, null);
channel.queueDeclare(queue2Name, true, false, false, null);
// 绑定队列与交换机
channel.queueBind(queue1Name, exchangeName, "error");
channel.queueBind(queue2Name, exchangeName, "info");
channel.queueBind(queue2Name, exchangeName, "warning");
channel.queueBind(queue2Name, exchangeName, "error");
// 发送消息
String body = "日志信息：张三调用了findAll方法...日志级别：info...";
channel.basicPublish(exchangeName, "info", null, body.getBytes());
```

对于消费者，只需要修改队列名称即可：

```java
String queue1Name = "test_direct_queue1";
```

```java
String queue2Name = "test_direct_queue2";
```

分别启动生产者与两个消费者，可以看到，只有`Consumer2`接收到了消息。

修改生产者：

RabbitMQ 提供了 6 种工作模式：简单模式、work queues、Publish/Subscribe 发布与订阅模式、Routing（路由模式）、Topics（主题模式）、RPC（远程调用模式）（远程调用，不太算 MQ；暂不作介绍）。官网对应模式介绍：https://www.rabbitmq.com/getstarted.html

```java
// 发送消息
String body = "日志信息：张三调用了delete方法...出错误了...日志级别：error...";
channel.basicPublish(exchangeName, "error", null, body.getBytes());
```

分别启动生产者与两个消费者，可以看到，两个消费者都接收到了消息。

## 通配符模式

指定交换机的类型为`Topic`，就得到通配符模式（[Topics](https://www.rabbitmq.com/tutorials/tutorial-five-python.html)）。与路由模式一样，通配符模式下交换机根据路由名称将消息路由到不同队列，只是`Topic`类型的交换机可以在绑定路由名称时使用通配符：

- `#`：匹配一个或多个词，词之间以`.`分割。
- `*`：匹配一个词。

![通配符模式](资源\通配符模式.png)

修改`Producer`的队列名称与绑定规则：

```java
// 创建交换机
String exchangeName = "test_topic";
channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, false, false, null);
// 创建队列
String queue1Name = "test_topic_queue1";
String queue2Name = "test_topic_queue2";
channel.queueDeclare(queue1Name, true, false, false, null);
channel.queueDeclare(queue2Name, true, false, false, null);
// 绑定队列与交换机
channel.queueBind(queue1Name, exchangeName, "#.error");
channel.queueBind(queue1Name, exchangeName, "order.*");
channel.queueBind(queue2Name, exchangeName, "*.*");
// 发送消息
String body = "日志信息：张三调用了findAll方法...日志级别：info...";
channel.basicPublish(exchangeName, "order.info", null, body.getBytes());
```

对于消费者，只需要修改队列名称即可：

```java
String queue1Name = "test_topic_queue1";
```

```java
String queue2Name = "test_topic_queue2";
```

分别启动生产者与两个消费者，可以看到，两个消费者都接收到了消息。

如果将路由名称改为：

```java
channel.basicPublish(exchangeName, "goods.info", null, body.getBytes());
```

则只有`Consumer2`接收到消息。

如果将路由名称改为：

```java
channel.basicPublish(exchangeName, "goods.error", null, body.getBytes());
```

则两个消费者都接收到消息。

# Spring整合RabbitMQ

创建两个模块，分别作为生产者与消费者。

引入依赖与插件：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
    <version>RELEASE</version>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>RELEASE</version>
</dependency>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

在src/main/resources目录下，创建rabbitmq.properties配置文件：

```properties
rabbitmq.host=192.168.227.128
rabbitmq.port=5672
rabbitmq.username=admin
rabbitmq.password=admin
rabbitmq.virtual-host=/itcast
```

在生产者的src/main/resources目录下，创建spring-rabbitmq-producer.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory"
                               host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>
    <!-- 定义管理交换机、队列 -->
    <rabbit:admin connection-factory="connectionFactory"/>

    <!-- 定义持久化队列，不存在则自动创建；不指定绑定的交换机则绑定到默认交换机，默认交换机类型为direct，名字为：""，路由键为队列的名称 -->
    <!-- id为bean的名称，name为queue的名称，auto-declare表示当队列不存在时是否自动创建队列；auto-delete表示当最后一个消费者与该队列断开连接后，是否自动删除队列；exclusive表示是否独占；durable表示是否持久化等 -->
    <rabbit:queue id="spring_queue" name="spring_queue" auto-declare="true"/>

    <!-- 广播；所有队列都能收到消息 -->
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_fanout_queue_1" name="spring_fanout_queue_1" auto-declare="true"/>

    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_fanout_queue_2" name="spring_fanout_queue_2" auto-declare="true"/>

    <!--定义广播类型交换机；并绑定上述两个队列-->
    <rabbit:fanout-exchange id="spring_fanout_exchange" name="spring_fanout_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding queue="spring_fanout_queue_1"/>
            <rabbit:binding queue="spring_fanout_queue_2"/>
        </rabbit:bindings>
    </rabbit:fanout-exchange>

    <!-- 通配符；*匹配一个单词，#匹配多个单词 -->
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_star" name="spring_topic_queue_star" auto-declare="true"/>
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_well" name="spring_topic_queue_well" auto-declare="true"/>
    <!--定义广播交换机中的持久化队列，不存在则自动创建-->
    <rabbit:queue id="spring_topic_queue_well2" name="spring_topic_queue_well2" auto-declare="true"/>

    <rabbit:topic-exchange id="spring_topic_exchange" name="spring_topic_exchange" auto-declare="true">
        <rabbit:bindings>
            <rabbit:binding pattern="heima.*" queue="spring_topic_queue_star"/>
            <rabbit:binding pattern="heima.#" queue="spring_topic_queue_well"/>
            <rabbit:binding pattern="itcast.#" queue="spring_topic_queue_well2"/>
        </rabbit:bindings>
    </rabbit:topic-exchange>

    <!--定义rabbitTemplate对象操作可以在代码中方便发送消息-->
    <rabbit:template id="rabbitTemplate" connection-factory="connectionFactory"/>
</beans>
```

生产者测试：

```java
package com.example.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-producer.xml")
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testHelloWorld() {
        rabbitTemplate.convertAndSend("spring_queue", "hello world spring...");
    }

    @Test
    public void testFanout() {
        rabbitTemplate.convertAndSend("spring_fanout_exchange", "", "spring fanout...");
    }

    @Test
    public void testTopics() {
        rabbitTemplate.convertAndSend("spring_topic_exchange", "heima.hehe.haha", "spring topic...");
    }

}
```

在消费者的src/main/resources目录下，创建spring-rabbitmq-consumer.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:rabbit="http://www.springframework.org/schema/rabbit"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/rabbit
       http://www.springframework.org/schema/rabbit/spring-rabbit.xsd">
    <!--加载配置文件-->
    <context:property-placeholder location="classpath:rabbitmq.properties"/>

    <!-- 定义rabbitmq connectionFactory -->
    <rabbit:connection-factory id="connectionFactory" host="${rabbitmq.host}"
                               port="${rabbitmq.port}"
                               username="${rabbitmq.username}"
                               password="${rabbitmq.password}"
                               virtual-host="${rabbitmq.virtual-host}"/>

    <bean id="springQueueListener" class="com.example.consumer.listener.SpringQueueListener"/>
<!--    <bean id="fanoutListener1" class="com.example.consumer.listener.FanoutListener1"/>-->
<!--    <bean id="fanoutListener2" class="com.example.consumer.listener.FanoutListener2"/>-->
<!--    <bean id="topicListenerStar" class="com.example.consumer.listener.TopicListenerStar"/>-->
<!--    <bean id="topicListenerWell" class="com.example.consumer.listener.TopicListenerWell"/>-->
<!--    <bean id="topicListenerWell2" class="com.example.consumer.listener.TopicListenerWell2"/>-->

    <rabbit:listener-container connection-factory="connectionFactory" auto-declare="true">
        <rabbit:listener ref="springQueueListener" queue-names="spring_queue"/>
<!--        <rabbit:listener ref="fanoutListener1" queue-names="spring_fanout_queue_1"/>-->
<!--        <rabbit:listener ref="fanoutListener2" queue-names="spring_fanout_queue_2"/>-->
<!--        <rabbit:listener ref="topicListenerStar" queue-names="spring_topic_queue_star"/>-->
<!--        <rabbit:listener ref="topicListenerWell" queue-names="spring_topic_queue_well"/>-->
<!--        <rabbit:listener ref="topicListenerWell2" queue-names="spring_topic_queue_well2"/>-->
    </rabbit:listener-container>
</beans>
```

创建对应的监听器：

```java
package com.example.consumer.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SpringQueueListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println(new String(message.getBody()));
    }
}
```

消费者测试（查看是否能接收消息打印到控制台）：

```java
package com.example.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-rabbitmq-consumer.xml")
public class ConsumerTest {

    @Test
    public void test() {
        while (true) {
            
        }
    }

}
```

# SpringBoot整合RabbitMQ

创建SpringBoot工程，创建两个模块，分别作为生产者与消费者。

引入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.7.0</version>
</dependency>
```

在src/main/resources目录下，创建application.yml配置文件：

```yml
spring:
  rabbitmq:
    host: 192.168.227.128
    port: 5672
    username: admin
    password: admin
    virtual-host: /
```

生产者启动类：

```java
package com.example.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

}
```

生产者配置类：

```java
package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "boot_topic_exchange";

    public static final String QUEUE_NAME = "boot_queue";

    // 交换机
    @Bean("bootExchange")
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    // 队列
    @Bean("bootQueue")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    // 队列与交换机的绑定关系
    @Bean
    public Binding bindQueueExchange(@Qualifier("bootQueue") Queue queue, @Qualifier("bootExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.#").noargs();
    }

}
```

生产者测试：

```java
package com.example.producer;

import com.example.producer.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSend() {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "boot.haha", "boot mq hello");
    }

}
```

消费者启动类：

```java
package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
```

定义监听类：

```java
package com.example.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @RabbitListener(queues = "boot_queue")
    public void ListenerQueue(Message message /* 可以提供第二个参数，表示消息体，类型为消息类型；可以提供第三个参数，表示当前传输数据的通道 */) {
        System.out.println(new String(message.getBody()));
    }

}
```

启动消费者（查看是否能接收消息打印到控制台）。

# 消息的[可靠投递](https://www.rabbitmq.com/reliability.html)

## 投递可靠性

保证消息不丢失、可靠抵达，可以使用事务消息，但是这会导致性能下降250倍，为此引入确认机制。

RabbitMQ整个消息投递的路径为：生产者$\rightarrow$rabbitmq broker$\rightarrow$交换机$\rightarrow$队列$\rightarrow$消费者。

在使用RabbitMQ的时候，作为消息发送方希望杜绝任何消息丢失或者投递失败场景。RabbitMQ提供了两种方式用来控制消息的投递可靠性：

- `confirm`：确认模式。消息从生产者到交换机则会返回一个`confirmCallback`。
- `return`：退回模式。消息从交换机到队列投递失败则会返回一个`returnsCallback`。

[^0]: `ReturnCallback`貌似过时了，所以使用`returnsCallback`。

创建SpringBoot工程，创建两个模块，分别作为生产者与消费者。

引入依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <version>2.7.0</version>
</dependency>
```

在生产者的src/main/resources目录下，创建application.yml配置文件：

```yml
spring:
  rabbitmq:
    host: 192.168.227.128
    port: 5672
    username: admin
    password: admin
    virtual-host: /
    publisher-confirm-type: correlated  # 开启确认模式：发布消息成功到交换器后会触发回调方法
```

创建配置类：

```java
package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "test_exchange_confirm";

    public static final String QUEUE_NAME = "test_queue_confirm";

    @Bean("test_exchange_confirm")
    public Exchange bootExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("test_queue_confirm")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding bindQueueExchange(@Qualifier("test_queue_confirm") Queue queue, @Qualifier("test_exchange_confirm") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("confirm").noargs();
    }

}
```

测试确认模式：

```java
package com.example.producer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerTest {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testConfirm() {
        // 定义回调函数
        rabbitTemplate.setConfirmCallback(
                (correlationData /* 相关配置信息，可以在convertAndSend的某些重载形式中设置 */,
                 ack /* 交换机是否成功接收到消息 */,
                 cause /* 失败原因 */
                ) -> {
                    System.out.println("confirm方法被执行了...");
                    if (ack) {
                        System.out.println("接收消息成功: " + cause);
                    } else {
                        System.out.println("接收消息失败: " + cause);
                        // 做一些处理，例如让消息再次发送
                    }
                });
        // 发送消息
        rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
    }

}
```

可以看到消息成功发送，回调函数执行。如果更改交换机名称，则消息发送失败，回调函数也执行（但是输出不同）。

下面在application.yml中开启退回模式：

```yml
spring:
  rabbitmq:
    publisher-returns: true  # 开启退回模式
```

测试退回模式：

```java
@Test
public void testReturn() {
    /* 设置交换机处理失败消息的模式，有两种：
       1. 如果消息没有路由到队列，则丢弃消息（默认），此时rabbitTemplate.setReturnsCallback不会被执行
       2. 如果消息没有路由到队列，则返回给消息发送方ReturnsCallback（本次使用）
     */
    rabbitTemplate.setMandatory(true);
    rabbitTemplate.setReturnsCallback(returned -> System.out.println("return执行了..."));
    // 发送消息
    rabbitTemplate.convertAndSend("test_exchange_confirm", "confirm", "message confirm...");
}
```

`ReturnsCallback`不会被执行。如果修改队列名称，则`ReturnsCallback`被执行。

## Consumer Ack

Ack即Acknowledge，表示消费端接收到消息后的确认方式，有三种：

- 自动确认：`acknowledge="none"`。消息一旦被消费端接收到，则自动确认消息收到，并将相应的消息从RabbitMQ的消息缓存中移除。
- 手动确认：`acknowledge="manual"`。在实际业务处理中，很可能消息接收到但业务处理出现一场。如果采用自动确认，那么该消息就会丢失。如果设置手动确认方式，则需要在业务处理成功后，调用`channel.basicAck`方法，手动签收；如果出现异常，则调用`channel.basicNack`或`channel.basicReject`方法，拒绝消息，让其自动重新发送消息。
- 根据异常情况确认：`acknowledge="auto"`（这里不作介绍）。

在消费者的src/main/resources目录下，创建application.yml配置文件：

```yml
spring:
  rabbitmq:
    host: 192.168.227.128
    port: 5672
    username: admin
    password: admin
    virtual-host: /
```

创建监听器：

```java
package com.example.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AckListener {

    @RabbitListener(queues = "test_queue_confirm", ackMode = "MANUAL")
    public void ListenerQueue(Message message, Channel channel) throws IOException, InterruptedException {
        Thread.sleep(1000);  // 暂停1秒（仅用于演示）
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 接收转换消息
            System.out.println(new String(message.getBody()));
            // 处理业务逻辑
            System.out.println("处理业务逻辑...");
            int i = 1 / 0;
            // 没有发生异常，手动签收
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            // 发生异常，拒绝签收（也可以使用channel.basicReject方法）
            channel.basicNack(deliveryTag, true, true  /* 是否重回队列，如果为true，则消息重新回到队列，broker会重新发送消息给消费端 */);
        }
    }
}
```

测试：

```java
package com.example.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConsumerTest {

    @Test
    public void test() {
        while (true) {}
    }

}
```

生产者发送消息后，可以看到消费端由于发生异常，消息一直被拒收。如果删除`AckListener.ListenerQueue`方法的`int i = 1 / 0;`，则消息发送成功。

## 消费端限流

修改`AckListener`为`QosListener`：

```java
package com.example.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QosListener {

    @RabbitListener(queues = "test_queue_confirm", ackMode = "MANUAL" /* 必须是手动确认模式 */, containerFactory = "mqConsumerListenerContainer" /* 指定监听器工厂 */)
    public void ListenerQueue(Message message, Channel channel) throws IOException, InterruptedException {
        Thread.sleep(1000);  // 暂停1秒（仅用于演示）
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 接收转换消息
            System.out.println(new String(message.getBody()));
            // 处理业务逻辑
            System.out.println("处理业务逻辑...");
            // 手动签收
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            // 拒绝签收，也可以使用channel.basicReject方法
            channel.basicNack(deliveryTag, true, true /* 是否重回队列，如果为true，则消息重新回到队列，broker会重新发送消息给消费端 */);
        }
    }
}
```

创建对应的配置类：

```java
package com.example.consumer.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean(name = "mqConsumerListenerContainer")
    public SimpleRabbitListenerContainerFactory mqConsumerListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(1);  // 限流，一次拉取一条消息
        return factory;
    }
}
```

[^0]: 这个实现中的`CachingConnectionFactory`未被使用，为什么？

生产者发送多条消息，可以看到消费端逐条消费所有消息。如果将手动签收注释掉，则生产者的多条消息会有一条处于`Unacked`状态，其他消息都不被拉取。如果将手动签收注释掉，且取消限流操作，则所有消息都处于`Unacked`状态；此时如果再次手动签收，且限流，则可以看到消息被逐条消费。

[^0]: 怎么实现？

## TTL

TTL全称Time To Live（存活时间/过期时间），当消息到达TTL后，如果还未被消费，会被自动清除。RabbitMQ可以对消息设置过期时间（参数：`expiration`），也可以对整个队列（Queue）设置过期时间（参数：`x-message-ttl`）。如果同时设置了消息的过期时间与队列的过期时间，则以时间短的为准。

队列过期后，会将队列所有消息全部移除。消息过期后，只有消息在队列顶端才会去判断其是否过期，如果判断过期则移除。

修改生产端的`RabbitMQConfig`：

```java
package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "test_exchange_ttl";

    public static final String QUEUE_NAME = "test_queue_ttl";

    @Bean("test_exchange_ttl")
    public Exchange bootExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("test_queue_ttl")
    public Queue bootQueue() {
        return QueueBuilder.durable(QUEUE_NAME).ttl(10000).build();  // 10秒过期
    }

    @Bean
    public Binding bindQueueExchange(@Qualifier("test_queue_ttl") Queue queue, @Qualifier("test_exchange_ttl") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.#").noargs();
    }

}
```

测试整个队列过期：

```java
@Test
public void testTTL() {
    for (int i = 0; i < 10; i++) {
        rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...");
    }
}
```

可以看到消息成功发送，且10秒后过期。

修改队列使其100秒过期，然后修改测试方法，通过消息后处理对象设置消息的过期时间：

```java
@Test
public void testTTL() {
    rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...",
                                  // 消息后处理对象，设置消息的参数信息
                                  message -> { message.getMessageProperties().setExpiration("5000");  // 消息的过期时间
                                              return message;
                                             });
}
```

可以看到消息成功发送，且5秒后过期。

修改测试：

```java
@Test
public void testTTL() {
    for (int i = 0; i < 10; i++) {
        if (i == 5) {
            rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...",
                    // 消息后处理对象，设置消息的参数信息
                    message -> {
                        message.getMessageProperties().setExpiration("5000");  // 消息的过期时间
                        return message;
                    });
        } else {
            rabbitTemplate.convertAndSend("test_exchange_ttl", "ttl.hehe", "message ttl...");
        }
    }
}
```

可以看到5秒后消息仍然为10条；100秒后所有消息才会被移除。

## 死信队列

当消息成为死信（Dead Message）后，可以被重新发送到另一个交换机，它就是死信交换机（Dead Letter Exchange，DLX）。当消息成为死信后，如果该队列绑定了死信交换机，则消息会被死信交换机重新路由到死信队列。在RabbitMQ中，死信队列一般指的是死信交换机；而在很多其他MQ产品中，没有交换机的概念，只有死信队列的说法。

消息在以下三种情况下会成为死信：

- 队列消息长度到达限制。
- 消费者拒接消费消息（通过`basicNack`或`basicReject`方法），并且不重回队列（`requeue=false`）。
- 原队列存在消息过期设置，消息到达超时时间未被消费。

[^0]: 如果消息超时，但不在队列顶端呢？

队列通过`x-dead-letter-exchange`参数绑定到死信交换机，队列通过`x-dead-letter-routing-key`参数指定队列向死信交换机发送消息的路由名称。

死信交换机/死信队列与普通交换机/队列没有区别。

修改生产端的`RabbitMQConfig`：

```java
package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "test_exchange_dlx";

    public static final String QUEUE_NAME = "test_queue_dlx";

    public static final String DLX_NAME = "exchange_dlx";

    public static final String DLX_QUEUE_NAME = "queue_dlx";

    @Bean("test_exchange_dlx")
    public Exchange testExchangeDlx() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("test_queue_dlx")
    public Queue testQueueDlx() {
        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange("exchange_dlx")  // x-dead-letter-exchange
                .deadLetterRoutingKey("dlx.hehe")  // x-dead-letter-routing-key
                .ttl(10000)  // 消息过期设置
                .maxLength(10)  // 队列消息长度限制
                .build();
    }

    @Bean
    public Binding bindQueueExchange(@Qualifier("test_queue_dlx") Queue queue, @Qualifier("test_exchange_dlx") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("test.dlx.#").noargs();
    }

    @Bean("exchange_dlx")
    public Exchange exchangeDlx() {
        return ExchangeBuilder.topicExchange(DLX_NAME).durable(true).build();
    }

    @Bean("queue_dlx")
    public Queue queueDlx() {
        return QueueBuilder.durable(DLX_QUEUE_NAME).build();
    }

    @Bean
    public Binding bindQueueExchangeDlx(@Qualifier("queue_dlx") Queue queue, @Qualifier("exchange_dlx") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dlx.#").noargs();
    }

}
```

[^0]: 队列给死信交换机绑定的routing key可以是哪些模式的？

测试：

```java
@Test
public void testDlx() {
    rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.haha", "我是一条信息，我会死吗？");
}
```

消息被发送到正常队列，然后10秒后进入死信队列。

修改测试：

```java
@Test
public void testDlx() {
    for (int i = 0; i < 20; i++) {
        rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.haha", "我是一条信息，我会死吗？");
    }
}
```

其中10条信息发送到正常队列，10条消息进入死信队列；10秒后，所有消息进入死信队列。

修改消费端的`OosListener`为`DlxListener`并运行消费端的测试：

```java
package com.example.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DlxListener {

    @RabbitListener(queues = "test_queue_dlx", ackMode = "MANUAL")
    public void ListenerQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 接收转换消息
            System.out.println(new String(message.getBody()));
            // 处理业务逻辑
            System.out.println("处理业务逻辑...");
            int i = 1 / 0;
            // 手动签收
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            System.out.println("出现异常，拒绝接收");
            // 拒绝签收，也可以使用channel.basicReject方法
            channel.basicNack(deliveryTag, true, false /* 不重回队列 */);
        }
    }
}
```

修改测试：

```java
@Test
public void testDlx() {
    rabbitTemplate.convertAndSend("test_exchange_dlx", "test.dlx.haha", "我是一条信息，我会死吗？");
}
```

运行测试用例。可以发现消息直接进入死信队列。

## 延迟队列

消息进入延迟队列后不会被立即消费，只有到达指定时间后，才会被消费。延迟队列可以实现以下需求：

- 下单后，30分钟未支付，取消订单，回滚库存。
- 新用户注册成功7天后，发送短信问候。

实现方式：

- 定时器。周期性执行一段代码。对于第一个需求，下单后会向订单表中添加一条记录，包括下单时间。因此可以周期性读取订单表记录，判断下单时间是否少于30分钟，每个订单是否支付，如果两者都否，可以取消订单并递增库存。这种实现不优雅，且周期过大误差大，周期过小系统负担重。
- 延迟队列。对于第一个需求，订单系统发送下单消息到MQ的延迟队列中，延迟队列在消息到达30分钟后发送到库存系统，判断订单状态，未支付则取消订单，回滚库存。这种方式针对每个订单只需查询一次数据库。

RabbitMQ中并未提供延迟队列功能。但是可以组合TTL与死信队列实现延迟队列的效果：

- 订单系统发送消息到交换机，交换机路由消息到队列中，设置队列的TTL为30分钟。
- 队列绑定一个死信交换机。队列中的消息成为死信后，死信交换机会将消息路由到其绑定的死信队列，库存系统监听该死信队列。
- 库存系统接收到消息，判断订单状态，未支付则取消订单，回滚库存。

修改生产端的`RabbitMQConfig`：

```java
package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "order_exchange";

    public static final String QUEUE_NAME = "order_queue";

    public static final String DLX_NAME = "order_exchange_dlx";

    public static final String DLX_QUEUE_NAME = "order_queue_dlx";

    @Bean("order_exchange")
    public Exchange orderExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("order_queue")
    public Queue orderQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange("order_exchange_dlx")
                .deadLetterRoutingKey("dlx.order.cancel")
                .ttl(10000)  // 消息过期设置
                .build();
    }

    @Bean
    public Binding bindQueueExchange(@Qualifier("order_queue") Queue queue, @Qualifier("order_exchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("order.#").noargs();
    }

    @Bean("order_exchange_dlx")
    public Exchange orderExchangeDlx() {
        return ExchangeBuilder.topicExchange(DLX_NAME).durable(true).build();
    }

    @Bean("order_queue_dlx")
    public Queue orderQueueDlx() {
        return QueueBuilder.durable(DLX_QUEUE_NAME).ttl(100000).build();
    }

    @Bean
    public Binding bindQueueExchangeDlx(@Qualifier("order_queue_dlx") Queue queue, @Qualifier("order_exchange_dlx") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dlx.order.#").noargs();
    }

}
```

运行生产端测试方法`testDlx`（目的是为了创建对应的交换机与队列）。

编写生产端测试：

```java
@Test
public void testDelay() throws InterruptedException {
    // 模拟发送订单消息
    rabbitTemplate.convertAndSend("order_exchange", "order.msg", "订单信息：id=1, time=2022年8月2日19:22:23");
    for (int i = 10; i > 0 ; --i) {
        System.out.println(i + "...");
        Thread.sleep(1000);
    }
}
```

修改消费端的`DlxListener`为`OrderListener`：

```java
package com.example.consumer.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderListener {

    @RabbitListener(queues = "order_queue_dlx", ackMode = "MANUAL")
    public void ListenerQueue(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 接收转换消息
            System.out.println(new String(message.getBody()));
            // 处理业务逻辑
            System.out.println("处理业务逻辑...");
            System.out.println("根据订单id查询其状态...");
            System.out.println("判断状态是否为支付成功");
            System.out.println("取消订单，回滚库存");
            // 手动签收
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            System.out.println("出现异常，拒绝接收");
            // 拒绝签收，也可以使用channel.basicReject方法
            channel.basicNack(deliveryTag, true, false /* 不重回队列 */);
        }
    }
}
```

分别启动消费端与生产端的测试用例。可以看到，倒计时10秒结束，消费端得到消息。

# 日志与监控

## RabbitMQ日志

RabbitMQ默认日志存放路径为`/var/log/rabbitmq`。一般查看的是`rabbit@[主机名].log`文件。日志包含了RabbitMQ的版本号、Erlang的版本号、RabbitMQ服务节点名称、cookie的hash值、RabbitMQ配置文件地址、内存限制、磁盘限制、默认账户guest的创建以及权限配置等等。

## web管控台监控

RabbitMQ管理界面提供图像化界面操作RabbitMQ。

## `rabbitmqctl`管理和监控

RabbitMQ提供了一些命令查看相关信息：

- `rabbitmqctl list_queues`：队列。
- `rabbitmqctl list_exchanges`：交换机。
- `rabbitmqctl list_users`：用户。
- `rabbitmqctl list_connections`：连接。
- `rabbitmqctl list_consumers`：消费者信息。
- `rabbitmqctl environment`：环境变量。
- `rabbitmqctl list_queues name messages_unacknowledged`：未被确认的队列。
- `rabbitmqctl list_queues name memory`：单个队列的内存使用。
- `rabbitmqctl list_queues name messages_ready`：准备就绪的队列。

## 消息追踪

在使用任何消息中间件的过程中，难免会出现某条消息异常丢失的情况。对于RabbitMQ而言，可能是因为生产者或消费者与RabbitMQ断开了连接，而它们与RabbitMQ又采用了不同的确认机制；也有可能是因为交换器与队列之间不同的转发策略；甚至是交换器并没有与任何队列进行绑定，生产者又不感知或者没有采取相应的措施；另外RabbitMQ本身的集群策略也可能导致消息的丢失。这个时候就需要有一个较好的机制跟踪记录消息的投递过程，以此协助开发和运维人员进行问题的定位。

在RabbitMQ中可以使用Firehose和rabbitmq_tracing插件功能来实现消息追踪。

### Firehose

Firehose的机制是将生产者投递给RabbitMQ的消息、RabbitMQ投递给消费者的消息按照指定的格式发送到默认的交换机上。这个默认的交换机的名称为amq.rabbitmq.trace，它是一个topic类型的交换机。发送到这个交换机上的消息的路由名称为`publish.exchangename`和`deliver.queuename`。其中`exchangename`和`queuename`为实际交换机与队列的名称，分别对应生产者投递到交换机的消息，和消费者从队列上获取的消息。

注意：打开trace会影响消息写入功能，适当打开后请关闭。

- `rabbitmqctl trace_on`：开启Firehose命令。
- `rabbitmqctl trace_off`：关闭Firehose命令。

[^0]: 这里介绍得比较简略。

### rabbitmq_tracing

rabbitmq_tracing和Firehose在实现上如出一辙，只不过rabbitmq_tracing的方式比Firehose多了一层GUI的包装，更容易使用和管理。

需要先启用插件：`rabbitmq-plugins enable rabbitmq_tracing`。

# RabbitMQ应用问题

## 消息可靠性保障

消息可靠性保障要求（几乎）100%确保消息发送成功。要想做到这点，可以使用消息补偿机制：

- 生产者将业务数据入库DB，然后发送消息到队列Q1，消费者监听Q1，取出消息消费，并操作自己的数据库。
- 消费者操作消息成功后会发送确认消息到队列Q2。回调检查服务监听确认消息，将其写入数据库MDB。
- 生产者在发送消息后，还会延迟发送一条相同消息到队列Q3，回调检查服务监听Q3，比对当前消息是否存在MDB中。如果发现消息不存在，说明消费者消费消息失败，因此通知生产者重新发送该条消息到Q1，重复以上流程。
- 极端情况下，发送到Q1与Q3的相同消息都丢失。因此还有一个定时检查服务，检查MDB与DB中的数据是否匹配，通知生产者重新发送DB中多余的消息，重复以上流程。

![消息补偿](资源\消息补偿.png)

## 消息幂等性保障

幂等性指一次和多次请求某一个资源，对于资源本身应该具有同样的结果。也就是说，其任意多次执行对资源本身所产生的影响均与一次执行的影响相同。在MQ中指消费多条相同的消息，得到与消费该消息一次相同的结果。

一种消息幂等性保障方案使用乐观锁机制：数据库更新要求版本号匹配；数据库更新后相应数据的版本号加1。这样重复的消息永远不会被执行。例如，针对账号扣减操作：

![乐观锁机制](资源\乐观锁机制.png)

# RabbitMQ集群搭建

[^0]: 未验证本章节的合法性。

摘要：实际生产应用中都会采用消息队列的集群方案，如果选择RabbitMQ那么有必要了解下它的集群方案原理

一般来说，如果只是为了学习RabbitMQ或者验证业务工程的正确性那么在本地环境或者测试环境上使用其单实例部署就可以了，但是出于MQ中间件本身的可靠性、并发性、吞吐量和消息堆积能力等问题的考虑，在生产环境上一般都会考虑使用RabbitMQ的集群方案。

### 3.1 集群方案的原理

RabbitMQ这款消息队列中间件产品本身是基于Erlang编写，Erlang语言天生具备分布式特性（通过同步Erlang集群各节点的magic cookie来实现）。因此，RabbitMQ天然支持Clustering。这使得RabbitMQ本身不需要像ActiveMQ、Kafka那样通过ZooKeeper分别来实现HA方案和保存集群的元数据。集群是保证可靠性的一种方式，同时可以通过水平扩展以达到增加消息吞吐量能力的目的。

![1565245219265](C:/Users/31654/Documents/Memory/Activity/rabbitmq/资料-rabbitMQ/day02/资料/pic/1566073768274.png)


### 3.2 单机多实例部署

由于某些因素的限制，有时候你不得不在一台机器上去搭建一个rabbitmq集群，这个有点类似zookeeper的单机版。真实生成环境还是要配成多机集群的。有关怎么配置多机集群的可以参考其他的资料，这里主要论述如何在单机中配置多个rabbitmq实例。

主要参考官方文档：https://www.rabbitmq.com/clustering.html

首先确保RabbitMQ运行没有问题

```shell
[root@super ~]# rabbitmqctl status
Status of node rabbit@super ...
[{pid,10232},
 {running_applications,
     [{rabbitmq_management,"RabbitMQ Management Console","3.6.5"},
      {rabbitmq_web_dispatch,"RabbitMQ Web Dispatcher","3.6.5"},
      {webmachine,"webmachine","1.10.3"},
      {mochiweb,"MochiMedia Web Server","2.13.1"},
      {rabbitmq_management_agent,"RabbitMQ Management Agent","3.6.5"},
      {rabbit,"RabbitMQ","3.6.5"},
      {os_mon,"CPO  CXC 138 46","2.4"},
      {syntax_tools,"Syntax tools","1.7"},
      {inets,"INETS  CXC 138 49","6.2"},
      {amqp_client,"RabbitMQ AMQP Client","3.6.5"},
      {rabbit_common,[],"3.6.5"},
      {ssl,"Erlang/OTP SSL application","7.3"},
      {public_key,"Public key infrastructure","1.1.1"},
      {asn1,"The Erlang ASN1 compiler version 4.0.2","4.0.2"},
      {ranch,"Socket acceptor pool for TCP protocols.","1.2.1"},
      {mnesia,"MNESIA  CXC 138 12","4.13.3"},
      {compiler,"ERTS  CXC 138 10","6.0.3"},
      {crypto,"CRYPTO","3.6.3"},
      {xmerl,"XML parser","1.3.10"},
      {sasl,"SASL  CXC 138 11","2.7"},
      {stdlib,"ERTS  CXC 138 10","2.8"},
      {kernel,"ERTS  CXC 138 10","4.2"}]},
 {os,{unix,linux}},
 {erlang_version,
     "Erlang/OTP 18 [erts-7.3] [source] [64-bit] [async-threads:64] [hipe] [kernel-poll:true]\n"},
 {memory,
     [{total,56066752},
      {connection_readers,0},
      {connection_writers,0},
      {connection_channels,0},
      {connection_other,2680},
      {queue_procs,268248},
      {queue_slave_procs,0},
      {plugins,1131936},
      {other_proc,18144280},
      {mnesia,125304},
      {mgmt_db,921312},
      {msg_index,69440},
      {other_ets,1413664},
      {binary,755736},
      {code,27824046},
      {atom,1000601},
      {other_system,4409505}]},
 {alarms,[]},
 {listeners,[{clustering,25672,"::"},{amqp,5672,"::"}]},
 {vm_memory_high_watermark,0.4},
 {vm_memory_limit,411294105},
 {disk_free_limit,50000000},
 {disk_free,13270233088},
 {file_descriptors,
     [{total_limit,924},{total_used,6},{sockets_limit,829},{sockets_used,0}]},
 {processes,[{limit,1048576},{used,262}]},
 {run_queue,0},
 {uptime,43651},
 {kernel,{net_ticktime,60}}]
```

停止rabbitmq服务

```shell
[root@super sbin]# service rabbitmq-server stop
Stopping rabbitmq-server: rabbitmq-server.

```



启动第一个节点：

```shell
[root@super sbin]# RABBITMQ_NODE_PORT=5673 RABBITMQ_NODENAME=rabbit1 rabbitmq-server start

              RabbitMQ 3.6.5. Copyright (C) 2007-2016 Pivotal Software, Inc.
  ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
  ##  ##
  ##########  Logs: /var/log/rabbitmq/rabbit1.log
  ######  ##        /var/log/rabbitmq/rabbit1-sasl.log
  ##########
              Starting broker...
 completed with 6 plugins.
```

启动第二个节点：

> web管理插件端口占用,所以还要指定其web插件占用的端口号。

```shell
[root@super ~]# RABBITMQ_NODE_PORT=5674 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15674}]" RABBITMQ_NODENAME=rabbit2 rabbitmq-server start

              RabbitMQ 3.6.5. Copyright (C) 2007-2016 Pivotal Software, Inc.
  ##  ##      Licensed under the MPL.  See http://www.rabbitmq.com/
  ##  ##
  ##########  Logs: /var/log/rabbitmq/rabbit2.log
  ######  ##        /var/log/rabbitmq/rabbit2-sasl.log
  ##########
              Starting broker...
 completed with 6 plugins.

```

结束命令：

```shell
rabbitmqctl -n rabbit1 stop
rabbitmqctl -n rabbit2 stop
```



rabbit1操作作为主节点：

```shell
[root@super ~]# rabbitmqctl -n rabbit1 stop_app  
Stopping node rabbit1@super ...
[root@super ~]# rabbitmqctl -n rabbit1 reset	 
Resetting node rabbit1@super ...
[root@super ~]# rabbitmqctl -n rabbit1 start_app
Starting node rabbit1@super ...
[root@super ~]# 
```

rabbit2操作为从节点：

```shell
[root@super ~]# rabbitmqctl -n rabbit2 stop_app
Stopping node rabbit2@super ...
[root@super ~]# rabbitmqctl -n rabbit2 reset
Resetting node rabbit2@super ...
[root@super ~]# rabbitmqctl -n rabbit2 join_cluster rabbit1@'super' ###''内是主机名换成自己的
Clustering node rabbit2@super with rabbit1@super ...
[root@super ~]# rabbitmqctl -n rabbit2 start_app
Starting node rabbit2@super ...

```

查看集群状态：

```
[root@super ~]# rabbitmqctl cluster_status -n rabbit1
Cluster status of node rabbit1@super ...
[{nodes,[{disc,[rabbit1@super,rabbit2@super]}]},
 {running_nodes,[rabbit2@super,rabbit1@super]},
 {cluster_name,<<"rabbit1@super">>},
 {partitions,[]},
 {alarms,[{rabbit2@super,[]},{rabbit1@super,[]}]}]
```

web监控：

![1566065096459](C:/Users/31654/Documents/Memory/Activity/rabbitmq/资料-rabbitMQ/day02/资料/pic/1566065096459.png)





### 3.3 集群管理

**rabbitmqctl join_cluster {cluster_node} [–ram]**
将节点加入指定集群中。在这个命令执行前需要停止RabbitMQ应用并重置节点。

**rabbitmqctl cluster_status**
显示集群的状态。

**rabbitmqctl change_cluster_node_type {disc|ram}**
修改集群节点的类型。在这个命令执行前需要停止RabbitMQ应用。

**rabbitmqctl forget_cluster_node [–offline]**
将节点从集群中删除，允许离线执行。

**rabbitmqctl update_cluster_nodes {clusternode}**

在集群中的节点应用启动前咨询clusternode节点的最新信息，并更新相应的集群信息。这个和join_cluster不同，它不加入集群。考虑这样一种情况，节点A和节点B都在集群中，当节点A离线了，节点C又和节点B组成了一个集群，然后节点B又离开了集群，当A醒来的时候，它会尝试联系节点B，但是这样会失败，因为节点B已经不在集群中了。

**rabbitmqctl cancel_sync_queue [-p vhost] {queue}**
取消队列queue同步镜像的操作。

**rabbitmqctl set_cluster_name {name}**
设置集群名称。集群名称在客户端连接时会通报给客户端。Federation和Shovel插件也会有用到集群名称的地方。集群名称默认是集群中第一个节点的名称，通过这个命令可以重新设置。

### 3.4 RabbitMQ镜像集群配置

> 上面已经完成RabbitMQ默认集群模式，但并不保证队列的高可用性，尽管交换机、绑定这些可以复制到集群里的任何一个节点，但是队列内容不会复制。虽然该模式解决一项目组节点压力，但队列节点宕机直接导致该队列无法应用，只能等待重启，所以要想在队列节点宕机或故障也能正常应用，就要复制队列内容到集群里的每个节点，必须要创建镜像队列。
>
> 镜像队列是基于普通的集群模式的，然后再添加一些策略，所以你还是得先配置普通集群，然后才能设置镜像队列，我们就以上面的集群接着做。

**设置的镜像队列可以通过开启的网页的管理端Admin->Policies，也可以通过命令。**

> rabbitmqctl set_policy my_ha "^" '{"ha-mode":"all"}'

![1566072300852](C:/Users/31654/Documents/Memory/Activity/rabbitmq/资料-rabbitMQ/day02/资料/pic/1566072300852.png)

> - Name:策略名称
> - Pattern：匹配的规则，如果是匹配所有的队列，是^.
> - Definition:使用ha-mode模式中的all，也就是同步所有匹配的队列。问号链接帮助文档。

### 3.5 负载均衡-HAProxy

HAProxy提供高可用性、负载均衡以及基于TCP和HTTP应用的代理，支持虚拟主机，它是免费、快速并且可靠的一种解决方案,包括Twitter，Reddit，StackOverflow，GitHub在内的多家知名互联网公司在使用。HAProxy实现了一种事件驱动、单一进程模型，此模型支持非常大的并发连接数。

##### 3.5.1  安装HAProxy

```shell
//下载依赖包
yum install gcc vim wget
//上传haproxy源码包
//解压
tar -zxvf haproxy-1.6.5.tar.gz -C /usr/local
//进入目录、进行编译、安装
cd /usr/local/haproxy-1.6.5
make TARGET=linux31 PREFIX=/usr/local/haproxy
make install PREFIX=/usr/local/haproxy
mkdir /etc/haproxy
//赋权
groupadd -r -g 149 haproxy
useradd -g haproxy -r -s /sbin/nologin -u 149 haproxy
//创建haproxy配置文件
mkdir /etc/haproxy
vim /etc/haproxy/haproxy.cfg
```




##### 3.5.2 配置HAProxy

配置文件路径：/etc/haproxy/haproxy.cfg

```shell
#logging options
global
	log 127.0.0.1 local0 info
	maxconn 5120
	chroot /usr/local/haproxy
	uid 99
	gid 99
	daemon
	quiet
	nbproc 20
	pidfile /var/run/haproxy.pid

defaults
	log global
	
	mode tcp

	option tcplog
	option dontlognull
	retries 3
	option redispatch
	maxconn 2000
	contimeout 5s
   
     clitimeout 60s

     srvtimeout 15s	
#front-end IP for consumers and producters

listen rabbitmq_cluster
	bind 0.0.0.0:5672
	
	mode tcp
	#balance url_param userid
	#balance url_param session_id check_post 64
	#balance hdr(User-Agent)
	#balance hdr(host)
	#balance hdr(Host) use_domain_only
	#balance rdp-cookie
	#balance leastconn
	#balance source //ip
	
	balance roundrobin
	
        server node1 127.0.0.1:5673 check inter 5000 rise 2 fall 2
        server node2 127.0.0.1:5674 check inter 5000 rise 2 fall 2

listen stats
	bind 172.16.98.133:8100
	mode http
	option httplog
	stats enable
	stats uri /rabbitmq-stats
	stats refresh 5s
```

启动HAproxy负载

```shell
/usr/local/haproxy/sbin/haproxy -f /etc/haproxy/haproxy.cfg
//查看haproxy进程状态
ps -ef | grep haproxy

访问如下地址对mq节点进行监控
http://172.16.98.133:8100/rabbitmq-stats
```

代码中访问mq集群地址，则变为访问haproxy地址:5672

# 参考

[^1]: [黑马程序员RabbitMQ全套教程，rabbitmq消息中间件到实战](https://www.bilibili.com/video/BV15k4y1k7Ep)
[^1]: [4、消息队列RabbitMQ](https://pan.baidu.com/s/1l2vljIWv-iQvaUFQFYsGZw?pwd=1438#list/path=%2F)，提取码：1438
[^2]: [Centos安装RabbitMQ超详细（必须收藏）](https://blog.csdn.net/weixin_47723549/article/details/124471613)
