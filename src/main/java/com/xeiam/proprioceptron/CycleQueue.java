package com.xeiam.proprioceptron;


public class CycleQueue<T> {// simple implementation of an instruction loop.

  private Node<T> current;
  public CycleQueue() {

  }

  public void add(T data, boolean isstable) {

    if (current == null) {
      current = new Node<T>(data, isstable);
      current.setNext(current);
      return;
    }
    Node<T> temp = current.getNext();
    current.setNext(new Node<T>(data, isstable));
    current.getNext().setNext(temp);
  }

  public T next() {

    current = current.getNext();
    return current.getData();
  }

  public boolean isStable() {

    return current.isStable();
  }

  public T remove() {

    current.setNext(current.getNext().getNext());
    return current.getNext().getData();
  }

  private class Node<T> {

    Node<T> next;
    T data;

    boolean isstable;

    public Node(T data, boolean isstable) {

      this.isstable = isstable;
      this.data = data;
    }

    /**
     * isstable a flag which may be set to determine when in the CycleQueue any actuation event may be added.
     */
    public boolean isStable() {

      return isstable;
    }
    public void setNext(Node<T> next) {

      this.next = next;
    }

    public Node<T> getNext() {

      return next;
    }

    public T getData() {

      return data;
    }


  }

}
