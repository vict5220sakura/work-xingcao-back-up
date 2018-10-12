/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月28日  下午3:05:50
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import com.alibaba.fastjson.JSONArray;
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
	
	private String secretkeyFileStrDecrypt;
	public Map<String,String> addressKeyMap = new HashMap<String,String>();//解密后的公钥私钥map
	
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
		readSecretkeyFile();
		loadAddressKeyMap();
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
		File file = new File(secretkeyFilePath);
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
			addressKeyMap.put(create.getAddress(), privateKey);
		}
		log.info("初始化-装在秘钥文件结束");
	}
}
