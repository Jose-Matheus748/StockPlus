/**
 * Serviço de Produtos
 * Responsável por gerenciar operações CRUD de produtos
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG, API_ENDPOINTS } from '../config/api.config';
import { Produto } from '../models';

@Injectable({
  providedIn: 'root',
})
export class ProdutoService {
  constructor(private http: HttpClient) {}

  /**
   * Cria um novo produto
   */
  create(produto: Produto, usuarioId: number): Observable<Produto> {
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.create}?usuarioId=${usuarioId}`,
      produto
    );
  }

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
   * Atualiza um produto
   */
  update(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.update(id)}`,
      produto
    );
  }

  /**
   * Deleta um produto
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.delete(id)}`
    );
  }

  /**
   * Adiciona quantidade a um produto
   */
  addQuantidade(id: number, quantidade: number): Observable<Produto> {
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.addQuantidade(id)}?quantidade=${quantidade}`,
      {}
    );
  }

  /**
   * Remove quantidade de um produto
   */
  removeQuantidade(id: number, quantidade: number): Observable<Produto> {
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.removeQuantidade(id)}?quantidade=${quantidade}`,
      {}
    );
  }

  /**
   * Calcula o valor total em estoque
   */
  getValorTotal(): Observable<number> {
    return this.http.get<number>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getValorTotal}`
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

  getMeusProdutos(usuarioId: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.listarPorUsuario(usuarioId)}`
    );
  }
}
