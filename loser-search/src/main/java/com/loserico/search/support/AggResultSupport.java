package com.loserico.search.support;

import com.loserico.search.vo.AggResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.InternalCardinality;
import org.elasticsearch.search.aggregations.metrics.InternalMax;
import org.elasticsearch.search.aggregations.metrics.InternalMin;
import org.elasticsearch.search.aggregations.metrics.InternalSum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2021-06-21 21:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class AggResultSupport {
	
	public static Map<String, Object> compositeResult(Aggregations aggregations) {
		Map<String, Object> result = new HashMap<>();
		for (Aggregation aggregation : aggregations) {
			if (aggregation instanceof InternalMax) {
				Double value = ((InternalMax) aggregation).getValue();
				String name = aggregation.getName();
				result.put(name, value);
			}
			
			if (aggregation instanceof InternalMin) {
				Double value = ((InternalMin) aggregation).getValue();
				String name = aggregation.getName();
				result.put(name, value);
			}
			
			if (aggregation instanceof InternalAvg) {
				Double value = ((InternalAvg) aggregation).getValue();
				String name = aggregation.getName();
				result.put(name, value);
			}
			
			if (aggregation instanceof LongTerms) {
				String name = aggregation.getName();
				List<LongTerms.Bucket> buckets = ((LongTerms) aggregation).getBuckets();
				Map<Object, Long> value = new HashMap<>(buckets.size());
				for (LongTerms.Bucket bucket : buckets) {
					Object key = bucket.getKey();
					long docCount = bucket.getDocCount();
					log.info("Bucket: {}, Doc Count: {}", key, docCount);
					value.put(key, docCount);
				}
				result.put(name, value);
			}
			
			if (aggregation instanceof StringTerms) {
				String name = aggregation.getName();
				List<StringTerms.Bucket> buckets = ((StringTerms) aggregation).getBuckets();
				Map<String, Long> value = new HashMap<>(buckets.size());
				for (StringTerms.Bucket bucket : buckets) {
					String key = bucket.getKeyAsString();
					long docCount = bucket.getDocCount();
					log.info("Bucket: {}, Doc Count: {}", key, docCount);
					value.put(key, docCount);
				}
				result.put(name, value);
			}
		}
		
		return result;
	}
	
	public static List<AggResult> termsResult(Aggregations aggregations) {
		List<AggResult> aggResults = new ArrayList<>();
		for (Aggregation aggregation : aggregations) {
			List<StringTerms.Bucket> buckets = ((StringTerms) aggregation).getBuckets();
			for (StringTerms.Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				long docCount = bucket.getDocCount();
				log.info("Bucket: {}, Doc Count: {}", key, docCount);
				aggResults.add(new AggResult(key, docCount));
			}
		}
		
		return aggResults;
	}
	
	public static Double avgResult(Aggregations aggregations) {
		for (Aggregation aggregation : aggregations) {
			return ((InternalAvg) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Long cardinalityResult(Aggregations aggregations) {
		for (Aggregation aggregation : aggregations) {
			return ((InternalCardinality) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Double maxResult(Aggregations aggregations) {
		for (Aggregation aggregation : aggregations) {
			return ((InternalMax) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Double minResult(Aggregations aggregations) {
		for (Aggregation aggregation : aggregations) {
			return ((InternalMin) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Double sumResult(Aggregations aggregations) {
		for (Aggregation aggregation : aggregations) {
			return ((InternalSum) aggregation).getValue();
		}
		
		return null;
	}
}
