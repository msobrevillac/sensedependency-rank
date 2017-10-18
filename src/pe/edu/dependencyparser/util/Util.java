package pe.edu.dependencyparser.util;

import pe.edu.dependencyparser.domain.Word;
import edu.smu.tspell.wordnet.SynsetType;

public class Util {
	
	public static final String WNET3_REL = "./sources/wnet30_rels.txt";
	public static final String WNET3G_REL = "./sources/wnet30g_rels.txt";
	public final static String WORDNET_DIR = "./WordNet-3.0/dict/";
	
	public static final String [] WNET3_KB = {WNET3_REL, WNET3G_REL};

	/**
	 * Get the SynsetType according to the tag
	 * @param tag grammatical class
	 * @return
	 */
	public static SynsetType mapSynset(String tag) {
		if(tag.equals("VERB"))
			return SynsetType.VERB;
		
		if(tag.equals("ADV"))
			return SynsetType.ADVERB;
		
		if(tag.equals("ADJ"))
			return SynsetType.ADJECTIVE;
		
		if(tag.equals("N"))
			return SynsetType.NOUN;
		
		return null;
	}
	
	/**
	 * Get the SynsetType according to the tag
	 * @param tag grammatical class
	 * @return
	 */
	public static SynsetType mapPTBSynset(String tag) {
		if(tag.startsWith("V"))
			return SynsetType.VERB;
		
		if(tag.startsWith("RB"))
			return SynsetType.ADVERB;
		
		if(tag.startsWith("J"))
			return SynsetType.ADJECTIVE;
		
		if(tag.startsWith("N"))
			return SynsetType.NOUN;
		
		return null;
	}
	
	/**
	 * Get the ILI from the synset and tag
	 * @param synset synset
	 * @param tag grammatical class
	 * @return
	 */
	public static String fromTBtoIli(int synset, String tag) {
		String t = "";
		if(tag.startsWith("N"))	t = "n";
		if(tag.startsWith("V"))	t = "v";
		if(tag.startsWith("J"))	t = "a";
		if(tag.startsWith("RB"))	t = "r";
		
		String sSynset = String.valueOf(synset);
		int lengthSynset = sSynset.length();
		while (8 - lengthSynset > 0) {
			sSynset = "0" + sSynset;
			lengthSynset++;
		}			 
		return sSynset+"-"+t;
	}

	/**
	 * Get the ILI from the synset and tag
	 * @param synset synset
	 * @param tag grammatical class
	 * @return
	 */
	public static String toIli(int synset, String tag) {
		String t = "";
		if(tag.equals("N"))	t = "n";
		if(tag.equals("VERB"))	t = "v";
		if(tag.equals("ADJ"))	t = "a";
		if(tag.equals("ADV"))	t = "r";
		
		String sSynset = String.valueOf(synset);
		int lengthSynset = sSynset.length();
		while (8 - lengthSynset > 0) {
			sSynset = "0" + sSynset;
			lengthSynset++;
		}			 
		return sSynset+"-"+t;
	}
	
	/**
	 * This method verifies if a word can be form a valid dependency, i.e., if it's a verb, noun, adjective or 
	 * adverb
	 * @param word
	 * @return
	 */
	public static boolean validDependency(Word word) {
		if(word.getPos().startsWith("V") || word.getPos().startsWith("N")
				|| word.getPos().startsWith("RB") || word.getPos().startsWith("J"))
			return true;
		return false;
	}

	public static boolean isVerb(Word gov) {
		if(gov.getPos().startsWith("V"))
			return true;		
		return false;
	}

	public static boolean isNoun(Word dep) {
		if(dep.getPos().startsWith("N"))
			return true;
		return false;
	}
	
	public static boolean isAdjective(Word dep) {
		if(dep.getPos().startsWith("J"))
			return true;
		return false;
	}
	
	public static boolean isAdverb(Word dep) {
		if(dep.getPos().startsWith("RB"))
			return true;
		return false;
	}
	
}
