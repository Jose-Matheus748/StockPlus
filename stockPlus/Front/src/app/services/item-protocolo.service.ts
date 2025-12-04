import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { ItemProtocolo } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ItemProtocoloService {

  private baseUrl = API_CONFIG.baseURL + '/api/protocolos/itens';

  constructor(private http: HttpClient) {}

  criar(data: { protocoloId: number; produtoId: number; quantidade: number }): Observable<ItemProtocolo> {
    return this.http.post<ItemProtocolo>(this.baseUrl, data);
  }

  buscarPorId(id: number): Observable<ItemProtocolo> {
    return this.http.get<ItemProtocolo>(`${this.baseUrl}/${id}`);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
