/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月29日  下午2:04:35
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.bean;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月29日 下午2:04:35
 * @version  V 1.0
 */
public class AwaitData {
	public AwaitData(){
		
	}
	
	public AwaitData(String nonce, String gasPrice, String gasLimit, String to, String value, String data, String orderNo, String from, String rawTransaction) {
		super();
		this.nonce = nonce;
		this.gasPrice = gasPrice;
		this.gasLimit = gasLimit;
		this.to = to;
		this.value = value;
		this.data = data;
		this.orderNo = orderNo;
		this.from = from;
		this.rawTransaction = rawTransaction;
	}

	private String nonce;
	private String gasPrice;
	private String gasLimit;
	private String to;
	private String value;
	private String data;
	private String orderNo;
	private String from;
	
	private String rawTransaction;
	
	public static class Builder{
		private String nonce;
		private String gasPrice;
		private String gasLimit;
		private String to;
		private String value;
		private String data;
		private String orderNo;
		private String from;
		private String rawTransaction;
		public Builder setNonce(String nonce) {
			this.nonce = nonce;
			return this;
		}
		public Builder setGasPrice(String gasPrice) {
			this.gasPrice = gasPrice;
			return this;
		}
		public Builder setGasLimit(String gasLimit) {
			this.gasLimit = gasLimit;
			return this;
		}
		public Builder setTo(String to) {
			this.to = to;
			return this;
		}
		public Builder setValue(String value) {
			this.value = value;
			return this;
		}
		public Builder setData(String data) {
			this.data = data;
			return this;
		}
		public Builder setOrderNo(String orderNo) {
			this.orderNo = orderNo;
			return this;
		}
		
		public Builder setFrom(String from) {
			this.from = from;
			return this;
		}
		
		public Builder setRawTransaction(String rawTransaction) {
			this.rawTransaction = rawTransaction;
			return this;
		}
		public AwaitData build(){
			return new AwaitData(nonce, gasPrice, gasLimit, to, value, data, orderNo, from, rawTransaction);
		}
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	public String getRawTransaction() {
		return rawTransaction;
	}
	public void setRawTransaction(String rawTransaction) {
		this.rawTransaction = rawTransaction;
	}
	
}
