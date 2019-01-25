import java.util.Scanner;
import java.util.Random;

/**
  * Graph objects can be used to work with undirected graphs.
  * They are internally represented using adjacency matrices.
  * @author Sophie Quigley
  * @author PUT YOUR NAMES HERE
  * <BR>THE ONLY METHODS THAT SHOULD BE MODIFIED ARE:
  * <BR>hasEulerCircuit, getEulerCircuit, 
  * and whichever helper function they need, such as findEuler.
  * 
  */
public class Graph {
    /**
     * Total number of vertices.
     */
    int vertices = 0;
    /**
     * Adjacency matrix of graph.
     * <br>edges[x][y] is the number of edges from vertex x to vertex y.
     */
    int[][] edges;
    /**
     * Total number of edges in graph
     */
    int totaledges = 0;
    /**
     * Used by graph visitors to keep track of visited vertices.
     */

    boolean[] visitedV;
    /**
     * Used by graph visitors to keep track of visited edges.
     */
    int[][] visitedE;
    /**
     * Used by graph visitors to keep track of unvisited edges
     * as an alternative to using visitedE.
     */
    int[][] unvisitedE;
    /**
     * Used to generate edges randomly
     */
    Random rand = new Random();   
    
    /**
     * 
     * Creates a new undirected Graph whose content will be read from the Scanner.
     * <br>Input format consists of non-negative integers separated by white space as follows:
     * <ul>
     * <li>First positive integer specifies the number of vertices n
     * <li>Next n*n integers specify the edges, listed in adjacency matrix order
     * </ul>
     * The graph information will be rejected when incorrect data is read.
     * @param in Scanner used to read graph description
     */
 
    int totalEdges = 0;
    int[] stackj = new int[100];
    int[] stacki  = new int[100];
	int[] circuit = new int[100];
	int countStk = 0;
	int countCir = 0;
	int[][] graph;
	int remainingedges = getEdges() +1;
    
    Graph(Scanner in) {
        int i, j;
        boolean inputMistake = false;
        
        // Read number of vertices
        vertices = in.nextInt();
        if (vertices <= 0) {
            System.out.println("Error: number of vertices must be positive");
            vertices = 0;
            return;
            
        }
        
        // Read adjacency matrix        
        edges = new int[vertices][vertices];
        for (i=0; i<vertices; i++) {
            for (j=0; j<vertices; j++) {
                edges[i][j]=in.nextInt();
                if (j>=i)   totaledges += edges[i][j];
                if (edges[i][j] <0){
                    System.out.println("Error: number of edges cannot be negative");
                    inputMistake = true;            
                }
            }
        }
        
        // Verify that adjacency matrix is symmetric
        for (i=0; i<vertices; i++)
            for (j=i+1; j<vertices; j++)
                if (edges[i][j] != edges[j][i]) {
                    System.out.println(
                        "Error: adjacency matrix is not symmetric");
                    inputMistake = true;
                }
        
        if (inputMistake) 
            this.vertices = 0;
        
        // Prepare visitation status arrays
        else {
            visitedV = new boolean[vertices];
            visitedE = new int[vertices][vertices];
            unvisitedE = new int[vertices][vertices];   
            clearVisited();
        }
    }
    /**
     * Creates a randomly generated graph according to specifications,
     * or an empty graph if the specifications are faulty.
     * @param vertices Number of vertices in graph - must be positive
     * @param maxParallelEdges Maximum number of parallel edges between any two vertices - must be non-negative
     */
     Graph( int vertices, int maxParallelEdges) {
        if (vertices <= 0) {
            System.out.println("Error: number of vertices must be positive");
            return;
        }
        if (maxParallelEdges <0) {
            System.out.println("Error: maxVertexDegree cannot be negative");
            return;           
        }
        this.vertices = vertices;
        edges = new int[vertices][vertices]; 
         
        // Populate edges randomly        
        int randmax = maxParallelEdges+1;
        for (int i=0; i<vertices; i++)
            for (int j=i; j<vertices; j++) {
                edges[i][j] = rand.nextInt(randmax); 
                edges[j][i] = edges[i][j];
                totaledges += edges[i][j];
            }  
       
        // Prepare visitation status arrays
        visitedV = new boolean[vertices];
        visitedE = new int[vertices][vertices];
        unvisitedE = new int[vertices][vertices];   
        clearVisited();
       
    
  
     }
    
    /**
     * Resets visitedV, visitedE, and unvisitedE matrices for a new visitation
     */
     private void clearVisited() {
        for (int i=0; i<vertices; i++) {
            visitedV[i] = false;
            for (int j=0; j<vertices; j++) {
                visitedE[i][j] = 0;
                unvisitedE[i][j] = edges[i][j];
            }
        }
    }
    
   /**
    * Returns a String representation of the graph
    * which is a 2-D representation of the adjacency matrix of that graph.
    * @return The 2-D representation of the adjacency matrix of that graph.
    * 
    */    
    @Override
    public String toString() {
        return matrixtoString(edges);
    }

    /**
     * Returns a String representation of 2 dimensional matrix
     * of size vertices X vertices.  
     * This can be used to visualize edges, visitedE, and unvisitedE
     * @param matrix matrix to be represented
     * @return 2D string representation of matrix
     */
    private String matrixtoString(int[][] matrix) {
        String result = "";
        for (int i=0; i<vertices; i++) {
            for (int j=0; j<vertices; j++) {
                result += matrix[i][j];
                result += " ";
            }
            result += "\n";
        }
        return result;
         
    }
    
