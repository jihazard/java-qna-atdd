package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
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
public class ApiAnswerController {
    private static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;


    @PostMapping("/{questionId}/answer")
    public ResponseEntity createAnswer(@LoginUser User loginUser,@PathVariable long questionId, @Valid @RequestBody AnswerDto answerDto) {
        System.out.println("답장 컨트롤러 입장");
        Answer saveAswer = qnaService.addAnswer(loginUser,questionId,answerDto.getContents());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/"+questionId+"/answer/" + saveAswer.getId()));
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }


}