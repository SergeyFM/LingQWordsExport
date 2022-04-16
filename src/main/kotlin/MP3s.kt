import java.io.File
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.TimeUnit

// Downloads mp3 audio for the file

fun downloadGooleAudio(pathfile: String, lang_code: String, the_word_: String, rewrite: Boolean = true): String {
    // downloads audio data from google.translate to a file
    val the_word = the_word_.replace(" ","%20")
    val gURL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=$the_word&tl=$lang_code&client=tw-ob"
    try {
        val f = File(pathfile)
        if(f.exists()) {
            if(rewrite) f.delete()
            else {
                print("░")
                return "EXISTS"
            }
        }
        f.createNewFile() // <--- if other instances are working, they'll see that the file is being worked on
        TimeUnit.MILLISECONDS.sleep( (1L..50L).random() ) // <--- random delay
        
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
        val request = HttpRequest.newBuilder()
            .timeout(Duration.ofSeconds(10))
            .uri(URI.create(gURL))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofFile(f.toPath()))
        print("▓")
        return if(response.statusCode()==200) "OK" else "NOT OK"
    } catch(ex: Exception) {
        //println("\nERROR: [$the_word] " + ex)
        print("†")
        return "FAILED"
    }
}