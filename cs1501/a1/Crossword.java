//Jake Hauser (3928675) 
//CS 1501 Summer 2018

import java.io.*;
import java.util.*;
import java.lang.*;

public class Crossword
{

	public static int cross_n; //the length of a side in the crossword puzzle 
	public static String dictType;
	public static int solutionCount = -1; //starts at -1, first solution increments to 0

	//prints the puzzle
	public static void printCrossword(char[][] crossword) 
	{
		System.out.println();
		char[][] toPrint = new char[cross_n][cross_n];

		if(crossword == null && dictType.equals("DLB") && solutionCount >= 0){
			System.out.println("   Solution Count: " + (solutionCount + 1)); //solution count if DLB
		}else if(crossword == null && solutionCount == -1){
			System.out.println("   No Solution");
		}else{
			
			for(int i = 0; i < cross_n; i++){
				for(int j = 0; j < cross_n; j++){
					toPrint[i][j] = crossword[i][j];
				}
			}		

			//convert any presets back to lowercase values 
			for(int i = 0; i < cross_n; i++){
				for(int j = 0; j < cross_n; j++){
					if(Character.isUpperCase(toPrint[i][j])){
						toPrint[i][j] = Character.toLowerCase(toPrint[i][j]);
					}
				}	
			}		

			//prints a new line after priting a full row 
			for(int i = 0; i < cross_n; i++){
				for(int j = 0; j < cross_n; j++){
					if(j == 0 && i != 0){  //to print each row on its own line       
						System.out.println();
						System.out.print("   " + toPrint[i][j]);
					}else{
						System.out.print("   " + toPrint[i][j]);	
					}		
				}
			}		
		}

		System.out.println();
	}	

	//reads in crossword to 2D array
	public static char[][] readCross(String filename) 
		throws IOException
	{
		Scanner s = new Scanner(new FileInputStream(filename));
		cross_n = s.nextInt();
		StringBuilder sb;
		String st; 
		char[][] crossword = new char[cross_n][cross_n]; //make 2D array size n x n

		s.nextLine();

		for(int i = 0; i < cross_n; i++){

			st = s.nextLine();         //read n next word and turn into StringBuilder  
			sb = new StringBuilder(st);

			for (int j = 0; j < sb.length(); j++){
				crossword[i][j] = sb.charAt(j); //transfer each char to corresponding place in 2D matrix 
			}
		}

		return crossword;
	}

	//convert presets to uppercase so they can be ignored until checking
	public static char[][] convertPresetChars(char[][] crossword) 
	{
		for(int i = 0; i < cross_n; i++){
			for(int j = 0; j < cross_n; j++){
				if(crossword[i][j] != '-' && crossword[i][j] != '+'){
					crossword[i][j] = Character.toUpperCase(crossword[i][j]);
				}
			}
		}
		return crossword;
	}

	 //checks if the crossword is a full solution, returns true if complete 
	public static boolean isSolution(char[][] crossword)
	{
		for(int i = 0; i < cross_n; i++){
			for(int j = 0; j < cross_n; j++){
				if(crossword[i][j] == '+'){ //if unfilled square is found, return false 
					return false;
				}
			}
		}
		return true; //othwerwise its good to go 
	}

	//the pruning part of the algorithm, will return true if crossword is rejected
	//see helper methods for full details
	public static boolean reject(char[][] crossword, DictInterface D) 
	{
		if(checkRows(crossword, D) == true){
			return true;
		}

		if(checkCols(crossword, D) == true){
			return true;
		}

		return false; //if it reaches this point, the crossword is valid and reject returns false
	}

	//extends the puzzle by placing the letter 'a' in the next available spot in the 2D array 
	public static char[][] extend(char[][] crossword)
	{
		char[][] temp = new char[cross_n][cross_n]; //temp 2-D array that is returned as new crossword at the end of the method
        boolean spaceFilled = false; //indicates if an index contains a value

        for(int i = 0; i < cross_n; i++){
            for(int j = 0; j < cross_n; j++) {
        		if(crossword != null){
        		    if(crossword[i][j] != '+'){
                    //transfer all currently filled indexes to temp
                    	temp[i][j] = crossword[i][j];
	                }else if(crossword[i][j] == '+' && spaceFilled == false){
	                    //upon finding the first available empty index, set its value to letter 'a'
	                    temp[i][j] = 'a';
	                    spaceFilled = true; //set to true so the rest of the puzzle copies to temp normally
	                }else if(crossword[i][j] == '+' && spaceFilled == true){
	                    temp[i][j] = crossword[i][j];
	                }
              	}  
            }
        }
        if(spaceFilled == false){
            //if spaceFilled remains false, it means the square was full and could not be extended, returns null
            return null;
        }else{
            return temp;
        }
	}

