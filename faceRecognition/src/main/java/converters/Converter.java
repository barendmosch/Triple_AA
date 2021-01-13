package converters;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/* This class contains methods that converts a datatypes. Some methods are in use, some arent but used to
        Currently in use:
                - HashMapToIntArray 
                - getClientImage 
                - bytesToImage 
                - Mat2BufferedImage
        Not in use: 
                - getMatFromImage */
public class Converter {

        public Mat mat;
        public BufferedImage img;

        public Converter() {}

        /* convert a HashMap (which is the histogram in this case) to an array
                Used in the setDistances method to create the histogram values for calculating the euclidean distance */
        public static int[] hashMapToIntArray(Map<Integer, Integer> m){
                Iterator it_m = m.entrySet().iterator();
                int[] v_arr = new int[m.size()];

                int i = 0;
                while(it_m.hasNext()){
                        Map.Entry pair = (Map.Entry)it_m.next();
                        v_arr[i] = (int)pair.getValue();
                        i++;
                }

                return v_arr;
        }

        /* used to make the BufferedImage from the incoming byte data from the faceDetector
                currently not needed but still can be used to create and save the image */
        public static BufferedImage bytesToImage(byte[] data) {
                InputStream in = new ByteArrayInputStream(data);
                BufferedImage bi = new BufferedImage(1, 1, 1);
                try {
                        bi = ImageIO.read(in);
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return bi;
        }

        /* used to save the LBP image 
                currently not in use, but can be called when we want to create an LBP image */
        public static BufferedImage mat2BufferedImage(Mat mat) throws IOException{
                //Encoding the image
                MatOfByte mat_of_byte = new MatOfByte();
                Imgcodecs.imencode(".jpg", mat, mat_of_byte);
                //Storing the encoded Mat in a byte array
                byte[] byte_array = mat_of_byte.toArray();
                //Preparing the Buffered Image
                InputStream in = new ByteArrayInputStream(byte_array);
                BufferedImage buf_image = ImageIO.read(in);
                return buf_image;
        }

        /* not in use
                can be used to convert a BufferedImage to Matrix */
        public static Mat getMatFromImage(BufferedImage in) {
                Mat out = new Mat(in.getHeight(), in.getWidth(), CvType.CV_8UC3);
                byte[] data = new byte[in.getWidth() * in.getHeight() * (int) out.elemSize()];
                int[] data_buff = in.getRGB(0, 0, in.getWidth(), in.getHeight(), null, 0, in.getWidth());
    
                for (int i = 0; i < data_buff.length; i++) {
                    data[i * 3] = (byte) ((data_buff[i]));
                    data[i * 3 + 1] = (byte) ((data_buff[i]));
                    data[i * 3 + 2] = (byte) ((data_buff[i]));
                }
    
                out.put(0, 0, data);
                return out;
        }
}
