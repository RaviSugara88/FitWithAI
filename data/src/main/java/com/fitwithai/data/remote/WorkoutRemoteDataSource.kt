package com.fitwithai.data.remote

import com.fitwithai.domain.model.Workout
import io.ktor.client.HttpClient

class WorkoutRemoteDataSource(
    private val httpClient: HttpClient,
) {
    suspend fun fetchWorkouts(): List<Workout> {
        // TODO: Implement Ktor request with OkHttp engine, caching, and error handling.
        return emptyList()
    }
}
