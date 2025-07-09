package com.techullurgy.chessk.data.api.utils

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal suspend fun HttpClient.postWithoutBody(url: String): HttpResponse =
    postWithBody<Unit>(url, null)

internal suspend inline fun <reified T> HttpClient.postWithBody(
    url: String,
    body: T?
): HttpResponse {
    return post(url) {
        body?.let {
            setPostBody(it)
        }
    }
}

private inline fun <reified T> HttpRequestBuilder.setPostBody(body: T) {
    contentType(ContentType.Application.Json)
    setBody(body)
}