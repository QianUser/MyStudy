# 概述

## MVC

**模型-视图-控制器（Model-View-Controller，MVC）**是一种软件架构的思想，将软件按照模型、视图、控制器来划分。

模型指工程中的JavaBean，作用是处理数据。JavaBean分为两类：

- 一类称为实体类Bean：专门存储业务数据的，如`Student`、`User`实体类等。
- 一类称为业务处理Bean：指Service或Dao对象，专门用于处理业务逻辑和数据访问。

视图指工程中的HTML或JSP等页面，作用是与用户进行交互，展示数据。

控制器指工程中的Servlet，作用是接收请求和响应浏览器。

MVC的工作流程：用户通过视图层发送请求到服务器，在服务器中请求被控制器接收，控制器调用相应的模型层处理请求，处理完毕将结果返回到控制器，控制器再根据请求处理的结果找到相应的视图，渲染数据后最终响应给浏览器。

## SpringMVC

**SpringMVC**是Spring的一个后续产品，是Spring的一个子项目。

SpringMVC是Spring为表述层开发提供的一整套完备的解决方案（三层架构分为表述层（或表示层）、业务逻辑层、数据访问层，表述层表示前台页面和后台Servlet）。在表述层框架历经Strust、WebWork、Struts2等诸多产品的历代更迭之后，目前业界普遍选择了SpringMVC作为Java EE项目表述层开发的首选方案。

SpringMVC的特点如下：

- Spring家族原生产品，与IOC容器等基础设施无缝对接。
- 基于原生的Servlet，通过了功能强大的前端控制器`DispatcherServlet`，对请求和响应进行统一处理。
- 表述层各细分领域需要解决的问题全方位覆盖，提供全面解决方案。
- 代码清新简洁，大幅度提升开发效率。
- 内部组件化程度高，可插拔式组件即插即用，想要什么功能配置相应组件即可。
- 性能卓著，尤其适合现代大型、超大型互联网项目要求。

# 开发

## 开发环境

- 语言：Java 8
- IDE：IDEA 2020.3.4
- 构建工具：Maven 3.6.3
- 服务器：Tomcat 8.0.42

## 引入依赖

创建Maven工程，打包方式设置为war，引入依赖：

```xml
<!-- SpringMVC -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.19</version>
</dependency>
<!-- 日志 -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.3.0-alpha14</version>
</dependency>
<!-- ServletAPI -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
<!-- Spring5和Thymeleaf整合包 -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
    <version>3.0.15.RELEASE</version>
</dependency>
```

## 添加Web模块

在项目结构中，找到当前模块，配置好Web Facet的Web资源目录（典型情况下是src\main\webapp）与Web模块部署描述符（典型情况下是src\main\webapp\WEB-INF\web.xml）。当然也可以通过maven-archetype-webapp创建Maven工程，此时这些配置自动生成。

## 配置web.xml

注册SpringMVC的前端控制器`DispatcherServlet`。

### 默认配置方式

