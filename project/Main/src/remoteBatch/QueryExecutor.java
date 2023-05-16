package remoteBatch;

import datastructure.IGraph;

public class QueryExecutor implements Runnable{

    private IGraph graph;
    private int from;
    private int to;
    private int index;
    private int shortestDistance;


    QueryExecutor(IGraph graph, int from,int to, int index){
        this.graph= graph;
        this.from= from;
        this.to= to;
        this.index= index;

    }

    public int getIndex() {
        return index;
    }

    public int getShortestDistance() {
        return shortestDistance;
    }
    @Override
    public void run() {
        shortestDistance= this.graph.getShortestPath(from,to);
    }
}
