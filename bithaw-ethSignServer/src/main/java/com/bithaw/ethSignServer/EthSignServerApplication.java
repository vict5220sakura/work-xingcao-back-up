/**
 * @Description 以太坊签名服务springboot启动文件
 * @author  WangWei
 * @Date    2018年8月27日  下午2:11:31
 * @version   V 1.0
 */
package com.bithaw.ethSignServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description 以太坊签名服务
 * @author   WangWei
 * @date     2018年8月27日 下午2:11:31
 * @version  V 1.0
 */
@SpringBootApplication
@EnableScheduling
public class EthSignServerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(EthSignServerApplication.class, args);
	}
}
