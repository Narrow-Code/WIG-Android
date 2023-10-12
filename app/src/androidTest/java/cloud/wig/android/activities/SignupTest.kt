package cloud.wig.android.activities

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class SignupTest{

    private lateinit var scenario: ActivityScenario<Signup>

    @Before fun setup(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun allFieldsEmpty(){
        onView(withId(R.id.signup_button)).perform(click())
        onView(withText(R.string.required_fields)).check(matches(isDisplayed()))
    }

    // onView(withId(R.id.username)).perform(ViewActions.typeText("Stitchy"))
    // Espresso.closeSoftKeyboard()


}