package com.zhicloud.ms.transform.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ManSystemRightVO
 * @Description: 功能权限实体映射
 * @author 张本缘 于 2015年5月12日 下午3:32:42
 */
public class ManSystemRightVO implements JSONBean {
    private String billid;
    private String name;
    private String insert_date;
    private String insert_user;
    private String code;
    private String roleid;
    private String menuid;
    private String menuname;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }
    
    
}
