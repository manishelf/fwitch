package com.fwitch.backend.message;

import java.nio.ByteBuffer;

import org.springframework.web.socket.WebSocketMessage;

public class Message implements WebSocketMessage<ByteBuffer> {
	
	ByteBuffer payload;
	
	public Message(ByteBuffer payload) {
		this.payload = payload;
	}

	@Override
	public int getPayloadLength() {
		return getPayload().remaining();
	}
	
	@Override
	public ByteBuffer getPayload() {
		return this.payload;
	}

	@Override
	public boolean isLast() {
		return true;
	}


}
