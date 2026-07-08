package com.tinyengine.it.test;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
// 这是一个故意包含漏洞的测试类，用于验证CodeQL扫描功能

@RestController
public class VulnerableTest {
	// 1. SQL 注入漏洞 (CWE-89)
	// 漏洞点：直接将用户输入拼接到SQL语句中
	@GetMapping("/sql-injection")
	public String sqlInjection(@RequestParam String productId, HttpServletRequest request) {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "user", "pass");
			// 危险：直接拼接用户输入
			String query = "SELECT * FROM products WHERE id = " + productId; // $source
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query); // $sink
			// ... 处理结果
			return "Query executed";
		} catch (SQLException e) {
			return "Error: " + e.getMessage();
		}
	}

	// 2. 命令注入漏洞 (CWE-78)
	// 漏洞点：用户输入被直接用于系统命令执行
	@GetMapping("/command-injection")
	public String commandInjection(@RequestParam String filename) {
		try {
			// 危险：用户输入直接拼接到命令中
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("ls -l " + filename); // $source -> $sink
			// ... 处理结果
			return "Command executed";
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}

	// 3. SSRF 服务端请求伪造漏洞 (CWE-918)
	// 漏洞点：用户控制的URL被用于发起网络请求
	@GetMapping("/ssrf")
	public String ssrf(@RequestParam String urlParam) {
		try {
			// 危险：用户输入直接构造URL并发起连接
			URL url = new URL(urlParam); // $source
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // $sink
			// ... 处理响应
			return "Request sent";
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}

	// 4. 硬编码密码/密钥 (CWE-798)
	// 漏洞点：敏感信息直接写在代码中
	public void hardcodedCredential() {
		String password = "admin123"; // $hardcoded
		String apiKey = "sk-1234567890abcdef"; // $hardcoded
		// ...
	}

	// 5. 路径遍历漏洞 (CWE-22)
	// 漏洞点：用户输入直接用于构造文件路径
	@GetMapping("/file-read")
	public String fileRead(@RequestParam String filename) {
		try {
			// 危险：用户输入直接用于文件读取
			File file = new File("/var/data/" + filename); // $source
			FileReader fr = new FileReader(file); // $sink
			// ... 读取文件内容
			return "File read";
		} catch (IOException e) {
			return "Error: " + e.getMessage();
		}
	}
}
