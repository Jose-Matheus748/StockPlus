import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LayoutComponent } from '../../components/layout/layout.component';
import { ProdutoService } from '../../services/produto.service';
import { Produto, ProdutoEstoque } from '../../models';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-estoque',
  standalone: true,
  imports: [CommonModule, FormsModule, LayoutComponent],
  templateUrl: './estoque.component.html',
  styleUrls: ['./estoque.component.scss'],
})
export class EstoqueComponent implements OnInit {

  constructor(
    private produtoService: ProdutoService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  produtos: ProdutoEstoque[] = [];
  isLoading = true;
  erro = '';
  showFormModal = false;
  isEditMode = false;

  estoqueId!: number;

  // FORM DATA 100% COMPATÍVEL COM A INTERFACE
  formData: ProdutoEstoque = {
    id: 0,
    quantidade: 0,
    produto: {
      id: 0,
      nome: '',
      descricao: '',
      fornecedor: '',
      marca: '',
      precoUnitario: 0,
      usuarioId: 0
    }
  };

  // Resumo
  valorTotalEstoque = 0;
  quantidadeTotalItens = 0;
  totalProdutos = 0;
  precoMedioUnitario = 0;

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
        // Normaliza valores vindos do backend
        this.produtos = data.map(pe => ({
          id: pe.id ?? 0,
          quantidade: pe.quantidade ?? 0,
          produto: {
            id: pe.produto.id ?? 0,
            nome: pe.produto.nome ?? '',
            descricao: pe.produto.descricao ?? '',
            fornecedor: pe.produto.fornecedor ?? '',
            marca: pe.produto.marca ?? '',
            precoUnitario: pe.produto.precoUnitario ?? 0,
            usuarioId: pe.produto.usuarioId ?? 0
          }
        }));

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
      (s, p) => s + (p.quantidade * p.produto.precoUnitario),
      0
    );

    this.precoMedioUnitario =
      this.totalProdutos > 0
        ? this.produtos.reduce((s, p) => s + p.produto.precoUnitario, 0) / this.totalProdutos
        : 0;
  }

  abrirFormulario(): void {
    this.isEditMode = false;

    this.formData = {
      id: 0,
      quantidade: 0,
      produto: {
        id: 0,
        nome: '',
        descricao: '',
        fornecedor: '',
        marca: '',
        precoUnitario: 0,
        usuarioId: this.authService.getUsuarioId() ?? 0
      }
    };

    this.showFormModal = true;
  }

  editarProduto(item: ProdutoEstoque): void {
    this.isEditMode = true;

    this.formData = {
      id: item.id ?? 0,
      quantidade: item.quantidade,
      produto: {
        ...item.produto,
        id: item.produto.id ?? 0,
        usuarioId: item.produto.usuarioId ?? 0
      }
    };

    this.showFormModal = true;
  }

  fecharFormulario(): void {
    this.showFormModal = false;
  }

  voltarEstoques(): void {
    this.router.navigate(['/estoques']);
  }

  salvarProduto(): void {

    if (!this.formData.produto.nome || !this.formData.produto.fornecedor || !this.formData.produto.marca) {
      alert('Preencha todos os campos obrigatórios');
      return;
    }

    if (this.formData.quantidade <= 0) {
      alert('A quantidade deve ser maior que 0');
      return;
    }

    const produto: Produto = {
      ...this.formData.produto,
      id: this.formData.produto.id ?? 0,
      usuarioId: this.formData.produto.usuarioId ?? 0
    };

    // Se for edição:
    if (this.isEditMode && produto.id !== 0) {
      this.produtoService.updateProdutoEstoque(
        produto,
        this.formData.quantidade,
        this.estoqueId
      ).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          alert('Produto atualizado com sucesso!');
        },
        error: (error) =>
          alert('Erro ao atualizar produto: ' + (error.error?.message || 'Erro desconhecido')),
      });

    } else {
      // Criar novo produto no estoque
      this.produtoService.createInEstoque(
        produto,
        this.formData.quantidade,
        this.estoqueId
      ).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          alert('Produto adicionado ao estoque com sucesso!');
        },
        error: (error) =>
          alert('Erro ao adicionar produto: ' + (error.error?.message || 'Erro desconhecido')),
      });
    }
  }

  deletarProdutoEstoque(id: number): void {
    if (!id) {
      alert("ID inválido.");
      return;
    }

    this.produtoService.removerDoEstoque(id).subscribe(() => {
      this.produtos = this.produtos.filter((p) => p.id !== id);
      this.calcularResumo();
    });
  }

  adicionarQuantidade(item: ProdutoEstoque): void {
    const novaQtd = item.quantidade + 1;

    const produto: Produto = {
      ...item.produto,
      id: item.produto.id ?? 0,
      usuarioId: item.produto.usuarioId ?? 0
    };

    this.produtoService
      .updateProdutoEstoque(produto, novaQtd, this.estoqueId)
      .subscribe(() => {
        item.quantidade = novaQtd;
        this.calcularResumo();
      });
  }

  removerQuantidade(item: ProdutoEstoque): void {
    if (item.quantidade === 0) return;

    const novaQtd = item.quantidade - 1;

    const produto: Produto = {
      ...item.produto,
      id: item.produto.id ?? 0,
      usuarioId: item.produto.usuarioId ?? 0
    };

    this.produtoService
      .updateProdutoEstoque(produto, novaQtd, this.estoqueId)
      .subscribe(() => {
        item.quantidade = novaQtd;
        this.calcularResumo();
      });
  }
}
