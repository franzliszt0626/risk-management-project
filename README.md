# 🛡️ 路港桥隧工程风险管理系统（后台）

> 面向路港桥隧工程场景的工人安全风险监控与预警系统，基于 Spring Boot + MyBatis-Plus 构建，聚焦工程现场工人健康指标监测、风险等级评估、预警记录管理与工作区域风险管控，为工程安全管理提供全流程数字化支撑。

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5+-orange)](https://baomidou.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 🌟 核心功能

### 🧑‍🏭 工人管理模块
- ✅ 工人基础信息全生命周期管理（增删改查）
- ✅ 多维度精准查询：工号、姓名（模糊）、岗位、工种、状态（正常/异常/离线）
- ✅ 工号唯一性校验、枚举参数合法性校验
- ✅ 分页查询支持（默认20条/页，最大50条）

### 📊 风险指标模块
- ✅ 实时风险指标录入（心率、呼吸频率、疲劳度、风险等级）
- ✅ 工人最新风险指标查询、历史风险指标分页查询
- ✅ 风险等级人数分布统计、当日各时段高风险工人数统计
- ✅ 指标参数合法性校验（心率范围、疲劳度百分比等）

### ⚠️ 预警记录模块
- ✅ 预警记录新增、删除、修改、标记已处理
- ✅ 多维度查询：工人ID、预警等级、预警类型（模糊）
- ✅ 预警等级合法性校验、处理人非空校验

### 🏗️ 工作区域模块
- ✅ 工作区域基础信息管理（增删改查）
- ✅ 按区域编码/名称/风险等级多条件分页查询
- ✅ 工作区域风险等级数量统计
- ✅ 区域编码唯一性校验、风险等级枚举校验

### 🛡️ 通用能力
- ✅ 统一响应格式：所有接口返回标准化 `Result<T>` 结构
- ✅ 全局异常处理：自动捕获业务异常/数据库异常并返回友好提示
- ✅ 枚举友好支持：风险等级/区域风险等级/工种/状态自动映射数据库字段
- ✅ 标准化日志输出：关键操作日志可追溯、可审计
- ✅ Swagger API 文档：自动生成 RESTful 接口文档，便于联调

---

## 🧰 技术栈选型

| 类别         | 技术/组件                          | 作用说明                     |
|--------------|------------------------------------|------------------------------|
| 核心框架     | Spring Boot 3.x                    | 快速构建企业级应用           |
| ORM 框架     | MyBatis-Plus 3.5+                 | 简化数据库操作、分页查询     |
| 数据库       | MySQL 8.0+                         | 关系型数据库存储业务数据     |
| 工具库       | Hutool                             | 对象转换、参数校验、类型处理 |
| 代码简化     | Lombok                             | 消除冗余代码（Getter/Setter）|
| API 文档     | Swagger 3 (OpenAPI)                | 自动生成接口文档             |
| 异常处理     | 自定义 BizException + 全局异常处理器 | 统一异常响应格式             |
| 构建工具     | Maven                              | 项目构建、依赖管理           |

---

## 🚀 快速启动

### 1. 克隆项目
```bash
git clone https://github.com/your-username/lugangqiaosui-risk-management.git
cd lugangqiaosui-risk-management
```

### 2. 数据库配置
#### 2.1 创建数据库
```sql
CREATE DATABASE `lugangqiaosui_risk_management` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 2.2 修改数据源配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lugangqiaosui_risk_management?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root  # 替换为你的数据库用户名
    password: 123456  # 替换为你的数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 打印SQL日志（开发环境）
  global-config:
    db-config:
      id-type: auto  # 主键自增
      table-prefix: t_  # 表名前缀
```

### 3. 初始化表结构
执行项目根目录下的 `sql/init_schema.sql` 脚本，创建核心业务表：
```bash
mysql -u root -p lugangqiaosui_risk_management < sql/init_schema.sql
```

> 📌 核心表清单：
> - `t_worker`：工人基础信息表
> - `t_risk_indicator`：实时风险指标表
> - `t_alert_record`：预警记录表
> - `t_work_area`：工作区域表

### 4. 启动应用
```bash
# 使用Maven启动
./mvnw spring-boot:run

# 或打包后运行
./mvnw clean package -DskipTests
java -jar target/lugangqiaosui-risk-management-1.0.0.jar
```

### 5. 访问服务
- **接口根路径**：`http://localhost:8080/api`
- **Swagger 文档**：`http://localhost:8080/swagger-ui/index.html`（接口调试/查看）
- **健康检查**：`http://localhost:8080/actuator/health`

---

## 📂 项目结构（DDD分层）

```
src/main/java/gang/lu/riskmanagementproject/
├── common/                # 通用核心模块
│   ├── Result.java        # 统一返回结果封装
│   ├── FailureMessages.java # 错误消息常量
│   ├── BusinessScene.java # 业务场景枚举
│   ├── exception/         # 自定义异常（BizException）
│   └── handler/           # 全局异常处理器（GlobalExceptionHandler）
├── domain/                # 领域模型层
│   ├── po/                # 持久化对象（与数据库表一一对应）
│   │   ├── Worker.java
│   │   ├── RiskIndicator.java
│   │   ├── AlertRecord.java
│   │   └── WorkArea.java
│   ├── dto/               # 数据传输对象（前端入参）
│   │   ├── WorkerDTO.java
│   │   ├── RiskIndicatorDTO.java
│   │   └── WorkAreaDTO.java
│   ├── vo/                # 视图对象（返回前端）
│   │   ├── WorkerVO.java
│   │   ├── RiskLevelCountVO.java
│   │   └── WorkAreaVO.java
│   └── enums/             # 业务枚举
│       ├── Status.java    # 工人状态（正常/异常/离线）
│       ├── WorkType.java  # 工种（高空/焊接/普通作业等）
│       ├── RiskLevel.java # 风险等级（低/中/高/严重）
│       └── AreaRiskLevel.java # 区域风险等级
├── util/                  # 工具类
│   ├── BusinessVerifyUtil.java # 业务参数校验工具
│   ├── ConvertUtil.java   # PO/DTO/VO转换工具
│   └── ParameterVerifyUtil.java # 指标参数校验工具
├── mapper/                # 数据访问层（MyBatis-Plus Mapper）
│   ├── WorkerMapper.java
│   ├── RiskIndicatorMapper.java
│   └── WorkAreaMapper.java
├── service/               # 业务层
│   ├── impl/              # 业务实现类
│   │   ├── WorkerServiceImpl.java
│   │   ├── RiskIndicatorServiceImpl.java
│   │   └── WorkAreaServiceImpl.java
│   └── *.java             # 业务接口
├── controller/            # 接口层（REST Controller）
│   ├── WorkerController.java
│   ├── RiskIndicatorController.java
│   └── WorkAreaController.java
└── RiskManagementProjectApplication.java # 应用启动类
```

---

## 🧪 核心接口示例

### 1. 工人管理
#### 创建工人
```http
POST /api/workers
Content-Type: application/json

{
  "workerCode": "W2026001",
  "name": "李四",
  "position": "桥梁焊接工",
  "workYears": 8,
  "workType": "HIGH_ALTITUDE",
  "status": "NORMAL"
}
```

#### 分页查询所有工人
```http
GET /api/workers?pageNum=1&pageSize=10
```

### 2. 风险指标管理
#### 新增工人风险指标
```http
POST /api/risk-indicators
Content-Type: application/json

{
  "workerId": 1,
  "heartRate": 85,
  "respiratoryRate": 18,
  "fatiguePercent": 75,
  "riskLevel": "HIGH_RISK",
  "recordTime": "2026-02-08 14:30:00"
}
```

#### 统计风险等级人数分布
```http
GET /api/risk-indicators/count-by-level
```

### 3. 预警记录管理
#### 标记预警记录为已处理
```http
PUT /api/alert-records/1/handle?handledBy=安全管理员
```

### 4. 统一响应格式示例
✅ 成功响应：
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "workerCode": "W2026001",
    "name": "李四",
    "position": "桥梁焊接工",
    "workType": "高空作业",
    "status": "正常"
  },
  "timestamp": 1740000000000
}
```

❌ 业务异常响应：
```json
{
  "code": 400,
  "message": "【创建工人失败】工号重复：W2026001",
  "data": null,
  "timestamp": 1740000000000
}
```

---

## 🛠️ 开发规范

### 1. 编码规范
- 包名：全小写，多级包用 `.` 分隔（如 `gang.lu.riskmanagementproject`）
- 类名：大驼峰（PascalCase），如 `WorkerServiceImpl`
- 方法名：小驼峰（camelCase），如 `validateWorkerExist`
- 常量：全大写，下划线分隔，如 `FAILURE_MESSAGES.WORKER_PARAM_DUPLICATE_CODE`

### 2. 分层开发规范
- **Controller 层**：仅负责接收请求、返回响应，不处理业务逻辑
- **Service 层**：核心业务逻辑处理，抛出 `BizException` 表示业务异常
- **Mapper 层**：仅定义数据库操作接口，使用 MyBatis-Plus 简化 CRUD
- **Util 层**：通用工具类，无业务逻辑，保证幂等/无状态

### 3. 异常处理规范
- 业务异常：手动抛出 `BizException`，携带错误码和提示信息
- 系统异常：由全局异常处理器自动捕获，返回通用错误提示
- 数据库异常：统一转换为友好提示，避免暴露底层信息

### 4. 对象转换规范
- PO → VO/DTO：使用 `ConvertUtil.convert()` 统一处理
- 枚举转换：数据库存储字符串值，通过 `@EnumValue` 映射

---

## 📈 扩展建议

### 1. 功能扩展
- 接入实时数据采集（如手环/传感器），自动录入风险指标
- 增加风险预警推送（短信/钉钉/企业微信）
- 新增工人健康报告生成（PDF/Excel）
- 接入大屏可视化（ECharts/Quick BI）展示风险数据

### 2. 技术扩展
- 引入 Redis 缓存高频查询数据（如工人基础信息、风险统计结果）
- 增加接口权限控制（Spring Security/Spring Cloud Gateway）
- 接入分布式事务（Seata）处理跨表操作
- 增加接口限流（Sentinel）防止高并发冲击

---

## 📜 许可证

MIT License  
允许自由使用、修改和分发，商业/非商业用途均可。

---

## 👨‍💻 作者

**Franz Liszt**  
💬 反馈：欢迎提交 Issue/PR 优化系统功能

---

> 💡 部署提示：
> 1. 生产环境建议关闭 SQL 日志、Swagger 文档
> 2. 配置数据库连接池（HikariCP）参数优化性能
> 3. 新增接口需补充单元测试（JUnit 5）
> 4. 建议配置日志归档（Logback），避免日志文件过大
