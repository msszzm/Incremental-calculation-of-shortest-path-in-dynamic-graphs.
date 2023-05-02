package datastructure;

import datastructure.IGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Graph implements IGraph {
    private HashMap<Integer,HashSet<Integer>> graph;
    public Graph(){
        this.graph= new HashMap<>();
    }
    @Override
    public boolean insertEdge(int node1,int node2){
        this.insertIfNotFound(node1);
        this.insertIfNotFound(node2);
        if(this.graph.get(node1).contains(node2))
            return false; //Already there!
        this.graph.get(node1).add(node2);
        return true;
    }
    @Override
    public boolean deleteEdge(int node1, int node2) {
        if(!this.graph.get(node1).contains(node2))
            return false; //It is not there!
        this.graph.get(node1).remove(node2);
        return true;
    }
    @Override
    public int getShortestPath(int n1, int n2) {
        TreeMap<Integer,Integer> priorityQueue= new TreeMap<>();
        priorityQueue.put(n1, 0);
        HashSet<Integer> visited= new HashSet<>();
        while(!priorityQueue.isEmpty()){
            int node= priorityQueue.firstKey();
            int distance= priorityQueue.get(node);
            priorityQueue.pollFirstEntry();
            visited.add(node);
            HashSet<Integer> neighbors= this.graph.get(node);
            for(int neighbor : neighbors){
                if(neighbor == n2)//Found!
                    return distance+1;
                if(!visited.contains(neighbor))
                    priorityQueue.put(neighbor, distance+1);
            }   
        }
        return -1; //No Path Found!
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
