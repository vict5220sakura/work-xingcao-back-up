package com.bithaw.btcSignServer.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @Description rsa签名工具类
 * @author WangWei
 * @date 2018年8月27日 下午2:45:15
 * @version V 1.0
 */
public class Rsa2Sign {

	/**
	 * @author WangWei
	 * @Description 对文本数据签名
	 * @method sign
	 * @param content
	 * @param privateKey
	 * @return
	 * @return String
	 * @date 2018年8月27日 下午2:45:24
	 */
	public static String sign(String content, String privateKey) {
		try {
			PrivateKey privateKey_ = getPrivateKey(privateKey);
			byte[] reqBs = content.getBytes("UTF-8");
			Signature signature = Signature.getInstance("SHA256WithRSA");
			signature.initSign(privateKey_);
			signature.update(reqBs);
			byte[] signedBs = signature.sign();
			String signedStr = Base64.encodeBase64String(signedBs);
			return signedStr;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @author WangWei
	 * @Description 私钥字符串转为私钥实体
	 * @method getPrivateKey
	 * @param key
	 * @return
	 * @throws Exception 
	 * @return PrivateKey
	 * @date 2018年8月27日 下午2:45:43
	 */
	private static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decodeBase64(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * @author WangWei
	 * @Description 签名验证
	 * @method verify
	 * @param data
	 * @param sign
	 * @param publicKeyString
	 * @throws Exception 
	 * @return boolean 
	 * @date 2018年8月27日 下午2:46:36
	 */
	public static boolean verify(String data, String sign, String publicKeyString) throws Exception {
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyf.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKeyString)));
		java.security.Signature signet = java.security.Signature.getInstance("SHA256WithRSA");
		signet.initVerify(publicKey);
		signet.update(data.getBytes("utf-8"));
		return signet.verify(Base64.decodeBase64(sign));
	}
	
	public static void main(String[] args) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
		System.out.println(Base64.encodeBase64String(rsaPublicKey.getEncoded()));
		System.out.println(Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
	}
}
