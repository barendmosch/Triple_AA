package algorithms;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import converters.Mat2Image;

import java.awt.image.BufferedImage;

/* Face detection working on only images using Haar Cascades from the Viola and Jones Face detection algorithm 

XML Yanked from: https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml 

Writen and researched at: 26-10-2020 */

public class FaceFinder {

        private static String HAAR_CASCADE = "/Users/barendmosch/source/repos/ZeroMQ_ws/faceDetector/resources/cascades/haarcascade_frontalface_alt.xml";

        private int absoluteFaceSize = 0;
        private CascadeClassifier faceDetector;
        private Mat2Image convert = new Mat2Image();
        private MatOfRect faces;

        private Mat grayFrame;

        public FaceFinder(){}

        /* Code partially yanked from: https://github.com/opencv-java/face-detection/blob/master/src/it/polito/teaching/cv/FaceDetectionController.java AND
                                        https://www.geeksforgeeks.org/image-processing-java-set-9-face-detection/ 
                but made it a bit my own
                
                Needs to be nicer and more fleshed out*/
        public Mat detect(BufferedImage frame){
                initClassifier();

                grayFrame = new Mat();
                Mat currentFrame = convert.getMatFromImage(frame);
                faces = new MatOfRect();

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
                        Point rectVertex = new Point(rect.x, rect.y);
                        Point oppoRectVertex = new Point(rect.x + rect.width, rect.y + rect.height);
                        Imgproc.rectangle(currentFrame, rectVertex, oppoRectVertex, new Scalar(0, 255, 0));
                }          

                return currentFrame;
        }

        public void initClassifier(){
                faceDetector = new CascadeClassifier();
                faceDetector.load(HAAR_CASCADE);
        }

        public Mat getGrayFrame(){
                return grayFrame;
        }

        public MatOfRect getRectsOfFaces(){
                return faces;
        }
        
        /* img2mat method yanked from: https://techutils.in/blog/2016/08/02/converting-java-bufferedimage-to-opencv-mat-and-vice-versa/ 
                Transform an image to a OpenCV Mat */
}
