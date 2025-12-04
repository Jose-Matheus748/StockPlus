import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { LayoutComponent } from '../../components/layout/layout.component';
import { ProtocoloService } from '../../services/protocolo.service';
import { ProdutoService } from '../../services/produto.service';
import { ItemProtocoloService } from '../../services/item-protocolo.service';
import { Protocolo, ItemProtocolo, Produto } from '../../models';
import { AlertaComponent } from '../../components/alerts/alerta.component';

@Component({
  selector: 'app-protocolo-detalhe',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LayoutComponent, AlertaComponent],
  templateUrl: './protocolo.component.html',
  styleUrls: ['./protocolo.component.scss']
})
export class ProtocoloComponent implements OnInit {

  protocolo: Protocolo = {
    id: 0,
    nome: '',
    preco: 0,
    usuarioId: 0,
    itens: []
  };
  produtos: Produto[] = [];

  total = 0;
  isLoading = true;
  erro = '';

  alertMensagem = '';
  alertTipo: 'erro' | 'sucesso' | 'info' = 'info';
  alertVisivel = false;

  novoItem = {
    produtoId: null as number | null,
    quantidade: 1
  };

  constructor(
    private protocoloService: ProtocoloService,
    private produtoService: ProdutoService,
    private itemProtocoloService: ItemProtocoloService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
  const id = Number(this.route.snapshot.paramMap.get('id'));

  if (!id) {
    this.erro = 'Protocolo inválido';
    return;
  }

  // Carrega produtos e só depois carrega protocolo
  this.produtoService.getAll().subscribe({
    next: produtos => {
      this.produtos = produtos;
      this.carregarProtocolo(id); // Agora sim, na hora certa
    },
    error: err => console.error(err)
  });
}


  carregarProdutos() {
    this.produtoService.getAll().subscribe({
      next: data => this.produtos = data,
      error: err => console.error(err)
    });
  }

  carregarProtocolo(id: number) {
    this.isLoading = true;

    this.protocoloService.buscarPorId(id).subscribe({
      next: data => {

        // Mapeia itens e recalcula valorItem
        data.itens = data.itens.map(item => {
          const produto = this.produtos.find(p => p.id === item.produtoId);

          return {
            ...item,
            produtoNome: produto?.nome || item.produtoNome,
            valorItem: produto ? produto.precoUnitario * item.quantidade : 0
          };
        });

        this.protocolo = data;
        this.calcularTotal();
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.erro = 'Erro ao carregar protocolo.';
        this.isLoading = false;
      }
    });
  }

  adicionarItem() {
    if (!this.protocolo.id) {
      this.mostrarAlerta("Protocolo precisa ser salvo antes de adicionar itens.", "erro");
      return;
    }

    if (!this.novoItem.produtoId || this.novoItem.quantidade <= 0) {
      this.mostrarAlerta("Selecione um produto e informe uma quantidade válida", "info");
      return;
    }

    const produto = this.produtos.find(p => p.id === this.novoItem.produtoId);
    if (!produto) {
      this.mostrarAlerta("Produto inválido.", "erro");
      return;
    }

    this.itemProtocoloService.criar({
      protocoloId: this.protocolo.id,
      produtoId: produto.id!,
      quantidade: this.novoItem.quantidade
    }).subscribe({
      next: (itemCriado) => {
        // Atualiza o array com o item retornado, que tem o id
        this.protocolo.itens.push({
          id: itemCriado.id,                 // Agora com id!
          produtoId: itemCriado.produtoId,
          produtoNome: produto.nome,
          quantidade: itemCriado.quantidade,
          valorItem: produto.precoUnitario * itemCriado.quantidade
        });
        this.calcularTotal();
        this.novoItem = { produtoId: null, quantidade: 1 };
      },
      error: err => {
        console.error(err);
        this.mostrarAlerta("Erro ao adicionar item", "erro");
      }
    });
  }

  removerItem(index: number) {
    const item = this.protocolo.itens[index];
    if (!item) return;

    this.itemProtocoloService.remover(item.id!).subscribe({
      next: () => {
        this.protocolo.itens.splice(index, 1);
        this.calcularTotal();
      },
      error: err => {
        console.error(err);
        this.mostrarAlerta("Erro ao remover item.", "erro");
      }
    });
  }

  calcularTotal() {
    this.total = this.protocolo.itens.reduce(
      (soma, item) => soma + (item.valorItem || 0),
      0
    );
  }

  excluirProtocolo() {
    if (!this.protocolo.id) return;

    if (!confirm('Deseja realmente excluir este protocolo?')) return;

    this.protocoloService.deletar(this.protocolo.id).subscribe({
      next: () => {
        this.mostrarAlerta("Protocolo excluído.", "sucesso");
        this.router.navigate(['/protocolos']);
      },
      error: err => {
        console.error(err);
        this.mostrarAlerta("Erro ao excluir.", "erro");
      }
    });
  }

  mostrarAlerta(msg: string, tipo: 'erro' | 'sucesso' | 'info' = 'info') {
    this.alertMensagem = msg;
    this.alertTipo = tipo;
    this.alertVisivel = true;

    setTimeout(() => {
      this.alertVisivel = false;
    }, 3000);
  }
}
