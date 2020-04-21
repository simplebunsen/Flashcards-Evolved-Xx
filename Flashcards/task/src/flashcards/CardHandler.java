package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


public class CardHandler {

    IOManager IO;

    Map<String, String> cardStack = new LinkedHashMap<>();
    Map<String, String> valueToKey = new HashMap<>();
    Map<String, Integer> cardMistakes = new HashMap<>();

    public CardHandler(IOManager IO) {
        this.IO = IO;
    }

    public Map<String, String> getCardStack() {
        return cardStack;
    }

    public void printHardestCard() {
        int currentMaxMistakes = 0;
        List<String> cardsWithMaxMistakes = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : cardMistakes.entrySet()){
            int mistakes = entry.getValue();
            if(mistakes > currentMaxMistakes){
                cardsWithMaxMistakes.clear();
                cardsWithMaxMistakes.add(entry.getKey());
                currentMaxMistakes = mistakes;
            }else if (mistakes == currentMaxMistakes && mistakes != 0) {
                cardsWithMaxMistakes.add(entry.getKey());
            }
        }
        switch(cardsWithMaxMistakes.size()){
            case 0:
                IO.outputLine("There are no cards with errors.");
                break;
            case 1:
                IO.outputNoLine("The hardest card is \"");
                IO.outputNoLine(cardsWithMaxMistakes.get(0));
                IO.outputLine(String.format("\". You have %d errors answering them.", cardMistakes.get(cardsWithMaxMistakes.get(0))));
                break;
            default:
                IO.outputNoLine("The hardest cards are ");
                for(int i = 0; i < cardsWithMaxMistakes.size(); i++){
                    if(i == cardsWithMaxMistakes.size() - 1){
                        IO.outputNoLine(String.format("\"%s\". ", cardsWithMaxMistakes.get(i)));
                        break;
                    }
                    IO.outputNoLine(String.format("\"%s\", ", cardsWithMaxMistakes.get(i)));
                }
                IO.outputLine(String.format("You have %d errors answering them.", cardMistakes.get(cardsWithMaxMistakes.get(0))));
                break;
        }
        IO.outputLine("");

    }

    public void resetMistakes() {
        for(Map.Entry<String, Integer> e : cardMistakes.entrySet()){
            cardMistakes.put(e.getKey(), 0);
        }
        IO.outputLine("All Card statistics has been reset.");
        IO.outputLine("");
    }

    public void addCard() {
        IO.outputLine("The question of your new card");
        String q = "";
        while (q.isEmpty()) {
            q = IO.getNextLine();
            if (cardStack.containsKey(q)) {
                IO.outputLine(String.format("The card \"%s\" already exists. Try again.", q));
                //TODO:
                return;
                //q = "";
            }
        }
        IO.outputLine("The definition of your new card");
        String d = "";
        while (d.isEmpty()) {
            d = IO.getNextLine();
            if (cardStack.containsValue(d)) {
                IO.outputLine(String.format("The definition \"%s\" already exists. Try again."));
                //TODO:
                return;
                //d = "";
            }
        }
        addToCardStack(q, d);
        IO.outputLine(String.format("The new card with (\"%s\":\"%s\") has been added.\n", q, d));
    }

    private void addToCardStack(String question, String definition) {
        cardStack.put(question, definition);
        valueToKey.put(definition, question);
        cardMistakes.put(question, 0);
    }

    public void removeCard() {
        IO.outputLine("The question of the card you want to remove");
        String q = IO.getNextLine();
        if (!cardStack.containsKey(q)) {
            IO.outputLine(String.format("Can't remove \"%s\", doesn't exist. Going back to main menu.", q));
            return;
        }
        String d = cardStack.get(q);
        cardStack.remove(q);
        valueToKey.remove(d);
        cardMistakes.remove(q);
        IO.outputLine(String.format("The card with (\"%s\":\"%s\") has been removed.\n", q, d));
    }

    public void importFromFile(String fileName) {
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
                    cardMistakes.replace(splitLine[0], Integer.parseInt(splitLine[2]));
                    //System.out.println("successfully overwrote val for key " + splitLine[0]);
                } else {
                    //System.out.println("Key " + splitLine[0] + " not here, adding anew");
                    addToCardStack(splitLine[0], splitLine[1]);
                    cardMistakes.put(splitLine[0], Integer.parseInt(splitLine[2]));
                }

                importCount++;
            }

        } catch (FileNotFoundException e) {
            IO.outputLine("not found");
            return;
        }
        IO.outputLine(String.format("%d cards have been loaded.\n", importCount));
    }


    public void exportToFile(String fileName) {
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
            IO.outputLine(String.format("%d cards have been saved.\n", exportCount));
        }
    }

    public void doQuiz(int times) {
        String[] keySet = cardStack.keySet().toArray(new String[cardStack.size()]);
        Random random = new Random();
        //System.out.println(Arrays.toString(keySet));

        for (int i = 0; i < times; i++) {
            //TODO:
            int rnjeesus = 0;
            if(cardStack.size() > 1) {
                rnjeesus = random.nextInt(cardStack.size());
            }
            String q = keySet[rnjeesus];
            String d = cardStack.get(q);
            IO.outputLine(String.format("Tell me the answer for question \"%s\":", q));
            String a = IO.getNextLine();
            if (d.equals(a)) {
                IO.outputLine("Correct answer.");
            } else if (cardStack.containsValue(a)) {
                IO.outputLine(String.format("Wrong answer. The correct one is \"%s\", you've just " +
                        "written the definition of \"%s\".\n", d, valueToKey.get(a)));
                cardMistakes.put(q, cardMistakes.get(q) + 1);
            } else {
                IO.outputLine(String.format("Wrong answer. The correct one is \"%s\".", d));
                cardMistakes.put(q, cardMistakes.get(q) + 1);
            }
        }
    }


}
