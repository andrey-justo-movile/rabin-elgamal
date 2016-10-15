package rabin.elgamal;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RabinElgamalTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String originalText = "Hello world!";
        List<String> decryptedText = new ArrayList<>(); // four possibilities
        
        BigInteger secretKey = null;
        if (args.length > 1) {
            secretKey =  new BigInteger(args[0]);
        } else {
            secretKey =  new BigInteger("1234567890"); // TODO: choose better default key
        }
        
        BigInteger[] key = RabinElgamal.genKey(secretKey, 3072);
        BigInteger h = key[0];
        BigInteger g = key[1];
        BigInteger r = key[2];

        BigInteger message = new BigInteger(originalText.getBytes(Charset.forName("ascii")));
        BigInteger[] cipherText = RabinElgamal.encrypt(message, h, g, r, 3072);

        BigInteger decryptedMessage = RabinElgamal.decrypt(cipherText[0], cipherText[1], secretKey, r);
        decryptedText.add(new String(decryptedMessage.toByteArray(), Charset.forName("ascii")));
        
        System.out.println("For ->");
        System.out.println("Public keys -> p: " + h);
        System.out.println("Public keys -> q: " + g);
        System.out.println("Public key -> N: " + r);
        System.out.println("Original Text: " + originalText);
        System.out.println("Encrypted Text Factor: " + cipherText[0]);
        System.out.println("Encrypted Text: " + cipherText[1]);
        System.out.println("Results:");
        System.out.println("----------------------------");
        decryptedText.forEach( d -> {
            System.out.println("Decrypted Text: " + d);
        });
    }
}