import java.util.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;
import java.io.*;
/**
 * CPSC 1150 W01
 * Assignment 4 part 2
 * Intructor: Dr. Bita Shadgar
 * @author Cordell Bonnieux
 * @date 07/12/2021
 * Description: A classic game of hangman; user guesses a letter and the game keeps score.
 */
public class SecretPhrase {
    public static void main(String[] args) throws Exception {

        // welcome screen, how many rounds?
        int rounds = getRounds();

        // 2D array to store round info
        String[][] data = new String[rounds][2];

        for (int i = 0; i < rounds; i++) {

            // get a phrase
            String secretPhrase = getPhrase(data);

            // make a hidden phrase version
            String phrase = scramblePhrase(secretPhrase, scrambleIndex(secretPhrase, spaces(secretPhrase)));

            // triggers when phrase == secret phrase
            Boolean complete = false;

            // round counter
            int counter = 1;

            while (!complete) {

                if (secretPhrase.equals(phrase)) {

                    complete = true;

                } else {
                    
                    // read a guess from the user
                    char userGuess = guess(phrase, counter);

                    // if guess is valid, update the phrase
                    phrase = checkPhrase(phrase, secretPhrase, userGuess);

                    // count the rounds
                    counter++;  

                }
            }

            // calculate and display user score for the match
            printSummary(secretPhrase, counter);

            // add the score and phrase to the score keeping array
            data[i][0] = secretPhrase;

            double score = secretPhrase.length() / counter;
            data[i][1] = Double.toString(score); 

        }

        // print the summary of the game
        printGameSummary(data);

        System.exit(0);
    }

    /**
     * Selects one of ten random Arnold Schwarzenegger quotes to be used in the game.
     * @return a random quote
     */
    public static String getPhrase(String[][] data) throws Exception{

        // get file
        File file = new File("phrases.txt");

        // scanner obj for the file
        Scanner scan = new Scanner(file);

        // number of phrases
        int max = 0;

        // count the lines
        while (scan.hasNextLine()) {
            max++;
            scan.nextLine();
        }
        scan.close();

        // select a random number line from the file
        int choice = (int)(Math.random() * (max - 1)) + 1;

        // to hold string from file
        StringBuilder phrase = new StringBuilder();

        // re-assign scanner
        scan = new Scanner(file);

        // read a line from file
        for (int i = 0; i < max; i++) {

            String line = "";

            if (i == choice) {

                line = scan.nextLine();
                phrase.append(line);

            } else if (scan.hasNextLine())
                scan.nextLine();
        }
        scan.close();

        String phraseS = phrase.toString();

        // only make duplicate lines if the user wants to play more rounds than there are lines
        for (int i = 0; i < data.length; i++) {
            if (max < data.length && i >= max)
                return phraseS;
            else if (data[i][0] == phraseS)
                return getPhrase(data);
        }

        return phraseS;
    }

