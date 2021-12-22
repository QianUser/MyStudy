# 概述

[Spring](https://repo.spring.io/)是轻量级的、开源的 JavaEE 框架，它可以解决企业应用开发的复杂性。

Spring有两个核心部分：

- **控制反转（Inversion of Control，IoC）**：把创建对象过程交给 Spring 进行管理。
- **面向切面编程（Aspect Oriented Programming，AOP）**：不修改源代码进行功能增强。

Spring的特点如下：

- 方便解耦，简化开发。
- AOP编程支持。
- 方便程序测试。
- 方便和其他框架进行整合。
- 方便进行事务操作。
- 降低API开发难度。

# IoC

IoC把对象创建和对象之间的调用过程，交给Spring进行管理。

使用IoC的目的是降低耦合性。

## 底层原理

IoC的底层原理：

- XML解析。
- 工厂模式。
- 反射。

IoC的过程如下：

1. 使用XML配置文件，配置创建的对象。

2. 解析XML文件，利用反射创建对象。

   假设要创建一个`UserDao`类的对象，则可以创建工厂类，如下：

   ```java
   class UserFactory {
       public static UserDao getDao() {
           String classValue = [class属性值];  // XML解析
           Class<?> clazz = Class.forName(classValue);  // 通过反射创建对象
           return (UserDao) clazz.newInstance();
       }
   }
   ```

   这样就可以进一步降低耦合度。例如，如果`UserDao`类路径变了，则不需要改变`UserFactory`的代码，只需要修改XML配置文件。

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
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.19</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

### 创建类

创建`Example`类：

```java
package com.example.spring;

public class Example {

    public void add() {
        System.out.println("add...");
    }

}
```

### 创建配置文件

在src/main/resources目录下，创建Spring配置文件（bean.xml），并在该配置文件中配置创建的对象：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- 创建Example对象 -->
    <bean name="example" class="com.example.spring.Example"/>
</beans>
```

### 测试

```java
package com.example.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExampleTest {

    @Test
    public void testAdd() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        Example example = context.getBean("example", Example.class);
        System.out.println(example);
        example.add();
    }

}
```

## `BeanFactory`接口

IoC思想基于IoC容器，IoC容器底层就是对象工厂。

Spring提供两种IoC容器实现方式（两个接口）：

- `BeanFactory`：IoC容器基本实现，是Spring内部的使用接口，不提供给开发人员使用。`BeanFactory`在加载配置文件时不会创建对象，在获取对象时才会创建对象。
- `ApplicationContext`：`BeanFactory`接口的子接口，提供更多更强大的功能，一般由开发人员使用。`ApplicationContext`在加载配置文件时候就会创建配置文件中配置的对象。

`ApplicationContext`的继承结构如下：

![ApplicationContext的继承结构](资源\ApplicationContext的继承结构.png)

`ApplicationContext`有两个主要的实现类：`FileSystemXmlApplicationContext`与`ClassPathXmlApplicationContext`，前者需要指定配置文件的路径，而后者需要指定配置文件的类路径。

## bean管理

bean管理指的是两个操作：

- Spring创建对象。
- Spring注入属性。

bean管理操作有两种方式：

- 基于XML配置文件方式实现。
- 基于注解方式实现。

### 基于XML方式管理bean

#### 对象创建

在Spring配置文件中，使用`bean`标签，标签里面添加对应属性，就可以实现对象创建。在`bean`标签中，`id`属性是对象的唯一标识，`class`属性指定类全路径。默认情况下，执行类的无参构造方法完成对象创建。

#### 依赖注入

**依赖注入（Dependency Injection，DI）**就是为对象注入属性。

##### 有参构造注入

创建`User`类：

```java
package com.example.spring;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;

    private String password;

}
```

配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="user" class="com.example.spring.User">
        <constructor-arg name="username" value="Alice"/>
        <constructor-arg name="password" value="123456"/>
    </bean>
</beans>
```

`bean`的子元素`constructor-arg`指定构造器参数，其中`name`指定参数名（还可以使用`index`属性指定索引），`value`指定该参数的取值。其中`name`、`index`与`value`也可以作为`constructor-arg`的子元素，以后的情况类似。

测试：

```java
package com.example.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserTest {

    @Test
    public void testUser() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        User user = context.getBean("user", User.class);
        System.out.println(user);
    }

}
```

##### `set`方法注入

配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="user" class="com.example.spring.User">
        <property name="username" value="Alice"/>
        <property name="password" value="123456"/>
    </bean>
</beans>
```

`bean`的子元素`property`指定`set`方法，其中`name`指定`set`方法名，`value`指定该`set`方法的参数取值。

##### p名称空间注入

使用p名称空间注入，可以简化XML配置。使用p名称空间注入时，首先需要将p名称空间添加到配置文件中，然后再操作`bean`标签：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="user" class="com.example.spring.User" p:username="Alice" p:password="123456"/>
</beans>
```

`bean`的属性`p:xxx`指定`set`方法，它的值指定该`set`方法的参数取值。

##### 特殊值注入

如果要注入`null`，则可以使用`null`标签。例如：

```xml
<property name="username"><null/></property>
```

如果属性值包含特殊符号，则可以对特殊符号转义，例如将`<`转义为`&lt;`将`>`转义为`&gt;`。也可以采用`![CDATA[]]`：

```xml
<property name="password"><value><![CDATA[<<123456>>]]></value></property>
```

##### 对象注入

创建`Dept`与`Emp`类，分别表示部门与员工。一个部门可以有多个员工，而一个员工只能属于一个部分，因此这里将`Dept`作为`Emp`的一个属性：

```java
package com.example.spring;

import lombok.Data;

@Data
public class Dept {

    private String name;

}
```

```java
package com.example.spring;

import lombok.Data;

@Data
public class Emp {

    private String name;

    private Dept dept;

}
```

注入对象的一种方式是直接在`beans`标签内部定义对象，然后属性使用`ref`去引用该对象的`id`：

```xml
<bean id="emp" class="com.example.spring.Emp">
    <property name="name" value="Alice"/>
    <property name="dept" ref="dept"/>
</bean>
<bean id="dept" class="com.example.spring.Dept">
    <property name="name" value="Security"/>
</bean>
```

直接定义在`beans`标签内的对象被称为**外部bean**。

