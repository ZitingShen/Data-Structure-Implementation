import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Hashtable;

public class WordNet {
   private BinarySearchTreeWordNet synset;
   private Hashtable<Integer, String> synsetString;
   private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synsetString = new Hashtable<Integer, String>();
        synset = new BinarySearchTreeWordNet();
        In syn = new In(synsets);
        while (syn.hasNextLine()) {
            String[] pieces = syn.readLine().split(",");
            int wordID = Integer.parseInt(pieces[0]);
            synsetString.put(wordID, pieces[1]);
            for (String word: pieces[1].split(" ")) {
               synset.add(wordID, word);
            }
        }  

        Digraph G = new Digraph(synsetString.size());
        In hyper = new In(hypernyms);
        while (hyper.hasNextLine()) {
            String[] pieces = hyper.readLine().split(",");
            int current = Integer.parseInt(pieces[0]);
            for (int i = 1; i < pieces.length; i++) {
                G.addEdge(current, Integer.parseInt(pieces[i]));
            }
        }

        if (!testRooted(G)) 
            throw new IllegalArgumentException("The graph is not a rooted DAG!");

        sap = new SAP(G);
    }

    private boolean testRooted(Digraph G) {
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                if (ancestor == -1) ancestor = i;
                else {
                    System.out.println(G.V() + " " + ancestor);
                    return false;
                }
            }
        }
        if (ancestor == -1) return false;
        return true;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synset.inOrderTraverse();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (synset.search(word) != null)
            return true;
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Iterable<Integer> aid, bid;
        aid = synset.search(nounA);
        bid = synset.search(nounB);
        if ((aid == null) || (bid == null)) 
            throw new IllegalArgumentException();
        return sap.length(aid, bid);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of 
    // nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Iterable<Integer> aid, bid;
        aid = synset.search(nounA);
        bid = synset.search(nounB);
        if ((aid == null) || (bid == null)) 
            throw new IllegalArgumentException();
        int result = sap.ancestor(aid, bid);
        return synsetString.get(result);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        //System.out.println("The number of nouns: " + wn.synset.size);
        //System.out.println();
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length = wn.distance(v, w);
            String ancestor = wn.sap(v, w);
            StdOut.printf("length = %d, ancestor = %s\n", length, ancestor);
        }
    }
}