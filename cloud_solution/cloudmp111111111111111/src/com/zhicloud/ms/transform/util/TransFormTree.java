package com.zhicloud.ms.transform.util;

import java.util.ArrayList;
import java.util.List;

public class TransFormTree {
    
    // 树ID值
    private String id;
    // 树显示值
    private String text;
    // 树是否被显示
    private String ischecked;
    // 子节点
    private List<TransFormTree> children = new ArrayList<TransFormTree>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIschecked() {
        return ischecked;
    }

    public void setIschecked(String ischecked) {
        this.ischecked = ischecked;
    }

    public List<TransFormTree> getChildren() {
        return children;
    }

    public void setChildren(List<TransFormTree> children) {
        this.children = children;
    }


}
