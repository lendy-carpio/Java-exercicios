
import java.util.Scanner;

public class GrupoEstudos {
    static int[] parent;
    static int[] size;

    // Inicializa cada elemento como seu próprio grupo de tamanho 1
    static void makeSet(int n) {
        parent = new int[n + 1];
        size = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    // Encontra a raiz com compressão de caminho
    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // Une os conjuntos de A e B (Weighted Quick-Union)
    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA == rootB) return;

        // Liga a árvore menor na maior
        if (size[rootA] < size[rootB]) {
            parent[rootA] = rootB;
            size[rootB] += size[rootA];
        } else {
            parent[rootB] = rootA;
            size[rootA] += size[rootB];
        }
    }

    // Verifica se A e B pertencem ao mesmo grupo
    static boolean same(int a, int b) {
        return find(a) == find(b);
    }

    // Retorna o tamanho do grupo ao qual A pertence
    static int getSize(int a) {
        return size[find(a)];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); // Número de usuários
        int Q = sc.nextInt(); // Número de comandos
        sc.nextLine(); // Consumir quebra de linha
        GrupoEstudos gp = new GrupoEstudos();
        gp.makeSet(N);

        for (int i = 0; i < Q; i++) {
            String comando = sc.next();
            if (comando.equals("UNION")) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                gp.union(a, b);
            } else if (comando.equals("SAME")) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                System.out.println(gp.same(a, b) ? "SIM" : "NAO");
            } else if (comando.equals("SIZE")) {
                int a = sc.nextInt();
                System.out.println(gp.getSize(a));
            }
        }
        sc.close();
    }
}