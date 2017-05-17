package NN;

/**
 * Created by ArxAr on 2/16/2017.
 */
public class Functions {


    public static Matrix normalize(Matrix matrix) {
        if (!allPositive(matrix)) {
            //throw new RuntimeException("negative value in probability density function");
            /*
            final double min = matrix.getMin();

            matrix = matrix.forEach(new Operator() {
                @Override
                public double operate(double a) {
                    return a + min;
                }
            });
            */
        }

        double abs = matrix.abs();

        if (abs == 0)
            return matrix;

        Matrix result = matrix.mult(1 / abs);
        result.abs = matrix.abs;

        return result;
    }

    public static Matrix softMax(Matrix matrix) {

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

    public static Matrix derSoftMax(Matrix matrix) {

        return derSigmoid(matrix);
    }

    public static Matrix derSigmoid(Matrix matrix) {
        return matrix.forEach(a -> a * (1 - a));
    }

    public static Matrix sigmoid(Matrix matrix) {

        //return matrix;

        return matrix.forEach(new Operator() {
            @Override
            public double operate(double a) {
                return 1.0 / (1 + Math.exp(-a));
            }
        });
    }

    public static Matrix invSigmoid(Matrix matrix) {
        //return matrix;

        return matrix.forEach(new Operator() {
            @Override
            public double operate(double a) {
                if (a <= 0 || a >= 1)
                    throw new ArithmeticException("trying to get sigmoid^-1 of " + a);
                return Math.log(a) - Math.log(1 - a);
            }
        });
    }

    public static boolean allPositive(Matrix matrix) {
        for (int i = 0; i < matrix.getHeight(); i++) {
            for (int j = 0; j < matrix.getWidth(); j++) {
                if (matrix.index(i, j) < 0)
                    return false;
            }
        }

        return true;
    }

}
