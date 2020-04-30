package io.getstream.thestream.services

import io.getstream.cloud.CloudClient

object FeedService {
    private lateinit var client: CloudClient
    private lateinit var user: String

    fun init(user: String, feedCredentials: BackendService.FeedCredentials) {
        FeedService.user = user;
        client = CloudClient
            .builder(feedCredentials.apiKey, feedCredentials.token, user)
            .build()
    }
}