package vision;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import algorithms.FaceFinder;
import converters.DataTypeConverter;

/* This class is responsible for:
    - making the needed objects in the constructor 
    - check if a face is found
    - crops the image to get only the facial rectangle */
public class FrameController {
    private Mat frame;
    private DataTypeConverter converter;
    private FaceFinder face_finder;
    private Camera camera;

    public FrameController() {
        frame = new Mat();
        converter = new DataTypeConverter();
        face_finder = new FaceFinder();
        camera = new Camera();
    }

    /* continuously reads the camera feed
        sends the frame to the Viola Jones detection algorithm
        if face is found then return true and proceed with cropping and sending data then go to the next frame
        if not, proceed with the next frame */
    public boolean findFace(){
        BufferedImage current_frame = camera.getOneFrame();
        frame = face_finder.detect(current_frame);

        if (!frame.empty()){
            return true;
        } else{
            return false;
        }
    }

    /* get the top left coordinates of the rectangular box around the face created by the Viola Jones algorithm (face.x, face.y) and crop the entire image with the help of these coordinates and the width + height to get the rectangle as a BufferedImage
    Return the image to main */
    public BufferedImage cropImage(BufferedImage img){
        Rect face = face_finder.getRectsOfFaces().toArray()[0];
        int x = face.x;
        int y = face.y;
        int width = face.width;
        int height = face.height;
        BufferedImage croppedImage = img.getSubimage(x, y, width, height);
        return croppedImage;
    }

    /* call the converter to convert an image to byte array */
    public byte[] getImageByteArray(BufferedImage img){
        return converter.convertImageToByteArray(img);
    }

    /* getters for the face rectangle coordinates, width and height */
    public int getFaceWidth(){
        Rect face = face_finder.getRectsOfFaces().toArray()[0];
        return face.width;
    }

    public int getFaceHeight(){
        Rect face = face_finder.getRectsOfFaces().toArray()[0];
        return face.height;
    }

    public int getFaceX(){
        Rect face = face_finder.getRectsOfFaces().toArray()[0];
        return face.x;
    }

    public int getFaceY(){
        Rect face = face_finder.getRectsOfFaces().toArray()[0];
        return face.y;
    }

    /* get the current frame as a BufferedImage */
    public BufferedImage getImage() {
        return camera.getMat2Image().getImageFromMat(frame);
    }

    /* currently not in use, but can be called to safe the current frame as an JPG locally */
    public void saveImage(int i) {
        try {
                File f = new File("resources/images" + i + ".jpg");
                ImageIO.write(getImage(), "JPG", f);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    /* these two methods are used when computing the face detection on images instead of the camera feed */
    public boolean findFaceFromJPG(){
        frame = face_finder.detect(getFakeImage());

        if (!frame.empty()){
            return true;
        } else{
            return false;
        }
    }

    public BufferedImage getFakeImage(){
        String path = "./resources/images/fileName.jpg";
        BufferedImage buf_image = new BufferedImage(1,1,1);
        File file = new File(path);

        try {
            buf_image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf_image;
    }
}
