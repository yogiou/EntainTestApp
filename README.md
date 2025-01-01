This application is developed with Android Studio Ladybug | 2024.2.1 Patch 3, tested with Google Pixel 8, Android 15, December 2024 patch.

The architecture is MVVM.

The main components are Jetpack compose, Hilt, Retrofit2.

To develop the Automatic API refresh and count down timer, I use one flow with while loop to achieve.
Call the API every minute (as we don't need the races 1 minute passed) and update the UI every second.

I cover unit tests of the main functions in view model and Utils.

To run the app, download the code and run with Android studio, after finish the gradle updates and executions, click run button with app.

Future possible improvements:
As I am using polling to update the API, it may be not the best solution as it may consume too much unnecessary resources. The possible ways include:
Using push instead of polling, but need to discuss with Backend
Discuss with Backend to have a proper API refresh interval (maybe most likely the races will be update every 5 minutes)

As we want to show the most updated races to the users, I did not save the data into local or using cache. But we may need to use it as it will help to lower Backend's loading.

