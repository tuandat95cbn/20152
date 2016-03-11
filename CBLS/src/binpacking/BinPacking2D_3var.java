package binpacking;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import search.ValueVariable;
import localsearch.constraints.basic.AND;
import localsearch.constraints.basic.Implicate;
import localsearch.constraints.basic.IsEqual;
import localsearch.constraints.basic.LessOrEqual;
import localsearch.constraints.basic.NotOverLap;
import localsearch.constraints.basic.OR;
import localsearch.functions.basic.FuncPlus;
import localsearch.model.ConstraintSystem;
import localsearch.model.IConstraint;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;
import localsearch.search.TabuSearch;

class Value{
	int x;
	int y; 
	int o;
	
	public Value(int x, int y, int o){
		this.x = x;
		this.y = y;
		this.o = o;
	}
}


public class BinPacking2D_3var {
	
	private LocalSearchManager mgr;
	private ConstraintSystem S;
	private VarIntLS x[];
	private VarIntLS y[];
	private VarIntLS o[];
	//private VarIntLS kq[][];
	
	private int w[];
	private int h[];
	private int height;
	private int width;
	private int nItems;
	private int vx[][];
	private int kq[][];
	
	public void readData(String fn){
		File f;
		try {
			f = new File(fn);
			Scanner scan = new Scanner(f);
			
			scan.nextLine();
			nItems=0;
			while(scan.nextInt()!=-1){
				scan.nextLine();
				nItems++;
			}
			System.out.println("nItems= "+nItems);
						
			scan.close();
			f= new File(fn);
			Scanner scan2 = new Scanner(f);
			
			width = scan2.nextInt();
			height = scan2.nextInt();
			System.out.println("height= "+height+"   width= "+width);
			
			w = new int[nItems];
			h = new int[nItems];
			
			for(int i=0; i<nItems; i++){
				w[i]=scan2.nextInt();
				h[i]=scan2.nextInt();
			}
			scan2.close();
			
			for(int i=0; i<nItems; i++){
				System.out.println("Item "+(i+1)+"  width: "+w[i]+",  height:"+h[i]);
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public void stateModel(){
		mgr = new LocalSearchManager();
		S = new ConstraintSystem(mgr);
		x = new VarIntLS[nItems];
		y = new VarIntLS[nItems];
		o = new VarIntLS[nItems];
		
		
		for(int i=0; i<nItems; i++){
			x[i] = new VarIntLS(mgr,1,width);
			y[i] = new VarIntLS(mgr,1,height);
			o[i] = new VarIntLS(mgr, 0,1);
		}
		
		
		for(int i=0; i<nItems; i++){
			S.post(new Implicate(new IsEqual(o[i], 0), 
								new AND(new LessOrEqual(new FuncPlus(x[i], w[i]), (width+1)), 
											new LessOrEqual(new FuncPlus(y[i], h[i]), (height+1)))));
			S.post(new Implicate(new IsEqual(o[i], 1), new AND(new LessOrEqual(new FuncPlus(x[i], h[i]), (width+1)), 
					new LessOrEqual(new FuncPlus(y[i], w[i]), (height+1)))));
		}
		
		for(int i=0; i<nItems-1; i++){
			for(int j=i+1; j<nItems; j++){

				IConstraint[] t1 = new LessOrEqual[4];
				t1[0] = new LessOrEqual(new FuncPlus(x[i],w[i]) , x[j]);
				t1[1] = new LessOrEqual(new FuncPlus(x[j],w[j]) , x[i]);
				t1[2] = new LessOrEqual(new FuncPlus(y[i],h[i]) , y[j]); 
				t1[3] = new LessOrEqual(new FuncPlus(y[j],h[j]) , y[i]); 
						
				S.post(new Implicate(
						new AND(new IsEqual(o[i], 0), new IsEqual(o[j], 0)), 
						new OR(t1)));

//				S.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(o[j], 0)), 
//						new OR(new NotOverLap(x[i], w[i], x[j], w[j]), new NotOverLap(y[i], h[i], y[j], h[j]))));

				IConstraint[] t2 = new LessOrEqual[4];
				t2[0] = new LessOrEqual(new FuncPlus(x[i],w[i]) , x[j]);
				t2[1] = new LessOrEqual(new FuncPlus(x[j],h[j]) , x[i]);
				t2[2] = new LessOrEqual(new FuncPlus(y[i],h[i]) , y[j]); 
				t2[3] = new LessOrEqual(new FuncPlus(y[j],w[j]) , y[i]); 
				
				S.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(o[j], 1)), 
						new OR(t2)));
				
//				S.post(new Implicate(new AND(new IsEqual(o[i], 0), new IsEqual(o[j], 1)), 
//						new OR(new NotOverLap(x[i], w[i], x[j], h[j]), new NotOverLap(y[i], h[i], y[j], w[j]))));
				
				IConstraint[] t3 = new LessOrEqual[4];
				t3[0] = new LessOrEqual(new FuncPlus(x[i],h[i]) , x[j]);
				t3[1] = new LessOrEqual(new FuncPlus(x[j],w[j]) ,x[i]);
				t3[2] = new LessOrEqual(new FuncPlus(y[i],w[i]) ,y[j]); 
				t3[3] = new LessOrEqual(new FuncPlus(y[j],h[j]) , y[i]); 
				
				S.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(o[j], 0)), 
						new OR(t3)));
				
//				S.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(o[j], 0)), 
//						new OR(new NotOverLap(x[i], h[i], x[j], w[j]), new NotOverLap(y[i], w[i], y[j], h[j]))));
				
