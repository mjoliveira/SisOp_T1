package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IO {
	
	public static DadosImportados carregarArquivo(String arquivo) throws IOException{ //L� os dados do arquivo.
		
		DadosImportados data = new DadosImportados();
		
		List<Processo> listaProcessos = new LinkedList<>();
		
		FileReader arq = new FileReader(arquivo);
	    BufferedReader lerArq = new BufferedReader(arq);
		
	 // L� a primeira linha.
	    int quantidadeProcessos = Integer.parseInt(lerArq.readLine()); 
	    data.quantidadeProcessos = quantidadeProcessos;
	    
	 // L� a segunda linha.
	    int fatiaTempo = Integer.parseInt(lerArq.readLine()); 
	    data.fatiaTempo = fatiaTempo;
	    

	 // L�  todas as outras linhas.
	    lerArq
	    	// todas as linhas restantes
	    	.lines()
	    	// divide a linha em um vetor de string
	    	.map(line-> line.split(" "))
	    	// converte o vetor de string em um vetor de inteiros
	    	.map(v->{
	    		Integer[] n = new Integer[v.length];
	    		for (int i=0; i<v.length; i++)
	    			n[i] = Integer.parseInt(v[i]); 
	    		return n;
	    	})
	    	// converte um vetor de inteiros em Processo
	    	.map(v-> new Processo(v))
	    	// add o processo a lista
	    	.forEach(n->listaProcessos.add(n));
		
	    Map<Integer, List<Processo>> dicionarioProcessos = new HashMap<Integer, List<Processo>>();
	    listaProcessos.forEach(p -> {
	    	
	    	if (dicionarioProcessos.containsKey(p.prioridade) == false) {
	    		dicionarioProcessos.put(p.prioridade, new LinkedList<>());
	    	}
	    	
	    	dicionarioProcessos.get(p.prioridade).add(p);
	    	
	    });
	    
	    // add a lista de processos a Data
	    data.processos = dicionarioProcessos;
		
	    // fecha o arquivo
	    lerArq.close();
	    
		return data;
		
	}
	
}
