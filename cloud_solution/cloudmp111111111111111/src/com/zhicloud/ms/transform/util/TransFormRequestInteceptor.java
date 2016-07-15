package com.zhicloud.ms.transform.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhicloud.ms.transform.constant.TransformConstant;

/**
 * @ClassName: TransFormRequestInteceptor
 * @Description: 登录拦截器
 * @author 张本缘 于 2015年5月29日 下午4:04:09
 */
public class TransFormRequestInteceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 取得session标示
        String URI = request.getRequestURI();
        // 判断session超时
        HttpSession session = request.getSession();
        // 页面到后台判断session是否失效的时候放开限制
        if (URI.indexOf("/main/checklogin") > -1) {
            return true;
        }
        if (session == null) {
            request.getRequestDispatcher("/views/transform/admin/login.jsp").forward(request, response);
            return false;
        }
        // 过滤静态资源文件
        if (URI.indexOf("/assets/") > -1 || URI.indexOf("/css/") > -1 || URI.indexOf("/js/") > -1
                || URI.indexOf("/images/") > -1 || URI.indexOf("/font-awesome/") > -1
                || URI.indexOf("/transform/login") > -1) {
            return true;
        }
        boolean islogin = TransFormLoginHelper.hasLogin(request);
        if (islogin) {
            // 如果已经登录 那么更新menuhtml
            TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
            request.getSession().setAttribute(TransformConstant.transform_session_menu,
                    TransFormLoginHelper.createMenuHtml(request, login));
            return true;
        } else {
            request.getRequestDispatcher("/views/transform/admin/login.jsp").forward(request, response);
            return false;
        }

    }

}
