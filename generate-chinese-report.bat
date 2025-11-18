@echo off
chcp 65001 >nul
echo 正在生成中文测试报告...

cd /d "%~dp0"

REM 编译Java文件
javac -encoding UTF-8 -cp "." GenerateChineseReport.java

if %errorlevel% neq 0 (
    echo 编译失败，请确保已安装JDK并配置了JAVA_HOME
    pause
    exit /b 1
)

REM 运行生成器
java -cp "." GenerateChineseReport . test-report-zh.html

if %errorlevel% equ 0 (
    echo.
    echo ✓ 中文测试报告已生成: test-report-zh.html
    echo 请用浏览器打开查看
) else (
    echo 生成报告时出错
)

pause

