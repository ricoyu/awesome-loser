package com.loserico.codec;

import com.loserico.common.lang.resource.PropertyReader;
import com.loserico.common.lang.utils.IOUtils;
import org.junit.Test;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * Copyright: (C), 2020-08-28 16:50
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class RsaUtilsTest {
	
	@Test
	public void testPublicKey() {
		System.out.println(RsaUtils.getPublicKeyStr());
	}
	
	@Test
	public void testPublicEncrypt() {
		//System.out.println(RsaUtils.getPublicKeyStr());
		//long millis = System.currentTimeMillis();
		//System.out.println(millis);
		System.out.println(RsaUtils.publicEncrypt("123456"));
		//System.out.println(RsaUtils.publicEncrypt("123456", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm0TOI2rlCmYWDVDu3EJvvdxK6/esXgF+iPvZQPO1cLxe+2JU5cqthD4mB8tOz6J8A31oAtS82hVkEzHv6d/dDOiAOJ93oKOd4+3+PKNtN09TV4YAsMu5oRnGd5SwXNDJX88v6oiHfTwMYAOxdNeACK9ySKYZYdbZZUZ9pnmC1mbeHjdeVAaFZgmkfg7EJifZ8T/Fz2rAKDIdHNNVCMHPYvzkNYmzX6AvqM9sJB9f38YuUxf1jbC0x/QAYwEiAZgqJ7U091FCkRmQBC4gOsHuY1AfyPNslRnBGKlGDJtPePWy1H08BQ5hy0ZorD/ZX9vClksnoCY38EqwHk+CSByHcQIDAQAB"));
	}
	
	
	@Test
	public void testRsaChecksum() {
		String checksum = "TrdDq1JOH7Ov6djQLqCbUg72aikW9kW7Fr6XYYRcvHfq1sNqiR7c7nIFOmTRd/Srsb/uS/u4CyCN5PP8gB9UJWqqHC3RH2f5qxhuAEB3r9o3tndvgjn5UibD3HaTxXQIcGGT/wKCG19CHJf6yflWEmL7+JPRlO7PYMW3xOhfHroNJrkcn1TofC3r7dKW8vrKrRjhvhi5f1EQcK4YMEAgn0HBdbSDjA7Zc8MbnibRp/1QgrKUDDGSYrr1vPQg78fC2geUBOl6UvC3EDFhA+5TWpURFUuSenjRLRUQKrfIWty3p7Svi/lPKBuifgAsGEPjkDha5e7KehlqfsDn+xwg2g==";
		String secretKey = "4b2ef0867a4af53076cdea03f210b18b";
		String timestamp = "2020-09-07 10:10:00";
		String myChecksum = RsaUtils.publicEncrypt(timestamp+secretKey);
		System.out.println(myChecksum);
		String decrypt = RsaUtils.privateDecrypt(myChecksum);
		
		assertThat(decrypt.equals(timestamp+secretKey)).isTrue();
	}
	
	@Test
	public void testRsaUtilsReInitialize() {
		/*String publicKeyStr = RsaUtils.getPublicKeyStr();
		String privateKeyStr = RsaUtils.getPrivateKeyStr();
		
		String encrypt = RsaUtils.publicEncrypt("hi", publicKeyStr);
		String decrypted = RsaUtils.privateDecrypt(encrypt, privateKeyStr);
		assertThat(decrypted.equals("hi")).isTrue();*/
		
		String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl5W7NPL26ys2s6kJJo52vj+QLw+FQb4BeU8bmTY0hlYb9dHcdKrM0Ah93nTM6t6sxrSKV50Nr41WyfCT5enXjsPtYnGcymt/tbNlcdQ7rh8E5/peAjYvZLPxXD/N88WGvgwytL6uoJBdyDwaHN6o6YQfpydHZ7cMRuk1nWLm2V274YmgFNIn/4mjqDgSbjp1e/WWh2kjQNLhwiKgPpymwj4prmNGJgAHapOhW2rvC83goPqEq11PSTNw90+jWcJz6Q9qIIeGpWu/P8NfiFw2S5zaEaQJgD/S+P2obzsSNcdrKj6ylSkFxNPK2qnxgKgWbt/0YF3FDjkunhUAuj+YsQIDAQAB";
		String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXlbs08vbrKzazqQkmjna+P5AvD4VBvgF5TxuZNjSGVhv10dx0qszQCH3edMzq3qzGtIpXnQ2vjVbJ8JPl6deOw+1icZzKa3+1s2Vx1DuuHwTn+l4CNi9ks/FcP83zxYa+DDK0vq6gkF3IPBoc3qjphB+nJ0dntwxG6TWdYubZXbvhiaAU0if/iaOoOBJuOnV79ZaHaSNA0uHCIqA+nKbCPimuY0YmAAdqk6Fbau8LzeCg+oSrXU9JM3D3T6NZwnPpD2ogh4ala78/w1+IXDZLnNoRpAmAP9L4/ahvOxI1x2sqPrKVKQXE08raqfGAqBZu3/RgXcUOOS6eFQC6P5ixAgMBAAECggEAZvBGfjGUnqTs89qRnT5XwuB1cI/wpXF7nL6s9kGKL8PgHvIR7KRki82IdH4K/mCTIilOXSsJNTMdQet+9D31pTzGQvj2SbvwsRqPhFpk4Nytvz8Vghj73D4aZNPrb2SvGPY9pOJswhrnpOezr/FA8btIp4GUCwmSa8FJKIUWUXs1QTVP2W+1YdtCWvam8AwiQMnZWoE2kYem1Q0w/ZpiARS0cApkfMHu8WbJdl2Japywr20wQiCz4gABTRzrW/eaKcXMi8Y2/6+6oaeigXZ9Cw4gyBm/sLviGnoAIQdm7pBnM2WsVddmOSvZMlzj6//S1/8sTwG3M7kKmO5KQconxQKBgQDz7eDzlkwfyHSHmam5P45mnHjt1xfZkpj55tc90eEwUZu28h1XBGijuTsP9M01a83605jU+kcrYzuapVp+OPqWkKFt7ACEJI7+MIonPNpK4BeQyWhSLzySRKwRS7wU5PQ8zX77bkC6Tf/SATItSfDZxsv7wGO9B5+kFvJKfxfAXwKBgQCfFgZY8g1zVxbtEmjyyLbwgISl20zYwudpbnmUcOORqYxSh9H3rBzOiwJlRm7J6JRsLBAa7XA7BJlZtdqoy7NhY9GydBKpfrZrfmw8IweRdXQiXhI4n07O7593JpT4ADw2EMrUhQIwziyX8QuiuyS95DSMuBP+01dB0hkyWc0A7wKBgHxTgzNEKJqOde5jQKFdpnvgRraaydooIPZGVPB6V7dj8OTG9HNUmOzRzSutzyXqQ3sTfDMZOUsA8fE5VC0/Z2F0aAL5pa20YHloThH/rxc4rmoqbZRt5QZ8H+NU4ZEYvO98obsPD8ilVr03xNZWeH7XF0LCZVOEXjapR2b27ikrAoGAcKgeMpqm38kKmTPyUgvFUScRyIo2L0JGsijjvDT9UseocUFxdVzSwYk7VxC7oAwuHzRM62S9l224UwkvIV2vWAZiF2ePV6w97n2GlEeSMnXkmfnYGXTnUC/s02nlEILKINdfa4QNexZmCetO/Bxe9oJGnGg28LwlDJ2F7lwdis0CgYEA2MyzZ9YeJB5fG8ukFY/9Lxx4O4C8RhKqFBfcHzhkatxOsGWauEoa45xO/AKozmMgRs8Sys5boc5xzjJYh56SomBdcL2nHsYzzHwXR2AKNxKcdzgokOUM7nHdcjOZhdrx7X38nGTHl0TWaW9KUbAFaoEJvFhFfGGspj/3xVRPxMY=";
		RsaUtils.reinitialize(publicKeyStr, privateKeyStr);
	}
	
	@Test
	public void testReadPemPublicKey() {
		String publicKey = IOUtils.readClassPathFileAsString("public.pem");
		System.out.println(publicKey);
	}
	
	@Test
	public void testReadKeyFromClasspath() {
		PropertyReader propertyReader = new PropertyReader("codec.properties");
		
		String location = propertyReader.getString("location");
		String publicKey = propertyReader.getString("publicKey");
		String privateKey = propertyReader.getString("privateKey");
		
		if (isNotBlank(location) && location.startsWith("classpath:")) {
			location = location.replace("classpath:", "");
		}
		
		if (!location.endsWith("/")) {
			location += "/";
		}
		
		publicKey = location + publicKey;
		privateKey = location + privateKey;
		
		String publicKeyStr = IOUtils.readClassPathFileAsString(publicKey);
		System.out.println(publicKeyStr);
		
		String privateKeyStr = IOUtils.readClassPathFileAsString(privateKey);
		System.out.println(privateKeyStr);
	}
}
