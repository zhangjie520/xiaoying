## 前言
这是一个基于springboot开发的在线视频网站
## 使用
通过application进入程序
zyy512zht
## 文档
[thymeleaf] https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#setting-attribute-values
[springboot文档] https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support
[springmvc拦截器文档]https://docs.spring.io/spring/docs/5.0.3.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor
```bash
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar
git pull
mvn clean compile flyway:migrate
mvn package

```"# xiaoying" 
