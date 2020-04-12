package cgm.java.question_answer.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "Question")
public class Question  {

  //Attributes
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "question_id")
  private Long question_id;

  @NotNull
  @Size(max = 255)
  @Column(name = "question_text", unique = true)
  private String question_text;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  @OneToMany(cascade = {CascadeType.ALL }, mappedBy = "question", fetch = FetchType.EAGER)
  public Set<Answers> answersList = new HashSet<>();

  //Default Constructor
  public Question() {
  }

  // Constructor overloading
  public Question(String question_text) {
    this.question_text = question_text;
  }

  public Question(String question_text, Set<Answers> answersList) {
    this.question_text = question_text;
    this.answersList = answersList;
  }

  public void addQuestionToAnswer(Answers answer) {
    answersList.add(answer);
    answer.setQuestion(this);
  }

}
