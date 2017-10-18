package pe.edu.dependencyparser.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.trees.TypedDependency;
import pe.edu.dependencyparser.domain.Sentence;
import pe.edu.dependencyparser.domain.Word;
import pe.edu.dependencyparser.pagerank.KnowledgeGraph;
import pe.edu.dependencyparser.pagerank.PageRank;
import pe.edu.dependencyparser.pagerank.Vertex;
import pe.edu.dependencyparser.parser.Parser;
import pe.edu.dependencyparser.semeval.SemevalReader;
import pe.edu.dependencyparser.util.Util;

public class Disambiguator {

	Parser parser;
	Map<String, Integer> toBeDisambiguated;
	Map<String, Integer> disambiguated;
	Map<String, Integer> correctlyDisambiguated;
	
	
	/**
	 * Constructor
	 */
	public Disambiguator() {
		parser = new Parser();
		toBeDisambiguated = new HashMap<String, Integer>();
		disambiguated = new HashMap<String, Integer>();
		correctlyDisambiguated = new HashMap<String, Integer>();
		
		toBeDisambiguated.put("ALL", 0);
		toBeDisambiguated.put("N", 0);
		toBeDisambiguated.put("V", 0);
		toBeDisambiguated.put("A", 0);
		toBeDisambiguated.put("R", 0);
		
		disambiguated.put("ALL", 0);
		disambiguated.put("N", 0);
		disambiguated.put("V", 0);
		disambiguated.put("A", 0);
		disambiguated.put("R", 0);
		
		correctlyDisambiguated.put("ALL", 0);
		correctlyDisambiguated.put("N", 0);
		correctlyDisambiguated.put("V", 0);
		correctlyDisambiguated.put("A", 0);
		correctlyDisambiguated.put("R", 0);		
	}
	
	/**
	 * This method add 1 to correctly Disambiguated words
	 * @param pos
	 */
	public void correctlyDisambiguated(String pos) {
		int n = correctlyDisambiguated.get(pos);
		n++;
		correctlyDisambiguated.put(pos, n);
	}
	
	/**
	 * This method add 1 to be disambiguated words
	 * @param pos
	 */
	public void toBeDisambiguated(String pos) {
		int n = toBeDisambiguated.get(pos);
		n++;
		toBeDisambiguated.put(pos, n);
	}
	
	/**
	 * This method add 1 to disambiguated words
	 * @param pos
	 */
	public void disambiguated(String pos) {
		int n = disambiguated.get(pos);
		n++;
		disambiguated.put(pos, n);
	}
	
	
	public static void main(String [] args) {

	/*	SemevalReader sr = new SemevalReader();
		String path1 = "./senseval3.semcor/wordnet3.0/d000.semcor.lexsn.key";
		String path2 = "./senseval3.semcor/wordnet3.0/d001.semcor.lexsn.key";
		String path3 = "./senseval3.semcor/wordnet3.0/d002.semcor.lexsn.key";
		
		String path4 = "./senseval2.semcor/wordnet3.0/d00.semcor.lexsn.key";
		String path5 = "./senseval2.semcor/wordnet3.0/d01.semcor.lexsn.key";
		String path6 = "./senseval2.semcor/wordnet3.0/d02.semcor.lexsn.key";
		
		Disambiguator disambiguator = new Disambiguator();
		sr.addFile(path1);
		sr.addFile(path2);
		sr.addFile(path3);
		//sr.setSentences(path1);
		disambiguator.execute(sr.getSentences());*/
		
	}



	/**
	 * This method executes the disambiguation of all sentences in a collection
	 * @param collection
	 */
	private void execute(List<Sentence> collection) {

		int i=1;
		for (Sentence sentence : collection) {

			List<TypedDependency> dependencies = parser.getDependencyRelations(sentence);
			
			KnowledgeGraph graph = new KnowledgeGraph();
			graph.create(sentence.getWords(), dependencies);
			
			PageRank pageRank = new PageRank(graph);
			
			pageRank.execute();
			
			evaluate(sentence,pageRank);
			System.out.println(sentence.getLiteralSentence() + "\t" + i + "/" + collection.size());
			i++;
		}
		measure();
	}

