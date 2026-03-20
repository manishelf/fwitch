package com.fwitch.backend.socket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class WebSocketSessionManager {

	private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

	public Set<WebSocketSession> getWebSocketInGroupSessionsExcept(WebSocketSession webSocketSession) {
		var nonMatchingSessions = this.webSocketSessions.stream()
				.filter(x -> !x.getId().equalsIgnoreCase(webSocketSession.getId())).collect(Collectors.toSet());
		return nonMatchingSessions;
	}

	public void addWebSocketToUserGroupSession(WebSocketSession webSocketSession) {
		this.webSocketSessions.add(webSocketSession);
	}

	public void removeWebSocketSession(WebSocketSession webSocketSession) {
		this.webSocketSessions.remove(webSocketSession);
	}

}