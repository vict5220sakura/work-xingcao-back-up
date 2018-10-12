/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月28日  下午3:13:50
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月28日 下午3:13:50
 * @version  V 1.0
 */
public class AESUtilTest {
	@Test
	public void AESEncodeAndAESDncodeTest(){
		String data = "asdadasdasdxcanfdklajsnjkahdabsda";
		String aesEncode = AESUtil.AESEncode("123123", data);
		System.out.println(aesEncode);
		String aesDncode = AESUtil.AESDncode("123123", aesEncode);
		assertTrue(data.equals(aesDncode));
	}
	
	@Test
	public void AESEncodeFile() throws Exception{
		File file = new File("D:\\秘钥文件.txt");
		BufferedReader bReader = new BufferedReader(new FileReader(file));
	    StringBuilder sb = new StringBuilder();
	    String s = "";
	    while ( (s = bReader.readLine() ) != null) {
	        sb.append(s);
	    }
	    bReader.close();
	    String fileStr = sb.toString();
	    System.out.println(fileStr);
	    JSONArray jsonArray = JSONArray.parseArray(fileStr);
	    for(int i = 0 ; i < jsonArray.size() ; i++){
	    	  String privateKey = jsonArray.getString(i);
	    	  jsonArray.set(i, AESUtil.AESEncode("5220", privateKey));
	    }
	    
	    String jsonString = jsonArray.toJSONString();
	    System.out.println(jsonString);
	    File fileOut = new File(file.getParent(),"aesPrivateKey.key");
	    
	    FileOutputStream fileOutputStream = null;
	    try {
	    	fileOutputStream = new FileOutputStream(fileOut);
            if (!file.exists()) {
                file.createNewFile();
            }

            byte[] contentInBytes = jsonString.getBytes("UTF-8");

            fileOutputStream.write(contentInBytes);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	fileOutputStream.close();
        }
	}
}
