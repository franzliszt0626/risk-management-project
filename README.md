# 🛡️ Risk Management System（风险管理系统）

> 一个基于 Spring Boot + MyBatis-Plus 的企业级工人安全风险监控与预警系统，支持实时状态追踪、风险指标分析与异常告警。

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5+-orange)](https://baomidou.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🌟 功能亮点

- ✅ **工人全生命周期管理**：增删改查、状态跟踪（正常/异常/离线）
- ✅ **多维度查询**：按姓名（模糊）、工号、岗位、工作类型、状态筛选
- ✅ **统一响应格式**：所有接口返回标准化 `Result<T>` 结构
- ✅ **全局异常处理**：自动捕获数据库异常、业务异常并友好提示
- ✅ **枚举友好支持**：`Status` / `WorkType` 自动映射数据库字符串字段
- ✅ **Swagger API 文档**：自动生成 RESTful 接口文档
- ✅ **分层架构清晰**：`PO` / `DTO` / `VO` 严格分离，符合 DDD 思想

---

## 🧰 技术栈

| 类别 | 技术 |
|------|------|
| **核心框架** | Spring Boot 3.x |
| **ORM 框架** | MyBatis-Plus 3.5+ |
| **数据库** | MySQL 8.0+ |
| **工具库** | Hutool（对象转换）、Lombok（简化代码） |
| **API 文档** | Swagger 3 (OpenAPI) |
| **构建工具** | Maven |
| **异常处理** | 全局 `@ControllerAdvice` + 自定义 `BizException` |
| **代码生成** | MyBatis-Plus FastAutoGenerator |

---

## 🚀 快速启动

### 1. 克隆项目
```bash
git clone https://github.com/your-username/risk-management-project.git
cd risk-management-project
```

### 2. 配置数据库
- 创建 MySQL 数据库：
  ```sql
  CREATE DATABASE `risk-management-project` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
- 修改 `application.yml` 中的数据源配置：
  ```yaml
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/risk-management-project?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: root
      password: your_password
  ```

### 3. 初始化表结构
运行项目根目录下的 SQL 脚本（如有），或依赖 MyBatis-Plus 自动建表（需开启 `mybatis-plus.global-config.db-config.id-type=auto`）。

> 💡 表名示例：`t_worker`, `t_alert_record`, `t_risk_indicator` 等。

### 4. 启动应用
```bash
./mvnw spring-boot:run
# 或在 IDE 中直接运行主类
```

### 5. 访问服务
- **API 接口**：`http://localhost:8080/api/workers`
- **Swagger 文档**：`http://localhost:8080/swagger-ui.html`

---

## 📂 项目结构

```
src/main/java
└── gang.lu.riskmanagementproject
    ├── common                 # 通用模块
    │   ├── Result.java        # 统一返回结果
    │   ├── exception/         # 自定义异常
    │   └── handler/           # 全局异常处理器
    ├── domain                 # 领域模型
    │   ├── po/                # 持久化对象（与数据库表对应）
    │   ├── dto/               # 数据传输对象（前端传入）
    │   ├── vo/                # 视图对象（返回前端）
    │   └── enums/             # 枚举（Status, WorkType）
    ├── application            # 应用层
    │   └── service/           # 业务接口
    ├── infrastructure         # 基础设施层
    │   ├── mapper/            # MyBatis-Plus Mapper
    │   └── service/impl/      # 业务实现
    ├── interfaces             # 接口层
    │   └── controller/        # REST 控制器
    └── RiskManagementProjectApplication.java  # 启动类
```

---

## 🧪 示例 API

### 创建工人
```http
POST /api/workers
Content-Type: application/json

{
  "workerCode": "W1001",
  "name": "张三",
  "position": "焊工",
  "workYears": 5,
  "workType": "HIGH_ALTITUDE",
  "status": "NORMAL"
}
```

### 按状态查询
```http
GET /api/workers/status/正常
```

✅ 成功响应：
```json
{
  "code": 1,
  "message": "success",
  "data": [
    {
      "workerCode": "W1001",
      "name": "张三",
      "position": "焊工",
      "workYears": 5,
      "workType": "高空作业",
      "status": "正常"
    }
  ],
  "httpStatus": 200
}
```

❌ 错误响应（如状态无效）：
```json
{
  "code": 0,
  "message": "无效的工人状态: [在线]，允许值为：正常、异常、离线",
  "data": null,
  "httpStatus": 400
}
```

---

## 🛠️ 开发规范

- **异常处理**：Service 层抛出 `BizException`，Controller 不捕获异常
- **对象转换**：使用 `Hutool.BeanUtil.copyProperties()` 进行 PO/DTO/VO 转换
- **枚举存储**：数据库字段存储 `@EnumValue` 对应的字符串值（如 `"正常"`）
- **REST 风格**：启用 `@RestController` + `@EnableRestStyle`

---

## 📜 License

MIT License — 允许自由使用、修改和分发。

---

## 👨‍💻 作者

**Franz Liszt**  

---

> 💡 **提示**：将 `your-username` 替换为你的 GitHub 用户名，并根据实际部署端口、包名等微调内容。  
> 欢迎 PR & Issue！
