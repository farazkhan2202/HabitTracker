# HabitTracker
A simple habit tracker app.
I wrote it study some Android development topics and for personal use.

## Features
- Create and track habits using simple UI
- View your progress by days in form of a bar chart and total progress
- Use simple indicators on habit items to motivate yourself: keep the indicators green!

## Screenshots
See [screenshots directory](screenshots/)

## What I used/learned
- MVVM architecture
- Persistence of habits and progress is implemented using Room
- LiveData is used to publish data from Room through ViewModel
- Bar chart is implemented using [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- Basic integration testing is implemented using Espresso to test whether UI interactions do what they're supposed to
- UI is constructed mostly using ConstraintLayout, CardView and RecyclerView
