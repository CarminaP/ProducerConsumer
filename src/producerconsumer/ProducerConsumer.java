
package producerconsumer;

public class ProducerConsumer {

    public static GUIFrame frame;
    
    public void start(){
        this.frame = new GUIFrame();
        this.frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        ProducerConsumer producerConsumer = new ProducerConsumer();
        producerConsumer.start();
    }
    
}
