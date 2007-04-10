import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
		//Get signatures from normal file
		ArrayList normalSignatures = getSignaturesFromFile("./data/Optimized_Normal-TRAINER");

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

}
