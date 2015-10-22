package com.zhicloud.op.app.propeties;

import java.util.ArrayList;
import java.util.List;

public class Property
{

	private String key;
	private String name;
	private String type;
	private String description;
	private String format;
	private String text;
	private String value;

	private Select select;

	// ****************

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Select getSelect()
	{
		return select;
	}

	public void setSelect(Select select)
	{
		this.select = select;
	}

	public String getValue()
	{
		return value;
	}

	public void initValue()
	{
		if( "String".equals(this.type) )
		{
			this.value = this.text;
		}
		else if( "select".equals(this.type) )
		{
			if( this.select.getOptionSize() > 0 )
			{
				Option option = this.select.getOptionAt(0);
				this.value = option.getValue();
				this.text = option.getText();
			}
		}
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	// ************************

	/*
	 * class
	 */
	public static class Select
	{
		private List<Option> options = new ArrayList<Option>();

		public void addOption(Option option)
		{
			this.options.add(option);
		}

		public int getOptionSize()
		{
			return this.options.size();
		}

		public Option getOptionAt(int index)
		{
			return this.options.get(index);
		}
	}

	/*
	 * class
	 */
	public static class Option
	{
		private String value;
		private String text;

		public String getValue()
		{
			return value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}

		public String getText()
		{
			return text;
		}

		public void setText(String text)
		{
			this.text = text;
		}
	}

	// *********************
}
