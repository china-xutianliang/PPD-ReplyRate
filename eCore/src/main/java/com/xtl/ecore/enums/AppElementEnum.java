package com.xtl.ecore.enums;

public enum AppElementEnum {
    ANDROID_TEXT_VIEW("android.widget.TextView", "文本视图"),
    ANDROID_EDIT_TEXT("android.widget.EditText", "输入框"),
    ANDROID_RECYCLER_VIEW("android.support.v7.widget.RecyclerView", "大量数据集合UI"),
    ANDROID_LINEARLAYOUT("android.widget.LinearLayout", "布局容器"),
    ANDROID_BUTTON("android.widget.Button", "按钮"),
    ANDROID_IMAGE_VIEW("android.widget.ImageView", "图片视图"),
    ANDROID_LIST_VIEW("android.widget.ListView", "列表视图"),
    ANDROID_SCROLL_VIEW("android.widget.ScrollView", "滚动视图"),
    ANDROID_CHECK_BOX("android.widget.CheckBox", "复选框"),
    ANDROID_RADIO_BUTTON("android.widget.RadioButton", "单选按钮"),
    ANDROID_SWITCH("android.widget.Switch", "开关按钮"),
    ANDROID_MENU("android.widget.PopupMenu", "菜单"),
    ANDROID_("android.app.Dialog", "对话框");

    private String code;
    private String name;

    AppElementEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
