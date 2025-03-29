import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MovieGraph {
    private static Map<Integer,String> mapKeyToMovie;
    private static Map<Integer,String> mapKeyToActor;
    private static Map<Integer, Set<Integer>> mapMovieToActor;
    protected static AdjacencyMapGraph<String,Set<String>> actorsToActors;



    /**
     * create maps for keys to movies and keys to actors
     * @param pathKeyToMovie is the filepath for movie ID and its value
     * @param pathKeyToActor is the filepath for actor ID and its value
     * @param pathMovieToActor is the filepath for movie ID to Actor ID
     * */
    public static void createMaps(String pathKeyToMovie, String pathKeyToActor, String pathMovieToActor) throws IOException {

        //initialize hash set for each maps
        mapKeyToMovie  = new HashMap<>();
        mapKeyToActor = new HashMap<>();
        mapMovieToActor  = new HashMap<>();

        /**
         * try buffered reading for each file path
         * */

        BufferedReader fileMapKeyToMovie = null;
        BufferedReader fileMapKeyToActor = null;
        BufferedReader fileMovieToActor = null;

        try {
            fileMapKeyToMovie = new BufferedReader(new FileReader(pathKeyToMovie));
        } catch(Exception e){
            System.out.println("file not found for ID -> Movies");
        }


        try {
            fileMapKeyToActor = new BufferedReader(new FileReader(pathKeyToActor));
        } catch(Exception e){
            System.out.println("file not found for ID -> Actor");
        }

        try {
            fileMovieToActor = new BufferedReader(new FileReader(pathMovieToActor));
        } catch(Exception e){
            System.out.println("file not found for Movie -> Actor");
        }

        /**
         * build ID to Movies map
         * */
        String[] moviesAndID;
        String line = "";
        try {
            if(fileMapKeyToMovie != null) {
                while ((line = fileMapKeyToMovie.readLine()) != null) {

                    //split line by pip | and gets ID and Movie value at index 0 and index 1
                    moviesAndID = line.split("\\|");

                    //index 0 is the id and index 1 is the movie
                    mapKeyToMovie.put(Integer.parseInt(moviesAndID[0]), moviesAndID[1]);


                }
            }
        } catch(Exception e){
            System.out.println("problem with line");
        }finally {
            fileMapKeyToMovie.close();
        }


        /**
         * build ID to Actors
         * */
        String[] actorsAndID;
        String line2 = "";
        try {
            if(fileMapKeyToActor != null) {
                while ((line2 = fileMapKeyToActor.readLine()) != null) {

                    //split line by pip | and gets ID and Actor value at index 0 and index 1
                    actorsAndID = line2.split("\\|");

                    //index 0 is the id and index 1 is the movie
                    mapKeyToActor.put(Integer.parseInt(actorsAndID[0]), actorsAndID[1]);

                }
            }
        } catch(Exception e){
            System.out.println("problem with line");
        }finally{
            fileMapKeyToActor.close();
        }


        /**
         * build Movies to Actors
         * */
        String[] movieAndActors;
        String line3 = "";
        try {
            if(fileMovieToActor != null) {
                while ((line3 = fileMovieToActor.readLine()) != null) {

                    //split line by pip | and gets ID and Actor value at index 0 and index 1
                    movieAndActors = line3.split("\\|");

                    //IDs of Actors
                    int movieID = Integer.parseInt(movieAndActors[0]);
                    int actorID = Integer.parseInt(movieAndActors[1]);

                    //index 0 is the id and index 1 is the movie
                    if(!mapMovieToActor.containsKey(Integer.parseInt(movieAndActors[0]))){

                        Set<Integer> newSet = new HashSet<>();
                        newSet.add(actorID);
                        mapMovieToActor.put(movieID,newSet);
//                        System.out.println(movieID + "->" + mapMovieToActor.get(movieID));

                    }else {
                        mapMovieToActor.get(movieID).add(actorID);
//                        System.out.println(movieID + "->" + mapMovieToActor.get(movieID));
                    }

                }
            }
        } catch(Exception e){
            System.out.println("problem with line");
        }finally {
            fileMovieToActor.close();
        }
    }

    public static void drawGraphs(){

        //initialize new graph
        actorsToActors = new AdjacencyMapGraph<>();

        /**
         * add all actor names to both in and out neighbors
         * with edges as map(key neighbor, value - set of movies
         */

        Set<Integer> actorIDs = mapKeyToActor.keySet();

        //insert all actor names to both in and out neighbors
        for(Integer id: actorIDs){
            //Map<String, Set<String>> neighbors = new HashMap<>();
            actorsToActors.insertVertex(mapKeyToActor.get(id));
        }


        //print out movie to all actors in associated to it
//        for(Integer movie: mapMovieToActor.keySet()){
//            Set<Integer> IDS = mapMovieToActor.get(movie);
//            String stringBuilder = "";
//            stringBuilder += mapKeyToMovie.get(movie) + "->" + "[";
//            for(Integer id: IDS){
//                stringBuilder += id.toString() + ",";
//            }
//            stringBuilder += "]";
//            System.out.println(stringBuilder);
//        }

        //iterate every movie in maps
        for(Integer movieID: mapMovieToActor.keySet()){

            //get the name of movie with movie ID
            String movie = mapKeyToMovie.get(movieID);

            //get all actor Ids for each movie
            Set<Integer> neighborIDS = mapMovieToActor.get(movieID);

            //iterate through actors
            for(Integer neighbourID: neighborIDS){

                //get name of actor
                String actor = mapKeyToActor.get(neighbourID);

                //iterate through actors except current actor and compare labels
                for(Integer partnerID: neighborIDS){

                    String other = mapKeyToActor.get(partnerID);

                    if(!actor.equals(other)){

                        //AdjacencyMapGraph<String,Set<String>>
                        if(actorsToActors.hasEdge(actor,other)){
                            actorsToActors.getLabel(actor,other).add(movie);
                        } else {
                            actorsToActors.insertUndirected(actor,other,new HashSet<>());

                            actorsToActors.getLabel(actor,other).add(movie);
                        }

                    }
                }

            }

        }

    }

    public static void processFile()throws IOException{
//        MovieGraph.createMaps("bacon/movies.txt", "bacon/actors.txt", "bacon/movie-actors.txt");
        System.out.println("file processed");
        System.out.println(actorsToActors);
    }


    public static void main(String[] args) throws IOException {  // Main method, the entry point of the program. Throws IOException if there are input/output errors

        /**
         * test cases for file reader in test files
         * */
        MovieGraph.createMaps("bacon/moviesTest.txt", "bacon/actorsTest.txt", "bacon/movie-actorsTest.txt");  // Calls the createMaps method in the MovieGraph class to load data from files into maps (movies, actors, movie-actors relationships)

        Set<Integer> keys = mapKeyToMovie.keySet();  // Retrieves the set of keys from the map 'mapKeyToMovie' that stores movie IDs

        System.out.println(keys);  // Prints the keys of the movie map. Test case to check if the movie map keys (movie IDs) are loaded correctly

        System.out.println(mapKeyToMovie.get(50));  // Test case: Retrieves and prints the movie associated with key 50 from the movie map. Validates if key-based retrieval works as expected

        Set<Integer> movieToActor = mapMovieToActor.keySet();  // Retrieves the set of movie keys from the map 'mapMovieToActor' which stores movie-to-actor relationships

        MovieGraph.drawGraphs();  // Test case: Calls the drawGraphs method to visualize the movie-actor relationships. This may depend on a graphical interface or output

        System.out.println(actorsToActors);  // Test case: Prints the 'actorsToActors' map to verify if actor relationships are populated correctly (e.g., if the actors' connections or collaborations are properly created)
    }
}
