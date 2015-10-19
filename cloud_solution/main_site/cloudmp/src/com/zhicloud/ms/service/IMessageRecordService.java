package com.zhicloud.ms.service;


import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.MessageRecordVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface IMessageRecordService {

    public List<MessageRecordVO> getAllRecord(int type);

    public MessageRecordVO getRecordById(String id);

    public MethodResult addRecord(Map<String, Object> parameter);

    public MethodResult removeRecordByIds(List<?> ids);
}
