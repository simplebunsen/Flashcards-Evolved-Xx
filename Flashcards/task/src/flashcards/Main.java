package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class Main {

    static Map<String, String> cardStack = new LinkedHashMap<>();
    static Map<String, String> valueToKey = new HashMap<>();
    static Map<String, Integer> cardMistakes = new HashMap<>();
    static List<String> IOhistory = new ArrayList<>();
    //TODO: maybe remove entryset
    static Set<Map.Entry<String, String>> entries = cardStack.entrySet();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;
        while(running) {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            String action = getNextLine();
            running = decideAction(action);
        }

    }

    private static String getNextLine() {
        String input = scanner.nextLine();
        IOhistory.add(input);
        return input;
    }
    private static int getNextInt() {
        int input = scanner.nextInt();
        IOhistory.add(Integer.toString(input));
        return input;
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
                importFromFile(getNextLine());
                break;
            case "export":
                System.out.println("File name:");
                exportToFile(getNextLine());
                break;
            case "ask":
                System.out.println("How many times to ask?");
                int n = getNextInt(); scanner.nextLine();
                doQuiz(n);
                break;
            case "exit":
                System.out.println("Bye bye!");
                running = false;
                break;
            case "log":
                saveLog();
                break;
            case "hardest card":
                getHardestCard();
                break;
            case "reset stats":
                resetMistakes();
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


    private static void saveLog() {
    }

    private static void getHardestCard() {
        //TODO:
    }

    private static void resetMistakes() {
        for(Map.Entry<String, Integer> e : cardMistakes.entrySet()){
            cardMistakes.put(e.getKey(), 0);
        }
        System.out.println("All Card statistics has been reset.");
    }

    private static void addCard() {
        System.out.println("The question of your new card");
        String q = "";
        while (q.isEmpty()) {
            q = getNextLine();
            if (cardStack.containsKey(q)) {
                System.out.println("The card \"" + q + "\" already exists. Try again.");
                //TODO:
                return;
                //q = "";
            }
        }
        System.out.println("The definition of your new card");
        String d = "";
        while (d.isEmpty()) {
            d = getNextLine();
            if (cardStack.containsValue(d)) {
                System.out.println("The definition \"" + d + "\" already exists. Try again.");
                //TODO:
                return;
                //d = "";
            }
        }
        addToCardStack(q, d);
        System.out.printf("The new card with (\"%s\":\"%s\") has been added.\n", q, d);
    }

    private static void addToCardStack(String question, String definition) {
        cardStack.put(question, definition);
        valueToKey.put(definition, question);
        cardMistakes.put(question, 0);
    }

    private static void removeCard() {
        System.out.println("The question of the card you want to remove");
        String q = getNextLine();
        if (!cardStack.containsKey(q)) {
            System.out.println("Can't remove \"" + q + "\", doesn't exist. Going back to main menu.");
            return;
        }
        String d = cardStack.get(q);
        cardStack.remove(q);
        valueToKey.remove(d);
        cardMistakes.remove(q);
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
                    addToCardStack(splitLine[0], splitLine[1]);
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
            for(String q : keySet){
                //System.out.println("working with " + q);
                printWriter.print(q);
                printWriter.print(":");
                printWriter.print(cardStack.get(q));
                printWriter.print(":");
                printWriter.print(cardMistakes.get(q));
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
            //TODO:
            int rnjeesus = 0;
            if(cardStack.size()  > 1) {
                rnjeesus = random.nextInt(cardStack.size() - 1);
            }
            String q = keySet[rnjeesus];
            String d = cardStack.get(q);
            System.out.println("Tell me the answer for question \"" + q + "\":");
            String a = getNextLine();
            if (d.equals(a)) {
                System.out.println("Correct answer.");
            } else if (cardStack.containsValue(a)) {
                System.out.printf("Wrong answer. The correct one is \"%s\", you've just " +
                        "written the definition of \"%s\".\n", d, valueToKey.get(a));
                cardMistakes.put(q, cardMistakes.get(q) + 1);
            } else {
                System.out.println("Wrong answer. The correct one is \"" + d + "\".");
                cardMistakes.put(q, cardMistakes.get(q) + 1);
            }
        }
    }
}
