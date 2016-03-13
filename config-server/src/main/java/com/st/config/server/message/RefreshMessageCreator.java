package com.st.config.server.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

@Component
public class RefreshMessageCreator {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void createMessage(final String itemKey, final String fieldKey) {
        // Send a message
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage messageMap = session.createMapMessage();
                messageMap.setString("itemKey", itemKey);
                messageMap.setString("fieldKey", fieldKey);
                return messageMap;
            }
        };
       // JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.send("client-refresh", messageCreator);
    }
}
