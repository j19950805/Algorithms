import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;
    private int next;

    public RandomizedQueue() {
        size = 0;
        items = (Item[]) new Object[8];
        next = 0;
    }

    private RandomizedQueue(RandomizedQueue<Item> other) {
        size = other.size;
        items = (Item[]) new Object[other.items.length];
        System.arraycopy(other.items, 0, items, 0, items.length);
        next = other.next;
    }

    private void resize(int capacity) {
        Item[] resized = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, resized, 0, size);
        items = resized;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null can't be added to deque");
        }
        size += 1;
        items[next] = item;
        next += 1;
        if (next >= items.length) {
            resize(items.length * 2);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int randomIndex = StdRandom.uniform(size);
        Item item = items[randomIndex];
        next -= 1;
        items[randomIndex] = items[next];
        items[next] = null;
        size -= 1;

        if (((double) size / items.length) < 0.25 && items.length >= 16) {
            resize(items.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int randomIndex = StdRandom.uniform(size);
        return items[randomIndex];
    }


    @Override
    public Iterator<Item> iterator() {
        return new RQIterator(this);
    }

    private class RQIterator implements Iterator<Item> {
        private RandomizedQueue<Item> rqCopy;

        RQIterator(RandomizedQueue<Item> rq) {
            rqCopy = new RandomizedQueue<>(rq);
        }

        @Override
        public boolean hasNext() {
            return rqCopy.size > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no next items.");
            }
            return rqCopy.dequeue();
        }

        @Override
        public void remove() {
            throw new  UnsupportedOperationException(
                    "Can't call remove() on DequeIterator");
        }
    }

    private void printDeque() {
        int p = 0;
        while (p < size) {
            System.out.printf("%s ", items[p]);
            p += 1;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }
        rq.printDeque();
        System.out.printf("size: %d\n", rq.size());
        for (int i = 0; i < 5; i++) {
            System.out.printf("sample %d: %d\n", i, rq.sample());
        }
        Iterator<Integer> rqIterator = rq.iterator();
        Iterator<Integer> rqIterator2 = rq.iterator();
        for (int i = 0; i < 10; i++) {
            System.out.printf("item %d: %d\n", i, rqIterator.next());
            System.out.printf("item2 %d: %d\n", i, rqIterator2.next());
        }
        for (int i = 0; i < 10; i++) {
            System.out.printf("removed item: %d\n", rq.dequeue());
            rq.printDeque();
        }
    }
}
