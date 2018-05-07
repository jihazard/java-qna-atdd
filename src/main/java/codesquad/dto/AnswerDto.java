package codesquad.dto;

import codesquad.domain.Answer;
import codesquad.domain.Question;
import codesquad.domain.User;

import javax.validation.constraints.Size;
import java.util.Objects;

public class AnswerDto {
    private long writer_id;

    private User writer;

    @Size(min = 5)
    private String contents;

    private Question question;
    private long questionId;

    public AnswerDto(){}

    public AnswerDto( User writer, String contents, Question question) {
       this.writer = writer;
       this.contents = contents;
       this.question = question;
    }

    public AnswerDto(long id, User writer, String contents ,Question question) {
        this.writer_id = id;
        this.writer = writer;
        this.contents = contents;
        this.question = question;
    }

    public AnswerDto(long id, User writer, String contents ,long question_id) {
        this.writer_id = id;
        this.writer = writer;
        this.contents = contents;
        this.questionId = question_id;
    }


    public long getWriter_id() {
        return writer_id;
    }

    public AnswerDto setWriter_id(long writer_id) {
        this.writer_id = writer_id;
        return this;
    }

    public User getWriter() {
        return writer;
    }

    public AnswerDto setWriter(User writer) {
        this.writer = writer;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public AnswerDto setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public Question getQuestion() {
        return question;
    }

    public AnswerDto setQuestion(Question question) {
        this.question = question;
        return this;
    }

    public Answer toAnswer(){
        return new Answer(this.writer_id,this.writer,this.question,this.contents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerDto answerDto = (AnswerDto) o;
        return writer_id == answerDto.writer_id &&
                Objects.equals(writer, answerDto.writer) &&
                Objects.equals(contents, answerDto.contents) &&
                Objects.equals(question, answerDto.question);
    }

    @Override
    public int hashCode() {

        return Objects.hash(writer_id, writer, contents, question);
    }

    @Override
    public String toString() {
        return "AnswerDto{" +
                "writer_id=" + writer_id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", question=" + question +
                '}';
    }
}