package com.example.quiz;

import com.example.quiz.domain.Answer;
import com.example.quiz.domain.MultipleChoice;
import com.example.quiz.domain.Question;
import com.example.quiz.domain.port.QuestionRepository;
import com.example.quiz.domain.quiz.Quiz;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuizConfiguration {

  @Bean
  Quiz createQuiz(QuestionRepository questionRepository) {
    return new Quiz(questionRepository);
  }

}
