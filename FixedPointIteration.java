import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class FixedPointIteration {
	public static void main(String[] args) throws FileNotFoundException{
		Scanner in = new Scanner(new File("sole"));
		int max = in.nextLine().split(" ").length;
		in = new Scanner(new File("sole"));
		int[][] system = new int[max][max];
		int counter = 0;
		while (in.hasNextLine()){
			String[] line = in.nextLine().split(" ");
			for (int i = 0;i<max;i++){
				system[counter][i] = Integer.parseInt(line[i]);
			}
			counter ++;
		}
		boolean isok = check(system, max);
		if(!isok){
			System.out.println("Условие преобладания диагональных элементов не выполняется.");
			System.out.println("Отсортируем строки. Результат:");
			system = shuffle(system, max);
			for(int[] opa : system){
				for(int apo : opa){
					System.out.print(apo+" ");
				}
				System.out.println();
			}
			System.out.println("Повторная проверка. Результат: " + check(system, max));
		}
	}
	public static boolean check(int[][] system, int max){
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
			System.out.printf("%d < %d\n", diag, other);
			diag = 0;
			other = 0;
		}
		return ok;
	}
	public static int[][] shuffle(int[][] system, int max){
		int[][] good = new int[max][max];
		for(int i = 0; i<max-1; i++){
			int maxi = 0;
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
}
