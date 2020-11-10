package vision.entity;

import java.util.LinkedList;

import org.opencv.core.Mat;

public class ListOfMatrices {

        private LinkedList<PixelMatrix> listOfPixelMatrices = new LinkedList<PixelMatrix>();

        public ListOfMatrices(){}
        
        public LinkedList<PixelMatrix> getAllPixelMatricesObjects(){
                return listOfPixelMatrices;
        }

        public void addMatrixToList(PixelMatrix pMat){
                listOfPixelMatrices.add(pMat);
        }

        public Mat getRGBRegion(int index){
                return listOfPixelMatrices.get(index).getRGBRegion();
        }

        public LinkedList<Mat> getAllRegions(){
                LinkedList<Mat> result = new LinkedList<Mat>();
                for (PixelMatrix p_mat: listOfPixelMatrices){
                        result.add(p_mat.getRGBRegion());
                }
                return result;
        }

        public LinkedList<LinkedList<Mat>> getAllGrayPixelMatsEveryRegion(){
                LinkedList<LinkedList<Mat>> result = new LinkedList<LinkedList<Mat>>();
                for(PixelMatrix p_mat: listOfPixelMatrices){
                        result.add(p_mat.getAllGrayPixelMats());
                }
                return result;
        }

        public LinkedList<Mat> getAllGrayPixelMatsPerRegion(int index){
                LinkedList<Mat> result = new LinkedList<Mat>();
                for(Mat mat: listOfPixelMatrices.get(index).getAllGrayPixelMats()){
                        result.add(mat);
                }
                return result;
        }






        

        public void printAllRegions(){
                System.out.println("Printing all region matrices: ");
                breakLine();

                int i = 0;
                for (PixelMatrix p_mat: listOfPixelMatrices){
                        System.out.println("Region: " + i);
                        System.out.println(p_mat.getRGBRegion().dump());
                        breakLine();
                        i++;
                }
        }
        
        public void printAllGrayPixelMatrices(){
                System.out.println("Printing Gray PixelMats matrices: ");
                breakLine();

                int i = 0;
                for (PixelMatrix p_mat: listOfPixelMatrices){
                        System.out.println("Region: " + i);
                        System.out.println(p_mat.getRGBRegion().dump());
                        breakLine();
                        i++;
                }
        }
        public void printSpecificRegion(int index){
                System.out.println("Printing region: " + index);
                breakLine();

                System.out.println(listOfPixelMatrices.get(index).getRGBRegion().dump());
        }
       
        public void printGrayValuePixelMatsPerRegion(int index){
                System.out.println("Printing all Gray PixelMats matrices for region: " + index);
                breakLine();

                for (Mat mat: listOfPixelMatrices.get(index).getAllGrayPixelMats()){
                        System.out.println(mat.dump());
                        breakLine();
                }
        }

        public void breakLine(){
                System.out.println("  ");   
                System.out.println(" --------------------------------------------------------- ");      
                System.out.println("  ");
        }
}
