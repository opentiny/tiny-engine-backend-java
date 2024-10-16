package com.tinyengine.it.config;

public class Enums {
    public enum E_i18Belongs {
        APP(1),
        BLOCK(2);

        private final int value;

        E_i18Belongs(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum E_Schema2CodeType {
        BLOCK("Block"),
        PAGE("Page");

        private final String value;

        E_Schema2CodeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_TaskType {
        ASSETS_BUILD(1),
        APP_BUILD(2),
        PLATFORM_BUILD(3),
        VSCODE_PLUGIN_BUILD(4),
        BLOCK_BUILD(5);

        private final int value;

        E_TaskType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum E_TaskStatus {
        INIT(0),
        RUNNING(1),
        STOPPED(2),
        FINISHED(3);

        private final int value;

        E_TaskStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum E_Framework {
        Vue("Vue"),
        Angular("Angular"),
        React("React"),
        HTML("HTML");

        private final String value;

        E_Framework(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_TimeRange {
        All("all"),
        Today("today"),
        Week("week"),
        Month("month"),
        Longer("longer");

        private final String value;

        E_TimeRange(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_UserRoles {
        Admin("Tinybuilder_Admin"),
        TenantAdmin("Tinybuilder_Tenant_Admin"),
        PlatformAdmin("Tinybuilder_Platform_Admin"),
        AppAdmin("Tinybuilder_App_Admin"),
        AppDeveloper("Tinybuilder_App_Developer"),
        Master("Master"),
        Guest("Guest");

        private final String value;

        E_UserRoles(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_AppState {
        Created(0),
        Published(1);

        private final int value;

        E_AppState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum E_SchemaFormatFunc {
        ToLocalTimestamp("toLocalTimestamp"),
        ToRootElement("toRootElement"),
        ToGroupName("toGroupName"),
        ToCreatorName("toCreatorName"),
        ToFormatString("toFormatString"),
        ToArrayValue("toArrayValue");

        private final String value;

        E_SchemaFormatFunc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_CanvasEditorState {
        Release("release"),
        Occupy("occupy");

        private final String value;

        E_CanvasEditorState(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_SystemUserToken {
        Admin("developer-admin"),
        TenantAdmin("developer-tenant"),
        PlatformAdmin("developer-platform"),
        AppAdmin("developer-app"),
        AppDeveloper("developer-worker"),
        Master("developer"),
        Guest("developer-worker");

        private final String value;

        E_SystemUserToken(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_AppTheme {
        Light("light"),
        Dark("Dark");

        private final String value;

        E_AppTheme(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_ProjectState {
        Created(1),
        Published(2);

        private final int value;

        E_ProjectState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum E_TYPES {
        Angular("ng-tiny"),
        React("react-fusion"),
        Vue("vue-tiny"),
        Html("html-vanilla");

        private final String value;

        E_TYPES(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_Task_Progress {
        Init("Build environment initialization"),
        Install("Install build dependencies"),
        AfterInstall("Install dependencies complete"),
        Build("Execute build logic"),
        AfterBuild("The build logic is executed"),
        Upload("Upload build result"),
        Complete("Build task completed"),
        Update("Update datasheet");

        private final String value;

        E_Task_Progress(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_Ecology_Category {
        Dsl("dsl"),
        Plugin("plugin"),
        Theme("theme"),
        Toolbar("toolbar"),
        AppExtend("appExtension");

        private final String value;

        E_Ecology_Category(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_Encodings {
        Utf8("utf8"),
        Base64("base64"),
        Hex("hex"),
        Ascii("ascii"),
        Utf16le("utf16le"),
        Latin1("latin1"),
        Base64url("base64url"),
        Binary("binary"),
        Ucs2("ucs2");

        private final String value;

        E_Encodings(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_I18nLangs {
        en_US("en_US"),
        zh_CN("zh_CN");

        private final String value;

        E_I18nLangs(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_MimeType {
        Json("application/json"),
        xZip("application/x-zip-compressed"),
        Zip("application/zip");

        private final String value;

        E_MimeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_IndicatorType {
        Duration("duration"),
        Cpu("cpu"),
        Mem("mem");

        private final String value;

        E_IndicatorType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum E_Public {
        Private(0),
        Public(1),
        SemiPublic(2);

        private final int value;

        E_Public(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum E_FOUNDATION_MODEL {
        GPT_35_TURBO("gpt-3.5-turbo"),  // openai
        Local_GPT("qwen-turbo"),  //本地兼容opanai-api接口的 大语言模型，如chatGLM6b,通义千问 等。
        ERNIE_BOT_TURBO("ERNIE-Bot-turbo");  // 文心一言
        private final String value;


        E_FOUNDATION_MODEL(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PUBLIC_SCOPE {
        PRIVATE(0),
        FULL_PUBLIC(1),
        PUBLIC_IN_TENANTS(2);

        private final int value;

        PUBLIC_SCOPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
