<div align="center">

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=28&pause=1000&color=2E9EF7&center=true&vCenter=true&width=720&lines=%F0%9F%9B%A1%EF%B8%8F+%E8%B7%AF%E6%B8%AF%E6%A1%A5%E9%9A%A7%E5%B7%A5%E7%A8%8B%E9%A3%8E%E9%99%A9%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F;Worker+Safety+Risk+Management+System" alt="Typing SVG" />

<br/>

> **面向路港桥隧工程场景的工人安全风险监控与预警平台**
> 聚焦现场工人健康指标监测、风险等级评估、AI 辅助预测与预警记录全流程管理

<br/>

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis--Plus-3.5+-FF6B35?style=for-the-badge&logo=mybatis&logoColor=white)](https://baomidou.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-22C55E?style=for-the-badge)](LICENSE)

<br/>

</div>

---

## 📖 项目简介

本系统是面向**路港桥隧工程**场景构建的安全管理后台，支持工程现场工人的心率、呼吸率、疲劳百分比等生理指标实时录入，结合视频分析算法与 **Qwen AI 大模型**自动评估风险等级、生成健康建议，并通过预警记录模块实现事件全生命周期管理，为工程安全管理提供数字化支撑。

<br/>

## ✨ 核心亮点

<table>
<tr>
<td width="50%">

**⚙️ 自研通用 CRUD 框架**

基于泛型模板方法模式封装 `BaseCrudService<PO, DTO, VO, Q>` + `BaseCrudServiceImpl`，四个模块仅需实现差异化逻辑，消除了大量样板代码：

- 统一处理增删改查、分页查询、批量删除
- `validateAdd / validateUpdate / validateSearch` 默认空实现，按需覆盖
- 内置存在性校验、数据库结果校验、分页构建

</td>
<td width="50%">

**🤖 AI 智能风险预测**

接入 Qwen 大模型，基于历史风险指标分析工人未来风险趋势：

- 返回风险等级预测、趋势描述（上升 / 平稳 / 下降）与改善建议
- AI 原始响应自动清理 Markdown 代码块，稳定解析 JSON
- 支持一键生成 PDF 风险评估报告导出

</td>
</tr>
<tr>
<td width="50%">

**🎥 视频算法集成**

对接 Python 算法服务，分析上传视频中工人的生理状态：

- 算法结果自动入库，触发风险指标记录与预警流程
- 支持 mp4 / avi / mov 格式，单文件限制 50 MB
- 文件大小、MIME 类型双重校验

</td>
<td width="50%">

**🔍 全链路 AOP 日志 & 多层校验**

- 自定义 `@BusinessLog` / `@ValidateLog` 注解，切面自动记录入参、返回结果、耗时
- 超长字符串（> 500 字符）自动截断，`byte[]` 智能格式化为 KB/MB
- `@ValidId` + `@ValidEnum` 自定义注解，参数错误自动返回中文提示

</td>
</tr>
</table>

<br/>

## 🏗️ 通用 CRUD 框架设计

```
BaseCrudService<PO, DTO, VO, Q>          ← 服务接口：定义标准 CRUD + 差异化钩子
        │
        ▼
BaseCrudServiceImpl<PO,DTO,VO,Q,Mapper,Converter>  ← 模板实现：封装通用流程
        │
        ▼
XxxServiceImpl                           ← 业务实现：只写差异化逻辑
  ├── validateAdd(dto)                   → 新增前业务校验（如工号唯一性）
  ├── validateUpdate(id, dto)            → 修改前业务校验
  ├── validateSearch(queryDTO)           → 查询前业务校验（如时间范围）
  ├── buildQueryWrapper(queryDTO)        → 构建多条件查询 Wrapper
  ├── getNotFoundMsg()                   → 返回「记录不存在」提示文案
  └── getBusinessScene()                 → 返回业务场景标识（用于分页日志）
```

**实际效果：** 四个核心模块（Worker / RiskIndicator / AlertRecord / WorkArea）的 CRUD 实现类代码量减少约 **60%**，新增模块只需继承并实现约 **6 个方法**即可获得完整的增删改查、分页、批量删除能力。

<br/>

## 🗂️ 功能模块

<details open>
<summary><b>🧑‍🏭 工人管理</b></summary>

- 工人信息增删改查，工号全局唯一校验
- 多条件分页查询：工号、姓名（模糊）、岗位、工种、状态
- 统计接口：工人总数、工种分布、状态分布

</details>

<details open>
<summary><b>📊 风险指标管理</b></summary>

- 实时指标录入（心率、呼吸率、疲劳百分比、风险等级）
- 多条件分页查询 + 范围过滤（心率区间、疲劳百分比区间）
- 统计接口：风险等级人数分布、当日各 4 小时时段高风险工人数

</details>

<details open>
<summary><b>⚠️ 预警记录管理</b></summary>

- 预警记录 CRUD，支持标记已处理（幂等保护，不可重复标记）
- 多条件查询：工人 ID、预警等级、预警类型（模糊）、处理状态
- 统计接口：按预警级别统计当前未处理数量

</details>

<details open>
<summary><b>🏗️ 工作区域管理</b></summary>

- 区域信息 CRUD，区域编码全局唯一校验
- 多条件分页查询：编码、名称（模糊）、风险等级
- 统计接口：各风险等级区域数量分布

</details>

<details open>
<summary><b>🤖 AI 分析 & 视频分析</b></summary>

- `GET /api/risk-ai/predict/{workerId}`：调用 Qwen 分析历史风险数据，返回预测结果
- `GET /api/risk-report/export/{workerId}`：生成 PDF 风险评估报告
- `POST /api/video-analysis/analyze/{workerId}`：上传视频 → 算法分析 → 自动入库

</details>

<br/>

## 🧰 技术栈

| 类别 | 技术 / 组件 | 说明 |
|------|------------|------|
| 核心框架 | Spring Boot 3.x | 应用脚手架 |
| ORM | MyBatis-Plus 3.5+ | CRUD 简化 + 分页 |
| 数据库 | MySQL 8.0+ | 主数据存储 |
| AI 服务 | Qwen（通义千问） | 风险趋势预测 |
| 工具库 | Hutool | 类型处理 / 反射 / 集合工具 |
| 代码简化 | Lombok | 消除样板代码 |
| 校验框架 | Bean Validation + 自定义注解 | 参数合法性校验 |
| AOP | Spring AOP | 业务日志 / 校验日志切面 |
| API 文档 | Swagger 3 (Springfox) | 接口文档自动生成 |
| 构建工具 | Maven | 依赖管理 / 打包 |

<br/>

## 🚀 快速启动

### 前置条件

- JDK 17+
- MySQL 8.0+
- Maven 3.8+

### 1. 克隆项目

```bash
git clone https://github.com/franzliszt0626/risk-management-project.git
cd risk-management-project
```

### 2. 创建数据库

```sql
CREATE DATABASE `risk-management-project`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
```

### 3. 配置 `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/risk-management-project?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: your_username       # ← 替换
    password: your_password       # ← 替换
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
  global-config:
    db-config:
      id-type: auto
      table-prefix: t_

# AI 服务配置
ai:
  qwen:
    api-key: your_api_key         # ← 替换为通义千问 API Key
    model: qwen-turbo
```

### 4. 初始化表结构

```bash
mysql -u root -p risk-management-project < sql/init_schema.sql
```

> 核心表：`t_worker` · `t_risk_indicator` · `t_alert_record` · `t_work_area`

### 5. 启动

```bash
./mvnw spring-boot:run
```

| 地址                               | 说明 |
|----------------------------------|------|
| `http://localhost:8080/api`      | 接口根路径 |
| `http://localhost:8080/doc.html` | Swagger 接口文档 |

<br/>

## 📂 项目结构

```
src/main/java/gang/lu/riskmanagementproject/
├── annotation/              # 自定义注解（@ValidId / @ValidEnum / @BusinessLog / @ValidateLog）
├── aspect/                  # AOP 切面（业务日志 / 校验日志）
├── common/                  # 通用核心（Result / BusinessConstants / AiConstants）
├── controller/              # REST 接口层
│   ├── WorkerController
│   ├── RiskIndicatorController
│   ├── AlertRecordController
│   ├── WorkAreaController
│   ├── RiskAiController
│   ├── RiskReportController
│   └── VideoAnalysisController
├── domain/
│   ├── po/                  # 持久化对象（对应数据库表）
│   ├── dto/                 # 请求传输对象
│   │   └── query/           # 多条件查询 DTO
│   ├── vo/
│   │   ├── normal/          # 标准业务 VO
│   │   └── statistical/     # 统计结果 VO（alert / area / indicator / worker）
│   └── enums/               # 业务枚举（RiskLevel / AlertLevel / WorkType / Status / AreaRiskLevel）
├── exception/               # 自定义异常（BizException）
├── handler/                 # 全局异常处理器（GlobalExceptionHandler）
├── helper/                  # 业务辅助（AiHelper / PageHelper）
├── mapper/                  # MyBatis-Plus Mapper 层
├── message/                 # 消息常量（SuccessMessages / FailedMessages）
├── property/                # 配置属性（MedicalProperty / PageProperty）
├── service/                 # 业务接口 + 实现
│   ├── BaseCrudService      # ← 通用 CRUD 接口
│   └── impl/
│       ├── BaseCrudServiceImpl  # ← 通用 CRUD 模板实现
│       ├── WorkerServiceImpl
│       ├── RiskIndicatorServiceImpl
│       ├── AlertRecordServiceImpl
│       └── WorkAreaServiceImpl
├── util/                    # 工具类（BasicUtil / StatisticalUtil / EnumConvertUtil）
├── validator/               # 校验器（GeneralValidator / MedicalValidator / VideoValidator）
│   └── annotation/          # 校验注解实现（IdValidator / EnumValidator）
└── converter/               # PO ↔ VO / DTO 转换器（含 PageConverter 基类）
```

<br/>

## 🧪 接口示例

### 新增风险指标

```http
POST /api/risk-indicator
Content-Type: application/json

{
  "workerId": 1,
  "heartRate": 110,
  "respiratoryRate": 28,
  "fatiguePercent": 85.5,
  "riskLevelValue": "高风险",
  "alertFlag": true
}
```

### AI 风险预测

```http
GET /api/risk-ai/predict/1
```

```json
{
  "code": 200,
  "message": "风险预测成功！",
  "data": {
    "workerId": 1,
    "recordCount": 20,
    "predictedRiskLevel": "高风险",
    "riskTrend": "上升",
    "analysisSummary": "该工人心率持续偏高，疲劳指数上升趋势明显，建议安排轮换休息。",
    "suggestions": ["每隔 2 小时安排 15 分钟休息", "增加现场巡视频率"],
    "confidenceNote": "基于 20 条历史数据，置信度较高。"
  }
}
```

### 统一错误响应

```json
{
  "code": 400,
  "message": "【参数校验失败】无效的工种！允许值：高空作业、受限空间、设备操作、正常作业！",
  "data": null
}
```

<br/>

## 📐 开发规范

| 层次 | 职责边界 |
|------|---------|
| **Controller** | 接收请求 / 返回响应，不处理业务逻辑，参数格式校验（`@ValidId` / `@ValidEnum`） |
| **Service** | 核心业务逻辑，抛出 `BizException` 表达业务错误，调用 Validator 做语义校验 |
| **Mapper** | 仅定义数据访问接口，复杂 SQL 写 XML，简单 CRUD 走 MyBatis-Plus |
| **Util** | 无状态纯函数工具，不注入 Spring Bean，禁止包含业务逻辑 |
| **Validator** | 封装跨模块复用的校验（ID 存在性 / 枚举合法性 / 生理指标范围） |

<br/>

## 📈 后续规划

- [ ] 接入 Redis 缓存高频统计结果
- [ ] 引入 WebSocket 实现预警实时推送
- [ ] 增加接口权限控制（Spring Security + JWT）
- [ ] 接入大屏可视化（ECharts）
- [ ] 补充单元测试（JUnit 5 + Mockito）

<br/>

## 📜 许可证

本项目基于 [MIT License](LICENSE) 开源，允许自由使用、修改与分发。

<br/>

## 👨‍💻 作者

<div align="center">

**Franz Liszt**

[![Email](https://img.shields.io/badge/Email-franzliszt709@gmail.com-EA4335?style=for-the-badge&logo=gmail&logoColor=white)](mailto:franzliszt709@gmail.com)
[![GitHub](https://img.shields.io/badge/GitHub-franzliszt0626-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/franzliszt0626)

*欢迎提交 Issue / PR，一起把它做得更好 🚀*

</div>

<br/>

---

<div align="center">

<sub>💡 生产环境建议：关闭 Swagger 文档 · 关闭 SQL 打印日志 · 配置 HikariCP 连接池 · 启用 Logback 日志归档</sub>

</div>