
public class Walk {
    /**
     * Marker for no vertex
     */
    public static int NOVERTEX=-1;
    /**
     * Maximum number of vertices in the walk.
     */
    int maxVertices;
    /**
     * Actual number of vertices in walk.
     */
    int totalVertices;
    /**
     * The vertices are listed in their order of traversal.
     * <br>Edges are not stored in this representation of walks.
     */
    int[] vertices;
   
    /**
     * Creates a new empty Walk with specified maximum number of edges.
     * @param maxVertices maximum number of edges in walk
     */
    Walk(int maxVertices) {
        this.maxVertices = maxVertices;
        totalVertices = 0;
        vertices = new int[maxVertices];
        for (int i=0; i<maxVertices; i++)    vertices[i] = NOVERTEX;
    }
    
   /**
    * Returns a String representation of the walk
    * which is simply a list of vertices separated by blanks.
    * @return The list of vertices in the walk separated by blanks
    * 
    */
    @Override
    public String toString() {
        String result = "";
        for (int i=0; i<totalVertices; i++) {
            result += vertices[i];
            result += " ";
       }
       return result;
    }

   /**
    * Returns the number of vertices in the walk.
    * <br>Note that in circuits the starting vertex will be counted twice.
    * @return The number of vertices in the walk
    *
    */  
    public int getVertices() {
        return totalVertices;
    }
    
   /** 
    
    * @param vertex Vertex to be added to walk
    * @return True iff the vertex could be added, i.e maxVertices was not reached
    * 
    */  
    public boolean addVertex(int vertex) {
        if (totalVertices == maxVertices)
               return false;
        vertices[totalVertices++] = vertex;
        return true;
    }
    
   /** 
    * Removes the last vertex added to walk if possible.
    * @return True iff the last vertex could be removed, i.e walk was not empty
    * 
    */  
    public boolean removeLastVertex() {
        if (totalVertices == 0)
            return false;
        vertices[--totalVertices] = 0;
        return true;
    }
    
    /**
     * Returns the nth vertex in the walk or Walk.NOVERTEX if there is no nth vertex.
     * Counting starts at 0.
     * @param n The position of the vertex to be returned, starting at 0.
     * @return the vertex at position n in the walk
     * or Walk.NOVERTEX if the walk doesn't have n vertices.
     */
    public int getVertex(int n) {
        if (n == totalVertices)
            return vertices[0];
        if (n>=0 && n<totalVertices)
            return vertices[n];
        else
            return NOVERTEX;
        
    }
}
