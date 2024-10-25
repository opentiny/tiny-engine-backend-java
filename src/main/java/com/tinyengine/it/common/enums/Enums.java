
package com.tinyengine.it.common.enums;

/**
 * The type Enums.
 *
 * @since 2024-10-20
 */
public class Enums {
    /**
     * The enum E i 18 belongs.
     */
    public enum I18Belongs {
        /**
         * App e i 18 belongs.
         */
        APP(1),
        /**
         * Block e i 18 belongs.
         */
        BLOCK(2);

        private final int value;

        I18Belongs(int value) {
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
    public enum Schema2CodeType {
        /**
         * Block e schema 2 code type.
         */
        BLOCK("Block"),
        /**
         * Page e schema 2 code type.
         */
        PAGE("Page");

        private final String value;

        Schema2CodeType(String value) {
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
    public enum TaskType {
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
        VSCODPLUGIN_BUILD(4),
        /**
         * Block build e task type.
         */
        BLOCK_BUILD(5);

        private final int value;

        TaskType(int value) {
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
    public enum TaskStatus {
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

        TaskStatus(int value) {
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
    public enum Framework {
        /**
         * Vue e framework.
         */
        VUE("Vue"),
        /**
         * Angular e framework.
         */
        ANGULAR("Angular"),
        /**
         * React e framework.
         */
        REACT("React"),
        /**
         * Html e framework.
         */
        HTML("HTML");

        private final String value;

        Framework(String value) {
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
    public enum TimeRange {
        /**
         * All e time range.
         */
        All("all"),
        /**
         * Today e time range.
         */
        TODAY("today"),
        /**
         * Week e time range.
         */
        WEEK("week"),
        /**
         * Month e time range.
         */
        MONTH("month"),
        /**
         * Longer e time range.
         */
        LONGER("longer");

        private final String value;

        TimeRange(String value) {
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
    public enum UserRoles {
        /**
         * Admin e user roles.
         */
        ADMIN("Tinybuilder_Admin"),
        /**
         * Tenant admin e user roles.
         */
        TENANTADMIN("Tinybuilder_Tenant_Admin"),
        /**
         * Platform admin e user roles.
         */
        PLATFORMADMIN("Tinybuilder_Platform_Admin"),
        /**
         * App admin e user roles.
         */
        APPADMIN("Tinybuilder_App_Admin"),
        /**
         * App developer e user roles.
         */
        APPDEVELOPER("Tinybuilder_App_Developer"),
        /**
         * Master e user roles.
         */
        MASTER("Master"),
        /**
         * Guest e user roles.
         */
        GUEST("Guest");

        private final String value;

        UserRoles(String value) {
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
    public enum AppState {
        /**
         * Created e app state.
         */
        CREATED(0),
        /**
         * Published e app state.
         */
        PUBLISHED(1);

        private final int value;

        AppState(int value) {
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
    public enum SchemaFormatFunc {
        /**
         * To local timestamp e schema format func.
         */
        TO_LOCALTIMESTAMP("toLocalTimestamp"),
        /**
         * To root element e schema format func.
         */
        TO_ROOTELEMENT("toRootElement"),
        /**
         * To group name e schema format func.
         */
        TO_GROUPNAME("toGroupName"),
        /**
         * To creator name e schema format func.
         */
        TO_CREATORNAME("toCreatorName"),
        /**
         * To format string e schema format func.
         */
        TO_FORMATSTRING("toFormatString"),
        /**
         * To array value e schema format func.
         */
        TO_ARRAYVALUE("toArrayValue");

        private final String value;

        SchemaFormatFunc(String value) {
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
    public enum CanvasEditorState {
        /**
         * Release e canvas editor state.
         */
        RELEASE("release"),
        /**
         * Occupy e canvas editor state.
         */
        OCCUPY("occupy");

        private final String value;

        CanvasEditorState(String value) {
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
    public enum SystemUserToken {
        /**
         * Admin e system user token.
         */
        ADMIN("developer-admin"),
        /**
         * Tenant admin e system user token.
         */
        TENANTADMIN("developer-tenant"),
        /**
         * Platform admin e system user token.
         */
        PLATFORMADMIN("developer-platform"),
        /**
         * App admin e system user token.
         */
        APPADMIN("developer-app"),
        /**
         * App developer e system user token.
         */
        APPDEVELOPER("developer-worker"),
        /**
         * Master e system user token.
         */
        MASTER("developer"),
        /**
         * Guest e system user token.
         */
        GUEST("developer-worker");

        private final String value;

        SystemUserToken(String value) {
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
    public enum AppTheme {
        /**
         * Light e app theme.
         */
        LIGHT("light"),
        /**
         * Dark e app theme.
         */
        DARK("Dark");

        private final String value;

        AppTheme(String value) {
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
    public enum ProjectState {
        /**
         * Created e project state.
         */
        CREATED(1),
        /**
         * Published e project state.
         */
        PUBLISHED(2);

        private final int value;

        ProjectState(int value) {
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
     * The enum E task progress.
     */
    public enum TaskProgress {
        /**
         * The Init.
         */
        INIT("Build environment initialization"),
        /**
         * The Install.
         */
        INSTALL("Install build dependencies"),
        /**
         * The After install.
         */
        AFTERINSTALL("Install dependencies complete"),
        /**
         * The Build.
         */
        BUILD("Execute build logic"),
        /**
         * The After build.
         */
        AFTERBUILD("The build logic is executed"),
        /**
         * The Upload.
         */
        UPLOAD("Upload build result"),
        /**
         * The Complete.
         */
        COMPLETE("Build task completed"),
        /**
         * The Update.
         */
        UPDATE("Update datasheet");

        private final String value;

        TaskProgress(String value) {
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
    public enum EcologyCategory {
        /**
         * Dsl e ecology category.
         */
        DSL("dsl"),
        /**
         * Plugin e ecology category.
         */
        PLUGIN("plugin"),
        /**
         * Theme e ecology category.
         */
        THEME("theme"),
        /**
         * Toolbar e ecology category.
         */
        TOOLBAR("toolbar"),
        /**
         * App extend e ecology category.
         */
        APPEXTEND("appExtension");

        private final String value;

        EcologyCategory(String value) {
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
    public enum Encodings {
        /**
         * Utf 8 e encodings.
         */
        UTF8("utf8"),
        /**
         * Base 64 e encodings.
         */
        BASE64("base64"),
        /**
         * Hex e encodings.
         */
        HEX("hex"),
        /**
         * Ascii e encodings.
         */
        ASCII("ascii"),
        /**
         * Utf 16 le e encodings.
         */
        UTF16LE("utf16le"),
        /**
         * Latin 1 e encodings.
         */
        LATIN1("latin1"),
        /**
         * Base 64 url e encodings.
         */
        BASE64URL("base64url"),
        /**
         * Binary e encodings.
         */
        BINARY("binary"),
        /**
         * Ucs 2 e encodings.
         */
        UCS2("ucs2");

        private final String value;

        Encodings(String value) {
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
    public enum I18nLangs {
        /**
         * En us e i 18 n langs.
         */
        EN_US("en_US"),
        /**
         * Zh cn e i 18 n langs.
         */
        ZH_CN("zh_CN");

        private final String value;

        I18nLangs(String value) {
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
    public enum MimeType {
        /**
         * Json e mime type.
         */
        JSON("application/json"),
        /**
         * X zip e mime type.
         */
        XZIP("application/x-zip-compressed"),
        /**
         * Zip e mime type.
         */
        ZIP("application/zip");

        private final String value;

        MimeType(String value) {
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
    public enum IndicatorType {
        /**
         * Duration e indicator type.
         */
        DURATION("duration"),
        /**
         * Cpu e indicator type.
         */
        CPU("cpu"),
        /**
         * Mem e indicator type.
         */
        MEM("mem");

        private final String value;

        IndicatorType(String value) {
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
    public enum Public {
        /**
         * Private e public.
         */
        PRIVATE(0),
        /**
         * Public e public.
         */
        PUBLIC(1),
        /**
         * Semi public e public.
         */
        SemiPublic(2);

        private final int value;

        Public(int value) {
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
    public enum FoundationModel {
        /**
         * Gpt 35 turbo e foundation model.
         */
        // openai
        GPT_35_TURBO("gpt-3.5-turbo"),
        /**
         * Local gpt e foundation model.
         */
        // 本地兼容opanai-api接口的 大语言模型，如chatGLM6b,通义千问 等。
        LOCAL_GPT("qwen-turbo"),
        /**
         * Ernie bot turbo e foundation model.
         */
        // 文心一言
        ERNIBOT_TURBO("ERNIE-Bot-turbo");
        private final String value;

        FoundationModel(String value) {
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
    public enum Scope {
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

        Scope(int value) {
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
