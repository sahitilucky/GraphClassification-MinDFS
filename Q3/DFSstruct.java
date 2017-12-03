

public class DFSstruct implements Comparable<DFSstruct>{
	int ID1,ID2;		//ID of the node in arraylist 
	int edgeweight;		
	int index1,index2;		//indexes of nodes
	String label1,label2;
	int edgeid;			//index for making edgecovered true or false
	
	public DFSstruct(int ind1, int ind2, String l1, int ew, String l2, int id1, int id2,int eid ) {
		index1 = ind1;
		index2 = ind2;
		edgeweight = ew;
		label1=l1;label2=l2;
		ID1=id1;ID2=id2;
		edgeid=eid;
	}
	@Override
	public int compareTo(DFSstruct arg0) {
		// TODO Auto-generated method stub
		if(this.index1<arg0.index1){
			return -1;
		}
		else if(this.index1==arg0.index1){
			if(this.index2<arg0.index2){
					return -1;
				}
				else if(this.index2==arg0.index2){
					if(this.label1.compareTo(arg0.label1)<0){
						return -1;
					}
					else if(this.label1.compareTo(arg0.label1)==0){
						if(this.edgeweight<arg0.edgeweight){
							return -1;
						}
						else if(this.edgeweight==arg0.edgeweight){
							if(this.label2.compareTo(arg0.label2)<0){
								return -1;
							}
							else if(this.label2.compareTo(arg0.label2)==0){
								return 0;
							}
							else {return 1;}
						}
						else{return 1;}
					}
					else{return 1;}
				}
				else{
					return 1;
				}
		}
		
		else {return 1;}
	
	}
	@Override
	public String toString(){
		
		return "<"+index1+","+index2+","+label1+","+edgeweight+","+label2+">";
		
	}
	
}
