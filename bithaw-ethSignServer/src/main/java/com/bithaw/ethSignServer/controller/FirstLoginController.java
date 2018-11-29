package com.bithaw.ethSignServer.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bithaw.ethSignServer.common.Common;
import com.bithaw.ethSignServer.db.UserDao;
import com.bithaw.ethSignServer.util.GoogleAuthenticator;
import com.bithaw.ethSignServer.util.JSONBuilder;

/**
 * @Description 第一次登录控制器
 * @author   WangWei
 * @date     2018年11月29日 上午9:49:34
 * @version  V 1.0
 */
@Controller
public class FirstLoginController {
	
	@Autowired
	private Common common;
	
	@Autowired
	private UserDao userDao;
	
	@Value("${initial.username}")
	private String initialUsername;

	@Value("${initial.password}")
	private String initialPassowrd;

	public boolean isNull(String str){
		if(str == null){
			return true;
		}
		if(str.trim().equals("")){
			return true;
		}
		return false;
	}
	
	/**
	 * @author WangWei
	 * @Description 跳转到第一次登录页面
	 * @method firstlogin
	 * @return String
	 * @date 2018年11月29日 上午9:51:34
	 */
	@RequestMapping("firstlogin")
	public String firstlogin(){
		if(Common.FIRST_LOGIN_FLAG){
			return "firstlogin";
		}else{
			return "index";
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 第一次登录后,跳转到admin界面
	 * @method admin
	 * @return String
	 * @date 2018年11月29日 上午9:51:34
	 */
	@RequestMapping("admin")
	public String admin(){
		if(Common.FIRST_LOGIN_FLAG){
			return "admin";
		}else{
			return "index";
		}
	}
	
	/**
	 * @author WangWei
	 * @Description 生成google验证码
	 * @method admin
	 * @return String
	 * @date 2018年11月29日 上午9:51:34
	 */
	@RequestMapping("first-init")
	@ResponseBody
	public String firstInit(@RequestParam("password")String password, 
			@RequestParam("password_re")String passwordRe){
		
		if(!Common.FIRST_LOGIN_FLAG){
			return new JSONBuilder().put("code", "0").put("message", "初始密码已失效,请联系管理员").build().toJSONString();
		}
		if(isNull(password) || isNull(passwordRe)){
			return new JSONBuilder().put("code", "0").put("message", "参数为空,重新输入").build().toJSONString();
		}
		
		if(!password.equals(passwordRe)){
			return new JSONBuilder().put("code", "0").put("message", "两次输入密码不一样,请重新输入").build().toJSONString();
		}
		
		//通过验证,可以生成	
		String createSecretKey = GoogleAuthenticator.createSecretKey();
		//通过验证可以创建用户文件
		userDao.password = password;
		userDao.authenticatorKey = createSecretKey;
		userDao.username = this.initialUsername;
		try {
			userDao.writeUserFile();
		} catch (IOException e) {
			e.printStackTrace();
			return new JSONBuilder().put("code", "0").put("message", "文件写入失败,请重试,多次失败请联系管理员").build().toJSONString();
		}
		Common.FIRST_LOGIN_FLAG = false;
		return new JSONBuilder().put("code", "1").put("message", "生成成功").put("google_sign", createSecretKey).build().toJSONString();
	}
	
	
	
	/**
	 * 
	 * @author WangWei
	 * @Description post登录请求
	 * @method login
	 * @param username
	 * @param password
	 * @param sign
	 * @return String
	 * @date 2018年11月28日 上午9:41:59
	 */
	@PostMapping("first-login-in")
	@ResponseBody
	public String firstLoginIn(
			@RequestParam("username")String username, 
			@RequestParam("password")String password){
		if(Common.FIRST_LOGIN_FLAG){
			
		}else{
			return new JSONBuilder().put("code", "0").put("message", "初始密码已失效,请联系管理员").build().toJSONString();
		}
		if(this.initialUsername == null || "".equals(this.initialUsername)){
			return new JSONBuilder().put("code", "0").put("message", "初始密码已失效,请联系管理员").build().toJSONString();
		}
		if(isNull(username) || isNull(password)){
			return new JSONBuilder().put("code", "0").put("message", "参数为空,请重新登录").build().toJSONString();
		}
		if(firstLoginCheck(username, password)){
			//通过
			//Common.FIRST_LOGIN_FLAG = false;//第一次登录失效放在生成google验证码后
			//返回token,存入token
			String createToken = common.createToken(username, password);
			common.saveToken(createToken);
			return new JSONBuilder().put("code", "1").put("message", "登录成功").put("token", createToken).build().toJSONString();
		}
		return new JSONBuilder().put("code", "0").put("message", "登录失败,请重试").build().toJSONString();
	}
	
	public boolean firstLoginCheck(String username, String password){
		if(username.equals(this.initialUsername) && password.equals(this.initialPassowrd)){
			return true;
		}
		return false;
	}
	
}
