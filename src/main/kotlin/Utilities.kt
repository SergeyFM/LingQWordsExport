import java.io.File
import java.lang.Exception

// utilities

fun getJSONproperty(field: String, str: String): String {
    if(field !in str) return ""
    return str.substringAfter(field).dropWhile{it in " \":"}
        .takeWhile {it !in ",\""}.trim()
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