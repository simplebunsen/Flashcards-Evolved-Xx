package flashcards;

import java.util.*;


public class Main {

    static Map<String, String> cardStack = new LinkedHashMap<>();
    static Map<String, String> valueToKey = new HashMap<>();
    static Set<Map.Entry<String, String>> entries = cardStack.entrySet();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;
        while(running) {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            String action = scanner.nextLine();
            running = decideAction(action);
        }

    }

    private static boolean decideAction(String action) {

        boolean running = true;
        switch (action) {
            case "add":
                addCard();
                break;
            case "remove":
                removeCard();
                break;
            case "import":
                importFromFile(scanner.nextLine());
                break;
            case "export":
                exportToFile(scanner.nextLine());
                break;
            case "ask":
                System.out.println("How many times to ask?");
                int n = scanner.nextInt(); scanner.nextLine();
                doQuiz(n);
                break;
            case "exit":
                System.out.println("Bye bye!");
                running = false;
                break;
            case "print":
                System.out.println(cardStack);
                break;
            default:
                System.out.printf("%s is not a valid input!\n", action);
                break;
        }

        return running;

    }

    private static void addCard() {
        System.out.println("The question of your new card");
        String q = "";
        while (q.isEmpty()) {
            q = scanner.nextLine();
            if (cardStack.containsKey(q)) {
                System.out.println("The card \"" + q + "\" already exists. Try again.");
                q = "";
            }
        }
        System.out.println("The definition of your new card");
        String d = "";
        while (d.isEmpty()) {
            d = scanner.nextLine();
            if (cardStack.containsValue(d)) {
                System.out.println("The definition \"" + d + "\" already exists. Try again.");
                d = "";
            }
        }
        cardStack.put(q, d);
        valueToKey.put(d, q);
        System.out.printf("The new card with (\"%s\":\"%s\") has successfully been added.\n", q, d);
    }

    private static void removeCard() {
        System.out.println("The question of the card you want to remove");
        String q = scanner.nextLine();
        if (!cardStack.containsKey(q)) {
            System.out.println("The card \"" + q + "\" doesn't exist. Going back to main menu.");
            return;
        }
        String d = cardStack.get(q);
        cardStack.remove(q);
        valueToKey.remove(d);
        System.out.printf("The card with (\"%s\":\"%s\") has successfully been removed.\n", q, d);
    }

    private static void importFromFile(String fileName) {

    }

    private static void exportToFile(String fileName) {

    }

    private static void doQuiz(int times) {

        String[] keySet = cardStack.keySet().toArray(new String[cardStack.size()]);
        Random random = new Random();
        //System.out.println(Arrays.toString(keySet));


        for (int i = 0; i < times; i++) {
            int rnjeesus =  random.nextInt(cardStack.size() - 1);
            String q = keySet[rnjeesus];
            String d = cardStack.get(q);
            System.out.println("Tell me the answer for question \"" + q + "\":");
            String a = scanner.nextLine();
            if (d.equals(a)) {
                System.out.println("Correct answer.");
            } else if (cardStack.containsValue(a)) {
                System.out.printf("Wrong answer. The correct one is \"%s\", you've just " +
                        "written the definition of \"%s\".\n", d, valueToKey.get(a));
            } else {
                System.out.println("Wrong answer of " + a + ". The correct one is \"" + d + "\".");
            }
        }
    }
}
