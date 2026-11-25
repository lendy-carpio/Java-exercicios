package BloomFiltersECountMinSketch;
//Lendy Naiara Carpio Pacheco - 10428525

public class CountMinSketch {
    private int[][] table;
    private int numHashes; // k (linhas)
    private int width;     // w (colunas)
    private int[] hashSeeds; // Sementes para gerar hashes diferentes

    // Construtor: define as dimensões da grade
    public CountMinSketch(int numHashes, int width) {
        this.numHashes = numHashes;
        this.width = width;
        this.table = new int[numHashes][width];
        this.hashSeeds = new int[numHashes];

        // Inicializa as sementes para as k funções hash
        for (int i = 0; i < numHashes; i++) {
            hashSeeds[i] = i * 31 + 7; // Apenas uma semente arbitrária
        }
    }

    // Calcula o índice na coluna para uma linha específica
    private int getIndex(String item, int row) {
        int hash = item.hashCode() ^ hashSeeds[row];
        return Math.abs(hash % width);
    }

    // Inserir um item (incrementa os contadores)
    public void insert(String item) {
        for (int i = 0; i < numHashes; i++) {
            int col = getIndex(item, i);
            table[i][col]++;
        }
    }

    // Consultar a frequência estimada de um item
    public int query(String item) {
        int minFrequency = Integer.MAX_VALUE;
        
        for (int i = 0; i < numHashes; i++) {
            int col = getIndex(item, i);
            // Pega o menor valor entre as linhas (min seleciona a linha "mais limpa")
            minFrequency = Math.min(minFrequency, table[i][col]);
        }
        
        return minFrequency;
    }

    public static void main(String[] args) {
        // Exemplo: 3 linhas (k) e 10 colunas (w)
        CountMinSketch cms = new CountMinSketch(3, 10);

        cms.insert("gato");
        cms.insert("gato");
        cms.insert("gato");
        cms.insert("pato");
        cms.insert("pato");

        System.out.println("Frequência de 'gato' (esperado 3): " + cms.query("gato"));
        System.out.println("Frequência de 'pato' (esperado 2): " + cms.query("pato"));
        System.out.println("Frequência de 'cachorro' (esperado 0): " + cms.query("cachorro"));
        
        // Nota: Devido a colisões, um item não inserido pode retornar > 0,
        // e um item inserido pode retornar > frequência real.
    }
}
