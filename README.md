# Quartermaster
## Developed using Android Studio

This application was a personal pet project created by me that turns an Android device into a combination clock, weather display, and Google Home link.

This app is my first attempt at a fully realized and completed app for personal use. There is a lot of room for improvement on all grounds, but the application is fully functional for everyday use.

The proof of concept is that I have been using this application without issue since its completion on an old personal phone. The phone can keep the application running 24/7 without error with the only stipulation being that it must be connected to the internet (for the purpose of retrieving the weather data from the openweathermap.org API), and that the device be configured to keep the screen on while charging. The application can be run without the above configurations, but weather information will not be able to be received, nor will the device be able to stay 'awake' under normal conditions respectively.

The weather information is taken from the openweathermap.org API, and the location data can be changed by changing the 'city code' under the settings dialog. This city code can be found by visiting openweathermap.org and locating your city, then copying the 6-digit code found at the end of the URL and pasting it into the settings dialog. Due to the nature of this process, user GPS/Wifi location data is not necessary nor accessed.

Future improvement documentation is tentatively forthcoming.
