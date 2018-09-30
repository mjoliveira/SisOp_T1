package application;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class Processador {

	private List<Data> memoriaProcessosEmEspera;
	private List<Data> memoriaProcessosChamadaDeSistema;
	private Data dadoEmProcessamento;
	private Boolean trocaDeContexto;
	private int tempoEntradaSaida;
	private int fatiaTempo;
	
	Processador(int fatiaTempo, int tempoEntradaSaida) {
		this.memoriaProcessosEmEspera = new LinkedList<>();
		this.memoriaProcessosChamadaDeSistema = new LinkedList<>();
		this.trocaDeContexto = false;
		this.fatiaTempo = fatiaTempo;
		this.tempoEntradaSaida = tempoEntradaSaida;
	}
	
	boolean isEmpty() {
		return (dadoEmProcessamento == null 
				&& memoriaProcessosEmEspera.isEmpty()
				&& memoriaProcessosChamadaDeSistema.isEmpty());
	}
	
	boolean add(Processo p, int tempo) {
		
		if (this.dadoEmProcessamento == null) {
			
			if (memoriaProcessosEmEspera.isEmpty() 
					|| p.prioridade < memoriaProcessosEmEspera.get(0).processo.prioridade) {
				
				setDadoEmProcessamento(new Data(p, this.fatiaTempo, this.tempoEntradaSaida), tempo);
				return true;
				
			} else {
				
				setDadoEmProcessamento(memoriaProcessosEmEspera.remove(0), tempo);
				return false;
				
			}
						
		}
		
		if (this.dadoEmProcessamento.processo.prioridade <= p.prioridade) {
			return false;
		}
		
		memoriaProcessosEmEspera.add(this.dadoEmProcessamento);
		memoriaProcessosEmEspera.sort(Comparator.comparingInt(d -> d.processo.prioridade));
		
		setDadoEmProcessamento(new Data(p, this.fatiaTempo,  this.tempoEntradaSaida), tempo);
		return true;
	}
	
	Processo processar(int tempo) {
		processarChamadaSistema();
		
		if (trocaDeContexto) {
			System.out.print("C");
			trocaDeContexto = false;
			return null;
		}
		
		if (this.dadoEmProcessamento != null) {
			this.dadoEmProcessamento.fatiaTempo--;
			this.dadoEmProcessamento.processo.tempoAcessoOperacaoES--;
			System.out.print(this.dadoEmProcessamento.processo.codigo);
			
			if (this.dadoEmProcessamento.processo.getTempoResposta() == 0)
				this.dadoEmProcessamento.processo.setTempoResposta(tempo);
			
			if (this.dadoEmProcessamento.processo.tempoAcessoOperacaoES == 0) {
				
				memoriaProcessosChamadaDeSistema.add(this.dadoEmProcessamento);
				setDadoEmProcessamento(null, tempo);
				return null;
				
			} else if (this.dadoEmProcessamento.fatiaTempo == 0) {
				
				Processo p = this.dadoEmProcessamento.processo;
				setDadoEmProcessamento(null, tempo);
				return p;
				
			} else {
				
				return null;
				
			}
		}
		
		if (!memoriaProcessosEmEspera.isEmpty()) {
			setDadoEmProcessamento(memoriaProcessosEmEspera.remove(0), tempo);
		}
		
			
		System.out.print("-");
		return null;
	}
	
	private void setDadoEmProcessamento(Data dadoEmProcessamento, int tempo) {
	    if (this.dadoEmProcessamento != null) {
	        this.dadoEmProcessamento.processo.setTempoSaida(tempo);
        }
		this.dadoEmProcessamento = dadoEmProcessamento;
		trocaDeContexto = true;
        if (this.dadoEmProcessamento != null) {
            this.dadoEmProcessamento.processo.setEntradaNoProcessador(tempo);
        }
	}
	
	private void processarChamadaSistema() {
		memoriaProcessosChamadaDeSistema.forEach(d -> d.chamadaDeSistema--);
		List<Data> dadosFinalizados = memoriaProcessosChamadaDeSistema
			.stream()
			.filter(d -> d.chamadaDeSistema == 0)
			.collect(Collectors.toList());
		memoriaProcessosEmEspera.addAll(dadosFinalizados);
		memoriaProcessosChamadaDeSistema.removeAll(dadosFinalizados);
	}
	
	private class Data {
		
		Processo processo;
		int fatiaTempo;
		int chamadaDeSistema;
		
		Data(Processo p, int fatiaTempo, int chamadaDeSistema) {
			this.processo = p;
			this.fatiaTempo = (this.processo.tempoExecucao < fatiaTempo) 
					? this.processo.tempoExecucao : fatiaTempo;
			this.processo.tempoExecucao -= fatiaTempo;
			this.chamadaDeSistema = chamadaDeSistema;
		}
		
	}
	
}
