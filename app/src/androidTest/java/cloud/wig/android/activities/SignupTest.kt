package cloud.wig.android.activities

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
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the username field is empty during sign up.
     */
    @Test
    fun usernameEmpty(){
        onView(withId(R.id.email)).perform(ViewActions.typeText("john3-16@breadoflife.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("PraiseHim12"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform((ViewActions.typeText(("PraiseHim12"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the email field is empty during sign up.
     */
    @Test
    fun emailEmpty(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("GodsServant"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("Ephesians6"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform((ViewActions.typeText(("Ephesians6"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the password field is empty during sign up.
     */
    @Test
    fun passwordEmpty(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Moses"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("ten@commandments.gov"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform((ViewActions.typeText(("LawAndOrder5"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the confirm password field is empty during sign up.
     */
    @Test
    fun confirmPasswordEmpty(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Goliath"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("BigGuy@philistine.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform((ViewActions.typeText(("Am1ADog?!"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the username field input is too short during sign up.
     */
    @Test
    fun usernameTooShort(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("nun"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("joshua1-19@imHisDad.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform((ViewActions.typeText(("PraiseHim12"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("PraiseHim12"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_username_criteria)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the username field input has spaces during sign up.
     */
    @Test
    fun usernameHasSpaces(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Jesus Loves Me"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("truth@bible.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform((ViewActions.typeText(("Romans3-23"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("Romans3-23"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_username_criteria))
    }

    /**
     * Testing the error message when the username field input starts with a space during sign up.
     */
    @Test
    fun usernameStartsWithSpace(){
        onView(withId(R.id.username)).perform(ViewActions.typeText(" JesusLovesMe"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("truth@bible.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform((ViewActions.typeText(("Romans3-23"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("Romans3-23"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_username_criteria))
    }

    /**
     * Testing the error message when the username field input has symbols during sign up.
     */
    @Test
    fun usernameHasSymbols(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("TheLoveOf$$$$"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("firtsTimmothy@sixTen.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform((ViewActions.typeText(("Money12$$"))))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("Money12$$"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_username_criteria)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the email field input is missing the @ symbol.
     */
    @Test
    fun emailMissingAtSign(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Solomon"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("buildingTemples.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the email field is missing a top level domain.
     */
    @Test
    fun emailMissingTopLevelDomain(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Solomon"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("originalMason@buildingTemples"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
    }

    /**
    Testing the error message when the email field has spaces in its name
     */
    @Test
    fun emailHasSpacesInName(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Solomon"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("original Mason@buildingTemples.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
    }

    /**
    Testing the error message when the email field has spaces in its domain
     */
    @Test
    fun emailHasSpacesInDomain(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Solomon"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("originalMason@building Temples.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
    }

    /**
    Testing the error message when the email field starts with a space
     */
    @Test
    fun emailStartsWithASpace(){
        onView(withId(R.id.username)).perform(ViewActions.typeText("Solomon"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText(" originalMason@buildingTemples.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("proverbs"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.email_not_valid)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the password and confirm_password fields do not match
     */
    @Test
    fun passwordsDoNotMatch() {
        onView(withId(R.id.username)).perform(ViewActions.typeText("Paul"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText(" writingLetters@epistles.net"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("Damascus1!"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("Dramatic1!"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.password_missmatch)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the confirm_password matches but ends with a space
     */
    @Test
    fun passwordConfirmationEndsWithSpace() {
        onView(withId(R.id.username)).perform(ViewActions.typeText("Paul"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText(" writingLetters@epistles.net"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("Damascus1!"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("Damascus1! "))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.password_missmatch)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the password is bellow the minimum character limit
     */
    @Test
    fun passwordMinimumCharacters() {
        onView(withId(R.id.username)).perform(ViewActions.typeText("Paul"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("writingLetters@epistles.net"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("Romans1"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("Romans1"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_password_criteria)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the password does not have a number in it
     */
    @Test
    fun passwordNoNumbers() {
        onView(withId(R.id.username)).perform(ViewActions.typeText("Paul"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("writingLetters@epistles.net"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("RomansOne"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("RomansOne"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_password_criteria)).check(matches(isDisplayed()))
    }

    /**
     * Testing the error message when the password does not have a capital in it
     */
    @Test
    fun passwordNoCapitals() {
        onView(withId(R.id.username)).perform(ViewActions.typeText("Paul"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.email)).perform(ViewActions.typeText("writingLetters@epistles.net"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("romans110"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirm_password)).perform(ViewActions.typeText("romans110"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.wrong_password_criteria)).check(matches(isDisplayed()))
    }

}