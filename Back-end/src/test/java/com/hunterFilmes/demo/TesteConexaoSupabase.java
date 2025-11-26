package com.hunterFilmes.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class TesteConexaoSupabase {
    @Autowired
    private DataSource dataSource;

    @Test
    void conectarSupabaseTest() throws SQLException {
        System.out.println("Testando conexão com Banco de dados");

        assertNotNull(dataSource, "Banco de dados não esta configurado");

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection, "Não foi possível obter conexão");
            assertTrue(connection.isValid(5), "Conexão não está válida");
            assertFalse(connection.isClosed(), "Conexão está fechada");

            // Verificar se é realmente o Supabase
            var metaData = connection.getMetaData();
            String url = metaData.getURL();

            assertTrue(url.contains("supabase.com"), "Não está conectado ao Supabase");
            assertTrue(url.contains("aws-1-sa-east-1"), "Região incorreta");
            assertTrue(url.contains("pooler.supabase.com"), "Não está usando o pooler");

            System.out.println("✅ CONEXÃO COM SUPABASE BEM-SUCEDIDA!");
            System.out.println("📊 Detalhes da conexão:");
            System.out.println("   Database: " + metaData.getDatabaseProductName());
            System.out.println("   URL: " + url);
            System.out.println("   Driver: " + metaData.getDriverName());
            System.out.println("   Versão: " + metaData.getDriverVersion());
            System.out.println("   Username: " + metaData.getUserName());
        }


    }

    @Test
    void deveExecutarQueryNoSupabase() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery("SELECT current_database(), version()")) {

            assertTrue(resultSet.next());

            String database = resultSet.getString(1);
            String version = resultSet.getString(2);

            assertNotNull(database);
            assertNotNull(version);
            assertTrue(version.contains("PostgreSQL"));

            System.out.println("✅ Query executada com sucesso no Supabase!");
            System.out.println("   Database atual: " + database);
            System.out.println("   Versão PostgreSQL: " + version);
        }
    }
}
