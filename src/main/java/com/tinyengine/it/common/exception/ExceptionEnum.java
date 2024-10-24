package com.tinyengine.it.common.exception;

/**
 * The enum Exception enum.
 */
public enum ExceptionEnum implements IBaseError {

    /**
     * Success exception enum.
     */
    SUCCESS("200", "操作成功"),
    /**
     * Cm 001 exception enum.
     */
    CM001("CM001", "系统异常，请稍后重试"),
    /**
     * Cm 002 exception enum.
     */
    CM002("CM002", "参数错误"),
    /**
     * Cm 003 exception enum.
     */
    CM003("CM003", "重复创建，请修改传入参数。"),
    /**
     * Cm 004 exception enum.
     */
    CM004("CM004", "认证失败，请重新登录"),
    /**
     * Cm 005 exception enum.
     */
    CM005("CM005", "登录信息已过期，请重新登录"),
    /**
     * Cm 006 exception enum.
     */
    CM006("CM006", "用户未登录。"),
    /**
     * Cm 007 exception enum.
     */
    CM007("CM007", "用户权限不足"),
    /**
     * Cm 008 exception enum.
     */
    CM008("CM008", "数据库错误"),
    /**
     * Cm 009 exception enum.
     */
    CM009("CM009", "找不到资源"),
    /**
     * Cm 010 exception enum.
     */
    CM010("CM010", "无权限，AK，SK认证失败"),

    /**
     * Cm 101 exception enum.
     */
    CM101("CM101", "设计器存在应用实例，无法删除"),
    /**
     * Cm 1110 exception enum.
     */
    CM1110("CM110", "申请加入的组织不存在"),
    /**
     * Cm 120 exception enum.
     */
    CM120("CM120", "设计器未关联物料资产包"),
    /**
     * Cm 121 exception enum.
     */
    CM121("CM121", "设计器未关联主题"),
    /**
     * Cm 122 exception enum.
     */
    CM122("CM122", "设计器未关联 dsl"),
    /**
     * Cm 123 exception enum.
     */
    CM123("CM123", "设计器未关联任何插件"),
    /**
     * Cm 124 exception enum.
     */
    CM124("CM124", "设计器未关联任何工具"),
    /**
     * Cm 125 exception enum.
     */
    CM125("CM125", "设计器关联的物料不存在"),
    /**
     * Cm 126 exception enum.
     */
    CM126("CM126", "设计器关联的生态扩展不存在"),

    /**
     * Cm 201 exception enum.
     */
    CM201("CM201", "有设计器关联，无法执行操作"),
    /**
     * Cm 202 exception enum.
     */
    CM202("CM202", "有物料使用该组件，无法执行操作"),
    /**
     * Cm 203 exception enum.
     */
    CM203("CM203", "有物料使用该区块，无法执行操作"),
    /**
     * Cm 204 exception enum.
     */
    CM204("CM204", "区块构建参数缺少必须内容(content)"),
    /**
     * Cm 205 exception enum.
     */
    CM205("CM205", "区块构建产物的版本已经存在"),
    /**
     * Cm 206 exception enum.
     */
    CM206("CM206", "无权限，分类不属于当前应用"),
    /**
     * Cm 207 exception enum.
     */
    CM207("CM207", "无权限构建该物料"),
    /**
     * Cm 208 exception enum.
     */
    CM208("CM208", "参数错误，关联的模板类型与分组模板类型不符"),

    /**
     * Cm 301 exception enum.
     */
    CM301("CM301", "默认页面不能删除和修改"),
    /**
     * Cm 302 exception enum.
     */
    CM302("CM302", "clone仓库到本地失败"),
    /**
     * Cm 303 exception enum.
     */
    CM303("CM303", "切换到分支失败，请确定分支是否存在"),
    /**
     * Cm 304 exception enum.
     */
    CM304("CM304", "创建并切换到新分支失败"),
    /**
     * Cm 305 exception enum.
     */
    CM305("CM305", "更新代码到本地失败"),
    /**
     * Cm 306 exception enum.
     */
    CM306("CM306", "更新代码到代码仓失败，请确定是否有修改提交"),
    /**
     * Cm 307 exception enum.
     */
    CM307("CM307", "没有上传文件"),
    /**
     * Cm 308 exception enum.
     */
    CM308("CM308", "文件格式必须是json"),
    /**
     * Cm 309 exception enum.
     */
    CM309("CM309", "解析文件异常"),
    /**
     * Cm 310 exception enum.
     */
    CM310("CM310", "批量上传词条失败"),
    /**
     * Cm 311 exception enum.
     */
    CM311("CM311", "此应用没有构建过"),
    /**
     * Cm 312 exception enum.
     */
    CM312("CM312", "开发此应用的设计器未使用正确物料资产包"),
    /**
     * Cm 313 exception enum.
     */
    CM313("CM313", "dsl生成页面代码失败"),
    /**
     * Cm 314 exception enum.
     */
    CM314("CM314", "文件格式必须是zip"),
    /**
     * Cm 315 exception enum.
     */
    CM315("CM315", "设置 git 用户信息出错"),
    /**
     * Cm 316 exception enum.
     */
    CM316("CM316", "未找到 .git 目录"),
    /**
     * Cm 317 exception enum.
     */
    CM317("CM317", "下载模板代码 npm 包出错"),
    /**
     * Cm 318 exception enum.
     */
    CM318("CM318", "获取 tgz 包路径失败"),
    /**
     * Cm 319 exception enum.
     */
    CM319("CM319", "解压 npm 包失败"),

    /**
     * Cm 320 exception enum.
     */
    CM320("CM320", "不能为空"),
    /**
     * Cm 321 exception enum.
     */
    CM321("CM321", "是必须的");
    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    private ExceptionEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
