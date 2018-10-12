/**
 * @Description 签名服务接口文件
 * @author  WangWei
 * @Date    2018年8月27日  下午2:16:31
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.service;

import java.util.List;

import com.bithaw.ethSignServer.bean.AwaitData;

/**
 * @Description 签名服务
 * @author   WangWei
 * @date     2018年8月27日 下午2:16:31
 * @version  V 1.0
 */
public interface SignService {
	public List<AwaitData> getAwaitData();
	public void sign(List<AwaitData> AwaitDatas);
	public void setRawTransaction(List<AwaitData> awaitDatas);
}
