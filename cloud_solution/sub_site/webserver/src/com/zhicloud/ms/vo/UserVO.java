
package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * @ClassName: UserVO
 * @Description: 用户实体对象
 * @author 张本缘 于 2015年9月8日 上午10:16:57
 */
public class UserVO implements JSONBean {

    /* 主键 */
    private String id;
    /* 用户名 */
    private String username;
    /* 密码 */
    private String password;
    /* 创建时间 */
    private String createtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

}
