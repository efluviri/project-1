package main.java.handlers;
public class CamelCaseHandler implements TextHandler {
    @Override
    public String handle(String input){
        if (input == null || input.isEmpty()) return input;
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder(words[0].toLowerCase());
        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            if (!word.isEmpty()) {
                String firstLetter = word.substring(0, 1).toUpperCase();
                String restOfTheWord = word.substring(1).toLowerCase();
                result.append(firstLetter).append(restOfTheWord);
            }
        }
        return result.toString();
    }
}
