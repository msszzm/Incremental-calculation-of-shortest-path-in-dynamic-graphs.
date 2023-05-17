package datastructure;

import datastructure.IGraph;
import server.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Graph implements IGraph {
    private HashMap<Integer,HashSet<Integer>> graph;
    private int[][] shortestPath;
    private static final boolean enhancedResponse=false;
    public Graph(){
        this.graph= new HashMap<>();
        clearCache();
    }
    @Override
    public boolean insertEdge(int node1,int node2){
        this.insertIfNotFound(node1);
        this.insertIfNotFound(node2);
        if(this.graph.get(node1).contains(node2))
            return false; //Already there!
        this.graph.get(node1).add(node2);
        Logger.writeLog("Insert Edge "+node1+" ---> "+node2);
        clearCache();
        return true;
    }
    @Override
    public boolean deleteEdge(int node1, int node2) {
        if(this.graph.get(node1) == null)
            return true;
        if(!this.graph.get(node1).contains(node2))
            return false; //It is not there!
        this.graph.get(node1).remove(node2);
        Logger.writeLog("Delete Edge "+node1+" ---> "+node2);
        clearCache();
        return true;
    }
    @Override
    public int getShortestPath(int n1, int n2) {
        if(enhancedResponse && this.shortestPath[n1][n2] != 0 )
            return this.shortestPath[n1][n2];
        TreeMap<Integer,Integer> priorityQueue= new TreeMap<>();
        priorityQueue.put(n1, 0);
        HashSet<Integer> visited= new HashSet<>();
        try {
            while (!priorityQueue.isEmpty()) {
                int node = priorityQueue.firstKey();
                int distance = priorityQueue.get(node);
                priorityQueue.pollFirstEntry();
                visited.add(node);
                HashSet<Integer> neighbors = this.graph.get(node);
                if(neighbors == null)
                    continue;
                for (int neighbor : neighbors) {
                    if (neighbor == n2) {//Found!
                        if(enhancedResponse)
                        this.shortestPath[n1][n2]=distance+1;
                        Logger.writeLog("Query "+n1+" ---> "+n2+" = "+(distance+1));
                        return distance + 1;
                    }
                    if (!visited.contains(neighbor)) {
                        if(enhancedResponse)
                        this.shortestPath[n1][neighbor]=distance+1;
                        priorityQueue.put(neighbor, distance + 1);
                    }
                }
            }
            Logger.writeLog("Query "+n1+" ---> "+n2+" = "+(-1));
            return -1; //No Path Found!
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public void clearCache(){
        if(enhancedResponse)
            shortestPath=new int[1001][1001];

    }
    private void insertIfNotFound(int node){
        if(this.graph.containsKey(node))
            return;
        this.graph.put(node, new HashSet<>());
    }
    public String toString(){
        String x= "";
        for(int node : this.graph.keySet()){
            x+=node+" ----> ";
            for(int n : this.graph.get(node))
                x+=n+" , ";
            x+="\n";
        }
        return x;
    }
}
