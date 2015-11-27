package ericyu.chepai;
/*===========================================================================+
 |      Copyright (c) 2014 Oracle Corporation, Redwood Shores, CA, USA       |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 |           Created by lliyu on 10/28/2015  (lin.yu@oracle.com)             |
 +===========================================================================*/
import ericyu.chepai.robot.*;
import ericyu.chepai.robot.bidstrategy.AmbushAndAidStrategy;
import ericyu.chepai.robot.bidstrategy.IBidStrategy;
import org.opencv.core.*;

import java.awt.*;

public class Console
{
    private static MyRobot robot;

    public static void main(String[] args)
    {
        if (!init())
        {
            System.out.println("init failed!");
            return;
        }
        IBidStrategy bidStrategy = new AmbushAndAidStrategy(robot);
        bidStrategy.execute();
    }

    private static boolean init()
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("this line is for test encoding :" + MyRobot.NOTIFICATION_REQUEST_VCODE_TOO_OFTEN);

        //generate a robot
        try
        {
            //will detect flash position here
            robot = new MyRobot(new Robot());
        }
        catch (AWTException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
