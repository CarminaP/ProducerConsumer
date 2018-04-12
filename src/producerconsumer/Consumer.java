
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
    //Spinner en GUI con el numero de tareas realizadas
    private JSpinner numConsumed = ProducerConsumer.frame.getConsumedSpinner();
    //barra de progreso que representa la capacidad del buffer
    private JProgressBar progress = ProducerConsumer.frame.getProgress();
    
    Consumer(Buffer buffer, int id, long time) {
        this.buffer = buffer;
        this.id = id;
        this.time = time;
    }
    
    //las divisiones en scheme con enteros regresan una version simplificada si el resultado no es un entero
    //estas funciones nos ayudan a simplificar las divisiones
    public static long gcm(int a, int b) {
        return b == 0 ? a : gcm(b, a % b);
    }
    
    public static String simplify(int a, int b) {
        long gcm = gcm(a, b);
        return (a / gcm) + "/" + (b / gcm);
    }
    
    //para actualizar el valor de la barra de progreso
    public synchronized void progressSetValue(int value){
        progress.setValue(value);
    }
    
    //para actualizar el valor del spinner con el numero de tareas realizadas
    public synchronized void consumedSetValue(int value){
        numConsumed.setValue(value);
    }
    
    @Override
    public void run() {
        System.out.println("Running Consumer...");
        String product = null;
        //Strings para separar el producto o guarar el resultado en forma de a/b (simplificacion)
        String num1_s, num2_s, result_s;
        //variables que guardan los numeros para las operaciones
        int num1, num2, result;
        //Para guardar el resultado de una division que no resulta en entero
        double result_d;
        //variable que define la operacion a realizar
        char op;
        //para saber si el resultado de una division seria un entero
        boolean divNotInt = false;
        
        //mientras haya productores aun produciendo o mientras el buffer no este vacio consumir
        while(this.buffer.getFinishedP() < ProducerConsumer.frame.getProdNum() || !buffer.BuffisEmpty()){
            num1_s = "";
            num2_s = "";
            result_s = "";
            result = 0;
            result_d = 0.0;
            
            //obtener el producto del buffer
            product = this.buffer.consume();
            
            //guardar el producto en un array de caracteres para revisarlo y dividirlo
            char[] array = product.toCharArray();
            
            //la operacion siempre estara en el indice 1 del arreglo
            op = array[1];
            //El primer numero empieza desde el indice 3
            int j = 3;
            //el primer numero llega hasta el siguiente espacio
            while(array[j]!= ' '){
                num1_s = num1_s+array[j];
                j++;
            }
            // el segundo numero empieza despues del espacio
            j++;
            //el segundo numero llega hasta el parentesis
            while(array[j]!= ')'){
                num2_s = num2_s+array[j];
                j++;
            }
            
            //guardar los numeros como enteros para realizar las operaciones
            num1 = Integer.parseInt(num1_s);
            num2 = Integer.parseInt(num2_s);

            //conjunto de operaciones {"+", "-", "*", "/"};
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
                        //si el resultado es entero, simplemente se dividen los numeros
                        result = num1 / num2;
                    } else {
                        //si el resultado no es entero, la division se simplifica y el resultado se guarda como double
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
                //si el resultado de la division no fue entero, agregrar la simplificacion y el resultado double
                Buffer.print("Consumer"+id+" consumed: " + product + " = "+result_s+" = "+result_d);
                //regresar a falso para el siguiente producto
                divNotInt = false;
                //para agregarlo a una fila de la tabla de tareas realizadas
                resultStr = " = "+result_s+" = "+result_d;
            } else {
                Buffer.print("Consumer"+id+" consumed: " + product + " = "+result);
                //para agregarlo a una fila de la tabla de tareas realizadas
                resultStr = " = "+result;
            }
            
            //Agregar +1 al numero de tareas realizadas
            ProducerConsumer.frame.addConsumedNum();
            //Obtener el numero actual de tareas realizadas
            int consumed = ProducerConsumer.frame.getConsumedNum();
            
            //guardar la nueva fila a la tabla de tareas realizadas
            String[] row = {""+consumed, ""+id, ""+product, resultStr};
            ProducerConsumer.frame.addDoneRow(row);
            
            //quitar la operacion de tareas por hacer
            ProducerConsumer.frame.removeToDo();
            
            //actualizar la barra de progreso de la capacidad del buffer y el spinner con el numero de tareas realizadas
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
