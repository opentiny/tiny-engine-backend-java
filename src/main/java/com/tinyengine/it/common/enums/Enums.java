package com.tinyengine.it.common.enums;

/**
 * The type Enums.
 */
public class Enums {
    /**
     * The enum E i 18 belongs.
     */
    public enum E_i18Belongs {
        /**
         * App e i 18 belongs.
         */
        APP(1),
        /**
         * Block e i 18 belongs.
         */
        BLOCK(2);

        private final int value;

        E_i18Belongs(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum E schema 2 code type.
     */
    public enum E_Schema2CodeType {
        /**
         * Block e schema 2 code type.
         */
        BLOCK("Block"),
        /**
         * Page e schema 2 code type.
         */
        PAGE("Page");

        private final String value;

        E_Schema2CodeType(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E task type.
     */
    public enum E_TaskType {
        /**
         * Assets build e task type.
         */
        ASSETS_BUILD(1),
        /**
         * App build e task type.
         */
        APP_BUILD(2),
        /**
         * Platform build e task type.
         */
        PLATFORM_BUILD(3),
        /**
         * Vscode plugin build e task type.
         */
        VSCODE_PLUGIN_BUILD(4),
        /**
         * Block build e task type.
         */
        BLOCK_BUILD(5);

        private final int value;

        E_TaskType(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum E task status.
     */
    public enum E_TaskStatus {
        /**
         * Init e task status.
         */
        INIT(0),
        /**
         * Running e task status.
         */
        RUNNING(1),
        /**
         * Stopped e task status.
         */
        STOPPED(2),
        /**
         * Finished e task status.
         */
        FINISHED(3);

        private final int value;

        E_TaskStatus(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum E framework.
     */
    public enum E_Framework {
        /**
         * Vue e framework.
         */
        Vue("Vue"),
        /**
         * Angular e framework.
         */
        Angular("Angular"),
        /**
         * React e framework.
         */
        React("React"),
        /**
         * Html e framework.
         */
        HTML("HTML");

        private final String value;

        E_Framework(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E time range.
     */
    public enum E_TimeRange {
        /**
         * All e time range.
         */
        All("all"),
        /**
         * Today e time range.
         */
        Today("today"),
        /**
         * Week e time range.
         */
        Week("week"),
        /**
         * Month e time range.
         */
        Month("month"),
        /**
         * Longer e time range.
         */
        Longer("longer");

        private final String value;

        E_TimeRange(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E user roles.
     */
    public enum E_UserRoles {
        /**
         * Admin e user roles.
         */
        Admin("Tinybuilder_Admin"),
        /**
         * Tenant admin e user roles.
         */
        TenantAdmin("Tinybuilder_Tenant_Admin"),
        /**
         * Platform admin e user roles.
         */
        PlatformAdmin("Tinybuilder_Platform_Admin"),
        /**
         * App admin e user roles.
         */
        AppAdmin("Tinybuilder_App_Admin"),
        /**
         * App developer e user roles.
         */
        AppDeveloper("Tinybuilder_App_Developer"),
        /**
         * Master e user roles.
         */
        Master("Master"),
        /**
         * Guest e user roles.
         */
        Guest("Guest");

        private final String value;

        E_UserRoles(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E app state.
     */
    public enum E_AppState {
        /**
         * Created e app state.
         */
        Created(0),
        /**
         * Published e app state.
         */
        Published(1);

        private final int value;

        E_AppState(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum E schema format func.
     */
    public enum E_SchemaFormatFunc {
        /**
         * To local timestamp e schema format func.
         */
        ToLocalTimestamp("toLocalTimestamp"),
        /**
         * To root element e schema format func.
         */
        ToRootElement("toRootElement"),
        /**
         * To group name e schema format func.
         */
        ToGroupName("toGroupName"),
        /**
         * To creator name e schema format func.
         */
        ToCreatorName("toCreatorName"),
        /**
         * To format string e schema format func.
         */
        ToFormatString("toFormatString"),
        /**
         * To array value e schema format func.
         */
        ToArrayValue("toArrayValue");

        private final String value;

        E_SchemaFormatFunc(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E canvas editor state.
     */
    public enum E_CanvasEditorState {
        /**
         * Release e canvas editor state.
         */
        Release("release"),
        /**
         * Occupy e canvas editor state.
         */
        Occupy("occupy");

        private final String value;

        E_CanvasEditorState(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E system user token.
     */
    public enum E_SystemUserToken {
        /**
         * Admin e system user token.
         */
        Admin("developer-admin"),
        /**
         * Tenant admin e system user token.
         */
        TenantAdmin("developer-tenant"),
        /**
         * Platform admin e system user token.
         */
        PlatformAdmin("developer-platform"),
        /**
         * App admin e system user token.
         */
        AppAdmin("developer-app"),
        /**
         * App developer e system user token.
         */
        AppDeveloper("developer-worker"),
        /**
         * Master e system user token.
         */
        Master("developer"),
        /**
         * Guest e system user token.
         */
        Guest("developer-worker");

        private final String value;

        E_SystemUserToken(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E app theme.
     */
    public enum E_AppTheme {
        /**
         * Light e app theme.
         */
        Light("light"),
        /**
         * Dark e app theme.
         */
        Dark("Dark");

        private final String value;

        E_AppTheme(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E project state.
     */
    public enum E_ProjectState {
        /**
         * Created e project state.
         */
        Created(1),
        /**
         * Published e project state.
         */
        Published(2);

        private final int value;

        E_ProjectState(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum E types.
     */
    public enum E_TYPES {
        /**
         * Angular e types.
         */
        Angular("ng-tiny"),
        /**
         * React e types.
         */
        React("react-fusion"),
        /**
         * Vue e types.
         */
        Vue("vue-tiny"),
        /**
         * Html e types.
         */
        Html("html-vanilla");

        private final String value;

        E_TYPES(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E task progress.
     */
    public enum E_Task_Progress {
        /**
         * The Init.
         */
        Init("Build environment initialization"),
        /**
         * The Install.
         */
        Install("Install build dependencies"),
        /**
         * The After install.
         */
        AfterInstall("Install dependencies complete"),
        /**
         * The Build.
         */
        Build("Execute build logic"),
        /**
         * The After build.
         */
        AfterBuild("The build logic is executed"),
        /**
         * The Upload.
         */
        Upload("Upload build result"),
        /**
         * The Complete.
         */
        Complete("Build task completed"),
        /**
         * The Update.
         */
        Update("Update datasheet");

        private final String value;

        E_Task_Progress(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E ecology category.
     */
    public enum E_Ecology_Category {
        /**
         * Dsl e ecology category.
         */
        Dsl("dsl"),
        /**
         * Plugin e ecology category.
         */
        Plugin("plugin"),
        /**
         * Theme e ecology category.
         */
        Theme("theme"),
        /**
         * Toolbar e ecology category.
         */
        Toolbar("toolbar"),
        /**
         * App extend e ecology category.
         */
        AppExtend("appExtension");

        private final String value;

        E_Ecology_Category(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E encodings.
     */
    public enum E_Encodings {
        /**
         * Utf 8 e encodings.
         */
        Utf8("utf8"),
        /**
         * Base 64 e encodings.
         */
        Base64("base64"),
        /**
         * Hex e encodings.
         */
        Hex("hex"),
        /**
         * Ascii e encodings.
         */
        Ascii("ascii"),
        /**
         * Utf 16 le e encodings.
         */
        Utf16le("utf16le"),
        /**
         * Latin 1 e encodings.
         */
        Latin1("latin1"),
        /**
         * Base 64 url e encodings.
         */
        Base64url("base64url"),
        /**
         * Binary e encodings.
         */
        Binary("binary"),
        /**
         * Ucs 2 e encodings.
         */
        Ucs2("ucs2");

        private final String value;

        E_Encodings(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E i 18 n langs.
     */
    public enum E_I18nLangs {
        /**
         * En us e i 18 n langs.
         */
        en_US("en_US"),
        /**
         * Zh cn e i 18 n langs.
         */
        zh_CN("zh_CN");

        private final String value;

        E_I18nLangs(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E mime type.
     */
    public enum E_MimeType {
        /**
         * Json e mime type.
         */
        Json("application/json"),
        /**
         * X zip e mime type.
         */
        xZip("application/x-zip-compressed"),
        /**
         * Zip e mime type.
         */
        Zip("application/zip");

        private final String value;

        E_MimeType(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E indicator type.
     */
    public enum E_IndicatorType {
        /**
         * Duration e indicator type.
         */
        Duration("duration"),
        /**
         * Cpu e indicator type.
         */
        Cpu("cpu"),
        /**
         * Mem e indicator type.
         */
        Mem("mem");

        private final String value;

        E_IndicatorType(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum E public.
     */
    public enum E_Public {
        /**
         * Private e public.
         */
        Private(0),
        /**
         * Public e public.
         */
        Public(1),
        /**
         * Semi public e public.
         */
        SemiPublic(2);

        private final int value;

        E_Public(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * The enum E foundation model.
     */
    public enum E_FOUNDATION_MODEL {
        /**
         * Gpt 35 turbo e foundation model.
         */
        GPT_35_TURBO("gpt-3.5-turbo"),  // openai
        /**
         * Local gpt e foundation model.
         */
        Local_GPT("qwen-turbo"),  //本地兼容opanai-api接口的 大语言模型，如chatGLM6b,通义千问 等。
        /**
         * Ernie bot turbo e foundation model.
         */
        ERNIE_BOT_TURBO("ERNIE-Bot-turbo");  // 文心一言
        private final String value;

        E_FOUNDATION_MODEL(String value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * The enum Public scope.
     */
    public enum PUBLIC_SCOPE {
        /**
         * Private public scope.
         */
        PRIVATE(0),
        /**
         * Full public public scope.
         */
        FULL_PUBLIC(1),
        /**
         * Public in tenants public scope.
         */
        PUBLIC_IN_TENANTS(2);

        private final int value;

        PUBLIC_SCOPE(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

}
