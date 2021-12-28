package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void unAuthorizedUserAvailableRoutes() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertFalse(driver.getTitle().equals("Home"));
	}

	@Test
	public void userAuthorizationRoutesTest() throws InterruptedException {
		String userName = "aly003";
		String password = "123456";

		doMockSignUp("Aly","Assem",userName, password);

		doLogIn(userName, password);

		WebElement logoutButton = driver.findElement(By.id("logout-button"));
		logoutButton.click();

		Assertions.assertFalse(driver.getTitle().equals("Home"));
	}

	public void redirectToNotes() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
	}

	public void redirectToCredentials() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
		driver.get("http://localhost:" + this.port + "/home");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
	}

	@Test
	public void noteCreationTest() throws InterruptedException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);

		String userName = "aly003";
		String password = "123456";

		doMockSignUp("Aly","Assem",userName, password);

		doLogIn(userName, password);

		WebElement navNotesTab = driver.findElement(By.id("nav-notes-tab"));
		navNotesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-button"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("note title test");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("note description test");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-button")));
		WebElement saveNoteButton = driver.findElement(By.id("save-note-button"));
		saveNoteButton.click();

		redirectToNotes();

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(1, notesList.size());
	}

	@Test
	public void noteEditTest() throws InterruptedException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);

		noteCreationTest();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-edit-button")));
		WebElement editNoteButton = driver.findElement(By.id("note-edit-button"));
		editNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.clear();
		noteTitle.sendKeys("title text edited");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys(" edited again");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-button")));
		WebElement saveNoteButton = driver.findElement(By.id("save-note-button"));
		saveNoteButton.click();

		redirectToNotes();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("note-table-title")).getText().contains("title text edited"));
	}

	@Test
	public void noteDeleteTest() throws InterruptedException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);

		noteCreationTest();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-delete-button")));
		WebElement noteDeleteButton = driver.findElement(By.id("note-delete-button"));
		noteDeleteButton.click();

		redirectToNotes();

		WebElement notesTable = driver.findElement(By.id("userTable"));
		List<WebElement> notesList = notesTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0, notesList.size());
	}

	@Test
	public void credentialsCreationTest() throws InterruptedException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);

		String userName = "aly003";
		String password = "123456";

		doMockSignUp("Aly","Assem",userName, password);

		doLogIn(userName, password);

		WebElement navCredentialsTab = driver.findElement(By.id("nav-credentials-tab"));
		navCredentialsTab.click();

		String inputCredentialPassword = "123456";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credentials-button")));
		WebElement addCredentialsButton = driver.findElement(By.id("add-credentials-button"));
		addCredentialsButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credentialUrl = driver.findElement(By.id("credential-url"));
		credentialUrl.click();
		credentialUrl.sendKeys("https://www.google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.click();
		credentialUsername.sendKeys("jacob");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));
		credentialPassword.click();
		credentialPassword.sendKeys(inputCredentialPassword);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential-button")));
		WebElement saveCredentialButton = driver.findElement(By.id("save-credential-button"));
		saveCredentialButton.click();


		redirectToCredentials();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("tbody"));
		WebElement savedCredentialPassword = driver.findElement(By.id("credentials-table-password"));


		// test if the password shown in table is encrypted or not equal to the original password.
		Assertions.assertNotEquals(inputCredentialPassword, savedCredentialPassword.getText());
		Assertions.assertEquals(1, credentialsList.size());
	}

	@Test
	public void credentialEditTest() throws InterruptedException {

		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);

		credentialsCreationTest();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-edit-button")));
		WebElement editCredentialButton = driver.findElement(By.id("credential-edit-button"));
		editCredentialButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement credentialUsername = driver.findElement(By.id("credential-username"));
		credentialUsername.clear();
		credentialUsername.sendKeys("bob");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		String modalCredentialPassword = driver.findElement(By.id("credential-password")).getAttribute("value");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-credential-button")));
		WebElement saveCredentialButton = driver.findElement(By.id("save-credential-button"));
		saveCredentialButton.click();

		redirectToCredentials();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		Assertions.assertEquals("bob", driver.findElement(By.id("credentials-table-username")).getText());
		Assertions.assertNotEquals(driver.findElement(By.id("credentials-table-password")).getText(), modalCredentialPassword);
	}

	@Test
	public void credentialDeleteTest() throws InterruptedException {
		WebDriverWait webDriverWait = new WebDriverWait(driver, 1);

		credentialsCreationTest();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-delete-button")));
		WebElement credentialDeleteButton = driver.findElement(By.id("credential-delete-button"));
		credentialDeleteButton.click();

		redirectToCredentials();

		WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
		List<WebElement> credentialsList = credentialsTable.findElements(By.tagName("tbody"));

		Assertions.assertEquals(0, credentialsList.size());

	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	public void doMockSignUp(String firstName, String lastName, String userName, String password) throws InterruptedException {
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
//		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() throws InterruptedException {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() throws InterruptedException {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() throws InterruptedException {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

}
