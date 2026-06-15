# Forca de Vendas - Especificacao

## Objetivo

Entregar um aplicativo Android academico de forca de vendas que permita cadastrar enderecos, clientes e itens, montar pedidos, aplicar as regras financeiras do enunciado, persistir os dados com Room e consultar pedidos pelo codigo gerado automaticamente.

## Escopo

O aplicativo tera quatro telas acessiveis por navegacao inferior:

- Pedido
- Clientes
- Enderecos
- Itens

Cada tela de cadastro exibira o formulario e a lista dos registros persistidos. A tela de pedido exibira o formulario completo e uma lista dos pedidos cadastrados.

## Persistencia

O Room armazenara:

- Endereco com logradouro, numero, bairro, cidade e UF.
- Cliente com nome, CPF, data de nascimento e endereco vinculado.
- Item com descricao, valor unitario e unidade de medida.
- Pedido de venda com cliente, endereco de entrega, condicao de pagamento, parcelas, totais, frete e data.
- Itens do pedido com produto, quantidade, valor unitario e subtotal do momento da venda.

Os codigos serao chaves primarias numericas com geracao automatica.

## Cadastros

Todos os campos obrigatorios serao validados antes da gravacao. O valor unitario do item devera ser maior que zero. CPF devera conter 11 digitos e UF devera conter duas letras.

Depois de uma gravacao bem-sucedida, os campos do formulario correspondente serao limpos e a lista sera atualizada pelo fluxo do Room.

## Pedido

O pedido exigira:

- Cliente selecionado.
- Pelo menos um item com quantidade maior que zero.
- Endereco de entrega selecionado entre todos os enderecos cadastrados.
- Condicao de pagamento a vista ou a prazo.
- Quantidade de parcelas maior que zero quando a venda for a prazo.

Ao selecionar um cliente, o endereco vinculado sera sugerido como endereco de entrega, permanecendo possivel selecionar outro endereco cadastrado.

Itens repetidos no mesmo pedido terao suas quantidades somadas. A lista permitira remover itens antes da conclusao.

## Regras Financeiras

O total dos itens sera a soma dos subtotais.

- Venda a vista: desconto de 5% sobre o total dos itens.
- Venda a prazo: acrescimo de 5% sobre o total dos itens.
- Toledo/PR: frete de R$ 0,00.
- Outra cidade no Parana: frete de R$ 20,00.
- Outro estado: frete de R$ 70,00, interpretando o enunciado como R$ 20,00 de outra cidade mais R$ 50,00 adicionais.

O valor final sera o total dos itens depois do desconto ou acrescimo, somado ao frete.

Na venda a prazo, o valor final sera dividido em parcelas com duas casas decimais. Qualquer diferenca de centavos sera aplicada na ultima parcela para manter a soma exata.

## Conclusao e Consulta

Ao concluir um pedido valido:

- O cabecalho e os itens serao gravados na mesma transacao.
- Um Toast informara sucesso e exibira o numero gerado.
- Todos os campos do novo pedido serao limpos.
- O proximo pedido permanecera com codigo automatico ainda nao gerado.

Um campo de consulta aceitara o codigo de um pedido e exibira cliente, endereco, itens, condicao, parcelas, frete e valores. A tela tambem exibira uma lista dos pedidos gravados.

## Interface

A interface permanecera simples, adequada a um trabalho de faculdade, usando Jetpack Compose e Material 3. Formularios serao organizados em cartoes, campos terao rotulos claros e valores monetarios usarao o formato brasileiro.

Textos com codificacao incorreta serao corrigidos. O codigo fonte sera entregue sem comentarios de linha, bloco ou documentacao.

## Testes e Validacao

Serao adicionados testes unitarios para:

- Desconto a vista.
- Acrescimo a prazo.
- Frete para Toledo/PR.
- Frete para outra cidade do Parana.
- Frete para outro estado.
- Divisao de parcelas e fechamento dos centavos.
- Totais e quantidade de itens do pedido.
- Validacoes numericas principais quando isolaveis.

A entrega exigira execucao bem-sucedida dos testes unitarios e geracao do APK debug. Quando houver emulador ou dispositivo disponivel, tambem sera feita instalacao e verificacao de inicializacao.

## Publicacao

O conteudo validado sera colocado no repositorio local `C:\ReverTech\ForcaVendas\forcavendas`, revisado para evitar arquivos de build e configuracoes locais, commitado e enviado ao remoto `FellypePires/ForcaVendas`.
