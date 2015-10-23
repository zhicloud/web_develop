package com.zhicloud.op.httpGateway;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Stack;

public class HttpGatewayAsyncChannelPool {

	private int region = 0;
	private Stack<HttpGatewayAsyncChannel> channelPool = new Stack<HttpGatewayAsyncChannel>();

	public HttpGatewayAsyncChannelPool(int region) {
		this.region = region;
	}

	public synchronized HttpGatewayAsyncChannel getChannel() throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = null;
		if (!channelPool.empty()) {
			channel = channelPool.pop();
		}
		if (channel == null) {
			channel = new HttpGatewayAsyncChannel(this.region);
		}

		channel.checkSessionRefresh();
		HttpGatewayActiveAsyncChannelPool.singleton().saveChannel(channel);

		return channel;
	}

	public synchronized void releaseChannel(HttpGatewayAsyncChannel channel) {
		HttpGatewayActiveAsyncChannelPool.singleton().removeChannel(channel);

		if (this.channelPool.contains(channel) == false) {
			this.channelPool.push(channel);
		}
	}
}
