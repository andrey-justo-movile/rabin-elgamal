package rabin;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RabinTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String originalText = "Hello World!!!";
        List<String> decryptedText = new ArrayList<>(); // four possibilities
        
        BigInteger[] key = Rabin.genKey(3072);
        BigInteger N = key[0];
        BigInteger p = key[1]; // private key
        BigInteger q = key[2]; // private key

        BigInteger message = new BigInteger(originalText.getBytes(Charset.forName("ascii")));
        BigInteger cipherText = Rabin.encrypt(message, N);

        BigInteger[] decryptedMessage = Rabin.decrypt(cipherText, p, q);
        for(BigInteger factors: decryptedMessage) {
            decryptedText.add(new String(factors.toByteArray(), Charset.forName("ascii")));
        }
        
        System.out.println("For ->");
        System.out.println("Private keys -> p: " + p);
        System.out.println("Private keys -> q: " + q);
        System.out.println("Public key -> N: " + N);
        System.out.println("Original Text: " + originalText);
        System.out.println("Encrypted Text: " + cipherText);
        System.out.println("Results:");
        System.out.println("----------------------------");
        decryptedText.forEach( d -> {
            System.out.println("Decrypted Text: " + d);
        });
    }
}