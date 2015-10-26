package com.zhicloud.op.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

	public static <T> T send(String url, String method, Map<String, String> requestProperties, byte[] postData, InputStreamHandler<T> inHandler) throws MalformedURLException, IOException {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();

			// set method
			conn.setRequestMethod(method);

			conn.setConnectTimeout(5000);

			conn.setReadTimeout(1000*60);

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

	// ------------------

	public static void main(String[] args) throws MalformedURLException, IOException {
		// http://192.168.66.202:8000/Zeus/gateway/api/connection?command=establish&verify_method=M1&version=1.0
		// http://192.168.66.202:8000/Zeus/gateway/api/isoImage?command=query&session_id=7c096804-2e68-4a83-afaf-b4255fbcc44a&
		// String url = "http://localhost:8080/webtest/a.jsp";
		String url = "http://192.168.66.202:8000/Zeus/gateway/api/isoImage?command=query&session_id=7c096804-2e68-4a83-afaf-b4255fbcc44a&";
		byte[] bytes = post(url, null);
		System.out.println(new String(bytes));
	}
}
