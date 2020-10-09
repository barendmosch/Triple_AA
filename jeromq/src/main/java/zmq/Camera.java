package zmq;

import org.zeromq.ZMQ;

import nu.pattern.OpenCV;

import org.zeromq.SocketType;
import org.zeromq.ZContext;

import javax.sound.sampled.SourceDataLine;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.video.*;
import org.opencv.videoio.VideoCapture;

public class Camera {

        VideoCapture video;

        Camera(){
                video = new VideoCapture();
        }

        public void doSomeVideoShit(){
                video.open(0);

                // if (video.isOpened()){
            
                // }
        }

}