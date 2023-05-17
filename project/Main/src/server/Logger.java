package server;

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;

public class Logger {

    private static final File logFile= new File("logs/server.log");

    private static String getLoggingMessage(String message){
        return message+"\t\t\t"+(new Timestamp(System.currentTimeMillis()).toString())+"\n";
    }
    private Logger(){
        try {
            File file = new File("logs/server.log");
            FileWriter writer=new FileWriter(file);
            writer.write(getLoggingMessage("Server created"));
            writer.close();
        }catch (Exception ignored){
        }
    }
    public static synchronized void writeLog(String message){
        try(FileWriter writer= new FileWriter(logFile,true)){
            writer.write(getLoggingMessage(message));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
