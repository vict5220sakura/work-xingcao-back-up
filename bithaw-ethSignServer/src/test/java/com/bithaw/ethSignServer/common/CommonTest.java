/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月29日  上午10:12:15
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.ethSignServer.EthSignServerApplication;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月29日 上午10:12:15
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EthSignServerApplication.class)
public class CommonTest {
	@Autowired
	Common common;
	
	@Test
	public void initTest(){
		for( String key : common.addressKeyMap.keySet() ){
			System.out.println(key + " : " + common.addressKeyMap.get(key));
		}
	}
}
