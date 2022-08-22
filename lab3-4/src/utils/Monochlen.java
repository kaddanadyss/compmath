package utils;


// Класс - вершина полинома (содержит степень, коэффициент и следующую вершину)
public class Monochlen {
    private int deg;
    private double chlenCoeff;
    private Monochlen next = null;

    public Monochlen(int deg, double chlenCoeff) {
        this.deg = deg;
        this.chlenCoeff = chlenCoeff;
    }

    public Monochlen(Monochlen monomial) {
        this.chlenCoeff = monomial.chlenCoeff;
        this.deg = monomial.deg;
    }

    public double getChlenCoeff() {
        return chlenCoeff;
    }

    public int getDeg() {
        return deg;
    }

    public Monochlen getNext() {
        return next;
    }

    public void setNext(Monochlen next) {
        this.next = next;
    }

    public void setChlenCoeff(double chlenCoeff) {
        this.chlenCoeff = chlenCoeff;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }
}