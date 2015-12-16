package com.zhicloud.ms.vo;

import com.zhicloud.ms.util.json.JSONBean;

/**
 * Created by sean on 12/10/15.
 */
public class ImageUploadAddressVO implements JSONBean {

    private String serviceName;         // SS服务名，不能为空且唯一，不能修改
    private String localIp;             //内网传输IP，不能为空
    private int localPort;              //内网传输Port，不能为空
    private String publicIp;            //外网传输IP，可以为空
    private int publicPort;             //外网传输Port，可以为空
    private int serviceEnable;          //是否启用该镜像服务，不能为空 1 启用 0禁用
    private String description;         //自定义描述信息，可以为空

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
    }

    public int getServiceEnable() {
        return serviceEnable;
    }

    public void setServiceEnable(int serviceEnable) {
        this.serviceEnable = serviceEnable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageUploadAddressVO clone() {
        ImageUploadAddressVO duplication = new ImageUploadAddressVO();
        duplication.setServiceName(this.getServiceName());
        duplication.setLocalIp(this.getLocalIp());
        duplication.setLocalPort(this.getLocalPort());
        duplication.setPublicIp(this.getPublicIp());
        duplication.setPublicPort(this.getPublicPort());

        return duplication;
    }
}
