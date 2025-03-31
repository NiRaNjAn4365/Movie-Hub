MovieHub is a dynamic Android application designed to provide an immersive movie discovery experience. Developed using Jetpack Compose for a modern and responsive UI, the app integrates with the TMDB API to display a vast collection of movies across various categories such as Action, Comedy, Drama, and more. It supports seamless browsing, detailed movie information, and personalized recommendations.

To ensure smooth data fetching and efficient memory management, Retrofit is used for API requests, while Room Database and Paging 3 handle offline caching and infinite scrolling. Users can effortlessly explore trending and popular movies, thanks to the app’s optimized pagination.

For user management, Firebase Authentication allows secure login and signup options. Additionally, users can create a personalized experience by adding movies to their Favorites Section, which is stored using Firebase Firestore. This makes it easy to manage and access their favorite movies across multiple devices.

The app is built with a robust architecture using Dagger Hilt for dependency injection, ensuring clean and maintainable code. RemoteMediator is used to manage remote data with local caching, providing a seamless offline experience. With smooth navigation using Compose Navigation, the app offers intuitive transitions and visually appealing animations.
