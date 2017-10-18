package pe.edu.dependencyparser.semeval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pe.edu.dependencyparser.domain.Sentence;
import pe.edu.dependencyparser.domain.Word;
import pe.edu.dependencyparser.util.Util;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class SemevalReader {
	
	private List<Sentence> sentences;
	WordNetDatabase wn;
	
	/**
	 * 
	 */
	public SemevalReader() {
		this.sentences = new ArrayList<Sentence>();
		
		System.setProperty("wordnet.database.dir", Util.WORDNET_DIR );
		wn = WordNetDatabase.getFileInstance();		
		
	}
	
	/**
	 * 
	 * @param path
	 */
	public void addFile(String path) {
		this.sentences.addAll(readFile(path));
	}		
	
	/**
	 * 
	 * @param path
	 */
	public void setSentences(String path) {
		this.sentences = readFile(path);
	}

	/**
	 * 
	 * @return
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public Sentence getSentence(int i) {
		return sentences.get(i);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public List<Sentence> readFile (String path) {
		List<Sentence> sentences = new ArrayList<Sentence>();

		File file = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			file = new File (path);
			fr = new FileReader (file);
			br = new BufferedReader(fr);

			String linea;
			Sentence sentence = null;
			while((linea=br.readLine())!=null) {
				if(linea.startsWith("<s"))
					sentence = new Sentence();
				if(linea.startsWith("</s>")) {
					sentences.add(sentence);
					sentence = null;
				}
				if(linea.startsWith("<wf") || linea.startsWith("<punc")) {
					sentence.addWord(getWord(linea));
				}			
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			try{                   
				if( null != fr ){  
					fr.close();    
				}                 
			}catch (Exception e2){
				e2.printStackTrace();
			}
		}
		
		return sentences;
	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	private Word getWord(String content) {
		Word word = new Word();
		if(content.startsWith("<wf")) {
			Pattern pattern = Pattern.compile("<wf(.*?)>(.*?)</wf>");
			Matcher matcher = pattern.matcher(content);
			String parameters = "";
			while (matcher.find()) {
				parameters = matcher.group(1).trim() + " ";
				word.setWord(matcher.group(2).trim());
			}
			
			if(parameters.compareTo("") == 0)
				return null;
			
			pattern = Pattern.compile("pos=(.*?)\\s");
			matcher = pattern.matcher(parameters);
			while (matcher.find())			
				word.setPos(matcher.group(1).trim());

			pattern = Pattern.compile("lemma=(.*?)\\s");
			matcher = pattern.matcher(parameters);
			while (matcher.find()) 			
				word.setLemma(matcher.group(1).trim());
			
			pattern = Pattern.compile("wnsn=(.*?)\\s");
			matcher = pattern.matcher(parameters);
			while (matcher.find()) {
				word.setWasDisambiguated(true);
				word.setWnsn(matcher.group(1).trim());
			}

			pattern = Pattern.compile("lexsn=(.*?)\\s");
			matcher = pattern.matcher(parameters);
			while (matcher.find())				
				word.setLexsn(matcher.group(1).trim());			

		} else {
			Pattern pattern = Pattern.compile("<punc>(.*?)</punc>");
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				word.setWord(matcher.group(1).trim());
				word.setPos("punc");
			}
		}
		if(Util.validDependency(word)) {
			word.setSynsets( wn.getSynsets(word.getLemma().trim(), Util.mapPTBSynset(word.getPos())) );
		/*	if(word.getWord().equals("bleary")) {
				System.out.println("READ: " + word.getWord() + "->" + word.getLemma() + "->" + word.getPos() + "->" + Util.mapPTBSynset(word.getPos()));
				System.out.println("READ: " + word.getSynsets() + " >>>>>>>> " + word.getSynsets().length);	
			} */
			
		}
			
		return word;

	}

	public static void main (String [] args) {
		SemevalReader sr = new SemevalReader();
		String path1 = "./senseval3.semcor/wordnet3.0/d000.semcor.lexsn.key";
		String path2 = "./senseval3.semcor/wordnet3.0/d001.semcor.lexsn.key";
		String path3 = "./senseval3.semcor/wordnet3.0/d002.semcor.lexsn.key";
		sr.addFile(path1);
		sr.addFile(path2);
		sr.addFile(path3);
		
		System.out.println("Number of sentences: " + sr.getSentences().size());
		
		for (Word w : sr.getSentence(4).getWords()) {
			System.out.println(w.getWord() + "-->" + w.getPos());
		}
		System.out.println("4: " + sr.getSentence(4).getLiteralSentence());
			
	}


}
