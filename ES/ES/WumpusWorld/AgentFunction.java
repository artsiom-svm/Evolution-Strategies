
package WumpusWorld;/*

 * Class that defines the agent function.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Last modified 2/19/07 
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */

import NN.Matrix;

import java.util.Random;

public class AgentFunction {

    // string to store the agent's name
    // do not remove this variable
    private String agentName = "WumpusWorld.Agent Smith";

    // all of these variables are created and used
    // for illustration purposes; you may delete them
    // when implementing your own intelligent agent
    private boolean bump;
    private boolean glitter;
    private boolean breeze;
    private boolean stench;
    private boolean scream;
    private Random rand;

    public AgentFunction() {
        // for illustration purposes; you may delete all code
        // inside this constructor when implementing your
        // own intelligent agent

        // this integer array will store the agent actions

        // new random number generator, for
        // randomly picking actions to execute
        rand = new Random();
    }

    public int process(TransferPercept tp) {
        double[] set = new double[5];


        set[0] = tp.getBreeze() ? 1 : 0;
        set[1] = tp.getBump() ? 1 : 0;
        set[2] = tp.getGlitter() ? 1 : 0;
        set[3] = tp.getScream() ? 1 : 0;
        set[4] = tp.getStench() ? 1 : 0;

        WorldApplication.nn.data.percepts.add(set);
        if(set[0] == set[1] && set[0] == set[2] && set[2] == set[3] && set[4] == set[3] && set[1] == 0){
            set[0] = 1;
        }
        Matrix data = WorldApplication.nn.feed(new Matrix(set));

        /*
        if(!Functions.allPositive(data)){
            final double min = data.getMin();
            data = data.forEach(new Operator() {
                @Override
                public double operate(double a) {
                    return a - 2*min;
                }
            });
        }*/

        double[] actionsDist = (data).toArray();

        if (Double.isNaN(actionsDist[0]))
            System.out.println("Something wrong");

        double choice = rand.nextDouble();

        int i = 0;
        for (; i < actionsDist.length; i++) {

            choice -= actionsDist[i];
            if (choice <= 0) {
                break;
            }
        }


        return i + 1;
    }

    // public method to return the agent's name
    // do not remove this method
    public String getAgentName() {
        return agentName;
    }
}

