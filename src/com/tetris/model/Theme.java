package com.tetris.model;

import java.awt.Color;

/**
 * Representa um tema visual para o jogo, contendo todas as cores necessárias.
 * Usamos um 'record' para uma definição concisa e imutável de um tema.
 */
public record Theme(
    String name,
    Color uiBackground,
    Color boardBackground,
    Color grid,
    Color[] pieceColors // Array com 8 cores: a primeira é para 'NoShape', as outras 7 para as peças.
) {
    // --- Temas Pré-definidos ---

    
    /**
     * Tema baseado em tons de verde seguindo a paleta próxima de #777b4f.
     */
    public static final Theme GREEN = new Theme(
        "Verde Oliva",
        new Color(79, 85, 50),   // uiBackground - tom médio de oliva (#4f5532)
        new Color(43, 47, 30),   // boardBackground - tom mais escuro para contraste
        new Color(90, 95, 62),   // grid - linhas suaves em tom verde-oliva
        new Color[] {
            new Color(15, 15, 10),    // NoShape - quase preto
            new Color(157, 180, 106), // ZShape - verde claro
            new Color(143, 176, 112), // SShape - verde suave
            new Color(99, 163, 117),  // LineShape - verde-teal
            new Color(182, 199, 138), // TShape - verde pálido
            new Color(225, 230, 184), // SquareShape - verde muito claro / creme esverdeado
            new Color(136, 176, 75),  // LShape - verde oliva claro
            new Color(95, 138, 57)    // MirroredLShape - verde médio-escuro
        }
    );

    // Array que contém o tema disponível — apenas o verde permanece.
    public static final Theme[] AVAILABLE_THEMES = { GREEN };
}
