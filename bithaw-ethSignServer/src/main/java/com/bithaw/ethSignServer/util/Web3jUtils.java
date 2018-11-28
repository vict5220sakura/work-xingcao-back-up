package com.bithaw.ethSignServer.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

public class Web3jUtils {
	
	public static ECKeyPair decodeKeystore(File file, String password) throws Exception{
		Credentials credentials = WalletUtils.loadCredentials(password, file);
		return credentials.getEcKeyPair();
	}
	
	public static ECKeyPair decodeKeystore(String fileSource, String password) throws Exception{
		File file = new File("adasdasdasdasd4c15a6s4d65asdq1sd4a65sd41a6s5d4ad13d41q56w4eqwe.josn");
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
		bufferedWriter.write(fileSource);
		bufferedWriter.close();
		Credentials credentials = WalletUtils.loadCredentials(password, file);
		file.delete();
		return credentials.getEcKeyPair();
	}
}
