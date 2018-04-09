
package producerconsumer;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer extends Thread {
    Buffer buffer;
    int id;
    //Range of numbers -> get from GUI
    int min = 1;
    int max = 30;
    //Number of products to produce per producer? 1 or another number?
    int productnum = 5;
    //Operations to use ->arithmetic
    String [] op = {"+", "-", "*", "/"};
    //To store 10 random numbers
    String[] nums = new String[10];
    
    Producer(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
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
        
        for(int i=0 ; i < productnum ; i++) {
            String operator = getRandom(op);
            String num1 = getRandom(nums);
            String num2 = getRandom(nums);
            product = "("+operator+" "+num1+" "+num2+")";
            this.buffer.produce(product);
            Buffer.print("Producer"+id+" produced: " + product);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
