# 概述

## 数据的持久化

持久化（persistence）：把数据保存到可掉电式存储设备中以供之后使用。大多数情况下，特别是企业级应用，数据持久化意味着将内存中的数据保存到硬盘上加以”固化”，而持久化的实现过程大多通过各种关系数据库来完成。

持久化的主要应用是将内存中的数据存储在关系型数据库中，当然也可以存储在磁盘文件、XML数据文件中。 

## Java中的数据存储技术

在Java中，数据库存取技术可分为如下几类：
- JDBC直接访问数据库。
- JDO（Java Data Object）技术。

- 第三方O/R工具，如Hibernate，MyBatis等。

JDBC是Java访问数据库的基石，JDO、Hibernate、MyBatis等只是更好的封装了JDBC。

## JDBC介绍

**JDBC（Java Database Connectivity）**是一个独立于特定数据库管理系统、通用的SQL数据库存取和操作的公共接口（一组API），定义了用来访问数据库的标准Java类库（`java.sql`、`javax.sql`），使用这些类库可以以一种标准的方法、方便地访问数据库资源。

JDBC为访问不同的数据库提供了一种统一的途径，为开发者屏蔽了一些细节问题。

JDBC的目标是使Java程序员使用JDBC可以连接任何提供了JDBC驱动程序的数据库系统，这样就使得程序员无需对特定的数据库系统的特点有过多的了解，从而大大简化和加快了开发过程。

如果没有JDBC，那么Java程序访问数据库时是这样的：

![没有JDBC时Java程序访问数据库的方式](资源\没有JDBC时Java程序访问数据库的方式.png)

有了JDBC，Java程序访问数据库时是这样的：


![有JDBC时Java程序访问数据库的方式](资源\有JDBC时Java程序访问数据库的方式.png)

总结如下：

![Java程序访问数据库的方式](资源\Java程序访问数据库的方式.png)

## JDBC体系结构

JDBC接口（API）包括两个层次：
- 面向应用的API：Java API，抽象接口，供应用程序开发人员使用（连接数据库，执行SQL语句，获得结果）。
-  面向数据库的API：Java Driver API，供开发商开发数据库驱动程序用。

JDBC是sun公司提供一套用于数据库操作的接口，Java程序员只需要面向这套接口编程即可。而不同的数据库厂商，需要针对这套接口，提供不同实现。不同的实现的集合，即为不同数据库的驱动。这就是面向接口编程。

## JDBC程序编写步骤

![JDBC程序编写步骤](资源\JDBC程序编写步骤.png)

补充：ODBC（Open Database Connectivity，开放式数据库连接），是微软在Windows平台下推出的。使用者在程序中只需要调用ODBC API，由ODBC驱动程序将调用转换成为对特定的数据库的调用请求。

# 获取数据库连接

## 要素一：`Driver`接口实现类

`java.sql.Driver`接口是所有JDBC驱动程序需要实现的接口。这个接口是提供给数据库厂商使用的，不同数据库厂商提供不同的实现。

在程序中不需要直接去访问实现了`Driver`接口的类，而是由驱动程序管理器类（`java.sql.DriverManager`）去调用这些`Driver`实现。
- Oracle的驱动：`oracle.jdbc.driver.OracleDriver`
- MySql的驱动： `com.mysql.jdbc.Driver`

### 加载与注册JDBC驱动

加载JDBC驱动需调用`Class`类的静态方法`forName`，向其传递要加载的JDBC驱动的类名。例如：`Class.forName("com.mysql.jdbc.Driver")`。

`DriverManager`类是驱动程序管理器类，负责管理驱动程序。使用`DriverManager.registerDriver(com.mysql.jdbc.Driver)`来注册驱动。

通常不用显式调用`DriverManager`类的`registerDriver`方法来注册驱动程序类的实例，因为`Driver`接口的驱动程序类都包含了静态代码块，在这个静态代码块中，会调用`DriverManager.registerDriver`方法来注册自身的一个实例。下图是MySQL的`Driver`实现类的源码：

```java
static {
    try {
        java.sql.DriverManager.registerDriver(new Driver());
    } catch (SQLException E) {
        throw new RuntimeException("Can't register driver!");
    }
}
```

## 要素二：URL

JDBC URL用于标识一个被注册的驱动程序，驱动程序管理器通过这个URL选择正确的驱动程序，从而建立到数据库的连接。

JDBC URL的标准由三部分组成，各部分间用`:`分隔：

- 协议：JDBC URL中的协议总是jdbc。
- 子协议：子协议用于标识一个数据库驱动程序。
- 子名称：一种标识数据库的方法。子名称可以依不同的子协议而变化，用子名称的目的是为了定位数据库提供足够的信息。包含主机名（对应服务端的IP地址），端口号，数据库名。例如，在URL `jdbc:mysql://localhost:3306/test`中，协议为`jdbc`，子协议为`mysql`，子名称为`//localhost:3306/test`。

几种常用数据库的JDBC URL书写方式如下：

- MySQL的连接URL编写方式：`jdbc:mysql://[主机名称]:[mysql服务端口号]/[数据库名称]?[参数]=[值]&[参数]=[值]...`。例如`jdbc:mysql://localhost:3306/test`

  、`jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8`（如果JDBC程序与服务器端的字符集不一致，会导致乱码，此时可以通过参数指定服务器端的字符集）、`jdbc:mysql://localhost:test/atguigu?user=root&password=123456`。

- Oracle 9i的连接URL编写方式：`jdbc:oracle:thin:@[主机名称]:[oracle服务端口号]:[数据库名称]`

  。例如：`jdbc:oracle:thin:@localhost:1521:test`。

- SQLServer的连接URL编写方式：`jdbc:sqlserver://[主机名称]:[sqlserver服务端口号]:DatabaseName=[数据库名称]`。例如`jdbc:sqlserver://localhost:1433:DatabaseName=test`。

## 要素三：用户名和密码

