package com.lidh.controller;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lidhk
 * @Title: ItemController
 * @ProjectName web-ssm
 * @date 2019/2/26 10:51
 * @Description: TODO
 */
@Controller
@RequestMapping("/index")
public class ItemController {

    @Autowired
    private HttpSolrClient server;

    @RequestMapping("/query")
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
}