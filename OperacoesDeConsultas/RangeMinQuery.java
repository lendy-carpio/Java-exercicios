package OperacoesDeConsultas;
//Lendy Naiara Carpio Pacheco - 10428525

public class RangeMinQuery {
    private int[][] st;
    private int[] log2;

    // Construtor: pré-processa a Sparse Table em O(n log n)
    public RangeMinQuery(int[] array) {
        int n = array.length;
        // O número máximo de colunas é log2(n) + 1
        int k = (int) (Math.log(n) / Math.log(2)) + 1;
        st = new int[n][k];

        // Inicializa a primeira coluna (intervalos de tamanho 1: 2^0 = 1)
        for (int i = 0; i < n; i++) {
            st[i][0] = array[i];
        }

        // Constrói a tabela para os outros tamanhos (potências de 2)
        for (int j = 1; j < k; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }

        // Pré-calcula os logaritmos para consultas O(1)
        log2 = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i / 2] + 1;
        }
    }

    // Consulta o mínimo no intervalo [L, R] em O(1)
    public int min(int L, int R) {
        if (L < 0 || R >= st.length || L > R) {
            throw new IllegalArgumentException("Intervalo inválido");
        }
        int length = R - L + 1;
        int j = log2[length]; // Maior potência de 2 que cabe no intervalo
        
        // O intervalo é coberto por duas partes de tamanho 2^j
        return Math.min(st[L][j], st[R - (1 << j) + 1][j]);
    }

    public static void main(String[] args) {
        int[] vetor = {1, 3, 8, 4, 6, 1, 4, 2}; // Exemplo da página 19
        RangeMinQuery rmq = new RangeMinQuery(vetor);

        System.out.println("Vetor original: [1, 3, 8, 4, 6, 1, 4, 2]");
        System.out.println("Mínimo(0, 7): " + rmq.min(0, 7)); // Esperado: 1
        System.out.println("Mínimo(2, 4): " + rmq.min(2, 4)); // Esperado: 4
        System.out.println("Mínimo(5, 6): " + rmq.min(5, 6)); // Esperado: 1
        System.out.println("Mínimo(3, 3): " + rmq.min(3, 3)); // Esperado: 4
    }
}
