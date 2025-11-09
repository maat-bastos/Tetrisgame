package com.tetris.db;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Database {
    private static final String URL = "jdbc:sqlite:tetris.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS game_session (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT,
                score INTEGER,
                level INTEGER,
                lines_cleared INTEGER,
                date_time TEXT
            );
        """;
        try (Connection conn = connect()) {
            if (conn == null) return;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(String player, int score, int level, int lines) {
        // Calcula a data/hora no fuso de Brasília (America/Sao_Paulo) e grava como texto
        String sql = "INSERT INTO game_session (player_name, score, level, lines_cleared, date_time) VALUES (?, ?, ?, ?, ?)";
        // Formato legível: yyyy-MM-dd HH:mm:ss
        String dateTime = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player);
            pstmt.setInt(2, score);
            pstmt.setInt(3, level);
            pstmt.setInt(4, lines);
            pstmt.setString(5, dateTime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 👇 INSIRA AQUI ESTE MÉTODO
    public static void listGames() {
        String sql = "SELECT player_name, score, level, lines_cleared, date_time FROM game_session ORDER BY id DESC";
        try (Connection conn = connect()) {
            if (conn == null) return;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                System.out.println("\n=== Histórico de partidas ===");
                while (rs.next()) {
                    System.out.println(
                        rs.getString("player_name") + " - " +
                        rs.getInt("score") + " pontos, nível " +
                        rs.getInt("level") + " (" + rs.getString("date_time") + ")"
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna o histórico de partidas como uma lista de strings (cada entrada é uma linha formatada).
     */
    public static java.util.List<String> getGameHistory() {
        java.util.List<String> history = new java.util.ArrayList<>();
        String sql = "SELECT player_name, score, level, lines_cleared, date_time FROM game_session ORDER BY id DESC";
        try (Connection conn = connect()) {
            if (conn == null) return history;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String line = rs.getString("player_name") + " - " +
                            rs.getInt("score") + " pontos, nível " +
                            rs.getInt("level") + " (" + rs.getString("date_time") + ")";
                    history.add(line);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Retorna os 10 melhores jogadores ordenados por pontuação
     */
    public static java.util.List<TopPlayer> getTopPlayers() {
        java.util.List<TopPlayer> topPlayers = new java.util.ArrayList<>();
        String sql = """
            SELECT player_name, MAX(score) as max_score, MAX(level) as max_level
            FROM game_session 
            GROUP BY player_name 
            ORDER BY max_score DESC 
            LIMIT 10
            """;
        try (Connection conn = connect()) {
            if (conn == null) return topPlayers;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    TopPlayer player = new TopPlayer(
                        rs.getString("player_name"),
                        rs.getInt("max_score"),
                        rs.getInt("max_level")
                    );
                    topPlayers.add(player);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topPlayers;
    }

    /**
     * Classe para representar um jogador no ranking
     */
    public static class TopPlayer {
        private final String name;
        private final int score;
        private final int level;

        public TopPlayer(String name, int score, int level) {
            this.name = name;
            this.score = score;
            this.level = level;
        }

        public String getName() { return name; }
        public int getScore() { return score; }
        public int getLevel() { return level; }
    }

    /**
     * Limpa todos os dados do banco de dados
     */
    public static void clearAllData() {
        String sql = "DELETE FROM game_session";
        try (Connection conn = connect()) {
            if (conn == null) return;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Banco de dados limpo com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao limpar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