	//iterates to the next letter in the current index being checked
	public static char[][] nextLetter(char[][] crossword)
	{

        //starting at the bottom right index of a square, iterate backwards until the appropriate index is found
        for(int i = cross_n - 1; i >= 0; i--) {
            for (int j = cross_n - 1; j >= 0; j--) {

                if(crossword[i][j] == 'z'){
                    //if the value is already maxed out, return null and backtrack
                    return null;
                }else if(crossword[i][j] >= 'a' && crossword[i][j] < 'z'){
                    //this value will be the last index that was extended or next'd
                    crossword[i][j] += 1; //add 1 to the value and return the crossword
                   // printCrossword(crossword);
                    return crossword;
                }
            }
        }
        return null;
	}

	//the recursive solve algorithm which uses the above methods to prune and backtrack 
	public static char[][] solve(char[][] crossword, DictInterface D) {
        if (reject(crossword, D) ==  true){ //if rejected, return null
			return null;
        }
        if (isSolution(crossword)){ //if a solution, return the completed puzzle 
        	if(dictType.equals("DLB")){

        		solutionCount++; //add one to solution count

        		if(solutionCount % 10000 == 0){ //print if needed 
        			System.out.println("\nSolution " + solutionCount + ":");
        			printCrossword(crossword);
        		}
        
        	}else{
        		return crossword; //if not DLB just return the crossword 
        	}
        }

        char[][] attempt = extend(crossword); //create an attempt by extending one letter

        while (attempt != null) {
            char[][] solution;
            solution = solve(attempt, D);  //make recursive call to solve to check attempt
            if (solution != null){
				return solution;		   //if valid, the algorithm will move to the next index
            }
            attempt = nextLetter(attempt); //else it will iterate to the next letter to check the dictionary 
        }

        return null; //if reaches this point, the crossword returns as null and backtracks, incremeting the letter of the previous index 
    }


	public static boolean checkRows(char[][] crossword, DictInterface D)
	{
		StringBuilder sb;
		int rowCount = 0; //current row being checked 
		boolean rowUsed = false; //if a row has been used
		boolean rowDone = false; //if the string for that row is done

		 //checks every row 
		while(rowCount < cross_n){

			sb = new StringBuilder();
			rowUsed = false;
			rowDone = false;
			boolean splitRow = false;
			boolean deleteChar = false;
			int start = 0;

			//this loop builds the sb to be checked in the search methods	
			for(int i = 0; i < cross_n; i++){

				if(crossword[rowCount][i] == '+' && rowUsed == true){
					rowDone = true;	 
				}

				if(crossword[rowCount][i] != '+' && rowDone == true){
					
				}else if(crossword[rowCount][i] != '+'){
					sb.append(crossword[rowCount][i]);	
					rowUsed = true;		
				}
			}

			//converts preset values back to lowercase 
			if(sb.length() > 0){
				for (int i = 0; i < sb.length(); i++){
   					char c = sb.charAt(i);
   					if(c != '-'){
   						sb.setCharAt(i, Character.toLowerCase(c));	
   					}
				}
			}

			for (int i = 0; i < sb.length(); i++){

				//if null, dont bother with any other checking
				if(sb.length() == 0){
					break;
				}

				char check = sb.charAt(i);
					
				//this accounts for - marks and searches accordingly 	
				if(check == '-'){
					splitRow = true;

					if(sb.length() == 1){

					}else if(i == 0){
						sb.deleteCharAt(i);
						splitRow = false;
						deleteChar = true;
					}else if(D.searchPrefix(sb, start, i-1) == 0){
						return true;
					}else if(D.searchPrefix(sb, start, i-1) == 1){
						//return true;
					}else{
						start = i+1;
					}
				}	

				//final check of the stringbuider 
				if(i == sb.length() - 1 && check != '-' && splitRow == true){
					if(D.searchPrefix(sb, start, sb.length() - 1) == 0){ //if not a word or a prefix 
						return true;
					}else if(D.searchPrefix(sb, start, sb.length() - 1) == 1 && sb.length() == cross_n){ //if prefix but full length of row 
						return true;
					}
				}
			}

			//if no - marks are found, the string is checked in a more standard way 
			if(splitRow == false){
					
				if(sb.length() == 0){
					rowCount++;
				}else if(D.searchPrefix(sb) == 0){
					return true;
				}else if(D.searchPrefix(sb) == 1 && deleteChar == true && sb.length() == cross_n - 1){
					return true;	
				}else if(D.searchPrefix(sb) == 1 && sb.length() == cross_n){
					return true;
				}else{	
					rowCount++;
				}				
			}else{
				rowCount++; //increment row being checked 
			}
		}	

		//if no - marks are found, the string is checked in a more standard way 
		return false;
	}

