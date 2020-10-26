package zmq;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.awt.image.BufferedImage;

/* Face detection working on only images using Haar Cascades from the Viola and Jones Face detection algorithm 

XML Yanked from: https://raw.githubusercontent.com/opencv/opencv/master/data/haarcascades/haarcascade_frontalface_alt.xml 

Writen and researched at: 26-10-2020 */

public class FaceFinder {

        private static String HAAR_CASCADE = "/Users/barendmosch/source/repos/ZeroMQ_ws/jeromq/resources/cascades/haarcascade_frontalface_alt.xml";
        private static String IMAGE_PATH = "/Users/barendmosch/source/repos/ZeroMQ_ws/jeromq/resources/images/";

        private int absoluteFaceSize = 0;
        private CascadeClassifier faceDetector;

        /* Code partially yanked from: https://github.com/opencv-java/face-detection/blob/master/src/it/polito/teaching/cv/FaceDetectionController.java AND
                                        https://www.geeksforgeeks.org/image-processing-java-set-9-face-detection/ 
                but made it a bit my own
                
                Needs to be nicer and more fleshed out*/
        public Mat detect(BufferedImage frame){
                initClassifier();

                Mat grayFrame = new Mat();
                Mat currentFrame = img2Mat(frame);
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
                
                for (Rect rect : faces.toArray()){
                        Imgproc.rectangle(currentFrame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                }          

                return currentFrame;
        }
        public void initClassifier(){
                faceDetector = new CascadeClassifier();
                faceDetector.load(HAAR_CASCADE);
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

        /* img2mat method yanked from: https://techutils.in/blog/2016/08/02/converting-java-bufferedimage-to-opencv-mat-and-vice-versa/ 
                Transform an image to a OpenCV Mat */
        public static Mat img2Mat(BufferedImage in) {
            Mat out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
            byte[] data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
            int[] dataBuff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());

            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i]));
                data[i * 3 + 1] = (byte) ((dataBuff[i]));
                data[i * 3 + 2] = (byte) ((dataBuff[i]));
            }

            out.put(0, 0, data);
            return out;
        }
}
