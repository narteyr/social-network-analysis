import java.util.*;


/**
 * The purpose of this class is to handle breadth first search
 * to find the shortest path from actor to center universe,
 * find missing vertices, out degrees and average separations
 * for each actor
 * */

public class GraphLib{

    public static Double totalDistance;


    /**
     * @param g is the graph of all vertices
     * @param source is the root to create the shortest path for all vertices
     * @return a tree graph for all shortest path of vertices from root to each vertex
     * */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source)throws Exception{

        if(!g.hasVertex(source)){throw new Exception("source not found in graph");}

        //create tree for shortest path
        Graph<V,E> tree = new AdjacencyMapGraph<>();

        //create queues for BFS
        List<V> queue = new LinkedList<>();
        queue.add(source);

        //iterate through until all neighbors are visited
        while(!queue.isEmpty()){

            //deque next vertex from queue
            V vertex = queue.remove(0);
            tree.insertVertex(vertex);


            //for each vertex, get it neighbors and check if not visited
            //if not visited, insert vertex to tree and insert
            //directed edge from vertex to neighbor
            for(V neighbor: g.outNeighbors(vertex)){


                if(!tree.hasVertex(neighbor)){

                    queue.add(neighbor);

                    tree.insertVertex(neighbor);


                    tree.insertDirected(vertex,neighbor,g.getLabel(vertex,neighbor));
                }
            }
        }

        return tree;
    };

    /**
     * @param tree has the shortest path for all vertices
     * @param v is the vertex goal from the root
     * @return shortest path list from root to vertex v
     * */
    public static <V,E> List<V> getPath(Graph<V,E>tree, V v) throws Exception{

        //throw an exception if goal is not found in vertex
        if(!tree.hasVertex(v)){throw new Exception("vertex not found in tree");}

        //create new list for shortest path
        List<V> path = new ArrayList<>();

        //add new string
        path.add(v);

        V vertex = v;

        while(tree.inDegree(vertex) > 0){

            V parent = tree.inNeighbors(vertex).iterator().next();
            path.add(parent);
            vertex = parent;
        }

        return path;
    }


    /**
     * @param subgraph is subgraph of graph that may not contain all vertices in graph
     * @param graph is the graph to compare subgraph with to find missing vertices
     * @return returns set of missing vertices
     * */
    public static<V,E> Set<V> missingVertices(Graph<V,E>graph, Graph<V,E> subgraph){

        //create new set for missing vertices
        Set<V> missingVertices = new HashSet<>();

        //add
        for(V vertex:graph.vertices()){

            if(!subgraph.hasVertex(vertex)) missingVertices.add(vertex);
        }

        return missingVertices;
    }

    public static <V, E> double averageSeparation(Graph<V, E> tree, V root) {


        if (!tree.hasVertex(root)) {  // Check if the root vertex exists in the tree
            System.out.println("Vertex " + root + " not found in BFS tree");  // Print an error message if the root is not found
            return 0;  // Return 0 as the average separation cannot be calculated
        }

        try {
            double total = averageSeparationHelper(tree, root, 0);  // Call the helper function to compute total separation
            return (total / tree.numVertices());  // Calculate and return the average separation by dividing total separation by the number of vertices
        } catch (Exception e) {  // Catch any exceptions thrown during the calculation
            System.out.println(e);  // Print the exception message
            return 0;  // Return 0 in case of an error
        }
    }

    private static <V, E> double averageSeparationHelper(Graph<V, E> tree, V root, double distance) throws Exception {
        double total = distance;  // Initialize total separation with the current distance

        if (tree == null) {  // Check if the tree is null to avoid NullPointerException
            throw new Exception("Tree for center Universe is Empty");  // Throw an exception if the tree is empty
        }

        V vertex = root;  // Assign the root vertex

        if (tree.outDegree(vertex) > 0) {  // Check if the vertex has outgoing edges (children in the tree)
            for (V v : tree.outNeighbors(vertex)) {  // Iterate through all direct neighbors (children)
                total += averageSeparationHelper(tree, v, distance + 1);  // Recursively call the function for each child, increasing the distance
            }
        }

        return total;  // Return the total sum of distances
    }


    public static void main(String[] args)throws Exception{

        /**
         * graph test
         * */

        Graph<String,String> g = new AdjacencyMapGraph<>();

        g.insertVertex("Alex");
        g.insertVertex("Tina");
        g.insertVertex("Clay");
        g.insertVertex("Richmond");
        g.insertVertex("Adams");
        g.insertVertex("Bob");

        g.insertUndirected("Alex","Bob","friends");
        g.insertUndirected("Alex","Tina","friends");
        g.insertDirected("Alex","Clay","follower");
        g.insertUndirected("Bob","Richmond","friends");
        g.insertDirected("Richmond","Clay","follower");
        g.insertDirected("Richmond","Adams","follower");
        g.insertDirected("Clay","Adams","follower");

        Graph<String,String> tree = GraphLib.bfs(g,"Alex");

        System.out.println(tree.vertices());
        List<String> path = GraphLib.getPath(tree, "Clay");

        System.out.println("path:" + path);

        System.out.println(GraphLib.missingVertices(g,tree));

        System.out.println(GraphLib.averageSeparation(tree, "Alex"));
    }

}
