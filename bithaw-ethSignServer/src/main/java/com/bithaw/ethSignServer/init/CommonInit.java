/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月28日  下午5:30:13
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.bithaw.ethSignServer.common.Common;
import com.bithaw.ethSignServer.service.impl.SignServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 初始化
 * @author   WangWei
 * @date     2018年8月28日 下午5:30:13
 * @version  V 1.0
 */
@Slf4j
@Component
@Order(value = 1)
public class CommonInit implements ApplicationRunner{
	@Autowired
	private Common common;
	/** 
	 * <p>Title: run</p>
	 * <p>Description: </p>
	 * @param args
	 * @throws Exception
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)  
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			log.info("初始化");
			common.init();
			log.info("初始化结束");
		} catch (Throwable e) {
			log.error("初始化失败",e);
		}
	}

}
