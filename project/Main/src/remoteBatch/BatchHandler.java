package remoteBatch;

import datastructure.IGraph;
import remoteBatch.IRemoteBatch;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedList;

public class BatchHandler implements IRemoteBatch {
    private IGraph graph;
    private static int counter= 0;
    private LinkedList<Thread> threads;
    private LinkedList<QueryExecutor> executors;
    private static final boolean ENHANCED_RESPONSE=true;


    public BatchHandler(IGraph graph){
        super();
        this.graph= graph;
    }

    @Override
    public  synchronized String executeBatch(String command) throws RemoteException {
        String[] lines= command.split("\n");
        String message="";
        if(ENHANCED_RESPONSE)
                message= executeEnhanced(lines);
        else {
            for (String line : lines) {
                message += execute(line);
            }
        }

        return "Query : "+(counter++)+"\n"+message;
    }

    private String executeEnhanced(String[] lines) {
        executors=new LinkedList<>();
        threads=new LinkedList<>();
        String[] returnedMessages= new String[lines.length-1];
        int index=0;
        for(String line:lines){
            if(index== lines.length-1)
                break;
            String[] query= line.split(" ");
            int node1= Integer.parseInt(query[1]);
            int node2= Integer.parseInt(query[2]);
            if(query[0].equals("Q")){
                QueryExecutor executor= new QueryExecutor(graph,node1,node2,index);
                Thread thread=new Thread(executor);
                executors.add(executor);
                threads.add(thread);
                thread.start();
            }
            else if(query[0].equals("A")){
                releaseThreads(returnedMessages);
                graph.insertEdge(node1,node2);
                returnedMessages[index]="";
            }
            else{//Delete
                releaseThreads(returnedMessages);
                graph.deleteEdge(node1,node2);
                returnedMessages[index]="";
            }
            index++;
        }
        releaseThreads(returnedMessages);
        String x="";
        for(String mes:returnedMessages)
            x+=mes;
        return x;
    }

    private void releaseThreads(String[] messages){
        try{
            for(int i= 0;i<threads.size();i++){
                threads.get(i).join();
                QueryExecutor executor=executors.get(i);
                messages[executor.getIndex()]=executor.getShortestDistance()+"\n";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        threads= new LinkedList<>();
        executors= new LinkedList<>();
    }

    private  String execute(String line){
        if(line.charAt(0)=='F')
            return "";
        String[] query= line.split(" ");
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
