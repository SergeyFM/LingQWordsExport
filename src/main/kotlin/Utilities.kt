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

fun downloadGooleAudio(pathfile: String, lang_code: String, the_word: String, rewrite: Boolean = true): String {
    // download audio data from google.translate to a file
    val gURL = "https://translate.google.com.vn/translate_tts?ie=UTF-8&q=$the_word&tl=$lang_code&client=tw-ob"
    try {
        val f = File(pathfile)
        if(f.exists()) {
            if(rewrite) f.delete()
            else {
                print("#")
                return "OK"
            }
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

fun wordToFilename(word: String): String = word.replace(" ","_").filter{it.isLetterOrDigit()}+".mp3"

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
                        val mp3file = wordToFilename(word.w)
                        val line = "<tr><th>${word.w}</th> \n" +
                        "\t<th><audio controls><source src=\"..\\mp3\\$mp3file\"></auio></th> \n" +
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
		audio {display:block;height:30px;width:110px}
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