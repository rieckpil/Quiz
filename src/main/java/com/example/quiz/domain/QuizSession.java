package com.example.quiz.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuizSession {

  private final Iterator<Question> iterator;
  private final List<Question> questions;

  private final List<Response> responses = new ArrayList<>();
  private Question question;

  public QuizSession(Quiz quiz) {
    if (quiz.questions().isEmpty()) {
      throw new IllegalArgumentException();
    }
    questions = quiz.questions();
    iterator = questions.iterator();
    question = iterator.next();
  }

  public Question question() {
    return question;
  }

  public void respondWith(Choice choice) {
    Response response = new Response(question, choice);
    responses.add(response);
    if (iterator.hasNext()) {
      question = iterator.next();
    }
  }

  public boolean isFinished() {
    return responses.size() == questions.size();
  }

  public int correctResponsesCount() {
    return responseCountFor(ResponseStatus.CORRECT);
  }

  public int incorrectResponsesCount() {
    return responseCountFor(ResponseStatus.INCORRECT);
  }

  private int responseCountFor(ResponseStatus status) {
    return Math.toIntExact(responses.stream()
        .filter(r -> r.status().equals(status))
        .count());
  }

  public Grade grade() {
    return new Grade(responses.size(), correctResponsesCount(), incorrectResponsesCount());
  }

}
