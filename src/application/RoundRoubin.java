package application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoundRoubin {

	int tempoEntradaSaida;
	private Data data;
	int tempo;
	List<Processo> filaAtualProcessos;
	
	public RoundRoubin(Data data) {
		this.data = data;
		this.tempoEntradaSaida = 4;
		this.tempo = 0;
		this.filaAtualProcessos = new LinkedList<>();
		this.filaAtualProcessos.addAll(data.processos);
	}
	
	public void start() {
		List<Processo> processosNaFila = null;
		Processo p = null;
		while(!filaAtualProcessos.isEmpty()) {
			tempo++;
			
			processosNaFila = filaAtualProcessos
					.stream()
					.filter(pf -> pf.tempoChegada <= tempo)
					.sorted((p1,p2) -> p1.prioridade - p2.prioridade)
					.collect(Collectors.toList());
			
			if (processosNaFila.isEmpty()) {
				System.out.print("-");
				continue;
			}
			
			p = processosNaFila.get(0);
			System.out.print("-");
			
			System.out.print(tempo);
		}
	}

}
