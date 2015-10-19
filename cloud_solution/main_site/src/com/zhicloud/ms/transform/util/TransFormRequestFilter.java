package com.zhicloud.ms.transform.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

/**
 * @ClassName: TransFormRequestInteceptor
 * @Description: 登录过滤器
 * @author 张本缘 于 2015年5月29日 下午4:04:09
 */
public class TransFormRequestFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    	//同步 添加产品title
    	synchronized (AppInconstant.productName){
        	request.setAttribute("productName", AppInconstant.productName);
    	}
        // 取得请求路径
        String URI = request.getRequestURI();
        // 特殊路径放开限制
        if (URI.indexOf("/main/checklogin") > -1 || URI.indexOf("/assets/") > -1 || URI.indexOf("/css/") > -1
                || URI.indexOf("/js/") > -1 || URI.indexOf("/images/") > -1 || URI.indexOf("/font-awesome/") > -1
                || URI.indexOf("/transform/login") > -1 || URI.indexOf("hgMessage/push.do") > -1
                || URI.indexOf("interface") > -1 || URI.indexOf("cloudserver/getbackupprogress") > -1
                || URI.indexOf("/transform/updateuserlogin") > -1 || URI.indexOf("/monitor/shieldobject") > -1) {
        } else {
            boolean islogin = TransFormLoginHelper.hasLogin(request);
            if (islogin) {
                if ((request.getContextPath() + "/").equals(URI)) {
                    response.sendRedirect(getFirstRequestURL(request));
                } else {
                    // 如果已经登录 那么更新menuhtml
                    TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
                    request.getSession().setAttribute(TransformConstant.transform_session_menu,
                            TransFormLoginHelper.createMenuHtml(request, login));
                }
            } else {
                request.getRequestDispatcher("/views/transform/admin/login.jsp").forward(request, response);
            }
        }
        chain.doFilter(request, response);
    }
    
    /**
     * @Description:取得当前用户所拥有角色的第一个子菜单
     * @param request
     * @return
     * @throws
     */
    public String getFirstRequestURL(HttpServletRequest request) {
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        List<ManSystemMenuVO> menus = login.getMenuSet();
        if (menus == null || menus.size() == 0) {
            return TransformConstant.transform_jsp_error;
        }
        ManSystemMenuVO parent = menus.get(0);
        ManSystemMenuVO firstChild = parent.getChildren().iterator().next();
        return request.getContextPath() + firstChild.getLinkname();
    }
}
