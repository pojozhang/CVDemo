package ericyu.recognize.image;
/*===========================================================================+
 |      Copyright (c) 2015 Eric Yu                                           |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 |           Created by lliyu on 10/31/2015  (yulin.jay@gmail.com)           |
 +===========================================================================*/

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;


public class SampleUtils
{
    public static String sampleDir = "resources\\samples";

    private static String[] straightImages = new String[]{
            "CodeImage\\0069.jpg",
            "CodeImage\\0441.jpg",
            "CodeImage\\0472.jpg",
            "CodeImage\\0593.jpg",
            "CodeImage\\0687.jpg",
            "CodeImage\\0709.jpg",
            "CodeImage\\1132.jpg",
            "CodeImage\\1345.jpg",
            "CodeImage\\1347.jpg",
            "CodeImage\\1367.jpg",
            "CodeImage\\1587.jpg",
            "CodeImage\\1697.jpg",
            "CodeImage\\1932.jpg",
            "CodeImage\\2118.jpg",
            "CodeImage\\2781.jpg",
            "CodeImage\\3014.jpg",
            "CodeImage\\3436.jpg",
            "CodeImage\\3535.jpg",
            "CodeImage\\3797.jpg",
            "CodeImage\\4150.jpg",
            "CodeImage\\4212.jpg",
            "CodeImage\\5103.jpg",
            "CodeImage\\6399.jpg",
            "CodeImage\\6863.jpg",
            "CodeImage\\8429.jpg",
            "CodeImage\\8502.jpg",
            "CodeImage\\8983.jpg",
            "CodeImage\\9149.jpg",
            "CodeImage\\9566.jpg",
    };

    static
    {
        mkDir(new File(ImageUtils.unNormalizedDir));
    }


    public static void mkDir(File file)
    {
        file = file.getAbsoluteFile();
        if (file.getParentFile().exists())
        {
            file.mkdir();
        }
        else
        {
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }

    /**
     * rename image name to image**.png
     * generate *.info for training
     * <p/>
     * fileName :  line  0 1 2 3 4 5 ... 9
     */
    @Deprecated
    private static void renameImageFiles(String fileName)
    {
        File file = new File("resources\\" + fileName);
        File[] images = file.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".png");
            }
        });

        for (int i = 0; i < images.length; ++i)
        {
            images[i].renameTo(new File(file.getAbsolutePath() + "\\image" + (i + 1) + ".png"));
        }
        try
        {
            FileWriter infoFile = new FileWriter(file.getAbsolutePath() + "\\" + fileName + ".info");
            for (int i = 0; i < images.length; ++i)
            {
                infoFile.write("image" + (i + 1) + ".png 1 0 0 20 20\n");
            }
            infoFile.close();
            FileWriter negdataFile = new FileWriter(file.getAbsolutePath() + "\\" + fileName + "negdata.txt");
            for (int i = 0; i < images.length; ++i)
            {
                negdataFile.write(fileName + "\\image" + (i + 1) + ".png\n");
            }
            negdataFile.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void writeObjectToFile(Object obj)
    {
        File file = new File("test.dat");
        FileOutputStream out;
        try
        {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("write object success!");
        }
        catch (Exception e)
        {
            System.out.println("write object failed");
            e.printStackTrace();
        }
    }

    /**
     * generate rotated samples (normalized)
     *
     * @param imagePath input image (should NOT be normalized AND NOT be enlarged!)
     * @param dstDir    dir to save rotated samples
     */
    public static void generateRotatedSamples(String imagePath, String dstDir)
    {
        mkDir(new File(dstDir));
        Point[] offsets = new Point[]{
                new Point(0, 0),
                new Point(-1, -1),
                new Point(-1, 0),
                new Point(-1, 1),
                new Point(0, -1),
                new Point(0, 1),
                new Point(1, -1),
                new Point(1, 0),
                new Point(1, 1)
        };

        Mat src = Imgcodecs.imread(imagePath);
        Mat digit = ImageUtils.color2Gray(src);

        /**
         * generate samples
         * 0. enlarge by size(2,2) and offset
         * 1. rotate
         * 2. cut with new contour
         * 3. enlarge
         */

        String curFileName = new File(imagePath).getName();
        String curFileNameWithNoSuffix = curFileName.substring(0, curFileName.lastIndexOf("."));



        for (Point offset : offsets)
        {
            //offset
            Mat offsetImage = ImageUtils.transition(digit, (int) offset.y, (int) offset.x);
            //enlarge
            Mat enlarged = ImageUtils.enlargeMat(offsetImage, 10, 10);

            for (int i = -16; i <= 16; ++i)
            {
                //rotate
                Mat rotated = ImageUtils.rotateMat(enlarged, i * 5);
                Mat normalized = ImageUtils.normalization(rotated);

                Imgcodecs.imwrite(dstDir + File.separator + curFileNameWithNoSuffix +
                                          "_rotated_" + i * 5 +
                                          "_x_" + offset.x +
                                          "_y_" + offset.y +
                                          ".png", normalized);
            }
        }
    }

    /**
     * rename unNormalizedImages according to its name
     * <p/>
     * e.g.
     * <p/>
     * 21182_xxx.png   ->   1_21182.png
     * 1 is "2118".charAt(2)
     */
    public static void renameUnNormalizedImage()
    {
        File file = new File(ImageUtils.unNormalizedDir);
        File[] images = file.listFiles();
        for (File image : images)
        {
            String curName = image.getName();
            String numbers = curName.substring(0, 4);
            String index = curName.substring(4, 5);
            char targetNumber = numbers.charAt(Integer.parseInt(index));
            image.renameTo(
                    new File(ImageUtils.unNormalizedDir + "\\" + String.valueOf(
                            targetNumber) + "_" + numbers + index + ".png"));
        }
    }

    public static void generateUnNormalizedSample()
    {
        File unNormalizedFile = new File(ImageUtils.unNormalizedDir);
        if (unNormalizedFile.exists())
        {
            File[] files = unNormalizedFile.listFiles();
            for (File cur : files)
            {
                cur.delete();
            }
        }
        Boolean bak = ImageUtils.dumpImg;
        ImageUtils.dumpUnNormalizedSamples = true;
        ImageUtils.dumpImg = false;
        for (String file : straightImages)
        {
            Segmentation.main(new String[]{file});
        }
        ImageUtils.dumpUnNormalizedSamples = false;
        ImageUtils.dumpImg = bak;
    }

    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        File sampleFile = new File(sampleDir);
        if (sampleFile.exists())
        {
            File[] files = sampleFile.listFiles();
            for (File cur : files)
            {
                cur.delete();
            }
        }


//        1.
        generateUnNormalizedSample();

//        2.
        renameUnNormalizedImage();

//        3.
        File file = new File(ImageUtils.unNormalizedDir);
        File[] files = file.listFiles();
        for (File image : files)
        {
            String path = image.getAbsolutePath();
            generateRotatedSamples(path, sampleDir);
        }

    }

}
