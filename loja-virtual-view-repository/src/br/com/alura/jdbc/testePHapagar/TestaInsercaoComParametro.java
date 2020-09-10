package br.com.alura.jdbc.testePHapagar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.management.RuntimeErrorException;

import br.com.alura.jdbc.factory.ConnectionFactory;



/*CLASSE USADA APENAS PARA ESTUDO, N�O USE EM PROJETOS REAIS POIS N�O POSSUI UM PADR�O IDEAL;*/
public class TestaInsercaoComParametro {

	/*
	 * Para que o JDBC n�o controle as suas transa��es � necess�rio setar AutoCommit
	 * para false e explicitar o commit e o rollback
	 * 
	 */

	public static void main(String[] args) throws SQLException {

		// cria um objeto para abrir a conex�o
		ConnectionFactory connectionFactory = new ConnectionFactory();

		/*
		 * Apartir do Java 7 foi implementado o Try-with-resources isto auxilia o
		 * fechamento dos Statements
		 * 
		 * Try-with-resources � declarado assim "try()" ao inv�s de try{}, os statement
		 * ser�o fechados automaticamente, pois este recurso extende o autoCloseable.
		 * Isso evita que o programador deixe uma conex�o aberta, o que pode gerar
		 * problema de desempenho.
		 * 
		 * n�o � mais necess�rio explicitar o close para fechar o statements (ResultSet,
		 * Connection, PreparedStatement)
		 */

		try (Connection connection = connectionFactory.recuperarConexao()) {

			/*
			 * 
			 * Definindo o "setAutoCommit()" como false o programador obt�m o controle da
			 * transa��o em caso de excess�o. Assim se na inser��o de v�rios registro, se UM
			 * der erro os DEMAIS n�o s�o inseridos e os que foram inseridos s�o removidos
			 * 
			 * Ou adiciona tudo ou nada;
			 */
			connection.setAutoCommit(false);

			try (
					// Evitando SQL EJECTION com "prepareStatement", que pode ser inserido nos dados
					// fornecidos pelo usu�rio
					PreparedStatement stm = connection.prepareStatement(
							"INSERT INTO PRODUTO(nome, descricao)values(?, ?)", Statement.RETURN_GENERATED_KEYS);) {

				adicionarVariavel("TV", "32 Polegadas", stm);
				adicionarVariavel("Radio", "AM/FM", stm);
				adicionarVariavel("Mouse", "Sem fio", stm);

				connection.commit();

				/*
				 * este closes() abaixo n�o s�o mais necess�rio pois o Try-with-resources
				 * resolveu a quest�o fo fechamento
				 */
				// stm.close();
				// connection.close();

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("RollBack Executado");
				connection.rollback();
			}
		}
	}

	private static void adicionarVariavel(String nome, String descricao, PreparedStatement stm) throws SQLException {

		stm.setString(1, nome);
		stm.setString(2, descricao);

		// o if abaixo foi criado apenas para testar o "setAutoCommit(false) e o
		// try-catch;"
		if (nome.equals("Radio")) {
			throw new RuntimeException("N�o foi poss�vel adicinar o produto");
		}
		stm.execute();

		// Try-with-resources
		try (ResultSet rst = stm.getGeneratedKeys()) {
			while (rst.next()) {
				Integer id = rst.getInt(1);
				System.out.println("O id do novo produto �: " + id);
			}
		}
	}
}
