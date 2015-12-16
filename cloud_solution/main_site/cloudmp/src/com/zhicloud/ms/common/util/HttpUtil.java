package com.zhicloud.ms.common.util;

import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

	public static <T> T send(String url, String method, Map<String, String> requestProperties, byte[] postData, InputStreamHandler<T> inHandler) throws MalformedURLException, IOException {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();

			// set method
			conn.setRequestMethod(method);

			// conn.setConnectTimeout(5000);

			// conn.setReadTimeout(1000*60);

			// set request properties
			if (requestProperties != null) {
				for (Entry<String, String> entry : requestProperties.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			conn.setDoInput(true);

			if (postData != null) {
				conn.setDoOutput(true);
				conn.getOutputStream().write(postData);
			}

			return inHandler.handle(conn.getInputStream(), conn);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	// ---------------

	public static byte[] get(String url) throws MalformedURLException, IOException {
		return send(url, "GET", null, null, new DefaultInputStreamHandler());
	}

	// ---------

	public static byte[] post(String url, byte[] postData) throws MalformedURLException, IOException {
		return send(url, "POST", null, postData, new DefaultInputStreamHandler());
	}

	public static <T> T post(String url, byte[] postData, InputStreamHandler<T> inHandler) throws MalformedURLException, IOException {
		return send(url, "POST", null, postData, inHandler);
	}

	public static <T> T post(String url, byte[] postData, Map<String, String> requestProperties, InputStreamHandler<T> inHandler) throws MalformedURLException, IOException {
		return send(url, "POST", requestProperties, postData, inHandler);
	}

	// ---------------

	public static interface InputStreamHandler<T> {
		public T handle(InputStream in, HttpURLConnection conn) throws IOException;
	}

	public static class DefaultInputStreamHandler implements InputStreamHandler<byte[]> {
		public byte[] handle(InputStream in, HttpURLConnection conn) throws IOException {
			return StreamUtil.inputStreamToByteArray(in);
		}
	}

	public static <T> T fileUpload(String url, String param, String fileName, InputStream fileStream, InputStreamHandler<T> inHandler) throws MalformedURLException, IOException {
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------Java_client"; // boundary就是request头和上传文件内容的分隔符

		try {
			conn = (HttpURLConnection) new URL(url).openConnection();

			// set method
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// set request properties
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			// output & input stream
			BufferedOutputStream output = new BufferedOutputStream(conn.getOutputStream());
			BufferedInputStream input = new BufferedInputStream(fileStream);

			// set field
			StringBuffer contentBody = new StringBuffer();
			contentBody.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			contentBody.append("Content-Disposition: form-data; name=\"")
			.append(param)
			.append("\";filename=\"")
			.append(fileName)
			.append("\"\r\n")
			.append("Content-Type:application/octet-stream")
			.append("\r\n\r\n");
			output.write(contentBody.toString().getBytes());

			// file data
			byte[] data = new byte[1024];// 1K
			int result = 0;
			while ((result = input.read(data)) != -1) {
				output.write(data, 0, result);
				output.flush();
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			output.write(endData);

			output.close();

			// input
			return inHandler.handle(conn.getInputStream(), conn);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

    /**
     *
     * @Title: isIpAddr
     * @Description: 判断是否是外网ip请求，true：innerIP false：outerIp
     * @param @param request
     * @param @return
     * @return boolean
     * @throws
     */
    public static boolean isIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress =  request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }

        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ipAddress);
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        //10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        //172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        //192.168.x.x/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }


    // ----------------

	public static void main(String[] args) throws IOException {
		// http://192.168.66.202:8000/Zeus/gateway/api/connection?command=establish&verify_method=M1&version=1.0
		// http://192.168.66.202:8000/Zeus/gateway/api/isoImage?command=query&session_id=7c096804-2e68-4a83-afaf-b4255fbcc44a&
		// String url = "http://localhost:8080/webtest/a.jsp";
		String url = "http://172.18.10.18:9080/image_usage";

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      // optional default is GET
      con.setRequestMethod("GET");

      //add request header
//      con.setRequestProperty("User-Agent", USER_AGENT);

      int responseCode = con.getResponseCode();

      System.err.println(responseCode);


//      BufferedReader in = new BufferedReader(
//          new InputStreamReader(con.getInputStream()));
//      String inputLine;
//      StringBuffer response = new StringBuffer();
//
//      while ((inputLine = in.readLine()) != null) {
//          response.append(inputLine);
//      }
//      in.close();
//
//      //print result
//      System.out.println(response.toString());



  }
}
