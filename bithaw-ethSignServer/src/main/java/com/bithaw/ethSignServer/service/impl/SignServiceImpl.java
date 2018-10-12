/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月29日  下午1:50:46
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bithaw.ethSignServer.bean.AwaitData;
import com.bithaw.ethSignServer.common.Common;
import com.bithaw.ethSignServer.service.SignService;
import com.bithaw.ethSignServer.util.Rsa2Sign;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月29日 下午1:50:46
 * @version  V 1.0
 */
@Slf4j
@Service
public class SignServiceImpl implements SignService {
	@Autowired
	private Common common;
	
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * @author WangWei
	 * @Description 远程获取待签名数据
	 * @method getAwaitData
	 * @return 
	 * @return List<AwaitData>
	 * @date 2018年8月29日 下午2:06:26
	 */
	@Override
	public List<AwaitData> getAwaitData(){
		log.info("获取带签名数据");
		//准备访问参数
		String timestamp = System.currentTimeMillis() + "";
		String sign = Rsa2Sign.sign(timestamp, common.signserverPrivateKey);
		log.info("获取带签名数据,时间戳 {} sign {}",timestamp,sign);
		
		//访问
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("timestamp", timestamp);
		map.add("sign", sign);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		ResponseEntity<JSONObject> postForEntity = restTemplate.postForEntity(common.getAwaitDataUrl, request, JSONObject.class);
		JSONObject body = postForEntity.getBody();
		
		//返回值验签
		JSONArray dataArray = body.getJSONArray("data");
		String dataStr = dataArray.toJSONString().replace(" ", "");
		boolean verify = false;
		try {
			verify = Rsa2Sign.verify(dataStr, body.getString("sign"), common.signserverPublicKey);
		} catch (Exception e) {
			e.printStackTrace();
			verify = false;
		}
		if( verify == false ){
			log.info("获取带签名数据-验签失败");
			return null;
		}
		
		List<AwaitData> returnList = new ArrayList<AwaitData>();
		for(int i = 0 ; i < dataArray.size() ; i++){
			AwaitData awaitData = new AwaitData.Builder()//
				.setOrderNo(dataArray.getJSONObject(i).getString("orderNo"))//
				.setFrom(dataArray.getJSONObject(i).getString("from"))//
				.setTo(dataArray.getJSONObject(i).getString("to"))//
				.setValue(dataArray.getJSONObject(i).getString("value"))//
				.setNonce(dataArray.getJSONObject(i).getString("nonce"))//
				.setGasPrice(dataArray.getJSONObject(i).getString("gasPrice"))//
				.setGasLimit(dataArray.getJSONObject(i).getString("gasLimit"))//
				.setData(dataArray.getJSONObject(i).getString("data"))//
				.build();
			returnList.add(awaitData);
		}
		log.info("获取带签名数据成功,查看数据 {}",body.toJSONString());
		return returnList;
	}

	/** 
	 * <p>Title: sign</p>
	 * <p>Description: </p>
	 * @param AwaitDatas
	 * @see com.bithaw.ethSignServer.service.SignService#sign(java.util.List)  
	 */
	@Override
	public void sign(List<AwaitData> AwaitDatas) {
		log.info("开始签名");
		for(AwaitData awaitData : AwaitDatas){
			//私钥检测
			String privateKey = common.addressKeyMap.get(awaitData.getFrom());
			if(StringUtils.isBlank(privateKey))
				continue;
			
			//签名
			byte[] signedMessage;
			try {
				Credentials credentials = Credentials.create(privateKey);
				RawTransaction rawTransaction  = RawTransaction.createTransaction(//
						new BigInteger( awaitData.getNonce() )//
						,new BigDecimal( awaitData.getGasPrice() ). multiply( new BigDecimal("1000000000") ).toBigInteger() //
						,new BigInteger( awaitData.getGasLimit() )//
						,awaitData.getTo()//
						,new BigDecimal( awaitData.getValue() ). multiply( new BigDecimal("1000000000000000000") ).toBigInteger()//
						,awaitData.getData());
				signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			String signedMessageHex = Numeric.toHexString(signedMessage);
			
			awaitData.setRawTransaction(signedMessageHex);
			log.info("签名结束");
		}
	}

	/** 
	 * <p>Title: setRawTransaction</p>
	 * <p>Description: </p>
	 * @param AwaitDatas
	 * @see com.bithaw.ethSignServer.service.SignService#setRawTransaction(java.util.List)  
	 */
	@Override
	public void setRawTransaction(List<AwaitData> awaitDatas) {
		log.info("开始返回数据");
		for(AwaitData awaitData : awaitDatas){
			try {
				setRawTransaction(awaitData);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
		}
		log.info("返回数据结束");
	}
	
	private void setRawTransaction(AwaitData awaitData){
		//准备访问参数
		String orderNo = awaitData.getOrderNo();
		String rawTransaction = awaitData.getRawTransaction();
		if(StringUtils.isBlank(rawTransaction))
			return;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("orderNo", orderNo);
		jsonObject.put("rawTransaction", rawTransaction);
		
		//签名
		String sign = Rsa2Sign.sign(jsonObject.toJSONString().replace(" ", ""), common.signserverPrivateKey);
		
		//访问
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("json", jsonObject.toJSONString());
		map.add("sign", sign);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		restTemplate.postForLocation(common.setRawTransactionUrl, request);
	}
}
