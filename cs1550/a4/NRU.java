/*
  Not Recently Used Page Replacement Algorithm
*/

import java.io.*;
import java.util.*;

public class NRU 
{

	private String filename;
	private int num_frames, refresh;
	private int total_mem_acc, total_pg_faults, total_writes_to_disk;
	private PTE[] RAM;
	private Hashtable<Integer, Integer> page_table; //not full page table, just stores already accessed pages
	private BufferedReader br;
	private ArrayList<PTE> class0, class1, class2, class3; // the four classes which pages will be stored in 
	private char r_w;
	private Random rand; //rand for selecting random page

	// constructor 
 	public NRU(int frames, String tracefile, int refresh_rate)
 	{
 		filename = tracefile;
 		num_frames = frames;
 		total_mem_acc = 0;
 		total_pg_faults = 0;
 		total_writes_to_disk = 0;
 		refresh = refresh_rate;
 		rand = new Random();
 		page_table = new Hashtable<Integer, Integer>();
 		RAM = new PTE[num_frames];
 		class0 = new ArrayList<PTE>(num_frames);
 		class1 = new ArrayList<PTE>(num_frames);
 		class2 = new ArrayList<PTE>(num_frames);
 		class3 = new ArrayList<PTE>(num_frames);
 	}

 	public void run() throws IOException 
 	{
 		// check for IO error
 		try{
			br = new BufferedReader(new FileReader(filename));
		} catch(Exception e){
			System.err.println(e);
			return;
		}

		String line = br.readLine();
		int frames_loaded = 0;

		while(line != null){
			// get the address, decode the value to an int and set it as the current page
			String address = "0x" + line.substring(0, 5);
  			long mem_addr = Long.decode(address);            
  			int cur_page = (int)(mem_addr);
  			r_w = line.charAt(9); // get read write bit
  			total_mem_acc++;

  			if(frames_loaded < num_frames){ // if there is room in physical memory
  				if(page_table.containsKey(cur_page)){
  					System.out.println(address + " hit");
  					// no check for dirty bit, as it should be in the right class anyway
  				}else{
  					System.out.println(address + " page fault, no eviction");
  					page_table.put(cur_page, frames_loaded);
  					RAM[frames_loaded] = new PTE(cur_page);
  					RAM[frames_loaded].setReference(true);
  					RAM[frames_loaded].setValid(true);

  					// check for dirty bit and add to the appropriate class 
  					if(r_w == 'W'){
  						RAM[frames_loaded].setDirty(true);
  						class3.add(RAM[frames_loaded]);
  					}else{
  						class2.add(RAM[frames_loaded]);
  					}

  					total_pg_faults++;
  					frames_loaded++;
  				}
  			}else{

  				if(page_table.containsKey(cur_page)){
  					System.out.println(address + " hit");
  					// again no dirty bit check, should be in the proper class anyway
  				}else{
  					total_pg_faults++;
					PTE evicted;
					int evict_index = -1;
					int class_index = -1;

					// go through the classes and find the earliest available one to evict from 
					// evicted page is selected randomly among the class
					if(class0.size() < 1) {
						if(class1.size() < 1) {
							if(class2.size() < 1) {
								class_index = rand.nextInt(class3.size());
								evicted = class3.get(class_index);
								class3.remove(evicted);
							}
							else {
								class_index = rand.nextInt(class2.size());
								evicted = class2.get(class_index);
								class2.remove(evicted);
							}
						}
						else {

							class_index = rand.nextInt(class1.size());
							evicted = class1.get(class_index);
							class1.remove(evicted);
						}
					}
					else {
						class_index = rand.nextInt(class0.size());
						evicted = class0.get(class_index);
						class0.remove(evicted);	

					}
					evict_index = page_table.get(evicted.getPageNum());
						
					if(RAM[evict_index].getDirty()){ //check for dirty bit, evict appropriately
						total_writes_to_disk++;
						System.out.println(address + " page fault, evict dirty");
					}else{
						System.out.println(address + " page fault, evict clean");
					}

					// store new page in RAM, set attributes properly
					page_table.remove(evicted.getPageNum());
					RAM[evict_index] = new PTE(cur_page);
					RAM[evict_index].setReference(true);
					RAM[evict_index].setValid(true);
					page_table.put(cur_page, evict_index);

					if(r_w == 'W'){
						RAM[evict_index].setDirty(true);
	  					class3.add(RAM[evict_index]);
					}else{
						class2.add(RAM[evict_index]);
					}

	  			}
  			}

  			// check if refresh is needed at the end
  			// simply add all class3 pages to class1, and all class 2 pages to class 0
  			if(total_mem_acc % refresh == 0){
  				for(PTE page : class3){
  					page.setReference(false);
  					class1.add(page);
  				}
  				class3.clear();

  				for(PTE page: class2){
  					page.setReference(false);
  					class0.add(page);
  				}
  				class2.clear();
  			}

  			line = br.readLine(); //next line
  		}

  		// summary information
		System.out.println("\nAlgorithm:\t\tNRU");
		System.out.println("Number of frames:\t" + num_frames);
		System.out.println("Total memory accesses:\t" + total_mem_acc);
		System.out.println("Total page faults:\t" + total_pg_faults);
		System.out.println("Total writes to disk:\t" + total_writes_to_disk);
 	}
}