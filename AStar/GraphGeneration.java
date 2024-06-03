import java.util.Random;

public class GraphGeneration{
    static final double MAX_X_BOUND = 100000;
    static final double MAX_Y_BOUND = 100000;
    
    // p è la probabilità di avere un arco tra due vertici (p=1 grafo denso), n è il numero dei vertici
    public static Node[] generateErdosRenyiGraph(int n, double p) throws IllegalArgumentException{

        // Check sugli input
        if (p <= 0 || p > 1) {
            //System.out.println("La probabilità di avere un arco tra due vertici deve essere compresa tra 0 e 1");
            throw new IllegalArgumentException("La probabilità di avere un arco tra due vertici deve essere compresa tra 0 e 1");
        }
        if (n <= 0) {
            //System.out.println("Il numero di vertici deve essere positivo");
            throw new IllegalArgumentException("Il numero di vertici deve essere positivo");
        }

        // lettura maxX e maxY da console
        /*Scanner userInput = new Scanner(System.in);
        System.out.println("Inserisci la coordinata massima sull'asse x: ");
        while (!userInput.hasNextDouble()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero");
        }
        double maxX = userInput.nextDouble();
        System.out.println("Inserisci la coordinata massima sull'asse y: ");
        while (!userInput.hasNextDouble()) {
            userInput.next();
            System.out.println("Riprova, devi inserire un numero");
        }
        double maxY = userInput.nextDouble();
        userInput.close();*/

        // scelta maxX e maxY con Random
        Random rand = new Random(System.currentTimeMillis());
        double maxX = rand.nextDouble(MAX_X_BOUND);
        double maxY = rand.nextDouble(MAX_Y_BOUND);

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(String.valueOf(i), rand.nextDouble(maxX), rand.nextDouble(maxY));
        }
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                if (rand.nextDouble() < p) {                                    // scegliamo se creare o no un arco in base alla probabilità p
                    Node nj = nodes[j];
                    Node nk = nodes[k];
                    double cost = Utilities.euclideanDistance(nj, nk);        // calcoliamo il costo dell'arco come distanza euclidea tra i due vertici
                    if (!nj.hasEdge(nk) && !nk.hasEdge(nj)) {
                        nodes[j].addEdge(nk, cost);
                        nodes[k].addEdge(nj, cost);                           // aggiungiamo un arco in entrambe le direzioni per un grafo doppiamente orientato/non orientato
                    }
                }
            }
        }
        return nodes;
    }

}