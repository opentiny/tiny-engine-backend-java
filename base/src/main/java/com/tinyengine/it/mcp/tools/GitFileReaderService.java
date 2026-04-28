package com.tinyengine.it.mcp.tools;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.mapper.ComponentLibraryMapper;
import com.tinyengine.it.mapper.ComponentMapper;
import com.tinyengine.it.mcp.annotation.MCPTool;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.model.entity.ComponentLibrary;
import com.tinyengine.it.model.entity.MaterialComponent;
import com.tinyengine.it.model.entity.MaterialHistoryComponent;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class GitFileReaderService {

	private static final Logger log = LoggerFactory.getLogger(GitFileReaderService.class);

	private static final int CONNECT_TIMEOUT = 30000;
	private static final int READ_TIMEOUT = 30000;
	@Autowired
	private  ComponentLibraryMapper componentLibraryMapper;
	@Autowired
	private ComponentMapper baseMapper;

	@MCPTool(name="bundle_create",description = "给定的Raw GitHub URL 读取内容.")
	public String readFileFromRepo(String rawGitHubUrl) {
		try {
			JSONObject arg = JSONObject.parseObject(rawGitHubUrl);
			rawGitHubUrl = (String) arg.get("rawGitHubUrl");
			log.info("准备从 Raw GitHub URL  {} 中读取文件内容", rawGitHubUrl);
			// 1. 克隆远程仓库并读取文件内容（字节数组）
			//byte[] fileBytes = readFileFromRemoteRepo(repoUrl, filePath);
			//1.从给定的 Raw GitHub URL 读取内容
			byte[] fileBytes = fetchBytes(rawGitHubUrl);

			// 2. 获取文件名和json内容
			JsonFile jsonFile = new JsonFile();
			URI uri = new URI(rawGitHubUrl);
			String path = uri.getPath();  // 获取路径部分，例如 /opentiny/tiny-engine/main/designer-demo/public/mock/bundle.json
			if (path == null || path.isEmpty()) {
				return "";
			}
			String fileName = Path.of(path).getFileName().toString();
			String jsonContent = new String(fileBytes, StandardCharsets.UTF_8);

			String jsonString = removeBOM(jsonContent);
			Map<String, Object> jsonData =
				JsonUtils.MAPPER.readValue(jsonString, new TypeReference<Map<String, Object>>() {
				});

			jsonFile.setFileName(fileName);
			jsonFile.setFileContent(jsonData);

			// 获取组件数据
			Object dataObj = jsonFile.getFileContent().get("data");
			Map<String, Object> data = new HashMap<>();

			if (dataObj instanceof Map) {
				data = (Map<String, Object>) dataObj;
			}
			BundleDto bundleDto = BeanUtil.mapToBean(data, BundleDto.class, true);
			Result<BundleResultDto> bundleResultDtoResult = parseBundle(bundleDto);
			log.info("从Raw GitHub URL  {} 中读取文件 '{}' 的内容并解析完成", rawGitHubUrl, path);
			log.info("组件列表: {}", bundleResultDtoResult.getData().getComponentList());

			BundleResultDto bundleResultDto = bundleResultDtoResult.getData();
			List<Component> componentList = bundleResultDto.getComponentList();
			List<ComponentLibrary> packageList = bundleResultDto.getPackageList();
			if (packageList == null || packageList.isEmpty()) {
				FileResult fileResult = bulkCreate(componentList);
				return JsonUtils.mapper().writeValueAsString(fileResult);

			}
			log.info("开始遍历: {}", packageList);
			for (ComponentLibrary componentLibrary : packageList) {
				componentLibrary.setIsDefault(true);
				componentLibrary.setIsStarted(true);
				componentLibrary.setIsOfficial(true);
				ComponentLibrary library = new ComponentLibrary();
				library.setName(componentLibrary.getName());
				// 默认覆盖更新 其他业务需求多版本组件可放开  library.setVersion(componentLibrary.getVersion());
				// 查询是否存在组件库
				List<ComponentLibrary> componentLibraryList = componentLibraryMapper.queryComponentLibraryByCondition(
					library);
				log.info("查询组件库: {}, 结果: {}", library, componentLibraryList);
				int result = 0;
				if (!componentLibraryList.isEmpty()) {
					componentLibrary.setId(componentLibraryList.get(0).getId());
					result = componentLibraryMapper.updateComponentLibraryById(componentLibrary);
					if (result != 1) {
						throw new Exception("更新失败：" + result);
					}
					continue;
				}
				result = componentLibraryMapper.createComponentLibrary(componentLibrary);
				if (result != 1) {
					throw new Exception("createComponentLibrary失败：" + result);
				}
			}
			FileResult fileResult = bulkCreate(componentList);
			return JsonUtils.mapper().writeValueAsString(fileResult);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("从Raw GitHub URL  {} 中读取文件 '{}' 失败: {}", rawGitHubUrl, "bundle.json", e.getMessage());
			return "Error: " + e.getMessage();
		}


	}



	/**
	 * 从给定的 Raw GitHub URL 读取内容并返回字节数组
	 * @param urlString Raw 内容链接
	 * @return 文件内容的字节数组
	 * @throws IOException 网络或IO错误
	 */
	public static byte[] fetchBytes(String urlString) throws IOException {
		log.info("开始从 Raw GitHub URL  {} 中读取内容", urlString);
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(CONNECT_TIMEOUT);
		connection.setReadTimeout(READ_TIMEOUT);
		connection.setRequestProperty("User-Agent", "Java-URL-Client");

		try {
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("HTTP response code: " + responseCode);
			}

			try (InputStream inputStream = connection.getInputStream();
			     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[8192];
				int len;
				while ((len = inputStream.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return baos.toByteArray();
			}
		} finally {
			connection.disconnect();
		}
	}
	/**
	 * 通过浅克隆仓库到临时目录，以字节数组形式读取指定文件
	 *
	 * @param gitUrl   Git 仓库地址（如 https://github.com/opentiny/tiny-engine.git）
	 * @param filePath 文件在仓库中的相对路径，如 "README.md"
	 * @param branch   分支名，如 "main"
	 * @return         文件的原始字节内容
	 */
	public static byte[] getFileBytesViaClone(String gitUrl, String filePath, String branch)
		throws GitAPIException, IOException, URISyntaxException {

		// 1. 创建临时目录
		Path tempDir = Files.createTempDirectory("git-clone-");

		try {
			// 2. 浅克隆指定分支（depth=1 只拉取最新快照，不含完整历史）
			System.out.println("正在克隆仓库到临时目录: " + tempDir);
			try (Git git = Git.cloneRepository()
				.setURI(gitUrl)
				.setDirectory(tempDir.toFile())
				.setBranch(branch)
				.setDepth(1)
				.setCloneAllBranches(false)
				.call()) {
				System.out.println("克隆完成。");
			}

			// 3. 读取目标文件为字节数组（不做任何字符编码转换）
			Path targetFile = tempDir.resolve(filePath);
			if (!Files.exists(targetFile)) {
				throw new IOException("文件不存在于仓库中: " + filePath);
			}
			return Files.readAllBytes(targetFile);

		} finally {
			// 4. 清理临时目录
			if (Files.exists(tempDir)) {
				Files.walk(tempDir)
					.sorted(Comparator.reverseOrder())
					.forEach(path -> {
						try {
							Files.delete(path);
						} catch (IOException e) {
							System.err.println("删除临时文件失败: " + path);
						}
					});
				System.out.println("临时目录已清理。");
			}
		}
	}


	private void deleteDirectory(java.io.File dir) {
		if (dir.isDirectory()) {
			java.io.File[] children = dir.listFiles();
			if (children != null) {
				for (java.io.File child : children) {
					deleteDirectory(child);
				}
			}
		}
		dir.delete();
	}


	public static String removeBOM(String input) {
		if (input != null && input.startsWith("\uFEFF")) {
			return input.substring(1);
		}
		return input;
	}
	private String toPascalCase(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		StringBuilder result = new StringBuilder();
		boolean isNextUpper = true;
		for (char c : input.toCharArray()) {
			if (isNextUpper) {
				result.append(Character.toUpperCase(c));
				isNextUpper = false;
			} else {
				result.append(Character.toLowerCase(c));
			}
		}
		return result.toString();
	}
	private List<Component> buildComponentList(BundleDto bundleDto, List<Map<String, Object>> components,
	                                           List<Child> snippets) {
		List<Component> componentList = new ArrayList<>();
		for (Map<String, Object> comp : components) {
			Component component = BeanUtil.mapToBean(comp, Component.class, true);
			component.setId(null);
			component.setIsDefault(true);
			component.setIsOfficial(true);
			component.setDevMode("proCode");
			component.setFramework(bundleDto.getFramework());
			component.setPublicStatus(1);
			component.setIsTinyReserved(false);
			Object schemaObject = comp.get("schema");
			if (schemaObject instanceof Map) {
				component.setSchemaFragment((Map<String, Object>) schemaObject);
			}
			if (snippets == null || snippets.isEmpty()) {
				componentList.add(component);
				continue;
			}
			for (Child child : snippets) {
				Snippet snippet = child.getChildren().stream()
					.filter(item -> toPascalCase(comp.get("component").toString())
						.equals(toPascalCase(item.getSnippetName())))
					.findFirst()
					.orElse(null);

				if (snippet == null) {
					continue;
				}
				Map<String, Object> snippetMap = BeanUtil.beanToMap(snippet);
				component.setSnippets(Arrays.asList(snippetMap));
				if (Objects.isNull(component.getCategory())) {
					component.setCategory(child.getGroup());
				}
			}
			componentList.add(component);
		}
		return componentList;
	}

	public Result<BundleResultDto> parseBundle(BundleDto bundleDto) {
		List<Map<String, Object>> components = bundleDto.getMaterials().getComponents();
		List<Child> snippets = bundleDto.getMaterials().getSnippets();

		if (components == null || components.isEmpty()) {
			return Result.failed(ExceptionEnum.CM009);
		}
		List<Component> componentList = buildComponentList(bundleDto, components, snippets);
		List<Map<String, Object>> packages = bundleDto.getMaterials().getPackages();

		BundleResultDto bundleList = new BundleResultDto();
		bundleList.setComponentList(componentList);
		if (null == packages || packages.isEmpty()) {
			return Result.success(bundleList);
		}
		List<ComponentLibrary> packageList = new ArrayList<>();
		for (Map<String, Object> library : packages) {
			ComponentLibrary componentLibrary = BeanUtil.mapToBean(library, ComponentLibrary.class, true);
			componentLibrary.setPackageName(String.valueOf(library.get("package")));
			componentLibrary.setFramework("Vue");
			packageList.add(componentLibrary);
		}
		bundleList.setPackageList(packageList);
		return Result.success(bundleList);
	}


	public FileResult bulkCreate(List<Component> componentList) {
		int addNum = 0;
		int updateNum = 0;
		for (Component component : componentList) {
			// 构建查询条件，假设 key 作为唯一键
			// 查询数据库中是否存在该记录
			Component componentParam = new Component();
			componentParam.setComponent(component.getComponent());
			componentParam.setName(component.getName());
			componentParam.setVersion(component.getVersion());
			List<Component> queryComponent = baseMapper.queryComponentByCondition(componentParam);
			// 查询组件库id
			String packageName = null;
			if (component.getNpm() != null && component.getNpm().get("package") != null) {
				packageName = String.valueOf(component.getNpm().get("package"));
			}
			if (packageName != null && !packageName.isEmpty()) {
				ComponentLibrary componentLibrary = new ComponentLibrary();
				componentLibrary.setPackageName(String.valueOf(component.getNpm().get("package")));
				componentLibrary.setVersion(component.getVersion());
				List<ComponentLibrary> componentLibraryList = componentLibraryMapper.queryComponentLibraryByCondition(
					componentLibrary);
				Integer componentLibraryId = null;
				if (!componentLibraryList.isEmpty()) {
					componentLibraryId = componentLibraryList.get(0).getId();
				}
				component.setLibraryId(componentLibraryId);
			}

			if (queryComponent.isEmpty()) {
				// 插入新记录
				Integer result =  baseMapper.createComponent(component);
				if (result == 1) {
					MaterialComponent materialComponent = new MaterialComponent();
					materialComponent.setMaterialId(1);
					materialComponent.setComponentId(component.getId());
					baseMapper.createMaterialComponent(materialComponent);
					MaterialHistoryComponent materialHistoryComponent = new MaterialHistoryComponent();
					materialHistoryComponent.setComponentId(component.getId());
					materialHistoryComponent.setMaterialHistoryId(1);
					baseMapper.createMaterialHistoryComponent(materialHistoryComponent);
				}
				addNum = addNum + 1;
			} else {
				// 更新记录
				component.setId(queryComponent.get(0).getId());
				component.setLastUpdatedTime(LocalDateTime.now());
				baseMapper.updateComponentById(component);
				updateNum = updateNum + 1;
			}
		}

		// 构造返回插入和更新的条数
		FileResult fileResult = new FileResult();
		fileResult.setInsertNum(addNum);
		fileResult.setUpdateNum(updateNum);
		return fileResult;
	}




}
