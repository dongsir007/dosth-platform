package com.dosth.gateway.filter;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import reactor.core.publisher.Mono;

/**
 * 全局token检验器
 * @author THINKPAD
 *
 */
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

	private static final String AUTHORIZE_TOKEN = "Authorization";
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String url_1 = "/login";
		String url = exchange.getRequest().getURI() + "";
		
		if (url.contains(url_1)) {
			return chain.filter(exchange);
		}
		
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		String token = headers.getFirst(AUTHORIZE_TOKEN);
		if (!stringRedisTemplate.hasKey(token)) {
			ServerHttpResponse response = exchange.getResponse();
			JSONObject message = new JSONObject();
			message.put("status", -1);
			message.put("data", "鉴权失败");
			byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
			DataBuffer buffer = response.bufferFactory().wrap(bits);
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			// 指定编码,否则在浏览器中中文出现乱码
			response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
			return response.writeWith(Mono.just(buffer));
		}
		return chain.filter(exchange);
	}

}
