package com.shimh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.*;
import com.shimh.service.ArticleService;
import com.shimh.service.TagService;
import com.shimh.vo.ArticleVo;
import com.shimh.vo.PageVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerTest extends BlogApiApplicationTests {

    @Autowired
    private ArticleController articleController;
    @Autowired
    private LoginController loginController;
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
        PageVo pageVo = new PageVo();
        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/articleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));
            String lineTxt = null;
            out.write("页码,页面大小,排序依据,排序方式,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                String[] names = lineTxt.split(",");
                pageVo.setPageNumber(parseInt(names[0]));
                pageVo.setPageSize(parseInt(names[1]));
                pageVo.setName(names[2]);
                pageVo.setSort(names[3]);
                MvcResult result = mockMvc.perform(get("/articles/").contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(pageVo)))
                        .andReturn();// 返回执行请求的结果
                JSONObject r= JSON.parseObject(result.getResponse().getContentAsString());
                out.write(lineTxt+","+r.getString("data"));
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
            out.newLine();
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
            out.newLine();
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
                MvcResult result = mockMvc.perform(get("/articles/"+names[0]).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(names[0])))
                        .andReturn();// 返回执行请求的结果
                JSONObject r= JSON.parseObject(result.getResponse().getContentAsString());
                out.write(lineTxt+","+r.getString("code"));
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
                MvcResult result = mockMvc.perform(get("/articles/tag/"+names[0]).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(names[0])))
                        .andReturn();// 返回执行请求的结果
                JSONObject r= JSON.parseObject(result.getResponse().getContentAsString());
                out.write(lineTxt+","+r.getString("data"));
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
                MvcResult result = mockMvc.perform(get("/articles/category/"+names[0]).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(names[0])))
                        .andReturn();// 返回执行请求的结果
                JSONObject r= JSON.parseObject(result.getResponse().getContentAsString());
                out.write(lineTxt+","+r.getString("code"));
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


    /**
     * 保存文章
     * 根据正交法，12个测试用例，分别考察6个变量：是否登录 t/f，是否有title t/f,是否有body t/f，
     * 是否有summary t/f, 是否选了tag t/f， 是否选了category t/f
*/
    @Rollback
    @Transactional
    @Test
    public void saveArticle() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");

        ArticleBody body=new ArticleBody();
        body.setContent("test");

        String content="test";

        Tag tag=new Tag();
        tag.setId(1);
        List<Tag> tags=new ArrayList<>();
        tags.add(tag);

        Category category=new Category();
        category.setId(1);

        try{
            File inputFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/saveArticleTest.txt");
            File outFile = ResourceUtils.getFile("classpath:unitTest/ArticleControllerTest/saveArticleTestRes.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                    StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
                    StandardCharsets.UTF_8));

            String lineTxt = null;
            out.write("登录,标题,内容,简介,标签,分类,预期,输出");
            out.newLine();
            while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
                Article article = new Article();
                String[] names = lineTxt.split(",");
                if(names[0].equals("t")){
                    loginController.login(user);
                    article.setAuthor(user);
                }
                if(names[1].equals("t")){
                    article.setTitle(content);
                }
                else if(names[1].equals("f")){
                    article.setTitle(null);
                }
                if(names[2].equals("t")){
                    article.setBody(body);
                }
                else if(names[2].equals("f")){
                    article.setBody(null);
                }
                if(names[3].equals("t")){
                    article.setSummary(content);
                }
                else if(names[3].equals("f")){
                    article.setSummary(null);
                }
                if(names[4].equals("t")){
                    article.setTags(tags);
                }
                else if(names[4].equals("f")){
                    article.setTags(null);
                }
                if(names[5].equals("t")){
                    article.setCategory(category);
                }
                else if(names[5].equals("f")){
                    article.setCategory(null);
                }
                try {
                    MvcResult result = mockMvc.perform(post("/articles/publish").contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(article)))
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                            .andReturn();
                    JSONObject r= JSON.parseObject(result.getResponse().getContentAsString());
                    out.write(lineTxt + "," + r.getString("code"));
                    out.newLine();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                loginController.logout();
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
                MvcResult result = mockMvc.perform(get("/articles/delete/"+names[0]).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(names[0])))
                        .andReturn();// 返回执行请求的结果
                JSONObject r= JSON.parseObject(result.getResponse().getContentAsString());
                out.write(lineTxt+","+r.getString("data"));
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
            out.newLine();
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