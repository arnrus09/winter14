package sentencecolorator;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.*;

class WmWrapper{
	
	WordManager wordManager;
	boolean 
		withA = false,
		paren = false;
	private String current;
	int faluires = 0;
	public WmWrapper(WordManager wm){
		this.wordManager = wm;
		current = update();
	}
	public WmWrapper(WordManager a, WordManager wm){
		withA = true;
		this.wordManager = wm;
		current = update();
	}
	public WmWrapper(WordManager a, WordManager wm, WordManager wm2){
		paren = true;
		withA = true;
		this.wordManager = wm;
		current = update();
	}
	public String update(){
		faluires++;
		String updated = wordManager.retrieve();
		current = ( withA 
				? ( updated.substring(0, 1).matches("[AEIOUaeiou]") ? "an " : "a ") 
						+ ( paren ? "\" " : "" ) 
				: "") 
				+ updated;
		if ( current.equals("be") )
			current = faluires < 3 ? update() : wordManager + "";
		faluires = 0;
		return current;
	}
	public String toString(){
		return current();
		
	}
	public String current(){
		return current;
	}

}

class Wrappers extends LinkedHashSet<WmWrapper>{
	
	static class Voice{
		private static Random rand = new Random(System.nanoTime());
		private static String[] singers = 
			{"-vCello", "-vPipe Organ","-vBells", "-vBad News"};
		public static String sing(){
			return singers[rand.nextInt(singers.length)];
		}
		public static String speak(){
			return "-vRalph";
		}
	}
	enum Vox{
		SING,SPEAK
	}
	public void vocalize(Vox how){
		String[] tempa = new String[ size() + 2];
		tempa[0] = "say";
		tempa[1] = how.equals(Vox.SING) ? Voice.sing() : Voice.speak();
		Iterator<WmWrapper> it = iterator();
		for(int i = 2; i < size() + 2; i++ )
			tempa[i] = it.next().current();
		Utilities.command( tempa );
	}
	public void sing(){
		vocalize(Vox.SING);
	}
	public void speak(){
		vocalize(Vox.SPEAK);
	}
}

public class SentenceManager implements Iterable<WordManager>{
	
	private static String[] tempHelplers = "be 	am 	is 	are was 	were 	been 	being have 	has 	had 	could should 	would 	may 	might must 	shall 	can 	will  do 	did 	does 	having".split("\\s");
	private static List<String> helpers = Arrays.asList(tempHelplers);
	LinkedHashSet<WordManager> wms;
	LinkedHashMap<WordManager, String> wmmap;
	String[] wmImages;
	
public SentenceManager(List<String> taggedSentence, String searchArg){
	wms = getWMs(taggedSentence, searchArg);
	wmmap = new LinkedHashMap<WordManager, String>();
	for( WordManager wm: wms){
		wmmap.put(wm, wm.retrieve());
	}
	wmImages = wmmap.values().toArray(new String[0]);	
}
	
public static List<String> tag(String sentence){
	MaxentTagger tagger = new MaxentTagger(
			"taggers/english-left3words-distsim.tagger");
	String tagged = tagger.tagString(sentence);
	System.out.println(tagged);
	return Arrays.asList(tagged.split(" "));
}

private static String findHas(String taggedSentence){
	String[] hasCnjgs = {"has","have","had","Has","Have","Had"};
	for(String cnj: hasCnjgs){
		String rgx = ".*"+cnj+"_V.+_V.*";
		if(taggedSentence.matches(rgx))
			return cnj;
}
	return null;
}




public static LinkedHashSet<WordManager> getWMs(List<String> tagged, String searcharg ){
	LinkedHashSet<WordManager> wms = new LinkedHashSet<WordManager>();
	WordManager[] wmArray;
	boolean hBeTest = tagged.toString().matches(".*_VB.*VB.*")? true: false;
	boolean participle = tagged.toString().contains("VBN")? true: false;
	//System.out.println(tagged.toString());
	for( String tw: tagged){
		String untw = tw.substring(0, tw.indexOf("_"));
		if(hBeTest && helpers.contains(untw))
			wms.add(new WordManager(untw));
		else if(participle && helpers.contains(untw))
			wms.add(new WordManager(untw));
		else{
			//System.out.println(untw);
			WordManager tempWm = new WordManager(tw, searcharg);
			wms.add(tempWm);

		}
	}
	return wms;
}

	
public 	Wrappers wrap(){
	WordManager wm;
	WordManager wm2;
	Wrappers wmal = new Wrappers();
	Iterator it = iterator();
	while (it.hasNext() ){
		if( ( wm = (WordManager)it.next() ).a() && it.hasNext() ){
			if ( (wm2 = (WordManager)it.next() ).equals("\"") )
				wmal.add(new WmWrapper(wm, wm2, (WordManager)it.next() ) );
			else
				wmal.add(new WmWrapper(wm, wm2) );
		}
		else
			wmal.add(new WmWrapper(wm) );
	}
	return wmal;
}

public Iterator<WordManager> iterator() {
	
		return wms.iterator();
}


public static void main(String[] args){
	//long start = System.nanoTime();
	//SentenceManager smTest = new SentenceManager("The new problem has the advantage of drawing a fairly sharp line between "
	//		+ "the physical and the intellectual capacities of a man. No engineer or chemist claims to be able to produce a "
			/*+ "material which is indistinguishable from the human skin. It is possible that at some time this might be done, "
			+ "but even supposing this invention available we should feel there was little point in trying to make a "
			+ "\"thinking machine\" more human by dressing it up in such artificial flesh. The form in which we have set the "
			+ "problem reflects this fact in the condition which prevents the interrogator from seeing or touching the other "
			+ "competitors, or hearing -their voices. Some other advantages of the proposed criterion may be shown up by specimen "
			+ "questions and answers."*///,"associate");
		//smTest.wrap().sing();
		
		System.out.println(tag("perfectly"));	

	
}

}
