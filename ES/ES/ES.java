import NN.Matrix;
import NN.Operator;

import java.util.Random;

/**
 * Created by Owner on 5/16/2017.
 */
public class ES {
    final static private int npop = 50;
    final static private double alpha = 0.001;
    final static private double sigma = 0.1;

    private Matrix current_guess;
    private Matrix solution;
    private Random random;

    ES(Matrix solution) {
        random = new Random(0);
        this.solution = solution;
        current_guess = Matrix.randInit(solution.getHeight(), solution.getWidth(), 0);
    }


    private static double mean;
    private static double std;

    private static double mean(Matrix matrix) {
        matrix.forEach(a -> {
            mean += a;
            return a;
        });
        mean = mean / (matrix.getHeight() * matrix.getWidth());
        return mean;
    }

    private static double std(Matrix matrix) {
        mean = mean(matrix);
        Matrix dev = matrix;
        dev = dev.forEach(a -> (a - mean) * (a - mean));

        std = Math.sqrt(mean(dev));
        return std;
    }

    public double f(Matrix guess) {
        double err = 0;
        for (int i = 0; i < guess.getHeight(); i++)
            for (int j = 0; j < guess.getWidth(); j++)
                err += (guess.index(i, j) - solution.index(i, j)) * (guess.index(i, j) - solution.index(i, j));

        return err;
    }

    public void evolve() {
        Matrix[] n_tries = new Matrix[npop];
        Matrix R = new Matrix(npop, 1);

        for (int i = 0; i < npop; i++) {
            n_tries[i] = new Matrix(solution.getHeight(), solution.getWidth());
            n_tries[i] =  n_tries[i].forEach(a -> random.nextGaussian());
        }

        for (int i = 0; i < npop; i++) {
            Matrix w_try = current_guess.add(n_tries[i].mult(sigma));
            R.set(f(w_try), i, 0);
        }

        std(R);
        mean(R);
        Matrix A = R.forEach(a -> a - mean).mult(1 / std);

        double c = alpha / (npop * sigma);
        for(int i=0;i<solution.getWidth();i++)
        {
            double t =0;
            for(int j=0;j<npop;j++)
            {
                t+=n_tries[j].index(0,i) * A.index(i,0);
            }
            current_guess.set(t,0,i);
        }
    }


    public Matrix getCurrent_guess(){
        return current_guess;
    }

}
