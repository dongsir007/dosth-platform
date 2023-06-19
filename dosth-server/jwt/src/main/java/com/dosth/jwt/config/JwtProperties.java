package com.dosth.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Jwt属性配置
 * @author Zhidong.Guo
 *
 */
@Data
@ConfigurationProperties(prefix = "dosth.jwt")
@Component
public class JwtProperties {
	/**
	 * 是否开启JWT,即注入相关的类对象
	 */
	private Boolean enabled;
	/**
	 * JWT秘钥
	 */
	private String secret;
	/**
	 * 有效时间
	 */
	private Long expiration;
	/**
	 * 前端向后端传递JWT时使用HTTP的header名称,前后端要统一
	 */
	private String header;
	/**
	 * 用户登录-用户名参数名称
	 */
	private String userParamName = "userId";
	/**
	 * 用户登录-密码参数名称
	 */
	private String pwdParamName = "password";
	/**
	 * 是否使用默认的JWTAuthController
	 */
	private Boolean userDefaultController = false;
	/**
	 * 忽略请求地址
	 */
	private String skipValidUrl;
}