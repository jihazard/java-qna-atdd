package support.test;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import codesquad.domain.User;
import codesquad.domain.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {
    private static final String DEFAULT_LOGIN_USER = "sanjigi";
    private static final String DEFAULT_TITLE = "runtime 에 reflect 발동 주체 객체가 뭔지 알 방법이 있을까요?";
    @Autowired
    private TestRestTemplate template;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;
    
    public TestRestTemplate template() {
        return template;
    } 
    
    public TestRestTemplate basicAuthTemplate() {
        return basicAuthTemplate(defaultUser());
    }
    
    public TestRestTemplate basicAuthTemplate(User loginUser) {
        return template.withBasicAuth(loginUser.getUserId(), loginUser.getPassword());
    }
    
    protected User defaultUser() {
        return findByUserId(DEFAULT_LOGIN_USER);
    }
    protected Question defaultQuestion() {
        return findbyTitle(DEFAULT_TITLE);
    }


    protected User findByUserId(String userId) {
        return userRepository.findByUserId(userId).get();
    }
    protected Question findbyTitle(String title) {
        return questionRepository.findByTitle(title).get();
    }
}
