package ericyu.recognize;

import ericyu.recognize.image.ImageUtils;
import ericyu.recognize.image.SegSingleColor;
import ericyu.recognize.image.Segmentation;
import ericyu.recognize.recognize.RecogUtils;
import ericyu.recognize.robot.PositionConstants;
import ericyu.recognize.robot.RobotUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.ml.KNearest;

import java.util.*;
import java.util.List;

/**
 * Created by 麟 on 2015/10/28.
 */
public class CvDemo
{
    private static Rect picRect;

    public static void main(String[] args)
    {
        if (!init())
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
            //get screen shot
            ImageUtils.screenCapture(ImageUtils.screenCaptureImage,
                                     PositionConstants.origin.x,
                                     PositionConstants.origin.y,
                                     PositionConstants.FLASH_WIDTH,
                                     PositionConstants.FLASH_HEIGHT);
            Mat src = Imgcodecs.imread(ImageUtils.screenCaptureImage);
            //get images to recognize
            List<Mat> digitsToRecog = Segmentation.segmentROI(src, picRect, new SegSingleColor());
            //recognize
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
                //TODO: do robot thing here
                RobotUtils.enterVerificationCode(numbers);
                RobotUtils.clickAt(PositionConstants.origin.x + PositionConstants.VERIFICATION_CODE_CONFIRM_ORIGIN_X,
                                   PositionConstants.origin.y + PositionConstants.VERIFICATION_CODE_CONFIRM_ORIGIN_Y);

                return;
            }
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static boolean init()
    {
        picRect = new Rect(PositionConstants.VERIFICATION_CODE_ORIGIN_X,
                           PositionConstants.VERIFICATION_CODE_ORIGIN_Y,
                           PositionConstants.VERIFICATION_CODE_WIDTH,
                           PositionConstants.VERIFICATION_CODE_HEIGHT);
        return true;
    }

    @Deprecated
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
        } catch (Exception e)
        {
            return false;
        }
        picRect = new Rect(x, y, width, height);
        return true;
    }


}
