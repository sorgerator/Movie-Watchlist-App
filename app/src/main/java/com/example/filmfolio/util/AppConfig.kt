package com.example.filmfolio.util

object AppConfig {
    /**
     * Set to true to use Firebase Firestore as the remote datasource.
     * Set to false to use the custom REST API as the remote datasource.
     * In either case, if connection is unavailable, it falls back automatically to Room.
     */
    var useFirebase: Boolean = true

}
