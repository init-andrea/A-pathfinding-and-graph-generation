import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.*;

public class GraphPanel extends JPanel {
    private Node[] nodes;
    private double scale = 1.0;
    private double translateX = 0;
    private double translateY = 0;
    private Point lastPoint;
    private boolean drawWeights;

    public GraphPanel(Node[] nodes, boolean drawWeights) {
        this.nodes = nodes;
        this.drawWeights = drawWeights;
        setPreferredSize(new Dimension(800, 600)); // Dimensioni del pannello

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastPoint = e.getPoint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                    double delta = 0.05f * e.getWheelRotation();
                    scale += delta;
                    scale = Math.max(0.1, Math.min(scale, 10.0));
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point newPoint = e.getPoint();
                if (lastPoint != null) {
                    translateX += newPoint.getX() - lastPoint.getX();
                    translateY += newPoint.getY() - lastPoint.getY();
                    lastPoint = newPoint;
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform transform = new AffineTransform();
        transform.translate(translateX, translateY);
        transform.scale(scale, scale);
        g2.setTransform(transform);

        // Disegna gli archi
        for (Node node : nodes) {
            for (Edge edge : node.getEdges()) {
                drawEdge(g2, edge);
            }
        }

        // Disegna i nodi
        for (Node node : nodes) {
            drawNode(g2, node);
        }
    }

    private void drawNode(Graphics2D g2, Node node) {
        int x = (int) node.getX();
        int y = (int) node.getY();
        int radius = 30; // Aumentato il raggio del nodo
        g2.setColor(Color.BLUE);
        g2.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        g2.setColor(Color.WHITE);
        g2.drawString(node.getLabel(), x - radius / 4, y + radius / 4);
    }

    private void drawEdge(Graphics2D g2, Edge edge) {
        int x1 = (int) edge.getSrc().getX();
        int y1 = (int) edge.getSrc().getY();
        int x2 = (int) edge.getDest().getX();
        int y2 = (int) edge.getDest().getY();
    
        g2.setStroke(new BasicStroke(2)); // Aumentato lo spessore della linea
        g2.setColor(Color.BLACK);
        g2.drawLine(x1, y1, x2, y2);
    
        // Disegna la freccia per l'arco direzionale
        int dx = x2 - x1;
        int dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - 10;
        double xn = xm;
        double ym = 10;
        double yn = -10;
        double x;
        double sin = dy / D;
        double cos = dx / D;
    
        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;
    
        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;
    
        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};
    
        g2.fillPolygon(xpoints, ypoints, 3);
    
        // Disegna il peso dell'arco se il flag drawWeights è true
        if (drawWeights) {
            int cost = (int) edge.getCost(); // Converti il costo a un intero
            String costString = String.valueOf(cost);
            Font originalFont = g2.getFont();
            //Font smallerFont = originalFont.deriveFont(originalFont.getSize() * 0.8F); // Font ridotto
            //g2.setFont(smallerFont);
            g2.setFont(originalFont);

            // Calcola l'angolo dell'arco
            double angle = Math.atan2(dy, dx);
            
            // Ruota il sistema di coordinate al punto medio dell'arco
            g2.rotate(angle, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            
            // Disegna il testo parallelo all'arco
            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2;
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.RED);
            g2.drawString(costString, midX, midY);
            
            // Ripristina il sistema di coordinate
            g2.rotate(-angle, (x1 + x2) / 2.0, (y1 + y2) / 2.0);
            g2.setFont(originalFont); // Ripristina il font originale
        }
    }
    

    public static void main(String[] args) {
        // Generazione del grafo utilizzando il metodo esterno
        //Node[] nodes = Utilities.generateErdosRenyiGraph(20, 0.1);
        Node[] nodes;
        try {
            nodes = GraphUtils.loadGraph("Nodes.txt","Edges.txt");
   
        } catch (IOException e) {
            return;
        }
        // Creazione del frame e aggiunta del pannello del grafo con pesi degli archi
        JFrame frameWithWeights = new JFrame("Graph Visualization With Weights");
        GraphPanel panelWithWeights = new GraphPanel(nodes, true);
        frameWithWeights.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWithWeights.add(panelWithWeights);
        frameWithWeights.pack();
        frameWithWeights.setLocation(100, 100);
        frameWithWeights.setVisible(true);

        // Creazione del frame e aggiunta del pannello del grafo senza pesi degli archi
        JFrame frameWithoutWeights = new JFrame("Graph Visualization Without Weights");
        GraphPanel panelWithoutWeights = new GraphPanel(nodes, false);
        frameWithoutWeights.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWithoutWeights.add(panelWithoutWeights);
        frameWithoutWeights.pack();
        frameWithoutWeights.setLocation(900, 100);
        frameWithoutWeights.setVisible(true);
    }
}