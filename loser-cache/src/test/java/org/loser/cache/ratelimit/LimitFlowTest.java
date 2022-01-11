package org.loser.cache.ratelimit;

import com.loserico.cache.JedisUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * 令牌桶算法提及到输入速率和输出速率，当输出速率大于输入速率，那么就是超出流量限制了。
 * 也就是说我们每访问一次请求的时候，可以从Redis中获取一个令牌，如果拿到令牌了，那就说明没超出限制，而如果拿不到，则结果相反。
 * <p>
 * Copyright: (C), 2021-12-29 21:25
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class LimitFlowTest {
	
	/**
	 * 获取令牌
	 * @param id
	 * @return boolean
	 */
	public boolean limitFlow(Long id){
		Object result = JedisUtils.LIST.lpop("limit_list");
		if(result == null){
			log.warn("当前令牌桶中无令牌");
			return false;
		}
		return true;
	}
	
	/**
	 * 再依靠Java的定时任务，定时往List中rightPush令牌，当然令牌也需要唯一性，所以我这里还是用UUID进行了生成
	 * 
	 * 针对这些限流方式我们可以在AOP或者filter中加入以上代码，用来做到接口的限流，最终保护你的网站。
	 */
	public void setTicket() {
		JedisUtils.LIST.rpush("limit_list", UUID.randomUUID().toString());
	}
}
