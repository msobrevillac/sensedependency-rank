package pe.edu.dependencyparser.domain;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	
	List<Word> words;
	
	public Sentence(){
		this.words = new ArrayList<Word>();
	}
	
	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}
	
	public void addWord(Word word) {
		this.words.add(word);
	}
	
	public String getLiteralSentence() {
		String sentence = "";
		for (Word word : this.words)
			sentence += word.getWord() + " ";
		return sentence;
	}

}
