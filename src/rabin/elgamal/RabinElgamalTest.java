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
        BigInteger p = key[0];
        BigInteger b = key[1];
        BigInteger c = key[2];
        BigInteger N = key[3];
        BigInteger pRabin = key[4]; // private key
        BigInteger qRabin = key[5]; // private key
        

        BigInteger message = new BigInteger(originalText.getBytes(Charset.forName("ascii")));
        BigInteger[] cipherText = RabinElgamal.encrypt(message, c, b, p, N, 3072);
        
        BigInteger EC = cipherText[0];
        BigInteger h = cipherText[1];

        BigInteger[] decryptedMessage = RabinElgamal.decrypt(EC, h, secretKey, p, pRabin, qRabin);
        for(BigInteger factors: decryptedMessage) {
            decryptedText.add(new String(factors.toByteArray(), Charset.forName("ascii")));
        }
        
        System.out.println("For ->");
        System.out.println("Public keys -> p: " + p);
        System.out.println("Public keys -> b: " + b);
        System.out.println("Public key -> c: " + c);
        System.out.println("Private keys -> pRabin: " + pRabin);
        System.out.println("Private keys -> qRabin: " + qRabin);
        System.out.println("Public key -> N: " + N);
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