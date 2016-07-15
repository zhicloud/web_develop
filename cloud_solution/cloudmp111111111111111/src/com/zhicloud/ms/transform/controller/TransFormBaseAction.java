
package com.zhicloud.ms.transform.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

import net.sf.json.JSONArray;

/**
 * @ClassName: TransFormBaseAction
 * @Description: 公用方法
 * @author 张本缘 于 2015年5月13日 上午10:54:14
 */
public class TransFormBaseAction {

    /**
     * @Description:拼装结果集
     * @param obj
     * @return
     */
    public static Map<String, Object> toSuccessReply(Object obj, boolean flag) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (flag) {
            map.put("status", TransformConstant.success);
        } else {
            map.put("status", TransformConstant.fail);
        }
        map.put("result", obj);
        return map;
    }

    /**
     * ajax输出.
     * @param obj
     * @throws IOException
     */
    public void printWriter(HttpServletResponse response, String obj) throws IOException {
        response.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
        response.getWriter().print(obj);
    }

    /**
     * ajax输出.
     * @param obj
     * @throws IOException
     */
    public void printWriterJSONArray(HttpServletResponse response, JSONArray obj) throws IOException {
        response.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
        response.getWriter().print(obj);
    }

    /**
     * @Description:判断是否具有该权限
     * @param request
     * @param privilege
     * @return boolean
     */
    public boolean isHasPrivilege(HttpServletRequest request, String privilege) {
        //String sessionId = request.getParameter(TransformConstant.transform_session_admin);
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        if (login == null)
            return false;
        return login.hasRight(privilege);
    }

    /**
     * @Description:从request中取得登录信息并返回
     * @param request
     * @return
     */
    public TransFormLoginInfo getLoginInfo(HttpServletRequest request) {
        //String sessionId = request.getParameter(TransformConstant.transform_session_admin);
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        return login;
    }
    
    /**
     * @Description:拼接左侧菜单html
     * @param request
     * @param sessionid
     * @return StringBuffer
     */
  public StringBuffer createMenuHtml(HttpServletRequest request, TransFormLoginInfo loginInfo) {
      // TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request, sessionid);
        StringBuffer sb = new StringBuffer();
        if (loginInfo != null) {
            List<ManSystemMenuVO> menuSet = loginInfo.getMenuSet();
            if (menuSet != null && menuSet.size() > 0) {
                String URI = request.getRequestURI();
                // 如果不是首页进来，为了展开左侧树，替换成首页的路径
                URI = URI.substring(0,URI.lastIndexOf("/")) + "/index.do";
                for (int i = 0; i < menuSet.size(); i++) {
                    sb.append("<li class=\"dropdown ");
                    StringBuffer htmlsb = new StringBuffer();
                    htmlsb.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"fa fa-th-large\"></i> ");
                    htmlsb.append(menuSet.get(i).getMenuname());
                    htmlsb.append("<b class=\"fa fa-plus dropdown-plus\"></b></a>");
                    Set<ManSystemMenuVO> children = menuSet.get(i).getChildren();
                    if (children.size() > 0) {
                        htmlsb.append(" <ul class=\"dropdown-menu\">");
                    }
                    Iterator<ManSystemMenuVO> it = children.iterator();
                    int tempcount = 0;
                    boolean flag = false;
                    while (it.hasNext()) {
                        ManSystemMenuVO menu = it.next();
                        // 判断菜单展开节点
                        if (URI.indexOf(menu.getLinkname()) > -1) {
                            flag = true;
                            htmlsb.append("<li class=\"active\">");
                        } else {
                            htmlsb.append("<li>");
                        }
                        htmlsb.append("<a href=\"" + request.getContextPath() + menu.getLinkname());
                        // 是否有其他参数
                        if (menu.getLinkname().indexOf("?") > -1) {
                            htmlsb.append("&");
                        } else {
                            htmlsb.append("?");
                        }
                        htmlsb.append(TransformConstant.transform_session_admin + "=" + loginInfo.getBillid());
                        htmlsb.append("\"");
                        htmlsb.append(">");
                        htmlsb.append("<i class=\"fa fa-caret-right\"></i>");
                        htmlsb.append(menu.getMenuname());
                        htmlsb.append("</a></li>");
                        tempcount++;
                    }
                    if (tempcount > 0) {
                        htmlsb.append("</ul>");
                    }
                    if (flag) {
                        sb.append(" open \">");
                    } else {
                        sb.append(" \">");
                    }
                    htmlsb.append("</li>");
                    sb.append(htmlsb);
                }
            }
        }
        return sb;
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
            return request.getContextPath() + TransformConstant.transform_jsp_error;
        }
        ManSystemMenuVO parent = menus.get(0);
        ManSystemMenuVO firstChild = parent.getChildren().iterator().next();
        return request.getContextPath() + firstChild.getLinkname();
    }
}
