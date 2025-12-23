# 运行错误修复指南

## ✅ 已修复的问题

### 1. 主类配置问题
**错误信息**：
```
错误: 找不到或无法加载主类 com.ruoyi.RuoYiApplication
原因: java.lang.ClassNotFoundException: com.ruoyi.RuoYiApplication
```

**修复内容**：
- 在 `ruoyi-admin/pom.xml` 的 `spring-boot-maven-plugin` 中添加了主类配置：
  ```xml
  <configuration>
      <mainClass>com.ruoyi.RuoYiApplication</mainClass>
      <fork>true</fork>
  </configuration>
  ```

## 🚀 正确的运行方式

### 方法一：使用 Maven 运行（推荐）

在项目根目录执行：

```bash
# 清理并编译项目
mvn clean install -DskipTests

# 进入 ruoyi-admin 模块
cd ruoyi-admin

# 运行 Spring Boot 应用
mvn spring-boot:run
```

### 方法二：在 IDEA 中运行

1. **清理项目**：
   - `Build` → `Clean Project`
   - 删除所有 `target` 目录

2. **重新构建**：
   - `Build` → `Rebuild Project`

3. **配置运行**：
   - `Run` → `Edit Configurations...`
   - 点击 `+` → `Spring Boot`
   - 配置如下：
     - **Name**: `RuoYiApplication`
     - **Main class**: `com.ruoyi.RuoYiApplication`
     - **Module**: `ruoyi-admin`
     - **Working directory**: `$MODULE_DIR$` 或项目根目录

4. **运行**：
   - 选择配置 `RuoYiApplication`
   - 点击运行按钮或按 `Shift+F10`

### 方法三：使用打包后的 JAR 运行

```bash
# 在项目根目录打包
mvn clean package -DskipTests

# 运行打包后的 JAR
java -jar ruoyi-admin/target/ruoyi-admin.jar
```

## ⚠️ 常见问题解决

### 问题 1: Maven settings.xml 错误

如果遇到 Maven settings.xml 解析错误，需要修复 Maven 配置文件：

**错误信息**：
```
Non-parseable settings E:\apache-maven-3.9.11-bin\conf\settings.xml
```

**解决方法**：
1. 打开 Maven settings.xml 文件
2. 检查第 151-153 行附近的 XML 标签是否匹配
3. 修复 XML 格式错误

### 问题 2: 路径包含中文字符

如果项目路径包含中文字符可能导致问题，建议：
- 将项目移动到不包含中文的路径
- 或使用短路径名

### 问题 3: 类路径问题

如果仍然找不到主类：

1. **检查编译输出**：
   ```bash
   # 检查类文件是否存在
   dir ruoyi-admin\target\classes\com\ruoyi\RuoYiApplication.class
   ```

2. **重新编译**：
   ```bash
   cd ruoyi-admin
   mvn clean compile
   ```

3. **检查依赖**：
   ```bash
   mvn dependency:tree
   ```

## 📝 验证步骤

1. **检查主类文件**：
   - 确认 `ruoyi-admin/src/main/java/com/ruoyi/RuoYiApplication.java` 存在

2. **检查编译输出**：
   - 确认 `ruoyi-admin/target/classes/com/ruoyi/RuoYiApplication.class` 存在

3. **检查配置**：
   - 确认 `ruoyi-admin/pom.xml` 中已配置 `<mainClass>com.ruoyi.RuoYiApplication</mainClass>`

4. **测试运行**：
   - 使用 `mvn spring-boot:run` 或 IDEA 运行配置

## 🎯 快速启动命令

在项目根目录执行：

```powershell
# PowerShell
cd ruoyi-admin
mvn spring-boot:run
```

或者：

```bash
# 一次性命令（在项目根目录）
mvn -pl ruoyi-admin spring-boot:run
```

## ✅ 成功标志

如果运行成功，您应该看到：

```
(♥◠‿◠)ﾉﾞ  若依启动成功   ლ(´ڡ`ლ)ﾞ  
 .-------.       ____     __        
 |  _ _   \      \   \   /  /    
 | ( ' )  |       \  _. /  '       
 |(_ o _) /        _( )_ .'         
 | (_,_).' __  ___(_ o _)'          
 |  |\ \  |  ||   |(_,_)'         
 |  | \ `'   /|   `-'  /           
 |  |  \    /  \      /           
 ''-'   `'-'    `-..-'              
```

然后访问：http://127.0.0.1:80/















