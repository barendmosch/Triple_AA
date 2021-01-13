package converters;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/* This class contains all the relevant convert algorithms
        - Image to byte array for getting the acceptable data type for ZMQ */
public class DataTypeConverter {

        public DataTypeConverter() {}

        public byte[] convertImageToByteArray(BufferedImage img) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                        ImageIO.write(img, "jpg", baos);
                        baos.flush();
                        byte[] image_in_byte = baos.toByteArray();
                        baos.close();
                        return image_in_byte;
                } catch (IOException e) {
                        e.printStackTrace();
                        return new byte[]{0};
                }
        }
}
