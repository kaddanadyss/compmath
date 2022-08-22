import interpolatePolynoms.IntepolatePolynom;
import interpolatePolynoms.LagrangePolynom;
import interpolatePolynoms.NewtonPolynom;
import utils.Grid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Lab3 {
    private static Grid grid;
    private static IntepolatePolynom lagrange;
    private static IntepolatePolynom newton;


    public static void main(String[] args) {
        try {
            grid = readDataFromFile();
            createInterpolatePolynoms();
            printPolynoms();
            printTable();
        } catch (FileNotFoundException e) {
            System.out.println("Невозможно открыть файл");
        }
    }

    // Функция
    private static double function(double x) {
        return Math.pow(x, 5) - 4.378 * Math.pow(x, 4) - 2.177 * Math.pow(x, 2) + 0.331;
    }

    // Чтение данных
    private static Grid readDataFromFile() throws FileNotFoundException {
        File file = new File("data.txt");
        Scanner scan = null;
        scan = new Scanner(file);
        Pattern pat = Pattern.compile("[\\s\\t]+");
        String str = scan.nextLine();
        String[] sn = pat.split(str);
        double a = Double.parseDouble(sn[0]);
        double b = Double.parseDouble(sn[1]);
        int n = Integer.parseInt(sn[2]);

        double h = Math.abs(b - a) / (n - 1);
        double[] x = new double[n];
        double[] y = new double[n];

        double curr_x = a;
        double curr_y = function(curr_x);

        if (a > b) {
            System.out.println("Левая граница больше правой");
        } else {
            for (int i = 0; i < n; i++) {
                x[i] = curr_x;
                y[i] = curr_y;

                curr_x += h;
                curr_y = function(curr_x);
            }
        }

        return new Grid(x, y);
    }

    // Создание интерполирующих полиномов
    private static void createInterpolatePolynoms() {
        lagrange = new LagrangePolynom(grid);
        newton = new NewtonPolynom(grid);
    }

    // Печать полинома
    private static void printPolynoms() {
        System.out.println();
        System.out.println("Полином Лагранжа: ");
        lagrange.getPolynom().print();
        System.out.println();
        System.out.println("Полином Ньютона: ");
        newton.getPolynom().print();
    }

    // Печать итоговй таблицы
    private static void printTable() {
        System.out.println("\t\t\tx\t\t\ty\t\t\tf(x)\t\t\tLagrange\t\t\tNewton");
        for (int i = 0; i < grid.getKnots().length - 1; i++) {
            System.out.print(i + "  ");
            System.out.printf("%15.6E\t%15.6E\t%15.6E\t%15.6E\t%15.6E\t\n",
                    grid.getKnots()[i],
                    grid.getValues()[i],
                    function(grid.getKnots()[i]),
                    lagrange.getPolynom().calculateTheValue(grid.getKnots()[i]),
                    newton.getPolynom().calculateTheValue(grid.getKnots()[i]));

            double m = (grid.getKnots()[i + 1] + grid.getKnots()[i]) / 2;
            System.out.print(i + 0.5);
            System.out.printf("%15.6E\t\t\t%s\t\t%15.6E\t%15.6E\t%15.6E\t\n",
                    m,
                    "-",
                    function(m),
                    lagrange.getPolynom().calculateTheValue(m),
                    newton.getPolynom().calculateTheValue(m));
        }

        System.out.print((grid.getKnots().length - 1) + " ");
        System.out.printf("%15.6E\t%15.6E\t%15.6E\t%15.6E\t%15.6E\t\n",
                grid.getKnots()[grid.getKnots().length - 1],
                grid.getValues()[grid.getValues().length - 1],
                function(grid.getKnots()[grid.getKnots().length - 1]),
                lagrange.getPolynom().calculateTheValue(grid.getKnots()[grid.getKnots().length - 1]),
                newton.getPolynom().calculateTheValue(grid.getKnots()[grid.getKnots().length - 1]));
    }
}

