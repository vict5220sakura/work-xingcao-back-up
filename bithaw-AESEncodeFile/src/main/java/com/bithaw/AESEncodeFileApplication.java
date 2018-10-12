/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月29日  上午11:22:21
 * @version   V 1.0
 */
package com.bithaw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.bithaw.util.AESUtil;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月29日 上午11:22:21
 * @version  V 1.0
 */
public class AESEncodeFileApplication {
	public static void main(String[] args) throws IOException {
		String filename = args[0];
		Scanner sc = new Scanner(System.in); 
        System.out.println("请输入密码(回车结束)："); 
        String password = sc.nextLine(); 
		System.out.println("文件名: " + filename);
		System.out.println("密码: " + password);
		
		if(StringUtils.isBlank(password)){
			return;
		}
		
		File file = new File(filename);
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
	    	  jsonArray.set(i, AESUtil.AESEncode(password, privateKey));
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
            System.out.println("加密成功");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	fileOutputStream.close();
        }
	}
}
