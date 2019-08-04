/*
  Optimal Page Replacement Algorithm 
*/

import java.lang.*;
import java.io.*;
import java.util.*;

 public class Opt
 {

 	private String filename;
	private int num_frames;
	private int total_mem_acc, total_pg_faults, total_writes_to_disk;
	private PTE[] RAM;
  // used on the first runthrough to holds accessed pages in sequenced order, used to simulate optimal page replacement
	private Hashtable<Integer, LinkedList<Integer>> access_table; 
	private Hashtable<Integer, Integer> page_table; // not full page table, holds accessed pages
	private BufferedReader br;

  // constructor
 	public Opt(int frames, String tracefile)
 	{
 		filename = tracefile;
 		num_frames = frames;
 		total_mem_acc = 0;
 		total_pg_faults = 0;
 		total_writes_to_disk = 0;
 		RAM = new PTE[num_frames];
 		page_table = new Hashtable<Integer, Integer>();
 		access_table = new Hashtable<Integer, LinkedList<Integer>>();
 	}


 	public void run() throws IOException
 	{
    // check for IO Errors
 		try{
      br = new BufferedReader(new FileReader(filename));
    } catch (IOException e){
      System.err.println(e);
      return;      
    }

 		int cur_page;
 		int sequence = 0;
 		String line = br.readLine();

    //first run through to simulate optimal algorithm 
 		while(line != null)
 		{
      // get the address, decode the value to an int and set it as the current page
 			String address = "0x" + line.substring(0, 5);
  		long mem_addr = Long.decode(address);            
  		cur_page = (int)(mem_addr);

      // if not in table, put it in with a new linked list
  		if(!access_table.containsKey(cur_page)){
  			access_table.put(cur_page, new LinkedList<Integer>());
  		}

      // get the linked list of the current page
  		LinkedList<Integer> next = access_table.get(cur_page);
  		next.add(sequence); // update/add the sequence number, put in table
  		access_table.put(cur_page, next);
  		sequence++;
  		line = br.readLine(); //next line
 		}

    // check for IO errors
 		try{
       	br = new BufferedReader(new FileReader(filename));
    } catch (IOException e){
       	System.err.println(e);
       	return; 
    }

    line = br.readLine();
 		char r_w;
 		int frames = 0;

 		while(line != null){	

        // get the address, decode the value to an int and set it as the current page
        String address = "0x" + line.substring(0, 5);   
     		long mem_addr = Long.decode(address);        
    		cur_page = (int)(mem_addr);
    		r_w = line.charAt(9); 
    		total_mem_acc++;

        // if room in memory, remove from access table
    		if(frames < num_frames){
    			access_table.get(cur_page).remove(0);

			   	if(page_table.containsKey(cur_page)){
					   System.out.println(address + " hit");

  					if(r_w == 'W'){ // check for dirty bit
  						int cur_frame = page_table.get(cur_page);
  						RAM[cur_frame].setDirty(true);
  					}
  				}else{
  					System.out.println(address + " page fault, no eviction");
            // add page to table, no need for eviction
  					page_table.put(cur_page, frames);
  					RAM[frames] = new PTE(cur_page);
  					RAM[frames].setReference(true);
  					RAM[frames].setValid(true);

  					if (r_w == 'W'){ // check for dirty bit
  	            int cur_frame = page_table.get(cur_page);
  	            RAM[cur_frame].setDirty(true);
  	        }

  	        total_pg_faults++;
  	        frames++;
  				}
    		}else{
            //remove page from access table
      			access_table.get(cur_page).remove(0);

            // if page is in table 
      			if(page_table.containsKey(cur_page)){
      				System.out.println(address + " hit");

    					if(r_w == 'W'){ // check for dirty bit
    						int cur_frame = page_table.get(cur_page);
    						RAM[cur_frame].setDirty(true);
    					}
      			}else{
              // need to evict the optimal page
      				total_pg_faults++; 
     			    int evicted = 0; 
        			int farthest = 0;    
              // go through all pages in RAM        
         			for (PTE page : RAM){
           			int candidate_page = page.getPageNum(); // get page number of candidate    
           			if (access_table.get(candidate_page).isEmpty()){ // if empty, choose this page to evict
             				evicted = page_table.get(candidate_page);
            			 	break;
          			}
          
           			int next_use = access_table.get(candidate_page).get(0); // get the farthese away used reference from current candidate          
           			farthest = access_table.get(RAM[evicted].getPageNum()).get(0);    
       
          			if (next_use > farthest) evicted = page_table.get(candidate_page);  // if farther, change candidate page 
    				  }

      				if (RAM[evicted].getDirty()){ //check for dirty bit, evict appropriately 
             			total_writes_to_disk++;             
             			System.out.println(address + " page fault, evict dirty");          
           		}else{
            			System.out.println(address + " page fault, evict clean");
           		}
         
              // remove page from memory and reset its attributes
        			int remove_page = RAM[evicted].getPageNum();
         			page_table.remove(remove_page);
        			RAM[evicted] = new PTE(cur_page);
        			page_table.put(cur_page, evicted);
              RAM[evicted].setReference(true);      
              RAM[evicted].setValid(true);   
         			if (r_w == 'W') RAM[evicted].setDirty(true); // check for dirty bit
      		  }
      		}
      		line = br.readLine(); // next line in file
 		}

    // summary information 
 		System.out.println("\nAlgorithm:\t\tOPT");
		System.out.println("Number of frames:\t" + num_frames);
		System.out.println("Total memory accesses:\t" + total_mem_acc);
		System.out.println("Total page faults:\t" + total_pg_faults);
		System.out.println("Total writes to disk:\t" + total_writes_to_disk);
 	}	 
 }