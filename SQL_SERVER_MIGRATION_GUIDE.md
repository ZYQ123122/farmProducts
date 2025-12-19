# SQL Server 迁移指南

## 已完成的配置修改

### 1. 数据库连接配置
- ✅ `application-druid.yml`: 已配置 SQL Server 驱动和连接信息
- ✅ `ruoyi-admin/pom.xml`: 已添加 SQL Server JDBC 驱动依赖
- ✅ `application.yml`: PageHelper 方言已改为 `sqlserver`

### 2. 需要手动修改的 SQL 语法

由于项目中有一些 MySQL 特定的 SQL 语法，需要适配 SQL Server。以下是需要修改的地方：

#### 2.1 LIMIT 语法
**MySQL**: `LIMIT 1`  
**SQL Server**: `TOP 1`

需要修改的文件：
- `ruoyi-system/src/main/resources/mapper/system/SysUserMapper.xml` (3处)
- `ruoyi-system/src/main/resources/mapper/system/SysDeptMapper.xml` (1处)
- `ruoyi-system/src/main/resources/mapper/system/SysConfigMapper.xml` (1处)
- `ruoyi-system/src/main/resources/mapper/system/SysDictTypeMapper.xml` (1处)
- `ruoyi-system/src/main/resources/mapper/system/FarmerProductMapper.xml` (1处)
- `ruoyi-system/src/main/resources/mapper/system/TradeOrderMapper.xml` (1处)

**修改示例**:
```xml
<!-- MySQL 语法 -->
where username = #{loginName} limit 1

<!-- SQL Server 语法 -->
where username = #{loginName}
```

然后在 SELECT 语句中使用 TOP:
```xml
<!-- MySQL -->
<select id="checkLoginNameUnique" ...>
    <include refid="selectUserVo"/>
    where username = #{loginName} limit 1
</select>

<!-- SQL Server -->
<select id="checkLoginNameUnique" ...>
    select top 1 <include refid="selectUserVo"/>
    where username = #{loginName}
</select>
```

#### 2.2 DATE_FORMAT 函数
**MySQL**: `date_format(created_at,'%Y%m%d')`  
**SQL Server**: `FORMAT(created_at,'yyyyMMdd')` 或 `CONVERT(VARCHAR(8), created_at, 112)`

需要修改的文件：
- `ruoyi-system/src/main/resources/mapper/system/SysUserMapper.xml` (2处)

**修改示例**:
```xml
<!-- MySQL -->
AND date_format(created_at,'%Y%m%d') &gt;= date_format(#{params.beginTime},'%Y%m%d')

<!-- SQL Server -->
AND FORMAT(created_at,'yyyyMMdd') &gt;= FORMAT(#{params.beginTime},'yyyyMMdd')
```

#### 2.3 CONCAT 函数
**MySQL**: `concat('%', #{username}, '%')`  
**SQL Server**: `'%' + #{username} + '%'` 或 `CONCAT('%', #{username}, '%')` (SQL Server 2012+)

**注意**: SQL Server 2012+ 支持 CONCAT 函数，但更推荐使用 `+` 连接符。

需要修改的文件：
- `ruoyi-system/src/main/resources/mapper/system/SysUserMapper.xml` (多处)
- `ruoyi-system/src/main/resources/mapper/system/SysDeptMapper.xml` (多处)

**修改示例**:
```xml
<!-- MySQL -->
AND username like concat('%', #{username}, '%')

<!-- SQL Server -->
AND username like '%' + #{username} + '%'
```

#### 2.4 FIND_IN_SET 函数
**MySQL**: `find_in_set(#{deptId}, ancestors)`  
**SQL Server**: `',' + CAST(#{deptId} AS VARCHAR) + ',' LIKE '%,' + ancestors + ',%'` 或使用 `CHARINDEX`

需要修改的文件：
- `ruoyi-system/src/main/resources/mapper/system/SysDeptMapper.xml` (2处)

**修改示例**:
```xml
<!-- MySQL -->
where find_in_set(#{deptId}, ancestors)

<!-- SQL Server -->
where CHARINDEX(',' + CAST(#{deptId} AS VARCHAR) + ',', ',' + ancestors + ',') > 0
```

## 快速修复脚本

可以使用以下 PowerShell 脚本批量替换（请先备份文件）：

```powershell
# 替换 LIMIT 1 (需要手动调整 SELECT 语句)
Get-ChildItem -Path "ruoyi-system\src\main\resources\mapper" -Filter "*.xml" -Recurse | 
    ForEach-Object {
        (Get-Content $_.FullName) -replace ' limit 1', '' | Set-Content $_.FullName
    }

# 替换 date_format
Get-ChildItem -Path "ruoyi-system\src\main\resources\mapper" -Filter "*.xml" -Recurse | 
    ForEach-Object {
        $content = Get-Content $_.FullName -Raw
        $content = $content -replace "date_format\(([^,]+),'%Y%m%d'\)", "FORMAT(`$1,'yyyyMMdd')"
        Set-Content $_.FullName -Value $content -NoNewline
    }

# 替换 concat (需要手动检查)
Get-ChildItem -Path "ruoyi-system\src\main\resources\mapper" -Filter "*.xml" -Recurse | 
    ForEach-Object {
        $content = Get-Content $_.FullName -Raw
        $content = $content -replace "concat\('%',\s*([^,]+),\s*'%'\)", "'%' + `$1 + '%'"
        Set-Content $_.FullName -Value $content -NoNewline
    }
```

## 数据库配置检查清单

- [x] 数据库驱动已更新为 SQL Server
- [x] 连接 URL 已配置
- [x] PageHelper 方言已改为 sqlserver
- [ ] Mapper XML 文件中的 LIMIT 语法已修改
- [ ] Mapper XML 文件中的 date_format 已修改
- [ ] Mapper XML 文件中的 concat 已修改
- [ ] Mapper XML 文件中的 find_in_set 已修改
- [ ] 数据库已创建并执行了初始化脚本
- [ ] 测试用户数据已导入

## 测试建议

1. 启动项目后，先测试登录功能
2. 测试用户查询功能
3. 测试分页功能
4. 测试日期范围查询功能
5. 检查日志中是否有 SQL 语法错误

## 注意事项

1. **备份**: 修改前请备份所有 Mapper XML 文件
2. **测试**: 每个修改后都要进行充分测试
3. **日志**: 启用 SQL 日志查看实际执行的 SQL 语句
4. **版本**: 确保 SQL Server 版本支持使用的函数（如 CONCAT 需要 SQL Server 2012+）














