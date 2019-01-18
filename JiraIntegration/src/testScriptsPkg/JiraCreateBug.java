package testScriptsPkg;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import pageObjectPkg.uniliverPageObj;

public class JiraCreateBug {
	
	static uniliverPageObj un= new uniliverPageObj();	
	static uniliverTestCases tc = new uniliverTestCases();
	
	public static void main(String[] args) throws Exception {
		tc.accessApplication();
		tc.login();
		//tc.searchItems();
		if(un.loginSuccessful()) {
			createABuginJira();
		}
		String test = "Create a bug in JIRA";
		System.out.println(test);
		un.browserClose();
	}
	
	/*public static void loginSuccess() throws Exception {		
		if(un.loginSuccessful()) {
			createABuginJira();
		}
	}*/
	
	public static void createABuginJira() {
		
		try {

			Client client = Client.create();			
			client.addFilter(new HTTPBasicAuthFilter("monikausha.moni.com", "Monika887712#"));
			WebResource webResource = client.resource("https://orangehrmapp.atlassian.net/rest/api/2/issue/");			

			String input="{\"fields\":{\"project\":{\"key\":\"OR\"},\"summary\":\"Test Bug for Jira\",\"description\":\"Can be deleted\", \"issuetype\":{\"name\":\"Bug\"}}}";
			
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
	
			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	

}
