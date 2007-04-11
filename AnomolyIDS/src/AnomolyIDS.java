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

	//This variable defines how strict we match stats, if number of deviations is > than SECURITY_LEVEL; isInBonds returns FALSE
	private static final int SECURITY_LEVEL = 2;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		/*
		 * setup an arraylist of 41 arrays that contain a Q1 and Q3 value of the normal data from a normal file
		 */
		BSReader myReader = new BSReader();
		ArrayList<ArrayList<Double>> normalStats = myReader.getRange("./data/Optimized_Normal-TRAINER");
		
		//now remove the ones we don't want (2,3,4,7,12,21,22)
		//adjust for array starts at 0 (so subtract 1
		normalStats.remove(1);
		normalStats.remove(2);
		normalStats.remove(3);
		normalStats.remove(6);
		normalStats.remove(11);
		normalStats.remove(20);
		normalStats.remove(21);
		
		/*
		 * now, normalStats has 34 arrays will hold all the Q1 and Q3 stats to check against
		 * 
		 * So, lets get our signatures to check
		 * 
		 */
		ArrayList<String> signatureStrings = getSignaturesFromFile("./data/TEST.DATA");
		
		/*
		 * run our signatures against our stats
		 */
		int misuse = 0;
		int normal = 0;
		ArrayList<Double> signature;
		
		for(String currString : signatureStrings)
		{
			signature = stringToDouble(currString);
			//filter out what we dont want to compare
			signature.remove(1);
			signature.remove(2);
			signature.remove(3);
			signature.remove(6);
			signature.remove(11);
			signature.remove(20);
			signature.remove(21);
			
			//check to make sure sizes correct
			if(normalStats.size() == signature.size())
			{
				if(isInBounds(normalStats, signature, SECURITY_LEVEL))
				{
					normal++;
				}
				else
				{
					misuse++;
				}
			}
			else
			{
				System.out.println("Bad sizes :: normalSats = "+normalStats.size()+" :: signature = "+signature.size());
			}
		}
		/*
		 * All done! lets print some stats!
		 */
		System.out.println("Found "+normal+" normal packets and "+misuse+" misuse packets!");
		

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
	 *
	 * return arraylist of Doubles
	 *
	 */
	private static ArrayList<Double> stringToDouble(String signatureString)
	{
		ArrayList<Double> sigDouble = new ArrayList<Double>();
		String tmp[] = signatureString.split(",");
		for( int i = 0; i<tmp.length ; i++)
		{
			sigDouble.add(Double.valueOf(tmp[i]));
		}
		
		return sigDouble;
	}
	
	/*
	 * This method takes in stat values and compares them to a given signature's values and based on the securityLevel says:
	 * 
	 * return TRUE if packet falls within stats
	 * return FALSE if packet does not
	 */
	 private static boolean isInBounds(ArrayList<ArrayList<Double>> normalStats, ArrayList<Double> signature, int securityLevel)
	 {
		 int failures = 0;
		 
		 //loop through signatures
		 for(int i = 0; i<signature.size(); i++)
		 {
			 //is it < Q1?
			 if(signature.get(i) < normalStats.get(i).get(0))
			 {
				 failures++;
			 }
			 //ok so far
			 else
			 {
				 //is it >Q3?
				 if(signature.get(i) > normalStats.get(i).get(1))
				 {
					 failures++;
				 }
			 }
			 
		}
		 
		 return failures<=securityLevel;
	 }

}
