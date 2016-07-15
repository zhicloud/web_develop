
package com.zhicloud.ms.vo;

/**
 * @ClassName: StaticSummaryVO
 * @Description: 服务概况和操作概况VO
 * @author 张本缘 于 2015年10月20日 下午5:36:42
 */
public class StaticSummaryVO {
    private Integer type;// 1、服务明细 2、操作明细
    private String[] room;// 机房唯一标示
    private String[] rack;// 机架唯一标示
    private String[] server_name;// 服务器名称
    private String[] cpu_count;// cpu核数
    private String[] total_memory;// 总内存
    private String[] available_memory;// 可用内存
    private String[] total_volume;// 磁盘总空间(字节数)
    private String[] used_volume;// 磁盘已用空间
    private String[] cpu_seconds;// 不同时刻cpu使用时长
    private String[] read_bytes;// 不同时刻读的字节数
    private String[] write_bytes;// 不同时刻写的字节数
    private String[] received_bytes;// 不同时刻接收的字节数
    private String[] sent_bytes;// 不同时刻发送的字节数

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String[] getRoom() {
        return room;
    }

    public void setRoom(String[] room) {
        this.room = room;
    }

    public String[] getRack() {
        return rack;
    }

    public void setRack(String[] rack) {
        this.rack = rack;
    }

    public String[] getServer_name() {
        return server_name;
    }

    public void setServer_name(String[] server_name) {
        this.server_name = server_name;
    }

    public String[] getCpu_count() {
        return cpu_count;
    }

    public void setCpu_count(String[] cpu_count) {
        this.cpu_count = cpu_count;
    }

    public String[] getTotal_memory() {
        return total_memory;
    }

    public void setTotal_memory(String[] total_memory) {
        this.total_memory = total_memory;
    }

    public String[] getAvailable_memory() {
        return available_memory;
    }

    public void setAvailable_memory(String[] available_memory) {
        this.available_memory = available_memory;
    }

    public String[] getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(String[] total_volume) {
        this.total_volume = total_volume;
    }

    public String[] getUsed_volume() {
        return used_volume;
    }

    public void setUsed_volume(String[] used_volume) {
        this.used_volume = used_volume;
    }

    public String[] getCpu_seconds() {
        return cpu_seconds;
    }

    public void setCpu_seconds(String[] cpu_seconds) {
        this.cpu_seconds = cpu_seconds;
    }

    public String[] getRead_bytes() {
        return read_bytes;
    }

    public void setRead_bytes(String[] read_bytes) {
        this.read_bytes = read_bytes;
    }

    public String[] getWrite_bytes() {
        return write_bytes;
    }

    public void setWrite_bytes(String[] write_bytes) {
        this.write_bytes = write_bytes;
    }

    public String[] getReceived_bytes() {
        return received_bytes;
    }

    public void setReceived_bytes(String[] received_bytes) {
        this.received_bytes = received_bytes;
    }

    public String[] getSent_bytes() {
        return sent_bytes;
    }

    public void setSent_bytes(String[] sent_bytes) {
        this.sent_bytes = sent_bytes;
    }

}
