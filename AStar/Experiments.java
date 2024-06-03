
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Experiments {

    // scegliamo a tempo di esecuzione il numero di esperimenti che vogliamo eseguire, il numero di nodi e la probabilità per ogni grafo generato
    public static void main(String[] args) {

        // variabili per l'input dell'utente
        int numberOfExperiments = 0;
        int numberOfNodes = 0;
        double probability = 0.0;
        String nodesFile = "";
        String edgesFile = "";
        Scanner userInput = new Scanner(System.in);

        double totalNumberOfEdges = 0;
        int successfulExperiments = 0;
        long totalTimeGraphGen = 0;
        long totalTimeAStar = 0;
        long startTimeExperiment;
        long endTimeExperiment;
        long totalTimeExperiment;
        double totalDistance = 0;
        double totalCalculatedDistance = 0;
        Node start, goal;
        Node[] nodes = new Node[0];

        Random rand = new Random(System.currentTimeMillis());
        AStar aStar = new AStar();

        System.out.println("Scegli cosa fare (scrivi 1, 2 o 3):\n1- esperimenti su grafo letto da file\n2- esperimenti su grafi generati tramite Erdos-Renyi\n3- generazione e scrittura su file di un grafo tramite Erdos-Renyi");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire 1, 2 o 3:\n1- esperimenti su grafo letto da file\n2- esperimenti su grafi generati tramite Erdos-Renyi\n3- generazione e scrittura su file di un grafo tramite Erdos-Renyi");
        }
        int usersChoice = userInput.nextInt();
        if (usersChoice != 1 && usersChoice != 2 && usersChoice != 3) {
            userInput.close();
            System.out.println("Il numero inserito deve essere 1, 2 o 3");
            return;
        }

        switch (usersChoice) {
            case 1 -> {
                numberOfExperiments = Utilities.getInputExperimentsNumber(userInput);
                String[] names = Utilities.getInputFileNames(userInput);
                nodesFile = names[0];
                edgesFile = names[1];
                probability = Utilities.getInputProbability(userInput);
                try {
                    nodes = Utilities.loadGraphFromFiles(nodesFile, edgesFile, probability);
                } catch (IOException e) {
                    System.out.println("Errore nel caricamento del grafo dai file");
                    return;
                }
                numberOfNodes = nodes.length;
            }
            case 2 -> {
                numberOfExperiments = Utilities.getInputExperimentsNumber(userInput);
                numberOfNodes = Utilities.getInputNodesNumber(userInput);
                probability = Utilities.getInputProbability(userInput);
            }
            case 3 -> {
                numberOfNodes = Utilities.getInputNodesNumber(userInput);
                probability = Utilities.getInputProbability(userInput);
                try {
                    Utilities.writeErdosRenyiGraphToFile(GraphGeneration.generateErdosRenyiGraph(numberOfNodes, probability), probability);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
                return;
            }
        }
        startTimeExperiment = System.nanoTime();
        userInput.close();
        for (int counter = 0; counter < numberOfExperiments; counter++) {

            // generiamo il grafo a partire dalla probabilità e numero di nodi dati
            if (usersChoice == 2) {
                try {
                    long startTimeGraphGen = System.nanoTime();
                    nodes = GraphGeneration.generateErdosRenyiGraph(numberOfNodes, probability);
                    long endTimeGraphGen = System.nanoTime();
                    totalTimeGraphGen += (endTimeGraphGen - startTimeGraphGen);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    return;
                }
            }

            //Utilities.printGraph(nodes);
            for (Node node : nodes) {
                totalNumberOfEdges += node.getEdges().size();
            }
            //System.out.println(totalNumberOfEdges + " archi totali");

            // scegliamo a caso, tra i nodi del grafo, start e goal
            start = nodes[rand.nextInt(nodes.length)];
            do {
                goal = nodes[rand.nextInt(nodes.length)];
            } while (start == goal);

            long startTimeAStar = System.nanoTime();
            Result result = aStar.findPath(start, goal);
            long endTimeAStar = System.nanoTime();

            if (result != null) {
                List<Node> bestPath = result.getPath();
                //System.out.println("Percorso da " + start.getLabel() + " a " + goal.getLabel() + " con costo " + new DecimalFormat("#.##").format(result.getTotalCost()) + " trovato: ");
                successfulExperiments++;
                totalCalculatedDistance += result.getTotalCost();
                totalDistance += Utilities.euclideanDistance(start, goal);
                for (Node n : bestPath) {
                    //System.out.print(n.getLabel() + " ");
                }
                //System.out.println();
            } else {
                //System.out.println("Nessun percorso trovato");
            }

            //System.out.println("Tempo di esecuzione esperimento " + counter + " : " + (endTime - startTime) + " ns");
            totalTimeAStar += (endTimeAStar - startTimeAStar);
        }
        endTimeExperiment = System.nanoTime();
        totalTimeExperiment = endTimeExperiment - startTimeExperiment;
        System.out.println("Esperimenti che hanno avuto successo: " + successfulExperiments + " su " + numberOfExperiments + ", " + new DecimalFormat("#.###").format(((((double) successfulExperiments / numberOfExperiments) * 100))) + "%");
        System.out.println("Tempo totale di esecuzione: " + (totalTimeExperiment / 1000000) + " ms. Tempo medio per esperimento: " + (totalTimeExperiment / numberOfExperiments) + " ns. Tempo medio di AStar per esperimento: " + (totalTimeAStar / numberOfExperiments) + " ns");
        String genericResultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + probability + " " + (int)(totalNumberOfEdges/numberOfExperiments) + " " + successfulExperiments;
        String experimentsResultsData = genericResultsData + " " + totalTimeExperiment;
        String generationResultsData = genericResultsData + " " + totalTimeGraphGen;
        String aStarResultsData = genericResultsData + " " + totalTimeAStar + " " + new DecimalFormat("#.#####").format(totalDistance) + " " + new DecimalFormat("#.#####").format(totalCalculatedDistance);
        if (usersChoice == 1) {
            Utilities.writeResultToFile("Risultati" + nodesFile.substring(5), experimentsResultsData);
            Utilities.writeResultToFile("RisultatiAStar" + nodesFile.substring(5), aStarResultsData);
        } else {
            Utilities.writeResultToFile("Risultati.txt", experimentsResultsData);
            Utilities.writeResultToFile("RisultatiGenerazione.txt", generationResultsData);
            Utilities.writeResultToFile("RisultatiAStar.txt", aStarResultsData);
        }
    }
}
