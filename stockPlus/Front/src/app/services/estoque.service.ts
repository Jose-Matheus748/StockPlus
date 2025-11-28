/**
 * Serviço de Estoques
 * Responsável por gerenciar operações CRUD de estoques
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG, API_ENDPOINTS } from '../config/api.config';
import { Estoque, Produto, ValorTotalEstoque } from '../models';

@Injectable({
  providedIn: 'root',
})
export class EstoqueService {
  constructor(private http: HttpClient) {}

  /**
   * Cria um novo estoque
   */
  create(estoque: Estoque): Observable<Estoque> {
    return this.http.post<Estoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.create}`,
      estoque
    );
  }

  /**
   * Obtém todos os estoques
   */
  getAll(): Observable<Estoque[]> {
    return this.http.get<Estoque[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.getAll}`
    );
  }

  /**
   * Obtém um estoque por ID
   */
  getById(id: number): Observable<Estoque> {
    return this.http.get<Estoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.getById(id)}`
    );
  }

  /**
   * Atualiza um estoque
   */
  update(id: number, estoque: Estoque): Observable<Estoque> {
    return this.http.put<Estoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.update(id)}`,
      estoque
    );
  }

  /**
   * Deleta um estoque
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.delete(id)}`
    );
  }

  /**
   * Lista os produtos de um estoque específico
   */
  listarProdutos(id: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.listarProdutos(id)}`
    );
  }

  /**
   * Calcula o valor total de um estoque
   */
  calcularValorTotal(id: number): Observable<ValorTotalEstoque> {
    return this.http.get<ValorTotalEstoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.calcularValorTotal(id)}`
    );
  }
}
