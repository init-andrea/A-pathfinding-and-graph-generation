
import java.util.List;
import java.util.Scanner;

public class Experiments {

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Inserisci il numero di esperimenti da eseguire e poi premi invio: ");
        while (!userInput.hasNextInt()) {
            userInput.next();
        }
        int numberOfExperiments = userInput.nextInt();
        userInput.close();
        double successfulExperiments = 0;
        double failedExperiments = 0;

        for (int counter = 0; counter < numberOfExperiments; counter++) {
            Node start = new Node("Start", 0, 0);
            Node goal = new Node("Goal", 5, 5);

            Node s = new Node("s", 310, 80);
            Node a = new Node("a", 80, 280);
            Node d = new Node("d", 125, 520);
            Node f = new Node("f", 270, 750);
            Node b = new Node("b", 370, 340);
            Node h = new Node("h", 410, 560);
            Node g = new Node("g", 620, 710);
            Node c = new Node("c", 845, 95);
            Node i = new Node("i", 920, 500);
            Node l = new Node("l", 1075, 320);
            Node k = new Node("k", 1070, 630);
            Node j = new Node("j", 1180, 490);
            Node e = new Node("e", 972, 855);

            s.addEdge(a, 700);
            s.addEdge(b, 200);
            s.addEdge(c, 300);
            a.addEdge(s, 700);
            a.addEdge(b, 300);
            a.addEdge(d, 400);
            b.addEdge(s, 200);
            b.addEdge(a, 300);
            b.addEdge(d, 400);
            b.addEdge(h, 100);
            d.addEdge(a, 400);
            d.addEdge(b, 400);
            d.addEdge(f, 500);
            h.addEdge(b, 100);
            h.addEdge(f, 300);
            h.addEdge(g, 200);
            f.addEdge(d, 500);
            f.addEdge(h, 300);
            g.addEdge(h, 200);
            g.addEdge(e, 200);
            c.addEdge(s, 300);
            c.addEdge(l, 200);
            l.addEdge(c, 200);
            l.addEdge(i, 400);
            l.addEdge(j, 400);
            i.addEdge(l, 400);
            i.addEdge(j, 600);
            i.addEdge(k, 400);
            j.addEdge(l, 400);
            j.addEdge(i, 600);
            j.addEdge(k, 400);
            k.addEdge(i, 400);
            k.addEdge(j, 400);
            k.addEdge(e, 500);
            e.addEdge(g, 200);
            e.addEdge(k, 500);

            AStar aStar = new AStar();
            long startTime = System.currentTimeMillis();
            List<Node> bestPath = aStar.findPath(s, e);
            long endTime = System.currentTimeMillis();

            if (bestPath != null) {
                System.out.println("Percorso trovato: ");
                successfulExperiments++;
                for (Node n : bestPath) {
                    System.out.print(n.getLabel() + " ");
                }
                System.out.println();
            } else {
                System.out.println("Nessun percorso trovato");
                failedExperiments++;
            }

            System.out.println("Tempo totale di esecuzione: " + (endTime - startTime) + " ms");
        }
        System.out.println("Esperimenti che hanno avuto successo: " + successfulExperiments + ", " + (((successfulExperiments/numberOfExperiments) * 100)) + "%");
        System.out.println("Esperimenti che sono falliti: " + failedExperiments + ", " + (((failedExperiments/numberOfExperiments) * 100)) + "%");

    }
}
