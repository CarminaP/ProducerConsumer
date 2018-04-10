
package producerconsumer;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Consumer extends Thread {
    Buffer buffer;
    int id;
    //Number of products to consume per consumer? 1 or another number?
    int productnum = 5;
    
    Consumer(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
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
        
        for(int i=0 ; i<productnum ; i++) {
            num1_s = "";
            num2_s = "";
            result_s = "";
            result = 0;
            result_d = 0.0;
            
            product = this.buffer.consume();
            
            //sometimes we still get null products??
            if(product == null){
                continue;
            }
            
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
            
            JTable doneTable = ProducerConsumer.frame.getDoneTable();
            String[] row = {""+id, ""+product, resultStr};
            DefaultTableModel currentModel = (DefaultTableModel)doneTable.getModel();
            currentModel.addRow(row);
            doneTable.setModel(currentModel);
            ProducerConsumer.frame.setDoneTable(doneTable);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
