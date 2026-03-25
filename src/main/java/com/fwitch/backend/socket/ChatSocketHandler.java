package com.fwitch.backend.socket;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

public class ChatSocketHandler extends BinaryWebSocketHandler {

	WebSocketSessionManager wsManager;

	public ChatSocketHandler(WebSocketSessionManager wsManager) {
		this.wsManager = wsManager;
	}
	
    @Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		wsManager.addWebSocketSession(session);
		super.afterConnectionEstablished(session);
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
		// echo incomming message
		List<WebSocketSession> sessions = wsManager.getWebSocketSessionsExcept(session);
		for(var otherSession : sessions) {
			try {
				otherSession.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		super.handleBinaryMessage(session, message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		wsManager.removeWebSocketSession(session);
		super.afterConnectionClosed(session, status);
	}

}