也可以将bean的定义嵌套在注入的属性中实现内部bean的注入，这被称为**内部bean**，它只能被该对象所引用。

```xml
<bean id="emp" class="com.example.spring.Emp">
    <property name="name" value="Alice"/>
    <property name="dept">
        <bean class="com.example.spring.Dept">
            <property name="name" value="Security"/>
        </bean>
    </property>
</bean>
```

外部bean与内部bean可以直接设置注入的对象的属性：

```xml
<bean id="emp" class="com.example.spring.Emp">
    <property name="name" value="Alice"/>
    <property name="dept" ref="dept"/>
    <property name="dept.name" value="Finance"/>
</bean>
<bean id="dept" class="com.example.spring.Dept">
    <property name="name" value="Security"/>
</bean>
```

其中`<property name="dept.name" value="Finance"/>`将注入的`dept`对象的`name`设置为`Finance`，此时它会覆盖`dept`对象原有的`name`值（即`Security`）。

##### 集合属性注入

创建`CollectionExample`类：

```java
package com.example.spring;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class CollectionExample {

    private String[] array;

    private List<String> list;

    private Set<String> set;

    private Map<String, Integer> map;

    private List<User> userList;

    private Map<Integer, User> userMap;

}
```

配置文件：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="collectionExample" class="com.example.spring.CollectionExample">
        <property name="array">
            <array>
                <value>a1</value>
                <value>a2</value>
            </array>
        </property>
        <property name="list">
            <list>
                <value>l1</value>
                <value>l2</value>
            </list>
        </property>
        <property name="set">
            <set>
                <value>s1</value>
                <value>s2</value>
            </set>
        </property>
        <property name="map">
            <map>
                <entry key="m1" value="1"/>
                <entry key="m2" value="2"/>
            </map>
        </property>
        <property name="userList">
            <list>
                <ref bean="user1"/>
                <ref bean="user2"/>
            </list>
        </property>
        <property name="userMap">
            <map>
                <entry key="1" value-ref="user1"/>
                <entry key="2" value-ref="user2"/>
            </map>
        </property>
    </bean>
    <bean id="user1" class="com.example.spring.User">
        <property name="username" value="Alice"/>
        <property name="password" value="123456"/>
    </bean>
    <bean id="user2" class="com.example.spring.User">
        <property name="username" value="Bob"/>
        <property name="password" value="abcdef"/>
    </bean>
</beans>
```

数组、`List`与`Set`的注入分别使用`array`、`list`与`set`标签，并在`value`子元素中设置集合中的各个值，或在`ref`子元素中通过`bean`属性指定对象的`id`。`Map`的注入使用`map`标签，并在`entry`子元素中通过`key`与`value`指定键值，或通过`key-ref`与`value-ref`指定键值对象的`id`。

通过`util`命名空间可以将集合注入部分提取出来：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd">
    <util:list id="userList">
        <ref bean="user1"/>
        <ref bean="user2"/>
    </util:list>
    <bean id="user1" class="com.example.spring.User">
        <property name="username" value="Alice"/>
        <property name="password" value="123456"/>
    </bean>
    <bean id="user2" class="com.example.spring.User">
        <property name="username" value="Bob"/>
        <property name="password" value="abcdef"/>
    </bean>
    <bean id="collectionExample" class="com.example.spring.CollectionExample">
        <property name="userList" ref="userList"/>
    </bean>
</beans>
```

上面通过`uitl:list`定义了一个`List`，它包含两个`User`对象，`bean`中属性可以通过`ref`标签引用这个`List`。

##### XML自动装配

自动装配就是根据指定的装配规则（属性名称、属性类型等），Spring自动将匹配的属性值进行注入。

`bean`标签的`autowire`属性指定自动装配规则，它有两个常用值：`byName`与`byType`。`byName`会根据属性名称注入，它要求注入的bean的`id`值与类属性名称相同；`byType`会根据属性类型注入，它要求注入的bean的类型与类属性类型兼容（也就是类属性类型）。当使用`byType`注入，要注意同属于类属性类型的bean不能有多个。

下面根据属性名称为`Emp`注入`Dept`属性：

```xml
<bean id="emp" class="com.example.spring.Emp" autowire="byName">
    <property name="name" value="Alice"/>
</bean>
<bean id="dept" class="com.example.spring.Dept">
    <property name="name" value="Security"/>
</bean>
```

##### 引用外部属性文件

下面引入Druid连接池依赖：

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.9</version>
</dependency>
```

以下代码配置了Druid连接池：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/user_db"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>
</beans>
```

对于配置信息，最好单独创建一个外部属性文件，并在配置文件中引用文件中的属性。因此下面在在src/main/resources目录下创建一个`jdbc.properties`文件,它包含如下数据库信息：

```properties
prop.driverClass = com.mysql.cj.jdbc.Driver
prop.url = jdbc:mysql://localhost:3306/userDb
prop.username = root
prop.password = root
```

然后在Spring配置文件中使用`context:property-placeholder`标签引入外部属性文件，其中`location`指定文件位置。最后通过EL表达式取到属性值。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder location="classpath:jdbc.properties"/>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${prop.driverClass}"/>
        <property name="url" value="${prop.url}"/>
        <property name="username" value="${prop.username}"/>
        <property name="password" value="${prop.password}"/>
    </bean>
</beans>
```

### 基于注解方式管理bean

基于注解方式管理bean可以简化XML配置。

#### 对象创建

Spring中针对bean管理中创建对象提供了如下注解：

- `@Component`：普通注解，通用。
- `@Service`：用于业务逻辑层。
- `@Controller`：用于控制层。
- `@Repository`：用于数据访问、持久层。

以上每个注解用在不同的层上，但这并不是强制的，只是为了让开发更加方便、清晰。

下面创建类，并在类上面添加创建对象的注解：

```java
package com.example.spring;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component(value = "user")
public class User {

    private String username;

    private String password;

}
```

其中，`value`等价于XML配置的`bean`中的`id`，默认值为类名首字母小写。

要使用注解方式管理bean，必须要在配置文件中开启组件扫描，也就是扫描哪些包（包括所有子包）中的类，这样Spring才能知道这些包中的类中有什么样的注解：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.example.spring"/>
</beans>
```

