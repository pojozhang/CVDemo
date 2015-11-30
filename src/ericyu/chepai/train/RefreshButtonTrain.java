package ericyu.chepai.train;
/*===========================================================================+
 |      Copyright (c) 2015 Eric Yu                                           |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 |           Created by lliyu on 11/30/2015  (yulin.jay@gmail.com)            |
 +===========================================================================*/

import ericyu.chepai.image.ImageUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class RefreshButtonTrain extends AbstractSampleTrain
{

    public RefreshButtonTrain(String trainDataPath, String trainClassPath,
                              EigenvetorStrategy eigenvetorStrategy)
    {
        super(trainDataPath, trainClassPath, eigenvetorStrategy);
    }

    public RefreshButtonTrain(String[] images, EigenvetorStrategy eigenvetorStrategy)
    {
        super(images, eigenvetorStrategy);
    }

    public RefreshButtonTrain(String dir, EigenvetorStrategy eigenvetorStrategy)
    {
        super(dir, eigenvetorStrategy);
    }

    @Override
    protected void setSampleEntries()
    {
        sampleEntries = new ArrayList<>();
        for(File file : srcImages)
        {
            Mat curData = ImageUtils.readImage(file.toString());
            int curClass = Integer.valueOf(file.getName().substring(0,1));
            sampleEntries.add(new AbstractMap.SimpleEntry(curData,curClass));
        }
    }

    @Override
    public List<Mat> process(Mat src)
    {
        List<Mat> ret = new ArrayList<>();
        ret.add(ImageUtils.color2Binary(src, 210));
        return ret;
    }

    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String sampleDir = "RefreshImage";

        FlashStatusTrain train = new FlashStatusTrain(sampleDir, new AllPixelEigenvetorStrategy());
        train.dumpTrainData(SampleConstants.REFRESH_BUTTON_SAMPLE_TRAIN_DATA_PATH,
                            SampleConstants.REFRESH_BUTTON_SAMPLE_TRAIN_CLASSES_PATH);

    }
}