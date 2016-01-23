package cblsLib;

import java.util.ArrayList;
import java.util.Random;

import cblsLib.Suduko.ValueVariable;
import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.NotEqual;
import localsearch.functions.sum.Sum;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.SA_search;
import localsearch.search.TabuSearch;
import localsearch.selectors.MinMaxSelector;

public class MagicSquare {
	
	class ValueVariable{
		int i;
		int j;
		//int k;
		//int l;
		
		public ValueVariable(int i, int j){
			this.i = i;
			this.j = j;
			//this.k = k;
		}
	}
	
	int n;
	int sum;
	
	LocalSearchManager mgr;
	ConstraintSystem S;
	VarIntLS[][] x;
	
	public void stateModel(){		
	
		for(int i=0; i<n; i++){
			S.post(new IsEqual(new Sum(x[i]), sum));
		}
		
		for(int i=0; i<n; i++){
			VarIntLS[] x_cot = new VarIntLS[n];
			for(int j=0; j<n; j++){
				x_cot[j]=x[j][i];
			}
			S.post(new IsEqual(new Sum(x_cot), sum));
		}
		
		VarIntLS[] x_cheo_xuoi = new VarIntLS[n];
		VarIntLS[] x_cheo_nguoc = new VarIntLS[n];
		
		for(int i=0; i<n; i++){
			x_cheo_xuoi[i]= x[i][i];
			x_cheo_nguoc[i]=x[i][n-1-i];
		}
		S.post(new IsEqual(new Sum(x_cheo_xuoi), sum));
		S.post(new IsEqual(new Sum(x_cheo_nguoc), sum));
		
		S.close();
		mgr.close();
	}
	
	public void search(int maxStep){
		
		int it=0;
		/*
		while(S.violations()!=0 && it<maxStep){
			int minDel = 100000;
			ArrayList<ValueVariable>  A = new ArrayList<ValueVariable>();
			
			for(int i=0; i<n*n-1; i++){
				for(int j=i+1; j<n*n; j++){
					if(S.getSwapDelta(x[i/n][i%n], x[j/n][j%n])<minDel){
						A.clear();
						A.add(new ValueVariable(i, j));
						minDel = S.getSwapDelta(x[i/n][i%n], x[j/n][j%n]);
					}
					else if(S.getSwapDelta(x[i/n][i%n], x[j/n][j%n]) == minDel){
						A.add(new ValueVariable(i, j));
					}
				}
			}
			
			Random R = new Random();
			ValueVariable v = A.get(R.nextInt(A.size()));
			
			x[(v.i)/n][(v.i)%n].swapValuePropagate(x[(v.j)/n][(v.j)%n]);
			
			it++;
			System.out.println("Step "+ it+ "  Swap x["+(v.i)/n+"]["+(v.i)%n+"]"
			+ "vs x["+(v.j)/n+"]["+(v.j)%n+"]"+"   Violation="+S.violations());
		}*/
   localsearch.search.TabuSearch ts=new localsearch.search.TabuSearch();
   ts.search(S, 20,20,1000000,100);
   System.out.println("S  =    "+S.violations());
	}
	
	public MagicSquare(int n){
		this.n = n;
		sum = n*(n*n+1)/2;
		mgr = new LocalSearchManager();
		S = new ConstraintSystem(mgr);
		x = new VarIntLS[n][n];
		
		int k=1;
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				x[i][j] = new VarIntLS(mgr, 1, n*n);
				x[i][j].setValue(k);
				k++;
			}
		}
	}
	
	public static void main(String args[]){
		MagicSquare m = new MagicSquare(6);
		
		m.stateModel();
		System.out.println(m.sum+" "+m.S.violations());
		m.search(10000);
		for(int i=0; i<m.n; i++){
			for(int j=0; j<m.n; j++){
				System.out.print(m.x[i][j].getValue()+",");
			}
			System.out.println();
		}
	}
}
