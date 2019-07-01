package com.shimh.controller;

import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.Comment;
import com.shimh.entity.User;
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
        //todo
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
        Comment comment=(Comment)(result.getData());
        System.out.println(comment.getContent());
        //todo
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
     * 根据正交法，4个测试用例，分别考察3个变量：Author，Level,Parent
     */
    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsA() {
        User user=new User();
        user.setAccount("diver27");
        Comment parent=new Comment();
        parent.setId(55);

        Comment comment=new Comment();

        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setLevel("2");
        comment.setParent(parent);

        commentController.saveCommentAndChangeCounts(comment);
    }

    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsB() {
        User user=new User();
        user.setAccount("diver27");

        Comment comment=new Comment();

        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setLevel("3");

        commentController.saveCommentAndChangeCounts(comment);
    }

    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsC() {
        User user=new User();
        user.setAccount("Non-existent");

        Comment comment=new Comment();

        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setLevel("2");

        commentController.saveCommentAndChangeCounts(comment);

    }

    @Rollback
    @Transactional
    @Test
    public void saveCommentAndChangeCountsD() {
        User user=new User();
        user.setAccount("diver27");
        Comment parent=new Comment();
        parent.setId(55);

        Comment comment=new Comment();

        comment.setContent("测试");
        comment.setAuthor(user);
        comment.setLevel("3");
        comment.setParent(parent);

        commentController.saveCommentAndChangeCounts(comment);
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
        Integer id=9999;
        Result result=commentController.deleteCommentByIdAndChangeCounts(id);
        System.out.println(result.getCode());
    }

    /**
     * 删除评论
     * 输入：有效评论id
     * 期望：成功代码
     */
    @Rollback
    @Transactional
    @Test
    public void deleteCommentByIdAndChangeCounts() {
        Integer id=55;
        Result result=commentController.deleteCommentByIdAndChangeCounts(id);
        System.out.println(result.getCode());
    }
}