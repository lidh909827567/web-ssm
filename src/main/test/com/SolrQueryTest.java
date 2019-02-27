package com;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lidhk
 * @Title: SolrQueryTest
 * @ProjectName web-ssm
 * @date 2019/2/26 16:21
 * @Description: TODO
 */
public class SolrQueryTest {
//    private final static String SOLR_URL = "http://192.168.199.213:8080/solr";

    @Autowired
    private HttpSolrClient server;

//    @Before
//    public void init() {
//        // 创建 server
//        server = new HttpSolrClient.Builder(SOLR_URL + "/collection1").withConnectionTimeout(10000).withSocketTimeout(60000).build();
//    }

    /**
     * 查询全部
     */
    @Test
    public void testQuery() {
        String queryStr = "*:*";
        SolrQuery params = new SolrQuery(queryStr);
        params.set("rows", 10);
        try {
            QueryResponse response = null;
            response = server.query(params);
            SolrDocumentList list = response.getResults();
            System.out.println("####### 总共 ： " + list.getNumFound() + "条记录");
            for (SolrDocument doc : list) {
                System.out.println("####### id : " + doc.get("id") + "  name : " + doc.get("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询
     */
    @Test
    public void testQueryCondition() {
        String queryStr = "吃饭";
        SolrQuery query = new SolrQuery(queryStr);
        query.set("rows", 10);
        query.setHighlight(true);
        query.addHighlightField("name");// 高亮字段
        //设置高亮的样式
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        try {
            QueryResponse response = null;
            response = server.query(query);
            SolrDocumentList list = response.getResults();
            System.out.println("####### 总共 ： " + list.getNumFound() + "条记录");
            for (SolrDocument doc : list) {
                System.out.println("####### id : " + doc.get("id") + "  name : " + doc.get("name"));
            }

            NamedList namedList = (NamedList) response.getResponse().get("highlighting");
            for (int i = 0; i < namedList.size(); i++) {
                System.out.println("id=" + namedList.getName(i) + "文档中高亮显示的字段：" + namedList.getVal(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
