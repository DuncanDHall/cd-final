/**
 * Created by duncan on 5/6/17.
 *
 * Stores data associated with links between nodes
 * endpoints are stored by the graph
 */
public class Link {
    int weight;
    int id;

    public Link(int weight, int id) {
        this.weight = weight;
        this.id = id;
    }
}
