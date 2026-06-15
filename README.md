# Forca de Vendas - App Android (Kotlin + Jetpack Compose + Room)

Aplicativo de forca de vendas para gerar pedidos de venda com itens, valores,
condicao de pagamento, parcelas e calculo de frete. Trabalho academico.


## Como executar
1. Abra a pasta `ForcaVendasApp` no **Android Studio** (Ladybug ou mais recente).
2. Aguarde o Gradle sincronizar (ele baixa as dependencias automaticamente).
3. Rode em um emulador ou dispositivo (API 24+).


## Regras de negocio implementadas
| Regra | Comportamento |
|------|---------------|
| Validacao de campos | Nenhum campo pode ficar vazio; CPF com 11 digitos; UF com 2 letras |
| Valores | Nao permite valor unitario/quantidade `<= 0` |
| A vista | **-5%** sobre o total dos itens |
| A prazo | **+5%** sobre o total dos itens + lista de parcelas |
| Frete - Toledo/PR | Gratis (R$ 0,00) |
| Frete - outra cidade (mesmo estado) | R$ 20,00 |
| Frete - outro estado | R$ 20,00 + R$ 50,00 = R$ 70,00 |
| Codigo do pedido | Gerado automaticamente (autoincremento do Room) |
| Consulta | Campo para digitar o no do pedido e exibir seus dados |
| Conclusao | Toast "Pedido no X cadastrado com sucesso!" e limpeza dos campos |

## Estrutura do codigo
```
app/src/main/java/com/faculdade/forcavendas/
├── MainActivity.kt
├── data/
│   ├── entity/        (Cliente, Endereco, Item, PedidoVenda, PedidoItem)
│   ├── dao/           (DAOs do Room)
│   ├── relation/      (PedidoComItens)
│   ├── AppDatabase.kt
│   ├── Converters.kt
│   ├── Repository.kt
│   └── CalculoPedido.kt   (regras de frete/desconto/parcelas)
└── ui/
    ├── theme/
    ├── components/    (Dropdown, SecaoCard, formatacao de moeda)
    ├── viewmodel/     (CadastroViewModel, PedidoViewModel, Factory)
    ├── navigation/    (AppNavigation + barra inferior)
    └── screens/       (Pedido, Cliente, Endereco, Item)
```
