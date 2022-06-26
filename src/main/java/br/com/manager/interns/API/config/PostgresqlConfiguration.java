package br.com.manager.interns.API.config;

import java.io.Serializable;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class PostgresqlConfiguration implements Serializable {

  @Value("jdbc:postgresql://localhost:5432/api_interns_manager")
  private String url;

  @Value("postgres")
  private String username;

  @Value("postgres")
  private String password;

  @Value("org.postgresql.Driver")
  private String driverClassName;

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setDriverClassName(driverClassName);
    return dataSource;
  }

}
