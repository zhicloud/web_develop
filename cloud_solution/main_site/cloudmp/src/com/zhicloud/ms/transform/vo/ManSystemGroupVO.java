package com.zhicloud.ms.transform.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ManSystemGroupVO
 * @Description: 系统角色组实体映射
 * @author 张本缘 于 2015年5月7日 上午9:29:33
 */
public class ManSystemGroupVO implements JSONBean{

    private String billid;
    private String name;
    private String insert_date;
    private String insert_user;

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

}
