package remoteBatch;

import datastructure.IGraph;
import remoteBatch.IRemoteBatch;

import java.rmi.RemoteException;
import java.util.Arrays;

public class BatchHandler implements IRemoteBatch {
    private IGraph graph;

    public BatchHandler(IGraph graph){
        super();
        this.graph= graph;
        System.out.println(this.graph);
    }

    @Override
    public synchronized String executeBatch(String command) throws RemoteException {
        String[] lines= command.split("\n");
        String message="";
        for(String line : lines) {
            message += execute(line);
        }
        System.out.println(message);
        return message;
    }
    private String execute(String line){
        if(line.charAt(0)=='F')
            return "";
        String[] query= line.split(" ");
        System.out.println(Arrays.toString(query));
        int node1= Integer.parseInt(query[1]);
        int node2= Integer.parseInt(query[2]);

        if(query[0].equals("Q"))
            return graph.getShortestPath(node1,node2)+"\n";
        if(query[0].equals("D")){
            graph.deleteEdge(node1,node2);
            return "";
        }
        if(query[0].equals("A")){
            graph.insertEdge(node1,node2);
            return "";
        }
        return "ERROR";
    }

}
