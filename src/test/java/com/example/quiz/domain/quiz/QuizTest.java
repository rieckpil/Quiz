package com.example.quiz.domain.quiz;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.quiz.domain.Choice;
import com.example.quiz.domain.Question;
import com.example.quiz.domain.ResponseStatus;
import com.example.quiz.domain.SingleChoice;
import com.example.quiz.domain.port.InMemoryQuestionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

public class QuizTest {
    private ResponseStatus PENDING = ResponseStatus.PENDING;

    @Test
    void new_quiz_hasNoQuestions() {
        final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
        // Given / when
        Quiz quiz = new Quiz(questionRepository);

        // Then
        List<Question> questions = quiz.questions();

        assertThat(questions)
                .isEmpty();
    }

    @Test
    void new_quiz_hasOneQuestion() {
        // Given / when
        List<Choice> choices = List.of(new Choice("Answer 1"), new Choice("Answer 2"));
        final Question question = new Question(
            "Question 1",
            new SingleChoice(
                new Choice("Answer 2"),
                choices
            )
        );

        final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
        questionRepository.save(question);

        Quiz quiz = new Quiz(questionRepository);

        List<Question> questions = quiz.questions();

        assertThat(questions).containsOnly(question);
    }
}
