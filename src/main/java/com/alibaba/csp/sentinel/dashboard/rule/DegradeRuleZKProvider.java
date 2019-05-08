package com.alibaba.csp.sentinel.dashboard.rule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.client.ZookeeperClient;
import com.alibaba.csp.sentinel.dashboard.config.ZookeeperConfigUtils;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;

@Component("degradeRuleZKProvider")
public class DegradeRuleZKProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {

	@Autowired
	private Converter<String, List<DegradeRuleEntity>> converter;

	@Override
	public List<DegradeRuleEntity> getRules(String appName) throws Exception {

		// zookeeper地址
		String zookeeper = System.getProperty(ZookeeperConfigUtils.ZOOKEEPER_SERVER);
		// zookeeper访问path=/${groupId}/${dataId}
		String groupId = ZookeeperConfigUtils.DEGRADE_RULE_GROUP_ID_POSTFIX;
		ZookeeperClient zookeeperClient = new ZookeeperClient(zookeeper, groupId, appName);

		String rules = zookeeperClient.read();
		if (StringUtil.isEmpty(rules)) {
			return new ArrayList<>();
		}
		return converter.convert(rules);
	}

}
