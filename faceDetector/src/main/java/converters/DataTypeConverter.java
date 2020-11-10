package converters;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DataTypeConverter {

        public DataTypeConverter() {

        }

        public byte[] convertImageToByteArray(BufferedImage img) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                        ImageIO.write(img, "jpg", baos);
                        baos.flush();
                        byte[] imageInByte = baos.toByteArray();
                        baos.close();
                        return imageInByte;
                } catch (IOException e) {
                        e.printStackTrace();
                        return new byte[]{0};
                }
        }
}
