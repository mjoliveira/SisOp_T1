package application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RoundRoubin {

	int tempoEntradaSaida;
	int tempo;
	List<Processo> filaProcessosRecebidosNoArquivo;
	Processador processador;
	
	public RoundRoubin(DadosImportados data) {
		this.tempoEntradaSaida = 4;
		this.tempo = 0;
		this.filaProcessosRecebidosNoArquivo = new LinkedList<>();
		this.filaProcessosRecebidosNoArquivo.addAll(data.processos);
		this.processador = new Processador(data.fatiaTempo);
	}
	
	public void start() {
		List<Processo> processosValidosParaAddNoProcessador = null;
		Processo p = null;
		while(!filaProcessosRecebidosNoArquivo.isEmpty()) {
			tempo++;
			
			Processo processado = processador.processar();
			
			if (processado != null && processado.tempoExecucao > 0)
				filaProcessosRecebidosNoArquivo.add(processado);
			
			processosValidosParaAddNoProcessador = filaProcessosRecebidosNoArquivo
					.stream()
					.filter(pf -> pf.tempoChegada <= tempo)
					.sorted((p1,p2) -> p1.prioridade - p2.prioridade)
					.collect(Collectors.toList());
			
			if (processosValidosParaAddNoProcessador.isEmpty()) {
				continue;
			}
			
			p = processosValidosParaAddNoProcessador.remove(0);
			
			if (processador.add(p))
				filaProcessosRecebidosNoArquivo.remove(p);
			
			
		}
	}

}
