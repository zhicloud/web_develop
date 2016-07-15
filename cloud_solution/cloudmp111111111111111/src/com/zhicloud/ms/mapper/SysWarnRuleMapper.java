
package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.SysWarnRuleVO;

/**
 * @ClassName: SysWarnRuleMapper
 * @Description: 预警规则Mappper
 * @author 张本缘 于 2015年7月13日 上午10:42:07
 */
public interface SysWarnRuleMapper {

    /**
     * @Description:取得所有数据
     * @return List
     */
    public List<SysWarnRuleVO> getAll();

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
    public List<SysWarnRuleVO> queryPage(Object condition);

    /**
     * @Description:取得单个实体对象
     * @param id
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getById(String id);

    /**
     * @Description:取得单个实体对象
     * @param id
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getByName(String name);

    /**
     * @Description:取得单个实体对象
     * @param id
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getByCode(String code);
    
    /**
     * @Description:新增实体对象信息
     * @param data
     * @return int
     */
    public int addSysWarnRule(Map<String, Object> data);

    /**
     * @Description:更新实体对象信息
     * @param data
     * @return int
     */
    public int updateSysWarnRule(Map<String, Object> data);

    /**
     * @Description:批量删除数据
     * @param ids
     * @return int
     */
    public int deleteSysWarnRule(String[] ids);

}
