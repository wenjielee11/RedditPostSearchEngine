
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class implements a simple user interface for users to lookup posts, file data, and statistics
 */
public class CHSearchFrontendFD implements CHSearchFrontendInterface {
    Scanner sc;
    CHSearchBackendInterface backend;

    /**
     * Constructor for the class.
     * @param userInput initializes the scanner variable that will be used for user input by all methods
     * @param backend the backend instance to pass user input to
     */
    public CHSearchFrontendFD(Scanner userInput, CHSearchBackendInterface backend) {
        sc = userInput;
        this.backend = backend;
    }

    /**
     * This method is the driver that calls all methods, while receiving user input to navigate them through the program
     */
    @Override
    public void runCommandLoop() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Choose an option from the list below");
            char result = mainMenuPrompt();
            switch (result) {
                case 'L':
                    System.out.println("You have chosen load data from a file.");
                    loadDataCommand();
                    break;
                case 'T':
                    System.out.println("You have chosen to search for post titles.");
                    searchTitleCommand(chooseSearchWordsPrompt());
                    break;
                case 'B':
                    System.out.println("You have chosen to search for post bodies.");
                    searchBodyCommand(chooseSearchWordsPrompt());
                    break;
                case 'P':
                    System.out.println("You have chosen to search post titles and bodies.");
                    List<String> results = chooseSearchWordsPrompt();
                    searchPostCommand(results);
                    break;
                case 'S':
                    System.out.println("You have chosen to display statistics for dataset");
                    displayStatsCommand();
                    break;
                case 'Q':
                    System.out.println("You have chosen to quit. Thank you for using the program!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Please enter a valid character that is within the [] brackets!");
            }
        }
    }

    /**
     * Gives the user the following options to choose between, and return a character representing their choice
     *
     * @return the char input of choice, (or '\0' if they fail to make a valid selection)
     */
    @Override
    public char mainMenuPrompt() {
        String s = "[L]oad data from file\n" +
                "Search Post [T]itles\n" +
                "Search Post [B]odies\n" +
                "Search [P]ost titles and bodies\n" +
                "Display [S]tatistics for dataset\n" +
                "[Q]uit";
        System.out.println(s);
        String input = sc.nextLine();
        if (input.length() != 1) {
            return '\0';
        } else {
            char in = input.toUpperCase().charAt(0);
            switch (in) {
                case 'L':
                    return 'L';
                case 'T':
                    return 'T';
                case 'B':
                    return 'B';
                case 'P':
                    return 'P';
                case 'S':
                    return 'S';
                case 'Q':
                    return 'Q';
                default:
                    return '\0';
            }
        }


    }

    /**
     * This method prompts the suer for a file name. Will inform user that the file name is invalid if caugh Filenotfound
     */
    @Override
    public void loadDataCommand() {
        System.out.println("Please input a file to search: ");
        String input = sc.nextLine();
        try {
            backend.loadData(input);
            System.out.println(input +" loaded successfully.");
            displayStatsCommand();
        } catch (FileNotFoundException e) {
            System.out.println("Error, file not found");
        }
    }

    /**
     * This is a helper method that will allow the user to double check their input before passing them to other methods
     * @return a list of words that were seperated by spaces from a user input string
     */
    @Override
    public List<String> chooseSearchWordsPrompt() {
        List<String> searchTerms = null;
        while (true) {
            System.out.println("Please choose your search words: ");
            String input = sc.nextLine();
            searchTerms = Arrays.asList(input.split(" +"));
            System.out.println("You have chosen the following search terms: ");
            for (int i = 0; i < searchTerms.size(); i++) {
                System.out.print("Term " + (i + 1) + ": " + searchTerms.get(i) + "  ");
            }

            while (true) {
                System.out.println("Do you want to proceed with the search? (Enter Y) or edit your search term(s)? (Enter E)");
                input = sc.nextLine();
                if (input.toUpperCase().equals("Y")) {
                    return searchTerms;
                } else if (input.toUpperCase().equals("E")) {
                    break;
                }
            }
        }
    }

    /**
     * This method will search for the title of a post when given search terms
     * @param words user's search terms
     */
    @Override
    public void searchTitleCommand(List<String> words) {

        List<String> results = new ArrayList<>();
        String s = "";
        for (int i = 0; i < words.size(); i++) {
            s += words.get(i) + " ";
        }
        s = s.trim();
        results.addAll(backend.findPostsByTitleWords(s));
        System.out.println("Search term " + s + " returned the results: \n");
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i) + "\n");
        }
    }

    /**
     * This method searches for a post body that contains the specified words
     * @param words user input that wants to search for in body
     */
    @Override
    public void searchBodyCommand(List<String> words) {
        List<String> results = new ArrayList<>();
        String s = "";
        for (int i = 0; i < words.size(); i++) {
            s += words.get(i) + " ";
        }
        s = s.trim();
        results.addAll(backend.findPostsByBodyWords(s));
        System.out.println("Search term " + s + " returned the results: \n");
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i) + "\n");
        }
    }

    /**
     * This method searches for both title and body
     * @param words the specified search terms that the user wants in the title and body
     */
    @Override
    public void searchPostCommand(List<String> words) {
        List<String> results = new ArrayList<>();
            String s = "";
            for (int i = 0; i < words.size(); i++) {
                s += words.get(i) + " ";
            }
            //Trim to avoid excess space
            s = s.trim();
            results.addAll(backend.findPostsByTitleOrBodyWords(s));
            System.out.println("Search term " + s + " returned the results: \n");
            for (int i = 0; i < results.size(); i++) {
                System.out.println(results.get(i) + "\n");
            }
        }

    /**
     * This method displays the statistics.
     */
    @Override
        public void displayStatsCommand () {
            System.out.println(backend.getStatisticsString());
        }
    }

