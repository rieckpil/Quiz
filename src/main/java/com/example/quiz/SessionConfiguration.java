package com.example.quiz;

import com.example.quiz.adapter.out.web.humanreadable.ReadableTokenGenerator;
import com.example.quiz.application.QuizService;
import com.example.quiz.application.QuizSessionService;
import com.example.quiz.application.port.*;
import com.example.quiz.domain.Choice;
import com.example.quiz.domain.Question;
import com.example.quiz.domain.Quiz;
import com.example.quiz.domain.SingleChoice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SessionConfiguration {
    @Bean
    public QuizService createQuizService(QuestionRepository questionRepository) {
        return new QuizService(questionRepository);
    }

    @Bean
    public QuizSessionService createQuizSessionService(QuizService quizService) {
        QuizRepository quizRepository = new InMemoryQuizRepository();
        final Question question = new Question("Question 1",
                new SingleChoice(List.of(new Choice("Answer 1", true))));
        List<Question> questions = List.of(question);
        quizRepository.save(new Quiz(questions));
        return new QuizSessionService(
                quizService,
                new InMemoryQuizSessionRepository(),
                quizRepository,
                null);
    }

    @Bean
    public TokenGenerator idGenerator() {
        return new ReadableTokenGenerator();
    }
}
