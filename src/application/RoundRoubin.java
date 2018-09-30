package application;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Classe representa o algoritmo de round robin com prioridade
 *
 * Autores: Mayara e Virgilius
 * CacheData: 30/09/2018
 */
class RoundRoubin {

    // tempo atual no processamento
    private int tempo;

    // dicionaro com os processos separados em filas de prioridades
    private final Map<Integer, List<Processo>> mapFilaProcessos;

    // lista coms os processos totalmente processados
	final List<Processo> processosFinalizados;

	// processador
    private final Processador processador;
	
    RoundRoubin(DadosImportados data) {
        int tempoEntradaSaida = 4;
		this.tempo = 0;
		this.mapFilaProcessos = data.mapFilaProcessos;
		this.processador = new Processador(data.fatiaTempo, tempoEntradaSaida);
		this.processosFinalizados = new LinkedList<>();
	}

    /**
     *  metodo que executa o processamento dos dados recebidos
     */
	void processar() {

		while(!mapFilaProcessos.isEmpty() || !processador.isEmpty()) {
			tempo++;

            executarProcessador();

            setNextProcesso();

		}
	}

    /**
     * metodos seta para o processador executar o processamento
     * verifica se o processo saiu do processaodr e seta o tempo de saida
     * caso necessario devolve o processo ao mapa de processos
     * ou adiciona a fila de processos totalmente executados
     */
    private void executarProcessador() {
        Processo processado = processador.processar(tempo);

        if (processado != null) {
            processado.tempoSaida = tempo;
        }

        if (processado != null && processado.tempoExecucao > 0) {

            if (!mapFilaProcessos.containsKey(processado.prioridade)) {
                mapFilaProcessos.put(processado.prioridade, new LinkedList<>());
            }
            List<Processo> listaProcessos = mapFilaProcessos.get(processado.prioridade);
            listaProcessos.add(processado);

        } else if (processado != null) {
            processosFinalizados.add(processado);
        }
    }

    /**
     * pega na lista de processos pendentes o proximo processo
     * tenta add ao processador se o tempo de chegada for menor ou igual que o tempo atual
     */
    private void setNextProcesso() {

        List<Processo> processosValidosParaAddNoProcessador;
        Processo p;

        processosValidosParaAddNoProcessador = new LinkedList<>();

        List<Entry<Integer, List<Processo>>> setList = new ArrayList<>(mapFilaProcessos
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
            return;
        }

        p = processosValidosParaAddNoProcessador.get(0);

        if (processador.add(p)) {
            mapFilaProcessos.get(p.prioridade).remove(p);
            if (mapFilaProcessos.get(p.prioridade).isEmpty()) {
                mapFilaProcessos.remove(p.prioridade);
            }
        }
    }

}
