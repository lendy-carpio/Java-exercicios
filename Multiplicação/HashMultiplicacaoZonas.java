package Multiplicação;
//Lendy Naiara Carpio Pacheco - 10428525

public class HashMultiplicacaoZonas {
    private static class Entrada {
        int chave;
        String valor;
        int proximo; // Índice do próximo elemento na cadeia (na zona de colisão)

        Entrada(int chave, String valor) {
            this.chave = chave;
            this.valor = valor;
            this.proximo = -1; // -1 indica fim da cadeia
        }
    }

    private Entrada[] tabela;
    private int tamanhoTotal;     // m (tamanho total da tabela)
    private int tamanhoZonaBase;  // m_base (tamanho da zona base)
    private int inicioZonaColisao;// Índice inicial da zona de colisão
    private int proximoLivreColisao; // Próximo slot livre na zona de colisão
    
    // Constante A de Knuth (Página 13)
    private static final double A = (Math.sqrt(5) - 1) / 2;

    public HashMultiplicacaoZonas(int tamanhoTotal, double porcentagemZonaBase) {
        this.tamanhoTotal = tamanhoTotal;
        this.tamanhoZonaBase = (int) (tamanhoTotal * porcentagemZonaBase);
        this.inicioZonaColisao = tamanhoZonaBase;
        this.proximoLivreColisao = inicioZonaColisao;
        this.tabela = new Entrada[tamanhoTotal];
    }

    // Função de dispersão pelo Método da Multiplicação
    private int hash(int chave) {
        // h(x) = floor(m * ((x * A) mod 1))
        // Garante que x seja positivo para o cálculo
        int chavePositiva = Math.abs(chave);
        double produto = chavePositiva * A;
        double parteFracionaria = produto - Math.floor(produto);
        return (int) Math.floor(tamanhoZonaBase * parteFracionaria);
    }

    // Inserção
    public void put(int chave, String valor) {
        int posBase = hash(chave);

        // 1. Tenta inserir na Zona Base
        if (tabela[posBase] == null) {
            tabela[posBase] = new Entrada(chave, valor);
            return;
        }

        // 2. Se a posição base está ocupada, precisamos resolver a colisão
        // Se for a mesma chave, atualizamos o valor
        if (tabela[posBase].chave == chave) {
            tabela[posBase].valor = valor;
            return;
        }

        // 3. Colisão real: usar a Zona de Colisão (Encadeamento Interior)
        // Aloca na zona de colisão se houver espaço
        if (proximoLivreColisao < tamanhoTotal) {
            int posColisao = proximoLivreColisao++;
            tabela[posColisao] = new Entrada(chave, valor);
            
            // Encontra o fim da cadeia atual na zona base para ligar ao novo elemento
            int atual = posBase;
            while (tabela[atual].proximo != -1) {
                atual = tabela[atual].proximo;
            }
            tabela[atual].proximo = posColisao; // Cria o link interior
        } else {
            System.out.println("Erro: Tabela cheia (Zona de colisão esgotada)!");
        }
    }

    // Busca
    public String get(int chave) {
        int posBase = hash(chave);
        
        // Percorre a cadeia a partir da zona base
        int atual = posBase;
        while (atual != -1) {
            Entrada e = tabela[atual];
            if (e != null && e.chave == chave) {
                return e.valor;
            }
            atual = (e != null) ? e.proximo : -1;
        }
        return null; // Não encontrado
    }

    public static void main(String[] args) {
        // Tabela com 10 posições, 70% para base, 30% para colisão
        HashMultiplicacaoZonas hash = new HashMultiplicacaoZonas(10, 0.7);
        
        hash.put(10, "Dez");
        hash.put(21, "Vinte e um"); // Pode colidir
        hash.put(32, "Trinta e dois"); // Pode colidir
        hash.put(43, "Quarenta e três"); // Pode colidir
        
        System.out.println("Busca 21: " + hash.get(21));
        System.out.println("Busca 43: " + hash.get(43));
        System.out.println("Busca 99: " + hash.get(99)); // null
    }
}
