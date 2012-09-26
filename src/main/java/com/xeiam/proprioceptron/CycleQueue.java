package com.xeiam.proprioceptron;


public class CycleQueue<T> {// simple implementation of an instruction loop.

  private Node<T> current;

  public CycleQueue() {

  }

  public void add(T data){

    if (current == null) {
      current = new Node<T>(data);
      current.setNext(current);
      return;
    }
    Node<T> temp = current.getNext();
    current.setNext(new Node<T>(data));
    current.getNext().setNext(temp);
  }

  public T next() {

    current = current.getNext();
    return current.getData();
  }

  public T remove() {

    current.setNext(current.getNext().getNext());
    return current.getNext().getData();
  }

  private class Node<T> {

    Node<T> next;
    T data;

    public Node(T data) {

      this.data = data;
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
