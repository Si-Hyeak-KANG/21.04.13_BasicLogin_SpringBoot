package com.sp.fc.web.student;
//통행증 발급: Authentication provider


import com.sp.fc.web.teacher.Teacher;
import com.sp.fc.web.teacher.TeacherAuthenticationToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component //bean으로 써야 되기 때문에 component 선언
public class StudentManager implements AuthenticationProvider, InitializingBean {

    //원칙적으로는 DB에 저장된 학생 정보(리스트)를 핸들링해야함. 지금은 테스트 HashMap(메모리객체)으로 대체
    private HashMap<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (studentDB.containsKey(token.getName())) {
                return getAuthenticationToken(token.getName());
            }
            return null;//내가 핸들링 할 수 없는 토큰은 null로 리턴 그래야 다음 토큰을 provider에게 넘어감.
        }
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
        if(studentDB.containsKey(token.getCredentials())){
           return getAuthenticationToken(token.getCredentials());
        }
        return null;
    }

    private StudentAuthenticationToken getAuthenticationToken(String id) {
        Student student = studentDB.get(id);
        return StudentAuthenticationToken.builder()
                .principal(student)
                .details(student.getUsername())
                .authenticated(true)
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class ||
                authentication == UsernamePasswordAuthenticationToken.class;
    }   //토큰을 받으면 검증해주는 provider 동작
        //httpBasic()->basicAuthenticationFilter는 기본적으로 username, password를 줘서 인증
        //그래서 UsernamePasswordAuthenticationToken도 핸들링링

    //teacher 아이디를 주면 그 teacherId에 링크된 학생list를 내려줌
    public List<Student> myStudent(String teacherId) {
        return studentDB.values().stream().filter(s->s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        Set.of(
                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"ko"),
                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"ko"),
                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")),"ko")
        ).forEach(s->
                studentDB.put(s.getId(), s)
        );

    }
}
