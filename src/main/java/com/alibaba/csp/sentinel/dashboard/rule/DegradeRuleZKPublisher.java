package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.client.ZookeeperClient;
import com.alibaba.csp.sentinel.dashboard.config.ZookeeperConfigUtils;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;

@Component("degradeRuleZKPublisher")
public class DegradeRuleZKPublisher implements DynamicRulePublisher<List<DegradeRuleEntity>> {

	@Autowired
	private Converter<List<DegradeRuleEntity>, String> converter;

	@Override
	public void publish(String app, List<DegradeRuleEntity> rules) throws Exception {
		AssertUtil.notEmpty(app, "app name cannot be empty");
		if (rules == null) {
			return;
		}

		String zookeeper = System.getProperty(ZookeeperConfigUtils.ZOOKEEPER_SERVER);
		ZookeeperClient zookeeperClient = new ZookeeperClient(zookeeper,
				ZookeeperConfigUtils.DEGRADE_RULE_GROUP_ID_POSTFIX, app);
		zookeeperClient.write(converter.convert(rules));

	}
}
