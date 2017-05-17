package NN;

/**
 * Created by ArxAr on 4/5/2017.
 */
public class Sigmoid implements ActivationFunction {
    @Override
    public Matrix activationFunction(Matrix matrix) {
        return matrix.forEach(a -> 1.0 / (1 + Math.exp(-a)));
    }

    @Override
    public Matrix inverseFunction(Matrix matrix) {
        return matrix.forEach(a -> a * (1 - a));
    }
}
