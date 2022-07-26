package br.com.manager.interns.API;

import br.com.manager.interns.API.config.CustomKeyGenerator;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class ApplicationConfig extends CachingConfigurerSupport {

  @Override
  @Bean("CustomKeyGenerator")
  public KeyGenerator keyGenerator() {
    return new CustomKeyGenerator();
  }

}
