
package com.zhicloud.ms.vo;

/**
 * @ClassName: SysWarnValueVO
 * @Description: 预警规则指标值
 * @author 张本缘 于 2015年7月13日 上午10:37:34
 */
public class SysWarnValueVO {

    private String id;
    private String name;
    private String code;
    private String operator;
    private String value;
    private String contact;
    private Integer sort;
    private String ruleid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRuleid() {
        return ruleid;
    }

    public void setRuleid(String ruleid) {
        this.ruleid = ruleid;
    }

}
