package com.zhicloud.ms.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.SysTenantMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ItenantService;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SysTenant;
import com.zhicloud.ms.vo.SysTenantExample;

@Service("tenantService")
@Transactional(readOnly=true)
public class TenantServiceImpl implements ItenantService{
	private static final Logger logger = Logger.getLogger(TenantServiceImpl.class);
	public static final Integer Region_LOCAL  = 0;
	
	@Resource
	private SqlSession sqlSession;
	
	@Override
	public List<SysTenant> getAllSysTenant(SysTenant parameter) {
		SysTenantMapper mapper= this.sqlSession.getMapper(SysTenantMapper.class);	
		CloudHostMapper cloudHostMapper= this.sqlSession.getMapper(CloudHostMapper.class); 
		SysTenantExample example  = new SysTenantExample();
		example.createCriteria().andStatusNotEqualTo(AppConstant.TENANT_STATUS_DELETE);
		List<SysTenant>  list = mapper.selectByExample(example);
		for(SysTenant tenant: list){
		    List<CloudHostVO> hostlist = cloudHostMapper.getCloudHostInTenant(tenant.getId());
		    BigInteger totalMemory = BigInteger.ZERO;
	        Integer totalCpu = 0;
	        BigInteger totalBandwidth = BigInteger.ZERO;
	        BigInteger totalDisk = BigInteger.ZERO;
	        for(CloudHostVO host : hostlist){
	            totalCpu = totalCpu+ host.getCpuCore();
	            totalMemory = totalMemory.add(host.getMemory());
	            totalBandwidth =totalBandwidth.add(host.getBandwidth());
	            totalDisk = totalDisk.add(host.getDataDisk());
	        }
            if(totalDisk.compareTo(new BigInteger("1099511627776"))<0){
                tenant.setUseLevel(totalCpu+"核/"+CapacityUtil.toGBValue(totalMemory, 0)+"GB/"+CapacityUtil.toGBValue(totalDisk, 0)+"GB");
            }else{
                tenant.setUseLevel(totalCpu+"核/"+CapacityUtil.toGBValue(totalMemory, 0)+"GB/"+CapacityUtil.toTB(totalDisk, 0));
            }		}
		return list;
	}

	@Override
    @Transactional(readOnly=false)
	public MethodResult addSysTenant(SysTenant tenant,
			HttpServletRequest request) {
		SysTenantMapper mapper= this.sqlSession.getMapper(SysTenantMapper.class);	
		tenant.setId(StringUtil.generateUUID());
		tenant.setStatus(AppConstant.TENANT_STATUS_ENABLE);
		tenant.setMem(CapacityUtil.fromCapacityLabel(tenant.getMem()+"GB")); 
		tenant.setDisk(CapacityUtil.fromCapacityLabel(tenant.getDisk()+"TB")); 
		mapper.insert(tenant);
		// TODO Auto-generated method stub
		logger.debug("添加租户"+tenant.getId()+"成功！");
		return new MethodResult(MethodResult.SUCCESS, "添加成功");
	}

	@Override
    @Transactional(readOnly=false)
	public SysTenant getSysTenantById(String id) {
		SysTenantMapper mapper= this.sqlSession.getMapper(SysTenantMapper.class);
		return mapper.selectByPrimaryKey(id);
	}

	@Override
    @Transactional(readOnly=false)
	public MethodResult modifyTenant(SysTenant tenant, HttpServletRequest request) {
		SysTenantMapper mapper= this.sqlSession.getMapper(SysTenantMapper.class);
		tenant.setMem(CapacityUtil.fromCapacityLabel(tenant.getMem()+"GB")); 
        tenant.setDisk(CapacityUtil.fromCapacityLabel(tenant.getDisk()+"TB")); 
		mapper.updateByPrimaryKeySelective(tenant);
		logger.debug("修改租户"+tenant.getId()+"成功！");
		return new MethodResult(MethodResult.SUCCESS, "修改成功");
	}

	@Override
	public MethodResult delTenant(String ids) {
		SysTenantMapper mapper= this.sqlSession.getMapper(SysTenantMapper.class);
		String[] uids = ids.split(",");
		for (String id : uids){
			mapper.deleteByPrimaryKey(id);
			logger.debug("修改租户"+id+"成功！");
		}
		return new MethodResult(MethodResult.SUCCESS, "修改成功");
	}

    @Override
    public List<SysTenant> getAllSysTenantByUserId(String userId) {
        SysTenantMapper mapper= this.sqlSession.getMapper(SysTenantMapper.class);
        CloudHostMapper cloudHostMapper= this.sqlSession.getMapper(CloudHostMapper.class); 
        List<SysTenant> list = mapper.qureyTenantByUserId(userId);
        for(SysTenant tenant: list){
            List<CloudHostVO> hostlist = cloudHostMapper.getCloudHostInTenant(tenant.getId());
            BigInteger totalMemory = BigInteger.ZERO;
            Integer totalCpu = 0;
            BigInteger totalBandwidth = BigInteger.ZERO;
            BigInteger totalDisk = BigInteger.ZERO;
            for(CloudHostVO host : hostlist){
                totalCpu = totalCpu+ host.getCpuCore();
                totalMemory = totalMemory.add(host.getMemory());
                totalBandwidth =totalBandwidth.add(host.getBandwidth());
                totalDisk = totalDisk.add(host.getDataDisk());
            }
            if(totalDisk.compareTo(new BigInteger("1099511627776"))<0){
                tenant.setUseLevel(totalCpu+"核/"+CapacityUtil.toGBValue(totalMemory, 0)+"GB/"+CapacityUtil.toGBValue(totalDisk, 0)+"GB");
            }else{
                tenant.setUseLevel(totalCpu+"核/"+CapacityUtil.toGBValue(totalMemory, 0)+"GB/"+CapacityUtil.toTB(totalDisk, 0));
            }
        }
        return list;

    }


}
