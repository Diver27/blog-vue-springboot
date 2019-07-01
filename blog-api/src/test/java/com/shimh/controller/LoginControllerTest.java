package com.shimh.controller;

import com.shimh.BlogApiApplicationTests;
import com.shimh.common.result.Result;
import com.shimh.entity.User;
import com.shimh.entity.UserStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
      File inputFile = ResourceUtils.getFile("classpath:unitTest/LoginControllerTest/loginTest.txt");
      File outFile = ResourceUtils.getFile("classpath:unitTest/LoginControllerTest/loginTestRes.txt");
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
        Result result=loginController.login(user);
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
  @Rollback
  public void registerTest() {
    try {
      User u = new User();
      u.setAccount("shimh9");
      u.setNickname("hhh");
      u.setPassword("123456");
      u.setAdmin(false);
      u.setCreateDate(new Date());
      u.setEmail("91948514@qq.com");
      u.setMobilePhoneNumber("1839616462");
      u.setStatus(UserStatus.normal);
      loginController.register(u);
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

  }

  @Test
  public void logoutTest() {

  }
}
