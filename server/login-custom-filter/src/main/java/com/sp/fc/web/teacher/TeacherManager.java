package com.sp.fc.web.teacher;
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
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    //원칙적으로는 DB에 저장된 학생 정보(리스트)를 핸들링해야함. 지금은 테스트 HashMap(메모리객체)으로 대체
    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

       TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;

        if(teacherDB.containsKey(token.getCredentials())){
            Teacher teacher = teacherDB.get((token.getCredentials()));
            return TeacherAuthenticationToken.builder()
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class;
        //토큰을 받으면 검증해주는 provider 동작
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Set.of(
                new Teacher("ko", "국어선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s->
                teacherDB.put(s.getId(), s)
        );

    }
}
