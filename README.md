# Spring Boot 后端脚手架

Spring Boot 3.5 + Java 21 后端项目起点，开箱即用的基础设施。

## 技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.5.14 | 基础框架 |
| MyBatis-Flex | 1.11.6 | ORM |
| Sa-Token + Redis | 1.45.0 | 认证鉴权 |
| Flyway | (Spring Boot 管理) | 数据库迁移 |
| SpringDoc OpenAPI | 2.8.9 | API 文档 |
| Redis | (Spring Boot 管理) | 缓存 / Token / 防刷 |
| MySQL | 8.x | 数据库 |

## 内置能力

- ✅ **统一响应** — `ApiResult<T>(code, msg, data)` 统一 JSON 格式
- ✅ **全局异常处理** — 业务异常、参数校验、Sa-Token 认证异常自动转换
- ✅ **分页封装** — `PageQuery` / `PageResult` 入参解析与出参封装
- ✅ **HMAC-SHA256 外部 API** — 签名验证 + Redis Nonce 防重放
- ✅ **文件上传** — 本地存储，图片格式校验，路径遍历防护
- ✅ **数据脱敏** — 手机号、邮箱、姓名、身份证、银行卡
- ✅ **接口防刷** — Redis 计数器限流
- ✅ **参数校验** — `BusinessValidator` 常用字段校验
- ✅ **DTO 转换** — 实体 → DTO 批量 / 分页转换
- ✅ **OpenAPI 文档** — Swagger UI: http://localhost:8080/swagger-ui.html

## 快速开始

### 1. 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.x
- Redis 7.x

### 2. 配置数据库

创建数据库并修改配置（或通过环境变量覆盖）：

```bash
# 复制环境变量模板
copy .env.example .env
```

`.env` 文件示例：

```env
SERVER_PORT=8080
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=scaffold
DB_USERNAME=root
DB_PASSWORD=root
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=
EXTERNAL_API_SECRET=your-random-secret-key
```

### 3. 启动

```bash
mvn spring-boot:run
```

Flyway 会在首次启动时自动创建 `app_user` 示例表。

### 4. 验证

```bash
# Ping
curl http://localhost:8080/api/system/ping

# Health
curl http://localhost:8080/api/system/health

# Swagger UI
# 浏览器打开 http://localhost:8080/swagger-ui.html
```

## 外部 API 调用

外部 API（`/api/ext/**`）需要 HMAC-SHA256 签名：

```bash
SECRET="your-external-api-secret"
METHOD="GET"
URI="/api/ext/system/metrics"
BODY=""
TIMESTAMP=$(date +%s)
NONCE=$(uuidgen | tr -d '-')
# payload = METHOD + "\n" + URI + "\n" + BODY + "\n" + TIMESTAMP + "\n" + NONCE
PAYLOAD="${METHOD}\n${URI}\n${BODY}\n${TIMESTAMP}\n${NONCE}"
SIGNATURE=$(echo -ne "${PAYLOAD}" | openssl dgst -sha256 -hmac "${SECRET}" | awk '{print $2}')

curl "http://localhost:8080/api/ext/system/metrics" \
  -H "X-Api-Timestamp: ${TIMESTAMP}" \
  -H "X-Api-Nonce: ${NONCE}" \
  -H "X-Api-Signature: ${SIGNATURE}"
```

## 项目结构

```
src/main/java/com/example/scaffold/
├── ScaffoldApplication.java          # 启动入口
├── common/
│   ├── api/           ApiResult / ApiResults
│   ├── error/         AppBizException / AppErrorCode / 子类异常
│   ├── exception/     ApiExceptionHandler / SaTokenExceptionAdvice
│   ├── page/          PageQuery / PageResult / PageQueries / PageResults
│   ├── query/         QueryBuilder（MyBatis-Flex）
│   ├── security/      DataMaskingUtils / RateLimiter
│   ├── text/          Texts
│   ├── validation/    BusinessValidator / ClientValidationException
│   ├── web/           WebResponses / RequestParams
│   └── converter/     DtoConverter
├── config/            配置（AppProperties / Jackson / CORS / OpenAPI / SaToken...）
├── domain/            领域实体（system/ 为基础，业务实体放此处）
├── infrastructure/    基础设施（AppTime / FileStorage / DatabaseHealth）
├── module/
│   ├── system/        Ping / Health 端点
│   ├── upload/        文件上传
│   ├── extapi/        外部 API（HMAC 签名）
│   └── auth/          LoginUserIds
└── persistence/
    └── mapper/        MyBatis Mapper 接口
```

## 业务开发指南

1. **添加实体** — 在 `domain/` 下创建 POJO，用 `@Table` 注解
2. **添加 Mapper** — 在 `persistence/mapper/` 继承 `BaseMapper<Entity>`
3. **添加服务** — 在 `module/` 下新建子包，创建 Service + Controller + DTO
4. **添加路由白名单** — 如需匿名访问，在 `SaTokenMvcConfiguration` 添加 `.notMatch()`
5. **数据库迁移** — 在 `resources/db/migration/` 新增 `V2__xxx.sql`

## 许可证

内部项目，保留所有权利。