如果要扫描多个包，则多个包使用逗号隔开；或者指定扫描包的上层目录。

组件扫描可以有更多的细节配置。例如，以下配置指定扫描`com.example.spring`包，但是不使用默认的过滤器，而是自己配置过滤器。`context:include-filter`指定扫描哪些内容。因此它只扫描包含`Controller`注解的类：

```xml
<context:component-scan base-package="com.example.spring" use-default-filters="false">
    <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

以下配置扫描包中所有内容，除了包含`Controller`注解的类：

```xml
<context:component-scan base-package="com.example.spring">
    <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

#### 依赖注入

`@Autowired`会根据属性类型进行自动装配。以下代码中，`UserDaoImpl`通过`@Autowired`注解注入了`User`对象，`UserService`通过`@Autowired`注入了`UserDao`的实现类对象`UserDaoImpl`：

```java
package com.example.spring.dao;

public interface UserDao {

    void printUser();

}
```

```java
package com.example.spring.dao.impl;

import com.example.spring.User;
import com.example.spring.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private User user;

    @Override
    public void printUser() {
        System.out.println("UserDaoImpl...");
        System.out.println(user);
    }

}
```

```java
package com.example.spring.service;

import com.example.spring.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void printUser() {
        System.out.println("UserService...");
        userDao.printUser();
    }

}
```

如果存在多个兼容类型，则进一步使用`@Qualifier`注解区分，它根据名称进行注入。该注解需要和`@Autowired`一起使用。例如，假设`UserDao`还有一个实现类`UserDaoImpl2`，此时需要`UserService`注入`UserDao`时需要使用`@Qualifier`。

```java
package com.example.spring.dao.impl;

import com.example.spring.User;
import com.example.spring.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl2 implements UserDao {

    @Autowired
    private User user;

    @Override
    public void printUser() {
        System.out.println("UserDaoImpl2...");
        System.out.println(user);
    }

}
```

```java
package com.example.spring.service;

import com.example.spring.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    @Qualifier(value = "userDaoImpl")
    private UserDao userDao;

    public void printUser() {
        System.out.println("UserService...");
        userDao.printUser();
    }

}
```

`@Resource`既可以根据类型注入，也可以根据名称注入（可以只根据名称注入，这与`@Qualifier`不同）。该注解是`javax.annotation`包中的注解，不推荐使用。

最后，`@Value`可以注入普通类型属性。下面使用`@Value`为`User`注入属性：

```java
package com.example.spring;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component(value = "user")
public class User {

    @Value("Alice")
    private String username;

    @Value("123456")
    private String password;

}
```

#### 完全注解开发

完全注解开发需要创建配置类，代替XML配置文件：

```java
package com.example.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration  // 作为配置类，替代 xml 配置文件
@ComponentScan(basePackages = "com.example.spring")  // 组件扫描
public class SpringConfig {}
```

然后使用`AnnotationConfigApplicationContext`指定配置类：

```java
package com.example.spring;

import com.example.spring.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserTest {

    @Test
    public void testUser() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        User user = context.getBean("user", User.class);
        System.out.println(user);
    }

}
```

### 工厂bean

Spring有两种类型bean，一种是普通的bean，还有一种是工厂bean（`FactoryBean`）。对于普通bean，在配置文件中定义的bean类型就是返回类型；对于工厂bean，在配置文件中定义的bean类型可以与返回类型不同。

要定义工厂bean，则需要创建一个实现`FactoryBean`接口的类，并实现接口里的方法：

```java
package com.example.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class UserFactoryBean implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        return new User("Bob", "abcdef");
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
```

测试：

```java
package com.example.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FactoryBeanTest {

    @Test
    public void testUserFactoryBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        User user = context.getBean("userFactoryBean", User.class);
        System.out.println(user);
    }

}
```

可以看到，`userFactoryBean`返回的是一个`User`对象。

### bean的作用域

在Spring中，默认情况下bean是单实例对象：

```java
package com.example.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScopeTest {

    @Test
    public void testScope() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        User user1 = context.getBean("user", User.class);
        User user2 = context.getBean("user", User.class);
        System.out.println(user1 == user2);
    }

}
```

程序输出`true`，说明`user1`与`user2`是同一个对象。

在定义bean时，可以通过`scope`属性（或使用`@Scope`注解）设置bean是单实例还是多实例：

- 当`scope = singleton`时，bean是单实例对象，此时加载Spring配置文件时就会创建单实例对象。
- 当`scope = prototype`时，bean是多实例对象，此时在调用`getBean`方法时才会创建多实例对象。

下面将`User`定义为多实例对象，然后再次运行以上代码，程序输出`false`：

```java
package com.example.spring;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component(value = "user")
@Scope(value = "prototype")
public class User {

    @Value("Alice")
    private String username;

    @Value("123456")
    private String password;

}
```

### bean的生命周期

生命周期就是从对象创建到对象销毁的过程。bean的生命周期有7步：

1. 通过构造方法创建bean实例（无参构造）。
2. 为bean的属性设置值和对其他bean引用（调用`set`方法等）。
3. 把bean实例传递bean的后置处理器方法：`postProcessBeforeInitialization`。
4. 调用bean的初始化方法（需要配置初始化方法）。
5. 把bean实例传递bean后置处理器的方法：`postProcessAfterInitialization`。
6. 使用bean。
7. 当容器关闭时候，调用bean的销毁方法（需要配置销毁方法）。

下面演示bean的生命周期：

```java
package com.example.spring;

public class LifeCycle {

    private String name;

    public LifeCycle() {
        System.out.println("通过构造方法创建Bean实例...");
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("为Bean的属性设置值和对其他Bean引用...");
    }

    public void initMethod() {
        System.out.println("调用Bean的初始化方法...");
    }

    public void destroyMethod() {
        System.out.println("调用Bean的销毁方法...");
    }

    @Override
    public String toString() {
        return "LifeCycle{" +
                "name='" + name + '\'' +
                '}';
    }

}
```

```java
package com.example.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("在初始化之前执行的方法...");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("在初始化之后执行的方法...");
        return bean;
    }

}
```

`MyBeanPostProcessor`实现了`MyBeanPostProcessor`的两个方法，用于实现bean生命周期的3、5步。注意它会对当前配置文件中的所有bean都添加后置处理器方法。两个方法的第一个参数就是传递过来的bean实例，这里都将其返回。

