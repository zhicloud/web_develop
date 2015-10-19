package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.mapper.SysGroupMapper;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.mapper.TerminalBoxMapper;
import com.zhicloud.ms.mapper.TerminalUserMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ITerminalBoxService;
import com.zhicloud.ms.vo.SysGroupVO;
import com.zhicloud.ms.vo.SysUser;
import com.zhicloud.ms.vo.TerminalBoxVO;
import com.zhicloud.ms.vo.TerminalUserVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 6/23/15.
 */

@Service("terminalBoxService")
@Transactional(readOnly=true)
public class TerminalBoxServiceImpl implements ITerminalBoxService{

    @Resource
    private SqlSession sqlSession;


    @Override
    public List<TerminalBoxVO> getAll() {

        return this.sqlSession.getMapper(TerminalBoxMapper.class).queryAll();
    }

    @Override public List<TerminalBoxVO> getAll(Map<String, Object> condition) {
        return this.sqlSession.getMapper(TerminalBoxMapper.class).queryAllWithConditions(condition);
    }

    @Override
    public TerminalBoxVO getById(String id) {

        return this.sqlSession.getMapper(TerminalBoxMapper.class).queryById(id);
    }

    @Override
    public TerminalBoxVO getByName(String name) {

        return this.sqlSession.getMapper(TerminalBoxMapper.class).queryByName(name);
    }

