package com.zhicloud.ms.transform.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ManSystemLogVO
 * @Description: 系统日志实体映射
 * @author 张本缘 于 2015年5月5日 上午11:37:26
 */
public class ManSystemLogVO implements JSONBean{

    private String billid;
    private String operateid;
    private String content;
    private String operate_date;
    private String type;
    private ManSystemUserVO user;

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getOperateid() {
        return operateid;
    }

    public void setOperateid(String operateid) {
        this.operateid = operateid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperate_date() {
        return operate_date;
    }

    public void setOperate_date(String operate_date) {
        this.operate_date = operate_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ManSystemUserVO getUser() {
        return user;
    }

    public void setUser(ManSystemUserVO user) {
        this.user = user;
    }

}
