package com.tetris.db;

public class ClearDatabase {
    public static void main(String[] args) {
        System.out.println("Limpando banco de dados do Tetris...");
        Database.createTable(); // Garante que a tabela existe
        Database.clearAllData();
    }
}