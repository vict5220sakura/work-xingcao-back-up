package com.bithaw.ethSignServer.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import com.bithaw.ethSignServer.common.Common;
import com.bithaw.ethSignServer.db.UserDao;
import com.bithaw.ethSignServer.util.GoogleAuthenticator;
import com.bithaw.ethSignServer.util.JSONBuilder;
import com.bithaw.ethSignServer.util.Web3jUtils;

@Controller
public class HtmlController {
	
	@Autowired
	private Common common;
	
	@Autowired
	private UserDao userDao;

	/**
	 * @author WangWei
	 * @Description 登录首页
	 * @method index
	 * @return String
	 * @date 2018年11月27日 下午4:43:43
	 */
	@RequestMapping("index")
	public String index(){
		if(Common.FIRST_LOGIN_FLAG){
			return "admin";
		}
		return "index";
	}
	
	@RequestMapping("/login")
	public String login(){
		if(Common.FIRST_LOGIN_FLAG){
			return "firstlogin";
		}
		return "login";
	}
	
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
	@PostMapping("login-in")
	@ResponseBody
	public String loginIn(
			@RequestParam("username")String username, 
			@RequestParam("password")String password, 
			@RequestParam("sign")String sign){
		if(isNull(username) || isNull(password) || isNull(sign)){
			return new JSONBuilder().put("code", "0").put("message", "参数为空,请重新登录").build().toJSONString();
		}
		if(loginCheck(username, password, sign)){
			//通过
			//返回token,存入token
			String createToken = common.createToken(username, password, sign);
			common.saveToken(createToken);
			return new JSONBuilder().put("code", "1").put("message", "登录成功").put("token", createToken).build().toJSONString();
		}
		return new JSONBuilder().put("code", "0").put("message", "登录失败,请重试").build().toJSONString();
	}
	
	public boolean loginCheck(String username, String password, String sign){
		System.out.println("登录验证输入参数" + username + "|" + password + "|" + sign);
		System.out.println("登录验证userDao参数" + userDao.username + "|" + userDao.password + "|" + userDao.authenticatorKey);
		if(username.equals(userDao.username) && password.equals(userDao.password)){
			if(GoogleAuthenticator.verify(userDao.authenticatorKey, sign)){
				return true;
			}
		}
		return false;
	}
	
	//获取公钥
	@PostMapping("get-public-key")
	@ResponseBody
	public String getPublicKey(){
		String returnStr = "";
		Set<String> keySet = common.addressKeyMap.keySet();
		for(String address : keySet){
			returnStr += address + "|";
		}
		if(!keySet.isEmpty()){
			returnStr = returnStr.substring(0, returnStr.length() - 1);
		}
		return returnStr;
	}
	
	//移除私钥
	@PostMapping("remove-private-key")
	@ResponseBody
	public String removePublicKey(String address, String sign){
		if(isNull(address) || isNull(sign)){
			return new JSONBuilder().put("code", "0").put("message", "请输入谷歌验证码").build().toJSONString();
		}
		if(GoogleAuthenticator.verify(userDao.authenticatorKey, sign)){
			
		}else{
			return new JSONBuilder().put("code", "0").put("message", "谷歌验证码错误").build().toJSONString();
		}
		//验证码正确可以删除 
		common.addressKeyMap.remove(address.trim().toLowerCase());
		try {
			common.writeSecretkeyFile();
		} catch (IOException e) {
			e.printStackTrace();
			return new JSONBuilder().put("code", "0").put("message", "写入文件错误").build().toJSONString();
		}
		return new JSONBuilder().put("code", "1").put("message", "删除成功").build().toJSONString();
	}
	
	//添加私钥
	@PostMapping("add-private-key")
	@ResponseBody
	public String addPrivateKey(
			@RequestParam("file")MultipartFile file, 
			@RequestParam("password")String password, 
			@RequestParam("sign")String sign){
		if(file == null){
			return new JSONBuilder().put("code", "0").put("message", "上传keystore为空").build().toJSONString();
		}
		
		if(isNull(password) || isNull(sign)){
			return new JSONBuilder().put("code", "0").put("message", "一些参数为空,请重新输入").build().toJSONString();
		}
		
		if(GoogleAuthenticator.verify(userDao.authenticatorKey, sign)){
			
		}else{
			return new JSONBuilder().put("code", "0").put("message", "谷歌验证码错误").build().toJSONString();
		}
		
		//验证keystore,并获取私钥
		ECKeyPair ecKeyPair;
		try {
			String keystoreSource = new String(file.getBytes(),"UTF-8");
			ecKeyPair = Web3jUtils.decodeKeystore(keystoreSource, password);
		} catch (Exception e1) {
			e1.printStackTrace();
			return new JSONBuilder().put("code", "0").put("message", "keystore文件格式错误").build().toJSONString();
		}
		
		String privateKey = ecKeyPair.getPrivateKey().toString(16);
		String address = Credentials.create(ecKeyPair).getAddress();
		
		common.addressKeyMap.put(address, privateKey);
		try {
			common.writeSecretkeyFile();
		} catch (IOException e) {
			e.printStackTrace();
			return new JSONBuilder().put("code", "0").put("message", "写入文件错误").build().toJSONString();
		}
		
		return new JSONBuilder().put("code", "1").put("message", "添加成功").build().toJSONString();
	}
	
