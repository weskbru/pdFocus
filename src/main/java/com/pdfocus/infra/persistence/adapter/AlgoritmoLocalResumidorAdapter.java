package com.pdfocus.infra.persistence.adapter;

import com.pdfocus.application.resumo.port.saida.ResumidorIAPort;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Implementação da porta {@link ResumidorIAPort} que utiliza um algoritmo local
 * para gerar resumos extrativos de textos em português.
 *
 * <p>Este adaptador implementa uma estratégia híbrida de resumo extrativo,
 * priorizando sentenças com base em frequência de palavras significativas,
 * posição no texto (início e fim) e tamanho ideal. O objetivo é fornecer
 * resumos rápidos e totalmente offline, adequados para o MVP do pdFocus.</p>
 *
 * <p>Características Principais:</p>
 * <ul>
 * <li>Processamento 100% local, sem dependências de APIs externas.</li>
 * <li>Rápido, projetado para processar textos em milissegundos.</li>
 * <li>Utiliza um conjunto pré-definido de stopwords em português.</li>
 * <li>Inclui um mecanismo de fallback para textos vazios ou erros de processamento.</li>
 * </ul>
 *
 * @version 2.0 - Implementação do algoritmo inteligente local.
 */
@Component
public class AlgoritmoLocalResumidorAdapter implements ResumidorIAPort {

    /**
     * Conjunto imutável de stopwords comuns em português.
     * Utilizado para ignorar palavras sem significado semântico (artigos, preposições, etc.)
     * durante o cálculo de frequência, melhorando a relevância das pontuações.
     */
    private static final Set<String> STOPWORDS;

    // Bloco estático para inicializar o conjunto de stopwords uma única vez.
    static {
        Set<String> tempSet = new HashSet<>();
        String[] stopwordsArray = {
                "a", "o", "de", "e", "do", "da", "em", "um", "uma", "para", "que", "se", "com", // Adicione a lista completa aqui
                "os", "as", "no", "na", "por", "uma", "uns", "umas", "ao", "aos", "à", "às",
                "pelo", "pela", "pelos", "pelas", "num", "numa", "nuns", "numas", "dum", "duma",
                "duns", "dumas", "este", "esta", "estes", "estas", "esse", "essa", "esses", "essas",
                "aquele", "aquela", "aqueles", "aquelas", "isto", "isso", "aquilo", "outro", "outra",
                "outros", "outras", "tal", "tais", "qual", "quais", "cujo", "cuja", "cujos", "cujas",
                "quanto", "quanta", "quantos", "quantas", "qualquer", "quaisquer", "algum", "alguma",
                "alguns", "algumas", "nenhum", "nenhuma", "nenhuns", "nenhumas", "todo", "toda",
                "todos", "todas", "cada", "vários", "várias", "outrem", "tudo", "nada", "algo", "alguém",
                "ninguém", "quem", "com", "sem", "sob", "sobre", "tras", "ante", "apos", "ate", "perante",
                "quando", "enquanto", "antes", "depois", "desde", "onde", "aonde", "como", "porque", "pois",
                "embora", "se", "mesmo", "talvez", "sempre", "nunca", "jamais", "agora", "logo", "aqui", "ali", "la"
                // É recomendado externalizar esta lista para um arquivo ou constante mais gerenciável
        };
        Collections.addAll(tempSet, stopwordsArray);
        STOPWORDS = Collections.unmodifiableSet(tempSet);
    }

    /**
     * Gera um resumo extrativo para o texto fornecido, respeitando um limite aproximado de palavras.
     *
     * <p>O processo envolve limpar o texto, dividi-lo em frases, calcular a relevância
     * de cada frase e selecionar as mais importantes (combinando início, meio pontuado e fim)
     * até atingir o limite de palavras.</p>
     *
     * @param textoCompleto O texto original a ser resumido.
     * @param maxPalavras O número máximo aproximado de palavras desejado para o resumo.
     * @return Uma {@code String} contendo o resumo gerado. Retorna uma mensagem de fallback
     * se o texto de entrada for nulo/vazio ou se ocorrer um erro durante o processamento.
     * Retorna um resumo básico truncado se a extração de frases falhar.
     */
    @Override
    public String resumir(String textoCompleto, int maxPalavras) {
        if (textoCompleto == null || textoCompleto.trim().isEmpty()) {
            return "Texto fornecido está vazio ou nulo."; // Mensagem mais clara
        }

        try {
            String textoLimpo = limparTexto(textoCompleto);
            List<String> frases = extrairFrases(textoLimpo);

            if (frases.isEmpty()) {
                // Se não conseguir extrair frases, usa o fallback básico
                return extrairResumoBasico(textoLimpo, maxPalavras);
            }

            // Gera o resumo usando a lógica de pontuação
            return criarResumoInteligente(frases, maxPalavras);

        } catch (Exception e) {
            // Em caso de qualquer erro inesperado, retorna o fallback básico
            // Idealmente, logar o erro aqui: log.error("Erro ao gerar resumo local", e);
            return extrairResumoBasico(textoCompleto, maxPalavras);
        }
    }

