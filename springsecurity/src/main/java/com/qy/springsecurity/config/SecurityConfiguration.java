package com.qy.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication()
//        auth.inMemoryAuthentication()
//                .withUser("qy")
//                .password(bcryptPasswordEncoder().encode("123"))
//                .roles("admin");

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bcryptPasswordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());
    }

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http   // 配置登录页并允许访问
                //.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/login").permitAll() 需要专门的loginPage
            .formLogin().usernameParameter("username").passwordParameter("password").permitAll()
            // 配置Basic登录
            .and().httpBasic()
            // 配置登出页面
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
            // 开放接口访问权限，不需要登录授权就可以访问
            .and().authorizeRequests().antMatchers("/oauth/**", "/login/**", "/logout/**").permitAll()
            // api接口需要admin管理员才能访问
            .antMatchers("/api/**").hasRole("admin")
            // 其余所有请求全部需要鉴权认证
            .anyRequest().authenticated()
            // 关闭跨域保护;
            .and().csrf().disable();
    }

}
