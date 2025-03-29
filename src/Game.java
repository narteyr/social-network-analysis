
/**
 * @author Richmond Nartey Tettey, CS10 Winter
 * */

public interface Game {


    /**
     * change the center of the acting universe to a valid actor
     * key u
     * */
    public void changeCenterUniverse(String actor) throws Exception;

    /**
     * find the shortest path to an actor from the current center
     * of the universe
     * key p
     * */
    public void findShortestPath() throws Exception;

    /**
     * find the number of actors who have a path
     * (connected by some number of steps) to the current center
     * */
    public Integer findNumOfActors();


    /**
     * list top (positive number) or bottom (negative) <#>
     * centers of the universe, sorted by average separation
     * key c
     * */
    public void sortByAverageSeparation(int order, int max) throws Exception;

    /**
     * list actors sorted by degree, with degree between low and high
     * key d
     * */
    public void sortByDegree(int low, int high);

    /**
     * find the average path length over all actors who are connected
     * by some path to the current center
     * */
    public void findAveragePath();

    /**
     * list actors with infinite separation from the current center
     * key i
     * */
    public void infiniteSeparation();


    /**
     * list actors sorted by non-infinite separation from the current center
     * with separation between low and high
     * key s
     * */
    public void separationsFromUniverse(int low,int high) throws Exception;

    /**
     *quit game
     * key q
     * */
    public void quitGame();
}
