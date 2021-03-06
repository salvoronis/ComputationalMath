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
					System.out.println(exc);
				}
			} else {
				System.out.println("please write console or file");
			}
		}
		question.close();
		if (!fixedPointIteration.isDiagCond()) {
			try{
				fixedPointIteration.MatrixToDiagCond();
			}catch(IncorrectMatrixException e){
				System.out.println(e);
			}
			if (!fixedPointIteration.isDiagCond()){
				System.out.println("There are no diagonal condition");
				System.exit(0);
			}
		}
		try{
			fixedPointIteration.solveByFixedPoint();
		}catch(AccuracyException e){
			System.out.println(e);
		}
	}

	//make matrix satisfy the diagonal condition
	private void MatrixToDiagCond() throws IncorrectMatrixException{
		ArrayList<Integer> positions = new ArrayList<Integer>();
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
            if (positions.contains(num)) {
				throw new IncorrectMatrixException("Incorrect matrix");
            }
            good[num] = matrix[i];
            positions.add(num);
            maxi = 0;
            num = 0;
        }
        matrix = good;
	}

	//make sure that the matrix satisfy the diagonal condition
	public boolean isDiagCond(){
		double diag = 0;
		double other = 0;
		boolean ok = true;
		for (int i = 0; i<size; i++){
			diag =matrix[i][i];
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

		while(true){
			System.out.println("Input main matrix size");
			size = scanner.nextInt();
			if (size >= 1) {break;}
			System.out.println("Incorect size");
		}

		matrix = new double[size][size + 1];

		System.out.println("Input matrix " + (size*size+size) +" numbers");
		for (int i = 0; i < size; i++) { 
			for (int j = 0; j < size + 1; j++) {
				matrix[i][j] = scanner.nextDouble();
			}
		}
		System.out.println("Input accuracy");
		eps = scanner.nextDouble();
		scanner.close();
	}

	//solve the System of linear equations
	private void solveByFixedPoint() throws AccuracyException{
		if (eps <= 0){
			throw new AccuracyException();
		}
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
		announceResults(previousVariableValues, error);
	}

	private void announceResults(double[] previousVariableValues, double error){
		printWriter.print("Result:"+"\n");
		for (int i = 0; i < size; i++) { 
			printWriter.print("x"+(i+1)+" = "+
				previousVariableValues[i]+"±"
				+error+"\n");
		}
		printWriter.print(errorList.size()+" iterations"+"\n");
		printWriter.print("Error vector: \n");
        for (int i = 0; i < errorList.size(); i++) {
			printWriter.print(errorList.get(i)+"\n");
		}
		printWriter.close(); 
	}
}
