package com.sonata.generic.automation.library;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//For all the lovely screencapture
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.AWTException;
import javax.imageio.ImageIO;
import java.awt.Toolkit;



public final class LoggingHelper
{
   
   /** Make this private so it cannot be initiated.
    * 
    */
   private LoggingHelper() {
   }
   /** Common Functions
   *
   * This will generate a log based on the full path
   *  @param path
   *           Path to the Log file
   *  @param detail
   *           Details you want to log in the log file
   *           
   *  @return Nothing 
   **/
   public static void writeResultToLog(String path, String detail)
   {      
      String fullFilePath = path + ".log";
      DateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
      Date date = new Date();
      try
      {
         BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilePath, true));
         writer.write(dateFormat.format(date) + ": " + detail + "\r\n");
         writer.close();
      }
      catch (IOException e)
      {
         System.out.println("Couldn't write to " + fullFilePath);
      }
      return;
   }     
   
   /** Common Functions
   *
   * This will generate a log based on the full path
   *  @param path
   *           Path to the Log file
   *  @param detail
   *           Details you want to log in the log file
   *  @param captureImage
   *           Capture an image of the screen (Currently set to 1000x800)
   *           
   *  @return Nothing 
   **/
   public static void writeResultToLog(String path, String detail, boolean captureImage)
   {      
      String fullFilePath = path + ".log";
      DateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
      Date date = new Date();
      try
      {
         BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilePath, true));
         writer.write(dateFormat.format(date) + ": " + detail + "\r\n");
         writer.close();
         if (captureImage){
            recordScreen(path);
         }
      }
      catch (IOException e)
      {
         System.out.println("Couldn't write to " + fullFilePath);
      }
      return;
   }
   
   private static void recordScreen(String path){
      try{
      Robot robot = new Robot();
      BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
      Date myDate = new Date();
      File imageFile = new File(path+"ScreenShot" + myDate.getTime()+ ".png");
      ImageIO.write(bufferedImage,"png",imageFile);
      } catch (AWTException e){
         e.printStackTrace();
      } catch (IOException e){
         e.printStackTrace();
      }    
   }


}
