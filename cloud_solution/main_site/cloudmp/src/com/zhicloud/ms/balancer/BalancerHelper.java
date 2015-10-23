package com.zhicloud.ms.balancer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.common.util.HttpUtil;
import com.zhicloud.ms.common.util.StreamUtil;
import com.zhicloud.ms.common.util.HttpUtil.InputStreamHandler;
import com.zhicloud.ms.exception.AppException;

/**
 * @author 梁绍辉
 * @function 负载均衡器功能提供类，用于管理负载均衡服务器
 *
 */
public class BalancerHelper {
    private static final Logger logger = Logger.getLogger(BalancerHelper.class);

    private static String request_base = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                    "<SOAP-ENV:Envelope " + 
                                    "   xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"" +
                                    "   xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"" +
                                    "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                                    "   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
                                    "   xmlns:ns=\"zhicloud\">" +
                                    "   <SOAP-ENV:Body>" +
                                    "       <ns:{%node}><context>{%json}</context></ns:{%node}>" +
                                    "   </SOAP-ENV:Body>" +
                                    "</SOAP-ENV:Envelope>";
    
    /*
     * 统计功能(待添加)
     */
    /*
    public static JSONObject statisticsQuery(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port") && param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();

            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.statistics_query);
            json.put("session_id", param.getString("session_id"));
            String xml = request_base.replace("{%node}", "statisticsQuery");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }*/
    
