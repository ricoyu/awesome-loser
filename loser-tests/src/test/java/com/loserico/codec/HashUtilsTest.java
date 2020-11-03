package com.loserico.codec;

import com.loserico.common.lang.utils.DateUtils;
import com.loserico.common.lang.utils.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class HashUtilsTest {
	
	@Test
	public void testHashing() {
//		System.out.println(join(":", "smscode", sha256("18013157315")));
//		System.out.println(join(":", "smscode", sha256("13222211882")));
//		System.out.println(join(":", "smscode", sha256("15988878640")));
//		System.out.println(join(":", "smscode", sha256("18260065504")));
//		System.out.println(join(":", "smscode", sha256("13913995910")));
//		System.out.println(join(":", "smscode", sha256("15862316530")));
		
//		System.out.println(join(":", "smscode", sha256("18260065504")));
//		System.out.println(join(":", "smscode", sha256("15651789172")));
//		System.out.println(join(":", "smscode", sha256("18260065504")));
//		System.out.println(join(":", "smscode", sha256("13913582189")));
//		System.out.println(join(":", "smscode", sha256("15062401898")));
//		System.out.println(join(":", "smscode", sha256("18260065504")));
//		System.out.println(join(":", "smscode", sha256("18051908251")));
//		System.out.println(join(":", "smscode", sha256("13771120212")));
//		System.out.println(join(":", "smscode", sha256("13222211882")));
//		System.out.println(join(":", "smscode", sha256("13915587353")));
//		System.out.println(join(":", "smscode", sha256("13913582817")));
//		System.out.println(join(":", "smscode", sha256("13812632965")));
//		System.out.println(join(":", "smscode", sha256("13912799494")));
//		System.out.println(join(":", "smscode", sha256("18260448427")));
	}

	@Test
	public void testSha1() {
//		String ticket = "HoagFKDcsGMVCIY2vOjf9gIe5APOsH82KC2MgUyDawx5V8caTh3v8I7kYzlZwxGVrKlHLMBtdsK1FpnUercLmg";
//		String timestamp = Long.toString(new Date().getTime());
//		System.out.println("timestamp:" + timestamp);
//		String noncestr = RandomStringUtils.randomAlphabetic(16);
//		System.out.println("noncestr:"+noncestr);
//		String url = "http://mulberrylearning.cn/reg/event.html?qrcodeid=1";
//		System.out.println("url:"+url);
//		String resultStr = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
//		//System.out.println(resultStr);
//		String signature = HashUtils.sha1(resultStr);
//		String signature2 = DigestUtils.sha1Hex(resultStr);
//		System.out.println("signature:"+signature);
//		System.out.println("signature2:"+signature2);
		
		//String shaHash = Hashing.sha1().hashString("password", UTF_8).toString();
		//System.out.println(shaHash);
		//shaHash = HashUtils.sha1("password");
		//System.out.println(shaHash);
		//shaHash = DigestUtils.sha1Hex("password");
		//System.out.println(shaHash);
	}
	
	@Test
	public void testRandom() {
		System.out.println(RandomStringUtils.randomAlphabetic(16));
	}
	
	@Test
	public void testMD5() {
		//String token = HashUtils.md5("BFPbEkNmW4cLpc17jp5S4fZOLJzd1Ea2Mv5M0KTQ2v87X96YRbjT609UNLgColmmrd6aFQXlXcFaqOzIvSbcRZ2xkpo1BIwOC8D");
		//System.out.println(token);
		//System.out.println(HashUtils.md5("BFPbEkNmW4cLpc17jp5S4fZOLJzd1Ea2Mv5M0KTQ2v87X96YRbjT609UNLgColmmrd6aFQXlXcFaqOzIvSbcRZ2xkpo1BIwOC8D"));
		//System.out.println(HashUtils.md5("/api-centre/statistic/2017F?access-token=BFPbEkNmW4cLpc17jp5S4fZOLJzd1Ea2Mv5M0KTQ2v87X96YRbjT609UNLgColmmrd6aFQXlXcFaqOzIvSbcRZ2xkpo1BIwOC8D"));
	}
	
	@Test
	public void testMD5Checksum() {
		String checksum = "bc611a61faa02d2b20aa659af7111bba";
		String secretKey = "4b2ef0867a4af53076cdea03f210b18b";
		long timestamp = DateUtils.toEpochMilis("2020-09-07 10:10:00");
		String myChecksum = HashUtils.md5Hex(timestamp + secretKey);
		assertThat(checksum.equals(myChecksum)).isTrue();
	}
	
	@Test
	public void testDegistTest() {
		byte[] bytes = IOUtils.readFileAsBytes("D:\\aa.pcap");
		String hash = HashUtils.sha256(bytes);
		String encrypt = RsaUtils.privateEncrypt(hash);
		
		String decrypt = RsaUtils.publicDecrypt(encrypt);
		assertEquals(decrypt, HashUtils.sha256(IOUtils.readFileAsBytes("D:\\aa.pcap")));
	}
	
	@Test
	public void testSha256Hash() {
		String hash = HashUtils.sha256("deepdata$");
		System.out.println(hash);
		assertEquals(hash, "3af7b98967ca04ad21ef6512a561e27b00bf4e8501772fe0e5273175998e7c2b");
	}
}
