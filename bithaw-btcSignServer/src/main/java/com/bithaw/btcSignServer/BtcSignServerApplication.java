/**
 * @Description 
 * @author  WangWei
 * @Date    2018年9月7日  下午5:55:10
 * @version   V 1.0
 */
package com.bithaw.btcSignServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年9月7日 下午5:55:10
 * @version  V 1.0
 */
@SpringBootApplication
@EnableScheduling
public class BtcSignServerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(BtcSignServerApplication.class, args);
	}

}
