
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LayoutComponent } from '../../components/layout/layout.component';
import { ProdutoService } from '../../services/produto.service';
import { Produto } from '../../models';
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
  produtos: Produto[] = [];
  isLoading: boolean = true;
  erro: string = '';
  showFormModal: boolean = false;
  isEditMode: boolean = false;
  formData: Produto = {
    nome: '',
    descricao: '',
    fornecedor: '',
    marca: '',
    quantidade: 0,
    precoUnitario: 0,
    estoqueId: 0,
    usuarioId: 0
  };

  // Resumo do estoque
  valorTotalEstoque: number = 0;
  quantidadeTotalItens: number = 0;
  totalProdutos: number = 0;
  precoMedioUnitario: number = 0;

  constructor(
    private produtoService: ProdutoService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  estoqueId!: number;

  ngOnInit(): void {
    this.estoqueId = Number(this.route.snapshot.paramMap.get('id'));
    if (!this.estoqueId) {
      this.router.navigate(['/estoques']);
      return;
    }
    this.carregarProdutos();
  }

  /**
   * Carrega todos os produtos do estoque específico
   */
  carregarProdutos(): void {
    this.isLoading = true;
    this.erro = '';

    // Filtrar produtos por estoque
    this.produtoService.listarPorEstoque(this.estoqueId).subscribe({
      next: (data) => {
        this.produtos = data;
        this.calcularResumo();
        this.isLoading = false;
      },
      error: (error) => {
        this.erro = 'Erro ao carregar produtos. Tente novamente.';
        console.error('Erro:', error);
        this.isLoading = false;
      },
    });
  }

  /**
   * Calcula o resumo do estoque
   */
  calcularResumo(): void {
    this.totalProdutos = this.produtos.length;
    this.quantidadeTotalItens = this.produtos.reduce((sum, p) => sum + p.quantidade, 0);
    this.valorTotalEstoque = this.produtos.reduce(
      (sum, p) => sum + p.quantidade * p.precoUnitario,
      0
    );
    this.precoMedioUnitario =
      this.totalProdutos > 0
        ? this.produtos.reduce((sum, p) => sum + p.precoUnitario, 0) / this.totalProdutos
        : 0;
  }

  /**
   * Abre o modal para adicionar novo produto
   */
  abrirFormulario(): void {
    this.isEditMode = false;
    this.formData = {
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

  /**
   * Abre o modal para editar um produto
   */
  editarProduto(produto: Produto): void {
    this.isEditMode = true;
    this.formData = { ...produto };
    this.formData.estoqueId = this.estoqueId;
    this.showFormModal = true;
  }

  /**
   * Fecha o modal
   */
  fecharFormulario(): void {
    this.showFormModal = false;
  }

  /**
   * Salva um novo produto ou atualiza um existente
   */
  salvarProduto(): void {
  if (!this.formData.nome || !this.formData.fornecedor || !this.formData.marca) {
    alert('Por favor, preencha todos os campos obrigatórios');
    return;
  }

  const usuarioId = this.authService.getUsuarioId();
  if (!usuarioId) {
    alert('Usuário não autenticado. Faça login novamente.');
    return;
  }

  if (this.isEditMode && this.formData.id) {
    // atualizar (se seu backend NÃO exigir usuarioId no update)
    this.produtoService.update(this.formData.id, this.formData).subscribe({
      next: () => {
        this.carregarProdutos();
        this.fecharFormulario();
        alert('Produto atualizado com sucesso!');
      },
      error: (error) => {
        console.error(error);
        alert('Erro ao atualizar produto: ' + (error.error?.message || 'Erro desconhecido'));
      }
    });
  } else {
    // criar (envia também o usuarioId como query param)
    this.produtoService.create(this.formData, usuarioId).subscribe({
      next: () => {
        this.carregarProdutos();
        this.fecharFormulario();
        alert('Produto adicionado com sucesso!');
      },
      error: (error) => {
        console.error(error);
        alert('Erro ao adicionar produto: ' + (error.error?.message || 'Erro desconhecido'));
      }
    });
  }
}


  /**
   * Deleta um produto
   */
  deletarProduto(id: number | undefined): void {
    if (!id) return;

    if (confirm('Tem certeza que deseja deletar este produto?')) {
      this.produtoService.delete(id).subscribe({
        next: () => {
          this.carregarProdutos();
          alert('Produto deletado com sucesso!');
        },
        error: (error) => {
          alert('Erro ao deletar produto: ' + error.error?.message);
        },
      });
    }
  }

  /**
   * Adiciona quantidade a um produto
   */
  adicionarQuantidade(produto: Produto): void {
    const quantidade = prompt('Quantos itens deseja adicionar?', '1');
    if (quantidade && !isNaN(Number(quantidade)) && Number(quantidade) > 0) {
      this.produtoService.addQuantidade(produto.id!, Number(quantidade)).subscribe({
        next: () => {
          this.carregarProdutos();
          alert('Quantidade adicionada com sucesso!');
        },
        error: (error) => {
          alert('Erro ao adicionar quantidade: ' + error.error?.message);
        },
      });
    }
  }

  /**
   * Remove quantidade de um produto
   */
  removerQuantidade(produto: Produto): void {
    const quantidade = prompt('Quantos itens deseja remover?', '1');
    if (quantidade && !isNaN(Number(quantidade)) && Number(quantidade) > 0) {
      this.produtoService.removeQuantidade(produto.id!, Number(quantidade)).subscribe({
        next: () => {
          this.carregarProdutos();
          alert('Quantidade removida com sucesso!');
        },
        error: (error) => {
          alert('Erro ao remover quantidade: ' + error.error?.message);
        },
      });
    }
  }

  /**
   * Calcula o valor total de um produto
   */
  calcularValorTotal(produto: Produto): number {
    return produto.quantidade * produto.precoUnitario;
  }

  /**
   * Volta para a tela de estoques
   */
  voltarEstoques(): void {
    this.router.navigate(['/estoques']);
  }
}
