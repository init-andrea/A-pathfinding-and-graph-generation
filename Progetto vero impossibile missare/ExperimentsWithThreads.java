import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExperimentsWithThreads {

    public static void main(String[] args) {
        int numberOfExperiments;
        int numberOfNodes;
        double probability;
        Scanner userInput = new Scanner(System.in);

        System.out.println("Inserisci il numero di esperimenti da eseguire e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        numberOfExperiments = userInput.nextInt();

        System.out.println("Inserisci il numero di nodi del grafo e premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero intero");
        }
        numberOfNodes = userInput.nextInt();

        System.out.println("Inserisci la probabilità (compresa tra 0 e 1) di generare un arco tra due vertici e premi invio:");
        while (true) {
            if (userInput.hasNextDouble()) {
                probability = userInput.nextDouble();
                if (probability >= 0 && probability <= 1) {
                    break;
                } else {
                    System.out.println("Riprova, devi inserire un numero compreso tra 0 e 1");
                }
            } else {
                System.out.println("Riprova, devi inserire un numero");
                userInput.next();
            }
        }
        userInput.close();

        long startExperimentTime = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<ExperimentResult>> results;
        try {
            results = executorService.invokeAll(createTasks(numberOfExperiments, numberOfNodes, probability));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        executorService.shutdown();

        int successfulExperiments = 0;
        int failedExperiments = 0;
        int numberOfConnectedGraphs = 0;
        long resAStar = 0;

        for (Future<ExperimentResult> future : results) {
            try {
                ExperimentResult result = future.get();
                successfulExperiments += result.successful ? 1 : 0;
                failedExperiments += result.successful ? 0 : 1;
                numberOfConnectedGraphs += result.connectedGraph ? 1 : 0;
                resAStar += result.executionTime;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        long endExperimentTime = System.nanoTime();
        long totalTime = endExperimentTime - startExperimentTime;

        System.out.println("Esperimenti che hanno avuto successo: " + successfulExperiments + ", " + ((((double) successfulExperiments / numberOfExperiments) * 100)) + "%");
        System.out.println("Esperimenti che sono falliti: " + failedExperiments + ", " + ((((double) failedExperiments / numberOfExperiments) * 100)) + "%");
        System.out.println("Grafi connessi: " + numberOfConnectedGraphs + ", il " + ((double) numberOfConnectedGraphs / numberOfExperiments * 100) + "%");
        System.out.println("Tempo totale: " + (totalTime / 1000000) + " ms. Tempo di esecuzione medio: " + totalTime / numberOfExperiments + " ns");

        String resultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + probability + " " + successfulExperiments + " " + numberOfConnectedGraphs + " " + totalTime;
        System.out.println(resultsData);
        String aStarResultsData = "" + numberOfExperiments + " " + numberOfNodes + " " + probability + " " + (resAStar / numberOfExperiments);
        Utilities.writeToFile("RisultatiThreads.txt", resultsData);
        Utilities.writeToFile("RisultatiAStarThreads.txt", aStarResultsData);
    }

    private static List<Callable<ExperimentResult>> createTasks(int numberOfExperiments, int numberOfNodes, double probability) {
        List<Callable<ExperimentResult>> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfExperiments; i++) {
            tasks.add(new ExperimentTask(numberOfNodes, probability));
        }
        return tasks;
    }

    static class ExperimentTask implements Callable<ExperimentResult> {
        private final int numberOfNodes;
        private final double probability;
        private final Random rand = new Random(System.currentTimeMillis());
        private final AStar aStar = new AStar();

        public ExperimentTask(int numberOfNodes, double probability) {
            this.numberOfNodes = numberOfNodes;
            this.probability = probability;
        }

        @Override
        public ExperimentResult call() {
            Node[] nodes = Utilities.generateErdosRenyiGraph(numberOfNodes, probability);
            boolean connectedGraph = Utilities.isConnected(nodes);

            Node start = nodes[rand.nextInt(nodes.length)];
            Node goal;
            do {
                goal = nodes[rand.nextInt(nodes.length)];
            } while (start == goal);

            long startTime = System.nanoTime();
            List<Node> bestPath = aStar.findPath(start, goal);
            long endTime = System.nanoTime();

            boolean successful = bestPath != null;
            long executionTime = endTime - startTime;

            return new ExperimentResult(successful, connectedGraph, executionTime);
        }
    }

    static class ExperimentResult {
        final boolean successful;
        final boolean connectedGraph;
        final long executionTime;

        public ExperimentResult(boolean successful, boolean connectedGraph, long executionTime) {
            this.successful = successful;
            this.connectedGraph = connectedGraph;
            this.executionTime = executionTime;
        }
    }
}
