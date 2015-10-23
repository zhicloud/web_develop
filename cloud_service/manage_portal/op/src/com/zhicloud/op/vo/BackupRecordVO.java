package com.zhicloud.op.vo;

/**
 * Created by sean on 7/15/15.
 */
public class BackupRecordVO {

    private String id;
    private int mode;
    private int disk;
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getDisk() {
        return disk;
    }

    public void setDisk(int disk) {
        this.disk = disk;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
