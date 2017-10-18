package pe.edu.dependencyparser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.edu.dependencyparser.domain.Word;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class SemanticRelation {

	Map<String, Double> probabilities;
	Map<String, Double> probability;

	public SemanticRelation () {
		probabilities = new HashMap<String, Double>();
		probability = new HashMap<String, Double>();
	}
	
	public void reset () {
		probabilities.clear();
		probability.clear();
	}
	
	public double getProbability(String synSource, String synDestination) {	
		if(this.probabilities.containsKey(synSource + "&&" + synDestination))			
			return this.probabilities.get(synSource + "&&" + synDestination);
		else
			return 0.1;
	}
	
	public static void main (String [] args) {

		System.setProperty("wordnet.database.dir", Util.WORDNET_DIR );
		WordNetDatabase wn = WordNetDatabase.getFileInstance();	


		Word w4 = new Word();
		w4.setWord("say");
		w4.setLemma("say");
		w4.setPos("V");
		w4.setSynsets( wn.getSynsets(w4.getLemma(), Util.mapPTBSynset(w4.getPos())) );
		
		Word w5 = new Word();
		w5.setWord("man");
		w5.setLemma("man");
		w5.setPos("N");
		w5.setSynsets( wn.getSynsets(w5.getLemma(), Util.mapPTBSynset(w5.getPos())) );		

		SemanticRelation sr = new SemanticRelation();
		/*sr.compute(w4, w5);
		System.out.println("...................");
		for (String string : sr.probabilities.keySet()) {
			if(string.startsWith("say-7"))
				System.out.println(string + "\t" + sr.probabilities.get(string));
		} */		
	}
	
	/**
	 * 
	 * @param gov
	 * @param dep
	 * @return
	 */
	public static float getDistance (String gov, String dep) {
		
		File fGov = new File("./distance/" + gov + ".txt");
		
		File file = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader (fGov);
			br = new BufferedReader(fr);

			String linea;
			while((linea=br.readLine())!=null) {
				String [] synset_dist = linea.split("\t");
				if( dep.equals( synset_dist[0] ) ) {
					float dist = Float.parseFloat(synset_dist[1]) - 1;
					if(dist == 0)
						return 0.0001f;
					else 
						return 1f/dist + 0.0001f;
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			try{                   
				if( null != fr ){  
					fr.close();    
				}          
			}catch (Exception e2){
				System.out.println(e2.getMessage());
			}
		}
		
		return 0.0001f;
	}
	
	

	/**a
	 * 
	 * @param word
	 */
	public void computeProbability(Word word) {
		
		double sum = 0;
		
		if(word.getSynsets() == null || word.getSynsets().length == 0)
			return;
		
		for (Synset synset : word.getSynsets()) {
			try {
				sum += synset.getTagCount(word.getLemma()) + 1;
			} catch (Exception e) {
				sum += 1;
			}
			
		}
		
		for (int i = 0; i < word.getSynsets().length; i++) {
			try {
				probability.put(word.getLemma() + "-" + i, (word.getSynsets()[i].getTagCount(word.getLemma()) + 1 )/sum);
			} catch (Exception e) {
				probability.put(word.getLemma() + "-" + i, 1/sum);
			}
		}
					
	}
	
	public double getProbability(String synset) {
		if(probability.containsKey(synset))
			return probability.get(synset);
		else
			return 1;
	}

}