    /**
     * Reads a character from the user, ensures that it is valid (i.e. not a number) and that 
     * it is not an already revealed.
     * @param phrase the phrase to be deciphered
     * @param counter counts which guess number it is currently
     * @return the user's guess (a character)
     */
    public static char guess(String phrase, int counter) {

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box,BoxLayout.Y_AXIS));

        JLabel prompt = new JLabel(String.format("Round #" + counter + ": Guess a letter to complete this sentence:"));
        JPanel promptP = new JPanel();
        promptP.add(prompt);
        box.add(promptP);

        JLabel quote = new JLabel(String.format(phrase));
        JPanel quoteP = new JPanel();
        quoteP.add(quote);
        box.add(quoteP);

        String input = JOptionPane.showInputDialog(null, box);
        char guess = (input.length() > 0) ? input.charAt(0) : ' ';
         

        // make sure guess is not a number
        Boolean isDigit = Character.isDigit(guess);
        if (isDigit){
            JOptionPane.showMessageDialog(null, "Your guess was a number, please enter a letter.");

            return guess(phrase, counter);
        }

        // make sure the guess is not already revealed
        for (int i = 0; i < phrase.length() - 1; i++) {
            if (phrase.charAt(i) == guess) {
                JOptionPane.showMessageDialog(null, "That letter is already revealed, pick a new letter.");

                return guess(phrase, counter);
            }
        }

        return guess;
    }

    /**
     * This program takes a phrase, and an array of indexs to scramble it with.
     * Then it iterates over the phrase, replacing each index point with an asterik.
     * @param phrase a phrase to be scrambled
     * @param scrambleIndex the indexes of the letters to be scrambled
     * @return a scrambled version of the phrase
     */
    public static String scramblePhrase(String phrase, int[] scrambleIndex) {

        //hold the scrambled phrase
        String copy = "";

        // scramble the phrase
        for (int w = 0; w < phrase.length(); w++) {

            // assign asteriks'
            for (int v = 0; v < scrambleIndex.length; v++) {
                if (scrambleIndex[v] == w) {
                    copy += '*';
                    break;
                }      
            }

            if (copy.length() <= w)
                    copy += phrase.charAt(w);
        }

        return copy;
    }

    /**
     * This method takes in a phrase and determines at what indexes it's
     * punctuation and spaces are at. It then returns and array of those indexes.
     * @param phrase the phrase to be examined
     * @return and array of indexes that represent the punctuation marks and spaces in the phrase
     */
    public static int[] spaces(String phrase) {

        // count the number of spaces and punctuation marks in the phrase
        int spaces = 0;
        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == ' ' || phrase.charAt(i) == ',' || phrase.charAt(i) == '.' || phrase.charAt(i) == '!')
                spaces++;
        }
        
        // create an array which indexes the spaces and punctuation
        int[] spaceArr = new int[spaces];
        
        // add the space and punctuation indexes to the array
        int x = 0;
        for (int i = 0; i < phrase.length(); i++) {
            if (phrase.charAt(i) == ' ' || phrase.charAt(i) == ',' || phrase.charAt(i) == '.' || phrase.charAt(i) == '!') {
                spaceArr[x] = i;
                x++;
            }
        }

        return spaceArr;
    }

    /**
     * This method determines which letters will be replaced with asteriks'.
     * @param phrase a phrase to do calculations on
     * @param spaceArr the indexes of the spaces and punctuation marks in the phrase
     * @return an array of indexes which represent which letters to replace with asteriks'
     */
    public static int[] scrambleIndex(String phrase, int[] spaceArr) {
        // create a scramble index list
        int[] scrambleIndex = new int[(((phrase.length() - spaceArr.length) / 2))];

        // choose scramble indexes
        int i = 0;
        while (i < scrambleIndex.length) {
        
            // select a random index
            //int pos = (int)(Math.random() * (phrase.length() - 0)) + 0;
            int pos = new Random().nextInt(phrase.length());

            // check if the pos has any duplicate letters
            int duplicateCounter = 0;
            for (int y = 0; y < phrase.length(); y++) {
                char a = Character.toLowerCase(phrase.charAt(y)),
                    b = Character.toLowerCase(phrase.charAt(pos));

                if (phrase.charAt(y) == ' ' || phrase.charAt(y) == ',' || phrase.charAt(y) == '.' || phrase.charAt(y) == '!')
                    continue;
                else if (a == b && y != pos)
                    duplicateCounter++;
            }

            // if the number of duplicates exceed the free space left in the array, break.
            if ((scrambleIndex.length - (i + 1)) <= duplicateCounter) break;

            // find each duplicate and store in an array
            int[] duplicates = new int[duplicateCounter];
            int d = 0;
            for (int y = 0; y < phrase.length(); y++) {
                char a = Character.toLowerCase(phrase.charAt(y)),
                    b = Character.toLowerCase(phrase.charAt(pos));

                if (phrase.charAt(y) == ' ' || phrase.charAt(y) == ',' || phrase.charAt(y) == '.' || phrase.charAt(y) == '!')
                    continue;
                else if (a == b && y != pos) {
                    duplicates[d] = y;
                    d++;
                }
            }

            // check if pos already exists
            Boolean valid = true;
            for (int y = 0; y < scrambleIndex.length; y++) {
                if (scrambleIndex[y] == pos)
                    valid = false;
            }

            // check if there is a space at that pos
            for (int y = 0; y < spaceArr.length; y++){
                if (spaceArr[y] == pos)
                    valid = false;
            }

            // add to scramble index list
            if (duplicateCounter > 0 && valid && (duplicates.length + 1) < (scrambleIndex.length - i)) {
                scrambleIndex[i] = pos;
                i++;
                for (int y = 0; y < duplicates.length; y++) {
                    scrambleIndex[i] = duplicates[y];
                    i++;
                }
            } else if (valid) {
                scrambleIndex[i] = pos;
                i++;
            }
        }

        return scrambleIndex;
    }

    /**
     * This method checks to see if the user guessed a correct letter.
     * @param phrase the phrase to be deciphered
     * @param secretPhrase the deciphered phrase
     * @param userGuess the user's guess (a character)
     * @return the updated phrase (if user guessed correctly, the same phrase if not)
     */
    public static String checkPhrase(String phrase, String secretPhrase, char userGuess) {

        String copy = "";

        for (int i = 0; i < phrase.length(); i++) {

            char a = Character.toLowerCase(secretPhrase.charAt(i)),
            b = Character.toLowerCase(userGuess);

            if (phrase.charAt(i) == '*' && a == b)
                copy += secretPhrase.charAt(i);
            else
                copy += phrase.charAt(i);
        }

        return copy;
    }

    /**
     * Calculates and displays the users score, the secret phrase and the number of tries.
     * @param secretPhrase the phrase that was deciphered
     * @param counter the amount of tries it took the user to decipher the phrase
     */
    public static void printSummary(String secretPhrase, int counter) {
        // calculate and display user score
        int length = secretPhrase.length();
        double score = length / counter;
        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary,BoxLayout.Y_AXIS));
        
        JLabel goodbye = new JLabel(String.format("Nice work, you found the secret phrase!"));
        JPanel goodbyeP = new JPanel();
        goodbyeP.add(goodbye); 
        summary.add(goodbyeP);
        
        JLabel secret = new JLabel(String.format(secretPhrase));
        JPanel secretP = new JPanel();
        secretP.add(secret);
        summary.add(secretP);
        
        JLabel results = new JLabel(String.format("Number of tries: %d; Length of phrase: %d; Score: %.2f", counter, length, score));
        JPanel resultsP = new JPanel();
        resultsP.add(results); 
        summary.add(resultsP);
        
        JOptionPane.showMessageDialog(null, summary);
    }

    /**
     * Reads the number of rounds from the user, if invalid the method calls again
     * @return and integer that represents the number of game rounds 
     */
    public static int getRounds() {

        String selection = JOptionPane.showInputDialog(null, "Enter the number(integer) of rounds you'd like to play.");

        try {

            int num = Integer.parseInt(selection);
            return num;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(null, "Error: couldn't read your number, try again.");
            return getRounds();

        }

    }

    public static void printGameSummary(String[][] data) {


        /// this method needs work!

        // create a display panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        // create a heading
        JLabel header = new JLabel(String.format("Round         Target Phrase       Score"));
        JPanel topRow = new JPanel();
        topRow.add(header);
        panel.add(topRow);

        Double total = 0.0;

        // add each game
        for (int i = 0; i < data.length; i++) {
            total += Double.valueOf(data[i][1]);
            JPanel y = new JPanel();

            // here!!!
            
            JLabel x = new JLabel(String.format("%-2d %-s %-2.2f", (i+1), data[i][0], Double.valueOf(data[i][1])));
            y.add(x);
            panel.add(y);
        }

        System.out.println(total);

        // add the average score across all games
        JLabel avg = new JLabel("Your average score is " + (total / data.length));
        JPanel bottomRow = new JPanel();
        bottomRow.add(avg);
        panel.add(bottomRow);

        // print the summary of the entire game here 
    }
}