接下来要将`LifeCycle`与`MyBeanPostProcessor`注册为bean：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.example.spring"/>
    <bean id="lifeCycle" class="com.example.spring.LifeCycle" init-method="initMethod" destroy-method="destroyMethod">
        <property name="name" value="name"/>
    </bean>
    <bean id="myBeanPostProcessor" class="com.example.spring.MyBeanPostProcessor"/>
</beans>
```

其中`init-method`与`destroy-method`分别指定bean的初始化方法与bean的销毁方法。

测试：

```java
package com.example.spring;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LifeCycleTest {

    @Test
    public void testLifeCycle() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        LifeCycle lifeCycle = context.getBean("lifeCycle", LifeCycle.class);
        System.out.println("获取创建的Bean实例对象：" + lifeCycle);
        context.close();
    }

}
```

# AOP

AOP的思想是将与核心业务无关的，但又影响着多个类的公共行为抽取、封装到一个可重用模块，从而实现代码复用和模块解耦的目的[^2]。利用 AOP 可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。通俗来说，AOP就是在不修改源代码的情况下，在主干功能中添加新功能。

## 底层原理

AOP底层使用动态代理，分为两种情况：

- 有接口时，使用JDK动态代理：创建接口实现类的代理对象，增强类的方法。
- 没有接口时，使用CGLIB动态代理：创建子类的代理对象，增强类的方法。

## 相关概念

**连接点**：类里面可以被增强的方法称为连接点。

**切入点**：实际被增强的方法称为切入点。

**通知**（**增强**）：实际增强的逻辑部分称为通知（或增强）。通知有多种类型：

- **前置通知**：在方法执行之前执行的通知。
- **后置通知**（**返回通知**）：在方法执行之后执行的通知，当方法执行过程中发生异常，后置通知不会执行。
- **环绕通知**：在方法执行前后执行的通知。
- **异常通知**：当抛出异常时执行的通知。
- **最终通知**：无论是否发生异常，在方法执行之后一定会执行最终通知。

**切面**：把通知应用到切入点的过程。

## 切入点表达式

切入点表达式表示对什么类里面的什么方法进行增强，语法结构如下：

```
execution([权限修饰符][返回类型][类全路径][方法名][参数列表])
```

其中，参数列表可以直接写类型名称（逗号分隔）；权限修饰符可以省略；返回类型、类全路径、方法名、参数类型都可以使用通配符`*`（对于类全路径，一个`*`表示一个路径部分），表示它们任意；可以使用`..`，表示任意参数列表（包括空参数列表）或多个包。

```java
public void com.example.spring.User.printUser()  // 完整写法
void com.example.spring.User.printUser()  // 省略权限修饰符
* com.example.spring.User.printUser()  // 返回类型使用通配符
* *.*.*.User.printUser()  // 包名使用通配符
*..User.printUser()  // *..表示当前包及其子包
* *..printUser()  // *..也可以包括类名
* *..*.*()  // 类名和方法名都可以使用通配符
* *..*()  // 让*..包括类名
* *()  // 更通用
* *(int, String)  // 以上都表示无参方法，这里表示方法的第一个参数类型为int，第二个参数类型为String
* *(..)  // 表示有无参均可，参数可以是任意数量的任意类型
```

## 开发

Spring框架一般都是基于AspectJ实现AOP操作。Aspectj不是Spring的组成部分，独立于AOP框架，一般把Aspectj和Spring框架一起使用，进行AOP操作。

添加相关依赖：

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjtools</artifactId>
    <version>1.9.9.1</version>
</dependency>
```

在Spring配置文件中开启组件扫描、开启Aspectj生成代理对象：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">
    <context:component-scan base-package="com.example.spring"/>
    <aop:aspectj-autoproxy/>
</beans>
```

创建`User`类：

```java
package com.example.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class User {

    @Value("Alice")
    private String username;

    @Value("123456")
    private String password;

    public void printUser() {
        System.out.println(this);
    }

}
```

演示不同通知的使用：

```java
package com.example.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserProxy {

    // 前置通知
    @Before(value = "execution(* com.example.spring.User.printUser(..))")
    public void before() {
        System.out.println("before...");
    }

    // 后置通知（返回通知）
    @AfterReturning(value = "execution(* com.example.spring.User.printUser(..))")
    public void afterReturning () {
        System.out.println("afterReturning...");
    }

    // 最终通知
    @After(value = "execution(* com.example.spring.User.printUser(..))")
    public void after() {
        System.out.println("after...");
    }

    // 异常通知
    @AfterThrowing(value = "execution(* com.example.spring.User.printUser(..))")
    public void afterThrowing() {
        System.out.println("afterThrowing...");
    }

    // 环绕通知
    @Around(value = "execution(* com.example.spring.User.printUser(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("around before...");
        // 执行被增强的方法
        Object object = proceedingJoinPoint.proceed();
        System.out.println("around after...");
        return object;
    }

}
```

测试：

```java
package com.example.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AOPTest {

    @Test
    public void testAOP() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        User user = context.getBean("user", User.class);
        user.printUser();
    }

}
```

可以看到，在方法执行前，前置通知、环绕通知执行；在方法执行后，后置通知、最终通知与环绕通知执行；异常通知没有执行。如果在`User`类的`printUser`方法中抛出异常，则前置通知、异常通知、最终通知以及环绕通知的第一个输出执行，环绕通知的第二个输出、后置通知不执行。

由于以上通知都是对`User`类的`printUser`方法进行增强，下面对这些相同的切入点抽取，修改`UserProxy`类如下：

```java
package com.example.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class UserProxy {

    // 相同切入点抽取
    @Pointcut(value = "execution(* com.example.spring.User.printUser(..))")
    public void point() {}

    @Before(value = "point()")
    public void before() {
        System.out.println("before...");
    }

    @AfterReturning(value = "point()")
    public void afterReturning () {
        System.out.println("afterReturning...");
    }

    @After(value = "point()")
    public void after() {
        System.out.println("after...");
    }

    @AfterThrowing(value = "point()")
    public void afterThrowing() {
        System.out.println("afterThrowing...");
    }

    @Around(value = "point()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("around before...");
        Object object = proceedingJoinPoint.proceed();
        System.out.println("around after...");
        return object;
    }

}
```

当相同切入点被抽取后，随后的通知的注解的`value`属性可以直接调用抽取切入点的方法完成通知。

当多个类对同一方法进行增强时，可以配置它们的优先级，方法是在类上面添加`org.springframework.core.annotation.Order`注解并设置其`value`属性。`value`值越大，优先级越高，则被增强方法执行前该类对应的通知越先执行，被增强方法执行后该类对应的通知越后执行（也就是说多个类的方法嵌套执行）。

下面使用XML配置文件完成AOP操作。将`UserProxy`修改为一个普通的bean（去除`@Aspect`注解）并去除所有通知，然后修改配置文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">
    <context:component-scan base-package="com.example.spring"/>
    <!-- 配置AOP增强 -->
    <aop:config>
        <!-- 配置切入点 -->
        <aop:pointcut id="p" expression="execution(* com.example.spring.User.printUser(..))"/>
        <!-- 配置切面 -->
        <aop:aspect ref="userProxy">
            <!-- 增强作用在具体的方法上 -->
            <aop:before method="before" pointcut-ref="p"/>
            <aop:after-returning method="afterReturning" pointcut-ref="p"/>
            <aop:after method="after" pointcut-ref="p"/>
            <aop:after-throwing method="afterThrowing" pointcut-ref="p"/>
            <aop:around method="around" pointcut-ref="p"/>
        </aop:aspect>
    </aop:config>
</beans>
```

