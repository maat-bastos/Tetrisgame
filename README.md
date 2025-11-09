# Tetrisgame

Um clone clássico do jogo Tetris, desenvolvido em Java, com arquitetura modular e suporte a temas personalizados. O projeto segue o padrão MVC (Model-View-Controller) e inclui recursos de pontuação, interface gráfica e diferentes estilos visuais.

## 🧩 Funcionalidades

  * Interface gráfica interativa em Java Swing.
  * Sistema de pontuação com registro em arquivo (`highscore.txt`).
  * Controle de peças com movimentação e rotação suaves.
  * Gerador aleatório de formas clássicas do Tetris.
  * Tela de informações com pontuação e próximo bloco.

## 🧱 Estrutura do Projeto

```
Tetrisgame/
├── highscore.txt   # Registro de pontuações
└── src/
    └── com/tetris/
        ├── Main.java       # Classe principal (ponto de entrada)
        ├── controller/
        │   └── GameController.java # Lógica principal do jogo
        ├── model/
        │   ├── Board.java  # Representação do tabuleiro
        │   ├── Piece.java  # Representação das peças
        │   ├── Shape.java  # Definição das formas
        │   └── Theme.java  # Cores e estilos visuais
        └── view/
            ├── GameFrame.java    # Janela principal
            ├── GamePanel.java    # Área do jogo
            ├── BoardPanel.java   # Painel do tabuleiro
            ├── InfoPanel.java    # Exibe pontuação e próxima peça
            └── OverlayPanel.java # Tela de pausa/fim de jogo
```

## 🎨 Personalização de Tema

É possível alterar as cores do tema diretamente na classe `Theme.java`. Exemplo de paleta em tons de verde:

```java
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
```

## 🏆 Pontuação

O jogo salva automaticamente a maior pontuação no arquivo:
`highscore.txt`

O valor é atualizado sempre que o jogador supera o recorde atual.

## 🧠 Estrutura MVC

  * **Model**: Gerencia dados e regras do jogo.
  * **View**: Exibe os elementos gráficos e informações.
  * **Controller**: Coordena interações e lógica entre modelo e interface.

## 📜 Licença

Este projeto é de uso livre para fins educacionais. Sinta-se à vontade para modificar, estudar e aprimorar o código.
