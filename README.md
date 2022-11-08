# Running the app 
This app is meant to be used on a Pixel 3a API 33 device. When using an emulator in Android Studio, please use this device with this API level, or some of the features may not work.

When first running the app, it will ask for permission to your location. Please accept this, otherwise no beaches will show up. 

Additionally, the Google Maps API is sometimes slow, and as a result the device will say that the app is not responding. If you give it some time, it should eventually show up. If no beaches show up on the map, try rerunning the app, as sometimes the slow connection throws an error.

Finally, please do not use the API key within this project for anything else. It is hidden on GitHub, but is required to run the app in a secrets.xml file.

# Features
When first opening the app, you will see a login/register page. From there, you can open the map. 

On the map, you should see beaches and your current location. Clicking any beach will show restaurants within 1000 feet. You can change the radius of the restaurant search in the footer of the app. Clicking on a restaurant should route to the restaurant and show an ETA of walking there, along with storing the trip in your profile. 

Clicking on the beach will show nearby parking lots. Clicking on a parking lot will show a route from your current location to the parking lot. It will also show the ETA for the trip as a marker on your current location. 

If you end the route, the trip will be stored in the app. You can see all previous trips on the trip page, which can be accessed through the footer.

You can also review the beaches in the review tab, along with seeing previous reviews and deleting any of your reviews.

We hope you enjoy! 
