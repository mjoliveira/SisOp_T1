package application;

class Processo {

    private static int codigoGeral = 0;
	
	int codigo;
	int tempoChegada;
	int tempoExecucao;
	int prioridade;
	int tempoAcessoOperacaoES;

	private int tempoResposta;

	private int tempoEspera;
    private int tempoSaida;
	
	Processo(Integer[] processos) {
		Processo.codigoGeral++;
		this.codigo = Processo.codigoGeral;
		
		this.tempoChegada = processos[0];
		this.tempoExecucao = processos[1];
		this.prioridade = processos[2];
		if (processos.length == 4)
			this.tempoAcessoOperacaoES = processos[3];
		
		this.tempoResposta = 0;

        this.tempoEspera = 0;
        this.tempoSaida = tempoChegada;
	}

	int getTempoResposta() {
		return tempoResposta - tempoChegada;
	}

	void setTempoResposta(int tempoResposta) {
		this.tempoResposta = tempoResposta;
	}

	void setEntradaNoProcessador(int tempo) {
	    tempoEspera += tempo - tempoSaida;
    }

    void setTempoSaida(int tempoSaida) {
        this.tempoSaida = tempoSaida;
    }

    int getTempoEspera() {
        return tempoEspera;
    }
}
