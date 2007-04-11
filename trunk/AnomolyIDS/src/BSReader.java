import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Created on Apr 2, 2007
 *
 * $Id$
 *
 */

/**
 * @author <a href="mailto:nsp1828@rit.edy">Nicholas Pike</a>
 * @version
 */


public class BSReader {
	private BufferedReader input = null;
	private ArrayList<Double>[] bullshitArray = null;
	private ArrayList<Integer> ignoreColumns = null;
	private ArrayList< ArrayList<Double> > columnData = null;
	
	public boolean debug = false;
	
	public BSReader() {
		ignoreColumns = new ArrayList<Integer>();
		ignoreColumns.add(new Integer(2));
		ignoreColumns.add(new Integer(3));
		ignoreColumns.add(new Integer(4));
		ignoreColumns.add(new Integer(7));
		ignoreColumns.add(new Integer(12));
		ignoreColumns.add(new Integer(21));
		ignoreColumns.add(new Integer(22));
	}
	
	private void initData() {
		for (int x = 0; x < 41; x++) {
			ArrayList<Double> column = new ArrayList<Double>();
			//columnData.set(x, column);
			columnData.add(column);
		}
	}
	
	public ArrayList< ArrayList<Double> > getRange(String filename) {
		ArrayList< ArrayList<Double> > retVal = new ArrayList< ArrayList<Double> >();
		ArrayList<Double> q1 = new ArrayList<Double>();
		ArrayList<Double> q3 = new ArrayList<Double>();
		
		//bullshitArray = new ArrayList<Double>[34];
		//ArrayList[] results = new ArrayList[4];
		
		columnData = new ArrayList< ArrayList<Double> >();
		initData();
		System.out.println("S:"+columnData.size());
		
	      try {
			input = new BufferedReader( new FileReader(filename) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	      String line = null; //not declared within while loop

	      int counter = 0;
	      try {
	    	  
			while (( line = input.readLine()) != null){
			    // parse by coma
				String tmp[] = line.split(",");
				int realColumn = 0;
				for (int y = 0; y < tmp.length; y++) {
					
					// we only want to store this columns value if its not in the ignore
					// list.  So we keep a value called realColumn to keep in synch with
					// our array of array lists
					//if (!ignoreColumns.contains(new Integer(y-1))) {
						columnData.get(y).add(Double.parseDouble(tmp[y]));
						realColumn++;
					//}
					
				}
				counter++;
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		// the current value of counter is how many lines we actually read.
	
		
		if (debug) {
		System.out.println("Finished reading "+counter+" lines of trainer data.");
		}
		
		// we need to sort the columns before we can find quartile
			for (int y = 0; y < 41; y++) {
				ArrayList<Double> column = columnData.get(y);
				// sort the sucker
				java.util.Collections.sort(column);
				//System.out.println(column.size());
				
				int q1Index = column.size()/4;
				int q3Index = column.size() - q1Index;
				
				
				Double q1Val = (Double)column.get(q1Index);
				Double q3Val = (Double)column.get(q3Index);
				
				ArrayList<Double> vals = new ArrayList<Double>();
				vals.add(q1Val);
				vals.add(q3Val);
				
				retVal.add(vals);
				
			//	q1.add(q1Val);
			//	q3.add(q3Val);
				
				if (debug) {
				//iterate and print its elements to make sure its actually sorted
					System.out.println("### Itterating through Column ###");
					for (Object foo : column) {
						Double bar = (Double)foo;
						System.out.println(bar);
					}
					System.out.println("#################");
					System.out.println("Q1:" + q1);
					System.out.println("Q3:" + q3);
				}
				
			}
		
		
		
		
		
		return retVal;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*
		 * NOTE:  This does not omit symbolic columns.
		 */
		
		BSReader foo = new BSReader();
		foo.debug = false;
		ArrayList< ArrayList<Double> >  bar = foo.getRange("Optimized_GuessPassword-TRAINER");
		

		
		int counter = 0;
		for (ArrayList<Double>  elt : bar) {
			
			
			System.out.println("Column "+counter+ " = "+elt.get(0)+"/"+elt.get(1));
			counter++;
		}
		
		
		//System.out.println(q1.size());
		//System.out.println(q2.size());
	   
		
	}

}
