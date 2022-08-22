package interpolatePolynoms;

import utils.Grid;
import utils.Polynom;

public class LagrangePolynom extends IntepolatePolynom{
    public LagrangePolynom(Grid grid) {
        super(grid);
    }

    // Интерполяция Лагранжем
    @Override
    public void interpolate() {
        // Просматриваем все точки, по которым интерполлируем
        for (int i = 0; i < getGrid().getKnots().length; i++) {
            double tmp_x = getGrid().getKnots()[i];
            Polynom subPoly;
            int subIndex = i == 0 ? 1 : 0;
            double summary = tmp_x - getGrid().getKnots()[subIndex];

            // Создаем текущую вершину полинома
            Polynom oneTermOfPoly = new Polynom(1, 1);
            oneTermOfPoly.add(new Polynom(0, -getGrid().getKnots()[subIndex]));

            // Перебираем все вершины, кроме первой, для обновления состояния полинома
            for (int j = 1; j < getGrid().getKnots().length; j++) {
                if (j == i)
                    continue;

                // Первую вершину пропускаем
                if (i != 0 || j != 1) {
                    double curElem = getGrid().getKnots()[j];
                    subPoly = new Polynom(1, 1); // x
                    summary *= (tmp_x - curElem);
                    subPoly.add(new Polynom(0, -curElem));
                    // Перемножаем новое состояние на текущее
                    oneTermOfPoly.multiply(subPoly);
                }
            }

            oneTermOfPoly.mulByNum(getGrid().getValues()[i] / summary);
            if (i == 0) {
                setPolynom(new Polynom(oneTermOfPoly.getHead())); // Создаем новый полином
            } else {
                getPolynom().add(oneTermOfPoly);  // Добавляем к старому полиному текущий
            }
        }
    }
}
