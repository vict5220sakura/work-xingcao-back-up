/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月28日  下午3:05:50
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import com.alibaba.fastjson.JSONArray;
import com.bithaw.ethSignServer.db.UserDao;
import com.bithaw.ethSignServer.util.AESUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 读取配置文件
 * @author   WangWei
 * @date     2018年8月28日 下午3:05:50
 * @version  V 1.0
 */
@Slf4j
@Component
public class Common {
	@Value("${serverdir}")
	private String serverdir;//服务存储目录
	@Value("${app.userfile}")
	private String userfile;//用户存储文件
	@Value("${secretkeyFile}")
	private String secretkeyFilePath;//秘钥文件地址,不可无效
	@Value("${secretkeyFileAESPassword}")
	private String secretkeyFileAESPassword;//AES秘钥,空则为未加密,空则为未加密
	@Value("${signserver.publicKey}")
	public String signserverPublicKey;//网络传输公钥,不可为空
	@Value("${signserver.privateKey}")
	public String signserverPrivateKey;//网络传输私钥,不可为空
	@Value("${getAwaitDataUrl}")
	public String getAwaitDataUrl;//获取签名数据地址,不可为空
	@Value("${setRawTransactionUrl}")
	public String setRawTransactionUrl;//返回签名数据地址,不可为空
	
	@Autowired
	private UserDao userDao;
	
	private String secretkeyFileStrDecrypt;
	public Map<String, String> addressKeyMap = new HashMap<String, String>();//解密后的公钥私钥map
	
	//服务开关
	public static boolean SIGNFLAG = false;
	
	//登陆过期实金
	@Value("${loginOutTime}")
	private Long loginOutTime;
	private static Long loginOutTimeStatic;
	//登录token
	public static String loginToken;
	//登录时间
	public static Long loginTime;
	
	/**
	 * firstLoginFlag : 第一次登录标记
	 */
	public static boolean FIRST_LOGIN_FLAG = true;
	
	public String createToken(String username, String password, String sign){
		String aesEncode = AESUtil.AESEncode(secretkeyFileAESPassword, username + "|" + password + "|" + sign);
		return aesEncode;
	}
	
	public String createToken(String username, String password){
		String aesEncode = AESUtil.AESEncode(secretkeyFileAESPassword, username + "|" + password);
		return aesEncode;
	}
	
	public void saveToken(String token){
		this.loginToken = token;
		this.loginTime = System.currentTimeMillis();
	}
	
	public static boolean checkLogin(String token){
		if(token == null){
			return false;
		}
		if(!token.equals(loginToken)){
			return false;
		}
		long overTime = loginOutTimeStatic * 60L * 1000L + loginTime;
		if(System.currentTimeMillis() > overTime){
			return false;
		}
		return true;
	}
	
	@Value("${signflag}")
	private String signflag;
	
	/**
	 * @author WangWei
	 * @Description 初始化
	 * @method init 
	 * @return void
	 * @throws Throwable 
	 * @throws Exception 
	 * @date 2018年8月28日 下午3:19:45
	 */
	public void init() throws Throwable{
		log.info("初始化");
		if(signflag.equals("0")){
			this.SIGNFLAG = false;
		}else{
			this.SIGNFLAG = true;
		}
		loginOutTimeStatic = loginOutTime;
		readSecretkeyFile();
		loadAddressKeyMap();
		userDao.loadUserFile();
		log.info("初始化结束");
	}
	
	/**
	 * @author WangWei
	 * @Description 读取文件并解密
	 * @method readSecretkeyFile
	 * @throws IOException 
	 * @return void
	 * @date 2018年8月28日 下午3:27:58
	 */
	private void readSecretkeyFile() throws IOException{
		log.info("初始化-读取文件");
		File file = new File(serverdir + secretkeyFilePath);
		if(!file.exists()){
			return;
		}
	    BufferedReader bReader = new BufferedReader(new FileReader(file));
	    StringBuilder sb = new StringBuilder();
	    String s = "";
	    while ( (s = bReader.readLine() ) != null) {
	        sb.append(s);
	    }
	    bReader.close();
	    String keyFileStr = sb.toString();
		if ( !StringUtils.isBlank(secretkeyFileAESPassword) ) {
			JSONArray jsonArray = JSONArray.parseArray(keyFileStr);
			for (int i = 0; i < jsonArray.size(); i++) {
				String privateKey = AESUtil.AESDncode(secretkeyFileAESPassword, jsonArray.getString(i));
				jsonArray.set(i, privateKey);
			}
			secretkeyFileStrDecrypt = jsonArray.toJSONString();
		}else{
			secretkeyFileStrDecrypt = keyFileStr;
		}
		log.info("初始化-读取文件结束");
	}
	
	/**
	 * @author WangWei
	 * @Description 载入map集合
	 * @method loadAddressKeyMap 
	 * @return void
	 * @throws NoSuchProviderException 
	 * @throws  
	 * @throws  
	 * @date 2018年8月28日 下午3:28:10
	 */
	private void loadAddressKeyMap() throws Throwable{
		log.info("初始化-装在秘钥文件");
		if(secretkeyFileStrDecrypt == null || "".equals(secretkeyFileStrDecrypt)){
			return;
		}
		JSONArray jsonArray = JSONArray.parseArray(secretkeyFileStrDecrypt);
		for(int i = 0 ; i < jsonArray.size() ; i++){
			String privateKey = jsonArray.getString(i);
			if(StringUtils.isBlank(privateKey)){
				continue;
			}
			if( !WalletUtils.isValidPrivateKey(privateKey.trim()) ){
				continue;
			}
			Credentials create = Credentials.create(privateKey.trim());
			addressKeyMap.put(create.getAddress().toLowerCase(), privateKey);
		}
		log.info("初始化-装在秘钥文件结束");
	}
	
	/**
	 * 将秘钥文件写入文件
	 * @author WangWei
	 * @throws IOException 
	 * @Description 
	 * @method writeSecretkeyFile void
	 * @date 2018年11月28日 上午10:12:31
	 */
	public void writeSecretkeyFile() throws IOException{
		File file = new File(serverdir + secretkeyFilePath);
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, false));
		if(addressKeyMap.isEmpty()){
			return;
		}
		JSONArray jsonArray = new JSONArray();
		Set<String> addresses = addressKeyMap.keySet();
		for(String address : addresses){
			String privateKey = addressKeyMap.get(address);
			String aesEncodePrivateKey = AESUtil.AESEncode(secretkeyFileAESPassword, privateKey);
			jsonArray.add(aesEncodePrivateKey);
		}
		
		bWriter.write(jsonArray.toJSONString());
		bWriter.flush();
		bWriter.close();
	}
	
	
}
