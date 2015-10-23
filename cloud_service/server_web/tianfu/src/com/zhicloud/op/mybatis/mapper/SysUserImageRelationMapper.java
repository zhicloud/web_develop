package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

public interface SysUserImageRelationMapper {
	
	public int addImageToUser(Map<String, Object> data);
	
	public int deleteImageByImageId(String sysImageId);

}
