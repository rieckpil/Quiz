package com.example.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class Question {
    private final String correctAnswer;
    private String text;
    private List<String> answers = new ArrayList<>();

    public Question(
            String text,
            String correctAnswer,
            String... answers
    ) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.answers.addAll(asList(answers));
    }

    public Question(String text) {
        this.text = text;
        this.correctAnswer = null;
    }

    @Override
    public boolean equals(Object other) {
        return reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return this.text;
    }

    public List<String> answers() {
        return answers;
    }

    public String correctAnswer() {
        return this.correctAnswer;
    }
}
