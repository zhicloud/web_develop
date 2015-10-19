/**
 * Project Name:ms
 * File Name:ITerminalUser.java
 * Package Name:com.zhicloud.ms.service
 * Date:Mar 18, 20158:16:44 AM
 * 
 *
 */

package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.TerminalUserVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName: ITerminalUser 
 * Function: 定义终端用户service层接口. 
 * date: Mar 18, 2015 8:16:44 AM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
public interface ITerminalUserService {

	public List<TerminalUserVO> queryAll();

    public List<TerminalUserVO> queryAll(Map<String, Object> condition);

    public TerminalUserVO queryById(String userId);

	public MethodResult addTerminalUser(Map<String, Object> parameter);

	public MethodResult importTerminalUser(String filePath);

	public MethodResult updateTerminalUserById(Map<String, Object> parameter);

	public MethodResult updateUSBStatusById(Map<String, Object> parameter);

	public MethodResult updateStatusById(Map<String, Object> parameter);

	public MethodResult deleteTerminalUserById(String userId);

}

