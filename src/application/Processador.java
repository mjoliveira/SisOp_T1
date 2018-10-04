package application;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe representa o processador e o cache do processador
 *
 * Autores: Mayara e Virgilius
 * CacheData: 30/09/2018
 */
class Processador {

    // lista dos processos que sairam do processador devido a prioridade mais baixa
	private final List<CacheData> memoriaProcessosEmEspera;

	// lista de processos que sairam do processador devido a chamada de sistema
	private final List<CacheData> memoriaProcessosChamadaDeSistema;

	// variavel do processo que esta sendo executado no processador
	private CacheData dadoEmProcessamento;

	// booleano acionado quando uma troca de contexto acontece
	private Boolean trocaDeContexto;

	// tempo padrao de entrada e saida
	private final int tempoES;

	// tempo padrao de processamento para processo
	private final int fatiaTempo;
	
	Processador(int fatiaTempo, int tempoES) {
		this.memoriaProcessosEmEspera = new LinkedList<>();
		this.memoriaProcessosChamadaDeSistema = new LinkedList<>();
		this.trocaDeContexto = false;
		this.fatiaTempo = fatiaTempo;
		this.tempoES = tempoES;
	}


    /**
     * @return retorna se o processador esta sem nenhum processo
     */
	boolean isEmpty() {
		return (dadoEmProcessamento == null 
				&& memoriaProcessosEmEspera.isEmpty()
				&& memoriaProcessosChamadaDeSistema.isEmpty());
	}


    /**
     * metodo que adiciona um processo ao processador
     *
     * @param p é o processo para tentar ser inserido no processador
     * @return verdadeiro ou falso se o processo foi inserido no processador
     */
	boolean add(Processo p) {
        if (this.dadoEmProcessamento == null) {
            return adicionarProcessadorVazio(p);
        } else {
            return adicionarProcessadorOcupado(p);
        }
	}

	private boolean adicionarProcessadorVazio(Processo p) {
        if (memoriaProcessosEmEspera.isEmpty()) {
            setDadoEmProcessamento(new CacheData(p, this.fatiaTempo, this.tempoES));
            return true;
        } else if (p.prioridade < memoriaProcessosEmEspera.get(0).processo.prioridade)  {
            setDadoEmProcessamento(new CacheData(p, this.fatiaTempo, this.tempoES));
            return true;
        } else {
            setDadoEmProcessamento(memoriaProcessosEmEspera.remove(0));
            return false;
        }
    }

    private boolean adicionarProcessadorOcupado(Processo p) {
        if (this.dadoEmProcessamento.processo.prioridade <= p.prioridade) {
            return false;
        } else {

            memoriaProcessosEmEspera.add(this.dadoEmProcessamento);
            memoriaProcessosEmEspera.sort(Comparator.comparingInt(d -> d.processo.prioridade));

            setDadoEmProcessamento(new CacheData(p, this.fatiaTempo, this.tempoES));
            return true;
        }
    }

    /**
     *  metodo que dispara o processamento do processador
     *
     * @param tempo tempo corrente pasado
     * @return retorna o processo que saiu do processador
     */
	Processo processar(int tempo) {
		processarChamadaSistema();

		if (trocaDeContexto) {
			System.out.print("C");
			trocaDeContexto = false;
			return null;
		}
		
		if (this.dadoEmProcessamento != null) {
		    return processarDadoEmProcessamento(tempo);
		}

        verificarProcessosEmEspera();
		System.out.print("-");
		return null;
	}

	private Processo processarDadoEmProcessamento(int tempo) {

	    CacheData dp = dadoEmProcessamento;
	    Processo processo = dp.processo;

        dp.fatiaTempo--;
        processo.tempoAcessoOperacaoES--;
        System.out.print(processo.codigo);

        if (processo.getTempoResposta() == 0) {
            processo.setTempoResposta(tempo);
        }

        if (processo.getTempoAcessoOperacaoES() == 0) {

            memoriaProcessosChamadaDeSistema.add(dp);
            processo.updateOperacaoES();
            setDadoEmProcessamento(null);
            return null;

        } else if (dp.fatiaTempo <= 0) {

            setDadoEmProcessamento(null);
            return processo;

        } else {

            return null;

        }
    }

    private void verificarProcessosEmEspera() {
        if (!memoriaProcessosEmEspera.isEmpty()) {
            setDadoEmProcessamento(memoriaProcessosEmEspera.remove(0));
        }
    }

    /**
     * metodo que seta cache no processador
     *
     * @param dadoEmProcessamento dado que sera setado no processador
     */
	private void setDadoEmProcessamento(CacheData dadoEmProcessamento) {
		this.dadoEmProcessamento = dadoEmProcessamento;
		trocaDeContexto = true;
	}

    /**
     * atualiza chamadas de sistema
     */
	private void processarChamadaSistema() {
		memoriaProcessosChamadaDeSistema.forEach(d -> d.chamadaDeSistema--);
		List<CacheData> dadosFinalizados = memoriaProcessosChamadaDeSistema
			.stream()
			.filter(d -> d.chamadaDeSistema == 0)
			.collect(Collectors.toList());
		memoriaProcessosEmEspera.addAll(dadosFinalizados);
		memoriaProcessosChamadaDeSistema.removeAll(dadosFinalizados);
	}

	private class CacheData {

		final Processo processo;
		int fatiaTempo;
		int chamadaDeSistema;

		CacheData(Processo p, int fatiaTempo, int chamadaDeSistema) {
			this.processo = p;
			this.fatiaTempo = (this.processo.tempoExecucao < fatiaTempo)
					? this.processo.tempoExecucao : fatiaTempo;
			this.processo.tempoExecucao -= fatiaTempo;
			this.chamadaDeSistema = chamadaDeSistema;
		}

	}
	
}
