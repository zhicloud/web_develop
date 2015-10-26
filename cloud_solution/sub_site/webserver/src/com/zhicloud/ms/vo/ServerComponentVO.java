package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ServerComponentVO
 * @Description: 服务组件实体对象
 * @author 梁绍辉 于 2015年9月16日 下午2:48:28
 */
public class ServerComponentVO implements JSONBean {
    private Integer id;
    private String name;
    private String realname;
    private String path;
    private String processname;
    private String keyword;
    private String status;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRealname() {
        return realname;
    }
    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getProcessname() {
        return processname;
    }
    public void setProcessname(String processname) {
        this.processname = processname;
    }
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
