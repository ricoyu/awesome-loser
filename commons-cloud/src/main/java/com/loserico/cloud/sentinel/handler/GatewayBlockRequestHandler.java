package com.loserico.cloud.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.DefaultBlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.loserico.common.lang.errors.ErrorTypes;
import com.loserico.common.lang.vo.Result;
import com.loserico.common.lang.vo.Results;
import com.loserico.json.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * <p>
 * Copyright: (C), 2022-09-24 15:45
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public class GatewayBlockRequestHandler extends DefaultBlockRequestHandler {
	
	@Override
	public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable ex) {
		if (acceptsHtml(exchange)) {
			return htmlErrorResponse(ex);
		}
		
		// JSON result
		return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
				.contentType(MediaType.APPLICATION_JSON)
				.body(fromValue(buildErrorResult(ex)));
	}
	
	private boolean acceptsHtml(ServerWebExchange exchange) {
		try {
			List<MediaType> acceptedMediaTypes = exchange.getRequest().getHeaders().getAccept();
			acceptedMediaTypes.remove(MediaType.ALL);
			MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
			return acceptedMediaTypes.stream()
					.anyMatch(MediaType.TEXT_HTML::isCompatibleWith);
		} catch (InvalidMediaTypeException ex) {
			return false;
		}
	}
	
	private Mono<ServerResponse> htmlErrorResponse(Throwable ex) {
		return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(new String(JacksonUtils.toJson(buildErrorResult(ex))));
	}
	
	
	private Result buildErrorResult(Throwable ex) {
		if (ex instanceof ParamFlowException) {
			return Results.status(ErrorTypes.TOO_MANY_REQUESTS).build();
		} else if (ex instanceof DegradeException) {
			return Results.status(ErrorTypes.TOO_MANY_REQUESTS).build();
		} else {
			return Results.status(ErrorTypes.TOO_MANY_REQUESTS).build();
		}
	}
}
