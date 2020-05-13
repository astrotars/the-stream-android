package io.getstream.thestream.services

import io.getstream.cloud.CloudClient
import io.getstream.core.models.Activity
import io.getstream.core.options.Limit

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

    fun timelineFeed(): MutableList<Activity> {
        return client
            .flatFeed("timeline")
            .getActivities(Limit(25))
            .join()
    }

    fun profileFeed(): MutableList<Activity> {
        return client
            .flatFeed("user")
            .getActivities(Limit(25))
            .join()
    }
}