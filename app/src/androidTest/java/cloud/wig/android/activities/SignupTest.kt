package cloud.wig.android.activities

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import cloud.wig.android.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * SignupTest is a set of Large UI tests for the Signup page of WIG.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class SignupTest{

    private lateinit var scenario: ActivityScenario<Signup>

    /**
     * Set up the scenario before each test
     */
    @Before fun setup(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    /**
     * Testing the error message when all fields are empty during sign up.
     */
    @Test
    fun allFieldsEmpty(){
        val username = ""; val email = ""; val password = ""; val confirmPassword = ""

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
        Log.d("SignupTest", " allFieldsEmpty: required_fields error is displayed")
    }

    /**
     * Testing the error message when the username field is empty during sign up.
     */
    @Test
    fun usernameEmpty(){
        val username = ""; val email = "john3-16@breadoflife.com"; val password = "PraiseHim12"; val confirmPassword = "PraiseHim12"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
        Log.d("SignupTest", "usernameEmpty: required_fields error is displayed")
    }

    /**
     * Testing the error message when the email field is empty during sign up.
     */
    @Test
    fun emailEmpty(){
        val username = "GodsServant"; val email = ""; val password = "Ephesians6"; val confirmPassword = "Ephesians6"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
        Log.d("SignupTest", "emailEmpty: required_fields error is displayed")
    }

    /**
     * Testing the error message when the password field is empty during sign up.
     */
    @Test
    fun passwordEmpty(){
        val username = "Moses"; val email = "ten@commandments.gov"; val password = ""; val confirmPassword = "LawAndOrder"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
        Log.d("SignupTest", "passwordEmpty: required_fields error is displayed")
    }

    /**
     * Testing the error message when the confirm password field is empty during sign up.
     */
    @Test
    fun confirmPasswordEmpty(){
        val username = "Goliath"; val email = "BigGuy@philistine.com"; val password = "Am1ADog"; val confirmPassword = ""

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
        Log.d("SignupTest", "confirmPasswordEmpty: required_fields error is displayed")
    }

    /**
     * Testing the error message when the username field input is too short during sign up.
     */
    @Test
    fun usernameTooShort(){
        val username = "nun"; val email = "joshua1-19@imHistDad.com"; val password = "PraiseHim12"; val confirmPassword = "PraiseHim12"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_username_criteria)).check(matches(isDisplayed()))
        Log.d("SignupTest", "usernameTooShort: wrong_username_criteria error is displayed")
    }

    /**
     * Testing the error message when the username field input has spaces during sign up.
     */
    @Test
    fun usernameHasSpaces(){
        val username = "Jesus Loves Me"; val email = "truth@bible.com"; val password = "Romans3-23"; val confirmPassword = "Romans3-23"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_username_criteria))
        Log.d("SignupTest", "usernameHasSpaces: wrong_username_criteria error is displayed")
    }

    /**
     * Testing the error message when the username field input starts with a space during sign up.
     */
    @Test
    fun usernameStartsWithSpace(){
        val username = " JesusLovesMe"; val email = "truth@bible.com"; val password = "Romans3-23"; val confirmPassword = "Romans3-23"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_username_criteria))
        Log.d("SignupTest", "usernameStartsWithSpace: wrong_username_criteria error is displayed")
    }

    /**
     * Testing the error message when the username field input has symbols during sign up.
     */
    @Test
    fun usernameHasSymbols(){
        val username = "TheLoveOf$$$$"; val email = "firstTimothy@sixTen.com"; val password = "Money12$$"; val confirmPassword = "Money12$$"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_username_criteria)).check(matches(isDisplayed()))
        Log.d("SignupTest", "usernameHasSymbols: wrong_username_criteria error is displayed")

    }

    /**
     * Testing the error message when the email field input is missing the @ symbol.
     */
    @Test
    fun emailMissingAtSign(){
        val username = "Solomon"; val email = "buildingTemples.com"; val password = "Proverbs1"; val confirmPassword = "Proverbs1"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
        Log.d("SignupTest", "emailMissingAtSign: email_not_valid error is displayed")

    }

    /**
     * Testing the error message when the email field is missing a top level domain.
     */
    @Test
    fun emailMissingTopLevelDomain(){
        val username = "Solomon"; val email = "originalMason@buildingTemples"; val password = "Proverbs1"; val confirmPassword = "Proverbs1"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
        Log.d("SignupTest", "emailMissingTopLevelDomain: email_not_valid error is displayed")
    }

    /**
    Testing the error message when the email field has spaces in its name
     */
    @Test
    fun emailHasSpacesInName(){
        val username = "Solomon"; val email = "original Mason@buildingTemples.com"; val password = "Proverbs1"; val confirmPassword = "Proverbs1"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
        Log.d("SignupTest", "emailHasSpacesInName: email_not_valid error is displayed")
    }

    /**
    Testing the error message when the email field has spaces in its domain
     */
    @Test
    fun emailHasSpacesInDomain(){
        val username = "Solomon"; val email = "originalMason@building Temples.com"; val password = "Proverbs1"; val confirmPassword = "Proverbs1"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
        Log.d("SignupTest", "emailHasSpacesInDomain: email_not_valid error is displayed")
    }

    /**
    Testing the error message when the email field starts with a space
     */
    @Test
    fun emailStartsWithASpace(){
        val username = "Solomon"; val email = " originalMason@buildingTemples.com"; val password = "Proverbs1"; val confirmPassword = "Proverbs1"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
        Log.d("SignupTest", "emailStartsWithASpace: email_not_valid error is displayed")
    }

    /**
     * Testing the error message when the password and confirm_password fields do not match
     */
    @Test
    fun passwordsDoNotMatch() {
        val username = "Paul"; val email = "writingLetters@epistles.net"; val password = "Damascus1!"; val confirmPassword = "Dramatic1!"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.password_mismatch)).check(matches(isDisplayed()))
        Log.d("SignupTest", "passwordsDoNoMatch: password_mismatch error is displayed")
    }

    /**
     * Testing the error message when the confirm_password matches but ends with a space
     */
    @Test
    fun passwordConfirmationEndsWithSpace() {
        val username = "Paul"; val email = "writingLetters@epistles.net"; val password = "Damascus1!"; val confirmPassword = "Damascus1! "

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.password_mismatch)).check(matches(isDisplayed()))
        Log.d("SignupTest", "passwordConfirmationEndsWithSpace: password_mismatch error is displayed")
    }

    /**
     * Testing the error message when the password is bellow the minimum character limit
     */
    @Test
    fun passwordMinimumCharacters() {
        val username = "Paul"; val email = "writingLetters@epistles.net"; val password = "Romans1"; val confirmPassword = "Romans1"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_password_criteria)).check(matches(isDisplayed()))
        Log.d("SignupTest", "passwordMinimumCharacters: wrong_password_criteria error is displayed")
    }

    /**
     * Testing the error message when the password does not have a number in it
     */
    @Test
    fun passwordNoNumbers() {
        val username = "Paul"; val email = "writingLetters@epistles.net"; val password = "RomansOne"; val confirmPassword = "RomansOne"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_password_criteria)).check(matches(isDisplayed()))
        Log.d("SignupTest", "passwordNoNumbers: wrong_password_criteria error is displayed")
    }

    /**
     * Testing the error message when the password does not have a capital in it
     */
    @Test
    fun passwordNoCapitals() {
        val username = "Paul"; val email = "writingLetters@epistles.net"; val password = "romans110"; val confirmPassword = "romans110"

        enterFields(username, email, password, confirmPassword)

        onView(withText(R.string.wrong_password_criteria)).check(matches(isDisplayed()))
        Log.d("SignupTest", "passwordNoCapitals: wrong_password_criteria error is displayed")
    }

    /**
     * The enterFields function enters all of the fields in the signup field with the arguments.
     * If a string is empty, the field will be skipped over
     *
     * @param username The username to enter
     * @param email The email to enter
     * @param password The password to enter
     * @param confirmPassword The confirm_password to enter
     */
    private fun enterFields(username: String, email: String, password: String, confirmPassword: String){
        val testName = Thread.currentThread().stackTrace[2].methodName

        Log.d("SignupTest", "$testName: test begins")
        if(username != "") {
            onView(withId(R.id.username)).perform(ViewActions.typeText(username))
            Espresso.closeSoftKeyboard()
            Log.d("SignupTest", "$testName: $username was entered in the username field")
        }
        else{
            Log.d("SignupTest", "$testName: username field was skipped")
        }

        if(email != "") {
            onView(withId(R.id.email)).perform(ViewActions.typeText(email))
            Espresso.closeSoftKeyboard()
            Log.d("SignupTest", "$testName: $email was entered in the email field")
        }
        else {
            Log.d("SignupTest", "$testName: email field was skipped")
        }

        if(password != "") {
            onView(withId(R.id.password)).perform(ViewActions.typeText(password))
            Espresso.closeSoftKeyboard()
            Log.d("SignupTest", "$testName: $password was entered in the password field")
        }
        else{
            Log.d("SignupTest", "$testName: password field was skipped")
        }

        if(confirmPassword != "") {
            onView(withId(R.id.confirm_password)).perform(ViewActions.typeText(confirmPassword))
            Espresso.closeSoftKeyboard()
            Log.d("SignupTest", "$testName: $confirmPassword was entered in the confirm_password field")
        }
        else{
            Log.d("SignupTest", "$testName: confirm_password field was skipped")
        }

        onView(withId(R.id.signup_button)).perform(click())
        Log.d("SignupTest", "$testName: signup_button was clicked")
    }

}