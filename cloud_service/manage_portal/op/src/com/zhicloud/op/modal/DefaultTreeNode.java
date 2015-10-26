package com.zhicloud.op.modal;

import java.util.LinkedList;
import java.util.List;

import com.zhicloud.op.common.util.json.JSONBean;

public class DefaultTreeNode implements JSONBean
{
	
	private String id;
	private String text;
	private boolean checked = false;
	private List<DefaultTreeNode> children = new LinkedList<DefaultTreeNode>();

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	public List<DefaultTreeNode> getChildren()
	{
		return children;
	}

	public void setChildren(List<DefaultTreeNode> children)
	{
		this.children = children;
	}
}