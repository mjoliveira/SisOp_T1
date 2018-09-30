package application;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class RoundRoubin {

	private int tempoEntradaSaida;
    private int tempo;
    private Map<Integer, List<Processo>> dicionarioProcessosRecebidosNoArquivo;
	List<Processo> processados;
    private Processador processador;
	
    RoundRoubin(DadosImportados data) {
		this.tempoEntradaSaida = 4;
		this.tempo = 0;
		this.dicionarioProcessosRecebidosNoArquivo = data.processos;
		this.processador = new Processador(data.fatiaTempo);
		processados = new LinkedList<>();
	}
	
	void start() {
		
		List<Processo> processosValidosParaAddNoProcessador;
		Processo p;
		
		while(!dicionarioProcessosRecebidosNoArquivo.isEmpty() || !processador.isEmpty()) {
			tempo++;
			
			Processo processado = processador.processar(tempo);

			if (processado != null) {
				processado.setTempoSaida(tempo);
			}

			if (processado != null && processado.tempoExecucao > 0) {
				
				if (!dicionarioProcessosRecebidosNoArquivo.containsKey(processado.prioridade)) {
					dicionarioProcessosRecebidosNoArquivo.put(processado.prioridade, new LinkedList<>());
				}
				List<Processo> listaProcessos = dicionarioProcessosRecebidosNoArquivo.get(processado.prioridade);
				listaProcessos.add(processado);
				
			} else if (processado != null) {
                processados.add(processado);
            }

			processosValidosParaAddNoProcessador = new LinkedList<>();

            List<Entry<Integer, List<Processo>>> setList = new ArrayList<>(dicionarioProcessosRecebidosNoArquivo
                    .entrySet());
            setList.sort(Comparator.comparingInt(Entry::getKey));

            for (Map.Entry<Integer, List<Processo>> entrySet : setList) {
				processosValidosParaAddNoProcessador.addAll(entrySet
						.getValue()
						.stream()
						.filter(pf -> pf.tempoChegada <= tempo)
						.collect(Collectors.toList()));
				if (!processosValidosParaAddNoProcessador.isEmpty()) break;
			}
	
			if (processosValidosParaAddNoProcessador.isEmpty()) {
				continue;
			}
			
			p = processosValidosParaAddNoProcessador.get(0);
			
			if (processador.add(p)) {
				p.setEntradaNoProcessador(tempo);
				dicionarioProcessosRecebidosNoArquivo.get(p.prioridade).remove(p);
				if (dicionarioProcessosRecebidosNoArquivo.get(p.prioridade).isEmpty()) {
					dicionarioProcessosRecebidosNoArquivo.remove(p.prioridade);
				}
			}
			
			
		}
	}

}
