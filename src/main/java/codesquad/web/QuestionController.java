package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.UnAuthenticationException;
import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public String create(QuestionDto questionDto) {
        qnaService.add(questionDto);
        return "redirect:/users";
    }
    @PostMapping("/create")
    public String create2(@LoginUser User loginUser ,String contents, String title ) throws UnAuthenticationException {
        log.debug("inputData" + loginUser.getUserId() +"//" + contents + "//" + title);
        qnaService.create(loginUser, new Question(title,contents));
        Question quest = qnaService.findByTitle(title);
        return "redirect:/questions/create";
    }
    @GetMapping("/list")
    public String list(Model model) {
        Iterable<Question> all = qnaService.findAll();
        List<Object> questions = new ArrayList<>();
        Iterator allIterator = all.iterator();
        while (allIterator.hasNext()) {
            questions.add(allIterator.next());
        }
        model.addAttribute("questions", questions);
        return "home";
    }

    @GetMapping("/form")
    public String form() {
        return "/qna/form";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@LoginUser User loginUser, @PathVariable long id, Model model) {

       // model.addAttribute("user", qnaService.findByTitle());
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@LoginUser User loginUser, @PathVariable long id, String title, String contents) {
        System.out.println("targettarget" + title +"//" + contents);
        //qnaService.update(loginUser, id, target);
        return "redirect:/questions/list";
    }

    @DeleteMapping("/{id}")
    public String delete(@LoginUser User loginUser, @PathVariable long id) throws CannotDeleteException {
        Iterable<Question> all2 = qnaService.findAll();
        Iterator allIterator2 = all2.iterator();
        while (allIterator2.hasNext()) {
            System.out.println("-------------------------------");
            System.out.println("삭제전글목록 " + allIterator2.next());
            System.out.println("-------------------------------");
        }

        System.out.println("targettarget" + id);
        qnaService.deleteQuestion(loginUser, id);

        Iterable<Question> all = qnaService.findAll();
        Iterator allIterator = all.iterator();
        while (allIterator.hasNext()) {
            System.out.println("-------------------------------");
            System.out.println("글목록 " + allIterator.next());
            System.out.println("-------------------------------");
        }


        return "redirect:/home";
    }

}
