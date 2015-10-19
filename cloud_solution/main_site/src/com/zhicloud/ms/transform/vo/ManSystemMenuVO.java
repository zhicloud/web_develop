
package com.zhicloud.ms.transform.vo;

import java.util.LinkedHashSet;
import java.util.Set;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: ManSystemMenuVO
 * @Description: 功能菜单ibatis实体映射
 * @author 张本缘 于 2015年5月12日 下午3:26:01
 */
public class ManSystemMenuVO implements JSONBean {
    
    private String billid;
    private String menuname;
    private String linkname;
    private String status;
    private String remark;
    private String parentid;
    private String insert_date;
    private String insert_user;
    private Integer sort;
    private Set<ManSystemMenuVO> children = new LinkedHashSet<ManSystemMenuVO>();
    private String cssname;
    
    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getLinkname() {
        return linkname;
    }

    public void setLinkname(String linkname) {
        this.linkname = linkname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getInsert_user() {
        return insert_user;
    }

    public void setInsert_user(String insert_user) {
        this.insert_user = insert_user;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Set<ManSystemMenuVO> getChildren() {
        return children;
    }

    public void setChildren(Set<ManSystemMenuVO> children) {
        this.children = children;
    }

    public String getCssname() {
        return cssname;
    }

    public void setCssname(String cssname) {
        this.cssname = cssname;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((billid == null) ? 0 : billid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManSystemMenuVO other = (ManSystemMenuVO) obj;
        if (billid == null) {
            if (other.billid != null)
                return false;
        } else if (!billid.equals(other.billid))
            return false;
        return true;
    }

}