	//添加私钥
	@PostMapping("add-private-key-keystoresource")
	@ResponseBody
	public String addPrivateKeyByKeystoreSource(
			@RequestParam("keystoresource")String keystoresource, 
			@RequestParam("password")String password, 
			@RequestParam("sign")String sign){
		
		if(GoogleAuthenticator.verify(userDao.authenticatorKey, sign)){
			
		}else{
			return new JSONBuilder().put("code", "0").put("message", "谷歌验证码错误").build().toJSONString();
		}
		
		//验证keystore,并获取私钥
		ECKeyPair ecKeyPair;
		try {
			ecKeyPair = Web3jUtils.decodeKeystore(keystoresource, password);
		} catch (Exception e1) {
			e1.printStackTrace();
			return new JSONBuilder().put("code", "0").put("message", "keystore文件格式错误").build().toJSONString();
		}
		
		String privateKey = ecKeyPair.getPrivateKey().toString(16);
		String address = Credentials.create(ecKeyPair).getAddress();
		
		common.addressKeyMap.put(address, privateKey);
		try {
			common.writeSecretkeyFile();
		} catch (IOException e) {
			e.printStackTrace();
			return new JSONBuilder().put("code", "0").put("message", "写入文件错误").build().toJSONString();
		}
		
		return new JSONBuilder().put("code", "1").put("message", "添加成功").build().toJSONString();
	}
	
	@PostMapping("get-sign-server-status")
	@ResponseBody
	public String getSignServerStatus(){
		if(Common.SIGNFLAG){
			return "1";
		}else{
			return "0";
		}
	}
	
	@PostMapping("change-sign-server-status")
	@ResponseBody
	public String changeSignServerStatus(@RequestParam("flag")String flag, @RequestParam("sign")String sign){
		if(GoogleAuthenticator.verify(userDao.authenticatorKey, sign)){
			
		}else{
			return new JSONBuilder().put("code", "0").put("message", "谷歌验证码错误").build().toJSONString();
		}
		if("0".equals(flag)){
			Common.SIGNFLAG = false;
			return new JSONBuilder().put("code", "1").put("message", "暂停成功").build().toJSONString();
		}
		if("1".equals(flag)){
			Common.SIGNFLAG = true;
			return new JSONBuilder().put("code", "1").put("message", "恢复成功").build().toJSONString();
		}
		return new JSONBuilder().put("code", "0").put("message", "未知错误").build().toJSONString();
	}
	
	/**
	 * @author WangWei
	 * @Description 修改密码
	 * @method changePassword
	 * @param password
	 * @param passwordRe
	 * @param sign
	 * @return String
	 * @date 2018年11月29日 下午1:14:47
	 */
	@PostMapping("change-password")
	@ResponseBody
	public String changePassword(
			@RequestParam("password")String password,
			@RequestParam("password_re")String passwordRe,
			@RequestParam("sign")String sign
			){
		if(isNull(password) || isNull(passwordRe) || isNull(sign)){
			return new JSONBuilder().put("code", "0").put("message", "参数为空").build().toJSONString();
		}
		
		if(!password.equals(passwordRe)){
			return new JSONBuilder().put("code", "0").put("message", "两次输入密码不一致").build().toJSONString();
		}
		
		if(!GoogleAuthenticator.verify(userDao.authenticatorKey, sign)){
			return new JSONBuilder().put("code", "0").put("message", "谷歌验证码错误").build().toJSONString();
		}
		
		//通过,可以修改密码
		userDao.password = password;
		try {
			userDao.writeUserFile();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				userDao.loadUserFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return new JSONBuilder().put("code", "0").put("message", "写入文件错误,请重试").build().toJSONString();
		}
		//清空token
		Common.loginToken= null;
		return new JSONBuilder().put("code", "1").put("message", "修改成功").build().toJSONString();
	}
}
