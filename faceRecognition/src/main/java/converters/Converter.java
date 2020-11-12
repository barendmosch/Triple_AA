package converters;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Converter {

        public Mat mat;
        public BufferedImage img;

        public Converter() {}

        public BufferedImage getClientImage(int width, int height, int type, byte[] frameByte) {
                BufferedImage image = new BufferedImage(width, height, type);
                WritableRaster r = image.getRaster();
                image.setData(r);
                return image;
        }

        public int bytesToInt(byte[] i) {
                ByteBuffer wrap = ByteBuffer.wrap(i);
                return wrap.getInt();
        }

        public BufferedImage bytesToImage(byte[] data) {
                InputStream in = new ByteArrayInputStream(data);
                BufferedImage bi = new BufferedImage(1, 1, 1);
                try {
                        bi = ImageIO.read(in);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return bi;
        }

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

        public void getSpace(Mat mat) {
                int type = 0;

                if (mat.channels() == 1 || mat.channels() == 4) {
                        type = BufferedImage.TYPE_BYTE_GRAY;
                } else if (mat.channels() == 3 || mat.channels() == 5) {
                        type = BufferedImage.TYPE_3BYTE_BGR;
                } 

                this.mat = mat;
                int width = mat.cols();
                int height = mat.rows();

                if (img == null || img.getWidth() != width || img.getHeight() != height || img.getType() != type) {
                        img = new BufferedImage(width, height, type);
                }
        }
    
        public BufferedImage Mat2BufferedImage(Mat mat) throws IOException{
                //Encoding the image
                MatOfByte matOfByte = new MatOfByte();
                Imgcodecs.imencode(".jpg", mat, matOfByte);
                //Storing the encoded Mat in a byte array
                byte[] byteArray = matOfByte.toArray();
                //Preparing the Buffered Image
                InputStream in = new ByteArrayInputStream(byteArray);
                BufferedImage bufImage = ImageIO.read(in);
                return bufImage;
        }

        /* This is for splitting a string to an int[] */
        public int[] stringToIntArray(String arr){
                String[] items = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

                int[] results = new int[items.length];
                
                for (int i = 0; i < items.length; i++) {
                    try {
                        results[i] = Integer.parseInt(items[i]);
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    };
                }

                return results;
        }
}
