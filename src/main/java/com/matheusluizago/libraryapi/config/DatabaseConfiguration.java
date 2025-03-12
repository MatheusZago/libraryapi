package com.matheusluizago.libraryapi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;


    //        //DATA SOURCE MAIS SIMPLES, MAS NÃO RECOMENDADO PARA PRODUÇÃO
//    @Bean
//    public DataSource dataSource(){
////        DriverManagerDataSource datasource = new DriverManagerDataSource();
////        datasource.setUrl(url);
////        datasource.setUsername(username);
////        datasource.setPassword(password);
////        datasource.setDriverClassName(driver);
////
////        return datasource;
//    }

    //HIKARI É O MAIS RECOMENDADO PARA USO EM PRODUÇÃO
    @Bean
    public DataSource hikariDataSource(){
        HikariConfig config  = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMaximumPoolSize(10); //Máximo de conexões q libera
        config.setMinimumIdle(1); //Minimo liberado já de inicio de conexões
        config.setPoolName("library-db-pool");
        config.setMaxLifetime(600000); //Qnt tempo dura uma conexão em milisegundos
        config.setConnectionTimeout(100000); //Tempo pra tentar conseguir a mesma conexão
        config.setConnectionTestQuery("select 1");//Quero simples pra testar a conexão com o bd

        return new HikariDataSource(config);
    }


}
