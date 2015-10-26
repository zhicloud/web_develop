
package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: LogVO
 * @Description: 日志实体对象
 * @author 张本缘 于 2015年9月7日 下午2:48:28
 */
public class LogVO implements JSONBean {
    /* 日志ID */
    private String id;
    /* 用户ID */
    private String userid;
    /* 动作描述 */
    private String actiondesc;
    /* 操作时间 */
    private String operatetime;
    /* 用户名 */
    private String username;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getActiondesc() {
        return actiondesc;
    }

    public void setActiondesc(String actiondesc) {
        this.actiondesc = actiondesc;
    }

    public String getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(String operatetime) {
        this.operatetime = operatetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
