/**
 * Componente de Layout Principal
 * Fornece a estrutura de navegação e sidebar para a aplicação
 */

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Usuario } from '../../models';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss'],
})
export class LayoutComponent implements OnInit {
  usuario: Usuario | null = null;
  sidebarOpen: boolean = true;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe((user) => {
      this.usuario = user;
    });
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  handleLogout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  getNomeInicial(): string {
    if (this.usuario?.nome && this.usuario.nome.length > 0) {
      return this.usuario.nome.charAt(0).toUpperCase();
    }
    return '';
  }
}
