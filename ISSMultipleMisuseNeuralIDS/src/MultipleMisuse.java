import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.joone.helpers.factory.JooneTools;
import org.joone.io.FileInputSynapse;
import org.joone.net.NeuralNet;
import org.joone.util.NormalizerPlugIn;

/**
 * This Misuse detector detects 5 attacks
 * 
 * ABOUT THE TRAING DATA
 *  - 10 lines of Optimized_BufferOverflow
 *  - 10 lines of Optimized_GuessPassword
 *  - 10 lines of Optimized_Neptune
 *  - 10 lines of Optimized_Normal
 *  - 10 lines of Optimized_PortSweep
 *  - 10 lines of Optimized_Satan
 * 
 * @author Nicholas Pike
 *
 */
public class MultipleMisuse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int trainingRows = 60;
//		 Prepare the training and testing data set
		   FileInputSynapse fileIn = new FileInputSynapse();
		   fileIn.setInputFile(new File("data/JOONE-MULTIPLEMISUSE.DATA"));
		   fileIn.setAdvancedColumnSelector("1-35");

		   // Input data normalized between -1 and +1
		   NormalizerPlugIn normIn = new NormalizerPlugIn();
		   normIn.setAdvancedSerieSelector("1-34");
		   normIn.setMin(-1);
		   normIn.setMax(1);
		   fileIn.addPlugIn(normIn);

		   // Target data normalized between 0 and 1
		   NormalizerPlugIn normDes = new NormalizerPlugIn();
		   normDes.setAdvancedSerieSelector("35");
		   fileIn.addPlugIn(normDes);
		   
		   // Extract the training data
		   double[][] inputTrain = JooneTools.getDataFromStream(fileIn, 1, trainingRows, 1, 34);
		   double[][] desiredTrain = JooneTools.getDataFromStream(fileIn, 1, trainingRows, 35, 35);
		       
		   // Extract the testing data
		   double[][] inputTest = JooneTools.getDataFromStream(fileIn, trainingRows+1, 127, 1, 34);
		  double[][] desiredTest = JooneTools.getDataFromStream(fileIn, trainingRows+1, 127, 35, 35);
		
//		 Line 1: Create an MLP network with 3 layers [2,2,1 nodes] with a logistic output layer
		NeuralNet nnet = JooneTools.create_standard(new int[]{34,15,5}, JooneTools.LOGISTIC);

//		 Line 2: Train the network for 5000 epochs, or until the rmse < 0.01
		double rmse = JooneTools.train(nnet, inputTrain, desiredTrain,
		                       5000,      // Max epoch
		                       0.01,      // Min RMSE
		                       0,          // Epochs between ouput reports
		                       null,      // Std Output
		                       false     // Asynchronous mode
		);
		System.out.println("Training complete.");
//		 Line 3: Interrogate the network
		
		double[][] out = JooneTools.compare(nnet, inputTest, desiredTest);
		 System.out.println("Comparion of the last "+out.length+" rows:");
	        int cols = out[0].length/2;
	        for (int i=0; i < out.length; ++i) {
	            System.out.print("\nOutput: ");
	            for (int x=0; x < cols; ++x) {
	                System.out.print(out[i][x]+" ");
	            }
	            System.out.print("\tTarget: ");
	            for (int x=cols; x < cols*2; ++x) {
	                System.out.print(out[i][x]+" ");
	            }
	        }
		
	}

}
