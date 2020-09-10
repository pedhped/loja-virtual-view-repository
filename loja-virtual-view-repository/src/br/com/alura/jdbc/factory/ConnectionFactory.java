package br.com.alura.jdbc.factory;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/*DataSource � um nome dado � conex�o configurada para um banco de dados de um servidor. 

Driver de pool de conex�es usado no curso: C3P0.jar

Pool de conex�es � um cache de conex�es de banco de dados mantido de forma que as 
conex�es possam ser reutilizadas quando requisi��es futuras ao banco de dados 
forem requeridas. Pools de conex�es s�o usados para garantir o desempenho da 
execu��o de comandos sobre um banco de dados. Isto faz com que o banco suporte 
uma quantidade elevada de conex�es por�m de forma controlada.	
*/
public class ConnectionFactory {
	
	public DataSource dataSource;

	/* IMPLEMENTANDO O POOL DE CONEX�ES */
	public ConnectionFactory() {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost/loja_virtual?useTimezone=true&serverTimezone=UTC");
		comboPooledDataSource.setUser("root");
		comboPooledDataSource.setPassword("root");// Using password: yes
		
		//comboPooledDataSource.setMaxPoolSize(15);//Carrega o pool com 15 conex�es;
		
		this.dataSource = comboPooledDataSource;
	}

	public Connection recuperarConexao() {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/* END - IMPLEMENTANDO O POOL DE CONEX�ES */
}
