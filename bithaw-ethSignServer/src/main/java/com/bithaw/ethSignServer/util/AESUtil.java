package com.bithaw.ethSignServer.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

/**
 * AES加解密工具类
 * 
 * @author 王玮
 *
 */
@Slf4j
public class AESUtil {
	/**
	 * 加密 1.构造密钥生成器 2.根据ecnodeRules规则初始化密钥生成器 3.产生密钥 4.创建和初始化密码器 5.内容加密 6.返回字符串
	 */
	public static String AESEncode(String encodeRules, String content) {
		try {
			log.info("AES加密:开始,秘钥:{} 加密密文:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***");
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(encodeRules.getBytes("UTF-8"));
			keygen.init(128, random);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byte_encode = content.getBytes("utf-8");
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(byte_encode);
			// 10.将加密后的数据转换为字符串
			// 这里用Base64Encoder中会找不到包
			// 解决办法：
			// 在项目的Build path中先移除JRE System Library，再添加库JRE System
			// Library，重新编译后就一切正常了。
			String AES_encode = BytesHexStrTranslate.bytesToHexFun1(byte_AES);
			// 11.将字符串返回
			log.info("AES加密:成功,秘钥:{} 加密密文:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***");
			return AES_encode;
		} catch (Exception e) {
			log.error("AES加密:错误,秘钥:{} 加密密文:{} 错误信息:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***", e);
			return null;
		}

	}
	
	public static String AESEncodeBytes(String encodeRules, byte[] content) {
		try {
			log.info("AES加密:开始,秘钥:{}", encodeRules.substring(0, 3) + "***");
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(encodeRules.getBytes("UTF-8"));
			keygen.init(128, random);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byte_AES = cipher.doFinal(content);
			// 10.将加密后的数据转换为字符串
			// 这里用Base64Encoder中会找不到包
			// 解决办法：
			// 在项目的Build path中先移除JRE System Library，再添加库JRE System
			// Library，重新编译后就一切正常了。
			String AES_encode = BytesHexStrTranslate.bytesToHexFun1(byte_AES);
			// 11.将字符串返回
			log.info("AES加密:成功,秘钥:{} ", encodeRules.substring(0, 3) + "***");
			return AES_encode;
		} catch (Exception e) {
			log.error("AES加密:错误,秘钥:{} 错误信息:{}", encodeRules.substring(0, 3) + "***", e);
			return null;
		}
		
	}

	/*
	 * 解密 解密过程： 1.同加密1-4步 2.将加密后的字符串反纺成byte[]数组 3.将加密内容解密
	 */
	public static String AESDncode(String encodeRules, String content) {
		try {
			log.info("AES解密:开始,秘钥:{} 解密密文:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***");
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(encodeRules.getBytes("UTF-8"));
			keygen.init(128, random);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			byte[] byte_content = BytesHexStrTranslate.toBytes(content);
			/*
			 * 解密
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			String AES_decode = new String(byte_decode, "utf-8");
			log.info("AES解密:成功,秘钥:{} 解密密文:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***");
			return AES_decode;
		} catch (Exception e) {
			log.error("AES解密:错误,秘钥:{} 解密密文:{} 错误信息:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***", e);
			// 如果有错就返加nulll
			return null;
		}

	}
	public static byte[] AESDncodeBytes(String encodeRules, String content) {
		try {
			log.info("AES解密:开始,秘钥:{} 解密密文:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***");
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(encodeRules.getBytes("UTF-8"));
			keygen.init(128, random);
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			// 5.根据字节数组生成AES密钥
			SecretKey key = new SecretKeySpec(raw, "AES");
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			byte[] byte_content = BytesHexStrTranslate.toBytes(content);
			/*
			 * 解密
			 */
			byte[] byte_decode = cipher.doFinal(byte_content);
			log.info("AES解密:成功,秘钥:{} 解密密文:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***");
			return byte_decode;
		} catch (Exception e) {
			log.error("AES解密:错误,秘钥:{} 解密密文:{} 错误信息:{}", encodeRules.substring(0, 3) + "***", content.substring(0, 3) + "***", e);
			// 如果有错就返加nulll
			return null;
		}
		
	}
}
