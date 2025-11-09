package com.tetris.view;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

/**
 * A janela principal do jogo (o JFrame).
 * Utiliza um JLayeredPane para sobrepor o painel do jogo e o painel de overlays.
 */
public class GameFrame extends JFrame {

    private GamePanel gamePanel;
    private OverlayPanel overlayPanel;
    private JLayeredPane layeredPane;
    private com.tetris.controller.GameController controller;

    public GameFrame() {
        initComponents();
    }

    private void initComponents() {
        // Cria o painel em camadas
        layeredPane = new JLayeredPane();
        
        // Cria os nossos painéis
        gamePanel = new GamePanel();
        overlayPanel = new OverlayPanel();

        // Define o tamanho dos painéis para que ocupem toda a janela
        Dimension gameSize = gamePanel.getPreferredSize();
        
        layeredPane.setPreferredSize(gameSize);
        gamePanel.setBounds(0, 0, gameSize.width, gameSize.height);
        overlayPanel.setBounds(0, 0, gameSize.width, gameSize.height);

        // Adiciona os painéis ao JLayeredPane em camadas diferentes
        // DEFAULT_LAYER é a camada de baixo, PALETTE_LAYER fica por cima
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);

        // Adiciona o JLayeredPane à janela
        add(layeredPane);

        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    // --- Métodos de acesso para o Controller ---

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public OverlayPanel getOverlayPanel() {
        return overlayPanel;
    }

    /**
     * Conecta o GameController à frame para que componentes (ex: OverlayPanel)
     * possam chamar ações do controller (ex: iniciar o jogo através do botão Start).
     */
    public void setController(com.tetris.controller.GameController controller) {
        this.controller = controller;
        if (this.overlayPanel != null) {
            this.overlayPanel.setController(controller);
        }
    }

    public com.tetris.controller.GameController getController() {
        return controller;
    }
}

