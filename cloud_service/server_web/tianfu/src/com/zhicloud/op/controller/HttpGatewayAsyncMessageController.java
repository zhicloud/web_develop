package com.zhicloud.op.controller;

import java.io.BufferedOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.httpGateway.HttpGatewayActiveAsyncChannelPool;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncMessageHandlerImpl;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayMessageHandler;

@Controller
public class HttpGatewayAsyncMessageController extends MultiActionController {

	private final static Logger logger = Logger.getLogger(HttpGatewayAsyncMessageController.class);

	private final static HttpGatewayAsyncMessageHandlerImpl hgAsyncMessageHandlerImpl = new HttpGatewayAsyncMessageHandlerImpl();

	private final static Map<String, Method> hgAsyncMessageHandlerMap = new HashMap<String, Method>();

	// 初始化
	static {
		HttpGatewayAsyncMessageController.init();
	}

	private static void init() {
		Method[] methods = HttpGatewayAsyncMessageHandlerImpl.class.getMethods();
		for (Method method : methods) {
			HttpGatewayMessageHandler annotation = method.getAnnotation(HttpGatewayMessageHandler.class);
			if (annotation == null) {
				continue;
			}
			if (StringUtil.isBlank(annotation.messageType())) {
				continue;
			}
			if (hgAsyncMessageHandlerMap.containsKey(annotation.messageType())) {
				logger.error("there is another method annotated with message type [" + annotation.messageType() + "]");
				continue;
			}
			hgAsyncMessageHandlerMap.put(annotation.messageType(), method);
		}

	}

	/**
	 * 通用方法，异步消息回调入口
	 */
	@RequestMapping("/hgMessage/push.do")
	public void push(HttpServletRequest request, HttpServletResponse response) {
		try {
			String sessionId = request.getParameter("session_id");
			if (StringUtil.isBlank(sessionId)) {
				logger.error("[" + Thread.currentThread().getId() + "] receives async message , url=[" + request.getRequestURI() + "], parameter 'sessionId' is blank.");
				return;
			}

			HttpGatewayAsyncChannel channel = this.getChannel(sessionId);
			if (channel == null) {
				logger.error("[" + Thread.currentThread().getId() + "] receives async message , url=[" + request.getRequestURI() + "], can not find the channel.");
				return;
			}
			// 更新channel接收异步消息时间
			channel.updateReceiveTime();
			//消息处理
			JSONObject postData = channel.decryptReceivedData(request);
			String messageType = JSONLibUtil.getString(postData, "message_type");
			// 查找处理方法
			Method method = hgAsyncMessageHandlerMap.get(messageType);
			if (method == null) {
				logger.error("[" + Thread.currentThread().getId() + "] received async message, url=[" + request.getRequestURI() + "], invalid message type [" + messageType + "]");
				return;
			}
			// 进行处理
			logger.debug("[" + Thread.currentThread().getId() + "] received async message, url=[" + request.getRequestURI() + "], message type [" + messageType + "]");

			Object returnValue = method.invoke(hgAsyncMessageHandlerImpl, channel, JSONLibUtil.getJSONObject(postData, "message_data"));
			// 返回值处理
			Type returnType = method.getGenericReturnType();// 获取返回参数类型
			if (returnType instanceof ParameterizedType) {// 带类型参数泛型
				ParameterizedType pType = (ParameterizedType) returnType;
				if (((Class<?>) pType.getRawType()).isAssignableFrom(Map.class)) {// 返回参数类型为Map子类
					Type[] argsType = pType.getActualTypeArguments();
					// 已知确定Map子类只有两个形参，判断两个形参是否都是String的子类
					if (((Class<?>) argsType[0]).isAssignableFrom(String.class) && ((Class<?>) argsType[1]).isAssignableFrom(String.class)) {
						Map<String, String> data = (Map<String, String>) returnValue;
						byte[] encryptData = channel.encrypt(data);// 加密

						// 构造response
						response.setCharacterEncoding("utf-8");
						response.setContentType("text/json/encryption");
						BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
						out.write(encryptData);// 写输出流
						out.flush();
					}
				}
			}
		} catch (InvocationTargetException e) {
			logger.error("", e.getTargetException());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	private HttpGatewayAsyncChannel getChannel(String sessionId) {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getActiveAsyncChannel(sessionId);
		return channel;
	}
	
}
