package io.getstream.thestream.services

import io.getstream.cloud.CloudClient

object FeedService {
    private lateinit var client: CloudClient
    private lateinit var user: String

    fun init(user: String, feedCredentials: BackendService.FeedCredentials) {
        this.user = user
        client = CloudClient
            .builder(feedCredentials.apiKey, feedCredentials.token, user)
            .build()
    }

    fun follow(otherUser: String) {
        client
            .flatFeed("timeline")
            .follow(client.flatFeed("user", otherUser))
            .join()
    }
}