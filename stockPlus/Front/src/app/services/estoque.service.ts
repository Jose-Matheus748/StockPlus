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
  create(estoque: Estoque): Observable<Estoque> {
    return this.http.post<Estoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.create}`,
      estoque
    );
  }

  getAll(): Observable<Estoque[]> {
    return this.http.get<Estoque[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.getAll}`
    );
  }

  getById(id: number): Observable<Estoque> {
    return this.http.get<Estoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.getById(id)}`
    );
  }

  update(id: number, estoque: Estoque): Observable<Estoque> {
    return this.http.put<Estoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.update(id)}`,
      estoque
    );
  }

  delete(id: number, usuarioId: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.delete(id)}?usuarioId=${usuarioId}`
    );
  }

  listarProdutos(id: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.listarProdutos(id)}`
    );
  }

  calcularValorTotal(id: number): Observable<ValorTotalEstoque> {
    return this.http.get<ValorTotalEstoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.calcularValorTotal(id)}`
    );
  }

  meusEstoques(usuarioId: number): Observable<Estoque[]> {
    return this.http.get<Estoque[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.meusEstoques}?usuarioId=${usuarioId}`
    );
  }
}
