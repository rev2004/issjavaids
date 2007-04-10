import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/*
 * ISS B Data Parser
 * 
 * This program created the B Data Files for IDS Testing
 * It randomly selects from the following files:
 * 30 		Buffer Overflow
 * 30 		Guess Password
 * 30 		Port Sweep
 * 30 		Satan
 * 30 		Rootkit
 * 2000 	Normal Situation Records
 * 
 * It then removes the chosen signatures form the files and puts
 * them into a new file.
 * 
 */

public class ISS_B_Data_Parser {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ArrayList <String> FileArray = new ArrayList<String>();
		FileArray.add("optimized_attacks_normal/Optimized_BufferOverflow");
		FileArray.add("optimized_attacks_normal/Optimized_GuessPassword");
		FileArray.add("optimized_attacks_normal/Optimized_PortSweep");
		FileArray.add("optimized_attacks_normal/Optimized_Satan");
		FileArray.add("optimized_attacks_normal/Optimized_Rootkit");
		
		for( String Foo : FileArray)
		{
			if(Foo.equalsIgnoreCase("optimized_attacks_normal/Optimized_Rootkit"))
			{
				getSignatures(Foo,10);
			}
			else
			{
				getSignatures(Foo,30);
			}
		}
		
		//now, get the normal content
		getSignatures("optimized_attacks_normal/Optimized_Normal", 2000);

	}

	private static void getSignatures(String file, int results) 
	{
		BufferedReader input = null;
		BufferedWriter testOutput = null;
		BufferedWriter trainerOutput = null;
		try 
		{
			input = new BufferedReader( new FileReader(file) );
			testOutput = new BufferedWriter( new FileWriter("optimized_attacks_normal/Section B Data/TEST.DATA",true));
			trainerOutput = new BufferedWriter( new FileWriter("optimized_attacks_normal/Section B Data/"+file.substring(25)+"-TRAINER",true));
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
			
			Random generator = new Random();
			String chosenOne = null;
			for( int i = 0; i<results; i++)
			{
				int number = generator.nextInt(signatureList.size());
				//select chosen one
				chosenOne = signatureList.get(number);
				
				//append to file
				testOutput.write(chosenOne);
				
				//remove chosen one from arraylist
				signatureList.remove(number);
				
			}
			// now take remaining signatures and write to new file
			for( String sig : signatureList)
			{
				trainerOutput.write(sig);
			}
			
			input.close();
			testOutput.close();
			trainerOutput.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}

}
