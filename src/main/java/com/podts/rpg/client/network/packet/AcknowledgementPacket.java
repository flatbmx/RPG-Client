package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.Packet;

public abstract class AcknowledgementPacket extends Packet {
	
	private final int ack;
	
	public final int getACK() {
		return ack;
	}
	
	public AcknowledgementPacket(int ack) {
		this.ack = ack;
	}
	
}
