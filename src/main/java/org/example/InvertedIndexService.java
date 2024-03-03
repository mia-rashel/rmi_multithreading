package org.example;

//RMI interface
// InvertedIndexService.java
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface InvertedIndexService extends Remote {
    // Returns the inverted index of words along with their locations
    Map<String, List<Integer>> getInvertedIndex(String fileName) throws RemoteException;
}

