package application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Processador {

	List<Data> memoriaProcessosEmEspera;
	List<Data> memoriaProcessosChamadaDeSistema;
	private Data dadoEmProcessamento;
	Boolean trocaDeContexto;
	int fatiaTempo;
	
	public Processador(int fatiaTempo) {
		this.memoriaProcessosEmEspera = new LinkedList<>();
		this.memoriaProcessosChamadaDeSistema = new LinkedList<>();
		this.trocaDeContexto = false;
		this.fatiaTempo = fatiaTempo;
	}
	
	public boolean isEmpty() {
		return (dadoEmProcessamento == null 
				&& memoriaProcessosEmEspera.isEmpty()
				&& memoriaProcessosChamadaDeSistema.isEmpty());
	}
	
	public boolean add(Processo p) {
		
		if (this.dadoEmProcessamento == null) {
			
			if (memoriaProcessosEmEspera.isEmpty() 
					|| p.prioridade < memoriaProcessosEmEspera.get(0).processo.prioridade) {
				
				setDadoEmProcessamento(new Data(p, this.fatiaTempo));
				return true;
				
			} else {
				
				setDadoEmProcessamento(memoriaProcessosEmEspera.remove(0));
				return false;
				
			}
						
		}
		
		if (this.dadoEmProcessamento.processo.prioridade <= p.prioridade) {
			return false;
		}
		
		memoriaProcessosEmEspera.add(this.dadoEmProcessamento);
		memoriaProcessosEmEspera.sort((d1,d2) -> d1.processo.prioridade - d2.processo.prioridade );
		
		setDadoEmProcessamento(new Data(p, this.fatiaTempo));
		return true;
	}
	
	public Processo processar() {
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
			
			if (this.dadoEmProcessamento.processo.tempoAcessoOperacaoES == 0) {
				
				memoriaProcessosChamadaDeSistema.add(this.dadoEmProcessamento);
				setDadoEmProcessamento(null);
				return null;
				
			} else if (this.dadoEmProcessamento.fatiaTempo == 0) {
				
				Processo p = this.dadoEmProcessamento.processo;
<<<<<<< HEAD
				setDadoEmProcessamento(null);
=======
				this.dadoEmProcessamento = null;
				trocaDeContexto = true;
>>>>>>> branch 'master' of https://github.com/mjoliveira/SisOp_T1.git
				return p;
				
			} else {
				
				return null;
				
			}
		}
		
		if (memoriaProcessosEmEspera.isEmpty() == false) {
			setDadoEmProcessamento(memoriaProcessosEmEspera.remove(0));
		}
		
			
		System.out.print("-");
		return null;
	}
	
	public void setDadoEmProcessamento(Data dadoEmProcessamento) {
		this.dadoEmProcessamento = dadoEmProcessamento;
		trocaDeContexto = true;
	}
	
	public void processarChamadaSistema() {
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
		
		public Data(Processo p, int fatiaTempo) {
			this.processo = p;
			this.fatiaTempo = (this.processo.tempoExecucao < fatiaTempo) 
					? this.processo.tempoExecucao : fatiaTempo;
			this.processo.tempoExecucao -= fatiaTempo;
			this.chamadaDeSistema = 4;
		}
		
	}
	
}
