package com.example.quiz.adapter.in.web.edit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddQuestionFormTest {
    @Test
    void transformsChoicesIntoListOfStrings() {
        final AddQuestionForm addQuestionForm = new AddQuestionForm(
                "question",
                "single", new ChoiceForm("a1", true),
                new ChoiceForm("a2", false),
                new ChoiceForm("a3", false),
                new ChoiceForm("a4", false));

        List<String> choices = addQuestionForm.transformToChoices();

        assertThat(choices)
                .containsExactly(
                        "a1",
                        "a2",
                        "a3",
                        "a4"
                );
    }

    @Test
    void transformToCorrectChoicesForSingleChoice() {
        final AddQuestionForm addQuestionForm = new AddQuestionForm(
                "question",
                "single", new ChoiceForm("a1", true),
                new ChoiceForm("a2", false),
                new ChoiceForm("a3", false),
                new ChoiceForm("a4", false));

        List<String> correctChoices = addQuestionForm.transformToCorrectChoices();

        assertThat(correctChoices)
                .containsExactly("a1");
    }

    @Test
    void transformToCorrectChoicesForMultipleChoice() {
        final AddQuestionForm addQuestionForm = new AddQuestionForm(
                "question",
                "multiple", new ChoiceForm("a1", true),
                new ChoiceForm("a2", true),
                new ChoiceForm("a3", false),
                new ChoiceForm("a4", false));

        List<String> correctChoices = addQuestionForm.transformToCorrectChoices();

        assertThat(correctChoices)
                .containsExactly("a1", "a2");
    }

    @Test
    void throwsErrorForTwoCorrectChoicesForSingleChoice() {
        final AddQuestionForm addQuestionForm = new AddQuestionForm(
                "question",
                "single", new ChoiceForm("a1", true),
                new ChoiceForm("a2", true),
                new ChoiceForm("a3", false),
                new ChoiceForm("a4", false));

        assertThatThrownBy(addQuestionForm::transformToCorrectChoices)
                .isInstanceOf(TooManyCorrectChoicesSelected.class);
    }

    @Test
    void throwsErrorForOneCorrectChoiceForMultipleChoice() {
        final AddQuestionForm addQuestionForm = new AddQuestionForm(
                "question",
                "multiple", new ChoiceForm("a1", true),
                new ChoiceForm("a2", false),
                new ChoiceForm("a3", false),
                new ChoiceForm("a4", false));

        assertThatThrownBy(addQuestionForm::transformToCorrectChoices)
                .isInstanceOf(TooFewCorrectChoicesSelected.class);
    }
}