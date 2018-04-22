package codesquad.web;

import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.UserDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import codesquad.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;


    @PostMapping("/create")
    public String login(@LoginUser User loginUser ,String contents, String title ) throws UnAuthenticationException {
        log.debug("inputData" + loginUser.getUserId() +"//" + contents + "//" + title);
        qnaService.create(loginUser, new Question(title,contents));
        Question quest = qnaService.findByTitle(title);
        return "redirect:/question/create";
    }

    @GetMapping("/list")
    public String list(Model model) {
       for (Question question: qnaService.findAll()){
           System.out.println(question.getTitle());
       }
       /*Iterable questions = qnaService.findAll();

       while(questions.iterator().hasNext()){
           log.debug("questions여기나와요 size : {}", questions.iterator().next());
       }*/

        model.addAttribute("question", qnaService.findAll());
         return "/qna/show";
    }
}
