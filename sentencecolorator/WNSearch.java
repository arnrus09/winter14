package sentencecolorator;

import java.io.*;
import java.util.*;

public class WNSearch{
	/*static final String setPath = "PATH=\"/usr/local/WordNet-3.0/bin:${PATH}\"\nexport PATH\nwn ";
	static File home = new File("/Users/russellarnold");*/
	
	public static void main(String[] args) throws Exception{
		String old;
		String neww;
		long start1 = System.nanoTime();
		neww = searchWn("physical", "syns","a");
		long end1 = System.nanoTime();
		long envTime = end1 - start1;
	/*	long start2 = System.nanoTime();
		old = searchWnOld("chemist", "syns","n");
		long end2 = System.nanoTime();
		long nonEnvTime = end2 - start2;
		System.out.println("env: "+envTime+"\n"+"no Env: "+nonEnvTime+"\nratio: "+((float)nonEnvTime/(float)envTime));
		System.out.println(old);*/
		System.out.println(neww); 
		
	}
		
	/*public static String searchWnOld(String word, String searchArg, String usage)  throws IOException {
		try{
		searchScript(word ,searchArg + usage);
		}
		catch(Exception e){
			System.out.println( e.toString() );
		}
		ProcessBuilder access = new ProcessBuilder("chmod","a+x","wnscript");
		access.directory(home);
		access.start();
		ProcessBuilder pb = new ProcessBuilder("./wnscript");
		pb.directory(home);
		Process p = pb.start();
		String output = convertStreamToString(p.getInputStream());
		return output;
	}*/
	public static String searchWn(String word, String searchArg, String usage)  throws IOException {
		if(searchArg.equals("associate") )
			searchArg = ( usage.equals("n") || usage.equals("v") ) ? "tree" : "pert";
		ProcessBuilder search = new ProcessBuilder("wn", word, "-"+searchArg+usage);
		Process searchProcess = search.start();
		String output = Utilities.convertStreamToString(searchProcess.getInputStream());
		return output;
		
	}

	
	/*static void searchScript(String word, String searchArg) throws IOException{
		FileWriter fe = new FileWriter("/Users/russellarnold/wnscript");
		fe.write(setPath + word + " -" + searchArg);
		fe.close();
	}*/
}

