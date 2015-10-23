package com.zhicloud.op.mybatis.mapper;

import com.zhicloud.op.vo.BackupRecordVO;

import java.util.Map;

/**
 * Created by sean on 7/15/15.
 */
public interface BackupRecordMapper {

    public BackupRecordVO queryBackupRecordById(String id);

    public int updateBackupRecord(Map<String, Object> data);
}
