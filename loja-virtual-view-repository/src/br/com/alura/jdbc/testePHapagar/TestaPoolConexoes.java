package br.com.alura.jdbc.testePHapagar;

import java.sql.SQLException;

import br.com.alura.jdbc.factory.ConnectionFactory;

public class TestaPoolConexoes {

	public static void main(String[] args) throws SQLException{
				
		ConnectionFactory ConnectionFactory = new ConnectionFactory();
		
		//Listar� apenas 15 conex�es conforme definido na Class ConnectionFactory
		for(int i=0;i<20;i++) {
			ConnectionFactory.recuperarConexao();
			System.out.printf("Conex�o de n�mero %d %n",i);
		}

	}

}
