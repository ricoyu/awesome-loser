package com.loserico.pattern.behavioral.state;

import lombok.Data;

/**
 * <p>
 * Copyright: (C), 2020/1/31 11:05
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Data
public class DeliveryContext {
	
	private PackageState currentState;
	
	private String packageId;
	
	public DeliveryContext(PackageState packageState, String packageId) {
		this.currentState = packageState;
		this.packageId = packageId;
		
		if (this.currentState == null) {
			this.currentState = AcknowledgedState.instance();
		}
	}
	
	public void update() {
		currentState.updateState(this);
	}
}
