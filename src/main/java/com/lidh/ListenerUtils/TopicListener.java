package com.lidh.ListenerUtils;

import com.lidh.dao.MessageMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
public class TopicListener implements MessageListener {
    private final static String SOLR_URL = "http://192.168.199.213:8080/solr";
    @Autowired
    private MessageMapper mapper;

    public void onMessage(Message message) {
        // 加载spring配置文件
        ApplicationContext  applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
        // 导入需要测试的
        mapper = applicationContext.getBean(MessageMapper.class);

        TextMessage textMessage = (TextMessage) message;
        //获取商品id
        try {
            String ItemId = textMessage.getText();
            System.out.println(ItemId);
            //睡眠1s钟，等事务完全执行完后才能查询到商品
            Thread.sleep(1000);
            //从数据库中查询该商品并添加到索引库
            com.lidh.entity.Message result = mapper.selectByPrimaryKey(Integer.parseInt(ItemId));
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", result.getId());
            document.addField("name", result.getCommand());
            HttpSolrClient solr = new HttpSolrClient.Builder(SOLR_URL + "/collection1").withConnectionTimeout(10000).withSocketTimeout(60000).build();
            solr.add(document);
            solr.commit();
            solr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
