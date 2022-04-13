import java.io.File
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.TimeUnit

// utilities

fun getJSONproperty(field: String, str: String): String {
    if(field !in str) {
        println("getJSONproperty: [$field] is not found in [$str]")
        return ""
    }
    return str.replace("\n"," ")
        .substringAfter(field)
        .dropWhile{it in " \":"}
        .substringBefore("\",")
        .substringBefore((",\""))
}

fun unEscapeUnicode(txt: String): String {
    val r = txt.split("\\u")
    if(r.size==1) return txt
    var ret = r[0] + (1..r.lastIndex).map {ind->
        val n = r[ind].take(4)
        val d = n.toIntOrNull(16)
        if(d==null) r[ind] else "" + d.toChar() + r[ind].drop(4)
    }.joinToString("")
    return ret
}

fun saveFile(data: List<Any>, pathfile: String): String {
    // saves file to disk
    if(data.isEmpty()) return "Nothing"
    try {
        val the_file = File(pathfile)
        the_file.printWriter().use {out->
            data.forEach {
                out.println(it.toString())
            }
        }
    } catch (ex: Exception) {
        println("ERROR " + ex.message)
        return "ERROR"
    }
    return "OK"
}

fun loadWordsFromFile(pathfile: String): List<Connector.Word> {
    // parses a file of words into List<Word>
    
    var txt = ""
    try {
        val f = File(pathfile)
        txt = f.readText()
    } catch(ex: Exception) {
        println("ERROR: " + ex.message)
        return listOf<Connector.Word>()
    }
    val ret: List<Connector.Word> = txt.split("\n").mapNotNull {line->
        val p = line.split("\t").map{it.trim()}
        if(p.size==5) Connector.Word(
            p[0], p[1], p[2],
            p[3].toIntOrNull()?:0,
            p[4].toIntOrNull()?:0
        ) else null
    }
    return ret
}

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
        
        TimeUnit.MILLISECONDS.sleep( (1L..50L).random() ) // <--- random delay
    
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
        val request = HttpRequest.newBuilder()
            .timeout(Duration.ofSeconds(5))
            .uri(URI.create(gURL))
            .build()
        val response = client.send(request,HttpResponse.BodyHandlers.ofFile(f.toPath()))
        print("▓")
        return if(response.statusCode()==200) "OK" else "NOT OK"
    } catch(ex: Exception) {
        println("\nERROR: [$the_word] " + ex)
        return "FAILED"
    }
}


fun downloadPicture(pathfile: String, lang_code: String, the_word_: String, the_word2_: String, rewrite: Boolean = true, engine: String = "bing"): String {
    // downloads a first picture for the word from google images
    try {
        val f = File(pathfile)
        if(f.exists()) {
            if(rewrite) f.delete()
            else {
                print("░")
                return "EXISTS"
            }
        }
        
        TimeUnit.MILLISECONDS.sleep( (1L..150L).random() ) // <--- random delay
    
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
        val client = HttpClient.newBuilder().build()
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
        //println(">>>> $pic_link")
        
        //------------------ download a pic ---------------
        val fclient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
        val frequest = HttpRequest.newBuilder()
            .uri(URI.create(pic_link))
            .build()
        val fresponse = fclient.send(frequest,HttpResponse.BodyHandlers.ofFile(f.toPath()))
        print("▓")
        return if(fresponse.statusCode()==200) "OK" else "NOT OK"
    } catch(ex: Exception) {
        println("\nERROR: " + ex)
        print("†")
        return "FAILED"
    }
}

fun wordToFilename(word: String, ext: String): String = word.replace(" ","_").filter{it.isLetterOrDigit()}+ext

fun saveToHTMLfile(data_full: List<Connector.Word>, pathfile_: String): String {
    // saves the list of words into a html-format file
    if(data_full.isEmpty()) return "Nothing"
    if("." !in pathfile_) return "Wrong filename"
    val SPLIT_SIZE = 3500
    val times = data_full.size/SPLIT_SIZE+1
    if(times>1) print("[$times]")
    val chunk_size = data_full.size/times+1
    var current_part=0
    var return_status = "OK"
    data_full.sortedBy{it.w.uppercase()}.chunked(chunk_size) {data->
        current_part++
            try {
                val added_part_number = if(times>1) "$current_part" else ""
                val pathfile = pathfile_.dropLastWhile{it!='.'}.dropLast(1) + "$added_part_number." + pathfile_.takeLastWhile{it!='.'}
                val the_file = File(pathfile)
                if(the_file.exists()) the_file.delete()
                the_file.printWriter().use {out->
                    out.println(head)
                    data.forEach {word->
                        val mp3file = wordToFilename(word.w, ".mp3")
                        val picfile = wordToFilename(word.w, ".jpeg")
                        val line = "<tr><th>${word.w}</th> \n" +
                        "\t<th><audio controls><source src=\"..\\mp3\\$mp3file\"></auio></th> \n" +
                        "\t<th><img src=\"..\\pic\\$picfile\"></img></th> \n" +
                        "\t<th class=t>${word.t}</th><th class=e>\n" +
                        "\t<th class=e>${word.f}</th>\n</tr>"
                        out.println(line)
                    }
                    out.println(tail)
                }
            } catch (ex: Exception) {
                println("ERROR " + ex.message)
                return_status = "ERROR"
            }
    }
    return return_status
}
private val head = """
<!DOCTYPE html>
<html>
  <head>
	<style>
		body {text-align: left;}
		th {padding-right:25px;font-size:22px;color:blue;font-style:normal}
		th.t {font-size:22px;color:black;font-style:normal}
		th.e {font-size:18px;color:black;font-style:italic}
		table {border-collapse:collapse;}
		tr {border-top:1px solid gainsboro;border-bottom:1px solid gainsboro;}
		audio {display:block;height:30px;width:110px}
        img {width:110px}
	</style>
    <meta charset="utf-8">
    <title>LingQ words</title>
  </head>
  <body>
	<table>
        """
private val tail = """
	</table>
  </body>
</html>
    <!--- Generated by LingQWordsExport application. --->
   """

fun getLanguageByCode(lang_code: String): String = mapOf(
    "cs" to "Czech", "no" to "Norwegian", "tr" to "Turkish", "fi" to "Finnish",
    "he" to "Hebrew", "ro" to "Romanian", "nl" to "Dutch", "el" to "Greek", "pl" to "Polish",
    "eo" to "Esperanto", "la" to "Latin", "da" to "Danish", "uk" to "Ukrainian", "sk" to "Slovak",
    "ms" to "Malay", "id" to "Indonesian", "zh-t" to "Chinese (Traditional)", "hk" to "Cantonese",
    "gu" to "Gujarati", "bg" to "Bulgarian", "fa" to "Persian", "be" to "Belarusian", "ar" to "Arabic",
    "srp" to "Serbian", "hrv" to "Croatian", "hu" to "Hungarian", "ca" to "Catalan", "en" to "English",
    "fr" to "French", "de" to "German", "es" to "Spanish", "it" to "Italian", "ja" to "Japanese",
    "ko" to "Korean", "zh" to "Chinese", "pt" to "Portuguese", "ru" to "Russian", "sv" to "Swedish",
    "hy" to "Armenian", "is" to "Icelandic"
)[lang_code]?:lang_code
