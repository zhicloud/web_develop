package com.zhicloud.ms.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.mapper.BlacklistMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IBlacklistService;
import com.zhicloud.ms.vo.BlacklistVO;

@Transactional(readOnly=true)
@Service("blacklistService")
public class BlacklistServiceImpl implements IBlacklistService {
    
    private static final Logger logger = Logger.getLogger(BlacklistServiceImpl.class); 
    @Resource
	private SqlSession sqlSession;

	@Override
	public List<BlacklistVO> getAll() {
		logger.info("BlacklistServiceImpl-->getAll()");
		BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
		List<BlacklistVO> blacklistList = blMapper.getAll();
		return blacklistList;
	}

	@Override
	public BlacklistVO getByRuleId(String ruleId) {
		logger.info("BlacklistServiceImpl-->getByRuleId()");
		BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
		BlacklistVO blacklist = blMapper.getByRuleId(ruleId);
		return blacklist;
	}

	@Override
	public BlacklistVO getByRuleName(String ruleName) {
		logger.info("BlacklistServiceImpl-->getByRuleName()");
		BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
		BlacklistVO blacklist = blMapper.getByRuleName(ruleName);
		return blacklist;
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult addBlacklist(BlacklistVO blacklist,HttpServletRequest request) {
		logger.info("BlacklistServiceImpl-->addBlacklist()");
		try{
			BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
			Map<String,Object> condition = new HashMap<>();
			condition.put("ruleId", StringUtil.generateUUID());
			condition.put("ruleName", blacklist.getRuleName());
			condition.put("ruleIp", blacklist.getRuleIp());
			condition.put("description", blacklist.getDescription());
			int n = blMapper.add(condition);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"添加成功");
			}
			return new MethodResult(MethodResult.FAIL,"添加失败");
		}catch(Exception e){
			return new MethodResult(MethodResult.FAIL,"添加失败");
		}
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult updateBlacklist(BlacklistVO blacklist,HttpServletRequest request) {
		logger.info("BlacklistServiceImpl-->updateBlacklist()");
		try{
			BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
			Map<String,Object> condition = new HashMap<>();
			condition.put("ruleId", blacklist.getRuleId());
			condition.put("ruleName", blacklist.getRuleName());
			condition.put("ruleIp", blacklist.getRuleIp());
			condition.put("description", blacklist.getDescription());
			int n = blMapper.update(condition);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"更新成功");
			}
			return new MethodResult(MethodResult.FAIL,"更新失败");
		}catch(Exception e){
			return new MethodResult(MethodResult.FAIL,"更新失败");
		}
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteBlacklist(String ruleId) {
		logger.info("BlacklistServiceImpl-->deleteBlacklist()");
		try{
			BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
			int n = blMapper.deleteById(ruleId);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"删除成功");
			}
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}catch(Exception e){
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteBlacklists(List<String> ruleIds) {
		logger.info("BlacklistServiceImpl-->deleteBlacklists()");
		try{
			BlacklistMapper blMapper = this.sqlSession.getMapper(BlacklistMapper.class);
			int n = blMapper.deleteByRuleIds(ruleIds);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"删除成功");
			}
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}catch(Exception e){
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}
	}

    
}
