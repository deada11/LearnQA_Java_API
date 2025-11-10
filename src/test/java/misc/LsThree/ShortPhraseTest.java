//package misc.LsThree;
//
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class ShortPhraseTest {
//
//    @ParameterizedTest
//    @ValueSource(strings = {
//            "",
//            "Short string",
//            "14 bound value",
//            "15 bound value!",
//            "16 bound value!!",
//            "This string contains 32 symbols"
//    })
//    public void phraseTest(@NotNull String string){
//
//        int expectedLength = 15;
//        int actualLength = string.length();
//
//        assertTrue(actualLength > expectedLength, "Actual length less or equal than expected." +
//                "Current length is " + actualLength + ", but expected is " + expectedLength);
//    }
//}
