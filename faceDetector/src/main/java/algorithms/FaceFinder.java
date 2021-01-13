package algorithms;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import converters.Mat2Image;

import java.awt.image.BufferedImage;

/* Face detection working on only images using Haar Cascades from the Viola and Jones Face detection algorithm */
public class FaceFinder {
        /* XML from: https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml  */
        private static String HAAR_CASCADE = "./resources/cascades/haarcascade_frontalface_alt.xml";

        private int absolute_face_size = 0;
        private CascadeClassifier face_detector;
        private Mat2Image convert;
        private MatOfRect faces;

        private Mat gray_frame;

        public FaceFinder(){
                convert = new Mat2Image();
        }

        /* Detect algorithm used from the following link: https://github.com/opencv-java/face-detection/blob/master/src/it/polito/teaching/cv/FaceDetectionController.java 
        credit GitHub username: luigidr, used for teaching purposes */
        public Mat detect(BufferedImage frame){
                initClassifier();

                gray_frame = new Mat();
                Mat current_frame = convert.getMatFromImage(frame);
                faces = new MatOfRect();

                Imgproc.cvtColor(current_frame, gray_frame, Imgproc.COLOR_RGB2GRAY);
                Imgproc.equalizeHist(gray_frame, gray_frame);

                if (absolute_face_size == 0){
                        int height = gray_frame.rows();
                        if(Math.round(height * 0.2f) > 0){
                                absolute_face_size = Math.round(height * 0.2f);
                        } 
                }

                face_detector.detectMultiScale(gray_frame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(this.absolute_face_size, this.absolute_face_size), new Size());
                
                /* added statement by me to check if there isnt a face found */
                if (faces.toArray().length == 0){
                        return new Mat();
                }

                for (Rect rect : faces.toArray()){
                        Point rect_vertex = new Point(rect.x, rect.y);
                        Point opposite_rect_vertex = new Point(rect.x + rect.width, rect.y + rect.height);
                        Imgproc.rectangle(current_frame, rect_vertex, opposite_rect_vertex, new Scalar(0, 255, 0));
                }          

                return current_frame;
        }

        /* Load the Viola Jones HAAR_CASCADE classifier */
        public void initClassifier(){
                face_detector = new CascadeClassifier();
                face_detector.load(HAAR_CASCADE);
        }

        /* Some getters */ 
        public Mat getGrayFrame(){
                return gray_frame;
        }

        public MatOfRect getRectsOfFaces(){
                return faces;
        }
}