此配置作用下，SpringMVC的配置文件默认位于src/main/webapp/WEB-INF下，默认名称为\<servlet-name>-servlet.xml。例如，以下配置所对应SpringMVC的配置文件位于WEB-INF下，文件名为springMVC-servlet.xml。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!-- 配置SpringMVC的前端控制器，对浏览器发送的请求统一进行处理 -->
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <!-- 设置springMVC的核心控制器所能处理的请求的请求路径  -->
        <!-- /所匹配的请求可以是/login、.html、.js或.css等方式的请求路径，但是/不能匹配.jsp请求路径的请求 -->
        <!-- 因此就可以避免在访问jsp页面时，该请求被DispatcherServlet处理，从而找不到相应的页面 -->
        <!-- /*则能够匹配所有请求，例如在使用过滤器时，若需要对所有请求进行过滤，就需要使用/*的写法 -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

### 扩展配置方式

可通过`init-param`标签设置SpringMVC配置文件的位置和名称，通过`load-on-startup`标签设置SpringMVC前端控制器DispatcherServlet的初始化时间。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!-- 配置SpringMVC的前端控制器，对浏览器发送的请求统一进行处理 -->
    <servlet>
        <servlet-name>springMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 通过初始化参数指定SpringMVC配置文件的位置和名称 -->
        <init-param>
            <!-- contextConfigLocation为固定值 -->
            <param-name>contextConfigLocation</param-name>
            <!-- 使用classpath:表示从类路径（例如maven工程中的src/main/resources）查找配置文件 -->
            <param-value>classpath:springMVC.xml</param-value>
        </init-param>
        <!-- 作为框架的核心组件，在启动过程中有大量的初始化操作要做，而这些操作放在第一次请求时才执行会严重影响访问速度 -->
        <!-- 因此需要通过此标签将启动控制DispatcherServlet的初始化时间提前到服务器启动时 -->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

## 创建控制器

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    // @RequestMapping注解：处理请求和控制器方法之间的映射关系
    // @RequestMapping注解的value属性可以通过请求地址匹配请求，/表示的当前工程的上下文路径
    @RequestMapping("/")
    public String index() {
        // 返回视图名称
        return "index";
    }

}
```

在src/main/webapp/WEB-INF/templates目录下，创建index.html文件：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
</body>
</html>
```

在src/main/resources目录下，创建springMVC.xml文件（采用以上的扩展配置方式，否则在src\main\webapp\WEB-INF下创建该文件）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <context:component-scan base-package="com.example.springmvc"/>
    <!-- 配置Thymeleaf视图解析器 -->
    <bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
        <property name="order" value="1"/>
        <property name="characterEncoding" value="UTF-8"/>
        <property name="templateEngine">
            <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
                <property name="templateResolver">
                    <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                        <!-- 视图前缀 -->
                        <property name="prefix" value="/WEB-INF/templates/"/>
                        <!-- 视图后缀 -->
                        <property name="suffix" value=".html"/>
                        <property name="templateMode" value="HTML5"/>
                        <property name="characterEncoding" value="UTF-8" />
                    </bean>
                </property>
            </bean>
        </property>
    </bean>
</beans>
```

配置好Tomcat服务器（应用上下文为/springmvc），启动服务器，然后就可以访问：http://localhost:8080/springmvc/。

下面修改index.html为：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
<a th:href="@{/target}">访问target.html</a><br>
</body>
</html>
```

在同目录下创建target.html：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>目标</title>
</head>
<body>
目标页面
</body>
</html>
```

在`HelloController`类中添加一个方法：

```java
@RequestMapping("/target")
public String target() {
    return "target";
}
```

然后重新访问：http://localhost:8080/springmvc/，点击超链接，页面成功跳转。

## 总结

浏览器发送请求，若请求地址符合前端控制器的`url-pattern`，该请求就会被前端控制器DispatcherServlet处理。前端控制器会读取SpringMVC的核心配置文件，通过扫描组件找到控制器，将请求地址和控制器中`@RequestMapping`注解的`value`属性值进行匹配，若匹配成功，该注解所标识的控制器方法就是处理请求的方法。处理请求的方法需要返回一个字符串类型的视图名称，该视图名称会被视图解析器解析，加上前缀和后缀组成视图的路径，通过Thymeleaf对视图进行渲染，最终转发到视图所对应页面。

# `@RequestMapping`注解

## 功能

从注解名称上我们可以看到，`@RequestMapping`注解的作用就是将请求和处理请求的控制器方法关联起来，建立映射关系。

SpringMVC接收到指定的请求，就会来找到在映射关系中对应的控制器方法来处理这个请求。

## 位置

`@RequestMapping`标识一个类：设置映射请求的请求路径的初始信息。

`@RequestMapping`标识一个方法：设置映射请求请求路径的具体信息。

再次创建一个控制器：

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestMappingController {

    @RequestMapping("/testRequestMapping")
    public String target() {
        return "target";
    }

}
```

重新运行后程序报错，因为此时控制器中有两个方法对应同一请求（`@RequestMapping`所表示的路径）。SpringMVC不允许有多个方法对应同一请求。

此时可以在类上加上注解：`@RequestMapping("/request")`，并在index.html文件中添加一个超链接：

```html
<a th:href="@{/testRequestMapping/target}">测试@RequestMapping注解的位置</a><br>
```

重新运行程序，访问：http://localhost:8080/springmvc/，点击该超链接，页面成功跳转。

## `value`属性

`@RequestMapping`注解的`value`属性通过请求的请求地址匹配请求映射，它是一个字符串类型的数组，表示该请求映射能够匹配多个请求地址所对应的请求。`value`属性必须设置。

在控制器中添加方法：

```java
@RequestMapping(value = {"/value1", "/value2"})
public String testValue() {
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testRequestMapping/value1}">测试@RequestMapping注解的value属性</a><br>
<a th:href="@{/testRequestMapping/value2}">测试@RequestMapping注解的value属性</a><br>
```

点击两个超链接页面都能成功跳转。

## `method`属性

`@RequestMapping`注解的`method`属性通过请求的请求方式匹配请求映射，它是一个`RequestMethod`类型的数组，表示该请求映射能够匹配多种请求方式的请求。若当前请求的请求地址满足请求映射的`value`属性，但是请求方式不满足`method`属性，则浏览器报错405。

GET与POST的区别：

- GET请求提交请求参数时会将请求参数拼接在请求地址后面（没有请求体），POST请求提交请求时会将请求参数放在请求体中，但格式仍然是`name=value&name=value...`。
- GET相对不安全，传输速度快（因为伴随请求地址传过去的）。
- GET传输的数据量有限，POST传输数据量可以很大。
- 文件上传不能使用GET。如果非要用GET，则使用文件域（`type="file"`）上传文件时，拼接到地址栏中的请求参数只能是文件名（而不是文件）。

在控制器中添加方法：

```java
// ...
import org.springframework.web.bind.annotation.RequestMethod;
// ...

@RequestMapping(value = "/method", method = {RequestMethod.GET, RequestMethod.POST})
public String testMethod() {
    return "target";
}
```

在index.html文件中添加：

```html
<a th:href="@{/testRequestMapping/method}">测试@RequestMapping注解的value属性，使用GET请求</a>
<form th:action="@{/testRequestMapping/method}" method="post">
    <input type="submit" value="测试@RequestMapping注解的value属性，使用POST请求">
</form>
```

点击超链接或表单提交按钮页面都能成功跳转。如果`method`为`RequestMethod.GET`，则点击表单报错405；如果`method`为`RequestMethod.POST`，则点击超链接报错405。如果不指定`mthod`，则默认匹配所有请求。

对于处理指定请求方式的控制器方法，SpringMVC中提供了`@RequestMapping`的派生注解：

- 处理GET请求的映射：`@GetMapping`。
- 处理POST请求的映射：`@PostMapping`。
- 处理PUT请求的映射：`@PutMapping`。
- 处理DELETE请求的映射：`@DeleteMapping`。

常用的请求方式有GET、POST、PUT、DELETE等，但是目前浏览器只支持GET和POST，若在form表单提交时，为`method`设置了其他请求方式的字符串，则按照默认的请求方式GET处理。若要发送PUT或DELETE请求，则需要通过Spring提供的过滤器`HiddenHttpMethodFilter`。

## `params`属性

`@RequestMapping`注解的`params`属性通过请求的请求参数匹配请求映射，它是一个字符串类型的数组（必须同时满足），可以通过四种表达式设置请求参数和请求映射的匹配关系：

- `"param"`：要求请求映射所匹配的请求必须携带`param`请求参数。
- `"!param"`：要求请求映射所匹配的请求必须不能携带`param`请求参数。
- `"param = value"`：要求请求映射所匹配的请求必须携带`param`请求参数且其值为`value`（注意`=`两端的空格不可忽略）。
- `"param != value"`：要求请求映射所匹配的请求必须携带`param`请求参数且其值不为`value`（注意`!=`两端的空格不可忽略）。

若当前请求满足`@RequestMapping`注解的`value`和`method`属性，但是不满足`params`属性，则浏览器报错400。

在控制器中添加方法：

```java
@RequestMapping(value = "/params", params = {"username", "password!=123456"})
public String testParams() {
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testRequestMapping/params(username='admin', password=123456)}">测试@RequestMapping的params属性</a><br>
```

点击超链接，浏览器报错400。

## `headers`属性

`@RequestMapping`注解的`headers`属性通过请求的请求头信息匹配请求映射，它是一个字符串类型的数组，可以通过四种表达式设置请求头信息和请求映射的匹配关系：

- `"header"`：要求请求映射所匹配的请求必须携带`header`请求头信息。
- `"!header"`：要求请求映射所匹配的请求必须不能携带`header`请求头信息。
- `"header = value"`：要求请求映射所匹配的请求必须携带`header`请求头信息且其值为`value`（注意`=`两端的空格不可忽略）。
- `"header != value"`：要求请求映射所匹配的请求必须携带`header`请求头信息且其值不为`value`（注意`!=`两端的空格不可忽略）。

若当前请求满足`@RequestMapping`注解的`value`和`method`属性，但是不满足`headers`属性，此时页面显示404错误，即资源未找到。

注：打开浏览器的开发人员工具，并选择“网络”查看请求信息（包括cookie信息）。

在控制器中添加方法：

```java
@RequestMapping(value = "/headers", params = {"username", "password=123456"}, headers = {"Host=localhost:8080"})
public String testHeaders() {
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testRequestMapping/headers(username='admin', password=123456)}">测试@RequestMapping的headers属性</a><br>
```

如果服务器当前端口为8080，则页面成功跳转；否则浏览器报错404。

## SpringMVC支持ant风格的路径

`?`：表示任意的单个字符（不能是`/`或`?`等特殊符号）。

`*`：表示任意的0个或多个字符（不能是`/`或`?`等特殊符号）。

`**`：表示任意层目录。注意它只能使用`/**/xxx`或`/**`的形式，否则`**`被解析为两个单独的`*`。

## SpringMVC支持路径中的占位符

SpringMVC路径中的占位符常用于RESTful风格中，当请求路径中将某些数据通过路径的方式传输到服务器中，就可以在相应的`@RequestMapping`注解的`value`属性中通过占位符`{xxx}`表示传输的数据，再通过`@PathVariable`注解，将占位符所表示的数据赋值给控制器方法的形参。如果路径中有占位符，则匹配的请求地址中必须有该层目录（例如`/pathVariables/{id}/{username}`不能匹配路径`/pathVariables/1`或`/pathVariables/1/`）。

在控制器中添加方法：

```java
// ...
import org.springframework.web.bind.annotation.PathVariable;
// ...

@RequestMapping("/pathVariables/{id}/{username}")
public String testPlaceHolder(@PathVariable("id") String id, @PathVariable("username") String username){
        System.out.println("id: " + id + ", username: " + username);
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testRequestMapping/pathVariables/1/admin}">测试路径中的占位符</a>
```

点击超链接，页面成功跳转，且控制台输出正确的`id`与`username`。

# SpringMVC获取请求参数

## 通过ServletAPI获取

将`HttpServletRequest`作为控制器方法的形参，此时`HttpServletRequest`类型的参数表示封装了当前请求的请求报文的对象。不建议使用。

创建控制器：

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
@RequestMapping("/testRequestParameter")
public class RequestParameterController {

    @RequestMapping("/HttpServletRequest")
    public String testHttpServletRequest(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String[] hobby = request.getParameterValues("hobby");
        System.out.println("username: " + username + ", password: " + password + ", hobby: " + Arrays.toString(hobby));
        return "target";
    }

}
```

在index.html文件中添加表单：

```html
<form th:action="@{/testRequestParameter/HttpServletRequest}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    爱好：<input type="checkbox" name="hobby" value="a">a
         <input type="checkbox" name="hobby" value="b">b
         <input type="checkbox" name="hobby" value="c">c<br>
    <input type="submit" value="测试通过ServletAPI获取请求参数">
</form>
```

填写并提交表单，页面成功跳转，且控制台输出正确的`username`、`password`与`hobby`。注意，若请求所传输的请求参数中有多个同名的请求参数，需要通过`request.getParameterValues`获取请求参数，否则只能获取到第一个同名的请求参数。

## 通过控制器方法的形参获取请求参数

在控制器方法的形参位置，设置和请求参数同名的形参，当浏览器发送请求，匹配到请求映射时，在`DispatcherServlet`中就会将请求参数赋值给相应的形参。

若请求所传输的请求参数中有多个同名的请求参数，此时可以在控制器方法的形参中设置字符串数组或者字符串类型的形参接收此请求参数。

若使用字符串数组类型的形参，此参数的数组中包含了每一个数据。

若使用字符串类型的形参，此参数的值为每个数据中间使用逗号拼接的结果。

在控制器中添加方法：

```java
@RequestMapping("/methodParameter")
public String testMethodParameter(String username, String password, String[] hobby){
    System.out.println("username: " + username + ", password: " + password + ", hobby: " + Arrays.toString(hobby));
    return "target";
}
```

在index.html文件中添加表单：

```html
<form th:action="@{/testRequestParameter/methodParameter}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    爱好：<input type="checkbox" name="hobby" value="a">a
    <input type="checkbox" name="hobby" value="b">b
    <input type="checkbox" name="hobby" value="c">c<br>
    <input type="submit" value="测试通过ServletAPI获取请求参数">
</form>
```

填写并提交表单，页面成功跳转，且控制台输出正确的`username`、`password`与`hobby`。

## `@RequestParam`

`@RequestParam`是将请求参数和控制器方法的形参创建映射关系，它一共有四个属性：

- `value`/`name`：指定为形参赋值的请求参数的参数名。
- `required`：设置是否必须传输此请求参数，默认值为`true`。若设置为`true`，则当前请求必须传输`value`所指定的请求参数，若没有传输该请求参数，且没有设置`defaultValue`属性，则页面报错400；若设置为`false`，则当前请求不是必须传输`value`所指定的请求参数，若没有传输，则注解所标识的形参的值为`null`。
- `defaultValue`：不管`required`属性值为`true`或`false`，当`value`所指定的请求参数没有传输或传输的值为`""`时，则使用默认值为形参赋值。

修改`testMethodParameter`方法：

```java
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/methodParameter")
public String testMethodParameter(@RequestParam("username") String name, String password, String[] hobby){
    System.out.println("username: " + name + ", password: " + password + ", hobby: " + Arrays.toString(hobby));
    return "target";
}
```

填写并提交表单，页面成功跳转，且控制台输出正确的`username`、`password`与`hobby`。

## `@RequestHeader`

`@RequestHeader`是将请求头信息和控制器方法的形参创建映射关系， 它一共有四个属性，`value`、`name`、`required`与`defaultValue`，用法同`@RequestParam`。

在控制器中添加方法：

```java
// ...
import org.springframework.web.bind.annotation.RequestHeader;
// ...

@RequestMapping("/RequestHeader")
public String testRequestHeader(@RequestHeader("Host") String host){
    System.out.println("host: " + host);
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testRequestParameter/RequestHeader}">测试获取请求头信息</a><br>
```

填写并提交表单，页面成功跳转，且控制台输出正确的`host`。

如果控制器方法有多个参数，则这些参数都有可能获取到值（只要符合既定规则）。例如，如果控制器参数有两个`User`对象，则这两个`User`对象都会得到相同的数据。

## `@CookieValue`

`@CookieValue`是将cookie数据和控制器方法的形参创建映射关系， 它一共有四个属性，`value`、`name`、`required`与`defaultValue`，用法同`@RequestParam`。

修改控制器方法：

```java
@RequestMapping("/HttpServletRequest")
public String testHttpServletRequest(HttpServletRequest request){
    request.getSession();  // 用于创建cookie
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String[] hobby = request.getParameterValues("hobby");
    System.out.println("username: " + username + ", password: " + password + ", hobby: " + Arrays.toString(hobby));
    return "target";
}
```

在控制器中添加方法：

```java
// ...
import org.springframework.web.bind.annotation.CookieValue;
// ...

@RequestMapping("/CookieValue")
public String testCookieValue(@CookieValue("JSESSIONID") String JSESSIONID) {
    System.out.println("JSESSIONID: " + JSESSIONID);
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testRequestParameter/CookieValue}">测试获取cookie信息</a><br>
```

首先先填写并提交`testHttpServletRequest`方法对应的表单，页面成功跳转，然后返回再次点击该超链接，页面成功跳转，且控制台输出正确的`JSESSIONID`

## 通过POJO获取请求参数

可以在控制器方法的形参位置设置一个实体类类型的形参，此时若浏览器传输的请求参数的参数名和实体类中的属性名（指的是对应的`set`方法）一致，那么请求参数就会为此属性赋值。

引入依赖：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

创建实体类`User`：

```java
package com.example.springmvc.pojo;

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

    private int age;

    private String sex;

    private String email;

}
```

在控制器中添加方法：

```java
// ...
import com.example.springmvc.pojo.User;
// ...

@RequestMapping("/pojo")
public String testPOJO(User user){
    System.out.println(user);
    return "target";
}
```

在index.html文件中添加表单：

```html
<form th:action="@{/testRequestParameter/pojo}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    性别：<input type="radio" name="sex" value="男">男
         <input type="radio" name="sex" value="女">女<br>
    年龄：<input type="text" name="age"><br>
    邮箱：<input type="text" name="email"><br>
    <input type="submit" value="测试通过实体类获取请求参数">
</form>
```

正确填写并提交表单，页面成功跳转，且控制台输出正确的`User`，但是`sex`属性乱码（如果请求方式是GET，则不会乱码）。

## 解决获取请求参数的乱码问题

解决获取请求参数的乱码问题，可以使用SpringMVC提供的编码过滤器`CharacterEncodingFilter`，但是必须在web.xml中进行注册（因为必须在获取请求参数之前设置编码）。

SpringMVC中处理编码的过滤器一定要配置到其他过滤器之前（因为必须在获取请求参数之前设置编码），否则无效。

```xml
<!-- 配置springMVC的编码过滤器 -->
<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceResponseEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

# 域对象共享数据

域对象有四种，除了JSP中的PageContext外，有三种：

- request：一次请求。
- session：一次会话，浏览器开启到浏览器关闭的过程。session中的数据与服务器是否关闭无关，因为session中有钝化（服务器关闭但浏览器没有关闭时会话仍然继续，此时session中的数据被序列化到磁盘上）和活化的概念（浏览器仍然没有关闭但服务器重启，此时钝化后的内容会被重新读取到session中）。
- ServletContext（application）：整个应用范围，即服务器开启到服务器关闭的过程，与浏览器是否关闭无关。ServletContext表示上下文对象，在服务器启动时创建，在服务器关闭时销毁。

## 使用`ServletAPI`向`request`域对象共享数据

不建议使用。

创建控制器：

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/testScope")
public class ScopeController {

    @RequestMapping("/HttpServletRequest")
    public String testHttpServletRequest(HttpServletRequest request) {
        request.setAttribute("username", "admin");
        return "scope";
    }

}
```

创建scope.html：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>域对象共享数据</title>
</head>
<body>
<p th:text="${username}"></p>
</body>
</html>
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/HttpServletRequest}">测试通过ServletAPI向请求域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

## 使用`ModelAndView`向`request`域对象共享数据

`ModelAndView`有`Model`和`View`的功能。`Model`主要用于向请求域共享数据，`View`主要用于设置视图，实现页面跳转。

实际上，不管使用什么方式向`request`域对象共享数据，最终都要将数据封装到`ModelAndView`中（只是如果使用`ServletAPI`，则`ModelAndView`的`model`属性中没有数据）。

在控制器中添加方法：

```java
// ...
import org.springframework.web.servlet.ModelAndView;
// ...

@RequestMapping("/ModelAndView")
public ModelAndView testModelAndView() {
    ModelAndView modelAndView = new ModelAndView();
    // 向请求域共享数据
    modelAndView.addObject("username", "admin");
    // 设置视图，实现页面跳转
    modelAndView.setViewName("scope");
    return modelAndView;
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/ModelAndView}">测试通过ModelAndView向请求域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

## 使用`Model`向`request`域对象共享数据

在控制器中添加方法：

```java
// ...
import org.springframework.ui.Model;
// ...

@RequestMapping("/Model")
public String testModel(Model model) {
    model.addAttribute("username", "admin");
    return "scope";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/Model}">测试通过Model向请求域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

## 使用`Map`向`request`域对象共享数据

在控制器中添加方法：

```java
// ...
import java.util.Map;
// ...

@RequestMapping("/Map")
public String testMap(Map<String, Object> map) {
    map.put("username", "admin");
    return "scope";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/Map}">测试通过Map向请求域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

## 使用`ModelMap`向`request`域对象共享数据

在控制器中添加方法：

```java
import org.springframework.ui.ModelMap;

@RequestMapping("/ModelMap")
public String testModelMap(ModelMap modelMap) {
    modelMap.addAttribute("username", "admin");
    return "scope";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/ModelMap}">测试通过ModelMap向请求域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

## `Model`、`ModelMap`、`Map`的关系

`Model`、`ModelMap`、`Map`类型的参数其实本质上都是 `BindingAwareModelMap`类型的，它们的继承关系如下所示：

```java
public interface Model {}
public class ModelMap extends LinkedHashMap<String, Object> {}
public class ExtendedModelMap extends ModelMap implements Model {}
public class BindingAwareModelMap extends ExtendedModelMap {}
```

## 向session域共享数据

在控制器中添加方法：

```java
import javax.servlet.http.HttpSession;

@RequestMapping("/session")
public String testHttpSession(HttpSession session) {
    session.setAttribute("password", "123456");
    return "scope";
}
```

在scope.html新建一个`p`标签：

```html
<p th:text="${session.password}"></p>
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/session}">测试通过ServletAPI向session域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

## 向application域共享数据

在控制器中添加方法：

```java
@RequestMapping("/application")
public String testApplication(HttpSession session) {
    ServletContext application = session.getServletContext();
    application.setAttribute("email", "admin@qq.com");
    return "scope";
}
```

在scope.html新建一个`p`标签：

```html
<p th:text="${application.email}"></p>
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testScope/application}">测试通过ServletAPI向application域中共享数据</a><br>
```

点击超链接，页面成功跳转，且数据正确显示。

# SpringMVC的视图

SpringMVC中的视图是View接口，视图的作用渲染数据，将模型Model中的数据展示给用户。

SpringMVC视图的种类很多，默认有转发视图和重定向视图。

当工程引入JSTL的依赖，转发视图会自动转换为JstlView。

若使用的视图技术为Thymeleaf，在SpringMVC的配置文件中配置了Thymeleaf的视图解析器，由此视图解析器解析之后所得到的是ThymeleafView。

## ThymeleafView

当控制器方法中所设置的视图名称没有任何前缀时，此时的视图名称会被SpringMVC配置文件中所配置的视图解析器解析，视图名称拼接视图前缀和视图后缀所得到的最终路径，会通过转发的方式实现跳转。

创建控制器：

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testView")
public class ViewController {

    @RequestMapping("/ThymeleafView")
    public String testThymeleafView() {
        return "target";
    }

}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testView/ThymeleafView}">测试ThymeleafView</a><br>
```

点击超链接，页面成功跳转。事实上，前面创建视图的都是ThymeleafView。

调试，查看`org.springframework.web.servlet`的`render`方法，可以看到创建的确实是`ThymeleafView`，且视图名称为`target`：

![ThymeleafView](资源\ThymeleafView.png)

## 转发视图

SpringMVC中默认的转发视图是`org.springframework.web.servlet.view.InternalResourceView`。

当控制器方法中所设置的视图名称以`forward:`为前缀时（例如`forward:/`、`forward:/employee`），创建`InternalResourceView`视图，此时的视图名称不会被SpringMVC配置文件中所配置的视图解析器解析，而是会将前缀`forward:`去掉，剩余部分作为最终路径通过转发的方式实现跳转。

在控制器中添加方法：

```java
@RequestMapping("/forward")
public String testForward() {
    return "forward:/testView/ThymeleafView";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testView/forward}">测试InternalResourceView</a><br>
```

点击超链接，页面成功跳转。

调试，查看`org.springframework.web.servlet`的`render`方法（第一次进入该方法时），可以看到创建的确实是`ThymeleafView`，且视图名称为`forward:/ThymeleafView`。

需要注意的是，在这个过程中创建了两个视图，一次是`InternalResourceView`，转发后再创建一次`ThymeleafView`（因此调试过程中会两次进入`render`方法）。

## 重定向视图

SpringMVC中默认的重定向视图是`RedirectView`。

当控制器方法中所设置的视图名称以`redirect:`为前缀时（例如`redirect:/`、`redirect:/employee`），创建`RedirectView`视图，此时的视图名称不会被SpringMVC配置文件中所配置的视图解析器解析，而是会将前缀`redirect:`去掉，剩余部分作为最终路径通过重定向的方式实现跳转。

转发与重定向的区别：

- 转发是一次请求：第一次是浏览器发送请求，第二次发生在服务器内部。这里的一次请求指的是浏览器发送的一次请求；重定向是浏览器发送两次请求，第一次访问Servlet，第二次访问重定向后的地址。
- 转发后的地址栏不变，是第一次发送请求时的地址；重定向的地址栏为重定向后的地址。
- 转发可以获取请求域中的数据，重定向不可以（因为是两次请求，所以用到的`request`对象不是同一个）。
- 转发能访问WEB-INF目录下的内容，重定向不可以。因为WEB-INF下的资源只能通过服务器内部访问。
- 转发不能跨域，重定向能跨域。因为转发发生在服务器内部，只能访问服务器内部的资源；而重定向是浏览器发送两次请求，通过浏览器可以访问任何资源。

在控制器中添加方法：

```java
@RequestMapping("/redirect")
public String testRedirect() {
    return "redirect:/testView/ThymeleafView";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testView/redirect}">测试RedirectView</a><br>
```

点击超链接，页面成功跳转，且地址栏为`/testView/ThymeleafView`。

调试，查看`org.springframework.web.servlet`的`render`方法（第一次进入该方法时），可以看到创建的确实是`ThymeleafView`，且视图名称为`redirect:/ThymeleafView`。同样，在这个过程中创建了两个视图。

## 视图控制器

当控制器方法中，仅仅用来实现页面跳转，即只需要设置视图名称时，可以将处理器方法使用`view-controller`标签进行表示。

当SpringMVC中设置任何一个`view-controller`时，其他控制器中的请求映射将全部失效，此时需要在SpringMVC的核心配置文件中设置开启mvc注解驱动的标签：

```html
<mvc:annotation-driven />
```

删除`HelloController`的`index`方法与`target`方法，并将配置文件修改如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <context:component-scan base-package="com.example.springmvc"/>
    <!-- 配置Thymeleaf视图解析器 -->
    <bean id="viewResolver" class="org.thymeleaf.spring5.view.ThymeleafViewResolver">
        <property name="order" value="1"/>
        <property name="characterEncoding" value="UTF-8"/>
        <property name="templateEngine">
            <bean class="org.thymeleaf.spring5.SpringTemplateEngine">
                <property name="templateResolver">
                    <bean class="org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver">
                        <!-- 视图前缀 -->
                        <property name="prefix" value="/WEB-INF/templates/"/>
                        <!-- 视图后缀 -->
                        <property name="suffix" value=".html"/>
                        <property name="templateMode" value="HTML5"/>
                        <property name="characterEncoding" value="UTF-8" />
                    </bean>
                </property>
            </bean>
        </property>
    </bean>
    <mvc:annotation-driven />
    <!-- path：设置处理的请求地址；view-name：设置请求地址所对应的视图名称 -->
    <mvc:view-controller path="/" view-name="index"/>
    <mvc:view-controller path="/target" view-name="target"/>
</beans>
```

# RESTful[^2]

REST即**Re**presentational **S**tate **T**ransfer（表现形式状态转换)，在开发中代表访问网络资源的格式。

按照REST风格访问资源时使用路径表示资源（/模块），行为动作区分对资源进行了何种操作：

| 功能             | 传统风格                              | REST风格                 | REST风格请求方法  |
| ---------------- | ------------------------------------- | ------------------------ | ----------------- |
| 查询全部用户信息 | http://localhost/user/getAllUsers     | http://localhost/users   | GET（查询）       |
| 查询指定用户信息 | http://localhost/user/getById?id=1    | http://localhost/users/1 | GET（查询）       |
| 添加用户信息     | http://localhost/user/saveUser        | http://localhost/users   | POST（新增/保存） |
| 修改用户信息     | http://localhost/user/updateUser      | http://localhost/users   | PUT（修改/更新）  |
| 删除用户信息     | http://localhost/user/deleteById?id=1 | http://localhost/users/1 | DELETE（删除）    |

注意，上述行为是约定方式，约定不是规范，可以打破，所以称其为REST风格，而不是REST规范。

描述模块的名称通常使用复数，表示此类资源，而非单个资源，例如：`users`、`books`、`accounts`等等。

REST风格的优点在于：

- 隐藏资源的访问行为，无法通过地址得知对资源是何种操作。
- 简化书写。

根据REST风格对资源进行访问称为RESTful。

## `HiddenHttpMethodFilter`

大部分浏览器只支持发送GET和POST方式的请求，SpringMVC 提供了`HiddenHttpMethodFilter`帮助我们将 POST 请求转换为 DELETE或PUT请求。

`HiddenHttpMethodFilter`处理PUT和DELETE请求的条件：

- 当前请求的请求方式必须为POST。
- 当前请求必须传输请求参数`_method`。

满足以上条件，`HiddenHttpMethodFilter`过滤器就会将当前请求的请求方式转换为请求参数`_method`的值，因此请求参数`_method`的值才是最终的请求方式。

在web.xml中注册`HiddenHttpMethodFilter`：

```xml
<filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

目前为止，已经介绍到SpringMVC中提供了两个过滤器：`CharacterEncodingFilter`和`HiddenHttpMethodFilter`。在web.xml中注册时，必须先注册`CharacterEncodingFilter`，再注册`HiddenHttpMethodFilter`，因为在`CharacterEncodingFilter`中是通过 `request.setCharacterEncoding(encoding)`方法设置字符集的，而`request.setCharacterEncoding(encoding)`方法要求前面不能有任何获取请求参数的操作。而`HiddenHttpMethodFilter`恰恰有一个获取请求方式的操作：

```java
String paramValue = request.getParameter(this.methodParam);
```

## RESTful案例

下面采用RESTful风格，实现对员工信息的增删改查。

创建Maven工程，打包方式设置为war，引入依赖：

```xml
<!-- SpringMVC -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.19</version>
</dependency>
<!-- 日志 -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.3.0-alpha14</version>
</dependency>
<!-- ServletAPI -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
<!-- Spring5和Thymeleaf整合包 -->
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
    <version>3.0.15.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.24</version>
</dependency>
```

添加Web模块、配置web.xml（注意配置`CharacterEncodingFilter`以及`HiddenHttpMethodFilter`）并创建配置文件。

创建实体类：

```java
package com.restful.springmvc.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

   private int id;
   
   private String lastName;

   private String email;
   
   private int gender;  // 1：male，0：female
   
}
```

准备DAO模拟数据：

```java
package com.restful.springmvc.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.restful.springmvc.pojo.Employee;
import org.springframework.stereotype.Repository;


@Repository
public class EmployeeDao {

   private static final Map<Integer, Employee> employees;
   
   static{
      employees = new HashMap<>();
      employees.put(1001, new Employee(1001, "E-AA", "aa@163.com", 1));
      employees.put(1002, new Employee(1002, "E-BB", "bb@163.com", 1));
      employees.put(1003, new Employee(1003, "E-CC", "cc@163.com", 0));
      employees.put(1004, new Employee(1004, "E-DD", "dd@163.com", 0));
      employees.put(1005, new Employee(1005, "E-EE", "ee@163.com", 1));
   }
   
   private static Integer initId = 1006;
   
   public void save(Employee employee){
      if(employee.getId() <= 0){
         employee.setId(initId++);
      }
      employees.put(employee.getId(), employee);
   }
   
   public Collection<Employee> getAll(){
      return employees.values();
   }
   
   public Employee get(int id){
      return employees.get(id);
   }
   
   public void delete(int id){
      employees.remove(id);
   }

}
```

### 功能清单

| 功能               | URL 地址    | 请求方式 |
| ------------------ | ----------- | -------- |
| 访问首页           | /           | GET      |
| 查询全部数据       | /employee   | GET      |
| 删除               | /employee/2 | DELETE   |
| 跳转到添加数据页面 | /toAdd      | GET      |
| 执行保存           | /employee   | POST     |
| 跳转到更新数据页面 | /employee/2 | GET      |
| 执行更新           | /employee   | PUT      |

### 访问首页

配置view-controller

```xml
<mvc:view-controller path="/" view-name="index"/>
```

创建index.html文件

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<h1>首页</h1>
<a th:href="@{/employee}">访问员工信息</a>
</body>
</html>
```

### 查询所有员工数据

创建控制器：

```java
package com.restful.springmvc.controller;

import com.restful.springmvc.dao.EmployeeDao;
import com.restful.springmvc.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@Controller
public class EmployeeController {
    
    @Autowired
    private EmployeeDao employeeDao;

    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public String getEmployeeList(Model model){
        Collection<Employee> employeeList = employeeDao.getAll();
        model.addAttribute("employeeList", employeeList);
        return "employee_list";
    }

}
```

创建employee_list.html：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Employee Info</title>
    <!-- 引入vue.js文件，注意它不能位于WEB-INF文件夹下，因为该文件夹下的内容只能通过转发访问 -->
    <script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
</head>
<body>

    <table border="1" cellpadding="0" cellspacing="0" style="text-align: center;" id="dataTable">
        <tr>
            <th colspan="5">Employee Info</th>
        </tr>
        <tr>
            <th>id</th>
            <th>lastName</th>
            <th>email</th>
            <th>gender</th>
            <th>options(<a th:href="@{/toAdd}">add</a>)</th>
        </tr>
        <tr th:each="employee : ${employeeList}">
            <td th:text="${employee.id}"></td>
            <td th:text="${employee.lastName}"></td>
            <td th:text="${employee.email}"></td>
            <td th:text="${employee.gender}"></td>
            <td>
                <a class="deleteA" @click="deleteEmployee" th:href="@{'/employee/'+${employee.id}}">delete</a>
                <a th:href="@{'/employee/'+${employee.id}}">update</a>
            </td>
        </tr>
    </table>
</body>
</html>
```

在配置文件中设置：

```html
<!-- 开放对静态资源的访问 -->
<!-- 静态资源访问首先被前端控制器处理，如果控制器中找不到相应的请求映射，则交给默认的servlet处理，如果还是找不到资源，则报错404。对于其他资源，同理。 -->
<!-- 必须和开启mvc注解驱动的标签一起使用，否则所有请求都被DispatcherServlet处理 -->
<mvc:default-servlet-handler/>
<mvc:view-controller path="/" view-name="index"/>
```

### 删除员工

在employee_list.html文件中，创建处理DELETE请求方式的表单：

```html
<!-- 作用：通过超链接控制表单的提交，将post请求转换为delete请求 -->
<form id="delete_form" method="post">
    <!-- HiddenHttpMethodFilter要求：必须传输_method请求参数，并且值为最终的请求方式 -->
    <input type="hidden" name="_method" value="delete"/>
</form>
```

通过vue处理点击事件

```html
<script type="text/javascript">
    var vue = new Vue({
        el:"#dataTable",
        methods:{
            //event表示当前事件
            deleteEmployee:function (event) {
                //通过id获取表单标签
                var delete_form = document.getElementById("delete_form");
                //将触发事件的超链接的href属性为表单的action属性赋值
                delete_form.action = event.target.href;
                //提交表单
                delete_form.submit();
                //阻止超链接的默认跳转行为
                event.preventDefault();
            }
        }
    });
</script>
```

在控制器中添加方法：

```java
 // ...
import org.springframework.web.bind.annotation.PathVariable;
// ...

@RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
public String deleteEmployee(@PathVariable("id") Integer id){
    employeeDao.delete(id);
    return "redirect:/employee";
}
```

### 添加员工

配置view-controller

```xml
<mvc:view-controller path="/toAdd" view-name="employee_add"></mvc:view-controller>
```

创建employee_add.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Add Employee</title>
</head>
<body>

<form th:action="@{/employee}" method="post">
    lastName:<input type="text" name="lastName"><br>
    email:<input type="text" name="email"><br>
    gender:<input type="radio" name="gender" value="1">male
    <input type="radio" name="gender" value="0">female<br>
    <input type="submit" value="add"><br>
</form>

</body>
</html>
```

在配置文件中设置：

```xml
<mvc:view-controller path="/toAdd" view-name="employee_add"/>
```

在控制器中添加方法：

```java
@RequestMapping(value = "/employee", method = RequestMethod.POST)
public String addEmployee(Employee employee){
    employeeDao.save(employee);
    return "redirect:/employee";
}
```

### 更新员工信息

在控制器中添加方法：

```java
@RequestMapping(value = "/employee/{id}", method = RequestMethod.GET)
public String getEmployeeById(@PathVariable("id") Integer id, Model model){
    Employee employee = employeeDao.get(id);
    model.addAttribute("employee", employee);
    return "employee_update";
}
```

创建employee_update.html：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Update Employee</title>
</head>
<body>

<form th:action="@{/employee}" method="post">
    <input type="hidden" name="_method" value="put">
    <input type="hidden" name="id" th:value="${employee.id}">
    lastName:<input type="text" name="lastName" th:value="${employee.lastName}"><br>
    email:<input type="text" name="email" th:value="${employee.email}"><br>
    <!--
        th:field="${employee.gender}"可用于单选框或复选框的回显
        若单选框的value和employee.gender的值一致，则添加checked="checked"属性
    -->
    gender:<input type="radio" name="gender" value="1" th:field="${employee.gender}">male
    <input type="radio" name="gender" value="0" th:field="${employee.gender}">female<br>
    <input type="submit" value="update"><br>
</form>

</body>
</html>
```

在控制器中添加方法：

```java
@RequestMapping(value = "/employee", method = RequestMethod.PUT)
public String updateEmployee(Employee employee){
    employeeDao.save(employee);
    return "redirect:/employee";
}
```

# `HttpMessageConverter`

**报文信息转换器（`HttpMessageConverter`）**用于将请求报文转换为Java对象，或将Java对象转换为响应报文。

`org.springframework.http.converter.HttpMessageConverter`提供了两个注解和两个类型：`@RequestBody`、`@ResponseBody`、`RequestEntity`、`ResponseEntity`。

## `@RequestBody`

`@RequestBody`可以获取请求体：在控制器方法设置一个形参，使用`@RequestBody`进行标识，当前请求的请求体就会为当前注解所标识的形参赋值。

创建控制器：

```java
package com.example.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testHttpMessageConverter")
public class HttpMessageConverterController {

    @RequestMapping("/RequestBody")
    public String testRequestBody(@RequestBody String requestBody) {
        System.out.println("requestBody: " + requestBody);
        return "target";
    }

}
```

在index.html文件中添加表单：

```html
<form th:action="@{/testHttpMessageConverter/RequestBody}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit" value="测试@RequestBody">
</form>
```

填写并提交表单，页面成功跳转，且控制台输出正确的请求体。

## `RequestEntity`

`RequestEntity`可以封装请求报文：需要在控制器方法的形参中设置该类型的形参，当前请求的请求报文就会赋值给该形参，可以通过`getHeaders`方法获取请求头信息，通过`getBody`方法获取请求体信息。

在控制器中添加方法：

```java
// ...
import org.springframework.http.RequestEntity;
// ...

@RequestMapping("/testRequestEntity")
public String testRequestEntity(RequestEntity<String> requestEntity){
    System.out.println("requestHeader: "+requestEntity.getHeaders());
    System.out.println("requestBody: "+requestEntity.getBody());
    return "success";
}
```

在index.html文件中添加表单：

```html
<form th:action="@{/testHttpMessageConverter/RequestEntity}" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    <input type="submit" value="测试@RequestEntity">
</form>
```

填写并提交表单，页面成功跳转，且控制台输出正确的请求头与请求体。

## `@ResponseBody`

`@ResponseBody`用于标识一个控制器方法，可以将该方法的返回值直接作为响应报文的响应体响应到浏览器。

在控制器中添加方法：

```java
// ...
import org.springframework.web.bind.annotation.ResponseBody;
// ...

@RequestMapping("/ResponseBody")
@ResponseBody
public String testResponseBody() {
    return "success";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testHttpMessageConverter/ResponseBody}">测试@ResponseBody</a><br>
```

点击超链接，浏览器页面显示“success”。

当然，也可以通过ServletAPI的`HttpServletResponse`对象响应浏览器数据：

```java
// ...
import javax.servlet.http.HttpServletResponse;
// ...

@RequestMapping("/ResponseBody")
public void testResponseBody(HttpServletResponse response) throws IOException {
    response.getWriter().print("success");
}
```

注意，`@ResponseBody`标注的方法不能返回一个pojo对象（浏览器只能接收文本）。

## SpringMVC处理JSON

可以将pojo对象转换为JSON字符串，从而让`@ResponseBody`标注的方法可以返回一个pojo对象。

引入依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.2.2</version>
</dependency>
```

在控制器中添加方法：

```java
import com.example.springmvc.pojo.User;

@RequestMapping("/ResponseBodyJSON")
@ResponseBody
public User testResponseBodyJSON() {
    return new User(1, "Alice", "123456", 17, "女", "Alice@qq.com");
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testHttpMessageConverter/ResponseBodyJSON}">测试@ResponseBody返回JSON</a><br>
```

点击超链接，浏览器页面显示正确的`User`。

实际上，让控制器方法的返回值转换为JSON，需要4个步骤：

- 引入相关依赖。
- 开启注解驱动。此时在`HandlerAdaptor`中会自动装配一个消息转换器：`MappingJackson2HttpMessageConverter`，可以将响应到浏览器的Java对象转换为JSON格式的字符串
- 在处理器方法上使用`@ResponseBody`注解进行标识。
- 将Java对象直接作为控制器方法的返回值返回，就会自动转换为JSON格式的字符串（不是JSON对象）。

## SpringMVC处理Ajax

SpringMVC处理Ajax没有什么特殊之处，只是其发送请求方式不一样，页面不刷新、不进行交互，因此不能使用转发与重定向，只能响应浏览器数据。

在控制器中添加方法：

```java
@RequestMapping("/testHttpMessageConverter/Ajax")
@ResponseBody
public String testAjax(String username, String password){
    System.out.println("username: " + username + ", password: " + password);
    return "success";
}
```

在index.html文件中添加以下内容：

```html
<div id="app">
    <a th:href="@{/Ajax}" @click="testAjax">测试SpringMVC处理ajax</a>
</div>
<script type="text/javascript" th:src="@{/static/js/vue.js}"></script>
<!-- 引入axios.min.js文件 -->
<script type="text/javascript" th:src="@{/static/js/axios.min.js}"></script>
<script type="text/javascript">
    var vue = new Vue({
        el:"#app",
        methods:{
            testAjax:function (event) {
                axios({
                    method:"post",
                    url:event.target.href,
                    params:{
                        username:"admin",
                        password:"123456"
                    }
                }).then(function (response) {
                    alert(response.data);
                });
                event.preventDefault();
            }
        }
    });
</script>
```

点击超链接，控制台输出正确的`username`与`password`，且浏览器弹出正确的弹出框。

## `@RestController`

`org.springframework.web.bind.annotation.RestController`注解是springMVC提供的一个复合注解，标识在控制器的类上，就相当于为类添加了`@Controller`注解，并且为其中的每个方法添加了`@ResponseBody`注解。

## `@ResponseEntity`

`@ResponseEntity`用于控制器方法的返回值类型，该控制器方法的返回值就是响应到浏览器的响应报文。

下面通过该注解实现文件上传与下载。

添加依赖，用于上传文件：

```xml
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
```

在SpringMVC的配置文件中配置文件上传解析器：

```xml
<!-- 必须通过文件解析器的解析才能将上传的文件转换（封装）为MultipartFile对象 -->
<!-- SpringMVC通过id获取该Bean，因此id不可少，且值固定 -->
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"/>
```

在index.html文件中添加以下内容：

```html
<a th:href="@{/testFileUpAndDown/testDown}">测试文件下载</a>
<!-- 文件上传要求form表单的请求方式必须为POST，并且添加属性enctype="multipart/form-data" -->
<form th:action="@{/testFileUpAndDown/testUp}" method="post" enctype="multipart/form-data">
    头像：<input type="file" name="photo"><br>
    <input type="submit" value="测试文件上传">
</form>
```

创建控制器：

```java
package com.example.springmvc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
@RequestMapping("/testFileUpAndDown")
public class FileUpAndDownController {

    @RequestMapping("/testDown")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session) throws IOException {
        ServletContext servletContext = session.getServletContext();
        // 获取服务器中文件的真实路径
        String realPath = servletContext.getRealPath("/static/img/1.jpg");
        InputStream is = new FileInputStream(realPath);
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        // 创建HttpHeaders对象设置响应头信息
        MultiValueMap<String, String> headers = new HttpHeaders();
        // 设置要下载方式以及下载文件的名字
        headers.add("Content-Disposition", "attachment;filename=1.jpg");
        // 设置响应状态码
        HttpStatus statusCode = HttpStatus.OK;
        // 创建ResponseEntity对象
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
        is.close();
        return responseEntity;
    }

    @RequestMapping("/testUp")
    // SpringMVC中将上传的文件封装到MultipartFile对象中，通过此对象可以获取文件相关信息
    public String testUp(MultipartFile photo, HttpSession session) throws IOException {
        // 获取上传的文件的文件名
        String fileName = photo.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            return "target";
        }
        // 处理文件重名问题
        int index = fileName.lastIndexOf(".");
        String suffix = index >= 0 ? fileName.substring(index) : "";
        fileName = UUID.randomUUID().toString() + suffix;
        // 获取服务器中photo目录的路径
        ServletContext servletContext = session.getServletContext();
        String photoPath = servletContext.getRealPath("photo");
        File file = new File(photoPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String finalPath = photoPath + File.separator + fileName;
        // 实现上传功能
        photo.transferTo(new File(finalPath));
        return "target";
    }

}
```

这样通过超链接或表单就能实现文件上传与下载。

# 拦截器

SpringMVC中的拦截器用于拦截控制器方法的执行。

过滤器作用在浏览器与`DispatcherServlet`之间，而拦截器作用在控制器执行前后。

SpringMVC中的拦截器有三个抽象方法：

- `preHandle`：控制器方法执行之前执行`preHandle`，其`boolean`类型的返回值表示是否拦截或放行。返回`true`为放行，即调用控制器方法；返回`false`表示拦截，即不调用控制器方法。
- `postHandle`：控制器方法执行之后执行`postHandle`。
- `afterCompletion`：处理完视图和模型数据，渲染视图完毕之后执行`afterCompletion`。

拦截器的执行顺序（注意SpringMVC本身可能创建了若干拦截器）：

- 若每个拦截器的`preHandle`都返回`true`，此时多个拦截器的执行顺序和拦截器在SpringMVC的配置文件的配置顺序有关：`preHandle`会按照配置的顺序执行，而`postHandle`和`afterCompletion`会按照配置的反序执行。


- 若某个拦截器的`preHandle`返回了`false`，返回`false`的`preHandle`和它之前的拦截器的`preHandle`都会执行，`postHandle`都不执行（控制器方法也不会执行），返回`false`的拦截器之前的拦截器的`afterCompletion`会执行。

SpringMVC中的拦截器需要实现`HandlerInterceptor`：

```java
package com.example.springmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FirstInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("FirstInterceptor, preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("FirstInterceptor, postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("FirstInterceptor, afterCompletion");
    }

}
```

SpringMVC的拦截器必须在SpringMVC的配置文件中进行配置：

```xml
<mvc:interceptors>
    <bean class="com.example.springmvc.interceptor.FirstInterceptor"/>
</mvc:interceptors>
```

或者将`FirstInterceptor`类标识为一个Bean（通过在类上添加`org.springframework.stereotype.Controller`等方式），然后配置：

```xml
<mvc:interceptors>
    <ref bean="firstInterceptor"/>
</mvc:interceptors>
```

点击任意超链接，`FirstInterceptor`中重写的三个方法都会执行。

以上两种配置方式都是对`DispatcherServlet`所处理的所有的请求进行拦截，要想控制拦截路径，可以使用`<mvc:interceptor>`标签：

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <!-- 设置需要拦截的请求，这里的/*只表示上下文路径下的一层目录，如果要拦截所有请求，使用/** -->
        <mvc:mapping path="/*"/>
        <!-- 设置需要排除的请求 -->
        <mvc:exclude-mapping path="/"/>
        <ref bean="firstInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

# 异常处理器

SpringMVC提供了一个处理控制器方法执行过程中所出现的异常的接口：`org.springframework.web.servlet.HandlerExceptionResolver`，它的实现类包括：`org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver`（SpringMVC默认使用的异常处理器）和`org.springframework.web.servlet.handler.SimpleMappingExceptionResolver`。

`HandlerExceptionResolver`的`resolveException`方法作用是：如果在控制器方法执行过程中出现了指定异常，它会返回一个新的`ModelAndView`，代替原来方法要返回的`ModelAndView`。

`SimpleMappingExceptionResolver`是SpringMVC提供的自定义的异常处理器，配置方式如下：

```xml
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
    <property name="exceptionMappings">
        <props>
            <!-- properties的键表示处理器方法执行过程中出现的异常；properties的值表示若出现指定异常时，设置一个新的视图名称（遵循视图规则，即可以有前缀），跳转到指定页面 -->
            <prop key="java.lang.ArithmeticException">error</prop>
        </props>
    </property>
    <!-- exceptionAttribute属性设置一个属性名，将出现的异常信息在请求域中进行共享（设置将异常信息共享在请求域中的键） -->
    <property name="exceptionAttribute" value="ex"/>
</bean>
```

在`HelloController`控制器中添加方法：

```java
@RequestMapping("/testExceptionHandler")
public String testExceptionHandler() {
    int i = 10 / 0;
    return "target";
}
```

在index.html文件中添加超链接：

```html
<a th:href="@{/testExceptionHandler}">测试异常处理</a>
```

新建error.html：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>错误</title>
</head>
<body>
错误页面
<p th:text="${ex}"></p>
</body>
</html>
```

点击超链接，页面跳转到error.html，且展示正确的异常信息。

也可以基于注解进行异常处理：

```java
package com.example.springmvc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// @ControllerAdvice将当前类标识为异常处理的组件
// 该注解具有@Component的功能
@ControllerAdvice
public class ExceptionController {

    // @ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(ArithmeticException.class)
    // ex表示当前请求处理中出现的异常对象
    public String handleArithmeticException(Exception ex, Model model){
        model.addAttribute("ex", ex);
        return "error";
    }

}
```

# 注解配置SpringMVC

下面使用配置类和注解代替web.xml和SpringMVC配置文件。

## 创建初始化类，代替web.xml

在Servlet3.0环境中，容器会在类路径中查找实现`javax.servlet.ServletContainerInitializer`接口的类，如果找到的话就用它来配置Servlet容器（即Tomcat服务器）。
Spring提供了这个接口的实现，名为`org.springframework.web.SpringServletContainerInitializer`，这个类反过来又会查找实现`org.springframework.web.WebApplicationInitializer`的类并将配置的任务交给它们来完成。Spring3.2引入了一个便利的`WebApplicationInitializer`基础实现，名为`org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer`，当我们的类扩展了`AbstractAnnotationConfigDispatcherServletInitializer`并将其部署到Servlet3.0容器的时候，容器会自动发现它，并用它来配置Servlet上下文：

```java
package com.example.springmvc.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * 指定Spring的配置类
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }

    /**
     * 指定SpringMVC的配置类
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * 指定DispatcherServlet的映射规则，即url-pattern
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 添加过滤器
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceRequestEncoding(true);
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{characterEncodingFilter, hiddenHttpMethodFilter};
    }

}
```

## 创建SpringConfig配置类，代替Spring的配置文件

```java
package com.example.springmvc.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {}
```

## 创建WebConfig配置类，代替SpringMVC的配置文件

引入相关依赖：

```xml
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf</artifactId>
    <version>3.0.15.RELEASE</version>
</dependency>
```

```java
package com.example.springmvc.config;

import com.example.springmvc.interceptor.FirstInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;
import java.util.Properties;

/**
 * 代替SpringMVC的配置文件
 */
