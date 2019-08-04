/*
  cs1550: Project 3 Virtual Memory Simulator
  Author: Jake Hauser 
  Compie: javac vmsim.java
*/
import java.io.*;

public class vmsim{

	public static void main(String[] args) throws IOException
	{
		// check for valid arg count
		if(args.length != 5 && args.length != 7){
			System.out.println("Error: invalid number of arguments");
			return;
		}

		int num_frames;
		int refresh = -1;
		String alg, tracefile;

		if(!args[0].equals("-n")){
			System.out.println("Error: First argument must be '-n'");
			return;
		}else{
			num_frames = Integer.parseInt(args[1]); //get frame count
		}

		if(!args[2].equals("-a")){
			System.out.println("Error: Third argument must be '-a'");
			return;
		}else{
			alg = args[3]; // get algorithm name 
		}

		// getting refresh rate if algorithm is nru 
		if(args.length == 7 && alg.equals("nru")){
			if(!args[4].equals("-r")){
				System.out.println("Error: Fourth argument must be '-r' for NRU algorithm");
			}else{
				refresh = Integer.parseInt(args[5]);
			}
		}else if(args.length == 7 && !alg.equals("nru")){
			System.out.println("Error: refresh argument only for NRU alogorithm");
			return;
		}

		// no matter the arg count, tracefile will be list argument
		tracefile = args[args.length - 1];

		// start simulation with the appropriate algorithm
		if(alg.equals("opt")){
			Opt OPT = new Opt(num_frames, tracefile);
			OPT.run();
		}else if(alg.equals("clock")){
			Clock clock = new Clock(num_frames, tracefile);
			clock.run();
		}else if(alg.equals("fifo")){
			FIFO fifo = new FIFO(num_frames, tracefile);
			fifo.run();
		}else if(alg.equals("nru")){
			NRU nru = new NRU(num_frames, tracefile, refresh);
			nru.run();
		}else{
			System.out.println("Error: invalid alogorithm name");
			return; 
		}
	}
}