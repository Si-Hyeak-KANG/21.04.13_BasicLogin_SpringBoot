# 21.04.12_BasicLogin_SpringBoot


웹과 모바일 서비스
(BasicAuthenticaionFilter)

-상황

1)웹으로 잘 만든 사이트를 모바일로도 서비스 할 때.

2)모바일 클라이언트 브라우절를 이용한 하이브리드 방식으로 개발

3)시간이 없어 JWT 토큰 기반으로 만들기가 어려울 때

4)Basic 토큰을 이용해 기존 서비스를 api로 이용하고 싶을 때

-예제 서비스

1) 선생님과 학생이 각각 로그인을 한다.

2) 선생님은 모바일을 통해 학생 리스트를 조회할 수 있다.

-구현

멀티 체인을 구성해 서비스를 구현

MobSecurityConfig 의 필터( /api/** )

-SecurityFilter

-BasicAuthenticationFilter

SecurityFilter 의 필터( /** )

-SecurityFilter

-CustomLoginFilter

-SecurityFilter


현 예제는 test라 studnet, teacher를 메모리에서 핸들링 했지만 , 실제 프로젝트에서는 DB를 핸들링
