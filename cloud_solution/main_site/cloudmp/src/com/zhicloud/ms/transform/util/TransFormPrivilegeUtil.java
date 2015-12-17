/**
 * Project Name:CloudDeskTopMS
 * File Name:TransFormPrivilegeUtil.java
 * Package Name:com.zhicloud.ms.transform.util
 * Date:2015年6月4日下午2:48:25
 * 
 *
*/ 

package com.zhicloud.ms.transform.util; 

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: TransFormPrivilegeUtil 
 * Function:  用户权限工具类
 * date: 2015年6月4日 下午2:48:25 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class TransFormPrivilegeUtil {
	
	/**
	 * 
	 * isHasPrivilege: 判断是否有权限 
	 *
	 * @author sasa
	 * @param request
	 * @param privilege
	 * @return boolean
	 * @since JDK 1.7
	 */ 
    public boolean isHasPrivilege(HttpServletRequest request, String privilege) {
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        if (login == null)
            return false;
        return login.hasRight(privilege);
    }

}

