package cblsNoLib;

import java.util.*;

public class SGP{
	int g;
	int n;
	int w;
	// golfmat[i][j][k] : golfer k choi o tuan j trong nhom i ?
	int[][][] golfmat;
	int s;	
	
	//Moi tuan moi nguoi chi choi trong mot nhom
/*	public int check1(int[][][] a){
		int dem=0;	
		for(int i=0; i<w; i++){
			for(int j=0; j<n; j++){
				int sum=0;					
				for(int k=0; k<g; k++){
					sum = sum + golfmat[k][i][j];				
				}
				if(sum != 1){
				dem++;				
				}			
			}			
		}
		return dem;
	}
*/
	//Moi group co s nguoi
/*	public int check2(int[][][] a){
		int dem=0;
		for(int i=0; i<w; i++){
			for(int j=0; j<g; j++){
				int sum=0;
				for(int k=0; k<n; k++){
					sum = sum + golfmat[j][i][k];
				}
				if(sum != s){
					dem++;
				}
			}
		}
		return dem;
	}	
*/
	//khong co 2 nguoi nao da tung choi trong cung mot nhom gap lai nhau
	public int checkViolation(int[][][] a){
		int dem = 0;
		for(int i1=0; i1<g; i1++){
			for(int i2=0; i2<w; i2++){
				for(int i3=i2+1; i3<w; i3++){
					for(int i4=0; i4<g; i4++){
						int sum=0;
						for(int i5=0; i5<n; i5++){
							sum = sum + a[i1][i2][i5]*a[i4][i3][i5];
						}
						if(sum > 1){
							dem++;			
						}	
					}
				}
			}
		}
		return dem;
	}
	
/*	public int checkViolation(int[][][] a){
		return (check1(a)+check2(a)+check3(a));
	}
*/	
	public void solve(){
		int T = 200;
		double al=0.99999999999;
		int dem=10000000;
		//for(double t=T; t>=0.00000000001; t=t*al){
			
		while(checkViolation(golfmat) != 0){
			if(dem==0) break;
			System.out.println(checkViolation(golfmat));
			int[][][] neighbor = change(golfmat);
			if(checkViolation(neighbor)<checkViolation(golfmat)){
				golfmat = neighbor;}
//			else{
//				double r = Math.random();
//				int del = checkViolation(golfmat)-checkViolation(neighbor);
//				if(r < Math.exp(del/t)){
//					golfmat = neighbor;			
//				}
//			}
			dem--;
		}
	}

