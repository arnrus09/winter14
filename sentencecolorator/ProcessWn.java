package sentencecolorator;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;


public class ProcessWn {
static String[] symbols = { "#", "(" };
public static String[][] getSenses(String word, String arg, String u) throws IOException{
	String in;
	String[] raw;
	in = WNSearch.searchWn(word, arg, u);
	raw = in.split("Sense [0-9]+");
	HashSet<String[]> senses = new HashSet<String[]>();
	HashSet<String> all = new HashSet<String>();
	int len = raw.length;
	ArrayList temp = new ArrayList();
	for( int i = 1; i < raw.length; i++){
		String[] tempsa1 = raw[i].split("\n");
		for( String raws : tempsa1){
			if( raws.matches(".*Similarity of.*|.*senses of.*") )
				continue;
			if( raws.length() >= 1 &&  raws.substring(0,1).matches("\\w+") 
					&& !raws.contains("Attributes") || raws.contains("=>") )
				for( String s : raws.split(",") ){
					 String s2 = s.trim();
					 s2 = s2.contains("=>") ? s2.substring(s2.lastIndexOf(">") + 1) : s2;
						for( String symbol : symbols )
							s2 = trimToSymbol(s2, symbol).trim();
						if( s2.length() >= 1 && !s2.equals(word) && !all.contains(s2) )
							temp.add(s2);
							all.add(s2);
	
				}		
		if( !temp.isEmpty() ){
			senses.add( (String[]) temp.toArray(new String[0]) );
			temp.clear();
		}
	}	
		
	}
	return senses.toArray(new String[0][0]);
}
static String trimToSymbol(String word, String symbol){
	return word.contains(symbol) ? word.substring(0, word.indexOf(symbol) ) : word;
}


public static void main(String[] args) throws IOException{
	String[][] senses = getSenses("first", "pert","a");
	for( String[] sa : senses )
		System.out.println(Arrays.toString(sa) );
}
}
