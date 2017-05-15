import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;

/**
 * Created by duncan on 5/5/17.
 */
public class Visualizer {
    // This class is used to generate the visualization

    public static void visualizeNetwork(Graph<Node, Link> network) {
        // The Layout<V, E> is parameterized by the vertex and edge types
//        Layout<Integer, String> layout = new CircleLayout(network);
        Layout<Integer, String> layout = new FRLayout(network);
        layout.setSize(new Dimension(300,300)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
        JFrame frame = new JFrame("Network Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public static void visualize(AODVEvent[] fullEvents) {
        // TODO-stretch: incorrect data type in argument
        // TODO-stretch: implement
        throw new NotImplementedException();
    }
}
