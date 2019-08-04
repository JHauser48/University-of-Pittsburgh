/*
  Clock (second chance) Page Replacement Algorithm
*/
import java.util.*;
import java.io.*;

public class Clock
{
	private String filename;
	private BufferedReader br;
	private PTE[] RAM; //physical memory 
	private Hashtable<Integer, Integer> page_table; // not a full page table, just stores pages that have been accessed already and corresponding frame in RAM
	private int clock_hand, num_frames, cur_page, frames_loaded;
	private int total_mem_acc, total_pg_faults, total_writes_to_disk;
	private char r_w;

  // constructor 
	public Clock(int frames, String tracefile)
	{
		total_mem_acc = 0;
		total_pg_faults = 0;
		total_writes_to_disk = 0;
		num_frames = frames;
		filename = tracefile;
		page_table = new Hashtable<Integer, Integer>();
		RAM = new PTE[num_frames];
		clock_hand = 0;
		frames_loaded = 0;
	}

	public void run() throws IOException 
	{
    // check for IO errors
		try{
			br = new BufferedReader(new FileReader(filename));
		} catch(Exception e){
			System.err.println(e);
			return;
		}

		String line = br.readLine(); 

		while(line != null){

      		// get the address, decode the value to an int and set it as the current page
		  	String address = "0x" + line.substring(0, 5);
			long mem_addr = Long.decode(address);            
			cur_page = (int)(mem_addr);
			r_w = line.charAt(9); // will be 'R' or 'W'
			total_mem_acc++;


			if(frames_loaded < num_frames){ // if there is room in physical memory
				if(page_table.containsKey(cur_page)){
					System.out.println(address + " hit");
					int cur_frame = page_table.get(cur_page); //get the corresponding frame in RAM
					
					if(r_w == 'W') RAM[cur_frame].setDirty(true); //check for dirty bit
					RAM[cur_frame].setReference(true);
				}else{
					System.out.println(address + "page fault, no eviction"); //page fault, add the page to RAM
					page_table.put(cur_page, frames_loaded);
					RAM[frames_loaded] = new PTE(cur_page);
					if(r_w == 'W') RAM[frames_loaded].setDirty(true); //check for dirty but
					RAM[frames_loaded].setReference(true); // set page information
			   	RAM[frames_loaded].setValid(true);
				  total_pg_faults++;
				  frames_loaded++;
				}
			}else{
       			 // no room in RAM
				if(page_table.containsKey(cur_page)){
					System.out.println(address + " hit"); //if already in page_table
					int cur_frame = page_table.get(cur_page);
					
					if(r_w == 'W') RAM[cur_frame].setDirty(true); //check for dirty bit
					RAM[cur_frame].setReference(true);
				}else{
          			//find a page to evict using clock hand 
					int evicted = -1;

					while(evicted < 0){
						if(RAM[clock_hand].getReference()){
							RAM[clock_hand].setReference(false);
						}else{
							evicted = clock_hand;
						}

						if(clock_hand < num_frames - 1){
							clock_hand++;
						}else{
							clock_hand = 0; // reset clock hand if its at the "end"
						}
					}

					if(RAM[evicted].getDirty()){ //if dirty, dirty evict, otherwise clean evict 
						total_writes_to_disk++;
						System.out.println(address + ": page fault - evict dirty");      
					}else{
						System.out.println(address + ": page fault - evict clean");      
					}

       			   // reset all page info 
					RAM[evicted].setDirty(false);
					RAM[evicted].setReference(false);
					RAM[evicted].setValid(false);

          			// remove evicted page 
					page_table.remove(RAM[evicted].getPageNum());
					RAM[evicted] = new PTE(cur_page);
					page_table.put(cur_page, evicted);
					if(r_w == 'W') RAM[evicted].setDirty(true);
				  RAM[evicted].setDirty(false);
					RAM[evicted].setReference(false);
					RAM[evicted].setValid(false);
					total_pg_faults++;
				}
			}
    		  //next line in file 
  			line = br.readLine();
		}

   		// summary information
		System.out.println("\nAlgorithm:\t\tClock");
		System.out.println("Number of frames:\t" + num_frames);
		System.out.println("Total memory accesses:\t" + total_mem_acc);
		System.out.println("Total page faults:\t" + total_pg_faults);
		System.out.println("Total writes to disk:\t" + total_writes_to_disk);
	}
}