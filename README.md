# Vhr
一个前后端分离的人事管理系统 <br>
起始框架参考：https://github.com/lenve/vhr
## 项目技术栈
### 后端技术栈
1. Spring Boot
2. Spring Security
3. JWT
4. MyBatis
5. MySQL
6. Redis
7. RabbitMQ
8. Spring Cache
9. WebSocket
10. Swagger
11. ...
### 前端技术栈
1. Vue
2. ElementUI
3. axios
4. vue-router
5. Vuex
## 对系统模块进行了研究，并且开发部分功能：
- 使用Swagger自动生成后端接口文档，避免人工编写维护文档的麻烦
- 针对MySQL进行优化，构建索引，使用存储过程处理复杂SQL语句
- 利用Spring Security的RBAC模型完成权限认证模块的开发
- 分别利用Spring Security+session和Spring Security+JWT+Redis完成登录模块的开发，解决多端登录踢出，Token的续签，注销，失效的问题
- 利用RabbitMQ消息和Redis完成邮件模块的开发，保证邮件发送的可靠性，避免消息被重复消费 
 
