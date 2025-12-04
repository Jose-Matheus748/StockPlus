import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { Protocolo } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ProtocoloService {

  private baseUrl = API_CONFIG.baseURL + '/api/protocolos';

  constructor(private http: HttpClient) {}

  criar(data: { nome: string; preco: number; usuarioId: number }): Observable<Protocolo> {
    return this.http.post<Protocolo>(`${this.baseUrl}`, data);
  }

  buscarPorId(id: number): Observable<Protocolo> {
    return this.http.get<Protocolo>(`${this.baseUrl}/${id}`);
  }

  listarPorUsuario(usuarioId: number): Observable<Protocolo[]> {
    return this.http.get<Protocolo[]>(`${this.baseUrl}/usuario/${usuarioId}`);
  }

  atualizar(protocolo: Protocolo): Observable<Protocolo> {
    return this.http.put<Protocolo>(`${this.baseUrl}/${protocolo.id}`, protocolo);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
