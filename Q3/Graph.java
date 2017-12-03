import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

public class Graph {
	int ID;
	int noofnodes;
	ArrayList<Node> nodes;
	ArrayList<Edge> edges;
	ArrayList<ArrayList<Edge>> adjacencylist;
	ArrayList<String> nodelabels;
	int noofedges;
	ArrayList<Boolean> coverededges;

	class Edge implements Comparable<Edge> {
		int src, dest;
		String srcl, destl;
		int edgeweight;
		int edgeid;

		// boolean covered;
		public Edge(int a, int b, String c, String d, int e) {
			src = a;
			dest = b;
			srcl = c;
			destl = d;
			edgeweight = e;
			// covered=false;
		}

		@Override
		public int compareTo(Edge arg0) {
			// TODO Auto-generated method stub
			if (nodes.get(this.dest).covered) {
				if (!nodes.get(arg0.dest).covered) {
					return -1;
				} else {
					if (nodes.get(this.dest).index < nodes.get(arg0.dest).index) {
						return -1;
					}
					else if(nodes.get(this.dest).index < nodes.get(arg0.dest).index){
						return 0;
					}
					else {
						return 1;
					}
				}
			} else {
				if (nodes.get(arg0.dest).covered) {
					return 1;
				} else {
					if (this.edgeweight < arg0.edgeweight) {
						return -1;
					} else if (this.edgeweight > arg0.edgeweight) {
						return 1;
					} else {
						if (this.destl.compareTo(arg0.destl)<0) {
							return -1;
						}
						else if(this.destl.compareTo(arg0.destl)==0){
							return 0;
						}
						else {
							return 1;
						}
					}
				}
			}

		}
	}

	public Graph(int id) {
		ID = id;
	}

	public void read(BufferedReader filereader) throws NumberFormatException,
			IOException {
		nodelabels = new ArrayList<String>();
		noofnodes = Integer.parseInt(filereader.readLine());
		nodes = new ArrayList<Node>();
		adjacencylist = new ArrayList<ArrayList<Edge>>();
		for (int i = 0; i < noofnodes; i++) {
			String line = filereader.readLine();
			nodes.add(new Node(line));
			adjacencylist.add(new ArrayList<Edge>());
			nodelabels.add(line);
		}
		edges = new ArrayList<Edge>();
		noofedges = Integer.parseInt(filereader.readLine());
		String line;
		String[] items = new String[32];
		coverededges = new ArrayList<Boolean>();
		for (int i = 0; i < noofedges; i++) {
			line = filereader.readLine();
			items = line.split(" ");
			int source = Integer.parseInt(items[0]);
			int dest = Integer.parseInt(items[1]);
			int edgel = Integer.parseInt(items[2]);
			coverededges.add(false);
			Edge e = new Edge(source, dest, nodes.get(source).label,
					nodes.get(dest).label, edgel);
			Edge e1 = new Edge(dest, source, nodes.get(dest).label,
					nodes.get(source).label, edgel);
			e.edgeid = i;
			e1.edgeid = i;
			// edges.add(e);
			adjacencylist.get(source).add(e);
			adjacencylist.get(dest).add(e1);
		}
		return;
	}

	int index = 0;

