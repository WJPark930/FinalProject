package com.spring.ex;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import member.model.MemberBean;

@Component
@RequestMapping("/echo")
public class EchoHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
    private static Logger logger = LoggerFactory.getLogger(EchoHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionList.add(session);
        logger.info("{} �����", session.getId());

        MemberBean loginInfo = (MemberBean) session.getAttributes().get("loginInfo");
        if (loginInfo != null) {
            String message = loginInfo.getUser_email() + " ���� �����ϼ̽��ϴ�.";
            broadcastMessage(message);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("{}�� ���� {} ����", session.getId(), message.getPayload());

        MemberBean loginInfo = (MemberBean) session.getAttributes().get("loginInfo");
        if (loginInfo != null) {
            String messageToSend = loginInfo.getUser_email() + ": " + message.getPayload();
            broadcastMessage(messageToSend);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionList.remove(session);
        logger.info("{} ���� ����.", session.getId());

        MemberBean loginInfo = (MemberBean) session.getAttributes().get("loginInfo");
        if (loginInfo != null) {
            String message = loginInfo.getUser_email() + " ���� �����ϼ̽��ϴ�.";
            broadcastMessage(message);
        }
    }

    private void broadcastMessage(String message) throws Exception {
        for (WebSocketSession sess : sessionList) {
            sess.sendMessage(new TextMessage(message));
        }
    }
}

