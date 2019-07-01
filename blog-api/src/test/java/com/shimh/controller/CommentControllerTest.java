package com.shimh.controller;

import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.Comment;
import org.apache.tomcat.jni.Error;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

public class CommentControllerTest extends BlogApiApplicationTests {

    @Autowired
    private CommentController commentController;

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
    }

    /**
     * 获取评论
     * 输入：合法id
     * 期望：返回评论内容
     */
    @Test
    public void getCommentByIdLegit(){
        Integer id=53;
        Result result=commentController.getCommentById(id);
        Comment comment=(Comment)result.getData();
        System.out.println(comment.getContent());
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
    }

    /**
     * 获取文章评论
     * 输入：合法id
     * 期望：返回评论列表
     */
    @Test
    public void listCommentsByArticleLegit() {
    }

    @Rollback
    @Transactional
    @Test
    public void saveComment() {
    }

    @Rollback
    @Transactional
    @Test
    public void deleteCommentById() {
    }

    @Test
    public void saveCommentAndChangeCounts() {
    }

    @Test
    public void deleteCommentByIdAndChangeCounts() {
    }
}