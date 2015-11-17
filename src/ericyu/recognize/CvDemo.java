package ericyu.recognize;

import ericyu.recognize.image.ImageUtils;
import ericyu.recognize.image.SegByContours;
import ericyu.recognize.image.Segmentation;
import ericyu.recognize.recognize.RecogUtils;
import org.opencv.core.*;
import org.opencv.ml.KNearest;

import java.util.*;
import java.util.List;

/**
 * Created by 麟 on 2015/10/28.
 */
public class CvDemo
{
    private static Rect picRect;
//    private static CascadeClassifier zeroDetector = new CascadeClassifier("resources\\data\\cascade.xml");

    public static void main(String[] args)
    {
        if (!initParams(args))
        {
            System.out.println("please verify the input params");
            return;
        }
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //get classifier
        KNearest kNearest = RecogUtils.getKnnClassifier();
//        ANN_MLP ann_mlp = RecogUtils.getAnnClassifier();

        while (true)
        {
            //get samples to recognize
            ImageUtils.screenCapture();
            List<Mat> digitsToRecog = Segmentation.digitSegmentationWithROI(ImageUtils.screenCaptureImage, picRect,
                                                                            new SegByContours());
            if (digitsToRecog != null && digitsToRecog.size() == 4)
            {
                ArrayList<Integer> numbers = new ArrayList<Integer>();
                for (Mat mat : digitsToRecog)
                {
                    Mat toRecog = RecogUtils.getEigenVec(mat, null);
                    int num = (int) kNearest.findNearest(toRecog, 10, new Mat());
//                    int num = (int)ann_mlp.predict(toRecog);

                    numbers.add(num);
                    System.out.println(num);
                }
//                return;
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static boolean initParams(String[] args)
    {
        int argCnt = args.length;
        if (argCnt < 4)
        {
            System.out.println("input args are not correct.\n" +
                                       "There should be 4 args: \n" +
                                       "x,y,width,height\n" +
                                       " ______________________________________\n" +
                                       "|              ^                       |\n" +
                                       "|              |                       |\n" +
                                       "|  screen      y                       |\n" +
                                       "|              |                       |\n" +
                                       "|              v                       |\n" +
                                       "|<----x-------> __width____            |\n" +
                                       "|          ^   |           |           |\n" +
                                       "|      height  | digits    |           |\n" +
                                       "|          v   |___________|           |\n" +
                                       "|                                      |\n" +
                                       "|______________________________________|");
            return false;
        }
        int x, y, width, height;
        try
        {
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            width = Integer.parseInt(args[2]);
            height = Integer.parseInt(args[3]);
        }
        catch (Exception e)
        {
            return false;
        }
        picRect = new Rect(x, y, width, height);
        return true;
    }


}