package com.xmcc.config;


import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.StatViewServlet;
//import org.assertj.core.util.Lists;
import com.google.common.collect.Lists;//********
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;


@Configuration//表示配置类
public class DruidConfig {

    @Bean(value = "druidDataSource" , initMethod = "init",destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.druid")//导入application。yml的配置文件
    public DruidDataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter()));
        return druidDataSource;

    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setSlowSqlMillis(5);
        statFilter.setMergeSql(true);
        return statFilter;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }
}
