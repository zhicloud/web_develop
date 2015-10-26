package com.zhicloud.ms.filter.login;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.vo.UserVO;

/**
 * @ClassName: LoginFilter
 * @Description: 登录过滤器
 * @author 张本缘 于 2015年9月8日 上午11:13:05
 */
public class LoginFilter implements Filter{

    @Override
    public void destroy() {
        
    }

    @Override
    public void doFilter(ServletRequest seq, ServletResponse ses, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) seq;
        String URI = request.getRequestURI();
        if (URI.indexOf("/css/") > -1 || URI.indexOf("/editor/") > -1 || URI.indexOf("/images/") > -1
                || URI.indexOf("/js/") > -1 || URI.indexOf("/javascript/") > -1
                || URI.indexOf("/login/beforelogin") > -1
                || URI.indexOf("/login/getMachineKey") > -1) {
        } else {
            UserVO user = (UserVO) request.getSession().getAttribute("user");
            if (user == null) {
                request.getRequestDispatcher("/views/common/login.jsp").forward(seq, ses);
            }
        }
        chain.doFilter(seq, ses);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        
    }

}
