package cblsLib;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.functions.basic.FuncMinus;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IFunction;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.SA_search;
import localsearch.search.TabuSearch;
import localsearch.selectors.MinMaxSelector;

public class Queen {
	int n=8;
	VarIntLS[] x;
	LocalSearchManager ls;
	ConstraintSystem S;
	
	public void model(){
		ls=new LocalSearchManager();
		 S=new ConstraintSystem(ls);
		 x=new VarIntLS[n];
		 for(int i=0;i<n;i++){
			 x[i]=new VarIntLS(ls, 0, n-1);
		 }
		 
		 // rang buoc
		 S.post(new AllDifferent(x));
		 IFunction[]f1=new IFunction[n];
		 IFunction[]f2=new IFunction[n];
		 for(int i=0;i<n;i++){
			 f1[i]=new FuncMinus(x[i],i);
			 f2[i]=new FuncPlus(x[i],i);
		 }
		 
		 S.post(new AllDifferent(f1));
		 S.post(new AllDifferent(f2));
		 
		 
		 // solve
		 S.close();
		 ls.close();	
	}
	
	public void stateSearch(int maxStep){
		MinMaxSelector m = new MinMaxSelector(S);
		
		int it=0;
		int minDelTa = 10000;
		
		VarIntLS sel_x;
		while(S.violations()!=0 && it<maxStep){
			System.out.print("Step: "+(it+1));
			sel_x = m.selectMostViolatingVariable();
			System.out.print("   sel_x.getValue = "+ sel_x.getValue());
			int sel_v = m.selectMostPromissingValue(sel_x);
			System.out.print("   sel_v=" +sel_v);
			sel_x.setValuePropagate(sel_v);
			System.out.print("   sel_x after" +sel_x.getValue()+"   Violation= "+S.violations());
			it++;
			System.out.println();
		}
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Queen m = new Queen();
		m.model();
		m.stateSearch(2);
	}

}
