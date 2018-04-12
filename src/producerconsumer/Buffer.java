
package producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

public class Buffer {

    private Queue<String> buffer = new LinkedList<String>();;
    int buffersize;
    long cTime;
    long pTime;
    public JProgressBar progress = ProducerConsumer.frame.getProgress();
    int pFinish;
    
    Buffer(int buffersize, long pTime, long cTime) {
        this.buffersize = buffersize;
        this.pTime = pTime;
        this.cTime = cTime;
        this.pFinish = 0;
    }
    
    synchronized String consume() {
        String product = null;
        
        if(BuffisEmpty()) {
            try {
                wait(cTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //take from queue
        product = this.buffer.remove();
        print("Buffer size after consume = "+buffer.size());
        notify();
        return product;
    }
    
    synchronized void produce(String product) {
        while(getCurrBuffSize() >= buffersize) {
            try {
                wait(pTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //add to queue
        this.buffer.add(product);
        print("Buffer size after produce = "+buffer.size());
        notify();
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
    
    public synchronized void pFinished(){
        this.pFinish++;
    }
    
    public synchronized int getFinishedP(){
        return this.pFinish;
    }
}
