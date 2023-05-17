package remoteBatch;

import datastructure.IGraph;

public class QueryExecutor implements Runnable{

    private IGraph graph;
    private final int from;
    private final int to;
    private final int index;
    private int shortestDistance;

    QueryExecutor(IGraph graph, int from,int to, int index){
        this.graph= graph;
        this.from= from;
        this.to= to;
        this.index= index;
    }
    @Override
    public void run() {
        shortestDistance= this.graph.getShortestPath(from,to);
    }

    public int getIndex() {
        return index;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }

}
