import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ProdutoService } from '../../services/produto.service';
import { Produto } from '../../models';
import { AuthService } from '../../services/auth.service';
import { LayoutComponent } from '../../components/layout/layout.component';
import { FormsModule } from '@angular/forms';
import { AlertaComponent } from '../../components/alerts/alerta.component';

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [CommonModule, LayoutComponent, FormsModule, AlertaComponent],
  templateUrl: './produtos.component.html',
  styleUrls: ['./produtos.component.scss'],
})
export class ProdutosComponent implements OnInit {

  produtos: Produto[] = [];
  isLoading: boolean = true;
  erro: string = '';

  showFormModal: boolean = false;
  isEditMode: boolean = false;

  alertMensagem = '';
  alertTipo: 'erro' | 'sucesso' | 'info' = 'info';
  alertVisivel = false;

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
      this.mostrarAlerta("Produto não encontrado", "erro");
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
      this.mostrarAlerta("Produto não encontrado", "erro");
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
          this.mostrarAlerta("Produto deletado com sucesso", "sucesso");
        },
        error: (error) => {
          this.mostrarAlerta("Erro ao deletar produto: " + error.error?.message, "erro");
        },
      });
    }
  }

  salvarProduto(): void {
    if (!this.formData.nome || !this.formData.fornecedor || !this.formData.marca) {
      this.mostrarAlerta("Preencha todos os campos obrigatóris", "info");
      return;
    }

    if (this.isEditMode && this.formData.id) {
      // Atualizar produto
      this.produtoService.update(this.formData.id, this.formData).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          this.mostrarAlerta("Produto atualizado com sucesso!", "sucesso")
        },
        error: (error) => {
          console.error(error);
          this.mostrarAlerta("Erro ao atualizar produto", "erro")
        }
      });

    } else {
      // Criar novo produto
      const usuarioId = this.authService.getUsuarioId();
      if (!usuarioId) {
        this.mostrarAlerta("Erro: usuário não autenticado!", "erro")
        return;
      }

      this.produtoService.create(this.formData, usuarioId).subscribe({
        next: () => {
          this.carregarProdutos();
          this.fecharFormulario();
          this.mostrarAlerta("Produto adicionado com sucesso!", "sucesso")
        },
        error: (error) => {
          console.error(error);
          this.mostrarAlerta("Produto não encontrado", "erro");
        }
      });
    }
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