下面使用完全注解方式完成AOP操作，只需要定义一个配置类代替XML配置文件即可：

```java
package com.example.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "com.example.spring")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AOPConfig {}
```

# JdbcTemplate

JdbcTemplate是Spring框架对JDBC的封装，它可以方便地实现对数据库的操作。

首先引入相关依赖：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.19</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.9</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.3.19</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.29</version>
</dependency>
```

开启组件扫描；配置数据库连接池；配置`JdbcTemplate`对象，注入`dataSource`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.example.spring"/>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/user_db"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
</beans>
```

创建数据库`user_db`，并在其中创建表`user`，它包含一个`int`类型的`user_id`、一个`varchar`类型的`username`以及一个`varchar`类型的`password`。

创建实体类：

```java
package com.example.spring.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int userId;

    private String username;

    private String password;

}

```

创建数据访问类，演示JdbcTemplate的添加、更新与删除操作，它们都是使用`update`方法实现：

```java
package com.example.spring.dao;

import com.example.spring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addUser(User user) {
        String sql = "insert into user values (?, ?, ?)";
        Object[] args = {user.getUserId(), user.getUsername(), user.getPassword()};
        return jdbcTemplate.update(sql, args);
    }

    public int updateUser(User user) {
        String sql = "update user set user_id = ?, username = ?, password = ? where user_id = ?";
        Object[] args = {user.getUserId(), user.getUsername(), user.getPassword(), user.getUserId()};
        return jdbcTemplate.update(sql, args);
    }

    public int deleteUser(int userId) {
        String sql = "delete from user where user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

}
```

创建业务逻辑类：

```java
package com.example.spring.service;

import com.example.spring.dao.UserDao;
import com.example.spring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public int addUser(User user) {
        return userDao.addUser(user);
    }

    public int updateUser(User user) {
        return userDao.updateUser(user);
    }

    public int deleteUser(int userId) {
        return userDao.deleteUser(userId);
    }

}
```

测试：

```java
package com.example.spring;

import com.example.spring.entity.User;
import com.example.spring.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JdbcTemplateTest {

    private static UserService userService;

    @BeforeAll
    public static void beforeAll() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        userService = context.getBean("userService", UserService.class);
    }

    @Test
    public void testAddUser() {
        User user = new User(1001, "Alice", "123456");
        int count = userService.addUser(user);
        System.out.println("添加的数量：" + count);
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1001, "Bob", "abcdef");
        int count = userService.updateUser(user);
        System.out.println("更新的数量：" + count);
    }

    @Test
    public void testDeleteUser() {
        int count = userService.deleteUser(1001);
        System.out.println("删除的数量：" + count);
    }

}
```

下面使用`queryForObject`实现查询返回某个值。

在`UserDao`里添加一个方法：

```java
public Integer countUser() {
    String sql = "select count(*) from user";
    return jdbcTemplate.queryForObject(sql, Integer.class);
}
```

在`UserService`中调用该方法实现功能：

```java
public int countUser() {
    return userDao.countUser();
}
```

测试：

```java
@Test
public void testCountUser() {
    int count = userService.countUser();
    System.out.println("用户总量：" + count);
}
```

下面使用`queryForObject`实现查询返回对象。

在`UserDao`里添加一个方法：

```java
// ...
import org.springframework.jdbc.core.BeanPropertyRowMapper;
// ...

public User getUserById(int id) {
    String sql = "select * from user where user_id = ?";
    return jdbcTemplate.queryForObject(sql,
                                       new BeanPropertyRowMapper<>(User.class), id);
}
```

`queryForObject`的第二个参数是`RowMapper`接口，针对返回不同类型数据，使用这个接口里面实现类完成数据封装。这里使用`BeanPropertyRowMapper`，它可以将数据库字段名映射到类的属性名（自动处理下划线属性名与驼峰字段名之间的转换）。

在`UserService`中调用该方法实现功能：

```java
public User getUserById(int id) {
    return userDao.getUserById(id);
}
```

测试：

```java
@Test
public void testGetUserById() {
    System.out.println(userService.getUserById(1001));
}
```

JdbcTemplate的`query`方法可以实现查询返回集合。

在`UserDao`里添加一个方法：

```java
public List<User> getAllUsers() {
    String sql = "select * from user";
    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
}
```

在`UserService`中调用该方法实现功能：

```java
public List<User> getAllUsers() {
    return userDao.getAllUsers();
}
```

测试：

```java
@Test
public void testGetAllUsers() {
    System.out.println(userService.getAllUsers());
}
```

JdbcTemplate提供`batchUpdate`方法实现批量操作。下面实现批量添加、修改、删除操作。

在`UserDao`里添加三个方法：

