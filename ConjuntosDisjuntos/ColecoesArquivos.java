
import java.util.Scanner;

public class ColecoesArquivos {
    static int[] parent;
    static int[] size;

    // Inicializa de 0 a N-1
    static void makeSet(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) return;

        if (size[rootA] < size[rootB]) {
            parent[rootA] = rootB;
            size[rootB] += size[rootA];
        } else {
            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }
    }

    static boolean same(int a, int b) {
        return find(a) == find(b);
    }

    static int getSize(int a) {
        return size[find(a)];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); // Número de arquivos
        int Q = sc.nextInt(); // Número de comandos
        sc.nextLine();

        makeSet(N);

        for (int i = 0; i < Q; i++) {
            String comando = sc.next();
            if (comando.equals("MERGE")) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                union(a, b);
            } else if (comando.equals("SAME")) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                System.out.println(same(a, b) ? "SIM" : "NAO");
            } else if (comando.equals("COUNT")) {
                int a = sc.nextInt();
                System.out.println(getSize(a));
            }
        }
        sc.close();
    }
}