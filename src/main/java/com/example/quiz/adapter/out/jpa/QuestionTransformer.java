package com.example.quiz.adapter.out.jpa;

import com.example.quiz.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionTransformer {
    private final ChoiceTransformer choiceTransformer;

    public QuestionTransformer(ChoiceTransformer choiceTransformer) {
        this.choiceTransformer = choiceTransformer;
    }

    Question toQuestion(QuestionDbo questionDbo) {
        List<Choice> choices = choiceTransformer.toChoices(questionDbo
                .getChoices());

        Question question = new Question(
                questionDbo.getText(),
                choiceType(questionDbo, choices));

        question.setId(QuestionId.of(questionDbo.getId()));

        return question;
    }

    QuestionDbo toQuestionDbo(Question question) {
        QuestionDbo questionDbo = new QuestionDbo();
        questionDbo.setText(question.text());
        ChoiceType choiceType = question.isSingleChoice() ? ChoiceType.SINGLE : ChoiceType.MULTIPLE;
        questionDbo.setChoiceType(choiceType);
        List<ChoiceDbo> choiceDbos = choiceTransformer.toChoiceDbos(question.choices());
        questionDbo.setChoices(choiceDbos);
        return questionDbo;
    }

    private com.example.quiz.domain.ChoiceType choiceType(QuestionDbo questionDbo, List<Choice> choices) {
        return questionDbo.getChoiceType().equals(ChoiceType.SINGLE) ?
                new SingleChoice(choices) : new MultipleChoice(choices);
    }

}
