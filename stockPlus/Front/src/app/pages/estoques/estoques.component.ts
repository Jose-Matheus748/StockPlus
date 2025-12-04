import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LayoutComponent } from '../../components/layout/layout.component';
import { EstoqueService } from '../../services/estoque.service';
import { Estoque } from '../../models';
import { AuthService } from '../../services/auth.service';
import { AlertaComponent } from '../../components/alerts/alerta.component';

@Component({
  selector: 'app-estoques',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LayoutComponent, AlertaComponent],
  templateUrl: './estoques.component.html',
  styleUrls: ['./estoques.component.scss'],
})
export class EstoquesComponent implements OnInit {
  estoques: Estoque[] = [];
  isLoading: boolean = true;
  erro: string = '';
  showFormModal: boolean = false;
  novoEstoque: Estoque = {
    nome: '',
    descricao: '',
  };

  alertMensagem = '';
  alertTipo: 'erro' | 'sucesso' | 'info' = 'info';
  alertVisivel = false;

  constructor(
    private estoqueService: EstoqueService,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.carregarEstoques();
  }

  /**
   * Carrega todos os estoques do usuário
   */
  carregarEstoques(): void {
  this.isLoading = true;
  this.erro = '';

  const usuario = this.authService.getCurrentUser();
  if (!usuario || !usuario.id) {
    this.erro = 'Usuário não autenticado';
    this.isLoading = false;
    return;
  }

  this.estoqueService.meusEstoques(usuario.id).subscribe({
    next: (data) => {
      this.estoques = data;
      this.isLoading = false;
    },
    error: (error) => {
      this.erro = 'Erro ao carregar estoques. Tente novamente.';
      console.error('Erro:', error);
      this.isLoading = false;
    },
  });
}

  /**
   * Abre o modal para criar novo estoque
   */
  abrirFormulario(): void {
    this.novoEstoque = {
      nome: '',
      descricao: '',
    };
    this.showFormModal = true;
  }

  /**
   * Fecha o modal
   */
  fecharFormulario(): void {
    this.showFormModal = false;
  }

  /**
   * Cria um novo estoque
   */
  criarEstoque(): void {
      if (!this.novoEstoque.nome.trim()) {
        this.mostrarAlerta("Por favor, insira um nome para o estoque", "info");
        return;
      }

      const usuario = this.authService.getCurrentUser();

      if (!usuario || !usuario.id) { this.mostrarAlerta("Usuário não autenticado", "erro"); return; }

      const payload = {
        nome: this.novoEstoque.nome,
        descricao: this.novoEstoque.descricao,
        usuarioId: usuario.id
      };

      this.estoqueService.create(payload).subscribe({
        next: () => {
          this.carregarEstoques();
          this.fecharFormulario();
          this.mostrarAlerta("Estoque criado com sucesso", "sucesso");
        },
        error: (error) => {
          this.mostrarAlerta("Erro ao criar estoque: " + error.error?.message);
        },
      });
    }

    /**
     * Seleciona um estoque e navega para a tela de produtos
     */
    selecionarEstoque(estoque: Estoque): void {
      if (estoque.id) {
        this.router.navigate(['/estoque', estoque.id]);
      }
    }

    /**
     * Deleta um estoque
     */
    deletarEstoque(estoque: Estoque): void {
    if (!estoque.id) return;

    if (confirm(`Tem certeza que deseja deletar o estoque "${estoque.nome}"? Todos os produtos serão removidos.`)) {
      const usuario = this.authService.getCurrentUser();
      if (!usuario || !usuario.id) {
        this.mostrarAlerta("Usuário não autenticado", "erro");
        return;
      }

      this.estoqueService.delete(estoque.id, usuario.id).subscribe({
        next: () => {
          this.carregarEstoques();
          this.mostrarAlerta("Estoque deletado com sucesso", "sucesso");
        },
        error: (error) => {
          console.error('Erro ao deletar:', error);
          this.mostrarAlerta("Erro ao deletar estoque: " + (error.error?.message || 'Erro desconhecido'), "erro");
        },
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
