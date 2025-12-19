#!/bin/bash
echo "正在生成中文测试报告..."

cd "$(dirname "$0")"

# 编译Java文件
javac -encoding UTF-8 -cp "." GenerateChineseReport.java

if [ $? -ne 0 ]; then
    echo "编译失败，请确保已安装JDK"
    exit 1
fi

# 运行生成器
java -cp "." GenerateChineseReport . test-report-zh.html

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ 中文测试报告已生成: test-report-zh.html"
    echo "请用浏览器打开查看"
else
    echo "生成报告时出错"
    exit 1
fi

