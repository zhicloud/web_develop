
package com.zhicloud.ms.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
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
        SharedMemoryVO available = mapper.queryAvailable();
        if(available != null){
            for(int i=0;i<ids.length;i++){
                if(ids[i].equals(available.getId())){
                    try {
                        Runtime.getRuntime().exec("umount  /image");
                        break;
                    } catch (IOException e) {
                         e.printStackTrace();
                    }
                }
            }
        }
        
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
		 SharedMemoryVO vo = mapper.queryAvailable();
		 //执行
		 try {
            this.executeShellOfMount(vo.getUrl());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		 if(n>0){
			 return new MethodResult(MethodResult.SUCCESS,"设置成功");
		 }
		 return new MethodResult(MethodResult.FAIL,"设置失败");
	}
	
	@Override
	public SharedMemoryVO queryAvailable() {
		 SharedMemoryMapper mapper = sqlSession.getMapper(SharedMemoryMapper.class);
		 return mapper.queryAvailable();
	}
	
	private int executeShellOfMount(String url) throws IOException { 
        Map<String,String> temp = new HashMap<String,String>();
        try {
            Process pro = Runtime.getRuntime().exec("mkdir  /image");
            pro = Runtime.getRuntime().exec("umount  /image");
            logger.info("create /image fiel");
            pro = Runtime.getRuntime().exec("mount -t nfs "+url+" /image");
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
             int i = 0;
            String str = br.readLine();
            while(!StringUtils.isBlank(str)){
                if(i == 0){
                    continue;
                }else{
                    String [] ips = StringUtil.getIpFromUrl(str); 
                    if(ips != null && ips.length >= 1){
                        temp.put(ips[0], str.replace(ips[0], ""));
                    } 
                }                 
                str = br.readLine();
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }  
        return 1;
     } 
}
