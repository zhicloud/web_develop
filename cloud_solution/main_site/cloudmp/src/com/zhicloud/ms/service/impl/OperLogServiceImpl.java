package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.mapper.OperLogMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.OperLogVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("operLogService")
@Transactional(readOnly=true)
public class OperLogServiceImpl implements IOperLogService{
	private static final Logger logger = Logger.getLogger(OperLogServiceImpl.class);
	@Resource
	private SqlSession sqlSession;
	@Override
	public List<OperLogVO> getAll(Map<String,Object> condition) {
		logger.debug("OperLogServiceImpl.getAll()");
		OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
		List<OperLogVO> operList = operLogMapper.getAll(condition);
		return operList;
	}

    /**
     * @function 分页查询所有操作日志
     * @author 张翔
     * @return
     */
    @Override
    public List<OperLogVO> getByPage(Map<String,Object> condition) {
        logger.debug("OperLogServiceImpl.getByPage()");
        return this.sqlSession.getMapper(OperLogMapper.class).queryByPage(condition);
    }

    /**
     * @function 分页查询所有操作日志条数
     * @author 张翔
     * @param condition
     * @return
     */
    @Override public int getPageCount(Map<String, Object> condition) {
        logger.debug("OperLogServiceImpl.getPageCount()");
        return this.sqlSession.getMapper(OperLogMapper.class).queryPageCount(condition);
    }

    @Override
	public List<OperLogVO> getByUserId(String userId) {
		logger.debug("OperLogServiceImpl.getByUserId()");
		OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
		List<OperLogVO> operList = operLogMapper.getByUserId(userId);
		return operList;
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult addLog(OperLogVO operLog,HttpServletRequest request) {
		logger.debug("OperLogServiceImpl.addLog()");
		if(operLog==null){
			return new MethodResult(MethodResult.FAIL,"找不到需要添加的信息");
		}
		if(operLog.getModule()==null){
			operLog.setModule("未知");
		}
		if(operLog.getContent()==null){
			
		}
		if(operLog.getStatus()==null){
			operLog.setStatus(2);
		}
		if(operLog.getLevel()==null){
			operLog.setLevel(1);
		}
		TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
		try {
			OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
			Map<String,Object> condition = new LinkedHashMap<>();
			condition.put("id", StringUtil.generateUUID());
			condition.put("userId", loginInfo.getBillid());
			condition.put("module", operLog.getModule());
			condition.put("userIp", request.getRemoteAddr());
			condition.put("content", operLog.getContent());
			condition.put("operTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			condition.put("status", operLog.getStatus());
			condition.put("level", operLog.getLevel());
			int n = operLogMapper.add(condition);
			if(n>0){
				return new MethodResult(MethodResult.SUCCESS,"添加成功");
			}
			return new MethodResult(MethodResult.FAIL,"添加失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"添加失败");
		}
		
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteById(String id) {
		logger.debug("OperLogServiceImpl.deleteById()");
		OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
		int n = operLogMapper.deleteById(id);
		if(n>0){
			return new MethodResult(MethodResult.SUCCESS,"删除成功");
		}
		return new MethodResult(MethodResult.FAIL,"删除失败");
	}
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteByIds(List<String> ids) {
		logger.debug("OperLogServiceImpl.deleteByIds()");
		if(ids==null || ids.size()<1){
			return new MethodResult(MethodResult.FAIL,"请选择要删除的日志");
		}
		OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
		int n = operLogMapper.deleteIds(ids);
		if(n>0){
			return new MethodResult(MethodResult.SUCCESS,"删除成功");
		}
		return new MethodResult(MethodResult.FAIL,"删除失败");
	}
	@Override
	public void addLog(String module, String content, String level,String status, HttpServletRequest request) {
		logger.debug("OperLogServiceImpl.addLog()");
		Integer l = null;
		Integer s = null;
		if(level!=null){
			l = Integer.parseInt(level);
		}
		if(status!=null){
			s = Integer.parseInt(status);
		}
		OperLogVO operLog = new OperLogVO();
		operLog.setContent(content);
		operLog.setModule(module);
		operLog.setLevel(l);
		operLog.setStatus(s);
		this.addLog(operLog,request);
		
	}
	
}
