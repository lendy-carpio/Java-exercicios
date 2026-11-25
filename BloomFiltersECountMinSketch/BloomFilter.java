package BloomFiltersECountMinSketch;
//Lendy Naiara Carpio Pacheco - 10428525

import java.util.BitSet;

public class BloomFilter {
    private BitSet bitArray;
    private int size; // Tamanho do vetor de bits (m)
    private int numHashes; // Número de funções hash (k)

    // Construtor: define o tamanho e o número de hashes
    public BloomFilter(int size, int numHashes) {
        this.size = size;
        this.numHashes = numHashes;
        this.bitArray = new BitSet(size);
    }

    // Funções de hash simples (simulando h_1, h_2, ..., h_k)
    // Em produção, usaríamos funções como MurmurHash ou SHA-256
    private int[] getHashes(String item) {
        int[] hashes = new int[numHashes];
        int hash1 = item.hashCode();
        // Um segundo hash para simular independência (Double Hashing)
        int hash2 = (item + "salt").hashCode(); 

        for (int i = 0; i < numHashes; i++) {
            // Fórmula clássica para gerar k hashes a partir de 2
            int combinedHash = hash1 + i * hash2;
            // Garante que o índice seja positivo e dentro do tamanho
            hashes[i] = Math.abs(combinedHash % size); 
        }
        return hashes;
    }

    // Inserir um item no filtro
    public void insert(String item) {
        int[] hashes = getHashes(item);
        for (int hash : hashes) {
            bitArray.set(hash, true);
        }
    }

    // Consultar se um item provavelmente existe
    public boolean contains(String item) {
        int[] hashes = getHashes(item);
        for (int hash : hashes) {
            if (!bitArray.get(hash)) {
                return false; // 100% de certeza que NÃO está
            }
        }
        return true; // Provavelmente está (pode ser falso positivo)
    }

    public static void main(String[] args) {
        // Exemplo: vetor de 1000 bits e 3 funções hash
        BloomFilter bf = new BloomFilter(1000, 3);

        bf.insert("gato");
        bf.insert("cachorro");

        System.out.println("gato está no filtro? " + bf.contains("gato")); // true
        System.out.println("pato está no filtro? " + bf.contains("pato")); // false
        System.out.println("passaro está no filtro? " + bf.contains("passaro")); // false
    }
}
