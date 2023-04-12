package com.tech.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.dto.SocketDTO;
import com.tech.exception.AuthException;
import com.tech.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{token}")
@Component
public class WebSocketServer {

    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static final Map<Long, Map<String, Session>> inboxRoomMap = new ConcurrentHashMap<>();

    //
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        try {
            Claims claims = JwtUtils.parseJWT(token);
            String userEmail = claims.getSubject();
            sessionMap.put(userEmail, session);
            System.out.println("A user connected, total number of online users is " + sessionMap.size());
        } catch (Exception e) {
            System.out.println("invalid token");
            return;
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("token") String token) {
        try {
            Claims claims = JwtUtils.parseJWT(token);
            String userEmail = claims.getSubject();
            sessionMap.remove(userEmail);
            System.out.println("a connection closed，removed" + userEmail + "'s session");
        } catch (Exception e) {
            System.out.println("invalid token");
            return;
        }
    }


    @OnMessage
    public void onMessage(@PathParam("token") String token, Session session, String messageObj) {

        try {
            if(messageObj.equals("ping")){
                System.out.println("client ping a message");
                return;
            }
            // deserialize object
            SocketDTO socketObj = new ObjectMapper().readValue(messageObj, SocketDTO.class);
            String actionType = socketObj.getActionType();
            Long roomId = socketObj.getRoomId();
            // parse token
            Claims claims = JwtUtils.parseJWT(token);
            // get email from token
            String userEmail = claims.getSubject();

            // User joins a chat room
            if (actionType.equals("join inbox room")) {
                Map<String, Session> roomUserSessionMap = inboxRoomMap.get(roomId);
                if (Objects.isNull(roomUserSessionMap)) {
                    roomUserSessionMap = new ConcurrentHashMap<>();
                    inboxRoomMap.put(roomId, roomUserSessionMap);
                }
                roomUserSessionMap.put(userEmail, session);
                System.out.println(userEmail + " joined chat room: " + roomId);
            }

            // User sends a message to a chat room
            else if (actionType.equals("send message")) {
                Map<String, Session> roomUserMap = inboxRoomMap.get(roomId);
                if (Objects.isNull(roomUserMap)) return;
                for (String email : roomUserMap.keySet()) {
                    if (email.equals(userEmail)) continue;
                    roomUserMap.get(email).getBasicRemote().sendText(socketObj.getMessage());
                }

            } else if (actionType.equals("leave inbox room")) {
                Map<String, Session> roomUserSessionMap = inboxRoomMap.get(roomId);
                if (Objects.isNull(roomUserSessionMap)) {
                    return;
                }
                roomUserSessionMap.remove(userEmail);
                System.out.println(userEmail + " leave chat room: " + roomId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}

