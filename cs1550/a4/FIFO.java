/*
  First In First Out Page Replacement Algorithm 
*/

import java.util.*;
import java.io.*;

public class FIFO
{
	private String filename;
	private BufferedReader br;
	private LinkedList<PTE> mem_queue; //the queue of pages in memory
	private Hashtable<Integer, Integer> page_table; // hold accessed pages and corresponding frame
	private int num_frames, cur_page, frames_loaded;
	private int total_mem_acc, total_pg_faults, total_writes_to_disk;
	private char r_w;

	public FIFO(int frames, String tracefile)
	{
		filename = tracefile;
		num_frames = frames;
		page_table = new Hashtable<Integer, Integer>();
		mem_queue = new LinkedList<PTE>();
		total_pg_faults = 0;
		total_mem_acc = 0;
		total_writes_to_disk = 0; 
		frames_loaded = 0;
	}

	public void run() throws IOException
	{
    // check for IO Error
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
  			r_w = line.charAt(9); // chec, for dirty bit
  			total_mem_acc++;

        // if there is room in in memory 
  			if(frames_loaded < num_frames){
  				if(page_table.containsKey(cur_page)){
  					System.out.println(address + " hit");

  					if(r_w == 'W'){ //check for dirty bit
  						int cur_frame = page_table.get(cur_page);
  						mem_queue.get(cur_frame).setDirty(true);
  					}
  				}else{
  					System.out.println(address +  " page fault, no eviction");
  					PTE newPTE = new PTE(cur_page);
  					mem_queue.add(newPTE);     // add page to the queue
  					int queue_i = mem_queue.indexOf(newPTE); //get queue index
  					page_table.put(cur_page, queue_i); // put the index in the hastable of accessed pages
  					mem_queue.get(queue_i).setReference(true);
  					mem_queue.get(queue_i).setValid(true);
  					total_pg_faults++;
  					frames_loaded++;

  					if(r_w == 'W'){ // check for dirty bit
  						int cur_frame = page_table.get(cur_page);
  						mem_queue.get(cur_frame).setDirty(true);
  					}
  				}
  			}else{
  				if(page_table.containsKey(cur_page)){
  					System.out.println(address + " hit");

  					if(r_w == 'W'){
  						int cur_frame = page_table.get(cur_page);
  						mem_queue.get(cur_frame).setDirty(true);
  					}
  				}else{ // eviction needed 
  					total_pg_faults++;
            // simply remove the first page in the queue (ie FIFO)
  					PTE evicted = mem_queue.removeFirst();
  					int evicted_page_num = evicted.getPageNum();

  					if(evicted.getDirty()){ //check for dirty bit and evict appropriately 
  						total_writes_to_disk++;
  						System.out.println(address + " page fault, evict dirty");
  					}else{
  						System.out.println(address + " page fault, evict clean");
  					}

            // remove evicted page from hastable 
  					page_table.remove(evicted_page_num);

            // add a new page to the queue
  					PTE newPTE = new PTE(cur_page);
  					mem_queue.add(newPTE);
  					int queue_i = mem_queue.indexOf(newPTE);
  					page_table.put(cur_page, queue_i);
  					mem_queue.get(queue_i).setReference(true); // set info
  					mem_queue.get(queue_i).setValid(true);

  					if(r_w == 'W'){ // check for dirty bit
  						int cur_frame = page_table.get(cur_page);
  						mem_queue.get(cur_frame).setDirty(true);
  					}
  				}
  			}

  			line = br.readLine(); // next line in the file 
  		}

    // summary info 
  	System.out.println("\nAlgorithm:\t\tFIFO");
		System.out.println("Number of frames:\t" + num_frames);
		System.out.println("Total memory accesses:\t" + total_mem_acc);
		System.out.println("Total page faults:\t" + total_pg_faults);
		System.out.println("Total writes to disk:\t" + total_writes_to_disk);
	}
}