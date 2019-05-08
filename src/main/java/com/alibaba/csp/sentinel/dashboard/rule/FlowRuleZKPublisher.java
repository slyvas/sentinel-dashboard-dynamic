package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.client.ZookeeperClient;
import com.alibaba.csp.sentinel.dashboard.config.ZookeeperConfigUtils;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;

@Component("flowRuleZKPublisher")
public class FlowRuleZKPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {

	@Autowired
	private Converter<List<FlowRuleEntity>, String> converter;

	@Override
	public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
		AssertUtil.notEmpty(app, "app name cannot be empty");
		if (rules == null) {
			return;
		}
		String zookeeper = System.getProperty(ZookeeperConfigUtils.ZOOKEEPER_SERVER);
		ZookeeperClient zookeeperClient = new ZookeeperClient(zookeeper,
				ZookeeperConfigUtils.FLOW_RULE_GROUP_ID_POSTFIX, app);
		zookeeperClient.write(converter.convert(rules));
	}
}