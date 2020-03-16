import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Solution {
	private int size;
	private double[][] matrix;
	private double eps;
	PrintWriter printWriter = new PrintWriter(System.out);

	public static void main(String[] args) {
		Solution fixedPointIteration = new Solution();
		Scanner question;
		while (true){
			System.out.println("console/file");
			question = new Scanner(System.in);
			String choice = question.nextLine();
			if (choice.equals("console")){
				fixedPointIteration.consoleWrite();
				break;
			} else if (choice.equals("file")) {
				try{
					fixedPointIteration.readFile();
					break;
				} catch(FileNotFoundException exc){
					System.out.println("can not read file.");
					System.exit(0);
				}
			} else {
				System.out.println("please write console or file");
			}
		}
		question.close();
		if (!fixedPointIteration.check()) {
			fixedPointIteration.shuffle();
		}
		fixedPointIteration.solve();
	}

	private void shuffle(){
		System.out.println("shuffle the matrix");
		double[][] good = new double[size][size+1];
		for(int i = 0; i<size; i++){
			double maxi = 0;
			int num = 0;
			for (int j = 0;j<size;j++){
				if (matrix[i][j] > maxi){
					maxi = matrix[i][j];
					num = j;
				}
			}
			good[num] = matrix[i];
			maxi = 0;
			num = 0;
		}
		matrix = good;
	}

	public boolean check(){
		int diag = 0;
		int other = 0;
		boolean ok = true;
		System.out.println("Проверка условия преобладания диагональных элементов");
		for (int i = 0; i<size; i++){
			diag+=matrix[i][i];
			for(int j = 0; j<size; j++){
				other+=matrix[i][j];
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

	private void readFile() throws FileNotFoundException{
		Scanner in = new Scanner(new File("sole"));
		size = in.nextInt();
		matrix = new double[size][size + 1];
		eps = in.nextDouble();
		String shit = in.nextLine();

		int counter = 0;
		while(in.hasNextLine()){
			String[] line = in.nextLine().split(" ");
			for (int i = 0;i<size+1;i++){
				matrix[counter][i] = Double.parseDouble(line[i]);
			}
			counter ++;
		}
		in.close();
	}

	private void consoleWrite(){
		Scanner scanner = new Scanner(System.in);
		scanner.useLocale(new Locale("Russian"));

		System.out.println("Input main matrix size");
		size = scanner.nextInt();

		matrix = new double[size][size + 1];

		System.out.println("Input matrix " + (size*size+size) +" numbers");
		String output = "Matrix:\n";
		for (int i = 0; i < size; i++) { 
			for (int j = 0; j < size + 1; j++) {
				matrix[i][j] = scanner.nextDouble();
				output += matrix[i][j] + " ";
			}
			output += "\n";
		}
		System.out.print(output);
		System.out.println("Input accuracy");
		eps = scanner.nextDouble();
		scanner.close();
	}

	private void solve(){
		double[] previousVariableValues = new double[size];
		for (int i = 0; i < size; i++) {
			previousVariableValues[i] = 0.0;
		}
		while (true) {
			// Введем вектор значений неизвестных на текущем шаге 
			double[] currentVariableValues = new double[size];
			// Посчитаем значения неизвестных на текущей итерации 
			// в соответствии с теоретическими формулами 
			for (int i = 0; i < size; i++) {
				// Инициализируем i-ую неизвестную значением 
				// свободного члена i-ой строки матрицы 
				currentVariableValues[i] = matrix[i][size];
				// Вычитаем сумму по всем отличным от i-ой неизвестным 
				for (int j = 0; j < size; j++) {
					if (i != j) {
						currentVariableValues[i] -= matrix[i][j] * previousVariableValues[j];
					}
				}
				// Делим на коэффициент при i-ой неизвестной 
				currentVariableValues[i] /= matrix[i][i];
			}
			// Посчитаем текущую погрешность относительно предыдущей итерации 
			double error = 0.0; 
			for (int i = 0; i < size; i++) {
				error += Math.abs(currentVariableValues[i] - previousVariableValues[i]);
			}
			// Если необходимая точность достигнута, то завершаем процесс 
			if (error < eps) { break; }
			// Переходим к следующей итерации, так 
			// что текущие значения неизвестных 
			// становятся значениями на предыдущей итерации 
			previousVariableValues = currentVariableValues; 
		}
		// Выводим найденные значения неизвестных 
		for (int i = 0; i < size; i++) { 
			printWriter.print("Variable "+(i+1)+" "+previousVariableValues[i] + "\n"); 
		} 
		// После выполнения программы необходимо закрыть 
		// потоки ввода и вывода  
		printWriter.close(); 
	}
}
/*3
10
1
1
12
2
10
1
13
2
2
10
14
0.0027*/
