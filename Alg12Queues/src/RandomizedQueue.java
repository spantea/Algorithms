import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private int N;
  private Item[] a = (Item[]) new Object[1];
  
  private class RandomizedQueueIterator implements Iterator<Item> {
    private int i;
	private Item[] shuffA;
	  
    public RandomizedQueueIterator() {
      shuffA = (Item[])new Object[N];
      for (int i=0; i<N; i++) {
        shuffA[i] = a[i];
      }
      StdRandom.shuffle(shuffA);
    }
  
    @Override
    public boolean hasNext() {
      return i < N;
    }
  
    @Override
    public Item next() {
      if(!hasNext()) {
        throw new NoSuchElementException(); 
      }
    
	  return shuffA[i++];   
    }
  
	@Override
	public void remove() {
		throw new UnsupportedOperationException();  
	}
  }
  
  public RandomizedQueue() {
    
  }
  
  public boolean isEmpty() {
    return N == 0;
  }
  
  public int size() {
    return N;
  }
  
  public void enqueue(Item item) {
    if(item == null)
      throw new NullPointerException();
    
    if(N == a.length) resize(2*a.length);
    a[N++] = item;
  }
  
  /**
   * Remove and return a random item.
   * @return random item
   */
  public Item dequeue() {
    if(isEmpty()) {
      throw new NoSuchElementException();  
    }
    int i = StdRandom.uniform(N);
    Item item = a[i];
    a[i] = a[N-1];
    a[N-1] = null;
    N--;
    if (N > 0 && N == a.length/4) resize(a.length/2);
    return item;
  }
  
  /**
   * Return (but do not remove) a random item.
   * @return random item
   */
  public Item sample() {
    if(isEmpty()) {
      throw new NoSuchElementException();  
    }
    int i = StdRandom.uniform(N);
    return a[i];
  }
  
  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }
  
  private void resize(int capacity) {
    assert capacity >= N;
    Item[] temp = (Item[]) new Object[capacity];
    for(int i = 0; i < N; i++) {
      temp[i] = a[i];
    }
    a = temp;
  }
  
  public static void main(String[] args) {
	  RandomizedQueue<Integer> myq = new RandomizedQueue<Integer>();
	  myq.enqueue(1);
	  myq.enqueue(2);
	  myq.enqueue(3);
	  
	  while(!myq.isEmpty()){
		  System.out.println(myq.dequeue());
	  }
  }
}
