package flashcards;


public class Main {

    static IOManager IO = new IOManager();
    static CardHandler cardHandler = new CardHandler(IO);


    public static void main(String[] args) {
        boolean running = true;
        while(running) {
            IO.outputLine("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String action = IO.getNextLine();
            running = decideAction(action);
        }
    }

    private static boolean decideAction(String action) {

        boolean running = true;
        switch (action) {
            case "add":
                cardHandler.addCard();
                break;
            case "remove":
                cardHandler.removeCard();
                break;
            case "import":
                IO.outputLine("File name:");
                cardHandler.importFromFile(IO.getNextLine());
                break;
            case "export":
                IO.outputLine("File name:");
                cardHandler.exportToFile(IO.getNextLine());
                break;
            case "ask":
                if(cardHandler.getCardStack().size() == 0){
                    IO.outputLine("You haven't added any cards!");
                    break;
                }
                IO.outputLine("How many times to ask?");
                int n = IO.getNextInt(); IO.getNextLine();
                cardHandler.doQuiz(n);
                break;
            case "exit":
                IO.outputLine("Bye bye!");
                running = false;
                break;
            case "log":
                IOManager.saveLog();
                break;
            case "hardest card":
                cardHandler.printHardestCard();
                break;
            case "reset stats":
                cardHandler.resetMistakes();
                break;
            case "print":
                System.out.println(cardHandler.getCardStack());
                System.out.println(cardHandler.cardMistakes);
                break;
            default:
                IO.outputLine(String.format("%s is not a valid input!\n", action));
                break;
        }

        return running;

    }
}