	public ArrayList<DFSstruct> getDFSCode() {
		int i;
		ArrayList<ArrayList<ArrayList<DFSstruct>>> tripledfs = new ArrayList<ArrayList<ArrayList<DFSstruct>>>();
		for (i = 0; i < noofnodes; i++) {
			tripledfs.add(new ArrayList<ArrayList<DFSstruct>>());
		}
		int triplerecurdepth = 1;
		Collections.sort(nodelabels);
		for(i=0;i<noofnodes;i++){
			if(!nodes.get(i).label.equals(nodelabels.get(0))){
				nodes.get(i).canberoot=0;
			}
		}
		int minimumnode = 0;
		while (triplerecurdepth < (noofnodes+1)) {
		//	System.out.println("triplerecurdepth " + triplerecurdepth);
			for (i = 0; i < noofnodes; i++) {
				if (nodes.get(i).canberoot == 1) {
					
					doDFS1(triplerecurdepth - 1, triplerecurdepth,
							tripledfs.get(i), i, 0);
		//			System.out.println(tripledfs.get(i).get(0).size());
					
		//			System.out.print("after going");
		//			System.out.println(tripledfs.get(i).get(0));
				}
			}
			int noofcanberoots = 0;
			ArrayList<ArrayList<DFSstruct>> min = tripledfs.get(0);

			for (i = 0; i < noofnodes; i++) {
				
				if(nodes.get(i).canberoot!=0){
					//System.out.println("coming here");
				//	System.out.println("tripledfs.get(" + i+").size() : " + tripledfs.get(i).size() );
				//	System.out.println("size of min : " + min.size() );
					if (min.size() == 0) {
						
						min = tripledfs.get(i);
						minimumnode = i;
					}
					if (comparelists(tripledfs.get(i).get(0), min.get(0)) == 0) {
						if(tripledfs.get(i).get(0).size()<=min.get(0).size()){
							min = tripledfs.get(i);
							minimumnode = i;
						}
					} else if (comparelists(tripledfs.get(i).get(0), min.get(0)) < 0) {
						min = tripledfs.get(i);
						minimumnode = i;
					}
					else {
						nodes.get(i).canberoot = 0;
					}
				}
			}
			for (i = 0; i < noofnodes; i++) {
				if (nodes.get(i).canberoot != 0) {
						
					if (comparelists(tripledfs.get(i).get(0), min.get(0)) != 0) {
						nodes.get(i).canberoot = 0;
					} else {
						noofcanberoots++;
					}
				}
			}
			/*
			System.out.println("printing all canberoots");
			for(i=0;i<noofnodes;i++){
				System.out.println(nodes.get(i).canberoot);
			}
			*/
			if (tripledfs.get(minimumnode).get(0).size() == noofedges) {
				return tripledfs.get(minimumnode).get(0);
			}
			/*
			 * if (noofcanberoots == 1) { doDFS1(triplerecurdepth - 1,
			 * noofnodes, tripledfs.get(minimumnode), minimumnode, 1); break; }
			 */
			//System.out.println(triplerecurdepth);
			triplerecurdepth ++;
		}
		return null;

	}

	private void doDFS1(int depth, int triplerecurdepth,
			ArrayList<ArrayList<DFSstruct>> DFSCode, int startnodeid, int flag) {
		// TODO Auto-generated method stub
		int i, dfslen = DFSCode.size();
		ArrayList<ArrayList<DFSstruct>> minlist = null;
		ArrayList<ArrayList<DFSstruct>> returnlist = new ArrayList<ArrayList<DFSstruct>>();
		if (dfslen == 0) {
			DFSCode.add(new ArrayList<DFSstruct>());
			returnlist = doDFS2(depth, triplerecurdepth, DFSCode.get(0),
					startnodeid, flag);
			minlist=returnlist;
		} else {

			for (i = 0; i < dfslen; i++) {
				int startnodeID = -1; // is the vertex from which we have to
										// extend edge by 1.

				if (DFSCode.get(i).size() == 0) {
					startnodeID = startnodeid;
				} else {
					if (DFSCode.get(i).get(DFSCode.get(i).size() - 1).index1 < DFSCode
							.get(i).get(DFSCode.get(i).size() - 1).index2)
						startnodeID = DFSCode.get(i).get(
								DFSCode.get(i).size() - 1).ID2;
					else
						startnodeID = DFSCode.get(i).get(
								DFSCode.get(i).size() - 1).ID1;
				}

				returnlist=doDFS2(depth, triplerecurdepth, DFSCode.get(i),
						startnodeID, flag);
	//			System.out.println("For what am i making it false"+DFSCode.get(i));
				if (DFSCode.get(i).size() > 0) {
					nodes.get(DFSCode.get(i).get(0).ID1).covered = false;
					nodes.get(DFSCode.get(i).get(0).ID1).index=-1;
					for (int j = 0; j < DFSCode.get(i).size(); j++) {
						nodes.get(DFSCode.get(i).get(j).ID2).covered = false;
						nodes.get(DFSCode.get(i).get(j).ID2).index = -1;
						coverededges.set(DFSCode.get(i).get(j).edgeid, false);
					}
				}
				if (i == 0)
					minlist = returnlist;
				else {
					if (comparelists(returnlist.get(0), minlist.get(0)) < 0) {
						minlist = returnlist;
					}
				}
			}
		}
		int sizediff=minlist.size();
		DFSCode.clear();
		
		for(i=0;i<sizediff;i++){
			DFSCode.add(minlist.get(i));
		}
		
	}

