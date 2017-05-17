package NN;

import java.util.Random;

/**
 * Created by ArxAr on 4/5/2017.
 */
public class Layer {
    public Matrix left;
    public Matrix right;
    Matrix bias;

    Layer(int numNeural, int input, int output, long seed, boolean bias){
        Random random = new Random(seed);

        this.left = Matrix.randInit(numNeural,input, random.nextLong());
        this.right = Matrix.randInit(output,numNeural, random.nextLong());

        if(bias)
            this.bias = Matrix.randInit(numNeural,1,random.nextLong());
        else
            this.bias = null;
    }

    public Layer(Matrix left, Matrix right, Matrix bias){
        this.left = left;
        this.right = right;
        this.bias = bias;
    }

    //temporary implementation
    Matrix calculation(Matrix input, ActivationFunction function){
        Matrix hidden = left.mult(input);
        if(bias != null){
            hidden.add(bias);
        }
        hidden = function.activationFunction(hidden);

        Matrix out = right.mult(hidden);

        return Functions.softMax(out);
    }
}
