# 🎲 Gerador Mega-Sena

Um elegante gerador de jogos para a Mega-Sena desenvolvido em Java com interface gráfica moderna e intuitiva.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-43853D?style=for-the-badge&logo=java&logoColor=white)

## ✨ Características

- 🎯 Interface gráfica moderna com tema escuro
- 🎨 Design com cantos arredondados
- 🔢 Geração de múltiplos jogos da mega
- ⚡ Personalização da quantidade de números por jogo
- 📋 Funcionalidade de copiar todos os jogos para a área de transferência
- 🔊 Efeitos sonoros para feedback de ações (arquivo de audio precisa ser adicionado)
- 🖥️ Sempre visível sobre outras janelas
- 🎯 Interface sem decoração padrão do sistema operacional

## 🚀 Funcionalidades

### Personalização de Jogos
- Escolha quantos jogos deseja gerar (1-50)
- Defina quantos números por jogo (6-30)
- Visualização clara dos números gerados em fonte monoespaçada

### Interface Moderna
- Tema escuro agradável aos olhos
- Botões com efeitos hover
- Cantos arredondados em toda a interface
- Feedback visual e sonoro para ações

### Usabilidade
- Botões intuitivos para gerar e copiar jogos
- Área de texto com rolagem automática
- Mensagens de confirmação para ações importantes
- Sempre visível sobre outras janelas

## 🛠️ Tecnologias Utilizadas

- Java Swing para a interface gráfica
- Java AWT para manipulação de eventos e gráficos
- Javax Sound para efeitos sonoros
- Padrão de projeto Singleton para gerenciamento de recursos

## 📦 Componentes Principais

### Classes e Recursos
- `GeradorMegaSena.java`: Classe principal que gerencia toda a aplicação
- Utilização de fontes personalizadas (Inter e JetBrains Mono)
- Sistema de cores customizado
- Gerenciamento de eventos do mouse e componentes

### Constantes de Design
```java
private static final Color DARK_BG = new Color(17, 23, 26);
private static final Color DARK_SECONDARY = new Color(25, 32, 36);
private static final Color ACCENT_COLOR = new Color(29, 185, 84);
private static final Color TEXT_COLOR = new Color(230, 230, 230);
```

## 🚦 Como Usar

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/gerador-megasena.git
```

2. Compile o projeto
```bash
javac GeradorMegaSena.java
```

3. Execute a aplicação
```bash
java GeradorMegaSena
```
Se preferir pode executar o .exe gerado na pasta anterior ao projeto "Programas/GeradorMegaSena.exe"

## 🖥️ Requisitos do Sistema

- Java Runtime Environment (JRE) 8 ou superior
- Sistema operacional compatível com Java (Windows, Linux, macOS)
- Ambiente gráfico para exibição da interface

## 🎮 Controles

- **Gerar Jogos**: Cria novos jogos com base nas configurações
- **Copiar Todos**: Copia todos os jogos gerados para a área de transferência
- **Fechar**: Encerra a aplicação

## 🎨 Personalização da Interface

A interface utiliza um esquema de cores moderno e agradável:
- Fundo principal: RGB(17, 23, 26)
- Elementos secundários: RGB(25, 32, 36)
- Cor de destaque: RGB(29, 185, 84)
- Texto: RGB(230, 230, 230)

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 🤝 Contribuindo

Contribuições são sempre bem-vindas! Sinta-se à vontade para:

1. Fazer um Fork do projeto
2. Criar uma Branch para sua Feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abrir um Pull Request

## 📫 Contato

Robson Pereira - robs.eng@outlook.com

Link do Projeto: [https://github.com/seu-usuario/gerador-megasena](https://github.com/seu-usuario/gerador-megasena)

## ✨ Agradecimentos

- [Java Swing Documentation](https://docs.oracle.com/javase/tutorial/uiswing/)
- [Inter Font Family](https://fonts.google.com/specimen/Inter)
- [JetBrains Mono](https://www.jetbrains.com/lp/mono/)
