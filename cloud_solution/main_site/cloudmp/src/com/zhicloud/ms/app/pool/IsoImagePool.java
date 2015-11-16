package com.zhicloud.ms.app.pool;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.zhicloud.ms.common.util.StringUtil;

public class IsoImagePool
{
	
	
	private Map<String, IsoImageData> realImageIdMap = new TreeMap<String, IsoImageData>();
	private Map<String, IsoImageData> nameMap        = new TreeMap<String, IsoImageData>();
	
	
	public synchronized void put(IsoImageData isoImageData)
	{
		if( StringUtil.isBlank(isoImageData.getRealImageId())==false )
		{
			realImageIdMap.put(isoImageData.getRealImageId(), isoImageData);
		}
		if( StringUtil.isBlank(isoImageData.getName())==false )
		{
			String key = isoImageData.getRealImageId();
			nameMap.put(key, isoImageData);
		}
	}
	public synchronized void remove(String realImageId)
    { 
	    realImageIdMap.remove(realImageId);
    }
	
	public synchronized IsoImageData getByRealImageId(String realImageId)
	{
		if( StringUtil.isBlank(realImageId) )
		{
			return null;
		}
		IsoImageData isoImageData = realImageIdMap.get(realImageId);
		if( isoImageData==null )
		{
			return null;
		}
		else
		{
			return isoImageData.clone();
		}
	}
	
	public synchronized IsoImageData getByRegionAndName(Integer region, String name)
	{
		if( StringUtil.isBlank(name) )
		{
			return null;
		}
		String key = region+"|"+name;
		IsoImageData isoImageData = nameMap.get(key);
		if( isoImageData==null )
		{
			return null;
		}
		else
		{
			return isoImageData.clone();
		}
	}
	
	public synchronized List<IsoImageData> getAllIsoImageDataByRegion(Integer region)
	{
		List<IsoImageData> result = new ArrayList<IsoImageData>();
		Iterator<IsoImageData> iter = realImageIdMap.values().iterator();
		while( iter.hasNext() )
		{
			IsoImageData isoImageData = iter.next();
			if( isoImageData.getRegion()==region )
			{
				result.add(isoImageData);
			}
		}
		return result;
	}
	
	public synchronized List<IsoImageData> getAllIsoImageData()
    {
        List<IsoImageData> result = new ArrayList<IsoImageData>();
        Iterator<IsoImageData> iter = realImageIdMap.values().iterator();
        while( iter.hasNext() )
        {
            IsoImageData isoImageData = iter.next(); 
            result.add(isoImageData); 
        }
        return result;
    }
	
	public synchronized void clearAll()
    {
	    realImageIdMap.clear();
    }
	
	//------------
	
	/**
	 * 
	 */
	public static class IsoImageData
	{
		private String realImageId;
		private String name;
		private String description;
		private BigInteger size;
		private Integer status; 
		private Integer region;
		
		public String getRealImageId()
		{
			return realImageId;
		}
		public void setRealImageId(String realImageId)
		{
			this.realImageId = realImageId;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getDescription()
		{
			return description;
		}
		public void setDescription(String description)
		{
			this.description = description;
		}
		public BigInteger getSize()
		{
			return size;
		}
		public void setSize(BigInteger size)
		{
			this.size = size;
		}
		public Integer getStatus()
		{
			return status;
		}
		public void setStatus(Integer status)
		{
			this.status = status;
		}
		public Integer getRegion()
		{
			return region;
		}
		public void setRegion(Integer region)
		{
			this.region = region;
		}
		public IsoImageData clone()
		{
			IsoImageData newImage = new IsoImageData();
			newImage.realImageId = this.realImageId;
			newImage.name        = this.name;
			newImage.description = this.description;
			newImage.size        = this.size;
			newImage.status      = this.status;
			newImage.region      = this.region;
			return newImage;
		}
	}
}
