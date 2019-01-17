package tui.atCore;



import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.sonata.generic.automation.fixture.GenericWebFixture;

import tui.atCore.TestInventory.DriverHandle;

public class TestBooking {
	static long DEL_MICRO = 500;
	static long DEL_SMALL = 1000;
	static long DEL_MED = 3000;
	static long DEL_LONG = 5000;
	static long DEL_EX = 10000;
	
	
	static String START_DATE = "20-May-2016";
	static String NO_OF_STAYS = "7";
	static String DEPART_FROM = "LGW";
	static String ACCM = "ESMJ0958";
	

	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testBooking() throws InterruptedException {
		Set <String> handleCollection = new HashSet<String>();
		GenericWebFixture fixture = new GenericWebFixture(
				"D:\\code\\fw\\SAF\\AutoFrameworkCNA2\\test\\tui\\atCore\\tui.xml",
				"browser is " + "Internet_Explorer" + ", server is " + "tui-phoenix-prjt1/Test1/inhouse/");	WebDriver mainDriver = fixture.getDriver();
		String mainHandle = mainDriver.getWindowHandle();
		System.out.println("main handle = "+mainHandle);
		handleCollection.add(mainHandle);
		String title = mainDriver.getTitle();
		System.out.println("main title= " + title);
		fixture.openPage();
		
		//Loging in to the app
		fixture.type("username", "T1NEWUSER");
		fixture.type("password", "welcome1");
		fixture.click("loginButton");
		Thread.sleep(DEL_SMALL);
		
		//Booking scenario
		fixture.click("booking");
		fixture.click("newBooking");
		
		Thread.sleep(DEL_MED);		
		fixture.getDriver().findElement(By.id("AMT")).click();
		Thread.sleep(DEL_MICRO);
		((JavascriptExecutor)fixture.getDriver()).executeScript("$(\'select').find('option:contains("+ "Inclusive" +")\').attr(\"selected\",true)");		
		fixture.click("proceed");
		
		fixture.waitFor("search");
		fixture.type("startDate", START_DATE);
		fixture.type("stays", NO_OF_STAYS);
		fixture.type("departFrom", DEPART_FROM);		
		fixture.type("accm", ACCM);
		fixture.click("search");			
		
		//INCLUSIVE FLIGHTS Screen
		fixture.waitFor("proceedButton");
		fixture.click("proceedButton");
				
		//ROOM LIST Screen
		fixture.waitFor("proceed");
		fixture.type("roomListTextBox", "1");
		fixture.click("proceed");
		Thread.sleep(DEL_MED);
		fixture.getDriver().switchTo().alert().accept();

		//FLIGHT EXTRAS Screen
		fixture.waitFor("proccedButtFlightExtras");
		fixture.click("proccedButtFlightExtras");
		
		//SERVICE SETS Screen
		fixture.waitFor("proccedButtService");
		fixture.click("proccedButtService");		
		
		//NEW OVERVIEW Screen
		fixture.waitFor("bookButton");
		fixture.click("bookButton");
		Thread.sleep(DEL_SMALL);
		fixture.click("overLayUpdateButton");
		fixture.getDriver().switchTo().alert().accept();
		Thread.sleep(DEL_MED);
		//AMEND NAMES Screen
		fixture.getDriver().findElement(By.id("I_-1_PAX_TP")).click();  //Type
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Adult" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		fixture.getDriver().findElement(By.id("I_-1_PAX_TITLE")).click(); //Title
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Mr" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		fixture.getDriver().findElement(By.id("I_-1_PAX_GNDR")).click(); //Gender
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Male" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		fixture.type("firstName1", "Vikas");
		fixture.type("surname1", "C");
		fixture.type("dob1", "26-10-1978");
		
		fixture.getDriver().findElement(By.id("I_-2_PAX_TP")).click();  //Type
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Adult" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		fixture.getDriver().findElement(By.id("I_-2_PAX_TITLE")).click(); //Title
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Mr" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		fixture.getDriver().findElement(By.id("I_-2_PAX_GNDR")).click(); //Gender
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Male" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		fixture.type("firstName2", "Anil");
		fixture.type("surname2", "N");
		fixture.type("dob2", "26-10-1978");
		
		fixture.type("address1", "Richmond Road");
		fixture.type("address2", "Bangalore");
		
		fixture.type("postalCode", "in45tn");
		
		fixture.type("mobile", "9999999999");
		fixture.type("homeMail", "cvikas78@gmail.com");
		
		fixture.click("proceed");
		Thread.sleep(DEL_MED);
		fixture.getDriver().switchTo().alert().accept();
		Thread.sleep(DEL_MICRO);

		final Set<String> handlesBefore = fixture.getDriver().getWindowHandles();		
		System.out.println("handlesBefore size= "+handlesBefore.size());

		fixture.click("bookButton");
		WebDriverWait wait = new WebDriverWait(fixture.getDriver(), 10);
		wait.until(new Predicate<WebDriver>() {
			
			@Override
			public boolean apply(WebDriver input) {
				System.out.println("WAITING>>>>>>>>>>> "+input.getWindowHandles().size());
				return (handlesBefore.size() <
						input.getWindowHandles().size());			
			}
		});
		
		
		DriverHandle driverHandle = getDriverHandle(mainDriver, handleCollection);
		WebDriver popUpDriver = driverHandle.driver;
		
		popUpDriver.switchTo().frame("genericframe");
		Thread.sleep(DEL_SMALL);
		popUpDriver.findElement(By.id("I_2_SRC_CD")).click();
		Thread.sleep(DEL_SMALL);
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "TrackingCode" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		popUpDriver.findElement(By.id("I_2_SRC_SUB_CD")).click();
		Thread.sleep(DEL_SMALL);
		((JavascriptExecutor)fixture.getDriver())
		.executeScript("$(\'select').find('option:contains("+ "Newpaper" +")\').attr(\"selected\",true)");
		Thread.sleep(DEL_SMALL);
		
		popUpDriver.findElement(By.id("SavButton")).click();
		Thread.sleep(DEL_SMALL);
		Thread.sleep(DEL_SMALL);
		popUpDriver.switchTo().window(mainHandle);
		
		fixture.getDriver().switchTo().alert().accept(); //Accept Insurance Alert		
		Thread.sleep(DEL_SMALL);
		
		fixture.click("declineButton");
		Thread.sleep(DEL_SMALL);
		fixture.getDriver().switchTo().alert().accept(); //Accept Insurance Alert
		fixture.click("bookButton");

	}
	
	DriverHandle getDriverHandle(WebDriver driver, Set <String> handleCollection) {
		System.out.println("");
		System.out.println("session ID = " + ((RemoteWebDriver)driver).getSessionId().toString());
		WebDriver d = driver;
		Set<String> handles = driver.getWindowHandles();
		System.out.println("handles count = " + handles.size());
		Iterator<String> iterator = handles.iterator();
		
		String currHandle = d.getWindowHandle();

		while (iterator.hasNext()) {
			String handle = iterator.next().toString();
			System.out.println("loop handle = " + handle);
			if (!handleCollection.contains(handle)) {
				System.out.println("Found the window with handle = " + handle);
				d = driver.switchTo().window(handle);
				currHandle = handle;
				System.out.println("getDriverHandle(): currHandle = " + currHandle);
				System.out.println("getDriverHandle(): currTitle =  " + d.getTitle());
				System.out.println("");
				handleCollection.add(currHandle);
				return new DriverHandle(d, currHandle);
			}
		}
		System.out.println("");
		return null;		
	}
	
	class DriverHandle {
		WebDriver driver;
		String handle;
		
		public DriverHandle(WebDriver d, String h) {
			driver = d;
			handle = h;
		}
	}

}
