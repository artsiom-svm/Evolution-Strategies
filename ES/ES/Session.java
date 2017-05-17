import NN.*;
import WumpusWorld.GameData;
import WumpusWorld.WorldApplication;

import java.util.Random;

/**
 * Created by ArxAr on r2/17/2017.
 */
public class Session {

    private WorldApplication wp;
    private Network nn;
    final public int numPercepts;
    final public int numActions;
    private Random rand;
    //private double t;


    private int count = 0;
    private boolean check = false;

    public Session(long seed) {
        wp = new WorldApplication();
        numPercepts = 5;
        numActions = 6;
        rand = new Random(seed);
        nn = createNetwork(6);
        //t = -50;
    }


    public void run(long count) {
        for (long i = 0; i < count; i++) {
            playGame();

        }
    }

    private void playGame() {


        count++;
        if (count > 10000)
            check = true;

        nn.data.clear();

        wp.runWorld(nn);


        GameData data = nn.data;

        if (data.scores.size() != data.actions.size() || data.scores.size() != data.percepts.size() || data.actions.size() != data.percepts.size())
            throw new ArrayIndexOutOfBoundsException("" + data.scores.size() + "  " + data.actions.size() + data.percepts.size());

        //calculate total score
        double totalScore = 0;
        for (Double score : data.scores) {
            if (score == 1000)
                score = score;
            totalScore += score;
        }

        for (int i = data.scores.size() - 1; i >= 0; i--) {

            int index = (int) (double) data.actions.get(i);

            //learning rate
            //double rate = Math.pow(nn.getDecay_rate(), data.scores.size() - index - 1);
            double rate = Math.pow(nn.getDecay_rate(), data.scores.size() - i - 1);
            //linear learning

            rate *= (totalScore + 50) / 1000;

            //create learning output set
            double[] set = new double[6];
            for (int j = 0; j < set.length; j++) {
                set[j] = index == j ? rate : 0.0;
            }

            nn._backprop(new Matrix(data.percepts.get(i)), new Matrix(set));
        }


    }

    double getScore(int count) {
        double score = wp.sampleGames(nn, count);
        //if(t < score)
        //    t = score;
        return score;
    }

    public void setNetwork(Network nn) {
        this.nn = nn;
    }

    public Network getNetwork() {
        return nn;
    }

    private Network createNetwork(int neurals) {

        Network network = new Network(numPercepts, 1);
        network.setOutput(numActions, 1);
        network.setActivationFunction(new NoFunction());

        network.add_layer(neurals, rand.nextLong());

        network.setNo_layers();

        return network;
    }



}
