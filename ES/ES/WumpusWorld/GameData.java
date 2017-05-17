package WumpusWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ArxAr on 2/21/2017.
 */
public class GameData {
    public List<Double> actions;
    public List<double[]> percepts;
    public List<Double> scores;

    public GameData() {
        actions = new ArrayList<>();
        percepts = new ArrayList<>();
        scores = new ArrayList<>();
    }

    public void clear()
    {
        actions.clear();
        percepts.clear();
        scores.clear();
    }


}
