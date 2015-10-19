package com.zhicloud.ms.mapper;



import com.zhicloud.ms.vo.MessageRecordVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface MessageRecordMapper {

    public List<MessageRecordVO> queryAllRecord(int type);

    public MessageRecordVO queryRecordById(String id);

    public int insertRecord(Map<String, Object> data);

    public int deleteRecordByIds(String[] id);

}
