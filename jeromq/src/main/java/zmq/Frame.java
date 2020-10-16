package zmq;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import javax.swing.border.EmptyBorder;

public class Frame extends JFrame {

        private static final long serialVersionUID = 4979157600829058817L;
        private JPanel contentPane;
        public Camera camera;

        public Frame() {
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setBounds(0, 0, 1920, 1080);
                contentPane = new JPanel();
                contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
                setContentPane(contentPane);
                contentPane.setLayout(null);
        
                camera = new Camera();
                new MyThread().start();
        }

        public void paint(Graphics g){
            g = contentPane.getGraphics();
            g.drawImage(camera.getOneFrame(), 0, 0, this);
        }
    
        class MyThread extends Thread {
            @Override
            public void run() {
                for (;;){
                    repaint();
                    try { 
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
    
                    }
                }
            }
        }
}
