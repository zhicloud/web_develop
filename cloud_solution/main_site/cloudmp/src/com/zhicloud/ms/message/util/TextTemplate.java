package com.zhicloud.ms.message.util;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/8/15.
 */
public class TextTemplate {
    private String template;

    public TextTemplate() {
        setTemplate("");
    }

    public TextTemplate(String template) {
        setTemplate(template);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    // 填充指定模板，返回结果字符串
    // 模板参数替换 ${key} 替换为对应 value
    public String fillTemplate(LinkedHashMap<String, Object> kvs) {
        List<String> searchList = new ArrayList<>();
        List<Object> replacementList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : kvs.entrySet()) {
            searchList.add("${" + entry.getKey() + "}");
            replacementList.add(entry.getValue());
        }
        int size = searchList.size();
        return StringUtils.replaceEachRepeatedly(template, searchList.toArray(new String[size]),
                replacementList.toArray(new String[size]));
    }
}
