package com.shimh.controller;

import com.alibaba.fastjson.JSONObject;
import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.Article;
import com.shimh.entity.Comment;
import com.shimh.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends BlogApiApplicationTests {

    @Autowired
    private CommentController commentController;

    @Autowired
    private WebApplicationContext wac; // 注入WebApplicationContext
    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。
    @Autowired
    private SecurityManager securityManager;
    @Autowired
    private LoginController loginController;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        SecurityUtils.setSecurityManager(securityManager);
    }

    /**
     * 获取所有评论
     * 输入：/
     * 期望：输出所有评论plain text
     */
    @Test
    public void listComments() {
        try {
            Result result = commentController.listComments();
            List<Comment> list = (List<Comment>) result.getData();
            for (Comment elem : list) {
                System.out.println(elem.getContent());
            }
        }catch(Exception e){
            System.out.println("失败");
        }
    }

    /**
     * 获取一条评论
     * 输入：id为null
     * 期望：返回错误代码1002 “参数为空”
     */
    @Test
    public void getCommentByIdEmpty() {
        Integer id=null;
        Result result=commentController.getCommentById(id);
        System.out.println(result.getCode());
    }

    /**
     * 获取评论
     * 输入：id在表中不存在
     * 期望：返回错误代码
     */
    @Test
    public void getCommentByIdNonExist(){
        Integer id=9999999;
        Result result=commentController.getCommentById(id);
        System.out.println(result.getCode());
        //todo
    }

    /**
     * 获取评论
     * 输入：合法id
     * 期望：返回评论内容
     */
    @Test
    public void getCommentByIdLegit() {
        Integer id=53;
        Result result=commentController.getCommentById(id);
        Comment comment=(Comment)(result.getData());
        System.out.println(comment.getContent());
//        try {
//            MvcResult result = mockMvc.perform(get("/comments/" + id.toString()))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                    .andReturn();
//            System.out.println(result.getResponse().getContentAsString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //TODO WTF??
    }

    /**
     * 获取文章评论
     * 输入：id为null
     * 期望：返回错误代码1002
     */
    @Test
    public void listCommentsByArticleEmpty() {
        Integer id=null;
        Result result=commentController.listCommentsByArticle(id);
        System.out.println(result.getCode());
    }

    /**
     * 获取文章评论
     * 输入：id不存在
     * 期望：返回错误代码
     */
    @Test
    public void listCommentsByArticleNonExist() {
        Integer id=9999;
        Result result=commentController.listCommentsByArticle(id);
        System.out.println(result.getCode());
        //todo
    }

    /**
     * 获取文章评论
     * 输入：合法id
     * 期望：返回评论列表
     */
    @Test
    public void listCommentsByArticleLegit() {
        Integer id=10;
        Result result=commentController.listCommentsByArticle(id);
        List<Comment> list = (List<Comment>) result.getData();
        for (Comment elem : list) {
            System.out.println(elem.getContent());
        }
    }

//    @Rollback
//    @Transactional
//    @Test
//    public void saveComment() {
//    }
//
//    @Rollback
//    @Transactional
//    @Test
//    public void deleteCommentById() {
//    }


    /**
     * 保存评论
     * 根据正交法，4个测试用例，分别考察3个变量：是否登录 t/f，是否有toUser t/f,是否有Parent t/f
     * A:ttt
     * B:tff
     * C:ftf
     * D:tft
     */
    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsA() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");
        loginController.login(user);

        Comment parent=new Comment();
        parent.setId(55);

        Article article=new Article();
        article.setId(10);

        User toUser=new User();
        toUser.setId(26L);

        Comment comment=new Comment();
        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setToUser(toUser);
        comment.setParent(parent);
        comment.setArticle(article);

//        commentController.saveCommentAndChangeCounts(comment);
        try {
            MvcResult result = mockMvc.perform(post("/comments/create/change" ).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(comment)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsB() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");
        loginController.login(user);

        Comment comment=new Comment();

        Article article=new Article();
        article.setId(10);

        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setArticle(article);

//        commentController.saveCommentAndChangeCounts(comment);
        try {
            MvcResult result = mockMvc.perform(post("/comments/create/change" ).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(comment)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsC() {
        Comment comment=new Comment();

        Article article=new Article();
        article.setId(10);

        User toUser=new User();
        toUser.setId(26L);

        comment.setContent("测试");
        comment.setToUser(toUser);
        comment.setArticle(article);

//        commentController.saveCommentAndChangeCounts(comment);
        try {
            MvcResult result = mockMvc.perform(post("/comments/create/change" ).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(comment)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsD() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");
        loginController.login(user);

        Comment parent=new Comment();
        parent.setId(55);

        Comment comment=new Comment();

        Article article=new Article();
        article.setId(10);

        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setParent(parent);
        comment.setArticle(article);

//        commentController.saveCommentAndChangeCounts(comment);
        try {
            MvcResult result = mockMvc.perform(post("/comments/create/change" ).contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(comment)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除评论
     * 输入：评论id为空
     * 期望：错误代码1002
     */
    @Rollback
    @Transactional
    @Test
    public void deleteCommentByIdAndChangeCountsEmpty() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");
        loginController.login(user);

        Integer id=null;
        Result result=commentController.deleteCommentByIdAndChangeCounts(id);
        System.out.println(result.getCode());
    }

    /**
     * 删除评论
     * 输入：评论id不存在
     * 期望：错误代码
     */
    @Rollback
    @Transactional
    @Test
    public void deleteCommentByIdAndChangeCountsNonExist() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");
        loginController.login(user);

        Integer id=9999;
        Result result=commentController.deleteCommentByIdAndChangeCounts(id);
        System.out.println(result.getCode());
    }

    /**
     * 删除评论
     * 输入：有效评论id
     * 期望：成功代码0
     */
    @Rollback
    @Transactional
    @Test
    public void deleteCommentByIdAndChangeCounts() {
        User user=new User();
        user.setAccount("diver28");
        user.setPassword("1234");
        loginController.login(user);

        Integer id=55;
        Result result=commentController.deleteCommentByIdAndChangeCounts(id);
        System.out.println(result.getCode());
    }
}