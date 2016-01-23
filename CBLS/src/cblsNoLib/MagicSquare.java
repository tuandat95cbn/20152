package cblsNoLib;

import java.util.Random;

public class MagicSquare {
	int n; 
	int magicSum;
	int[][] kq ;
	public MagicSquare(int n){
		this.n =n;
		magicSum = n*(n*n+1)/2;
		kq = new int[n][n];
	}
	
	public static void main(String args[]){
		MagicSquare m = new MagicSquare(5);
		int[][] a= {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20},{21,22,23,24,25}};
		m.solve(a);
		
		System.out.println("Check: " + m.check(m.kq));
		for(int i=0; i<m.n; i++){
			for(int j=0; j<m.n; j++){
				System.out.print(m.kq[i][j]+"  ");
			}
			System.out.println();
		}
	}
	
	
	public void solve(int[][] a){
		int T = 200;
		double al=0.999999;
		
		for(double t= T; t>=0.0000000000001; t=t*al){		
			if(check(a)==0){
				//kq = a;
				break;
			}
	
			int[][] neighbor = change(a);
				
			if(check(neighbor)<check(a)){
				a = neighbor;
			}			
			else{
				double r = Math.random();
				int del=check(a)-check(neighbor);
				if(r<Math.exp(del/t)){
					a = neighbor;
				}
			}
		}
		
		kq = a;
	}

	
	public int[][] change(int[][] a){
		int[][] temp = new int[n][n];
		Random rand = new Random();
		int temp2;
		int x = rand.nextInt(n);
		int y = rand.nextInt(n);
		int changeValue = rand.nextInt(n*n) + 1;
		
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				temp[i][j] = a[i][j];
			}
		}
		
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++){
				if(temp[i][j]==changeValue){
					temp2 = temp[i][j];
					temp[i][j]=temp[x][y];
					temp[x][y]= temp2;
					return temp;
				}
			}
		}
		
		return temp;
	}
	

	public int check(int[][] a){
		int dem =0;
		int checkSumDia1=0;
		int checkSumDia2=0;
		
		for(int i=0; i<n; i++){
			int checkSumCol=0;
			int checkSumRow=0;
			
			for(int j=0; j<n; j++){
				checkSumRow = checkSumRow + a[i][j];
				checkSumCol = checkSumCol + a[j][i];
			}
			
			if(checkSumRow != magicSum){
				dem++;
			}
			if(checkSumCol != magicSum){
				dem++;
			}
			checkSumDia1 = checkSumDia1 + a[i][i];
			checkSumDia2 = checkSumDia2 + a[i][n-i-1];
		}
	
		if(checkSumDia1 != magicSum){
			dem++;
		}
		
		if(checkSumDia2 != magicSum){
			dem++;
		}
		
		return dem;
	}
}