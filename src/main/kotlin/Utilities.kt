import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    val r = txt
        .replaceSomeCharacters()
        .split("\\u")
    if(r.size<2) return txt
    val ret = r[0] + (1..r.lastIndex).map {ind->
        val n = r[ind].take(4)
        val d = n.toIntOrNull(16)
        if(d==null) r[ind] else "" + d.toChar() + r[ind].drop(4)
    }.joinToString("")
    
    return ret
}

fun readFile(pathfile: String): String {
    // returns a content of the file
    var txt = ""
    try {
        val f = File(pathfile)
        txt = f.readText()
    } catch(ex: Exception) {
        println("ERROR: " + ex)
        return "ERROR"
    }
    return txt.trim()
}

fun fileExists(pathfile: String): Boolean {
    try {
    val f = File(pathfile)
    return f.exists()
    } catch (ex: Exception) {
        println("ERROR checking if file exists. [$pathfile] $ex")
        return false
    }
}

fun today(): String {
    // returns today date as "2022-04-22"
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return now.format(formatter)
}