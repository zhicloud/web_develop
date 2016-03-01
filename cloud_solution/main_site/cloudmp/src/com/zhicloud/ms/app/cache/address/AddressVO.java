package com.zhicloud.ms.app.cache.address;

/**
 * Created by sean on 2/25/16.
 */
public class AddressVO {

    private String fileSystem;  // 设备名
    private String dir;         // 挂载路径
    private String type;        // 文件系统类型
    private String option;      // 设置选项
    private int dump;           // 备份选项 0: 不备份 1:整个设备备份
    private int pass;           // 检查硬盘 0: 不检查 挂载点为/,必须为1, 其他分区不能为1

    public AddressVO() {
    }

    public AddressVO(String fileSystem, String dir, String type, String option,
        int dump, int pass) {

        this.fileSystem = fileSystem;
        this.dir = dir;
        this.type = type;
        this.option = option;
        this.dump = dump;
        this.pass = pass;
    }

    public String getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getDump() {
        return dump;
    }

    public void setDump(int dump) {
        this.dump = dump;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }
}