	public int[][][] change(int a[][][]){

		//Tao ra bang moi, thao tac tren bang moi		
		int[][][] temp = new int[g][w][n];
		for(int i=0; i<g; i++){
			for(int j=0; j<w; j++){
				for(int k=0; k<n; k++){
					temp[i][j][k] = a[i][j][k];
				}
			}
		}
		
//		System.out.println("temp");
//		for(int i=0; i<g; i++){
//			for(int j=0; j<w; j++){
//				System.out.print("(");
//				for(int k=0; k<n; k++){
//					System.out.print(temp[i][j][k]+ " ");
//				}
//				System.out.print(")");
//			}
//			System.out.println();
//		}
		
		//Tim nhom bi vi pham nhieu nhat
		int[][] value = new int[g][w];
		for(int i1=0; i1<g; i1++){
			for(int i2=0; i2<w; i2++){
				for(int i3=i2+1; i3<w; i3++){
					for(int i4=0; i4<g; i4++){
						int sum=0;
						for(int i5=0; i5<n; i5++){
							sum = sum + temp[i1][i2][i5]*temp[i4][i3][i5];
						}
						System.out.println("sum ="+sum);
						if(sum > 1){
							value[i1][i2]++;
						}	
					}
				}
			}
		}
		
		ArrayList<Integer> group = new ArrayList<Integer>();
		ArrayList<Integer> week = new ArrayList<Integer>();
		//ArrayList<Integer> A = new ArrayList<Integer>();
		//int i1=-1, i2=-1;
		int max = value[0][0];
		for(int i=0; i<g; i++){
			for(int j=0; j<w; j++){
				System.out.print("Value"+i+j+": "+value[i][j]+"  ");
				if(value[i][j]>max){
					group.clear();
					week.clear();
					max = value[i][j];
					group.add(i);
					week.add(j);
					//i1=i;
					//i2=j;
				}
				if(value[i][j]== max){
						group.add(i);
						week.add(j);
				}
			}
		}
		System.out.println();
		
		System.out.println("group: "+group);
		System.out.println("week: "+week);
		Random R = new Random();
		int index= R.nextInt(group.size());
		int i1 = group.get(index);
		int i2 = week.get(index);
		//ArrayList A = new ArrayList();
		//System.out.println("gMax= "+i1+"       wMax="+i2);					
		
		ArrayList<Integer> A = new ArrayList<Integer>();
		for(int i=0; i<n; i++){
			if(temp[i1][i2][i]==1){
				A.add(i);
			}
		}
		
		int i3 = A.get(R.nextInt(A.size()));
		//Tim cap 1 0 nhieu nhat
//		int[] value2 = new int[n];
//		for(int i=0; i<n; i++){
//			if(temp[i1][i2][i]==0){
//				for(int j=0; j<w; j++){
//					if(w != i2){
//						for(int k=0; k<g; k++){
//							if(temp[k][j][i]==0 && temp[k][j][i3]==1){
//								value2[i]++;							
//							}						
//						}
//					}
//				}
//			}
//		}
//		
		ArrayList<Integer> A1 = new ArrayList<Integer>();
		for(int i=0; i<n; i++){
			if(temp[i1][i2][i]==0){
				A1.add(i);
			}
		}
		
		
		//max=value2[0];
//		for(int i=0; i<n; i++){
//			if(value2[i]>max){
//				max = value2[i];
//				A1.clear();
//				A1.add(i);
//			}
//			if(value2[i]==max){
//				A1.add(i);
//			}
//		}
		
		int i4 = A1.get(R.nextInt(A1.size()));		
		//System.out.println("index4= "+i4);	
		for(int i=0; i<g; i++){
			if(temp[i][i2][i4]==1){
				//System.out.println("indexChanged= "+i);	
				temp[i][i2][i4]=0;
				temp[i][i2][i3]=1;
				temp[i1][i2][i3]=0;
				temp[i1][i2][i4]=1;
				break;			
			}
		}
//		for(int i=0; i<g; i++){
//			System.out.print("Week "+(i+1)+":");
//			for(int j=0; j<w; j++){
//				System.out.print("(");
//				for(int k=0; k<n; k++){
//					System.out.print(temp[i][j][k]+", ");
//				}
//				System.out.print(")");	
//			}
//			System.out.println();	
//		}
		return temp;
	}

	public SGP(int nGroup, int nGolfer, int nWeek){
		g = nGroup;
		n = nGolfer;
		w = nWeek;
		s = n/g;
		golfmat = new int[g][w][n];
		
		for(int i1=0; i1<w; i1++){
			int i=0;				
			for(int i2=0; i2<g; i2++){
				int t=s;					
				while(i<n){					
					if(t==0) break;
					golfmat[i2][i1][i]=1;
					i++;
					t--;				
				}			
			}
			
		}
	}

	public static void main(String args[]){
		SGP sgp = new SGP(5,15,5);
		
		sgp.solve();
		for(int i=0; i<sgp.w; i++){
			System.out.print("Week "+(i+1)+":");
			for(int j=0; j<sgp.g; j++){
				System.out.print("(");
				for(int k=0; k<sgp.n; k++){
					System.out.print(sgp.golfmat[j][i][k]+", ");
				}
				System.out.print(")");	
			}
			System.out.println();	
		}
		System.out.println(sgp.checkViolation(sgp.golfmat));
		
	}
}	