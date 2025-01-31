package com.example.quiz.domain;

public record ResponseId(long id) {
    public static ResponseId of(long id) {
        return new ResponseId(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "=" + id;
    }
}
