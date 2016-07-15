
package com.zhicloud.ms.transform.util;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

/**
 * @ClassName: TransFormLoginInfo
 * @Description: 登录信息
 * @author 张本缘 于 2015年5月18日 上午9:45:34
 */
public class TransFormLoginInfo {

    private String billid;
    private String usercount;
    private String email;
    private String telphone;
    private int status;
    private int userType;//0 管理员用户 1 租户管理员用户
    //上次更新时间
    private long lastupdatetime = 0;
    // 是否已经登录
    private boolean isLogin = false;
    // 功能权限
    private Set<String> rightSet = new LinkedHashSet<String>();
    // 菜单权限
    private List<ManSystemMenuVO> menuSet = new LinkedList<ManSystemMenuVO>();

    public TransFormLoginInfo() {
    }
    
    public TransFormLoginInfo(boolean isLogin) {
        this.isLogin = isLogin;
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

    public Set<String> getRightSet() {
        return rightSet;
    }

    public void setRightSet(Set<String> rightSet) {
        this.rightSet = rightSet;
    }

    public List<ManSystemMenuVO> getMenuSet() {
        return menuSet;
    }

    public void setMenuSet(List<ManSystemMenuVO> menuSet) {
        this.menuSet = menuSet;
    }

    public boolean isLogin() {
        return isLogin;
    }
    
    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
    
    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }
    

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    /**
     * @Description:判断是否有权限
     * @param privilege
     * @return boolean
     */
    public boolean hasRight(String privilege) {
        if (rightSet.contains(privilege)) {
            return true;
        } else {
            return false;
        }
    }
    
    
    
}
