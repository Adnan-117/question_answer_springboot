package cgm.java.question_answer.utilities;

import cgm.java.question_answer.entities.Answers;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utilities {

  public static Set<Answers> getSetOfAnswersFromString(String answers) {
    return Stream.of(answers.split(","))
                 .map(String::trim)
                 .map(Answers::new)
                 .collect(Collectors.toSet());
  }

  // function to generate a random string of length n
  public static StringBuilder getAlphaNumericString(int n) {

    // chose a Character random from this String
    String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "0123456789"
        + "abcdefghijklmnopqrstuvxyz";

    // create StringBuffer size of AlphaNumericString
    StringBuilder sb = new StringBuilder(n);

    for (int i = 0; i < n; i++) {

      // generate a random number between
      // 0 to AlphaNumericString variable length
      int index
          = (int) (AlphaNumericString.length()
          * Math.random());

      // add Character one by one in end of sb
      sb.append(AlphaNumericString
                    .charAt(index));
    }

    return sb;
  }

}
