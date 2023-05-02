import datastructure.Graph;
import datastructure.IGraph;
import remoteBatch.BatchHandler;
import remoteBatch.IRemoteBatch;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Server{


    public static void main(String[] args) {
        System.out.println("Starting Server...");
        IGraph graph= readGraph();
        System.out.println(graph);
        BatchHandler batchHandler= new BatchHandler(graph);
        try{
            IRemoteBatch remoteStub=(IRemoteBatch) UnicastRemoteObject.exportObject(batchHandler, 0);
            Registry registry= LocateRegistry.getRegistry();
            registry.rebind("Batch", remoteStub);
            System.out.println("Server is ready for action!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
    
    private static IGraph readGraph(){
        IGraph graph= new Graph();
        Scanner sc= new Scanner(System.in);
        while(true){
            String line= sc.nextLine();
            if(line.equals("S"))
                break;
            String[] splitter= line.split(" ");
            if(splitter.length !=2){
                sc.close();
                throw new RuntimeException("Expected two indices!");
            }
            graph.insertEdge(Integer.parseInt(splitter[0]),Integer.parseInt(splitter[1]));
        }
        sc.close();
        return graph;
    }
}
