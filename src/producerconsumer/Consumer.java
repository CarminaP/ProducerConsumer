
package producerconsumer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jdk.nashorn.internal.ir.Symbol;

public class Consumer extends Thread {
    Buffer buffer;
    int id;
    long time;
    private JSpinner numConsumed = ProducerConsumer.frame.getConsumedSpinner();
    private JProgressBar progress = ProducerConsumer.frame.getProgress();
    
    Consumer(Buffer buffer, int id, long time) {
        this.buffer = buffer;
        this.id = id;
        this.time = time;
    }
    
    //divisions on scheme with integers give back a simplified division rather than the result
    //these two functions gives us that simplified version if there is
    public static long gcm(int a, int b) {
        return b == 0 ? a : gcm(b, a % b);
    }
    
    public static String simplify(int a, int b) {
        long gcm = gcm(a, b);
        return (a / gcm) + "/" + (b / gcm);
    }
    
    public synchronized void progressSetValue(int value){
        progress.setValue(value);
    }
    
    public synchronized void consumedSetValue(int value){
        numConsumed.setValue(value);
    }
    
    @Override
    public void run() {
        System.out.println("Running Consumer...");
        String product = null;
        String num1_s, num2_s, result_s;
        int num1, num2, result;
        double result_d;
        char op;
        //divisions on scheme with integers give back a simplified division rather than the result
        boolean divNotInt = false;
        
        while(this.buffer.getFinishedP() < ProducerConsumer.frame.getProdNum() || !buffer.BuffisEmpty()){
            num1_s = "";
            num2_s = "";
            result_s = "";
            result = 0;
            result_d = 0.0;
            
            product = this.buffer.consume();
            
            char[] array = product.toCharArray();
            op = array[1];
            int j = 3;

            while(array[j]!= ' '){
                num1_s = num1_s+array[j];
                j++;
            }

            j++;

            while(array[j]!= ')'){
                num2_s = num2_s+array[j];
                j++;
            }

            num1 = Integer.parseInt(num1_s);
            num2 = Integer.parseInt(num2_s);

            //{"+", "-", "*", "/"};
            switch (op) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    if(num1%num2 == 0){
                        result = num1 / num2;
                    } else {
                        result_s = simplify(num1,num2);
                        result_d = (double)num1 /(double)num2;
                        divNotInt = true;
                    }                 
                    break;
                default:
                    break;
            }
            
            String resultStr;
            if(divNotInt){
                Buffer.print("Consumer"+id+" consumed: " + product + " = "+result_s+" = "+result_d);
                divNotInt = false;
                resultStr = " = "+result_s+" = "+result_d;
            } else {
                Buffer.print("Consumer"+id+" consumed: " + product + " = "+result);
                resultStr = " = "+result;
            }
            
            ProducerConsumer.frame.addConsumedNum();
            int consumed = ProducerConsumer.frame.getConsumedNum();
            
            String[] row = {""+consumed, ""+id, ""+product, resultStr};
            ProducerConsumer.frame.addDoneRow(row);
            
            ProducerConsumer.frame.removeToDo();
            
            progressSetValue(buffer.getCurrBuffSize());
            consumedSetValue(consumed);
            
            
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
