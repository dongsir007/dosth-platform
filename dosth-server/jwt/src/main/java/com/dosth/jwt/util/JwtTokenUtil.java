package com.dosth.jwt.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.dosth.jwt.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JwtToken工具类
 * @author Zhidong.Guo
 *
 */
@Component
public class JwtTokenUtil {
	
	private static final String JWT_CACHE_KEY = "jwt:userId:";
	private static final String USER_NAME = "username";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String EXPIRE_IN = "expire_in";
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private JwtProperties jwtProperties;
	
	public Map<String, Object> generateTokenAndRefreshToken(String userId, String username) {
		Map<String, Object> tokenMap = this.buildToken(userId, username);
		this.cacheToken(userId, tokenMap);
		return tokenMap;
	}
	
	public Map<String, Object> refreshTokenAndGenerateToken(String userId, String username) {
		Map<String, Object> tokenMap = this.buildToken(userId, username);
		this.stringRedisTemplate.delete(JWT_CACHE_KEY + userId);
		this.cacheToken(userId, tokenMap);
		return tokenMap;
	}
	
	public boolean removeToken(String userId) {
		return Boolean.TRUE.equals(this.stringRedisTemplate.delete(JWT_CACHE_KEY + userId));
	}
	
	/**
	 * 从令牌中获取sub
	 * @param token token
	 * @return
	 */
	public String getSubFromToken(String token) {
		String sub;
		try {
			Claims claims = this.getClaimsFromToken(token);
			sub = claims.getSubject();
		} catch (Exception e) {
			sub = null;
		}
		return sub;
	}
	
	/**
	 * 从令牌获取用户名
	 * @param token token
	 * @return
	 */
	public String getUserNameFromToken(String token) {
		String username;
		try {
			Claims claims = this.getClaimsFromToken(token);
			username = (String) claims.get(USER_NAME);
		} catch (Exception e) {
			username = null;
		}
		return username;
	}
	
	/**
	 * 判断令牌是否不存在redis中
	 * @param token 刷新令牌
	 * @return true 不存在 false 存在
	 */
	public Boolean isRefreshTokenNotExistCache(String token) {
		String userId = getSubFromToken(token);
		String refreshToken = (String) this.stringRedisTemplate.opsForHash().get(JWT_CACHE_KEY + userId, REFRESH_TOKEN);
		return refreshToken == null || !refreshToken.equals(token);
	}
	
	/**
	 * 判断令牌是否过期
	 * @param token 令牌
	 * @return
	 */
	public Boolean isTokenExpired(String token) {
		try {
			Claims claims = this.getClaimsFromToken(token);
			Date expiration = claims.getExpiration();
			return expiration.before(new Date());
		} catch (Exception e) {
			// 验证Jwt失败等同于令牌过期
			return true;
		}
	}
	
	/**
	 * 刷新令牌
	 * @param token 原令牌
	 * @return 新令牌
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			Claims claims = this.getClaimsFromToken(token);
			claims.put("created", new Date());
			refreshedToken = this.generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}
	
	/**
	 * 验证令牌
	 * @param token 令牌
	 * @param userId 用户Id
	 * @return 是否有效
	 */
	public Boolean validateToken(String token, String userId) {
		String sub = getSubFromToken(token);
		return sub.equals(userId) && !isTokenExpired(token);
	}

	/**
	 * 从令牌中获取数据声明,验证JWT签名
	 * @param token 令牌
	 * @return
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(this.jwtProperties.getSecret()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Map<String, Object> buildToken(String userId, String username) {
		String accessToken = this.generateToken(userId, username, null);
		String refreshToken = this.generateRefreshToken(userId, username, null);
		Map<String, Object> tokenMap = new HashMap<>();
		tokenMap.put(ACCESS_TOKEN, accessToken);
		tokenMap.put(REFRESH_TOKEN, refreshToken);
		tokenMap.put(EXPIRE_IN, this.jwtProperties.getExpiration());
		return tokenMap;
	}

	private String generateToken(String userId, String username, Map<String, String> payloads) {
		Map<String, Object> claims = this.buildClaims(userId, username, payloads);
		return this.generateToken(claims);
	}

	private String generateRefreshToken(String userId, String username, Map<String, String> payloads) {
		Map<String, Object> claims = this.buildClaims(userId, username, payloads);
		return this.generateRefreshToken(claims);
	}

	/**
	 * 生成刷新令牌refreshToken,有效期是令牌的2倍
	 * @param claims 数据声明
	 * @return
	 */
	private String generateRefreshToken(Map<String, Object> claims) {
		Date expirationDate = new Date(System.currentTimeMillis() + this.jwtProperties.getExpiration() * 2);
		return Jwts.builder().setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, this.jwtProperties.getSecret())
				.compact();
	}

	/**
	 * 生成令牌
	 * @param claims 数据声明
	 * @return 令牌
	 */
	private String generateToken(Map<String, Object> claims) {
		Date expirationDate = new Date(System.currentTimeMillis() + this.jwtProperties.getExpiration());
		return Jwts.builder().setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, this.jwtProperties.getSecret())
				.compact();
	}

	private Map<String, Object> buildClaims(String userId, String username, Map<String, String> payloads) {
		int payloadSize = payloads == null ? 0 : payloads.size();
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userId);
		claims.put("username", username);
		claims.put("created", new Date());
		if (payloadSize > 0) {
			claims.putAll(payloads);
		}
		return claims;
	}

	private void cacheToken(String userId, Map<String, Object> tokenMap) {
		this.stringRedisTemplate.opsForHash().put(JWT_CACHE_KEY + userId, ACCESS_TOKEN, tokenMap.get(ACCESS_TOKEN));
		this.stringRedisTemplate.opsForHash().put(JWT_CACHE_KEY + userId, REFRESH_TOKEN, tokenMap.get(REFRESH_TOKEN));
		this.stringRedisTemplate.expire(userId, this.jwtProperties.getExpiration() * 2, TimeUnit.MILLISECONDS);
	}
}