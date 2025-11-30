/**
 * Serviço de Produtos
 * Responsável por gerenciar operações CRUD de produtos
 */

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG, API_ENDPOINTS } from '../config/api.config';
import { Produto } from '../models';

@Injectable({
  providedIn: 'root',
})
export class ProdutoService {
  constructor(private http: HttpClient) {}

  // ============================================================
  // CREATE
  // ============================================================

  /**
   * Cria um novo produto
   */
  create(produto: Produto, usuarioId: number): Observable<Produto> {
    const params = new HttpParams().set('usuarioId', usuarioId);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.create}`,
      produto,
      { params }
    );
  }

  // ============================================================
  // READ
  // ============================================================

  /**
   * Obtém todos os produtos
   */
  getAll(): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getAll}`
    );
  }

  /**
   * Obtém um produto por ID
   */
  getById(id: number): Observable<Produto> {
    return this.http.get<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getById(id)}`
    );
  }

  /**
   * Lista produtos de um estoque específico
   */
  listarPorEstoque(estoqueId: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.listarProdutos(estoqueId)}`
    );
  }

  /**
   * Lista produtos do usuário logado
   */
  getMeusProdutos(usuarioId: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.listarPorUsuario(usuarioId)}`
    );
  }

  // ============================================================
  // UPDATE
  // ============================================================

  /**
   * Atualiza um produto
   */
  update(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.update(id)}`,
      produto
    );
  }

  /**
   * Adiciona quantidade ao produto
   */
  addQuantidade(id: number, quantidade: number): Observable<Produto> {
    const params = new HttpParams().set('quantidade', quantidade);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.addQuantidade(id)}`,
      {},
      { params }
    );
  }

  /**
   * Remove quantidade do produto
   */
  removeQuantidade(id: number, quantidade: number): Observable<Produto> {
    const params = new HttpParams().set('quantidade', quantidade);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.removeQuantidade(id)}`,
      {},
      { params }
    );
  }

  // ============================================================
  // DELETE / CALCULOS
  // ============================================================

  /**
   * Exclui um produto
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.delete(id)}`
    );
  }

  /**
   * Retorna o valor total do estoque de produtos
   */
  getValorTotal(): Observable<number> {
    return this.http.get<number>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getValorTotal}`
    );
  }
}
