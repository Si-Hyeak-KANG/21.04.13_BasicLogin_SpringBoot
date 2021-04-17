# 21.04.17_session-management

# 세션 관리

토큰을 발급하고, 세션에는 토큰을 저장해 놓고 세션이 유지되는 동안, 혹은 remember-me 토큰이 있다면 해당 토큰이 살아있는 동안
로그인 없이 해당 토큰만으로 사용자를 인증하고 요청처리 함.

세션 관리 구성을 잘하여 정보를 탈취하려는 악의적인 사용자를 방지하여야 한다.


# ConcurrentSessionFilter
  SessionRegistry를 사용한다. 이 빈을 이용해 세션사용자(SessionInformation)를 모니터링 할 수 있음.
  
  만료된 세션에 대한 요청인 경우 세션 즉시 종료.
  세션 만료에 대한 판단은 SessionManagementFilter의 ConcurrentSessionControlAuthenticationStrategy 에서 처리
  
  문제점
  1) 서블릿 컨테이너(톰켓)로 부터 HttpSessionEventPublisher 를 리스닝 하도록 listener로 등록해야 함.
  2) 톰켓의 세션과는 별도로 session을 SessionRegistry의 SessionInfirmation 에서 관리함.
  3) SessionRegistry의 세션 정보를 expire 시키면 톰켓에서 세션을 아무리 허용하더라도 애플리케이션 내로 들어와서 활동할 수 없음.
  4) SessionRegistry에는 Authentication으로 등록된 사용자만 모니터링 함.
  
# SessionManagementFilter
  SessionAuthenticationStrategy 에서 여러가지 세션 인증 정책을 관리하도록 설정할 수 있음.
    
    1) 세션 생성 정책
    2) 세션 아이디 고정 설정
    3) 동시접근 허용 문제
    4) 세션 타임아웃 문제
  SessionFixationProtectionStrategy : 세션 고정 문제 해결
  ConcurrentSessionControlAuthenticationStrategy : 동시 세션의 개수 제한. RegisterSessionAuthenticationStrategy 와 함께 SessionRegistry 를 참조해 작업
  
  세션 정책

    Always : 항상 세션을 생성함
    If_Required : 필요시 생성
    Never : 생성하지 않음. 존재하면 사용함.
    Stateless : 생성하지 않음. 존재해도 사용하지 않음.
    session
    
# 세션관리 실습
  
  실시간으로 로그인한 사용자들의 세션 개수와 로그인한 URL을 모니터링 한다.
  
  3명의 사용자를 만든다.
  
  동시접속 제어를 테스트 한다.
  
  RememberMe 도 설정한다.
  
  remember me를 설정했을때
