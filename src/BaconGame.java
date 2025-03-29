import java.io.IOException;
import java.util.*;


/**
 * implements game interface with required methods
 * for finding shortest path from goal to center,
 * find average separations, changing center universe
 * and many more
 *
 * @author Richmond Nartey Tettey, Dartmouth CS10, Winter 2025
 *
 * */

public class BaconGame implements Game{
    private String centerUniverse;
    private String goal;
    private Graph<String,Set<String>> graph;
    private List<String> path;
    private Graph<String,Set<String>> tree;
    private Double averageSeparation;
    private boolean endgame = true;


    public BaconGame(String centerUniverse) throws Exception {
        this.createGraphs();
        this.centerUniverse = centerUniverse;
        this.newBfs();
    }

    /**
     * getter and setter for center universe and goal
     * */
    public String getCenterUniverse() {
        return centerUniverse;
    }

    public void setCenterUniverse(String centerUniverse) {
        this.centerUniverse = centerUniverse;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<String> getPath() {
        return path;
    }

    public Double getAverageSeparation() {
        return averageSeparation;
    }

    public void setAverageSeparation(Double averageSeparation) {
        this.averageSeparation = averageSeparation;
    }

    private void createGraphs() throws IOException {
        //bacon game graphs
        MovieGraph.createMaps("bacon/movies.txt", "bacon/actors.txt", "bacon/movie-actors.txt");
        MovieGraph.drawGraphs();
        graph = MovieGraph.actorsToActors;
    }

    public boolean isEndgame() {
        return endgame;
    }


    public void setEndgame(boolean endgame) {
        this.endgame = endgame;
    }

    /***
     * create a new bfs when center universe is changed
     */

    private void newBfs() throws Exception{
        tree = GraphLib.bfs(graph,centerUniverse);
    }

    /**
     * @param goal is the starting point(actor) to the center universe
     * */
    protected void getPath(String goal) throws Exception {

        tree = GraphLib.bfs(graph,centerUniverse);
        path = GraphLib.getPath(tree,goal);
    }

    /**
     * iterate shortest path list and display movie collaborations
     *
     * */
    protected void startMovieTrace() throws Exception{

        if(path == null){ throw new Exception("path not available");}

        System.out.println(path.get(0) + "'s number is " + (path.size()-1));

        for(int i = 0; i < path.size()- 1;i++){

            String pathBuilder = path.get(i) + " " + "appeared" + " " + "in"
                                + " " +graph.getLabel(path.get(i), path.get(i+1))
                                + " " + "with" + " " + path.get(i+1);

            System.out.println(pathBuilder);
        }

    }

    /**
     * change the center of the acting universe to a valid actor
     * key u
     * */
    @Override
    public void changeCenterUniverse(String actor) throws Exception{


        if(!actor.equals(centerUniverse)){

            //change center universe to new actor
            setCenterUniverse(actor);

            //new bread-first search for universe
            newBfs();

            //compute new average separation
            try {
                setAverageSeparation(GraphLib.averageSeparation(tree, centerUniverse));
                path = null;

                //display confirmation message
                String universeBuilder = centerUniverse + " is now the center of the acting universe, connected to "
                        + "[#]/" + (tree.outDegree(centerUniverse)) + " actors " + "with average separation "
                        + "[#]/ " + getAverageSeparation();

                System.out.println(universeBuilder);

            } catch(Exception e){
                System.out.println("Failed to Calculate Average Separation! -> " + e);
            }


        } else {
            System.out.println(actor + " is already the center");
        }

    }




    /**
     * find the shortest path to an actor from the current center
     * of the universe
     * key p
     * */
    @Override
    public void findShortestPath() throws Exception {
        startMovieTrace();
    }

    /**
     * find the number of actors who have a path
     * (connected by some number of steps) to the current center
     * */
    @Override
    public Integer findNumOfActors() {
        return tree.inDegree(centerUniverse);
    }

    /**
     * list top (positive number) or bottom (negative) <#>
     * centers of the universe, sorted by average separation
     * key c
     * */
    @Override
    public void sortByAverageSeparation(int order, int max) throws Exception {
        // Step 1: Precompute average separations and store them
        Map<String, Double> avgSeparations = new HashMap<>();

        for (String center : graph.vertices()) {
            try {
                double separation = GraphLib.averageSeparation(GraphLib.bfs(graph, center), center);
                avgSeparations.put(center, separation);
            } catch (Exception e) {
                System.out.println("Error computing BFS for " + center + ": " + e.getMessage());
            }
        }

        // Step 2: Use a priority queue to sort by precomputed values
        PriorityQueue<Map.Entry<String, Double>> queue = new PriorityQueue<>(
                (a, b) -> order == -1 ? Double.compare(b.getValue(), a.getValue()) : Double.compare(a.getValue(), b.getValue())
        );

        queue.addAll(avgSeparations.entrySet()); // Add all (vertex, separation) pairs

        // Step 3: Print sorted centers
        System.out.println("Centers sorted by average separation:");
        int count = 0;
        while (!queue.isEmpty() && count < max) {
            Map.Entry<String, Double> entry = queue.poll();
            double val =  (double) entry.getValue();
            System.out.println(entry.getKey() + " -> " + (int) val);
            count++;
        }
    }


    /**
     * list actors sorted by degree, with degree between low and high
     * key d
     * */
    @Override
    public void sortByDegree(int low, int high) {

        //new maps of vertices and their out degree
        Map<String, Integer> vertexDegree = new HashMap<>();


        PriorityQueue<String> sortDegree = new PriorityQueue<> (
                (a,b) -> {
                    int valueA = vertexDegree.get(a);
                    int valueB = vertexDegree.get(b);

                    return valueA - valueB;
                }
        );

        //iterate and put center and out degree to maps
        for(String center: graph.vertices()){
            int boundary = graph.inDegree(center);

            if (boundary <= high && boundary >= low){
                vertexDegree.put(center, graph.inDegree(center));
                sortDegree.offer(center);
            }

        }


        //display vertex and its degree in ascending order
        while(!sortDegree.isEmpty()){
            String center = sortDegree.poll();
            System.out.println(center + "->" +vertexDegree.get(center));
        }

    }

    /**
     * find the average path length over all actors who are connected
     * by some path to the current center
     * */
    @Override
    public void findAveragePath() {
        GraphLib.averageSeparation(tree,centerUniverse);
    }

    /**
     * list actors with infinite separation from the current center
     * key i
     * */
    @Override
    public void infiniteSeparation() {

        Set<String> infinite = GraphLib.missingVertices(graph,tree);

        String stringBuilder = "Actors wih Infinite Separation: \n";
        for(String actor: infinite){
            stringBuilder += " " + "[" + actor + "]" + "\n";
        }

        System.out.println(stringBuilder);
    }

    /**
     * list actors sorted by non-infinite separation from the current center
     * with separation between low and high
     * key s
     * */
    @Override
    public void separationsFromUniverse(int low, int high) throws Exception {

        // Validate input: Ensure that 'low' is non-negative and 'high' is greater than or equal to 'low'.
        // If the condition is violated, throw an exception.
        if(low < 0 || high < low) throw new Exception("low and upper bound invalid!");

        // Map to store each actor and their corresponding separation distance from the universe center.
        Map<String,Integer> actorToDistance = new HashMap<>();

        // Priority queue to store actors sorted by their separation distance in ascending order.
        PriorityQueue<String> orderActorsBySeparation = new PriorityQueue<>(
                (a, b) -> {
                    // Retrieve separation values from the map.
                    int valueA = actorToDistance.get(a);
                    int valueB = actorToDistance.get(b);

                    // Compare separation distances (lower separation comes first).
                    return valueA - valueB;
                }
        );

        // Iterate over all vertices (actors) in the graph.
        for(String actor: graph.vertices()){

            // Check if the actor exists in the BFS tree and is not the universe center itself.
            if(tree.hasVertex(actor) && !actor.equals(centerUniverse)){

                // Retrieve the shortest path from the universe center to the actor.
                List<String> shortestPath = GraphLib.getPath(tree, actor);

                // Calculate the separation distance as the path length minus one.
                int separation = shortestPath.size() - 1;

                // If the separation is within the specified bounds, store it in the map and add the actor to the priority queue.
                if (separation <= high && separation >= low){
                    actorToDistance.put(actor, separation);
                    orderActorsBySeparation.offer(actor);
                }
            }
        }

        // Display each actor and their separation distance in ascending order.
        while(!orderActorsBySeparation.isEmpty()){
            // Retrieve and remove the actor with the lowest separation distance from the priority queue.
            String center = orderActorsBySeparation.poll();
            // Print the actor and their corresponding separation value.
            System.out.println(center + "->" + actorToDistance.get(center));
        }
    }


    @Override
    public void quitGame() {
        if(!endgame){
            endgame = true;
        } else {
            System.out.println("Game not running!");
        }
    }

    public static void main(String[] args) throws Exception{
        BaconGame  game = new BaconGame("Buster Keaton");


        /**
         * test case 1
         * */
        System.out.println("Testing setUniverseCenter:");
        game.changeCenterUniverse("John Longden");
        System.out.println("Expected: Universe center set to John Longden");

        /**
         * test case 2
         * */
        System.out.println("\nTesting findShortestPath:");
        game.getPath("Tom Hanks");
        game.findShortestPath();
        System.out.println("Expected: Shortest path from Tom Hanks to John Longden displayed");

        /**
         * test case 3
         * */
        System.out.println("\nTesting sortByDegree:");
        game.sortByDegree(1, 5);
        System.out.println("Expected: Actors sorted by degree between 1 and 5 displayed");

        /**
         * test case 4
         * */
        System.out.println("\nTesting sortByAverageSeparation:");
        game.sortByAverageSeparation(1, 10);
        System.out.println("Expected: Top 10 actors sorted by average separation displayed");

        /**
         * test case 5
         * */
        System.out.println("\nTesting infiniteSeparation:");
        game.infiniteSeparation();
        System.out.println("Expected: List of actors with infinite separation displayed");

        /**
         * test case 6
         * */
        System.out.println("\nTesting separationsFromUniverse:");
        try {
            game.separationsFromUniverse(2, 6);
            System.out.println("Expected: List of actors with separation between 2 and 6 displayed");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
}
