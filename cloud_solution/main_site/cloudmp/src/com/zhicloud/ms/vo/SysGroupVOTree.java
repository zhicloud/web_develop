package com.zhicloud.ms.vo;

import java.util.List;

import com.zhicloud.ms.util.Tree;
import com.zhicloud.ms.util.TreeNode;

/**
 * 
 * ClassName: SysGroupVOTree 
 * Function: 定义SysGroupVO树. 
 * date: Apr 3, 2015 9:04:11 AM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */

public class SysGroupVOTree extends Tree {
	
	private static SysGroupVOTree instance = null;
	
	private SysGroupVOTree() {

	}

	public static synchronized SysGroupVOTree getInstance(List<SysGroupVO> params) {
		instance = new SysGroupVOTree();
		instance.reloadSysGroupVOTree(params);
		
		return instance;
	}

	@Override
	protected TreeNode transform(Object info) {
		SysGroupVO sysGroupVO = (SysGroupVO) info;
		TreeNode node = new TreeNode();
		node.setNodeId(sysGroupVO.getId());
		node.setParentId(sysGroupVO.getParentId());
		node.setBindData(sysGroupVO);
		return node;
	}

	public void reloadSysGroupVOTree(List<SysGroupVO> params) {
		List<SysGroupVO> nodes = params;
		super.reload(nodes);
	}

	public SysGroupVO getSysGroupVONode(String groupId) {
		TreeNode node = super.getTreeNode(groupId);
		return node == null ? null : (SysGroupVO) node.getBindData();
	}

}
