import java.io.FileNotFoundException;


public class Lab2 {

	public static void main(String[] args) {
		Gaus matrix = new Gaus();

		// Считывание и создание матрицы
		try{
			matrix.init("data.txt");
		}catch (FileNotFoundException e){
			System.out.println("File Not Found!");
			return;
		}

		// Печать изначальной матрицы
		matrix.print("Изначальная система: ");

		// Здесь мы пытаемся преобразовать матрицу, если это необходимо
//		Если преобразовать не получилось, то структура matrix изменится, а вывод результатов выдаст ошибку
		if (matrix.prepare()) {
			matrix.print("Матрица после преобразования: ");

			// Вычисляем матрицу
			matrix.compute();
		}

		// Печатаем решение или ошибки
		matrix.printSolution();
	}
}
