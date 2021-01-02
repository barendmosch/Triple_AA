package vision;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import algorithms.FaceFinder;
import converters.DataTypeConverter;

/* This object will:
    - control the camera (getting incoming frames in different formats)
    - call the detection algorithm on the incoming frames read from the camera
    - call data type converters when:
        - convert a Mat to BufferedImage or other way around 
        - convert a BufferedImage to Byte[]
    - saving the current image as a jpg in the resources folder (just for testing) */
public class FrameController {
    private Mat frame;
    private DataTypeConverter dtConverter;
    private FaceFinder faceFinder;
    private Camera camera;

    public FrameController() {
        frame = new Mat();
        dtConverter = new DataTypeConverter();
        faceFinder = new FaceFinder();
        camera = new Camera();
    }

    public boolean findFace(){
        BufferedImage currentFrame = camera.getOneFrame();
        frame = faceFinder.detect(currentFrame);

        if (!frame.empty()){
            return true;
        } else{
            return false;
        }
    }

    public int getFaceWidth(){
        Rect face = faceFinder.getRectsOfFaces().toArray()[0];
        return face.width;
    }

    public int getFaceHeight(){
        Rect face = faceFinder.getRectsOfFaces().toArray()[0];
        return face.height;
    }

    public int getFaceX(){
        Rect face = faceFinder.getRectsOfFaces().toArray()[0];
        return face.x;
    }

    public int getFaceY(){
        Rect face = faceFinder.getRectsOfFaces().toArray()[0];
        return face.y;
    }

    public BufferedImage cropImage(BufferedImage img){
        Rect face = faceFinder.getRectsOfFaces().toArray()[0];
        int x = face.x;
        int y = face.y;
        int width = face.width;
        int height = face.height;
        BufferedImage croppedImage = img.getSubimage(x, y, width, height);
        return croppedImage;
    }

    public byte[] getImageByteArray(BufferedImage img){
        return dtConverter.convertImageToByteArray(img);
    }

    public BufferedImage getImage() {
        return camera.mat2Img.getImageFromMat(frame);
    }

    public void saveImage(int i) {
        try {
                File f = new File("resources/images" + i + ".jpg");
                ImageIO.write(getImage(), "JPG", f);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public boolean findFaceFromJPG(BufferedImage currentFrame){
        frame = faceFinder.detect(currentFrame);

        if (!frame.empty()){
            return true;
        } else{
            return false;
        }
    }

    public BufferedImage getFakeImage(String path){
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
