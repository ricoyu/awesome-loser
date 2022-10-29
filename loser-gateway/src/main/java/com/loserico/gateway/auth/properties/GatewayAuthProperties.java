package com.loserico.gateway.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Data
@ConfigurationProperties("gateway.auth")
public class NotAuthUrlProperties {
	
	private Set<String> shouldSkipUrls;
	
}
