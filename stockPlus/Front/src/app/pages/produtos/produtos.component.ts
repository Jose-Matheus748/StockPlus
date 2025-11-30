/**
 * Componente de Lista de Produtos
 * Exibe todos os produtos criados pelo usuário
 */

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProdutoService } from '../../services/produto.service';
import { Produto } from '../../models';
import { AuthService } from '../../services/auth.service';
import { LayoutComponent } from '../../components/layout/layout.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [CommonModule, LayoutComponent, FormsModule],
  templateUrl: './produtos.component.html',
  styleUrls: ['./produtos.component.scss'],
})
export class ProdutosComponent implements OnInit {

  produtos: Produto[] = [];
  isLoading: boolean = true;
  erro: string = '';

  showFormModal: boolean = false;
  isEditMode: boolean = false;

  formData: Produto = {
    id: 0,
    nome: '',
    descricao: '',
    fornecedor: '',
    marca: '',
    quantidade: 0,
    precoUnitario: 0,
    estoqueId: 0,
    usuarioId: 0
  };

  constructor(
    private produtoService: ProdutoService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarProdutos();
  }

  /**
   * Carrega os produtos do usuário logado
   */
  carregarProdutos(): void {
    this.isLoading = true;
    this.erro = '';

    const usuario = this.authService.getCurrentUser();
    if (!usuario || !usuario.id) {
      this.erro = 'Usuário não autenticado';
      this.isLoading = false;
      return;
    }

    this.produtoService.getMeusProdutos(usuario.id).subscribe({
      next: (lista) => {
        this.produtos = lista;
        this.isLoading = false;
      },
      error: (error) => {
        this.erro = 'Erro ao carregar produtos.';
        console.error(error);
        this.isLoading = false;
      },
    });
  }

  abrirFormulario(produto?: Produto): void {
    if (produto) {
      this.isEditMode = true;
      this.formData = { ...produto };
    } else {
      this.isEditMode = false;
      this.formData = {
        id: 0,
        nome: '',
        descricao: '',
        fornecedor: '',
        marca: '',
        quantidade: 0,
        precoUnitario: 0,
        estoqueId: 0,
        usuarioId: 0
      };
    }

    this.showFormModal = true;
  }

  fecharFormulario(): void {
    this.showFormModal = false;
  }

  adicionarQuantidade(produto: Produto): void {
    if (!produto || !produto.id){
      alert('Produto não encontrado.');
      return;
    }
    produto.quantidade++;
    this.produtoService.update(produto.id, produto).subscribe({
      next: () => this.carregarProdutos(),
      error: (error) => console.error(error)
    });
  }

  removerQuantidade(produto: Produto): void {
    if (!produto || !produto.id){
      alert('Produto não encontrado.');
      return;
    }
    if (produto.quantidade > 0) produto.quantidade--;
    this.produtoService.update(produto.id, produto).subscribe({
      next: () => this.carregarProdutos(),
      error: (error) => console.error(error)
    });
  }

  deletarProduto(id: number | null): void {
    if (id === null) return;

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

  salvarProduto(): void {
    if (!this.formData.nome || !this.formData.fornecedor || !this.formData.marca) {
      alert('Preencha todos os campos obrigatórios.');
      return;
    }

    if (this.isEditMode && this.formData.id) {
      // Atualizar produto
      this.produtoService.update(this.formData.id, this.formData).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          alert('Produto atualizado com sucesso!');
        },
        error: (error) => {
          console.error(error);
          alert('Erro ao atualizar produto.');
        }
      });

    } else {
      // Criar novo produto
      const usuarioId = this.authService.getUsuarioId();
      if (!usuarioId) {
        alert("Erro: usuário não autenticado!");
        return;
      }

      this.produtoService.create(this.formData, usuarioId).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          alert('Produto adicionado com sucesso!');
        },
        error: (error) => {
          console.error(error);
          alert('Erro ao adicionar produto.');
        }
      });
    }
  }
}
