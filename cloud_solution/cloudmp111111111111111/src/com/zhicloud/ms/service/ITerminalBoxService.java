package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.TerminalBoxVO;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 6/23/15.
 */
public interface ITerminalBoxService {

    /**
     *
     * @return 全部终端盒子数据
     */
    public List<TerminalBoxVO> getAll();

    /**
     * @function 按条件获取盒子数据
     * @author 张翔
     * @param condition
     * @return 全部终端盒子数据
     */
    public List<TerminalBoxVO> getAll(Map<String, Object> condition);

    /**
     *
     * @param id 终端盒子ID
     * @return 指定ID的盒子数据
     */
    public TerminalBoxVO getById(String id);

    /**
     *
     * @param name 终端盒子名称
     * @return 指定名称的盒子数据
     */
    public TerminalBoxVO getByName(String name);

    /**
     *
     * @param serialNumber 终端盒子编号
     * @return 是否存在改盒子
     */
    public MethodResult checkSerialNumber(String serialNumber);

    /**
     *
     * @param parameter 终端盒子信息
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult addTerminalBox(Map<String, Object> parameter);

    /**
     *
     * @param parameter 终端盒子序列号、盒子名称、ID
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult modifyTerminalBox(Map<String, Object> parameter);

    /**
     *
     * @param parameter 终端盒子的分配用户、ID
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult allocateTerminalBox(Map<String, Object> parameter);

    /**
     *
     * @param parameter 终端盒子的ID
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult releaseTerminalBox(Map<String, Object> parameter);

    /**
     *
     * @param ids 多个终端盒子的ID
     * @return 该方法执行结果（成功或者失败）
     */
    public MethodResult deleteTerminalBoxByIds(List<String> ids);
    /**
     * 
    * @Title: buildTreeJSON 
    * @Description: 分配树 
    * @param @param menuid
    * @param @param array
    * @param @param roleid
    * @param @return      
    * @return JSONArray     
    * @throws
     */
    public JSONArray buildTreeJSON(String menuid, JSONArray array, String roleid);
    /**
     * 
    * @Title: releaseTerminalBoxByUserId 
    * @Description: 根据用户id回收当前用户所有的盒子
    * @param @param userId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult releaseTerminalBoxByUserId(String userId);
    
    /**
     * 查询所有未绑定的盒子
     * @return
     */
    public List<TerminalBoxVO> getUnboundBox();
    

}
