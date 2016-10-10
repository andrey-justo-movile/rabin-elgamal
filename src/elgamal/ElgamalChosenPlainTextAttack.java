package elgamal;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class ElgamalChosenPlainTextAttack {

    private static Set<BigInteger> cipheredMessages = new HashSet<>();
    private static String choosenText = "Hello world!";
    private static Long attempts = 30L;
    
    @SuppressWarnings("unused")
    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println("Choosen Text: " + choosenText);

        for (int i = 0; i < attempts; i++) {
            BigInteger secretKey = null;
            if (args.length > 1) {
                secretKey =  new BigInteger(args[0]);
            } else {
                secretKey =  new BigInteger("1234567890"); // TODO: choose better default key
            }

            BigInteger[] key = Elgamal.genKey(secretKey, 3072);
            BigInteger p = key[0];
            BigInteger b = key[1];
            BigInteger c = key[2];

            BigInteger message = new BigInteger(choosenText.getBytes(Charset.forName("ascii")));
            BigInteger[] cipherText = Elgamal.encrypt(message, c, b, p, 3072);

            BigInteger EC = cipherText[0];
            BigInteger h = cipherText[1];
            
            BigInteger decryptedMessage = Elgamal.decrypt(EC, h, secretKey, p);
            String decryptedText = new String(decryptedMessage.toByteArray(), Charset.forName("ascii"));

            cipheredMessages.add(EC);
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
