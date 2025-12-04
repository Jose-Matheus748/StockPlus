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

  create(produto: Produto, usuarioId: number): Observable<Produto> {
    const params = new HttpParams().set('usuarioId', usuarioId);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.create}`,
      produto,
      { params }
    );
  }

  getAll(): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getAll}`
    );
  }

  getById(id: number): Observable<Produto> {
    return this.http.get<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getById(id)}`
    );
  }

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

  update(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.update(id)}`,
      produto
    );
  }

  addQuantidade(id: number, quantidade: number): Observable<Produto> {
    const params = new HttpParams().set('quantidade', quantidade);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.addQuantidade(id)}`,
      {},
      { params }
    );
  }

  removeQuantidade(id: number, quantidade: number): Observable<Produto> {
    const params = new HttpParams().set('quantidade', quantidade);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.removeQuantidade(id)}`,
      {},
      { params }
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.delete(id)}`
    );
  }

  getValorTotal(): Observable<number> {
    return this.http.get<number>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getValorTotal}`
    );
  }
}
