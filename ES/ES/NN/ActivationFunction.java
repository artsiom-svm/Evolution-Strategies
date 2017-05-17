package NN;

/**
 * Created by ArxAr on 4/5/2017.
 */
public interface ActivationFunction {
    Matrix activationFunction(Matrix matrix);

    Matrix inverseFunction(Matrix matrix);
}
