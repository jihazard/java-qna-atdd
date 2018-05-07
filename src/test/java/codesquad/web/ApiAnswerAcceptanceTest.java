package codesquad.web;

import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.dto.QuestionDto;
import codesquad.dto.UserDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;


    @Test
    public void 질문생성_로그인() throws Exception {
        User anotherUser = anotherUser();
        QuestionDto dto = createQuestionDto(3L, "질문타이틀테스트","질문컨텐츠",anotherUser);
        String location = createResource("/api/questions",dto,anotherUser);
        assertThat(location, is("/api/questions/"+dto.getId()));
    }

    @Test
    public void 답변생성() throws Exception {
        User user = defaultUser();
        QuestionDto dto = createQuestionDto(3L, "질문타이틀테스트","질문컨텐츠",user);
        String location = createResource("/api/questions",dto,user);

        AnswerDto answer = new AnswerDto(anotherUser(),"답변테스트",dto.toQuestion());
        String answerLocation = createResource(location+"/answer",answer,user);

        assertThat(answerLocation, is(location+"/answer/"+answer.getWriter_id()));
    }

    @Test
    public void 질문생성_비로그인() throws Exception {
        QuestionDto dto = createQuestionDto(3L, "질문타이틀테스트","질문컨텐츠" ,anotherUser());
        ResponseEntity response = template().postForEntity("/api/questions", dto, String.class);
        assertThat( response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
    @Test
    public void 리스트_비로그인() throws Exception {
        ResponseEntity response = template().getForEntity("/api/questions", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void 리스트_로그인() throws Exception {
        ResponseEntity response = basicAuthTemplate(defaultUser()).getForEntity("/api/questions", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
    }

    @Test
    @Transactional
    public void 질문_상세조회() throws Exception {
        Question question = defaultQuestion(2L);
        ResponseEntity<String> response = template().getForEntity(String.format("/api/questions/%d",question.getId()),String.class);
        log.debug("response.getStatusCode() : {}", response.getStatusCode());
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
    }
    @Test
    @Transactional
    public void 질문_상세조회_로그인() throws Exception {
        Question question = defaultQuestion(2L);
        ResponseEntity<String> response = basicAuthTemplate(defaultUser()).getForEntity(String.format("/api/questions/%d",question.getId()),String.class);
        log.debug("response.getStatusCode() : {}", response.getStatusCode());
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
    }

    @Test
    public void 질문업데이트_로그인() throws Exception {
        User user = anotherUser();
        QuestionDto newQuestion = createQuestionDto(3L, "질문타이틀테스트","질문컨텐츠", user);
        log.debug("response.getStatusCode() : {}", newQuestion.toString());
        ResponseEntity<String> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        QuestionDto updateQuestion = new QuestionDto(user.getId(), "질문타이틀업데이트테스트","질문업데이트컨텐츠",user);
        basicAuthTemplate(user).put(location,updateQuestion);

        QuestionDto dbQuestion = basicAuthTemplate(user).getForObject(location,QuestionDto.class);
        assertThat(dbQuestion, is(updateQuestion));
    }

    @Test
    public void 질문업데이트_다른계정로그인() throws Exception {
        User user = anotherUser();
        QuestionDto newQuestion = createQuestionDto(3L, "질문타이틀테스트","질문컨텐츠", user);
        log.debug("response.getStatusCode() : {}", newQuestion.toString());
        ResponseEntity<String> response = basicAuthTemplate(user).postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        String location = response.getHeaders().getLocation().getPath();

        QuestionDto updateQuestion = new QuestionDto(defaultUser().getId(), "질문타이틀업데이트테스트","질문업데이트컨텐츠",defaultUser());
         basicAuthTemplate(defaultUser()).put(location,updateQuestion);

        QuestionDto dbQuestion = basicAuthTemplate(defaultUser()).getForObject(location,QuestionDto.class);
        assertThat(dbQuestion, is(newQuestion));
    }


    private QuestionDto createQuestionDto(Long id , String title, String contents, User writer) {
        return new QuestionDto(id,title,contents, writer);
    }
    private UserDto createUserDto(String userId) {
        return new UserDto(userId, "password", "name", "javajigi@slipp.net");
    }

}
