/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月27日  下午2:19:03
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bithaw.ethSignServer.bean.AwaitData;
import com.bithaw.ethSignServer.common.Common;
import com.bithaw.ethSignServer.service.SignService;

import lombok.extern.slf4j.Slf4j;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月27日 下午2:19:03
 * @version  V 1.0
 */
@Slf4j
@Component
public class SignTask {
	@Autowired
	private SignService signService;
	@Autowired
	private Common common;
	/**
	 * @author WangWei
	 * @Description 获取带签名数据
	 * @method getAwaitSignData 
	 * @return void
	 * @date 2018年8月27日 下午2:29:45
	 */
	//@Scheduled(cron = "0/5 * * * * ? ")
    public void getAwaitSignData(){
		if(common.addressKeyMap == null || common.addressKeyMap.size() == 0){
			log.info("秘钥为空,直接略过");
			return;
		}
		log.info("定时签名任务开始");
        try {
        	
        	//1,调远程eth接口获取待签名数据
        	log.info("调远程eth接口获取待签名数据");
        	List<AwaitData> awaitData = signService.getAwaitData();
        	//2,签名
        	log.info("签名");
        	signService.sign(awaitData);
        	//3,调用远程eth接口将签名后的数据返回
        	log.info("调用远程eth接口将签名后的数据返回");
        	signService.setRawTransaction(awaitData);
        	
		} catch (Throwable e) {
			log.error("任务报错",e);
		}
        log.info("定时签名任务结束");
    }
}
