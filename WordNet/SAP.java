import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class SAP {
   private Digraph digraph;
   private LinkedList<Vertex> queue;
   private Vertex[] visited;

   private class Vertex {
      private int id;
      private char vorw;
      private int d;
      Vertex(int id, char vorw, int d) {
         this.id = id;
         this.vorw = vorw;
         this.d = d;
      }
   }

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G) {
      this.digraph = new Digraph (G);
      queue = new LinkedList<Vertex>();
   }

   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w) {
      if (v == w) return 0;
      queue.clear();
      visited = new Vertex[digraph.V()];
      visited[v] = new Vertex(v, 'v', 0);
      visited[w] = new Vertex(w, 'w', 0);
      queue.add(new Vertex(v, 'v', 0));
      queue.add(new Vertex(w, 'w', 0));
      int l = digraph.E() + 1;

      while (!queue.isEmpty()) {
         Vertex i = queue.poll();
         if (i.d >= l) break;
         for (int j: digraph.adj(i.id)) {
            if (visited[j] != null) {
               if (visited[j].vorw != i.vorw) {
                 l = Math.min(l, visited[j].d + i.d + 1);
               }
            } else {
               visited[j] = new Vertex(j, i.vorw, i.d + 1);
            }
            queue.add(new Vertex(j, i.vorw, i.d + 1));
         }
      }

      if (l <= digraph.E()) return l;
      return -1;
   }

   // a common ancestor of v and w that participates in a shortest ancestral
   // path; -1 if no such path
   public int ancestor(int v, int w) {
      if (v == w) return v;
      queue.clear();
      visited = new Vertex[digraph.V()];
      visited[v] = new Vertex(v, 'v', 0);
      visited[w] = new Vertex(w, 'w', 0);
      queue.add(new Vertex(v, 'v', 0));
      queue.add(new Vertex(w, 'w', 0));
      int l = digraph.E() + 1;
      int result = -1;

      while (!queue.isEmpty()) {
         Vertex i = queue.poll();
         if (i.d >= l) break;
         for (int j: digraph.adj(i.id)) {
            if (visited[j] != null) {
               if (visited[j].vorw != i.vorw) {
                  if (visited[j].d + i.d + 1 < l) {
                     l = visited[j].d + i.d + 1;
                     result = j;
                  }
               }
            } else {
               visited[j] = new Vertex(j, i.vorw, i.d + 1);
            }
            queue.add(new Vertex(j, i.vorw, i.d + 1));
         }
      }
      return result;
   }

   // length of shortest ancestral path between any vertex in v and any 
   // vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w) {
      visited = new Vertex[digraph.V()];
      queue.clear();
      for (int i: v) {
         visited[i] = new Vertex(i, 'v', 0);
         queue.add(new Vertex(i, 'v', 0));
      }
      for (int i: w) {
         if (visited[i] == null) {
            visited[i] = new Vertex(i, 'w', 0);
         } else {
            return 0;
         }
         queue.add(new Vertex(i, 'w', 0));
      }
      int l = digraph.E() + 1;

      while (!queue.isEmpty()) {
         Vertex i = queue.poll();
         if (i.d >= l) break;
         for (int j: digraph.adj(i.id)) {
            if (visited[j] != null) {
               if (visited[j].vorw != i.vorw) {
                  l = Math.min(l, visited[j].d + i.d + 1);
               }
            } else {
               visited[j] = new Vertex(j, i.vorw, i.d + 1);
            }
            queue.add(new Vertex(j, i.vorw, i.d + 1));
         }
      }
      if (l <= digraph.E()) return l;
      return -1;
   }

   // a common ancestor that participates in shortest ancestral path; 
   // -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
      visited = new Vertex[digraph.V()];
      queue.clear();
      for (int i: v) {
         visited[i] = new Vertex(i, 'v', 0);
         queue.add(new Vertex(i, 'v', 0));
      }
      for (int i: w) {
         if (visited[i] == null) {
           visited[i] = new Vertex(i, 'w', 0);
         } else {
            return i;
         }
         queue.add(new Vertex(i, 'w', 0));
      }
      int l = digraph.E() + 1;
      int result = -1;

      while (!queue.isEmpty()) {
         Vertex i = queue.poll();
         if (i.d >= l) break;
         for (int j: digraph.adj(i.id)) {
            if (visited[j] != null) {
               if (visited[j].vorw != i.vorw) {
                  if (visited[j].d + i.d + 1 < l) {
                     l = visited[j].d + i.d + 1;
                     result = j;
                  }
               }
            } else {
               visited[j] = new Vertex(j, i.vorw, i.d + 1);
            }
            queue.add(new Vertex(j, i.vorw, i.d + 1));
         }
      }
      return result;
   }

   // do unit testing of this class
   public static void main(String[] args) {
      In in = new In(args[0]);
      Digraph G = new Digraph(in);
      SAP sap = new SAP(G);
      while (!StdIn.isEmpty()) {
         int v = StdIn.readInt();
         //Integer[] varray = {3};
         //Iterable<Integer> v = Arrays.asList(varray);
         int w = StdIn.readInt();
         //Integer[] warray = {11};
         //Iterable<Integer> w = Arrays.asList(warray);
         int length   = sap.length(v, w);
         int ancestor = sap.ancestor(v, w);
         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
      }
   }
}