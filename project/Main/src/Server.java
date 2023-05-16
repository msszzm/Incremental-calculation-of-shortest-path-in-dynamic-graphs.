import datastructure.Graph;
import datastructure.IGraph;
import remoteBatch.BatchHandler;
import remoteBatch.IRemoteBatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Server{

    public static void main(String[] args) {
        System.out.println("Starting Server...");
        IGraph graph= readGraph();
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
        IGraph graph= null;
        try(FileReader reader= new FileReader("input.txt");
            BufferedReader bufferedReader= new BufferedReader(reader);){
            graph= new Graph();
            while (true){
                String line = bufferedReader.readLine();
                if(line.equals("S"))
                    break;
                String[] splitter= line.split(" ");
                if(splitter.length !=2){
                    throw new RuntimeException("Expected two indices!");
                }
                graph.insertEdge(Integer.parseInt(splitter[0]),Integer.parseInt(splitter[1]));
            }
            System.out.println(graph);
        }catch (Exception e){
            e.printStackTrace();
        }

        return graph;
    }
}
