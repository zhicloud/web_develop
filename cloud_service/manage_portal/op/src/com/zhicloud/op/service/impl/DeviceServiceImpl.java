package com.zhicloud.op.service.impl;

import com.zhicloud.op.app.pool.device.DeviceExt;
import com.zhicloud.op.app.pool.device.DevicePool;
import com.zhicloud.op.app.pool.device.DevicePoolManager;
import com.zhicloud.op.service.DeviceService;

public class DeviceServiceImpl implements DeviceService {

	@Override
	public DeviceExt[] getAll() {
		DevicePool pool = DevicePoolManager.singleton().getPool();
		DeviceExt[] deviceList = pool.getAllDuplication();

		return deviceList;
	}

}
