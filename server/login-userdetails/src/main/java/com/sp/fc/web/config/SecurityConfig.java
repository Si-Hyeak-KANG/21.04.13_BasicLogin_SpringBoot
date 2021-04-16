package com.sp.fc.web.config;

import com.sp.fc.user.srvice.SpUserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)//user는 user,admin은 admin
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //로그인 정보의 details 조작
    private final SpUserService userService;

    public SecurityConfig(SpUserService userService) {
        this.userService = userService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userService);
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }//ADMIN에게 USER 권한도 접근이 가능하게 설정

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests-> {
                    requests
                            .antMatchers("/").permitAll()
                            .anyRequest().authenticated()
                            ;
                })
        //      UserNamePassword 설정
                //login 페이지를 특정해주지 않으면 default필터와 UserNamePassword 필터 동시 적용
                //그래서 폼로그인에 로그인 설정해줌
                .formLogin(
                        login->login.loginPage("/login")
                        .permitAll()//로그인 페이지에 대한 무한루프 방지
                        .defaultSuccessUrl("/",false)
                        //로그인을 하고 갈 곳이 없다면 메인페이지로, alwaysuse<false> 로그인 하고 내가 가려는 페이지 유지/ true는 다시 메인으로
                        .failureUrl("/login-error")//로그인 실패시 보내줄 Url
                )
                .logout(logout->logout.logoutSuccessUrl("/"))//로그아웃 이후 갈 Url
                .exceptionHandling(exception->exception.accessDeniedPage("/access-denied"))//잘못된 접근시 나올 Url 설정
                ;
    }
    //시큐리티를 적용해서 모든 페이지(웹 리소스까지) 문제 생김
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console() //h2 접근 Path
                )
                ;
    }
}