```java
public int[] addUsers(List<User> users) {
    String sql = "insert into user values (?, ?, ?)";
    List<Object[]> batch = new ArrayList<>();
    users.forEach(user -> batch.add(new Object[]{user.getUserId(), user.getUsername(), user.getPassword()}));
    return jdbcTemplate.batchUpdate(sql, batch);
}

public int[] updateUsers(List<User> users) {
    String sql = "update user set username = ?, password = ? where user_id = ?";
    List<Object[]> batch = new ArrayList<>();
    users.forEach(user -> batch.add(new Object[]{user.getUsername(), user.getPassword(), user.getUserId()}));
    return jdbcTemplate.batchUpdate(sql, batch);
}

public int[] deleteUsers(List<Integer> ids) {
    String sql = "delete from user where user_id = ?";
    List<Object[]> batch = new ArrayList<>();
    ids.forEach(id -> batch.add(new Object[]{id}));
    return jdbcTemplate.batchUpdate(sql, batch);
}
```

在`UserService`中调用这些方法实现功能：

```java
public int[] addUsers(List<User> users) {
    return userDao.addUsers(users);
}

public int[] updateUsers(List<User> users) {
    return userDao.updateUsers(users);
}

public int[] deleteUsers(List<Integer> ids) {
    return userDao.deleteUsers(ids);
}
```

测试：

```java
@Test
public void testAddUsers() {
    List<User> users = new ArrayList<>();
    users.add(new User(1002, "Bob", "abcdef"));
    users.add(new User(1003, "Trudy", "ABCDEF"));
    int[] counts = userService.addUsers(users);
    System.out.println("添加的数量：" + Arrays.toString(counts));
}

@Test
public void testUpdateUsers() {
    List<User> users = new ArrayList<>();
    users.add(new User(1002, "Bob", "fedcba"));
    users.add(new User(1003, "Mike", "ABCDEF"));
    int[] counts = userService.updateUsers(users);
    System.out.println("更新的数量：" + Arrays.toString(counts));
}

@Test
public void testDeleteUsers() {
    List<Integer> ids = new ArrayList<>();
    ids.add(1002);
    ids.add(1003);
    int[] counts = userService.deleteUsers(ids);
    System.out.println("删除的数量：" + Arrays.toString(counts));
}
```

# 事务

下面通过转账操作演示事务的使用。

在数据库`user_db`中创建表`account`，它包含一个`int`类型的`user_id`（唯一非空）以及一个`int`类型的`money`，并添加相应记录。注意，表的存储引擎是`InnoDB`（或其他支持事务的存储引擎）。

引入相关依赖：

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.3.19</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.2.9</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.3.19</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.29</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.9.1</version>
    <scope>runtime</scope>
</dependency>
```

```java
package com.example.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addMoney(int user_id, int amount) {
        String sql = "update account set money = money + ? where user_id = ?";
        jdbcTemplate.update(sql, amount, user_id);
    }

    public void reduceMoney(int user_id, int amount) {
        String sql = "update account set money = money - ? where user_id = ?";
        jdbcTemplate.update(sql, amount, user_id);
    }

}
```

```java
package com.example.spring.service;

import com.example.spring.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    public void transfer(int from, int to, int amount) {
        accountDao.reduceMoney(from, amount);
        accountDao.addMoney(to, amount);
    }

}
```

```java
package com.example.spring;

import com.example.spring.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TransactionTest {

    @Test
    public void testTransfer() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        AccountService accountService = context.getBean("accountService", AccountService.class);
        accountService.transfer(1001, 1002, 100);
    }

}
```

此时转账操作是正常的。

下面模拟异常情况，修改`AccountService`的`transfer`如下，并再次运行测试：

```java
public void transfer(int from, int to, int amount) {
    accountDao.reduceMoney(from, amount);
    int i = 10 / 0;
    accountDao.addMoney(to, amount);
}
```

代码抛出异常，但是`user_id`为`1001`的账户余额莫名减少100，转账操作的原子性与一致性没有得到保证，需要使用事务，它的操作过程如下：

```java
try {
    // 开启事务
    // 进行业务操作
    // ...
    // 没有发生异常，事务提交
} catch(Exception e) {
    // 发生异常，事务回滚
}
```

在Spring中进行事务管理操作有两种方式，上面的是编程式事务管理方式，Spring还提供了声明式事务管理方式，它底层基于AOP原理，同样可以基于XML方式或注解方式管理。

Spring提供一个代表事务管理器的接口`PlatformTransactionManager`，该接口针对不同的框架提供不同的实现类，如下所示：

![事务管理器API](资源\事务管理器API.png)

下面基于注解方式，实现转账的事务操作。

在配置文件中，创建事务管理器，并开启事务注解：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">
    <context:component-scan base-package="com.example.spring"/>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/user_db"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!-- 创建事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!-- 开启事务注解 -->
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```

在需要实现事务操作的类（`AccountService`）上添加`org.springframework.transaction.annotation.Transactional`事务注解，再次运行测试。可以发现不管有没有异常，转账操作的原子性与一致性都得到保证。

下面是采用XML方式管理声明式事务的配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
                           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
                           http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">
    <context:component-scan base-package="com.example.spring"/>
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/user_db"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <tx:annotation-driven transaction-manager="transactionManager"/>
    <!-- 配置通知 -->
    <tx:advice id="advice">
        <!-- 配置事务参数 -->
        <tx:attributes>
            <!-- 指定在什么方法上添加事务 -->
            <!-- 可以使用*表示任意字符串 -->
            <tx:method name="transfer" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>
    <!-- 配置切入点和切面 -->
    <aop:config>
        <!-- 配置切入点 -->
        <aop:pointcut id="pt" expression="execution(* com.example.spring.service.AccountService.*(..))"/>
        <!-- 配置切面 -->
        <aop:advisor advice-ref="advice" pointcut-ref="pt"/>
    </aop:config>
</beans>
```

下面采用完全注解方式管理声明式事务：

```java
package com.example.spring.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.example.spring")
@EnableTransactionManagement // 开启事务
public class TxConfig {

