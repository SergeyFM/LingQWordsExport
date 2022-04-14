import java.io.File
import java.lang.Exception

// Saves wordlist into a text file
// The file contains following fields:
// * Word
// * Translation
// * Example
// * Audio
// * Image


fun saveToAnkiFile(data: List<Word>, pathfile: String): String {
    // saves words to a text file
    
    if(data.isEmpty()) return "Nothing"
    try {
        val the_file = File(pathfile)
        the_file.printWriter().use {out->
            data.forEach {w->
                val the_line = with(w){
                        ""   + word +
                        "\t" + translation +
                        "\t" + fragment +
                        "\t [sound:" + wordToFilename(word,".mp3") + "]" +
                        "\t <img src='" + wordToFilename(word,".jpeg") + "'>"
                }
                out.println(the_line)
            }
        }
    } catch (ex: Exception) {
        println("ERROR " + ex.message)
        return "ERROR"
    }
    return "OK"
    
}