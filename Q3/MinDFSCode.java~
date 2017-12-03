import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MinDFSCode {
	public static void main(String[] args) throws IOException {
		FileReader file = new FileReader(args[0]);//("aido99_all.txt");// ("aids12 problem.txt");
															// ("input3.txt");
		BufferedReader filereader = new BufferedReader(file);
		FileWriter fileout=new FileWriter("output.txt");
		BufferedWriter filewriter=new BufferedWriter(fileout);
		String line = filereader.readLine();

		// ArrayList<Graph> graphs=new ArrayList<Graph>();
		int ID;
		IsConnected ic = new IsConnected();
		PrintWriter writer1 = new PrintWriter("problem graphs 1.txt");
		PrintWriter writer2 = new PrintWriter("problem graphs 2.txt");
		int leastnodes = 500;
		int bestid = -1;
		double time1 = System.currentTimeMillis(); 
		
		while (line != null) {
			ID = Integer.parseInt(line.substring(1, line.length()));
	//		System.out.println("graph id : " + ID);
			Graph g = new Graph(ID);
			// graphs.add(g);
			g.read(filereader);
			ArrayList<DFSstruct> answer = g.getDFSCode();
			int i;
			if (answer == null) {
				
				writer1.println(ID);
				if (ic.isConnected(g) == false)
					;
				else {
					System.out.println("Again some error!!!!!! for graph : "+ID );
					writer2.println("null output for connected graph : #" + ID);
					if (g.noofnodes < leastnodes) {
						leastnodes = g.noofedges;
						bestid = g.ID;
					}
				}
			}
			else {
				
				for (i = 0; i < answer.size(); i++) {
					filewriter.write(answer.get(i).toString());
				}
				filewriter.write("\n");
			}
	//		System.out.println("###########################");
			line = filereader.readLine();

		}				// end of while loop
		writer1.close();
		writer2.close();
		filewriter.flush();
		filewriter.close();
		double time2 = System.currentTimeMillis();
	    double duration=(time2-time1)/1000;
	    System.out.println("Generation time = " + duration + 
				" seconds");
	    
	//	System.out.println("Best graph id : " + bestid + " with " + leastnodes + " no of edges");
	}

}
