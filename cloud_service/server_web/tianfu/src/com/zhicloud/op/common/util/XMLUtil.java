package com.zhicloud.op.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.zhicloud.op.agentapi.vo.Result;

public class XMLUtil
{

	/**
	 * 从一个文件中读取xml
	 * 
	 * @param f
	 * @return
	 */
	public static Document readXmlFromFile(File f) throws DocumentException, IOException
	{
		FileInputStream fis = null;
		Document doc = null;
		try
		{
			fis = new FileInputStream(f);
			// 解释xml
			SAXReader reader = new SAXReader();
			doc = reader.read(fis);
		}
		finally
		{
			if( fis != null )
			{
				fis.close();
			}
		}
		return doc;
	}
	
	/**
	 * 将一个xml写入一个文件里面
	 * 
	 * @param f
	 *            : 要写入的文件
	 * @param doc
	 *            : 要写入的xml
	 * @param encoding
	 *            : 字符编码
	 */
	public static void writeXmlToFile(File f, Document doc, String encoding, String indent, boolean newLines) throws IOException
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(f);
			XMLWriter xmlWriter = new XMLWriter(fos, new OutputFormat(indent, newLines, encoding));
			xmlWriter.write(doc);
		}
		finally
		{
			if( fos != null )
			{
				fos.close();
			}
		}
	}
	/**
	 * 将java对象转换成xml
	 * 
	 * @param f
	 *            : 要写入的文件
	 * @param doc
	 *            : 要写入的xml
	 * @param encoding
	 *            : 字符编码
	 */
	public static String javaToXml(Object object) throws IOException
	{ 
		try
		{
			JAXBContext context = JAXBContext.newInstance(object.getClass());  
            Marshaller mar = context.createMarshaller();  
				mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");             
            StringWriter writer = new StringWriter();  
            mar.marshal(object, writer);
            return writer.toString().replace("standalone=\"yes\"", "");
		}catch(Exception e){
			e.printStackTrace();
		} 
		return "";
	}

	/**
	 * 
	 */
	public static void writeXmlToFile(File f, Document doc, String encoding) throws IOException
	{
		writeXmlToFile(f, doc, encoding, "", false);
	}

	/**
	 * 
	 */
	public static void writeXmlToFile(File f, Document doc) throws IOException
	{
		writeXmlToFile(f, doc, "utf-8", "", false);
	}
	
	public  static void main(){
		 Result result = new Result();
         result.setStatus("failure");
         result.setErrorCode("1"); 
         result.setMessage("无法找到账号信息"); 
		try {
			javaToXml(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
