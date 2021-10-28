package com.loserico.pattern.guru.bridge.remotes;

import com.loserico.pattern.guru.bridge.devices.Device;

/**
 * 这是廉价遥控器 
 * <p>
 * Copyright: Copyright (c) 2021-10-19 17:07
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 */
public class BasicRemote implements Remote {
	
	protected Device device;
	
	public BasicRemote() {
	}
	
	public BasicRemote(Device device) {
		this.device = device;
	}
	
	@Override
	public void power() {
		System.out.println("Remote: power toggle");
		if (device.isEnabled()) {
			device.disable();
		} else {
			device.enable();
		}
	}
	
	@Override
	public void volumeDown() {
		System.out.println("Remote: volume down");
		device.setVolume(device.getVolume() - 10);
	}
	
	@Override
	public void volumeUp() {
		System.out.println("Remote: volume up");
		device.setVolume(device.getVolume() + 10);
	}
	
	@Override
	public void channelDown() {
		System.out.println("Remote: channel down");
		device.setChannel(device.getChannel() - 1);
	}
	
	@Override
	public void channelUp() {
		System.out.println("Remote: channel up");
		device.setChannel(device.getChannel() + 1);
	}
}
