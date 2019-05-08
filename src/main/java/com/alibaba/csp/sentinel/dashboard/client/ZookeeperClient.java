package com.alibaba.csp.sentinel.dashboard.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClient {

	Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

	private static final int RETRY_TIMES = 3;
	private static final int SLEEP_TIME = 1000;
	private String remoteAddress;
	private String groupId;
	private String dataId;

	public ZookeeperClient(String remoteAddress, String groupId, String dataId) {
		this.remoteAddress = remoteAddress;
		this.groupId = groupId;
		this.dataId = dataId;
	}

	public void write(String rule) {
		try {
			CuratorFramework zkClient = CuratorFrameworkFactory.newClient(remoteAddress,
					new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
			zkClient.start();
			String path = getPath(groupId, dataId);
			Stat stat = zkClient.checkExists().forPath(path);
			if (stat == null) {
				zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,
						null);
			}
			zkClient.setData().forPath(path, rule.getBytes());
			zkClient.getData().forPath(path);
			zkClient.close();
		} catch (Exception e) {
			logger.error("写配置中心zookeeper异常:", e);
		}
	}

	public String read() {
		try {
			CuratorFramework zkClient = CuratorFrameworkFactory.newClient(remoteAddress,
					new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
			zkClient.start();
			String path = getPath(groupId, dataId);
			Stat stat = zkClient.checkExists().forPath(path);
			if (stat == null) {
				zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,
						null);
			}
			byte[] bytes = zkClient.getData().forPath(path);
			zkClient.close();
			if (null != bytes) {
				return new String(bytes, "UTF-8");
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error("读配置中心zookeeper异常:", e);
		}
		return null;
	}

	private static String getPath(String groupId, String dataId) {
		String path = "";
		if (groupId.startsWith("/")) {
			path += groupId;
		} else {
			path += "/" + groupId;
		}
		if (dataId.startsWith("/")) {
			path += dataId;
		} else {
			path += "/" + dataId;
		}
		return path;
	}
}
