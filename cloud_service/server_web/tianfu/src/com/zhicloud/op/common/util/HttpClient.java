/**
 * Project Name:tianfu
 * File Name:HttpClient.java
 * Package Name:com.zhicloud.op.common.util
 * Date:May 5, 20156:35:41 PM
 * 
 *
*/

package com.zhicloud.op.common.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.zhicloud.op.service.constant.AppConstant;

/**
 * ClassName:HttpClient 
 * Date:     May 5, 2015 6:35:41 PM 
 * @author   sean
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class HttpClient {
	
	public static String invoker(Map<String, Object> params, String request) {
		
		try {
			URL url = new URL(request);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod(AppConstant.METHOD_POST);
			connection.setRequestProperty("Charset", AppConstant.CHARSET);
			connection.setDoOutput(true);

			PrintWriter out = new PrintWriter(connection.getOutputStream());
			Iterator<Entry<String, Object>> it = params.entrySet().iterator(); 
			while(it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) it.next(); 
			    String key = (String) entry.getKey(); 
			    String val = (String) entry.getValue();
			    
			    if(it.hasNext()) {
			    	out.print(key+"="+URLEncoder.encode(val,AppConstant.CHARSET) + "&");
			    } else {
			    	out.print(key+"="+URLEncoder.encode(val,AppConstant.CHARSET));
			    }
			}
			out.flush();
			out.close();
			InputStream inStream = connection.getInputStream();
			@SuppressWarnings("resource")
			Scanner in = new Scanner(inStream);
			while (in.hasNext()) {
				String line = in.nextLine();
				return line;
			}
			return "fail";
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}

	}
}

