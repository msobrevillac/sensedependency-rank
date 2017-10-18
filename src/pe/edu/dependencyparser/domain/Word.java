package pe.edu.dependencyparser.domain;

import edu.smu.tspell.wordnet.Synset;

public class Word {
	
	String lemma;
	String word;
	String pos;
	String nSynset;
	boolean wasDisambiguated;
	String wnsn;
	String lexsn;
	public Synset[] synsets;
	
	public Word(){
		this.lemma = "";
		this.word = "";
		this.pos = "";
		this.nSynset = "";
		this.wasDisambiguated = false;
		this.lexsn = "";
		this.wnsn = "0";
	}
	
	public Word(String lemma, String word, String pos) {
		this.lemma = lemma;
		this.word = word;
		this.pos = pos;
		this.nSynset = "";
		this.wnsn = "0";
		this.lexsn = "";
		this.wasDisambiguated = false;
	}
	
	public Word(String string, String string2) {
		// TODO Auto-generated constructor stub
	}

	public String getLemma() {
		return lemma;
	}
	
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getPos() {
		return pos;
	}
	
	public void setPos(String pos) {
		this.pos = pos;
	}
	
	public String getnSynset() {
		return nSynset;
	}
	
	public void setnSynset(String nSynset) {
		this.nSynset = nSynset;
	}

	public boolean wasDisambiguated() {
		return wasDisambiguated;
	}

	public void setWasDisambiguated(boolean wasDisambiguated) {
		this.wasDisambiguated = wasDisambiguated;
	}

	public String getWnsn() {
		return wnsn;
	}

	public void setWnsn(String wnsn) {
		this.wnsn = wnsn;
	}

	public String getLexsn() {
		return lexsn;
	}

	public void setLexsn(String lexsn) {
		this.lexsn = lexsn;
	}		
	
	public String getString() {
		return "word:" + this.word + "\t" + "lemma:" + this.lemma + "\t"
				+ "pos:" + this.pos + "\t" + "wnsn:" + this.wnsn + "\t"
				+ "lexsn:" + this.lexsn;
	}
	
	public Synset[] getSynsets() {
		return synsets;
	}

	public void setSynsets(Synset[] synsets) {
		this.synsets = synsets;
	}
	
}
