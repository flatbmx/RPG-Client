package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.Packet;

public class AcknowledgePacket extends Packet {
	
	private final int ack;
	
	public final int getACK() {
		return ack;
	}
	
	public AcknowledgePacket(int ack) {
		this.ack = ack;
	}
	
	public AcknowledgePacket(AcknowledgementPacket packet) {
		this(packet.getACK());
	}
	
}