    /*
     * 负载均衡器 - 查询概要
     */
    public static JSONObject queryBalancerSummary(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port") && param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();

            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.summary_query);
            json.put("session_id", param.getString("session_id"));
            String xml = request_base.replace("{%node}", "queryBalancerSummary");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }
    
    /*
     * 负载均衡器 - 启动服务
     */
    public static JSONObject startBalacerService(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port") && param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.service_start);
            json.put("session_id", param.getString("session_id"));    
            String xml = request_base.replace("{%node}", "startBalacerService");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }
    
    /*
     * 负载均衡器 - 重启服务
     */
    public static JSONObject restartBalacerService(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port") && param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.service_restart);
            json.put("session_id", param.getString("session_id"));    
            String xml = request_base.replace("{%node}", "restartBalacerService");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }
    /*
     * 负载均衡器 - 停止服务
     */
    public static JSONObject stopBalacerService(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port") && param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.service_stop);
            json.put("session_id", param.getString("session_id"));    
            String xml = request_base.replace("{%node}", "stopBalacerService");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }
    /*
     * 监听端口 - 监听端口列表
     */
    public static JSONObject queryAllListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port") && param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_query_all);
            json.put("session_id", param.getString("session_id"));    
            String xml = request_base.replace("{%node}", "queryAllListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }
    
    /*
     * 监听端口 - 查看监听端口
     */
    public static JSONObject queryListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")&& param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_query);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "queryListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }
        throw new AppException("request param error.");
    }
    /*
     * 监听端口 - 创建监听端口
     */
    public static JSONObject createListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_create);
            json.put("session_id", param.getString("session_id"));
            json.put("listen_port", param.get("listen_port").toString());
            String xml = request_base.replace("{%node}", "createListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
        
    }
    /*
     * 监听端口 - 修改监听端口
     */
    public static JSONObject modifyListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")&& param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_modify);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            json.put("listen_port", param.get("listen_port").toString());            
            String xml = request_base.replace("{%node}", "modifyListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    /*
     * 监听端口 - 启动监听端口
     */
    public static JSONObject startListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")&& param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_modify);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "startListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    /*
     * 监听端口 - 停止监听端口
     */
    public static JSONObject stopListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")&& param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_stop);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "stopListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        }         
        throw new AppException("request param error.");
    }
    
    /*
     * 监听端口 - 删除监听端口
     */
    public static JSONObject removeListenPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")&& param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.listen_port_remove);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "removeListenPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    /*
     * 转发端口 - 转发端口列表
     */
    public static JSONObject queryAllForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_query_all);
            json.put("session_id", param.getString("session_id"));
            String xml = request_base.replace("{%node}", "queryAllForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        
        throw new AppException("request param error.");
    }
    /*
     * 转发端口 - 转发端口ID列表
     */
    public static JSONObject queryAllForwardID(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_query_all_id);
            json.put("session_id", param.getString("session_id"));
            String xml = request_base.replace("{%node}", "queryAllForwardID");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    /*
     * 转发端口 - 查看转发端口
     */
    public static JSONObject queryForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_query);
            json.put("session_id", param.getString("session_id"));
            String xml = request_base.replace("{%node}", "queryForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    
    /*
     * 转发端口 - 添加转发端口
     */
    public static JSONObject addForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("forward_port")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_add);
            json.put("session_id", param.getString("session_id"));
            json.put("forward_port", param.get("forward_port").toString());
            String xml = request_base.replace("{%node}", "addForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    
    /*
     * 转发端口 - 修改转发端口
     */
    public static JSONObject modifyForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id") && param.containsKey("forward_port")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_modify);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            json.put("forward_port", param.get("forward_port").toString());
            String xml = request_base.replace("{%node}", "modifyForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    
    /*
     * 转发端口 - 停止转发端口
     */
    public static JSONObject stopForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_stop);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "stopForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    /*
     * 转发端口 - 启动转发端口
     */
    public static JSONObject startForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_stop);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "startForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    /*
     * 转发端口 - 释放转发端口
     */
    public static JSONObject removeForwardPort(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.forward_port_remove);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "removeForwardPort");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    
    /*
     * 证书文件 - 上传证书
     */
    /*
    public static JSONObject uploadCertificte(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.certificte_upload);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "uploadCertificte");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    */
    /*
     * 证书文件 - 证书列表
     */
    /*
    public static JSONObject queryCertificte(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.certificte_query);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "queryCertificte");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    */
    /*
     * 证书文件 - 删除证书
     */
    /*
    public static JSONObject removeCertificte(JSONObject param) throws MalformedURLException, IOException{
        String url = "http://";
        if(param.containsKey("ip") && param.containsKey("port")&& param.containsKey("session_id") && param.containsKey("id")){
            url += param.getString("ip");
            url += ":" + param.get("port").toString();
            
            JSONObject json = new JSONObject();
            json.put("command", BalancerConstant.certificte_remove);
            json.put("session_id", param.getString("session_id"));
            json.put("id", param.getString("id"));
            String xml = request_base.replace("{%node}", "removeCertificte");
            xml = xml.replace("{%json}", json.toString());

            return HttpPost(url, xml);
        } 
        throw new AppException("request param error.");
    }
    */
    
    /*
     * 提交POST的主函数
     */
    private static JSONObject HttpPost(String url, String postDataXml ) throws MalformedURLException, IOException{        
        byte[] encryptParam = null;
        if(postDataXml != null){
            encryptParam = postDataXml.getBytes();
        }
        
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "text/xml");

        return  HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    }    
    
    public static class DefaultResponseHanlder implements InputStreamHandler<JSONObject>
    {
        public JSONObject handle(InputStream in, HttpURLConnection conn) throws IOException
        {
            // 处理结果
            String contentType = conn.getHeaderField("Content-Type");
            logger.debug("contentType:" + contentType);
            return decryptReceivedData(in, contentType);
        }
    }
    public static JSONObject decryptReceivedData(InputStream in, String contentType) throws IOException
    {
        // 处理结果
        if( contentType.contains("text/xml;") )
        {
            byte[] bytes = StreamUtil.inputStreamToByteArray(in);
            //bytes = decrypt(bytes);
            logger.debug(">>>> decrypt return: ["+new String(bytes)+"]");
            String xml = new String(bytes);
            if(xml.indexOf("{") > 0 && xml.indexOf("}") > xml.indexOf("{")){
                String json = xml.substring(xml.indexOf("{"));
                json = json.substring(0, json.lastIndexOf("}") + 1);
                return (JSONObject)JSONObject.fromObject(json);
            }else{
                throw new AppException("data format error.");
            }
        }
        else
        {
            throw new AppException("unsupported Content-Type["+contentType+"]");
        }
    }
       
    /*
    public static byte[] encrypt(Map<String, String> param)
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            Iterator<Entry<String, String>> iter = param.entrySet().iterator();
            while( iter.hasNext() )
            {
                Entry<String, String> entry = iter.next();
                sb.append(entry.getKey()+"="+URLEncoder.encode(entry.getValue(), "utf-8"));
                if( iter.hasNext() )
                {
                    sb.append("&");
                }
            }

            return AESUtil.encrypt(encryptionKey, sb.toString());
        }
        catch( UnsupportedEncodingException e )
        {
            throw new AppException(encryption);
        }
    }
    
    public static byte[] decrypt(byte[] bytes)
    {
        return AESUtil.decrypt(encryptionKey, bytes);
    }
    */
    
    public static void main(String [] args) throws MalformedURLException, IOException{
        JSONObject json = new JSONObject();
        json.put("ip", "172.18.20.195");
        json.put("port", "50000");
        json.put("session_id", "123");
        logger.info("request json:" + json.toString());
        
        JSONObject json0 = BalancerHelper.queryBalancerSummary(json);
        logger.info(json0.toString());
    }
}
