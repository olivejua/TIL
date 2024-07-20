package me.olivejua.learningjava.gc;

public class GCDemo {

    public static void main(String[] args) {
        GCDemo demo = new GCDemo();
        demo.createObjectGraph();
    }

    private void createObjectGraph() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");

        a.next = b;
        b.next = c;
        c.next = d;
    }

    class Node {
        String name;
        Node next;

        public Node(String name) {
            this.name = name;
        }
    }
}
