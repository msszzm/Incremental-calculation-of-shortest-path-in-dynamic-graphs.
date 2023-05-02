package datastructure;

public interface IGraph {
    public boolean insertEdge(int n1,int n2);
    public boolean deleteEdge(int n1,int n2);
    public int getShortestPath(int n1,int n2);
}