				IConstraint[] t4 = new LessOrEqual[4];
				t4[0] = new LessOrEqual(new FuncPlus(x[i],h[i]) , x[j]);
				t4[1] = new LessOrEqual(new FuncPlus(x[j],h[j]) , x[i]);
				t4[2] = new LessOrEqual(new FuncPlus(y[i],w[i]) , y[j]); 
				t4[3] = new LessOrEqual(new FuncPlus(y[j],w[j]) , y[i]); 
				
				S.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(o[j], 1)), 
						new OR(t4)));

//				S.post(new Implicate(new AND(new IsEqual(o[i], 1), new IsEqual(o[j],1)), 
//						new OR(new NotOverLap(x[i], h[i], x[j], h[j]), new NotOverLap(y[i], w[i], y[j], w[j]))));
			}
		}
		
		
		mgr.close();
	}

	
	public void libSearch(){
		TabuSearch ts = new TabuSearch();
		//ts.search(S, tabulen, maxTime, maxIter, maxStable);
		ts.search(S, 20, 100, 10000, 100);
		System.out.println("3Var: S= "+S.violations());
		
		vx = new int[height][width];
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				vx[i][j]=-1;
			}
		}
		
		for(int i=0; i<nItems; i++){
			System.out.println("Item_solution "+(i+1)+": "+"  x="+(x[i].getValue())+"  y="+(y[i].getValue())+"  o="+o[i].getValue());
			
			if(o[i].getValue()==0){
				for(int j = (y[i].getValue()-1); j<(y[i].getValue()-1+h[i]); j++){
					for(int k=(x[i].getValue()-1); k<(x[i].getValue()+w[i]-1); k++){
						vx[j][k]=i;
					}
				}
			}
			else{
				for(int j = (y[i].getValue()-1); j<(y[i].getValue()+w[i]-1); j++){
					for(int k=(x[i].getValue()-1); k<(x[i].getValue()+h[i]-1); k++){
						vx[j][k]=i;
					}
				}
			}
		}
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				System.out.print(vx[i][j]+ "  ");
			}
			System.out.println();
		}
	}
	
	
	public ArrayList<ValueVariable> findFreeArea(){
		
		ArrayList<ValueVariable> freeSquare = new ArrayList<>();
		int max=-100000;
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				if(kq[i][j] == -1){
					int dem=0;
					int h=i;
					while(h<height){
						int k=j;
						while(k<width){
							if(kq[h][k]==-1){
								dem++;
							}
							else{
								break;
							}
							k++;
						}
						h++;
					}
					if(dem>max){
						freeSquare.clear();
						freeSquare.add(new ValueVariable(i, j));
						max = dem;
					}
					else if (dem==max){
						freeSquare.add(new ValueVariable(i, j));
					}
				}
			}
		}
		//System.out.println("freeSquare size"+freeSquare.size());
		return freeSquare;
	}
	
	public void search(int maxIt, int maxStable){
		System.out.println("init S="+S.violations());
		int it=0;
		
		ArrayList<ValueVariable> move = new ArrayList<>();
		Random rand = new Random();
		ValueVariable sel;
		int nic = 0;
		
		while(S.violations()>0&&it<maxIt){
			
			updateMarkTable();
			
			sel = findFreeArea().get(rand.nextInt(findFreeArea().size()));
			
			move.clear();
			int minDel = 100000;
			int tempX,tempY,tempO;
			int best = S.violations();
			
			for(int i=0; i<nItems; i++){
				tempY = y[i].getValue();
				tempX = x[i].getValue();
				//tempO = o[i].getValue();
				
				y[i].setValuePropagate(sel.i+1);
				x[i].setValuePropagate(sel.j+1);
				int del = S.violations() - best;
				if(del<minDel){
					move.clear();
					int del2 = S.getAssignDelta(o[i], (o[i].getValue()+1)%2);
					if(del2<0){
						move.add(new ValueVariable(i, (o[i].getValue()+1)%2));
						minDel = del + del2;
					}
					else{
						move.add(new ValueVariable(i, o[i].getValue()));
						minDel = del;
					}
				}
				else if(del == minDel){
					int del2 = S.getAssignDelta(o[i], (o[i].getValue()+1)%2);
					if(del2<0){
						move.clear();
						move.add(new ValueVariable(i, (o[i].getValue()+1)%2));
						minDel = del + del2;
					}
					else{
						move.add(new ValueVariable(i, o[i].getValue()));
					}
				}
				
				x[i].setValuePropagate(tempX);
				y[i].setValuePropagate(tempY);
				//o[i].setValuePropagate(tempO);
			}
			
			int in2 = rand.nextInt(move.size());
			ValueVariable sel2 = move.get(in2);
			x[sel2.i].setValuePropagate(sel.j+1);
			y[sel2.i].setValuePropagate(sel.i+1);
			o[sel2.i].setValuePropagate(sel2.j);
			
			it++;
			System.out.println("Step "+it+":  violations=  "+S.violations());
			
			if(minDel>=0){
				nic++;
				if(nic>maxStable){
					System.out.println("Restart----------");
					restart();
					nic=0;
				}
			}
			else{
				nic = 0;
			}
		}
	}
	
	public void restartMaintainConstraints(){
		int tempX, tempY, tempVio;
		
		tempVio = S.violations();
		
		for(int i=0; i<nItems; i++){
			tempX = x[i].getValue();
			tempY = y[i].getValue();
			ArrayList<Value> L = new ArrayList<Value>();
			//int dem=0;
			//int dem2=0;
			for(int j=0; j<height; j++){
				y[i].setValuePropagate((j+1));
				for(int k=0; k<width; k++){
					x[i].setValuePropagate((k+1));
					int del = S.getAssignDelta(o[i], (o[i].getValue()+1)%2);
					if(S.violations()<tempVio){
						L.add(new Value((k+1), (j+1), o[i].getValue()));
						if(del<0){
							L.add(new Value((k+1), (j+1), (o[i].getValue()+1)%2));
						}
					}
					
					else{
						//dem2++;
						if((S.violations()+del)<tempVio){
							//dem++;
							L.add(new Value((k+1), (j+1), (o[i].getValue()+1)%2));
						}
					}
					x[i].setValuePropagate(tempX);
				}
				y[i].setValuePropagate(tempY);
			}
			//System.out.println("dem="+dem+"dem2= "+dem2);
			Random R = new Random();
			//System.out.println("Lsize"+L.size());
			if(L.size()>0){
				Value in = L.get(R.nextInt(L.size()));
				x[i].setValuePropagate(in.x);
				y[i].setValuePropagate(in.y);
				o[i].setValuePropagate(in.o);
			}
		}
	}
	
	public void restart(){
		Random R = new Random();	
		
		for(int i=0; i<nItems; i++){	
			x[i].setValuePropagate(R.nextInt(width)+1);
			y[i].setValuePropagate(R.nextInt(height)+1);
			o[i].setValuePropagate(R.nextInt(2));
		}
	}
	
	public void updateMarkTable(){
		kq=new int[height+10][width+10];
		for(int i=0; i<height+10; i++){
			for(int j=0; j<width+10; j++){
				kq[i][j] = -1;
			}
		}
		
		for(int i=0; i<nItems; i++){
			if(o[i].getValue()==0){
				for(int j = (y[i].getValue()-1); j<(y[i].getValue()-1+h[i]); j++){
					for(int k=(x[i].getValue()-1); k<(x[i].getValue()+w[i]-1); k++){
						kq[j][k]=i;
					}
				}
			}
			else{
				for(int j = (y[i].getValue()-1); j<(y[i].getValue()+w[i]-1); j++){
					for(int k=(x[i].getValue()-1); k<(x[i].getValue()+h[i]-1); k++){
						kq[j][k]=i;
					}
				}
			}
		}
		
//		for(int i=0; i<height; i++){
//			for(int j=0; j<width; j++){
//				System.out.print(" "+kq[i][j]);
//			}
//			System.out.println();
//		}
	}
	
	
	public void printHTML(String fn){
		try {
			PrintWriter out = new PrintWriter(fn);
			out.println("<html>");
			out.println("<body>");
			int s=30; 
			
			Random rand = new Random();
			int r;
			int g;
			int b;
	
			out.println("<canvas id=\"myCanvas\" width=\""+(width*s+100)+"\" height=\""+
			(height*s+100)+"\" style=\"border:1px solid #d3d3d3;\">");
			out.println("</canvas>");
			
			out.println("<script>");
			out.println("var c = document.getElementById(\"myCanvas\");");
			out.println("var ctx = c.getContext(\"2d\");");
		
			out.println("ctx.strokeStyle = \"black\";");
			out.println("ctx.strokeRect(0,0,"+width*s+","+height*s+");");
			for(int i=0; i<nItems; i++){
				r = rand.nextInt(255);
				g = rand.nextInt(255);
				b = rand.nextInt(255);
				
				out.println("ctx.fillStyle = \""+"rgb("+r+","+g+","+b+")\";");
				out.println("ctx.strokeStyle = \"black\";");
				if(o[i].getValue()==0){
					out.println("ctx.fillRect("+((x[i].getValue()-1)*s)+","+((y[i].getValue()-1)*s)+","+
							w[i]*s+","+h[i]*s+");");
					out.println("ctx.strokeRect("+((x[i].getValue()-1)*s)+","+((y[i].getValue()-1)*s)+","+
							w[i]*s+","+h[i]*s+");");
				}
				else{
					out.println("ctx.fillRect("+((x[i].getValue()-1)*s)+","+((y[i].getValue()-1)*s)+","+
							h[i]*s+","+w[i]*s+");");
					out.println("ctx.strokeRect("+((x[i].getValue()-1)*s)+","+((y[i].getValue()-1)*s)+","+
							h[i]*s+","+w[i]*s+");");
				}
			}
			
			out.println("</script>");
			
			out.println("</body>");
			out.println("</html>");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void printHTMLTable(String fn){
		try {
			PrintWriter out = new PrintWriter(fn);
			
			ArrayList<String> color = new ArrayList<>();
			Random rand = new Random();
			int s=30;
			int r;
			int g;
			int b;
			
			for(int i=0; i<nItems; i++){
				r = rand.nextInt(255);
				g = rand.nextInt(255);
				b = rand.nextInt(255);
				
				color.add("rgb("+r+","+g+","+b+")");
			}
			
			for(int i=0; i<nItems; i++){
				System.out.println(color.get(i));
			}
			
			out.println("<html>");
			out.println("<body>");
			
			out.println("<table border=1 width=\""+(width)*s+"\" height=\""+(height)*s+"\""+">");
//			out.println("<tr>"+"<td></td>");
//			for(int i=0; i<width; i++){
//				out.println("<td>"+(i+1)+"</td>");
//			}
//			out.println("<tr>");
			for(int i=0; i<height; i++){
				out.println("<tr>");
				for(int j=0; j<width; j++){
					if(vx[i][j]==-1){
						out.println("<td></td>");
					}
					else{
						out.println("<td style=\"background-color:"+color.get(vx[i][j])+"\"></td>");
					}
				}
				out.println("</tr>");
			}
			out.println("</table>");
			
			out.println("</body>");
			out.println("</html>");
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		BinPacking2D_3var BP = new BinPacking2D_3var();
		BP.readData("./data/BinPacking2D/bin-packing-2D-W19-H16-I21.txt");
		BP.stateModel();
//		long t1 = System.currentTimeMillis();
//		BP.libSearch();
//		long t2 = System.currentTimeMillis();
//		System.out.println("time= "+(t2-t1));
		//BP.updateMarkTable();
		long t1 = System.currentTimeMillis();
		BP.search(10000,100);
		long t2 = System.currentTimeMillis();
		System.out.println("time= "+(t2-t1));
//		ArrayList<ValueVariable> fa = BP.findFreeArea();
//		for(int i=0; i<fa.size(); i++){
//			System.out.println("i="+fa.get(i).i+"  j="+fa.get(i).j);
//		}
		
		//BP.printHTML("./src/binpacking/output/table-bin-packing-W19-H23-I21.html");
		//BP.test();
	}

}
