package interpolatePolynoms;

import utils.Grid;
import utils.Polynom;

abstract public class IntepolatePolynom {
    private Polynom polynom;
    private Grid grid;

    public IntepolatePolynom(Grid grid) {
        this.grid = grid;
        polynom = new Polynom();
        interpolate();
    }

    public Polynom getPolynom() {
        return polynom;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setPolynom(Polynom polynom) {
        this.polynom = polynom;
    }

    abstract public void interpolate();
}
