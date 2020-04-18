package flashcards;

import javax.print.DocFlavor;
import java.util.*;

class Card{
    public String question;
    public String definition;

    public Card(String question, String definition) {
        this.question = question;
        this.definition = definition;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input the number of cards:");
        int n = scanner.nextInt();
        scanner.nextLine();

        Map<String, String> cardStack = new LinkedHashMap<>();
        Map<String, String> valueToKey = new HashMap<>();

        handleInput(scanner, n, cardStack, valueToKey);

        Set<Map.Entry<String, String>> entries = cardStack.entrySet();
        doQuiz(scanner, cardStack, valueToKey, entries);
    }

    private static void handleInput(Scanner scanner, int n, Map<String, String> cardStack, Map<String, String> valueToKey) {
        for (int i = 0; i < n; i++) {
            System.out.println("The question of card #" + (i+1));
            String q = "";
            while(q.isEmpty()) {
                q = scanner.nextLine();
                if (cardStack.containsKey(q)) {
                    System.out.println("The card \"" + q + "\" already exists. Try again.");
                    q = "";
                }
            }
            System.out.println("The definition of card #" + (i+1));
            String d = "";
            while(d.isEmpty()) {
                d = scanner.nextLine();
                if (cardStack.containsValue(d)) {
                    System.out.println("The definition \"" + d + "\" already exists. Try again.");
                    d = "";
                }
            }
            cardStack.put(q, d);
            valueToKey.put(d, q);
        }
    }

    private static void doQuiz(Scanner scanner, Map<String, String> cardStack, Map<String,
            String> valueToKey, Set<Map.Entry<String, String>> entries) {

        for(Map.Entry<String, String> e : entries){
            String q = e.getKey();
            String d = e.getValue();
            System.out.println("Tell me the answer for question \"" + q + "\":");
            String a = scanner.nextLine();
            if (d.equals(a)) {
                System.out.println("Correct answer.");
            }else if(cardStack.containsValue(a)){
                System.out.println("Wrong answer. The correct one is \"" + d + "\", you've just written the definition" +
                                    " of \"" + valueToKey.get(a) + "\".");
            }else{
                System.out.println("Wrong answer of "+ a +". The correct one is \"" + d + "\".");
            }
        }
    }
}
