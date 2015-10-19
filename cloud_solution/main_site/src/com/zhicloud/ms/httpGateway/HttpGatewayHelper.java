package com.zhicloud.ms.httpGateway;

import com.zhicloud.ms.common.util.AESUtil;
import com.zhicloud.ms.common.util.HttpUtil;
import com.zhicloud.ms.common.util.HttpUtil.InputStreamHandler;
import com.zhicloud.ms.common.util.StreamUtil;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.exception.ErrorCode;
import com.zhicloud.ms.httpGateway.verifyMethod.VerifyMethod;
import com.zhicloud.ms.httpGateway.verifyMethod.VerifyMethodFactory;
import com.zhicloud.ms.util.RegionHelper;
 
  

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpGatewayHelper
{
    
    private static final Logger logger = Logger.getLogger(HttpGatewayHelper.class);
    
    
    //---------------------
    
    
    private static HttpGatewayHelper manager = new HttpGatewayHelper(1);
    
    
    private static HttpGatewayHelper getInstance()
    {
        return manager;
    }
    
    
    //-----------------------------
    
    
    private String baseUrl = null;
    
    private long lastRequestTimestamp = 0;
    
    private VerifyMethod verifyMethod;
    private String challengeKey;
    private String sessionId;
    
    private String encryption;
    private String encryptionKeyBase64;
    private byte[] encryptionKey;
    
    //--------------
    
    private HttpGatewayHelper thisHttpGatewayHelper = null;
    
    public HttpGatewayHelper(int region)
    {
        baseUrl = "http://"+RegionHelper.singleton.getRegionData(region).getHttpGatewayAddr()+"/gateway/";
        thisHttpGatewayHelper = this;
    }
    
    public long getLastRequestTimestamp()
    {
        return this.lastRequestTimestamp;
    }
    
    //---------------
    
    public String getSessionId()
    {
        return sessionId;
    }

    //---------------
    
    public void connectEstablish(String verifyMethod, String version) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "connection?";
        url += "command=establish&";
        url += "verify_method="+verifyMethod+"&";
        url += "version="+version;
        
        if( logger.isDebugEnabled() )
        {
            logger.info("connectEstablish() -> url=["+url+"]");
        }
        
        // 发送http消息，并取得返回的数据
        JSONObject json = HttpUtil.post(url, (byte[])null, new DefaultResponseHanlder());
        
        // 处理返回的数据
        String status = (String)json.get("status");
        if( "success".equals(status) )
        {
            this.verifyMethod = VerifyMethodFactory.create((String)json.get("verify_method"));
            this.challengeKey = (String)json.get("challenge_key");
            this.sessionId    = (String)json.get("session_id");
        }
        else
        {
            String message = (String)json.get("message");
            throw new AppException("establish connect failed! status=["+status+"], message=["+message+"]", ErrorCode.ERROR_CODE_HTTP_GATEWAY_ESTABLISH_CONNECT_FAILED);
        }
        
    }
    
    //---------------
    
    public void connectVerify() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "connection?";
        url += "command=verify&";
        url += "session_id="+this.sessionId+"&";
        url += "digest="+this.verifyMethod.digest(this.challengeKey);
        
        if( logger.isDebugEnabled() )
        {
            logger.info("connectVerify() -> url=["+url+"]");
        }
        
        // 发送http消息，并取得返回的数据
        JSONObject json = HttpUtil.post(url, (byte[])null, new DefaultResponseHanlder());
        
        // 处理返回的数据
        String status = (String)json.get("status");
        if( "success".equals(status) )
        {
            this.encryption          = (String)json.get("encryption");
            this.encryptionKeyBase64 = (String)json.get("key");
            this.encryptionKey       = StringUtil.base64ToBytes(this.encryptionKeyBase64);
        }
        else
        {
            String message = (String)json.get("message");
            throw new AppException("establish connect failed! status=["+status+"], message=["+message+"]", ErrorCode.ERROR_CODE_HTTP_GATEWAY_ESTABLISH_CONNECT_FAILED);
        }
        
    }
    
    
    //----------------
    
    
    public JSONObject keepConnectionAlive(double sleepSeconds) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "connection?";
        url += "command=keep_alive&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("keepConnection() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("sleep", StringUtil.trim(sleepSeconds));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    //----------------
    
    
    public JSONObject messagePushRegister(String pushUrl) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "messagePush?";
        url += "command=register&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("messagePushRegister() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("url", StringUtil.trim(pushUrl));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    //---------------
    
    public JSONObject login(String user, String authentication) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "login?";
        url += "command=login&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("login() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("user", StringUtil.trim(user));
        postDataMap.put("authentication", StringUtil.trim(authentication));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //---------------
    
    public JSONObject applicationQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "application?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("applicationQuery() -> url=["+url+"]");
        }
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, null, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //----------------
    
    public JSONObject hostQuery(String pool) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", StringUtil.trim(pool));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    public JSONObject hostQueryInfo(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=query_info&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQueryInfo() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    
    public JSONObject hostCreationResult(String host_name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=query_creation_result&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostCreationResult() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("host_name", StringUtil.trim(host_name));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject hostCreate(
            String name, String pool, Integer cpu_count, BigInteger memory, Integer[] option, String image, 
            BigInteger[] disk_volume, Integer[] port, String user, String group, String display, String authentication, 
            String network, BigInteger inbound_bandwidth, BigInteger outbound_bandwidth
            ) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostCreate() -> url=["+url+"]");
        }
        port = new Integer[]{1,8080};
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", StringUtil.trim(name));
        postDataMap.put("pool", StringUtil.trim(pool));
        postDataMap.put("cpu_count", StringUtil.trim(cpu_count));
        postDataMap.put("memory", StringUtil.trim(memory));
        postDataMap.put("option", StringUtil.joinWrap(option));
        postDataMap.put("image", StringUtil.trim(image));
        postDataMap.put("disk_volume", StringUtil.joinWrap(disk_volume));
        postDataMap.put("port", StringUtil.joinWrap(port));
        postDataMap.put("user", StringUtil.trim(user));
        postDataMap.put("group", StringUtil.trim(group));
        postDataMap.put("display", StringUtil.trim(display));
        postDataMap.put("authentication", StringUtil.trim(authentication));
        postDataMap.put("network", StringUtil.trim(network));
        postDataMap.put("inbound_bandwidth", StringUtil.trim(inbound_bandwidth));
        postDataMap.put("outbound_bandwidth", StringUtil.trim(outbound_bandwidth));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject hostCreate(
        String name, String pool, Integer cpu_count, BigInteger memory, Integer[] option, String image,
        BigInteger[] disk_volume, Integer[] port, String user, String group, String display, String authentication,
        String network, BigInteger inbound_bandwidth, BigInteger outbound_bandwidth, String path, String crypt,
        int maxIops, int cpuPriority
    ) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        if( logger.isDebugEnabled() )
        {
            logger.info("hostCreate() -> url=["+url+"]");
        }
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", StringUtil.trim(name));
        postDataMap.put("pool", StringUtil.trim(pool));
        postDataMap.put("cpu_count", StringUtil.trim(cpu_count));
        postDataMap.put("memory", StringUtil.trim(memory));
        postDataMap.put("option", StringUtil.joinWrap(option));
        postDataMap.put("image", StringUtil.trim(image));
        postDataMap.put("disk_volume", StringUtil.joinWrap(disk_volume));
        postDataMap.put("port", StringUtil.joinWrap(port));
        postDataMap.put("user", StringUtil.trim(user));
        postDataMap.put("group", StringUtil.trim(group));
        postDataMap.put("display", StringUtil.trim(display));
        postDataMap.put("authentication", StringUtil.trim(authentication));
        postDataMap.put("network", StringUtil.trim(network));
        postDataMap.put("inbound_bandwidth", StringUtil.trim(inbound_bandwidth));
        postDataMap.put("outbound_bandwidth", StringUtil.trim(outbound_bandwidth));
        postDataMap.put("path", StringUtil.trim(path));
        postDataMap.put("crypt", StringUtil.trim(crypt));
        postDataMap.put("io", String.valueOf(maxIops));
        postDataMap.put("priority", String.valueOf(cpuPriority));
        byte[] encryptParam = encrypt(postDataMap);
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject hostDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    /**
     * @param uuid: string 云主机唯一标识
     * @param name：string 云主机名
     * @param cpu_count: uint cpu核数
     * @param memory: uint 内存，单位：字节
     * @param option: uint array  0/1,[是否自动启动]
     * @param port: uint array 需要开放的端口清单，[type1,port1,type2,port2,...]
     *      type=0
     *          所有协议
     *      type=1
     *          TCP
     *      type=2
     *          UDP
     * @param display: string 监控验证用户名
     * @param authentication: string 监控验证密码
     * @param network: string 连接的虚拟网络（预留）
     * @param inbound_bandwidth: uint 入口带宽，单位：字节
     * @param outbound_bandwidth: uint 出口带宽，单位：字节
     */
    public JSONObject hostModify(   String uuid, 
                                    String name,
                                    Integer cpu_count, 
                                    BigInteger memory, 
                                    Integer[] option, 
                                    Integer[] port, 
                                    String display, 
                                    String authentication, 
                                    String network, 
                                    BigInteger inbound_bandwidth, 
                                    BigInteger outbound_bandwidth) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid",               StringUtil.trim(uuid));
        postDataMap.put("name",               StringUtil.trim(name));
        postDataMap.put("cpu_count",          StringUtil.trim(cpu_count));
        postDataMap.put("memory",             StringUtil.trim(memory));
        postDataMap.put("option",             StringUtil.joinWrap(option));
        postDataMap.put("port",               StringUtil.joinWrap(port));
        postDataMap.put("display",            StringUtil.trim(display));
        postDataMap.put("authentication",     StringUtil.trim(authentication));
        postDataMap.put("network",            StringUtil.trim(network));
        postDataMap.put("inbound_bandwidth",  StringUtil.trim(inbound_bandwidth));
        postDataMap.put("outbound_bandwidth", StringUtil.trim(outbound_bandwidth));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    /**
     * @param uuid: string 云主机唯一标识
     * @param name：string 云主机名
     * @param cpu_count: uint cpu核数
     * @param memory: uint 内存，单位：字节
     * @param option: uint array  0/1,[是否自动启动]
     * @param port: uint array 需要开放的端口清单，[type1,port1,type2,port2,...]
     *      type=0
     *          所有协议
     *      type=1
     *          TCP
     *      type=2
     *          UDP
     * @param display: string 监控验证用户名
     * @param authentication: string 监控验证密码
     * @param network: string 连接的虚拟网络（预留）
     * @param inbound_bandwidth: uint 入口带宽，单位：字节
     * @param outbound_bandwidth: uint 出口带宽，单位：字节
     * @param maxIops: 最大iops, 0代表不限制
     * @param cpuPriority: cpu优先级，0=高优先<默认>，1=中优先，2=低优先
     */
    public JSONObject hostModify(   String uuid,
        String name,
        Integer cpu_count,
        BigInteger memory,
        Integer[] option,
        Integer[] port,
        String display,
        String authentication,
        String network,
        BigInteger inbound_bandwidth,
        BigInteger outbound_bandwidth,
        int maxIops,
        int cpuPriority) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid",               StringUtil.trim(uuid));
        postDataMap.put("name",               StringUtil.trim(name));
        postDataMap.put("cpu_count",          StringUtil.trim(cpu_count));
        postDataMap.put("memory",             StringUtil.trim(memory));
        postDataMap.put("option",             StringUtil.joinWrap(option));
        postDataMap.put("port",               StringUtil.joinWrap(port));
        postDataMap.put("display",            StringUtil.trim(display));
        postDataMap.put("authentication",     StringUtil.trim(authentication));
        postDataMap.put("network",            StringUtil.trim(network));
        postDataMap.put("inbound_bandwidth",  StringUtil.trim(inbound_bandwidth));
        postDataMap.put("outbound_bandwidth", StringUtil.trim(outbound_bandwidth));
        postDataMap.put("io", String.valueOf(maxIops));
        postDataMap.put("priority", String.valueOf(cpuPriority));
        byte[] encryptParam = encrypt(postDataMap);
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject hostStart(String uuid, Integer boot, String image) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=start&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid",  StringUtil.trim(uuid));
        postDataMap.put("boot",  StringUtil.trim(boot));
        postDataMap.put("image", StringUtil.trim(image));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject hostRestart(String uuid, Integer boot, String image) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=restart&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("boot", StringUtil.trim(boot));
        postDataMap.put("image", StringUtil.trim(image));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject hostStop(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=stop&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject hostHalt(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=halt&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostHalt() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject hostReset(String uuid, String callback) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=reset&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostReset() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("callback", StringUtil.trim(callback));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject hostInsertMedia(String uuid, String image) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=insert_media&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostInsertMedia() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("image", StringUtil.trim(image));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject hostChangeMedia(String uuid, String image) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=change_media&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostChangeMedia() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("image", StringUtil.trim(image));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject hostEjectMedia(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=eject_media&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostEjectMedia() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    /**
        uuid
            string
            云主机唯一标识
        disk_volume
            uint
            新增磁盘容量，单位：字节
        disk_type
            uint
            磁盘模式，0=本地，1=云存储
        disk_source
            string
            云存储时，对应的存储资源池uuid
        mode
            uint
            磁盘格式
                0=raw，裸盘
     */
    public JSONObject hostAttachDisk(String uuid, BigInteger disk_volume, Integer disk_type, String disk_source, Integer mode) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=attach_disk&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostAttachDisk() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("disk_volume", StringUtil.trim(disk_volume));
        postDataMap.put("disk_type", StringUtil.trim(disk_type));
        postDataMap.put("disk_source", StringUtil.trim(disk_source));
        postDataMap.put("mode", StringUtil.trim(mode));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    /**
        uuid
            string
            云主机唯一标识
        index
            uint
            数据磁盘索引，0代表第一个数据磁盘
     */
    public JSONObject hostDetachDisk(String uuid, Integer index) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "host?";
        url += "command=detach_disk&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("hostDetachDisk() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid",  StringUtil.trim(uuid));
        postDataMap.put("index", StringUtil.trim(index));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject hostStartMonitor(String[] target, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "host?";
        url += "command=start_monitor&";
        url += "session_id=" + this.sessionId + "&";
        
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", StringUtil.joinWrap(target));
        postDataMap.put("callback", StringUtil.trim(callback));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        
        return json;        
    }
    
    public JSONObject hostStopMonitor(Integer task) throws MalformedURLException, IOException {
        String url = this.baseUrl + "host?";
        url += "command=stop_monitor&";
        url += "session_id=" + this.sessionId + "&";
        
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("task", StringUtil.trim(task));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        
        return json;
    }
    
    //---------------
    

//  public JSONObject diskQuery(String pool) throws MalformedURLException, IOException
//  {
//      String url = this.baseUrl + "disk?";
//      url += "command=query&";
//      url += "session_id="+this.sessionId+"&";
//      
//      if( logger.isDebugEnabled() )
//      {
//          logger.info("diskQuery() -> url=["+url+"]");
//      }
//      
//      // 参数
//      Map<String, String> postDataMap = new LinkedHashMap<String, String>();
//      postDataMap.put("pool", StringUtil.trim(pool));
//      
//      byte[] encryptParam = encrypt(postDataMap);
//      
//      // 发送http消息，并取得返回的数据
//      Map<String, String> requestProperties = new LinkedHashMap<String, String>();
//      requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
//      
//      JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
//      return json;
//  }
//  
//
//  public JSONObject diskCreate(String name, String pool, String disk_volume, String count) throws MalformedURLException, IOException
//  {
//      String url = this.baseUrl + "disk?";
//      url += "command=create&";
//      url += "session_id="+this.sessionId+"&";
//      
//      if( logger.isDebugEnabled() )
//      {
//          logger.info("diskQuery() -> url=["+url+"]");
//      }
//      
//      // 参数
//      Map<String, String> postDataMap = new LinkedHashMap<String, String>();
//      postDataMap.put("name", StringUtil.trim(name));
//      postDataMap.put("pool", StringUtil.trim(pool));
//      postDataMap.put("disk_volume", StringUtil.trim(disk_volume));
//      postDataMap.put("count", StringUtil.trim(count));
//      
//      byte[] encryptParam = encrypt(postDataMap);
//      
//      // 发送http消息，并取得返回的数据
//      Map<String, String> requestProperties = new LinkedHashMap<String, String>();
//      requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
//      
//      JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
//      return json;
//  }
//  
//
//  public JSONObject diskDelete(String uuid) throws MalformedURLException, IOException
//  {
//      String url = this.baseUrl + "disk?";
//      url += "command=delete&";
//      url += "session_id="+this.sessionId+"&";
//      
//      if( logger.isDebugEnabled() )
//      {
//          logger.info("diskQuery() -> url=["+url+"]");
//      }
//      
//      // 参数
//      Map<String, String> postDataMap = new LinkedHashMap<String, String>();
//      postDataMap.put("uuid", StringUtil.trim(uuid));
//      
//      byte[] encryptParam = encrypt(postDataMap);
//      
//      // 发送http消息，并取得返回的数据
//      Map<String, String> requestProperties = new LinkedHashMap<String, String>();
//      requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
//      
//      JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
//      return json;
//  }
    
    //---------------
    

    public JSONObject cloudTerminalQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "cloudTerminal?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("cloudTerminalQuery() -> url=["+url+"]");
        }
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, null, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject cloudTerminalAdd(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "cloudTerminal?";
        url += "command=add&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("cloudTerminalAdd() -> url=["+url+"]");
        }
        

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject cloudTerminalRemove(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "cloudTerminal?";
        url += "command=remove&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("cloudTerminalRemove() -> url=["+url+"]");
        }
        

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject cloudTerminalBind(String uuid, String host) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "cloudTerminal?";
        url += "command=bind&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("cloudTerminalRemove() -> url=["+url+"]");
        }
        

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("host", StringUtil.trim(host));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //---------------
    
    
    public JSONObject diskImageQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "diskImage?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("diskImageQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject diskImageCreate(String name, String uuid, String description, String[] identity, String group, String user, String callback) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "diskImage?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("diskImageCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", StringUtil.trim(name));
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("description",   StringUtil.trim(description));
        postDataMap.put("identity",   StringUtil.joinWrap(identity));
        postDataMap.put("group",   StringUtil.trim(group));
        postDataMap.put("user",   StringUtil.trim(user));
        if (callback != null) {
            postDataMap.put("async", "1");
            postDataMap.put("callback", callback);
        }
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }   

    public JSONObject diskImageDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "diskImage?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("diskImageDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject diskImageModify(String uuid, String name, String description, String[] identity) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "diskImage?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("diskImageModify() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("name", StringUtil.trim(name));
        postDataMap.put("description", StringUtil.trim(description));
        postDataMap.put("identity", StringUtil.joinWrap(identity));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    //-----------
    

    public JSONObject isoImageQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "isoImage?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("isoImageQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject isoImageUpload(String name, String target, String description, String group, String user,int diskType,String uuid, String callback) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "isoImage?";
        url += "command=upload&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("isoImageUpload() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", StringUtil.trim(name));
        postDataMap.put("target", StringUtil.trim(target));
        postDataMap.put("description", StringUtil.trim(description));
        postDataMap.put("group", StringUtil.trim(group));
        postDataMap.put("user", StringUtil.trim(user));
        postDataMap.put("callback", StringUtil.trim(callback));
        postDataMap.put("disk_type", diskType+"");
        postDataMap.put("uuid", uuid);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject isoImageModify(String uuid, String name, String description) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "isoImage?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("isoImageModify() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("name", StringUtil.trim(name));
        postDataMap.put("description", StringUtil.trim(description));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject isoImageDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "isoImage?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("isoImageDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //----------
    

    public JSONObject resourcePoolQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "resourcePool?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("resourcePoolQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //------------
    

	public JSONObject computePoolQuery() throws MalformedURLException, IOException
	{
		String url = this.baseUrl + "computePool?";
		url += "command=query&";
		url += "session_id="+this.sessionId+"&";
		
		if( logger.isDebugEnabled() )
		{
			logger.info("computePoolQuery() -> url=["+url+"]");
		}
		
		// 参数
		Map<String, String> postDataMap = new LinkedHashMap<String, String>();
		byte[] encryptParam = encrypt(postDataMap);
		
		// 发送http消息，并取得返回的数据
		Map<String, String> requestProperties = new LinkedHashMap<String, String>();
		requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
		
		JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
		return json;
	}

    public JSONObject computePoolDetail(String uuid, String callback) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "computePool?";
        url += "command=detail&";
        url += "session_id="+this.sessionId+"&";

        if( logger.isDebugEnabled() )
        {
            logger.info("computePoolDetail() -> url=["+url+"]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", StringUtil.trim(uuid));
        postDataMap.put("callback", StringUtil.trim(callback));
        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
	
	
	public JSONObject computePoolCreate(String name, int network_type, String network, int disk_type, String disk_source,Integer[] mode, String path, String crypt) throws MalformedURLException, IOException
	{
		String url = this.baseUrl + "computePool?";
		url += "command=create&";
		url += "session_id="+this.sessionId+"&";
		
		if( logger.isDebugEnabled() )
		{
			logger.info("computePoolCreate() -> url=["+url+"]");
		}
		
		// 参数
		Map<String, String> postDataMap = new LinkedHashMap<String, String>();
		postDataMap.put("name", name);
		postDataMap.put("network_type", ""+network_type);
		postDataMap.put("network", network);
		postDataMap.put("disk_type", ""+disk_type);
		postDataMap.put("disk_source", disk_source);
        postDataMap.put("mode", StringUtil.joinWrap(mode));		
		postDataMap.put("crypt", crypt);
		postDataMap.put("path", path);		
		byte[] encryptParam = encrypt(postDataMap);
		
		// 发送http消息，并取得返回的数据
		Map<String, String> requestProperties = new LinkedHashMap<String, String>();
		requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
		
		JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
		return json;
	}

    public JSONObject computePoolCreate(String name, Integer networkType, String network, Integer diskType, String diskSource, Integer[] mode, String path, String crypt, String callback) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "computePool?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";

        if( logger.isDebugEnabled() )
        {
            logger.info("computePoolCreate() -> url=["+url+"]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        postDataMap.put("network_type", String.valueOf(networkType));
        postDataMap.put("network", network);
        postDataMap.put("disk_type", String.valueOf(diskType));
        postDataMap.put("disk_source", diskSource);
        postDataMap.put("mode", StringUtil.joinWrap(mode));
        postDataMap.put("path", path);
        postDataMap.put("crypt", crypt);
        postDataMap.put("callback", callback);
        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
	
	
	public JSONObject computePoolDelete(String uuid) throws MalformedURLException, IOException
	{
		String url = this.baseUrl + "computePool?";
		url += "command=delete&";
		url += "session_id="+this.sessionId+"&";
		
		if( logger.isDebugEnabled() )
		{
			logger.info("computePoolDelete() -> url=["+url+"]");
		}
		
		// 参数
		Map<String, String> postDataMap = new LinkedHashMap<String, String>();
		postDataMap.put("uuid", uuid);
		byte[] encryptParam = encrypt(postDataMap);
		
		// 发送http消息，并取得返回的数据
		Map<String, String> requestProperties = new LinkedHashMap<String, String>();
		requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
		
		JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
		return json;
	}
	

    public JSONObject computePoolModify(String uuid, String name, int networkType, String network, int diskType, String diskSource, Integer[] mode, int option, String path, String crypt ,String callback) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "computePool?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";

        if( logger.isDebugEnabled() )
        {
            logger.info("computePoolModify() -> url=["+url+"]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        postDataMap.put("network_type", String.valueOf(networkType));
        postDataMap.put("network", network);
        postDataMap.put("disk_type", String.valueOf(diskType));
        postDataMap.put("disk_source", diskSource);
        postDataMap.put("mode", StringUtil.joinWrap(mode));
        postDataMap.put("option", String.valueOf(option));
        postDataMap.put("path", path);
        postDataMap.put("crypt", crypt);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

  	
    public JSONObject computePoolAddResource(String pool, String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "computePool?";
        url += "command=add_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("computePoolDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject computePoolRemoveResource(String pool, String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "computePool?";
        url += "command=remove_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("computePoolRemoveResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject computePoolQueryResource(String pool) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "computePool?";
        url += "command=query_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("computePoolQueryResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //-----------
    

    public JSONObject storagePoolQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject storagePoolCreate(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject storagePoolModify(String uuid, String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject storagePoolDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject storagePoolAddResource(String pool, String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=add_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("name", name);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject storagePoolRemoveResource(String pool, String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=remove_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolRemoveResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("name", name);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject storagePoolQueryResource(String pool) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "storagePool?";
        url += "command=query_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("storagePoolQueryResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //----------
    
    
    public JSONObject addressPoolQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "addressPool?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("addressPoolQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject addressPoolCreate(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "addressPool?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("addressPoolCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject addressPoolDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "addressPool?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("addressPoolDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject addressPoolAddResource(String pool, String ip, String range) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "addressPool?";
        url += "command=add_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("addressPoolDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("ip", ip);
        postDataMap.put("range", range);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    public JSONObject addressPoolRemoveResource(String pool, String ip) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "addressPool?";
        url += "command=remove_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("addressPoolDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("ip", ip);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject addressPoolQueryResource(String pool) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "addressPool?";
        url += "command=query_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("addressPoolDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //----------
    

    public JSONObject portPoolQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "portPool?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject portPoolCreate(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "portPool?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject portPoolDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "portPool?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject portPoolAddResource(String pool, String ip, String port) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "portPool?";
        url += "command=add_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolAddResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("ip", ip);
        postDataMap.put("port", port);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    public JSONObject portPoolRemoveResource(String pool, String ip) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "portPool?";
        url += "command=remove_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolRemoveResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        postDataMap.put("ip", ip);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        
        return json;
    }
    

    public JSONObject portPoolQueryResource(String pool) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "portPool?";
        url += "command=query_resource&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolQueryResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("pool", pool);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //-----------
    

    public JSONObject structQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "struct?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("portPoolQueryResource() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //---------
    

    public JSONObject serverRoomQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRoom?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRoomQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject serverRoomCreate(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRoom?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRoomCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    public JSONObject serverRoomDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRoom?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRoomDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    public JSONObject serverRoomModify(String uuid, String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRoom?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRoomModify() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //----------------

    public JSONObject serverRackQuery(String room) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRack?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRackQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("room", room);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject serverRackCreate(String name, String room) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRack?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRackQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        postDataMap.put("room", room);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject serverRackDelete(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRack?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRackDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject serverRackModify(String uuid, String name, String room) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serverRack?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRackModify() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        postDataMap.put("room", room);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //-----------
    

    public JSONObject serverQuery(String rack) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "server?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("rack", rack);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject serverAdd(String name, String rack, String ethernet_address) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "server?";
        url += "command=add&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverAdd() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        postDataMap.put("rack", rack);
        postDataMap.put("ethernet_address", ethernet_address);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject serverRemove(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "server?";
        url += "command=remove&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverRemove() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject serverModify(String uuid, String name, String rack) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "server?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverModify() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        postDataMap.put("rack", rack);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject serverReboot(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "server?";
        url += "command=reboot&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverReboot() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    

    public JSONObject serverShutdown(String uuid) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "server?";
        url += "command=shutdown&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serverShutdown() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    //----------
    
    
    public JSONObject serviceTypeQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serviceType?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serviceTypeQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject serviceGroupQuery(int type) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "serviceGroup?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serviceGroupQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("type", String.valueOf(type));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    public JSONObject serviceQuery(int type, String group) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "service?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serviceQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("type", String.valueOf(type));
        postDataMap.put("group", group);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    

    public JSONObject serviceRestart(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "service?";
        url += "command=restart&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serviceRestart() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject serviceEnable(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "service?";
        url += "command=enable&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serviceRestart() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject serviceDisable(String name) throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "service?";
        url += "command=disable&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("serviceRestart() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    } 
    
     
    
    //-----------
    

    
    //----------
    

    public JSONObject statisticsQuery() throws MalformedURLException, IOException
    {
        String url = this.baseUrl + "statistics?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("statisticsQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    
    //----------
    
    public JSONObject deviceQuery(String target, int type) throws MalformedURLException, IOException {
        String url = this.baseUrl + "device?";
        url += "command=query&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("deviceQuery() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", target);
        postDataMap.put("type", StringUtil.trim(type));
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject deviceCreate(String name, String pool, BigInteger diskVolume, int page, int replication, 
            Integer[] option, int diskType, String user, String authentication, String crypt, String snapshot) throws MalformedURLException, IOException {
        
        String url = this.baseUrl + "device?";
        url += "command=create&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("deviceCreate() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        postDataMap.put("pool", pool);
        postDataMap.put("disk_volume", StringUtil.trim(diskVolume));
        postDataMap.put("page", StringUtil.trim(page));
        postDataMap.put("replication", StringUtil.trim(replication));
        postDataMap.put("option", StringUtil.joinWrap(option));
        postDataMap.put("disk_type", StringUtil.trim(diskType));
        //可选参数
        if (user != null && user.trim().length() != 0) {
            postDataMap.put("user", user);
        }
        if (authentication != null && authentication.trim().length() != 0) {
            postDataMap.put("authentication", authentication);
        }
        if (crypt != null && crypt.trim().length() != 0) {
            postDataMap.put("authentication", crypt);
        }
        if (snapshot != null && snapshot.trim().length() != 0) {
            postDataMap.put("snapshot", snapshot);
        }
        
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject deviceDelete(String uuid) throws MalformedURLException, IOException {
        
        String url = this.baseUrl + "device?";
        url += "command=delete&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("deviceDelete() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject deviceModify(String uuid, String name,  
            Integer[] option, int diskType, String user, String authentication, String snapshot) throws MalformedURLException, IOException {
        
        String url = this.baseUrl + "device?";
        url += "command=modify&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("deviceModify() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        postDataMap.put("option", StringUtil.joinWrap(option));
        postDataMap.put("disk_type", StringUtil.trim(diskType));
        //可选参数
        if (user != null && user.trim().length() != 0) {
            postDataMap.put("user", user);
        }
        if (authentication != null && authentication.trim().length() != 0) {
            postDataMap.put("authentication", authentication);
        }
        if (snapshot != null && snapshot.trim().length() != 0) {
            postDataMap.put("snapshot", snapshot);
        }
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject forwarderAdd(String target, int type, int networkType, String networkSource) throws MalformedURLException, IOException {
        String url = this.baseUrl + "forwarder?";
        url += "command=add&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("forwarderAdd() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", target);
        postDataMap.put("type", String.valueOf(type));
        postDataMap.put("network_type", String.valueOf(networkType));
        postDataMap.put("network_source", networkSource);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject forwarderRemove(String target, int type, String uuid) throws MalformedURLException, IOException {
        String url = this.baseUrl + "forwarder?";
        url += "command=remove&";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("forwarderRemove() -> url=["+url+"]");
        }
        
        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", target);
        postDataMap.put("type", String.valueOf(type));
        postDataMap.put("uuid", uuid);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject fileUpload(String param, String fileName, InputStream fileStream) throws MalformedURLException, IOException {
        String url = this.baseUrl + "file_upload?";
        url += "session_id="+this.sessionId+"&";
        
        if( logger.isDebugEnabled() )
        {
            logger.info("fileUpload() -> url=["+url+"]");
        }
        
        JSONObject json = HttpUtil.fileUpload(url, param, fileName, fileStream, new DefaultResponseHanlder());
        return json;
    }
    
    //----------
    
    
    public byte[] encrypt(Map<String, String> param)
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
            
//          if( logger.isDebugEnabled() )
//          {
//              logger.info("encrypt data: ["+sb+"]");
//              logger.info("encryptionKey: ["+this.encryptionKeyBase64+"]");
//              logger.info("after encrypt: ["+StringUtil.bytesToBase64(AESUtil.encrypt(this.encryptionKey, sb.toString()))+"]");
//          }
            
            return AESUtil.encrypt(this.encryptionKey, sb.toString());
        }
        catch( UnsupportedEncodingException e )
        {
            throw new AppException(encryption);
        }
    }
    
    public byte[] decrypt(byte[] bytes)
    {
        return AESUtil.decrypt(this.encryptionKey, bytes);
    }
    
    //---------------
    

    public JSONObject decryptReceivedData(HttpServletRequest request) throws IOException
    {
        InputStream in = request.getInputStream();
        String contentType = request.getHeader("Content-Type");
        return decryptReceivedData(in, contentType);
    }
    
    
    public JSONObject decryptReceivedData(InputStream in, String contentType) throws IOException
    {
        // 处理结果
        if( contentType.contains("text/json/encryption;") )
        {
            byte[] bytes = StreamUtil.inputStreamToByteArray(in);
            bytes = decrypt(bytes);
            logger.debug(">>>> decrypt return: ["+new String(bytes)+"]");
            String json = new String(bytes);
            return (JSONObject)JSONObject.fromObject(json);
        }
        else if( contentType.contains("text/json;") )
        {
            byte[] bytes = StreamUtil.inputStreamToByteArray(in);
            String json = new String(bytes, "utf8");
            logger.debug(">>>> return: ["+json+"]");
            return (JSONObject)JSONObject.fromObject(json);
        }
        else
        {
            throw new AppException("unsupported Content-Type["+contentType+"]");
        }
    }
    
    
    public class DefaultResponseHanlder implements InputStreamHandler<JSONObject>
    {
        public JSONObject handle(InputStream in, HttpURLConnection conn) throws IOException
        {
            // 更新时间
            thisHttpGatewayHelper.lastRequestTimestamp = System.currentTimeMillis();
            // 处理结果
            String contentType = conn.getHeaderField("Content-Type");
            return decryptReceivedData(in, contentType);
        }
    }
    
    //--------------------
//  
//  public static void main(String[] args) throws MalformedURLException, IOException
//  {
////        HttpGatewayHelper.getInstance().connectEstablish("M1,M2,M3,M4,M5,M6", "1.0");
////        HttpGatewayHelper.getInstance().connectVerify();
////        
////        // 登录
////        // HttpGatewayHelper.getInstance().hostCreationResult("T2_chz_t3");
////        JSONObject result = HttpGatewayHelper.getInstance().diskImageQuery();
////        System.out.println("" + result);
//      
//      
//      
//      HttpGatewayHelper.getInstance().connectEstablish("M1,M2,M3,M4,M5,M6", "1.0");
//      HttpGatewayHelper.getInstance().connectVerify();
//      
//      /*
//      String name, String pool, Integer cpu_count, BigInteger memory, Integer[] option, String image, 
//      BigInteger[] disk_volume, Integer[] port, String user, String group, String display, String authentication, 
//      String network, BigInteger inbound_bandwidth, BigInteger outbound_bandwidth
//      */
//      JSONObject result = HttpGatewayHelper.getInstance().hostCreate( "T2_chz2", 
//                                                                      "c37e4daf44fc42eab844f3110a3f4afb", 
//                                                                      1, 
//                                                                      new BigInteger("1073741824"), 
//                                                                      new Integer[]{0, 1, 0}, 
//                                                                      "", 
//                                                                      new BigInteger[]{new BigInteger("1073741824"), new BigInteger("1073741824")}, 
//                                                                      new Integer[]{}, 
//                                                                      "operator", 
//                                                                      "T2", 
//                                                                      "operator", 
//                                                                      "operator", 
//                                                                      "", 
//                                                                      new BigInteger("9999999999"),
//                                                                      new BigInteger("9999999999")
//                                                                      );
//      System.out.println(result);
//      
//  }
    

    public static void main(String[] args) throws MalformedURLException, IOException
    {
        
        HttpGatewayHelper.getInstance().connectEstablish("M1,M2,M3,M4,M5,M6", "1.0");
        HttpGatewayHelper.getInstance().connectVerify();
        
//      JSONObject result = HttpGatewayHelper.getInstance().hostAttachDisk("7cf61147-1b1d-40b4-a76a-625db196eb80", new BigInteger(1024*1024*1024+""), 0, "", 0);
        JSONObject result = HttpGatewayHelper.getInstance().hostDetachDisk("7cf61147-1b1d-40b4-a76a-625db196eb80", 1);
        System.out.println(result);
        
    }
    
    public JSONObject startSystemMonitor(String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "system_monitor?";
        url += "command=start&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("startSystemMonitor() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject stopSystemMonitor(int task) throws MalformedURLException, IOException {
        String url = this.baseUrl + "system_monitor?";
        url += "command=stop&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("stopSystemMonitor() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("task", String.valueOf(task));

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject hostFlushDiskImage(String uuid, int disk, int mode, String image, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "host?";
        url += "command=flush_disk_image&";
        url += "session_id=" + this.sessionId + "&";
        
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("disk", StringUtil.trim(disk));
        postDataMap.put("mode", StringUtil.trim(mode));
        postDataMap.put("image", StringUtil.trim(image));
        postDataMap.put("callback", callback);
        
        byte[] encryptParam = encrypt(postDataMap);
        
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");
        
        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        
        return json;
    }
    
    public JSONObject hostBackup(String uuid, int mode, int disk, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "host?";
        url += "command=backup&";
        url += "session_id=" + this.sessionId + "&";

        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("mode", StringUtil.trim(mode));
        if (mode == 1) {
            postDataMap.put("disk", StringUtil.trim(disk));
        }
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

        return json;
    }
    
    public JSONObject hostResume(String uuid, int mode, int disk, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "host?";
        url += "command=resume&";
        url += "session_id=" + this.sessionId + "&";

        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("mode", StringUtil.trim(mode));
        if (mode == 1) {
            postDataMap.put("disk", StringUtil.trim(disk));
        }
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

        return json;
    }
    
    public JSONObject hostQueryBackup(String uuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "host?";
        url += "command=query_backup&";
        url += "session_id=" + this.sessionId + "&";

        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

        return json;
    }
    
    public JSONObject ruleAdd(String target, int mode, String[] ip, Integer[] port, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "rule?";
        url += "command=add&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("ruleAdd() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", target);
        postDataMap.put("mode", String.valueOf(mode));
        postDataMap.put("ip", StringUtil.joinWrap(ip));
        postDataMap.put("port", StringUtil.joinWrap(port));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject ruleRemove(String target, int mode, String[] ip, Integer[] port, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "rule?";
        url += "command=remove&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("ruleRemove() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", target);
        postDataMap.put("mode", String.valueOf(mode));
        postDataMap.put("ip", StringUtil.joinWrap(ip));
        postDataMap.put("port", StringUtil.joinWrap(port));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject ruleQuery(String target, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "rule?";
        url += "command=query&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("ruleQuery() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("target", target);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
    
    public JSONObject networkCreate(String name, int netmask, String description, String pool, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=create&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkCreate() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("name", name);
        postDataMap.put("netmask", String.valueOf(netmask));
        postDataMap.put("description", description);
        postDataMap.put("pool", pool);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkQuery(String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=query&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkQuery() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkModify(String uuid, String name, String description, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=modify&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkModify() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("name", name);
        postDataMap.put("description", description);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkDetail(String uuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=detail&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkDetail() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkStart(String uuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=start&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkStart() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkStop(String uuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=stop&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkStop() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkDelete(String uuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=delete&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkDelete() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkQueryHost(String uuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=query_host&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkQueryHost() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkAttachHost(String uuid, String hostUuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=attach_host&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkAttachHost() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("host", hostUuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkDetachHost(String uuid, String hostUuid, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=detach_host&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkDetachHost() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("host", hostUuid);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkAttachAddress(String uuid, int count, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=attach_address&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkAttachAddress() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("count", String.valueOf(count));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkDetachAddress(String uuid, String[] ip, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=detach_address&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkDetachAddress() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("ip_list", StringUtil.joinWrap(ip));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkBindPort(String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=bind_port&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkBindPort() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("protocol_list", StringUtil.joinWrap(protocolList));
        postDataMap.put("ip_list", StringUtil.joinWrap(ipList));
        postDataMap.put("port_list", StringUtil.joinWrap(portList));
        postDataMap.put("host_list", StringUtil.joinWrap(hostList));
        postDataMap.put("host_port_list", StringUtil.joinWrap(hostPortList));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject networkUnbindPort(String uuid, String[] protocolList, String[] ipList, String[] portList, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "network?";
        url += "command=unbind_port&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("networkUnbindPort() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("uuid", uuid);
        postDataMap.put("protocol_list", StringUtil.joinWrap(protocolList));
        postDataMap.put("ip_list", StringUtil.joinWrap(ipList));
        postDataMap.put("port_list", StringUtil.joinWrap(portList));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject startMonitor(int level, String[] target, String callback) throws MalformedURLException,
            IOException {
        String url = this.baseUrl + "monitor?";
        url += "command=start&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("startMonitor() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("level", String.valueOf(level));
        postDataMap.put("target", StringUtil.joinWrap(target));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject monitorHeartbeat(int task) throws MalformedURLException, IOException {
        String url = this.baseUrl + "monitor?";
        url += "command=heartbeat&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("monitorHeartbeat() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("task", String.valueOf(task));

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }

    public JSONObject stopMonitor(int task, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "monitor?";
        url += "command=stop&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("stopMonitor() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("task", String.valueOf(task));
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }  
    
public JSONObject serverQueryStorageDevice(int level, String target, int diskType, String callback) throws MalformedURLException, IOException {     
    String url = this.baseUrl + "server?";
    url += "command=query_storage_device&";
    url += "session_id=" + this.sessionId + "&";

    if (logger.isDebugEnabled()) {
        logger.info("serverQuerystorageDevice() -> url=[" + url + "]");
    }

    // 参数
    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("level", String.valueOf(level));
    postDataMap.put("target", target);
    postDataMap.put("disk_type", String.valueOf(diskType));
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    // 发送http消息，并取得返回的数据
    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    return json;
}

public JSONObject serverAddStorageDevice(int level, String target, int diskType, Integer[] index, String name, String path, String crypt, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "server?";
    url += "command=add_storage_device&";
    url += "session_id=" + this.sessionId + "&";

    if (logger.isDebugEnabled()) {
        logger.info("serverAddStorageDevice() -> url=[" + url + "]");
    }

    // 参数
    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("level", String.valueOf(level));
    postDataMap.put("target", target);
    postDataMap.put("disk_type", String.valueOf(diskType));
    postDataMap.put("index", StringUtil.joinWrap(index));
    postDataMap.put("name", name);
    postDataMap.put("path", path);
    postDataMap.put("crypt", crypt);
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    // 发送http消息，并取得返回的数据
    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    return json;
}

public JSONObject serverRemoveStorageDevice(int level, String target, int diskType, int index, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "server?";
    url += "command=remove_storage_device&";
    url += "session_id=" + this.sessionId + "&";

    if (logger.isDebugEnabled()) {
        logger.info("serverRemoveStorageDevice() -> url=[" + url + "]");
    }

    // 参数
    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("level", String.valueOf(level));
    postDataMap.put("target", target);
    postDataMap.put("disk_type", String.valueOf(diskType));
    postDataMap.put("index", String.valueOf(index));
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    // 发送http消息，并取得返回的数据
    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    return json;
}

public JSONObject serverEnableStorageDevice(int level, String target, int diskType, int index, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "server?";
    url += "command=enable_storage_device&";
    url += "session_id=" + this.sessionId + "&";

    if (logger.isDebugEnabled()) {
        logger.info("serverEnableStorageDevice() -> url=[" + url + "]");
    }

    // 参数
    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("level", String.valueOf(level));
    postDataMap.put("target", target);
    postDataMap.put("disk_type", String.valueOf(diskType));
    postDataMap.put("index", String.valueOf(index));
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    // 发送http消息，并取得返回的数据
    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    return json;
}

public JSONObject serverDisableStorageDevice(int level, String target, int diskType, int index, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "server?";
    url += "command=disable_storage_device&";
    url += "session_id=" + this.sessionId + "&";

    if (logger.isDebugEnabled()) {
        logger.info("serverDisableStorageDevice() -> url=[" + url + "]");
    }

    // 参数
    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("level", String.valueOf(level));
    postDataMap.put("target", target);
    postDataMap.put("disk_type", String.valueOf(diskType));
    postDataMap.put("index", String.valueOf(index));
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    // 发送http消息，并取得返回的数据
    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    return json;
	}

public JSONObject snapshotQuery(int type,String uuid, int mode, int index, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "snapshot?";
    url += "command=query&";
    url += "session_id=" + this.sessionId + "&";

    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("type", StringUtil.trim(type));
    postDataMap.put("uuid", uuid);
    postDataMap.put("mode", StringUtil.trim(mode));
    postDataMap.put("index", StringUtil.trim(index));        
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

    return json;
}

public JSONObject snapshotDel(int type,String uuid, int mode, String snapshotId, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "snapshot?";
    url += "command=delete&";
    url += "session_id=" + this.sessionId + "&";

    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("type", StringUtil.trim(type));
    postDataMap.put("target", uuid);
    postDataMap.put("mode", StringUtil.trim(mode));
    postDataMap.put("snapshot", snapshotId);        
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

    return json;
}  

public JSONObject snapshotCreate(int type,String uuid, int mode, String index, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "snapshot?";
    url += "command=create&";
    url += "session_id=" + this.sessionId + "&";

    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("type", StringUtil.trim(type));
    postDataMap.put("target", uuid);
    postDataMap.put("mode", StringUtil.trim(mode));
    postDataMap.put("index", index);        
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

    return json;
}
 
public JSONObject hostMigrate(String uuid, String target, int type, String callback) throws MalformedURLException, IOException {
    String url = this.baseUrl + "host?";
    url += "command=migrate&";
    url += "session_id=" + this.sessionId + "&";

    if (logger.isDebugEnabled()) {
        logger.info("hostMigrate() -> url=[" + url + "]");
    }

    // 参数
    Map<String, String> postDataMap = new LinkedHashMap<String, String>();
    postDataMap.put("host", uuid);
    postDataMap.put("target", target);
    postDataMap.put("type", String.valueOf(type)); 
    postDataMap.put("callback", callback);

    byte[] encryptParam = encrypt(postDataMap);

    // 发送http消息，并取得返回的数据
    Map<String, String> requestProperties = new LinkedHashMap<String, String>();
    requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

    JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
    return json;
}
  
    public JSONObject snapshotResume(int type,String uuid, int mode, String snapshotId, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "snapshot?";
        url += "command=resume&";
        url += "session_id=" + this.sessionId + "&";

        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("type", StringUtil.trim(type));
        postDataMap.put("target", uuid);
        postDataMap.put("mode", StringUtil.trim(mode));
        postDataMap.put("snapshot", snapshotId);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());

        return json;
    }

    public JSONObject serviceModify(int type, String target, int diskType, String diskSource, String crypt, String callback) throws MalformedURLException, IOException {
        String url = this.baseUrl + "service?";
        url += "command=modify_service&";
        url += "session_id=" + this.sessionId + "&";

        if (logger.isDebugEnabled()) {
            logger.info("serviceModify() -> url=[" + url + "]");
        }

        // 参数
        Map<String, String> postDataMap = new LinkedHashMap<String, String>();
        postDataMap.put("type", String.valueOf(type));
        postDataMap.put("target", target);
        postDataMap.put("disk_type", String.valueOf(diskType));
        postDataMap.put("disk_source", diskSource);
        postDataMap.put("crypt", crypt);
        postDataMap.put("callback", callback);

        byte[] encryptParam = encrypt(postDataMap);

        // 发送http消息，并取得返回的数据
        Map<String, String> requestProperties = new LinkedHashMap<String, String>();
        requestProperties.put("Content-type", "application/x-www-form-urlencoded/encryption");

        JSONObject json = HttpUtil.post(url, encryptParam, requestProperties, new DefaultResponseHanlder());
        return json;
    }
}



