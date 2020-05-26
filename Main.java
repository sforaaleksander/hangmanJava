import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.text.*;
import java.time.LocalDate;
// import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String countryCapital = readFromFileChooseRandom();
        System.out.println(countryCapital);
        String[] countryAndCapitalList = countryCapital.split("[|]+");
        String country = countryAndCapitalList[0].strip().toUpperCase();
        String capitalToGuess = countryAndCapitalList[1].strip().toUpperCase();
        int userLifes = 10;
        List<String> lettersNotInWord = new ArrayList<String>();
        ArrayList<String> dashedWordList = new ArrayList<String>();
        for (int i = 0; i < capitalToGuess.length(); i++) {
            dashedWordList.add("_");
        }

        do {
            printDashed(dashedWordList);
            System.out.println("Type in a letter to guess!");
            printOrNotMissedLetters(lettersNotInWord);
            System.out.println("Your lifes " + userLifes);
            Scanner scan = new Scanner(System.in);
            String userLetter = scan.next().toUpperCase();
            clearScreen();
            if (capitalToGuess.contains(userLetter) && !dashedWordList.contains(userLetter)) {
                System.out.println("YOU GUESSED THE LETTER");
                dashedWordList = replaceDashesWithLetters(dashedWordList, capitalToGuess, userLetter);
            } else if (!capitalToGuess.contains(userLetter) && !lettersNotInWord.contains(userLetter)) {
                System.out.println("Missed!");
                userLifes -= 1;
                lettersNotInWord.add(userLetter);
                if (userLifes == 0) {
                    endGame("loose", capitalToGuess, country, 0);
                    playAgain();
                }
            } else
                System.out.println("You guessed that letter already!");
            if (userLifes == 1) {
                System.out.println("Your last chance! The city is the capital of " + country);
            }
        } while (dashedWordList.contains("_"));
        long end = System.currentTimeMillis();
        long gameTime = (end - start) / 1000;
        endGame("win", capitalToGuess, country, gameTime);
        playAgain();
    }

    public static ArrayList<String> replaceDashesWithLetters(ArrayList<String> dashedWordList, String capitalToGuess,
            String userLetter) {
        for (int i = 0; i < dashedWordList.size(); i++) {
            char capitalLetter = capitalToGuess.charAt(i);
            char userCharacter = userLetter.charAt(0);
            if (capitalLetter == userCharacter) {
                dashedWordList.set(i, userLetter);
            }
        }
        return dashedWordList;
    }

    public static void printOrNotMissedLetters(List<String> lettersNotInWord) {
        if (lettersNotInWord.size() > 0) {
            String lettersJoined = String.join(",", lettersNotInWord);
            System.out.println("Letters you have missed: " + lettersJoined);
        }
    }

    public static void printDashed(ArrayList<String> dashedString) {
        String result = "";
        for (String dash : dashedString) {
            result += dash + " ";
        }
        System.out.println(result);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void endGame(String winLose, String capitalToGuess, String country, long gameTime) {
        clearScreen();
        System.out.println("You " + winLose + "!");
        System.out.println("The capital of " + country + ", " + capitalToGuess + " was the sercet word!");
        if (winLose.equals("win")) {
            System.out.println("Your time was " + gameTime + " seconds!");
            writeToFile(capitalToGuess, gameTime);
        }
        printHighScores();
    }

    public static void playAgain() {
        System.out.println("Do you want to play again? [Y/N] ");
        Scanner scan = new Scanner(System.in);
        String userInput = scan.next().toUpperCase();
        if (userInput.equals("Y")) {
            clearScreen();
            mainCaller();
        } else {
            System.exit(0);
        }
    }

    static void mainCaller() {
        main(null);
    }

    public static String readFromFileChooseRandom() {
        ArrayList<String> fullData = new ArrayList<String>();
        try {
            File textObject = new File("countries-capitals.txt");
            Scanner textReader = new Scanner(textObject);
            while (textReader.hasNextLine()) {
                String data = textReader.nextLine();
                fullData.add(data);
            }
            textReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not load the file.");
            e.printStackTrace();
        }
        Random randomInt = new Random();
        String countryCapital = fullData.get(randomInt.nextInt(fullData.size()));
        return countryCapital;
    }

    public static void writeToFile(String capitalToGuess, long gameTime){
        System.out.println("Type in your name: ");
        Scanner scan = new Scanner(System.in);
        String userName = scan.next();
        LocalDate today = LocalDate.now();
        // SimpleDateFormat dateStr = new SimpleDateFormat("yyy.MM.dd");
        String[] highScoreEntry = {userName, today.toString(), capitalToGuess, Long.toString(gameTime)}; 
        String joinedEntry = String.join("|", highScoreEntry);
        try {
            File file = new File("highscores.txt");
            FileWriter highScores = new FileWriter(file, true);
            highScores.write(joinedEntry + System.lineSeparator());
            // highScores.newLine();
            highScores.close();
            } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }


    public static void printHighScores(){
        ArrayList<String> highScoresList = new ArrayList<>();
        try {
            File textObject = new File("highscores.txt");
            Scanner textReader = new Scanner(textObject);
            while (textReader.hasNextLine()) {
                String data = textReader.nextLine();
                highScoresList.add(data);
            }
            textReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not open high scores file.");
            e.printStackTrace();
        }
        clearScreen();
        System.out.println("HIGH SCORES");
        if (highScoresList.size() < 10) {
            for (String element : highScoresList){
                System.out.println(element);
            }}
        else {
            for (int i=0; i<10; i++) {
                System.out.println(highScoresList.get(i));
            }
        }
    }
}

// public static String convertDashedToString(ArrayList<String> dashedWord){
// StringBuffer buffer = new StringBuffer();
// for (String s : dashedWord) {
// buffer.append(s);
// }
// String str = buffer.toString();
// return str;}