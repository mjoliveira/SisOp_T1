package application;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class RoundRoubin {

	int tempoEntradaSaida;
	int tempo;
	Map<Integer, List<Processo>> dicionarioProcessosRecebidosNoArquivo;
	Processador processador;
	
	public RoundRoubin(DadosImportados data) {
		this.tempoEntradaSaida = 4;
		this.tempo = 0;
		this.dicionarioProcessosRecebidosNoArquivo = data.processos;
		this.processador = new Processador(data.fatiaTempo);
	}
	
	public void start() {
		
		List<Processo> processosValidosParaAddNoProcessador = null;
		Processo p = null;
		
		while(!dicionarioProcessosRecebidosNoArquivo.isEmpty()) {
			tempo++;
			
			Processo processado = processador.processar();
			
			if (processado != null && processado.tempoExecucao > 0) {
				
				if (dicionarioProcessosRecebidosNoArquivo.containsKey(processado.prioridade) == false) {
					dicionarioProcessosRecebidosNoArquivo.put(processado.prioridade, new LinkedList<Processo>());
				}
				List<Processo> listaProcessos = dicionarioProcessosRecebidosNoArquivo.get(processado.prioridade);
				listaProcessos.add(processado);
				
			}
			
			processosValidosParaAddNoProcessador = new LinkedList<>();
			
			List<Entry<Integer, List<Processo>>> setList = dicionarioProcessosRecebidosNoArquivo
					.entrySet()
					.stream()
					.sorted((e1,e2) -> e1.getKey() - e2.getKey())
					.collect(Collectors.toList());
			
			for (Map.Entry<Integer, List<Processo>> entrySet : setList) {
				processosValidosParaAddNoProcessador.addAll(entrySet
						.getValue()
						.stream()
						.filter(pf -> pf.tempoChegada <= tempo)
						.collect(Collectors.toList()));
				if (processosValidosParaAddNoProcessador.isEmpty() == false) break;
			}
	
			if (processosValidosParaAddNoProcessador.isEmpty()) {
				continue;
			}
			
			p = processosValidosParaAddNoProcessador.get(0);
			
			if (processador.add(p)) {
				dicionarioProcessosRecebidosNoArquivo.get(p.prioridade).remove(p);
				if (dicionarioProcessosRecebidosNoArquivo.get(p.prioridade).isEmpty()) {
					dicionarioProcessosRecebidosNoArquivo.remove(p.prioridade);
				}
			}
			
			
		}
	}

}
