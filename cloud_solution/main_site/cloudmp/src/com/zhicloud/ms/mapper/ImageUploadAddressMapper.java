package com.zhicloud.ms.mapper;



import com.zhicloud.ms.vo.ImageUploadAddressVO;

import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @description 上传镜像地址管理mapper
 */
public interface ImageUploadAddressMapper {

    /**
     * @funtion 查询全部地址
     * @param condition 启用和未启用
     * @return 地址List对象
     */
    List<ImageUploadAddressVO> queryAllAddress(Map<String, Object> condition);

    /**
     * @function 按serviceName查询地址
     * @param serviceName
     * @return 地址对象
     */
    ImageUploadAddressVO queryAddressByServiceName(String serviceName);

    /**
     * @function 更新地址信息
     * @param data
     * @return 大于零为成功
     */
    int updateAddress(Map<String, Object> data);

    /**
     * @function 删除地址信息
     * @param serviceNames
     * @return 大于零为成功
     */
    int deleteAddresses(String[] serviceNames);

}
