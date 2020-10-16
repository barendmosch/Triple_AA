package zmq;

import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class Mat2Image {
        Mat mat = new Mat();
        BufferedImage img;
    
        public Mat2Image() {
        }
    
        public Mat2Image(Mat mat) {
                getSpace(mat);  
        }
    
        public void getSpace(Mat mat) {
                int type = 0;

                if (mat.channels() == 1) {
                        type = BufferedImage.TYPE_BYTE_GRAY;
                } else if (mat.channels() == 3) {
                        type = BufferedImage.TYPE_3BYTE_BGR;
                }

                this.mat = mat;
                int width = mat.cols();
                int height = mat.rows();

                if (img == null || img.getWidth() != width || img.getHeight() != height || img.getType() != type) {
                        img = new BufferedImage(width, height, type);
                }
        }
    
        BufferedImage getImage(Mat mat){
                getSpace(mat);
                /* Raster is a rectangular array of pixels */
                WritableRaster raster = img.getRaster();
                DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                byte[] data = dataBuffer.getData();
                mat.get(0, 0, data);
                return img;
        }
}

