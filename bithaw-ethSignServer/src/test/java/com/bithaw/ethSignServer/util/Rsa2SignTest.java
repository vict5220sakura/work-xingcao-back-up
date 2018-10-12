/**
 * @Description 
 * @author  WangWei
 * @Date    2018年8月27日  下午2:47:43
 * @version   V 1.0
 */
package com.bithaw.ethSignServer.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bithaw.ethSignServer.EthSignServerApplication;


/**
 * @Description 
 * @author   WangWei
 * @date     2018年8月27日 下午2:47:43
 * @version  V 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EthSignServerApplication.class)
public class Rsa2SignTest {
	
	private String dataTest = "testData";
	
	private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWQXnDtmzhdXxTZrueSj6FqboYArELYweFPwlCEpA1uv0fqJR3TcUyx4K/2NkaAoEtXU7ON/2Nmpp3eNp00N7geS88ka4K+dUTO18B2dqgnH3ueNPkqUyoajWQFZKwdikeoOasRBQS50ex9ky1MwtFVBq+YefVTXfQOUUT1v3VQwIDAQAB";
	private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJZBecO2bOF1fFNmu55KPoWpuhgCsQtjB4U/CUISkDW6/R+olHdNxTLHgr/Y2RoCgS1dTs43/Y2amnd42nTQ3uB5LzyRrgr51RM7XwHZ2qCcfe540+SpTKhqNZAVkrB2KR6g5qxEFBLnR7H2TLUzC0VUGr5h59VNd9A5RRPW/dVDAgMBAAECgYAPPntVENgBE8NWTtDwIUYwl2Sq9PLzXcuwiBGvY2TAHsV5hcfyRrCgEz+/qQd1rRVf/dx17ZZK3ImZX2iCe4JxCYjyR0/0kMPkXOlqcHyA6jfSCMrFBkZuJboMhb9zplS8Aaoaiix3li7LYN8UoCfYY8iRZS+LMO0Us5nkDldcoQJBAOHcFhdt/WGdn3dnJyBII8hbr5OVgu0GpPOYkgttYcbf4dL1UtBbXT+8qjNWY/NDkD9tR+ClOqSUW9WzfIeIe1MCQQCqTpNjy2WLQybiborp+LHvBLR1BI8M9MvmG4n2veAnW++/HYDeEFl0oPRySGJzUPIyokB+/avGaLl5TK+E6/BRAkEAtnpPauyVe8mSrjCsHtvJ9TWGXFG+buwgVyMcU5kzFy+Izx0fpHE8nKM8S6/vnvomT+hl2y0DfTbUH4sdAI+XOwJABpcTzQDJ5SGsF4b/iR4+hGlCMmUCxBWU7kqShCHE0sET/ek+W1l2nLS9wM5cQOoY9SyiIkfepcbXpoH5KMOYgQJAdZPpcjHDbJdqOWd4Qc7Nc5ls83+pot00v+tG04/Y77wcoa+YP6LhjdBT555Gap70PeelaBU1cro5dxjVW1TDnA==";
	
	private String publicKey2 = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJg4y/bi17nOvMwOyaerqjmJTnTTsx6qZqow200JXuOfG8WTDpDbR7YM6N2oal9dz2/RgqNuTm9qOaL2sif7gK0CAwEAAQ==";
	private String privateKey2 = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmDjL9uLXuc68zA7Jp6uqOYlOdNOzHqpmqjDbTQle458bxZMOkNtHtgzo3ahqX13Pb9GCo25Ob2o5ovayJ/uArQIDAQABAkEAkzY6auUuY89/3ohelPz7fvE/ItSpYXrCFs1xCKuLTO1Qh4x2snlOt3eMGM3AJjk+oGXgNq0YX2vHnKWZT1qkgQIhAM/Jd9UuP7wJO7BuOgzxBkxjiVbvv0AXuG8/9w0C9/elAiEAu4rDrdh8S0nI6vJ8PVQAR4lxxXHwCpeXyFEkCa7H1mkCIDTqh9wAhTar20mZKKt3DjWR+73qHHiEEfIzBgWIRgIJAiBdQScELv2/QjVR/rmglUB+Ue3szDccTT0AMrjEx9A+4QIgFIxSXNqr0Jbw44K89EgOol430eY0mogHnRZQTPy8gTE=";
	@Test
	public void signAndVerifyTest() throws Exception{
		String sign = Rsa2Sign.sign(dataTest, privateKey);
		assertTrue( Rsa2Sign.verify(dataTest, sign, publicKey) );
		assertFalse( Rsa2Sign.verify(dataTest + "1", sign, publicKey) );
		String sign2 = Rsa2Sign.sign(dataTest, privateKey2);
		assertTrue( Rsa2Sign.verify(dataTest, sign2, publicKey2) );
		assertFalse( Rsa2Sign.verify(dataTest + "1", sign2, publicKey2) );
	}
}
