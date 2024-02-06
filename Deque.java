import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

class Node<Item> {
  Item item;
  Node<Item> next;
  Node<Item> previous;

  public Node(Item item, Node<Item> next, Node<Item> previous) {
    this.item = item;
    this.next = next;
    this.previous = previous;
  }

  public void setNext(Node<Item> node) {
    this.next = node;
  }

  public void setPrevious(Node<Item> node) {
    this.previous = node;
  }

  public Item getItem() {
    return this.item;
  }

  public Node<Item> getNext() {
    return this.next;
  }

  public Node<Item> getPrevious() {
    return this.previous;
  }
}

class DequeIterator<Item> implements Iterator<Item> {
  private Node<Item> current;

  public DequeIterator(Node<Item> first) {
    current = first;
  }

  public boolean hasNext() {
    return current != null;
  }

  public Item next() {
    if (current == null) {
      throw new java.util.NoSuchElementException("There is no next");
    }
    Item item = current.getItem();
    current = current.getNext();
    return item;
  }
}

public class Deque<Item> implements Iterable<Item> {
  private Node<Item> first, last;
  private int nodesCount;

  // construct an empty deque
  public Deque() {
    nodesCount = 0;
    first = null;
    last = null;
  }

  // is the deque empty?
  public boolean isEmpty() {
    return first == null;
  }

  // return the number of items on the deque
  public int size() {
    return nodesCount;
  }

  // add the item to the front
  public void addFirst(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("There is no value");
    }
    if (nodesCount == 0) {
      setOnlyNode(item);
    } else {
      Node<Item> oldFirst = first;
      first = new Node<>(item, oldFirst, null);
      oldFirst.setPrevious(first);
    }
    nodesCount++;
  }

  // add the item to the back
  public void addLast(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("There is no value");
    }
    if (nodesCount == 0) {
      setOnlyNode(item);
    } else {
      Node<Item> oldLast = last;
      last = new Node<>(item, null, oldLast);
      oldLast.setNext(last);
    }
    nodesCount++;
  }

  private void setOnlyNode(Item item) {
    first = new Node<>(item, null, null);
    last = first;
  }

  // remove and return the item from the front
  public Item removeFirst() {
    if (nodesCount == 0) {
      throw new java.util.NoSuchElementException("There are 0 nodes to remove");
    }
    Node<Item> oldFirst = first; 
    if (nodesCount == 1) {
      deleteOnlyNode();
    } else {
      first = oldFirst.getNext();
      first.setPrevious(null);
    }
    nodesCount--;
    return oldFirst.getItem();
  }

  // remove and return the item from the back
  public Item removeLast() {
    if (nodesCount == 0) {
      throw new java.util.NoSuchElementException("There are 0 nodes to remove");
    }
    Node<Item> oldLast = last;
    if (nodesCount == 1) {
      deleteOnlyNode();
    } else {
      last = oldLast.getPrevious();
      last.setNext(null);
    }
    nodesCount--;
    return oldLast.getItem();
  }

  private void deleteOnlyNode() {
    first = null;
    last = null;
  }

  // return an iterator over items in order from front to back
  public Iterator<Item> iterator() {
    return new DequeIterator<>(first);
  }

  // unit testing (required)
  public static void main(String[] args) {
    Deque<String> testingDeque = new Deque<>();
    testingDeque.addFirst("test 1");
    testingDeque.addFirst("test 2");
    Iterator<String> testingIterator = testingDeque.iterator();
    while (testingIterator.hasNext()) {
      StdOut.println("two add first: " + testingIterator.next());
    }
    testingDeque.addLast("test 3");
    testingIterator = testingDeque.iterator();
    while (testingIterator.hasNext()) {
      StdOut.println("add last: " + testingIterator.next());
    }
    StdOut.println("size: " + testingDeque.size());
    testingDeque.removeFirst();
    testingIterator = testingDeque.iterator();
    while (testingIterator.hasNext()) {
      StdOut.println("remove first: " + testingIterator.next());
    }
    testingDeque.removeLast();
    testingIterator = testingDeque.iterator();
    while (testingIterator.hasNext()) {
      StdOut.println("remove last: " + testingIterator.next());
    }
  }
}