    @Bean
    public DruidDataSource getDruidDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/user_db");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
```

`@Transactional`既可以添加到类上，也可以添加到方法上：

- 如果把这个注解添加类上面，这个类里面所有的方法都添加事务。
- 如果把这个注解添加方法上面，为这个方法添加事务。

在`@Transactional`中可以配置事务的相关参数，包括：

- `propagation`：事务传播行为。
- `isolation`：事务隔离性级别。
- `timeout`：超时时间（单位为秒）。如果事务在指定时间内不提交则回滚。
- `readonly`：是否只读，默认为`false`，表示可以执行查询、添加、修改、删除操作；设置其为`true`，则只能查询。
- `rollbackFor`：设置出现哪些异常进行事务回滚。
- `noFollbackFor`：设置出现哪些异常不进行事务回滚。

## 事务传播行为

事务传播行为指多个拥有事务的方法在嵌套调用时的事务控制方式。Spring框架定义了7种事务传播行为，默认为`org.springframework.transaction.annotation.Propagation.REQUIRED`：

| 事务传播类型                | 含义                                                         |
| --------------------------- | ------------------------------------------------------------ |
| `Propagation.REQUIRED`      | 如果有事务在运行，当前的方法就在这个事务内运行；否则，就启动一个新的事务，并在自己的事务内运行 |
| `Propagation.SUPPORTS`      | 如果有事务在运行，当前的方法就在这个事务内运行，否则它可以不运行在事务中 |
| `Propagation.MANDATORY`     | 当前的方法必须运行在事务内部；如果没有正在运行的事务，就抛出异常 |
| `Propagation.REQUIRES_NEW`  | 当前的方法必须启动新事务，并在它自己的事务内运行；如果有事务正在运行，则将它挂起 |
| `Propagation.NOT_SUPPORTED` | 当前的方法不应该运行在事务内部；如果有运行的事务，将它挂起   |
| `Propagation.NEVER`         | 当前的方法不应该运行在事务中；如果有运行的事务，就抛出异常   |
| `Propagation.NESTED`        | 如果有事务在运行，当前的方法就在这个事务的嵌套事务内运行；否则，就启动一个新的事务，并在它自己的事务内运行 |

在`AccountService`中添加一个方法：

```java
public void spend(int userId, int amount) {
    accountDao.reduceMoney(userId, amount);
    int i = 1 / 0;  // 模拟抛出异常情况
    accountDao.reduceMoney(userId, amount);
}
```

新建`AccountController`类：

```java
package com.example.spring.controller;

import com.example.spring.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    public void transferAndSpend() {
        accountService.transfer(1001, 1002, 100);
        accountService.spend(1002, 50);
    }

}
```

测试：

```java
package com.example.spring;

