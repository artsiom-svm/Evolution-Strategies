/**
 * Created by ArxAr on 2/16/2017.
 */

import NN.Layer;
import NN.Matrix;
import NN.Network;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;

public class Main {

    public static boolean debug = false;

    public static void main(String[] args) {

        //test model
        //WumpusLearning(rand.nextLong());

    }

    private static Network createNetwork(double[] data, Session session) {
        Network nn = session.getNetwork();

        Matrix left = nn.layers.get(0).left;
        Matrix right = nn.layers.get(0).right;
        nn.layers.remove(0);

        int index = 0;
        //left is numneu * input = 6 * 6
        //right is output * nummeu = 5 * 6
        for (int i = 0; i < left.getHeight(); i++)
            for (int j = 0; j < left.getWidth(); j++)
                left.set(data[index++], i, j);

        for (int i = 0; i < right.getHeight(); i++)
            for (int j = 0; j < right.getWidth(); j++)
                right.set(data[index++], i, j);

        nn.layers.add(new Layer(left, right, null));

        return nn;
    }


    public static double esScore(double[] data) {
        Session session = new Session(0);

        Network nn = createNetwork(data,session);

        session.setNetwork(nn);

        return -1000 + session.getScore(1000);
    }

    public static void outputData(double[] data, int index) {
        Session session = new Session(0);

        Network nn = createNetwork(data,session);

        session.setNetwork(nn);

        outputData("data.txt",session,index);
    }

    private static void WumpusLearning(long seed) {
        int numLearn = 10000;
        String output = "results.txt";

        Session sess = new Session(seed);

        System.out.println("Average score before learning: " + sess.getScore(10000) + '\n');

        int i = 1;
        while (true) {
            sess.run(numLearn);

            debug = true;
            System.out.println("Average score after trial # " + i + " and " + numLearn + " learning : " + sess.getScore(10000) + '\n');
            outputData(output, sess, i);
            i++;
        }

    }

    private static double[][] createSets() {
        double[][] data = new double[32][5];

        for (int i = 0; i < 32; i++) {
            for (int k = 0; k < 5; k++)
                data[i][k] = i >> k & 1;
        }

        return data;
    }

    private static void outputData(String filename, Session sess, int index) {
        double[][] sets = createSets();

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            writer.println("**BEGINNING OF OUTPUT of trial #" + index + "**");


            for (double[] set : sets) {
                printout(writer, set, sess);
            }

            writer.println("**END OF OUTPUT**\n");
            writer.close();
        } catch (Exception e) {
            System.err.println("Something went wrong while printing data to file " + e);
        }
    }

    private static void printout(PrintWriter writer, double[] set, Session sess) {
        writer.print("[ ");

        if (set[0] == 1)
            writer.print("Breeze, ");
        else
            writer.print("      , ");

        if (set[1] == 1)
            writer.print("Bump, ");
        else
            writer.print("    , ");

        if (set[2] == 1)
            writer.print("Glitter, ");
        else
            writer.print("       , ");

        if (set[3] == 1)
            writer.print("Scream, ");
        else
            writer.print("      , ");

        if (set[4] == 1)
            writer.print("Stench, ");
        else
            writer.print("      , ");

        writer.print("]\t");

        Network nn = sess.getNetwork();


        double[] result = nn.feed(new Matrix(set)).toArray();


        writer.print("[ ");
        for (double v : result) {
            writer.print(String.format("%.2f", v) + " ");
        }
        writer.println("]");
    }
}
