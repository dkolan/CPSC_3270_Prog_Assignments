import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

class CollectData {

    static double ComputeSumPowersFunction(double a, double x, int n) {
        double sum = 0;
        double prod = 1;
        for (int i = 1; i <= n; i++) {
            sum += prod;
            prod *= x;
        }
        return a * sum;
    }

    private static void CollectData(String[] args) {
        try {
            FileWriter fw = new FileWriter("data.csv");
            fw.append("n,T(n)/n*sqrt(n),T(n)/n**2,T(n)/nlog(n)\n");

            // Create Array G of a huge length, filled with random values, perfect opportunity for streams/anon functions
            // I tested G.length of MAX_INT, 1 billion, and 500 million. First two ran out of heap space
            int[]  G = IntStream.generate(() -> new Random().nextInt(Integer.MAX_VALUE)).limit(250000).toArray();

            int[] A = new int[G.length];

            for (int n = 5000; n <= G.length ; n += 1500) {
                System.arraycopy(G,0,A,0,n);
                long startTime = System.nanoTime();
                MergeSort(A,0,n-1);
                long timeElapsed = System.nanoTime() - startTime;

                double nRootN = (double) timeElapsed / Math.sqrt(n);
                double nSquared = (double) timeElapsed / (n * Math.sqrt(log2(n)));
                double nLogN = (double) timeElapsed / (n * Math.log(n));

                String csvLine = String.format(
                    "%s,%s,%s,%s\n",
                    n,
                    nRootN,
                    nSquared,
                    nLogN
                );
                fw.append(csvLine);
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println("An exception occurred writing to the CSV file.");
            e.printStackTrace();
        }
    }

    private static void MergeSort(int[] A, int p, int r) {
        if (p < r) {
            int q = (int) Math.floor((p + r ) / 2);
            MergeSort(A, p, q);
            MergeSort(A, q + 1, r);
            Merge(A, p, q, r);
        }
    }

    private static void Merge(int[] A, int p, int q, int r) {
        int n1 = q - p + 1;
        int n2 = r - q;
        int[] L = new int[n1 + 1];
        int[] R = new int[n2 + 1];
        for (int i = 0; i < n1; i++) {
            L[i] = A[p + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = A[q + j + 1];
        }
        L[n1] = Integer.MAX_VALUE;
        R[n2] = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;
        for (int k = p; k <= r; k++) {
            if (L[i] <= R[j]) {
                A[k] = L[i];
                i++;
            } else {
                A[k] = R[j];
                j++;
            }
        }
    }

    public static int log2(int N) {
        int result = (int)(Math.log(N) / Math.log(2));
        return result;
    }

    public static String cowSay(String message) {
        String topTextBoxChar = "_";
        String bottomTextBoxChar = "-";
        int textBoxMultiplier = message.length() + 4;

        String topTextBox = topTextBoxChar.repeat(textBoxMultiplier);
        String bottomTextBox = bottomTextBoxChar.repeat(textBoxMultiplier);

        String out = topTextBox + "\n"
            + "< " + message + " >\n"
            + bottomTextBox + "\n"
            + "        \\   ^__^\n"
            + "         \\  (oo)\\_______\n"
            + "            (__)\\       )\\/\\\n"
            + "                ||----w |\n"
            + "                ||     ||\n";
        return out;
    }

    public static void main(String[] args) {
        System.out.println("Running CollectData...");
        CollectData(args);
        System.out.println(cowSay("CollectData finished!"));
    }
}
