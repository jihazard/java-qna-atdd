package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;



    @Test
    public void createForm() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/users/form", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
    }



    @Test
    public void 질문_생성_로그인유저() throws Exception {
        String title = "질문생성테스트";
        ResponseEntity<String> response =  template()
                .withBasicAuth("yoon","test")
                .postForEntity("/question/create", HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("title", title)
                .addParameter("contents","질문생성테스트_컨텐츠")
                .build(), String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertNotNull(questionRepository.findByTitle(title));
        assertThat(response.getHeaders().getLocation().getPath(), is("/question/create"));
    }

/* 비로그인 유저일 경우 어떻게 테스트 하는지
    @Test(expected = UnAuthenticationException.class)
    public void 질문_생성_비로그인유저() throws Exception {
        String title = "질문생성테스트";
        ResponseEntity<String> response =  template()
                .postForEntity("/question/create", HtmlFormDataBuilder.urlEncodedForm()
                        .addParameter("title", title)
                        .addParameter("contents","질문생성테스트_컨텐츠")
                        .build(), String.class);
    }
*/


}
