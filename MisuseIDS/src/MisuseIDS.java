/*
 * MisuseIDS.java
 * 
 * This class test incoming data packets and determines if 
 * it is a security threat to the system. The incoming variables
 * are defined as follows:
 * 
 * @param stats - Arraylist populated with 1st and 3rd quartile thresholds
 * @param signature - Arraylist populated with incoming packet to be evaluated
 * @param secLevel - Integer representing the number of allowed out-of-bounds fields
 * 
 */

		
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
		


public class MisuseIDS
{

	//This variable defines how strict we match stats, if number of deviations is > than SECURITY_LEVEL; isInBonds returns FALSE
	private static final int SECURITY_LEVEL = 3;

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		/*
		 * setup an arraylist of 41 arrays that contain a Q1 and Q3 value of the normal data from a normal file
		 */
		BSReader myBOReader = new BSReader();
		BSReader myGPReader = new BSReader();
		BSReader myPSReader = new BSReader();
		BSReader myRReader = new BSReader();
		BSReader mySReader = new BSReader();
		
		ArrayList<ArrayList<Double>> bufferOverflowStats = myBOReader.getRange("./data/Optimized_BufferOverflow-TRAINER");
		ArrayList<ArrayList<Double>> guessPasswordStats = myGPReader.getRange("./data/Optimized_GuessPassword-TRAINER");
		ArrayList<ArrayList<Double>> portSweepStats = myPSReader.getRange("./data/Optimized_PortSweep-TRAINER");		
		ArrayList<ArrayList<Double>> rootkitStats = myRReader.getRange("./data/Optimized_Rootkit-TRAINER");
		ArrayList<ArrayList<Double>> satanStats = mySReader.getRange("./data/Optimized_Satan-TRAINER");		
		
		//now remove the ones we don't want (2,3,4,7,12,21,22)
		//adjust for array starts at 0 (so subtract 1
		bufferOverflowStats.remove(21);
		bufferOverflowStats.remove(20);
		bufferOverflowStats.remove(11);
		bufferOverflowStats.remove(6);
		bufferOverflowStats.remove(3);
		bufferOverflowStats.remove(2);
		bufferOverflowStats.remove(1);
		
		guessPasswordStats.remove(21);
		guessPasswordStats.remove(20);
		guessPasswordStats.remove(11);
		guessPasswordStats.remove(6);
		guessPasswordStats.remove(3);
		guessPasswordStats.remove(2);
		guessPasswordStats.remove(1);
		
		portSweepStats.remove(21);
		portSweepStats.remove(20);
		portSweepStats.remove(11);
		portSweepStats.remove(6);
		portSweepStats.remove(3);
		portSweepStats.remove(2);
		portSweepStats.remove(1);
		
		rootkitStats.remove(21);
		rootkitStats.remove(20);
		rootkitStats.remove(11);
		rootkitStats.remove(6);
		rootkitStats.remove(3);
		rootkitStats.remove(2);
		rootkitStats.remove(1);
		
		satanStats.remove(21);
		satanStats.remove(20);
		satanStats.remove(11);
		satanStats.remove(6);
		satanStats.remove(3);
		satanStats.remove(2);
		satanStats.remove(1);
		
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
		int bufferMisuse = 0, passMisuse = 0, portMisuse = 0, rootkitMisuse = 0, satanMisuse = 0;
		int total = 0, normal = 0;

		ArrayList<Double> signature;
		
		for(String currString : signatureStrings)
		{
			boolean detected = false;
			signature = stringToDouble(currString);
			//filter out what we dont want to compare 1 2 3 6 11 20 21
			signature.remove(21);
			signature.remove(20);
			signature.remove(11);
			signature.remove(6);
			signature.remove(3);
			signature.remove(2);
			signature.remove(1);
			
			//check to make sure sizes correct
			if(bufferOverflowStats.size() == signature.size())
			{
				if(isInBounds(bufferOverflowStats, signature, SECURITY_LEVEL) && !detected)
				{
					bufferMisuse++;
					detected = true;
				}
			
			}
			else
			{
				System.out.println("Bad sizes :: bufferOverflowSats = "+bufferOverflowStats.size()+" :: signature = "+signature.size());
			}
			
			if(guessPasswordStats.size() == signature.size())
			{
				if(isInBounds(guessPasswordStats, signature, SECURITY_LEVEL) && !detected)
				{
					passMisuse++;
					detected = true;
				}
			}
			else
			{
				System.out.println("Bad sizes :: guessPasswordSats = "+guessPasswordStats.size()+" :: signature = "+signature.size());
			}
			
			if(portSweepStats.size() == signature.size())
			{
				if(isInBounds(portSweepStats, signature, SECURITY_LEVEL) && !detected)
				{
					portMisuse++;
					detected = true;
				}
			}
			else
			{
				System.out.println("Bad sizes :: portSweepSats = "+portSweepStats.size()+" :: signature = "+signature.size());
			}
			
			if(satanStats.size() == signature.size())
			{
				if(isInBounds(satanStats, signature, SECURITY_LEVEL) && !detected)
				{
					satanMisuse++;
					detected = true;
				}
			}
			else
			{
				System.out.println("Bad sizes :: satanStats = "+satanStats.size()+" :: signature = "+signature.size());
			}
			
			if(rootkitStats.size() == signature.size())
			{
				if(isInBounds(rootkitStats, signature, SECURITY_LEVEL) && !detected)
				{
					rootkitMisuse++;
					detected = true;
				}
			}
			else
			{
				System.out.println("Bad sizes :: rootkitSats = "+rootkitStats.size()+" :: signature = "+signature.size());
			}
			
			total++;
			
			if(!detected)
				normal++;
		}
		/*
		 * All done! lets print some stats!
		 */
		System.out.println(total+" total packets scanned.");
		System.out.println(normal+" normal packets scanned.");
		System.out.println("Found "+bufferMisuse+" BufferOverflow misuse packets.");
		System.out.println("Found "+passMisuse+" GuessPassword misuse packets.");
		System.out.println("Found "+portMisuse+" PortSweep misuse packets.");
		System.out.println("Found "+rootkitMisuse+" Rootkit misuse packets.");
		System.out.println("Found "+satanMisuse+" Satan misuse packets.");
		
		BufferedWriter misuseOutput = null;
		try 
		{
			misuseOutput = new BufferedWriter( new FileWriter("data/README.DATA",true));
			misuseOutput.write(total+" total packets scanned. \n");
			misuseOutput.write(normal+" normal packets scanned. \n");
			misuseOutput.write("Found "+bufferMisuse+" BufferOverflow misuse packets. \n");
			misuseOutput.write("Found "+passMisuse+" GuessPassword misuse packets. \n");
			misuseOutput.write("Found "+portMisuse+" PortSweep misuse packets. \n");
			misuseOutput.write("Found "+rootkitMisuse+" Rootkit misuse packets. \n");
			misuseOutput.write("Found "+satanMisuse+" Satan misuse packets. \n\n");
			
			misuseOutput.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		

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