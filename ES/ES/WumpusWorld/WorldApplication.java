package WumpusWorld;/*
 * Wumpus-Lite, version 0.18 alpha
 * A lightweight Java-based Wumpus World Simulator
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Thanks to everyone who provided feedback and
 * suggestions for improving this application,
 * especially the students from Professor
 * Gmytrasiewicz's Spring 2007 CS511 class.
 * 
 * Last modified 3/5/07
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */


import NN.Network;

import java.util.Random;

public class WorldApplication {

    private static String VERSION = "v0.18a";
    public static Network nn;


    public void runWorld(Network nn) {

        WorldApplication.nn = nn;

        int worldSize = 4;
        int maxSteps = 50;

        boolean nonDeterministicMode = true;
        boolean randomAgentLoc = true;

        Random rand = new Random();
        int seed = rand.nextInt();

        char[][][] wumpusWorld = generateRandomWumpusWorld(seed, worldSize, randomAgentLoc);
        Environment wumpusEnvironment = new Environment(worldSize, wumpusWorld);


        Simulation trial = new Simulation(wumpusEnvironment, maxSteps, nonDeterministicMode);


        System.runFinalization();

    }

    public double sampleGames(Network nn, int count) {
        WorldApplication.nn = nn;

        int worldSize = 4;
        int maxSteps = 50;

        boolean nonDeterministicMode = true;
        boolean randomAgentLoc = true;

        Random rand = new Random();
        int seed = rand.nextInt();

        char[][][] wumpusWorld = generateRandomWumpusWorld(seed, worldSize, randomAgentLoc);
        Environment wumpusEnvironment = new Environment(worldSize, wumpusWorld);

        double score = 0;

        for (int i = 0; i < count; i++) {
            Simulation trial = new Simulation(wumpusEnvironment, maxSteps, nonDeterministicMode);

            score+= trial.getScore();
            System.runFinalization();

        }

        return score/count;
    }


    public static char[][][] generateRandomWumpusWorld(int seed, int size, boolean randomlyPlaceAgent) {

        char[][][] newWorld = new char[size][size][4];
        boolean[][] occupied = new boolean[size][size];

        int x, y;

        Random randGen = new Random(seed);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < 4; k++) {
                    newWorld[i][j][k] = ' ';
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                occupied[i][j] = false;
            }
        }

        int pits = 2;

        // default agent location
        // and orientation
        int agentXLoc = 0;
        int agentYLoc = 0;
        char agentIcon = '>';

        // randomly generate agent
        // location and orientation
        if (randomlyPlaceAgent == true) {

            agentXLoc = randGen.nextInt(size);
            agentYLoc = randGen.nextInt(size);

            switch (randGen.nextInt(4)) {

                case 0:
                    agentIcon = 'A';
                    break;
                case 1:
                    agentIcon = '>';
                    break;
                case 2:
                    agentIcon = 'V';
                    break;
                case 3:
                    agentIcon = '<';
                    break;
            }
        }

        // place agent in the world
        newWorld[agentXLoc][agentYLoc][3] = agentIcon;

        // Pit generation
        for (int i = 0; i < pits; i++) {

            x = randGen.nextInt(size);
            y = randGen.nextInt(size);

            while ((x == agentXLoc && y == agentYLoc) | occupied[x][y] == true) {
                x = randGen.nextInt(size);
                y = randGen.nextInt(size);
            }

            occupied[x][y] = true;

            newWorld[x][y][0] = 'P';

        }

        // Wumpus Generation
        x = randGen.nextInt(size);
        y = randGen.nextInt(size);

        while (x == agentXLoc && y == agentYLoc) {
            x = randGen.nextInt(size);
            y = randGen.nextInt(size);
        }

        occupied[x][y] = true;

        newWorld[x][y][1] = 'W';

        // Gold Generation
        x = randGen.nextInt(size);
        y = randGen.nextInt(size);

        //while (x == 0 && y == 0) {
        //	x = randGen.nextInt(size);
        //	y = randGen.nextInt(size);
        //}

        occupied[x][y] = true;

        newWorld[x][y][2] = 'G';

        return newWorld;
    }

}