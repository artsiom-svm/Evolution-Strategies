package NN;


import WumpusWorld.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static NN.Functions.softMax;

/**
 * Created by ArxAr on 2/16/2017.
 */
public class Network {

    public GameData data;

    private ActivationFunction activationFunction;
    public List<Layer> layers;
    private List<Pair> hidden_layers;
    private List<Matrix> hidden_out;
    private Matrix input;
    private Matrix output;
    private double learning_rate;
    private double decay_rate;
    private double bias_learning_rate;
    private int counter;
    private Matrix no_layers;


    //define input dimensions
    public Network(int height, int width) {
        learning_rate = 0.05;
        bias_learning_rate = 0.1;
        decay_rate = 0;
        hidden_layers = new ArrayList<>(0);
        hidden_out = new ArrayList<>(0);
        layers = new ArrayList<>(0);
        input = new Matrix(height, width);
        output = null;
        data = new GameData();

        counter = 0;
        no_layers = null;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    public void add_layer(int numNeural, long seed) {
        Matrix hidden_left;
        Matrix hidden_right = null;

        //init random generator
        Random rand = new Random(seed);

        Layer l = new Layer(numNeural, input.getHeight(), output.getHeight(), seed, false);
        layers.add(l);

        /*
        //if the first layer
        if (hidden_layers.size() == 0) {
            hidden_left = Matrix.randInit(numNeural, input.getHeight(), rand.nextLong());
        } else {
            Pair prev = hidden_layers.get(hidden_layers.size() - 1);

            prev.second = Matrix.randInit(prev.first.getHeight(), numNeural, rand.nextLong());
            hidden_left = Matrix.randInit(numNeural, prev.second.getHeight(), rand.nextLong());
        }

        if (output != null) {
            hidden_right = Matrix.randInit(output.getHeight(), numNeural, rand.nextLong());
        }

        System.out.println(hidden_right);

        hidden_layers.add(new Pair(hidden_left, hidden_right));
        */
    }

    //define output dimensions
    public void setOutput(int height, int width) {
        output = new Matrix(height, width);
    }

    public void backprop(Matrix input, Matrix output) {

        feed(input);

        for (int i = hidden_out.size() - 1; i >= 0; i--) {

            Matrix hright = hidden_layers.get(i).second,
                    hleft = hidden_layers.get(i).first;
            hidden_layers.remove(i);

            // w <- w + nIj
            hright = hright.add(hright.scalMult(output).mult(learning_rate));

            Matrix temp = hidden_out.get(i).subract(hright.transpose().mult(output));

            hleft = hleft.add(hleft.scalMult(temp).mult(learning_rate));
            output = hleft;

            hidden_layers.add(i, new Pair(hleft, hright));
        }

    }

    public void _backprop(Matrix input, Matrix output) {

        if(layers.size() == 0){
            _backprop_no_layers(input,output);
            return;
        }
        counter++;
        updateRate();

        feed(input);

        Matrix lw = layers.get(0).left,
                rw = layers.get(0).right,
                bias = layers.get(0).bias,
                hout = hidden_out.get(0);
        hidden_out.remove(0);
        layers.remove(0);

        //f(x) = softmax(x)

        //derr/dout = df(out)/dx
        Matrix delrw = output.hadamard(activationFunction.inverseFunction((rw.mult(hout))));
        //d(rw) = derr/dout out^T
        Matrix drw = delrw.mult(hout.transpose());

        //d(lw) = (rw^T derr/dout)* df(out)/dx input^T
        //i hope it works now
        Matrix dellw = rw.transpose().mult(delrw).hadamard(lw.mult(input));

        Matrix dlw = dellw.mult(input.transpose());

        //w <- w + n*d(w)
        rw = rw.add(drw.hadamard(rw).mult(learning_rate));
        lw = lw.add(dlw.hadamard(lw).mult(learning_rate));

        //bias update
        if (bias != null)
            bias = softMax(bias.add(dellw.mult(bias_learning_rate)));

        layers.add(new Layer(lw, rw, bias));
        //hidden_layers.add(new Pair(lw, rw));
    }

    private void _backprop_no_layers(Matrix input, Matrix output) {
        Matrix changes = output.mult((input.transpose()));

        no_layers = no_layers.add(no_layers.hadamard(changes).mult(learning_rate));
    }

    public Matrix feed(Matrix input) {

        //return softMax(no_layers.mult(input));


        if (layers.size() == 0)
            return softMax((no_layers.mult(input)));

        this.input = input;
        hidden_out.clear();

        //Matrix result = input;
        Matrix left, right, bias, hout, result;
        left = layers.get(0).left;
        right = layers.get(0).right;
        bias = layers.get(0).bias;

        hout = (left.mult(input));

        if(bias != null)
            hout = hout.add(bias);

        hout = activationFunction.activationFunction(hout);

        hidden_out.add(hout);

        result = right.mult(hout);

        if (Double.isNaN(result.index(0, 0))) {
            System.err.println("How the heck you can get nan after multiplication???");
        }

        output = softMax(result);
        return output;



        /*
        for (Pair hidden_layer : hidden_layers) {
            Matrix temp = sigmoid(hidden_layer.first.mult(result));
            hidden_out.add(temp);

            result = sigmoid(hidden_layer.second.mult(temp));
        }

        output = invSigmoid(result);
        return output;

        //return normalize(result);
        */
    }

    public double getDecay_rate() {
        return decay_rate;
    }

    private void updateRate() {
        if (counter == 1000) {
            // learning_rate*= 0.99;
            // counter = 0;
        }
    }

    public void setNo_layers() {
        no_layers = Matrix.randInit(output.getHeight(), input.getHeight(), new Random().nextLong());
    }
}
