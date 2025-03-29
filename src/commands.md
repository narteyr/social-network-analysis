Graph lib interface
public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source);
public static <V,E> List<V> getPath(Graph<V,E> tree, V v);
public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph);
public static <V,E> double averageSeparation(Graph<V,E> tree, V root);




Commands:
c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation

d <low> <high>: list actors sorted by degree, with degree between low and high

i: list actors with infinite separation from the current center

p <name>: find path from <name> to current center of the universe

s <low> <high>: list actors sorted by non-infinite separation from the current center,
with separation between low and high

u <name>: make <name> the center of the universe

q: quit game