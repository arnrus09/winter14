package sentencecolorator;

import java.io.IOException;
import java.util.*;

public class WordManager {
	static Random rand = new Random(47);
	static String[] banned = {"not", "then" };	//extensible list of word not to convert
	int 
		useMeasure = 1,
		useCounter = -1,
		wordCounter = 0;
	boolean 
		foundSynset = false,
		plural = false,
		a = false,
		cap = false,
		comparative = false,
		superlative = false;
	String 
		word,
		current,
		tag,
		usage;
	WordManager previous = null;
	String[][] allUses;
	Inflector inflector = Inflector.getInstance();
	
	public WordManager(String word){
		this.word = word;
	}

	public WordManager(String taggedWord, String searcharg){
		word = taggedWord.substring(0, taggedWord.indexOf("_"));
		tag = taggedWord.substring(taggedWord.indexOf("_")+1);
		cap = word.substring(0, 1).matches("[A-Z]");
		String searchWord = word;
		current = word;
		for(String bw : banned)
			if(word.equals(bw) )
				return;
		if(word.matches("an?")){
			a = true;
			return;
		}
		if(tag.contains("V")){ usage = "v"; searchWord = ProcessTenses.toFpp(word);}
		else if(tag.contains("JJ")) usage = "a";
		else if(tag.contains("RB")) usage = "r";
		else if(tag.contains("NN")){ usage = "n";
			if( tag.endsWith("S")){
				searchWord = inflector.singularize(word);
				plural = true;
			}
		}
		else return;
		
		if( usage.equals("a") || usage.equals("r") ){
			comparative = tag.endsWith("R") ? true : false;
			superlative = tag.endsWith("S") ? true : false;

		}
		try{
			if(usage.equals("n") || usage.equals("a")){
				String[][] temp1 = JWNSearch.getSenses( searchWord, searcharg, usage );
				String[][] temp2 = JWNSearch.getSenses( searchWord, "attr", usage );
				allUses = new String[temp1.length + temp2.length][];
				for(int i = 0; i < temp1.length; i++)
					allUses[i] = temp1[i];
				for(int i = 0; i < temp2.length; i++)
					allUses[temp1.length + i] = temp2[i];
			}
			else
				allUses = JWNSearch.getSenses( searchWord, searcharg, usage );
				foundSynset = allUses.length >= 1 ? true : false;
		}
		catch(Exception e){
			System.out.println(e.toString());
		}

		}
	
	
	
	public String retrieve(){
			
		if(!foundSynset)	return word;
		useMeasure++;
		useCounter = useMeasure % allUses.length;
		int bounds = allUses[useCounter].length;
		wordCounter = (useMeasure / allUses.length) % bounds;
		String searchResult = allUses[useCounter][wordCounter];
		if( usage.equals("v")){
			current = ProcessTenses.revert(searchResult, tag);
		}
		else if( plural ){
			current = inflector.pluralize(searchResult);
		}
		else if( comparative ){
			current = "more " + searchResult;
		}
		else if( superlative ){
			current = "most " + searchResult;
		}
		else
			current = searchResult;
		if( current.toUpperCase().equals(current))
			current = current.toLowerCase();
		return cap ? current.substring(0, 1).toUpperCase() + current.substring(1) : current;
	}
	
	public boolean a(){
		return a;
	}
	
	public String toString(){
		return word;
	}
	
	public static void main(String[] args){
		WordManager test = new WordManager( "possible_JJ","pert");
		for(String[] sense : test.allUses)
			System.out.println(Arrays.toString(sense) );
		for(int i = 1; i < 10; i++ )
			System.out.println(test.retrieve() );
	}
	
}

