import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LayoutComponent } from '../../components/layout/layout.component';
import { ProdutoService } from '../../services/produto.service';
import { Produto } from '../../models';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AlertaComponent } from '../../components/alerts/alerta.component';

@Component({
  selector: 'app-estoque',
  standalone: true,
  imports: [CommonModule, FormsModule, LayoutComponent, AlertaComponent],
  templateUrl: './estoque.component.html',
  styleUrls: ['./estoque.component.scss'],
})
export class EstoqueComponent implements OnInit {
  produtos: Produto[] = [];
  isLoading = true;
  erro = '';
  showFormModal = false;
  isEditMode = false;

  formData: Produto = {
    id: null,
    nome: '',
    descricao: '',
    fornecedor: '',
    marca: '',
    quantidade: 0,
    precoUnitario: 0,
    estoqueId: 0
  };

  valorTotalEstoque = 0;
  quantidadeTotalItens = 0;
  totalProdutos = 0;
  precoMedioUnitario = 0;

  alertMensagem = '';
  alertTipo: 'erro' | 'sucesso' | 'info' = 'info';
  alertVisivel = false;

  estoqueId!: number;

  constructor(
    private produtoService: ProdutoService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.estoqueId = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.estoqueId) {
      this.router.navigate(['/estoques']);
      return;
    }

    this.carregarProdutos();
  }

  carregarProdutos(): void {
    this.isLoading = true;
    this.erro = '';

    this.produtoService.listarPorEstoque(this.estoqueId).subscribe({
      next: (data) => {
        this.produtos = data;
        this.calcularResumo();
        this.isLoading = false;
      },
      error: () => {
        this.erro = 'Erro ao carregar produtos. Tente novamente.';
        this.isLoading = false;
      },
    });
  }

  calcularResumo(): void {
    this.totalProdutos = this.produtos.length;
    this.quantidadeTotalItens = this.produtos.reduce((s, p) => s + p.quantidade, 0);
    this.valorTotalEstoque = this.produtos.reduce(
      (s, p) => s + p.quantidade * p.precoUnitario,
      0
    );
    this.precoMedioUnitario =
      this.totalProdutos > 0
        ? this.produtos.reduce((s, p) => s + p.precoUnitario, 0) / this.totalProdutos
        : 0;
  }

  abrirFormulario(): void {
    this.isEditMode = false;

    this.formData = {
      id: null,
      nome: '',
      descricao: '',
      fornecedor: '',
      marca: '',
      quantidade: 0,
      precoUnitario: 0,
      estoqueId: this.estoqueId
    };

    this.showFormModal = true;
  }

  editarProduto(produto: Produto): void {
    this.isEditMode = true;
    this.formData = { ...produto };
    this.showFormModal = true;
  }

  fecharFormulario(): void {
    this.showFormModal = false;
  }

  salvarProduto(): void {
    if (!this.formData.nome || !this.formData.fornecedor || !this.formData.marca) {
      this.mostrarAlerta("Por favor, preencha todos os campos obrigatórios", "erro");
      return;
    }

    const usuarioId = this.authService.getUsuarioId();

    if (!usuarioId) {
      this.mostrarAlerta("Usuário não autenticado. Faça login novamente.", "erro");
      return;
    }

    if (this.isEditMode && this.formData.id != null) {

      this.produtoService.update(this.formData.id, this.formData).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          this.mostrarAlerta("Produto atualizado com sucesso", "sucesso");
        },
        error: (error) => {
          this.mostrarAlerta("Erro ao atualizar produto: " + (error.error?.message || 'Erro desconhecido'), "erro");
        },
      });

    } else {

      this.formData.id = null;

      this.produtoService.create(this.formData, usuarioId).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          this.mostrarAlerta("Produto adicionado com sucesso!", "sucesso");
        },
        error: (error) => {
          this.mostrarAlerta("Erro ao adicionar produto: " + (error.error?.message || 'Erro desconhecido'), "erro");
        },
      });
    }
  }

  deletarProduto(id: number | null | undefined): void {
    if (!id) return;

    if (confirm('Tem certeza que deseja deletar este produto?')) {
      this.produtoService.delete(id).subscribe({
        next: () => {
          this.carregarProdutos();
          this.mostrarAlerta("Produto deletado com sucesso", "sucesso");
        },
        error: (error) => {
          this.mostrarAlerta("Erro ao deletar produto: " + error.error?.message, "erro");
        },
      });
    }
  }

  adicionarQuantidade(produto: Produto): void {
    const quantidade = prompt('Quantos itens deseja adicionar?', '1');

    if (quantidade && Number(quantidade) > 0) {
      this.produtoService.addQuantidade(produto.id!, Number(quantidade)).subscribe({
        next: () => this.carregarProdutos(),
        error: (error) =>
          this.mostrarAlerta("Erro ao adicionar quantidade: " + error.error?.message, "erro"),
      });
    }
  }

  removerQuantidade(produto: Produto): void {
    const quantidade = prompt('Quantos itens deseja remover?', '1');

    if (quantidade && Number(quantidade) > 0) {
      this.produtoService.removeQuantidade(produto.id!, Number(quantidade)).subscribe({
        next: () => this.carregarProdutos(),
        error: (error) =>
          this.mostrarAlerta("Erro ao remover quantidade: " + error.error?.message, "erro"),
      });
    }
  }

  calcularValorTotal(produto: Produto): number {
    return produto.quantidade * produto.precoUnitario;
  }

  voltarEstoques(): void {
    this.router.navigate(['/estoques']);
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
