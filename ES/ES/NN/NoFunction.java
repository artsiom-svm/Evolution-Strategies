package NN;

/**
 * Created by ArxAr on 4/5/2017.
 */
public class NoFunction implements ActivationFunction {
    @Override
    public Matrix activationFunction(Matrix matrix) {
        return matrix;
    }

    @Override
    public Matrix inverseFunction(Matrix matrix) {
        return  new Matrix(matrix.getHeight(), matrix.getWidth()).forEach(a-> a = 1);
    }
}