    /**
    * Returns the number of vertices in the graph.
    * @return The number of vertices in the graph.
    *
    */  
    public int getVertices() {
        return vertices;
    }
    
    /**
    * Returns the number of edges in the graph.
    * @return The number of edges in the graph.
    *
    */  
    public int getEdges() {
        return totaledges;
    }   
    
    /**
     * Returns the adjacency matrix of the graph
     * @return The adjacency matrix of the graph 
     */
    public int[][] getMatrix() {
        return edges;
    }
    
    /**
     * Returns the number of edges from sourceV to destV 
     * @param sourceV The source vertex
     * @param destV The destination vertex
     * @return the number associated with edges from sourceV to destV
     */
    public int getEdges(int sourceV, int destV) {
        if (sourceV>=0 && sourceV<vertices && destV>=0 && destV<vertices)
            return edges[sourceV][destV];
        else
            return 0;
    }  
      
    /**
     * Verifies whether graph is connected
     * @return True iff graph is connected
     */
    public boolean isConnected() {
        clearVisited();
        DFSvisit(0);
        for (int i=0; i<vertices; i++)  
            if (!visitedV[i]) {
                clearVisited();
                return false;
            }
        clearVisited();
        return true;        
    }
    
    /**
     * Conducts a Depth First Search visit of the unvisited vertices 
     * of the graph starting at vertex.  
     * <br>Ties between vertices are broken in numeric order.
     * <br>Used by isConnected()
     * @param vertex First vertex to be visited.
     */
    private void DFSvisit(int vertex) {
        visitedV[vertex] = true;
        for (int i=0; i<vertices; i++)
            if (edges[vertex][i]!=0 && !visitedV[i])
                DFSvisit(i);
    } 
    /**
     * Decides whether the graph contains an Euler circuit
     * This implements Euler's theorem about the existence of Euler circuits,
     * @param printexplanation when this is true, an explanation is also printed.
     * @return True iff the graph contains an Euler circuit
     */
    public boolean hasEulerCircuit(boolean printexplanation) {
    	this.graph = getMatrix();
    	if(doesHaveEuler() && isConnected() && printexplanation){
    		System.out.println("This graph contains an Euler circuit.");
        	return true;
    	}
    	else  {		System.out.println("This graph does not contaion Euler circuit."); 
    	return false;
    	}

    }
       
    
    public Walk getEulerCircuit() {
        
    	boolean hasEuler = doesHaveEuler();
    
    	if(hasEuler == true && isConnected() ){
    		int i =0, j = adjedg(i,0);
    	
    		addStk(i,j);
    		minuseg(i,j);
 
    		remainingedges--; 
    		findEuler(0,j,0);
    		
    		Walk w = new Walk(getEdges());
         	for(int p = 0; p < countStk; p++){
         		int t = stacki[p];
         		w.addVertex(t);
         	}
    		return w ;
    	}
   
		return null; 
    	
		
    	
    }
    
    private void findEuler(int startV, int currentV, int firstrun){
        
    	int i = currentV;
    	int j = adjedg(i,0); 
    	
    	if(remainingedges == 0 && isConnected()){
    		return;
    	}
    	
    	if(this.graph[i][j] > 1){
    		i= edgeLoop(i,j); 
    		currentV = i;
    		j = adjedg(i,0);
    		
    		if(remainingedges==0){return;}
    		findEuler(startV,i,1);
    		return;
    	}
    	else if(currentV == startV && firstrun !=0){
    		 int tmp = countStk; tmp--;
    		j = sameAsStart(startV);
    		i =  stacki[tmp];
  
    	}
    		
    	
    		addStk(i,j);
    		minuseg(i,j);
    		remainingedges--;
    		findEuler(startV,j,1);
    		return;
    	
    
    	
     }
     public int sameAsStart(int startV){
    	 int i, j;
    	 int tmp = --countStk;
        		i = stacki[tmp];
        		j = stackj[tmp];
        			
        		graph[i][j]++; graph[j][i]++;
        		
        		j = adjedg(i, stackj[tmp]+1);
        	
        		//findEuler(startV,i,1);
        		return j;
        		
     }
     public int edgeLoop(int i,int j){
    	int rul;
    	 addStk(i,j);
    
    	 if(this.graph[i][j] == 0){
    		removeLast();
    		 return i;
    	}
    	
    	this.graph[i][j]--;
    	this.graph[j][i]--;
    	remainingedges--;
 		rul = edgeLoop(j,i);
		return rul;
     }
     public void minuseg(int i, int j){
    	if(i == j){ graph[i][j]--;}
    	else{
    	 graph[i][j]--;
    	 graph[j][i]--;}
     }
     public void removeLast(){
    	if(countStk !=0){
    		countStk--;	
    		stacki[countStk] = -1;
    	} 
     }
     public int adjedg(int row, int j){
     	for(int j1=j; j1 < getVertices(); j1++){
     		
     		if(graph[row][j1] !=0){ 
     		
     			return j1;
     		} 
     		
     	}
     	return 0;
     }
     public boolean doesHaveEuler(){
	
	
		for(int i=0; i < getVertices(); i++){
			int temp = 0;
			for(int j=0; j < getVertices(); j++){
			
				if(i != j){
					temp += graph[i][j];
				
				}	
				
			} 
			if(temp % 2 != 0){ return false;}
		
		} 
		
		return true;	
	}
     public void addStk(int i,int j ){
     	stacki[countStk] = i;
     	stackj[countStk] = j;
     	countStk++;
     }
     public void printStacki(){
    	 for(int i=0; i <= countStk ; i++){
      		System.out.print(" "+stacki[i]);
      	
      	}
      	
      
     }
    
}













