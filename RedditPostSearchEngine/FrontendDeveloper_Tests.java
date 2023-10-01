

import java.util.List;
import java.util.Scanner;

public class FrontendDeveloper_Tests {
    /**
     * This helper removes duplicate code from having to initialize TextUITester everytime.
     * @param expectedInput will be passed to the tester constructor as user input
     * @param expectedOutput will be used to compare with the output of textUITester
     * @return true if the comparison is correct, false otherwise
     */
    private static boolean inOutHelper(String expectedInput, String expectedOutput){
        TextUITester test = new TextUITester(expectedInput);
        Scanner sc = new Scanner(System.in);
        CHSearchFrontendFD fd =  new CHSearchFrontendFD(sc, new CHSearchBackendFD(null, null));
        //Prompts the user for input
        fd.runCommandLoop();
        String result = test.checkOutput();

        if(!result.contains(expectedOutput) || !result.startsWith("Choose an option from the list below")){
            System.out.println("Expected out: \n"+ expectedOutput);
            System.out.println("Resulted output: \n" +result);
            return false;
        }

        return true;
    }

    /**
     * Tests the main menu output and the statistics command
     * @return true if the output is correct, false otherwise
     */
    public static boolean test1() { /* test code here */
        //Main menu
        try{
            String menu = "[L]oad data from file\n" +
                    "Search Post [T]itles\n" +
                    "Search Post [B]odies\n" +
                    "Search [P]ost titles and bodies\n" +
                    "Display [S]tatistics for dataset\n" +
                    "[Q]uit";
            String result = "You have chosen to quit. Thank you for using the program!";

            if(!inOutHelper("q\n", menu) || !inOutHelper("q\n", result)){
                System.out.println("Error, test1(): main menu prompt was not displayed correctly after using Q");
                return false;
            }

            if(!inOutHelper("S\nQ\n", "You have chosen to display statistics for dataset")||
            !inOutHelper("S\nQ\n", "90% reddit users are still living in their mom's basement!")){
                System.out.println("Error, test1(): statistics were not displayed correctly");
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Testing chooseSearchWordsPrompt to ensure it correctly returns the user input
     * @return true if no bugs found, false otherwise
     */
    public static boolean test2() {

            try {
                // Test 1: Check if it accepts an input of one word correctly, with no edits.
                TextUITester test = new TextUITester("Sussy\nY\n");
                CHSearchFrontendFD frontend =
                        new CHSearchFrontendFD(new Scanner(System.in), new CHSearchBackendFD(null, null));

                List<String> expectedResult = frontend.chooseSearchWordsPrompt();

                String output = test.checkOutput();

                if (expectedResult.size() != 1 || !expectedResult.get(0).equals("Sussy")) {
                    System.out.println("Error, test2(): chooseSearchwordsprompt did not correctly return a single search word");
                    return false;
                }

                String confirmationPrompt = "Do you want to proceed with the search? (Enter Y) or edit your search term(s)? (Enter E)";
                if (!output.contains(confirmationPrompt)) {
                    System.out.println("Error, test2(): chooseSearchWords did not provide a confirmation prompt");
                    return false;
                }

                // Testing if it still accepts input after the first confirmation
                test = new TextUITester("Sussy\nY\nBaka\nY\n");
                frontend = new CHSearchFrontendFD(new Scanner(System.in), new CHSearchBackendFD(null, null));
                expectedResult = frontend.chooseSearchWordsPrompt();
                output = test.checkOutput();

                if (expectedResult.size() != 1 || !expectedResult.get(0).equals("Sussy")) {
                    System.out.println("Error, test2(): chooseSearchWords accepted a second input after confirmation");
                    return false;
                }
                if (!output.contains(confirmationPrompt)) {
                    System.out.println("Error, test2(): chooseSearchWords did not provide a confirmation prompt-2");
                    return false;
                }

                // Test 2: Testing when user inputed an incorrect confirmation, then with correct confirmation.
                test = new TextUITester("Sussy\nbaka\ne\nNotSussy\nY\n");
                frontend = new CHSearchFrontendFD(new Scanner(System.in), new CHSearchBackendFD(null, null));
                expectedResult = frontend.chooseSearchWordsPrompt();
                output = test.checkOutput();

                if (expectedResult.size() != 1 || !expectedResult.get(0).equals("NotSussy")) {
                    System.out.println("Error, test2(): chooseSearchWords did not override after user replaced with single word");
                    return false;
                }

                if (!output.contains("Do you want to proceed with the search? (Enter Y) or edit your search term(s)? (Enter E)")) {
                    System.out.println("Error, test2(): chooseSearchWords did not prompt the user for a correct input");
                    return false;
                }

                // Test 3: User input with multiple words and spaces
                test = new TextUITester("Sussy pink baka\nY\n");
                frontend = new CHSearchFrontendFD(new Scanner(System.in), new CHSearchBackendFD(null, null));
                expectedResult = frontend.chooseSearchWordsPrompt();
                output = test.checkOutput();

                if (expectedResult.size() != 3 || !expectedResult.get(0).equals("Sussy") || !expectedResult.get(1).equals("pink") ||
                        !expectedResult.get(2).equals("baka")) {
                    System.out.println("Error, test2(): chooseSearchWords did not return a correct list with 3 elements");
                    return false;
                }
                if (!output.contains(confirmationPrompt)) {
                    System.out.println("Error, test2(): chooseSearchWords did not prompt the user for a correct input");
                    return false;
                }

                // Test 4: User input with invalid confirmation and with a sentence of words
                test = new TextUITester("Sussy baka red\nwhat\nE\nBalls Huge\nY\n");
                frontend = new CHSearchFrontendFD(new Scanner(System.in), new CHSearchBackendFD(null, null));
                expectedResult = frontend.chooseSearchWordsPrompt();
                output = test.checkOutput();

                if (expectedResult.size() != 2 || !expectedResult.get(0).equals("Balls") || !expectedResult.get(1).equals("Huge")) {
                    System.out.println("Error, test2(): chooseSearchWords did not return a correct list with 2 elements after an invalid confirmation");
                    return false;
                }

                if (!output.contains(confirmationPrompt)) {
                    System.out.println("Error, test2(): chooseSearchWords did not prompt the user for a correct input after a sentence with multiple words");
                    return false;
                }

                // Test 5: Some random input
                test = new TextUITester("Edit this\nE\nBig sussy baka juicy\nred\npink\norange\ne\ngoat\nY\n");
                frontend = new CHSearchFrontendFD(new Scanner(System.in), new CHSearchBackendFD(null, null));
                expectedResult = frontend.chooseSearchWordsPrompt();
                output = test.checkOutput();

                if (expectedResult.size() != 1 || !expectedResult.get(0).equals("goat")) {

                    System.out.println("Error, test2(): chooseSearchWords did not correctly return a list after user inputted random words");
                    return false;
                }

                if (!output.contains(confirmationPrompt)) {
                    System.out.println("Error, test2(): chooseSearchWords did not prompt the user for a correct input after random inputs");
                    return false;
                }
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        return true;
    }

    /**
     * Tests the implementation of search commands
     * @return true if no bugs found, false otherwise
     */
    public static boolean test3() {
        // Testing searchTitleCommand with one term
        if(!inOutHelper("T\nSussy\nY\nQ\n", "post with Sussy")){
            System.out.println("Error, test3(): searchTitle did not return the correct out with a single term");
            return false;
        }

        // Testing searchTitleCommand with two terms
        if(!inOutHelper("T\nSussy Baka\nY\nQ\n", "post with Sussy Baka")){
            System.out.println("Error, test3(): searchTitle did not return the correct out with two terms");
            return false;
        }

        //Testing searchTitleCommand with multiple user input

        if(!inOutHelper("T\nSussy Baka\na\nc\nE\nRed\nE\nSussy Baka Red\nY\nQ\n", "post with Sussy Baka Red")){
            System.out.println("Error, test3(): searchTitle did not return the correct out with multiple user inputs");
            return false;
        }
        // Testing searchBodyCommand with one term
        if(!inOutHelper("B\nSussy\nY\nQ\n", "post with body words: Sussy")){
            System.out.println("Error, test3(): searchBody did not return the correct out with a single term");
            return false;
        }
        // Testing searchBodyCommand with two terms
        if(!inOutHelper("B\nSussy Baka\nY\nQ\n", "post with body words: Sussy Baka")){
            System.out.println("Error, test3(): searchBody did not return the correct out with two terms");
            return false;
        }
        //Testing searchBodyCommand with multiple user input
        if(!inOutHelper("B\nSussy Baka\na\nc\nE\nRed\nE\nSussy Baka Red\nY\nQ\n", "post with body words: Sussy Baka Red")){
            System.out.println("Error, test3(): searchBody did not return the correct out with multiple user inputs");
            return false;
        }
        //Testing searchBodyandTitleCommand with multiple user input
        String expectedOutput2 = "post with Sussy Baka Red";
        String expectedOutput3 = "post with body words: Sussy Baka Red";
        if(!inOutHelper("P\nSussy Baka\na\nc\nE\nRed\nE\nSussy Baka Red\nY\nQ\n", expectedOutput2)){
            System.out.println("Error, test3(): searchbodyandtitleCommand did not return the correct out with multiple user inputs");
            return false;
        }
        if(!inOutHelper("P\nSussy Baka\na\nc\nE\nRed\nE\nSussy Baka Red\nY\nQ\n", expectedOutput3)){
            System.out.println("Error, test3(): searchbodyandtitleCommand did not return the correct out with multiple user inputs");
            return false;
        }
        return true;
    }

    /**
     * Testing the implemetation of loadDataCommand()
     * @return true if no bugs found, false otherwise
     */
    public static boolean test4() {
        try {
            // Test 1: Valid filename
            if (!inOutHelper("L\nValid File\nQ\n", "This file contains multiple reddit posts about cute cats")) {
                System.out.println("Error, test4(): loadDataCommand() did not produce the correct output when loading a correct file");
                return false;
            }
            if (!inOutHelper("L\nAlso another file\nQ\n", "This file contains multiple reddit posts about cute cats")) {
                System.out.println("Error, test4(): loadDataCommand() did not produce the correct output when loading a correct file with different names");
                return false;
            }

            // Test 2: Invalid filename
            if (!inOutHelper("L\nillegalfile\nQ\n", "Error, file not found")) {
                System.out.println("Error, test4(): loadDataCommand() did not give the error message to suer when loading a wrong file");
                return false;
            }

            // Test 3: Combined input
            String input1 = "L\nillegalfile\nL\nillegalfile\nL\nValid file\nQ\n";
            if(!inOutHelper(input1, "Error, file not found")|| !inOutHelper(input1, "This file contains multiple reddit posts about cute cats")){
                System.out.println("Error test4(): loadDataCommand() did not give the correct messages when having multiple user input");
                return false;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Testing how the programm runs with multiple inputs
     * @return true if no bugs found, false otherwise
     */
    public static boolean test5() {
        String input = "S\nL\nLegalFile\nL\nillegalfile\nF\nP\nCute cats\nY\nQ\n";
        if(!inOutHelper(input, "90% reddit users are still living in their mom's basement!")){
            System.out.println("Error test5(): result did not contain statistics");
            return false;
        }
        if(!inOutHelper(input, "This file contains multiple reddit posts about cute cats")){
            System.out.println("Error test5(): result did not contain file loaded");
            return false;
        }
        if(!inOutHelper(input, "Error, file not found")){
            System.out.println("Error test5(): result did not contain error message when user input illegal file");
            return false;
        }
        if(!inOutHelper(input, "post with Cute cats")){
            System.out.println("Error test5(): result did not contain result from postwithbodyandtitlecommand");
            return false;
        }
        if(!inOutHelper(input, "post with body words: Cute cats")){
            System.out.println("Error test5(): result did not contain result from postwithbodyandtitlecommand");
            return false;
        }
        return true;
    }
    public static void main(String[]args){
        System.out.println("test1: "+ test1());
        System.out.println("test2: "+ test2());
        System.out.println("test3: "+ test3());
        System.out.println("test4: "+ test4());
        System.out.println("test5: "+ test5());
    }
}

