package ArvoreBMais;
//Lendy Naiara Carpio Pacheco - 10428525

import java.util.ArrayList;
import java.util.List;

public class ArvoreBPlus {

    // Ordem da árvore (d)
    private int d;
    // Número máximo de chaves por nó (2d)
    private int maxKeys;
    private No raiz;

    // Classe interna para representar um Nó (Página)
    private class No {
        boolean folha;          // Indica se é folha ou página interna
        List<Integer> chaves;   // Chaves armazenadas no nó
        List<No> filhos;        // Ponteiros para os filhos (se for página interna)
        No proximo;             // Ponteiro para a próxima folha (apenas se for folha)

        public No(boolean folha) {
            this.folha = folha;
            this.chaves = new ArrayList<>();
            this.filhos = new ArrayList<>();
            this.proximo = null;
        }

        // Verifica se o nó está cheio (overflow)
        public boolean isCheio() {
            return chaves.size() > maxKeys;
        }
    }

    public ArvoreBPlus(int d) {
        this.d = d;
        this.maxKeys = 2 * d;
        this.raiz = new No(true); // Inicialmente, a raiz é uma folha vazia
    }

    // ==========================================
    // 1. BUSCA (Necessária para a inserção)
    // ==========================================
    // Encontra a folha onde a chave deveria estar ou está
    private No encontrarFolha(int chave) {
        No atual = raiz;
        while (!atual.folha) {
            int i = 0;
            // Avança enquanto a chave for maior que a chave atual do nó
            while (i < atual.chaves.size() && chave >= atual.chaves.get(i)) {
                i++;
            }
            atual = atual.filhos.get(i);
        }
        return atual;
    }

    // ==========================================
    // 2. INSERÇÃO
    // ==========================================
    public void inserir(int chave) {
        No folha = encontrarFolha(chave);

        // Insere a chave na folha mantendo a ordem
        int pos = 0;
        while (pos < folha.chaves.size() && chave > folha.chaves.get(pos)) {
            pos++;
        }
        folha.chaves.add(pos, chave);

        // Se a folha estourou a capacidade (overflow)
        if (folha.isCheio()) {
            dividirFolha(folha);
        }
    }

    private void dividirFolha(No folha) {
        int meio = folha.chaves.size() / 2;
        
        // Cria a nova folha (direita)
        No novaFolha = new No(true);
        // A nova folha recebe a metade da direita das chaves
        novaFolha.chaves.addAll(folha.chaves.subList(meio, folha.chaves.size()));
        // A folha original (esquerda) mantém a metade da esquerda
        folha.chaves.subList(meio, folha.chaves.size()).clear();

        // Encadeia as folhas (ponteiro para leitura sequencial)
        novaFolha.proximo = folha.proximo;
        folha.proximo = novaFolha;

        // A chave do meio sobe para o pai (CÓPIA, a chave fica na folha também)
        int chaveMeio = novaFolha.chaves.get(0);
        inserirNoPai(folha, chaveMeio, novaFolha);
    }

    private void inserirNoPai(No filhoEsquerdo, int chave, No filhoDireito) {
        // Se o filho é a raiz, precisamos criar uma nova raiz
        if (filhoEsquerdo == raiz) {
            No novaRaiz = new No(false);
            novaRaiz.chaves.add(chave);
            novaRaiz.filhos.add(filhoEsquerdo);
            novaRaiz.filhos.add(filhoDireito);
            raiz = novaRaiz;
            return;
        }

        // Encontra o pai do filhoEsquerdo
        No pai = encontrarPai(raiz, filhoEsquerdo);

        // Insere a chave e o novo ponteiro no pai
        int pos = 0;
        while (pos < pai.chaves.size() && chave > pai.chaves.get(pos)) {
            pos++;
        }
        pai.chaves.add(pos, chave);
        pai.filhos.add(pos + 1, filhoDireito);

        // Se o pai estourou, precisamos dividir o pai também
        if (pai.isCheio()) {
            dividirNoInterno(pai);
        }
    }

    // Encontra o nó pai de um determinado filho (busca recursiva)
    private No encontrarPai(No atual, No filho) {
        if (atual.folha) return null;
        for (No f : atual.filhos) {
            if (f == filho) return atual;
            No pai = encontrarPai(f, filho);
            if (pai != null) return pai;
        }
        return null;
    }

    private void dividirNoInterno(No no) {
        int meio = no.chaves.size() / 2;
        
        // A chave do meio sobe para o pai (na página interna, ela NÃO fica)
        int chaveMeio = no.chaves.get(meio);

        // Cria o novo nó interno (direita)
        No novoNo = new No(false);
        novoNo.chaves.addAll(no.chaves.subList(meio + 1, no.chaves.size()));
        novoNo.filhos.addAll(no.filhos.subList(meio + 1, no.filhos.size()));

        // Ajusta o nó original (esquerda)
        no.chaves.subList(meio, no.chaves.size()).clear();
        no.filhos.subList(meio + 1, no.filhos.size()).clear();

        // Sobe a chave para o pai
        inserirNoPai(no, chaveMeio, novoNo);
    }

    // ==========================================
    // 3. IMPRESSÃO DA ÁRVORE (Visualização)
    // ==========================================
    public void imprimirArvore() {
        System.out.println("\n--- Estrutura da Árvore B+ (Ordem d=" + d + ") ---");
        imprimirNo(raiz, 0);
        
        // Imprime as folhas encadeadas
        System.out.print("\nFolhas encadeadas: ");
        No folha = raiz;
        while (!folha.folha) {
            folha = folha.filhos.get(0);
        }
        while (folha != null) {
            System.out.print(folha.chaves + " -> ");
            folha = folha.proximo;
        }
        System.out.println("null");
    }

    private void imprimirNo(No no, int nivel) {
        if (no == null) return;
        for (int i = 0; i < nivel; i++) System.out.print("    ");
        
        if (no.folha) {
            System.out.println("[Folha] " + no.chaves);
        } else {
            System.out.println("[Interno] " + no.chaves);
            for (No filho : no.filhos) {
                imprimirNo(filho, nivel + 1);
            }
        }
    }

    // ==========================================
    // MAIN (Exercício da página 14 do PDF)
    // ==========================================
    public static void main(String[] args) {
        // Exercício: Ordem 4 (d=4, max=8) e os valores: 2, 3, 5, 7, 11, 17, 19, 23, 29, 31
        // Nota: Com d=4, o nó precisa ter entre 4 e 8 chaves. 
        // Ao inserir a 5ª chave, haverá a primeira divisão (overflow).
        
        System.out.println("Inserindo os valores do Exercício (Ordem 4)...");
        ArvoreBPlus arvore = new ArvoreBPlus(4);
        
        int[] valores = {2, 3, 5, 7, 11, 17, 19, 23, 29, 31};
        
        for (int v : valores) {
            System.out.println("-> Inserindo: " + v);
            arvore.inserir(v);
            arvore.imprimirArvore();
        }
    }
}
