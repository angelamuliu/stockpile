
# Stockpile
Restock your food hoard with confidence!

Developer
Angela M Liu :: CMU F14, 05-433 SSUI Mobile Lab

--------------------------------------------------------------------------------------

# What is

Stockpile is a native android application that is designed to help users keep track of
what foods have been recently purchased. Ever go shopping for groceries and forget what
you got last time? Do you usually buy eggs and you forgot this time? Stockpile can help
both those scenarios.

This application is essentially an exploration of android's features and documentation
of common functionalities. A sqlite database is used, along with sharedpreferences, to 
save user data. Speech-to-text is also played with. Populating listviews programatically
is another pattern that is repeated. Making Stockpile has solidified my understanding of
Android's lifecycle, how to pass data between activities, how to start and finish related
activities, and how to manage resources for difference screen sizes.


# Issues/Future

I had wished to implement a 'rot reminder' feature that would keep track and remind users
of when certain foods were going back (e.g. eggs stocked a month ago are about to rot).
However, I decided that this would be outside the scope of my project. I would have to draw
on a database of food names and amount of time they would perish in three settings 
(outside, fridge, freezer) as well as account for variability in how the user inputs a 
food item. Eggs could be input as 'dozen eggs,' 'eggeronies,' etc. I believe in the future, 
the best way to implement the rot feature would be connecting to a database hosted on the 
cloud via internet and pull relevant data, as saving all the food names and expirations
would be a bit much clientside.

For now, I simply have the rot reminder page show the users the most recent record and
age of all of the foods (which is the same).


--------------------------------------------------------------------------------------

#References

A HUGE thank you to these great online sources! Starred are ones that I especially
referenced from. This project would NOT be possible without the help from these great
online resources.

Transitions between activities
	http://stackoverflow.com/questions/3389501/activity-transition-in-android
	http://stackoverflow.com/questions/5151591/android-left-to-right-slide-animation
	http://chrisrisner.com/31-Days-of-Android--Day-17%E2%80%93Animating-between-Activities
Transition clicking back
	http://stackoverflow.com/questions/12047770/android-how-to-animate-an-activity-transition-when-the-default-back-button-is
Transition clicking 'up' button
	http://stackoverflow.com/questions/17854528/actionbar-up-button-transition-effect
	
Centering content in layouts
	http://stackoverflow.com/questions/1957831/centre-a-button-in-a-linear-layout
	
Speech to Text
	* http://www.androidhive.info/2014/07/android-speech-to-text-tutorial/
	* http://www.jameselsey.co.uk/blogs/techblog/android-how-to-implement-voice-recognition-a-nice-easy-tutorial/
	
ArrayAdapters with Listviews
	https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView

Item Click Listeners in Listviews
	http://stackoverflow.com/questions/12328804/listview-setonitemclicklistener-not-working-for-custom-listview-but-working-in
	* http://www.ezzylearning.com/tutorial/handling-android-listview-onitemclick-event
	
Managing SQLite DB in android
	* http://www.vogella.com/tutorials/AndroidSQLite/article.html
	* http://developer.android.com/training/basics/data-storage/databases.html
	http://www.sqlite.org/cli.html
	http://developer.android.com/reference/android/database/Cursor.html
	http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html

Clicking on items in ListActivity
	http://stackoverflow.com/questions/5716599/how-to-set-onlistitemclick-for-listview-in-android
	http://stackoverflow.com/questions/11096601/how-to-reference-onlistitemclick-in-listactivity

Passing information between Activities
	http://stackoverflow.com/questions/7257631/how-to-pass-array-to-another-activity

Start activity for result help (Passing info between activities, having activities go back and resume a previous one)
	http://stackoverflow.com/questions/22553672/android-startactivityforresult-setresult-for-a-view-class-and-an-activity-cla
	
Removing the keyboard on click of another button
	http://stackoverflow.com/questions/4235070/how-to-clear-focus-and-remove-keyboard-on-android

Check if intent has extras
	http://stackoverflow.com/questions/8255618/check-if-extras-are-set-or-not

Expandable List
	* http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/

Ordering SQL Query for android
	http://stackoverflow.com/questions/8948435/how-do-i-order-my-sqlite-database-in-descending-order-for-an-android-app

Using SharedPreferences for K-V
	http://stackoverflow.com/questions/5946135/difference-between-getdefaultsharedpreferences-and-getsharedpreferences
	* http://stackoverflow.com/questions/12432585/android-shared-preferences-with-multiple-activities
	http://stackoverflow.com/questions/5950043/how-to-use-getsharedpreferences-in-android
	http://stackoverflow.com/questions/11264214/storing-data-in-sharedpreferences-accessible-to-all-activities
	http://developer.android.com/reference/android/content/SharedPreferences.Editor.html

Removing the Action Bar with Themes
	http://tech.xster.net/tips/remove-title-bar-on-android-application
	http://stackoverflow.com/questions/8456835/how-to-disable-action-bar-permanently/

Define Style, Theme, Colors
	http://stackoverflow.com/questions/3769762/android-color-xml-resource-file
	https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/styles.xml
	https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/themes.xml

Drawables in terms of making own button
	http://developer.xamarin.com/recipes/android/resources/general/style_a_button/
	* http://dmytrodanylyk.com/pages/blog/flat-button.html

Relative Layout help
	http://stackoverflow.com/questions/2318775/how-to-create-fixed-footer-in-android-layout

Applying Drawable Style programatically
	http://stackoverflow.com/questions/2349652/android-open-resource-from-drawable-string


	
	
	
	
	