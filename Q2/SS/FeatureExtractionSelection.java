

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class FeatureExtractionSelection {
	ArrayList<Integer> patternstopatternid=new ArrayList<Integer>();
	ArrayList<Integer> graphids=new ArrayList<Integer>(); 
	ArrayList<ArrayList<Integer>> graphreps=new ArrayList<ArrayList<Integer>>();
	ArrayList<Integer> patternids=new ArrayList<Integer>();
	ArrayList<Integer> positivecounts=new ArrayList<Integer>();
	ArrayList<Integer> negativecounts=new ArrayList<Integer>();
	ArrayList<String> patterns=new ArrayList<String>();
	ArrayList<Integer> Removedlistcount=new ArrayList<Integer>();
	int positivepatterncount=0;
	int negativepatterncount=0;
	int maxgraphid1=0;
	int maxgraphid2=0;
	int noofgraphs1;
	int noofgraphs2;
	public ArrayList<Integer> closedornot;
	public ArrayList<Integer> confidentornot;
	private int actualpositivepatterncount;
	private int actualnegativepatterncount;
	int positivefpedges;
	public void featureextract(String inputfile,String inputfile2) throws IOException{
		FileReader file=new FileReader(inputfile);
		BufferedReader filereader=new BufferedReader(file);
		FileReader file2=new FileReader(inputfile2);
		BufferedReader filereader2=new BufferedReader(file2);
		
		String line=filereader.readLine();
		String[] items;
		int i;
		int graphcount=graphreps.size();
		patternstopatternid.add(0);
		int numedgestillnow=1;
		positivepatterncount=0;
		while(line!=null){
			
			items=line.split(" ");
			patterns.add(items[0]);
			patternids.add(positivepatterncount);
			if(Integer.parseInt(items[items.length-1])>maxgraphid1){
				maxgraphid1=Integer.parseInt(items[items.length-1]);
			}
			String[] items2=items[0].split("-");
			if(Integer.parseInt(items2[0])!=numedgestillnow){
				numedgestillnow=Integer.parseInt(items2[0]);
				patternstopatternid.add(positivepatterncount);
			}
			positivecounts.add(items.length-1);
			negativecounts.add(0);
			positivepatterncount++;
			line=filereader.readLine();
		}
		positivefpedges=numedgestillnow;
		System.out.println("file reading of positive pattern is completed");
		negativepatterncount=0;
		line=filereader2.readLine();
		
		patternstopatternid.add(positivepatterncount);
		numedgestillnow=1;
		while(line!=null){
			items=line.split(" ");
			patterns.add(items[0]);
			patternids.add(negativepatterncount);
			if(Integer.parseInt(items[items.length-1])>maxgraphid2){
				maxgraphid2=Integer.parseInt(items[items.length-1]);
			}
			String[] items2=items[0].split("-");
			if(Integer.parseInt(items2[0])!=numedgestillnow){
				numedgestillnow=Integer.parseInt(items2[0]);
				patternstopatternid.add(positivepatterncount+negativepatterncount);
			}
			positivecounts.add(0);
			negativecounts.add(items.length-1);
			negativepatterncount++;
			line=filereader2.readLine();
		}
		System.out.println(patternstopatternid.size());
		maxgraphid1=noofgraphs1-1;
		maxgraphid2=noofgraphs2-1;
		for(i=0;i<positivepatterncount+negativepatterncount;i++){
			Removedlistcount.add(0);
		}
		actualnegativepatterncount=negativepatterncount;
		actualpositivepatterncount=positivepatterncount;
		System.out.println("printing my faith");
		System.out.println(patternstopatternid);
	}
	
	public void fillgraphreps(String inputfile,String inputfile2) throws IOException{
		FileReader file=new FileReader(inputfile);
		BufferedReader filereader=new BufferedReader(file);
		FileReader file2=new FileReader(inputfile2);
		BufferedReader filereader2=new BufferedReader(file2);
		
		
		String line=filereader.readLine();
		String[] items;
		int i;

		for(i=0;i<maxgraphid1+1;i++){
			graphreps.add(new ArrayList<Integer>());
		}
		for(i=0;i<maxgraphid2+1;i++){
			graphreps.add(new ArrayList<Integer>());
		}
		int totalgraphs=maxgraphid1+maxgraphid2+2;
		System.out.println("file reading of negative pattern is completed");
		file.close();
		file2.close();
		file=new FileReader(inputfile);
		filereader=new BufferedReader(file);
		line=filereader.readLine();
		ArrayList<Integer> intitems = new ArrayList<Integer>();
		int count=0;
		int numofattributesingraphs=0;
		while(line!=null){
			
			if(closedornot.get(count)==1 ){
				items=line.split(" ");
				intitems=new ArrayList<Integer>();
				for(i=1;i<items.length;i++){
					intitems.add(Integer.parseInt(items[i]));
				}
				ArrayList<Integer> flags=new ArrayList<Integer>();
				for(i=0;i<totalgraphs;i++){
					flags.add(0);
				}
				for(i=0;i<intitems.size();i++){
					graphreps.get(intitems.get(i)).add(1);
					flags.set(intitems.get(i),1);
				}
				for(i=0;i<totalgraphs;i++){
					if(flags.get(i)==0){
						graphreps.get(i).add(0);
					}
				}
				numofattributesingraphs++;
			}
			count++;
			line=filereader.readLine();
			
		}
		System.out.println("noofpositive patterns: "+count);
		System.out.println("file reading of positive pattern1 is completed");
		file2=new FileReader(inputfile2);
		filereader2=new BufferedReader(file2);
		line=filereader2.readLine();
									
		while(line!=null){
			if(closedornot.get(count)==1){
				items=line.split(" ");
				intitems = new ArrayList<Integer>();  //items in int format ,graph ids in int format
				for(i=1;i<items.length;i++){
					intitems.add(Integer.parseInt(items[i]));
					
				}
				
				ArrayList<Integer> flags=new ArrayList<Integer>();
				for(i=0;i<totalgraphs;i++){
					flags.add(0);
				}
				for(i=0;i<intitems.size();i++){
					graphreps.get(intitems.get(i)+maxgraphid1+1).add(1);
					flags.set(intitems.get(i)+maxgraphid1+1,1);
				}
				for(i=0;i<totalgraphs;i++){
					if(flags.get(i)==0){
						graphreps.get(i).add(0);
					}
				}
				numofattributesingraphs++;
			}
			line=filereader2.readLine();
			count++;
		}
		System.out.println("file reading of negative pattern1 is completed");
		System.out.println("Graph 0 size "+graphreps.get(0).size());
		System.out.println("Grpah 10 size"+graphreps.get(10).size());
		System.out.println("noofattributes in graphs:"+numofattributesingraphs);
	}
	
	public void findclosedpatterns(String inputfile1,String inputfile2) throws IOException{
		FileReader file1=new FileReader(inputfile1);
		BufferedReader filereader1=new BufferedReader(file1);
		String line=filereader1.readLine();
		String[] items;
		int i;
		int len1=positivepatterncount+negativepatterncount;
		closedornot=new ArrayList<Integer>();
		for(i=0;i<len1;i++){
			closedornot.add(1);
		}
		while(line!=null){
			
			items=line.split(" ");
			int parentcount=positivecounts.get(patterntopatternid(items[0],0));
			for(i=1;i<items.length;i++){
				if(positivecounts.get(patterntopatternid(items[i],0)) ==parentcount){
					closedornot.set(patterntopatternid(items[i],0),0);
				}
				
			}
			line=filereader1.readLine();
		}
		System.out.println("closed pattern found half way");
		FileReader file2=new FileReader(inputfile2);
		BufferedReader filereader2=new BufferedReader(file2);
		line=filereader2.readLine();
		while(line!=null){
			
			items=line.split(" ");
			int parentcount=negativecounts.get(patterntopatternid(items[0],1));
			for(i=1;i<items.length;i++){
				if(negativecounts.get(patterntopatternid(items[i],1)) ==parentcount){
					closedornot.set(patterntopatternid(items[i],1),0);
					System.out.println("coming even once");
				}
			}
			line=filereader2.readLine();
		}
		System.out.println("closed pattern completed...");
	}
	
	public ArrayList<Integer> getclosedpatterns(int flag){
		if(flag==1){
			return new ArrayList<Integer>(closedornot.subList(0,actualpositivepatterncount ));
		}
		else{
			return new ArrayList<Integer>(closedornot.subList(actualpositivepatterncount, actualpositivepatterncount+actualnegativepatterncount));
		}
	}
	
	public void Preprocessingfromotherside(String inputfile1,String inputfile2) throws IOException{
		FileReader file=new FileReader(inputfile1);
		BufferedReader filereader=new BufferedReader(file);
		FileReader file2=new FileReader(inputfile2);
		BufferedReader filereader2=new BufferedReader(file2);
		//inputfile is positive fps and negative graphs
		int totalgraphs=maxgraphid1+maxgraphid2+2;
		String line=filereader.readLine();
		String[] items;
		int i;
		System.out.println("processing from other side");
		ArrayList<Integer> intitems = new ArrayList<Integer>();		//items in int format ,graph ids in int format
		int count=0;
		while(line!=null){
			items=line.split(" ");
			int index=patterntopatternid(items[0], 0);
		//	System.out.println("Indexbyme: "+index+" "+count);
			negativecounts.set(index,items.length-1);
			line=filereader.readLine();
			count++;
		}
		
		line=filereader2.readLine();
		//inputfile 2 is negative fps and positive graphs
		
		intitems = new ArrayList<Integer>();		//items in int format ,graph ids in int format
		while(line!=null){
			items=line.split(" ");
			int index=patterntopatternid(items[0], 1);
		//	System.out.println("Indexbyme: "+index+" "+count);
			positivecounts.set(index,items.length-1);
			line=filereader2.readLine();
			count++;
		}
		
	} 

	public void Processingfromotherside(String inputfile1,String inputfile2) throws IOException{
		FileReader file=new FileReader(inputfile1);
		BufferedReader filereader=new BufferedReader(file);
		FileReader file2=new FileReader(inputfile2);
		BufferedReader filereader2=new BufferedReader(file2);
		//inputfile is positive fps and negative graphs
		int totalgraphs=maxgraphid1+maxgraphid2+2;
		String line;
		String[] items;
		int i;
		System.out.println("processing from other side");
		ArrayList<Integer> intitems = new ArrayList<Integer>();		//items in int format ,graph ids in int format
		int count=0;
		int index=0;
		int j;
		for(j=0;j<actualpositivepatterncount;j++){
			if(closedornot.get(j)==1){
				line=filereader.readLine();
				items=line.split(" ");
				//int index=patterntopatternid(items[0], 0);
		//		System.out.println("Index:"+index+"Pattern:"+items[0]+"Indexbyme:"+patterntopatternid(items[0], 0));
				intitems=new ArrayList<Integer>();
				for(i=1;i<items.length;i++){
					intitems.add(Integer.parseInt(items[i]));
				}
				int len=intitems.size();
				for(i=0;i<len;i++){
					graphreps.get(intitems.get(i)+maxgraphid1+1).set(index,1);
				}	
				if(index==negativecounts.size()){	//Error case
					System.out.println(items[0]);
					int x=patternstopatternid.get(Integer.parseInt(items[0])-1);
					System.out.println(x+" "+Removedlistcount.get(x+Integer.parseInt(items[1])));
				}
				//negativecounts.set(index,items.length-1);
				index++;
			}
			
			count++;
		}
		line=filereader.readLine();
		if(line==null){System.out.println("Yey completed all");}
		
		//inputfile 2 is negative fps and positive graphs
		
		intitems = new ArrayList<Integer>();		//items in int format ,graph ids in int format
		for(j=actualpositivepatterncount;j<actualpositivepatterncount+actualnegativepatterncount;j++){
			if(closedornot.get(j)==1){
				line=filereader2.readLine();
				items=line.split(" ");
				
				//int index=patterntopatternid(items[0], 1);
				//System.out.println("Index:"+index+"Pattern: "+items[0]+"Indexbyme:"+patterntopatternid(items[0], 1));
				
				intitems=new ArrayList<Integer>();
				for(i=1;i<items.length;i++){
					intitems.add(Integer.parseInt(items[i]));
				}
				int len=intitems.size();
				for(i=0;i<len;i++){
					graphreps.get(intitems.get(i)).set(index,1);
				}
				if(index==positivecounts.size()){
					System.out.println(items[0]);
					int x=patternstopatternid.get(positivefpedges+Integer.parseInt(items[0])-1);
					System.out.println(x+" "+Removedlistcount.get(x+Integer.parseInt(items[1])));
				}
				//positivecounts.set(index,items.length-1);
				index++;
			}
			
			count++;
		}
		line=filereader2.readLine();
		if(line==null){System.out.println("Yey completed all");}
		
		System.out.println("COunt:"+count+" Index:"+index);
		System.out.println("Graph 0 size:"+graphreps.get(0).size());
	}
	public void removeredundantpatterns(ArrayList<Integer> flaglist){
		int i,removed=0,index,removed1=0,removed2=0;
		System.out.println("redundant pattern started....");
		System.out.println("before removing nonclosed patterns:"+positivepatterncount+" "+negativepatterncount);
		for(i=0;i<positivepatterncount+ negativepatterncount;i++){
			Removedlistcount.set(i,Removedlistcount.get(i)+removed);
			
			if(flaglist.get(i)==0){
				if(i<positivepatterncount){
					removed1++;
				}
				else{
					removed2++;
				}
				//patternstopatternid.set(index,patternstopatternid.get(index+1)-1);
				//for(j=0;j<maxgraphid1+maxgraphid2+2;j++)
				//	graphreps.get(j).remove(i-removed);
				patterns.remove(i-removed);
				positivecounts.remove(i-removed);
				negativecounts.remove(i-removed);
				patternids.remove(i-removed);
				
				removed++;
			}
		
		}
		positivepatterncount-=removed1;
		negativepatterncount-=removed2;
		System.out.println("redundant pattern removed....");
		System.out.println("after removing nonclosed patterns:"+positivepatterncount+" "+negativepatterncount);
		System.out.println("numofattributes remiaining"+(flaglist.size()-removed));
		return;
	}
	public void removeredundantconfidentpatterns(ArrayList<Integer> flaglist){
		int i,removed=0,index,removed1=0,removed2=0,j=0;
		System.out.println("redundantconfident pattern started....");
		System.out.println("before removing nonconfident patterns:"+positivepatterncount+" "+negativepatterncount);
		ArrayList<Integer> NewRemovedlistcount=new ArrayList<Integer>();
		for(i=0;i<actualpositivepatterncount+actualnegativepatterncount;i++){
			NewRemovedlistcount.add(removed);
			if(closedornot.get(i)==1){
				if(flaglist.get(j)==0){
					closedornot.set(i,0);
					if(j<positivepatterncount){
						removed1++;
					}
					else{
						removed2++;
					}
					//patternstopatternid.set(index,patternstopatternid.get(index+1)-1);
					//for(j=0;j<maxgraphid1+maxgraphid2+2;j++)
					//	graphreps.get(j).remove(i-removed);
					patterns.remove(j-removed);
					positivecounts.remove(j-removed);
					negativecounts.remove(j-removed);
					patternids.remove(j-removed);
					
					removed++;
				}
				j++;
			}
		}
		if(j==flaglist.size()){
			
			System.out.println("Yeah covering all "+j+" "+confidentornot.size());
		}
		
		positivepatterncount-=removed1;
		negativepatterncount-=removed2;
		System.out.println("Removed list:"+Removedlistcount);
		System.out.println("NewRemoved list:"+NewRemovedlistcount);
		System.out.println(Removedlistcount.size());
		System.out.println(NewRemovedlistcount.size());
		for(i=0;i<Removedlistcount.size()-1;i++){
			Removedlistcount.set(i, Removedlistcount.get(i)+NewRemovedlistcount.get(i));
		}
		
		System.out.println("redundantconfident pattern removed....");
		System.out.println("after removing nonconfident patterns:"+positivepatterncount+" "+negativepatterncount);
		System.out.println("The list closedornot:"+closedornot);
		return;
	}
	//removing non confident patterns.
	public void removenonconfidentpatterns(){
		int i;
		System.out.println("redundant confident  pattern started....");
		confidentornot=new ArrayList<Integer>();
		double maxpositiveconfidence=0,maxnegativeconfidence=0;
		ArrayList<Double> positiveconfidences=new ArrayList<Double>();
		ArrayList<Double> negativeconfidences=new ArrayList<Double>();
		for(i=0;i<positivepatterncount+negativepatterncount;i++){
			double positivefraction=((double)positivecounts.get(i)/(double)(maxgraphid1+1));
			double negativefraction=((double)negativecounts.get(i)/(double)(maxgraphid2+1));
			double positiveconfidence=positivefraction/(positivefraction+negativefraction);
			double negativeconfidence=negativefraction/(positivefraction+negativefraction);
			if(maxpositiveconfidence<positiveconfidence){
				maxpositiveconfidence=positiveconfidence;
			}
			if(maxnegativeconfidence<negativeconfidence){
				maxnegativeconfidence=negativeconfidence;
			}
			positiveconfidences.add(positiveconfidence);
			negativeconfidences.add(negativeconfidence);
		}
		for(i=0;i<positivepatterncount+negativepatterncount;i++){
			if(positiveconfidences.get(i)-negativeconfidences.get(i) >0.04 
					|| positiveconfidences.get(i)-negativeconfidences.get(i)< -0.03){
				confidentornot.add(1);
			}
			else{
				confidentornot.add(0);
			}
		}
		System.out.println("positiveconfidences:");
		System.out.println(positiveconfidences);
		System.out.println("negativeconfidences:");
		System.out.println(negativeconfidences);
		
		removeredundantconfidentpatterns(confidentornot);
		System.out.println("confident pattern removed....");
	}
	
	private int  patterntopatternid(String pattern,int flag){
		String[] items=pattern.split("-");
		int x;
		if(flag==1){
			x=patternstopatternid.get(positivefpedges+Integer.parseInt(items[0])-1);
			
		}else{
			x=patternstopatternid.get(Integer.parseInt(items[0])-1);
		}
		int removed=Removedlistcount.get(x+Integer.parseInt(items[1]));
		//System.out.println(x+Integer.parseInt(items[1])+" "+removed);
		return ((x+Integer.parseInt(items[1]))-removed);
	} 
	
	public void outputtofile(String outputfile ,String outputfile2) throws IOException{
		FileWriter fileout=new FileWriter(outputfile);
		BufferedWriter filewriter=new BufferedWriter(fileout);
		FileWriter fileout2=new FileWriter(outputfile2);
		BufferedWriter filewriter2=new BufferedWriter(fileout2);
		
		int i,j;
		for(i=0;i<maxgraphid1+maxgraphid2+2;i++){
			if(i<maxgraphid1+1){
				filewriter.write("1 ");
			}
			else{
				filewriter.write("0 ");
			}
			for(j=0;j<graphreps.get(i).size();j++){
				filewriter.write(j+":"+graphreps.get(i).get(j)+" ");
			}	
			
			filewriter.write("\n");
		}
		for(i=0;i<maxgraphid1+maxgraphid2+2;i++){
			if(i<maxgraphid1+1){
				filewriter2.write("1 ");
			}
			else{
				filewriter2.write("0 ");
			}
			for(j=0;j<positivepatterncount;j++){
				filewriter2.write(j+":"+graphreps.get(i).get(j)+" ");
			}	
			
			filewriter2.write("\n");
		}
	
	}
	
}	
