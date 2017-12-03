




import ctree.index.*;
import ctree.alg.Ullmann2;
import ctree.graph.*;
import ctree.lgraph.LGraph;
import ctree.lgraph.LGraphFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DataSplitting {

	ArrayList<String> elements = new ArrayList<String>();

	public DataSplitting(String[] args) throws Exception {
		go(args);
	}

	public void go(String[] args) throws Exception {
		FileReader inputfile = new FileReader(args[0]); // all graphs
		FileReader activefile = new FileReader(args[1]); // active graph ids
		FileReader inactivefile = new FileReader(args[2]); // inactive graph ids
		FileReader testfile = new FileReader(args[3]); // inactive graph ids

		BufferedReader inputfileReader = new BufferedReader(inputfile);
		BufferedReader activefileReader = new BufferedReader(activefile);
		BufferedReader inactivefileReader = new BufferedReader(inactivefile);
		BufferedReader testfilereader=new BufferedReader(testfile);
		double time1=System.currentTimeMillis();
		ArrayList<Integer> activeGraphs = new ArrayList<Integer>();
		String line = activefileReader.readLine();
		while (line != null) {
			activeGraphs.add(Integer.parseInt(line));
			line = activefileReader.readLine();
		}

		activefileReader.close();

		ArrayList<Integer> inactiveGraphs = new ArrayList<Integer>();
		line = inactivefileReader.readLine();
		while (line != null) {
			inactiveGraphs.add(Integer.parseInt(line));
			line = inactivefileReader.readLine();
		}

		inactivefileReader.close();

		PrintWriter writerTrainActive = new PrintWriter("Train_A_"
				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".g");
		PrintWriter writerTrainInactive = new PrintWriter("Train_I_"
				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".g");
		PrintWriter FinalTest = new PrintWriter(args[3].substring(0, args[3].lastIndexOf(".")) + ".g");
		
		PrintWriter writerNoClass = new PrintWriter("noclass_"
				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".g");
		ArrayList<Integer> actualLabels = new ArrayList<Integer>();

		line = inputfileReader.readLine();
		System.out.println(line);
		int ID, noofgraphs = 0, noofActiveTrainGraphs = 0, noofInactiveTrainGraphs = 0;
		int noofActiveTestGraphs = 0, noofInactiveTestGraphs = 0;
		while (line != null) {
			noofgraphs++;
			System.out.println(noofgraphs + " " + line);
			ID = Integer.parseInt(line.substring(1));
			if (activeGraphs.contains(ID)) {
					noofActiveTrainGraphs++;
					writerTrainActive.println("t # " + line.substring(1));
					write(writerTrainActive, inputfileReader);
			} else if (inactiveGraphs.contains(ID)) {
					noofInactiveTrainGraphs++;
					writerTrainInactive.println("t # " + line.substring(1));
					write(writerTrainInactive, inputfileReader);
			
			} else {
				writerNoClass.println("t # " + line.substring(1));
				write(writerNoClass, inputfileReader);
			}
			line = inputfileReader.readLine();
			
		}
		
		line=testfilereader.readLine();
		while(line!=null){
			FinalTest.println("t # " + line.substring(1));
			write(FinalTest, testfilereader);
			line=testfilereader.readLine();
					
		}
		System.out.println("Total noofgraphs = " + noofgraphs);
		System.out
				.println("no_of_active_Train_graphs " + noofActiveTrainGraphs);
		System.out.println("no of inactiveTrain_graphs"
				+ noofInactiveTrainGraphs);
		System.out.println("no_of_active_Test_graphs " + noofActiveTestGraphs);
		System.out.println("no of inactive test_graphs"
				+ noofInactiveTestGraphs);

		writerTrainActive.close();
		writerTrainInactive.close();
		writerNoClass.close();
		
//		Double supportA = 20.0;
//		
//		System.out.println("gonna do fsg for train A");
//		String command = "./fsg -s " + supportA + " " + "Train_A_"
//				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".g -t -p";
//		runFsg(command, "fsg_active_" + args[0]);
//		Double supportI = 25.0;
//		
//		System.out.println("gonna do fsg for train I");
//		command = "./fsg -s " + supportI + " " + "Train_I_"
//				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".g -t -p";
//		runFsg(command, "fsg_inactive_" + args[0]);
//
//		// Now we convert this output of fsg*.fp to the G1.txt format using
//		// fsgFpToGraphLib.java
//
//		// / here sahiti will give me the subgraphs in the active compounds in a
//		// single file
//		// now i have to check frequency of the frequent subgraphs in the active
//		// molecules to
//		// that in the inactive molecules
//
//		FeatureExtractionSelection fes = new FeatureExtractionSelection();
//		fes.noofgraphs1 = noofActiveTrainGraphs;
//		fes.noofgraphs2 = noofInactiveTrainGraphs;
//
//		
//		System.out.println("gonna do feature extraction");
//		fes.featureextract(
//				"Train_A_" + args[0].substring(0, args[0].lastIndexOf("."))
//						+ ".tid",
//				"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
//						+ ".tid");
//		// this will write frequent subgraphs from both classes in arraylist of
//		// arraylist graphrep
//
//		// sahiti
//
//		int numActivefeatures = fes.positivepatterncount;
//		int numInactivefeatures = fes.negativepatterncount;
//
//		System.out.println("gonna find closed patterns");
//		fes.findclosedpatterns(
//				"Train_A_" + args[0].substring(0, args[0].lastIndexOf("."))
//						+ ".pc",
//				"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
//						+ ".pc");
//		
//		System.out.println("gonna remove redundant patterns");
//		fes.removeredundantpatterns(fes.closedornot);
//		ArrayList<Integer> closedActiveOrNot = fes.getclosedpatterns(1);
//		ArrayList<Integer> closedInactiveOrNot = fes.getclosedpatterns(0);
		
		
		Double supportA = 20.0;
		Double supportI = 30.0;
		int numActivefeatures = 0 , numInactivefeatures = 0;
		ArrayList<Integer> closedActiveOrNot = null ;
		ArrayList<Integer> closedInactiveOrNot = null;
		FeatureExtractionSelection fes = null ;

		// now we know size of train and test in the folds.
		int maxSubGraphIsomorphisms = 400 * 45000;
		boolean inc = false, dec = false;
		int fsgcount=0;
		while (true) {
			if(fsgcount>3){
				break;
			}
			fsgcount++;
			System.out.println("supportA " + supportA + " SupportI " + supportI );
			System.out.println("gonna do fsg for train A");
			String command = "./fsg -s " + supportA + " " + "Train_A_"
					+ args[0].substring(0, args[0].lastIndexOf("."))
					+ ".g -t -p";
			runFsg(command, "fsg_active_" + args[0]);

			System.out.println("gonna do fsg for train I");
			command = "./fsg -s " + supportI + " " + "Train_I_"
					+ args[0].substring(0, args[0].lastIndexOf("."))
					+ ".g -t -p";
			runFsg(command, "fsg_inactive_" + args[0]);

			fes = new FeatureExtractionSelection();
			fes.noofgraphs1 = noofActiveTrainGraphs;
			fes.noofgraphs2 = noofInactiveTrainGraphs;

			System.out.println("gonna do feature extraction");
			fes.featureextract(
					"Train_A_" + args[0].substring(0, args[0].lastIndexOf("."))
							+ ".tid",
					"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
							+ ".tid");
			// this will write frequent subgraphs from both classes in arraylist
			// of
			// arraylist graphrep

			// sahiti

			numActivefeatures = fes.positivepatterncount;
			numInactivefeatures = fes.negativepatterncount;

			System.out.println("gonna find closed patterns");
			fes.findclosedpatterns(
					"Train_A_" + args[0].substring(0, args[0].lastIndexOf("."))
							+ ".pc",
					"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
							+ ".pc");

			System.out.println("gonna remove redundant patterns");
			fes.removeredundantpatterns(fes.closedornot);
			closedActiveOrNot = fes.getclosedpatterns(1);
			closedInactiveOrNot = fes.getclosedpatterns(0);

			int numAfeatures = 0, numIfeatures = 0;
			for (int f = 0; f < closedActiveOrNot.size(); f++)
				if (closedActiveOrNot.get(f) == 1)
					numAfeatures++;

			System.out.println("numclosedactivefeatures : " + numAfeatures);
			for (int f = 0; f < closedInactiveOrNot.size(); f++)
				if (closedInactiveOrNot.get(f) == 1)
					numIfeatures++;
			
			System.out.println("numclosedinactivefeatures : " + numIfeatures);
			int numfet = numAfeatures*noofInactiveTrainGraphs + numInactivefeatures*noofActiveTrainGraphs;
			System.out.println("target : " + maxSubGraphIsomorphisms);
			System.out.println("current : " + numfet);
			if (numfet < 1.1 * maxSubGraphIsomorphisms
					&& numfet > 0.9 * maxSubGraphIsomorphisms)
				break;

			if (numfet > 1.1 * maxSubGraphIsomorphisms ) {
				if (dec == true)
					break;
				supportA += numIfeatures*5.0/(numAfeatures + numIfeatures);
				supportI += numAfeatures*5.0/(numAfeatures + numIfeatures);
				inc = true;
				dec = false;
			}
			
			else {
				if (inc == true)
					break;
				supportA -= numIfeatures*5.0/numfet;
				supportI -= numAfeatures*5.0/numfet;
				inc = false;
				dec = true;
			}
		}

		ArrayList<String> closedActivePatternName = new ArrayList<String>(fes.patterns
				.subList(0, fes.positivepatterncount));
		ArrayList<String> closedInactivePatternName = new ArrayList<String>(fes.patterns
				.subList(fes.positivepatterncount, fes.positivepatterncount
						+ fes.negativepatterncount));

		
		System.out.println("activesie:"+closedActivePatternName.size()+" inactivesize:"+closedInactivePatternName.size());
		fsgFpToGraphLib fsgfptographlib = new fsgFpToGraphLib();
		String featureFileA = "Train_A_"
				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".fp";
		// the output file with the active features

		fsgfptographlib.convert(featureFileA, "graphlib1.txt", elements);
		fsgfptographlib.convert(
				"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
						+ ".g", "graphlib2.txt", elements);
		PrintWriter writerTid = new PrintWriter(
				"InactiveTidForActiveFeatures.txt");

		
		System.out.println("writing into required fes format");
		System.out.println("Printing in Datasplitting : numActivefeatures"+numActivefeatures);
		System.out.println("Printing in Datasplitting : numInActivefeatures"+noofInactiveTrainGraphs);
		System.out.println("Printing in Datasplitting : closedActiveOrNot"+closedActiveOrNot);
		System.out.println("Printing in Datasplitting : closedInActiveOrNot"+closedInactiveOrNot);
		LGraph[] Lgraph1=LGraphFile.loadLGraphs("graphlib1.txt");
		LGraph[] Lgraph2=LGraphFile.loadLGraphs("graphlib2.txt");
		
		for (int i = 0, j = 0; i < numActivefeatures; i++) {
			if (closedActiveOrNot.get(i) == 0)
				continue;
			if(i%100==0)
				System.out.println("DOne till :"+i);
			writerTid.print(closedActivePatternName.get(j) + " "); // here write
																	// what tid
																	// list
																	// sahiti
																	// gives
			for (int inTr = 0; inTr < noofInactiveTrainGraphs; inTr++) 
			{
					if (checkSubgraphs(Lgraph1, Lgraph2, i,
							inTr) == 1)
						writerTid.print(inTr + " ");

			}
			writerTid.println();
			
			j++;
		}

		writerTid.close();

		// ///////////////////////////////////////////////////////

		String featureFileB = "Train_I_"
				+ args[0].substring(0, args[0].lastIndexOf(".")) + ".fp";
		// the output file with the active features

		fsgfptographlib.convert(featureFileB, "graphlib3.txt", elements);
		fsgfptographlib.convert(
				"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
						+ ".g", "graphlib4.txt", elements);
		writerTid = new PrintWriter("ActiveTidForInactiveFeatures.txt");
		
		System.out.println("writing into fes format");
		Lgraph1=LGraphFile.loadLGraphs("graphlib3.txt");
		Lgraph2=LGraphFile.loadLGraphs("graphlib4.txt");
	
		for (int i = 0, j = 0; i < numInactivefeatures; i++) {
			if (closedInactiveOrNot.get(i) == 0)
				continue;
			writerTid.print(closedInactivePatternName.get(j) + " "); // here write
																	// what tid
																	// list
																	// sahiti
																	// gives
			for (int inTr = 0; inTr < noofActiveTrainGraphs; inTr++) {
				
					if (checkSubgraphs(Lgraph1,Lgraph2, i,
							inTr) == 1)
						writerTid.print(inTr + " ");
			}
			writerTid.println();
			j++;
		}

		writerTid.close();
		fes.Preprocessingfromotherside("InactiveTidForActiveFeatures.txt", "ActiveTidForInactiveFeatures.txt");
		System.out.println("gonna remve non confident patterns");
		fes.removenonconfidentpatterns();
		
		fes.fillgraphreps("Train_A_" + args[0].substring(0, args[0].lastIndexOf("."))
				+ ".tid",
		"Train_I_" + args[0].substring(0, args[0].lastIndexOf("."))
				+ ".tid" );
		fes.Processingfromotherside("InactiveTidForActiveFeatures.txt", "ActiveTidForInactiveFeatures.txt");
		fes.outputtofile("TrainSVM.txt","TrainSVM2.txt");
		
		NewSVM svm = new NewSVM();
		System.out.println("gonna do svm output to file");
		
		System.out.println("gonna train svm classification");
		svm.Doclassification("TrainSVM.txt","TrainSVM2.txt");
		int noOfTestCases = noofActiveTestGraphs + noofInactiveTestGraphs;
		PrintWriter writerCord = new PrintWriter("TestSVM.txt");
		PrintWriter writerCord2 = new PrintWriter("TestSVM2.txt");
		fsgfptographlib.convert(args[3].substring(0, args[3].lastIndexOf(".")) + ".g", "graphlib5.txt", elements);
		Lgraph1=LGraphFile.loadLGraphs("graphlib1.txt");
		Lgraph2=LGraphFile.loadLGraphs("graphlib5.txt");
		closedActiveOrNot = fes.getclosedpatterns(1);
		closedInactiveOrNot = fes.getclosedpatterns(0);
		LGraph[] Lgraph3=LGraphFile.loadLGraphs("graphlib3.txt");
		int j=0;
		for (int testNo = 0; testNo < noOfTestCases; testNo++) {
			writerCord.print("1 ");
			writerCord2.print("1 ");
			j=0;
			for (int i = 0; i < numActivefeatures; i++) {
				if (closedActiveOrNot.get(i) == 0)
					continue;
				writerCord.print(j+":"+checkSubgraphs(Lgraph1,Lgraph2, i, testNo)+" ");
				writerCord2.print(j+":"+checkSubgraphs(Lgraph1,Lgraph2, i, testNo)+" ");
				
				j++;
				
			}

			for (int i = 0; i < numInactivefeatures; i++) {
				if (closedInactiveOrNot.get(i) == 0)
					continue;
				writerCord.print(j+":"+checkSubgraphs(Lgraph3, Lgraph2, i, testNo)+" ");
				j++;
			}
			writerCord.println();
			writerCord2.println();
		}
		System.out.println("numofattributes in test:"+j);
		writerCord.close();
		writerCord2.close();
		F1_score_harmonic f1 = new F1_score_harmonic();
		System.out.println("gonna do svm classification on test");
		ArrayList<Integer> predictedlabels=svm.Classify("TestSVM.txt","TestSVM2.txt");
		Integer[] pred_labels = new Integer[predictedlabels.size()];
		pred_labels = predictedlabels.toArray(pred_labels);
		f1.predicted_labels = pred_labels;
		System.out.println(predictedlabels);
		FileWriter testfilewriter=new FileWriter("output.txt");
		BufferedWriter filewriter=new BufferedWriter(testfilewriter);
		for(int i=0;i<predictedlabels.size();i++){
			testfilewriter.write(predictedlabels.get(i));
			testfilewriter.write("\n");
		}
		double time2 = System.currentTimeMillis();
	    double duration=(time2-time1)/1000;
	    System.out.println("Generation time = " + duration + 
				" seconds");
	}

	public int checkSubgraphs(LGraph[] lgraph1, LGraph[] lgraph2, int a, int b)
			throws IOException {
		//System.out.println(a+" "+b+" "+str1+" "+str2);
		Graph query1 = lgraph1[a];
		Graph query2 = lgraph2[b];

		// This uses Ullman algo. guaranteed correctness. includes edge labels.
		// Slowest.
		int[][] B = Util.getBipartiteMatrix2(query1, query2);
		int[] map = Ullmann2.subgraphIsomorphism(Ullmann2.getAdjMat(query1),
				Ullmann2.getAdjMat(query2), B);
		// System.out.println("Ullmann algo (including edge labels)");
		if (map == null)
			return 0;
		else
			return 1;
	}

	public void runFsg(String command, String outfile) throws IOException {

		Process process = Runtime.getRuntime().exec(command);
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		PrintWriter fsgActive = new PrintWriter(outfile);
		while ((line = input.readLine()) != null) {
			// System.out.println(line);
			fsgActive.println(line);
		}
		fsgActive.close();
	}

	public void write(PrintWriter writer, BufferedReader reader)
			throws IOException {

		String line;
		line = reader.readLine();
		int noofnodes;
		noofnodes = Integer.parseInt(line);
		for (int i = 0; i < noofnodes; i++) {
			line = reader.readLine();
			writer.println("v " + i + " " + line);
			if (!elements.contains(line))
				elements.add(line);
		}

		line = reader.readLine();
		noofnodes = Integer.parseInt(line);
		for (int i = 0; i < noofnodes; i++) {
			line = reader.readLine();
			writer.println("u " + line); // u for fsg , e for gspan and
			// gaston
		}
	}

	public static void main(String[] args) throws Exception {

		//String[] hc = { "aido99_all.txt", "ca.txt", "ci.txt", "blah.txt" };
		DataSplitting ds = new DataSplitting(args);
	}
}
