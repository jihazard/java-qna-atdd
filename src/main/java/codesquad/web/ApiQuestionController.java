package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.dto.UserDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import codesquad.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
    private static final Logger log = LoggerFactory.getLogger(ApiQuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public ResponseEntity createQuestions(@LoginUser User loginUser, @Valid @RequestBody QuestionDto question) {
        Question savedQuestion = qnaService.add(question);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + savedQuestion.getId()));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping("")
    public Iterable<Question> questionsList() {
        return qnaService.findAll();
    }

    @GetMapping("/{id}")
    public QuestionDto showQuestion(@PathVariable long id) {
        Question question = qnaService.findById(id);
        log.debug("find Questions : {}", question.toString());
        return question.toQuestionDto();
    }

    @PutMapping("/{id}")
    public void updateQuestion(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody QuestionDto updateQuestion) {
        qnaService.update(loginUser, id, updateQuestion);
        log.debug("after updateQuestions : {}" + updateQuestion.toString());
    }
}