package ru.itmo.antipov.coursework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dantipov on 27.01.15.
 */
public class PersistentList<E> {
    private List<FatNode> first = new ArrayList<FatNode>();
    private List<FatNode> last = new ArrayList<FatNode>();
    private int curVersion = 0;

    //fat node that contains one or two versions of node
    private class FatNode {

        //node that contains data and links
        private class Node {
            private E data;
            private FatNode next, prev;

            public Node (E data) {
                this.data = data;
                this.next = null;
                this.prev = null;
            }

            public Node(E data, FatNode prev, FatNode next) {
                this.data = data;
                this.prev = prev;
                this.next = next;
            }

            public Node(Node node) {
                data = node.data;
                next = node.next;
                prev = node.prev;
            }

            public void setData(E data) {
                this.data = data;
            }

            public void setNext(FatNode next) {
                this.next = next;
            }

            public void setPrev(FatNode prev) {
                this.prev = prev;
            }

            public E getData() {
                return data;
            }

            public FatNode getNext() {
                return next;
            }

            public FatNode getPrev() {
                return prev;
            }
        }

        private int vers1, vers2;
        private Node node1, node2;

        public FatNode(int version, E data) {
            vers1 = version;
            vers2 = -1;

            node1 = new Node(data);
            node2 = null;
        }

        //set pointer on the next fat node in the node with max version
        public void setNext(FatNode next) {
            if (node2 != null) {
                node2.setNext(next);
            } else {
                node1.setNext(next);
            }
        }

        //set pointer on the previous fat node in the node with max version
        public void setPrev(FatNode prev) {
            if (node2 != null) {
                node2.setPrev(prev);
            } else {
                node1.setPrev(prev);
            }
        }

        //get the next fat node in the last version of list
        public FatNode getNext() {
            return node2 == null ? node1.getNext() : node2.getNext();
        }

        //get the previous fat node in the last version of list
        public FatNode getPrev() {
            return node2 == null ? node1.getPrev() : node2.getPrev();
        }

        //create new version of node with link to the next fat node and return it
        public FatNode setPersistentNext(FatNode next) {
            if (vers2 == -1) {
                node2 = new Node(node1);
                vers2 = curVersion;
                node2.setNext(next);
                return this;
            } else if (node2 != null) {
                FatNode newFatNode = new FatNode(curVersion, node2.getData());
                newFatNode.setNext(next);
                if (node2.getPrev() != null) {
                    newFatNode.setPrev(node2.getPrev().setPersistentNext(newFatNode));
                    return newFatNode;
                }
                first.add(newFatNode);
                return newFatNode;
            }
            return null;
        }

        //create new version of node with link to the previous fat node and return it
        public FatNode setPersistentPrev(FatNode prev) {
            if (vers2 == -1) {
                node2 = new Node(node1);
                vers2 = curVersion;
                node2.setPrev(prev);
                return this;
            } else if (node2 != null) {
                FatNode newFatNode = new FatNode(curVersion, node2.getData());
                newFatNode.setPrev(prev);
                if (node2.getNext() != null) {
                    newFatNode.setNext(node2.getNext().setPersistentPrev(newFatNode));
                    return newFatNode;
                }
                last.add(newFatNode);
                return newFatNode;
            }
            return null;
        }

        public int getVers1() {
            return vers1;
        }

        public int getVers2() {
            return vers2;
        }

        //get next fat node for the specified version
        public FatNode getNext(int version) {
            if (vers2 == -1 || vers2 > version) {
                return node1.getNext();
            }
            return node2.getNext();
        }

        //get data for the specified version
        public E getData(int version) {
            if (vers2 == -1 || vers2 > version) {
                return node1.getData();
            }
            return node2.getData();
        }
    }

    // add element to the end of the list
    public void add(E data) {
        if (first.size() == 0 || first.get(first.size() - 1) == null) {
            FatNode firstNode = new FatNode(curVersion, data);
            first.add(firstNode);
            last.add(firstNode);
        } else {
            FatNode newNode = new FatNode(curVersion, data);
            FatNode oldLast = last.get(last.size() - 1);
            newNode.setPrev(oldLast.setPersistentNext(newNode));
            last.add(newNode);
        }
        curVersion++;
    }

    // add element to the specified position
    public void add(E data, int index) {
        if (first.size() == 0 || first.get(first.size() - 1) == null) {
            if (index == 0) {
                FatNode node = new FatNode(curVersion, data);
                first.add(node);
                last.add(node);
                return;
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        FatNode nextNode = first.get(first.size() - 1);
        FatNode prevNode = null;

        for (int i = 1; i < index; i++) {
            if (nextNode == null) {
                throw new IndexOutOfBoundsException();
            }
            prevNode = nextNode;
            nextNode = nextNode.getNext();
        }

        FatNode newNode = new FatNode(curVersion, data);

        if (nextNode != null) {
            newNode.setNext(nextNode.setPersistentPrev(newNode));
        } else {
            last.add(newNode);
        }

        if (prevNode != null) {
            newNode.setPrev(prevNode.setPersistentNext(newNode));
        } else {
            first.add(newNode);
        }
        curVersion++;
    }

    //delete last element
    public void delete() {
        if (last.size() != 0 && last.get(last.size() - 1) != null) {
            FatNode oldLast = last.get(last.size() - 1);
            if (oldLast.getPrev() != null) {
                last.add(oldLast.getPrev().setPersistentNext(null));
            } else {
                last.add(null);
                first.add(null);
            }
            curVersion++;
        }
    }

    //delete element on the specified index
    public void delete(int index) {
        if (first.size() == 0 || first.get(first.size() - 1) == null) {
            throw new IndexOutOfBoundsException();
        }
        FatNode curNode = first.get(first.size() - 1);

        for (int i = 0; i < index; i++) {
            curNode = curNode.getNext();
            if (curNode == null) {
                throw new IndexOutOfBoundsException();
            }
        }

        FatNode prevNode = curNode.getPrev();
        FatNode nextNode = curNode.getNext();

        if (prevNode != null) {
            prevNode = prevNode.setPersistentNext(null);
        }
        if (nextNode != null) {
            nextNode = nextNode.setPersistentPrev(null);
        }

        if (prevNode != null) {
            prevNode.setNext(nextNode);
        } else {
            first.add(nextNode);
        }
        if (nextNode != null) {
            nextNode.setPrev(prevNode);
        } else {
            last.add(prevNode);
        }
        curVersion++;
    }

    //get i-th element of the list of specified version
    public E get(int index, int version) {
        if (first.size() == 0) {
            throw new IndexOutOfBoundsException();
        }

        //looking for the first node of the list of the required version
        FatNode curNode = null;
        for (FatNode fatNode : first) {
            //there cannot be two nulls in a row in the list of first nodes
            if (fatNode == null) {
                if (Math.max(curNode.vers1, curNode.vers2) == version - 1) {
                    throw  new IndexOutOfBoundsException();
                } else {
                    continue;
                }
            }
            if (fatNode.getVers1() > version) {
                break;
            }
            curNode = fatNode;
            if (fatNode.getVers2() > version) {
                break;
            }
        }

        //looking for the node with required index
        for (int i = 0; i < index; i++) {
            curNode = curNode.getNext(version);
            if (curNode == null) {
                throw new IndexOutOfBoundsException();
            }
        }
        return curNode.getData(version);
    }

}
