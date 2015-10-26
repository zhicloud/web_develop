package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.MyFileVO;

public interface MyFileMapper {

	public int queryPageCount(Map<String, Object> condition);

	public List<MyFileVO> queryPage(Map<String, Object> condition);
	
	public MyFileVO getMyFileByUserIdAndFileName(Map<String, Object> condition);
	
	public MyFileVO getMyFileById(String id);
	
	public int addMyFile(Map<String, Object> data);
	
	public int  deleteMyFIleByIds(String[] id);
	
}
