import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class Gaus {
    private double[][] a; // матрица
    private int[] order_of_rows; // порядок строк
    private int n, m; // размеры
    private double[] roots; // корни
    private final double epsilon = 0.000001; // погрешность

    private void create(int k, int l) { //создаем матрицу
        a = new double[k][];
        int i;
        for (i = 0; i < k; i++)
            a[i] = new double[l + 1];
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
        String[] sn = pat.split(str);
        n = Integer.parseInt(sn[0]);
        m = Integer.parseInt(sn[1]);
        create(n, m);
        order_of_rows = new int[n];
        int i, j;
        for (i = 0; i < n; i++) {
            str = scan.nextLine();
            sn = pat.split(str);
            for (j = 0; j < m + 1; j++) {
                if (j != m) {
                    a[i][j] = Double.parseDouble(sn[j]);
                } else {
                    a[i][j] = i;
                }
            }
            order_of_rows[i] = i;
        }
        scan.close();
    }

    // проверяем наличие нулей на диагонали (1)
    private boolean checkingDiagonal(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            double myZero = 1E-4;
            if (Math.abs(a[i][i]) < myZero) return false;
        }
        return true;
    }

    // если есть 0 на диагонали, делаем перестановку строк
    public boolean ZeroDiagonal() {
        if (!checkingDiagonal(a)) {
            int res = find(); // ищем перестановку
            if (res != 0) {
                saveChanges();
                return true;
            }
            else {
                for (int i = 0; i < n; i++) {
                    if (i != order_of_rows[i]) {
                        saveChanges();
                        return true;
                    }
                }
                roots = null;
                return false;
            }
        }
        return true;
    }

    
    // ищем перестановку строк, для которой выполняется ДУС
    // если найти не удалось, ищем любую без 0 на диагонали
    // если перестановку найти удалось - сохраняем ее в копию матрицы
    private int find() {
        // инициализируем переменную итогового результата
        int result = 0;
        int arrang = getFactorial(n);

        // перебираем все комбинации
        // если находим комбинацию с ДУС, завершаем, если находим без 0 на диагонали - переставляем
        for (int pos = 0; pos < arrang; pos++) {
            double[][] res = new double[n][];
            double[][] source = new double[n][];
            System.arraycopy(a, 0, source, 0, n);
            int len = n;

            for (int i = 0; i < n; i = i + 1) {
                int p = pos / getFactorial(n - 1 - i) % len;
                res[i] = source[p];
                source[p] = null;
                for (int j = p; j < n - 1; j++) {
                    double[] tmp = source[j];
                    source[j] = source[j + 1];
                    source[j + 1] = tmp;
                }
                len--;
            }
            if (checkingDiagonal(res)) {
                saveOrder(res);
                if (checkingCondition(res)) return 1;
            }
        }
        return result;
    }

    // соответсвует ДУС или нет
    public void compute() {
        roots = new double[n];
        if (checkingCondition(a)) {
            computeWithCondition(1 + epsilon);
        }
        else {
            computeWithoutCondition();
        }
    }

    // проверяем сходимость по ДУС или при проверке
    private void computeWithCondition(double init_diff) {
        double diff = init_diff;

        // ищем нужню точность
        while (diff > epsilon) {
            diff = computeNewRootsAndFindMax();
        }
    }

    // интерационный метод со счетчиком монотонности (5)
    private void computeWithoutCondition() {
        // инициализируем счетчик монотонности
        int check_sum = 0;
        double[] old_roots = new double[n];

        // ищем корни
        double diff = computeNewRootsAndFindMax();
        System.arraycopy(roots, 0, old_roots, 0, old_roots.length);

        // если не получили монотонность за 10, то метод расходится
        for (int t = 0; t < 9; t++) {
            // если корни получены - выходим из метода
            if (diff < epsilon) return;

            // ищем новые корни
            double new_diff = computeNewRootsAndFindMax();

            // сравниваем максимальную разницу с предыдущей
            // если она меньше - увеличиваем счетчик монотонности
            // иначе - обнуляем
            if (new_diff < diff) check_sum++;
            else check_sum = 0;

            diff = new_diff;
        }
        // если счетчик больше необходимого, то считаем, что метод сходится, и продолжаем вычисления
        if (check_sum >= 4) {
            computeWithCondition(diff);
        }
        else {
            roots = new double[1];
        }
    }

    // вычисляем корни и возвращаем разницу
    private double computeNewRootsAndFindMax() {
        double max_diff = 0;

        // вычисляем корни
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    sum += a[i][j] * roots[j];
                }
            }
            double diff = (a[i][n] - sum) / a[i][i];

            // ищем разницу
            if (Math.abs(roots[i] - diff) > max_diff) {
                max_diff = Math.abs(roots[i] - diff);
            }
            roots[i] = diff;
        }
        return max_diff;
    }

    // проверяем выполнение ДУС (4)
    private boolean checkingCondition(double[][] a) {
        for (int j = 0; j < a.length; j++) {
            double sum = 0;
            for (int k = 0; k < a.length; k++) {
                if (j == k) continue;

                sum += Math.abs(a[j][k]);
            }

            if (sum > Math.abs(a[j][j])) {
                return false;
            }
        }

        for (int j = 0; j < a.length; j++) {
            double sum = 0;
            for (int k = 0; k < a.length; k++) {
                if (j == k) continue;

                sum += Math.abs(a[j][k]);
            }

            if (sum < Math.abs(a[j][j])) {
                return true;
            }
        }
        return false;
    }

    // результаты
    public void printSolution() {
        if (roots == null) {
            System.out.println("Невозможно решить итерационным методом");
            return;
        }
        if (roots.length == 1) {
            System.out.println("Метод расходится");
            return;
        }

        System.out.println("Корни:");
        for (double root : roots) {
            System.out.printf("%15.6E", root);
            System.out.println();
        }
        System.out.println();
    }

    public void saveOrder(double[][] a) {
        for (int i = 0; i < n; i++) {
            order_of_rows[i] = (int) a[i][m];
        }
    }

    public void saveChanges() {
        double[][] tmp_map = new double[n][m];

        // копируем полученную перестановку
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (order_of_rows[i] == a[j][m])
                    tmp_map[i] = a[j];
            }
        }
        System.arraycopy(tmp_map, 0, a, 0, n);
    }

    private int getFactorial(int f) {
        int result = 1;
        for (int i = 1; i <= f; i++) {
            result = result * i;
        }
        return result;
    }
}