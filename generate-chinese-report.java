import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * 生成中文测试报告HTML
 */
public class GenerateChineseReport {
    
    static class TestSuite {
        String name;
        int tests;
        int errors;
        int failures;
        int skipped;
        double time;
        List<TestCase> testCases = new ArrayList<>();
    }
    
    static class TestCase {
        String name;
        String className;
        double time;
        String status; // "success", "error", "failure"
        String message;
        String stackTrace;
    }
    
    public static void main(String[] args) throws Exception {
        String baseDir = args.length > 0 ? args[0] : ".";
        String outputFile = args.length > 1 ? args[1] : baseDir + "/test-report-zh.html";
        
        List<TestSuite> suites = new ArrayList<>();
        
        // 扫描所有XML报告文件
        Path reportsDir = Paths.get(baseDir);
        if (Files.exists(reportsDir.resolve("ruoyi-admin/target/surefire-reports"))) {
            scanDirectory(reportsDir.resolve("ruoyi-admin/target/surefire-reports"), suites);
        }
        if (Files.exists(reportsDir.resolve("ruoyi-system/target/surefire-reports"))) {
            scanDirectory(reportsDir.resolve("ruoyi-system/target/surefire-reports"), suites);
        }
        
        // 生成HTML
        generateHTML(suites, outputFile);
        System.out.println("中文测试报告已生成: " + outputFile);
    }
    
    static void scanDirectory(Path dir, List<TestSuite> suites) throws Exception {
        if (!Files.exists(dir)) return;
        
        Files.list(dir)
            .filter(p -> p.toString().endsWith(".xml") && p.getFileName().toString().startsWith("TEST-"))
            .forEach(xmlFile -> {
                try {
                    TestSuite suite = parseXML(xmlFile.toFile());
                    if (suite != null) suites.add(suite);
                } catch (Exception e) {
                    System.err.println("解析失败: " + xmlFile + " - " + e.getMessage());
                }
            });
    }
    
    static TestSuite parseXML(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        
        Element testsuite = doc.getDocumentElement();
        TestSuite suite = new TestSuite();
        suite.name = testsuite.getAttribute("name");
        suite.tests = Integer.parseInt(testsuite.getAttribute("tests"));
        suite.errors = Integer.parseInt(testsuite.getAttribute("errors"));
        suite.failures = Integer.parseInt(testsuite.getAttribute("failures"));
        suite.skipped = Integer.parseInt(testsuite.getAttribute("skipped"));
        suite.time = Double.parseDouble(testsuite.getAttribute("time"));
        
        NodeList testcases = testsuite.getElementsByTagName("testcase");
        for (int i = 0; i < testcases.getLength(); i++) {
            Element testcase = (Element) testcases.item(i);
            TestCase tc = new TestCase();
            tc.name = testcase.getAttribute("name");
            tc.className = testcase.getAttribute("classname");
            tc.time = Double.parseDouble(testcase.getAttribute("time"));
            
            if (testcase.getElementsByTagName("error").getLength() > 0) {
                tc.status = "error";
                Element error = (Element) testcase.getElementsByTagName("error").item(0);
                tc.message = error.getAttribute("message");
                tc.stackTrace = error.getTextContent();
            } else if (testcase.getElementsByTagName("failure").getLength() > 0) {
                tc.status = "failure";
                Element failure = (Element) testcase.getElementsByTagName("failure").item(0);
                tc.message = failure.getAttribute("message");
                tc.stackTrace = failure.getTextContent();
            } else {
                tc.status = "success";
            }
            
            suite.testCases.add(tc);
        }
        
        return suite;
    }
    
