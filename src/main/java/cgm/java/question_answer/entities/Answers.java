package cgm.java.question_answer.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "Answers")
public class Answers {

  //Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "answer_id")
  private Long answer_id;

  @NotNull
  @Size(max = 255)
  @Column(name = "answer_text")
  private String answer_text;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  public Answers() {
  }

  public Answers(String answer_text) {
    this.answer_text = answer_text;
  }

  public Answers(String answer_text, Question question) {
    this.answer_text = answer_text;
    this.question = question;
  }

}
