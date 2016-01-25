
package com.zhicloud.ms.transform.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set; 

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.zhicloud.ms.common.util.SHA1Util;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

/**
 * @ClassName: TransFormLoginHelper
 * @Description: 登录信息处理类
 * @author 张本缘 于 2015年5月18日 上午11:55:25
 */
public class TransFormLoginHelper {

    // 存放标示ID和用户对象信息
    public static Map<String, TransFormLoginInfo> sessionMap = new HashMap<String, TransFormLoginInfo>();
    // 存放标示ID和session信息
    public static Map<String, String> session = new HashMap<String, String>();

    /**
     * @Description:参数加密
     * @param password 参数
     * @return String
     */
    public static String toEncryptedPassword(String password) {
        try {
            if (StringUtil.isBlank(password)) {
                return "";
            }
            return SHA1Util.digestToHex((password + "xyz").getBytes("utf8"));
        } catch (UnsupportedEncodingException e) {
            throw new AppException("失败");
        }
    }

    /**
     * 使用用户名和密码同时加密
     * @param str1 用户名
     * @param str2 密码
     * @return
     */
    public static String md5(String str1, String str2) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((str1+str2).getBytes());
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;

    }
    /**
     * @Description:将用户信息放入sessionMap中
     * @param sessionid
     * @param loginInfo
     * @throws
     */
    public synchronized static void putSessionMap(String userid, TransFormLoginInfo loginInfo,
            HttpServletRequest request) {
        sessionMap.put(userid, loginInfo);
        request.getSession().setAttribute(TransformConstant.transform_session_admin, userid);
        session.put(userid, request.getSession().getId());
        // 将菜单html放入session
        String menuhtml = createMenuHtml(request, loginInfo).toString();
        request.getSession().setAttribute(TransformConstant.transform_session_menu, menuhtml);
    }

    /**
     * @Description:删除session中的值
     * @param sessionid
     * @throws
     */
    public synchronized static void removeSessionMap(String sessionid) {
        if (sessionid == null || "".equals(sessionid)) {
            return;
        }
        sessionMap.remove(sessionid);
        session.remove(sessionid);
    }

    /**
     * @Description:判断是否已经登录
     * @param sessionid 标示ID
     * @return boolean
     */
    public static boolean hasLogin(HttpServletRequest request) {
        String userid = (String) request.getSession().getAttribute(TransformConstant.transform_session_admin);
        if (userid == null || "".equals(userid)) {
            return false;
        }
        String sessionid = request.getSession().getId();
        //System.out.println("当前会话ID===================="+sessionid+",当前requestID===="+request.getSession().getCreationTime());
        // 代表是同一个会话
        if (sessionMap.containsKey(userid) && sessionid.equals(session.get(userid))) {
            return true;
        }
        return false;
    }

    /**
     * @Description:是否重复登录
     * @param sessionid
     * @param request
     * @return boolean
     */
    public synchronized static boolean isRepeatLogin(String sessionid, HttpServletRequest request) {
        if (sessionid == null || "".equals(sessionid)) {
            return false;
        }
        if (sessionMap.containsKey(sessionid)) {
            if (!session.get(sessionid).equals(request.getSession().getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description:根据标示ID取得用户信息
     * @param sessionid
     * @return
     * @throws
     */
    public static TransFormLoginInfo getLoginInfo(HttpServletRequest request) {
        String sessionid = (String) request.getSession().getAttribute(TransformConstant.transform_session_admin);
        if (sessionid == null || "".equals(sessionid)) {
            return null;
        }
        return sessionMap.get(sessionid);
    }
    
    /**
     * @Description:拼接左侧菜单html
     * @param request
     * @param sessionid
     * @return StringBuffer
     */
  public static  StringBuffer createMenuHtml(HttpServletRequest request, TransFormLoginInfo loginInfo) {
        StringBuffer sb = new StringBuffer();
        if (loginInfo != null) {
            List<ManSystemMenuVO> menuSet = loginInfo.getMenuSet();
            if (menuSet != null && menuSet.size() > 0) {
                String URI = request.getRequestURI();
                String data = request.getParameter("data");
                // 如果不是首页进来，为了展开左侧树，替换成首页的路径
                //URI = URI.substring(0,URI.lastIndexOf("/")) + "/index.do";
                for (int i = 0; i < menuSet.size(); i++) {
                    sb.append("<li class=\"dropdown ");
                    StringBuffer htmlsb = new StringBuffer();
                    htmlsb.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">");
                    htmlsb.append("<i class=\"" + menuSet.get(i).getCssname() + "\"></i> ");
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
/*                        if (URI.indexOf(menu.getLinkname()) > -1) {
                            flag = true;
                            htmlsb.append("<li class=\"active\">");
                        } else {
                            htmlsb.append("<li>");
                        }*/
                        //平台配置URI /transform/..开始
                        if (URI.contains("/transform/")) {
                            if (URI.indexOf(menu.getLinkname().substring(0, menu.getLinkname().lastIndexOf("/") + 1)) > -1) {
                                flag = true;
                                htmlsb.append("<li class=\"active\">");
                            } else {
                                htmlsb.append("<li>");
                            }
                        } else if (URI.contains("/monitor/")) {
                            // 由于监控的多个菜单共用一个controller，并且有共用页面，所以特殊处理下
                            if (data != null && !"".equals(data)) {
                                JSONObject json = JSONObject.fromObject(data);
                                if (!"".equals(json.getString("areaid"))) {
                                    URI = "/monitor/areaquery";
                                } else if (!"".equals(json.getString("roomid"))) {
                                    URI = "/monitor/roomquery";
                                } else if (!"".equals(json.getString("rackid"))) {
                                    URI = "/monitor/rackquery";
                                } else if (!"".equals(json.getString("serverid"))) {
                                    URI = "/monitor/serverquery";
                                } else if ("server".equals(json.getString("type"))) {
                                    URI = "/monitor/serverquery";
                                } else if ("host".equals(json.getString("type"))) {
                                    URI = "/monitor/hostquery";
                                } else if (!"".equals(json.getString("poolid"))) {
                                    URI = "/monitor/resourcequery";
                                }
                            }
                            if (URI.contains(menu.getLinkname())) {
                                flag = true;
                                htmlsb.append("<li class=\"active\">");
                            } else {
                                htmlsb.append("<li>");
                            }
                        } else if (URI.contains("/networkpool/")) { // 网络资源池合并,暂时特殊处理下
                            if (menu.getLinkname().indexOf("/networkpool/") > -1) {
                                flag = true;
                                htmlsb.append("<li class=\"active\">");
                            } else {
                                htmlsb.append("<li>");
                            }
                        } else { // 其他URI取第3次出现/截止的字符串
                            if (getStr(URI, "/", 3)
                                    .indexOf(
                                            menu.getLinkname().substring(0, menu.getLinkname().lastIndexOf("/") + 1)
                                                    .toString()) > -1) {
                                flag = true;
                                htmlsb.append("<li class=\"active\">");
                            } else {
                                htmlsb.append("<li>");
                            }
                        }
                        htmlsb.append("<a href=\"" + request.getContextPath() + menu.getLinkname());
                        // 是否有其他参数
/*                        if (menu.getLinkname().indexOf("?") > -1) {
                            htmlsb.append("&");
                        } else {
                            htmlsb.append("?");
                        }*/
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
  
  
  public static  StringBuffer createMenuHtml(HttpServletRequest request) {
      StringBuffer sb = new StringBuffer();   
      sb.append("<li class=\"dropdown ");
      StringBuffer htmlsb = new StringBuffer();
      htmlsb.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">");
      htmlsb.append("<i class=\"" + "fa fa-th-large" + "\"></i> ");
      htmlsb.append("系统管理");
      htmlsb.append("<b class=\"fa fa-plus dropdown-plus\"></b></a>");  
      

     
      htmlsb.append("<li class=\"active\">");  
      htmlsb.append("<a href=\"" + "/transform/useradmin/init"); 
      htmlsb.append("\"");
      htmlsb.append(">");
      htmlsb.append("<i class=\"fa fa-caret-right\"></i>");
      htmlsb.append("用户管理");
      htmlsb.append("</a></li>");
      
      htmlsb.append("</li>");
      sb.append(htmlsb);
                  
       

      return sb;
  }  
  
  /**
   * @Description:截取字符串到第N次出现str的位置为止
   * @param string 初始字符串
   * @param str 截取的特殊字符
   * @param n 第几次出现
   * @return 截取后的字符串
   */
    public static String getStr(String string, String str, int n) {
        int i = 0;
        int s = 0;
        while (i++ < n - 1) {
            s = string.indexOf(str, s + 1);
            if (s == -1) {
                return string;
            }

        }
        return string.substring(0, s + 1);
    }
    
    /**
     * @Description:截取字符串 从第N次出现字符串的位置开始到字符串结尾
     * @param string
     * @param str
     * @param n
     * @return String
     */
    public static String getStr2(String string, String str, int n) {
        int i = 0;
        int s = 0;
        while (i++ < n - 1) {
            s = string.indexOf(str, s + 1);
            if (s == -1) {
                return string;
            }

        }
        return string.substring(s, string.length());
    }
    
    /**
     * @Description:获取客户端访问IP
     * @param request
     * @return IP
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
}
