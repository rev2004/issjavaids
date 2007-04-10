import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This is an Anomoly based IDS
 * 
 * The program takes a file of normal usage (specified in code) and creates a 'normal' usage pattern.
 * 
 * It then takes a file of usage that may be good or bad (specified in code) and gives statistics as to how many
 * packets were good or bad.
 */


public class AnomolyIDS 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		/*
		 * setup an arraylist of 34 arrays that contain a Q1 and Q3 value of the normal data from a normal file
		 */
		
		
		/*
		 * now, these 34 arrays will hold all the Q1 and Q3 stats to check against
		 */
		
		

	}
	
	/*
	 * This method takes in a filename and returns an Arraylist of signatures
	 */
	private static ArrayList getSignaturesFromFile(String file)
	{
		BufferedReader input = null;
		
		try 
		{
			input = new BufferedReader( new FileReader(file) );
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		String line = null;
		ArrayList<String> signatureList = new ArrayList<String>();
		/*
		* readLine is a bit quirky :
		* it returns the content of a line MINUS the newline.
		* it returns null only for the END of the stream.
		* it returns an empty String if two newlines appear in a row.
		*/
		try 
		{
			//count so that we dont crash java
			int count = 0;
			while (( line = input.readLine()) != null && count<150000)
			{
				//parse by newline
				signatureList.add(line+"\n");
				count++;

			}
			//now we have an array with all signatures
			System.out.println("File Read: "+file+" :: "+signatureList.size()+" Signatures Read");
			//randomly choose results
			
			input.close();
			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return signatureList;
	}
	
	/*
	 * Given a string that represents a full signature (41 comma seperated values):
	 * Filter out values to be ignored
	 * 
	 * signatures ignored are:
	 * 2,3,4,7,12,21,22
	 * 
	 * return arraylist of ints without ignored values
	 *
	 */
	private ArrayList stringToInt(String signatureString)
	{
		
		return null;
	}
	
	/*
	 * This method takes in stat values and compares them to a given signature's values and based on the securityLevel says:
	 * 
	 * return TRUE if packet falls within stats
	 * return FALSE if packet does not
	 */
	 private static boolean isInBounds(ArrayList<ArrayList> stats, ArrayList signature, int securityLevel)
	 {
		 boolean inBounds = false;
		 
		 return inBounds;
	 }

}
