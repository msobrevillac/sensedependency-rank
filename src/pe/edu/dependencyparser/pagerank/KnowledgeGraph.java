package pe.edu.dependencyparser.pagerank;

import java.util.ArrayList;
import java.util.List;

import pe.edu.dependencyparser.domain.Word;
import pe.edu.dependencyparser.util.SemanticRelation;
import pe.edu.dependencyparser.util.Util;
import edu.smu.tspell.wordnet.Synset;
import edu.stanford.nlp.trees.TypedDependency;

public class KnowledgeGraph {

	List<Vertex> vertexs;
	float [][] matrix;

	/**
	 * This method builds the knowledge graph 
	 * @param words
	 * @param dependencies
	 */
	public void create(List<Word> words,
			List<TypedDependency> dependencies) {
		
		createVertexs(words, dependencies);
		
		createEdges(dependencies);

	}

	/**
	 * This method creates the edges using the dependencies
	 * @param dependencies
	 */
	private void createEdges(List<TypedDependency> dependencies) {
		
		for (TypedDependency dependency : dependencies) {
			if(!dependency.reln().getShortName().equals("root")) {
				int gov = dependency.gov().index() - 1;
				int dep = dependency.dep().index() - 1;				
				createEdge(gov,dep);
			}
		}
	}

	/**
	 * This method creates the edge for a gov dependency and a dep
	 * @param gov
	 * @param dep
	 */
	private void createEdge(int gov, int dep) {
		for(int i = 0; i < vertexs.size(); i++)
			for(int j = 0; j < vertexs.size(); j++) {
				if(vertexs.get(i).sense != -1 && vertexs.get(j).sense != -1) {
					if(vertexs.get(i).position == gov && vertexs.get(j).position == dep) {
						String g = Util.fromTBtoIli(vertexs.get(i).word.synsets[vertexs.get(i).sense].hashCode(), vertexs.get(i).word.getPos());
						String d = Util.fromTBtoIli(vertexs.get(j).word.synsets[vertexs.get(j).sense].hashCode(), vertexs.get(j).word.getPos());
						matrix[i][j] = 	SemanticRelation.getDistance(g, d);
						matrix[j][i] = 	matrix[i][j];
							
					}
				}
			}
	}

	/**
	 * This method builds the vertexs using the words and the dependencies
	 * @param words
	 * @param dependencies
	 * @return
	 */
	private void createVertexs(List<Word> words,
			List<TypedDependency> dependencies) {
		
		vertexs = new ArrayList<Vertex>();
		List<Integer> positions = new ArrayList<Integer>();
		
		for (TypedDependency dependency : dependencies) {
			int pos;
			if (dependency.gov().index() != 0) {
				pos = dependency.gov().index() - 1;
				if(!positions.contains(pos))
					positions.add(pos);
			}
			if (dependency.dep().index() != 0) {
				pos = dependency.dep().index() - 1;
				if(!positions.contains(pos))
					positions.add(pos);
			}			
		}
		
		for ( int i = 0; i < words.size(); i++ )
			if( positions.contains ( i ) ) {
				if(words.get(i).synsets != null && words.get(i).synsets.length != 0) {
					
					int sum = sumWeights(words.get(i));
					
					for (int j = 0; j < words.get(i).synsets.length; j++) {
						Vertex vertex = new Vertex();
						vertex.position = i;
						vertex.word = words.get(i);
						vertex.sense = j;
						try {
							vertex.weight = (float)(words.get(i).synsets[j].getTagCount(words.get(i).getLemma()) + 1)/(float)sum;
						} catch(Exception e) {
							vertex.weight = 1f/(float) sum;
						}
						vertexs.add(vertex);
					}
				} else {
					Vertex vertex = new Vertex();
					vertex.position = i;
					vertex.word = words.get(i);
					vertex.sense = -1;
					vertex.weight = 0;
					vertexs.add(vertex);
				}
			}
		initMatrix();
	}

	/**
	 * This method initializes the graph's matrix
	 */
	private void initMatrix() {
		matrix = new float[vertexs.size()][vertexs.size()];
		for (int i = 0; i < vertexs.size(); i++)
			for (int j = 0; j < vertexs.size(); j++) 
				matrix[i][j] = 0;
	}

	/**
	 * This method calculates the total sum of weights for a word (using its synsets)
	 * @param word
	 * @return
	 */
	private int sumWeights(Word word) {
		int sum = 0;
		for (Synset synset : word.getSynsets()) {
			try {
				sum += synset.getTagCount(word.getLemma()) + 1;
			} catch (Exception e) {
				sum += 1;
			}			
		}
		return sum;
	}
	
	/**
	 * This method returns the list of vertexs
	 * @return
	 */
	public List<Vertex> getVertexs() {
		return vertexs;
	}

}
