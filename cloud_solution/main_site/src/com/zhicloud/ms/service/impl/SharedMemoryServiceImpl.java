
package com.zhicloud.ms.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.SharedMemoryMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.SharedMemoryService;
import com.zhicloud.ms.vo.SharedMemoryVO;

@Service("sharedMemoryService")
@Transactional(readOnly = false)
public class SharedMemoryServiceImpl implements SharedMemoryService {

    /* 日志 */
    public static final Logger logger = Logger.getLogger(SharedMemoryServiceImpl.class);

    @Resource
    private SqlSession sqlSession;

    /**
     * Description:查询列表信息
     * @param condition 参数
     * @return List<SharedMemoryVO>
     */
    public List<SharedMemoryVO> queryInfo(Map<String, Object> condition) {
        SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
        return mapper.queryInfo(condition);
    }

    /**
     * Description:增加信息
     * @param data 数据
     * @return int
     */
    public int addInfo(Map<String, Object> data) {
        SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
        return mapper.addInfo(data);
    }

    /**
     * Description:修改信息
     * @param data 数据
     * @return int
     */
    public int updateInfo(Map<String, Object> data) {
        SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
        return mapper.updateInfo(data);
    }

    /**
     * Description:删除信息
     * @param data 数据
     * @return int
     */
    public int deleteInfo(String[] ids) {
        SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
        return mapper.deleteInfo(ids);
    }

    /**
     * Description:取得单个实体对象
     * @param id 主键
     * @return SharedMemoryVO 
     */
    public SharedMemoryVO getVO(String id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", id);
        SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
        List<SharedMemoryVO> lists = mapper.queryInfo(map);
        if (lists != null && lists.size() > 0) {
            return lists.get(0);
        } else {
            return null;
        }
    }
    
    /**
     * Description:验证名称是否重复
     * @param condition 参数
     * @return boolean (true 重复,false 不重复)
     */
    public boolean validateName(Map<String,Object> condition){
        if (condition != null && !condition.isEmpty()) {
            boolean returnflag = false;
            String type = condition.get("type") + "";
            String id = condition.get("id") + "";
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("name", condition.get("name"));
            SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
            List<SharedMemoryVO> lists = mapper.queryInfo(map);
            if (lists != null && lists.size() > 0) {
                if ("edit".equals(type)) {
                    SharedMemoryVO memory = lists.get(0);
                    if (!id.equals(memory.getId())) {
                        returnflag = true;
                    }
                } else {
                    returnflag = true;
                }
            }
            return returnflag;
        }
        return false;
    }

	@Override
	public MethodResult setAvailable(String id) {
		 SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
		 mapper.setDisable();
		 int n = mapper.updateAvailable(id);
		 if(n>0){
			 return new MethodResult(MethodResult.SUCCESS,"设置成功");
		 }
		 return new MethodResult(MethodResult.FAIL,"设置失败");
	}
}
