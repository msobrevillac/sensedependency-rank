package pe.edu.dependencyparser.parser;

import java.util.ArrayList;
import java.util.List;

import pe.edu.dependencyparser.semeval.SemevalReader;
import pe.edu.dependencyparser.util.Util;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Parser {

	String PARSER_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	LexicalizedParser lp;

	/**
	 * Constructor
	 */
	public Parser() {
		lp = LexicalizedParser.loadModel(PARSER_MODEL);
	}


	public static void main(String[] args) {
		Parser parser = new Parser();
		
		SemevalReader sr = new SemevalReader();
		String path1 = "./senseval3.semcor/wordnet3.0/d000.semcor.lexsn.key";
		sr.addFile(path1);
		System.out.println(sr.getSentence(4).getLiteralSentence());
		List<TypedDependency> tds = parser.getDependencyRelations(sr.getSentence(4));
	}

	/**
	 * This method gets the most important dependencies (extracting the MD, and relations of conj etc)
	 * @param sentence
	 * @return
	 */
	public List<TypedDependency> getDependencyRelations(pe.edu.dependencyparser.domain.Sentence sentence) {
		
		String [] sent = new String [sentence.getWords().size()];
		for (int i=0; i<sentence.getWords().size(); i++)
			sent [i] = sentence.getWords().get(i).getWord();
		
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
		Tree parse = lp.apply(rawWords);	
		
		TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

		return filterDependencies(sentence, tdl);
	}

	/**
	 * This method filters the dependencies according to the grammatical classes
	 * @param tdl
	 * @return
	 */
	private List<TypedDependency> filterDependencies(pe.edu.dependencyparser.domain.Sentence sentence, List<TypedDependency> tdl) {
		List<TypedDependency> dependencies = new ArrayList<TypedDependency>();
		for (TypedDependency typedDependency : tdl) {
			if( typedDependency.gov().index() == 0 )
				dependencies.add(typedDependency);
			else {
				if( (Util.validDependency(sentence.getWords().get(typedDependency.gov().index()-1)) && 
						Util.validDependency(sentence.getWords().get(typedDependency.dep().index()-1))) &&
						!typedDependency.reln().getShortName().equals("aux")) {
					dependencies.add(typedDependency);					
				}	
			}
				
		}		
		return dependencies;
	}
}
