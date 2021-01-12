package vision;

import sql.DatabaseAction;
import vision.maths.LBP;

import java.util.HashMap;
import java.util.Map;

import org.opencv.core.Mat;

import converters.Converter;

public class LBPHAlgorithm {
        private static final int HISTOGRAM_MAX_PIXEL = 255;

        /* mat is considered the grayscale mat */
        private Mat mat;

        private LBP lbp;
        private int[] lbp_values;
        private Map<Integer, Integer> h_recognise;
        
        public LBPHAlgorithm(Mat mat) {
                this.mat = mat;
                lbp = new LBP(this.mat);
        }

        public Map<Integer, Integer> createHistogram(){
                Map<Integer, Integer> histogram = new HashMap<>();
                int n = lbp_values.length;

                for(int i=0; i<HISTOGRAM_MAX_PIXEL; i++){
                        histogram.put(i, 0);
                }

                for(int i=0; i<n; i++){
                        if (histogram.containsKey(lbp_values[i])){
                                histogram.put(lbp_values[i], histogram.get(lbp_values[i]) + 1);
                        }
                }

                return histogram;
        }

        public int[] getLBPValues(int[][] lpb_values){
                return lbp.getLBPValues(lpb_values);
        }

        private int[][] convertMat2DIntArrayWithBorders(Mat mat){
                return lbp.convertMat2DIntArrayWithBorders(mat);
        }

        public void initPersonsData(String name){
                int[][] mat_data_with_borders = convertMat2DIntArrayWithBorders(mat);
                lbp_values = getLBPValues(mat_data_with_borders);
                h_recognise = createHistogram();

                String json_string = Converter.buildJSONStringFromMap(h_recognise);

                DatabaseAction.saveHistograms(name, json_string);
        }

        public Mat getGrayScaleMat(){
                return mat;
        }
}
