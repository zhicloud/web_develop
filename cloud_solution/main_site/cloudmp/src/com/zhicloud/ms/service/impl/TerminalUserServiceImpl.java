/**
 * Project Name:ms
 * File Name:TerminalUserServiceImpl.java
 * Package Name:com.zhicloud.ms.service.impl
 * Date:Mar 18, 20158:20:17 AM
 * 
 *
 */

package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.common.util.ExcelReader;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.mapper.*;
import com.zhicloud.ms.message.MessageServiceManager;
import com.zhicloud.ms.message.email.EmailSendService;
import com.zhicloud.ms.message.email.EmailTemplateConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ITerminalUserService;
import com.zhicloud.ms.service.IUserService;
import com.zhicloud.ms.transform.mapper.ManSystemUserMapper;
import com.zhicloud.ms.transform.vo.ManSystemUserVO;
import com.zhicloud.ms.util.MD5;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.TerminalUserVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: TerminalUserServiceImpl Function: 终端用户的service实现类. date: Mar 18,
 * 2015 8:20:17 AM
 *
 * @author sean
 * @version
 * @since JDK 1.7
 */
@Service("terminalUserService")
@Transactional(readOnly=true)
public class TerminalUserServiceImpl implements ITerminalUserService {

	private static final Logger logger = Logger.getLogger(TerminalUserServiceImpl.class);

    @Resource
    private SqlSession sqlSession;

    @Resource
    private IUserService userService;

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#queryAll()
	 */
	@Override
	public List<TerminalUserVO> queryAll() {
		TerminalUserMapper terminalUserMapper = this.sqlSession
				.getMapper(TerminalUserMapper.class);
		return terminalUserMapper.getAll();
	}

    @Override public List<TerminalUserVO> queryAll(Map<String, Object> condition) {
        return this.sqlSession.getMapper(TerminalUserMapper.class).queryAllWithCondition(condition);
    }

    /**
	 * @see com.zhicloud.ms.service.ITerminalUserService#queryById(java.lang.String)
	 */
	@Override
	public TerminalUserVO queryById(String userId) {
		TerminalUserMapper terminalUserMapper = this.sqlSession
				.getMapper(TerminalUserMapper.class);
		return terminalUserMapper.getById(userId);
	}

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#addTerminalUser(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult addTerminalUser(Map<String, Object> parameter) {

		String userName = StringUtil.trim(parameter.get("username"));
//		String alias = StringUtil.trim(parameter.get("alias"));
//		String password = StringUtil.trim(parameter.get("password"));
    String password = AppConstant.DEFAULT_USER_PASSWORD; //中税默认密码

    String passwordBase = MD5.md5(userName, password);
		
		String groupId = StringUtil.trim(parameter.get("group_id"));
		
