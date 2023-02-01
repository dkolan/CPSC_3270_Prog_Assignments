import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

class CollectData {
    private static void CollectData(int limit) {
        try {
            FileWriter fw = new FileWriter("data_"+limit+".csv");
            fw.append("n,T(n)/sqrt(n),T(n)/nsqrt(lg(n)),T(n)/nln(n)\n");

            int[]  G = IntStream.generate(() -> new Random().nextInt(Integer.MAX_VALUE)).limit(limit + 1).toArray();

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

    public static void Merge(int[] A, int p, int q, int r) {
        int n1 = q - p + 1;
        int n2 = r - q;
        int[] L = new int[n1 + 1 + 1];
        int[] R = new int[n2 + 1 + 1];
        for (int i = 1; i <= n1; i++) {
            L[i] = A[p + i - 1];
        }
        for (int j = 1; j <= n2; j++) {
            R[j] = A[q + j];
        }
        L[n1 + 1] = Integer.MAX_VALUE;
        R[n2 + 1] = Integer.MAX_VALUE;
        int i = 1;
        int j = 1;
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

    public static double log2(int N) {
        return (Math.log(N) / Math.log(2));
    }

    public static String cowSay(String message) {
        String topTextBoxChar = "_";
        String bottomTextBoxChar = "-";
        int textBoxMultiplier = message.length() + 4;

        String topTextBox = multiplyString(topTextBoxChar, textBoxMultiplier);
        String bottomTextBox = multiplyString(bottomTextBoxChar, textBoxMultiplier);

        return topTextBox + "\n"
            + "< " + message + " >\n"
            + bottomTextBox + "\n"
            + "        \\   ^__^\n"
            + "         \\  (oo)\\_______\n"
            + "            (__)\\       )\\/\\\n"
            + "                ||----w |\n"
            + "                ||     ||\n";
    }

    public static String multiplyString(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static Integer tryParsePositiveInt(String value) {
        try {
            Integer i = Integer.parseInt(value);
            if (i <= 0) {
                return -1;
            } else {
                return i;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void printOptions() {
        System.out.println("Please choose a from the following options (enter a number):");
        System.out.println("1 - Demonstrate Merge-Sort by sorting a random 10 element array");
        System.out.println("2 - Enter an array size to pass to CollectData");
        System.out.println("3 - Exit");
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        while (true) {
            printOptions();
            System.out.print("Option: ");
            int userChoice = Integer.parseInt(s.nextLine());

            switch (userChoice) {
                case 1:
                    System.out.println("\nGenerating a random array to demonstrate a successful implementation of Merge-Sort...");
                    int[]  G = IntStream.generate(() -> new Random().nextInt(10)).limit(11).toArray();
                    System.out.println("Unsorted array = " + Arrays.toString(Arrays.copyOfRange(G, 1, 11)));
                    MergeSort(G, 1, 10);
                    System.out.println("Sorted array = " + Arrays.toString(Arrays.copyOfRange(G, 1, 11)) + "\n");
                    break;
                case 2:
                    while(true) {
                        System.out.println("\nEnter the integer array size (min 5000) to pass to CollectData: ");
                        System.out.print("Array size: ");

                        String userInput = s.nextLine();

                        if (tryParsePositiveInt(userInput) != null && tryParsePositiveInt(userInput) >= 5000) {
                            int arraySize = tryParsePositiveInt(userInput);
                            System.out.println("Running CollectData...");
                            CollectData(arraySize);
                            System.out.print(cowSay("CollectData finished!"));
                            break;
                        } if (tryParsePositiveInt(userInput) == null || tryParsePositiveInt(userInput) == -1) {
                            System.out.println("Invalid input. Enter another integer number.");
                            continue;
                        } if (tryParsePositiveInt(userInput) < 5000) {
                            System.out.println("Enter an integer number greater than or equal to 5000.");
                            continue;
                        } else {
                            System.out.println("Unrecognized input. Please try again");
                            continue;
                        }
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        }
    }
}
