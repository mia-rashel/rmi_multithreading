package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.rmi.Naming;


public class InvertedIndexServiceImpl extends UnicastRemoteObject implements InvertedIndexService {
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private ExecutorService executorService;

    protected InvertedIndexServiceImpl() throws RemoteException {
        super();
        // Create a fixed thread pool with available processors
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
    }

    @Override
    public Map<String, List<Integer>> getInvertedIndex(String fileName) throws RemoteException {
        Map<String, List<Integer>> invertedIndex = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    invertedIndex.computeIfAbsent(word, k -> new ArrayList<>()).add(lineNumber * 1000 + i);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return invertedIndex;
    }

    public static void main(String[] args) {
        try {
            // Create an instance of the service
            InvertedIndexServiceImpl invertedIndexService = new InvertedIndexServiceImpl();
            LocateRegistry.createRegistry(8099);
            // Bind the service to the RMI registry
            Naming.rebind("rmi://168.138.73.201:8099/InvertedIndexService", invertedIndexService);

            System.out.println("InvertedIndexService is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
