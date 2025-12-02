import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG, API_ENDPOINTS } from '../config/api.config';
import { Produto, ProdutoEstoque } from '../models';

@Injectable({
  providedIn: 'root',
})
export class ProdutoService {
  constructor(private http: HttpClient) {}

  // ============================================================
  // PRODUTOS (SEM ESTOQUE)
  // ============================================================

  /** Cria um produto SEM ligação com estoque */
  create(produto: Produto, usuarioId: number): Observable<Produto> {
    const params = new HttpParams().set('usuarioId', usuarioId);
    return this.http.post<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.create}`,
      produto,
      { params }
    );
  }

  /** Lista todos os produtos */
  getAll(): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getAll}`
    );
  }

  /** Lista produtos do usuário (SEM estoque) */
  getMeusProdutos(usuarioId: number): Observable<Produto[]> {
    return this.http.get<Produto[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.listarPorUsuario(usuarioId)}`
    );
  }

  /** Buscar produto por ID */
  getById(id: number): Observable<Produto> {
    return this.http.get<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getById(id)}`
    );
  }

  /** Atualiza um produto normal */
  update(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.update(id)}`,
      produto
    );
  }

  /** Exclui um produto */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.delete(id)}`
    );
  }

  // ============================================================
  // OPERAÇÕES ESPECIAIS DOS PRODUTOS (NÃO RELACIONADAS A ESTOQUE)
  // ============================================================

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

  // ============================================================
  // PRODUTO ESTOQUE (ProdutoEstoque)
  // ============================================================

  /** Lista produtos dentro de um estoque — retorna ProdutoEstoque[] */
  listarPorEstoque(estoqueId: number): Observable<ProdutoEstoque[]> {
    return this.http.get<ProdutoEstoque[]>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.listarProdutos(estoqueId)}`
    );
  }

  /** Cria produto *dentro* do estoque */
   createInEstoque(produto: Produto, quantidade: number, estoqueId: number): Observable<ProdutoEstoque> {
      return this.http.post<ProdutoEstoque>(
        `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.listarProdutos(estoqueId)}`,
        { produto, quantidade }
      );
  }

  /** Atualiza um item do estoque */
  updateProdutoEstoque(
    produto: Produto,
    quantidade: number,
    estoqueId: number
  ): Observable<ProdutoEstoque> {
    return this.http.put<ProdutoEstoque>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.updateProduto(estoqueId, produto.id!)}`,
      { produto, quantidade }
    );
  }

  /** Remove um ProdutoEstoque */
  removerDoEstoque(produtoEstoqueId: number): Observable<void> {
    return this.http.delete<void>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.estoques.removerProdutoEstoque(produtoEstoqueId)}`
    );
  }

  // ============================================================
  // CÁLCULOS
  // ============================================================

  getValorTotal(): Observable<number> {
    return this.http.get<number>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.produtos.getValorTotal}`
    );
  }
}