    /**
     * Realiza uma limpeza básica no texto, removendo espaços e quebras de linha excessivas.
     * @param texto O texto original.
     * @return O texto limpo e sem espaços extras nas extremidades.
     */
    private String limparTexto(String texto) {
        return texto.replaceAll("\\s+", " ") // Substitui múltiplos espaços/tabs/newlines por um único espaço
                .replaceAll("\\n+", " ") // Garante que quebras de linha sejam espaços
                .trim(); // Remove espaços no início e fim
    }

    /**
     * Divide o texto limpo em uma lista de frases.
     * Utiliza pontuações finais (. ! ?) como delimitadores e filtra frases muito curtas.
     * @param texto O texto já limpo.
     * @return Uma lista de {@code String}, onde cada string é uma frase. Retorna lista vazia se não encontrar frases válidas.
     */
    private List<String> extrairFrases(String texto) {
        // Regex para dividir por ponto, exclamação ou interrogação, seguido por espaço(s)
        String[] frasesArray = texto.split("(?<=[.!?])\\s+");
        return Arrays.stream(frasesArray)
                .map(String::trim) // Remove espaços extras de cada frase
                .filter(frase -> frase.split("\\s+").length > 3 && frase.length() > 10) // Filtra frases muito curtas (ex: menos de 4 palavras ou 10 chars)
                .toList(); // Usa toList() para Java 16+ (ou .collect(Collectors.toList()) para versões anteriores)
    }

    /**
     * Orquestra a geração do resumo inteligente, calculando frequências, pontuações e combinando estratégias.
     * @param frases A lista de frases extraídas do texto.
     * @param maxPalavras O limite máximo de palavras para o resumo.
     * @return O resumo gerado.
     */
    private String criarResumoInteligente(List<String> frases, int maxPalavras) {
        Map<String, Integer> frequenciaPalavras = calcularFrequenciaPalavras(frases);
        Map<String, Double> pontuacaoFrases = calcularPontuacaoFrases(frases, frequenciaPalavras);
        return combinarEstrategias(frases, pontuacaoFrases, maxPalavras);
    }

    /**
     * Calcula a frequência de cada palavra significativa (não-stopword e com mais de 2 caracteres) no texto.
     * @param frases A lista de frases do texto.
     * @return Um {@code Map} onde a chave é a palavra (minúscula) e o valor é sua contagem.
     */
    private Map<String, Integer> calcularFrequenciaPalavras(List<String> frases) {
        Map<String, Integer> frequencia = new HashMap<>();
        for (String frase : frases) {
            // Divide por qualquer caractere não-palavra (pontuação, espaços)
            for (String palavra : frase.toLowerCase().split("\\W+")) {
                // Considera apenas palavras relevantes
                if (palavra.length() > 2 && !STOPWORDS.contains(palavra)) {
                    frequencia.put(palavra, frequencia.getOrDefault(palavra, 0) + 1);
                }
            }
        }
        return frequencia;
    }

    /**
     * Atribui uma pontuação de relevância para cada frase.
     * A pontuação é baseada na soma das frequências das palavras significativas que a compõem,
     * com bônus por posição (início/fim) e tamanho ideal.
     * @param frases A lista de frases.
     * @param frequencia O mapa de frequência de palavras.
     * @return Um {@code LinkedHashMap} (para manter a ordem original das frases) onde a chave é a frase
     * e o valor é sua pontuação de relevância.
     */
    private Map<String, Double> calcularPontuacaoFrases(List<String> frases, Map<String, Integer> frequencia) {
        Map<String, Double> pontuacao = new LinkedHashMap<>(); // Mantém a ordem de inserção
        int totalFrases = frases.size();

        for (int i = 0; i < totalFrases; i++) {
            String frase = frases.get(i);
            double score = 0.0;

            // Pontuação baseada na frequência das palavras
            for (String palavra : frase.toLowerCase().split("\\W+")) {
                score += frequencia.getOrDefault(palavra, 0);
            }

            // Bônus por posição: primeiras 10% (máx 5) e últimas 10% (máx 3)
            if (i < Math.min(5, totalFrases * 0.1)) score *= 1.5; // Bônus maior para o início
            if (i >= totalFrases - Math.min(3, totalFrases * 0.1)) score *= 1.2; // Bônus menor para o fim

            // Bônus por tamanho ideal (entre 8 e 25 palavras)
            int numPalavras = frase.split("\\s+").length;
            if (numPalavras >= 8 && numPalavras <= 25) score *= 1.1;

            pontuacao.put(frase, score / numPalavras); // Normaliza pelo tamanho da frase para evitar viés
        }
        return pontuacao;
    }

