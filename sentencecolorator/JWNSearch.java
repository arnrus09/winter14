package sentencecolorator;

import edu.mit.jwi.*;
import edu.mit.jwi.data.*;
import edu.mit.jwi.item.*;

import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;


public class JWNSearch{
public static void main(String[] args) throws Exception {

try{
	for(String[] sa : getSenses("dog", "junk", "n") )
		System.out.println(Arrays.toString(sa) );
}
//catch(Exception e){
finally{
	System.out.println("");
}
}

public static String[][] getSenses(String qw, String arg, String u) throws Exception{
String temp;
String temp2;
LinkedHashSet<String> lhs = new LinkedHashSet<String>();
LinkedHashSet<String[]> lhs0 = new LinkedHashSet<String[]>();
POS p;
p = u.equals("a") ? POS.ADJECTIVE : (u.equals("r") ? POS.ADVERB : 
	(u.equals("n") ? POS.NOUN : (u.equals("v") ? POS.VERB : null ) ) );
String path = "/usr/local/WordNet-3.0/dict";
URL url;
	url = new URL("file", null , path );
IDictionary dict = new edu.mit.jwi.Dictionary ( url);
	dict . open ();
int i = 0;
 // look up first sense of the word "dog "
 IIndexWord idxWord = dict.getIndexWord(qw,p );

 
 while(true){
	 try{
 IWordID wordID = idxWord . getWordIDs ().get (i) ; // 1st meaning
 IWord word = dict.getWord ( wordID );
 ISynset synset = word. getSynset ();

 // iterate over words associated with the synset
 for( IWord w : synset.getWords ()){
	 temp = w.getLemma();
	if( !lhs.contains( temp ) && !w.getLemma().trim().equals(qw) ){
		 temp2 = temp.replaceAll("_", " ");
		 lhs.add( temp );
	}
 }
 if( !lhs.isEmpty() )
	 lhs0.add(lhs.toArray(new String[0]) );
 lhs.clear();
 i++;
	 }
	 catch(Exception e){
		 for(String[] sa : lhs0 )
				System.out.println(Arrays.toString(sa) );
		 return lhs0.toArray(new String[0][0]);
	 }
	 
 }
 }
}

