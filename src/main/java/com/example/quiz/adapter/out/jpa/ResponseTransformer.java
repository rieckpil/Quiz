package com.example.quiz.adapter.out.jpa;

import com.example.quiz.application.port.QuestionRepository;
import com.example.quiz.domain.Choice;
import com.example.quiz.domain.Question;
import com.example.quiz.domain.QuestionId;
import com.example.quiz.domain.Response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseTransformer {
    private final QuestionRepository questionRepository;

    public ResponseTransformer(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public ResponseDbo toResponseDbo(Response response) {
        ResponseDbo responseDbo = new ResponseDbo();
        responseDbo.setQuestionId(response.questionId().id());
        Set<Long> choiceIds = response
                .choices()
                .stream()
                .map(choice -> choice.getId().id())
                .collect(Collectors.toSet());
        responseDbo.setChoiceIds(choiceIds);

        return responseDbo;
    }

    public Response toResponse(ResponseDbo responseDbo) {
        Set<Long> choiceIds = responseDbo.getChoiceIds();
        QuestionId questionId = QuestionId.of(responseDbo.getQuestionId());
        Question question = questionRepository
                .findById(questionId)
                .orElseThrow();
        List<Choice> choiceEntries = question.choices()
                                             .stream()
                                             .filter(c -> choiceIds.contains(c.getId().id()))
                                             .toList();
        Choice[] choices = choiceEntries.toArray(new Choice[]{});

        return new Response(questionId, question.isCorrectAnswer(choices), choices);
    }
}
