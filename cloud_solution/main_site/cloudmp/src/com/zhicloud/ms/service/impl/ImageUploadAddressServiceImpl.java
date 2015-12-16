package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.app.pool.uploadAddressPool.ImageUploadAddressPool;
import com.zhicloud.ms.app.pool.uploadAddressPool.ImageUploadAddressPoolManager;
import com.zhicloud.ms.common.util.HttpUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.mapper.ImageUploadAddressMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ImageUploadAddressService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.ImageUploadAddressVO;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @description 上传镜像地址管理service实现
 */
@Transactional(readOnly = true)
public class ImageUploadAddressServiceImpl implements ImageUploadAddressService {

    public static final Logger logger = Logger.getLogger(ImageUploadAddressServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * @function 获取所有地址
     * @return 所有地址List
     */
    @Override public List<ImageUploadAddressVO> getAllAddress() {
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        return this.sqlSession.getMapper(ImageUploadAddressMapper.class).queryAllAddress(condition);
    }

    /**
     * @function 获取可用地址（异步）
     * @return 是否获取成功
     */
    private Boolean getAvaliableAddressAsync() throws IOException {
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
        JSONObject result = null;

        try {
            result = channel.imageServiceQuery();
        } catch (Exception e) {
            logger.error(String.format("fail to query image service. exception[%s]", e));
            channel.release();
            return false;
        }

        if (HttpGatewayResponseHelper.isSuccess(result) == true) {
            return true;

        }
        return false;
    }

    /**
     * @funtion 从平台获取可用地址
     * @return 可用地址List
     * @throws IOException
     */
    @Override public List<ImageUploadAddressVO> getAvailableAddressFromPlatform() throws IOException {

        boolean result = this.getAvaliableAddressAsync();

        if (result) {
            //获取对象
            ImageUploadAddressPool pool = ImageUploadAddressPoolManager.singleton().getPool();
            List <ImageUploadAddressVO> imageUploadAddresses = pool.getAll();

            try {
                synchronized (imageUploadAddresses) {//wait for 5 second or notify by response message.
                    imageUploadAddresses.wait(5 * 1000);
                }
            } catch (InterruptedException e) {
                logger.error("error occur when query compute pool detail response call back.", e);
                return null;
            }

            if (imageUploadAddresses.size() > 0) {//成功
                Iterator<ImageUploadAddressVO> it = imageUploadAddresses.iterator();
                Iterator<ImageUploadAddressVO> data = this.sqlSession.getMapper(ImageUploadAddressMapper.class).queryAllAddress(
                    new LinkedHashMap<String, Object>() {
                    }).iterator();
                while (it.hasNext()) {
                    ImageUploadAddressVO imageUploadAddressVO = it.next();
                    while (data.hasNext()) {
                        ImageUploadAddressVO temp = data.next();
                        if (temp.getServiceName().equals(imageUploadAddressVO.getServiceName())) {
                            it.remove();
                        }
                    }
                }
                return imageUploadAddresses;
            } else {//失败或无应答
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * @function 轮询获取可用地址
     * @param request 用户请求
     * @return ip:port 字符串 没有返回 none
     * @throws IOException
     */
    @Override public String getAvailableAddress(HttpServletRequest request) throws IOException {
        boolean flag = HttpUtil.isIpAddr(request);

        ImageUploadAddressVO address = this.getAddressByPolling();
        if (address == null) {
            return "none";
        }
        String url = "";
        if (flag) {
            // 内网请求
            url = address.getLocalIp() + ":"+address.getLocalPort();
        } else {
            // 外网请求
            url = address.getPublicIp() + ":"+address.getPublicPort();
        }
        return url;
    }

    /**
     * @function 轮询获取可用地址
     * @return 可用地址对象
     */
    private ImageUploadAddressVO getAddressByPolling(){
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("service_name", AppConstant.SERVICE_ENABLE);
        List<ImageUploadAddressVO> addressVOs = this.sqlSession.getMapper(ImageUploadAddressMapper.class).queryAllAddress(condition);
        int size = addressVOs.size();
        if (size == 0) {
            return null;
        }
        ImageUploadAddressVO[] addressArr = addressVOs.toArray(new ImageUploadAddressVO[size]);
        int length = addressArr.length;
        int current = java.util.concurrent.ThreadLocalRandom.current().nextInt(length);
        return addressArr[current];
    }

    /**
     * @function 按serviceName 获取地址
     * @param serviceName
     * @return 地址对象
     */
    @Override public ImageUploadAddressVO getAddressByServiceName(String serviceName) {
        return this.sqlSession.getMapper(ImageUploadAddressMapper.class).queryAddressByServiceName(serviceName);
    }

    /**
     * @function 更新上传地址
     * @param parameter
     * @return 执行结果
     */
    @Transactional(readOnly = false)
    @ResponseBody
    @Override public MethodResult modifyAddress(Map<String, Object> parameter) {

        try {
            // 参数处理
            String serviceName = StringUtil.trim(parameter.get("service_name"));
            String localIp = StringUtil.trim(parameter.get("local_ip"));
            String localPortStr = StringUtil.trim(parameter.get("local_port"));
            int localPort;
            String publicIp = StringUtil.trim(parameter.get("public_ip"));
            String publicPortStr = StringUtil.trim(parameter.get("public_port"));
            int publicPort = 0;
            String serviceEnableStr = StringUtil.trim(parameter.get("service_enable"));
            int serviceEnable = 0;
            String description = StringUtil.trim(parameter.get("description"));

            if(serviceName == null || serviceName==""){
                return new MethodResult(MethodResult.FAIL,"服务名称不能为空");
            }

            if(localIp == null || localIp==""){
                return new MethodResult(MethodResult.FAIL,"本地IP不能为空");
            }

            if(localPortStr == null || localPortStr == ""){
                return new MethodResult(MethodResult.FAIL,"本地端口不能为空");
            } else{
                localPort = Integer.valueOf(localPortStr);
            }

            if(publicPortStr != null && publicPortStr != ""){
                publicPort = Integer.valueOf(publicPortStr);
            }

            if(serviceEnableStr != null && serviceEnableStr != ""){
                serviceEnable = Integer.valueOf(serviceEnableStr);
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("service_name", serviceName);
            data.put("local_ip", localIp);
            data.put("local_port", localPort);
            data.put("public_ip", publicIp);
            data.put("public_port", publicPort);
            data.put("service_enable", serviceEnable);
            data.put("description", description);

            int result = this.sqlSession.getMapper(ImageUploadAddressMapper.class).updateAddress(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "写入成功");
            }


            return new MethodResult(MethodResult.FAIL, "写入失败");
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("写入失败");
        }
    }

    /**
     *  @function 删除上传地址
     * @param serviceNames
     * @return 执行结果
     */
    @Transactional(readOnly = false)
    @Override public MethodResult removeAddress(List<String> serviceNames) {

        try {
            if( serviceNames == null || serviceNames.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "serviceNames不能为空");
            }

            ImageUploadAddressMapper imageUploadAddressMapper = this.sqlSession.getMapper(ImageUploadAddressMapper.class);
            int n = imageUploadAddressMapper.deleteAddresses(serviceNames.toArray(new String[0]));

            if( n > 0 )
            {
                return new MethodResult(MethodResult.SUCCESS, "删除成功");
            }
            else
            {
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("删除失败");
        }
    }

    /**
     *  @function 测试地址可用性
     * @param addressVO
     * @return 执行结果
     */
    @ResponseBody
    @Override public MethodResult testAvaliable(ImageUploadAddressVO addressVO) throws IOException {

        String localUrl = "http://"+addressVO.getLocalIp() + ":" + addressVO.getLocalPort() + "/image_usage";

        int localCode = this.getResponseCodeByGet(localUrl);

        if (localCode != 200) {
            return new MethodResult(MethodResult.FAIL, "内网地址不可用, 请重新选择");
        }

        if (!StringUtil.isBlank(addressVO.getPublicIp())) {
            String publicUrl = "http://"+addressVO.getPublicIp() + ":" + addressVO.getPublicPort() + "/image_usage";

            int publicCode = this.getResponseCodeByGet(publicUrl);

            if (publicCode != 200) {
                return new MethodResult(MethodResult.FAIL, "公网地址不可用, 请重新填写");
            }
        }

        return new MethodResult(MethodResult.SUCCESS, "地址可用");
    }

    /**
     *  @function 发送get请求获取HTTP 状态码
     * @param url
     * @return HTTP 状态码
     */
    private int getResponseCodeByGet(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = -1;
        try {
            responseCode = con.getResponseCode();
            return responseCode;
        } catch (Exception e) {
            logger.error("该URL无法访问");
        } finally {
            return responseCode;
        }

    }

}