可以（在子名称中）使用`user=[用户名]&password=[密码]`的方式告诉数据库用户名与密码。在程序中，可以调用`DriverManager`类的`getConnection`方法建立到数据库的连接。

## 开发

### 开发环境

- 语言：Java 8
- IDE：IDEA 2020.3.4
- 构建工具：Maven 3.6.3
- MySQL版本：MySQL 5.7.26

### 引入依赖

创建Maven工程，打包方式设置为jar，引入依赖：

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.29</version>
</dependency>
```

### 创建数据库

创建数据库`test`。

### 连接数据库

封装JDBC数据库连接操作如下：

```java
package com.example.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

public class JDBCUtils {

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/test";
        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            Properties info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "root");
            DriverManager.registerDriver(driver);
            return driver.connect(url, info);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
```

测试：

```java
package com.example.jdbc;

import com.example.jdbc.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class JDBCUtilsTest {

    @Test
    public void testConnection() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

上述连接操作代码中显式出现了第三方数据库的API。可以使用反射实例化`Driver`，不在代码中体现第三方数据库的API，从而体现面向接口编程思想：

```java
// ...
// 修改Driver的初始化方式（并添加相应的异常说明）
Driver driver =(Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
// ...
```

也可以使用`DriverManager`连接数据库：

```java
package com.example.jdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {

    public static Connection getConnection() {
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";
        try {
            Driver driver = (Driver) Class.forName(driverName).newInstance();
            DriverManager.registerDriver(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
```

因为在`DriverManager`的源码中已经存在静态代码块，实现了驱动的注册，因此不必显式的注册驱动：

```java
package com.example.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {

    public static Connection getConnection() {
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
```

下面使用配置文件保存配置信息，在代码中加载配置文件，完成数据库的连接。

在src/main/resources目录下，新建jdbc.properties配置文件：

```properties
driverName=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/test
user=root
password=root
```

连接数据库：

```java
package com.example.jdbc.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JDBCUtils {

    public static Connection getConnection() {
        try (
                InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("jdbc.properties")
        ) {
            Properties pros = new Properties();
            pros.load(is);
            String driverName = pros.getProperty("driverName");
            String url = pros.getProperty("url");
            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            Class.forName(driverName);
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
```

使用配置文件的好处：

- 实现了代码和数据的分离，如果需要修改配置信息，直接在配置文件中修改，不需要深入代码。
- 如果修改了配置信息，省去重新编译/打包的过程。

# CRUD操作

数据库连接被用于向数据库服务器发送命令和SQL语句，并接受数据库服务器返回的结果。其实一个数据库连接就是一个Socket连接。

在`java.sql`包中有 3 个接口分别定义了对数据库的调用的不同方式：
- `Statement`：用于执行静态SQL语句并返回它所生成结果的对象。 
- `PrepatedStatement`：SQL语句被预编译并存储在此对象中，可以使用此对象多次高效地执行该语句。
- `CallableStatement`：用于执行SQL存储过程。

![3个接口定义对数据库的调用](资源\3个接口定义对数据库的调用.png)

### `Statement`

引入依赖：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

在数据库`test`中创建表`user`，包含一个`int`类型的`id`（自增主键）、一个`varchar`类型的`username`以及一个`varchar`类型的`password`，并创建对应的实体类：

```java
package com.example.jdbc.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private int id;

	private String username;
	
	private String password;

}
```

通过调用`Connection`对象的`createStatement`方法创建`Statement`对象。该对象用于执行静态的SQL语句，并且返回执行结果。

`Statement`接口中定义了下列方法用于执行SQL语句：

- `int excuteUpdate(String sql)`：执行更新操作（`INSERT`、`UPDATE`、`DELETE`）。
- `ResultSet executeQuery(String sql)`：执行查询操作（`SELECT`）。

测试：

```java
package com.example.jdbc;

import com.example.jdbc.pojo.User;
import com.example.jdbc.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;

public class StatementTest {

    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("用户名：");
        String username = scanner.nextLine();
        System.out.print("密码：");
        String password = scanner.nextLine();
        String sql = "select username, password from user where username = '" + username + "' and password = '" + password + "'";
        User user = get(sql, User.class);
        if (user != null) {
            System.out.println("登陆成功!");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }

    public <T> T get(String sql, Class<T> clazz) {
        try (
                Connection connection = JDBCUtils.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnVal = resultSet.getObject(columnLabel);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
```

注意，这里用到了对象关系映射（Object Relation Mapping，ORM）思想：

- 一个数据表对应一个`Java`类。
- 表中的一条记录对应`Java`类的一个对象。
- 表中的一个字段对应`Java`类的一个属性。

注：如果无法在控制台中进行输入输出交互，则在IDEA的导航找到Help $\rightarrow$ Edit Custom VM Options…，然后在弹出的对话框文件中追加：`-Deditable.java.test.console=true`，并重启IDEA。

使用`Statement`操作数据表存在弊端：

- 存在拼串操作，繁琐。
- 存在SQL注入问题。SQL注入是利用某些系统没有对用户输入的数据进行充分的检查的情形，而在用户输入数据中注入非法的SQL语句段或命令，从而利用系统的SQL引擎完成恶意行为的做法。例如，对于以上测试的SQL语句，如果输入的用户名为`a' OR 1 = `且密码为` OR '1' = '1`，则SQL语句为：`SELECT username, password FROM user WHERE username = 'a' OR 1 = ' AND password = ' OR '1' = '1'`，条件部分永远为真，这样登录将永远成功（只要表中有数据），且表中所有记录都将暴露。对于Java而言，要防范SQL注入，只要用 `PreparedStatement`（从`Statement`扩展而来）取代`Statement`就可以了。

综上：

![使用PreparedStatement替换Statement](资源\使用PreparedStatement替换Statement.png)

### `PreparedStatement`

可以通过调用`Connection`对象的`preparedStatement(String sql)`方法获取`PreparedStatement`对象。`PreparedStatement`接口是`Statement`的子接口，它表示一条预编译过的SQL语句。

`PreparedStatement`对象所代表的SQL语句中的参数用问号（`?`）来表示，调用`PreparedStatement`对象相应的`set`方法来设置这些参数.。`set`方法有两个参数，第一个参数是要设置的SQL语句中的参数的索引（从1开始），第二个是设置的SQL语句中的参数的值。

`PreparedStatement`相比于`Statement`的优势包括：

- 代码的可读性和可维护性更高。
- `PreparedStatement`能最大可能提高性能。DBServer会对预编译语句提供性能优化。因为预编译语句有可能被重复调用，所以语句在被DBServer的编译器编译后的执行代码被缓存下来，那么下次调用时只要是相同的预编译语句就不需要编译，只要将参数直接传入编译过的语句执行代码中就会得到执行。在`Statement`语句中,即使是相同操作但因为数据内容不一样,所以整个语句本身不能匹配,没有缓存语句的意义。事实是没有数据库会对普通语句编译后的执行代码缓存。这样每执行一次都要对传入的语句编译一次。
- `PreparedStatement`可以防止SQL注入。

下面使用`PreparedStatement`实现数据库的通用操作。

在`JDBCUtils`类中添加两个方法：

```java
// ...
import java.lang.reflect.Field;
import java.sql.*;
// ...

public static void update(Connection connection, String sql, Object... args) {
    try (
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        for (int i = 0; i < args.length; ++i) {
            statement.setObject(i + 1, args[i]);
        }
        statement.execute();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public static <T> T queryForObject(Connection connection, Class<T> clazz, String sql, Object... args) {
    try (
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        for (int i = 0; i < args.length; ++i) {
            statement.setObject(i + 1, args[i]);
        }
        try (
            ResultSet resultSet = statement.executeQuery()
        ) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; ++i) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnVal = resultSet.getObject(i + 1);
                    try {
                        Field field = clazz.getDeclaredField(columnLabel);
                        field.setAccessible(true);
                        field.set(t, columnVal);
                    } catch (NoSuchFieldException ignored) {}
                }
                return t;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
```

在`JDBCUtilsTest`类中测试：

```java
// ...
import com.example.jdbc.pojo.User;
// ...

@Test
public void testUpdate() {
    try (
        Connection connection = JDBCUtils.getConnection()
    ) {
        String sql = "insert into user (username, password) values (?, ?)";
        JDBCUtils.update(connection, sql, "Alice", "123456");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Test
public void testQueryForObject() {
    try (
        Connection connection = JDBCUtils.getConnection()
    ) {
        String sql = "select id, username, password from user where id = ?";
        System.out.println(JDBCUtils.queryForObject(connection, User.class, sql, "1"));

    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

### `ResultSet`

查询需要调用`PreparedStatement`的`executeQuery`方法，查询结果是一个`ResultSet`对象。

`ResultSet`对象以逻辑表格的形式封装了执行数据库操作的结果集，`ResultSet`接口由数据库厂商提供实现。

`ResultSet`返回的实际上就是一张数据表，有一个指针指向数据表的第一条记录的前面。

`ResultSet`对象维护了一个指向当前数据行的游标，初始的时候，游标在第一行之前，可以通过`ResultSet`对象的`next`方法移动到下一行。调用`next`方法会检测下一行是否有效，若有效，该方法返回`true`，且指针下移，相当于`java.util.Iterator`对象的`hasNext`和`next`方法的结合体。

当指针指向一行时, 可以通过调用相应的`get`方法（参数列的索引或列名）获取每一列的值。例如: `getInt(1)`、`getString("name")`。需要注意的是，Java与数据库交互涉及到的相关Java API中的索引都从1开始。

### `ResultSetMetaData`

可以通过`ResultSet`的`getMetaData`对象得到对应的`ResultSetMetaData`对象。`ResultSetMetaData`可用于获取关于`ResultSet`对象中列的类型和属性信息的对象，一些方法如下：

- `getColumnName(int column)`：获取指定列的名称。
- `getColumnLabel(int column)`：获取指定列的别名。

- `getColumnCount()`：返回当前`ResultSet`对象中的列数。 
- `getColumnTypeName(int column)`：检索指定列的特定于数据库的类型名称。 
- `getColumnDisplaySize(int column)`：指示指定列的最大标准宽度，以字符为单位。 
- `isNullable(int column)`：指示指定列中的值是否可以为`NULL`。 
- `isAutoIncrement(int column)`：指示是否自动为指定列进行编号，这样这些列仍然是只读的。 

### 资源的释放

操作完成后，需要释放相关的`ResultSet`、` Statement`、`Connection`对象（调用`close`方法）。数据库连接（`Connection`）是非常稀有的资源，用完后必须马上释放，如果`Connection`不能及时正确的关闭将导致系统宕机。`Connection`的使用原则是尽量晚创建，尽量早释放。

# 类型

Java与对应数据类型转换表如下：

| Java类型             | SQL类型                          |
| -------------------- | -------------------------------- |
| `boolean`            | `BIT`                            |
| `byte`               | `TINYINT`                        |
| `short`              | `SMALLINT`                       |
| `int`                | `INTEGER`                        |
| `long`               | `BIGINT`                         |
| `String`             | `CHAR`、`VARCHAR`、`LONGVARCHAR` |
| `byte`数组           | `BINARY`  ,    `VAR BINARY`      |
| `java.sql.Date`      | `DATE`                           |
| `java.sql.Time`      | `TIME`                           |
| `java.sql.Timestamp` | `TIMESTAMP`                      |

## 操作BLOB类型字段

MySQL中，`BLOB`是一个二进制大型对象，是一个可以存储大量数据的容器，它能容纳不同大小的数据。MySQL一共有四种`BLOB`类型，除了在存储的最大信息量上不同外，他们是等同的：

| 类型         | 最大大小（字节） |
| ------------ | ---------------- |
| `TinyBlob`   | 255              |
| `Blob`       | 65K              |
| `MediumBlob` | 16M              |
| `LongBlob`   | 4G               |

实际使用中根据需要存入的数据大小定义不同的`BLOB`类型。另外，要注意，如果存储的文件过大，数据库的性能会下降。

如果在指定了相关的`Blob`类型以后，还报错：`xxx too large`，那么在MySQL的安装目录下，在my.ini文件加上如下的配置参数（只需要放在最后一行即可）：

- `max_allowed_packet=16M`（或其他数值）。同时注意：修改了my.ini文件之后，需要重新启动MySQL服务。

插入`BLOB`类型的数据必须使用`PreparedStatement`，因为`BLOB`类型的数据无法使用字符串拼接。

下面在`user`表中添加更多的字段，演示各种类型的使用。

在`user`表中添加一个`bit`类型的`sex`、一个`date`类型的`birthday`、一个`varchar`类型的`email`，以及一个`MediumBlob`类型的字段`avatar`，用于存储用户图像。

修改`User`类：

```java
package com.example.jdbc.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private int id;

	private String username;

	private String password;

	private boolean sex;

	private Date birthday;

	private String email;

}
```

然后修改`JDBCUtilsTest`测试类如下：

```java
// ...
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
// ...

