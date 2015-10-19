package com.zhicloud.ms.transform.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.zhicloud.ms.transform.constant.TransformConstant;

/**
 * @ClassName: TransFormSessionValidate
 * @Description: 监听session失效
 * @author 张本缘 于 2015年6月1日 下午3:37:22
 */
public class TransFormSessionValidate implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 设置session有效时间
        se.getSession().setMaxInactiveInterval(60 * 30);

    }

    /**
     * Description:session失效时删除session常量里面的值
     * @param se
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String sessionid = (String) session.getAttribute(TransformConstant.transform_session_admin);
        session.removeAttribute(TransformConstant.transform_session_admin);
        session.removeAttribute("displayname");
        TransFormLoginHelper.removeSessionMap(sessionid);
    }

}
