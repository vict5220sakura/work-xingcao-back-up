/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月29日  下午2:47:11
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.other;

import java.math.BigInteger;
import java.util.Arrays;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月29日 下午2:47:11
 * @version  V 1.0
 */
public class SignTest {
	public static void main(String[] args) {
//		Credentials credentials = Credentials.create("964376b59290052a6791dd3795adb0d5a9fd7b0db0001133ff223a73c7d162da");
//		RawTransaction rawTransaction  = RawTransaction.createTransaction(//
//				new BigInteger( "76" )//
//				,new BigInteger( "6000000000" )//
//				,new BigInteger( "500000" )//
//				,"0x4fe415ddf8451c9de3f89d8f815f0d014fbe567b"//
//				,new BigInteger( "0" )//
//				,"a9059cbb0000000000000000000000004fe415ddf8451c9de3f89d8f815f0d014fbe567b00000000000000000000000000000000000000000000000000000000000f4241");
//		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
//		String signedMessageHex = Numeric.toHexString(signedMessage);
//		System.out.println(signedMessageHex);
		testOldSign();
	}
	public static void signPrivateKey(){
		Credentials credentials = Credentials.create("2d635e6d90fac44417bd6f89248fe40522b6aafa5828158806fa29d7a149edfe");
		RawTransaction rawTransaction  = RawTransaction.createTransaction(//
				new BigInteger( "0" )//
				,new BigInteger( "6" )//
				,new BigInteger( "6" )//
				,"0xb00ecbd39b5138f9eb7680205f565848b3699742"//
				,new BigInteger( "0" )//
				,"0x123");
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
		String signedMessageHex = Numeric.toHexString(signedMessage);
		System.out.println(signedMessageHex);
		//0xf85f80060694b00ecbd39b5138f9eb7680205f565848b3699742808201231ca0ef05fccc27fcef2e79cc821e7c6992040aae75b9abdcf8a9723be4e0f790ed83a0130f6a028d22c0a3eac513f50564e2b0ef82ff9ff3a3d2da4003c99d8c26468a
		//0xf85f80060694b00ecbd39b5138f9eb7680205f565848b3699742808201231ca0ef05fccc27fcef2e79cc821e7c6992040aae75b9abdcf8a9723be4e0f790ed83a0130f6a028d22c0a3eac513f50564e2b0ef82ff9ff3a3d2da4003c99d8c26468a
	}
	
	public static void testOldSign(){
		BigInteger gasPrice = new BigInteger("6").multiply(new BigInteger("1000000000"));
		BigInteger gasLimit = BigInteger.valueOf(500000L);
		
		Credentials credentials = WalletUtils.loadBip39Credentials("123", "idea hire door road hybrid business steak victory kangaroo notice actress motion");
		RawTransaction rawTransaction  = RawTransaction.createTransaction(//
				new BigInteger("76"),gasPrice,gasLimit,"0xb00ecbd39b5138f9eb7680205f565848b3699742",new BigInteger("0"),"0xa9059cbb0000000000000000000000004fe415ddf8451c9de3f89d8f815f0d014fbe567b00000000000000000000000000000000000000000000000000000000000f4241");//可以额外带数据
		
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,credentials);
		String hexValue = Numeric.toHexString(signedMessage);
		System.out.println(hexValue);
	}
}
