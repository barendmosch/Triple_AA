package vision; 

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.border.EmptyBorder;

import org.opencv.core.Mat;

import algorithms.FaceFinder;

public class Frame extends JFrame {

        private static final long serialVersionUID = 4979157600829058817L;
        private JPanel contentPane;
        public Camera camera;
        private FaceFinder faceFinder;
        public boolean isFrame = false;
        public Mat faceFrame = new Mat();

        public Frame() {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(0, 0, 1920, 1080);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            setContentPane(contentPane);
            contentPane.setLayout(null);
    
            faceFinder = new FaceFinder();
            camera = new Camera();
            new MyThread().start();
        }

        public void paint(Graphics g){
            g = contentPane.getGraphics();

            BufferedImage currentFrame = camera.getOneFrame();

            Mat noFaceFrame = faceFinder.returnGrayFrame(currentFrame);
            Mat frame = faceFinder.detect(currentFrame);
            
            if (frame.empty()){
                g.drawImage(camera.returnImageFromMat(noFaceFrame), 0, 0, this);
            } else{
                g.drawImage(camera.returnImageFromMat(frame), 0, 0, this);
            }
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