    /**
     * Seleciona e combina as frases para formar o resumo final, respeitando o limite de palavras.
     * A estratégia é:
     * 1. Incluir algumas frases iniciais (até 30% do limite).
     * 2. Adicionar as frases mais pontuadas do meio do texto (até 80% do limite).
     * 3. Incluir algumas frases finais (até 100% do limite).
     * @param frases A lista original de frases.
     * @param pontuacao O mapa com a pontuação de relevância de cada frase.
     * @param maxPalavras O limite máximo de palavras para o resumo.
     * @return O resumo final como uma {@code String}.
     */
    private String combinarEstrategias(List<String> frases, Map<String, Double> pontuacao, int maxPalavras) {
        // Usar um Set para evitar frases duplicadas e manter a ordem de inserção
        Set<String> selecionadas = new LinkedHashSet<>();
        int palavrasUsadas = 0;

        // 1. Adiciona as primeiras frases (contexto inicial)
        for (int i = 0; i < Math.min(3, frases.size()); i++) {
            String frase = frases.get(i);
            int tamanho = contarPalavras(frase);
            if (palavrasUsadas + tamanho <= maxPalavras * 0.3) { // Limita a 30% do total
                if (selecionadas.add(frase)) { // Adiciona apenas se não estiver já presente
                    palavrasUsadas += tamanho;
                }
            } else {
                break; // Para se atingir o limite
            }
        }

        // 2. Adiciona as frases mais relevantes (meio)
        for (String frase : ordenarPorRelevancia(pontuacao)) {
            // Verifica se a frase não está entre as primeiras/últimas já potencialmente adicionadas
            int indice = frases.indexOf(frase);
            boolean jaSelecionadaOuExtremo = selecionadas.contains(frase) ||
                    indice < 3 || indice >= frases.size() - 2;

            if (!jaSelecionadaOuExtremo) {
                int tamanho = contarPalavras(frase);
                if (palavrasUsadas + tamanho <= maxPalavras * 0.8) { // Limita a 80% do total
                    if (selecionadas.add(frase)) {
                        palavrasUsadas += tamanho;
                    }
                }
            }
        }

        // 3. Adiciona as últimas frases (conclusão)
        int finais = Math.min(2, frases.size() / 10); // Pega as últimas 2 ou 10%, o que for menor
        for (int i = Math.max(0, frases.size() - finais); i < frases.size(); i++) {
            String frase = frases.get(i);
            int tamanho = contarPalavras(frase);
            if (palavrasUsadas + tamanho <= maxPalavras) { // Completa até o limite total
                if (selecionadas.add(frase)) {
                    palavrasUsadas += tamanho;
                }
            } else {
                break; // Para se atingir o limite
            }
        }

        // Reconstrói o resumo mantendo a ordem original das frases selecionadas
        List<String> resumoOrdenado = frases.stream()
                .filter(selecionadas::contains)
                .toList();

        String resumo = String.join(". ", resumoOrdenado);
        // Garante que termina com pontuação, mas evita ponto duplo
        return resumo.endsWith(".") ? resumo : resumo + ".";
    }

    /**
     * Ordena as frases com base na sua pontuação de relevância, da mais alta para a mais baixa.
     * @param pontuacao O mapa de frases e suas pontuações.
     * @return Uma lista de frases ordenada por relevância.
     */
    private List<String> ordenarPorRelevancia(Map<String, Double> pontuacao) {
        return pontuacao.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed()) // Ordena por valor (pontuação) descendente
                .map(Map.Entry::getKey)
                .toList(); // Ou .collect(Collectors.toList())
    }

    /**
     * Fornece um resumo muito básico como fallback, simplesmente truncando o texto original
     * no limite de palavras especificado.
     * @param texto O texto original.
     * @param maxPalavras O número máximo de palavras.
     * @return O texto truncado, com "..." no final se for cortado.
     */
    private String extrairResumoBasico(String texto, int maxPalavras) {
        String textoLimpo = limparTexto(texto); // Limpa primeiro
        String[] palavras = textoLimpo.split("\\s+");
        if (palavras.length <= maxPalavras) {
            return textoLimpo.endsWith(".") ? textoLimpo : textoLimpo + "."; // Garante pontuação final
        }
        // Pega as primeiras 'maxPalavras' palavras e junta com espaço
        String truncado = String.join(" ", Arrays.copyOfRange(palavras, 0, maxPalavras));
        return truncado + "...";
    }

    /**
     * Método auxiliar para contar palavras em uma frase.
     * @param frase A frase a ser analisada.
     * @return O número de palavras na frase.
     */
    private int contarPalavras(String frase) {
        return frase.split("\\s+").length;
    }
}