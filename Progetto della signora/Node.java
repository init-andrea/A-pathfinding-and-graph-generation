import java.util.Objects;

public class Node implements Comparable<Node> {
    public int x, y;
    public double g, h;
    public Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.g = 0;
        this.h = 0;
        this.parent = null;
    }

    public double getF() {
        return this.g + this.h;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.getF(), other.getF());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}