    @Override
    public MethodResult checkSerialNumber(String serialNumber) {
        TerminalBoxVO terminalBoxVO = this.sqlSession.getMapper(TerminalBoxMapper.class).queryBySerialNumber(serialNumber);
        if(terminalBoxVO != null) {
            return new MethodResult(MethodResult.FAIL, "改编号已存在");
        }
        return new MethodResult(MethodResult.SUCCESS, "改编号不存在");
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult addTerminalBox(Map<String, Object> parameter) {
        String serialNumber = StringUtil.trim(parameter.get("serial_number"));
        String name = StringUtil.trim(parameter.get("name"));

        TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", StringUtil.generateUUID());
        data.put("serial_number", serialNumber);
        data.put("name", name);
        data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

        Integer result = terminalBoxMapper.addTerminalBox(data);

        if(result > 0) {
            return new MethodResult(MethodResult.SUCCESS, "创建成功");
        }

        return new MethodResult(MethodResult.FAIL, "创建失败");
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult modifyTerminalBox(Map<String, Object> parameter) {
        String id = StringUtil.trim(parameter.get("id"));
        String serialNumber = StringUtil.trim(parameter.get("serial_number"));
        String name = StringUtil.trim(parameter.get("name"));

        TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);

        Map<String, Object> condition = new LinkedHashMap<String, Object>();

        condition.put("id", id);
        condition.put("serial_number", serialNumber);
        condition.put("name", name);
        condition.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        Integer result = terminalBoxMapper.updateTerminalBoxById(condition);

        if(result > 0) {
            return new MethodResult(MethodResult.SUCCESS, "修改成功");
        }

        return new MethodResult(MethodResult.FAIL, "修改失败");
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult allocateTerminalBox(Map<String, Object> parameter) {
        String id = StringUtil.trim(parameter.get("id"));
        String allocateUserId = StringUtil.trim(parameter.get("allocate_user_id"));

        TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);

        Map<String, Object> condition = new LinkedHashMap<String, Object>();

        condition.put("id", id);
        condition.put("allocate_user_id", allocateUserId);
        condition.put("status", AppConstant.TERMINAL_BOX_ALLOCATE);
        condition.put("allocate_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        condition.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

        Integer result = terminalBoxMapper.allocateTerminalBoxById(condition);

        if(result > 0) {
            return new MethodResult(MethodResult.SUCCESS, "分配成功");
        }

        return new MethodResult(MethodResult.FAIL, "分配失败");
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult releaseTerminalBox(Map<String, Object> parameter) {
        String id = StringUtil.trim(parameter.get("id"));

        TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);

        Map<String, Object> condition = new LinkedHashMap<String, Object>();

        condition.put("id", id);
        condition.put("status", AppConstant.TERMINAL_BOX_RELEASE);
        condition.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

        Integer result = terminalBoxMapper.allocateTerminalBoxById(condition);

        if(result > 0) {
            return new MethodResult(MethodResult.SUCCESS, "回收成功");
        }

        return new MethodResult(MethodResult.FAIL, "回收失败");
    }

    @Override
    @Transactional(readOnly=false)
    public MethodResult deleteTerminalBoxByIds(List<String> ids) {
        TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);

        Integer result = terminalBoxMapper.deleteTerminalBoxByIds(ids.toArray(new String[ids.size()]));

        if(result > 0) {
            return new MethodResult(MethodResult.SUCCESS, "删除成功");
        }

        return new MethodResult(MethodResult.FAIL, "删除失败");
    }
    
    /**
     * 创建盒子分配的树 
    * <p>Title: buildTreeJSON</p> 
    * <p>Description: </p> 
    * @param menuid
    * @param array
    * @param roleid
    * @return 
    * @see com.zhicloud.ms.service.ITerminalBoxService#buildTreeJSON(java.lang.String, net.sf.json.JSONArray, java.lang.String)
     */
    public JSONArray buildTreeJSON(String menuid, JSONArray array, String roleid) {
         
        SysGroupMapper sgMapper = this.sqlSession.getMapper(SysGroupMapper.class);
        SysUserMapper suMapper = this.sqlSession.getMapper(SysUserMapper.class);
        TerminalUserMapper terminalMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
        // 获取所有第一级节点(parentid为0)
        JSONArray returnArray = new JSONArray();
        if(array==null){
            return null;
        }
        // 定义根节点
        if("all".equals(menuid)){
            JSONObject json = new JSONObject();
            json.put("id", "allUsers");
            json.put("text", "全体员工");
            json.put("level","all");
            JSONArray obj = buildTreeJSON(null, array, roleid);
            if(obj==null || obj.size()<1){
                obj = new JSONArray();
            }
            Map<String,Object> uCondition = new LinkedHashMap<String, Object>();
            uCondition.put("type", 2);
            List<SysUser> uList = suMapper.queryAllTerminalOutGroup(uCondition);
            for(SysUser u : uList){
                JSONObject jb = new JSONObject();
                jb.put("id", u.getId());
                jb.put("text", u.getUsername());
                jb.put("level", "user");
                obj.add(jb);
            }
            if (obj!=null && obj.size() > 0) {
                json.put("children", obj);
            }
            returnArray.add(json);
        }
        // 循环所有一级节点加入group
        List<SysGroupVO> sgList = null;
        if(menuid==null || StringUtil.isBlank(menuid)){
            sgList = sgMapper.queryByParentId("0");
        }else if(!"all".equals(menuid) && menuid!=null){
            sgList = sgMapper.queryByParentId(menuid);
        }else{
             return returnArray;
        }
        if(sgList!=null && sgList.size()>0){
            for (SysGroupVO group : sgList) {
                JSONObject obj = new JSONObject();
                obj.put("id", group.getId());
                obj.put("text", group.getGroupName());
                obj.put("level","group");
                JSONArray childArray = buildTreeJSON(group.getId(),array,roleid);
                if(childArray==null || childArray.size()<1){
                    childArray = new JSONArray();
                }
                List<TerminalUserVO> userList = terminalMapper.getUserByGroupIdForBoxAllocate(group.getId());
                if(userList!=null && userList.size()>0){
                    for(TerminalUserVO user : userList){
                        JSONObject subObj = new JSONObject();
                        subObj.put("id", user.getId());
                        subObj.put("text", user.getUsername());
                        subObj.put("level","user");
                        childArray.add(subObj);
                    }
                }
                if(childArray!=null && childArray.size()>0){
                    obj.put("children", childArray);
                }
                returnArray.add(obj);
            }
        }else {
            return null;
        }
        return returnArray;
    }

    /**
     * 根据用户id回收盒子
    * <p>Title: releaseTerminalBoxByUserId</p> 
    * <p>Description: </p> 
    * @param userId
    * @return 
    * @see com.zhicloud.ms.service.ITerminalBoxService#releaseTerminalBoxByUserId(java.lang.String)
     */
    @Override
    public MethodResult releaseTerminalBoxByUserId(String userId) {
        TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);
        int i = terminalBoxMapper.releaseTerminalBoxByUserId(userId);
        if(i > 0){
            return new MethodResult(MethodResult.SUCCESS,"回收成功");
        }else{
            return new MethodResult(MethodResult.FAIL,"回收失败");
        }
    }
    
    @Override
    public List<TerminalBoxVO> getUnboundBox(){
    	TerminalBoxMapper terminalBoxMapper = this.sqlSession.getMapper(TerminalBoxMapper.class);
    	List<TerminalBoxVO> boxList = terminalBoxMapper.getUnboundBox();
    	return boxList;
    }
}
