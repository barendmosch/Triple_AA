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

    public void saveImage(int i) {
        try {
                File f = new File("resources/wholeImg" + i + ".jpg");
                ImageIO.write(getImage(), "JPG", f);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public void saveFace(int i) {
        File f = new File("resources/faceOutput" + i + ".jpg");
        try {
                ImageIO.write(cropImage(getImage()), "JPG", f);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public byte[] getImageByteArray(BufferedImage img){
        return dtConverter.convertImageToByteArray(img);
    }

    public BufferedImage getImage() {
        return camera.mat2Img.getImageFromMat(frame);
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
}