import com.example.spring.controller.AccountController;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TransactionTest {

    @Test
    public void testTransferAndSpend() {
        ApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
        AccountController accountController = context.getBean("accountController", AccountController.class);
        accountController.transferAndSpend();
    }

}
```

下面演示各种事务传播行为下事务的执行情况（假设`transfer`没有开启事务）[^3]：

- `transferAndSpend`的事务传播行为为`Propagation.REQUIRED`，`spend`不开启事务。此时`transfer`与`spend`同处于`transferAndSpend`的事务中，`transfer`与`spend`都回滚，表未发生变化。

- `transferAndSpend`不开启事务，`spend`的事务传播行为为`Propagation.REQUIRED`。`spend`发生异常而回滚，但是`transfer`仍然生效。

- `transferAndSpend`与`spend`的事务传播行为均为`Propagation.REQUIRED`。`spend`发生异常，开始回滚。异常继续向上抛，`transferAndSpend`发生异常，所有操作都回滚，最终表未发生变化。

- 假设`spend`方法调用进行了异常处理：

  ```java
  @Transactional(propagation = Propagation.REQUIRED)
  public void transferAndSpend() {
      accountService.transfer(1001, 1002, 100);
      try {
          accountService.spend(1002, 50);
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
  ```

  - 然后继续验证上面三种情况。在第一种情况下，`transferAndSpend`与`spend`都不回滚；在第二种情况下，`transferAndSpend`不回滚而`spend`回滚；在第三种情况下，`transferAndSpend`与`spend`都回滚（实际上`spend`会抛出异常：`org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only`）。综上，如果父方法与子方法都开启事务，异常发生让子事务回滚，父事务一定回滚（前提是在子事务没有将父事务挂起的情况下）；只要`try-catch`在内层，`@Transactional`在外层，异常被`try-catch`住，事务就不会回滚；如果`@Transactional`在内层，`try-catch`在外层，则`try-catch`还没来得及处理异常就在`@Transactional`的作用下回滚了。

- 对于以上6种情况，如果将`Propagation.REQUIRED`改为`Propagation.NESTED`，则其行为基本一致。但是当`spend`方法调用进行了异常处理，且`transferAndSpend`与`spend`的事务传播行为均为`Propagation.NESTED)`时，没有抛出异常，且`transferAndSpend`没有回滚而`spend`回滚。

- 假设`transferAndSpend`没有开启事务，`spend`的事务传播类型为`Propagation.SUPPORTS`。因为`transferAndSpend`没有开启事务，因此`spend`也不开启事务，不会回滚数据。

- 假设`transferAndSpend`的事务传播行为为`Propagation.REQUIRED`，而`spend`的事务传播行为为`Propagation.SUPPORTS`，则`spend`发生异常回滚；因为子事务没将父事务挂起，父事务也回滚。

- 假设`transferAndSpend`没有开启事务，`spend`的事务传播行为为`Propagation.MANDATORY`。则`spend`抛出异常：`org.springframework.transaction.IllegalTransactionStateException: No existing transaction found for transaction marked with propagation 'mandatory'`，因为`Propagation.MANDATORY`要求外层方法开启事务。但是因为`transferAndSpend`没有开启事务，`transfer`仍然成功执行。

- 假设`transferAndSpend`的事务传播行为为`Propagation.REQUIRED`，`spend`的事务传播行为为`Propagation.MANDATORY`，则`spend`发生异常回滚；因为子事务没将父事务挂起，父事务也回滚。

- 假设`transferAndSpend`的事务传播行为为`Propagation.REQUIRED`，`spend`的事务传播行为为`Propagation.REQUIRES_NEW`，则发生死锁。因为`transferAndSpend`中的事务被挂起，没有释放对相关记录的锁，导致`spend`必须等待。如果将`transferAndSpend`修改为：

  ```java
  public void transferAndSpend() {
      accountService.transfer(1001, 1002, 100);
      accountService.spend(1003, 50);
  }
  ```

  则`transferAndSpend`与`spend`都回滚（前提是表使用行级锁而非表锁等粗粒度的锁），因为`spend`中的异常向上抛给了`transferAndSpend`。假设`spend`没有抛出异常，而`transferAndSpend`中最后抛出异常。则`spend`的操作不会回滚，因为`transferAndSpend`抛出异常时`spend`中的事务已经提交了，但`transferAndSpend`会回滚。

- 假设`transferAndSpend`如下所示，且其事务传播行为为`Propagation.REQUIRED`，`spend`的事务传播行为为`Propagation.NOT_SUPPORTED`，则`spend`不会回滚，但是其异常向上抛给`transferAndSpend`，导致后者回滚（前提是表使用行级锁而非表锁等粗粒度的锁）。`Propagation.NOT_SUPPORTED`可以用在查询方法上，将父事务挂起，而查询本身以非事务的方式执行。

  ```java
  public void transferAndSpend() {
      accountService.transfer(1001, 1002, 100);
      accountService.spend(1003, 50);
  }
  ```

- 假设`transferAndSpend`的事务传播行为为`Propagation.REQUIRED`，`spend`的事务传播行为为`Propagation.NEVER`，则抛出异常：`org.springframework.transaction.IllegalTransactionStateException: Existing transaction found for transaction marked with propagation 'never'`。`spend`因为`Propagation.NEVER`，内容未执行；`transferAndSpend`因为接收到`spend`的异常而回滚。如果`transferAndSpend`没有开启事务，则两个方法都以非事务方式执行。`Propagation.NEVER`的行为与`Propagation.MANDATORY`正好相反。

需要注意的是，在SpringBoot中，如果同一对象内的`a`方法调用`b`方法，则`b`方法的任何事务设置无效，而是和`a`共用一个事务。因为事务是使用代理对象来控制的，因此这种做法和内联行为一致，绕过了代理对象。如果要同一个对象内事务方法互相调用不失效，则需要引入：

```xml
<dependency>
    <!-- 引入aspectj -->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

并开启动态代理功能，在主类上加上注解：`@org.springframework.context.annotation.EnableAspectJAutoProxy(exposeProxy = true)`。这样，以后所有的动态代理都是aspectj创建的，而不是使用JDK的代理代理。

然后用代理对象调用方法：

```java
import org.springframework.aop.framework.AopContext;

MyObject obj = (MyObject) AopContext.currentProxy();
obj.b();
```

# Spring5框架新功能

整个Spring5框架的代码基于Java8，运行时兼容 JDK9，许多不建议使用的类和方法在代码库中删除。

## 日志

Spring5框架自带了通用的日志封装。Spring5框架整合Log4j2，且已经移除 `Log4jConfigListener`（用于Log4j整合），官方建议使用 Log4j2。

引入依赖：

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.17.2</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.0-alpha7</version>
</dependency>
```

在在src/main/resources目录下，创建log4j2.xml配置文件。注意该文件的路径与名字是固定的。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
<!-- Configuration后面的status用于设置log4j2自身内部的信息输出，可以不设置；当设置成trace时，可以看到log4j2内部各种详细输出 -->
<configuration status="INFO">
    <!-- 先定义所有的appender -->
    <appenders>
        <!-- 输出日志信息到控制台 -->
        <console name="Console" target="SYSTEM_OUT">
            <!-- 控制日志输出的格式 -->
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </console>
    </appenders>
    <!-- 然后定义 logger，只有定义logger并引入的appender，appender才会生效 -->
    <!-- root：用于指定项目的根日志，如果没有单独指定 Logger，则会使用root作为默认的日志输出 -->
    <loggers>
        <root level="info">
            <appender-ref ref="Console"/>
        </root>
    </loggers>
</configuration>
```

运行上面的`TransactionTest`就有日志输出了。当然也可以在其中定义`org.slf4j.Logger`对象手动输出日志。

## `@Nullable`注解

Spring5框架核心容器支持`org.springframework.lang.Nullable`注解。该注解可以使用在方法、属性或参数上面，表示方法返回值、属性值或参数值可以为`null`。

## 函数式风格：`GenericApplicationContext`

下面使用`GenericApplicationContext`类，通过函数式风格创建对象，将对象交给Spring管理。

创建`User`类：

```java
package com.example.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;

    private String password;

}
```

创建测试类：

```java
package com.example.spring;

import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;

public class GenericApplicationContextTest {

    @Test
    public void testGenericApplicationContext() {
        GenericApplicationContext context = new GenericApplicationContext();
        context.refresh();
        // 对象注册
        context.registerBean("user", User.class, () -> new User("Alice", "123456"));
        User user = context.getBean("user", User.class);
        System.out.println(user);
    }

}
```

其中`context.registerBean`方法注册对象；`context.getBean`方法获取在Spring中注册的对象。如果`context.registerBean`未指定`beanName`（第一个参数），则需要使用类全路径指定bean的名字。

## 整合JUnit

### 整合JUnit4

引入依赖：

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.3.19</version>
</dependency>
```

然后在类上加上以下注解：

```java
// ...
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
// ...

@RunWith(SpringJUnit4ClassRunner.class)  // 单元测试框架
@ContextConfiguration("classpath:bean.xml")  // 加载配置文件
// ...
```

这样就将该类作为Junit4测试类了。

### 整合JUnit5

引入相关依赖：

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.3.19</version>
</dependency>
```

然后在类上加上以下注解：

```java
// ...
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.ContextConfiguration;
// ...

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:bean.xml")
// ...
```

这样就将该类作为Junit5测试类了。

Spring提供了复合注解`@SpringJUnitConfig`代替上面两个注解完成整合：

```java
// ...
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
// ...

@SpringJUnitConfig(locations = "classpath:bean.xml")
// ...
```

# 参考

[^1]: [尚硅谷Spring框架视频教程（spring5源码级讲解）](https://www.bilibili.com/video/BV1Vf4y127N5)
[^1]: [spring5](https://pan.baidu.com/s/1BPdI_vDWW2M-1A0okF3Pww#list/path=%2F)，提取码：2333
[^2]: [什么是面向切面编程?](https://zhuanlan.zhihu.com/p/421999882)

[^3]: [详解事务的7种传播行为](https://blog.csdn.net/qq_34115899/article/details/115602002)
