import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author spantea
 * @date 
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {
  
  private Node first = null;
  private Node last = null;
  private int N = 0; 
  
  private class Node {
    private Item item;
    private Node next;
    private Node prev;
  }
  
  private class DequeIterator implements Iterator<Item> {
    private Node current = first;
    
    @Override
    public boolean hasNext() {
      return current != null;
    }
    
    @Override
    public Item next() {
      if (!hasNext())
        throw new NoSuchElementException();
      
      Item item = current.item;
      current = current.next;
      return item;
    }
    
    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  /**
   * construct an empty deque.
   */
  public Deque() {

  }

  /**
   * @return is the deque empty?
   */
  public boolean isEmpty() {
    return first == null;
  }

  /**
   * @return the number of items on the deque
   */
  public int size() {
    return N;
  }

  /**
   * add the item to the front.
   * @param item
   */
  public void addFirst(Item item) {
    if (item == null)
      throw new NullPointerException();
    
    if (isEmpty()) {
      first = new Node();
      first.item = item;
      last = first;
    } else {
      Node oldFirst = first;
      first = new Node();
      first.item = item;
      first.next = oldFirst;
      oldFirst.prev = first;
    }    
    N++;
  }

  /**
   * add the item to the end.
   * @param item
   */
  public void addLast(Item item) {
    if (item == null)
      throw new NullPointerException();
    
    if (isEmpty()) {
      last = new Node();
      last.item = item;
      first = last;
    } else {
      Node oldLast = last;
      last = new Node();
      last.item = item;
      last.prev = oldLast;
      oldLast.next = last;
    }
    N++;
  }

  /**
   * @return remove and return the item from the front
   */
  public Item removeFirst() {
    if (isEmpty())
      throw new NoSuchElementException();
    
    Item item = first.item;
    if (size() == 1) {
      last = null;
      first = null;	
    } else {
      first = first.next;
      first.prev = null;
    }
    
    N--;
    return item;
  }

  /**
   * @return remove and return the item from the end
   */
  public Item removeLast() {
    if (isEmpty())
      throw new NoSuchElementException();
    
    Item item = last.item;
    if (size() == 1) {
      last = null;
      first = null;
    } else {
      last = last.prev;
      last.next = null;
    }
  
    N--;
    return item;
  }

  /**
   * an iterator over items in order from front to end.
   * @return Iterator
   */
  @Override
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  /**
   * unit testing.
   * @param args
   */
  public static void main(String[] args) {
	  
  }
}
