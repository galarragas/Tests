package test2;

import java.util.StringTokenizer;

public class BetterProgrammerTask {

    public static String capitalizeFirstLetters(String s) {
        /*
          Please implement this method to
          capitalize all first letters of the words in the given String.
          All other symbols shall remain intact. If a word starts not with a letter, it shall remain intact too.
          Assume that the parameter String can only contain spaces and alphanumeric characters.

          NOTE: please keep in mind that the words can be divided by single or multiple spaces.
          The spaced also can be found at the beginning or the end of the parameter string,
          and you need to preserve them.
         */

        StringBuffer result = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(s, " ", true);
        while(tokenizer.hasMoreTokens()) {
            String currToken = tokenizer.nextToken();
            if(" ".equals(currToken)) {
                result.append(" ");
            }
            else if(Character.isLowerCase(currToken.charAt(0))) {
                result.append(Character.toUpperCase(currToken.charAt(0)));
                result.append(currToken.substring(1));
            }
            else {
                result.append(currToken);
            }
        }
        return result.toString();
    }
}