package com.shimh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.User;
import com.shimh.entity.UserStatus;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoginControllerTest extends BlogApiApplicationTests {


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
  public void loginTest() {

    User user = new User();
    try{
      File inputFile = ResourceUtils.getFile("classpath:unitTest/LoginControllerTest/loginTest.csv");
      File outFile = ResourceUtils.getFile("classpath:unitTest/LoginControllerTest/loginTestRes.csv");
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
              StandardCharsets.UTF_8));
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
              StandardCharsets.UTF_8));
      String lineTxt = null;
      out.write("账号,密码,预期,输出");
      out.newLine();
      while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
        String[] names = lineTxt.split(",");
        user.setAccount(names[0]);
        user.setPassword(names[1]);
        MvcResult result=mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(user))).andReturn();
        JSONObject r=JSON.parseObject(result.getResponse().getContentAsString());
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
  @Rollback
  @Transactional
  public void registerTest() {
    try{
      File inputFile = ResourceUtils.getFile("classpath:unitTest/LoginControllerTest/registerTest.csv");
      File outFile = ResourceUtils.getFile("classpath:unitTest/LoginControllerTest/registerTestRes.csv");
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
              StandardCharsets.UTF_8));
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),
              StandardCharsets.UTF_8));
      String lineTxt = null;
      out.write("账号,密码,邮箱,手机号,用户名,预期,输出");
      out.newLine();
      while ((lineTxt = in.readLine()) != null) {//数据以逗号分隔
        User user = new User();
        String[] names = lineTxt.split(",");
        user.setAccount(names[0]);
        user.setPassword(names[1]);
        user.setEmail(names[2]);
        user.setMobilePhoneNumber(names[3]);
        user.setNickname(names[4]);
       // Result result=loginController.register(user);
        MvcResult result = mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(user)))
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
  public void executeLoginTest() {

  }

  @Test
  public void handleLoginTest() {
    try {
      MvcResult result=mockMvc.perform(get("/handleLogin")).andReturn();
      JSONObject r=JSON.parseObject(result.getResponse().getContentAsString());
      System.out.println("第一次返回结果为:"+r.getString("code"));
      User user=new User();
      user.setAccount("twhello");
      user.setPassword("1914");
      mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(JSONObject.toJSONString(user))).andReturn();
      result=mockMvc.perform(get("/handleLogin")).andReturn();
      r=JSON.parseObject(result.getResponse().getContentAsString());
      System.out.println("第二次返回结果为:"+r.getString("code"));
    }
    catch (Exception e){
      System.out.println("测试失败");
    }
  }

  @Test
  public void logoutTest() {
    try {
      MvcResult result = mockMvc.perform(get("/logout")).andReturn();
      JSONObject r = JSON.parseObject(result.getResponse().getContentAsString());
      System.out.println("返回结果为:" + r.getString("code"));
    }
    catch (Exception e){
      System.out.println("测试失败");
    }
  }
}