	private void measure() {
		float precision = ((float) ( (float) correctlyDisambiguated.get("ALL") / (float) disambiguated.get("ALL")));
		float recall = ((float) ( (float) correctlyDisambiguated.get("ALL") / (float) toBeDisambiguated.get("ALL")));
		float f1 = (2f*precision*recall)/(precision + recall);
		
		System.out.println("=== TOTAL ===");
		System.out.println("P: " + precision + "\t R: " + recall + "\t F1: " + f1);
		
		precision = ((float) ( (float) correctlyDisambiguated.get("N") / (float) disambiguated.get("N")));
		recall = ((float) ( (float) correctlyDisambiguated.get("N") / (float) toBeDisambiguated.get("N")));
		f1 = (2f*precision*recall)/(precision + recall);
		
		System.out.println("=== NOUN ===");
		System.out.println("P: " + precision + "\t R: " + recall + "\t F1: " + f1);
		
		precision = ((float) ( (float) correctlyDisambiguated.get("V") / (float) disambiguated.get("V")));
		recall = ((float) ( (float) correctlyDisambiguated.get("V") / (float) toBeDisambiguated.get("V")));
		f1 = (2f*precision*recall)/(precision + recall);
		
		System.out.println("=== VERB ===");
		System.out.println("P: " + precision + "\t R: " + recall + "\t F1: " + f1);
		
		precision = ((float) ( (float) correctlyDisambiguated.get("A") / (float) disambiguated.get("A")));
		recall = ((float) ( (float) correctlyDisambiguated.get("A") / (float) toBeDisambiguated.get("A")));
		f1 = (2f*precision*recall)/(precision + recall);
		
		System.out.println("=== ADJECTIVE ===");
		System.out.println("P: " + precision + "\t R: " + recall + "\t F1: " + f1);
		
		precision = ((float) ( (float) correctlyDisambiguated.get("R") / (float) disambiguated.get("R")));
		recall = ((float) ( (float) correctlyDisambiguated.get("R") / (float) toBeDisambiguated.get("R")));
		f1 = (2f*precision*recall)/(precision + recall);
		
		System.out.println("=== ADVERB ===");
		System.out.println("P: " + precision + "\t R: " + recall + "\t F1: " + f1);
	}

	/**
	 * This method evaluates the performance of the Disambiguation Method
	 * @param sentence
	 * @param pageRank
	 */
	private void evaluate(Sentence sentence, PageRank pageRank) {
		int i = 0;
		for (Word w : sentence.getWords()) {	
			if(!w.getWnsn().equals("0")) {
				verify(w, i, pageRank);
			} else {
				
				if(w.getLexsn().equals("NOWN")) {

					toBeDisambiguated("ALL");
					
					if(Util.isNoun(w)) toBeDisambiguated("N");
					if(Util.isVerb(w)) toBeDisambiguated("V");
					if(Util.isAdjective(w)) toBeDisambiguated("A");
					if(Util.isAdverb(w)) toBeDisambiguated("R");
				} else if (!w.getLexsn().trim().equals("U") && !w.getLexsn().trim().equals("NOANSWER") && !w.getLexsn().trim().equals("")) {
					verify(w, i, pageRank);
				} 
				
				
			}
			i++;
		}
		
	}

	private void verify(Word w, int i, PageRank pageRank) {
		
		toBeDisambiguated("ALL");
		
		if(Util.isNoun(w)) toBeDisambiguated("N");
		if(Util.isVerb(w)) toBeDisambiguated("V");
		if(Util.isAdjective(w)) toBeDisambiguated("A");
		if(Util.isAdverb(w)) toBeDisambiguated("R");
						
		if(w.getSynsets()!= null && w.getSynsets().length != 0) {
			
			disambiguated("ALL");
			
			if(Util.isNoun(w)) disambiguated("N");
			if(Util.isVerb(w)) disambiguated("V");
			if(Util.isAdjective(w)) disambiguated("A");
			if(Util.isAdverb(w)) disambiguated("R");
			
			if(w.getWnsn().contains(";")) {
				String [] expectedSenses = w.getWnsn().split(";");
				for (String expected : expectedSenses) {
					int iExpected = Integer.parseInt(expected) - 1;
					if ( iExpected == getResult(i,pageRank) ) {
						correctlyDisambiguated("ALL");
						if(Util.isNoun(w)) correctlyDisambiguated("N");
						if(Util.isVerb(w)) correctlyDisambiguated("V");
						if(Util.isAdjective(w)) correctlyDisambiguated("A");
						if(Util.isAdverb(w)) correctlyDisambiguated("R");
					}
						
				}
			} else {
				int expected = Integer.parseInt(w.getWnsn());
				if( (expected - 1) == getResult(i,pageRank) ) {
					correctlyDisambiguated("ALL");
					if(Util.isNoun(w)) correctlyDisambiguated("N");
					if(Util.isVerb(w)) correctlyDisambiguated("V");
					if(Util.isAdjective(w)) correctlyDisambiguated("A");
					if(Util.isAdverb(w)) correctlyDisambiguated("R");
				}
					
			}					
		}		
	}

	/**
	 * This method returns the sense with highest probability
	 * @param wordPosition
	 * @param pageRank
	 * @return
	 */
	private int getResult(int wordPosition, PageRank pageRank) {
		int n = 0;
		float max = -1;
		int cont = 0;
		for (int i = 0; i < pageRank.getGraph().getVertexs().size(); i++) {
			Vertex v = pageRank.getGraph().getVertexs().get(i);
			if(wordPosition == v.getPosition()) {				
				if(pageRank.getPr(i) > max) {
					n = cont;
					max = pageRank.getPr(i);
				}
				cont++;				
			}
		}
		return n;
	}
	
}
