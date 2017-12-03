
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class NewSVM {
	public void Doclassification(String inputfile,String inputfile2) throws IOException, InterruptedException{
		System.out.println("doing this");
		String command="./svm-train "+inputfile+" model.txt" ;
		runFsg(command, "dump1.txt");
		command="./svm-train "+inputfile2+" model2.txt" ;
		runFsg(command, "dump2.txt");
	//	
		//	Process process = Runtime.getRuntime().exec("sh runsvm.sh "+inputfile);
		/*
		Process process = Runtime.getRuntime().exec("./svm-train "+inputfile+" model.txt" );
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		*/
		System.out.println("completed this");
	}
	
	public ArrayList<Integer> Classify(String testfile,String testfile2) throws IOException, InterruptedException {
		Process process =   Runtime.getRuntime().exec("chmod 777 svm-predict");
		process.waitFor();
		
		String command="./svm-predict "+testfile+" model.txt"+" output1.txt" ;
		runFsg(command, "dump.txt");
		command="./svm-predict "+testfile2+" model2.txt"+" output2.txt" ;
		runFsg(command, "dump.txt");
			
		FileReader file = new FileReader("output1.txt");
		BufferedReader bf = new BufferedReader(file);
		FileReader file2 = new FileReader("output2.txt");
		BufferedReader bf2 = new BufferedReader(file2);
		
		String line=bf.readLine();
		String line2 =bf2.readLine();
		ArrayList<Integer> predictedlabels = new ArrayList<Integer>();
		while(line!=null){
			if(Integer.parseInt(line)==1){
				predictedlabels.add(Integer.parseInt(line2));
			}else{
				predictedlabels.add(Integer.parseInt(line));
			}
			line=bf.readLine();
			line2=bf2.readLine();
		}
		return predictedlabels;
	}
	/*
	public static void main (String[] args) throws IOException, InterruptedException {
		NewSVM n = new NewSVM();
		n.Doclassification("TrainSVM.txt");
		System.out.println("completed learning");
		ArrayList<Integer> predictedlabels=n.Classify("TestSVM.txt");
		System.out.println(predictedlabels);
	}
	*/
	public void runFsg(String command, String outfile) throws IOException, InterruptedException {
		Process process =   Runtime.getRuntime().exec("chmod 777 svm-train");
		process.waitFor();
		
		process = Runtime.getRuntime().exec(command);
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		PrintWriter fsgActive = new PrintWriter(outfile);
		while ((line = input.readLine()) != null) {
			System.out.println(line);
			fsgActive.println(line);
		}
		fsgActive.close();
		System.out.println("svm done");

	}
	
	
}
