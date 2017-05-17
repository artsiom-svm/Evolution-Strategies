package WumpusWorld;/*
 * Class that defines the simulation environment.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
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

public class Simulation {

    private int currScore = 0;
    private static int actionCost = -1;
    private static int deathCost = -1000;
    private static int shootCost = -10;
    private int stepCounter = 0;
    private int lastAction = 0;


    private boolean simulationRunning;

    private Agent agent;
    private Environment environment;
    private TransferPercept transferPercept;

    public Simulation(Environment wumpusEnvironment, int maxSteps, boolean nonDeterministic) {

        // start the simulator
        simulationRunning = true;

        transferPercept = new TransferPercept(wumpusEnvironment);
        environment = wumpusEnvironment;

        agent = new Agent(environment, transferPercept, nonDeterministic);

        environment.placeAgent(agent);


        try {


            while (simulationRunning == true && stepCounter < maxSteps) {


                handleAction(agent.chooseAction());
                wumpusEnvironment.placeAgent(agent);


                //Scanner in = new Scanner(System.in);
                //in.next();

                stepCounter += 1;

                if (stepCounter == maxSteps || !simulationRunning) {

                    lastAction = Action.END_TRIAL;
                }

            }

        } catch (Exception e) {
            System.out.println("An exception was thrown: " + e);
        }

    }

    public void handleAction(int action) {

        try {

            //WorldApplication.nn.data.actions.add((double) (action - 1));
            WorldApplication.nn.data.actions.add((double) (action - 1));

            int tcur = currScore;

            if (action == Action.GO_FORWARD) {

                if (environment.getBump()) environment.setBump(false);

                agent.goForward();
                environment.placeAgent(agent);

                if (environment.checkDeath()) {

                    currScore += deathCost;
                    simulationRunning = false;

                    agent.setIsDead(true);
                } else {
                    currScore += actionCost;
                }

                if (environment.getScream()) environment.setScream(false);

                lastAction = Action.GO_FORWARD;
            } else if (action == Action.TURN_RIGHT) {

                currScore += actionCost;

                agent.turnRight();
                environment.placeAgent(agent);

                if (environment.getBump()) environment.setBump(false);
                if (environment.getScream()) environment.setScream(false);

                lastAction = Action.TURN_RIGHT;
            } else if (action == Action.TURN_LEFT) {

                currScore += actionCost;

                agent.turnLeft();
                environment.placeAgent(agent);

                if (environment.getBump()) environment.setBump(false);
                if (environment.getScream()) environment.setScream(false);

                lastAction = Action.TURN_LEFT;
            } else if (action == Action.GRAB) {

                if (environment.grabGold()) {

                    currScore += 1000;

                    simulationRunning = false;

                    agent.setHasGold(true);
                } else {
                    currScore += actionCost;
                }

                environment.placeAgent(agent);

                if (environment.getBump()) environment.setBump(false);
                if (environment.getScream()) environment.setScream(false);

                lastAction = Action.GRAB;
            } else if (action == Action.SHOOT) {

                if (agent.shootArrow()) {

                    if (environment.shootArrow()) environment.setScream(true);

                    currScore += shootCost;

                } else {

                    if (environment.getScream()) environment.setScream(false);

                    currScore += actionCost;

                }

                environment.placeAgent(agent);

                if (environment.getBump()) environment.setBump(false);

                lastAction = Action.SHOOT;
            } else if (action == Action.NO_OP) {

                environment.placeAgent(agent);

                if (environment.getBump()) environment.setBump(false);
                if (environment.getScream()) environment.setScream(false);


                lastAction = Action.NO_OP;
            }


            double dscore = (double) (currScore - tcur);
            if (lastAction == Action.NO_OP) {
                WorldApplication.nn.data.scores.add(0.0);
            } else {
                WorldApplication.nn.data.scores.add(dscore);
            }


        } catch (Exception e) {
            System.out.println("An exception was thrown: " + e);
        }
    }

    public int getScore() {

        return currScore;

    }


}