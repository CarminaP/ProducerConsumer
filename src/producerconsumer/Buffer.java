
package producerconsumer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Buffer {
    
    private String buffer;
    
    Buffer() {
        this.buffer = null;
    }
    
    synchronized String consume() {
        String product = null;
        
        if(this.buffer == null) {
            try {
                wait(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        product = this.buffer;
        this.buffer = null;
        notify();
        
        return product;
    }
    
    synchronized void produce(String product) {
        if(this.buffer != null) {
            try {
                wait(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.buffer = product;
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}
