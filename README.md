# 21.04.16_login-rememberme


Login 처리
  1) UsernamePasswordAuthentication
      -폼 로그인
      -세션 기반
      
  2) BasicAuthentication
      -클라이언트 로그인
      -세션(혹은 sessionless)
      
  3) OAuth2 로그인
      -로그인 위임
      
  4) JWT 토큰( Bearer토큰 )
      -클라이언트 로그인
      -SessionLess(혹은 session)
      
      
 # SecurityContextPersistenceFilter
 
 SecurutyContextRepository에 저장된 SecurityContext를 Request의 LocalThread에 넣어주었다가 뺏는 역할을 함.
 
 # RememberMeAuthenticationFilter
 인증 정보를 세션 관리하는 경우, 세션 timeout이 발생하게 되면, remember-me 쿠키를 이용해 로그인을 기억했다가 자동으로 재로그인 시켜주는 기능
 
      1. TokenBaseRememberMeServices : 토큰 기반
      
      포멧: 아이디:만료시간:Md5Hex(아이디:만료시간:비밀번호:인증키)
      만약 User가 password를 바꾼다면 토큰을 쓸 수 없게 됨.
      기본 유효기간은 14일이고 설정에서 바꿀 수 있음.
      약점 : 탈취 된 토큰은 비밀번호를 바꾸지 않는 한 유효기간 동안 만능키가 된다.
      
      
      2.PersistenceTokenBasedRememberMeServices : 
      
      포맷: series:token
      토큰에 username이 노출되지 않고, 만료시간도 노출되지 않음.
      만료시간은 서버에서 정하고 노출하지 않고 서버는 로그인 시간만 저장
      series 값이 키가 됨.( 일종의 채널 )
      대신 재로그인이 될 때마다 token 값을 갱신해 줌.
      --> 그래서 토큰이 탈취되어 다른 사용자가 다른 장소에서 로그인을 했다면 정상 사용자가 다시 로그인 할 때,
      CookieTheftException이 발생하게 되고, 서버는 해당 사용자로 발급된 모든 remember-me 쿠키값들을 삭제하고 재로그인을 요청
      InmemoryTokenRepository는 서버가 재시작하면 등록된 토큰들이 사라진다. 따라서 자동로그인을 설정했더라도 다시 로그인을 해야한다.
      재시작 후에도 토큰을 남기고 싶다면 JdbcTokenRepository를 사용하거나 이와 유사한 방법으로 토큰을 관리해야 한다.
 
 
  Rememberme로 로그인한 사용자는 UsernamePasswordAuthenticationToken이 아닌 RememberMeAuthenticationToken으로 서비스를 이용
  --> 같은 사용자이긴 하지만, 토큰의 종류가 다르게 구분되어 있음.
 
 
  #AnonymousAuthenticationFilter

  로그인 하지 않은 사용자 혹은 로그인이 검증되지 않은 사용자는 기본적으로 AnonymousAuthenticationToken 을 발급해 줍니다. anonymous를 허용한 곳만 다닐 수 있게 하겠다는 것입니다.
  익명사용자의 권한을 별도로 설정할 수 있고, 익명사용자에게 주는 principal 객체도 설계해서 대체해 줄 수 있습니다.
