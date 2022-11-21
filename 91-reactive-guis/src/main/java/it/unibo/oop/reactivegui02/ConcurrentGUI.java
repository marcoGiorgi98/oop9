package it.unibo.oop.reactivegui02;

import java.awt.Dimension;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Second example of reactive GUI.
 */
public final class ConcurrentGUI extends JFrame {
    final private JFrame frame = new JFrame();
    final private JPanel panel=new JPanel();
    final private JLabel label=new JLabel();
    final Counter cont = new Counter();
    Thread avanti= new Thread(cont);
    Thread indietro= new Thread(cont);
    final private JButton btn1;
    final private JButton btn2;
    final private JButton btn3;

    public  ConcurrentGUI(){
        frame.setSize(400,400);   
        btn1=new JButton("Up"); 
        btn2=new JButton("Down");
        btn3=new JButton("Stop");
        
        avanti.start();
        btn1.addActionListener((e) -> cont.avanza());
        btn2.addActionListener((e) -> cont.reverse());
        btn3.addActionListener((e) -> cont.stopCounting());
        panel.add(label);
        panel.add(btn1);
        panel.add(btn2);
        panel.add(btn3);
        frame.add(panel);
        frame.setVisible(true);
        
    }       
    private class Counter implements Runnable{
        private volatile boolean rev=false;
        private volatile boolean stop;
        private int counter;
        @Override
        public void run() {
            while(!this.stop){
            final var nextText = Integer.toString(this.counter);
            if(rev==false){
                    try {
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.label.setText(nextText));
                    this.counter++;
                    Thread.sleep(100);
                    } catch (InvocationTargetException | InterruptedException e) {
                        
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.label.setText(nextText));
                        this.counter--;
                    Thread.sleep(100);
                    } catch (InvocationTargetException | InterruptedException e) {
                        
                        e.printStackTrace();
                    }
                }
                }
        }
    public void reverse(){
        this.rev=true;
    }
    public void avanza(){
        this.rev=false;
    }
        public void stopCounting() {
            this.stop = true;
            btn1.setEnabled(false);
            btn2.setEnabled(false);
            btn3.setEnabled(false);
            
        }

    }
}
