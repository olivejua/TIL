package me.olivejua.learningjava.gc;

public class G1GCDemo {

    public static void main(String[] args) {
        // 객체 생성 - 대부분 Eden 리전에 할당
        for (int i = 0; i < 1000; i++) {
            createObjects();
        }

        // 일부 객체가 Old Generation으로 이동될 수 있도록 GC 유도
        System.gc();
    }

    private static void createObjects() {
        Node root = new Node("Root");
        for (int i = 0; i < 100; i++) {
            Node child = new Node("Child " + i);
            root.addChild(child);
        }
    }

    static class Node {
        String name;
        Node[] children = new Node[10];

        public Node(String name) {
            this.name = name;
        }

        void addChild(Node child) {
            for (int i = 0; i < children.length; i++) {
                if (children[i] == null) {
                    children[i] = child;
                    break;
                }
            }
        }
    }
}
