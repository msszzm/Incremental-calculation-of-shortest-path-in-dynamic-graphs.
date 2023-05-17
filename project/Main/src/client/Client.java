package client;

import remoteBatch.IRemoteBatch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Client implements Runnable {
    public static final int NUMBER_OF_GRAPH_NODES= 10000;
    public static final String[] QUERIES_TYPES= new String[]{"Q","A","D"};
    public static Random random= new Random();
    public static final int MAX_TIME_MILLI= 5000;
    public static final int NUMBER_OF_CLIENTS= 100;
    public static final int MIN_NUMBER_OF_RECORDS= 4000;
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
    private static void cleanDirectory(File f){
        File[] files= f.listFiles();
        for(File file: files){
            if(file.isDirectory())
                cleanDirectory(file);
            else
                file.delete();
        }
    }
    private static void manageDirectory(){
            try{
                File f= new File("logs");
                cleanDirectory(f);
            }
            catch (Exception e){
                throw  new RuntimeException();
            }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread[] threads= new Thread[NUMBER_OF_CLIENTS];
        manageDirectory();
        BufferedWriter responseWriter= new BufferedWriter(new FileWriter("logs/responses.log"));
        responseWriter.write("ClientID,number of queries,Response Time in milliseconds\n");
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
                String[] gen= generateQuery().split("--");
                int numberOfQueries= Integer.parseInt(gen[0]);
                String query= gen[1];
                long currentTime= System.currentTimeMillis();
                String result=sendRMI(query);
                writeResponse(numberOfQueries,System.currentTimeMillis()-currentTime);
                writeResponseData(query,result);

            }catch (Exception e){
                System.out.println("Error in client"+ID);
            }
        }
    }

    public static String generateQuery(){
        int numberOfQueries=0;
        String x= "";
        for(int i= 0;i<4;i++){
            String query= QUERIES_TYPES[random.nextInt(3)];
            if (query.equals("Q"))
                numberOfQueries++;
            int node1,node2;
            do {
                node1 = random.nextInt(NUMBER_OF_GRAPH_NODES) + 1;
                node2 = random.nextInt(NUMBER_OF_GRAPH_NODES) + 1;
            } while (node1 == node2);
            query += " "+node1+" "+node2+"\n";
            x+=query;
        }
        x+="F";
        return numberOfQueries+"--"+x;
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
    private synchronized void writeResponse(int numberOfQueries,long responseTime){
        try {
            FileWriter writer= new FileWriter(this.responseWriter,true);
            BufferedWriter bufferedWriter=new BufferedWriter(writer);
            bufferedWriter.write(this.ID+","+numberOfQueries+","+responseTime + "\n");
            bufferedWriter.close();
            counter--;
            if(counter % 100 == 0)
                System.out.println("Recorded "+(MIN_NUMBER_OF_RECORDS-counter)+ "records!");

        }catch (Exception ignored){
        }
    }
}
