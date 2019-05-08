package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.client.ZookeeperClient;
import com.alibaba.csp.sentinel.dashboard.config.ZookeeperConfigUtils;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;

@Component("flowRuleZKProvider")
public class FlowRuleZKProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {

	@Autowired
	private Converter<String, List<FlowRuleEntity>> converter;

	@Override
	public List<FlowRuleEntity> getRules(String appName) throws Exception {

		// zookeeper地址
		String zookeeper = System.getProperty(ZookeeperConfigUtils.ZOOKEEPER_SERVER);
		// zookeeper访问path=/${groupId}/${dataId}
		ZookeeperClient zookeeperClient = new ZookeeperClient(zookeeper,
				ZookeeperConfigUtils.FLOW_RULE_GROUP_ID_POSTFIX, appName);

		String rules = zookeeperClient.read();
		if (StringUtil.isEmpty(rules)) {
			return new ArrayList<>();
		}
		return converter.convert(rules);
	}
}