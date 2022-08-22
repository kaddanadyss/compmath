import java.io.FileNotFoundException;

public class Lab1 {

    public static void main(String[] args) {
        Gaus matrix = new Gaus();

        try{
            matrix.init("dataC.txt");
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
        }

        matrix.print("Изначальная матрица: ");
        int code = matrix.triangalize();
        matrix.print("После преобразования: ");

        matrix.getSoluton(code);
        matrix.printSolution();
    }
}
