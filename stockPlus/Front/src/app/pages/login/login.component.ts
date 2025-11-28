import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  email: string = '';
  senha: string = '';
  erro: string = '';
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  handleSubmit(): void {
    this.erro = '';
    this.isLoading = true;

    // Validações básicas
    if (!this.email || !this.senha) {
      this.erro = 'Por favor, preencha todos os campos';
      this.isLoading = false;
      return;
    }

    if (!this.email.includes('@')) {
      this.erro = 'Por favor, insira um email válido';
      this.isLoading = false;
      return;
    }

    const credentials: LoginRequest = {
      email: this.email,
      senha: this.senha,
    };

    this.authService.login(credentials).subscribe({
      next: () => {
        this.router.navigate(['/estoque']);
      },
      error: (error) => {
        this.erro = error.error?.message || 'Erro ao fazer login. Tente novamente.';
        this.isLoading = false;
      },
    });
  }
}
