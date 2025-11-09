package com.tetris.view;

import com.tetris.model.Theme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Painel principal que contém e organiza os outros componentes da view.
 * Usa um BorderLayout para posicionar o tabuleiro e o painel de informações.
 */
public class GamePanel extends JPanel {

    private BoardPanel boardPanel;
    private InfoPanel infoPanel;
    private JButton historyButton;

    public GamePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        boardPanel = new BoardPanel();
        infoPanel = new InfoPanel();

        // Painel lateral que contém as info visual e botões
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(infoPanel, BorderLayout.CENTER);

        // Painel para botões embaixo
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        historyButton = new JButton("Histórico");
        historyButton.setToolTipText("Ver histórico de partidas");

        // Estiliza o botão para combinar com o painel lateral
        styleHistoryButton(historyButton, infoPanel.getBackground());

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pausa o jogo, abre diálogo de histórico e retoma quando o diálogo é fechado.
                JFrame top = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                if (top instanceof GameFrame) {
                    com.tetris.controller.GameController controller = ((GameFrame) top).getController();
                    if (controller != null) {
                        controller.pause();
                    }
                }

                HistoryDialog dlg = new HistoryDialog(top);
                dlg.setModal(false);

                // Quando o diálogo fechar, retomar o jogo e devolver foco ao painel
                dlg.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        resumeGameAfterDialog();
                    }

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        resumeGameAfterDialog();
                    }
                });

                dlg.setVisible(true);
                // Restaurar foco ao painel do jogo (pequeno atraso para garantir que a janela já está visível)
                SwingUtilities.invokeLater(() -> GamePanel.this.requestFocusInWindow());
            }

            private void resumeGameAfterDialog() {
                JFrame top = (JFrame) SwingUtilities.getWindowAncestor(GamePanel.this);
                if (top instanceof GameFrame) {
                    com.tetris.controller.GameController controller = ((GameFrame) top).getController();
                    if (controller != null) {
                        controller.resume();
                    }
                }
                SwingUtilities.invokeLater(() -> GamePanel.this.requestFocusInWindow());
            }
        });
        buttonsPanel.add(historyButton);
        sidePanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
    }

    /**
     * Atualiza o tema de todos os componentes visuais filhos.
     */
    public void updateTheme(Theme theme) {
        infoPanel.updateTheme(theme);
        boardPanel.updateTheme(theme);
        if (historyButton != null) {
            styleHistoryButton(historyButton, infoPanel.getBackground());
        }
    }

    /**
     * Aplica estilo ao botão de histórico de acordo com a cor de fundo do painel lateral.
     */
    private void styleHistoryButton(JButton btn, Color bg) {
        Color blockColor = bg.darker();
        Color borderColor = bg.brighter();
        Color textColor = (bg.getRed() < 128) ? Color.WHITE : Color.BLACK;

        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(blockColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("Consolas", Font.BOLD, 13));
        Border b = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(borderColor), BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setBorder(b);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 36));
    }


    // --- Getters para o Controller ---

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }
}
