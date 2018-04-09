
package producerconsumer;

public class ProducerConsumer {

    public static void main(String[] args) {
        int numProd = 1;
        int numCons = 3;
        
        Buffer buffer = new Buffer();
        
        for(int i = 0; i < numProd; i++){
            Producer p = new Producer(buffer,i);
            p.start();
        }
        
        for(int j = 0; j < numCons; j++){
            Consumer c = new Consumer(buffer,j);
            c.start();
        }
    }
    
}
