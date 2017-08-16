package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.Packet;
import com.podts.rpg.client.state.States;

public final class StatePacket extends Packet {
	
	private final States state;
	
	public States getState() {
		return state;
	}
	
	public StatePacket(States state) {
		this.state = state;
	}
	
}
