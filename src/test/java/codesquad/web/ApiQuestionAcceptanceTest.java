package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.dto.UserDto;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import support.test.AcceptanceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {


    @Test
    public void 질문생성_로그인() throws Exception {
        User anotherUser = anotherUser();
        QuestionDto dto = createQuestionDto(anotherUser.getId(), "111111111111111","2222222222222222222");
        String location = createResource("/api/questions",dto,anotherUser);
        assertThat(location, is("/api/questions/"+anotherUser.getId()));
    }

    @Test
    public void 질문생성_비로그인() throws Exception {
        QuestionDto dto = createQuestionDto(defaultUser().getId(), "111111111111111","2222222222222222222");
        ResponseEntity response = template().postForEntity("/api/questions", dto, String.class);
        assertThat( response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }


    private QuestionDto createQuestionDto(Long id , String title, String contents) {
        return new QuestionDto(id,title,contents);
    }
    private UserDto createUserDto(String userId) {
        return new UserDto(userId, "password", "name", "javajigi@slipp.net");
    }

}
