package com.lidh.ListenerUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author lidhk
 * @Title: QueueListener
 * @ProjectName web-ssm
 * @date 2019/2/26 10:41
 * @Description: TODO
 */
public class QueueListener implements MessageListener {

    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            //取消息内容
            String text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
