import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 * This class is the interactive engine for the game
 * interface. It allows the input from user to starts
 * game and other interactive commands
 *
 *
 * @Richmond Nartey Tettey, Dartmouth CS10, Winter 2025
 * */


public class GameEngine {
    BaconGame session;
    String universeCenter = "Kevin Bacon"; //default center universe
    String goal = "Buster Keaton"; //default goal
    Set<String> validInputs = Set.of("c", "d", "i", "p", "s", "u", "q"); //accepted commands

    /**
     * constructor method
     * instantiate new game object
     * sets endgame boolean to false
     * */
    public GameEngine()throws Exception{
        session = new BaconGame(universeCenter);
        session.setEndgame(!session.isEndgame());

    }

    /**
     * getter and setter methods for instance variables
     * */
    public void setUniverseCenter(String universeCenter) {
        this.universeCenter = universeCenter;
    }

    public String getUniverseCenter() {
        return universeCenter;
    }

    private void testGame()throws Exception{
        session.getPath(goal);
        session.startMovieTrace();
    }

    private boolean sessionOver(){
        return session.isEndgame();
    }

    private void endSession(){
        session.quitGame();
    }

    private void changeCenter(){
        try{
            session.changeCenterUniverse(universeCenter);
        } catch(Exception e){
            System.out.println("Failed to compute new average separation!" + e);
        }
    }

    /**
     * prints welcome message
     * @return string of commands to the use
     * */
    private String welcomeMessage(){
        return ("""
                
                Commands:
                c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation
                d <low> <high>: list actors sorted by degree, with degree between low and high
                i: list actors with infinite separation from the current center
                p <name>: find path from <name> to current center of the universe
                s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high
                u <name>: make <name> the center of the universe
                q: quit game
                """ + "\n" + "Current Center:" + universeCenter);
    }

    private void validateInput(String input){

        // Trim any leading or trailing spaces from input
        input = input.trim();

        // Split the input into at most two parts: command and arguments
        String[] parts = input.split(" ", 2);

        // Check if the command is a valid input
        if(validInputs.contains(parts[0])){

            try{
                // Case for setting universe center
                if(Objects.equals(parts[0], "u")){

                    // Ensure there is a second argument for the universe center
                    if(parts.length > 1){
                        setUniverseCenter(parts[1]);
                        changeCenter();
                    } else {
                        System.out.println("Expect two arguments but provided 1 or less");
                    }

                }
                // Case for finding the shortest path
                else if(Objects.equals(parts[0], "p")){

                    // Ensure there is a second argument for the path
                    if(parts.length > 1){
                        try{
                            session.getPath(parts[1]);
                            session.findShortestPath();
                        }catch(Exception e){
                            System.out.println("Actor not associated with " + universeCenter);
                        }
                    }
                }
                // Case for sorting by degree, expecting three arguments
                else if(Objects.equals(parts[0], "d")){

                    // Splitting input into three parts (command, lower bound, upper bound)
                    parts = input.split(" ", 3);

                    if(parts.length == 3){
                        try{
                            session.sortByDegree(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        }catch(Exception e){
                            System.out.println("Failed to find list of actors sorted by degree" + e);
                        }
                    } else {
                        System.out.println("Expected three arguments: d, lower and upper bound");
                    }
                }
                // Case for sorting by average separation
                else if(Objects.equals(parts[0], "c")){

                    System.out.println("loading...");

                    try{
                        // If the number is negative, convert it to positive and pass -1 to indicate bottom sorting
                        if(Integer.parseInt(parts[1]) < 0){
                            session.sortByAverageSeparation(-1, Math.abs(Integer.parseInt(parts[1])));
                        } else {
                            session.sortByAverageSeparation(1, Integer.parseInt(parts[1]));
                        }

                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
                // Case for finding infinite separations
                else if(Objects.equals(parts[0], "i")){
                    session.infiniteSeparation();
                }
                // Case for finding separations within a bound, expecting three arguments
                else if(Objects.equals(parts[0], "s")){
                    parts = input.split(" ", 3);

                    if(parts.length == 3){
                        try{
                            session.separationsFromUniverse(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        }catch(Exception e){
                            System.out.println("Failed to find list of non-infinite separations from center" + e);
                        }
                    } else {
                        System.out.println("Expected three arguments: s, lower and upper bound");
                    }
                }
                // Case for quitting the game
                else if(Objects.equals(parts[0], "q")){
                    session.quitGame();
                }

            }catch(Exception e){
                System.out.println("Invalid input" + e);
            }

        } else {
            System.out.println("Input invalid!");
        }
    }

    public static void main(String[] args) throws Exception {  // Main method, the entry point of the program, throws an exception in case of errors

        System.out.println("Welcome to Bacon Game!!!!");


        GameEngine game = new GameEngine();  // Creates a new instance of the GameEngine class, initializing the game


        while (!game.sessionOver()) {  // Loop that continues running as long as the session is not over
            System.out.println(game.welcomeMessage());  // Prints the welcome message by calling the welcomeMessage method from the GameEngine class

            Scanner line = new Scanner(System.in);  // Creates a new Scanner object to read user input from the console

            System.out.print("\n" + "->");  // Prints a prompt on the console to indicate the user should input something

            String input = line.nextLine();  // Reads the next line of user input

            game.validateInput(input);  // Passes the user input to the validateInput method of the GameEngine class to validate it
        }
    }
}
