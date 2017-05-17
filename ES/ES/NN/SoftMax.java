package NN;

/**
 * Created by ArxAr on 4/5/2017.
 */
public class SoftMax implements ActivationFunction {
    @Override
    public Matrix activationFunction(Matrix matrix) {
        final double boundary = 200;
        double min = matrix.getMin(),
                max = matrix.getMax();

        if (min < -boundary) {
            final double coef = -min - boundary;
            matrix = matrix.forEach(new Operator() {
                @Override
                public double operate(double a) {
                    return a + coef;
                }
            });
        }
        if (max > boundary) {
            final double coef = -max + boundary;
            matrix = matrix.forEach(new Operator() {
                @Override
                public double operate(double a) {
                    return a + coef;
                }
            });
        }

        double expsum = 0;
        for (int i = 0; i < matrix.getHeight(); i++) {
            for (int j = 0; j < matrix.getWidth(); j++) {
                expsum += Math.exp(matrix.index(i, j));
            }
        }

        Matrix result = matrix.forEach(new Operator() {
            @Override
            public double operate(double a) {
                return Math.exp(a);
            }
        });

        return result.mult(1 / expsum);
    }

    @Override
    public Matrix inverseFunction(Matrix matrix) {
        return matrix.forEach(a -> a * (1 - a));
    }
}
