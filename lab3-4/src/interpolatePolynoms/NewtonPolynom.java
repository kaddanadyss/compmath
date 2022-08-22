package interpolatePolynoms;

import utils.Grid;
import utils.Monochlen;
import utils.Polynom;

public class NewtonPolynom extends IntepolatePolynom {

    public NewtonPolynom(Grid grid) {
        super(grid);
    }

    @Override
    public void interpolate() {
        // Итоговые разницы 0 степени
        double[] diff = new double[getGrid().getKnots().length];

        // Подсчет разниц
        double[] first = new double[getGrid().getKnots().length];
        double[] second = new double[getGrid().getKnots().length];
        for (int i = 0; i < getGrid().getKnots().length; i++) {
            first[i] = getGrid().getValues()[i];
        }
        diff[0] = first[0];
        for(int i = 1; i < getGrid().getKnots().length; i++) {
            for(int j = 0; j < getGrid().getKnots().length - i; j++) {
                second[j] = (first[j] - first[j + 1]) / (getGrid().getKnots()[j] - getGrid().getKnots()[j + i]);
            }
            diff[i] = second[0];
            System.arraycopy(second, 0, first, 0, second.length);
        }

        // Итоговый полином сохраняем с изначальным свободным членом
        setPolynom(new Polynom(0, diff[0]));
        Polynom bin = new Polynom();

        // Пока не пройдем все разности - модифицируем полином
        for (int i = 0; i < getGrid().getKnots().length - 1; i++) {
            Polynom tmp = new Polynom(0,1);
            for(int j = 0; j <= i; j++) {
                double free = -getGrid().getKnots()[j];

                if (bin.getHead() != null) {
                    bin.getHead().getNext().setChlenCoeff(free);
                } else {
                    bin.setHead(new Monochlen(1, 1));
                    bin.getHead().setNext(new Monochlen(0, free));
                }

                tmp.multiply(bin);
            }
            tmp.mulByNum(diff[i + 1]);
            getPolynom().add(tmp);
        }
    }
}
