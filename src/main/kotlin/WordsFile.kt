import java.io.File
import java.lang.Exception

// Saves or reads file with the words

fun wordToFilename(word: String, ext: String): String =
    // returns a filename from a word
    //word.filter{it.isLetterOrDigit()}+ext
        "LQWE_" +
            word.replace("  "," ").replace(" ","_").filter{it.isLetterOrDigit() || it=='_'}.take(60) +
                ext

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

fun loadWordsFromFile(pathfile: String): List<Word> {
    // parses a file of words into List<Word>
    var txt = ""
    try {
        val f = File(pathfile)
        txt = f.readText()
    } catch(ex: Exception) {
        println("ERROR: " + ex.message)
        return listOf<Word>()
    }
    val ret: List<Word> = txt.split("\n").filter{it.length>4}.mapNotNull {line->
        val p = line
            .replaceSomeCharacters()
            .split("\t")
            .map{it.trim()}
        if(p.size==5) Word(
            p[0], p[1], p[2],
            p[3].toIntOrNull()?:0,
            p[4].toIntOrNull()?:0
        ) else {
            println("WRONG line in $pathfile")
            null
        }
    }
    return ret
}
