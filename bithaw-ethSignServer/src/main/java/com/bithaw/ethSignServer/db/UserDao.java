package com.bithaw.ethSignServer.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.bithaw.ethSignServer.common.Common;
import com.bithaw.ethSignServer.util.AESUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserDao {
	
	@Value("${serverdir}")
	private String serverdir;//服务存储目录
	
	@Value("${app.userfile}")
	private String userfile;//用户存储文件
	
	@Value("${secretkeyFileAESPassword}")
	private String secretkeyFileAESPassword;//AES秘钥,空则为未加密,空则为未加密
	
	//用户名
	public String username;
	//密码
	public String password;
	//谷歌验证key
	public String authenticatorKey;
	
	/**
	 * @author WangWei
	 * @throws IOException 
	 * @Description 读取用户文件 
	 * @method loadUserFile void
	 * @date 2018年11月29日 上午10:58:36
	 */
	public void loadUserFile() throws IOException{
		File file = new File(this.serverdir + this.userfile);
		if(!file.exists()){
			//用户文件不存在
			log.info("用户文件不存在");
			return;
		}
	    BufferedReader bReader = new BufferedReader(new FileReader(file));
	    StringBuilder sb = new StringBuilder();
	    String s = "";
	    while ( (s = bReader.readLine() ) != null) {
	        sb.append(s);
	    }
	    bReader.close();
	    String userFileStr = sb.toString();
	    JSONArray jsonArray = JSONArray.parseArray(userFileStr);
	    for (int i = 0; i < jsonArray.size(); i++) {
	    	String text = jsonArray.getString(i);
	    	if ( !StringUtils.isBlank(secretkeyFileAESPassword) ) {
	    		text = AESUtil.AESDncode(secretkeyFileAESPassword, jsonArray.getString(i));
	    	}
	    	jsonArray.set(i, text);
	    }
	    this.username = jsonArray.getString(0);
	    this.password = jsonArray.getString(1);
	    this.authenticatorKey = jsonArray.getString(2);
	    
	    //载入用户文件成功,将第一次登录标记置为空
	    Common.FIRST_LOGIN_FLAG = false;
	    log.info("载入用户文件成功");
	}
	
	/**
	 * 将秘钥文件写入文件
	 * @author WangWei
	 * @throws IOException 
	 * @Description 
	 * @method writeUserFile void
	 * @date 2018年11月28日 上午10:12:31
	 */
	public void writeUserFile() throws IOException{
		File dir = new File(this.serverdir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file = new File(this.serverdir + this.userfile);
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, false));
		if(this.username == null || "".equals(this.username)
			|| this.password == null || "".equals(this.password)
			|| this.authenticatorKey == null || "".equals(this.authenticatorKey)){
			return;
		}
		JSONArray jsonArray = new JSONArray();
		String username = this.username;
		String password = this.password;
		String authenticatorKey = this.authenticatorKey;
		if ( !StringUtils.isBlank(secretkeyFileAESPassword) ) {
			username = AESUtil.AESEncode(secretkeyFileAESPassword, this.username);
			password = AESUtil.AESEncode(secretkeyFileAESPassword, this.password);
			authenticatorKey = AESUtil.AESEncode(secretkeyFileAESPassword, this.authenticatorKey);
    	}
		jsonArray.add(username);
		jsonArray.add(password);
		jsonArray.add(authenticatorKey);
		
		bWriter.write(jsonArray.toJSONString());
		bWriter.flush();
		bWriter.close();
	}
}
