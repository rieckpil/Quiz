package com.example.quiz.adapter.in.console;

import com.example.quiz.domain.Choice;
import com.example.quiz.domain.Question;
import com.example.quiz.domain.SingleChoice;
import com.example.quiz.domain.port.InMemoryQuestionRepository;
import com.example.quiz.domain.quiz.Quiz;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@ConditionalOnProperty("console.quiz.enabled")
@Profile("!test")
@Component
public class ConsoleQuiz implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        List<Choice> choices =
            List.of(new Choice("Answer 1"),
                new Choice("Answer 2"),
                new Choice("Answer 3"),
                new Choice("Answer 4"));
        Question question1 = new Question(
            "Question 1",
            new SingleChoice(
                new Choice("Answer 2"),
                choices));
        Question question2 = new Question(
            "Question 2",
            new SingleChoice(new Choice("Answer 3"),
                choices)
        );
        Question question3 = new Question(
            "Question 3",
            new SingleChoice(new Choice("Answer 1"),
                choices
            )
        );
        final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);
        final Quiz quiz = new Quiz(questionRepository);
        new Questioner(quiz).start();
    }
}
