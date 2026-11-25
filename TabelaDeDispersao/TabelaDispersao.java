package TabelaDeDispersao;
//Lendy Naiara Carpio Pacheco - 10428525

public class TabelaDispersao {

    // Definição dos métodos de sondagem
    public enum MetodoSondagem {
        LINEAR,
        QUADRATICA,
        DUPLA
    }

    private Integer[] tabela; // Usando Integer para permitir valores nulos
    private int tamanho;      // m (tamanho da tabela)
    private int elementosInseridos;
    private MetodoSondagem metodoAtual;

    // Constantes para a sondagem quadrática (conforme PDF página 18)
    private final int C1 = 1;
    private final int C2 = 1; 

    public TabelaDispersao(int tamanho, MetodoSondagem metodo) {
        this.tamanho = tamanho;
        this.tabela = new Integer[tamanho];
        this.elementosInseridos = 0;
        this.metodoAtual = metodo;
    }

    // Função de dispersão base para Strings (PDF página 4)
    // Usando o multiplicador 31, mas com módulo para caber na tabela
    private int hash(String s) {
        int h = 0;
        for (int i = 0; i < s.length(); i++) {
            h = (31 * h + s.charAt(i)) % tamanho;
        }
        return h;
    }

    // Função de dispersão secundária para a Dispersão Dupla
    // Deve ser diferente de zero e independente da primeira.
    // Usaremos um primo menor que o tamanho da tabela.
    private int hash2(String s) {
        int h = 0;
        for (int i = 0; i < s.length(); i++) {
            h = (17 * h + s.charAt(i)); // Multiplicador diferente
        }
        // Garante que o resultado seja positivo e no intervalo [1, tamanho-1]
        int resultado = Math.abs(h) % (tamanho - 1) + 1;
        return resultado;
    }

    // Calcula a posição h(x, k) dependendo do método escolhido
    private int calcularPosicao(String chave, int k) {
        int h1 = hash(chave);

        switch (metodoAtual) {
            case LINEAR:
                // h(x, k) = (h(x) + k) mod m
                return (h1 + k) % tamanho;

            case QUADRATICA:
                // h(x, k) = (h'(x) + c1*k + c2*k^2) mod m
                return Math.abs((h1 + C1 * k + C2 * k * k) % tamanho);

            case DUPLA:
                // h(x, k) = (h1(x) + k * h2(x)) mod m
                int h2 = hash2(chave);
                return Math.abs((h1 + k * h2) % tamanho);

            default:
                return h1;
        }
    }

    // Inserção (PDF página 10: testar sucessivamente até encontrar compartimento vazio)
    public boolean inserir(String chave) {
        if (elementosInseridos >= tamanho) {
            System.out.println("Erro: Tabela cheia!");
            return false;
        }

        for (int k = 0; k < tamanho; k++) {
            int posicao = calcularPosicao(chave, k);
            
            if (tabela[posicao] == null) {
                // Encontrou compartimento vazio, insere
                // (Convertendo String para Integer apenas para o exemplo funcionar)
                tabela[posicao] = chave.hashCode(); 
                elementosInseridos++;
                return true;
            }
        }
        return false; // Não encontrou posição (não deve ocorrer se a tabela não estiver cheia)
    }

    // Busca (PDF página 12: mesma sequência de teste)
    public boolean buscar(String chave) {
        for (int k = 0; k < tamanho; k++) {
            int posicao = calcularPosicao(chave, k);
            
            if (tabela[posicao] == null) {
                // Encontrou nulo, o elemento não está na tabela
                return false;
            }
            if (tabela[posicao].equals(chave.hashCode())) {
                // Encontrou o elemento
                return true;
            }
        }
        return false;
    }

    // Método auxiliar para visualizar a tabela (apenas para debug)
    public void imprimirTabela() {
        System.out.println("Tabela (" + metodoAtual + "):");
        for (int i = 0; i < tamanho; i++) {
            System.out.print("[" + i + "]: " + (tabela[i] == null ? "null" : tabela[i]) + " | ");
        }
        System.out.println("\n");
    }

    // ==========================================
    // MÉTODO MAIN PARA TESTAR OS TRÊS CASOS
    // ==========================================
    public static void main(String[] args) {
        int tamanhoTabela = 13; // Usando um número primo para melhor distribuição

        // Teste A: Sondagem Linear
        System.out.println("--- a) Sondagem Linear ---");
        TabelaDispersao tLinear = new TabelaDispersao(tamanhoTabela, MetodoSondagem.LINEAR);
        tLinear.inserir("gato");
        tLinear.inserir("cachorro");
        tLinear.inserir("pato"); // Pode colidir
        tLinear.imprimirTabela();
        System.out.println("Buscar 'gato': " + tLinear.buscar("gato"));
        System.out.println("Buscar 'passaro': " + tLinear.buscar("passaro"));

        // Teste B: Sondagem Quadrática
        System.out.println("\n--- b) Sondagem Quadrática ---");
        TabelaDispersao tQuadratica = new TabelaDispersao(tamanhoTabela, MetodoSondagem.QUADRATICA);
        tQuadratica.inserir("gato");
        tQuadratica.inserir("cachorro");
        tQuadratica.inserir("pato");
        tQuadratica.imprimirTabela();

        // Teste C: Dispersão Dupla
        System.out.println("\n--- c) Dispersão Dupla ---");
        TabelaDispersao tDupla = new TabelaDispersao(tamanhoTabela, MetodoSondagem.DUPLA);
        tDupla.inserir("gato");
        tDupla.inserir("cachorro");
        tDupla.inserir("pato");
        tDupla.imprimirTabela();
        System.out.println("Buscar 'pato': " + tDupla.buscar("pato"));
    }
}
