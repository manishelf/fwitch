package com.fwitch.backend.socket;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;


public class MediaSocketHandler extends BinaryWebSocketHandler {

	WebSocketSessionManager wsManager;

	public MediaSocketHandler(WebSocketSessionManager wsManager) {
		this.wsManager = wsManager;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		wsManager.addWebSocketSession(session);
		super.afterConnectionEstablished(session);
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		// TODO Auto-generated method stub
		super.handleBinaryMessage(session, message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		wsManager.removeWebSocketSession(session);
		super.afterConnectionClosed(session, status);
	}

}
