package com.iknow.train.eigen;
/*===========================================================================+
 |      Copyright (c) 2015 Eric Yu                                           |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 |           Created by lliyu on 10/31/2015  (yulin.jay@gmail.com)           |
 +===========================================================================*/

import org.opencv.core.Mat;

/**
 * interface for different eigenVector strategy
 */
public interface IEigenvectorStrategy
{
    Mat getEigenVec(Mat src);
}
