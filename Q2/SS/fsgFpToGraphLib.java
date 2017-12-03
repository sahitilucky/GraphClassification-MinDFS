

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class fsgFpToGraphLib {

	public void convert(String fsgfp, String G1, ArrayList<String> Ele)
			throws IOException {

		System.out.println("converting " + fsgfp + " to " + G1
				+ " using fsgfptographlib");
		FileReader file = new FileReader(fsgfp);
		FileReader file2 = new FileReader(fsgfp);
		BufferedReader filereader = new BufferedReader(file);
		BufferedReader filereader2 = new BufferedReader(file2);
		String line = filereader.readLine();
		String line2 = filereader2.readLine();
		PrintWriter writer = new PrintWriter(G1);
		int noofnodes = -1, noofedges = -1;
		int noofgraphs = 0;
		String[] words;
		while (line != null) {			
//			System.out.println("line = " + line);
			switch (line.charAt(0)) {
			case 'u':
				if (noofedges >= 0) {
					writer.println(noofedges);
					noofedges = -1;
				}
				writer.println(line.substring(2));
				break;
			case 'v':
				words = line.split(" ");
				writer.println(Ele.indexOf(words[words.length - 1]));
				break;
			case 't':
				noofgraphs++;
				writer.println("#");
				noofnodes = noofedges = 0;
				line2 = filereader2.readLine();
				while (line2.charAt(0) == 'v') {
					noofnodes++;
					line2 = filereader2.readLine();
				}
				while (line2 != null && !line2.equals("")) {
		//			System.out.println("line 2 : " + line2 + " size = " + line2.length());
					if (line2.charAt(0) == 'u') {
						noofedges++;
						line2 = filereader2.readLine();
					} else {
		//				System.out.println("breaking");
						break;
					}
				}
				writer.println(noofnodes);
				break;
			case '#':
				line2 = filereader2.readLine();
		//		System.out.println("line2 : " + line2);
				break;
			}
			line = filereader.readLine();
		}
		writer.close();
		System.out.println(noofgraphs);
		filereader.close();
		filereader2.close();
	}
}
