package com.example.movie_watchlist_app.model

object HardcodedData {
    val movies = listOf(
        Movie(
            1,
            "The Shawshank Redemption",
            "Drama",
            "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            "High",
            false,
            true
        ),
        Movie(
            2,
            "The Godfather",
            "Crime",
            "The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.",
            "High",
            true,
            true
        ),
        Movie(
            3,
            "The Dark Knight",
            "Action",
            "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            "High",
            false,
            true
        ),
        Movie(
            4,
            "Pulp Fiction",
            "Crime",
            "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            "Medium",
            true,
            false
        ),
        Movie(
            5,
            "Inception",
            "Sci-Fi",
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            "Medium",
            false,
            false
        ),
        Movie(
            6,
            "The Matrix",
            "Sci-Fi",
            "A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.",
            "Medium",
            true,
            true
        ),
        Movie(
            7,
            "Goodfellas",
            "Biography",
            "The story of Henry Hill and his life in the mob, covering his relationship with his wife Karen Hill and his mob partners Jimmy Conway and Tommy DeVito in the Italian-American crime syndicate.",
            "High",
            false,
            false
        ),
        Movie(
            8,
            "Se7en",
            "Crime",
            "Two detectives, a rookie and a veteran, hunt a serial killer who uses the seven deadly sins as his motives.",
            "Medium",
            true,
            false
        )
    )

    val genres = listOf(
        "Action",
        "Comedy",
        "Drama",
        "Horror",
        "Sci-Fi",
        "Thriller",
        "Crime",
        "Biography"
    )
}