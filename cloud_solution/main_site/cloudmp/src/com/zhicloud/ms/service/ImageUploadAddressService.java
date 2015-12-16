package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.ImageUploadAddressVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @description 上传镜像地址管理service
 */
public interface ImageUploadAddressService {

    /**
     * @function 获取所有地址
     * @return 所有地址List
     */
    List<ImageUploadAddressVO> getAllAddress();

    /**
     * @funtion 从平台获取可用地址
     * @return 可用地址List
     * @throws IOException
     */
    List <ImageUploadAddressVO> getAvailableAddressFromPlatform() throws IOException;

    /**
     * @function 轮询获取可用地址
     * @param request 用户请求
     * @return ip:port 字符串 没有返回 none
     * @throws IOException
     */
    String getAvailableAddress(HttpServletRequest request) throws IOException;

    /**
     * @function 按serviceName 获取地址
     * @param serviceName
     * @return 地址对象
     */
    ImageUploadAddressVO getAddressByServiceName(String serviceName);

    /**
     * @function 更新上传地址
     * @param parameter
     * @return 执行结果
     */
    MethodResult modifyAddress(Map<String, Object> parameter);

    /**
     *  @function 删除上传地址
     * @param serviceNames
     * @return 执行结果
     */
    MethodResult removeAddress(List<String> serviceNames);

    /**
     *  @function 测试地址可用性
     * @param addressVO
     * @return 执行结果
     */
    MethodResult testAvaliable(ImageUploadAddressVO addressVO) throws IOException;
}
