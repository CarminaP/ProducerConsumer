
package producerconsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

public class Buffer {
    //Queue en donde se guardan los productos -> buffer
    private Queue<String> buffer = new LinkedList<String>();
    //tamaño del buffer declarado
    int buffersize;
    //tiempo de espera para consumidores
    long cTime;
    //tiempo de espera para productores
    long pTime;
    //barra de progreso de la capacidad del buffer
    public JProgressBar progress = ProducerConsumer.frame.getProgress();
    //numero de productores que terminaron de producir
    int pFinish;
    
    //Constructor que inicializa las variables
    Buffer(int buffersize, long pTime, long cTime) {
        this.buffersize = buffersize;
        this.pTime = pTime;
        this.cTime = cTime;
        this.pFinish = 0;
    }
    
    //metodo para consumir un producto del buffer
    synchronized String consume() {
        //producto
        String product = null;
        //Mientras el buffer este vacio, esperar el tiempo declarado
        while(BuffisEmpty()) {
            try {
                wait(cTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //obtener producto del buffer y avisar a threads dormidos o esperando
        product = this.buffer.remove();
        print("Buffer size after consume = "+buffer.size());
        notify();
        
        return product;
    }
    
    //metodo para producir un porducto y agregarlo al buffer
    synchronized void produce(String product) {
        //mientras no haya espacio en el buffer por su tamaño declarado, esperar el tiempo declarado
        while(getCurrBuffSize() >= buffersize) {
            try {
                wait(pTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //agregar el producto al buffer y avisar a threads dormidos o esperando
        this.buffer.add(product);
        print("Buffer size after produce = "+buffer.size());
        notify();
    }
    
    //Para mensajes en consola del buffer
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
    //obtener el numero actual de productos en el buffer
    public synchronized int getCurrBuffSize(){
        return buffer.size();
    }
    
    //obtener si el buffer esta vacio o no
    public synchronized boolean BuffisEmpty(){
        return buffer.isEmpty();
    }
    
    //agregar +1 a la variable que guarda el numero de productores que han terminado de producir
    public synchronized void pFinished(){
        this.pFinish++;
    }
    
    //obtener el numero de productores que han terminado de producir
    public synchronized int getFinishedP(){
        return this.pFinish;
    }
}
