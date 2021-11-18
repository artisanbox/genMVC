# genMVC - 好用的 MVC 框架
- 基于教学的 MVC 框架, MySQL 作为数据库
- 学习本框架对 Java 基本知识有更全面的理解和认识
- 利用反射, 注释, 泛型等常见知识实现框架
- 扩展性高, 后期可利用 Socket 实现自己的 HTTP 服务器

# 框架结构
## Controller
- 控制层, 通过控制台输入执行指定函数, 并自动将输入传给函数参数
## Mapper
- 数据库映射, 自动生成 SQL 语句
## Service
- 服务层, 通过简单 @Aspect 注释自动实现拦截
## Model
- 模型对应的数据库表, 通过简单 @Table 配置对应数据库信息, 设置表名和主键
- 通过 @Column 配置对应数据库表字段, 解决查询字段不一致问题
## Aspect
- 利用 cglib 实现的 AOP, 可以实现多重拦截, 内置事务

# 安装
## maven
```
<dependency>
  <groupId>cn.juantu.vip</groupId>
  <artifactId>genMVC</artifactId>
  <version>2.1</version>
</dependency>
```

## jar 包
- 通过 release 下载https://github.com/artisanbox/genMVC/releases/tag/Jar


# Demo
- 通过 /demo 目录内置项目即可学习
- 也提供了对应的 maven demo 项目
