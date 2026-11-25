package OperacoesDeConsultas;
//Lendy Naiara Carpio Pacheco - 10428525

public class RangeSumQuery {
    private int[] prefixSum;

    // Construtor: pré-processa o vetor em O(n)
    public RangeSumQuery(int[] array) {
        int n = array.length;
        prefixSum = new int[n];
        
        if (n > 0) {
            prefixSum[0] = array[0];
            for (int i = 1; i < n; i++) {
                prefixSum[i] = prefixSum[i - 1] + array[i];
            }
        }
    }

    // Consulta a soma no intervalo [a, b] em O(1)
    public int sum(int a, int b) {
        if (a < 0 || b >= prefixSum.length || a > b) {
            throw new IllegalArgumentException("Intervalo inválido");
        }
        if (a == 0) {
            return prefixSum[b];
        }
        return prefixSum[b] - prefixSum[a - 1];
    }

    public static void main(String[] args) {
        int[] vetor = {3, 7, 5, 1, 2, 4, 0, 9};
        RangeSumQuery rsq = new RangeSumQuery(vetor);

        System.out.println("Vetor original: [3, 7, 5, 1, 2, 4, 0, 9]");
        System.out.println("Soma(3, 6): " + rsq.sum(3, 6)); // Esperado: 7
        System.out.println("Soma(0, 4): " + rsq.sum(0, 4)); // Esperado: 18
    }
} 
    

