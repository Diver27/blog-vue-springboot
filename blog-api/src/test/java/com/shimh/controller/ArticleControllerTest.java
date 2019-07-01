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
            out.write("年,月,标签id,分类id,浏览量,页码,单页大小,排序依据,排序方式,预期,输出");
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
        try {
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/hotArticleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/hotArticleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            out.write("预期,输出");
            Result result=articleController.listHotArticles();
            out.write(in.readLine()+","+result.getCode());
            out.flush();
            out.close();
            in.close();
        }
        catch (Exception e){
            System.out.println("测试失败");
        }
    }

    @Test
    public void listNewArticles() {
        try {
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/newArticleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/newArticleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            out.write("预期,输出");
            Result result=articleController.listNewArticles();
            out.write(in.readLine()+","+result.getCode());
            out.flush();
            out.close();
            in.close();
        }
        catch (Exception e){
            System.out.println("测试失败");
        }
    }

    @Test
    public void getArticleById() {
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleIdTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleIdTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("文章id,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                Result result=articleController.getArticleById(parseInt(names[0]));
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
    public void getArticleAndAddViews() {
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleViewsTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleViewsTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("文章id,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                Result result=articleController.getArticleAndAddViews(parseInt(names[0]));
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
    public void listArticlesByTag() {
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleTagTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleTagTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("标签id,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                Result result=articleController.listArticlesByTag(parseInt(names[0]));
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
    public void listArticlesByCategory() {
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleCategoryTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleCategoryTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("种类id,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                Result result=articleController.listArticlesByCategory(parseInt(names[0]));
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
    public void saveArticle() {
        ArticleVo article = new ArticleVo();
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/saveArticleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/saveArticleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("年,月,标签id,分类id,浏览量,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                article.setYear(parseInt(names[0]));
                article.setMonth(parseInt(names[1]));
                article.setTagId(parseInt(names[2]));
                article.setCategoryId(parseInt(names[3]));
                article.setCount(parseInt(names[4]));
                Result result=articleController.saveArticle(article);
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
    public void updateArticle() {
        ArticleVo article = new ArticleVo();
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/updateArticleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/updateArticleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("年,月,标签id,分类id,浏览量,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                article.setYear(parseInt(names[0]));
                article.setMonth(parseInt(names[1]));
                article.setTagId(parseInt(names[2]));
                article.setCategoryId(parseInt(names[3]));
                article.setCount(parseInt(names[4]));
                Result result=articleController.updateArticle(article);
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
    public void deleteArticleById() {
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/deleteArticleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/deleteArticleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("文章id,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                Result result=articleController.deleteArticleById(parseInt(names[0]));
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
    public void listArchives() {
        try {
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/archivesTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/archivesTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            out.write("预期,输出");
            Result result=articleController.listArchives();
            out.write(in.readLine()+","+result.getCode());
            out.flush();
            out.close();
            in.close();
        }
        catch (Exception e){
            System.out.println("测试失败");
        }
    }
}