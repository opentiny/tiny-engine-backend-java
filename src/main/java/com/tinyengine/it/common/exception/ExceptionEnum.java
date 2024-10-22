package com.tinyengine.it.common.exception;

public enum ExceptionEnum implements IBaseError {

    SUCCESS("200", "操作成功"),
    CM001("CM001", "系统异常，请稍后重试"),
    CM002("CM002", "参数错误"),
    CM003("CM003", "重复创建，请修改传入参数。"),
    CM004("CM004", "认证失败，请重新登录"),
    CM005("CM005", "登录信息已过期，请重新登录"),
    CM006("CM006", "用户未登录。"),
    CM007("CM007", "用户权限不足"),
    CM008("CM008", "数据库错误"),
    CM009("CM009", "找不到资源"),
    CM010("CM010", "无权限，AK，SK认证失败"),

    CM101("CM101", "设计器存在应用实例，无法删除"),
    CM1110("CM110", "申请加入的组织不存在"),
    CM120("CM120", "设计器未关联物料资产包"),
    CM121("CM121", "设计器未关联主题"),
    CM122("CM122", "设计器未关联 dsl"),
    CM123("CM123", "设计器未关联任何插件"),
    CM124("CM124", "设计器未关联任何工具"),
    CM125("CM125", "设计器关联的物料不存在"),
    CM126("CM126", "设计器关联的生态扩展不存在"),

    CM201("CM201", "有设计器关联，无法执行操作"),
    CM202("CM202", "有物料使用该组件，无法执行操作"),
    CM203("CM203", "有物料使用该区块，无法执行操作"),
    CM204("CM204", "区块构建参数缺少必须内容(content)"),
    CM205("CM205", "区块构建产物的版本已经存在"),
    CM206("CM206", "无权限，分类不属于当前应用"),
    CM207("CM207", "无权限构建该物料"),
    CM208("CM208", "参数错误，关联的模板类型与分组模板类型不符"),

    CM301("CM301", "默认页面不能删除和修改"),
    CM302("CM302", "clone仓库到本地失败"),
    CM303("CM303", "切换到分支失败，请确定分支是否存在"),
    CM304("CM304", "创建并切换到新分支失败"),
    CM305("CM305", "更新代码到本地失败"),
    CM306("CM306", "更新代码到代码仓失败，请确定是否有修改提交"),
    CM307("CM307", "没有上传文件"),
    CM308("CM308", "文件格式必须是json"),
    CM309("CM309", "解析文件异常"),
    CM310("CM310", "批量上传词条失败"),
    CM311("CM311", "此应用没有构建过"),
    CM312("CM312", "开发此应用的设计器未使用正确物料资产包"),
    CM313("CM313", "dsl生成页面代码失败"),
    CM314("CM314", "文件格式必须是zip"),
    CM315("CM315", "设置 git 用户信息出错"),
    CM316("CM316", "未找到 .git 目录"),
    CM317("CM317", "下载模板代码 npm 包出错"),
    CM318("CM318", "获取 tgz 包路径失败"),
    CM319("CM319", "解压 npm 包失败"),

    CM320("CM320", "不能为空"),
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
