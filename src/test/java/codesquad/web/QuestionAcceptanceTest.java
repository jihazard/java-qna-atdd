package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void 리스트_비로그인유저() throws Exception {
        ResponseEntity<String> response = template().getForEntity("/questions/list", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
         assertThat(response.getBody().contains(defaultQuestion().getTitle()), is(true));
    }
    @Test
    public void 리스트_로그인유저() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate(defaultUser())
                                          .getForEntity("/questions/list", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
        assertThat(response.getBody().contains(defaultQuestion().getTitle()), is(true));
    }

    @Test
    public void 질문_생성_로그인유저() throws Exception {
        String title = "질문생성테스트";
        ResponseEntity<String> response = create(title, basicAuthTemplate(defaultUser()));
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertNotNull(questionRepository.findByTitle(title));
        assertThat(response.getHeaders().getLocation().getPath(), is("/questions/create"));
    }

    @Test
    public void 질문_생성_비로그인유저() throws Exception {
        ResponseEntity<String> response = create("질문생성테스트", template());
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<String> create(String title, TestRestTemplate testRestTemplate) {
        return testRestTemplate
                .postForEntity("/questions/create", HtmlFormDataBuilder.urlEncodedForm()
                        .addParameter("title", title)
                        .addParameter("contents", "질문생성테스트_컨텐츠")
                        .build(), String.class);
    }

    @Test
    public void  업데이트_로그인유저() throws Exception {
        Question question = defaultQuestion();
        ResponseEntity<String> response = update(question, basicAuthTemplate(defaultUser()));
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertTrue(response.getHeaders().getLocation().getPath().startsWith("/home"));
     }
    @Test
    public void  업데이트_비로그인유저() throws Exception {
        Question question = defaultQuestion();
        ResponseEntity<String> response = update(question, template());
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<String> update(Question question, TestRestTemplate template) {
        return template
                .postForEntity(String.format("/questions/%d", question.getId()), HtmlFormDataBuilder.urlEncodedForm()
                        .addParameter("_method", "put")
                        .addParameter("title", "수정할 제목 내용")
                        .addParameter("contents", "수정할 컨텐츠 내용")
                        .build(), String.class);
    }

    @Test
    public void 상세보기_비로그인() throws Exception {
        Question question = defaultQuestion();
        ResponseEntity<String> response = template().getForEntity(String.format("/questions/%d",question.getId()), String.class);
        log.debug("body : {}", response.getBody());
        assertThat(response.getBody().contains(question.getContents()), is(true));
    }

    @Test
    public void  삭제_로그인유저() throws Exception {
        Question question = defaultQuestion();
        ResponseEntity<String> response = delete(question, basicAuthTemplate(defaultUser()));
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertTrue(response.getHeaders().getLocation().getPath().startsWith("/home"));
    }
    @Test
    public void  삭제_비로그인유저() throws Exception {
        ResponseEntity<String> response = delete(defaultQuestion(), template());
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }

    private ResponseEntity<String> delete(Question question, TestRestTemplate template) {
        return template
                .postForEntity(String.format("/questions/%d", question.getId()), HtmlFormDataBuilder
                        .urlEncodedForm()
                        .addParameter("_method", "delete")
                        .build(), String.class);
    }
}