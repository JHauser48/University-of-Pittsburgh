/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
import java.io.*;

public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int W = 9;         // codeword width
    private static int maxW = 16;
    private static boolean reset;

    public static void compress() { 
        char input = BinaryStdIn.readChar();
        TST<Integer> st = new TST<Integer>();
        StringBuilder x;

        for (int i = 0; i < R; i++){
            x = new StringBuilder();
            x.append((char) i);
            st.put(x, i);
        }
        int code = R+1;  // R is codeword for EOF

        char flag;
        if(reset == true){
            flag = 'r';
            BinaryStdOut.write(flag, W);
        }else if(reset == false){
            flag = 'n';
            BinaryStdOut.write(flag, W);
        }

        while(!BinaryStdIn.isEmpty()){

            StringBuilder sb = new StringBuilder();
            sb.append(input);

            while(st.contains(sb) && !BinaryStdIn.isEmpty()){
                sb.append(BinaryStdIn.readChar());
            }

            StringBuilder temp = new StringBuilder(sb);
            temp.deleteCharAt(temp.length() - 1);

            BinaryStdOut.write(st.get(temp), W);      // Print s's encoding.  

            if(code == L-1 && W < maxW){
                L = L*2;
                W++;
            }else if(code == L-1 && W == maxW && reset == true){
                st = new TST<Integer>();
                StringBuilder z;

                for (int i = 0; i < R; i++){
                    z = new StringBuilder();
                    z.append((char) i);
                    st.put(z, i);
                }
                code = R+1;  // R is codeword for EOF      
                L = 512;
                W = 9;        
            }

            if (code < L && sb.length() > 0){    // Add s to symbol table.
                st.put(sb, code++);
            }
            input = sb.charAt(sb.length()-1);            // Scan past s in input.

            if(BinaryStdIn.isEmpty()){
                temp = new StringBuilder();
                temp.append(input);
                BinaryStdOut.write(st.get(temp), W);      // Print s's encoding.    
            }
        }

        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void expand() {
        String[] st = new String[65536]; //65536 is 2^16, so it can hold max codeWords
        int i; // next available codeword value
        boolean resetDict = false;
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        char flag = BinaryStdIn.readChar(W);
        if(flag == 'r'){
            resetDict = true;
        }else if(flag == 'n'){
            resetDict = false;
        }

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            if(i == L-1 && W < maxW){
                L = L*2;
                W++;
            }else if(i == L-1 && W == maxW && resetDict == true){
                st = new String[65536];
                for (i = 0; i < R; i++)
                     st[i] = "" + (char) i;
                 st[i++] = ""; 
                 L = 512;
                 W = 9; 
            }

            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
           
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        //BinaryStdOut.close();
        if(args[0].equals("-")){
            if(args[1].equals("r")){
                reset = true; 
                compress();
            }else if(args[1].equals("n")){
                reset = false;
                compress();
            }else{
                throw new RuntimeException("Illegal command line argument");
            }
        }
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}