@Test
public void testUpdate() {
    try (
        Connection connection = JDBCUtils.getConnection()
    ) {
        String sql = "insert into user (username, password, sex, birthday, email, avatar) values (?, ?, ?, ?, ?, ?)";
        Path path = Paths.get("src", "main", "resources", "avatar", "1.jpg");
        JDBCUtils.update(connection, sql, "Alice", "123456", false, new Date(System.currentTimeMillis()), "Alice@qq.com", new FileInputStream(path.toString()));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

`date`类型的字段也可以通过字符串等类型传输，只要日期格式正确即可。或者可以通过`SimpleDateFormat`类将特定格式的日期转换为`Date`：

```java
// ...
import java.text.SimpleDateFormat;
// ...

SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
java.util.Date date = dateFormat.parse("2000-01-01");
JDBCUtils.update(connection, sql, "Alice", "123456", false, new Date(date.getTime()), "Alice@qq.com", new FileInputStream(path.toString()));
```

`BLOB`类型的字段可以通过流的方式传输；可以通过`PreparedStatement`的`setBlob`方法专门设置`BLOB`类型字段。

下面执行查询操作，并将`avatar`字段对应的数据存储到本地：

```java
// ...
import java.sql.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
// ...

@Test
public void testQuery() {
    String sql = "select id, username, password, sex, birthday, email, avatar from user where id = ?";
    try (
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        statement.setInt(1, 1);
        try (
            ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                boolean sex = resultSet.getBoolean(4);
                Date birthday = resultSet.getDate(5);
                String email = resultSet.getString(6);
                User user = new User(id, username, password, sex, birthday, email);
                System.out.println(user);
                Blob avatar = resultSet.getBlob(7);
                Path path = Paths.get("src", "main", "resources", "avatar", "2.jpg");
                try (
                    InputStream is = avatar.getBinaryStream();
                    OutputStream os = new FileOutputStream(path.toString())
                ) {
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    os.write(buffer);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

也可以使用`String`接受`date`类型字段，然后转换为`java.sql.Date`：

```java
String birthdayString = resultSet.getString(5);
Date birthday = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(birthdayString).getTime());
```

# 批量插入

当需要成批插入或者更新记录时，可以采用Java的批量更新机制，这一机制允许将多条语句一次性提交给数据库批量处理。通常情况下比单独提交处理更有效率。

JDBC的批量处理涉及（`Statement`或`PreparedStatement`类中的）三个方法：
- `addBatch(String)`：添加需要批量处理的SQL语句或参数。
- `executeBatch()`：执行批量处理语句。
- `clearBatch()`：清空缓存的数据。

通常我们会遇到两种批量执行SQL语句的情况：
- 多条SQL语句的批量处理；
- 一个SQL语句的批量传参；

在数据库`test`中创建表`goods`，它包含一个`int`类型的`id`（自增主键）、一个`varchar`类型的`name`。下面向表中插入100000条数据。注意，`update`与`delete`本身就有批量操作的效果。

一种方式是使用`Statement`（注意每次操作前清空表）：

```java
package com.example.jdbc;

import com.example.jdbc.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

public class BatchTest {

    @Test
    public void batchInsertByStatement() {
        try (
                Connection connection = JDBCUtils.getConnection();
                Statement statement = connection.createStatement()
        ) {
            long start = System.currentTimeMillis();
            for (int i = 1; i <= 100000; ++i) {
                String sql = "insert into goods (name) values (concat('name_'" + ", '" + i + "'))";
                statement.executeUpdate(sql);
            }
            System.out.println("花费的时间为：" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

另一种方式是使用`PreparedStatement`：

```java
// ...
import java.sql.PreparedStatement;
// ...

@Test
public void batchInsertByPreparedStatement() {
    String sql = "insert into goods (name) values (?)";
    try (
            Connection connection = JDBCUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; ++i) {
            statement.setString(1, "name_" + i);
            statement.executeUpdate();
        }
        System.out.println("花费的时间为：" + (System.currentTimeMillis() - start));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

理论上，由于`PreparedStatement`会缓存预编译语句，因此这里插入的效率比`Statement`高。

MySQL服务器默认是关闭批处理的，我们需要通过一个参数，让MySQL开启批处理的支持：`?rewriteBatchedStatements=true`，需要将其写在配置文件的`url`后面（同时确保mysql-connector-java不要过老，例如5.1.7版本的不支持批量操作，5.1.37版本的支持）。

```java
@Test
public void batchInsertByPreparedStatement() {
    String sql = "insert into goods (name) values (?)";
    try (
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; ++i) {
            statement.setString(1, "name_" + i);
            statement.addBatch();
            if (i % 1000 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
        }
        System.out.println("花费的时间为：" + (System.currentTimeMillis() - start));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

这样会大大提升插入的效率。

关闭连接的自动提交，可以进一步提升插入的效率：

```java
@Test
public void batchInsertByPreparedStatement() {
    String sql = "insert into goods (name) values (?)";
    try (
        Connection connection = JDBCUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
        connection.setAutoCommit(false);
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; ++i) {
            statement.setString(1, "name_" + i);
            statement.addBatch();
            if (i % 1000 == 0) {
                statement.executeBatch();
                statement.clearBatch();
            }
        }
        connection.commit();
        System.out.println("花费的时间为：" + (System.currentTimeMillis() - start));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

# 数据库事务

事务是一组逻辑操作单元，使数据从一种状态变换到另一种状态。

事务处理（事务操作）的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操作时，要么所有的事务都被提交，那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚到最初状态。

为确保数据库中数据的一致性，数据的操纵应当是离散的成组的逻辑单元：当它全部完成时，数据的一致性可以保持，而当这个单元中的一部分操作失败，整个事务应全部视为错误，所有从起始点以后的操作应全部回退到开始状态。 

数据一旦提交，就不可回滚。数据在以下情况下会提交：

- DDL操作一旦执行，都会自动提交。`set autocommit = false`对DDL操作无效。
- 当一个连接对象被创建时，默认情况下是自动提交事务：每次执行一个 SQL 语句时，如果执行成功，就会向数据库自动提交，而不能回滚。
- 关闭数据库连接，数据就会自动的提交。如果多个操作，每个操作使用的是自己单独的连接，则无法保证事务。即同一个事务的多个操作必须在同一个连接下。

JDBC程序中通过以下操作，让让多个 SQL 语句作为一个事务执行：

- 调用`Connection`对象的 `setAutoCommit(false)`以取消自动提交事务。
- 在所有的SQL语句都成功执行后，调用`commit()`方法提交事务。
- 在出现异常时，调用`rollback()`方法回滚事务。

若此时`Connection`没有被关闭，还可能被重复使用，则需要恢复其自动提交状态`setAutoCommit(true)`。尤其是在使用数据库连接池技术时，执行`close()`方法前，建议恢复自动提交状态。

在表`user`中添加一个字段`int`类型的字段`balance`，并添加相应记录，并模拟转账操作：

```java
package com.example.jdbc;

import com.example.jdbc.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionTest {

    @Test
    public void testTransfer() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            connection.setAutoCommit(false);
            String sql1 = "update user set balance = balance-100 where id = ?";
            JDBCUtils.update(connection, sql1, "1");
            int i = 10 / 0;  // 模拟网络异常
            String sql2 = "update user set balance = balance+100 where id = ?";
            JDBCUtils.update(connection, sql2, "2");
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // 恢复每次DML操作的自动提交功能
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
```

由于抛出异常，事务回滚，转账未完成，表中记录没有改变。如果一开始没有设置`connection.setAutoCommit(false)`，或者捕获网络异常后没有执行`connection.rollback()`，则不会回滚。

一个事务与其他事务隔离的程度称为隔离级别。数据库规定了多种事务隔离级别, 不同隔离级别对应不同的干扰程度, 隔离级别越高, 数据一致性就越好, 但并发性越弱。下面通过代码演示隔离级别的使用。

在`User`类中添加一个属性：

```java
private int balance;
```

在`TransactionTest`类中测试：

```java
// ...
import com.example.jdbc.pojo.User;
// ...

@Test
public void testIsolationRead() {
    try (
            Connection connection = JDBCUtils.getConnection()
    ) {
        System.out.println("修改前连接的隔离级别：" + connection.getTransactionIsolation());
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        System.out.println("修改后连接的隔离级别：" + connection.getTransactionIsolation());
        String sql = "select balance from user where id = ?";
        User user = JDBCUtils.queryForObject(connection, User.class, sql, 1);
        System.out.println(user);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Test
public void testIsolationUpdate() {
    try (
            Connection connection = JDBCUtils.getConnection()
    ) {
        connection.setAutoCommit(false);
        String sql = "update user set balance = balance + 1000 where id = ?";
        JDBCUtils.update(connection, sql, 1);
        Thread.sleep(20000);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

首先执行`testIsolationRead`，输出的`user.balance`为$A$，然后执行`testIsolationUpdate`，并在其运行过程中再次执行`testIsolationRead`，输出的`user.balance`为$A+1000$，当`testIsolationUpdate`执行完毕，再执行`testIsolationRead`，输出的`user.balance`为$A$，可见在这个过程中发生了脏读。要想避免脏读，只要将隔离级别设置为`Connection.TRANSACTION_READ_COMMITTED`或更高级别即可。

# DAO及相关实现类

数据访问对象（Data Access Object，DAO）是访问数据信息的类和接口，包括了对数据的CRUD（Create、Retrival、Update、Delete），而不包含任何业务相关的信息。有时也称作BaseDAO。

DAO的作用：为了实现功能的模块化，更有利于代码的维护和升级。

引入依赖：

```xml
<dependency>
    <groupId>commons-dbutils</groupId>
    <artifactId>commons-dbutils</artifactId>
    <version>1.7</version>
</dependency>
```

下面是一个表示用户的类（`user`表的字段要与类中的属性对应）：

```java
package com.example.jdbc.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private int id;

	private String username;

	private String password;

	private boolean sex;

	private Date birthday;

	private String email;

}
```

下面是一个表示页码的类：

```java
package com.example.jdbc.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

	private int pageNo;  // 当前页

	private int pageSize;  // 每页显示的记录数

	private List<T> list;  // 每页查到的记录存放的集合

	private int totalRecord;  // 总记录数，通过查询数据库得到

	public void startPage(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

}
```

下面定义一个用来被继承的对数据库进行基本操作的`BaseDao`：

```java
package com.example.jdbc.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public abstract class BaseDao<T> {

    private final QueryRunner queryRunner;

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public BaseDao() {
        this.queryRunner = new QueryRunner();
        Class<?> clazz = this.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = parameterizedType.getActualTypeArguments();
        this.type = (Class<T>) types[0];
    }

    /**
     * 通用的增删改操作
     */
    public int update(Connection connection, String sql, Object... params) {
        int count = 0;
        try {
            count = queryRunner.update(connection, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 查询返回一个对象，对应一条记录
     */
    public T getBean(Connection connection, String sql, Object... params) {
        T t = null;
        try {
            t = queryRunner.query(connection, sql, new BeanHandler<>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 查询返回对象集合，对应记录集合
     */
    public List<T> getBeanList(Connection connection, String sql, Object... params) {
        List<T> list = null;
        try {
            list = queryRunner.query(connection, sql, new BeanListHandler<>(type), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Object getValue(Connection connection, String sql, Object... params) {
        Object object = null;
        try {
            object = queryRunner.query(connection, sql, new ScalarHandler<>(), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

}
```

下面是`UserDao`的接口及其实现类：

```java
package com.example.jdbc.dao;

import com.example.jdbc.pojo.Page;
import com.example.jdbc.pojo.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {

	int insertUser(Connection connection, User user);

	int deleteUserById(Connection connection, int id);

	int updateUserById(Connection connection, User user);

	User getUserById(Connection connection, int id);

	List<User> getAllUsers(Connection connection);

	int getCount(Connection connection);

	Page<User> getPageUsers(Connection connection, Page<User> page);

}
```

```java
package com.example.jdbc.dao.impl;

import com.example.jdbc.dao.BaseDao;
import com.example.jdbc.dao.UserDao;
import com.example.jdbc.pojo.Page;
import com.example.jdbc.pojo.User;

import java.sql.Connection;
import java.util.List;


public class UserDaoImpl extends BaseDao<User> implements UserDao {

   @Override
   public int insertUser(Connection connection, User user) {
      String sql = "insert into user (username, password, sex, birthday, email) values (?, ?, ?, ?, ?)";
      return update(connection, sql, user.getUsername(), user.getPassword(), user.isSex(), user.getBirthday(), user.getEmail());
   }

   @Override
   public int deleteUserById(Connection connection, int id) {
      String sql = "delete from user where id = ?";
      return update(connection, sql, id);
   }

   @Override
   public int updateUserById(Connection connection, User user) {
      String sql = "update user set username = ?, password = ?, sex = ?, birthday = ?, email = ? where id = ?";
      return update(connection, sql, user.getUsername(), user.getPassword(), user.isSex(), user.getBirthday(), user.getEmail(), user.getId());
   }

   @Override
   public User getUserById(Connection connection, int id) {
      String sql = "select id, username, password, sex, birthday, email from user where id = ?";
      return getBean(connection, sql, id);
   }

   @Override
   public List<User> getAllUsers(Connection connection) {
      String sql = "select id, username, password, sex, birthday, email from user";
      return getBeanList(connection, sql);
   }

   @Override
   public int getCount(Connection connection) {
      String sql = "select count(*) from user";
      return (int) (long) getValue(connection, sql);
   }

   @Override
   public Page<User> getPageUsers(Connection connection, Page<User> page) {
      String sql1 = "select count(*) from user";
      int count = (int) (long) getValue(connection, sql1);
      page.setTotalRecord(count);
      String sql2 = "select id, username, password, sex, birthday, email from user limit ?, ?";
      List<User> users = getBeanList(connection, sql2, (page.getPageNo() - 1) * page.getPageSize(), page.getPageSize());
      page.setList(users);
      return page;
   }

}
```

测试：

```java
package com.example.jdbc;

import com.example.jdbc.dao.UserDao;
import com.example.jdbc.dao.impl.UserDaoImpl;
import com.example.jdbc.pojo.Page;
import com.example.jdbc.pojo.User;
import com.example.jdbc.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserDaoTest {

    private final UserDao userDao = new UserDaoImpl();

    @Test
    public void testInsertUser() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime());
            User user = new User(0, "Alice", "123456", false, date, "Alice@qq.com");
            int count = userDao.insertUser(connection, user);
            System.out.println("添加的数量：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteUserById() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            int count = userDao.deleteUserById(connection, 1);
            System.out.println("删除的数量：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateUserById() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            Date date = new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01").getTime());
            User user = new User(1, "Bob", "abcdef", false, date, "Bob@qq.com");
            int count = userDao.updateUserById(connection, user);
            System.out.println("更新的数量：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserById() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            User user = userDao.getUserById(connection, 1);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllUsers() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            List<User> users = userDao.getAllUsers(connection);
            users.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetCount() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            int count = userDao.getCount(connection);
            System.out.println("用户总量：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPageUsers() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            Page<User> page = new Page<>();
            page.startPage(1, 4);
            Page<User> userPage = userDao.getPageUsers(connection, page);
            System.out.println(userPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

# 数据库连接池

## JDBC数据库连接池的必要性

在使用开发基于数据库的web程序时，传统的模式基本是按以下步骤：　　
- 在主程序（如servlet、beans）中建立数据库连接。
- 进行SQL操作。
- 断开数据库连接。

这种模式开发，存在的问题:
- 普通的JDBC数据库连接使用`DriverManager`来获取，每次向数据库建立连接的时候都要将`Connection`加载到内存中，再验证用户名与密码。需要数据库连接的时候，就向数据库要求一个，执行完成后再断开连接。这样的方式将会消耗大量的资源和时间。数据库的连接资源并没有得到很好的重复利用。若同时有大量用户在线，频繁的进行数据库连接操作将占用很多的系统资源，严重的甚至会造成服务器的崩溃。
- 对于每一次数据库连接，使用完后都得断开。否则，如果程序出现异常而未能关闭，将会导致数据库系统中的内存泄漏，最终将导致重启数据库。
- 这种开发不能控制被创建的连接对象数，系统资源会被毫无顾及的分配出去，如连接过多，也可能导致内存泄漏，服务器崩溃。 

## 数据库连接池技术

为解决传统开发中的数据库连接问题，可以采用数据库连接池技术。

数据库连接池的基本思想是为数据库连接建立一个“缓冲池”。预先在缓冲池中放入一定数量的连接，当需要建立数据库连接时，只需从“缓冲池”中取出一个，使用完毕之后再放回去。

数据库连接池负责分配、管理和释放数据库连接，它允许应用程序重复使用一个现有的数据库连接，而不是重新建立一个。

数据库连接池在初始化时将创建一定数量的数据库连接放到连接池中，这些数据库连接的数量是由最小数据库连接数来设定的。无论这些数据库连接是否被使用，连接池都将一直保证至少拥有这么多的连接数量。连接池的最大数据库连接数量限定了这个连接池能占有的最大连接数，当应用程序向连接池请求的连接数超过最大连接数量时，这些请求将被加入到等待队列中。

数据库连接池技术的优点：

- 资源重用

  - 由于数据库连接得以重用，避免了频繁创建，释放连接引起的大量性能开销。在减少系统消耗的基础上，另一方面也增加了系统运行环境的平稳性。


- 更快的系统反应速度

  - 数据库连接池在初始化过程中，往往已经创建了若干数据库连接置于连接池中备用。此时连接的初始化工作均已完成。对于业务请求处理而言，直接利用现有可用连接，避免了数据库连接初始化和释放过程的时间开销，从而减少了系统的响应时间。


- 新的资源分配手段

  - 对于多应用共享同一数据库的系统而言，可在应用层通过数据库连接池的配置，实现某一应用最大可用数据库连接数的限制，避免某一应用独占所有的数据库资源。


- 统一的连接管理，避免数据库连接泄漏

  - 在较为完善的数据库连接池实现中，可根据预先的占用超时设定，强制回收被占用连接，从而避免了常规数据库连接操作中可能出现的资源泄露。


## 多种开源的数据库连接池

JDBC的数据库连接池使用`javax.sql.DataSource`来表示，`DataSource`只是一个接口，该接口通常由服务器（Weblogic、WebSphere、Tomcat等）提供实现，也有一些开源组织提供实现：
- DBCP是Apache提供的数据库连接池。Tomcat服务器自带DBCP数据库连接池。速度相对C3P0较快，但因自身存在BUG，Hibernate3已不再提供支持。
- C3P0是一个开源组织提供的一个数据库连接池，速度相对较慢，稳定性还可以。Hibernate官方推荐使用。
- Proxool是sourceforge下的一个开源项目数据库连接池，有监控连接池状态的功能，稳定性较C3P0差一点。
- BoneCP是一个开源组织提供的数据库连接池，速度快。
- Druid是阿里提供的数据库连接池，据说是集DBCP 、C3P0 、Proxool优点于一身的数据库连接池，但是速度不确定是否有BoneCP快。

`DataSource`通常被称为数据源，它包含连接池和连接池管理两个部分，习惯上也经常把`DataSource`称为连接池。

`DataSource`用来取代`DriverManager`来获取`Connection`，获取速度快，同时可以大幅度提高数据库访问速度。

注意：
- 数据源和数据库连接不同，数据源无需创建多个，它是产生数据库连接的工厂，因此整个应用只需要一个数据源即可。
- 当数据库访问结束后，程序还是像以前一样关闭数据库连接：`connection.close()`。 但`connection.close()`并没有关闭数据库的物理连接，它仅仅把数据库连接释放，归还给了数据库连接池。

### C3P0数据库连接池

添加依赖：

```xml
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.5</version>
</dependency>
```

测试：

```java
package com.example.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.jupiter.api.Test;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

public class DataSourceTest {

    @Test
    public void getC3P0Connection() throws SQLException, PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setInitialPoolSize(10);
        System.out.println(dataSource.getConnection());
    }

}
```

推荐使用C3P0数据库连接池的配置文件方式，获取数据库的连接。

在src/main/resources目录下，创建c3p0-config.xml配置文件。注意该文件的路径与名字是固定的。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<c3p0-config>
    <named-config name="exampleC3P0">
        <!-- 获取连接的4个基本信息 -->
        <property name="user">root</property>
        <property name="password">root</property>
        <property name="jdbcUrl">jdbc:mysql://localhost:3306/test</property>
        <property name="driverClass">com.mysql.cj.jdbc.Driver</property>

        <!-- 涉及到数据库连接池的管理的相关属性的设置 -->

        <!-- 若数据库中连接数不足时，一次向数据库服务器申请多少个连接 -->
        <property name="acquireIncrement">5</property>
        <!-- 初始化数据库连接池时连接的数量 -->
        <property name="initialPoolSize">5</property>
        <!-- 数据库连接池中的最小的数据库连接数 -->
        <property name="minPoolSize">5</property>
        <!-- 数据库连接池中的最大的数据库连接数 -->
        <property name="maxPoolSize">10</property>
        <!-- C3P0 数据库连接池可以维护的 Statement 的个数 -->
        <property name="maxStatements">20</property>
        <!-- 每个连接同时可以使用的 Statement 对象的个数 -->
        <property name="maxStatementsPerConnection">5</property>
    </named-config>
</c3p0-config>
```

然后修改测试：

```java
@Test
public void getC3P0Connection() throws SQLException {
    ComboPooledDataSource dataSource = new ComboPooledDataSource("exampleC3P0");
    System.out.println(dataSource.getConnection());
}
```

### DBCP数据库连接池

`DBCP`是Apache软件基金组织下的开源连接池实现，该连接池依赖该组织下的另一个开源系统：Common-pool。

Tomcat的连接池正是采用该连接池来实现的。该数据库连接池既可以与应用服务器整合使用，也可由应用程序独立使用。

添加依赖：

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
    <version>2.9.0</version>
</dependency>
```

在`DataSourceTest`类中测试：

```java
// ...
import org.apache.commons.dbcp2.BasicDataSource;
// ...

@Test
public void getDBCPConnection() throws SQLException {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/test");
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    dataSource.setInitialSize(10);
    System.out.println(dataSource.getConnection());
}
```

这里同样可以采用读取配置文件的方式建立连接：

```java
// ...
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;
// ...

@Test
public void getDBCPConnection() throws Exception {
    InputStream is = DataSourceTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
    Properties pros = new Properties();
    pros.load(is);
    DataSource dataSource = BasicDataSourceFactory.createDataSource(pros);
    System.out.println(dataSource.getConnection());
}
```

运行前，需要在src/main/resources目录下，创建dbcp.properties配置文件：

```properties
driverClassName=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true&useServerPrepStmts=false
username=root
password=root

initialSize=10
```

### Druid（德鲁伊）数据库连接池

Druid是阿里巴巴开源平台上一个数据库连接池实现，它结合了C3P0、DBCP、Proxool等DB池的优点，同时加入了日志监控，可以很好的监控DB池连接和SQL的执行情况，可以说是针对监控而生的DB连接池，可以说是目前最好的连接池之一。

下面使用Druid连接池重写`JDBCUtils`的`getConnection`方法。

添加依赖：

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.9</version>
</dependency>
```

在src/main/resources目录下，创建druid.properties配置文件：

```properties
url=jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true
username=root
password=root
driverClassName=com.mysql.cj.jdbc.Driver

initialSize=10
maxActive=20
maxWait=1000
filters=wall
```

```java
// ...
import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
// ...

public static Connection getConnection() {
    if (dataSource == null) {
        try (
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties")
        ) {
            Properties pros = new Properties();
            pros.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(pros);
            return getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } else {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

# Apache-DBUtils实现CRUD操作

`commons-dbutils`是Apache组织提供的一个开源JDBC工具类库，它是对JDBC的简单封装，学习成本极低，并且使用DBUtils能极大简化JDBC编码的工作量，同时也不会影响程序的性能。

API介绍：

- `org.apache.commons.dbutils.QueryRunner`：提供数据库操作的一系列重载的`update`和`query`操作。该类简单化了SQL查询，它与`ResultSetHandler`组合在一起使用可以完成大部分的数据库操作，能够大大减少编码量。
- `org.apache.commons.dbutils.ResultSetHandler`：此接口用于处理数据库查询操作得到的结果集（`ResultSet`），将数据按要求转换为另一种形式。不同的结果集的情形由其不同的子类来实现。它提供了一个方法`handler`将`ResultSet`转变为其他对象。接口的主要实现类如下：
  - 接口的主要实现类：
    - `ArrayHandler`：把结果集中的第一行数据转成对象数组。
    - `ArrayListHandler`：把结果集中的每一行数据都转成一个数组，再存放到`List`中。
    - `BeanHandler`：将结果集中的第一行数据封装到一个对应的JavaBean实例中。
    - `BeanListHandler`：将结果集中的每一行数据都封装到一个对应的JavaBean实例中，存放到`List`里。
    - `ColumnListHandler`：将结果集中某一列的数据存放到`List`中。
    - `KeyedHandler`：将结果集中的每一行数据都封装到一个`Map`里，把这些`Map`再存到一个`Map`里。
    - `MapHandler`：将结果集中的第一行数据封装到一个`Map`里，`key`是列名，`value`就是对应的值。
    - `MapListHandler`：将结果集中的每一行数据都封装到一个`Map`里，然后再存放到`List`。
    - `ScalarHandler`：查询单个值对象。

- `org.apache.commons.dbutils.DbUtils  `：提供如关闭连接、装载JDBC驱动程序等常规工作的工具类，里面的所有方法都是静态的。

使用演示如下：

```java
package com.example.jdbc;

import com.example.jdbc.pojo.User;
import com.example.jdbc.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

public class DBUtilsTest {

    @Test
    public void testInsert() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "insert into user (username, password, sex, birthday, email) values (?, ?, ?, ?, ?)";
            int count = queryRunner.update(connection, sql, "Bob", "abcdef", true, "1990-01-01", "Bob@qq.com");
            System.out.println("添加的数量：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from user where id = ?";
            int count = queryRunner.update(connection, sql, 2);
            System.out.println("删除的数量：" + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryBean() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id, username, password, sex, birthday, email from user where id = ?";
            User user = queryRunner.query(connection, sql, new BeanHandler<>(User.class), 1);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryBeanList() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id, username, password, sex, birthday, email from user";
            List<User> users = queryRunner.query(connection, sql, new BeanListHandler<>(User.class));
            users.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryCustomizedBean() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            QueryRunner queryRunner = new QueryRunner();
            ResultSetHandler<User> handler = new ResultSetHandler<User>() {
                @Override
                public User handle(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        boolean sex = rs.getBoolean("sex");
                        Date birthday = rs.getDate("birthday");
                        String email = rs.getString("email");
                        return new User(id, username, password, sex, birthday, email);
                    }
                    return null;
                }
            };
            String sql = "select id, username, password, sex, birthday, email from user where id = ?";
            User user = queryRunner.query(connection, sql, handler, 1);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryValue() {
        try (
                Connection connection = JDBCUtils.getConnection()
        ) {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select max(birthday) from user";
            Date date = queryRunner.query(connection, sql, new ScalarHandler<>());
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

# 参考

[^1]: [尚硅谷JDBC核心技术视频教程（康师傅带你一站式搞定jdbc）](https://www.bilibili.com/video/BV1eJ411c7rf)
[^1]: [尚硅谷\_宋红康\_JDBC核心技术(2019新版)](https://pan.baidu.com/s/1i8WLqTZqn2ENtvi08r0JFA#list/path=%2F)，提取码：1sfv
