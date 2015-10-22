
package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.SysWarnValueVO;

/**
 * @ClassName: SysWarnValueMapper
 * @Description: 预警值Mappper
 * @author 张本缘 于 2015年7月13日 上午10:42:07
 */
public interface SysWarnValueMapper {

    /**
     * @Description:取得所有数据
     * @return List
     */
    public List<SysWarnValueVO> getAll();

    /**
     * @Description:根据规则取得所有ID
     * @return List
     */
    public List<SysWarnValueVO> getByRuleID(String ruleid);
    
    /**
     * @Description:根据条件查询数据条数
     * @param condition
     * @return int
     */
    public int queryPageCount(Object condition);

    /**
     * @Description:根据条件查询数据
     * @param condition
     * @return List
     */
    public List<SysWarnValueVO> queryPage(Object condition);

    /**
     * @Description:取得单个实体对象
     * @param id
     * @return SysWarnValueVO
     */
    public SysWarnValueVO getById(String id);

    /**
     * @Description:新增实体对象信息
     * @param data
     * @return int
     */
    public int addSysWarnValue(Map<String, Object> data);

    /**
     * @Description:更新实体对象信息
     * @param data
     * @return int
     */
    public int updateSysWarnValue(Map<String, Object> data);

    /**
     * @Description:批量删除数据
     * @param ids
     * @return int
     */
    public int deleteSysWarnValue(String[] ids);
    
    /**
     * @Description:根据规则ID删除内容
     * @param ruleid
     * @return int
     */
    public int deleteByRule(String ruleid);

}
