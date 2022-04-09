import java.io.File
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

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

fun logit(v: Any) {
    println(v)
}


fun saveFile(data: List<Any>, pathfile: String): String {
    // saves file to disk
    
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

fun downloadGooleAudio(pathfile: String, lang_code: String, the_word: String, rewrite: Boolean = true): String {
    // download audio data from google.translate to a file
    val gURL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=$the_word&tl=$lang_code&client=tw-ob"
    try {
        val f = File(pathfile)
        if(!rewrite && f.exists()) {
            println("File already exists: " + pathfile)
            return "ALREADY EXISTS"
        }
        val client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(gURL))
            .build()
        print("<")
        val response = client.send(request,HttpResponse.BodyHandlers.ofFile(f.toPath()))
        print(">")
        return if(response.statusCode()==200) "OK" else "NOT OK"
    } catch(ex: Exception) {
        println("ERROR: " + ex.message)
        return "FAILED"
    }
}