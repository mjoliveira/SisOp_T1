package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class IO {
	public static List<String[]> carregarArquivo(String arquivo) throws IOException{ //Lê os dados do arquivo.
		
		List<String[]> lista = new LinkedList<String[]>();
		
		FileReader arq = new FileReader(arquivo);
	    BufferedReader lerArq = new BufferedReader(arq);
		
	    int quantidade = Integer.parseInt(lerArq.readLine()); // Lê a primeira linha.
		int aux = 0;
		
		String linha = null;
		
		while (aux != quantidade) {   
			linha = lerArq.readLine();
			lista.add(linha.split(" ", 4));
			aux++;

		}
		
		return lista;
		
	}
}
