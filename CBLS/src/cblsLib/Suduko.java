package cblsLib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.selectors.MinMaxSelector;

public class Suduko {

	class ValueVariable{
		int i;
		int j;
		int k;
		
		public ValueVariable(int i, int j, int k){
			this.i = i;
			this.j = j;
			this.k = k;
		}
	}
	
	int n; 
	VarIntLS x[][];
	LocalSearchManager mgr;
	ConstraintSystem csys ;
	
	
	public void stateModel(){
				
		for(int i=0; i<n; i++){
			VarIntLS x_cot[] = new VarIntLS[n];
			for(int j=0; j<n; j++){
				x_cot[j] = x[j][i];
			}
			csys.post(new AllDifferent(x_cot));
		}
		
		
		int sqrtN = (int) Math.sqrt(n);
	
		for(int i=0; i<sqrtN; i++){
			for(int j=0; j<sqrtN; j++){
				VarIntLS x_oVuongCon[] = new VarIntLS[n];
				int m=0;
				for(int k=0; k<sqrtN; k++){
					for(int l=0; l<sqrtN; l++){
						x_oVuongCon[m]=x[i*sqrtN+k][j*sqrtN+l];
						m++;
					}
				}
				csys.post(new AllDifferent(x_oVuongCon));
			}
		}
		mgr.close();
	}
	
	public void search(int maxStep, int tabulen){		
		int it=0;
		
		int tabu[][] = new int[n][n];
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				tabu[i][j]=-1;
			}
		}
		
		ArrayList<ValueVariable> move = new ArrayList<ValueVariable>();
		Random R = new Random();
		int best = csys.violations();
		
		while(it<maxStep && csys.violations()>0){			
			int minDel=10000;
			move.clear();
			
			for(int i=0; i<n; i++){
				for(int j=0; j<n-1; j++){
					for(int k=j+1; k<n; k++){
						int delta = csys.getSwapDelta(x[i][j], x[i][k]);
				
						if(tabu[i][j]<=it || csys.violations()+delta<best){
							if(delta<minDel){
								move.clear();
								move.add(new ValueVariable(i, j, k));
								minDel = csys.getSwapDelta(x[i][j], x[i][k]);
							}
							else if(delta==minDel){
								move.add(new ValueVariable(i, j, k));
							}
						}
					}
				}
			}
			
			
			ValueVariable r = move.get(R.nextInt(move.size()));
							
			x[r.i][r.j].swapValuePropagate(x[r.i][r.k]);
			tabu[r.i][r.j]=it+tabulen;
			if(csys.violations()<best){
				best = csys.violations();
			}
			
			it++;
			System.out.println("Step "+it + " swap "+"x["+r.i+"]["+ r.j+"] vs "+"x["+r.i+"]["+ r.k+"]"+ "   Violations= "+csys.violations());
		}
	}
	
	public Suduko(int n){
		this.n = n;
		x = new VarIntLS[n][n];
		
		mgr = new LocalSearchManager();
		csys = new ConstraintSystem(mgr);

		for(int i=0; i<n ; i++){
			int v = 1;
			for(int j=0; j<n; j++){
				x[i][j] = new VarIntLS(mgr, 1,n);
				x[i][j].setValue(v);
				v++;
			}
		}			
	}
	
	public static void main(String args[]){
		Suduko S = new Suduko(49);
		S.stateModel();
		S.search(100000,40);
		System.out.println(S.csys.violations());
		for(int i=0; i<S.n ; i++){
			for(int j=0; j<S.n; j++){
				System.out.print(S.x[i][j].getValue() + " ");
			}
			System.out.println();
		}
	}
	
}
