

package com.zhicloud.ms.transform.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemUserVO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SysUserService
 * @Description: 用户管理
 * @author 张本缘 于 2015年4月23日 下午3:45:56
 */
public interface ManSysUserService {

    /**
     * @Description:登录
     * @param parameter
     * @return Map
     */
    public Map<String, Object> login(Map<String, String> parameter);

    /**
     * @Description:注销
     * @param sessionID
     * @return Map
     */
    public Map<String, Object> logout(String sessionID);

    /**
     * @Description:得到所有用户
     * @return List<ManSystemUserVO>
     */
    public List<ManSystemUserVO> getAll();

    /**
     * @Description:根据多个条件得到所有用户
     * @author  张翔
     * @return
     */
    public List<ManSystemUserVO> getAll(Map<String, Object> condition);

    /**
     * @Description:新增用户
     * @param parameter
     * @param login
     * @return String
     */
    public String addSysUser(Map<String, Object> parameter, TransFormLoginInfo login);

    /**
     * @Description:删除用户
     * @param billids
     * @param login
     * @return String
     */
    public String deleteSysUser(String billids, TransFormLoginInfo login);

    /**
     * @Description:重置密码
     * @param billid
     * @param login
     * @return String
     */
    public String resetPassword(String billid, TransFormLoginInfo login);

    /**
     * @Description:修改用户信息
     * @param parameter
     * @param login
     * @return String
     */
    public String modSysUser(Map<String, Object> parameter, TransFormLoginInfo login);

    /**
     * @Description:根据角色ID取得角色拥有的用户数据
     * @param billid
     * @return List<ManSystemUserVO>
     */
    public List<ManSystemUserVO> getUserInRoleID(String billid);

    /**
     * @Description:根据角色ID取得角色没有拥有的用户数据
     * @param billid
     * @return List<ManSystemUserVO>
     */
    public List<ManSystemUserVO> getUserOutRoleID(String billid);

    /**
     * @Description:根据角色组ID取得该角色组的用户
     * @param billid
     * @return List<ManSystemUserVO>
     */
    public List<ManSystemUserVO> getUserInGroupID(String billid);

    /**
     * @Description:根据角色组ID取得不在该角色组中的用户数据
     * @param billid
     * @return List<ManSystemUserVO>
     */
    public List<ManSystemUserVO> getUserOutGroupID(String billid);

    /**
     * @Description:通过ID取得用户信息
     * @param billid
     * @return ManSystemUserVO
     */
    public ManSystemUserVO getUserInfoByID(String billid);

    /**
     * @Description:修改用户密码
     * @param billid
     * @param newpassword
     * @return String
     */
    public String updatePassword(String billid, String newpassword, String oldpassword);
    
    /**
     * @Description:修改用户状态
     * @param billids ID数组
     * @param status 状态值
     * @param login
     * @return String
     */ 
    public String updateUserStatus(String billids, String status, TransFormLoginInfo login);
    
    /**
     * @Description:修改用户USB权限
     * @param billids ID数组
     * @param status 状态值
     * @param login
     * @return String
     */
    public String updateUserUSBStatus(String billids, String status, TransFormLoginInfo login);
    
    /**
     * @Description:获取未有设置租户的人员。
     * @return List<ManSystemUserVO>
     */
	public List<ManSystemUserVO> queryUserOutTenant(String id);
    /**
     * @Description:获取已经设置过租户的人员。
     * @param id 租户ID
     * @return List<ManSystemUserVO>
     */
	public List<ManSystemUserVO> queryUserTenant(String id);
	
	/**
	 * 
	 * modifyTenant: 设置租户下的用户
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult setTenantUser(String id, String userIds);
	
    /**
     * @Description:直接修改密码,不进行对比
     * @param billid
     * @param newpassword
     * @return String
     */
    public String manualPassword(String billid, String newpassword, String email);
}
