package application;

class Processo {

    private static int codigoGeral = 0;
	
	int codigo;
	int tempoChegada;
	int tempoExecucao;
	int prioridade;
	int tempoAcessoOperacaoES;

	private int tempoResposta;

	int tempoSaida;
    private int tempoExecucaoDefault;
	
	Processo(Integer[] processos) {
		Processo.codigoGeral++;
		this.codigo = Processo.codigoGeral;
		
		this.tempoChegada = processos[0];
		this.tempoExecucao = processos[1];
		this.prioridade = processos[2];
		if (processos.length == 4)
			this.tempoAcessoOperacaoES = processos[3];

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

}
