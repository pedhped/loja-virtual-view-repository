package br.com.alura.jdbc.testePHapagar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.com.alura.jdbc.factory.ConnectionFactory;

public class TestaListagem {
	
	public static void main(String[] args) throws SQLException {
		
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		Connection connection = connectionFactory.recuperarConexao();
		
		
		Statement statement = connection.createStatement();
		boolean resultado = statement.execute("select * from produto");
		
		ResultSet resultSet = statement.getResultSet();

		while (resultSet.next()) {
			
			int id = resultSet.getInt("id");
			String nome = resultSet.getString("nome");
			String descricao = resultSet.getString("descricao");
			
			System.out.println("Código: " + id + " Nome: " + nome + " Descrição: " + descricao);
		}

		resultSet.close();
		statement.close();
		connection.close();

	}

}