import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class NewNNParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		 read in the normal file
		double outputNode = 1;
		int[] symbolicFields = {2,3,4,7,12,21,22};
		BufferedReader input = null;
		BufferedWriter output = null;
		String dataDir = "data/";
		String file = "TEST2.DATA";
		String line = null;
		int currentLine = 0;
		
		try 
		{
			input = new BufferedReader( new FileReader(dataDir+file) );
			output = new BufferedWriter( new FileWriter(dataDir+"JOONE-"+file,true));
			while ( (line = input.readLine()) != null) {
				// we are going to build the double array
				// read line by line, splitting on commas, dropping
				// symbolic columns as needed
				
				// for example
				// inputArray[0][0] = 0.0
				// inputArray[0][1] = 0.01
				// ...
				// inputArray[0][30] = 0.09
				// inputArray[1][0] = 0.0
				// ...
				
				String[] splits = line.split(",");
				int outerCurrentField = 0;
				int innerCurrentField = 0;
	
				for (String field : splits) {
					// should we ignore this field?
					if (Arrays.binarySearch(symbolicFields, innerCurrentField-1) >= 0) {
						// ignore this field
					} else {
						// process this field
						field = field.replaceAll(" ","");
						System.out.print(field+";");
						output.write(field+";");
						outerCurrentField++;
					}
					innerCurrentField++;
				}
				output.write(outputNode+"\n");
				System.out.print(outputNode+"\n");
				//System.out.println("."+line);
				//currentLine++;
				
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
