import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RabinChosenPlainTextAttack {

    private static Set<BigInteger> cipheredMessages = new HashSet<>();
    private static String choosenText = "Hello world!";
    private static Long attempts = 1000L;
    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println("Choosen Text: " + choosenText);

        for (int i = 0; i < attempts; i++) {
            List<String> decryptedText = new ArrayList<>(); // four possibilities

            BigInteger[] key = Rabin.genKey(3072);
            BigInteger N = key[0];
            BigInteger p = key[1]; // private key
            BigInteger q = key[2]; // private key

            BigInteger message = new BigInteger(choosenText.getBytes(Charset.forName("ascii")));
            BigInteger cipherText = Rabin.encrypt(message, N);

            BigInteger[] decryptedMessage = Rabin.decrypt(cipherText, p, q);
            for(BigInteger factors: decryptedMessage) {
                decryptedText.add(new String(factors.toByteArray(), Charset.forName("ascii")));
            }

            cipheredMessages.add(cipherText);
        }

        if (cipheredMessages.size() == 1) {
            System.out.println("Not probabilistic method. ");
            System.out.println("Always returning same: " + cipheredMessages.toArray()[0]);
        } else {
            System.out.println("Probabilistic method");
            System.out.println("Returning different ciphered messages: ");
            cipheredMessages.forEach(cipherM -> {
                System.out.println(cipherM);
            });
        }

    }

}
