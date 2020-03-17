import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;



public class Solution {
	private int size;
	private double[][] matrix;
	//user's error
	private double eps;
	PrintWriter printWriter = new PrintWriter(System.out);
	ArrayList<Double> errorList = new ArrayList<Double>();

	public static void main(String[] args) {
		Solution fixedPointIteration = new Solution();
		Scanner question;
		while (true){
			System.out.print("console/file: ");
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
					System.out.println("Can not read file.");
					System.exit(0);
				}
			} else {
				System.out.println("please write console or file");
			}
		}
		question.close();
		if (!fixedPointIteration.check()) {
			fixedPointIteration.shuffle();
			if (!fixedPointIteration.check()){
				System.exit(0);
			}
		}
		fixedPointIteration.solve();
	}

	//make matrix satisfy the diagonal condition
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

	//make sure that the matrix satisfy the diagonal condition
	public boolean check(){
		int diag = 0;
		int other = 0;
		boolean ok = true;
		System.out.print("Checking the condition: ");
		for (int i = 0; i<size; i++){
			diag+=matrix[i][i];
			for(int j = 0; j<size; j++){
				other+=matrix[i][j];
			}
			other = other - diag;
			if(diag < other){
				ok = false;
			}
			diag = 0;
			other = 0;
		}
		System.out.println(ok);
		return ok;
	}

	//read data from file
	private void readFile() throws FileNotFoundException{
		System.out.print("File name: ");
		Scanner in = new Scanner(System.in);
		in = new Scanner(new File(in.nextLine()));
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

	//read data from console
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

	//solve the System of linear equations
	private void solve(){
		double[] previousVariableValues = new double[size];
		for (int i = 0; i < size; i++) {
			previousVariableValues[i] = 0.0;
		}
		double error;
		while (true) {
			double[] currentVariableValues = new double[size];
			for (int i = 0; i < size; i++) {
				currentVariableValues[i] = matrix[i][size];
				for (int j = 0; j < size; j++) {
					if (i != j) {
						currentVariableValues[i] -= matrix[i][j] * previousVariableValues[j];
					}
				}
				currentVariableValues[i] /= matrix[i][i];
			}
			error = 0.0;
			ArrayList<Double> errors = new ArrayList<Double>();
			for (int i = 0; i < size; i++) {
				errors.add(Math.abs(currentVariableValues[i] -
				 previousVariableValues[i]));
			}
			error = Collections.max(errors);
			errorList.add(error);
			if (error < eps) { break; }
			previousVariableValues = currentVariableValues; 
		}
		printWriter.print("Result:"+"\n");
		for (int i = 0; i < size; i++) { 
			printWriter.print("Variable "+(i+1)+" = "+
				previousVariableValues[i]+"±"
				+error+"\n");
		}
		printWriter.print(errorList.size()+" iterations"+"\n");
		printWriter.print("Error list"+"\n");
		for (int i = 0; i < errorList.size(); i++) {
			printWriter.print(errorList.get(i)+"\n");
		}
		printWriter.close(); 
	}
}