import java.io.FileNotFoundException;


public class Lab2 {

	public static void main(String[] args) {
		Gaus matrix = new Gaus();

		// ���������� � �������� �������
		try{
			matrix.init("data.txt");
		}catch (FileNotFoundException e){
			System.out.println("File Not Found!");
			return;
		}

		// ������ ����������� �������
		matrix.print("����������� �������: ");

		// ����� �� �������� ������������� �������, ���� ��� ����������
//		���� ������������� �� ����������, �� ��������� matrix ���������, � ����� ����������� ������ ������
		if (matrix.prepare()) {
			matrix.print("������� ����� ��������������: ");

			// ��������� �������
			matrix.compute();
		}

		// �������� ������� ��� ������
		matrix.printSolution();
	}
}
