
package producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

public class Buffer {
    
    //private String buffer;
    private Queue<String> buffer = new LinkedList<String>();;
    int buffersize;
    long cTime;
    long pTime;
    public JProgressBar progress = ProducerConsumer.frame.getProgress();
    
    Buffer(int buffersize, long pTime, long cTime) {
        this.buffersize = buffersize;
        this.pTime = pTime;
        this.cTime = cTime;
    }
    
    synchronized String consume() {
        String product = null;
        if(this.buffer.isEmpty()) {
            try {
                wait(cTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //take from queue
        product = this.buffer.remove();
        print("Buffer size after consume = "+buffer.size());
        notify();
        }
        
        return product;
    }
    
    synchronized void produce(String product) {
        if(buffer.size() >= buffersize) {
            try {
                wait(pTime);
                //add to queue
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //add to queue
            this.buffer.add(product);
            print("Buffer size after produce = "+buffer.size());
            notify();
        }
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
    public synchronized int getCurrBuffSize(){
        return buffer.size();
    }
    
    public synchronized boolean BuffisEmpty(){
        return buffer.isEmpty();
    }
}
