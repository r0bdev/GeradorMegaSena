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
    private static final Color ACCENT_COLOR = new Color(29, 185, 84);  // Spotify green
    private static final Color ACCENT_HOVER = new Color(30, 215, 96);
    private static final Color TEXT_COLOR = new Color(230, 230, 230);
    private static final Color CLOSE_BTN_COLOR = new Color(232, 17, 35);
    private static final Color CLOSE_BTN_HOVER = new Color(241, 112, 122);

    private JTextArea areaJogos;
    private JButton btnGerar;
    private JButton btnCopiar;
    private JSpinner spinnerJogos;
    private JSpinner spinnerNumeros;
    private static final Font FONTE_PRINCIPAL = new Font("Inter", Font.PLAIN, 14);
    private static final Font FONTE_BOTOES = new Font("Inter", Font.BOLD, 14);
    private Clip clipSucesso;
    private Clip clipBotao;

    public GeradorMegaSena() {
        super("Gerador Mega-Sena");
        configurarJanela();
        criarComponentes();
        pack();
        setLocationRelativeTo(null);
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
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

        JPanel painelConfig = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        painelConfig.setBackground(DARK_BG);

        JLabel lblJogos = new JLabel("Quantidade de Jogos:");
        lblJogos.setForeground(TEXT_COLOR);
        lblJogos.setFont(FONTE_PRINCIPAL);
        spinnerJogos = criarSpinner(1, 50, 5);

        JLabel lblNumeros = new JLabel("Números por Jogo:");
        lblNumeros.setForeground(TEXT_COLOR);
        lblNumeros.setFont(FONTE_PRINCIPAL);
        spinnerNumeros = criarSpinner(6, 15, 6);

        painelConfig.add(lblJogos);
        painelConfig.add(spinnerJogos);
        painelConfig.add(lblNumeros);
        painelConfig.add(spinnerNumeros);

        painelSuperior.add(painelConfig, BorderLayout.CENTER);
        add(painelSuperior, BorderLayout.NORTH);

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

        // Criar o botão fechar
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

                // Desenha o fundo arredondado com antialiasing
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));

                // Configura a fonte e desenha o texto
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

        for (int i = 1; i <= quantidadeJogos; i++) {
            Set<Integer> numeros = new HashSet<>();
            while (numeros.size() < numerosPerJogo) {
                numeros.add(random.nextInt(60) + 1);
            }

            jogos.append(String.format("Jogo %02d: ", i));
            numeros.stream().sorted()
                    .forEach(numero -> jogos.append(String.format("%02d ", numero)));
            jogos.append("\n");
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
