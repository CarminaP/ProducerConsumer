
package producerconsumer;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Producer extends Thread {
    Buffer buffer;
    int id;
    long time;
    //Rango de numeros -> Obtenidos desde la GUI
    int min;
    int max;
    //Operaciones a usar -> Aritmeticas
    String [] op = {"+", "-", "*", "/"};
    //Para guardar 10 numeros random dentro del rango
    String[] nums = new String[10];
    //Para acceder a la barra de progreso en la GUI
    private JProgressBar progress = ProducerConsumer.frame.getProgress();
    
    Producer(Buffer buffer, int id, int min, int max, long time) {
        this.buffer = buffer;
        this.id = id;
        this.min = min;
        this.max = max;
        this.time = time;
    }
    
    //Para obtener un valor de una posicion random de un arreglo
    static String getRandom(String[] arr) {
        Random gen = new Random();
        int rand = gen.nextInt(arr.length);  
        return arr[rand];  
    }
    
    //Para actualizar el valor de la barra de progreso de la capacidad del buffer
    public synchronized void progressSetValue(int value){
        progress.setValue(value);
    }
    
    @Override
    public void run() {
        System.out.println("Running Producer...");
        
        String product;
        int i;
        
        //Crear numeros aleatorios dentro del rango y guardarlos en u array
        Random rand = new Random();
        for (int j = 0; j < 10; j++) {
          nums[j] = Integer.toString(rand.nextInt((max - min) + 1) + min);
        }
        
        //Cada productor produce lo suficiente como para llenar el buffer 1 vez
        for(i=0 ; i < this.buffer.buffersize ; i++) {
            //obtener una operacion aleatoria
            String operator = getRandom(op);
            //obtener un primer numero aleatorio
            String num1 = getRandom(nums);
            //obtener un segundo numero aleatorio
            String num2 = getRandom(nums);
            //Concaternar las partes para el producto
            product = "("+operator+" "+num1+" "+num2+")";
            //agregar el producto al buffer
            this.buffer.produce(product);
            
            Buffer.print("Producer"+id+" produced: " + product);
            
            //agregar la nueva fila a la tabla de tareas por hacer
            String[] row = {""+id, ""+product};
            ProducerConsumer.frame.addToDoRow(row);
            
            //actualizar la barra de progreso de la capacidad del buffer
            progressSetValue(buffer.getCurrBuffSize());
            
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Si se ha terminado de producir
        //agregar +1 a la variable en buffer que cuenta el numero de productores que han terminado de producir
        if(i == this.buffer.buffersize){
            this.buffer.pFinished();
        }
    }
    
}
