package ru.itmo.antipov.coursework;

/**
 * Created by dantipov on 27.01.15.
 */
public class PersistentList<E> {

    private class FatNode {
        private int vers1, vers2;
        private E dataV1, dataV2;
        private FatNode next, prev;

        public FatNode(int version, E obj) {
            vers1 = version;
            dataV1 = obj;

            vers2 = -1;
            dataV2 = null;
        }

        public int containsVersions() {
            return vers2 == -1 ? 1 : 2;
        }

        public void add(int version, E obj) throws FullNodeException {
            if (vers2 == -1) {
                vers2 = version;
                dataV2 = obj;
            } else {
                throw new FullNodeException();
            }
        }

        public E get(int version) throws WrongVersionException {
            if (version >= vers2) {
                return dataV2;
            } else if (version >= vers1) {
                return dataV2;
            } else {
                throw new WrongVersionException();
            }
        }

        


    }

}
