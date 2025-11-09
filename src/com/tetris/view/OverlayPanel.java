package com.tetris.view;

import com.tetris.controller.GameController;
import com.tetris.model.Board;
import com.tetris.model.Theme;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Um painel transparente que desenha os 'overlays' (telas por cima do jogo),
 * como a tela de início, pausa e game over.
 */
public class OverlayPanel extends JPanel {

    private Board board;
    private GameController controller;
    private JButton startButton;
    private BufferedImage backgroundImage;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private TopPlayersPanel overlayTopPlayersPanel;

    public OverlayPanel() {
        setOpaque(false); // Torna o painel transparente
        loadBackgroundImage();
        initStartButton();
        // Ajusta posição do botão quando o painel muda de tamanho
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layoutStartButton();
            }
        });
        
        // Inicializa o painel de top players
        overlayTopPlayersPanel = new TopPlayersPanel();
        overlayTopPlayersPanel.setPreferredSize(new java.awt.Dimension(300, 120));
        overlayTopPlayersPanel.setOpaque(false);
        overlayTopPlayersPanel.setVisible(false);
        add(overlayTopPlayersPanel);
    }

    private void loadBackgroundImage() {
        try {
            // Tenta localizar a imagem no classpath em vários locais
            java.net.URL url = null;
            // 1) caminho absoluto padrão: /com/tetris/view/resources/menu_bg.png
            url = getClass().getResource("/com/tetris/view/resources/menu_bg.png");
            // 2) antigo caminho alternativo: /resources/menu_bg.png
            if (url == null) url = getClass().getResource("/resources/menu_bg.png");

            if (url != null) {
                backgroundImage = ImageIO.read(url);
                System.out.println("OverlayPanel: loaded background image from classpath: " + url);
                return;
            }

            // 3) fallback: tenta carregar diretamente do sistema de arquivos (projeto)
            java.io.File f = new java.io.File("src/com/tetris/view/resources/menu_bg.jpg");
            if (f.exists()) {
                backgroundImage = ImageIO.read(f);
                System.out.println("OverlayPanel: loaded background image from filesystem: " + f.getAbsolutePath());
                return;
            }

            // Nenhuma imagem encontrada
            backgroundImage = null;
            System.out.println("OverlayPanel: no background image found (looked in classpath and src/ path)");
        } catch (IOException e) {
            backgroundImage = null;
            System.out.println("OverlayPanel: error loading background image: " + e.getMessage());
        }
    }

    private void initStartButton() {
        startButton = new JButton("Start");
        startButton.setFocusable(true);
        startButton.setVisible(false);
        startButton.setFont(new Font("Consolas", Font.BOLD, 16));
        startButton.setBorderPainted(false);
        // cores default — serão atualizadas via updateTheme
        startButton.setBackground(new Color(50, 50, 50));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    // passa o nome para o controller antes de iniciar
                    if (nameField != null) {
                        controller.setPlayerName(nameField.getText());
                    }
                    controller.startGameFromUI();
                }
            }
        });
        setLayout(null);
        // Campo de nome do jogador
        nameLabel = new javax.swing.JLabel("Nome:");
        nameLabel.setFont(new Font("Consolas", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setVisible(false);
        add(nameLabel);

        nameField = new javax.swing.JTextField();
        nameField.setFont(new Font("Consolas", Font.PLAIN, 14));
        nameField.setVisible(false);
        add(nameField);

        // Atualiza o estado do botão ao digitar
        nameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                String s = nameField.getText();
                startButton.setEnabled(s != null && !s.trim().isEmpty());
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });

        add(startButton);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Solicita foco para o campo de nome (usado quando o controller precisa que o usuário digite o nome).
     */
    public void requestFocusForName() {
        if (nameField != null) {
            nameField.requestFocusInWindow();
        }
    }

    /**
     * Atualiza o estilo do overlay com base no tema atual.
     */
    public void updateTheme(Theme theme) {
        if (theme == null) return;
        // Usa cores do tema para ajustar o botão
        Color bg = theme.uiBackground().darker();
        Color fg = (bg.getRed() < 128) ? Color.WHITE : Color.BLACK;
        startButton.setBackground(bg);
        startButton.setForeground(fg);
        // força repintura
        startButton.repaint();
        repaint();
    }

    private void layoutStartButton() {
        if (startButton == null) return;
        int w = 180;
        int h = 48;
        int x = (getWidth() - w) / 2;
        // posiciona o botão na parte inferior, com um padding de 40px do fundo
        int y = Math.max(20, getHeight() - h - 40);
        // coloca o campo de nome acima do botão
        int nameW = 200;
        int nameH = 28;
        int nameX = (getWidth() - nameW) / 2;
        int nameY = y - nameH - 12;
        nameLabel.setBounds(nameX, nameY - 18, nameW, 18);
        nameField.setBounds(nameX, nameY, nameW, nameH);

        // posiciona o painel de top players no centro da tela, entre os controles e o campo de nome
        if (overlayTopPlayersPanel != null) {
            int tpW = overlayTopPlayersPanel.getPreferredSize().width;
            int tpH = overlayTopPlayersPanel.getPreferredSize().height;
            int tpX = (getWidth() - tpW) / 2;
            // Posiciona o painel a uma distância fixa abaixo do título/controles
            int topThird = getHeight() / 3;
            int tpY = topThird + 60; // 60 pixels abaixo do texto de controles
            overlayTopPlayersPanel.setBounds(tpX, tpY, tpW, tpH);
        }

        startButton.setBounds(x, y, w, h);
        // start habilitado somente se tiver nome
        String s = nameField.getText();
        startButton.setEnabled(s != null && !s.trim().isEmpty());
    }

    public void updateBoard(Board board) {
        this.board = board;
        // Mostrar/ocultar o botão Start conforme o estado do jogo
        if (startButton != null) {
            boolean showMenu = (board != null && !board.isStarted());
            boolean showingControls = (board != null && board.isShowingControls());
            
            startButton.setVisible(showMenu);
            if (nameField != null && nameLabel != null) {
                nameField.setVisible(showMenu);
                nameLabel.setVisible(showMenu);
            }
            // Atualiza ranking e esconde quando mostrando controles
            if (overlayTopPlayersPanel != null) {
                overlayTopPlayersPanel.setVisible(showMenu && !showingControls);
                if (showMenu && !showingControls) overlayTopPlayersPanel.updateTopPlayers();
            }
            layoutStartButton();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (board == null) {
            return;
        }

        // If controls are currently requested, draw them as a modal overlay
        // (this should happen both on the start screen and during the game).
        if (board.isShowingControls()) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
            drawControls(g, getWidth() / 2 - 110, getHeight() / 2 - 100);
            return;
        }

        // Decide qual overlay desenhar com base no estado do jogo
        if (!board.isStarted()) {
            drawStartScreen(g);
        } else if (board.isGameOver()) {
            drawGameOver(g);
        } else if (board.isPaused()) {
            drawPaused(g);
        }
    }

    private void drawStartScreen(Graphics g) {
        // Desenha imagem de fundo se existir, caso contrário usa um overlay escuro
        if (backgroundImage != null) {
            // escala a imagem para preencher o painel
            g.drawImage(backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
            // aplica uma camada semi-transparente para aumentar contraste do texto
            g.setColor(new Color(0, 0, 0, 120));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Se estiver mostrando controles, aplica um fundo mais escuro
        if (board.isShowingControls()) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Posiciona o título no terço superior da tela
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 36));
        int topThird = getHeight() / 3;
        g.drawString("TETRIS", getWidth() / 2 - 60, topThird);

        // Se não estiver mostrando controles, mostra o texto "Manual de Controles (M)"
        if (!board.isShowingControls()) {
            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            String controlsText = "Manual de Controles (M)";
            int controlsWidth = g.getFontMetrics().stringWidth(controlsText);
            g.drawString(controlsText, (getWidth() - controlsWidth) / 2, topThird + 36);
        } else {
            // Se estiver mostrando controles, desenha os controles logo abaixo do título
            drawControls(g, getWidth() / 2 - 110, topThird + 40);
        }
        

    }

    private void drawGameOver(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 36));
        g.drawString("GAME OVER", getWidth()/2 - 80, getHeight() / 2);
        g.setFont(new Font("Consolas", Font.PLAIN, 18));
        g.drawString("ENTER para reiniciar", getWidth()/2 - 110, getHeight() / 2 + 40);
    }
    
    private void drawPaused(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 36));
        g.drawString("PAUSADO", getWidth() / 2 - 70, getHeight() / 2 - 150);

        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        String controlsText = "Manual de Controles (M)";
        int controlsWidth = g.getFontMetrics().stringWidth(controlsText);
        g.drawString(controlsText, (getWidth() - controlsWidth) / 2, getHeight() / 2 - 80);

        g.setFont(new Font("Consolas", Font.BOLD, 18));
        int textY = getHeight() - 150;
        g.drawString("Pressione P para continuar", getWidth() / 2 - 125, textY);
    }

    /**
     * Novo método auxiliar para desenhar a lista de controlos.
     * É reutilizado pela tela de início e pela tela de pausa.
     */
    private void drawControls(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.drawString("MANUAL DE CONTROLES", x, y);
        y += 30;
        g.drawString("←   Mover Esquerda", x, y);
        y += 20;
        g.drawString("→   Mover Direita", x, y);
        y += 20;
        g.drawString("↑   Girar (Horário)", x, y);
        y += 20;
        g.drawString("Z   Girar (Anti-horário)", x, y);
        y += 20;
        g.drawString("↓   Acelerar Queda", x, y);
        y += 20;
        g.drawString("Espaço   Cair Imediatamente", x, y);
        y += 20;
        g.drawString("P   Pausar Jogo", x, y);
        y += 20;
        g.drawString("M   Mostrar/Ocultar Controles", x, y);
    }
}

