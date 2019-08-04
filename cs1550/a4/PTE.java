/*
  Page Table Entry class for vmsim algorithms 
*/

public class PTE{
	private int pn;
	private boolean d;
	private boolean r; 
	private boolean v;

	// constructor 
	// dirty, valid and ref are booleans since they are just 0 or 1 anyway 
	public PTE(int page_num){
		pn = page_num;
		d = false;
		r = false;
		v = false;
	}

	// getter and setter methods

	public int getPageNum()
	{
		return pn;
	}

	public boolean getValid()
	{
		return v;
	}

	public boolean getReference()
	{
		return r;
	}

	public boolean getDirty()
	{
		return d;
	}
	
    public void setValid(boolean valid)
    {
    	v = valid;
    }

	public void setReference(boolean ref)
	{
		r = ref;
	}

	public void setDirty(boolean dirty)
	{
		d = dirty; 
	}
}