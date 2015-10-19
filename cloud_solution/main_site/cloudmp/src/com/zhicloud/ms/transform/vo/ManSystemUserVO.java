package com.zhicloud.ms.transform.vo;

import com.zhicloud.ms.common.util.json.JSONBean;
import com.zhicloud.ms.vo.SysTenant;

/**
 * @ClassName: SystemUserVO
 * @Description: 系统用户实体映射
 * @author 张本缘 于 2015年4月23日 下午3:59:20
 */
public class ManSystemUserVO implements JSONBean{

    private String billid;
    private String usercount;
    private String password;
    private String email;
    private String telphone;
    private int status;
    private int usbStatus;
    private String insert_date;
    private String insert_user;
    private String displayname;
    private String tenant_id;
    private int userType;       
    private SysTenant tenant;
    /* 用户类型显示(0,管理员用户 1,租户管理员用户) */
    private String usertype_name;
    /* 状态显示(0,正常 1,禁用) */
    private String status_name;
    
    public SysTenant getTenant() {
		return tenant;
	}

	public void setTenant(SysTenant tenant) {
		this.tenant = tenant;
	}

	public String getTenant_id() {
		return tenant_id;
	}

	public void setTenant_id(String tenant_id) {
		this.tenant_id = tenant_id;
	} 

	public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getUsercount() {
        return usercount;
    }

    public void setUsercount(String usercount) {
        this.usercount = usercount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

	public int getUsbStatus() {
		return usbStatus;
	}

	public void setUsbStatus(int usbStatus) {
		this.usbStatus = usbStatus;
	}

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    /**
     * @Description:翻译用户类型
     * @return String
     */
    public String getUsertype_name() {
        if (userType == 0) {
            usertype_name = "管理员用户";
        } else {
            usertype_name = "租户管理员用户";
        }
        return usertype_name;
    }

    /**
     * @Description:翻译状态类型
     * @return String
     */
    public String getStatus_name() {
        if (status == 0) {
            status_name = "正常";
        } else {
            status_name = "禁用";
        }
        return status_name;
    }
    
}
