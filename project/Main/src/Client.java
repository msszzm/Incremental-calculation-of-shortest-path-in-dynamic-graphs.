import remoteBatch.IRemoteBatch;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try{
            Registry reg= LocateRegistry.getRegistry(null);
            IRemoteBatch stub= (IRemoteBatch) reg.lookup("Batch");
            /*String query= "Q 1 3\n";
            query+="A 4 5\n";
            query+="Q 1 5\n";
            query+="Q 5 1\n";
            query+="F";*/
            String query= "A 5 3\n";
            query+="Q 1 3\n";
            query+="D 2 3\n";
            query+="Q 1 3\n";
            query+="F";
            System.out.println("Waiting ...");
            System.out.println(stub.executeBatch(query));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
