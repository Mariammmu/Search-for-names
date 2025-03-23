# Search-for-names
Search for names using SharedFlow

README for Search by SharedFlow App
Overview
This app demonstrates a simple search functionality using Jetpack Compose, where the user can search for names in a list, and the list is filtered in real time as the user types. It uses SharedFlow from Kotlin Coroutines to handle search queries efficiently, ensuring that the UI remains responsive even with frequent updates.

Features
Real-time Search: As you type into the search bar, the list of names is filtered in real time.

Debounced Search: To reduce unnecessary filtering, the search input is debounced, meaning it will wait for 500 milliseconds of inactivity before updating the search results.

Snackbar Host: The app includes a Snackbar host to handle showing messages (not actively used in this sample code but can be useful for showing results or errors).

Lazy Loading: A LazyColumn is used to efficiently display the list of filtered names, ensuring smooth scrolling performance.

Libraries Used
Jetpack Compose: A modern toolkit for building native UIs in Android.

Kotlin Coroutines: For handling background operations such as debounced search.

SharedFlow: A state flow for emitting and collecting search queries with buffer and flow control.


Code Explanation
MainActivity:

The MainActivity sets up the content view using setContent.

It initializes the searchBar SharedFlow and listens for incoming search queries.

SearchScreen Composable:

Contains a TextField for input and a LazyColumn for displaying the filtered results.

Uses LaunchedEffect to start a coroutine that listens to the searchBar and performs debouncing, filtering, and updating the UI.

The SnackbarHost is ready to display notifications.

Filtering Logic:

The filter() function takes the list of names and filters them based on the search query, ignoring case.

SharedFlow:

searchBar is a MutableSharedFlow that emits search queries.

The flow is debounced to avoid excessive filtering and ensures smooth UX
