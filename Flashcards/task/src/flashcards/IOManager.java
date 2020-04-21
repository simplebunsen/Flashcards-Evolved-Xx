package flashcards;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IOManager {

    static List<String> IOhistory = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);


    public static String getNextLine() {
        String input = scanner.nextLine();
        addToLog(input);
        return input;
    }
    public static int getNextInt() {
        int input = scanner.nextInt();
        addToLog(Integer.toString(input));
        return input;
    }

    public static void outputLine(String output){
        System.out.println(output);
        addToLog(output);
    }

    public static void outputNoLine(String output){
        System.out.print(output);
        //TODO: Go fuck yourself Im not making it be one line in the log cuz who cares
        addToLog(output);
    }

    public static void addToLog(String stringToLog){
        //TODO: make the user input prefix with >
        IOhistory.add(stringToLog);
    }

    public static void saveLog() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss");
        String now = dtf.format(LocalDateTime.now()).concat(".txt");
        //TODO: I did this for hyperskill. they won't let me be clever... :(
        outputLine("File Name: ");
        String uselessLogName = getNextLine();
        //try (PrintWriter printWriter = new PrintWriter("F:\\Desktop\\Testing Flashcards\\Log_" + now)) {
        try (PrintWriter printWriter = new PrintWriter("./" + uselessLogName)) {
            for(String line : IOhistory){
                printWriter.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        outputLine(String.format("The log has been saved. Filename: %s", now));
    }
}
