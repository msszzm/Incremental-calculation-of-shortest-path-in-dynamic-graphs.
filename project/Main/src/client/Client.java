package client;

import remoteBatch.IRemoteBatch;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Client implements Runnable {
    public static final int NUMBER_OF_NODES= 5;
    public static final String[] QUERIES_TYPES= new String[]{"Q","A","D"};
    public static Random random= new Random();
    public static final int MAX_TIME_MILLI= 10000;
    private int ID;

    public Client(int ID){
        this.ID= ID;
    }

    public static void main(String[] args) {
        Thread[] threads= new Thread[10];
        for(int i=0;i<10;i++) {
            Client client= new Client(i);
            threads[i] = new Thread (client);
        }
        for(Thread th: threads)
                th.start();
    }
    public  String sendRMI(String query) {
        try{
            Registry reg= LocateRegistry.getRegistry(null);
            IRemoteBatch stub= (IRemoteBatch) reg.lookup("Batch");
            System.out.println("Waiting ...");
            return stub.executeBatch(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(random.nextInt(MAX_TIME_MILLI));
                String query= generateQuery();
                System.out.println("Now Sending the query: \n"+ query);
                String result=sendRMI(query);
                System.out.println("Client "+ID+"Received!");
                System.out.println(result);
            }catch (Exception e){
                System.out.println("Error in client"+ID);
            }
        }
    }

    public static String generateQuery(){
        String x= "";
        for(int i= 0;i<4;i++){
            String query= QUERIES_TYPES[random.nextInt(3)];
            int node1,node2;
            do {
                node1 = random.nextInt(NUMBER_OF_NODES) + 1;
                node2 = random.nextInt(NUMBER_OF_NODES) + 1;
            } while (node1 == node2);
            query += " "+node1+" "+node2+"\n";
            x+=query;
        }
        x+="F";
        return x;
    }
}
