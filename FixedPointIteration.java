import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;


public class FixedPointIteration {
	public static void main(String[] args) throws FileNotFoundException{
		Scanner in = new Scanner(new File("sole"));
		int max = in.nextLine().split(" ").length;
		in = new Scanner(new File("sole"));
		double[][] system = new double[max-1][max];
		int counter = 0;
		while (in.hasNextLine()){
			String[] line = in.nextLine().split(" ");
			for (int i = 0;i<max;i++){
				system[counter][i] = Double.parseDouble(line[i]);
			}
			counter ++;
		}
		boolean isok = check(system, max);
		if(!isok){
			System.out.println("Условие преобладания диагональных элементов не выполняется.");
			System.out.println("Отсортируем строки. Результат:");
			system = shuffle(system, max);
			for(double[] opa : system){
				for(double apo : opa){
					System.out.print(apo+" ");
				}
				System.out.println();
			}
			System.out.println("Повторная проверка. Результат: " + check(system, max));
			System.out.println();
		}
		answer(system, max);
	}
	public static boolean check(double[][] system, int max){
		int diag = 0;
		int other = 0;
		boolean ok = true;
		System.out.println("Условие преобладания диагональных элементов");
		for (int i = 0; i<max-1; i++){
			diag+=system[i][i];
			for(int j = 0; j<max-1; j++){
				other+=system[i][j];
			}
			other = other - diag;
			if(diag < other){
				ok = false;
			}
			System.out.printf("%d > %d\n", diag, other);
			diag = 0;
			other = 0;
		}
		return ok;
	}
	public static double[][] shuffle(double[][] system, int max){
		double[][] good = new double[max-1][max];
		for(int i = 0; i<max-1; i++){
			double maxi = 0;
			int num = 0;
			for (int j = 0;j<max-1;j++){
				if (system[i][j] > maxi){
					maxi = system[i][j];
					num = j;
				}
			}
			good[num] = system[i];
			maxi = 0;
			num = 0;
		}
		return good;
	}
	public static void answer(double[][] system, int max){
		for (int i = 0;i<max-1;i++) {
			double accum = system[i][0];
			double hero = system[i][i];
			system[i][0] = system[i][i];
			system[i][i] = accum;
			for (int j = 0;j<max;j++) {
				if((j != 0)&&(j != max-1)){
					system[i][j] = -system[i][j]/hero;
				} else {
					system[i][j] = system[i][j]/hero;
				}
			}
		}
		double[] answers = new double[max-1];
		double[] copy = new double[max-1];
		System.out.printf("%d круг ада\n", 0);
		for (int i = 0;i<max-1;i++){
			answers[i] = system[i][max-1];
			System.out.print(answers[i]+" ");
		}
		System.out.println();


		for (int k = 1;k<6;k++) {
			System.out.printf("%d круг ада\n", k);
			for (int i = 0;i<max-1;i++) {
				double sum = 0;
				for (int j = 1;j<max-1;j++){
					if(j<=i){
						sum += system[i][j]*Math.pow(answers[j-1],k);
						//System.out.printf("%f*%f^%d+",system[i][j],answers[j-1], k);
					} else {
						sum += system[i][j]*Math.pow(answers[j],k);
						//System.out.printf("%f*%f^%d+",system[i][j], answers[j], k);
					}
				}
				//System.out.println();
				sum += system[i][max-1];
				sum = Math.pow(sum, 1/k);
				copy[i] = sum;
				System.out.printf("%f ",sum);
			}
			for (int i = 0;i<max-1;i++) {
				answers[i] = copy[i];
			}
			System.out.println();
		}
	}
}
