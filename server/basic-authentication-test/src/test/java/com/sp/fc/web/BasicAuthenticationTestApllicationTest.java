package com.sp.fc.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicAuthenticationTestApllicationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    public String greetingUrl() {

        return "http:localhost:"+port+"/greetingUrl";
    }

    @DisplayName("1. 인증 실패")
    @Test
    void test_1(){

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });
        assertEquals(401,exception.getRawStatusCode());

    }

    @DisplayName("2. 인증 성공")
    @Test
    void test_2(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,"Basic "+ Base64.getEncoder().encodeToString(
                "user1:1111".getBytes()
        ));
        HttpEntity entity = new HttpEntity(null, headers);

        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        assertEquals("hello", resp.getBody());
    }

    @DisplayName("3. 인증 성공 2")
    @Test
    void test_3(){

        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");//TestRestTemplate는 기본적으로 basic토큰을 지원
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);
    }

    @DisplayName("4. POST 인증")
    @Test
    void test_4(){
        TestRestTemplate testClient = new TestRestTemplate("user1","1111");
        ResponseEntity<String> resp = testClient.postForEntity(greetingUrl(), "kang", String.class);
        assertEquals("hello kang", resp.getBody());
    }//POST에 대해서는 데이터 오류 -> csrf필터가 작동 중


}