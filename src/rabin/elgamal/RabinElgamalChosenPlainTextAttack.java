package rabin.elgamal;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RabinElgamalChosenPlainTextAttack {

    private static Set<String> cipheredMessages = new HashSet<>();
    private static String choosenText = "Hello world!";
    private static Long attempts = 30L;
    
    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println("Choosen Text: " + choosenText);
        List<String> decryptedText = new ArrayList<>(); // four possibilities

        for (int i = 0; i < attempts; i++) {
            BigInteger secretKey = null;
            if (args.length > 1) {
                secretKey =  new BigInteger(args[0]);
            } else {
                secretKey =  new BigInteger("1234567890"); // TODO: choose better default key
            }

            BigInteger[] key = RabinElgamal.genKey(secretKey, 3072);
            BigInteger p = key[0];
            BigInteger b = key[1];
            BigInteger c = key[2];
            BigInteger N = key[3];
            BigInteger pRabin = key[4]; // private key
            BigInteger qRabin = key[5]; // private key

            BigInteger message = new BigInteger(choosenText.getBytes(Charset.forName("ascii")));
            BigInteger[] cipherText = RabinElgamal.encrypt(message, c, b, p, N, 3072);

            BigInteger EC = cipherText[0];
            BigInteger h = cipherText[1];
            
            BigInteger[] decryptedMessage = RabinElgamal.decrypt(EC, h, secretKey, p, pRabin, qRabin);
            for(BigInteger factors: decryptedMessage) {
                decryptedText.add(new String(factors.toByteArray(), Charset.forName("ascii")));
            }

            cipheredMessages.add(EC.toString().trim());
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
