package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

class IO {

    private IO(){}
	
	static DadosImportados carregarArquivo(String arquivo) throws IOException{ //L� os dados do arquivo.
		
		DadosImportados data = new DadosImportados();
		
		List<Processo> listaProcessos = new LinkedList<>();
		
		FileReader arq = new FileReader(arquivo);
	    try (BufferedReader lerArq = new BufferedReader(arq)) {

			// L� a primeira linha.
            data.quantidadeProcessos = parseInt(lerArq.readLine());

			// L� a segunda linha.
            data.fatiaTempo = parseInt(lerArq.readLine());


			// L�  todas as outras linhas.
			lerArq
					// todas as linhas restantes
					.lines()
					// divide a linha em um vetor de string
					.map(line -> line.split(" "))
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
					.forEach(listaProcessos::add);
		}

	    Map<Integer, List<Processo>> dicionarioProcessos = new HashMap<>();
	    listaProcessos.forEach(p -> {
	    	
	    	if (!dicionarioProcessos.containsKey(p.prioridade)) {
	    		dicionarioProcessos.put(p.prioridade, new LinkedList<>());
	    	}
	    	
	    	dicionarioProcessos.get(p.prioridade).add(p);
	    	
	    });
	    
	    // add a lista de processos a Data
	    data.processos = dicionarioProcessos;
	    
		return data;
		
	}
	
}