		//检查用户的登录名是否被管理员使用过
		ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
        // 判断账户是否已经存在
        LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("usercount", userName);
        condition.put("email", userName); 
        // 验证该账号和邮箱是否已经存在
        ManSystemUserVO systemUserVO = systemUserMapper.validateUserIsExists(condition);
        if(systemUserVO != null){
        	return new MethodResult(MethodResult.FAIL, "用户名已经存在");
        }

		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String id = StringUtil.generateUUID();
		data.put("id", id);
		data.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		data.put("username", userName);
//		data.put("alias", alias);
		data.put("password", passwordBase);
		data.put("group_id", groupId);
		data.put("create_time",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		data.put("modified_time",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		Integer sysUserResult = sysUserMapper.addSysUser(data);

		String name = StringUtil.trim(parameter.get("name"));
		String email = StringUtil.trim(parameter.get("email"));
		String phone = StringUtil.trim(parameter.get("phone"));
		String usbStatus = StringUtil.trim(parameter.get("usb_status"));
		String status = StringUtil.trim(parameter.get("status"));
      String region = StringUtil.trim(parameter.get("region"));
      String industry = StringUtil.trim(parameter.get("industry"));

		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		Map<String, Object> userData = new LinkedHashMap<String, Object>();
      userData.put("id", id);
      userData.put("name", name);
      userData.put("email", email);
      userData.put("phone", phone);
      userData.put("usb_status", usbStatus);
      userData.put("status", status);
      userData.put("cloud_host_amount", 0);
      userData.put("region", region);
      userData.put("industry", industry);
      userData.put("create_time",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
      userData.put("modified_time",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		Integer terminalUserResult = terminalUserMapper.addTerminalUser(userData);

		if (sysUserResult > 0 && terminalUserResult > 0) {

        try {
            // 发送注册通知邮件
            if(!StringUtil.isBlank(email)) {
                Map<String, Object> param = new LinkedHashMap<>();
                param.put("name", userName);
                param.put("password", password);
                EmailSendService emailSendService = MessageServiceManager.singleton().getMailService();
                emailSendService.sendMailWithBcc(EmailTemplateConstant.INFO_REGISTER, email, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new MethodResult(MethodResult.SUCCESS, "添加成功");
		}

		return new MethodResult(MethodResult.FAIL, "添加失败");
	}

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#importTerminalUser(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult importTerminalUser(String filePath) {
		String message = "";
		try {
			ExcelReader excelReader = new ExcelReader();
            String[] title = excelReader.readExcelTitle(filePath);
            
            boolean flag = ("用户名".equals(title[0])) && ("显示名".equals(title[1])) && ("邮箱".equals(title[2])) && ("电话".equals(title[3]));

            if(!flag){
            	message = "导入失败，请检查excel标题格式是否正确";
            	return new MethodResult(MethodResult.FAIL, message);
            }
			Map<Integer, String> map = excelReader.readExcelContent(filePath);
			int successResult = 0;
			int failResult = 0;
        int duplicateResult = 0;
			System.out.println("获得Excel表格的内容:");
			for (int i = 1; i <= map.size(); i++) {
				String row = map.get(i);
				String[] result = row.split(" +");
				for(int j = 0; j < result.length; j+=4){
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            if (!userService.checkAvailable(result[j])){
                duplicateResult++;
                continue;
            }
            data.put("username", result[j]);
            data.put("name", result[j+1]);
//            data.put("password", AppConstant.DEFAULT_PASSWORD);
            data.put("password", AppConstant.DEFAULT_USER_PASSWORD); //中税默认密码
            data.put("group_id", AppConstant.DEFAULT_GROUP_ID);
            data.put("email", result[j+2]);
            data.put("phone", result[j+3]);
            data.put("usb_status", AppConstant.USB_STATUS_DISABLE);
            data.put("status", AppConstant.USER_STATUS_ENABLE);
            try {
						    addTerminalUser(data);
                successResult++;
					} catch (Exception e) {
						failResult++;
						e.printStackTrace();
						continue;
					}
	            }
			}
			message = "导入完成\n导入：" + successResult + "条纪录 失败：" + (failResult + duplicateResult)+"条纪录</br>用户名重名："+duplicateResult+"条纪录</br>默认密码："+AppConstant.DEFAULT_USER_PASSWORD;

        if(failResult > 0){
            message = message+"\n 请检查用户名是否重名";
        }
			return new MethodResult(MethodResult.SUCCESS, message);
		} catch(POIXMLException e) {
			message = "请导入Excel文件";
			return new MethodResult(MethodResult.FAIL, message);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL, message);
		} 
	}

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#updateTerminalUserById(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult updateTerminalUserById(Map<String, Object> parameter) {

		SysUserMapper sysUserMapper = this.sqlSession
				.getMapper(SysUserMapper.class);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		String id = StringUtil.trim(parameter.get("id"));
		String groupId = StringUtil.trim(parameter.get("group_id"));

//		String alias = StringUtil.trim(parameter.get("alias"));
		data.put("id", id);
		data.put("group_id", groupId);
//		data.put("alias", alias);
		Integer sysUserResult = sysUserMapper.updateSysUserById(data);

		TerminalUserMapper terminalUserMapper = this.sqlSession
				.getMapper(TerminalUserMapper.class);
		Map<String, Object> userData = new LinkedHashMap<String, Object>();

		String name = StringUtil.trim(parameter.get("name"));
		String email = StringUtil.trim(parameter.get("email"));
		String phone = StringUtil.trim(parameter.get("phone"));
      String region = StringUtil.trim(parameter.get("region"));
      String industry = StringUtil.trim(parameter.get("industry"));

		userData.put("id", id);
		userData.put("name", name);
		userData.put("email", email);
		userData.put("phone", phone);
      userData.put("region", region);
      userData.put("industry", industry);
		userData.put("modified_time",
				StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		Integer terminalUserResult = terminalUserMapper
				.updateTerminalUserById(userData);

		if (sysUserResult > 0 && terminalUserResult > 0) {
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		return new MethodResult(MethodResult.FAIL, "修改失败");
	}

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#updateUSBStatusById(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult updateUSBStatusById(Map<String, Object> parameter) {
		TerminalUserMapper terminalUserMapper = this.sqlSession
				.getMapper(TerminalUserMapper.class);

		String id = StringUtil.trim(parameter.get("id"));
		Integer usbStatus = (Integer) parameter.get("usb_status");

		Map<String, Object> userData = new LinkedHashMap<String, Object>();

		userData.put("id", id);
		userData.put("usb_status", usbStatus);
		userData.put("modified_time",
				StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

		Integer terminalUserResult = terminalUserMapper
				.updateUSBStatusById(userData);

		if (terminalUserResult > 0) {
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		return new MethodResult(MethodResult.FAIL, "修改失败");
	}

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#updateStatusById(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult updateStatusById(Map<String, Object> parameter) {
		TerminalUserMapper terminalUserMapper = this.sqlSession
				.getMapper(TerminalUserMapper.class);

		String id = StringUtil.trim(parameter.get("id"));
		Integer status = (Integer) parameter.get("status");

		Map<String, Object> userData = new LinkedHashMap<String, Object>();

		userData.put("id", id);
		userData.put("status", status);
		userData.put("modified_time",
				StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

		Integer terminalUserResult = terminalUserMapper
				.updateStatusById(userData);

		if (terminalUserResult > 0) {
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		return new MethodResult(MethodResult.FAIL, "修改失败");
	}

	/**
	 * @see com.zhicloud.ms.service.ITerminalUserService#deleteTerminalUserById(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteTerminalUserById(String userIds) {
	    MethodResult mr = new  MethodResult(MethodResult.SUCCESS, "删除成功");
		String names = "";
//		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		CloudHostWarehouseMapper chwMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);

		TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		String [] ids = userIds.split(",");
		int count = 0;
		for(String userId : ids){
			
			TerminalUserVO user = terminalUserMapper.getById(userId);
			
			if(user == null ){			
				break;
			}
			names = names + user.getUsername();
			//查询出名下所有的主机，逐一放回仓库
			List<CloudHostVO> hosts = cloudHostMapper.getCloudHostByUserId(userId);
			Map<String,Object> data = new LinkedHashMap<String,Object>(); 
			for(CloudHostVO host : hosts){
				//仓库
				data.put("id", host.getWarehouseId());
				chwMapper.updateWarehouseForRetrieveHost(data);
				//主机
				data.put("id", host.getId());
				cloudHostMapper.updateCloudHostUserIdById(data);
			}
			
 
	        Map<String, Object> userData = new LinkedHashMap<String, Object>();

	        userData.put("id", userId);
	        userData.put("status", AppConstant.USER_STATUS_DELETE);
	        userData.put("modified_time",StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

	        //逻辑删除用户
	        Integer terminalUserResult = terminalUserMapper.updateStatusById(userData);
	        //回收用户分配的盒子
	        terminalBoxMapper.releaseTerminalBoxByUserId(userId);
//			Integer terminalUserResult = terminalUserMapper.deleteTerminalUserById(userId);
			
			System.out.println("terminalUserResult" + terminalUserResult);
			
			if (terminalUserResult > 0) {
//				sysUserMapper.deleteSysUserById(userId); 
				count ++;
			}
		}
		mr.setProperty("names", names);
		if(count == ids.length){
		    return mr; 
		}
		if(count >0){		
		    mr.message = "部分删除成功";
			return mr;
		}
		mr.status = MethodResult.FAIL;
		mr.message = "删除失败";
		return mr;
	}


}
