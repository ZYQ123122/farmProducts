# SQL Server 数据库配置说明

## 数据库连接配置

已修改为 SQL Server 配置：
- **驱动类**：`com.microsoft.sqlserver.jdbc.SQLServerDriver`
- **连接URL**：`jdbc:sqlserver://localhost:1433;databaseName=ry;encrypt=false;trustServerCertificate=true`
- **用户名**：`admin`
- **密码**：`admin123`

## 数据库回滚操作

### 方法一：使用 sqlcmd 命令行工具（推荐）

```bash
# Windows 命令提示符或 PowerShell
sqlcmd -S localhost -U admin -P admin123 -d ry -i ruoyi_backup_20251024.sql
```

或者交互式输入密码（更安全）：
```bash
sqlcmd -S localhost -U admin -d ry -i ruoyi_backup_20251024.sql
# 然后输入密码：admin123
```

### 方法二：使用 SQL Server Management Studio (SSMS)

1. 打开 SQL Server Management Studio
2. 连接到数据库服务器（localhost，用户名：admin，密码：admin123）
3. 选择数据库 `ry`
4. 点击菜单：**文件** → **打开** → **文件**
5. 选择 `ruoyi_backup_20251024.sql` 文件
6. 点击 **执行** 按钮（或按 F5）

### 方法三：使用 Azure Data Studio

1. 打开 Azure Data Studio
2. 连接到数据库服务器
3. 打开 SQL 文件：`ruoyi_backup_20251024.sql`
4. 选择数据库 `ry`
5. 点击 **运行** 按钮

## 更新测试用户数据

执行以下命令更新测试用户：

```bash
sqlcmd -S localhost -U admin -P admin123 -d ry -i sql/update_test_users_sqlserver.sql
```

或者使用 SSMS 打开并执行 `sql/update_test_users_sqlserver.sql` 文件。

## 测试用户账号

- **管理员**
  - 用户名：`admin`
  - 密码：`admin123`

- **农户用户（ID: 100）**
  - 用户名：`1234`
  - 密码：`123456`

- **买家用户（ID: 101）**
  - 用户名：`buyer1`
  - 密码：`buyer1234`

## 注意事项

1. **端口**：默认 SQL Server 端口是 1433，如果您的 SQL Server 使用其他端口，请修改 `application-druid.yml` 中的 URL
2. **数据库名称**：确保数据库 `ry` 已创建
3. **权限**：确保用户 `admin` 有足够的权限执行 SQL 脚本
4. **编码**：SQL Server 脚本使用 UTF-8 编码，确保文件编码正确

## 常见问题

### 问题1：连接失败
- 检查 SQL Server 服务是否启动
- 检查防火墙是否允许 1433 端口
- 检查 SQL Server 是否允许 SQL Server 身份验证（不仅仅是 Windows 身份验证）

### 问题2：找不到 sqlcmd 命令
- 安装 SQL Server Command Line Utilities
- 或者使用 SSMS 图形界面工具

### 问题3：编码问题
- 确保 SQL 文件使用 UTF-8 或 UTF-8 with BOM 编码
- 在 SSMS 中打开文件时，选择正确的编码














