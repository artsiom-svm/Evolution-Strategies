package NN;

import java.util.Random;

/**
 * Created by ArxAr on 2/16/2017.
 */
public class Matrix {
    private int height;
    private int width;
    private double[][] data;

    private static double boundary = 200;

    double abs;

    public Matrix() {
        height = width = 0;
        data = new double[height][width];
        abs = 0;
    }

    public Matrix(int height, int width) {
        this.height = height;
        this.width = width;
        this.data = new double[height][width];
        this.abs = 0;
    }

    public Matrix(double[][] data) {
        this.height = data.length;
        this.width = data[0].length;

        this.data = new double[height][width];

        for (int i = 0; i < data.length; i++)
            System.arraycopy(data[i], 0, this.data[i], 0, data[i].length);

        this.abs = 0;
    }

    public Matrix(double[] data) {
        this.height = data.length;
        this.width = 1;

        this.data = new double[height][width];

        for (int i = 0; i < data.length; i++)
            this.data[i][0] = data[i];

        this.abs = 0;
    }

    public void setRandom(long seed) {
        Random rand = new Random(seed);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = rand.nextDouble();
            }
        }
    }

    public Matrix mult(Matrix second) {
        if (this.width != second.height)
            throw new IndexOutOfBoundsException("matrix " + this.height + "x" + this.width + " multiplied with " + second.height + "x" + second.width + "\n");


        Matrix result = new Matrix(this.height, second.width);

        for (int i = 0; i < result.data.length; i++) {
            for (int m = 0; m < second.data.length; m++) {
                for (int j = 0; j < result.data[i].length; j++) {
                    result.data[i][j] += this.data[i][m] * second.data[m][j];
                }
            }
        }

        return result;
    }

    public Matrix mult(double scalar) {
        Matrix result = new Matrix(height, width);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                result.data[i][j] = scalar * data[i][j];
            }
        }

        return result;
    }

    public Matrix scalMult(Matrix second) {
        if (this.height != second.height)
            throw new IndexOutOfBoundsException("matrix " + this.height + "x" + this.width + " multiplied with " + second.height + "x" + second.width + "\n");

        Matrix result = new Matrix(this.height, this.width);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                result.data[i][j] = data[i][j] * second.data[i][0];
            }
        }

        return result;
    }

    public Matrix qaziInv(Matrix second) {
        if (this.height != second.height)
            throw new IndexOutOfBoundsException("matrix " + this.height + "x" + this.width + " multiplied with " + second.height + "x" + second.width + "\n");

        Matrix result = new Matrix(this.height, second.width);

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                for (int k = 0; k < data.length; k++) {
                    result.data[i][j] += data[i][k] * second.data[i][j];
                }
            }
        }

        return result;
    }

    public Matrix add(Matrix second) {
        if (this.height != second.height || this.width != second.width)
            throw new ArrayIndexOutOfBoundsException();

        Matrix result = new Matrix(height, width);

        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[i].length; j++) {
                result.data[i][j] = this.data[i][j] + second.data[i][j];
            }
        }

        return result;
    }

    public Matrix subract(Matrix second) {
        if (this.height != second.height || this.width != second.width)
            throw new ArrayIndexOutOfBoundsException();

        Matrix result = new Matrix(height, width);

        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[i].length; j++) {
                result.data[i][j] = this.data[i][j] - second.data[i][j];
            }
        }

        return result;
    }

    public double index(int height, int width) {
        if (this.width < width || this.height < height || height < 0 || width < 0)
            throw new ArrayIndexOutOfBoundsException();

        return data[height][width];
    }

    //return sum of all entrances
    public double abs() {
        abs = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                abs += data[i][j];
            }
        }

        return abs;
    }

    public void set(double value, int height, int width) {
        if (this.width < width || this.height < height || height < 0 || width < 0)
            throw new ArrayIndexOutOfBoundsException();

        data[height][width] = value;
    }

    public Matrix forEach(Operator operator) {
        Matrix result = new Matrix(height, width);

        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[i].length; j++) {
                result.data[i][j] = operator.operate(data[i][j]);
            }
        }

        return result;
    }

    public Matrix hadamard(Matrix second) {
        if (height != second.height || width != second.width)
            throw new ArrayIndexOutOfBoundsException("unequal size: " + height + " & " + second.height + ", " + width + " & " + second.width + "\n");

        Matrix result = new Matrix(height, width);

        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[i].length; j++) {
                //result.data[i][j] = data[i][j] * second.data[i][j];

                double t = data[i][j] * second.data[i][j];
                if(t > boundary)
                    t = boundary;
                if(t < -boundary)
                    t = - boundary;

                result.data[i][j] = t;
            }
        }

        return result;
    }

    public Matrix transpose() {
        Matrix result = new Matrix(width, height);

        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[i].length; j++) {
                result.data[i][j] = data[j][i];
            }
        }

        return result;
    }

    public double getMin() {
        double min = data[0][0];
        for (double[] doubles : data) {
            for (double v : doubles) {
                if (v < min)
                    min = v;
            }
        }

        return min;
    }

    public double getMax() {
        double max = data[0][0];
        for (double[] doubles : data) {
            for (double v : doubles) {
                if (v > max)
                    max = v;
            }
        }

        return max;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String toString() {
        String ans = "";

        for (int i = 0; i < data.length; i++) {
            ans += "| ";
            for (int j = 0; j < data[i].length; j++) {
                ans += String.format("%.2f", data[i][j]) + " ";
            }
            ans += "|\n";
        }

        return ans;
    }

    public double[] toArray() {
        double[] result = new double[height * width];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                result[i * width + j] = data[i][j];
            }
        }

        return result;
    }

    public static Matrix randInit(int height, int width, long seed) {
        Matrix result = new Matrix(height, width);
        result.setRandom(seed);

        return (result);
    }

    public static Matrix randInitDiagonal(int height, long seed){
        Matrix result =  new Matrix(height,height);

        Random random = new Random(seed);

        for (int i = 0; i < result.data.length; i++) {
            for (int j = 0; j < result.data[i].length; j++) {
                if( i == j)
                    result.data[i][j] = random.nextDouble();
                else
                    result.data[i][j] = 0;
            }
        }

        return result;
    }
}

