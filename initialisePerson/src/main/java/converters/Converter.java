package converters;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Converter {

        public Converter() {}

        public static String buildJSONStringFromMap(Map<Integer, Integer> h){
                StringBuilder json_string = new StringBuilder();
                json_string.append("{");
                for(int i=0; i<h.size(); i++){
                        json_string.append("\""+i+"\":"+h.get(i)+",");
                }

                json_string.setLength(json_string.length() - 1);
                json_string.append("}");
                
                return json_string.toString();
        }
}
