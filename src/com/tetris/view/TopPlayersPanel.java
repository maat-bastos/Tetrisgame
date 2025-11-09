package com.tetris.view;

import com.tetris.db.Database;
import com.tetris.db.Database.TopPlayer;
import com.tetris.model.Theme;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TopPlayersPanel extends JPanel {
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font PLAYER_FONT = new Font("Arial", Font.PLAIN, 14);
    private List<TopPlayer> topPlayers;
    private Theme currentTheme;

    public TopPlayersPanel() {
        this.currentTheme = Theme.AVAILABLE_THEMES[0];
        setOpaque(false);
        // Inicializa com lista vazia; o conteúdo será carregado por updateTopPlayers()
        this.topPlayers = new java.util.ArrayList<>();
        repaint();
    }

    public void updateTheme(Theme theme) {
        if (theme != null) {
            this.currentTheme = theme;
            repaint();
        }
    }

    public void updateTopPlayers() {
        topPlayers = Database.getTopPlayers();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Configura rendering para melhor qualidade
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Desenha o fundo semi-transparente usando as cores do tema
    Color bg = currentTheme.uiBackground();
    Color panelBg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 200);
    g2d.setColor(panelBg);
    g2d.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 20, 20);

    // Desenha o título
    Color titleColor = currentTheme.grid().brighter();
    g2d.setColor(titleColor);
    g2d.setFont(TITLE_FONT);
        String title = "TOP 10 JOGADORES";
        FontMetrics titleMetrics = g2d.getFontMetrics(TITLE_FONT);
        int titleX = (getWidth() - titleMetrics.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 30);

        // Desenha a lista de jogadores
    g2d.setFont(PLAYER_FONT);
    int y = 50;
    int rank = 1;
    Color textColor = (bg.getRed() < 128) ? Color.WHITE : Color.BLACK;
    g2d.setColor(textColor);

        if (topPlayers.isEmpty()) {
            String message = "Nenhum jogador registrado";
            FontMetrics metrics = g2d.getFontMetrics(PLAYER_FONT);
            int messageX = (getWidth() - metrics.stringWidth(message)) / 2;
            g2d.drawString(message, messageX, y);
        } else {
            for (TopPlayer player : topPlayers) {
                String playerInfo = String.format("%d. %s - %d pontos (Nível %d)",
                        rank++, player.getName(), player.getScore(), player.getLevel());
                g2d.drawString(playerInfo, 20, y);
                y += 20;
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 250);
    }
}