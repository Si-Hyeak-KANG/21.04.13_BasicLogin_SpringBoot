package com.sp.fc.web.student;
//통행증 발급: provider


import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component //bean으로 써야 되기 때문에 component 선언
public class StudentManager implements AuthenticationProvider, InitializingBean {

    //원칙적으로는 DB에 저장된 학생 정보(리스트)를 핸들링해야함. 지금은 테스트 HashMap(메모리객체)으로 대체
    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;

        if(studentDB.containsKey(token.getCredentials())){
            Student student = studentDB.get((token.getCredentials()));
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class;
        //토큰을 받으면 검증해주는 provider 동작
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Set.of(
                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")))
        ).forEach(s->
                studentDB.put(s.getId(), s)
        );

    }
}
