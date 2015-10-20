package com.zhicloud.ms.transform.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ManSystemRoleVO
 * @Description: 系统角色实体映射
 * @author 张本缘 于 2015年5月7日 上午9:28:29
 */
public class ManSystemRoleVO implements JSONBean{

    private String billid;
    private String name;
    private String code;
    private String insert_date;
    private String insert_user;
    private String userid;
    private String roleid;
    private String groupid;

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
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

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getInsert_user() {
        return insert_user;
    }

    public void setInsert_user(String insert_user) {
        this.insert_user = insert_user;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    
}
