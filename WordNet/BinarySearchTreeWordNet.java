import java.util.ArrayList;

class BinarySearchTreeWordNet {
    private Node root;
    private int size;
    private ArrayList<String> nouns;

    private class Node {
        private ArrayList<Integer> wordId;
        private String word;
        private Node left, right;

        public Node(int id, String w) {
            wordId = new ArrayList<Integer>();
            wordId.add(id);
            word = w;
        }
    }

    public BinarySearchTreeWordNet() {
        size = 0;
    }

    public void add(int id, String word) {
        root = add(id, word, root);
    }

    public Node add(int id, String word, Node n) {
        if (n == null) {
            size++;
            return new Node(id, word);
        }
        if (word.compareTo(n.word) < 0) {
            n.left = add(id, word, n.left);
        } else if (word.compareTo(n.word) > 0) {
            n.right = add(id, word, n.right);
        } else {
            n.wordId.add(id);
        }
        return n;
    }

    public Iterable<Integer> search(String word) {
        if (word == null) throw new NullPointerException("The word is null!");
        return search(word, root);
    }

    public Iterable<Integer> search(String word, Node n) {
        if (n == null)
            return null;
        int result = word.compareTo(n.word);
        if (result == 0) {
            return n.wordId;
        }
        if (result < 0) {
            return search(word, n.left);
        }
        return search(word, n.right);
    }

    public ArrayList<String> inOrderTraverse() {
        nouns = new ArrayList<String>();
        inOrderTraverse(root);
        return nouns;
    }

    private void inOrderTraverse(Node n) {
        if (n == null) {
            return;
        }
        inOrderTraverse(n.left);
        nouns.add(n.word);
        inOrderTraverse(n.right);
    }

    public int size() {
        return size;
    }

    // optional tester
    public static void main(String[] args) {
        BinarySearchTreeWordNet b = new BinarySearchTreeWordNet();
        b.add(0, "a");
        b.add(0, "one");
        b.add(1, "b");
        b.add(1, "two");
        b.add(2, "c");
        b.add(2, "three");
        //System.out.println(b.search("orange"));
        System.out.println(b.inOrderTraverse());
        System.out.println(b.size);
    }
}