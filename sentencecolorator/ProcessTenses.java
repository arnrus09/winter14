package sentencecolorator;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class ProcessTenses {
	static Lexicon lexicon = Lexicon.getDefaultLexicon();
    static NLGFactory nlgFactory = new NLGFactory(lexicon);
    static Realiser realiser = new Realiser(lexicon);
    
	
	public static String toFpp(String word){
		SPhraseSpec p = nlgFactory.createClause();
		p.setVerb(word);
		p.setFeature(Feature.TENSE, Tense.PRESENT);
		p.setFeature(Feature.PERSON, Person.FIRST);
		return realiser.realise(p).toString();
	}
	
	public static String revert(String word, String tag){
		if( tag.equals("VB") || tag.equals("VBP") )
			return word;
		else if( tag.equals("VBD")){
			SPhraseSpec p = nlgFactory.createClause();
			p.setVerb(word);
			p.setFeature(Feature.TENSE, Tense.PAST);
			return realiser.realise(p).toString();
		}
		else if( tag.equals("VBN")){
			SPhraseSpec p = nlgFactory.createClause();
			p.setVerb(word);
			p.setFeature(Feature.PERFECT, true);
			p.setFeature(Feature.TENSE, Tense.PAST);
			String[] temp = realiser.realise(p).toString().split(" ");
			return temp[1];
		}
		else if( tag.equals("VBZ")){
			SPhraseSpec p = nlgFactory.createClause();
			p.setVerb(word);
			p.setFeature(Feature.PERSON, Person.THIRD);
			return realiser.realise(p).toString();
		}
		else if( tag.equals("VBG")){
			SPhraseSpec p = nlgFactory.createClause();
			p.setVerb(word);
			p.setFeature(Feature.PROGRESSIVE, true);
			return realiser.realise(p).toString().split(" ")[1];
		}
		else return word;
	}

	public static void main(String[] args){

		System.out.println(revert("typify","VBG"));
		
	}
	
}
