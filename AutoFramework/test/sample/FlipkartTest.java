package sample;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import com.sonata.generic.automation.fixture.GenericWebFixture;
import org.openqa.selenium.interactions.Actions;

public class FlipkartTest {
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testFlipkart() throws InterruptedException {
//		GenericWebFixture fixture = new GenericWebFixture(
//				"D:\\code\\SonataFrameWork\\Sonata-AutomationFramework\\AutoFrameworkCNA2\\test\\sample\\flipkart.xml",
//				"browser is " + "Chrome" + ", server is " + "www.flipkart.com");
		GenericWebFixture fixture = new GenericWebFixture(
				"C:\\Users\\vaibhav.hb.SSLTUIODC\\code\\fw\\SAF\\AutoFrameworkCNA2\\test\\sample\\flipkart.xml",
				"browser is " + "Chrome" + ", server is " + "www.flipkart.com");
		fixture.openPage();
		
		Actions  a = new Actions(fixture.getDriver());
//		a.moveToElement(fixture.getDriver().findElement(By.xpath("//*[@id='fk-mainhead-id']/div[2]/div/div/ul/li[1]/a/span"))).build().perform();
		
		Thread.sleep(5000);
		
		fixture.hover("men");
		
		fixture.click("boots");
	}

}
