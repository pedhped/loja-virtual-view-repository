package br.com.alura.jdbc.testePHapagar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.management.RuntimeErrorException;

import br.com.alura.jdbc.factory.ConnectionFactory;



/*CLASSE USADA APENAS PARA ESTUDO, NÃO USE EM PROJETOS REAIS POIS NÃO POSSUI UM PADRÃO IDEAL;*/
public class TestaInsercaoComParametro {

	/*
	 * Para que o JDBC não controle as suas transações é necessário setar AutoCommit
	 * para false e explicitar o commit e o rollback
	 * 
	 */

	public static void main(String[] args) throws SQLException {

		// cria um objeto para abrir a conexão
		ConnectionFactory connectionFactory = new ConnectionFactory();

		/*
		 * Apartir do Java 7 foi implementado o Try-with-resources isto auxilia o
		 * fechamento dos Statements
		 * 
		 * Try-with-resources é declarado assim "try()" ao invés de try{}, os statement
		 * serão fechados automaticamente, pois este recurso extende o autoCloseable.
		 * Isso evita que o programador deixe uma conexão aberta, o que pode gerar
		 * problema de desempenho.
		 * 
		 * não é mais necessário explicitar o close para fechar o statements (ResultSet,
		 * Connection, PreparedStatement)
		 */

		try (Connection connection = connectionFactory.recuperarConexao()) {

			/*
			 * 
			 * Definindo o "setAutoCommit()" como false o programador obtém o controle da
			 * transação em caso de excessão. Assim se na inserção de vários registro, se UM
			 * der erro os DEMAIS não são inseridos e os que foram inseridos são removidos
			 * 
			 * Ou adiciona tudo ou nada;
			 */
			connection.setAutoCommit(false);

			try (
					// Evitando SQL EJECTION com "prepareStatement", que pode ser inserido nos dados
					// fornecidos pelo usuário
					PreparedStatement stm = connection.prepareStatement(
							"INSERT INTO PRODUTO(nome, descricao)values(?, ?)", Statement.RETURN_GENERATED_KEYS);) {

				adicionarVariavel("TV", "32 Polegadas", stm);
				adicionarVariavel("Radio", "AM/FM", stm);
				adicionarVariavel("Mouse", "Sem fio", stm);

				connection.commit();

				/*
				 * este closes() abaixo não são mais necessário pois o Try-with-resources
				 * resolveu a questão fo fechamento
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
			throw new RuntimeException("Não foi possível adicinar o produto");
		}
		stm.execute();

		// Try-with-resources
		try (ResultSet rst = stm.getGeneratedKeys()) {
			while (rst.next()) {
				Integer id = rst.getInt(1);
				System.out.println("O id do novo produto é: " + id);
			}
		}
	}
}
