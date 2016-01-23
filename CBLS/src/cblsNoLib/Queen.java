package cblsNoLib;

	import java.util.ArrayList;
	import java.util.Random;
	import java.util.zip.Checksum;

	public class Queen {
		int n=100;
		int [] x;
		int violations;
		int [] best;
		
		
		
		public int checkviolation(int[] a){
			int dem=0;
			for(int i=0;i<n-1;i++){
				for(int j=i+1;j<n;j++)
				{
					if(a[i]==a[j])
					{
						dem++;
					}
					if((a[i]+i)==(a[j]+j)){
						dem++;
					}
					if((a[i]-i)==(a[j]-j)){
						dem++;
					}
				}
			}
			return dem;
		}
		
		
		public int[] find(int[] a){
			int [] value=new int[n];
			for(int i=0;i<n;i++){
				for(int j=0;j<n;j++)
				{
					if(i!=j){
					if(a[i]==a[j])
					{
						value[i]++;
					}
					if((a[i]+i)==(a[j]+j)){
						value[i]++;
					}
					if((a[i]-i)==(a[j]-j)){
						value[i]++;
					}
					}
				}
			}
			
			int k=-1;
			int temp=-1;
			for(int i=0;i<n;i++){
				if(temp<value[i]){
					temp=value[i];
					k=i;
				}
			}
			
			ArrayList<Integer> l=new ArrayList<>();
			for(int i=0;i<n;i++){
				if(value[i]==temp)
				{
					l.add(i);
				}
			}
			
			
			int [] tempe=new int[l.size()];
			for(int i=0;i<l.size();i++)
				tempe[i]=l.get(i);
			return tempe;
		}
		
		public void solver(){
			x=new int[n];
			best=new int[n];
			int[] temp=new int[n];
			int dem=0;
			violations=checkviolation(best);
			
			System.out.println("begin");
			while(violations!=0&&dem<100000){
//			int T = 200;
//			double al=0.9999;
//			
//			for(double t= T; t>=0.0000000000001; t=t*al){		
//				if(violations==0){
//					//kq = a;
//					break;
//				}
				int[] list=find(best);
				System.out.println("leg =  "+list.length);
				Random rand = new Random();
				int u=rand.nextInt(list.length);
				
				int max=list[u];
				
				System.out.println("max  ==   "+max);
		            for(int i=0;i<n;i++){
		            	temp[i]=best[i];
		            }
		            int i;
		            
//		        int value[] = new int[n];
//		        int r;
		        for(i=0;i<n;i++)
				{
					temp[max]=i;
					if(violations>checkviolation(temp))
					{
						violations=checkviolation(temp);
						best[max]=i;
					}
				}
				
//				if(dem>2000){
//					double r = Math.random();
//					int del = violations-checkviolation(temp);
//					if(r<Math.exp(del/t)){
//						violations=checkviolation(temp);
//						best[max]=i;
//					}
//				}

				System.out.println("violation  =    "+violations+"   dem   =   "+dem);
				dem++;
				
			}
			
			
			for(int i=0;i<n;i++){
				System.out.print(best[i]+"    ");
			}
			
			
			
			
		}
		
		
		public static void main(String[] args){
		
			Queen t=new Queen();
			int []a =new int[t.n];
			for(int i=0;i<a.length;i++)
				a[i]=4;
			//System.out.println("vio   =    "+t.checkviolation(a)+"  max   =   "+t.find(a));
			t.solver();
			
		}

	}

