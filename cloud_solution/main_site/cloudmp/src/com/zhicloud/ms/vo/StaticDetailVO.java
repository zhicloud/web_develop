
package com.zhicloud.ms.vo;

/**
 * @ClassName: StaticDetailVO
 * @Description: 服务明细情况和操作明细VO
 * @author 张本缘 于 2015年10月20日 下午5:16:21
 */
public class StaticDetailVO {
    private Integer type;// 1、服务明细 2、操作明细
    private Integer cpu_count = 0;
    private String total_volume;// 磁盘总空间(字节数)
    private String used_volume;// 磁盘已用空间
    private String[] timestamp;// 时间戳
    private String[] service_status;// 服务状态
    private String[] total_cpu_usage;// 不同时刻CPU使用率
    private String[] disk_usage;// 不同时刻磁盘使用率
    private String[] memory_usage;// 不同时刻内存使用率
    private String[] cpu_seconds;// 不同时刻cpu使用时长
    private String[] read_request;// 不同时刻读请求数
    private String[] read_bytes;// 不同时刻读的字节数
    private String[] write_request;// 不同时刻的写请求次数
    private String[] write_bytes;// 不同时刻写入字节数
    private String[] io_error;// 不同时刻IO出错次数
    private String[] read_speed;// 不同时刻读的速度
    private String[] write_speed;// 不同时刻写的速度
    private String[] received_bytes;// 不同时刻接收的字节数
    private String[] received_packets;// 不同时刻接收的数据包流量
    private String[] received_errors;// 不同时刻接收出错次数
    private String[] received_drop;// 不同时刻接收时的丢包数
    private String[] sent_bytes;// 不同时刻发送的字节数
    private String[] sent_packets;// 不同时刻的发送的数据包流量
    private String[] sent_errors;// 不同时刻发送出错次数
    private String[] sent_drop;// 不同时刻发送时丢包数
    private String[] received_speed;// 不同时刻的接收速度
    private String[] sent_speed;// 不同时刻的发送速度

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCpu_count() {
        return cpu_count;
    }

    public void setCpu_count(Integer cpu_count) {
        this.cpu_count = cpu_count;
    }

    public String getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(String total_volume) {
        this.total_volume = total_volume;
    }

    public String getUsed_volume() {
        return used_volume;
    }

    public void setUsed_volume(String used_volume) {
        this.used_volume = used_volume;
    }

    public String[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String[] timestamp) {
        this.timestamp = timestamp;
    }

    public String[] getService_status() {
        return service_status;
    }

    public void setService_status(String[] service_status) {
        this.service_status = service_status;
    }

    public String[] getTotal_cpu_usage() {
        return total_cpu_usage;
    }

    public void setTotal_cpu_usage(String[] total_cpu_usage) {
        this.total_cpu_usage = total_cpu_usage;
    }

    public String[] getDisk_usage() {
        return disk_usage;
    }

    public void setDisk_usage(String[] disk_usage) {
        this.disk_usage = disk_usage;
    }

    public String[] getCpu_seconds() {
        return cpu_seconds;
    }

    public void setCpu_seconds(String[] cpu_seconds) {
        this.cpu_seconds = cpu_seconds;
    }

    public String[] getRead_request() {
        return read_request;
    }

    public void setRead_request(String[] read_request) {
        this.read_request = read_request;
    }

    public String[] getRead_bytes() {
        return read_bytes;
    }

    public void setRead_bytes(String[] read_bytes) {
        this.read_bytes = read_bytes;
    }

    public String[] getWrite_request() {
        return write_request;
    }

    public void setWrite_request(String[] write_request) {
        this.write_request = write_request;
    }

    public String[] getWrite_bytes() {
        return write_bytes;
    }

    public void setWrite_bytes(String[] write_bytes) {
        this.write_bytes = write_bytes;
    }

    public String[] getIo_error() {
        return io_error;
    }

    public void setIo_error(String[] io_error) {
        this.io_error = io_error;
    }

    public String[] getRead_speed() {
        return read_speed;
    }

    public void setRead_speed(String[] read_speed) {
        this.read_speed = read_speed;
    }

    public String[] getWrite_speed() {
        return write_speed;
    }

    public void setWrite_speed(String[] write_speed) {
        this.write_speed = write_speed;
    }

    public String[] getReceived_bytes() {
        return received_bytes;
    }

    public void setReceived_bytes(String[] received_bytes) {
        this.received_bytes = received_bytes;
    }

    public String[] getReceived_packets() {
        return received_packets;
    }

    public void setReceived_packets(String[] received_packets) {
        this.received_packets = received_packets;
    }

    public String[] getReceived_errors() {
        return received_errors;
    }

    public void setReceived_errors(String[] received_errors) {
        this.received_errors = received_errors;
    }

    public String[] getReceived_drop() {
        return received_drop;
    }

    public void setReceived_drop(String[] received_drop) {
        this.received_drop = received_drop;
    }

    public String[] getSent_bytes() {
        return sent_bytes;
    }

    public void setSent_bytes(String[] sent_bytes) {
        this.sent_bytes = sent_bytes;
    }

    public String[] getSent_packets() {
        return sent_packets;
    }

    public void setSent_packets(String[] sent_packets) {
        this.sent_packets = sent_packets;
    }

    public String[] getSent_errors() {
        return sent_errors;
    }

    public void setSent_errors(String[] sent_errors) {
        this.sent_errors = sent_errors;
    }

    public String[] getSent_drop() {
        return sent_drop;
    }

    public void setSent_drop(String[] sent_drop) {
        this.sent_drop = sent_drop;
    }

    public String[] getReceived_speed() {
        return received_speed;
    }

    public void setReceived_speed(String[] received_speed) {
        this.received_speed = received_speed;
    }

    public String[] getSent_speed() {
        return sent_speed;
    }

    public void setSent_speed(String[] sent_speed) {
        this.sent_speed = sent_speed;
    }

    public String[] getMemory_usage() {
        return memory_usage;
    }

    public void setMemory_usage(String[] memory_usage) {
        this.memory_usage = memory_usage;
    }

}
