package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Classe usada para leitura de arquivo
 * Autores: Mayara e Virgilius
 * CacheData: 30/09/2018
 */
class IO {

    private IO(){}

    /**
     * Metodo responsavel por:
     * - ler o arquivo
     * - converter as informações em dados
     *
     * @param arquivo, string com o caminho e nome do arquivo
     * @throws IOException erro na leitura do arquivo
     * @throws ExceptionInInitializerError erro na quaidade de mapProcessos
     */
	@SuppressWarnings("SameParameterValue")
    static DadosImportados carregarArquivo(String arquivo) throws IOException, ExceptionInInitializerError {
		
		DadosImportados dadosImportados = new DadosImportados();

		List<Processo> listaCompletaProcessos = new LinkedList<>();

		FileReader arq = new FileReader(arquivo);
	    try (BufferedReader lerArq = new BufferedReader(arq)) {

			// Lê a primeira linha.
            dadosImportados.quantidadeProcessos = parseInt(lerArq.readLine());

			// Lê a segunda linha.
            dadosImportados.fatiaTempo = parseInt(lerArq.readLine());


			// Lê  todas as outras linhas.
			lerArq
					// todas as linhas restantes
					.lines()
					// divide a linha em um vetor de string
					.map(line -> line.split(" "))
                    // valida somente as linhas com tres elementos ou mais
                    .filter(line -> line.length >= 3)
					// converte o vetor de string em um vetor de inteiros
					.map(v -> {
						Integer[] n = new Integer[v.length];
						for (int i = 0; i < v.length; i++)
							n[i] = parseInt(v[i]);
						return n;
					})
					// converte um vetor de inteiros em Processo
					.map(Processo::new)
					// add o processo a lista
					.forEach(listaCompletaProcessos::add);
		}

		if (listaCompletaProcessos.size() != dadosImportados.quantidadeProcessos) {
		    throw new ExceptionInInitializerError("numero de mapProcessos incompativeis");
        }

        // cria uma fila de prioridade para prioridade
        // adiciona cada proprioridade a sua respectiva fila
	    Map<Integer, List<Processo>> dicionarioFilaProcessos = new HashMap<>();
	    listaCompletaProcessos.forEach(p -> {

	    	if (!dicionarioFilaProcessos.containsKey(p.prioridade)) {
	    		dicionarioFilaProcessos.put(p.prioridade, new LinkedList<>());
	    	}

	    	dicionarioFilaProcessos.get(p.prioridade).add(p);

	    });

	    // add a lista de mapProcessos a CacheData
	    dadosImportados.mapProcessos = dicionarioFilaProcessos;
	    
		return dadosImportados;
		
	}

}
