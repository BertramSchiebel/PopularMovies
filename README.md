# PopularMovies

## Project Overview

This app will:
 - Present the user with a grid arrangement of movie posters upon launch.
 - Allow your user to change sort order via a setting:
    - The sort order can be by most popular or by highest-rated
 - Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
   - original title
   - movie poster image thumbnail
   - A plot synopsis (called overview in the api)
   - user rating (called vote_average in the api)
   - release date


## APK Key
Youn need a APK key from [www.themoviedb.org](https://www.themoviedb.org).
After you have received your APK key it must be stored in the file [NetworkUtils.java](./app/src/main/java/com/pinschaneer/bertram/popularmovies/utilities/MovieDb_APKKey.java).

```java

    /**
     * These Api key is your personal key in order to communicate you have to
     * receive your key from the website  https://www.themoviedb.org/
     */
    private static final String API_KEY = "<your key>";
    
```

