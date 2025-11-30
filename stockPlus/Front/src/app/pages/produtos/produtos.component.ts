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

@Component({
  selector: 'app-produtos',
  standalone: true,
  imports: [CommonModule, LayoutComponent],
  templateUrl: './produtos.component.html',
  styleUrls: ['./produtos.component.scss'],
})
export class ProdutosComponent implements OnInit {

  produtos: Produto[] = [];
  isLoading: boolean = true;
  erro: string = '';

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
}
