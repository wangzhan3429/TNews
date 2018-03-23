package com.wz.tnews.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by v_wangzhan on 2017/10/18.
 */

public class CommentObject extends BmobObject {
    private String title;
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}
