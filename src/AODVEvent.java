import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by duncan on 5/5/17.
 */
public class AODVEvent implements Comparable {
    // TODO: implement, but maybe this should be abstract and be extended by all the different kinds of events?
    public AODVEvent() {
        throw new NotImplementedException();
    }

    @Override
    public int compareTo(Object o) {
        // TODO: needs to be implemented for priority queue to work
        return 0;
    }
}
