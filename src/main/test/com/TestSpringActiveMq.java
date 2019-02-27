package com;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * @author lidhk
 * @Title: TestSpringActiveMq
 * @ProjectName web-ssm
 * @date 2019/2/26 10:27
 * @Description: TODO
 */
public class TestSpringActiveMq {

    @Test
    public void testSpringActiveMq() throws Exception {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //从spring容器中获得JmsTemplate对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //从spring容器中取Destination对象
        Destination destination = (Destination) applicationContext.getBean("queueDestination");
        //使用JmsTemplate对象发送消息。
        jmsTemplate.send(destination, new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                //创建一个消息对象并返回
                TextMessage textMessage = session.createTextMessage("spring activemq queue message");
                return textMessage;
            }
        });
    }
    @Test
    public void testQueueConsumer() throws Exception {
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //等待
        System.in.read();
    }

}
