package pe.edu.dependencyparser.pagerank;

import pe.edu.dependencyparser.domain.Word;

public class Vertex {

	int position;
	Word word;
	int sense;
	float weight;
	
	public int getPosition() {
		return position;
	}
	public Word getWord() {
		return word;
	}
	public int getSense() {
		return sense;
	}
	public float getWeight() {
		return weight;
	}
	
	
	
}
