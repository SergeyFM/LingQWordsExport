import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

// Connector to LingQ responsible for all communications

data class Connection(
    val connectionString: String,
    val APIKey: String
)

data class Word (
    val word: String,           // the word
    val translation: String,    // a translation
    val fragment: String,       // an example sentence, fragment
    val status: Int,            // status 0-3, 3 is known
    val numOfWords: Int)        // numbers of words in the word (more than one means a sentence)
{
    override fun toString() = this::class.java.declaredFields.filter{it.name!="INSTANCE"}
        .map{it.get(this).toString()}.joinToString("\t")
}

fun isConnectionOK(cnn: Connection): Boolean {
        try {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .timeout(Duration.ofSeconds(10))
            .uri(URI.create(cnn.connectionString+"v2/contexts/"))
            .header("Authorization",  "Token " + cnn.APIKey)
            .build();
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val ret = response.body()
        return "knownWords" in ret
        } catch(ex: Exception) {
            println("ERROR: " + ex)
            return false
        }
    }

fun getListOfLanguages(cnn: Connection): List<String> {
    // returns the list of languages with amount of known words
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(cnn.connectionString+"v2/contexts/"))
        .header("Authorization",  "Token " + cnn.APIKey)
        .build();
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    val ret = response.body()
    return ret.split("{").filter{"knownWords" in it}
        .map{line->
            "id:" + getJSONproperty("id",line) + " code:" +
            getJSONproperty("code",line) + " \t" +
            getJSONproperty("title",line) + " (" +
            getJSONproperty("knownWords", line) + ")"
    }
}

fun getListOfWords(cnn: Connection, lang_code: String, pages_limit: Int): List<Word> {
    // returns the list of words from LingQ: Word, Translation, Status
    if(pages_limit<1) return listOf<Word>()
    val words = mutableListOf<Word>()
    val client = HttpClient.newBuilder().build()
    for(page in (1..pages_limit)) {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(cnn.connectionString+"v2/$lang_code/cards/?page_size=25&page=$page"))
            .header("Authorization","Token "+cnn.APIKey)
            .header("Content-Type", "application/json; charset=utf-8")
            .build()
        val response = client.send(request,HttpResponse.BodyHandlers.ofString())
        val ret = response.body()
        if("""{"detail":"Invalid page."}""" in ret || ret.length<30) break
        print("|$page" + if(page%23==0) "\n" else "")
        ret.replace("\t"," ").split("\"pk\":").filter{"text" in it}.forEach {w_def->
            val term = unEscapeUnicode(getJSONproperty("term",w_def)).trim()
            val new_word = Word(
                term,
                unEscapeUnicode(getJSONproperty("text",w_def)),
                unEscapeUnicode(getJSONproperty("fragment",w_def)),
                getJSONproperty("status",w_def).toIntOrNull()?:0,
                term.count{it==' '}+1
            )
            if(new_word.word.length>0) words += new_word
        }
    }
    print("|")
    return words
}

fun transformWords(words: List<Word>): List<Word> {
    // take the words from example sentences, for LingQ is case-insensitive
    var counter = 0
    val transformed_words = words.map {word->
        val word_to_try = replaceSomeCharacters(word.word)
        val new_word = takeWordFromSentence(word_to_try,word.fragment)
        if(word.word!=new_word) counter++
        Word(new_word,word.translation,word.fragment,word.status,word.numOfWords)
    }
    println(" $counter changed. ")
    return transformed_words
}

private fun takeWordFromSentence(w:String, s:String): String {
    // finds case-insensitive w_ord in the s_entence, gets it from there, or returns w_ord
    val ind = s.indexOf(w,0,true)
    if(ind<0) return w
    for(i in (ind-1 downTo 0)) when(s[i]) {
        ' ' -> continue
        '.' -> return w
        else -> break
    }
    return s.substring(ind..w.length+ind-1)
}

private fun replaceSomeCharacters(w:String): String {
    val to_replace = mapOf("ﬂ" to "fl", "%" to "")
    return w.map {c->
        to_replace["$c"]?:"$c"
    }.joinToString("")
}

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
