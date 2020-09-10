package br.com.alura.jdbc.factory;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/*DataSource é um nome dado à conexão configurada para um banco de dados de um servidor. 

Driver de pool de conexões usado no curso: C3P0.jar

Pool de conexões é um cache de conexões de banco de dados mantido de forma que as 
conexões possam ser reutilizadas quando requisições futuras ao banco de dados 
forem requeridas. Pools de conexões são usados para garantir o desempenho da 
execução de comandos sobre um banco de dados. Isto faz com que o banco suporte 
uma quantidade elevada de conexões porém de forma controlada.	
*/
public class ConnectionFactory {
	
	public DataSource dataSource;

	/* IMPLEMENTANDO O POOL DE CONEXÕES */
	public ConnectionFactory() {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost/loja_virtual?useTimezone=true&serverTimezone=UTC");
		comboPooledDataSource.setUser("root");
		comboPooledDataSource.setPassword("root");// Using password: yes
		
		//comboPooledDataSource.setMaxPoolSize(15);//Carrega o pool com 15 conexões;
		
		this.dataSource = comboPooledDataSource;
	}

	public Connection recuperarConexao() {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/* END - IMPLEMENTANDO O POOL DE CONEXÕES */
}
