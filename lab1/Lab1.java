import java.io.FileNotFoundException;

public class Lab1{

	public static void main(String[] args) {
		Gaus matrix = new Gaus();

		try{
			matrix.init("data.txt");
		}catch (FileNotFoundException e){
			System.out.println("File Not Found!");
		}

		matrix.print();
		int code = matrix.triangalize();
		matrix.print();
		matrix.getSoluton(code);
		matrix.printSolution();
	}
}
