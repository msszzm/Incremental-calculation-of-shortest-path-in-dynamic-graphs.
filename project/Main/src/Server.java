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
            Registry registry= LocateRegistry.createRegistry(1099);
            registry.rebind("Update", remoteStub);
            System.out.println("Server is ready for action!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }   
    
    private static IGraph readGraph(){
        IGraph graph= new Graph();
        graph.insertEdge(1,2);
        graph.insertEdge(2,3);
        graph.insertEdge(3,1);
        graph.insertEdge(4,1);
        graph.insertEdge(2,4);

        /*
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
        sc.close();*/
        return graph;
    }
}
