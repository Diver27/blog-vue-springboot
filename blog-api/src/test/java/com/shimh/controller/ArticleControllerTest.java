package com.shimh.controller;

import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.Article;
import com.shimh.entity.User;
import com.shimh.service.ArticleService;
import com.shimh.service.TagService;
import com.shimh.vo.ArticleVo;
import com.shimh.vo.PageVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.*;

public class ArticleControllerTest extends BlogApiApplicationTests {

    @Autowired
    private ArticleController articleController;
    @Autowired
    private WebApplicationContext wac; // 注入WebApplicationContext
    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。
    @Autowired
    private SecurityManager securityManager;

    @Autowired
    private TagController tagController;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        SecurityUtils.setSecurityManager(securityManager);
    }

    @Test
    public void listArticles() {
        ArticleVo article = new ArticleVo();
        PageVo pageVo = new PageVo();
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("年，月，标签id，分类id，浏览量，页码，单页大小，排序依据，排序方式，预期，输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                article.setYear(parseInt(names[0]));
                article.setMonth(parseInt(names[1]));
                article.setTagId(parseInt(names[2]));
                article.setCategoryId(parseInt(names[3]));
                article.setCount(parseInt(names[4]));
                pageVo.setPageNumber(parseInt(names[5]));
                pageVo.setPageSize(parseInt(names[6]));
                pageVo.setName(names[7]);
                pageVo.setSort(names[8]);
                Result result=articleController.listArticles(article,pageVo);
                out.write(lineTxt+","+result.getCode());
                out.newLine();
            }
            out.flush();
            out.close();
            in.close();
        }
        catch (Exception e){
            System.out.println("测试失败");
        }
    }

    @Test
    public void listHotArticles() {
    }

    @Test
    public void listNewArticles() {
    }

    @Test
    public void getArticleById() {
    }

    @Test
    public void getArticleAndAddViews() {
    }

    @Test
    public void listArticlesByTag() {
    }

    @Test
    public void listArticlesByCategory() {
    }

    @Test
    public void saveArticle() {
    }

    @Test
    public void updateArticle() {
    }

    @Test
    public void deleteArticleById() {
    }

    @Test
    public void listArchives() {
    }
}