    static void generateHTML(List<TestSuite> suites, String outputFile) throws Exception {
        int totalTests = 0, totalErrors = 0, totalFailures = 0, totalSkipped = 0;
        double totalTime = 0;
        
        for (TestSuite suite : suites) {
            totalTests += suite.tests;
            totalErrors += suite.errors;
            totalFailures += suite.failures;
            totalSkipped += suite.skipped;
            totalTime += suite.time;
        }
        
        double successRate = totalTests > 0 ? 
            ((totalTests - totalErrors - totalFailures - totalSkipped) * 100.0 / totalTests) : 0;
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"zh-CN\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("  <title>单元测试报告（中文）</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: 'Microsoft YaHei', Arial, sans-serif; margin: 20px; background: #f5f5f5; }\n");
        html.append("    .container { max-width: 1200px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n");
        html.append("    h1 { color: #333; border-bottom: 3px solid #4CAF50; padding-bottom: 10px; }\n");
        html.append("    h2 { color: #555; margin-top: 30px; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
        html.append("    th, td { padding: 12px; text-align: left; border: 1px solid #ddd; }\n");
        html.append("    th { background-color: #4CAF50; color: white; font-weight: bold; }\n");
        html.append("    tr:nth-child(even) { background-color: #f9f9f9; }\n");
        html.append("    tr:hover { background-color: #f1f1f1; }\n");
        html.append("    .success { color: #4CAF50; font-weight: bold; }\n");
        html.append("    .error { color: #f44336; font-weight: bold; }\n");
        html.append("    .failure { color: #ff9800; font-weight: bold; }\n");
        html.append("    .skipped { color: #9e9e9e; }\n");
        html.append("    .summary { background: #e8f5e9; padding: 15px; border-radius: 5px; margin: 20px 0; }\n");
        html.append("    .test-case { margin: 10px 0; padding: 10px; background: #fafafa; border-left: 4px solid #4CAF50; }\n");
        html.append("    .test-case.error { border-left-color: #f44336; }\n");
        html.append("    .test-case.failure { border-left-color: #ff9800; }\n");
        html.append("    .stack-trace { background: #fff3cd; padding: 10px; margin-top: 10px; border-radius: 4px; font-family: monospace; font-size: 12px; white-space: pre-wrap; }\n");
        html.append("    .nav { margin: 20px 0; }\n");
        html.append("    .nav a { margin: 0 10px; padding: 8px 15px; background: #4CAF50; color: white; text-decoration: none; border-radius: 4px; }\n");
        html.append("    .nav a:hover { background: #45a049; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <div class=\"container\">\n");
        html.append("    <h1>📊 单元测试报告</h1>\n");
        html.append("    <p style=\"color: #666;\">生成时间: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</p>\n");
        
        // 导航
        html.append("    <div class=\"nav\">\n");
        html.append("      <a href=\"#summary\">测试摘要</a>\n");
        html.append("      <a href=\"#packages\">测试套件</a>\n");
        html.append("      <a href=\"#details\">详细结果</a>\n");
        html.append("    </div>\n");
        
        // 摘要
        html.append("    <div id=\"summary\" class=\"summary\">\n");
        html.append("      <h2>📈 测试摘要</h2>\n");
        html.append("      <table>\n");
        html.append("        <tr><th>测试用例总数</th><th>错误</th><th>失败</th><th>跳过</th><th>成功率</th><th>总耗时（秒）</th></tr>\n");
        html.append("        <tr>\n");
        html.append("          <td>").append(totalTests).append("</td>\n");
        html.append("          <td class=\"error\">").append(totalErrors).append("</td>\n");
        html.append("          <td class=\"failure\">").append(totalFailures).append("</td>\n");
        html.append("          <td class=\"skipped\">").append(totalSkipped).append("</td>\n");
        html.append("          <td class=\"").append(successRate == 100 ? "success" : "failure").append("\">")
             .append(String.format("%.2f%%", successRate)).append("</td>\n");
        html.append("          <td>").append(String.format("%.3f", totalTime)).append("</td>\n");
        html.append("        </tr>\n");
        html.append("      </table>\n");
        html.append("    </div>\n");
        
        // 测试套件列表
        html.append("    <div id=\"packages\">\n");
        html.append("      <h2>📦 测试套件列表</h2>\n");
        html.append("      <table>\n");
        html.append("        <tr><th>测试类</th><th>用例数</th><th>错误</th><th>失败</th><th>跳过</th><th>成功率</th><th>耗时（秒）</th></tr>\n");
        
        for (TestSuite suite : suites) {
            double suiteRate = suite.tests > 0 ? 
                ((suite.tests - suite.errors - suite.failures - suite.skipped) * 100.0 / suite.tests) : 0;
            html.append("        <tr>\n");
            html.append("          <td><a href=\"#").append(suite.name.replace(".", "_")).append("\">").append(suite.name).append("</a></td>\n");
            html.append("          <td>").append(suite.tests).append("</td>\n");
            html.append("          <td class=\"error\">").append(suite.errors).append("</td>\n");
            html.append("          <td class=\"failure\">").append(suite.failures).append("</td>\n");
            html.append("          <td class=\"skipped\">").append(suite.skipped).append("</td>\n");
            html.append("          <td class=\"").append(suiteRate == 100 ? "success" : "failure").append("\">")
                 .append(String.format("%.2f%%", suiteRate)).append("</td>\n");
            html.append("          <td>").append(String.format("%.3f", suite.time)).append("</td>\n");
            html.append("        </tr>\n");
        }
        
        html.append("      </table>\n");
        html.append("    </div>\n");
        
        // 详细结果
        html.append("    <div id=\"details\">\n");
        html.append("      <h2>🔍 详细测试结果</h2>\n");
        
        for (TestSuite suite : suites) {
            html.append("      <h3 id=\"").append(suite.name.replace(".", "_")).append("\">").append(suite.name).append("</h3>\n");
            
            for (TestCase tc : suite.testCases) {
                html.append("      <div class=\"test-case ").append(tc.status).append("\">\n");
                html.append("        <strong>").append(tc.name).append("</strong> ");
                html.append("        <span class=\"").append(tc.status).append("\">");
                if ("success".equals(tc.status)) {
                    html.append("✓ 通过");
                } else if ("error".equals(tc.status)) {
                    html.append("✗ 错误");
                } else if ("failure".equals(tc.status)) {
                    html.append("✗ 失败");
                }
                html.append("</span> ");
                html.append("        <span style=\"color: #666;\">(").append(String.format("%.3f", tc.time)).append("秒)</span>\n");
                
                if (tc.message != null && !tc.message.isEmpty()) {
                    html.append("        <p><strong>错误信息:</strong> ").append(escapeHtml(tc.message)).append("</p>\n");
                }
                
                if (tc.stackTrace != null && !tc.stackTrace.isEmpty()) {
                    html.append("        <details>\n");
                    html.append("          <summary style=\"cursor: pointer; color: #666;\">查看堆栈跟踪</summary>\n");
                    html.append("          <div class=\"stack-trace\">").append(escapeHtml(tc.stackTrace)).append("</div>\n");
                    html.append("        </details>\n");
                }
                
                html.append("      </div>\n");
            }
        }
        
        html.append("    </div>\n");
        html.append("  </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        Files.write(Paths.get(outputFile), html.toString().getBytes("UTF-8"));
    }
    
    static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}

