package flashcards;

import java.util.Scanner;

class card{
    public String question;
    public String definition;

    public card(String question, String definition) {
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
        card[] cardStack = new card[n];

        for (int i = 0; i < n; i++) {
            System.out.println("The question of card #" + (i+1));
            String q = scanner.nextLine();
            System.out.println("The definition of card #" + (i+1));
            String d = scanner.nextLine();
            cardStack[i] = new card(q, d);
        }

        for (int i = 0; i < n; i++) {
            String q = cardStack[i].question;
            String d = cardStack[i].definition;
            System.out.println("Tell me the answer for question \"" + q + "\":");
            String a = scanner.nextLine();
            if (d.equals(a)) {
                System.out.println("Correct answer.");
            }
            else{
                System.out.println("Wrong answer of "+a+". The correct one is \"" + d + "\".");
            }

        }

    }
}
