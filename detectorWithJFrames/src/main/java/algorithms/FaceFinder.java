package algorithms;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import converters.Mat2Image;

import java.awt.image.BufferedImage;

/* Face detection working on only images using Haar Cascades from the Viola and Jones Face detection algorithm 

XML from: https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml */

public class FaceFinder {

        private static String HAAR_CASCADE = "/Users/barendmosch/source/repos/ZeroMQ_ws/detectorWithJFrames/resources/cascades/haarcascade_frontalface_alt.xml";
        private static String IMAGE_PATH = "/Users/barendmosch/source/repos/ZeroMQ_ws/detectorWithJFrames/resources/images/";

        private int absoluteFaceSize = 0;
        private CascadeClassifier faceDetector;
        private Mat2Image convert = new Mat2Image();

        /* Code partially yanked from: https://github.com/opencv-java/face-detection/blob/master/src/it/polito/teaching/cv/FaceDetectionController.java */
                
        public Mat detect(BufferedImage frame){
                initClassifier();

                Mat grayFrame = new Mat();
                Mat currentFrame = convert.img2Mat(frame);
                MatOfRect faces = new MatOfRect();

                Imgproc.cvtColor(currentFrame, grayFrame, Imgproc.COLOR_RGB2GRAY);
                Imgproc.equalizeHist(grayFrame, grayFrame);

                if (absoluteFaceSize == 0){
                        int height = grayFrame.rows();
                        if(Math.round(height * 0.2f) > 0){
                                absoluteFaceSize = Math.round(height * 0.2f);
                        } 
                }

                faceDetector.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
                
                /* If no face detected */
                if (faces.toArray().length == 0){
                        return new Mat();
                }

                for (Rect rect : faces.toArray()){
                        Imgproc.rectangle(currentFrame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                }          

                return currentFrame;
        }

        public void initClassifier(){
                faceDetector = new CascadeClassifier();
                faceDetector.load(HAAR_CASCADE);
        }

        public Mat returnGrayFrame(BufferedImage frame){
                initClassifier();

                Mat grayFrame = new Mat();
                Mat currentFrame = convert.img2Mat(frame);

                Imgproc.cvtColor(currentFrame, grayFrame, Imgproc.COLOR_RGB2GRAY);
                Imgproc.equalizeHist(grayFrame, grayFrame);

                if (absoluteFaceSize == 0){
                        int height = grayFrame.rows();
                        if(Math.round(height * 0.2f) > 0){
                                absoluteFaceSize = Math.round(height * 0.2f);
                        } 
                }

                return grayFrame;
        }

        /* Detect face from an image instead of a video stream, not needed anymore but lets leave it in here */
        public void detectFaceFromImage(){
                CascadeClassifier faceDetector = new CascadeClassifier();

                faceDetector.load(HAAR_CASCADE);

                Mat image = Imgcodecs.imread(IMAGE_PATH + "Wesje.jpeg");
        
                MatOfRect faceDetections = new MatOfRect();
                faceDetector.detectMultiScale(image, faceDetections);

                for (Rect rect : faceDetections.toArray()){
                        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                }

                Imgcodecs.imwrite(IMAGE_PATH + "output.jpg", image);
        }
}
