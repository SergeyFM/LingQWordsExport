import java.io.File
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.TimeUnit

// Get pictures from a search engine for the words


fun downloadPicture(pathfile: String, lang_code: String, the_word_: String, the_word2_: String, rewrite: Boolean = true, engine: String = "bing"): String {
    // downloads a first picture for the word from google images
    try {
        val f = File(pathfile)
        val MINIMUM_FILE_SIZE = 300L // if the recieved file too small, we'd return "too small"
        if(!rewrite) {
            if(f.exists() && f.length()==0L || f.length()>MINIMUM_FILE_SIZE) {
                print("░")
                return "EXISTS"
            }
        }
        f.createNewFile() // <--- a zero-length file. if other instances are working, they'll see that the file is being worked on
        TimeUnit.MILLISECONDS.sleep( (1L..50L).random() ) // <--- random delay
        
        //------------------ get the link to a pic --------
        val the_word = the_word_.filter{it.isLetterOrDigit() || it in " "}.replace(" ","%20")
        val the_word2 = the_word2_.replace(the_word_," ").filter{it.isLetterOrDigit() || it in " "}.split(" ").sortedByDescending{it.length}.take(2).joinToString("%20OR%20")
        val gURL = when(engine) {
            "bing" -> "https://www.bing.com/images/search?q=%22$the_word%22%20OR%20*$the_word*%20OR%20$the_word2%20" //bing
            else -> "https://www.google.com/search?q=%22$the_word%22%20OR%20*$the_word*%20OR%20$the_word2&tbm=isch" //google
        }
        val the_header = when(engine) { // header rotation
            "bing" -> listOf("Mozilla/4.0 (compatible;MSIE 5.5; Windows 98)",
                "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT5)",
                "Mozilla/4.0 (compatible; MSIE 5.5b1; Mac_PowerPC")[(0..2).random()]
            else -> listOf("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/5.0 (compatible, MSIE 11, Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; AS; rv:11.0) like Gecko")[(0..2).random()]
            
        }
        val client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build()
        val request = HttpRequest.newBuilder()
            .timeout(Duration.ofSeconds(10))
            .uri(URI.create(gURL))
            .header("User-Agent", the_header)
            .header("Accept-Language", "en-US,en")
            .header("Content-Type", "Content-Type: text/html; img/png")
            .build();
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val ret_body = response.body()
        //println(">>>$ret_body")
        val ret = when(engine) {
            "bing" -> ret_body.split("img_cont hoff").drop(1).filter{"img class=\"mimg\"" in it || "Image result for" in it} //bing
            else -> ret_body.split("<table class=").drop(1).filter{"<img class=" in it} //google
        }
        if(ret.size<1) {
            print("X");
            println("\nX: [$the_word_] [$the_word2_] [$gURL] [$the_header]")
            return "$the_word_: NO PICTURE"
        }
        
        val pic_link = ret.first()
            .substringAfter(" src=\"")
            .substringBefore("\"")
            .substringBefore("<")
        
        //------------------ download a pic ---------------
        val fclient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
        val frequest = HttpRequest.newBuilder()
            .uri(URI.create(pic_link))
            .build()
        val fresponse = fclient.send(frequest, HttpResponse.BodyHandlers.ofFile(f.toPath()))
        
        // check the filesize
        val filesize = f.length()
        if(filesize<MINIMUM_FILE_SIZE) {
            print("▪")
            return "TOO SHORT"
        }
        
        print("▓")
        return if(fresponse.statusCode()==200) "OK" else "NOT OK"
    } catch(ex: Exception) {
        print("†")
        return "FAILED"
    }
}