@Configuration
// 扫描组件
@ComponentScan("com.example.springmvc")
// 开启MVC注解驱动
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    // 配置default-servlet-handler：使用默认的servlet处理静态资源
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    // 配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        FirstInterceptor interceptor = new FirstInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns("/**");  // 与配置文件中的不同
    }

    // 配置view-controller
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/target").setViewName("/target");
    }

    // 配置文件上传解析器
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    // 配置异常映射
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("java.lang.ArithmeticException", "error");
        exceptionResolver.setExceptionMappings(properties);
        exceptionResolver.setExceptionAttribute("ex");
        resolvers.add(exceptionResolver);
    }

    // 视图解析器：配置生成模板解析器
    @Bean
    public ITemplateResolver templateResolver() {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        // ServletContextTemplateResolver需要一个ServletContext作为构造参数，可通过WebApplicationContext 的方法获得
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(webApplicationContext.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    // 视图解析器：生成模板引擎并为模板引擎注入模板解析器
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    // 视图解析器：生成视图解析器并未解析器注入模板引擎
    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }

}
```

# SpringMVC执行流程

## SpringMVC常用组件

`DispatcherServlet`：前端控制器，不需要工程师开发，由框架提供。作用：统一处理请求和响应，整个流程控制的中心，由它调用其它组件处理用户的请求。

`HandlerMapping`：处理器映射器，不需要工程师开发，由框架提供。作用：根据请求的URL、`method`等信息查找Handler，即控制器方法。

Handler：处理器（就是控制器），需要工程师开发。作用：在`DispatcherServlet`的控制下Handler对具体的用户请求进行处理。

`HandlerAdapter`：处理器适配器，不需要工程师开发，由框架提供。作用：通过`HandlerAdapter`执行处理器（控制器方法）。

`ViewResolver`：视图解析器，不需要工程师开发，由框架提供。例如：`ThymeleafView`、`InternalResourceView`、`RedirectView`等。作用：进行视图解析，得到相应的视图。

`View`：视图。作用：将模型数据通过页面展示给用户。

## SpringMVC的执行流程

用户向服务器发送请求，请求被SpringMVC前端控制器`DispatcherServlet`捕获。

`DispatcherServlet`对请求URL进行解析，得到请求资源标识符（URI），判断请求URI对应的映射是否存在：

- 如果不存在，再判断是否配置了`mvc:default-servlet-handler`。如果没配置，则控制台报映射查找不到，客户端展示404错误；如果有配置，则访问目标资源（一般为静态资源，如：JS、CSS、HTML），找不到资源客户端也会展示404错误。
- 存在则执行下面的流程：
  - 根据该URI，调用`HandlerMapping`获得该Handler配置的所有相关的对象（包括Handler对象以及Handler对象对应的拦截器），最后以`HandlerExecutionChain`执行链对象的形式返回。
  - `DispatcherServlet`根据获得的Handler，选择一个合适的`HandlerAdapter`。
  - 如果成功获得`HandlerAdapter`，此时将开始（正向）执行拦截器的`preHandle`方法。
  - 提取Request中的模型数据，填充Handler入参，开始执行Handler方法，处理请求。在填充Handler的入参过程中，根据你的配置，Spring将帮你做一些额外的工作：
    - `HttpMessageConveter`：将请求消息（如JSON、XML等数据）转换成一个对象，将对象转换为指定的响应信息。
    - 数据转换：对请求消息进行数据转换。如`String`转换成`Integer`、`Double`等。
    - 数据格式化：对请求消息进行数据格式化。 如将字符串转换成格式化数字或格式化日期等。
    - 数据验证： 验证数据的有效性（长度、格式等），验证结果存储到`org.springframework.validation.BindingResult`或`org.springframework.validation.Errors`中。
  - Handler执行完成后，向`DispatcherServlet`返回一个`ModelAndView`对象。
  - 此时将开始（逆向）执行拦截器的`postHandle`。
  - 根据返回的`ModelAndView`，选择一个适合的`ViewResolver`进行视图解析（例如`ThymeleafView`、`InternalResourceView`、`RedirectView`等），根据`Model`与`View`，来渲染视图。（在这之前会判断是否存在异常，如果存在异常，则执行`HandlerExceptionResolver`进行异常处理）
  - 渲染视图完毕（逆向）执行拦截器的`afterCompletion`。
  - 将渲染结果返回给客户端。

# 参考

[^1]: [【尚硅谷】SpringMVC教程丨一套快速上手spring mvc](https://www.bilibili.com/video/BV1Ry4y1574R)
[^1]: [尚硅谷2021全新SpringMVC教程](https://pan.baidu.com/s/1uHmFSf2M3WvPjjp9KyDAvQ#list/path=%2F)，提取码：yyds
[^2]: [黑马程序员SpringBoot2全套视频教程，springboot零基础到项目实战（spring boot2完整版）](https://www.bilibili.com/video/BV15b4y1a7yG?p=12)
