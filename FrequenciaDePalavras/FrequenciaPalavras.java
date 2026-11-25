package FrequenciaDePalavras;
//Lendy Naiara Carpio Pacheco - 10428525

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FrequenciaPalavras {

    public static void main(String[] args) {
        // Caminho do arquivo de texto (altere para o caminho do seu arquivo)
        String caminhoArquivo = "texto.txt"; 
        
        // Mapa para armazenar a frequência das palavras (chave: palavra, valor: frequência)
        Map<String, Integer> frequencias = new HashMap<>();

        // 1. Leitura do arquivo e contagem de frequência
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Converte para minúsculo e remove pontuações, mantendo apenas letras e números
                // O regex "[^a-zA-Z0-9\\s]" substitui qualquer coisa que não seja letra, número ou espaço por vazio
                linha = linha.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", "");
                
                // Divide a linha em palavras (separadas por espaços)
                String[] palavras = linha.split("\\s+");
                
                for (String palavra : palavras) {
                    if (!palavra.isEmpty()) {
                        // Se a palavra já existe no mapa, incrementa a contagem
                        // Se não, adiciona com valor 1
                        frequencias.put(palavra, frequencias.getOrDefault(palavra, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return; // Encerra o programa se o arquivo não for encontrado
        }

        // 2. Ordenação do Mapa por valor (frequência) em ordem decrescente
        // Convertemos o mapa para uma lista de entradas (Entry) para poder ordenar
        List<Map.Entry<String, Integer>> listaOrdenada = new ArrayList<>(frequencias.entrySet());

        // Usa Collections.sort com um Comparator customizado
        // Compara os valores (frequências) de forma decrescente (b.getValue() - a.getValue())
        // Se as frequências forem iguais, ordena alfabeticamente pela chave (a.getKey().compareTo(b.getKey()))
        Collections.sort(listaOrdenada, (a, b) -> {
            int comparacaoValor = b.getValue().compareTo(a.getValue()); // Decrescente
            if (comparacaoValor == 0) {
                return a.getKey().compareTo(b.getKey()); // Desempate alfabético
            }
            return comparacaoValor;
        });

        // 3. Exibição do resultado no formato pedido
        System.out.println("Frequência das palavras (ordem decrescente):");
        for (Map.Entry<String, Integer> entrada : listaOrdenada) {
            System.out.println(entrada.getKey() + " (" + entrada.getValue() + ")");
        }
    }
}