	public ArrayList<ArrayList<DFSstruct>> doDFS2(int depth,
			int triplerecurdepth, ArrayList<DFSstruct> DFSCode,
			int startnodeid, int flag) {
		int endnodeID = -1;
		ArrayList<ArrayList<DFSstruct>> returnArraylist = new ArrayList<ArrayList<DFSstruct>>();
		int index = 0;
		if (DFSCode.size() == 0) {
			endnodeID = startnodeid;
			nodes.get(endnodeID).index = 0;
		} else {
			nodes.get(DFSCode.get(0).ID1).covered = true;
			
			nodes.get(DFSCode.get(0).ID1).index = index;
			for (int i = 0; i < DFSCode.size(); i++) {
				if (!nodes.get(DFSCode.get(i).ID2).covered) {
					nodes.get(DFSCode.get(i).ID2).index = ++index ;
					//if(nodes.get(DFSCode.get(i).ID2).label=='Z'){
					//	System.out.println("found Z"+nodes.get(DFSCode.get(i).ID2).index );
					//}
				
				}
				nodes.get(DFSCode.get(i).ID2).covered = true;
				coverededges.set(DFSCode.get(i).edgeid, true);

			}
			
			endnodeID = startnodeid; // is the node from which we have to extend
										// edge by 1
			//index - index is the index of the next node (for farward edge )to add 
			  if (DFSCode.get(DFSCode.size() - 1).index1 < DFSCode
			  .get(DFSCode.size() - 1).index2) 
				  index =DFSCode.get(DFSCode.size() - 1).index2;
			  else 
				  index= DFSCode.get(DFSCode.size() - 1).index1;
			 
		}
		Collections.sort(adjacencylist.get(endnodeID));

		int flag2 = -1;
		for (int i = 0; i < adjacencylist.get(endnodeID).size(); i++) {
			Edge e = adjacencylist.get(endnodeID).get(i);
			if (!coverededges.get(e.edgeid)) {
				if (nodes.get(e.dest).covered) {
					flag2 = 0;
					DFSCode.add(new DFSstruct(nodes.get(endnodeID).index, nodes
							.get(e.dest).index, nodes.get(endnodeID).label,
							e.edgeweight, nodes.get(e.dest).label, endnodeID,
							e.dest, e.edgeid));
					// returnArraylist.add(DFSCode);

				} else {
					flag2 = 1;
					if (i != adjacencylist.get(endnodeID).size() - 1) {
						if (e.compareTo(adjacencylist.get(endnodeID).get(i + 1)) < 0) {

							// e.dest
							DFSCode.add(new DFSstruct(
									nodes.get(endnodeID).index, 
										index + 1, nodes
											.get(endnodeID).label,
									e.edgeweight, nodes.get(e.dest).label,
									endnodeID, e.dest, e.edgeid));
							returnArraylist.add(DFSCode);
							break;
						} else {
							ArrayList<DFSstruct> temp=new ArrayList<DFSstruct>();
							
							DFSCode.add(new DFSstruct(
									nodes.get(endnodeID).index,
											index + 1, nodes
											.get(endnodeID).label,
									e.edgeweight, nodes.get(e.dest).label,
									endnodeID, e.dest, e.edgeid));
							temp.addAll(DFSCode);
							returnArraylist.add(temp);
							DFSCode.remove(DFSCode.size() - 1);
						}
					}
					else{
						DFSCode.add(new DFSstruct(
								nodes.get(endnodeID).index, 
										 index+ 1, nodes
										.get(endnodeID).label,
								e.edgeweight, nodes.get(e.dest).label,
								endnodeID, e.dest, e.edgeid));
						returnArraylist.add(DFSCode);
						break;

					}
				}
			}
		}
		if (flag2 == 0 || flag2 == -1) {
			int parentid = -1, i;
			for (i = DFSCode.size() - 1; i >= 0; i--) {
				if (DFSCode.get(i).ID2 == startnodeid
						&& DFSCode.get(i).index1 < nodes.get(startnodeid).index) {
					parentid = DFSCode.get(i).ID1;
					break;
				}
			}
			if ( parentid == -1) {
				//if (flag2 == 0) {
				returnArraylist.add(DFSCode);
			//	System.out.println("tracking down in DFS2 " + returnArraylist);
				
				return returnArraylist;
			//	}
			}
			
			returnArraylist = doDFS2(depth - 1, triplerecurdepth - 1, DFSCode,
					parentid, flag);
			// returnArraylist.add(DFSCode);
		}
		//System.out.println("tracking down in DFS2 : " + returnArraylist);
		return returnArraylist;
		
	}

	public int comparelists(ArrayList<DFSstruct> DFSlist1,
			ArrayList<DFSstruct> DFSlist2) {
		int i, len1 = DFSlist1.size(), len2 = DFSlist2.size();
		int min = Math.min(len1, len2);
		for (i = 0; i < min; i++) {
			if (DFSlist1.get(i).compareTo(DFSlist2.get(i)) < 0) {
				//System.out.println("came here1"+DFSlist1.get(i)+" "+DFSlist2.get(i));
				return -1;
			}
			if (DFSlist1.get(i).compareTo(DFSlist2.get(i)) > 0) {
				//System.out.println("came here2"+DFSlist1.get(i)+" "+DFSlist2.get(i));
				return 1;
			}
		}
		return 0;

	}

}