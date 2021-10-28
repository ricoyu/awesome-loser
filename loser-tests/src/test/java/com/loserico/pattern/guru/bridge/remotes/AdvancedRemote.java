package com.loserico.pattern.guru.bridge.remotes;

import com.loserico.pattern.guru.bridge.devices.Device;

/**
 * 这是高级遥控器 
 * <p>
 * Copyright: Copyright (c) 2021-10-19 17:07
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class AdvancedRemote extends BasicRemote {
	
	public AdvancedRemote(Device device) {
		super.device = device;
	}
	
	public void mute() {
		System.out.println("Remote: mute");
		device.setVolume(0);
	}
}
