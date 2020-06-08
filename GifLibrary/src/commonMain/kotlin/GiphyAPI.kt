package org.gifLibrary

class GiphyAPI {

    val apiKey: String = ""

    private val client: HttpClient = HttpClient { // 1
        install(JsonFeature) { // 2
            serializer = KotlinxSerializer(Json.nonstrict).apply { // 3
                setMapper(GifResult::class, GifResult.serializer()) // 4
            }
        }
    }

    private fun HttpRequestBuilder.apiUrl(path: String) { // 5
        url { // 6
            takeFrom("https://api.giphy.com/") // 7
            encodedPath = path // 8
        }
    }
    suspend fun callGiphyAPI(): GifResult = client.get {
        apiUrl(path = "v1/gifs/trending?api_key=$apiKey&limit=25&rating=G")
    }

    fun getGifUrls(callback: (List<String>) -> Unit) { // 1
        GlobalScope.apply { // 2
            launch(dispatcher) { // 3
                val result: GifResult = callGiphyAPI() // 4
                val urls = result.data.map { // 5
                    it.images.original.url // 6
                }
                callback(urls) // 7
            }
        }
    }
}
