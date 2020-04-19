package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
                System.out.println("File name:");
                importFromFile(scanner.nextLine());
                break;
            case "export":
                System.out.println("File name:");
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
                //TODO: ask again instead of going back to main
                return;
                //q = "";
            }
        }
        System.out.println("The definition of your new card");
        String d = "";
        while (d.isEmpty()) {
            d = scanner.nextLine();
            if (cardStack.containsValue(d)) {
                System.out.println("The definition \"" + d + "\" already exists. Try again.");
                //TODO: ask again instead of going back to main
                return;
                //d = "";
            }
        }
        cardStack.put(q, d);
        valueToKey.put(d, q);
        System.out.printf("The new card with (\"%s\":\"%s\") has been added.\n", q, d);
    }

    private static void removeCard() {
        System.out.println("The question of the card you want to remove");
        String q = scanner.nextLine();
        if (!cardStack.containsKey(q)) {
            System.out.println("Can't remove \"" + q + "\", doesn't exist. Going back to main menu.");
            return;
        }
        String d = cardStack.get(q);
        cardStack.remove(q);
        valueToKey.remove(d);
        System.out.printf("The card with (\"%s\":\"%s\") has been removed.\n", q, d);
    }

    private static void importFromFile(String fileName) {
        int importCount = 0;
        File file = new File("F:\\Desktop\\Testing Flashcards\\" + fileName);
        try(Scanner fileScanner = new Scanner(file)){
            while(fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] splitLine = line.split(":");

                if(cardStack.containsKey(splitLine[0])){
                    //dont forget to change in valueToKey counterpart as well.
                    String preVal = cardStack.get(splitLine[0]);
                    cardStack.replace(splitLine[0], splitLine[1]);
                    valueToKey.remove(preVal);
                    valueToKey.put(splitLine[1], splitLine[0]);
                    //System.out.println("successfully overwrote val for key " + splitLine[0]);
                } else {
                    //System.out.println("Key " + splitLine[0] + " not here, adding anew");
                    cardStack.put(splitLine[0], splitLine[1]);
                    valueToKey.put(splitLine[1], splitLine[0]);
                }

                importCount++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("not found");
            return;
        }
        System.out.printf("%d cards have been loaded.\n", importCount);
    }


    private static void exportToFile(String fileName) {
        int exportCount = 0;
        String[] keySet = cardStack.keySet().toArray(new String[cardStack.size()]);
        try (PrintWriter printWriter = new PrintWriter("F:\\Desktop\\Testing Flashcards\\" + fileName)) {
            for(String e : keySet){
                //System.out.println("working with " + e);
                printWriter.print(e);
                printWriter.print(":");
                printWriter.print(cardStack.get(e));
                printWriter.println();
                exportCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.printf("%d cards have been saved.\n", exportCount);
        }
    }

    private static void doQuiz(int times) {
        String[] keySet = cardStack.keySet().toArray(new String[cardStack.size()]);
        Random random = new Random();
        //System.out.println(Arrays.toString(keySet));

        for (int i = 0; i < times; i++) {
            //0 so we don't get random from 0 - 0 if size = 1;
            int rnjeesus = 0;
            if(cardStack.size()  > 1) {
                rnjeesus = random.nextInt(cardStack.size() - 1);
            }
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
                System.out.println("Wrong answer. The correct one is \"" + d + "\".");
            }
        }
    }
}
