# IDEA 项目配置指南

## 已完成的 SQL Server 配置

### 1. 数据库连接配置
- ✅ `application-druid.yml`: SQL Server 驱动和连接信息已配置
- ✅ `ruoyi-admin/pom.xml`: SQL Server JDBC 驱动已添加
- ✅ `application.yml`: PageHelper 方言已改为 `sqlserver`

### 2. Mapper XML 文件修复
所有 Mapper XML 文件中的 MySQL 特定语法已修复为 SQL Server 语法：
- ✅ `LIMIT 1` → `TOP 1`
- ✅ `concat('%', #{var}, '%')` → `'%' + #{var} + '%'`
- ✅ `date_format()` → `FORMAT()`
- ✅ `find_in_set()` → `CHARINDEX()`
- ✅ `sysdate()` / `NOW()` → `GETDATE()`
- ✅ `ifnull()` → `ISNULL()`
- ✅ `CAST(#{status} AS UNSIGNED)` → `CAST(#{status} AS BIT)`

## IDEA 项目设置步骤

### 步骤 1: 清理构建缓存

在 IDEA 中执行以下操作：

1. **删除 target 目录**：
   - 右键项目根目录
   - 选择 "Open in Explorer" 或直接在文件管理器中打开
   - 删除所有模块的 `target` 目录（如果存在）

2. **清理 IDEA 缓存**：
   - 菜单：`File` → `Invalidate Caches / Restart...`
   - 选择 `Invalidate and Restart`

### 步骤 2: 重新导入 Maven 项目

1. **刷新 Maven 项目**：
   - 右键项目根目录
   - 选择 `Maven` → `Reload Project`
   - 或者点击右侧 Maven 工具栏的刷新按钮

2. **更新项目结构**：
   - 菜单：`File` → `Project Structure` (Ctrl+Alt+Shift+S)
   - 检查 `Modules` 中的模块是否正确
   - 检查 `SDKs` 中 Java 版本是否为 17

### 步骤 3: 配置 JDK

1. **设置项目 JDK**：
   - `File` → `Project Structure` → `Project`
   - `SDK`: 选择 Java 17
   - `Language level`: 选择 17

2. **设置模块 JDK**：
   - `File` → `Project Structure` → `Modules`
   - 选择每个模块，在 `Dependencies` 标签页
   - 确保 `Module SDK` 为 Java 17

### 步骤 4: 配置运行配置

1. **创建运行配置**：
   - `Run` → `Edit Configurations...`
   - 点击 `+` → `Spring Boot`
   - 配置如下：
     - **Name**: `RuoYiApplication`
     - **Main class**: `com.ruoyi.RuoYiApplication`
     - **Module**: `ruoyi-admin`
     - **Working directory**: 项目根目录

### 步骤 5: 验证配置

1. **检查编译错误**：
   - `Build` → `Rebuild Project`
   - 查看是否有编译错误

2. **检查依赖**：
   - 打开 `ruoyi-admin/pom.xml`
   - 确认 SQL Server 驱动依赖存在：
     ```xml
     <dependency>
         <groupId>com.microsoft.sqlserver</groupId>
         <artifactId>mssql-jdbc</artifactId>
         <version>12.4.2.jre8</version>
     </dependency>
     ```

## 常见问题解决

### 问题 1: target 目录错误

**错误信息**:
```
The declared package "com.ruoyi" does not match the expected package ""
```

**解决方法**:
1. 删除所有 `target` 目录
2. `File` → `Invalidate Caches / Restart...`
3. `Build` → `Rebuild Project`

### 问题 2: Maven 依赖下载失败

**解决方法**:
1. 检查网络连接
2. 配置 Maven 镜像（如果需要）：
   - `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven`
   - 在 `User settings file` 中配置镜像

### 问题 3: SQL Server 连接失败

**检查项**:
1. SQL Server 服务是否启动
2. 端口是否正确（默认 1433）
3. 数据库 `ry` 是否已创建
4. 用户名和密码是否正确（admin / admin123）

### 问题 4: 编译错误

**解决方法**:
1. `Build` → `Clean Project`
2. `Build` → `Rebuild Project`
3. 检查 Java 版本是否为 17
4. 检查 Maven 配置是否正确

## 启动项目

1. **确保数据库已配置**：
   - SQL Server 服务已启动
   - 数据库 `ry` 已创建
   - 已执行初始化 SQL 脚本

2. **运行项目**：
   - 选择运行配置 `RuoYiApplication`
   - 点击运行按钮或按 `Shift+F10`

3. **访问系统**：
   - 浏览器打开：http://127.0.0.1:80/
   - 使用管理员账号登录：`admin` / `admin123`

## 验证清单

- [ ] 所有 `target` 目录已删除
- [ ] IDEA 缓存已清理
- [ ] Maven 项目已重新加载
- [ ] JDK 版本设置为 17
- [ ] SQL Server 驱动依赖已添加
- [ ] 数据库连接配置正确
- [ ] 所有 Mapper XML 文件已修复
- [ ] 项目可以成功编译
- [ ] 项目可以成功启动
- [ ] 可以正常访问系统

## 注意事项

1. **端口 80**: 如果无法使用 80 端口（需要管理员权限），可以修改 `application.yml` 中的端口为 8080
2. **数据库**: 确保 SQL Server 允许 SQL Server 身份验证（不仅仅是 Windows 身份验证）
3. **防火墙**: 确保防火墙允许 1433 端口（SQL Server）和 80/8080 端口（Web 服务）














