package com.example.quiz.domain.quiz;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import com.example.quiz.domain.Answer;
import com.example.quiz.domain.FinalMark;
import com.example.quiz.domain.Grade;
import com.example.quiz.domain.MultipleChoice;
import com.example.quiz.domain.Question;
import com.example.quiz.domain.ResponseStatus;
import com.example.quiz.domain.port.InMemoryQuestionRepository;
import com.example.quiz.domain.port.QuestionRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class QuizSessionTest {
  @Test
  void quizStartsASession() {
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    // Given
    Quiz quiz = new Quiz(questionRepository);

    // When
    QuizSession session = quiz.start();

    // Then
    assertThat(session)
        .isNotNull();
  }

  @Test
  void sessionStartsWithTheFirstQuestion() {
    // Given
    final MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"),
        Collections.singletonList(new Answer("Answer 1")));

    final Question question = new Question("Question 1", choice);

    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question);

    Quiz quiz = new Quiz(questionRepository);

    // When
    QuizSession session = quiz.start();

    assertThat(session.question())
        .isEqualTo(question);
  }

  @Test
  void testTakerCanAddResponseToQuestionFromTheSession() {
    // Given
    final MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"),
        Collections.singletonList(new Answer("Answer 1")));
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(new Question("Question 1", choice));
    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question question = session.question();
    session.respondWith("Answer 1", question);

    assertThat(session.lastResponseStatus())
        .isEqualTo(ResponseStatus.CORRECT);
  }

  @Test
  void testTakerCanCheckIfSessionWithOneQuestionIsFinished() {
    final MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"),
        Collections.singletonList(new Answer("Answer 1")));
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(new Question("Question 1", choice));
    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question question = session.question();
    session.respondWith("Answer 1", question);

    assertThat(session.isFinished())
        .isTrue();
  }

  @Test
  void testTakerCanCheckIfSessionWithThreeQuestionsIsFinishedAfterSecondQuestion() {
    List<Answer> answers = List.of(
        new Answer("Answer 1")
    );

    MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"), answers);

    Question question1 = new Question("Question 1", choice);
    Question question2 = new Question("Question 2", choice);
    Question question3 = new Question("Question 3", choice);

    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question1);
    questionRepository.save(question2);
    questionRepository.save(question3);
    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question q1 = session.question();
    session.respondWith("Answer 1", q1);
    Question q2 = session.question();
    session.respondWith("Answer 2", q2);

    assertThat(session.isFinished())
        .isFalse();
  }

  @Test
  void testTakerCanCheckIfSessionWithThreeQuestionsIsFinishedAfterThirdQuestion() {
    List<Answer> answers = List.of(
        new Answer("Answer 1")
    );

    MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"), answers);

    Question question1 = new Question("Question 1", choice);
    Question question2 = new Question("Question 2", choice);
    Question question3 = new Question("Question 3", choice);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question1);
    questionRepository.save(question2);
    questionRepository.save(question3);
    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question q1 = session.question();
    session.respondWith("Answer 1", q1);
    Question q2 = session.question();
    session.respondWith("Answer 2", q2);
    Question q3 = session.question();
    session.respondWith("Answer 2", q3);

    assertThat(session.isFinished())
        .isTrue();
  }

  @Test
  void respondWith_givesCorrectStatusForAnswer() {
    final MultipleChoice choice = new MultipleChoice(
        new Answer("Answer 1"),
        List.of(new Answer("Answer 1"))
    );
    Question question = new Question("Question", choice);

    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question);
    Quiz quiz = new Quiz(questionRepository);

    final QuizSession quizSession = new QuizSession(quiz);

    quizSession.respondWith("Answer 1", question);

    assertThat(quizSession.lastResponseStatus())
        .isEqualByComparingTo(ResponseStatus.CORRECT);
  }

  // Ask Grade
  @Test
  void grade_gives_number_of_correct_responses_for_Session() {
    List<Answer> answers = List.of(
        new Answer("Answer 1")
    );

    MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"), answers);

    Question question1 = new Question("Question 1", choice);
    Question question2 = new Question("Question 2", choice);
    Question question3 = new Question("Question 3", choice);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question1);
    questionRepository.save(question2);
    questionRepository.save(question3);

    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question q1 = session.question();
    session.respondWith("Answer 1", q1);
    Question q2 = session.question();
    session.respondWith("Answer 2", q2);
    Question q3 = session.question();
    session.respondWith("Answer 2", q3);

    assertThat(session.correctResponsesCount())
        .isEqualTo(1L);
  }

  @Test
  void counts_incorrect_responses() {
    List<Answer> answers = List.of(
        new Answer("Answer 1")
    );

    MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"), answers);

    Question question1 = new Question("Question 1", choice);
    Question question2 = new Question("Question 2", choice);
    Question question3 = new Question("Question 3", choice);

    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question1);
    questionRepository.save(question2);
    questionRepository.save(question3);
    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question q1 = session.question();
    session.respondWith("Answer 1", q1);
    Question q2 = session.question();
    session.respondWith("Answer 2", q2);
    Question q3 = session.question();
    session.respondWith("Answer 2", q3);

    assertThat(session.incorrectResponsesCount())
        .isEqualTo(2L);
  }

  @Test
  void giveAGrade() {
    List<Answer> answers = List.of(
        new Answer("Answer 1")
    );

    MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"), answers);

    Question question1 = new Question("Question 1", choice);
    Question question2 = new Question("Question 2", choice);
    Question question3 = new Question("Question 3", choice);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question1);
    questionRepository.save(question2);
    questionRepository.save(question3);
    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    // when
    Question q1 = session.question();
    session.respondWith("Answer 1", q1);
    Question q2 = session.question();
    session.respondWith("Answer 2", q2);
    Question q3 = session.question();
    session.respondWith("Answer 2", q3);

    final Grade grade = new Grade(3, new FinalMark(1L, 2L));

    assertThat(session.grade())
        .isEqualTo(grade);
  }

  @Test
  void returnSameQuestionIfItHasntBeenAnswered() {
    List<Answer> answers = List.of(
        new Answer("Answer 1")
    );

    MultipleChoice choice = new MultipleChoice(new Answer("Answer 1"), answers);
    Question question1 = new Question("Question 1", choice);
    final InMemoryQuestionRepository questionRepository = new InMemoryQuestionRepository();
    questionRepository.save(question1);

    Quiz quiz = new Quiz(questionRepository);
    QuizSession session = quiz.start();

    Question q1 = session.question();
    Question q2 = session.question();

    assertThat(q1)
        .isEqualTo(q2);
  }

}
