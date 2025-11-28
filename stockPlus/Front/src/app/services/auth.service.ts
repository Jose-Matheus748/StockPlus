/**
 * Serviço de Autenticação
 * Responsável por gerenciar login, logout e autenticação do usuário
 */

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { API_CONFIG, API_ENDPOINTS } from '../config/api.config';
import { Usuario, LoginRequest, AuthResponse } from '../models';

const TOKEN_KEY = 'authToken';
const USER_KEY = 'currentUser';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<Usuario | null>(
    this.getCurrentUserFromStorage()
  );
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
   * Realiza o login do usuário
   */
  login(credentials: LoginRequest): Observable<Usuario> {
    return this.http
      .post<Usuario>(`${API_CONFIG.baseURL}${API_ENDPOINTS.auth.login}`, credentials)
      .pipe(
        tap((usuario) => {
          this.setCurrentUser(usuario);
          this.currentUserSubject.next(usuario);
        })
      );
  }

  /**
   * Registra um novo usuário
   */
  register(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(
      `${API_CONFIG.baseURL}${API_ENDPOINTS.usuarios.create}`,
      usuario
    );
  }

  /**
   * Faz logout do usuário
   */
  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUserSubject.next(null);
  }

  /**
   * Obtém o usuário atual do localStorage
   */
  private getCurrentUserFromStorage(): Usuario | null {
    const user = localStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  /**
   * Armazena o usuário atual no localStorage
   */
  setCurrentUser(usuario: Usuario): void {
    localStorage.setItem(USER_KEY, JSON.stringify(usuario));
    this.currentUserSubject.next(usuario);
  }

  /**
   * Verifica se o usuário está autenticado
   */
  isAuthenticated(): boolean {
    return this.getCurrentUserFromStorage() !== null;
  }

  /**
   * Obtém o usuário atual
   */
  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  /**
   * Obtém o token de autenticação
   */
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  /**
   * Armazena o token de autenticação
   */
  setToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }
}
