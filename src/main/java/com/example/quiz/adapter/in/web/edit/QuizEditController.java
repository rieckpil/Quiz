package com.example.quiz.adapter.in.web.edit;

import com.example.quiz.application.QuestionService;
import com.example.quiz.domain.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class QuizEditController {

    private final QuestionService questionService;

    public QuizEditController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/add-question")
    public String addQuestion(@Valid AddQuestionForm addQuestionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-question";
        }
        try {
            questionService.add(addQuestionForm);
        } catch (NoCorrectChoiceSelected | TooManyCorrectChoicesSelected e) {
            ObjectError error = new ObjectError("Error", e.getMessage());
            bindingResult.addError(error);
            if (bindingResult.hasErrors()) {
                return "add-question";
            }
        }

        return "redirect:/add-question";
    }

    @GetMapping("/add-question")
    public String showAddQuestion(Model model) {
        AddQuestionForm form = new AddQuestionForm();
        model.addAttribute("addQuestionForm", form);
        return "add-question";
    }


    @GetMapping("/add-choice")
    public String addChoice(Model model, @RequestParam("index") int index) {
        int nextIndex = index + 1;
        model.addAttribute("index", nextIndex);
        model.addAttribute("hasErrors", false);
        model.addAttribute("fieldNameChoiceText", "dummy.choices[" + index + "].choice");
        model.addAttribute("fieldNameCorrectAnswer", "dummy.choices[" + index + "].correctAnswer");
        model.addAttribute("dummy", new DummyQuestionChoices(nextIndex));
        return "fragments/form-fragments :: choice-input";
    }

    @GetMapping("/view-questions")
    public String viewQuestions(Model model) {
        final List<Question> questions = questionService.findAll();

        final List<QuestionView> questionViews = questions.stream()
                                                          .map(QuestionView::of)
                                                          .toList();

        model.addAttribute("questions", questionViews);
        return "view-questions";
    }
}