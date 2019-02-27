package com;

import com.lidh.dao.MessageMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * @author lidhk
 * @Title: MessageMapperTest
 * @ProjectName web-ssm
 * @date 2019/2/26 9:48
 * @Description: TODO
 */
public class MessageMapperTest {

    private ApplicationContext applicationContext;

    @Autowired
    private MessageMapper mapper;

//    @Autowired
//    private JmsTemplate jmsTemplate;
//
//    @Resource
//    private Destination topicDestination;

    @Before
    public void setUp() throws Exception {
        // 加载spring配置文件
        applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        // 导入需要测试的
        mapper = applicationContext.getBean(MessageMapper.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void insert() throws Exception {
        com.lidh.entity.Message message1 = new com.lidh.entity.Message();
        message1.setId(2);
        message1.setCommand("吃饭");
        message1.setContent("睡觉");
        message1.setDescription("打豆豆");
        int result = mapper.insert(message1);
        System.out.println(result);
        assert (result == 1);
        final Integer id = message1.getId();
        //初始化spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        //从spring容器中获得JmsTemplate对象
        JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        //从spring容器中取Destination对象
        Destination topicDestination = (Destination) applicationContext.getBean("topicDestination");

        //通过activemq发送商品id，让接收端同步到solr索引库中
        jmsTemplate.send(topicDestination, new MessageCreator() {
            //发送消息
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(id.toString());
                return message;
            }
        });
    }
}
