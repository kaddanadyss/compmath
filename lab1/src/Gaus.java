import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class Gaus {
    private double [][] a; // матрица
    private int n, m; // размеры
    private double[] roots; // корни
    private final double myZero = 1E-4;

    private void create(int k, int l) { // создаем матрицу
        a = new double[k][];
        int i;
        for (i = 0; i < k; i++)
            a[i] = new double [l];
    }

    public void print(String s) { // печататем матрицу
        System.out.println(s);
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < m; j++)
                System.out.printf("%15.6E", a[i][j]);
            System.out.println();
        }
        System.out.println();
    }

    public void init(String s) throws FileNotFoundException { // инициализируем матрицу
        File file = new File(s);
        Scanner scan = new Scanner(file);
        Pattern pat = Pattern.compile("[\\s\\t]+");
        String str = scan.nextLine();
        String [] sn = pat.split(str);
        n = Integer.parseInt(sn[0]);
        m = Integer.parseInt(sn[1]);
        create(n, m);
        int i, j;
        for (i = 0; i < n; i++)
        {
            str = scan.nextLine();
            sn = pat.split(str);
            for(j = 0; j < m; j++)
                a[i][j] = Double.parseDouble(sn[j]);
        }
        scan.close();
    }

    public int triangalize() { // приведение к треугольному виду
        int i; // активное уравнение

        for(i = 0; i < n-1; i++) {
            if(Math.abs(a[i][i]) < myZero) { // переставляем строки
                if(!switchZeroRatio(i)) {// выходим, если не переставляем
                    return 0;
                }
            }

            countRatio(i); // перерасчет коэффициента строк ниже i
        }

        return triangaleResponse(); // (после countRation)
    }

    private void countRatio(int i) { // пересчет коэффициента строк ниже i
        int k,j;
        double ratio;

        for(j = i+1; j<n; j++) { // вызываем построчно
            ratio = a[j][i]/a[i][i];
            a[j][i] = 0; // обнуляем последний левый столбец

            for(k = i+1; k<m; k++) {
                a[j][k] -= ratio*a[i][k];

                if(Math.abs(a[j][k])<myZero)a[j][k] = 0; // округляем до нуля
            }
        }
    }

    private int triangaleResponse() {

        if(Math.abs(a[n-1][m-2]) < myZero) { // проверяем систему на вырожденность
            if(Math.abs(a[n-1][m-1]) < myZero) {
                return -1; // бесконечно много решений
            }
            return -2; // нет решений
        }

        return 1;
    }

    private void backSubst() { // делаем обратнуб подстановку
        int i,j,k;
        roots = new double[n];

        k = 2;
        for(i = n-1; i>=0; i--) {
            for (j = m-2; j>m-(k); j--) {
                roots[i] += a[i][j]*roots[j];
            }
            roots[i] = ((a[i][m-1]) - roots[i])/a[i][j];
            k++;
        }
    }

    private boolean switchZeroRatio(int i) { // ищем строки с ненеулевым множителем
        for(int j = i+1; j<n; j++) {
            if(Math.abs(a[j][i])>myZero) {
                switchLines(j, i); // делаем переатсновку
                return true;
            }
        }
        return false;
    }

    private void switchLines(int i, int j) { // перестановка строк
        double[] tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public void getSoluton(int code) { // находим корни
        switch (code) { // проверяем на ошибки
            case 0 -> {
                System.out.println("Система вырожденная"); // система вырожденная
                return;
            }
            case -1 -> {
                System.out.println("Решений бесконечно много"); // решений бесконечно много
                return;
            }
            case -2 -> {
                System.out.println("Нет решений"); // нет решений
                return;
            }
        }

        backSubst(); // обратная подстановка
    }

    public void printSolution() { // выводим корни
        if(roots == null)return;

        System.out.println("Корни:");
        for (int i = 0; i<n; i++) {
            System.out.printf("%15.6E\n", roots[i]);
        }
        System.out.println();
    }
}
