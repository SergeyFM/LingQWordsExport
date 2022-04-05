import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class tests {

    @Test
    fun unEscapeUnicode() {
        val actual = unEscapeUnicode("ausgefr\\u00e4stem")
        Assertions.assertEquals("ausgefrästem",actual)
    }

    @Test
    internal fun testOfTheTest() {
        Assertions.assertEquals(1,1)
    }
    /*
    @Test
    @Disabled
    fun takeWordFromSentence() {
        with(Connector) {
            var word = "ladentüren"
            var sentence = "trotz geschlossener Ladentüren weiter Bücher verkauft"
            var new_word = takeWordFromSentence(word,sentence)
            Assertions.assertEquals("Ladentüren",new_word,"when in the middle of the string")
    
            //sentence = "Ladentüren trotz geschlossener  weiter Bücher verkauft"
            //new_word = takeWordFromSentence(word,sentence)
            //Assertions.assertEquals("ladentüren",new_word,"at the beginning")
        
            sentence = "trotz geschlossener. Ladentüren weiter Bücher verkauft"
            new_word = takeWordFromSentence(word,sentence)
            Assertions.assertEquals("ladentüren",new_word,"after a dot")
     
            word = "mit füßen getreten"
            sentence = "dass das internationale System mit Füßen getreten wird"
            new_word = takeWordFromSentence(word,sentence)
            Assertions.assertEquals("mit Füßen getreten",new_word,"sentence in the middle")
        
            word = "wellblechfassade"
            sentence = "corrugated iron facade\tEine Festung mit Wellblechfassade-"
            new_word = takeWordFromSentence(word,sentence)
            Assertions.assertEquals("Wellblechfassade",new_word,"word with - at the end")
        }
    }
    
     */
}