package vision.entity;

import java.util.LinkedList;

import org.opencv.core.Mat;

public class PixelMatrix {

        private Mat rgb_region;
        private LinkedList<Mat> gray_pixel_mat;

        public PixelMatrix(){}

        public PixelMatrix(Mat rgb_region, LinkedList<Mat> gray_pixel_mat){
                this.rgb_region = rgb_region;
                this.gray_pixel_mat = gray_pixel_mat;
        }

        public Mat getRGBRegion(){
                return rgb_region;
        }

        public LinkedList<Mat> getAllGrayPixelMats(){
                return gray_pixel_mat;
        }
}
