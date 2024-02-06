import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

class RandomIterator<Item> implements Iterator<Item> {
  private Item[] origQueue;
  private int[] shuffledPos;
  private int posNext;

  public RandomIterator(Item[] queue, int numElem) {
    origQueue = queue;
    shuffledPos = new int[numElem];
    for (int i = 0; i < numElem; i++) {
      shuffledPos[i] = i;
    }
    StdRandom.shuffle(shuffledPos);
    posNext = 0;
  }

  public boolean hasNext() {
    return posNext < shuffledPos.length;
  }

  public Item next() {
    if (posNext == shuffledPos.length) {
      throw new java.util.NoSuchElementException("There is no next");
    }
    Item returnable = origQueue[shuffledPos[posNext]];
    posNext++;
    return returnable;
  }
}

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] queue;
  private int pos;

  // construct an empty randomized queue
  public RandomizedQueue() {
    pos = 0;
    queue = (Item[]) new Object[1];
  }

  // is the randomized queue empty?
  public boolean isEmpty() {
    return pos == 0;
  }

  // return the number of items on the randomized queue
  public int size() {
    return pos;
  }

  // add the item
  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("There is no value");
    }
    if (pos < queue.length) {
      queue[pos] = item;
    } else {
      Item[] oldQueue = queue;
      queue = (Item[]) new Object[queue.length * 2];
      for (int i = 0; i < oldQueue.length; i++) {
        pos = i;
        queue[pos] = oldQueue[pos];
      }
      pos++;
      queue[pos] = item;
    }
    pos++;
  }

  private int getRandomPosition(int length) {
    return StdRandom.uniformInt(length);
  }

  // remove and return a random item
  public Item dequeue() {
    if (pos == 0) {
      throw new java.util.NoSuchElementException("There are 0 elements to remove");
    }
    int posDelete = getRandomPosition(pos);
    Item returnable = queue[posDelete];
    queue[posDelete] = queue[pos - 1];
    pos--;
    return returnable;
  }

  // return a random item (but do not remove it)
  public Item sample() {
    if (queue.length == 0) {
      throw new java.util.NoSuchElementException("There are 0 elements to sample");
    }
    return queue[getRandomPosition(pos)];
  }

  // return an independent iterator over items in random order
  public Iterator<Item> iterator() {
    return new RandomIterator<>(queue, pos);
  }

  // unit testing (required)
  public static void main(String[] args) {
    RandomizedQueue<String> testRandQueue = new RandomizedQueue<>();
    testRandQueue.enqueue("test1");
    testRandQueue.enqueue("test2");
    testRandQueue.enqueue("test3");
    testRandQueue.enqueue("test4");
    Iterator<String> testIterator = testRandQueue.iterator();
    while (testIterator.hasNext()) {
      StdOut.println("Test add: " + testIterator.next());
    }
    StdOut.println("Test sample(): " + testRandQueue.sample());
    StdOut.println("Test dequeue(): " + testRandQueue.dequeue());
  }
}
