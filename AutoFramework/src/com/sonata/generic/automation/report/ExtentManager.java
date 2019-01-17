package com.sonata.generic.automation.report;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.ReporterType;

public class ExtentManager {
    private static ExtentReports extent;
    
    public static ExtentReports getInstance() {
        if (extent == null) {
            extent = new ExtentReports("D:\\code\\fw\\SAF\\AutoFrameworkCNA2\\extent\\sample.html", true);
            
            // optional
            extent.config()
                .documentTitle("TUI Automation Report")
                .reportName("TUI ATCOMRES")
                .reportHeadline("Inventory");
               
            // optional
            extent
                .addSystemInfo("Selenium Version", "2.44")
                .addSystemInfo("Environment", "QA");
        }
        return extent;
    }
}
