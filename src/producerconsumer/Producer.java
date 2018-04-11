
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
    //Range of numbers -> get from GUI
    int min;
    int max;
    //Number of products to produce per producer? 1 or another number?
    int productnum = 5;
    //Operations to use ->arithmetic
    String [] op = {"+", "-", "*", "/"};
    //To store 10 random numbers
    String[] nums = new String[10];
    
    Producer(Buffer buffer, int id, int min, int max, long time) {
        this.buffer = buffer;
        this.id = id;
        this.min = min;
        this.max = max;
        this.time = time;
    }
    
    //To get random value in an array
    static String getRandom(String[] arr) {
        Random gen = new Random();
        int rand = gen.nextInt(arr.length);  
        return arr[rand];  
    }
    
    @Override
    public void run() {
        System.out.println("Running Producer...");
        
        String product;
        
        //create new random numbers and add them to the array
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
          nums[i] = Integer.toString(rand.nextInt((max - min) + 1) + min);
        }
        
        for(int i=0 ; i < this.buffer.buffersize ; i++) {
            String operator = getRandom(op);
            String num1 = getRandom(nums);
            String num2 = getRandom(nums);
            product = "("+operator+" "+num1+" "+num2+")";
            this.buffer.produce(product);
            Buffer.print("Producer"+id+" produced: " + product);
            
            JTable toDoTable = ProducerConsumer.frame.getToDoTable();
            String[] row = {""+id, ""+product};
            DefaultTableModel currentModel = (DefaultTableModel)toDoTable.getModel();
            currentModel.addRow(row);
            toDoTable.setModel(currentModel);
            ProducerConsumer.frame.setToDoTable(toDoTable);
            JProgressBar progress = ProducerConsumer.frame.getProgress();
            progress.setValue(buffer.getCurrBuffSize());
            
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
