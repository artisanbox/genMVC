# genMVC v2.2.3 - 好用的 MVC 框架
- 基于教学的 MVC 框架, MySQL 作为数据库
- 学习本框架对 Java 基本知识有更全面的理解和认识
- 利用反射, 注释, 泛型等常见知识实现框架
- 扩展性高, 可利用 Socket 或 Servlet 实现自己的 HTTP 框架

# 框架结构
### Controller
- 控制层, 通过控制台输入执行指定函数, 并自动将输入传给函数参数
### Mapper
- 数据库映射, 通过继承 BaseMapper 自动生成 SQL 语句
### Service
- 服务层, 通过简单 @Aspect 注释自动实现拦截
### Model
- 模型对应的数据库表, 通过简单 @Table 配置对应数据库信息, 设置表名和主键
- 通过 @Column 配置对应数据库表字段, 解决查询字段不一致问题
### Aspect
- 利用 cglib 实现的 AOP, 可以实现多重拦截, 内置事务

# 安装
### maven
```
<dependency>
  <groupId>cn.juantu.vip</groupId>
  <artifactId>genMVC</artifactId>
  <version>2.2.3</version>
</dependency>
```

### jar 包
- https://repo1.maven.org/maven2/cn/juantu/vip/genMVC/2.2.3/genMVC-2.2.3.jar

# Demo 快速上手
### 前言
  - 通过最新 demo 项目即可学习
  
  - 也提供了对应的 maven demo 项目


### 项目结构
- src
  - userDemo (项目包名)
    - aspect (切面 拦截噗器)
    - controller (控制层)
    - mapper (数据库映射)
    - model (模型)
    - service (服务层)
    - Main.java (项目运行入口)
  - application.properties (配置文件)

### 项目启动
- 首先利用 test.sql 文件恢复数据库 
- 在 application.properties 配置数据库
- 把 lib 下的 jar 包加入项目 (maven 项目改为引入依赖)
- 运行 Main.java

### 项目说明
- 读取相关配置信息
- 扫描项目路径下的所有 带有 @Controller 的类进行初始化, 放入 genMVC 管理的容器中
- 并将 Controller 中带有 @Inject 的属性进行自动注入到 Controller 中

# 其他
开 issue 即可