package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPool;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.httpGateway.*;
import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.CloudHostWarehouseMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IComputePoolService;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * @author 张翔
 */
@Service("computePoolService")
@Transactional(readOnly=true)
public class ComputePoolServiceImpl implements IComputePoolService {

    private static final Logger logger = Logger.getLogger(ComputePoolServiceImpl.class);
    @Resource private SqlSession sqlSession;

    /**
     * @function 获取计算资源池详情（异步）
     * @param uuid
     * @return
     */
    @Override public Boolean getComputePoolDetailAsync(String uuid) throws IOException {
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
        ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();
        JSONObject result = null;
        try {
            result = channel.computePoolDetail(uuid);
        } catch (Exception e) {
            logger.error(String.format("fail to query compute pool detail. exception[%s]", e));
            channel.release();
            return false;
        }

        if (HttpGatewayResponseHelper.isSuccess(result) == true) {
            ComputeInfoExt computeInfoExt = new ComputeInfoExt();
            computeInfoExt.setUuid(uuid);
            pool.put(uuid, computeInfoExt);
            return true;

        }
        return false;
    }

    /**
     * @function 获取计算资源池详情（同步）
     * @param uuid
     * @return
     */
    @Override public ComputeInfoExt getComputePoolDetailSync(String uuid) throws IOException {

        boolean result = this.getComputePoolDetailAsync(uuid);

        if (result) {
            //获取对象
            ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();
            ComputeInfoExt computeInfoExt = pool.get(uuid);

            try {
                synchronized (computeInfoExt) {//wait for 5 second or notify by response message.
                 computeInfoExt.wait(5 * 1000);
                }
            } catch (InterruptedException e) {
                logger.error("error occur when query compute pool detail response call back.", e);
                return null;
            }

            if (computeInfoExt.isSuccess()) {//成功
                return computeInfoExt.clone();
            } else {//失败或无应答
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public String createComputePoolAsync(String name, int networkType, String network,
        int diskType, String diskSource, Integer[] mode, String path, String crypt)
        throws IOException {
        ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();

        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
        JSONObject result = null;
        String sessionId = null;
        try {
            result = channel.computePoolCreate(name, networkType, network, diskType, diskSource,
                mode, path, crypt);
            sessionId = channel.getSessionId();
        } catch (Exception e) {
            logger.error(String.format("fail to create compute pool. exception[%s]", e));
            return null;
        }

        if (HttpGatewayResponseHelper.isSuccess(result) == true) {
            ComputeInfoExt computeInfoExt = new ComputeInfoExt();
            pool.put(sessionId, computeInfoExt);
            return sessionId;

        }
        return null;
    }

    @Override public MethodResult createComputePoolSync(Map<String, Object> parameter)
        throws IOException {
        //获取参数
        String name = StringUtil.trim(parameter.get("name"));
        int networkType = Integer.valueOf(StringUtil.trim(parameter.get("network_type")));
        String network = StringUtil.trim(parameter.get("network"));
        int diskType = Integer.valueOf(StringUtil.trim(parameter.get("disk_type")));
        String diskSource = StringUtil.trim(parameter.get("disk_source"));
        int mode0 = Integer.valueOf(StringUtil.trim(parameter.get("mode0")));
        int mode1 = Integer.valueOf(StringUtil.trim(parameter.get("mode1")));
        int mode2 = Integer.valueOf(StringUtil.trim(parameter.get("mode2")));
        int mode3 = Integer.valueOf(StringUtil.trim(parameter.get("mode3")));
        Integer[] mode = new Integer[4];
        mode[0] = mode0;
        mode[1] = mode1;
        mode[2] = mode2;
        mode[3] = mode3;
//        String path = StringUtil.trim(parameter.get("path"));
//        String crypt = StringUtil.trim(parameter.get("crypt"));
        String path = "";
        String crypt = "";


        HttpGatewayChannel channel = HttpGatewayManager.getChannel(1);
        JSONObject result = null;

        result = channel.computePoolCreate(name,networkType, network, diskType, diskSource ,mode,path,crypt);
        if ("success".equals(result.getString("status"))){
            return new MethodResult(MethodResult.SUCCESS,"资源池创建成功");
        }

        return new MethodResult(MethodResult.FAIL,"资源池创建失败");


    }


    /**
     * @function 修改资源池(异步)
     * @param uuid
     * @param name
     * @param networkType
     * @param network
     * @param diskType
     * @param diskSource
     * @param option
     * @return
     * @throws IOException
     */
    @Override public String modifyComputePoolAsync(String uuid, String name, int networkType, String network, int diskType, String diskSource, Integer[] mode, int option,String path,String crypt)
        throws IOException {

        ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();

        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
        JSONObject result = null;
        String sessionId = null;
        try {
            result = channel.computePoolModify(uuid, name, networkType, network, diskType,
                diskSource, mode, option, path ,crypt);
            sessionId = channel.getSessionId();
        } catch (Exception e) {
            logger.error(String.format("fail to modify compute pool. exception[%s]", e));
            return null;
        }

        if (HttpGatewayResponseHelper.isSuccess(result) == true) {
            ComputeInfoExt computeInfoExt = new ComputeInfoExt();
            computeInfoExt.setUuid(uuid);
            pool.put(sessionId, computeInfoExt);
            return sessionId;

        }
        return null;
    }

    /**
     * @function 修改资源池(同步)
     * @param parameter
     * @return
     */
    @Override public MethodResult modifyComputePoolSync(Map<String, Object> parameter)
        throws IOException {

        //获取参数
        String uuid = StringUtil.trim(parameter.get("uuid"));
        String name = StringUtil.trim(parameter.get("name"));
        int networkType = Integer.valueOf(StringUtil.trim(parameter.get("network_type")));
        String network = StringUtil.trim(parameter.get("network"));
        int diskType = Integer.valueOf(StringUtil.trim(parameter.get("disk_type")));
        String diskSource = StringUtil.trim(parameter.get("disk_source"));
        int mode0 = Integer.valueOf(StringUtil.trim(parameter.get("mode0")));
        int mode1 = Integer.valueOf(StringUtil.trim(parameter.get("mode1")));
        int mode2 = Integer.valueOf(StringUtil.trim(parameter.get("mode2")));
        int mode3 = Integer.valueOf(StringUtil.trim(parameter.get("mode3")));        
//        String path = StringUtil.trim(parameter.get("path"));
//        String crypt = StringUtil.trim(parameter.get("crypt"));
        String path = "";
        String crypt = "";
        Integer[] mode = new Integer[4];
        mode[0] = mode0;
        mode[1] = mode1;
        mode[2] = mode2;
        mode[3] = mode3;        
        int option = 0;

        String sessionId = null;
        sessionId = this.modifyComputePoolAsync(uuid, name, networkType, network, diskType, diskSource, mode, option, path, crypt);

        ComputeInfoExt computeInfoExt = null;


        if (!StringUtil.isBlank(sessionId)) {
            try {
                //获取对象
                ComputeInfoPool pool = ComputeInfoPoolManager.singleton().getPool();
                computeInfoExt = pool.get(sessionId);

                synchronized (computeInfoExt) {//wait for 5 second or notify by response message.
                    computeInfoExt.wait(5 * 1000);
                }
            } catch (InterruptedException e) {
                logger.error("error occur when modify compute pool response call back.", e);
                return new MethodResult(MethodResult.FAIL,"资源池修改失败");
            }

            if (computeInfoExt.isSuccess()) {//成功
                return new MethodResult(MethodResult.SUCCESS,"资源池修改成功");
            } else {//失败或无应答
                return new MethodResult(MethodResult.FAIL,"资源池修改失败");

            }
        }else{
            return new MethodResult(MethodResult.FAIL,"资源池修改失败");
        }


    }

    /**
     * @function 删除资源池
     * @param uuid
     * @return
     */
    @Override
    @Transactional(readOnly=false)
    public MethodResult removeComputePool(String uuid) {
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
            CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);
            if(channel!=null){
                JSONObject result = channel.hostQuery(uuid);
                if("success".equals(result.get("status"))){

                    //删除资源池中的主机
                    JSONArray computerList = result.getJSONArray("hosts");
                    for (int i = 0; i < computerList.size(); i ++) {
                        JSONObject computerObject = computerList.getJSONObject(i);
                        String uid = computerObject.getString("uuid");
                        CloudHostVO cloudHostVO = cloudHostMapper.getByRealHostId(uid);
                        String warehostId = cloudHostVO.getWarehouseId();
                        channel.hostDelete(uid);
                        CloudHostWarehouse cloudHostWarehouse = cloudHostWarehouseMapper.getById(
                            warehostId);
                        Integer totalAmount = cloudHostWarehouse.getTotalAmount();
                        Integer remainAmount = cloudHostWarehouse.getRemainAmount();
                        Integer assignedAmount = cloudHostWarehouse.getAssignedAmount();
                        if (totalAmount > 0 && remainAmount > 0 && assignedAmount >0) {
                            if (totalAmount >= remainAmount && totalAmount >= assignedAmount) {
                                //删除该主机
                                cloudHostMapper.deleteById(cloudHostVO.getId());
                                if (cloudHostVO.getUserId() == null) { // 该主机未分配
                                    cloudHostWarehouseMapper.updateWarehouseAmountForDeleteHost(warehostId);
                                }else {                               //  该主机已分配
                                    cloudHostWarehouseMapper.updateWarehouseAmountForDeleteDispatchedHost(warehostId);

                                }

                            } else {
                                return new MethodResult(MethodResult.FAIL,"仓库数据异常");
                            }
                        } else {
                            return new MethodResult(MethodResult.FAIL,"仓库数据异常");
                        }

                    }
                }
                //删除NC
                JSONObject nodeResult = channel.computePoolQueryResource(uuid);
                if("success".equals(result.get("status"))){
                    JSONArray nodeList = nodeResult.getJSONArray("compute_resources");
                    for (int i = 0; i < nodeList.size(); i ++) {
                        JSONObject nodeObject = nodeList.getJSONObject(i);
                        String name = nodeObject.getString("name");
                        channel.computePoolRemoveResource(uuid, name);
                    }
                }
            }
            //删除资源池
            JSONObject drp = channel.computePoolDelete(uuid);
            if("success".equals(drp.getString("status"))){
                return new MethodResult(MethodResult.SUCCESS,"资源池删除成功");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MethodResult(MethodResult.FAIL,"资源池删除失败");
    }
}
