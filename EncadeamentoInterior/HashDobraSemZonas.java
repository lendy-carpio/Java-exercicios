package EncadeamentoInterior;
//Lendy Naiara Carpio Pacheco - 10428525

public class HashDobraSemZonas {
    private static class Entrada {
        long chave;
        String valor;
        int proximo; // Índice do próximo elemento na cadeia

        Entrada(long chave, String valor) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = -1;
        }
    }

    private Entrada[] tabela;
    private int tamanho;

    public HashDobraSemZonas(int tamanho) {
        this.tamanho = tamanho;
        this.tabela = new Entrada[tamanho];
    }

    // Função de dispersão pelo Método da Dobra (Página 18: chaves de 6 dígitos, endereço de 2 dígitos)
    // Adaptado para aceitar qualquer chave numérica e produzir um índice válido para a tabela
    private int hash(long chave) {
        String strChave = String.valueOf(Math.abs(chave));
        // Preenche com zeros à esquerda se necessário (ex: 123 -> 000123)
        while (strChave.length() < 6) {
            strChave = "0" + strChave;
        }
        
        // Pega os últimos 6 dígitos se for maior
        if (strChave.length() > 6) {
            strChave = strChave.substring(strChave.length() - 6);
        }

        // Realiza a dobra: divide em 3 partes de 2 dígitos
        int p1 = Integer.parseInt(strChave.substring(0, 2));
        int p2 = Integer.parseInt(strChave.substring(2, 4));
        int p3 = Integer.parseInt(strChave.substring(4, 6));

        // Soma sem vai-um (módulo 10 dígito a dígito)
        int somaDezenas = (p1 / 10) + (p2 / 10) + (p3 / 10);
        int somaUnidades = (p1 % 10) + (p2 % 10) + (p3 % 10);
        
        int endereco2Digitos = ((somaDezenas % 10) * 10) + (somaUnidades % 10);
        
        // Ajusta o endereço de 2 dígitos (0-99) para o tamanho real da tabela
        return endereco2Digitos % tamanho; 
    }

    // Inserção com Encadeamento Interior (sem separação de zona)
    public void put(long chave, String valor) {
        int posBase = hash(chave);

        // 1. Tenta inserir na posição base
        if (tabela[posBase] == null) {
            tabela[posBase] = new Entrada(chave, valor);
            return;
        }

        // 2. Atualiza se a chave já existir
        if (tabela[posBase].chave == chave) {
            tabela[posBase].valor = valor;
            return;
        }

        // 3. Colisão: Procurar o próximo slot vazio na tabela (qualquer posição)
        int posLivre = -1;
        for (int i = 1; i < tamanho; i++) {
            int posCandidata = (posBase + i) % tamanho;
            if (tabela[posCandidata] == null) {
                posLivre = posCandidata;
                break;
            }
        }

        if (posLivre != -1) {
            // Insere no slot livre
            tabela[posLivre] = new Entrada(chave, valor);

            // Liga o fim da cadeia a este novo slot
            int atual = posBase;
            while (tabela[atual].proximo != -1) {
                atual = tabela[atual].proximo;
            }
            tabela[atual].proximo = posLivre;
        } else {
            System.out.println("Erro: Tabela cheia!");
        }
    }

    // Busca
    public String get(long chave) {
        int posBase = hash(chave);
        
        int atual = posBase;
        while (atual != -1) {
            Entrada e = tabela[atual];
            if (e != null && e.chave == chave) {
                return e.valor;
            }
            atual = (e != null) ? e.proximo : -1;
        }
        return null;
    }

    public static void main(String[] args) {
        HashDobraSemZonas hash = new HashDobraSemZonas(10);
        
        hash.put(813459, "Chave do exemplo");
        hash.put(123456, "Outra chave");
        hash.put(654321, "Mais uma");
        
        System.out.println("Busca 813459: " + hash.get(813459));
        System.out.println("Busca 123456: " + hash.get(123456));
        System.out.println("Busca 999999: " + hash.get(999999)); // null
    }
}