package application;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Processo {

    private static int codigoGeral = 0;
	
	final int codigo;
	final int tempoChegada;
	int tempoExecucao;
	final int prioridade;
	private int tempoAcessoOperacaoES;
	private final List<Integer> listOperacoesES;

	private int tempoResposta;

	int tempoSaida;
    private final int tempoExecucaoDefault;
	
	Processo(Integer[] processos) {
		Processo.codigoGeral++;
		this.codigo = Processo.codigoGeral;
		
		this.tempoChegada = processos[0];
		this.tempoExecucao = processos[1];
		this.prioridade = processos[2];

		if (processos.length > 3) {
            this.tempoAcessoOperacaoES = processos[3];
        }

        this.listOperacoesES = new LinkedList<>();
        if (processos.length > 4) {
            this.listOperacoesES.addAll(Arrays.asList(processos).subList(4, processos.length));
        }

		this.tempoResposta = tempoChegada;

        this.tempoSaida = tempoChegada;
        this.tempoExecucaoDefault = tempoExecucao;
	}

	int getTempoResposta() {
		return tempoResposta - tempoChegada;
	}

	void setTempoResposta(int tempoResposta) {
		this.tempoResposta = tempoResposta;
	}

    int getTempoEspera() {
        return tempoSaida - tempoChegada - tempoExecucaoDefault;
    }

    void updateOperacaoES() {
        tempoAcessoOperacaoES--;
        if (tempoAcessoOperacaoES < 0 && !listOperacoesES.isEmpty()) {
            tempoAcessoOperacaoES = listOperacoesES.remove(0);
        }
    }

    int getTempoAcessoOperacaoES() {
	    return  tempoAcessoOperacaoES;
    }
}
