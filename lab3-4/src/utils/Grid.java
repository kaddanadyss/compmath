package utils;

// Класс, хранящий узлы и значения в узлах
public class Grid {
    private double[] knots;
    private double[] values;

    public Grid(double[] x, double[] y) {
        knots = x;
        values = y;
    }

    public double[] getKnots() {
        return knots;
    }

    public double[] getValues() {
        return values;
    }
}
