package com.zhicloud.op.mybatis.mapper;


import com.zhicloud.op.vo.MessageRecordVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface MessageRecordMapper {

    public List<MessageRecordVO> queryAllPage(Map<String, Object> condition);

    public int queryAllPageCount(Map<String, Object> condition);

    public MessageRecordVO queryRecordById(String id);

    public int insertRecord(Map<String, Object> data);

    public int deleteRecord(String[] id);

}
