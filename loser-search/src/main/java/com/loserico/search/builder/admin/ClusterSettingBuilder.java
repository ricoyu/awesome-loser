package com.loserico.search.builder.admin;

import com.loserico.common.lang.utils.ReflectionUtils;
import com.loserico.search.ElasticUtils;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequestBuilder;
import org.elasticsearch.common.settings.Settings;

/**
 * <p>
 * Copyright: (C), 2021-04-18 8:39
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class ClusterSettingBuilder {
	
	private ClusterPersistentSettingBuilder persistentSettingBuilder;
	
	public ClusterPersistentSettingBuilder persistent() {
		return new ClusterPersistentSettingBuilder(this);
	}
	
	public boolean update() {
		ClusterUpdateSettingsRequestBuilder builder = ElasticUtils.CLIENT.admin().cluster().prepareUpdateSettings();
		if (persistentSettingBuilder != null) {
			Settings persistentSettings = ReflectionUtils.invokeMethod(persistentSettingBuilder, "build");
			builder.setPersistentSettings(persistentSettings);
		}
		
		return builder.get().isAcknowledged();
	}
}
