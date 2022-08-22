import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;
import static java.lang.Math.*; 

public class Gaus {
	private double [][] a;
	private int n, m;
	private double[] roots;
	private double myZero = 1E-4;

	private void create(int k, int l)
	{
		a = new double[k][];
		int i;
		for (i = 0; i < k; i++)
			a[i] = new double [l];
	}
	
	public void print()
	{
		int i, j;
		for (i = 0; i < n; i++)
		{
			for (j = 0; j < m; j++)
				System.out.printf("%15.6E", a[i][j]);
			System.out.println();
		}
		System.out.println();
	}

	public void init(String s) throws FileNotFoundException
 	{
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

	public int triangalize(){
		int i;//номер активного уравнения
		int j;//переменная, в которой хранится номер следующего уранения 

		for(i = 0; i < n-1; i++){
			if(Math.abs(a[i][i]) < myZero){// Если a11 == 0, a a21 != 0, или a31 != 0 То переставляем строки
				if(!switchZeroRatio(i)){// Если нет возможности переставить - выходим из метода, потом в checker это поймаем
					return 0;
				}
			}

			countRatio(i);//Пересчет коэффициента строк ниже i
		}

		return triangaleResponse();
	}

	private void countRatio(int i){//Пересчет коэффициента строк ниже i
		int k,j;
		double ratio;

		for(j = i+1; j<n; j++){//Посторочно вызов пересчета коэффицента
			ratio = a[j][i]/a[i][i];// Вводим множитель m
			a[j][i] = 0;//Анулируем крайний слева столбец

			for(k = i+1; k<m; k++){//умножаем m на нынешние ур-ние (a[i]) и вычтем его из следущего(по циклу) a[j]
				a[j][k] -= ratio*a[i][k];

				if(Math.abs(a[j][k])<myZero)a[j][k] = 0;//если коэффицет получился меньше нашего "Нуля" округляем его до нуля 
			}
		}
	}

	public double[] getSoluton(int code){//находит корни
		switch(code){//Проверяем, есть ли ошибки
			case 0:
				System.out.println("Система вырожденная");//Система вырожденная
				return roots;
			case -1:
				System.out.println("Бесконечно решений");//Бесконечно решений
				return roots;
			case -2:
				System.out.println("Нет решений");//Нет решений
				return roots;
		}

		//если все ок, делаем обратную подстановку 
		backSubst();
		return roots;
	}

	public void printSolution(){//выводит корни
		if(roots == null)return;

		System.out.println("Roots:");
		for (int i = 0; i<n; i++){
			System.out.printf("%15.6E\n", roots[i]);
		}
		System.out.println();
	}

	private int triangaleResponse(){

		if(Math.abs(a[n-1][m-2]) < myZero){//Проверка на вырожденность
			if(Math.abs(a[n-1][m-1]) < myZero){// если amn`` == 0 && bn`` == 0
				return -1;//бесконечно решений
			}// если amn`` == 0 && bn`` != 0
			return -2;//нет решений
		}

		return 1;
	}

	private void backSubst(){//Обратная подстановка
		int i,j,k;


		roots = new double[n];

		/*
			Обратная подстановка
			x3 = b3``/a33``
			x2 = (b2` - a23`*x3) / a22`
			x1 = (b1 - a12*x2 - a13*x3) / a11
		*/

		k = 2;
		for(i = n-1; i>=0; i--){
			for (j = m-2; j>m-(k); j--) {
				roots[i] += a[i][j]*roots[j];
			}
			roots[i] = ((a[i][m-1]) - roots[i])/a[i][j];
			k++;
		}
	}

	private boolean switchZeroRatio(int i){//Поиск строки с ненеулевым множителем и перестановка
		for(int j = i+1; j<n; j++){
			if(Math.abs(a[j][i])>myZero){
				switchLines(j, i);
				return true;
			}
		}
		return false;
	}

	private void switchLines(int i, int j){//перестановка строк
		double[] tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
}

