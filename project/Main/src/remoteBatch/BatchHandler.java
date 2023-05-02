package remoteBatch;

import datastructure.IGraph;
import remoteBatch.IRemoteBatch;

import java.rmi.RemoteException;

public class BatchHandler implements IRemoteBatch {
    private IGraph graph;

    public BatchHandler(IGraph graph){
        super();
        this.graph= graph;
        System.out.println(this.graph);
    }

    @Override
    public String executeBatch(String command) throws RemoteException {
        return null;
    }

}
