package com.robshouse.geradormegasena;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.sound.sampled.*;
import java.awt.geom.RoundRectangle2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.awt.Font;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeradorMegaSena extends JFrame {

    private static final Color DARK_BG = new Color(17, 23, 26);
    private static final Color DARK_SECONDARY = new Color(25, 32, 36);
    private static final Color ACCENT_COLOR = new Color(29, 185, 84);
    private static final Color ACCENT_HOVER = new Color(30, 215, 96);
    private static final Color TEXT_COLOR = new Color(230, 230, 230);
    private static final Color CLOSE_BTN_COLOR = new Color(232, 17, 35);
    private static final Color CLOSE_BTN_HOVER = new Color(241, 112, 122);

    // Definição dos tipos de jogos
    private enum TipoJogo {
        MEGA_SENA("Mega-Sena", 60, 6, 20),
        QUINA("Quina", 80, 5, 15),
        LOTOFACIL("Lotofácil", 25, 15, 20),
        LOTOMANIA("Lotomania", 100, 50, 50),
        DUPLA_SENA("Dupla Sena", 50, 6, 15),
        DIA_DE_SORTE("Dia de Sorte", 31, 7, 15),
        SUPER_SETE("Super Sete", 10, 7, 7),
        TIMEMANIA("Timemania", 80, 10, 10),
        PERSONALIZADO("Personalizado", 60, 6, 20); // Valores padrão que podem ser alterados

        private final String nome;
        private int numeroMaximo;
        private final int numerosMinimos;
        private final int numerosMaximos;

        TipoJogo(String nome, int numeroMaximo, int numerosMinimos, int numerosMaximos) {
            this.nome = nome;
            this.numeroMaximo = numeroMaximo;
            this.numerosMinimos = numerosMinimos;
            this.numerosMaximos = numerosMaximos;
        }

        public void setNumeroMaximo(int novoMaximo) {
            if (this == PERSONALIZADO) {
                this.numeroMaximo = novoMaximo;
            }
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    private JTextArea areaJogos;
    private JButton btnGerar;
    private JButton btnCopiar;
    private JSpinner spinnerJogos;
    private JSpinner spinnerNumeros;
    private JSpinner spinnerMaximo;
    private JComboBox<TipoJogo> comboJogos;
    private JPanel painelNumeroMaximo;
    private static final Font FONTE_PRINCIPAL = new Font("Inter", Font.PLAIN, 14);
    private static final Font FONTE_BOTOES = new Font("Inter", Font.BOLD, 14);
    private Clip clipSucesso;
    private Clip clipBotao;

    public GeradorMegaSena() {
        super("Gerador de Loteria");
        configurarJanela();
        criarComponentes();
        pack();
        setLocationRelativeTo(null);
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 400));
        setUndecorated(true);
        setAlwaysOnTop(true);
        requestFocus();
        getContentPane().setBackground(DARK_BG);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));

        carregarSons();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            }
        });
    }

    private void criarComponentes() {
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.setBackground(DARK_BG);
        painelSuperior.setBorder(new EmptyBorder(15, 15, 0, 15));

        // Painel para seleção do tipo de jogo
        JPanel painelTipoJogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        painelTipoJogo.setBackground(DARK_BG);

        JLabel lblTipoJogo = new JLabel("Tipo de Jogo:");
        lblTipoJogo.setForeground(TEXT_COLOR);
        lblTipoJogo.setFont(FONTE_PRINCIPAL);

        comboJogos = new JComboBox<>(TipoJogo.values());
        comboJogos.setFont(FONTE_PRINCIPAL);
        comboJogos.setBackground(DARK_SECONDARY);
//        comboJogos.setForeground(TEXT_COLOR);
        comboJogos.setPreferredSize(new Dimension(150, 30));

// Ajusta a cor do texto na dropdown
        comboJogos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_COLOR : DARK_SECONDARY);
                setForeground(TEXT_COLOR);
                return this;
            }
        });
        ((JComponent) comboJogos.getRenderer()).setOpaque(true);

        // Painel para número máximo personalizado
        painelNumeroMaximo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        painelNumeroMaximo.setBackground(DARK_BG);

        JLabel lblMaximo = new JLabel("Número Máximo:");
        lblMaximo.setForeground(TEXT_COLOR);
        lblMaximo.setFont(FONTE_PRINCIPAL);

        spinnerMaximo = criarSpinner(10, 1000, 60); // Permite valores bem flexíveis
        painelNumeroMaximo.add(lblMaximo);
        painelNumeroMaximo.add(spinnerMaximo);
        painelNumeroMaximo.setVisible(true); // Inicialmente oculto

        painelTipoJogo.add(lblTipoJogo);
        painelTipoJogo.add(comboJogos);
        painelTipoJogo.add(painelNumeroMaximo);

        // Painel para configurações do jogo
        JPanel painelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        painelConfig.setBackground(DARK_BG);

        JLabel lblJogos = new JLabel("Quantidade de Jogos:");
        lblJogos.setForeground(TEXT_COLOR);
        lblJogos.setFont(FONTE_PRINCIPAL);
        spinnerJogos = criarSpinner(1, 50, 5);

        JLabel lblNumeros = new JLabel("Números por Jogo:");
        lblNumeros.setForeground(TEXT_COLOR);
        lblNumeros.setFont(FONTE_PRINCIPAL);
        spinnerNumeros = criarSpinner(6, 20, 6);

        painelConfig.add(lblJogos);
        painelConfig.add(spinnerJogos);
        painelConfig.add(lblNumeros);
        painelConfig.add(spinnerNumeros);

        // Adiciona os painéis ao painel superior
        JPanel painelConfigCompleto = new JPanel(new GridLayout(2, 1, 0, 5));
        painelConfigCompleto.setBackground(DARK_BG);
        painelConfigCompleto.add(painelTipoJogo);
        painelConfigCompleto.add(painelConfig);

        painelSuperior.add(painelConfigCompleto, BorderLayout.CENTER);
        add(painelSuperior, BorderLayout.NORTH);

        // Configuração do evento de mudança do tipo de jogo
        comboJogos.addActionListener((var e) -> {
            TipoJogo tipoSelecionado = (TipoJogo) comboJogos.getSelectedItem();
            if (tipoSelecionado != null) {
                boolean isPersonalizado = tipoSelecionado == TipoJogo.PERSONALIZADO;
//                painelNumeroMaximo.setVisible(isPersonalizado);

                // Atualiza os limites do spinner de números
                SpinnerNumberModel model = (SpinnerNumberModel) spinnerNumeros.getModel();
                model.setMinimum(tipoSelecionado.numerosMinimos);
                model.setMaximum(tipoSelecionado.numerosMaximos);
                model.setValue(tipoSelecionado.numerosMinimos);

                SpinnerNumberModel maxModel = (SpinnerNumberModel) spinnerMaximo.getModel();
                maxModel.setValue(tipoSelecionado.numeroMaximo);
                if (isPersonalizado) {
                    // Atualiza o número máximo quando alterado
                    spinnerMaximo.addChangeListener(evt -> {
                        int novoMaximo = (int) spinnerMaximo.getValue();
                        tipoSelecionado.setNumeroMaximo(novoMaximo);
                    });
                }

                pack(); // Reajusta o tamanho da janela
            }
        });

        areaJogos = new JTextArea(12, 35);
        areaJogos.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        areaJogos.setEditable(false);
        areaJogos.setMargin(new Insets(10, 10, 10, 10));
        areaJogos.setBackground(DARK_SECONDARY);
        areaJogos.setForeground(TEXT_COLOR);
        areaJogos.setCaretColor(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(areaJogos);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_SECONDARY, 1));
        scrollPane.getViewport().setBackground(DARK_SECONDARY);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(DARK_BG);
        painelCentral.setBorder(new EmptyBorder(0, 15, 0, 15));
        painelCentral.add(scrollPane, BorderLayout.CENTER);
        add(painelCentral, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        painelBotoes.setBackground(DARK_BG);
        painelBotoes.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnGerar = criarBotaoPrincipal("Gerar Jogos", ACCENT_COLOR);
        btnCopiar = criarBotaoPrincipal("Copiar Todos", ACCENT_COLOR);

        JButton closeButton = criarBotaoPrincipal("Fechar", CLOSE_BTN_COLOR);
        closeButton.setPreferredSize(new Dimension(150, 40));
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(CLOSE_BTN_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(CLOSE_BTN_COLOR);
            }
        });

        painelBotoes.add(btnGerar);
        painelBotoes.add(btnCopiar);
        painelBotoes.add(closeButton);

        add(painelBotoes, BorderLayout.SOUTH);

        btnGerar.addActionListener(e -> gerarJogos());
        btnCopiar.addActionListener(e -> copiarJogos());
    }

    private JButton criarBotaoPrincipal(String texto, Color corBase) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));

                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        botao.setFont(FONTE_BOTOES);
        botao.setBackground(corBase);
        botao.setForeground(TEXT_COLOR);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setPreferredSize(new Dimension(150, 40));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setOpaque(false);
        botao.setContentAreaFilled(false);

        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                botao.setBackground(ACCENT_HOVER);
                botao.repaint();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                botao.setBackground(corBase);
                botao.repaint();
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                tocarSom(clipBotao);
            }
        });

        return botao;
    }

    private JSpinner criarSpinner(int min, int max, int inicial) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(inicial, min, max, 1));
        JComponent editor = spinner.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();

        spinner.setPreferredSize(new Dimension(70, 30));
        editor.setBackground(DARK_SECONDARY);
        textField.setBackground(DARK_SECONDARY);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBorder(BorderFactory.createLineBorder(DARK_SECONDARY, 1));
        textField.setFont(FONTE_PRINCIPAL);

        return spinner;
    }

    private void carregarSons() {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
            byte[] dados = new byte[1];
            AudioInputStream stream = new AudioInputStream(
                    new ByteArrayInputStream(dados), format, dados.length);

            clipSucesso = AudioSystem.getClip();
            clipSucesso.open(stream);

            clipBotao = AudioSystem.getClip();
            clipBotao.open(stream);

        } catch (LineUnavailableException | IOException e) {
            System.err.println("Erro ao carregar sons: " + e);
        }
    }

    private void tocarSom(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void gerarJogos() {
        StringBuilder jogos = new StringBuilder();
        Random random = new Random();
        int quantidadeJogos = (int) spinnerJogos.getValue();
        int numerosPerJogo = (int) spinnerNumeros.getValue();
        TipoJogo tipoSelecionado = (TipoJogo) comboJogos.getSelectedItem();

        if (tipoSelecionado != null) {
            // Adiciona cabeçalho com informações do jogo
            jogos.append(String.format("=== %s ===\n", tipoSelecionado.nome));
            jogos.append(String.format("Números por jogo: %d (entre 1 e %d)\n\n",
                    numerosPerJogo, tipoSelecionado.numeroMaximo));

            // Gera os jogos
            for (int i = 1; i <= quantidadeJogos; i++) {
                Set<Integer> numeros = new HashSet<>();
                while (numeros.size() < numerosPerJogo) {
                    numeros.add(random.nextInt(tipoSelecionado.numeroMaximo) + 1);
                }

                jogos.append(String.format("Jogo %02d: ", i));
                numeros.stream().sorted()
                        .forEach(numero -> jogos.append(String.format("%02d ", numero)));
                jogos.append("\n");
            }
        }

        areaJogos.setText(jogos.toString());
    }

    private void copiarJogos() {
        String textoJogos = areaJogos.getText();
        if (!textoJogos.isEmpty()) {
            StringSelection stringSelection = new StringSelection(textoJogos);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            tocarSom(clipSucesso);

            UIManager.put("OptionPane.background", DARK_BG);
            UIManager.put("Panel.background", DARK_BG);
            UIManager.put("OptionPane.messageForeground", TEXT_COLOR);

            JOptionPane.showMessageDialog(this,
                    "Jogos copiados para a área de transferência!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("TextField.background", DARK_SECONDARY);
            UIManager.put("TextField.foreground", TEXT_COLOR);
            UIManager.put("TextField.caretForeground", TEXT_COLOR);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            System.err.println("Erro: " + e);
        }

        SwingUtilities.invokeLater(() -> {
            GeradorMegaSena frame = new GeradorMegaSena();
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
        });
    }
}
