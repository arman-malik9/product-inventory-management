package com.reactiveApp.configurations;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
 
import lombok.extern.slf4j.Slf4j;
 
@Configuration
@Slf4j
public class RedisConfig {
//	@Value("${spring.data.redis.host}")
//	private String redisHost;
//	
//	@Value("${spring.data.redis.port}")
//	private int redisPort;

//	@Bean
//	public JedisConnectionFactory jedisConnectionFactory()
//	{
//		//JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//		configuration.setHostName(redisHost);
//		configuration.setPort(redisPort);
//		return new JedisConnectionFactory(configuration);
//	}
//	
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate()
//	{
//		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//		template.setConnectionFactory(jedisConnectionFactory());
//		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//		return template;
//		
//	}
	@Value("${spring.data.redis.host}")
	private String host;
	@Value("${spring.data.redis.password}")
    private String password;
	@Value("${spring.data.redis.port}")
	private int port;
	@Value("${spring.data.redis.username}")
	private String username;
    @Bean
    @Primary
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisConfiguration defaultRedisConfig) {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().build();
        return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
    }
    @Bean
    public RedisConfiguration defaultRedisConfig() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setUsername(username);
        config.setPassword(RedisPassword.of(password));
        return config;
    }
}
 
