package bacp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.LessThan;
import localsearch.functions.conditionalsum.ConditionalSum;
import localsearch.functions.max_min.Max;
import localsearch.functions.max_min.Min;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.TabuSearch;
import localsearch.selectors.MinMaxSelector;

public class BACP {
	class ValueVariable{
		int i;
		int j;
		
		public ValueVariable(int i, int j){
			this.i = i;
			this.j = j;
		}
	}
	
	int period;
	int minLoad;
	int maxLoad;
	int minCourse;
	int maxCourse;
	int nCourse;
	int[] creditPerCourse;
	int[][] prereqIndex;
	int prereqNum;	
	
	VarIntLS[] x;
	LocalSearchManager mgr;
	ConstraintSystem S;
	IFunction[] sum;
	
	public void readFile(String file){
		try {
			FileReader f = new FileReader(file);
			Scanner s = new Scanner(f);
		
			nCourse = s.nextInt();
			period = s.nextInt();
			minLoad = s.nextInt();
			maxLoad = s.nextInt();
			minCourse = s.nextInt();
			maxCourse = s.nextInt();
			
			creditPerCourse = new int[nCourse];
			for(int i=0; i<nCourse; i++){
				creditPerCourse[i]=s.nextInt();
			}
			
			prereqNum = s.nextInt();
			prereqIndex = new int[prereqNum][2];
			for(int i=0; i<prereqNum; i++){
				for(int j=0; j<2; j++){
					prereqIndex[i][j]=s.nextInt();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	public void stateModel(){
		mgr = new LocalSearchManager();
		S = new ConstraintSystem(mgr);
		
		x = new VarIntLS[nCourse];
		for(int i=0; i<nCourse; i++){
			x[i] = new VarIntLS(mgr,0, (period-1));
		}
		
//		int w[] = new int[nCourse];
//		for(int i=0; i<nCourse; i++){
//			w[i]=1;
//		}
		for(int i=0;i<period; i++){
			S.post(new LessOrEqual(new ConditionalSum(x, i), maxCourse));
			S.post(new LessOrEqual(minCourse, new ConditionalSum(x, i)));
		}
		
		sum = new IFunction[period];
		for(int i=0; i<period; i++){
			sum[i]= new ConditionalSum(x, creditPerCourse, i);
			S.post(new LessOrEqual(sum[i], maxLoad));
			S.post(new LessOrEqual(minLoad, sum[i]));
		}
		
		for(int i=0; i<prereqNum; i++){
			S.post(new LessThan(x[prereqIndex[i][0]-1], x[prereqIndex[i][1]-1]));
		}
		mgr.close();
	}
	
	
	public void search(int maxStep){
		int it=0;
		
		ArrayList<ValueVariable> move = new ArrayList<ValueVariable>();
		Random R = new Random();
		
		while(it<maxStep && S.violations()>0){
			int minDel=100000;
			move.clear();
			for(int i=0; i<nCourse; i++){
				for(int j=0; j<period; j++){
					int delta = S.getAssignDelta(x[i], j);
					if(delta<minDel){
						move.clear();
						move.add(new ValueVariable(i, j));
						minDel = delta;
					}
					else if(delta == minDel){
						move.add(new ValueVariable(i, j));
					}
				}
			}
			
			ValueVariable sel = move.get(R.nextInt(move.size()));
			x[sel.i].setValuePropagate(sel.j);
			
			it++;
			System.out.println("Step "+it+"   x["+sel.i+"].setValue = "+sel.j+"   violations = "+S.violations());
		}
//		TabuSearch t = new TabuSearch();
//		t.search(S, 20, 100, 100000, 100);
	}
	
	public void TabuSearch(int maxStep, int tabulen){
		
		int[][] tabu = new int[nCourse][period];
		for(int i=0; i<nCourse; i++){
			for(int j=0; j<period; j++){
				tabu[i][j]=-1;
			}
		}
		
		int it=0;
		int best = S.violations();
		ArrayList<ValueVariable> move = new ArrayList<ValueVariable>();
		Random r = new Random();
		
		while(it<maxStep && S.violations()>0){
			move.clear();
			int minDel = 10000;
			
			for(int i=0; i<nCourse; i++){
				for(int j=0; j<period; j++){
					int delta = S.getAssignDelta(x[i], j);
					if(tabu[i][j]<it || (S.violations()+delta)<best){
						if(delta<minDel){
							move.clear();
							move.add(new ValueVariable(i, j));
							minDel=delta;
						}
						else if(delta == minDel){
							move.add(new ValueVariable(i, j));
						}
					}
				}
			}
			
			ValueVariable sel = move.get(r.nextInt(move.size()));
			x[sel.i].setValuePropagate(sel.j);
			
			if(S.violations()<best){
				best = S.violations();
			}
			
			it++;
			System.out.println("Step "+it+"   x["+sel.i+"].setValue = "+sel.j+"   violations = "+S.violations());
		}
	}
	
	
	public int load(){
		//System.out.println("begin load");
		int minCredit = 10000000;
		int maxCredit = -10000000;
		
		for(int i=0; i<period; i++){
			if(sum[i].getValue()<minCredit){
				minCredit=sum[i].getValue();
			}
			if(sum[i].getValue()>maxCredit){
				maxCredit = sum[i].getValue();
			}
		}
//		System.out.println("minCredit"+ minCredit);
//		System.out.println("maxCredit"+ maxCredit);
		
		int load = maxCredit - minCredit;
		
		return load;
	}
	
	public void optimize(int maxStep){
		System.out.println("begin minimize---load="+load());
		int it=0;
		
		Random R = new Random();
		ArrayList<ValueVariable> move = new ArrayList<ValueVariable>();
		while(it<maxStep && load()>0){
			int min=100000;
			move.clear();
			for(int i=0; i<nCourse; i++){
				for(int j=0; j<period; j++){
					if(S.getAssignDelta(x[i],j)==0){
						int temp = x[i].getValue();
						x[i].setValuePropagate(j);
						
						if(load()<min){
							min=load();
							move.clear();
							move.add(new ValueVariable(i, j));
						}else if(load()==min){
							move.add(new ValueVariable(i, j));
						}
						
						x[i].setValuePropagate(temp);
					}
				}
			}	
			
			ValueVariable sel = move.get(R.nextInt(move.size())); 
			x[sel.i].setValuePropagate(sel.j);

			it++;
			System.out.println("Step "+it+ "   Violations="+S.violations()+"  load="+min
					+"   x["+sel.i+"].setValue("+sel.j+")");
		}
		
	}
	
	public static void main(String args[]){
		BACP b = new BACP();
		b.readFile("./data/bacp/bacp.in03");
				
		b.stateModel();
		System.out.println(b.S.violations());
		//b.search(10000);
		b.TabuSearch(10000,10);
		
		for(int i=0; i<b.period; i++){
			System.out.print("Period "+(i+1)+" ("+b.sum[i].getValue()+") "+": ");
			for(int j=0; j< b.nCourse; j++){
				if(b.x[j].getValue()==i){
					System.out.print((j+1)+ " ");
				}
			}
			System.out.println();
		}
		b.optimize(1000);
		
		for(int i=0; i<b.period; i++){
			System.out.print("Period "+(i+1)+" ("+b.sum[i].getValue()+") "+": ");
			for(int j=0; j< b.nCourse; j++){
				if(b.x[j].getValue()==i){
					System.out.print((j+1)+ " ");
				}
			}
			System.out.println();
		}
	}
	
}
