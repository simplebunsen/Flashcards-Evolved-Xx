package flashcards;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        scanner.nextLine();
        String def = scanner.nextLine();
        String answ = scanner.nextLine();

        if (def.equals(answ)) {
            System.out.println("right");
        }
        else{
            System.out.println("wrong");
        }

    }
}
