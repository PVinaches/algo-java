import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
  public static void main(String[] args) {
    int maxPrint = Integer.parseInt(args[0]);
    RandomizedQueue<String> randQueue = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      randQueue.enqueue(StdIn.readString());
    }
    Iterator<String> testIterator = randQueue.iterator();
    int pos = 0;
    while (testIterator.hasNext() && pos < maxPrint) {
      StdOut.println(testIterator.next());
      pos++;
    }
  }
}