	//same as check rows, but for columns 
	public static boolean checkCols(char[][] crossword, DictInterface D)
	{
		StringBuilder sb;
		int colCount = 0;
		boolean colUsed = false;
		boolean colDone = false;

		while(colCount < cross_n){ //checks every column 

			sb = new StringBuilder();
			colUsed = false;
			colDone = false;
			boolean splitCol = false;
			boolean deleteChar = false;
			int startCol = 0;

			//this loop builds the sb to be checked in the search methods 
			for(int i = 0; i < cross_n; i++){

				if(crossword[i][colCount] == '+' && colUsed == true){
					colDone = true;
				}

				if(crossword[i][colCount] != '+' && colDone == true){
				
				}else if(crossword[i][colCount] != '+'){
					sb.append(crossword[i][colCount]);	
					colUsed = true;
				}		
			}
			//converts preset values back to lowercase 
			if(sb.length() > 0){
				for (int i = 0; i < sb.length(); i++) {
						char c = sb.charAt(i);
						if(c != '-'){
							sb.setCharAt(i, Character.toLowerCase(c));	
						}
				}
			}


			for (int i = 0; i < sb.length(); i++){
				//if null, dont bother with any other checking
				if(sb.length() == 0){
					break;
				}

				char check = sb.charAt(i);
				//this accounts for - marks and searches accordingly 
				if(check == '-'){
					splitCol = true;

					if(sb.length() == 1){

					}else if(i == 0){
						sb.deleteCharAt(i);
						splitCol = false;
						deleteChar = true;
					}else if(D.searchPrefix(sb, startCol, i-1) == 0){
						return true;
					}else if(D.searchPrefix(sb, startCol, i-1) == 1){
						//return true;
					}else{
						startCol = i+1;	
					}

				}

				//final check of the stringbuider 
				if(i == sb.length() - 1 && check != '-' && splitCol == true){
					if(D.searchPrefix(sb, startCol, sb.length() - 1) == 0){
						return true;
					}else if(D.searchPrefix(sb, startCol, sb.length() - 1) == 1 && sb.length() == cross_n){
						return true;
					}
				} 
					
			}
			//if no - marks are found, the string is checked in a more standard way 
			if(splitCol == false){
				if(sb.length() == 0){
					colCount++;
				}else if(D.searchPrefix(sb) == 0){
					return true;
				}else if(D.searchPrefix(sb) == 1 && deleteChar == true && sb.length() == cross_n-1){
					return true;
				}else if(D.searchPrefix(sb) == 1 && sb.length() == cross_n){
					return true;
				}else{	
					colCount++;
				}
			}else{
				colCount++; //increment column being checked
			}
	
		}
		//if the method reaces this point, the puzzle is still valid and reject will return false
		return false;
	}

	public static void main(String[] args) throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream("dict8.txt"));
		String st;	
		char[][] crossword;
		DictInterface D;
		dictType = args[0];

		if (dictType.equals("DLB")){
			D = new DLB();
		}
		else{
			D = new MyDictionary();
		}

		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.add(st);
		}

		Scanner input = new Scanner(System.in);
		System.out.println("Enter a crossword filename: ");
		String filename = input.nextLine();

		crossword = readCross(filename);

		System.out.println("\nOriginal crossword:");
		printCrossword(crossword);

		crossword = convertPresetChars(crossword);

		System.out.println("\nSolving...\n");
		long startTime = System.currentTimeMillis();
		crossword = solve(crossword, D);

		long elapsedTime = System.currentTimeMillis() - startTime;
		long elapsedSeconds = elapsedTime / 1000;
		long secondsDisplay = elapsedSeconds % 60;
		long elapsedMinutes = elapsedSeconds / 60;

		System.out.println("\nFinished:");
		printCrossword(crossword);
		System.out.println("\n   Time: " + elapsedMinutes + "m" + secondsDisplay + "s");
		
		System.exit(0);
	}
}
