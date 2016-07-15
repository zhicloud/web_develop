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
import com.zhicloud.ms.mapper.WhitelistMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IWhitelistService;
import com.zhicloud.ms.vo.WhitelistVO;

@Transactional(readOnly=true)
@Service("whitelistService")
public class WhitelistServiceImpl implements IWhitelistService {
    
    private static final Logger logger = Logger.getLogger(WhitelistServiceImpl.class); 
    @Resource
	private SqlSession sqlSession;
    
	@Override
	public List<WhitelistVO> getAll() {
		logger.info("WhitelistServiceImpl-->getAll()");
		WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
		List<WhitelistVO> whitelistList = wlMapper.getAll();
		return whitelistList;
	}

	@Override
	public WhitelistVO getByRuleId(String ruleId) {
		logger.info("WhitelistServiceImpl-->getByRuleId()");
		WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
		WhitelistVO whitelist = wlMapper.getByRuleId(ruleId);
		return whitelist;
	}

	@Override
	public WhitelistVO getByRuleName(String ruleName) {
		logger.info("WhitelistServiceImpl-->getByRuleName()");
		WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
		WhitelistVO whitelist = wlMapper.getByRuleName(ruleName);
		return whitelist;
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult addWhitelist(WhitelistVO whitelist,HttpServletRequest request) {
		logger.info("WhitelistServiceImpl-->addWhitelist()");
		try{
			WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
			Map<String,Object> condition = new HashMap<>();
			condition.put("ruleId", StringUtil.generateUUID());
			condition.put("ruleName", whitelist.getRuleName());
			condition.put("ruleIp", whitelist.getRuleIp());
			condition.put("description", whitelist.getDescription());
			int n = wlMapper.add(condition);
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
	public MethodResult updateWhitelist(WhitelistVO whitelist,HttpServletRequest request) {
		logger.info("WhitelistServiceImpl-->updateWhitelist()");
		try{
			WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
			Map<String,Object> condition = new HashMap<>();
			condition.put("ruleId", whitelist.getRuleId());
			condition.put("ruleName", whitelist.getRuleName());
			condition.put("ruleIp", whitelist.getRuleIp());
			condition.put("description", whitelist.getDescription());
			int n = wlMapper.update(condition);
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
	public MethodResult deleteWhitelist(String ruleId) {
		logger.info("WhitelistServiceImpl-->deleteWhitelist()");
		try{
			WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
			int n = wlMapper.delete(ruleId);
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
	public MethodResult deleteWhitelists(List<String> ruleIds) {
		logger.info("WhitelistServiceImpl-->deleteWhitelists()");
		try{
			WhitelistMapper wlMapper = this.sqlSession.getMapper(WhitelistMapper.class);
			int n = wlMapper.deleteByRuleIds(ruleIds);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"删除成功");
			}
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}catch(Exception e){
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}
	}

    
}
