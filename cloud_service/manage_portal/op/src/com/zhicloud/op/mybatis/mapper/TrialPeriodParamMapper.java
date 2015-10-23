package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

import com.zhicloud.op.vo.TrialPeriodParamVO;

public interface TrialPeriodParamMapper {

	public TrialPeriodParamVO getOne(Integer type);
	
	public int addTrialPeriodParam(Map<String, Object> condition);
	
	public int updateTrialPeriodParam(Map<String, Object> condition);
	
}
