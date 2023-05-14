package client;

import remoteBatch.IRemoteBatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.PublicKey;
import java.util.Random;

public class Client implements Runnable {
    public static final int NUMBER_OF_NODES= 1000;
    public static final String[] QUERIES_TYPES= new String[]{"Q","A","D"};
    public static Random random= new Random();
    public static final int MAX_TIME_MILLI= 10000;
    public static final int NUMBER_OF_CLIENTS= 5;
    public static final int MIN_NUMBER_OF_RECORDS= 100;
    public  String responseWriter;
    public String clientWriter;
    public static int counter= MIN_NUMBER_OF_RECORDS; //Minimum number of records required to end.


    private int ID;

    public Client(int ID, String responseWriter){
        this.ID= ID;
        this.responseWriter= responseWriter;
        this.clientWriter= "logs/client/"+this.ID+".log";
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.clientWriter));
            writer.close();
        }catch (Exception ignored){}
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Thread[] threads= new Thread[NUMBER_OF_CLIENTS];
        BufferedWriter responseWriter= new BufferedWriter(new FileWriter("logs/responses.log"));
        responseWriter.write("ClientID,Response Time in milliseconds\n");
        responseWriter.close();
        for(int i=0;i<threads.length;i++) {
            Client client= new Client(i,"logs/responses.log");
            threads[i] = new Thread (client);
        }
        for(Thread th: threads)
            th.start();
        for(Thread th: threads)
            th.join();
        System.out.println("Thread Ended!");

    }
    public  String sendRMI(String query) {
        try{
            Registry reg= LocateRegistry.getRegistry(1099);
            IRemoteBatch stub= (IRemoteBatch) reg.lookup("Update");
            return stub.executeBatch(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        while(counter>=0){
            try{
                Thread.sleep(random.nextInt(MAX_TIME_MILLI));
                String query= generateQuery();
                long currentTime= System.currentTimeMillis();
                String result=sendRMI(query);
                writeResponse(System.currentTimeMillis()-currentTime);
                writeResponseData(query,result);

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

    private void writeResponseData(String query, String result){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.clientWriter, true));
            writer.write(query + "\n\n");
            writer.write(result);
            writer.write("\n\t\t-------------------------------------------------------\n");
            writer.close();
        }catch (Exception ignored){
            ignored.printStackTrace();
        }
    }
    private synchronized void writeResponse(long responseTime){
        try {
            FileWriter writer= new FileWriter(this.responseWriter,true);
            BufferedWriter bufferedWriter=new BufferedWriter(writer);
            bufferedWriter.write(this.ID+","+responseTime + "\n");
            bufferedWriter.close();
            counter--;
            if(counter % 10 == 0)
                System.out.println("Recorded "+(MIN_NUMBER_OF_RECORDS-counter)+ "records!");

        }catch (Exception ignored){
        }
    }
}
