
package com.tinyengine.it.service.app.impl.v1;

import static com.tinyengine.it.common.utils.Utils.findMaxVersion;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.utils.Schema;
import com.tinyengine.it.common.utils.Utils;
import com.tinyengine.it.config.log.SystemServiceLog;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.MaterialHistoryMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.model.dto.BlockHistoryDto;
import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.MaterialHistoryMsg;
import com.tinyengine.it.model.dto.MetaDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.app.I18nEntryService;
import com.tinyengine.it.service.app.v1.AppV1Service;
import com.tinyengine.it.service.platform.PlatformService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type App v 1 service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class AppV1ServiceImpl implements AppV1Service {
    private final List<String> exposedFields = Arrays.asList("config", "constants", "css");
    /**
     * The App mapper.
     */
    @Autowired
    private AppMapper appMapper;
    /**
     * The 18 n entry mapper.
     */
    @Autowired
    private I18nEntryMapper i18nEntryMapper;
    /**
     * The 18 n entry service.
     */
    @Autowired
    private I18nEntryService i18nEntryService;
    /**
     * The App extension mapper.
     */
    @Autowired
    private AppExtensionMapper appExtensionMapper;
    /**
     * The Datasource mapper.
     */
    @Autowired
    private DatasourceMapper datasourceMapper;
    /**
     * The Page mapper.
     */
    @Autowired
    private PageMapper pageMapper;
    /**
     * The Block history mapper.
     */
    @Autowired
    private BlockHistoryMapper blockHistoryMapper;
    /**
     * The Block group mapper.
     */
    @Autowired
    private BlockGroupMapper blockGroupMapper;
    /**
     * The Material history mapper.
     */
    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;
    /**
     * The Platform service.
     */
    @Autowired
    private PlatformService platformService;
    private MetaDto metaDto;

    /**
     * 获取应用schema
     *
     * @param id id
     * @return app schema
     */
    @SystemServiceLog(description = "获取app schema 实现类")
    @Override
    public Map<String, Object> appSchema(Integer id) {
        this.metaDto = setMeta(id);
        Map<String, Object> schema = new HashMap<>();
        Map<String, Object> meta = getSchemaMeta();
        schema.put("meta", meta);
        List<Datasource> list = this.metaDto.getSource();
        Map<String, Object> dataHandler = this.metaDto.getApp().getDataSourceGlobal();
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.putAll(dataHandler);
        schema.put("dataSource", data);
        Map<String, Map<String, String>> i18n = getSchemaI18n();
        schema.put("i18n", i18n);
        List<Map<String, Object>> componentsTree = getSchemaComponentsTree(this.metaDto);
        schema.put("componentsTree", componentsTree);
        List<Map<String, Object>> componentsMap = getSchemaComponentsMap(this.metaDto);
        schema.put("componentsMap", componentsMap);
        // 单独处理混合了bridge和utils的extensions
        Map<String, Object> extensions = getSchemaExtensions(this.metaDto.getExtension());
        schema.put("bridge", extensions.get("bridge"));
        schema.put("utils", extensions.get("utils"));

        // 拷贝属性
        Map<String, Object> app = Utils.convert(this.metaDto.getApp());
        for (String field : exposedFields) {
            schema.put(field, app.getOrDefault(field, ""));
        }
        schema.put("version", "");
        return schema;
    }

    @SystemServiceLog(description = "合并数据实现类")
    @Override
    public Map<String, Map<String, String>> mergeEntries(Map<String, Map<String, String>> appEntries,
            Map<String, Map<String, String>> blockEntries) {
        // 直接将 blockEntries 赋值给 res

        if (appEntries == null || blockEntries == null) {
            return (appEntries != null) ? appEntries : blockEntries;
        }

        // 遇到相同的key，用应用的词条覆盖区块的
        for (Map.Entry<String, Map<String, String>> appEntry : appEntries.entrySet()) {
            String lang = appEntry.getKey();
            Map<String, String> langEntries = appEntry.getValue();

            blockEntries.putIfAbsent(lang, new HashMap<>());

            for (Map.Entry<String, String> langEntry : langEntries.entrySet()) {
                String key = langEntry.getKey();
                String value = langEntry.getValue();
                blockEntries.get(lang).put(key, value);
            }

            // 如果区块没有这个国际化分组，把应用的合并进来
            if (!blockEntries.containsKey(lang)) {
                blockEntries.put(lang, langEntries);
            }
        }

        return blockEntries;
    }

    /**
     * 获取元数据
     */
    private Map<String, Object> getSchemaMeta() {
        Map<String, Object> appData = Utils.convert(this.metaDto.getApp());
        Map<String, Object> config = new HashMap<>();
        config.put("sdkVersion", "1.0.3");
        config.put("historyMode", "hash");
        config.put("targetRootID", "app");
        this.metaDto.getApp().setConfig(config);
        Schema schema = new Schema();
        String type = "app";
        return schema.assembleFields(appData, type);
    }

    /**
     * 获取应用信息
     *
     * @param id the id
     * @return the meta
     */
    public MetaDto setMeta(Integer id) {
        App app = appMapper.queryAppById(id);
        MaterialHistoryMsg materialhistoryMsg = new MaterialHistoryMsg();

        Platform platform = platformService.queryPlatformById(app.getPlatformId());
        // 当前版本暂无设计器数据
        if (platform == null) {
            materialhistoryMsg.setMaterialHistoryId(1);
        } else {
            materialhistoryMsg.setMaterialHistoryId(platform.getMaterialHistoryId());
        }
        materialhistoryMsg.setIsUnpkg(true);

        MetaDto metaDto = new MetaDto();
        metaDto.setApp(app);

        List<Page> page = pageMapper.queryPageByApp(app.getId());
        metaDto.setPages(page);

        I18nEntry i18nEntry = new I18nEntry();
        i18nEntry.setHost(app.getId());
        i18nEntry.setHostType("app");
        List<I18nEntryDto> i18n = i18nEntryMapper.findI18nEntriesByHostandHostType(app.getId(), "app");
        metaDto.setI18n(i18n);

        Datasource datasource = new Datasource();
        datasource.setApp(app.getId());
        List<Datasource> datasourceList = datasourceMapper.queryDatasourceByCondition(datasource);
        metaDto.setSource(datasourceList);

        AppExtension appExtension = new AppExtension();
        appExtension.setApp(app.getId());
        List<AppExtension> appExtensionList = appExtensionMapper.queryAppExtensionByCondition(appExtension);
        metaDto.setExtension(appExtensionList);

        MaterialHistory materialHistory = materialHistoryMapper
                .queryMaterialHistoryById(materialhistoryMsg.getMaterialHistoryId());
        metaDto.setMaterialHistory(materialHistory);

        List<BlockHistory> blockHistory = getBlockHistory(app, materialhistoryMsg);
        metaDto.setBlockHistories(blockHistory);

        return metaDto;
    }

    /**
     * 获取区块历史信息
     *
     * @param app 应用信息 materialhistoryMsg 物料历史信息
     * @return {Promise<any>} 区块历史信息
     */
    private List<BlockHistory> getBlockHistory(App app, MaterialHistoryMsg materialhistoryMsg) {
        Boolean isUnpkg = materialhistoryMsg.getIsUnpkg();
        Integer materialHistoryId = materialhistoryMsg.getMaterialHistoryId();
        List<Integer> materialBlockHistoryId = new ArrayList<>();
        List<BlockHistory> blockHistory = new ArrayList<>();
        List<BlockVersionDto> blocksVersionCtl;
        if (!isUnpkg) {
            // 不是unpkg的旧版本，部分区块构建产物id直接从关联关系取
            materialBlockHistoryId = materialHistoryMapper.queryBlockHistoryBymaterialHistoryId(materialHistoryId);
            /*
             * 其余区块构建产物信息根据 区块分组的关联信息
             * 这里需要注意，区块分组中关联的区块， 有的是版本控制的区块，有的是旧的存量数据
             * 对于存量数据默认返回最新一次的区块发布记录
             */
            Integer historyId = 0;
            blocksVersionCtl = getAppBlocksVersionCtl(app, historyId);
        } else {
            blocksVersionCtl = getAppBlocksVersionCtl(app, materialHistoryId);
        }
        if (blocksVersionCtl.isEmpty()) {
            return blockHistory;
        }
        List<Integer> blockHistoryIds = getBlockHistoryIdBySemver(blocksVersionCtl);
        blockHistoryIds.addAll(materialBlockHistoryId);
        List<Integer> listWithoutDuplicates = Utils.removeDuplicates(blockHistoryIds);
        blockHistory = blockHistoryMapper.queryBlockHistoryByIds(listWithoutDuplicates);
        return blockHistory;
    }

    /**
     * 获取应用关联的区块及版本信息
     *
     * @param app appInfo 应用信息
     * @param materialHistoryId materialHistoryId
     * @return {Promise<any>} 应用关联的区块版本控制信息
     */
    private List<BlockVersionDto> getAppBlocksVersionCtl(App app, Integer materialHistoryId) {
        List<BlockGroup> blockGroupsList = blockGroupMapper.queryBlockGroupByApp(app.getId());
        if (blockGroupsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> blockGroupsIds = blockGroupsList.stream().map(BlockGroup::getId).collect(Collectors.toList());

        return blockHistoryMapper.queryBlockAndVersion(blockGroupsIds, materialHistoryId);
    }

    /**
     * 有版本控制的利用 semver 比较版本信息， 得到 historyId 数组。 没有版本控制的返回最新的历史记录id。
     *
     * @param blocksVersionCtl the blocks version ctl
     * @return the block history id by semver
     */
    public List<Integer> getBlockHistoryIdBySemver(List<BlockVersionDto> blocksVersionCtl) {
        // 获取 区块id-区块历史记录id-区块历史记录版本 集合 [{blockId:995,historyId:1145,version: '1.0.4'}]
        List<Integer> blockId = blocksVersionCtl.stream().map(BlockVersionDto::getBlock).collect(Collectors.toList());
        List<BlockHistoryDto> blockHistory = blockHistoryMapper.queryMapByIds(blockId);
        // 将 集合序列化为 综合信息映射(区块id 为key 的map, map 中保存了 k-v 为 区块版本-区块历史id的map 和 版本数组)
        List<Integer> historiesId = new ArrayList<>();
        if (blockHistory.isEmpty()) {
            return historiesId;
        }
        Map<String, Map<String, Object>> blocksVersionMap = getStringMapMap(blockHistory);

        // 遍历区块历史记录 综合信息映射关系
        for (String key : blocksVersionMap.keySet()) {
            Map<String, Object> keyMap = blocksVersionMap.get(key);
            List<String> versions = (List<String>) keyMap.get("versions");

            String targetVersion;
            // 默认先取最新的
            if (!versions.isEmpty()) {
                targetVersion = findMaxVersion(versions);
            } else {
                targetVersion = versions.get(versions.size() - 1);
            }
            Map<String, Object> historyMap = new HashMap<>();
            historyMap = (Map<String, Object>) keyMap.get("historyMap");
            Integer historyId = (Integer) historyMap.get(targetVersion);
            historiesId.add(historyId);
        }
        return historiesId;
    }

    private static Map<String, Map<String, Object>> getStringMapMap(List<BlockHistoryDto> blockHistory) {
        Map<String, Map<String, Object>> blocksVersionMap = new HashMap<>();
        for (BlockHistoryDto item : blockHistory) {
            Map<String, Object> itemMap = new HashMap<>();
            Map<String, Object> historyMap = new HashMap<>();
            List<String> versionList = new ArrayList<>();
            String version = item.getVersion();
            versionList.add(version);
            historyMap.put(version, item.getHistoryId());
            itemMap.put("historyMap", historyMap);
            itemMap.put("versions", versionList);
            blocksVersionMap.put("blockId", itemMap);
        }
        return blocksVersionMap;
    }

    private Map<String, Map<String, String>> getSchemaI18n() {
        List<BlockHistory> blockHistoryList = this.metaDto.getBlockHistories();
        List<I18nEntryDto> i18n = this.metaDto.getI18n();
        Map<String, Map<String, String>> blockEntries = new HashMap<>();
        // 提取区块构建产物中的国际化词条
        for (BlockHistory blockHistory : blockHistoryList) {
            blockEntries = mergeEntries(blockHistory.getI18n(), blockEntries);
        }
        // 序列化国际化词条
        Map<String, Map<String, String>> appEntries = i18nEntryService.formatEntriesList(i18n);

        return mergeEntries(appEntries, blockEntries);
    }

    /**
     * Gets schema components tree.
     *
     * @param metaDto the meta dto
     * @return the schema components tree
     * @throws ServiceException the service exception
     */
    public List<Map<String, Object>> getSchemaComponentsTree(MetaDto metaDto) {
        List<Map<String, Object>> pageSchemas = new ArrayList<>();
        List<Page> pageList = metaDto.getPages();
        App app = metaDto.getApp();
        // 创建字符串列表
        List<String> resKeys = new ArrayList<>();
        resKeys.add("is_body");
        resKeys.add("parent_id");
        resKeys.add("is_page");
        resKeys.add("is_default");
        for (Page pageInfo : pageList) {
            Map<String, Object> data = Utils.convert(pageInfo);
            boolean isToLine = false;
            Map<String, Object> page = formatDataFields(data, resKeys, isToLine);
            page.put("isHome", String.valueOf(page.get("id")).equals(app.getHomePage().toString()));
            Map<String, Object> schema;
            Schema schemaUtil = new Schema();
            if (!pageInfo.getIsPage()) {
                schema = schemaUtil.getFolderSchema(page);
            } else {
                schema = schemaUtil.getSchemaBase(page);
                // 从page_schema中获取基本字段
                String type = "pageMeta";
                Map<String, Object> meta = schemaUtil.assembleFields(page, type);
                schema.put("meta", meta);
            }
            pageSchemas.add(schema);
        }
        return pageSchemas;
    }

    /**
     * Gets schema components map.
     *
     * @param metaDto the meta dto
     * @return the schema components map
     */
    public List<Map<String, Object>> getSchemaComponentsMap(MetaDto metaDto) {
        MaterialHistory materialHistory = metaDto.getMaterialHistory();
        List<BlockHistory> blockHistories = metaDto.getBlockHistories();
        // 转换区块数据为schema
        List<Map<String, Object>> blocksSchema = getBlockSchema(blockHistories);
        // 转换组件数据为schema
        List<Component> components = materialHistory.getComponents();
        List<Map<String, Object>> componentsSchema = getComponentSchema(components);
        // 合并两个 List
        List<Map<String, Object>> componentsMap = new ArrayList<>(componentsSchema);
        componentsMap.addAll(blocksSchema);
        return componentsMap;
    }

    // 将区块组装成schema数据
    private List<Map<String, Object>> getBlockSchema(List<BlockHistory> blockHistories) {
        List<Map<String, Object>> schemas = new ArrayList<>();
        for (BlockHistory blockHistory : blockHistories) {
            String path = blockHistory.getPath();
            String version = blockHistory.getVersion();
            Map<String, Object> content = blockHistory.getContent();

            if (content == null) {
                throw new IllegalArgumentException("Each block history record must have content");
            }

            String componentName = (String) content.get("fileName");
            Map<String, Object> dependencies = (Map<String, Object>) content.get("dependencies");

            Map<String, Object> schema = new HashMap<>();
            schema.put("componentName", componentName);
            schema.put("dependencies", dependencies);
            schema.put("path", path != null ? path : "");
            schema.put("destructuring", false);
            schema.put("version", version != null ? version : "");

            schemas.add(schema);
        }
        return schemas;
    }

    // 转换组件schema数据
    private List<Map<String, Object>> getComponentSchema(List<Component> components) {
        List<Map<String, Object>> schemas = new ArrayList<>();
        if (components.isEmpty()) {
            return schemas;
        }
        for (Component component : components) {
            String componentName = component.getComponent();
            Map<String, Object> npm = component.getNpm();
            String packageName = (String) npm.get("package");
            String exportName = (String) npm.get("exportName");
            String version = (String) npm.get("version");
            Boolean destructuring = (Boolean) npm.get("destructuring");

            Map<String, Object> schema = new HashMap<>();
            schema.put("componentName", componentName);
            schema.put("package", packageName);
            schema.put("exportName", exportName);
            schema.put("destructuring", destructuring != null ? destructuring : false);
            schema.put("version", version != null ? version : "");
            schemas.add(schema);
        }
        return schemas;
    }

    /**
     * Gets schema extensions.
     *
     * @param appExtensionList the app extension list
     * @return the schema extensions
     */
    public Map<String, Object> getSchemaExtensions(List<AppExtension> appExtensionList) {
        List<Map<String, Object>> bridge = new ArrayList<>();
        List<Map<String, Object>> utils = new ArrayList<>();

        for (AppExtension item : appExtensionList) {
            String name = item.getName();
            String type = item.getType();
            Object content = item.getContent();
            String category = item.getCategory();

            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("type", type);
            data.put("content", content);

            if ("bridge".equals(category)) {
                bridge.add(data);
            } else {
                utils.add(data);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("bridge", bridge);
        result.put("utils", utils);
        return result;
    }

    /**
     * Format data fields map.
     *
     * @param data the data
     * @param fields the fields
     * @param isToLine the is to line
     * @return the map
     * @throws ServiceException the service exception
     */
    public Map<String, Object> formatDataFields(Map<String, Object> data, List<String> fields, boolean isToLine)
            throws ServiceException {
        // 获取 toLine 和 toHump 方法
        Function<String, String> format = isToLine ? Utils::toLine : Utils::toHump;
        // 将 fields 转换为 HashMap
        Map<String, Object> fieldsMap = new HashMap<>();
        for (Object field : fields) {
            if (field instanceof String) {
                fieldsMap.put((String) field, true);
            } else if (field instanceof IFieldItem) {
                IFieldItem fieldItem = (IFieldItem) field;
                fieldsMap.put(fieldItem.getKey(), fieldItem.getValue());
            }
        }

        Map<String, Object> res = new HashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object val = fieldsMap.get(key);
            if (val != null) {
                String convert = (val.equals(true)) ? format.apply(key) : (String) val;
                res.put(convert, entry.getValue());
            } else {
                res.put(key, entry.getValue());
            }
        }

        return res;
    }

    /**
     * The interface Field item.
     */
    public interface IFieldItem {
        /**
         * Gets key.
         *
         * @return the key
         */
        String getKey();

        /**
         * Gets value.
         *
         * @return the value
         */
        Object getValue();
    }
}
