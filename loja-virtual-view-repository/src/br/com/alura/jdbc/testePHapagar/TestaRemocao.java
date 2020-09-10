package br.com.alura.jdbc.testePHapagar;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.alura.jdbc.factory.ConnectionFactory;

public class TestaRemocao {

	public static void main(String[] args)throws SQLException{

		ConnectionFactory connectionFactory = new ConnectionFactory();
		Connection connection = connectionFactory.recuperarConexao();
		
		
		Statement stm = connection.createStatement();
		stm.execute("DELETE FROM produto WHERE ID >= 11");
		
		Integer linhasModificadas = stm.getUpdateCount();
		
		System.out.println("Quantidade de linhas modificadas: "+linhasModificadas);

	}

}
