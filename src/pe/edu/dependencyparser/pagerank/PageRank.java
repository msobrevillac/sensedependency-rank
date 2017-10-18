package pe.edu.dependencyparser.pagerank;

import java.util.ArrayList;
import java.util.List;

public class PageRank {

	KnowledgeGraph graph;
	
	float damping = 0.85f;
	int nIterations = 30;
	
	List<Float> randomSurfing;
	float [][] transitionMatrix;
	List<Float> pr;
	
	/**
	 * Constructor
	 * @param graph
	 */
	public PageRank(KnowledgeGraph graph) {
		this.graph = graph;
		initPageRank();
		initTransitionMatrix();
		//initRandomSurfing();
		initDefaultRandomSurfing();
	}

	/**
	 * This method initializes the pagerank vector
	 */
	private void initPageRank() {
		pr = new ArrayList<Float>();
		for (Vertex v : graph.vertexs)
			pr.add(1f/(float)graph.vertexs.size());
	}

	/**
	 * This method initializes the transition matrix for the PageRank algorithm
	 */
	private void initTransitionMatrix() {
		int n = graph.vertexs.size();
		transitionMatrix = new float[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++) 
				transitionMatrix[i][j] = 0f;
			
		float[] sum = new float[n];
		for( int j = 0; j < n; j++ )
			for( int i = 0; i < n; i++ ) 
				sum[j] += graph.matrix[i][j];
		
		for( int j = 0; j < n; j++ )
			for( int i = 0; i < n; i++ ) 
				if(sum[j] != 0)
					transitionMatrix[i][j] = graph.matrix[i][j]/sum[j];
	}

	/**
	 * This method initializes the Random Surfing Vector
	 */
	private void initRandomSurfing() {
		randomSurfing = new ArrayList<Float>();
		float sum = 0;
		for (Vertex v : graph.vertexs)
			sum += v.weight;
		
		for (Vertex v : graph.vertexs) 
			randomSurfing.add(v.weight/sum);		
	}
	
	/**
	 * This method initializes the Random Surfing Vector
	 */
	private void initDefaultRandomSurfing() {
		randomSurfing = new ArrayList<Float>();
		float sum = 0;
		for (Vertex v : graph.vertexs)
			sum += 1;
		
		for (Vertex v : graph.vertexs) 
			randomSurfing.add(1/sum);		
	}

	/**
	 * This method executes the pagerank algorithm
	 */
	public void execute () {
		
		for ( int z=0; z<nIterations; z++) {
			
			//Calculating transition Matrix
			List<Float> p1 = new ArrayList<Float>();
			for(int i = 0; i < pr.size(); i++) {
				float sumI = 0f;
				for(int j = 0; j < pr.size(); j++)
					sumI += transitionMatrix[i][j]*pr.get(j);
				p1.add(sumI*damping);
			}					

			//Calculating random surfing
			List<Float> p2 = new ArrayList<Float>();
			for (Float rs : randomSurfing)
				p2.add(rs*(1-damping));
			
			for(int i = 0; i < pr.size(); i++)
				pr.set(i, p1.get(i) + p2.get(i));
		}
		

	}
	
	/**
	 * This method returns the Knowledge Graph
	 * @return
	 */
	public KnowledgeGraph getGraph() {
		return graph;
	}
	
	/**
	 * 
	 * @param pos
	 * @return
	 */
	public float getPr(int pos) {
		return pr.get(pos);
	}
	
}
