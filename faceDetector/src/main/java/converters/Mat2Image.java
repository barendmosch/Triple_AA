package converters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class Mat2Image {

        public Mat mat = new Mat();
        public BufferedImage img;

        public Mat2Image() {}
    
        public void getSpace(Mat mat) {
                int type = 0;
                if (mat.channels() == 1 || mat.channels() == 4) {
                        type = BufferedImage.TYPE_BYTE_GRAY;
                } else if (mat.channels() == 3 || mat.channels() == 4) {
                        type = BufferedImage.TYPE_3BYTE_BGR;
                } 

                this.mat = mat;
                int width = mat.cols();
                int height = mat.rows();

                if (img == null || img.getWidth() != width || img.getHeight() != height || img.getType() != type) {
                        img = new BufferedImage(width, height, type);
                }
        }
    
        /* Read the camera input (matrix) and return the current frame as a BufferedImage */
        public BufferedImage getImageFromMat(Mat mat){
                getSpace(mat);
                /* Raster is a rectangular array of pixels */
                WritableRaster raster = img.getRaster();
                DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                byte[] data = dataBuffer.getData();
                mat.get(0, 0, data);
                return img;
        }

        /* convert the BufferedImage back to a Matrix */
        public Mat getMatFromImage(BufferedImage in) {
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

