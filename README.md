## Note
Search feature only show for names and addresses from <b>Indonesia</b>. If you want to search by names and addresses in your country, you can change in file QueryParameter called "<b>ID</b>" based your ISO country code</br>
For google maps api key, use your own API key which enables billing. Due to the security and size limits of my API key, therefore I replaced my API key with a text which is bound to fail when you build this application. Please put your API key in gradle.properties and in the values -> google_maps_api.xml. Or you can look up the text value for the google maps API key in a project called "<b>YOUR_API_KEY</b>"</br>
You can get your API key by following the this <a href="https://developers.google.com/maps/documentation/places/android-sdk/cloud-setup">Tutorial</a></br>
And don't forget to enable your billing, Places API and Geocoding API.

## Preview
<p align="center"><img src="https://media3.giphy.com/media/OoYQYtx4Nhfj8kVo6W/giphy.gif" alt="animated" /></p>

## Developed with tools and libraries:
IDE: Android Studio</br>
Gradle Tools: 4.1.2 (gradle-6.5-bin.zip)</br>
Language: Kotlin</br>
Layouting: XML</br>
Architechture: Model-View-ViewModel (MVVM)</br>
Asynchronous: Coroutines</br>
Other libraries:</br>
<a href="https://github.com/LouisCAD/Splitties">Splitties (Kotlin extensions for Toast)</a></br>
<a href="https://github.com/afollestad/assent">Assent (Runtime Permission with combination Kotlin, AndroidX and Coroutines</a></br>
<a href="https://github.com/facebook/shimmer-android">Facebook Shimmer</a></br>

## Features:
- Maps</br>
a screen that displays a google map and has a marker in the middle with a display below like a custom bottomsheet (without the behavior of the bottomsheet) which contains the name and address based on the coordinates of the marker in the middle of the map and when the user presses the select address button it will display the full address in a toast (small message with a pop up at the bottom of the screen).</br>
Build it with fragment google maps, ImageView as marker in a center of layout, custom toolbar with back button, title and search button, custom view like bottomsheet without bottomsheet behavior include MaterialTextView for name and address and have a loading when getting name and address location with Facebook Shimmering. Coordinate take with FusedLocationProviderClient and ofcourse with permission ACCESS_FINE_LOCATION and ACCESS_COURSE_LOCATION. To get name and address with coordinate, first look for the place_id with the Geocoding API and then return the Places API based on the place_id, because it contains more information about it, such as the address component and the building or area name.</br>

- Search Address</br>
a screen that displays the address search feature based on user input. After the user enters, below the search box will appear a list of names and addresses predicted according to the user's input. When you tap on the address you are looking for, the search feature will close and redirect the marker to the destination address.</br>
Build with SearchView as the search field for the address that the user wants to find and the RecyclerView as the result of searching for names or addresses that have been entered automatically if the user is no longer typing which displays a list of predicted names and addresses. When the user presses the address that is searched for, the layout of the search feature will be hidden again and the map feature will display the marker that has been moved based on the selected address that the user pressed.
