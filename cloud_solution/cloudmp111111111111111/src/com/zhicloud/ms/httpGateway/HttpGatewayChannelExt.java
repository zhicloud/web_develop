package com.zhicloud.ms.httpGateway;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class HttpGatewayChannelExt extends HttpGatewayChannel {

	public static final Logger logger = Logger.getLogger(HttpGatewayChannelExt.class);

	public HttpGatewayChannelExt(int region) {
		super(region);
	}

	@SuppressWarnings("unchecked")
	public JSONObject getDefaultComputePool() throws MalformedURLException, IOException {
		JSONObject result = this.computePoolQuery();

		if (HttpGatewayResponseHelper.isSuccess(result)) {
			List<JSONObject> computePools = (List<JSONObject>) result.get("compute_pools");
			for (JSONObject computePool : computePools) {
				if ("default".equals(computePool.get("name"))) {
					return computePool;
				}
			}
		}

		logger.warn("HttpGatewayChannelExt.getDefaultComputePool() > [" + Thread.currentThread().getId() + "] fail to get default compute pool, message:[" + HttpGatewayResponseHelper.getMessage(result) + "]");
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getNodeClients() throws MalformedURLException, IOException {
		JSONObject json = this.serviceQuery(NodeTypeDefine.node_client, "default");
		return json;
	}

	public JSONObject getDefaultServerRack(String room) throws MalformedURLException, IOException {
		JSONObject result = this.serverRackQuery(room);

		if (HttpGatewayResponseHelper.isSuccess(result)) {
			List<JSONObject> serverRacks = (List<JSONObject>) result.get("server_racks");
			if (serverRacks != null) {
				for (JSONObject rack : serverRacks) {
					if ("default".equalsIgnoreCase(rack.getString("name"))) {
						return rack;
					}
				}
			}
		}

		logger.debug("found no default server rack in server room[" + room + "]");
		return null;
	}

	public JSONObject getDefaultServerRackFromDefaultServerRoom() throws MalformedURLException, IOException {
		JSONObject result = this.serverRoomQuery();

		if (HttpGatewayResponseHelper.isSuccess(result)) {
			List<JSONObject> serverRooms = (List<JSONObject>) result.get("server_rooms");
			if (serverRooms == null) {// 兼容低版本http_gateway
				serverRooms = (List<JSONObject>) result.get("count");
			}
			if (serverRooms != null) {
				for (JSONObject room : serverRooms) {
					if ("default".equalsIgnoreCase(room.getString("name"))) {
						return this.getDefaultServerRack(room.getString("uuid"));
					}
				}
			}
		}

		logger.debug("found no default server room");
		return null;
	}

}
